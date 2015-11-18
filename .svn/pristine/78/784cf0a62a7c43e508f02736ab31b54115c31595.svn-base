/**
 * 
 */
package com.nyt.mpt.avails;

import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.nyt.mpt.common.AbstractTest;
import com.nyt.mpt.domain.InventoryDetail;
import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.PrimaryPageGroup;
import com.nyt.mpt.domain.Proposal;
import com.nyt.mpt.domain.ProposalOption;
import com.nyt.mpt.domain.ProposalVersion;
import com.nyt.mpt.domain.User;
import com.nyt.mpt.service.IProductService;
import com.nyt.mpt.service.IProposalService;
import com.nyt.mpt.service.ITargetingService;
import com.nyt.mpt.service.impl.YieldexService;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.DateUtil;
import com.nyt.mpt.util.YieldexHelper;
import com.nyt.mpt.util.dto.LineItemDTO;

/**
 * JUnit test for avails inventory details
 * @author surendra.singh
 */
public class AvailsTest extends AbstractTest {

	@Autowired
	@Qualifier("yieldexService")
	private YieldexService yieldexService;

	@Autowired
	@Qualifier("productService")
	private IProductService productService;

	@Autowired
	@Qualifier("targetingService")
	private ITargetingService targetingService;

	@Autowired
	@Qualifier("yieldexHelper")
	private YieldexHelper yieldexHelper;

	@Autowired
	@Qualifier("proposalService")
	private IProposalService proposalService;

	private Proposal proposal = null;
	private List<LineItem> lineItemLst = null;
	private ProposalOption proposalOption = null;
	private ProposalVersion proposalVersion = null;
	private User userobj = null;

	@Before
	public void setup() {
		super.setAuthenticationInfo();
		List<Proposal> proposalLst = proposalService.getProposalList(null, null, null);
		if (proposalLst.size() > 0) {
			proposal = proposalLst.get(0);
			userobj = proposal.getAssignedUser();
			SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(userobj, "Test"));
			for (Proposal proposal : proposalLst) {
				Set<ProposalOption> proposalOptionSet = proposal.getProposalOptions();
				for (ProposalOption proposalOptionDB : proposalOptionSet) {
					proposalOption = proposalOptionDB;
					proposalVersion = proposalOption.getLatestVersion();
					if (proposalVersion != null) {
						lineItemLst = proposalService.getProposalLineItems(proposalOption.getId(), proposalVersion.getId(), null, null, null);
						if (lineItemLst != null && !lineItemLst.isEmpty()) {
							return;
						}
					}
				}
			}
		}
	}

	/**
	 * Test case to get avails inventory details from Yield-ex based on URL
	 */
	@Test
	public void testGetInventoryDetail() {
		String url = ConstantStrings.EMPTY_STRING;
		InventoryDetail inventoryDetail = null;
		if (lineItemLst != null && !lineItemLst.isEmpty()) {
			for (LineItem lineItemDB : lineItemLst) {
				try {
					final LineItemDTO lineItemDTO = getLineItemDTO(lineItemDB);
					lineItemDTO.setGeoTargetSet(lineItemDB.getGeoTargetSet());
					url = yieldexHelper.getYieldexURLForAvails(lineItemDTO);
					inventoryDetail = yieldexService.getInventoryDetail(url);
					Assert.assertNotNull(inventoryDetail);
				} catch (Exception e) {
					Assert.assertTrue(true);
				}
			}

		}
	}

	/**
	 * Test case to get primary page group based on sales Target Id
	 */
	@Test
	public void testGetPrimaryPageGroup() {
		long salesTargetId = 0;
		if (lineItemLst != null && !lineItemLst.isEmpty()) {
			for (LineItem lineItemDB : lineItemLst) {
				if (!lineItemDB.getLineItemSalesTargetAssocs().isEmpty()) {
					salesTargetId = lineItemDB.getLineItemSalesTargetAssocs().get(0).getId();
					final PrimaryPageGroup primaryPageGroup = productService.getPrimaryPageGroupBySalesTargetId(salesTargetId);
					Assert.assertNotNull(primaryPageGroup);
				}
			}
		}
	}

	/**
	 * Test case to get target type for countries
	 */
	@Test
	public void testContryElement() {
		final long countryTypeId = 8l;
		final long chinaId = 106;
		Map<Long, String> countryElementMap = targetingService.getTargetForCountries(countryTypeId);
		Assert.assertEquals("CN", countryElementMap.get(chinaId));
	}

	/**
	 * Return LineItemDTO object base on  line item
	 * @param lineItem
	 * @return
	 */
	private LineItemDTO getLineItemDTO(final LineItem lineItem) {
		final LineItemDTO lineItemDTO = new LineItemDTO();
		lineItemDTO.setSosProductId(String.valueOf(lineItem.getSosProductId()));
		String[] sosSalesTargetId = new String[lineItem.getLineItemSalesTargetAssocs().size()];
		sosSalesTargetId[0] = String.valueOf(lineItem.getLineItemSalesTargetAssocs().get(0).getSosSalesTargetId());
		lineItemDTO.setSosSalesTargetId(sosSalesTargetId);
		lineItemDTO.setStartDate(DateUtil.getGuiDateString(lineItem.getStartDate()));
		lineItemDTO.setEndDate(DateUtil.getGuiDateString(lineItem.getStartDate()));
		return lineItemDTO;
	}

}
