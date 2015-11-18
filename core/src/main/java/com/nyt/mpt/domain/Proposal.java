/**
 *
 */
package com.nyt.mpt.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import com.nyt.mpt.util.ChangeTrackedDomain;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.DateUtil;
import com.nyt.mpt.util.enums.Criticality;
import com.nyt.mpt.util.enums.ProposalStatus;
import com.sforce.soap.enterprise.sobject.Attachment;

/**
 * This <code>Proposal</code> class includes all the attributes related to
 * Proposal and their getter and setter. The attributes have mapping with
 * <code>MP_PROPOSALS</code> table in the AMPT database
 * 
 * @author surendra.singh
 */

@Entity
@Table(name = "MP_PROPOSALS")
public class Proposal extends ChangeTrackedDomain implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;

	private int version;

	private String name;

	private String campaignName;

	private boolean global;

	private Criticality criticality;

	private ProposalStatus proposalStatus;

	private boolean active = true;

	private Long clonedFromProposalVersion;

	private Long sosAdvertiserId;

	private String sosAdvertiserName;

	private Long sosAgencyId;

	private Long sosSalesCategoryId;

	private User assignedUser;

	private User assignedByUser;

	private Set<ProposalOption> proposalOptions = new LinkedHashSet<ProposalOption>();

	private Set<CampaignObjective> campaignObjectiveSet;

	private Date dueDate;

	private Date dateRequested;

	private Date startDate;

	private Date endDate;

	private String priceType;

	private String accountManager;

	private Long sosOrderId;

	private Long currencyId;

	private Double conversionRate;

	private String currency;

	private String reservationEmails;

	private Double agencyMargin;

	private String salesforceID;

	private boolean specialAdvertiser = false;

	private String sfRevisionType;

	private List<Attachment> attachments;

	private Date lastProposedDate;

	private boolean withPricing = false;
	
	private Double conversionRate_Euro;
	
	private Double conversionRate_Yen;

	private Date pricingSubmittedDate;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "MP_PROPOSAL_CAMPAIGN_OBJECTIVE", joinColumns = { @JoinColumn(name = "PROPOSAL_ID") }, inverseJoinColumns = { @JoinColumn(name = "CAMPAIGN_OBJECTIVE_ID") })
	public Set<CampaignObjective> getCampaignObjectiveSet() {
		return campaignObjectiveSet;
	}

	public void setCampaignObjectiveSet(Set<CampaignObjective> campaignObjectiveSet) {
		this.campaignObjectiveSet = campaignObjectiveSet;
	}

	public void addNewOption(final ProposalOption proposalOption) {
		proposalOptions.add(proposalOption);
		proposalOption.setProposal(this);
	}

	@Transient
	public ProposalOption getDefaultOption() {
		if (!proposalOptions.isEmpty()) {
			ProposalOption propOption = null;
			for (ProposalOption option : proposalOptions) {
				if (option.isDefaultOption()) {
					propOption = option;
					break;
				}
			}
			return propOption;
		} else {
			return null;
		}
	}

	@Id
	@Column(name = "ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROPOSALS_SEQUENCE")
	@SequenceGenerator(name = "PROPOSALS_SEQUENCE", sequenceName = "MP_PROPOSALS_SEQUENCE", allocationSize = 1)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Version
	@Column(name = "VERSION", nullable = false)
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Column(name = "NAME", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "CAMPAIGN_NAME", nullable = false)
	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	@Column(name = "IS_GLOBAL")
	public boolean isGlobal() {
		return global;
	}

	public void setGlobal(boolean global) {
		this.global = global;
	}

	@Column(name = "IS_ACTIVE")
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Column(name = "CLONED_FROM_PROPOSAL_VERSION", nullable = true, updatable = false)
	public Long getClonedFromProposalVersion() {
		return clonedFromProposalVersion;
	}

	public void setClonedFromProposalVersion(Long clonedFromProposalVersion) {
		this.clonedFromProposalVersion = clonedFromProposalVersion;
	}

	@Column(name = "SOS_ADVERTISER_ID", nullable = true)
	public Long getSosAdvertiserId() {
		return sosAdvertiserId;
	}

	public void setSosAdvertiserId(Long sosAdvertiserId) {
		this.sosAdvertiserId = sosAdvertiserId;
	}

	@Column(name = "SOS_AGENCY_ID", nullable = true)
	public Long getSosAgencyId() {
		return sosAgencyId;
	}

	public void setSosAgencyId(Long sosAgencyId) {
		this.sosAgencyId = sosAgencyId;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(insertable = true, updatable = true, nullable = true, name = "ASSIGN_TO_USER")
	public User getAssignedUser() {
		return assignedUser;
	}

	public void setAssignedUser(User assignedUser) {
		this.assignedUser = assignedUser;
	}

	@OneToMany(mappedBy = "proposal", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@OrderBy("id DESC")
	public Set<ProposalOption> getProposalOptions() {
		return proposalOptions;
	}

	public void setProposalOptions(Set<ProposalOption> proposalOptions) {
		this.proposalOptions = proposalOptions;
	}

	@Column(name = "PRIORITY", columnDefinition = "string", insertable = true, updatable = true, nullable = false)
	@Type(type = "com.nyt.mpt.domain.GenericEnumUserType", parameters = { @Parameter(name = "enumClass", value = "com.nyt.mpt.util.enums.Criticality"), @Parameter(name = "identifierMethod", value = "name"),
			@Parameter(name = "valueOfMethod", value = "findByName") })
	public Criticality getCriticality() {
		return criticality;
	}

	public void setCriticality(Criticality criticality) {
		this.criticality = criticality;
	}

	@Column(name = "CURRENT_STATUS", columnDefinition = "string", insertable = true, updatable = true, nullable = false)
	@Type(type = "com.nyt.mpt.domain.GenericEnumUserType", parameters = { @Parameter(name = "enumClass", value = "com.nyt.mpt.util.enums.ProposalStatus"), @Parameter(name = "identifierMethod", value = "name"),
			@Parameter(name = "valueOfMethod", value = "findByName") })
	public ProposalStatus getProposalStatus() {
		return proposalStatus;
	}

	public void setProposalStatus(ProposalStatus proposalStatus) {
		this.proposalStatus = proposalStatus;
	}

	@Column(name = "SOS_SALES_CATEGORY_ID", nullable = true)
	public Long getSosSalesCategoryId() {
		return sosSalesCategoryId;
	}

	public void setSosSalesCategoryId(Long sosSalesCategoryId) {
		this.sosSalesCategoryId = sosSalesCategoryId;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(insertable = true, updatable = true, nullable = false, name = "ASSIGN_BY_USER")
	public User getAssignedByUser() {
		return assignedByUser;
	}

	public void setAssignedByUser(User assignedByUser) {
		this.assignedByUser = assignedByUser;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DUE_DATE", nullable = true)
	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_REQUESTED", nullable = true)
	public Date getDateRequested() {
		return dateRequested;
	}

	public void setDateRequested(Date dateRequested) {
		this.dateRequested = dateRequested;
	}

	@Column(name = "SOS_ADVERTISER_NAME", nullable = true)
	public String getSosAdvertiserName() {
		return sosAdvertiserName;
	}

	public void setSosAdvertiserName(String sosAdvertiserName) {
		this.sosAdvertiserName = sosAdvertiserName;
	}

	@Column(name = "PRICE_TYPE", nullable = true)
	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}

	@Column(name = "SOSORDERID")
	public Long getSosOrderId() {
		return sosOrderId;
	}

	public void setSosOrderId(Long sosOrderId) {
		this.sosOrderId = sosOrderId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_DATE")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_DATE")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "ACCOUNT_MANAGER")
	public String getAccountManager() {
		return accountManager;
	}

	public void setAccountManager(String accountManager) {
		this.accountManager = accountManager;
	}

	/**
	 * This method is used to export flight range
	 * 
	 * @return
	 */
	@Transient
	public String getFlightRange() {
		if (this.startDate == null || this.endDate == null) {
			return ConstantStrings.EMPTY_STRING;
		} else {
			return DateUtil.getGuiDateString(this.startDate) + ConstantStrings.SPACE + ConstantStrings.HYPHEN + ConstantStrings.SPACE + DateUtil.getGuiDateString(this.endDate);
		}
	}

	/**
	 * This method is used to export proposal proposed date
	 * 
	 * @return
	 */
	@Transient
	public String getExportedDate() {
		return DateUtil.getGuiDateString(DateUtil.getCurrentDate());
	}

	@Column(name = "CURRENCYID")
	public Long getCurrencyId() {
		return currencyId;
	}

	/**
	 * @param currencyId
	 *            the currencyId to set
	 */
	public void setCurrencyId(Long currencyId) {
		this.currencyId = currencyId;
	}

	@Column(name = "CONVERSIONRATE")
	public Double getConversionRate() {
		return conversionRate;
	}

	public void setConversionRate(Double conversionRate) {
		this.conversionRate = conversionRate;
	}

	@Column(name = "CURRENCY_CODE")
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Column(name = "RESERVATION_EMAILS")
	public String getReservationEmails() {
		return reservationEmails;
	}

	public void setReservationEmails(String reservationEmails) {
		this.reservationEmails = reservationEmails;
	}

	@Column(name = "AGENCY_MARGIN")
	public Double getAgencyMargin() {
		return agencyMargin;
	}

	public void setAgencyMargin(Double agencyMargin) {
		this.agencyMargin = agencyMargin;
	}

	@Column(name = "SALESFORCE_ID")
	public String getSalesforceID() {
		return salesforceID;
	}

	public void setSalesforceID(String salesforceID) {
		this.salesforceID = salesforceID;
	}

	@Column(name = "IS_SPECIAL_ADVERTISER")
	public boolean isSpecialAdvertiser() {
		return specialAdvertiser;
	}

	public void setSpecialAdvertiser(boolean specialAdvertiser) {
		this.specialAdvertiser = specialAdvertiser;
	}

	@Column(name = "SF_REVISION_TYPE")
	public String getSfRevisionType() {
		return sfRevisionType;
	}

	public void setSfRevisionType(String sfRevisionType) {
		this.sfRevisionType = sfRevisionType;
	}

	@Transient
	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_PROPOSED_DATE", nullable = true)
	public Date getLastProposedDate() {
		return lastProposedDate;
	}

	public void setLastProposedDate(Date lastProposedDate) {
		this.lastProposedDate = lastProposedDate;
	}

	@Column(name = "IS_WITH_PRICING")
	public boolean isWithPricing() {
		return withPricing;
	}

	public void setWithPricing(boolean withPricing) {
		this.withPricing = withPricing;
	}

	@Column(name = "CONVERSIONRATE_EURO")
	public Double getConversionRate_Euro() {
		return conversionRate_Euro;
	}

	public void setConversionRate_Euro(Double conversionRateEuro) {
		conversionRate_Euro = conversionRateEuro;
	}

	@Column(name = "CONVERSIONRATE_YEN")
	public Double getConversionRate_Yen() {
		return conversionRate_Yen;
	}

	public void setConversionRate_Yen(Double conversionRateYen) {
		conversionRate_Yen = conversionRateYen;
	}
	
	@Column(name = "PRICING_SUBMITTED_DATE")
	public Date getPricingSubmittedDate() {
		return pricingSubmittedDate;
	}

	public void setPricingSubmittedDate(Date pricingSubmittedDate) {
		this.pricingSubmittedDate = pricingSubmittedDate;
	}
}
