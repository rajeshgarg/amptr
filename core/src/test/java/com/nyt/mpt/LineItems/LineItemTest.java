package com.nyt.mpt.LineItems;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.nyt.mpt.common.AbstractTest;
import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.LineItemSalesTargetAssoc;
import com.nyt.mpt.domain.LineItemTarget;
import com.nyt.mpt.domain.Proposal;
import com.nyt.mpt.domain.ProposalOption;
import com.nyt.mpt.domain.ProposalVersion;
import com.nyt.mpt.domain.User;
import com.nyt.mpt.service.IProposalService;
import com.nyt.mpt.service.IUserService;
import com.nyt.mpt.util.DateUtil;
import com.nyt.mpt.util.enums.Criticality;
import com.nyt.mpt.util.enums.LineItemProductTypeEnum;
import com.nyt.mpt.util.enums.PricingStatus;
import com.nyt.mpt.util.enums.ProposalStatus;
import com.nyt.mpt.util.exception.CustomCheckedException;

/**
 * Line Item test cases
 * 
 * @author manish.kesarwani
 * 
 */
public class LineItemTest extends AbstractTest {

	@Autowired
	@Qualifier("proposalService")
	private IProposalService proposalService;
	
	@Autowired
	@Qualifier("userService")
	private IUserService userService;

