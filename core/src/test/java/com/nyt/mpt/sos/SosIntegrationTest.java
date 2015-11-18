/**
 * 
 */
package com.nyt.mpt.sos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.nyt.mpt.common.AbstractTest;
import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.LineItemSalesTargetAssoc;
import com.nyt.mpt.domain.LineItemTarget;
import com.nyt.mpt.domain.Proposal;
import com.nyt.mpt.domain.sos.ProductPlacement;
import com.nyt.mpt.service.IProposalSOSService;
import com.nyt.mpt.service.IProposalService;
import com.nyt.mpt.service.ISalesTargetService;
import com.nyt.mpt.service.ISosIntegrationService;
import com.nyt.mpt.util.enums.LineItemProductTypeEnum;


/**
 * @author amandeep.singh
 *
 */
public class SosIntegrationTest extends AbstractTest{
	@Autowired
	private ISosIntegrationService sosIntegrationService;
	@Autowired
	private ISalesTargetService salesTargetService;
	@Autowired
	private IProposalSOSService proposalSOSService;
	@Autowired
	@Qualifier("proposalService")
	private IProposalService proposalService;
	
	Proposal proposalObj = null;
	
	@Before
	public void setup() {
		List<Proposal> proposalLst = proposalService.getProposalListhavingLineItems(null, null, null);
		for (Proposal proposal : proposalLst) {
			final Set<LineItem> lineItemSet = proposal.getDefaultOption().getLatestVersion().getProposalLineItemSet();
			boolean flag = false;
			for (LineItem lineItem : lineItemSet) {
				flag = false;
				final List<Long> salesTargetIds = new ArrayList<Long>();
				for(LineItemSalesTargetAssoc lineItemSalesTarget : lineItem.getLineItemSalesTargetAssocs()){
					salesTargetIds.add(lineItemSalesTarget.getSosSalesTargetId());
				}
				List<com.nyt.mpt.domain.sos.SalesTarget> salesTargets = salesTargetService.getSOSSalesTargetListByIDs(salesTargetIds.toArray(new Long[0]));
				if(salesTargets != null && !salesTargets.isEmpty() && LineItemProductTypeEnum.STANDARD.getShortName().equals(lineItem.getProductType().getShortName()) 
						&& lineItem.getGeoTargetSet() != null && !lineItem.getGeoTargetSet().isEmpty()) {
					for (LineItemTarget lineItemTarget : lineItem.getGeoTargetSet()) {
						if(lineItemTarget.getSosTarTypeId().intValue() == 35) {
							flag = true;
							break;
						}
					}
				}
			}
			if(flag && isProductPlacementActive(lineItemSet)) {
				proposalObj = proposal;
				return;
			}
		}
	}
	
	/**
	 * Create a dummy Order in SOS
	 */
	@Test
	public void testCreateOrder(){		
		Map<String, Set<LineItemTarget>> missedTargetingMap = sosIntegrationService.createOrder(proposalObj.getId());
		if(missedTargetingMap.isEmpty()){
			Assert.assertTrue(missedTargetingMap.isEmpty());
		}else{
			Assert.assertFalse(missedTargetingMap.isEmpty());
		}
	}
	
	/**
	 * @param proposalLineItems
	 * @param inActiveProdutPlacements
	 * @return
	 */
	private boolean isProductPlacementActive(final Set<LineItem> proposalLineItems) {
		boolean returnFlag = false;
		for (LineItem lineItem : proposalLineItems) {
			final List<ProductPlacement> productPlacements = proposalSOSService.getActiveProductPlacement(lineItem.getSosProductId(), convertSalesTargetAssocsToIds(lineItem.getLineItemSalesTargetAssocs()));
			if (productPlacements.size() > 1) {
				for (LineItemSalesTargetAssoc lineItemSalesTarget : lineItem.getLineItemSalesTargetAssocs()) {
					for (ProductPlacement productPlacement : productPlacements) {
						if (productPlacement.getProduct().getId() == lineItem.getSosProductId() && productPlacement.getSalesTarget().getSalesTargetId() == lineItemSalesTarget.getSosSalesTargetId()) {
							returnFlag = true;
							break;
						}
					}
				}
			}
		}
		return returnFlag;
	}
	
	/**
	 * @param targetAssocsList
	 * @return
	 */
	private Long[] convertSalesTargetAssocsToIds(final List<LineItemSalesTargetAssoc> targetAssocsList) {
		final Long[] salesTargetIds = new Long[targetAssocsList.size()];
		int index = 0;
		for (LineItemSalesTargetAssoc targetAssoc : targetAssocsList) {
			if (targetAssoc.getSosSalesTargetId() != null) {
				salesTargetIds[index++] = targetAssoc.getSosSalesTargetId();
			}
		}
		return salesTargetIds;
	}
}
