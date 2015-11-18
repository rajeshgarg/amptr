package com.nyt.mpt.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nyt.mpt.domain.CampaignObjective;
import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.LineItemExceptions;
import com.nyt.mpt.domain.LineItemReservations;
import com.nyt.mpt.domain.LineItemTarget;
import com.nyt.mpt.domain.Notes;
import com.nyt.mpt.domain.Proposal;
import com.nyt.mpt.domain.ProposalOption;
import com.nyt.mpt.domain.ProposalVersion;
import com.nyt.mpt.domain.User;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.enums.ProposalStatus;
import com.nyt.mpt.util.exception.CustomCheckedException;
import com.nyt.mpt.util.filter.FilterCriteria;
import com.nyt.mpt.util.filter.RangeFilterCriteria;
import com.sforce.ws.ConnectionException;

/**
 * This interface declare Proposal Service level methods
 * 
 * @author surendra.singh
 * 
 */
public interface IProposalService {

	/**
	 * Return list of all active proposal.
	 * 
	 * @param filterCriteria
	 * @return List<Proposal>
	 */
	List<Proposal> getProposalList(List<RangeFilterCriteria> filterCriteriaLst,
			PaginationCriteria paginationCriteria,
			SortingCriteria sortingCriteria);

	/**
	 * Return proposal list count
	 * 
	 * @param filterCriteriaLst
	 * @return int
	 */
	int getProposalListCount(List<RangeFilterCriteria> filterCriteriaLst);

	/**
	 * Save a new Proposal if Proposal is not already exist else update Proposal
	 * 
	 * @param proposal
	 * @return
	 * @throws CustomCheckedException 
	 */
	long saveProposal(long proposalId, Proposal proposal) throws CustomCheckedException;

	/**
	 * Return Proposal based on proposalId.
	 * 
	 * @param proposalId
	 * @return Proposal
	 */
	Proposal getProposalbyId(Long proposalId);

	/**
	 * Return proposal based on proposalId
	 * 
	 * @param proposalId
	 * @return
	 */
	Proposal getProposalForClone(final Long proposalId);

	/**
	 * Return proposal and assigned User based on proposalId
	 * 
	 * @param proposalId
	 * @return
	 */
	Proposal getProposalAndAssgndUsr(final Long proposalId);

	/**
	 * Return a list of ProposalLineItemAssoc based on option, proposalversion,
	 * filter, pagination and sorting criteria
	 * 
	 * @param optionID
	 * @param proposalVersion
	 * @param criteria
	 * @param pgCriteria
	 * @param sortCriteria
	 * @return List<ProposalLineItemAssoc>
	 */
	List<LineItem> getProposalLineItems(Long optionID, Long proposalVersion,
			FilterCriteria criteria, PaginationCriteria pgCriteria,
			SortingCriteria sortCriteria);

	/**
	 * Return count of Option line items based on Option Id, proposalversion and
	 * filter criteria.
	 * 
	 * @param optionID
	 * @param proposalVersion
	 * @param filterCriteria
	 * @return int
	 */
	int getFilteredPackageLineItemsCount(Long optionID, Long proposalVersion,
			FilterCriteria criteria);

	/**
	 * Saving Line Items Of Option with OptionId, proposalLineItem and
	 * proposalversion
	 * 
	 * @param proposalID
	 * @param OptionId
	 * @param proposalLineItem
	 * @param proposalversion
	 * @return
	 * @throws CustomCheckedException 
	 */
	Long saveLineItemsOfProposal(final Long proposalID, long OptionId,
			LineItem proposalLineItem, long proposalversion) throws CustomCheckedException;

	/**
	 * Updating Line Items When Associated with Option version based on LineItem
	 * id.
	 * 
	 * @param proposalID
	 * @param proposalLineItem
	 * @param forceupdate
	 * @return Long
	 * @throws CustomCheckedException 
	 */
	Long updateLineItemsOfProposal(final Long proposalID, LineItem proposalLineItem) throws CustomCheckedException;

	/**
	 * Return a list of LineItem based on lineItemIds(in clause)
	 * 
	 * @param lineItemId
	 * @return List<LineItem>
	 */
	List<LineItem> getLineItems(String lineItemId);

	/**
	 * Creating version for a Option.
	 * 
	 * @param proposalID
	 * @param optionId
	 * @param proposalversion
	 * @return Long
	 */
	Long createVersion(Long proposalId, Long optionId, Long proposalversion);

	/**
	 * Retrieves the Proposal option based on optionId
	 * 
	 * @param optionId
	 * @return
	 */
	ProposalOption getOptionbyId(Long optionId);