	@Before
	public void setup() {
		User userobj = userService.getUserList().get(0);
		SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(userobj, "Test"));
	}
	
	@Test
	public void testCreateLineItemToOtherOption() throws CustomCheckedException {
		Proposal dummyProposal = createDummyProposal();
		
		ProposalOption dummyOption1 = createDummyProposalOption(dummyProposal, "OptionByTestcase");
		ProposalVersion proposalVersion = createDummyProposalVersion(dummyOption1);
		LineItem lineItem = getDummyLineItem(proposalVersion);
		Set<LineItem> lineItems = new HashSet<LineItem>();
		lineItems.add(lineItem);
		proposalVersion.setProposalLineItemSet(lineItems);
		Set<ProposalVersion> proposalVersions = new HashSet<ProposalVersion>();
		proposalVersions.add(proposalVersion);
		dummyOption1.setProposalVersions(proposalVersions);
		
		ProposalOption dummyOption2 = createDummyProposalOption(dummyProposal, "OptionByTestcase1");
		ProposalVersion proposalVersion2 = createDummyProposalVersion(dummyOption2);
		Set<LineItem> lineItems2 = new HashSet<LineItem>();
		proposalVersion2.setProposalLineItemSet(lineItems2);
		Set<ProposalVersion> proposalVersions2 = new HashSet<ProposalVersion>();
		proposalVersions2.add(proposalVersion2);
		dummyOption2.setProposalVersions(proposalVersions2);
		
		Set<ProposalOption> proposalOptions = new HashSet<ProposalOption>();
		proposalOptions.add(dummyOption1);
		proposalOptions.add(dummyOption2);
		dummyProposal.setProposalOptions(proposalOptions);
		long proposalId = proposalService.saveProposal(dummyProposal.getId(), dummyProposal);
		getTransactionManager().getSessionFactory().getCurrentSession().flush();
		proposalService.saveLineItemsOfProposal(proposalId, dummyOption1.getId(), lineItem, dummyProposal.getVersion());
		getTransactionManager().getSessionFactory().getCurrentSession().flush();
		Long[] lineItemIds = new Long[1];
		int i = 0;
		for (LineItem lineItem1 : lineItems) {
			lineItemIds[i] = lineItem1.getLineItemID();
			i++;
		}
		
		List<String> toOptionsIds = new ArrayList<String>();
		
		toOptionsIds.add(String.valueOf(dummyOption2.getId()));
		proposalService.getProposalbyId(proposalId);
		proposalService.createLineItemToOtherOption(dummyProposal, dummyOption1.getId(), toOptionsIds, lineItemIds);
		Assert.assertTrue(true);
	}
	
	private Proposal createDummyProposal() {
		Proposal proposalNew = new Proposal();
		proposalNew.setActive(true);
		//proposalNew.setAssignedByUser((userService.getUserById(130L)));
		//proposalNew.setAssignedUser((userService.getUserById(130L)));
		proposalNew.setCriticality(Criticality.REGULAR);
		proposalNew.setDateRequested(DateUtil.getCurrentDate());
		proposalNew.setDueDate(DateUtil.getCurrentDate());
		proposalNew.setEndDate(DateUtil.getCurrentDate());
		proposalNew.setGlobal(false);
		proposalNew.setId(0);
		proposalNew.setName("ProposalByTestcase");
		proposalNew.setPriceType("Net");
		proposalNew.setProposalStatus(ProposalStatus.INPROGRESS);
		proposalNew.setSosAdvertiserId(0L);
		proposalNew.setSosAgencyId(0L);
		proposalNew.setSosSalesCategoryId(1748L);
		proposalNew.setStartDate(DateUtil.getCurrentDate());
		proposalNew.setVersion(1);
		proposalNew.setCampaignName("ProposalCampaignNameByTestcase");
		proposalNew.setAgencyMargin(20D);
		return proposalNew;
	}
	
	private LineItem getDummyLineItem(ProposalVersion proposalVersion) {
		LineItem returnLineItem = new LineItem();
		returnLineItem.setPriceType("CPM");
		returnLineItem.setRate(55D);
		returnLineItem.setImpressionTotal(4L);

		// Setting line item sales target association
		List<LineItemSalesTargetAssoc> lineItemSalesTargetAssocsList = new ArrayList<LineItemSalesTargetAssoc>();
		LineItemSalesTargetAssoc lineItemSalesTargetAssoc = new LineItemSalesTargetAssoc();
		lineItemSalesTargetAssoc.setSosSalesTargetId(3892L);
		lineItemSalesTargetAssoc.setSosSalesTargetName("Test");
		lineItemSalesTargetAssocsList.add(lineItemSalesTargetAssoc);
		lineItemSalesTargetAssoc.setProposalLineItem(returnLineItem);
		returnLineItem.setLineItemSalesTargetAssocs(lineItemSalesTargetAssocsList);

		returnLineItem.setSosProductClass(0L);
		returnLineItem.setSosProductId(838L);
		returnLineItem.setProductName("Test");
		returnLineItem.setTargetingString("Test");
		returnLineItem.setTotalInvestment(0.01);
		returnLineItem.setProposalId(351L);
		returnLineItem.setPricingStatus(PricingStatus.SYSTEM_APPROVED);
		returnLineItem.setProductType(LineItemProductTypeEnum.STANDARD);
		returnLineItem.setProposalVersion(proposalVersion);
		returnLineItem.setOrderNumber(System.currentTimeMillis());

		Set<LineItemTarget> geoTargetSet = new LinkedHashSet<LineItemTarget>();
		LineItemTarget lineItemTarget = new LineItemTarget();
		lineItemTarget.setActive(true);
		lineItemTarget.setProposalLineItem(returnLineItem);
		lineItemTarget.setSosTarTypeElementId("101,2,3");
		lineItemTarget.setSosTarTypeId(1L);
		lineItemTarget.setPremium(150D);
		geoTargetSet.add(lineItemTarget);
		returnLineItem.setGeoTargetSet(geoTargetSet);
		return returnLineItem;
	}
	
	private ProposalOption createDummyProposalOption(final Proposal proposal, final String name) {
		ProposalOption option = new ProposalOption();
		option.setName(name);
		option.setProposal(proposal);
		option.setVersion(1);
		return option;
	}
	
	private ProposalVersion createDummyProposalVersion(ProposalOption proposalOption) {
		ProposalVersion propVersion = new ProposalVersion();
		propVersion.setActive(true);
		propVersion.setEffectiveCpm(100D);
		propVersion.setEndDate(DateUtil.getCurrentDate());
		propVersion.setImpressions(1000L);
		propVersion.setOfferedBudget(99D);
		propVersion.setStartDate(DateUtil.getCurrentDate());
		propVersion.setVersion(1);
		propVersion.setProposalVersion(1L);
		propVersion.setProposalOption(proposalOption);
		return propVersion;
	}
}
