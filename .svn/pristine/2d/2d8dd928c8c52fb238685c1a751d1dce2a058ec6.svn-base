/**
 * 
 */
package com.nyt.mpt.domain.sos;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.nyt.mpt.util.DateUtil;

/**
 * This <code>SalesOrder</code> class includes all the attributes related to
 * SalesOrder and their getter and setter. The attributes have mapping with
 * <code>SALES_ORDER</code> table in the SOS database
 * 
 * @author surendra.singh
 */
@Entity
@Table(name = "SALES_ORDER")
@Where(clause = "STATUS_ID IN (1, 2, 3, 5, 7, 9, 10, 12, 15)")
public class SalesOrder {

	private long salesOrderId;
	private String campaignName;
	private Date startDate;
	private Date endDate;
	private Customer advertiser;
	private Set<OrderLineItem> lineItem = new LinkedHashSet<OrderLineItem>();
	private Long territoryId;
	private Long billToAddressId;
	private Long billingMethodId;
	private Long paymentMethodId;
	private Set<OrderContactAssociation> contacts = new LinkedHashSet<OrderContactAssociation>();
	private Date orderDate = DateUtil.getCurrentDate();
	private Long currencyId;
	private Double agencyDiscount;
	private SosUser owner;
	private Double discount = 0D;
	private Long version = 0L;
	private Long renewal = 0L;
	private Double adjustment = 0D;
	private Double downPayment = 0D;
	private Long tcTypeId = 0L;
	private Long georgianId = 0L;
	private Long invoiceId = 5L;
	private Long prePayId = 0L;
	private Long statusId = 1L;
	private Long modifiedBy;
	private Date modifiedDate = DateUtil.getCurrentDate();
	private Long divisionId = 2L;
	private Long orderClassId = 1L;
	private Long agencyId;
	private Long BillingCustomerId;
	private Long outClause = 30L;
	private boolean isViewable;