	/**
	 * Create a new option from Existing One
	 * 
	 * @param proposalId
	 * @param optionId
	 * @param optionversion
	 * @param maxOptionNo
	 * @return
	 */
	Long createOptionClone(Long proposalId, Long optionId, Long optionversion,
			int maxOptionNo);

	/**
	 * Creating Proposal Clone based on ProposalId and optionIds.
	 * 
	 * @param proposalId
	 * @param optionIds
	 * @return
	 */
	Proposal createClone(Long proposalId, String optionIds);

	/**
	 * Deleting proposal Line Items.
	 * 
	 * @param proposalId
	 * @param lineItemId
	 * @return
	 * @throws CustomCheckedException 
	 */
	List<Long> deleteProposalLineItem(final Long proposalId,
			final String lineItemId) throws CustomCheckedException;

	/**
	 * Copying LineItems to proposal based on proposalID, proposalVersion, type,
	 * lineItems and partiallyCopiedUnbreakPackage.
	 * 
	 * @param proposalID
	 * @param proposalVersion
	 * @param type
	 * @param lineItems
	 * @param partiallyCopiedUnbreakPackage
	 * @param isCopiedFromExpired
	 * @return Long
	 */
	boolean saveCopiedLineItemsToProposal(final Long proposalID,
			final Long optionId, final long proposalVersion, final String type,
			final Long[] lineItems, boolean partiallyCopiedUnbreakPackage,
			boolean isCopiedFromExpired, int copiedLineItemIds,
			final String priceType, final boolean iscopiedFromGrid,
			final double sourceAgencyMargin);

	/**
	 * Return a list of ProposalVersion based on optionId and proposalversion.
	 * 
	 * @param optionId
	 * @param proposalversion
	 * @return List<ProposalVersion>
	 */
	List<ProposalVersion> getproposalVersions(Long optionId,
			Long proposalversion);

	/**
	 * Method to return ProposalVersion on Base of proposalversionId
	 * 
	 * @param proposalversionId
	 * @return ProposalVersion
	 */
	ProposalVersion getProposalVersion(long proposalversionId);

	/**
	 * Method will return the next sequence no for the proposal Line Item
	 * 
	 * @param versionId
	 * @return
	 */
	int getNextSequenceNoForLineItem(long versionId);

	/**
	 * This method is used to fetch all campaign objectives from database
	 * 
	 * @return List<CampaignObjective>
	 */
	List<CampaignObjective> getCampaignObjectives();

	/**
	 * Return list of campaign objectives for a Proposal
	 * 
	 * @param proposalId
	 * @return Set<CampaignObjective>
	 */
	Set<CampaignObjective> getCampaignObjectivesByProposalId(long proposalId);

	/**
	 * Returns the campaign Objective Map.
	 * 
	 * @return
	 */
	Map<String, Long> getCampaignObjectivesMap();

	/**
	 * Check for proposal with same name based on proposalName and proposalId.
	 * 
	 * @param proposalName
	 * @param proposalId
	 * @return int
	 */
	int proposalCountWithSameName(String proposalName, Long proposalId);

	/**
	 * Return list of linrItrmException based on lineItem Id.
	 * 
	 * @param lineItemID
	 * @return List <LineItemExceptions>
	 */
	List<LineItemExceptions> getLineItemExceptions(Long lineItemID);

	/**
	 * Calculate Net CPM, Net Impressions of a Proposal and update Proposal
	 * Version NetImpression and CPM
	 * 
	 * @param optionId
	 * @param proposalVersion
	 * @return Long
	 */
	Long updateProposalVersionNetImpressionAndCPM(Long optionId,
			Long proposalVersion);

	/**
	 * Return a list of LineItemTarget based on elementId.
	 * 
	 * @param elementId
	 * @return List<LineItemTarget>
	 */
	List<LineItemTarget> getProposalGeoTargets(Long elementId);

	/**
	 * Update all line item avails
	 * 
	 * @param proposalId
	 * @param lineItemLst
	 */
	void updateLineItemsAvails(Long proposalId, List<LineItem> lineItemLst);

	/**
	 * @param proposalId
	 * @param option
	 * @return
	 * @throws CustomCheckedException 
	 */
	ProposalOption saveOption(Long proposalId, ProposalOption option) throws CustomCheckedException;

	/**
	 * Mark option as default
	 * 
	 * @param proposalId
	 * @param optionId
	 */
	void updateOptionAsDefault(long proposalId, long optionId);

	/**
	 * Returns the line item with the given line item Id.
	 * 
	 * @param lineItemId
	 * @return
	 */
	public LineItem getLineItemById(final Long lineItemId);

	/**
	 * Update Assignment of a proposal
	 * 
	 * @param proposalId
	 * @param usr
	 * @return
	 * @throws CustomCheckedException 
	 */
	long updateAssignToUser(Long proposalId, User usr) throws CustomCheckedException;

