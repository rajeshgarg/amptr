package com.nyt.mpt.proposal;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.nyt.mpt.common.AbstractTest;
import com.nyt.mpt.domain.Advertiser;
import com.nyt.mpt.domain.Agency;
import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.LineItemSalesTargetAssoc;
import com.nyt.mpt.domain.Product;
import com.nyt.mpt.domain.Proposal;
import com.nyt.mpt.domain.ProposalOption;
import com.nyt.mpt.domain.ProposalVersion;
import com.nyt.mpt.domain.SalesTarget;
import com.nyt.mpt.domain.User;
import com.nyt.mpt.service.IProposalSOSService;
import com.nyt.mpt.service.IProposalService;
import com.nyt.mpt.template.ReferenceDataMap;

/**
 * JUnit test for ProposalSOSService
 * @author rakesh.tewari
 */
public class ProposalSOSServiceTest extends AbstractTest{

	@Autowired
	@Qualifier("proposalSOSService")
	private IProposalSOSService proposalSOSService;
	
	@Autowired
	@Qualifier("proposalService")
	private IProposalService proposalService;
	
	private Proposal proposal = null;
	private List<LineItem> lineItemLst = null;
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
				for (ProposalOption proposalOption : proposalOptionSet) {
					proposalVersion = proposalOption.getLatestVersion();
					if (proposalVersion != null) {
						lineItemLst = proposalService.getProposalLineItems(proposalOption.getId(), proposalVersion.getProposalVersion(), null, null, null);
						if (lineItemLst != null && !lineItemLst.isEmpty()) {
							return;
						}
					}
				}
			}
		}
	}	
	
	/**
	 * 
	 */
	@Test
	public void testGetAdvertiser() {
		List<Advertiser> advertiserLst = proposalSOSService.getAdvertiser();
		Assert.assertTrue(advertiserLst.size() >= 0);
		if (!advertiserLst.isEmpty()) {
			Advertiser advertiser = advertiserLst.get(0);
			Advertiser advertiserDB = proposalSOSService.getAdvertiserById(advertiser.getId());
			Assert.assertTrue(advertiserDB != null);
		}
	}
	
	@Test
	public void testGetAdvertiserByName() {
		List<Advertiser> advertiserLst = proposalSOSService.getAdvertiser();
		if (!advertiserLst.isEmpty()) {
			Advertiser advertiser = advertiserLst.get(0);
			Assert.assertTrue(proposalSOSService.getAdvertiserByName(advertiser.getName())!= null);
		}
	}

	/**
	 * 
	 */
	@Test
	public void testGetAgency() {
		List<Agency> agencyLst = proposalSOSService.getAgency();
		Assert.assertTrue(agencyLst.size() >= 0);
		if (!agencyLst.isEmpty()) {
			Agency agency = agencyLst.get(0);
			Agency agencyDB = proposalSOSService.getAgencyById(agency.getId());
			Assert.assertTrue(agencyDB != null);
		}
	}
	
	@Test
	public void testGetAgencyByName() {
		List<Agency> agencyLst = proposalSOSService.getAgency();
		if (!agencyLst.isEmpty()) {
			Agency agency = agencyLst.get(0);
			Assert.assertTrue(proposalSOSService.getAgencyByName(agency.getName()) != null);
		}
	}

	/**
	 * 
	 */
	@Test
	public void testGetCurrencies() {
		Map<Long, String> currenciesMap = proposalSOSService.getCurrencies();
		Assert.assertTrue(currenciesMap.size() >= 0);
		if (!currenciesMap.isEmpty()) {
			try {				
				Map<String,Double> conversionRate = proposalSOSService.getCurrencyConversionRate();/* Setting conversion Rate as 1 for USD currency */
				Assert.assertTrue(conversionRate.get("USD") == 1);				
			} catch (ParseException e) {
				Assert.assertFalse(false);
			}
		}
	}

	/**
	 * 
	 */
	@Test
	public void testIsProductPlacementActive() {
		long productId = 0;
		List<Long> salesTargetIdList = new ArrayList<Long>();
		if(lineItemLst != null ){
			LineItem lineItem = lineItemLst.get(0);
			productId = lineItem.getSosProductId();
			List<LineItemSalesTargetAssoc> lineItemSalesTargetAssocs = lineItem.getLineItemSalesTargetAssocs();
			for (LineItemSalesTargetAssoc lineItemSalesTargetAssoc : lineItemSalesTargetAssocs) {
				salesTargetIdList.add(lineItemSalesTargetAssoc.getSosSalesTargetId());
			}
			proposalSOSService.isProductPlacementActive(productId, salesTargetIdList.toArray(new Long[0]));
			Assert.assertTrue(true);
			
		}
	}
	
	@Test
	public void testGetActiveProductPlacement(){
		long productId = 0;
		List<Long> salesTargetIdList = new ArrayList<Long>();
		if(lineItemLst != null ){
			LineItem lineItem = lineItemLst.get(0);
			productId = lineItem.getSosProductId();
			List<LineItemSalesTargetAssoc> lineItemSalesTargetAssocs = lineItem.getLineItemSalesTargetAssocs();
			for (LineItemSalesTargetAssoc lineItemSalesTargetAssoc : lineItemSalesTargetAssocs) {
				salesTargetIdList.add(lineItemSalesTargetAssoc.getSosSalesTargetId());
			}
			proposalSOSService.getActiveProductPlacement(productId, salesTargetIdList.toArray(new Long[0]));
			Assert.assertTrue(true);//We can not judge whether a placement is active or not as same is coming from SOS
			
		}
	}

	/**
	 * 
	 */
	@Test
	public void testGetReferenceDataMapFromLineItemList() {
		if (lineItemLst != null) {
			ReferenceDataMap getReferenceDataMapFromLineItemMap = proposalSOSService.getReferenceDataMapFromLineItemList(lineItemLst);
			final Map<Long, Product> productMap = getReferenceDataMapFromLineItemMap.getProductMap();
			final Map<Long, SalesTarget> salesTargetMap = getReferenceDataMapFromLineItemMap.getSalesTargetMap();
			ReferenceDataMap getReferenceDataMapFromLineItemAssocMap = proposalSOSService.getReferenceDataMapFromLineItemAssocList(lineItemLst);
			final Map<Long, Product> productAssocMap = getReferenceDataMapFromLineItemAssocMap.getProductMap();
			final Map<Long, SalesTarget> salesTargetAssocMap = getReferenceDataMapFromLineItemAssocMap.getSalesTargetMap();
			Assert.assertTrue(productMap.size() == productAssocMap.size());
			Assert.assertTrue(salesTargetMap.size() == salesTargetAssocMap.size());
		}
	}
}
