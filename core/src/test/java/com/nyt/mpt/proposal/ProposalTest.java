/**
 * 
 */
package com.nyt.mpt.proposal;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.nyt.mpt.common.AbstractTest;
import com.nyt.mpt.domain.Advertiser;
import com.nyt.mpt.domain.CampaignObjective;
import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.LineItemExceptions;
import com.nyt.mpt.domain.LineItemReservations;
import com.nyt.mpt.domain.LineItemSalesTargetAssoc;
import com.nyt.mpt.domain.LineItemTarget;
import com.nyt.mpt.domain.Notes;
import com.nyt.mpt.domain.Proposal;
import com.nyt.mpt.domain.ProposalOption;
import com.nyt.mpt.domain.ProposalVersion;
import com.nyt.mpt.domain.User;
import com.nyt.mpt.service.ICalendarReservationService;
import com.nyt.mpt.service.IProposalSOSService;
import com.nyt.mpt.service.IProposalService;
import com.nyt.mpt.service.ISOSService;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.DateUtil;
import com.nyt.mpt.util.NumberUtil;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.enums.Criticality;
import com.nyt.mpt.util.enums.LineItemProductTypeEnum;
import com.nyt.mpt.util.enums.PriceType;
import com.nyt.mpt.util.enums.PricingStatus;
import com.nyt.mpt.util.enums.ProposalStatus;
import com.nyt.mpt.util.enums.ReservationStatus;
import com.nyt.mpt.util.enums.SearchOption;
import com.nyt.mpt.util.exception.CustomCheckedException;
import com.nyt.mpt.util.exception.ProposalAccessException;
import com.nyt.mpt.util.filter.RangeFilterCriteria;
import com.nyt.mpt.util.security.SecurityUtil;
import com.sforce.ws.ConnectionException;

/**
 * JUnit test for Proposal service
 * @author amandeep.singh
 */
public class ProposalTest extends AbstractTest {

	@Autowired
	@Qualifier("proposalService")
	private IProposalService proposalService;

	@Autowired
	@Qualifier("proposalSOSService")
	private IProposalSOSService proposalSOSService;
	
	@Autowired
	@Qualifier("reservationService")
	private ICalendarReservationService reservationService;
	
	@Autowired
	@Qualifier("sosService")
	private ISOSService sosService;

	private Proposal proposal = null;
	private List<LineItem> lineItemLst = new ArrayList<LineItem>();
	private ProposalOption proposalOption = null;
	private ProposalVersion proposalVersion = null;
	private User userobj = null;
	private LineItem reservlineItemDB = null;