	@Id
	@Column(name = "SALESORDER_ID", updatable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORDER_SEQUENCE")
	@SequenceGenerator(name = "ORDER_SEQUENCE", sequenceName = "S_SALESORDER_ID", allocationSize = 1)
	public long getSalesOrderId() {
		return salesOrderId;
	}

	public void setSalesOrderId(long salesOrderId) {
		this.salesOrderId = salesOrderId;
	}

	@Column(name = "CAMPAIGN_NAME", updatable = false)
	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_DATE", updatable = false)
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_DATE", updatable = false)
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@OneToMany(mappedBy = "salesOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	public Set<OrderLineItem> getLineItem() {
		return lineItem;
	}

	public void setLineItem(Set<OrderLineItem> lineItem) {
		this.lineItem = lineItem;
	}

	@ManyToOne
	@JoinColumn(name = "ADVERTISER_ID", updatable = false)
	public Customer getAdvertiser() {
		return advertiser;
	}

	public void setAdvertiser(Customer advertiser) {
		this.advertiser = advertiser;
	}

	@Column(name = "TERRITORY_ID", updatable = false)
	public Long getTerritoryId() {
		return territoryId;
	}

	public void setTerritoryId(Long territoryId) {
		this.territoryId = territoryId;
	}

	@Column(name = "BILL_TO_ADDRESS_ID", updatable = false)
	public Long getBillToAddressId() {
		return billToAddressId;
	}

	public void setBillToAddressId(Long billToAddressId) {
		this.billToAddressId = billToAddressId;
	}

	@Column(name = "BILLING_METHOD_ID", updatable = false)
	public Long getBillingMethodId() {
		return billingMethodId;
	}

	public void setBillingMethodId(Long billingMethodId) {
		this.billingMethodId = billingMethodId;
	}

	@Column(name = "PAYMENT_METHOD_ID", updatable = false)
	public Long getPaymentMethodId() {
		return paymentMethodId;
	}

	public void setPaymentMethodId(Long paymentMethodId) {
		this.paymentMethodId = paymentMethodId;
	}

	@OneToMany(mappedBy = "id.order", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	public Set<OrderContactAssociation> getContacts() {
		return contacts;
	}

	public void setContacts(Set<OrderContactAssociation> contacts) {
		this.contacts = contacts;
	}

	@Column(name = "ORDER_DATE", updatable = false)
	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	@Column(name = "CURRENCY_ID", updatable = false)
	public Long getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Long currencyId) {
		this.currencyId = currencyId;
	}

	@Column(name = "AGENCY_DISCOUNT", updatable = false)
	public Double getAgencyDiscount() {
		return agencyDiscount;
	}

	public void setAgencyDiscount(Double agencyDiscount) {
		this.agencyDiscount = agencyDiscount;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false, name = "OWNER_ID")
	public SosUser getOwner() {
		return owner;
	}

	public void setOwner(SosUser owner) {
		this.owner = owner;
	}

	@Column(name = "DISCOUNT", updatable = false)
	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	@Column(name = "VERSION", updatable = false)
	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	@Column(name = "RENEWAL", updatable = false)
	public Long getRenewal() {
		return renewal;
	}

	public void setRenewal(Long renewal) {
		this.renewal = renewal;
	}

	@Column(name = "ADJUSTMENT", updatable = false)
	public Double getAdjustment() {
		return adjustment;
	}

	public void setAdjustment(Double adjustment) {
		this.adjustment = adjustment;
	}

	@Column(name = "DOWNPAYMENT", updatable = false)
	public Double getDownPayment() {
		return downPayment;
	}

	public void setDownPayment(Double downPayment) {
		this.downPayment = downPayment;
	}

	@Column(name = "TC_TYPE_ID", updatable = false)
	public Long getTcTypeId() {
		return tcTypeId;
	}

	public void setTcTypeId(Long tcTypeId) {
		this.tcTypeId = tcTypeId;
	}

	@Column(name = "GEOREGION_ID", updatable = false)
	public Long getGeorgianId() {
		return georgianId;
	}

	public void setGeorgianId(Long georgianId) {
		this.georgianId = georgianId;
	}

	@Column(name = "INVOICEMETHOD_ID", updatable = false)
	public Long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}

	@Column(name = "PRE_PAY", updatable = false)
	public Long getPrePayId() {
		return prePayId;
	}

	public void setPrePayId(Long prePayId) {
		this.prePayId = prePayId;
	}

	@Column(name = "STATUS_ID", updatable = false)
	public Long getStatusId() {
		return statusId;
	}

	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

	@Column(name = "LAST_MODIFIED_BY", updatable = false)
	public Long getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Long modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	@Column(name = "LAST_MOD_DATE", updatable = false)
	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@Column(name = "DIVISION_ID", updatable = false)
	public Long getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(Long divisionId) {
		this.divisionId = divisionId;
	}

	@Column(name = "ORDERCLASS_ID", updatable = false)
	public Long getOrderClassId() {
		return orderClassId;
	}

	public void setOrderClassId(Long orderClassId) {
		this.orderClassId = orderClassId;
	}

	@Column(name = "AGENCY_ID", updatable = false)
	public Long getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(Long agencyId) {
		this.agencyId = agencyId;
	}

	@Column(name = "BILLING_CUSTOMER_ID", updatable = false)
	public Long getBillingCustomerId() {
		return BillingCustomerId;
	}

	public void setBillingCustomerId(Long billingCustomerId) {
		BillingCustomerId = billingCustomerId;
	}

	@Column(name = "OUT_CLAUSE", updatable = false)
	public Long getOutClause() {
		return outClause;
	}

	public void setOutClause(Long outClause) {
		this.outClause = outClause;
	}

	@Column(name= "IS_VIEWABLE")
	public boolean isViewable() {
		return isViewable;
	}

	public void setViewable(boolean isViewable) {
		this.isViewable = isViewable;
	}
	
}
