/**
 * 
 */
package com.nyt.mpt.form;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.nyt.mpt.domain.CampaignObjective;
import com.nyt.mpt.domain.Proposal;
import com.nyt.mpt.domain.ProposalOption;
import com.nyt.mpt.domain.ProposalVersion;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.DateUtil;
import com.nyt.mpt.util.NumberUtil;
import com.nyt.mpt.util.enums.Criticality;
import com.nyt.mpt.util.enums.ProposalStatus;

/**
 * This form bean is used for proposal information
 * 
 * @author Shishir.Srivastava
 * 
 */
@SuppressWarnings("serial")
public class ProposalForm extends BaseForm<Proposal> implements Serializable {

	private static final String ZERO = "0";

	private static final String DECIMAL_ZERO = "0.00";

	private String proposalName;

	private String campaignName;

	private String optionName;

	private long id;

	private long optionId;

	private String proposalVersion;

	private String remBudget;

	private String previousProposalVersion;

	private String budget;

	private String netCpm;

	private String netImpressions;

	private Set<ProposalForm> proposalVersions = new HashSet<ProposalForm>();

	private String clonedFormProposal;

	private String createdAt;

	private String createdBy;

	private String modifiedBy;

	private String modifiedOn;

	private String requestedOn;

	private String dueOn;

	private String startDate;

	private String endDate;

	private String agencyName;

	private String salescategory;

	private String salesCategoryDisplayName;

	private String advertiserName;

	private String proposalCommentsByMediaPlanner;

	private String advertisorContact;

	private String userName;

	private String userId;

	private String criticality;

	private String criticalityDisplayName;

	private String proposalStatus;

	private String proposalStatusDisplayName;

	private String assignedBy;

	private String proposalTemplate;

	private String priceType;

	private long[] campaignObjective;

	private long[] availCampaignObjective;

	private String myProposal;

	private String accountManager;

	private String newAdvertiserName;

	private String expired;

	private String offeredBudget;

	private String summaryAdvertiserName;

	private String summaryAgencyName;

	private String sosOrderId;

	private String discount;

	private String maxOptionNo;

	private String defaultOption;

	private String currency;

	private String conversionRate;
	
	private Double conversionRate_Euro;
	
	private Double conversionRate_Yen;

	private Long currencyId;

	private String reservationEmails;

	private String proposalTitle;

	private String agencyMargin;

	private String noOfReservations;

	private String sosURL;

	private String thresholdLimit;

	private boolean specialAdvertiser = false;

	private String qualifingLineItemInvestment;

	private String optionThresholdValueString;

	private String salesforceID;
	
	private String salesForceSearchKey;
		
	private String lastPricingReviewedDate;
	
	private String lastProposedDate;
	
	private boolean withPricing = false;
	
	private String pricingSubmittedDate;
	
	private String isOptionViewable;

	public boolean isSpecialAdvertiser() {
		return specialAdvertiser;
	}

	public void setSpecialAdvertiser(final boolean specialAdvertiser) {
		this.specialAdvertiser = specialAdvertiser;
	}

	public String getAgencyMargin() {
		return agencyMargin;
	}

	public void setAgencyMargin(final String agencyMargin) {
		this.agencyMargin = agencyMargin;
	}

	public Set<ProposalForm> getProposalVersions() {
		return proposalVersions;
	}

	public void setProposalVersions(final Set<ProposalForm> proposalVersions) {
		this.proposalVersions = proposalVersions;
	}

	public String getClonedFormProposal() {
		if (StringUtils.isBlank(clonedFormProposal)) {
			clonedFormProposal = ConstantStrings.NA;
		}
		return clonedFormProposal;
	}