	@Before
	public void setup() {
		if(reservlineItemDB == null){
			setReservationData();
			if(lineItemLst == null && lineItemLst.isEmpty()){
				setTestData();
			}
		}
		SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(userobj, "Test"));
	}
	
	private void setReservationData(){
		PaginationCriteria pgCriteria = new PaginationCriteria(1, 10);
		final List<RangeFilterCriteria> filterCriteriaLst = new ArrayList<RangeFilterCriteria>();
		RangeFilterCriteria filterCriteria = new RangeFilterCriteria();
		filterCriteria.setSearchField("date");
		filterCriteria.setSearchOper(SearchOption.BETWEEN.toString());
		filterCriteria.setSearchStringFrom(DateUtil.getGuiDateString(DateUtil.getPriorDateFromCurrentDate(90)));
		filterCriteria.setSearchStringTo(DateUtil.getCurrentDateTimeString("MM/dd/yyyy"));
		filterCriteriaLst.add(filterCriteria);
		RangeFilterCriteria filterCriteria1 = new RangeFilterCriteria();
		filterCriteria1.setSearchField(ConstantStrings.STATUS);
		filterCriteria1.setSearchOper(SearchOption.EQUAL.toString());
		filterCriteria1.setSearchString(ReservationStatus.HOLD.name()+","+ReservationStatus.RE_NEW.name());
		filterCriteriaLst.add(filterCriteria1);
		lineItemLst = reservationService.getProposalsForReservationSearch(filterCriteriaLst, pgCriteria, null);
		if(lineItemLst != null && !lineItemLst.isEmpty()){
			for (LineItem lineItem : lineItemLst) {
				if(lineItem.getProductType().getShortName().equals(LineItemProductTypeEnum.RESERVABLE.getShortName())){
					reservlineItemDB = lineItem;
					proposal = proposalService.getProposalbyId(reservlineItemDB.getProposalId());
					userobj = proposal.getAssignedUser();
					proposalOption = proposal.getDefaultOption();
					proposalVersion = proposalOption.getLatestVersion();
					break;
				}
				
			}
		}
	}
	
	
	private void setTestData(){
		//Setting up test data
		PaginationCriteria pgCriteria = new PaginationCriteria(1, 100);
		final List<Proposal> proposalLst = proposalService.getProposalListhavingLineItems(null, pgCriteria, null);		
		if (proposalLst.size() > 0) {
			for (Proposal proposalNew : proposalLst) {
				if(ProposalStatus.INPROGRESS.name().equals(proposalNew.getProposalStatus().name()) && StringUtils.isNotBlank(proposalNew.getCampaignName())){
					proposal = proposalNew;
					userobj = proposal.getAssignedUser();
					SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(userobj, "Test"));
					final Set<ProposalOption> proposalOptionSet = proposal.getProposalOptions();
					if(proposalOptionSet!=null && proposalOptionSet.size() > 0) {
					proposalOption = proposalOptionSet.iterator().next();
					proposalVersion = proposalOption.getLatestVersion();
						if (proposalVersion != null) {
							//Converting line item set to list for easy navigation
							Set<LineItem> lineSet = proposalVersion.getProposalLineItemSet();
							for (LineItem lineItem : lineSet) {
								lineItemLst.add(lineItem);
							}
							//Setting reservalbe line item if exist
							if (lineItemLst != null && !lineItemLst.isEmpty()) {
								for (LineItem lineItemDB : lineItemLst) {
									if (lineItemDB.getProductType() != null && ("R").equalsIgnoreCase(lineItemDB.getProductType().getShortName()) && lineItemDB.getReservation() != null) {
										final String reservationStatus = lineItemDB.getReservation().getStatus().getDisplayName();
										if (ReservationStatus.HOLD.getDisplayName().equalsIgnoreCase(reservationStatus) || ReservationStatus.RE_NEW.getDisplayName().equalsIgnoreCase(reservationStatus)) {
											reservlineItemDB = lineItemDB;
											return;
										}
									}
								}
							}
						}
					}
					if(reservlineItemDB != null){
						break;
					}
				}
			} 
		}
	}
	
	@Test
	public void testCreateLineItemsBySpliting() throws CustomCheckedException{
		if(lineItemLst!=null && lineItemLst.size() > 0) {
			//Fetching a line item and updating it with desired data
			LineItem lineItem = lineItemLst.get(0);
			
			
			//Setting start date, month and year to the current year of line item  
			Calendar startDate = Calendar.getInstance();
			startDate.set(Calendar.DAY_OF_MONTH, 1);
			startDate.set(Calendar.MONTH, 1);
			lineItem.setStartDate(startDate.getTime());
			
			//Setting end date, month and year to the current year of line item
			Calendar endDate = Calendar.getInstance();
			endDate.set(Calendar.DAY_OF_MONTH, 31);
			endDate.add(Calendar.MONTH, 3);
			lineItem.setEndDate(endDate.getTime());
			
			//Setting investment, impression and CPM
			lineItem.setImpressionTotal(1000L);
			lineItem.setRate(50D);
			lineItem.setTotalInvestment(50.0);
			
			//Updating line item with set data
			long lineItemId = proposalService.updateLineItemsOfProposal(proposal.getId(), lineItem);
			Assert.assertTrue(lineItemId >= 0);
			
			//Verifying split process
			boolean lineItemSplitted = proposalService.createLineItemsBySpliting(proposal.getId(), proposalOption.getId(), proposalVersion.getProposalVersion(),String.valueOf(lineItemId));
			Assert.assertFalse(lineItemSplitted);
			
			if(lineItemLst.size() > 1){
				LineItem lineItem2 = lineItemLst.get(1);  //This line item is used to test zero impression and first one delete after splitting
				//Testing scenarios when impression counts goes to zero
				//Setting start date, month and year to the current year of line item  
				startDate.set(Calendar.DAY_OF_MONTH, 1);
				startDate.set(Calendar.MONTH, 1);
				lineItem2.setStartDate(startDate.getTime());
				
				//Setting end date, month and year to the current year of line item
				endDate.set(Calendar.DAY_OF_MONTH, 31);
				endDate.add(Calendar.MONTH, 3);
				lineItem2.setEndDate(endDate.getTime());
				
				//Setting investment, impression and CPM
				lineItem2.setImpressionTotal(2L);
				lineItem2.setRate(50D);
				lineItem2.setTotalInvestment(50.0);
				
				//Updating line item with set data
				lineItemId = proposalService.updateLineItemsOfProposal(proposal.getId(), lineItem2);
				Assert.assertTrue(lineItemId >= 0);
				
				//Verifying split process
				lineItemSplitted = proposalService.createLineItemsBySpliting(proposal.getId(), proposalOption.getId(), proposalVersion.getProposalVersion(),String.valueOf(lineItemId));
				Assert.assertTrue(lineItemSplitted);
			}
			
		}
		
	}

	/**
	 * Return Proposal based on proposalId.
	 */
	@Test
	public void testgetProposalById() {
		if (proposal != null) {
			final Proposal propDB = proposalService.getProposalbyId(proposal.getId());
			Assert.assertTrue(proposal.getName().equals(propDB.getName()));
		}
	}

	/**
	 * Method to return ProposalVersion on Base of proposalversionId
	 */
	@Test
	public void testGetproposalVersion() {
		if (proposalVersion != null) {
			final ProposalVersion pVersion = proposalService.getProposalVersion(proposalVersion.getId());
			Assert.assertEquals(proposalVersion.getId(), pVersion.getId());
		}
	}

	/**
	 * Saving Line Items Of Option with OptionId, proposalLineItem and
	 * proposalversion
	 * @throws CustomCheckedException 
	 */
	@Test
	public void testSaveLineItemsOfProposal() throws CustomCheckedException {
		if (proposalVersion != null) {
			final Long returnId = proposalService.saveLineItemsOfProposal(proposalOption.getProposal().getId(), proposalOption.getId(), getDummyLineItem(proposalVersion,proposal.getId()), proposalVersion.getProposalVersion());
			Assert.assertTrue(returnId > 0);
		}
	}
	
	@Test
	(expected = ProposalAccessException.class)
	public void testSaveLineItemsOfProposalWithNonProposalOwner() throws CustomCheckedException {
		if (proposalVersion != null) {
			super.setAuthenticationInfo();
			final Long returnId = proposalService.saveLineItemsOfProposal(proposalOption.getProposal().getId(), proposalOption.getId(), getDummyLineItem(proposalVersion,proposal.getId()), proposalVersion.getProposalVersion());
			Assert.assertTrue(returnId > 0);
		}
	}


	/**
	 * Creating version for a Option
	 * @throws CustomCheckedException 
	 */
	@Test
	public void testCreateVersion() throws CustomCheckedException {
		if (proposalVersion != null) {
			try {
				getTransactionManager().getSessionFactory().getCurrentSession().clear();
				final Long returnId = proposalService.createVersion(proposalVersion.getProposalOption().getProposal().getId(), proposalVersion.getProposalOption().getId(), proposalVersion
						.getProposalVersion());
				Assert.assertTrue(returnId > 0);
			} catch (ProposalAccessException e) {
				Assert.assertTrue(true);
			}
		}
	}

	/**
	 * Create a new option from Existing One
	 * @throws CustomCheckedException 
	 */
	@Test
	public void testCreateNewoption() throws CustomCheckedException {
		if (proposalVersion != null) {
			if (proposal.getProposalOptions().size() < 5) {
				getTransactionManager().getSessionFactory().getCurrentSession().clear();
				Long returnId = proposalService.createOptionClone(proposal.getId(), proposalOption.getId(), proposalVersion.getId(), proposal.getProposalOptions().size() + 1);
				Assert.assertTrue(returnId > 0);
			}
		}
	}


	/**
	 * Deleting proposal Line Items.
	 * @throws CustomCheckedException 
	 */
	@Test
	public void testDeleteProposalLineItem() throws CustomCheckedException {
		if (lineItemLst != null && !lineItemLst.isEmpty()) {
			LineItem lineItem = lineItemLst.get(0);
			List<Long> lineItemLst = proposalService.deleteProposalLineItem(proposal.getId(), String.valueOf(lineItem.getLineItemID()));
			Assert.assertFalse(lineItemLst.contains(lineItem));
		}
	}

	/**
	 * Copying LineItems to proposal based on proposalID, proposalVersion, type,
	 * lineItems and partiallyCopiedUnbreakPackage.
	 */
	@Test
	public void testSaveCopiedLineItemsToProposal() {
		if (lineItemLst != null && !lineItemLst.isEmpty()) {
			Long lineItem = lineItemLst.get(0).getLineItemID();
			Long[] lineItemsArray = {lineItem };
			proposalService.saveCopiedLineItemsToProposal(proposal.getId(), proposalOption.getId(), 1L, "Proposal", lineItemsArray, false, false, 1, "Net", false, 15.0);
			Assert.assertTrue(true);
		}
	}

	/**
	 * Updating Line Items When Associated with Option version based on LineItem
	 * id.
	 * @throws CustomCheckedException 
	 */
	@Test
	public void testUpdateLineItemsOfProposal() throws CustomCheckedException {
		if (lineItemLst != null && !lineItemLst.isEmpty()) {
			LineItem bean = lineItemLst.get(0);

			Set<LineItemTarget> geoTargetSet = bean.getGeoTargetSet();
			LineItemTarget lineItemTarget = new LineItemTarget();
			lineItemTarget.setActive(true);
			lineItemTarget.setProposalLineItem(bean);
			lineItemTarget.setSosTarTypeElementId("111,222,333");
			lineItemTarget.setSosTarTypeId(1L);
			lineItemTarget.setPremium(150D);
			geoTargetSet.add(lineItemTarget);
			bean.setGeoTargetSet(geoTargetSet);

			bean.setImpressionTotal(143L);

			List<LineItemSalesTargetAssoc> lineItemSalesTargetAssocsList = bean.getLineItemSalesTargetAssocs();
			LineItemSalesTargetAssoc lineItemSalesTargetAssoc = new LineItemSalesTargetAssoc();
			lineItemSalesTargetAssoc.setSosSalesTargetId(4326L);
			lineItemSalesTargetAssoc.setSosSalesTargetName("test");
			lineItemSalesTargetAssocsList.add(lineItemSalesTargetAssoc);
			lineItemSalesTargetAssoc.setProposalLineItem(bean);
			bean.setLineItemSalesTargetAssocs(lineItemSalesTargetAssocsList);

			Long lineItemId = proposalService.updateLineItemsOfProposal(proposal.getId(), bean);
			Assert.assertTrue(lineItemId >= 0);
			
		}
	}

	/**
	 * Save a new Proposal if Proposal is not already exist else update Proposal
	 * @throws CustomCheckedException 
	 */
	@Test
	public void testCreateProposal() throws CustomCheckedException {
		long propsalId = 0;
		if(userobj == null){
			userobj = SecurityUtil.getUser();
		}
		if (userobj != null && proposal != null) {
			Proposal dummyProposal = createDummyProposal();
			propsalId = proposalService.saveProposal(dummyProposal.getId(), dummyProposal);
			getTransactionManager().getSessionFactory().getCurrentSession().evict(proposal);
			Assert.assertTrue(propsalId >= 0);
			proposal.setName("test");
			Map<Long,String> salesCategoryMap = sosService.getSalesCategories();
			proposal.setSosSalesCategoryId(salesCategoryMap.keySet().iterator().next());
			List<Advertiser> advertiserLst = proposalSOSService.getAdvertiser();
			proposal.setSosAdvertiserId(advertiserLst.get(0).getId());
			if(PriceType.Net.name().equals(proposal.getPriceType())){
				proposal.setPriceType(PriceType.Gross.name());
			}else{
				proposal.setPriceType(PriceType.Net.name());
			}
			propsalId = proposalService.saveProposal(proposal.getId(), proposal);
			getTransactionManager().getSessionFactory().getCurrentSession().evict(proposal);
			if(PriceType.Net.name().equals(proposal.getPriceType())){
				proposal.setPriceType(PriceType.Gross.name());
			}else{
				proposal.setPriceType(PriceType.Net.name());
			}
			propsalId = proposalService.saveProposal(proposal.getId(), proposal);
			Assert.assertTrue(propsalId == proposal.getId());
		}
	}

	/**
	 * Return list of all active proposal.
	 */
	@Test
	public void testGetProposalListCount() {
		List<Proposal> proposalList = proposalService.getProposalList(null, null, null);
		if (proposalList != null && proposalList.size() > 0) {
			int count = proposalService.getProposalListCount(null);
			Assert.assertEquals(proposalList.size(), count);
		}

		if (proposalList != null && proposalList.size() > 0) {
			List<RangeFilterCriteria> filterCriteriaLst = new ArrayList<RangeFilterCriteria>();
			RangeFilterCriteria rangeFilterCriteria = new RangeFilterCriteria();
			rangeFilterCriteria.setSearchField("proposalName");
			rangeFilterCriteria.setSearchString(proposalList.get(0).getName());
			filterCriteriaLst.add(0, rangeFilterCriteria);
			proposalList = proposalService.getProposalList(filterCriteriaLst, new PaginationCriteria(1, 10), null);
			if (proposalList != null && proposalList.size() > 0) {
				int count = proposalService.getProposalListCount(null);
				Assert.assertTrue(proposalList.size() <= count);
				count = proposalService.getProposalListCount(filterCriteriaLst);
			}

			rangeFilterCriteria.setSearchField("id");
			rangeFilterCriteria.setSearchString(String.valueOf(proposalList.get(0).getId()));
			filterCriteriaLst.add(0, rangeFilterCriteria);
			proposalList = proposalService.getProposalList(filterCriteriaLst, new PaginationCriteria(1, 10), null);

			rangeFilterCriteria.setSearchField("agencyName");
			rangeFilterCriteria.setSearchString(String.valueOf(proposalList.get(0).getSosAgencyId()));
			filterCriteriaLst.add(0, rangeFilterCriteria);
			proposalList = proposalService.getProposalList(filterCriteriaLst, new PaginationCriteria(1, 10), null);

			rangeFilterCriteria.setSearchField("salescategory");
			rangeFilterCriteria.setSearchString(String.valueOf(proposalList.get(0).getSosSalesCategoryId()));
			filterCriteriaLst.add(0, rangeFilterCriteria);
			proposalList = proposalService.getProposalList(filterCriteriaLst, new PaginationCriteria(1, 10), null);

			rangeFilterCriteria.setSearchField("advertiserName");
			rangeFilterCriteria.setSearchString(String.valueOf(proposalList.get(0).getSosAdvertiserId()));
			filterCriteriaLst.add(0, rangeFilterCriteria);
			proposalList = proposalService.getProposalList(filterCriteriaLst, new PaginationCriteria(1, 10), null);

			rangeFilterCriteria.setSearchField("advertiserName");
			rangeFilterCriteria.setSearchString(String.valueOf(proposalList.get(0).getSosAdvertiserId()));
			filterCriteriaLst.add(0, rangeFilterCriteria);
			proposalList = proposalService.getProposalList(filterCriteriaLst, new PaginationCriteria(1, 10), null);

			rangeFilterCriteria.setSearchField("proposalStatus");
			filterCriteriaLst.add(0, rangeFilterCriteria);
			proposalList = proposalService.getProposalList(filterCriteriaLst, new PaginationCriteria(1, 10), null);

		}

	}

	/**
	 * Returns all active options of a proposal
	 */
	@Test
	public void testGetProposalOptionsById() {
		if (proposal != null) {
			List<ProposalOption> proposalOptionLst = proposalService.getProposalOptionsById(proposal.getId());
			if (proposalOptionLst != null && !proposalOptionLst.isEmpty()) {
				Assert.assertTrue(proposal.getProposalOptions().size() == proposalOptionLst.size());
			}
		}
	}

	/**
	 * Update Assignment of a proposal
	 * @throws CustomCheckedException 
	 */
	@Test
	public void testUpdateAssignToUser() throws CustomCheckedException {
		if (userobj != null && proposal != null) {
			long id = proposalService.updateAssignToUser(proposal.getId(), userobj);
			Assert.assertTrue(id > 0);
		}
	}

	/**
	 * Mark option as default
	 */
	@Test
	public void testUpdateOptionAsDefault() {
		if (proposal != null) {
			List<ProposalOption> proposalOptionLst = proposalService.getProposalOptionsById(proposal.getId());
			if (proposalOptionLst != null && !proposalOptionLst.isEmpty()) {
				proposalService.updateOptionAsDefault(proposal.getId(), proposalOptionLst.get(0).getId());
				Assert.assertTrue(true);
			}
		}
	}

	/**
	 * save option
	 * @throws CustomCheckedException 
	 */
	@Test
	public void testSaveOption() throws CustomCheckedException {
		if (proposal != null) {
			ProposalOption dummyOption = createDummyProposalOption(proposal);
			ProposalOption option = proposalService.saveOption(proposal.getId(), dummyOption);
			Assert.assertTrue(option.getId() > 0);
		}
	}
	
	/**
	 * save option
	 * @throws CustomCheckedException 
	 */
	@Test
	public void testUpdateOption() throws CustomCheckedException {
		if (proposal != null) {
			ProposalOption optionDB = proposal.getDefaultOption();
			optionDB.setActive(false);
			ProposalOption option = proposalService.saveOption(proposal.getId(), optionDB);
			Assert.assertFalse(option.isActive());
		}
	}

	/**
	 * Update all line item avails
	 */
	@Test
	public void testUpdateLineItemsAvails() {
		if (lineItemLst != null && !lineItemLst.isEmpty()) {
			lineItemLst.get(0).setAvails(111D);
			proposalService.updateLineItemsAvails(proposal.getId(), lineItemLst);
			Assert.assertTrue(true);
		}
	}

	/**
	 * Calculate Net CPM, Net Impressions of a Proposal and update Proposal
	 * Version NetImpression and CPM
	 */
	@Test
	public void testUpdateProposalVersionNetImpressionAndCPM() {
		if (proposalOption != null) {
			ProposalVersion proposalVersion = proposalOption.getLatestVersion();
			if (proposalVersion != null) {
				Long id = proposalService.updateProposalVersionNetImpressionAndCPM(proposalOption.getId(), proposalVersion.getProposalVersion());
				Assert.assertTrue(id == proposalVersion.getId());
			}
		}
	}

	/**
	 * Method is executed on AssignToMe Functionality to update the Assigned
	 * user of a Proposal
	 * @throws CustomCheckedException 
	 */
	@Test
	public void testUpdateAssignToUserProposal() throws CustomCheckedException {
		if (proposal != null) {
			long id = proposalService.updateAssignToUser(proposal.getId(), userobj);
			Assert.assertTrue(id == proposal.getId());
		}
	}

	/**
	 * Return list of linrItrmException based on lineItem Id.
	 */
	@Test
	public void testGetLineItemExceptions() {
		if (lineItemLst != null && !lineItemLst.isEmpty()) {
			List<LineItemExceptions> lineItemExceptionsLst = proposalService.getLineItemExceptions(lineItemLst.get(0).getLineItemID());
			Assert.assertTrue(lineItemLst.get(0).getLineItemExceptions().size() == lineItemExceptionsLst.size());
		}
	}

	/**
	 * Return list of campaign objectives for a Proposal
	 */
	@Test
	public void testGetCampaignObjectivesByProposalId() {
		if (proposal != null) {
			Set<CampaignObjective> campaignObjectiveSet = proposalService.getCampaignObjectivesByProposalId(proposal.getId());
			Assert.assertTrue(campaignObjectiveSet.size() == proposal.getCampaignObjectiveSet().size());
		}
	}

	/**
	 * Update base price of given line item
	 */
	@Test
	public void testUpdateAllLineItemsPrice() {
		if (proposal != null && (lineItemLst != null && !lineItemLst.isEmpty())) {
			List<Long> lineItemsIDLst = new ArrayList<Long>();
			for (LineItem lineItem : lineItemLst) {
				lineItemsIDLst.add(lineItem.getLineItemID());
			}
			if (lineItemsIDLst != null && !lineItemsIDLst.isEmpty()) {
				proposalService.updateAllLineItemsPrice(proposal.getId(), lineItemsIDLst.toArray(new Long[0]), proposal.getSosSalesCategoryId(), "Gross", 15.0);
				Assert.assertTrue(true);
			}
		}
	}

	@Test
	public void testUpdateProposalStatus() throws CustomCheckedException, ParseException {
		long proposalId = 0;
		Proposal proposalDB = null;
		List<ProposalStatus> statusLst = new ArrayList<ProposalStatus>();
		statusLst.add(ProposalStatus.INPROGRESS);
		List<Proposal> proposalLst = proposalService.getProposalsForUpdation(DateUtil.parseToDate(DateUtil.getGuiDateString(DateUtil.getPriorDateFromCurrentDate(1))), statusLst);

		if (proposalLst != null && !proposalLst.isEmpty()) {
			Proposal proposal = proposalLst.get(0);
			proposalId = proposalService.updateProposalStatus(proposal.getId(), ProposalStatus.INPROGRESS, proposal.getVersion());
			proposalDB = proposalService.getProposalbyId(proposalId);
			Assert.assertTrue(proposalDB.getProposalStatus().equals(ProposalStatus.INPROGRESS));
			proposalId = proposalService.updateProposalStatus(proposal.getId(), ProposalStatus.DELETED, proposal.getVersion());
			proposalDB = proposalService.getProposalbyId(proposalId);
			Assert.assertTrue(proposalDB.getProposalStatus().equals(ProposalStatus.DELETED));
		}
	}

	@Test
	public void testUpdateProposalStatusByExpired() {
		List<ProposalStatus> statusLst = new ArrayList<ProposalStatus>();
		statusLst.add(ProposalStatus.REJECTED_BY_CLIENT);
		statusLst.add(ProposalStatus.PROPOSED);
		List<Proposal> proposalLst = proposalService.getProposalsForUpdation(DateUtil.parseToDate(DateUtil.getGuiDateString(DateUtil.getPriorDateFromCurrentDate(60))), statusLst);

		if (!proposalLst.isEmpty()) {
			List<Proposal> proposalDBLst = proposalService.updateProposalStatus(proposalLst, ProposalStatus.EXPIRED);
			Assert.assertTrue(proposalDBLst.get(0).getProposalStatus().equals(ProposalStatus.EXPIRED));
		}
	}

	@Test
	public void testUpdateProposalConversionRate() {
		Proposal proposalDB = null;
		if (proposal != null) {
				Map<String,Double> conversionRateMap = new HashMap<String, Double>();				
				try {
					conversionRateMap = proposalSOSService.getCurrencyConversionRate();
				} catch (ParseException e) {
					Assert.assertTrue(true);
				}
				proposalDB = proposalService.updateProposalConversionRate(proposal.getId(), conversionRateMap);
				proposalDB = proposalService.getProposalbyId(proposalDB.getId());
				Assert.assertTrue(proposalDB.getConversionRate() == conversionRateMap.get(proposalDB.getCurrency()));
		}
	}

	
	@Test
	public void testGetAllReservedLineItemsForProposal() {
		long proposalId = 0;
		if (proposal != null) {
			proposalId = proposal.getId();
			List<ProposalOption> optionLst = proposalService.getProposalOptionsById(proposalId);
			final List<Long> proposalVersionIdLst = new ArrayList<Long>(optionLst.size());
			for (ProposalOption option : optionLst) {
				proposalVersionIdLst.add(option.getLatestVersion().getId());
			}
			final List<LineItem> lineItemLst = proposalService.getAllReservedLineItemsForProposal(proposalId, proposalVersionIdLst, null, null, null);
			int count = proposalService.getReservedLineItemsCount(proposalId, proposalVersionIdLst);
			Assert.assertTrue(lineItemLst.size() <= count);
		}
	}

	@Test
	public void testUpdateReservations() throws CustomCheckedException {
		long lineItemID = 0;
		if (reservlineItemDB != null) {
			lineItemID = reservlineItemDB.getLineItemID();
			List<LineItemReservations> lineItemReservationsLst = proposalService.getReservationBylineItemID(lineItemID);
			Assert.assertTrue(lineItemReservationsLst.size() > 0);
			LineItemReservations lineItemReservations = lineItemReservationsLst.get(0);
			lineItemReservations.setStatus(ReservationStatus.RE_NEW);
			LineItemReservations lineItemReservationsDB = proposalService.updateLineItemReservations(lineItemReservations);
			Assert.assertTrue(lineItemReservationsDB.getStatus().equals(ReservationStatus.RE_NEW));
			try {
				LineItemReservations lineItemReservationBD = proposalService.updateReservations(reservlineItemDB.getProposalId(), lineItemID, DateUtil.getCurrentDate());
				Assert.assertTrue(lineItemReservationBD != null);
			} catch (ProposalAccessException e) {
				Assert.assertTrue(true);
			}
		}
	}
	
	@Test
	public void testUpdateBulkReservations() throws CustomCheckedException {
		long lineItemID = 0;
		if (reservlineItemDB != null) {
			lineItemID = reservlineItemDB.getLineItemID();
			List<Long> lineItemLst = new ArrayList<Long>();
			lineItemLst.add(lineItemID);
			List<LineItemReservations> lineItemReservationsLst = proposalService.getReservationsBylineItemIDs(lineItemLst);
			Assert.assertTrue(lineItemReservationsLst.size() > 0);
			LineItemReservations lineItemReservations = lineItemReservationsLst.get(0);
			lineItemReservations.setStatus(ReservationStatus.RE_NEW);
			proposalService.updateAllLineItemReservations(lineItemReservationsLst);
			Assert.assertTrue(lineItemReservations.getStatus().equals(ReservationStatus.RE_NEW));
			try {
				LineItemReservations lineItemReservationBD = proposalService.updateReservations(reservlineItemDB.getProposalId(), lineItemID, DateUtil.getCurrentDate());
				Assert.assertTrue(lineItemReservationBD != null);
			} catch (ProposalAccessException e) {
				Assert.assertTrue(true);
			}
		}
	}

	@Test
	public void testCreateAndMoveReservationData() throws CustomCheckedException {
		long lineItemID = 0;
		long proposalID = 0;
		long newOptionID = 0;
		if (reservlineItemDB != null && proposal != null) {
			proposalID = proposal.getId();
			lineItemID = reservlineItemDB.getLineItemID();
			newOptionID = proposal.getProposalOptions().iterator().next().getId();
			try {
				Set<LineItem> lineItemSet = proposalService.createAndMoveReservationData(proposalID, lineItemID, newOptionID);
				Assert.assertTrue(lineItemSet != null);
			} catch (ProposalAccessException e) {
				Assert.assertTrue(true);
			}
		}
	}

	@Test
	public void testApplyAgencyMargin() {
		double basePrice = 65;
		Double basePriceDB = proposalService.applyAgencyMargin("Gross", basePrice, 15.0);
		basePrice = basePrice * 100 / (100 - 15);
		basePrice = NumberUtil.round(basePrice, 2);
		Assert.assertTrue(basePrice == basePriceDB);
	}

	@Test
	public void testGetProposalsForReservationSearch() {
		String expiryDate = DateUtil.getGuiDateTimeString(DateUtil.getCurrentDate().getTime());
		List<LineItem> lineItemLst = proposalService.getProposalsForReservationSearch(getFilterCriteria(expiryDate));
		Assert.assertTrue(lineItemLst.size() >= 0);
		final List<RangeFilterCriteria> criteriaLst = new ArrayList<RangeFilterCriteria>();
		lineItemLst = proposalService.getProposalsForReservationSearch(criteriaLst);
		List<LineItem> lineItemLstWithNullCriteria = proposalService.getProposalsForReservationSearch(null);
		Assert.assertTrue(lineItemLstWithNullCriteria.size() == lineItemLst.size());
	}

	/**
	 * @throws CustomCheckedException 
	 * 
	 */
	@Test
	public void testSaveReservationDataFrmProposalToAnother() throws CustomCheckedException {
		String expiryDate = DateUtil.getGuiDateString(DateUtil.getCurrentDate().getTime());
		long newOptionID = 0;
		if (reservlineItemDB != null && proposal != null) {
			PaginationCriteria pgCriteria = new PaginationCriteria(1, 5);
			final List<Proposal> proposalLst = proposalService.getProposalListhavingLineItems(null, pgCriteria, null);
			for (Proposal proposalDB : proposalLst) {
				if(proposalDB.getId() != proposal.getId()){
					newOptionID = proposalDB.getDefaultOption().getId();
					break;
				}
			}
			Set<LineItem> lineItemSet = proposalService.saveReservationDataFrmProposalToAnother(reservlineItemDB.getLineItemID(), newOptionID ,expiryDate);
			Assert.assertTrue(lineItemSet.size() > 0);
		}
	}
	
	@Test
	public void testGetProposalbyIdForSosIntegration(){
		Assert.assertNotNull(proposalService.getProposalbyIdForSosIntegration(proposal.getId()));
	}
	
	@Test
	public void testGetAllProposalList(){
		Assert.assertNotNull(proposalService.getAllProposalList());
	}
	
	@Test
	public void testGetProposalForClone(){
		Assert.assertNotNull(proposalService.getProposalForClone(proposal.getId()));
	}
	
	@Test
	public void testGetLineItems(){
		Assert.assertNotNull(proposalService.getLineItems(lineItemLst.get(0).getLineItemID().toString()));
	}
	
	@Test
	public void testCloneProposal() throws CustomCheckedException{
		getTransactionManager().getSessionFactory().getCurrentSession().clear();
		Proposal clonedProposal = proposalService.createClone(proposal.getId(), String.valueOf(proposal.getDefaultOption().getId()));
		Assert.assertTrue(clonedProposal.getId() != proposal.getId());
	}
	
	@Test
	public void testDeleteReservationsOfNonDefaultOptions() throws CustomCheckedException{
		getTransactionManager().getSessionFactory().getCurrentSession().clear();
		proposalService.createClone(proposal.getId(), String.valueOf(proposal.getDefaultOption().getId()));
		getTransactionManager().getSessionFactory().getCurrentSession().flush();
		proposalService.deleteReservationsOfNonDefaultOptions(proposal.getId());
		Assert.assertTrue(true);
		
	}
	
	@Test
	public void testGetCampaignObjectivesMap(){
		Assert.assertFalse(proposalService.getCampaignObjectivesMap().isEmpty());
	}
	
	@Test
	public void testGetProposalGeoTargets(){
		List<LineItemTarget>  targetLst = proposalService.getProposalGeoTargets(lineItemLst.get(0).getLineItemID());
		if(targetLst != null && !targetLst.isEmpty()){
			Assert.assertFalse(targetLst.isEmpty());
		}
	}
	
	@Test
	public void testUpdateProposalOrderId(){
		long updatedProposal = proposalService.updateProposalOrderId(proposal.getId(), 236476l);
		Assert.assertTrue(updatedProposal > 0);
	}
	
	@Test
	public void testGetLineItemById(){
		Assert.assertNotNull(proposalService.getLineItemById(lineItemLst.get(0).getLineItemID()));
	}
	
	@Test
	public void testSaveNotes(){
		Notes notes = new Notes();
		notes.setCreatedByUserName(userobj.getFullName());
		notes.setId(0);
		notes.setDescription("Test notes from jUnits");
		notes.setProposalId(proposal.getId());
		notes.setPushedInSalesforce(false);
		notes.setRole(userobj.getUserRoles().iterator().next().getRoleName());		
		notes = proposalService.saveNotes(proposal.getId(), 0, notes);
		getTransactionManager().getSessionFactory().getCurrentSession().flush();
		Assert.assertTrue(notes.getId() > 0);
		notes.setDescription("ABC");
		notes = proposalService.saveNotes(proposal.getId(), notes.getId(), notes);
		getTransactionManager().getSessionFactory().getCurrentSession().flush();
		Assert.assertTrue(notes.getDescription().equals("ABC"));
	}
	
	@Test
	public void testGetProposalNotes(){
		Notes notes = new Notes();
		notes.setCreatedByUserName(userobj.getFullName());
		notes.setId(0);
		notes.setDescription("Test notes from jUnits");
		notes.setProposalId(proposal.getId());
		notes.setPushedInSalesforce(false);
		notes.setRole(userobj.getUserRoles().iterator().next().getRoleName());		
		notes = proposalService.saveNotes(proposal.getId(), 0, notes);
		getTransactionManager().getSessionFactory().getCurrentSession().flush();
		Assert.assertTrue(proposalService.getProposalNotes(proposal.getId()).size() > 0);
	}
	
	@Test
	public void testDeleteNotes() {
		Notes notes = new Notes();
		notes.setCreatedByUserName(userobj.getFullName());
		notes.setId(0);
		notes.setDescription("Test notes from jUnits");
		notes.setProposalId(proposal.getId());
		notes.setPushedInSalesforce(false);
		notes.setRole(userobj.getUserRoles().iterator().next().getRoleName());		
		notes = proposalService.saveNotes(proposal.getId(), 0, notes);
		getTransactionManager().getSessionFactory().getCurrentSession().flush();
		long notesId = proposalService.deleteNotes(proposal.getId(), notes.getId());
		Assert.assertTrue(notesId > 0);
	}
	
	@Test
	public void testDeleteReservationByLineItemId() throws CustomCheckedException{
		if(reservlineItemDB!=null){
			Assert.assertTrue(proposalService.deleteReservationByLineItemId(reservlineItemDB.getLineItemID()));
		}
	}
	
	@Test
	public void testGetReservationByLineItemId(){
		if(reservlineItemDB != null){
			Assert.assertNotNull(proposalService.getReservationBylineItemID(reservlineItemDB.getLineItemID()));
		}
	}
	
	@Test
	public void testUpdateLineItemReservations(){
		if(reservlineItemDB != null){
			LineItemReservations reservation = reservlineItemDB.getReservation();
			reservation.setStatus(ReservationStatus.RE_NEW);
			Assert.assertNotNull(proposalService.updateLineItemReservations(reservation));
		}
	}
	
	@Test
	public void testGetLineItemsOnBaseOfId(){
		Long[] lineItems = new Long[1];
		lineItems[0] = reservlineItemDB.getLineItemID();
		Assert.assertTrue(proposalService.getLineItemsOnBaseOfId(lineItems).size() > 0);
	}
	
	@Test
	public void testUpdateAllLineItemPricingStatus(){
		proposalService.updateAllLineItemPricingStatus(proposalOption.getId());
		Assert.assertTrue(true);
	}
	
	@Test
	public void testUpdateLineItemsOfProposalInBulk(){
		proposalService.updateLineItemsOfProposal(lineItemLst);
		Assert.assertTrue(true);
	}
	
	@Test
	public void testSaveOptionsThresholdValue(){
		proposalService.saveOptionsThresholdValue(proposal.getProposalOptions());
		Assert.assertTrue(true);
	}
	
	@Test
	public void testGetLineItemsList(){
		Assert.assertTrue(proposalService.getLineItemsList(lineItemLst.get(0).getLineItemID().toString()).size() > 0);
	}
	
	@Test
	public void testGetOptionLstbyIds(){
		Long[] options = new Long[1];
		options[0] = proposalOption.getId();
		Assert.assertTrue(proposalService.getOptionLstbyIds(options).size() > 0);
	}
	
	@Test
	public void testGetProposalAndAssgndUsr(){
		Assert.assertNotNull(proposalService.getProposalAndAssgndUsr(proposal.getId()));
	}
	
	@Test
	public void testGetProposalsAndAssgndUsrs(){
		List<Long> proposalLst = new ArrayList<Long>();
		proposalLst.add(proposal.getId());
		Assert.assertNotNull(proposalService.getProposalsAndAssgndUsrs(proposalLst));
	}
	
	@Test
	public void testsaveNewSalesfoceId() throws CustomCheckedException, ConnectionException{
		PaginationCriteria pgCriteria = new PaginationCriteria(1, 100);
		Proposal proposalWithNoSf = null;
		final List<Proposal> proposalLst = proposalService.getProposalList(null, pgCriteria, null);
		for (Proposal prop : proposalLst) {
			if(prop.getSalesforceID() == null) {
				proposalWithNoSf = prop;
				break;
			}
		}
		proposalWithNoSf.setAssignedUser(SecurityUtil.getUser());
		proposalService.saveNewSalesfoceId(proposalWithNoSf.getId(), "123456789abcde");
		getTransactionManager().getSessionFactory().getCurrentSession().evict(proposalWithNoSf);
		Assert.assertTrue("123456789abcde".equals(proposalWithNoSf.getSalesforceID()));
	}
	
	/**
	 * @param expiryDate
	 * @return
	 */
	private List<RangeFilterCriteria> getFilterCriteria(final String expiryDate) {
		final List<RangeFilterCriteria> criteriaLst = new ArrayList<RangeFilterCriteria>();

		RangeFilterCriteria currentDateFilterCriteria = new RangeFilterCriteria();
		currentDateFilterCriteria.setSearchField("expiryDate");
		currentDateFilterCriteria.setSearchString(expiryDate);
		criteriaLst.add(currentDateFilterCriteria);

		RangeFilterCriteria renewStatusFilterCriteria = new RangeFilterCriteria();
		renewStatusFilterCriteria.setSearchField(ConstantStrings.STATUS);
		renewStatusFilterCriteria.setSearchString(String.valueOf(ReservationStatus.RE_NEW) + ConstantStrings.COMMA + String.valueOf(ReservationStatus.HOLD));
		criteriaLst.add(renewStatusFilterCriteria);

		return criteriaLst;
	}

	/**
	 * Return dummy proposal option from proposal
	 * @param proposal
	 * @return
	 */
	private ProposalOption createDummyProposalOption(final Proposal proposal) {
		ProposalOption option = new ProposalOption();
		option.setName("OptionByTestcase");
		option.setProposal(proposal);
		option.setVersion(1);
		return option;
	}

	private LineItem getDummyLineItem(ProposalVersion proposalVersion, long proposalId) {
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
		returnLineItem.setProposalId(proposalId);
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
	
	
	/**
	 * Used to create dummy proposal
	 * @return
	 */
	private Proposal createDummyProposal() {
		Proposal proposalNew = new Proposal();
		proposalNew.setActive(true);
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
	
}