	/**
	 * Returns all active options of a proposal
	 * 
	 * @param proposalId
	 * @return
	 */
	List<ProposalOption> getProposalOptionsById(Long proposalId);

	/**
	 * Update base price of given line item
	 * 
	 * @param proposalID
	 * @param lineItems
	 * @param salesCategoryId
	 * @param propPriceType
	 * @param propAgencyMargin
	 */
	void updateAllLineItemsPrice(final Long proposalID, final Long[] lineItems,
			final Long salesCategoryId, String propPriceType,
			double propAgencyMargin);

	/**
	 * Update status of a proposal
	 * 
	 * @param proposalId
	 * @param status
	 * @return
	 * @throws CustomCheckedException 
	 * @throws ParseException 
	 */
	long updateProposalStatus(long proposalId, ProposalStatus status,
			int version) throws CustomCheckedException, ParseException;

	/**
	 * Update Order No of a proposal
	 * 
	 * @param proposalId
	 * @param sosOrderId
	 * @return
	 */
	long updateProposalOrderId(long proposalId, Long sosOrderId);

	/**
	 * Updates the conversion rate of the proposal.
	 * 
	 * @param proposalId
	 * @param conversionRateMap
	 * @return
	 */
	Proposal updateProposalConversionRate(long proposalId, Map<String,Double> conversionRateMap);

	/**
	 * Save the Notes if notes does not exist else update it
	 * 
	 * @param notesId
	 * @param proposalId
	 * @param notes
	 * @return
	 */
	Notes saveNotes(final long proposalId, final long notesId, final Notes notes);

	/**
	 * Deletes the notes with the given id.
	 * 
	 * @param proposalId
	 * @param notesId
	 * @return
	 */
	Long deleteNotes(final long proposalId, final long notesId);

	/**
	 * Returns all the notes of a proposal.
	 * 
	 * @param proposalId
	 * @return
	 */
	List<Notes> getProposalNotes(final long proposalId);

	/**
	 * Returns the list of all proposals having status among statuses in
	 * statusLst and whose due date is older than expiryDueDate.
	 * 
	 * @param expiryDueDate
	 * @return
	 */
	List<Proposal> getProposalsForUpdation(Date expiryProposedDate,
			List<ProposalStatus> statusLst);

	/**
	 * Updates the status of all the proposals to be expired as 'EXPIRED'
	 * 
	 * @param proposalLst
	 * @return
	 */
	List<Proposal> updateProposalStatus(List<Proposal> proposalLst,
			ProposalStatus newStatus);

	/**
	 * Get list of reserved line items
	 * 
	 * @param proposalId
	 * @param proposalVersionIdLst
	 * @param filterCriteria
	 * @param pgCriteria
	 * @param sortingCriteria
	 * @return
	 */
	List<LineItem> getAllReservedLineItemsForProposal(final Long proposalId,
			final List<Long> proposalVersionIdLst,
			final FilterCriteria filterCriteria,
			final PaginationCriteria pgCriteria,
			final SortingCriteria sortingCriteria);

	/**
	 * Get count of reserved line items
	 * 
	 * @param proposalId
	 * @param proposalVersionIdLst
	 * @return
	 */
	int getReservedLineItemsCount(final Long proposalId,
			final List<Long> proposalVersionIdLst);

	/**
	 * Get reservation data by lineItemID
	 * 
	 * @param lineItemID
	 * @return
	 */
	List<LineItemReservations> getReservationBylineItemID(final Long lineItemID);

	/**
	 * Update line item reservations data, without intercepter
	 * 
	 * @param lineItemReservations
	 * @return
	 */
	LineItemReservations updateLineItemReservations(
			LineItemReservations lineItemReservations);

	/**
	 * Update line item reservations data with 'ValidateProposal' intercepter
	 * 
	 * @param proposalID
	 * @param lineItemID
	 * @param expiryDate
	 * @return
	 * @throws CustomCheckedException 
	 */
	LineItemReservations updateReservations(long proposalID, long lineItemID,
			Date expiryDate) throws CustomCheckedException;

	/**
	 * Move line item reservation data into given option
	 * 
	 * @param proposalID
	 * @param lineItemID
	 * @param newOptionId
	 * @return
	 */
	Set<LineItem> createAndMoveReservationData(long proposalID,
			final long lineItemID, final long newOptionId);

	/**
	 * This method is used to apply margin rules on rate card price if the price
	 * type is "Gross"
	 * 
	 * @param ruleId
	 * @return List<PricingBase>
	 */
	double applyAgencyMargin(String priceType, double basePrice, double margin);