	public void setClonedFormProposal(final String clonedFormProposal) {
		this.clonedFormProposal = clonedFormProposal == null ? clonedFormProposal : clonedFormProposal.trim();
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(final String createdAt) {
		this.createdAt = createdAt == null ? createdAt : createdAt.trim();
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(final String createdBy) {
		this.createdBy = createdBy == null ? createdBy : createdBy.trim();
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(final String modifiedBy) {
		this.modifiedBy = modifiedBy == null ? modifiedBy : modifiedBy.trim();
	}

	public String getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(final String modifiedOn) {
		this.modifiedOn = modifiedOn == null ? modifiedOn : modifiedOn.trim();
	}

	public String getRequestedOn() {
		return requestedOn;
	}

	public void setRequestedOn(final String requestedOn) {
		this.requestedOn = requestedOn == null ? requestedOn : requestedOn.trim();
	}

	public String getDueOn() {
		return dueOn;
	}

	public void setDueOn(final String dueOn) {
		this.dueOn = dueOn == null ? dueOn : dueOn.trim();
	}

	public String getProposalCommentsByMediaPlanner() {
		return proposalCommentsByMediaPlanner;
	}

	public void setProposalCommentsByMediaPlanner(final String comments) {
		this.proposalCommentsByMediaPlanner = comments == null ? comments : comments.trim();
	}

	public String getSalescategory() {
		return salescategory;
	}

	public void setSalescategory(final String salescategory) {
		this.salescategory = salescategory == null ? salescategory : salescategory.trim();
	}

	public String getAdvertiserName() {
		return advertiserName;
	}

	public void setAdvertiserName(final String advertiserName) {
		this.advertiserName = advertiserName == null ? advertiserName : advertiserName.trim();
	}

	public String getAgencyName() {
		return agencyName;
	}

	public void setAgencyName(final String agencyName) {
		this.agencyName = agencyName == null ? agencyName : agencyName.trim();
	}

	public String getProposalName() {
		return proposalName;
	}

	public void setProposalName(final String proposalName) {
		this.proposalName = proposalName == null ? proposalName : proposalName.trim();
	}

	public String getOptionName() {
		return optionName;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(final String campaignName) {
		this.campaignName = campaignName == null ? campaignName : campaignName.trim();
	}

	public void setOptionName(final String optionName) {
		this.optionName = optionName == null ? optionName : optionName.trim();
	}

	public long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public long getOptionId() {
		return optionId;
	}

	public void setOptionId(final long optionId) {
		this.optionId = optionId;
	}

	public String getBudget() {
		return budget;
	}

	public void setBudget(final String budget) {
		this.budget = budget == null ? budget : budget.trim();
	}

	public String getProposalVersion() {
		return proposalVersion;
	}

	public void setProposalVersion(final String proposalVersion) {
		this.proposalVersion = proposalVersion == null ? proposalVersion : proposalVersion.trim();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(final String userName) {
		this.userName = userName == null ? userName : userName.trim();
	}

	public String getAdvertisorContact() {
		return advertisorContact;
	}

	public void setAdvertisorContact(final String advertisorContact) {
		this.advertisorContact = advertisorContact == null ? advertisorContact : advertisorContact.trim();
	}

	public String getCriticality() {
		return criticality;
	}

	public void setCriticality(final String criticality) {
		this.criticality = criticality == null ? criticality : criticality.trim();
	}

	public String getProposalStatus() {
		return proposalStatus;
	}

	public void setProposalStatus(final String proposalStatus) {
		this.proposalStatus = proposalStatus == null ? proposalStatus : proposalStatus.trim();
	}

	public String getAssignedBy() {
		return assignedBy;
	}

	public void setAssignedBy(final String assignedBy) {
		this.assignedBy = assignedBy == null ? assignedBy : assignedBy.trim();
	}

	public String getSosOrderId() {
		return sosOrderId;
	}

	public void setSosOrderId(final String sosOrderId) {
		this.sosOrderId = sosOrderId == null ? sosOrderId : sosOrderId.trim();
	}

	@Override
	public Proposal populate(final Proposal bean) {
		bean.setVersion(super.getVersion());
		bean.setSpecialAdvertiser(this.isSpecialAdvertiser());
		bean.setId(this.getId());
		if(StringUtils.isNotBlank(this.getSosOrderId())){
			bean.setSosOrderId(Long.valueOf(this.getSosOrderId()));
		}
		bean.setName(this.getProposalName());
		bean.setCampaignName(this.getCampaignName());
		bean.setAccountManager(this.getAccountManager());
		if (StringUtils.isNotBlank(this.getCriticality())) {
			bean.setCriticality(Criticality.findByName(this.getCriticality()));
		}
		bean.setGlobal(true);
		bean.setSosSalesCategoryId(Long.parseLong(StringUtils.isNotBlank(this.getSalescategory()) ? this.getSalescategory() : ZERO));

		bean.setSosAdvertiserId(Long.parseLong(StringUtils.isNotBlank(this.getAdvertiserName()) ? this.getAdvertiserName() : ZERO));
		bean.setSosAdvertiserName(this.getNewAdvertiserName());
		bean.setSosAgencyId(Long.parseLong(StringUtils.isNotBlank(this.getAgencyName()) ? this.getAgencyName() : ZERO));
		bean.setDateRequested(DateUtil.parseToDateTime(this.getRequestedOn()));
		bean.setDueDate(DateUtil.parseToDateTime(this.getDueOn()));
		if (StringUtils.isNotBlank(this.getStartDate())) {
			bean.setStartDate(DateUtil.parseToDate(this.getStartDate()));
		} else {
			bean.setStartDate(null);
		}
		if (StringUtils.isNotBlank(this.getEndDate())) {
			bean.setEndDate(DateUtil.parseToDate(this.getEndDate()));
		} else {
			bean.setEndDate(null);
		}
		bean.setPriceType(this.getPriceType());
		if (bean.getId() == 0) {
			bean.setProposalStatus(ProposalStatus.INPROGRESS);
			final Set<ProposalOption> optionSet = new LinkedHashSet<ProposalOption>();
			final ProposalOption option = new ProposalOption();
			option.setProposal(bean);
			option.setDefaultOption(true);
			option.setName(ConstantStrings.OPTION_NAME + 1);
			option.setOptionNo(1);
			option.setId(0);
			optionSet.add(option);
			bean.setProposalOptions(optionSet);

			final ProposalVersion proposalVersion = new ProposalVersion();
			proposalVersion.setProposalVersion(1L);
			proposalVersion.setProposalOption(option);
			final Set<ProposalVersion> proposalVersions = new HashSet<ProposalVersion>();
			proposalVersions.add(proposalVersion);
			option.setProposalVersions(proposalVersions);
		}
		final Set<CampaignObjective> campaignObjectives = new HashSet<CampaignObjective>();
		if (this.campaignObjective != null) {
			for (long objectiveId : this.campaignObjective) {
				final CampaignObjective objective = new CampaignObjective();
				objective.setCmpObjId(objectiveId);
				campaignObjectives.add(objective);
			}
		}
		bean.setCampaignObjectiveSet(campaignObjectives);
		bean.setCurrencyId(this.getCurrencyId());
		bean.setConversionRate(Double.valueOf(this.getConversionRate()));
		bean.setConversionRate_Euro(this.getConversionRate_Euro());
		bean.setConversionRate_Yen(this.getConversionRate_Yen());
		bean.setCurrency(this.getCurrency());
		bean.setReservationEmails(this.getReservationEmails());
		bean.setAgencyMargin(NumberUtil.doubleValue(this.getAgencyMargin()));
		bean.setWithPricing(this.isWithPricing());
		return bean;
	}

	@Override
	public void populateForm(final Proposal bean) {
		super.setVersion(bean.getVersion());
		this.setActive(bean.isActive());
		this.setSpecialAdvertiser(bean.isSpecialAdvertiser());
		this.setProposalName(bean.getName());
		this.setProposalTitle(bean.getName());
		this.setCampaignName(bean.getCampaignName());
		this.setAccountManager(bean.getAccountManager());
		if(bean.getSosOrderId() != null){
			this.setSosOrderId(String.valueOf(bean.getSosOrderId()));
		}
		this.setId(bean.getId());
		if (bean.getCriticality() != null) {
			this.setCriticality(bean.getCriticality().name());
			this.setCriticalityDisplayName(bean.getCriticality().getDisplayName());
		}
		if (bean.getProposalStatus() != null) {
			this.setProposalStatus(bean.getProposalStatus().name());
			this.setProposalStatusDisplayName(bean.getProposalStatus().getDisplayName());
		}

		this.setPriceType(bean.getPriceType());
		if (bean.getSosAdvertiserId() == null || bean.getSosAdvertiserId() == 0) {
			this.setAdvertiserName(ZERO);
			this.setNewAdvertiserName(bean.getSosAdvertiserName());
		} else {
			this.setAdvertiserName(bean.getSosAdvertiserId().toString());
		}
		this.setAgencyName((bean.getSosAgencyId() == null) ? ZERO : bean.getSosAgencyId().toString());
		this.setUserName((bean.getAssignedUser() == null) ? ConstantStrings.EMPTY_STRING : bean.getAssignedUser().getFullName());
		this.setSalescategory((bean.getSosSalesCategoryId() == null) ? ZERO : bean.getSosSalesCategoryId().toString());

		// Setting remaining budget
		final double remainingBudget = NumberUtil.doubleValue(this.getBudget()) - NumberUtil.doubleValue(this.getOfferedBudget());
		this.setRemBudget(NumberUtil.formatDouble(remainingBudget, true));

		if (bean.getAssignedByUser() != null) {
			this.setAssignedBy(bean.getAssignedByUser().getFullName());
		}

		if (bean.getDateRequested() != null) {
			this.setRequestedOn(DateUtil.getGuiDateTimeString(bean.getDateRequested().getTime()));
		}
		if(bean.getPricingSubmittedDate() != null) {
			this.setPricingSubmittedDate(DateUtil.getGuiDateTimeString(bean.getPricingSubmittedDate().getTime()));
		}
		if (bean.getDueDate() != null) {
			this.setDueOn(DateUtil.getGuiDateTimeString(bean.getDueDate().getTime()));
		}
		if (bean.getStartDate() == null) {
			this.setStartDate(ConstantStrings.EMPTY_STRING);
		} else {
			this.setStartDate(DateUtil.getGuiDateString(bean.getStartDate()));
		}

		if (bean.getEndDate() == null) {
			this.setEndDate(ConstantStrings.EMPTY_STRING);
		} else {
			this.setEndDate(DateUtil.getGuiDateString(bean.getEndDate()));
		}

		final Calendar cal = Calendar.getInstance();
		cal.setTime(bean.getDueDate());
		cal.add(Calendar.DATE, 61);
		this.expired = String.valueOf(cal.getTime().before(DateUtil.getCurrentDate()));
		this.setCurrencyId(bean.getCurrencyId());
		this.setConversionRate(String.valueOf(bean.getConversionRate()));
		this.setConversionRate_Euro(bean.getConversionRate_Euro());
		this.setConversionRate_Yen(bean.getConversionRate_Yen());
		this.setCurrency(bean.getCurrency());
		this.setReservationEmails(bean.getReservationEmails());
		this.setAgencyMargin((bean.getAgencyMargin() != null) ? NumberUtil.formatDouble(bean.getAgencyMargin(), true) : ConstantStrings.EMPTY_STRING);
		this.setSalesforceID(bean.getSalesforceID());
		
		if(bean.getLastProposedDate() != null){
			this.setLastProposedDate(DateUtil.getGuiDateTimeString(bean.getLastProposedDate()));
		}else{
			this.setLastProposedDate(ConstantStrings.EMPTY_STRING);
		}
		this.setWithPricing(bean.isWithPricing());
	}

	public void populateOptionVersionData(final ProposalOption option, final ProposalVersion proposalVersion) {
		this.setOptionId(option.getId());
		this.setOptionName(option.getName());
		this.setBudget((option.getBudget() == null) ? DECIMAL_ZERO : NumberUtil.formatDouble(option.getBudget(), true));
		this.setProposalVersion(String.valueOf(proposalVersion.getProposalVersion()));
		this.setPreviousProposalVersion(String.valueOf(proposalVersion.getProposalVersion()));
		this.setOfferedBudget(proposalVersion.getOfferedBudget() == null ? DECIMAL_ZERO : NumberUtil.formatDouble(proposalVersion.getOfferedBudget(), true));
		this.setNetCpm(proposalVersion.getEffectiveCpm() == null ? DECIMAL_ZERO : NumberUtil.formatDouble(proposalVersion.getEffectiveCpm(), true));
		this.setNetImpressions(proposalVersion.getImpressions() == null ? ZERO : NumberUtil.formatLong(proposalVersion.getImpressions(), true));
		// Setting remaining budget
		final double remainingBudget = NumberUtil.doubleValue(this.getBudget()) - NumberUtil.doubleValue(this.getOfferedBudget());
		this.setRemBudget(NumberUtil.formatDouble(remainingBudget, true));
		this.setProposalVersion(String.valueOf(proposalVersion.getProposalVersion()));
		this.setModifiedBy(proposalVersion.getModifiedBy());
		this.setCreatedBy(proposalVersion.getCreatedBy());
		this.setModifiedOn(proposalVersion.getModifiedDate() == null ? ConstantStrings.EMPTY_STRING : DateUtil.getGuiDateString(proposalVersion.getModifiedDate()));
		this.setCreatedAt(proposalVersion.getCreatedDate() == null ? ConstantStrings.EMPTY_STRING : DateUtil.getGuiDateString(proposalVersion.getCreatedDate()));
		this.setThresholdLimit(option.getThresholdLimit() == null ? ConstantStrings.EMPTY_STRING : NumberUtil.formatDouble(option.getThresholdLimit(), true));
		this.setLastPricingReviewedDate(option.getLastReviewedDate() == null ? ConstantStrings.EMPTY_STRING : DateUtil.getGuiDateTimeString(option.getLastReviewedDate()));
	}

	public String getProposalTemplate() {
		return proposalTemplate;
	}

	public void setProposalTemplate(final String proposalTemplate) {
		this.proposalTemplate = proposalTemplate == null ? proposalTemplate : proposalTemplate.trim();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(final String userId) {
		this.userId = userId == null ? userId : userId.trim();
	}

	public String getMyProposal() {
		return myProposal;
	}

	public void setMyProposal(final String myProposal) {
		this.myProposal = myProposal == null ? myProposal : myProposal.trim();
	}

	public String getNetCpm() {
		return netCpm;
	}

	public void setNetCpm(final String netCpm) {
		this.netCpm = netCpm == null ? netCpm : netCpm.trim();
	}

	public String getNetImpressions() {
		return netImpressions;
	}

	public void setNetImpressions(final String netImpressions) {
		this.netImpressions = netImpressions == null ? netImpressions : netImpressions.trim();
	}

	public String getNewAdvertiserName() {
		return newAdvertiserName;
	}

	public void setNewAdvertiserName(final String newAdvertiserName) {
		this.newAdvertiserName = newAdvertiserName == null ? newAdvertiserName : newAdvertiserName.trim();
	}

	public String getPreviousProposalVersion() {
		return previousProposalVersion;
	}

	public void setPreviousProposalVersion(final String previousProposalVersion) {
		this.previousProposalVersion = previousProposalVersion == null ? previousProposalVersion : previousProposalVersion.trim();
	}

	public String getExpired() {
		return Boolean.valueOf(expired) ? "Yes" : "No";
	}

	public void setExpired(final String expired) {
		this.expired = expired == null ? expired : expired.trim();
	}

	public long[] getCampaignObjective() {
		return campaignObjective;
	}

	public void setCampaignObjective(final long[] campaignObjective) {
		this.campaignObjective = campaignObjective;
	}

	public long[] getAvailCampaignObjective() {
		return availCampaignObjective;
	}

	public void setAvailCampaignObjective(final long[] availCampaignObjective) {
		this.availCampaignObjective = availCampaignObjective;
	}

	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(final String priceType) {
		this.priceType = priceType == null ? priceType : priceType.trim();
	}

	public String getSalesCategoryDisplayName() {
		return salesCategoryDisplayName;
	}

	public void setSalesCategoryDisplayName(final String salesCategoryDisplayName) {
		this.salesCategoryDisplayName = salesCategoryDisplayName == null ? salesCategoryDisplayName : salesCategoryDisplayName.trim();
	}

	public String getCriticalityDisplayName() {
		return criticalityDisplayName;
	}

	public void setCriticalityDisplayName(final String criticalityDisplayName) {
		this.criticalityDisplayName = criticalityDisplayName == null ? criticalityDisplayName : criticalityDisplayName.trim();
	}

	public String getProposalStatusDisplayName() {
		return proposalStatusDisplayName;
	}

	public void setProposalStatusDisplayName(final String statusDisplayName) {
		this.proposalStatusDisplayName = statusDisplayName == null ? statusDisplayName : statusDisplayName.trim();
	}

	public String getOfferedBudget() {
		return offeredBudget;
	}

	public void setOfferedBudget(final String offeredBudget) {
		this.offeredBudget = offeredBudget == null ? offeredBudget : offeredBudget.trim();
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(final String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(final String endDate) {
		this.endDate = endDate;
	}

	/**
	 * @param summaryAdvertiserName the summaryAdvertiserName to set
	 */
	public void setSummaryAdvertiserName(final String summaryAdvertiserName) {
		this.summaryAdvertiserName = summaryAdvertiserName == null ? summaryAdvertiserName : summaryAdvertiserName.trim();
	}

	/**
	 * @return the summaryAdvertiserName
	 */
	public String getSummaryAdvertiserName() {
		return summaryAdvertiserName;
	}

	/**
	 * @param summaryAgencyName the summaryAgencyName to set
	 */
	public void setSummaryAgencyName(final String summaryAgencyName) {
		this.summaryAgencyName = summaryAgencyName == null ? summaryAgencyName : summaryAgencyName.trim();
	}

	/**
	 * @return the summaryAgencyName
	 */
	public String getSummaryAgencyName() {
		return summaryAgencyName;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(final String discount) {
		this.discount = discount;
	}

	public String getMaxOptionNo() {
		return maxOptionNo;
	}

	public void setMaxOptionNo(final String maxOptionNo) {
		this.maxOptionNo = maxOptionNo;
	}

	public String getDefaultOption() {
		return defaultOption;
	}

	public void setDefaultOption(final String defaultOption) {
		this.defaultOption = defaultOption;
	}

	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(final String currency) {
		this.currency = currency;
	}

	/**
	 * @return the conversionRate
	 */
	public String getConversionRate() {
		return conversionRate;
	}

	/**
	 * @param conversionRate the conversionRate to set
	 */
	public void setConversionRate(final String conversionRate) {
		this.conversionRate = conversionRate;
	}

	/**
	 * @return the currencyId
	 */
	public Long getCurrencyId() {
		return currencyId;
	}

	/**
	 * @param currencyId the currencyId to set
	 */
	public void setCurrencyId(final Long currencyId) {
		this.currencyId = currencyId;
	}

	public String getAccountManager() {
		return accountManager;
	}

	public void setAccountManager(final String accountManager) {
		this.accountManager = accountManager;
	}

	public String getRemBudget() {
		return remBudget;
	}

	public void setRemBudget(final String remBudget) {
		this.remBudget = remBudget;
	}

	public String getReservationEmails() {
		return reservationEmails;
	}

	public void setReservationEmails(final String reservationEmails) {
		this.reservationEmails = reservationEmails == null ? reservationEmails : reservationEmails.trim();
	}

	public String getProposalTitle() {
		return proposalTitle;
	}

	public void setProposalTitle(final String proposalTitle) {
		this.proposalTitle = proposalTitle;
	}

	public String getNoOfReservations() {
		return noOfReservations;
	}

	public void setNoOfReservations(final String noOfReservations) {
		this.noOfReservations = noOfReservations;
	}

	public String getSosURL() {
		return sosURL;
	}

	public void setSosURL(final String sosURL) {
		this.sosURL = sosURL;
	}

	public String getThresholdLimit() {
		return thresholdLimit;
	}

	public void setThresholdLimit(final String thresholdLimit) {
		this.thresholdLimit = thresholdLimit;
	}

	public String getQualifingLineItemInvestment() {
		return qualifingLineItemInvestment;
	}

	public void setQualifingLineItemInvestment(final String qualifingLineItemInvestment) {
		this.qualifingLineItemInvestment = qualifingLineItemInvestment;
	}

	public String getOptionThresholdValueString() {
		return optionThresholdValueString;
	}

	public void setOptionThresholdValueString(final String optionThresholdValueString) {
		this.optionThresholdValueString = optionThresholdValueString;
	}

	public void setSalesforceID(final String salesforceID) {
		this.salesforceID = salesforceID;
	}

	public String getSalesforceID() {
		return salesforceID;
	}

	public String getSalesForceSearchKey() {
		return salesForceSearchKey;
	}

	public void setSalesForceSearchKey(String salesForceSearchKey) {
		this.salesForceSearchKey = salesForceSearchKey;
	}

	public String getLastPricingReviewedDate() {
		return lastPricingReviewedDate;
	}

	public void setLastPricingReviewedDate(String lastPricingReviewedDate) {
		this.lastPricingReviewedDate = lastPricingReviewedDate;
	}

	public String getLastProposedDate() {
		return lastProposedDate;
	}

	public void setLastProposedDate(String lastProposedDate) {
		this.lastProposedDate = lastProposedDate;
	}

	public boolean isWithPricing() {
		return withPricing;
	}

	public void setWithPricing(boolean withPricing) {
		this.withPricing = withPricing;
	}

	public Double getConversionRate_Euro() {
		return conversionRate_Euro;
	}

	public void setConversionRate_Euro(Double conversionRateEuro) {
		conversionRate_Euro = conversionRateEuro;
	}

	public Double getConversionRate_Yen() {
		return conversionRate_Yen;
	}

	public void setConversionRate_Yen(Double conversionRateYen) {
		conversionRate_Yen = conversionRateYen;
	}

	public String getPricingSubmittedDate() {
		return pricingSubmittedDate;
	}

	public void setPricingSubmittedDate(String pricingSubmittedDate) {
		this.pricingSubmittedDate = pricingSubmittedDate;
	}

	public String getIsOptionViewable() {
		return isOptionViewable;
	}

	public void setIsOptionViewable(String isOptionViewable) {
		this.isOptionViewable = isOptionViewable;
	}
	
	

	
}