	/**
	 * Returns the list of reservation LineItem based search criteria
	 * 
	 * @param criteriaLst
	 * @return
	 */
	List<LineItem> getProposalsForReservationSearch(
			final List<RangeFilterCriteria> criteriaLst);

	/**
	 * deletes reservation by lineitem ID
	 * @param lineItemId
	 * @return 
	 * @throws CustomCheckedException 
	 */
	boolean deleteReservationByLineItemId(Long lineItemId) throws CustomCheckedException;
	
	/**
	 * 
	 * Move line item reservation data into another Proposal(Default Option)
	 * 
	 * @param lineItemID
	 * @param newOptionId
	 * @return
	 * @throws CustomCheckedException 
	 */
	Set<LineItem> saveReservationDataFrmProposalToAnother(long lineItemID,
			long newOptionId, String expirationDate) throws CustomCheckedException;

	/**
	 * 
	 * @param proposalId
	 * @return
	 * @throws CustomCheckedException
	 */
	List<LineItem> deleteReservationsOfNonDefaultOptions(long proposalId) throws CustomCheckedException;

	/**
	 * Returns List of Proposals without anything initialized
	 * 
	 * @return
	 */
	List<Proposal> getAllProposalList();

	/**
	 * Return Proposal based on proposalId.
	 * 
	 * @param proposalId
	 * @return Proposal
	 */
	Proposal getProposalbyIdForSosIntegration(Long proposalId);

	/**
	 * Return a list of line Items on base of lineItems .
	 * 
	 * @param lineItems
	 * @return List<LineItem>
	 */
	List<LineItem> getLineItemsOnBaseOfId(Long[] lineItems);
	
	/**
	 * Returns the qualifying Line Items Price Type
	 * @return
	 */
	Map<String,String> getQualifyingLineItems();

	/**
	 * @param optionID
	 * @return
	 */
	void updateAllLineItemPricingStatus(final Long optionID);
	
	/**
	 * Saves the threshold value of the given set of options
	 * @param optionSet
	 */
	void saveOptionsThresholdValue(Set<ProposalOption> optionSet);
	
	/**
	 * update the List of Line Items
	 * @param lineItemLst
	 */
	void updateLineItemsOfProposal(List<LineItem> lineItemLst);
	
	/**
	 * Returns the List of Line items corresponding to the Ids given. 
	 * @param lineItemId
	 * @return
	 */
	List<LineItem> getLineItemsList(final String lineItemId);
	
	/**
	 * Returns the list of options with the given Ids.
	 * @param optionIds
	 * @return
	 */
	List<ProposalOption> getOptionLstbyIds(final Long[] optionIds);
	
	/**
	 * Splits the line items on monthly basis.
	 * @param proposalID
	 * @param optionId
	 * @param proposalVersion
	 * @param lineItemLst
	 * @throws CustomCheckedException 
	 */
	boolean createLineItemsBySpliting(final Long proposalID, final Long optionId, final long proposalVersion, final String lineItemIds) throws CustomCheckedException;

	/**
	 * @param proposal
	 * @param fromOptionId
	 * @param toOptionList
	 * @param lineItemIds
	 */
	void createLineItemToOtherOption(final Proposal proposal,  final Long fromOptionId, final List<String> toOptionList, final Long[] lineItemIds);
	
	/**
	 * Return a list of Proposal based on filter, pagination and sorting criteria  
	 * @param criteriaLst
	 * @param pgCriteria
	 * @param sortCriteria
	 * @return List<Proposal>
	 */
	List<Proposal> getProposalListhavingLineItems(List<RangeFilterCriteria> criteriaLst, PaginationCriteria pgCriteria, SortingCriteria sortCriteria);

	/**
	 * getReservations of multiple lineItemIds
	 * @param lineItemIDs
	 * @return
	 */
	List<LineItemReservations> getReservationsBylineItemIDs(List<Long> lineItemIDs);

	/**
	 * update bulk reservations
	 * @param reservationLst
	 * @throws CustomCheckedException 
	 */
	void updateAllLineItemReservations(List<LineItemReservations> reservationLst) throws CustomCheckedException;

	/**
	 * returns list of proposal based on ID
	 * @param proposalId
	 * @return
	 */
	List<Proposal> getProposalsAndAssgndUsrs(List<Long> proposalId);

	/**
	 * @param proposalId
	 * @param newSalesforceId
	 * @throws CustomCheckedException
	 * @throws ConnectionException
	 */
	void saveNewSalesfoceId(long proposalId, String newSalesforceId) throws CustomCheckedException, ConnectionException;
	
	
	/**
	 * Return a list of lineItems in vulnerable state having given product class Id.
	 * @param productClassId
	 * @return
	 */
   List<LineItem> getVulnerableHomepageReservations(Long productClassId);
}
