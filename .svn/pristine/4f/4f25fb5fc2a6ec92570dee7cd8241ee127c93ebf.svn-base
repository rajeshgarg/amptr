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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Where;

import com.nyt.mpt.util.DateUtil;

/**
 * This <code>OrderLineItem</code> class includes all the attributes related to
 * Order LineItem and their getter and setter. The attributes have mapping with
 * <code>ORDER_LINEITEM</code> table in the SOS database
 * 
 * @author surendra.singh
 */
@Entity
@Table(name = "ORDER_LINEITEM")
public class OrderLineItem {

	private long lineItemId;
	private long productId;
	private long salesTargetId;
	private String lineItemName;
	private Long targetTypeId;
	private Long totalQtyExpected;
	private Long dailyImpExpected;
	private Double shareOfReservation;
	private Date startDate;
	private Date endDate;
	private SalesOrder salesOrder;
	private Set<LineItemTargeting> lineItemTargeting = new LinkedHashSet<LineItemTargeting>();
	private Long unitId = 1L;// CPM
	private Long totalQtyActual = 0L;
	private Long priorityId;
	private Long clusterId;
	private Long availStatus = 0L;
	private Long checkAvails = 0L;
	private Double grossRate;
	private Double rate;
	private Long tearCreate = 0L;
	private Long creativeSourceId = 1L;
	private Long glTypeId = 1L;
	private Long evgMonths = 0L;
	private Double vendorDlvryDscrp = 0D;
	private Date createDate = DateUtil.getCurrentDate();
	private Long splitFlag = 0L;
	private Long modifiedBy;
	private Date modifiedDate = DateUtil.getCurrentDate();
	private Long vendorId;
	private Long servingVendorId;
	private Long parentLineItemId;
	private String clusterTargets;
	private Long frequencyCapType;
	private Long frequencyCapValue;
	private Boolean bonusFlag = false;
	private Long revenueTypeId;
	private String comments;
	private Integer viewabilityLevel;
	
	@Id
	@Column(name = "LINEITEM_ID", updatable = false)
	public long getLineItemId() {
		return lineItemId;
	}

	public void setLineItemId(long lineItemId) {
		this.lineItemId = lineItemId;
	}

	@Column(name = "PRODUCT_ID", updatable = false)
	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	@Column(name = "SALESTARGET_ID", updatable = false)
	public long getSalesTargetId() {
		return salesTargetId;
	}

	public void setSalesTargetId(long salesTargetId) {
		this.salesTargetId = salesTargetId;
	}

	@Column(name = "NAME", updatable = false)
	public String getLineItemName() {
		return lineItemName;
	}

	public void setLineItemName(String lineItemName) {
		this.lineItemName = lineItemName;
	}

	@Column(name = "TARGETTYPE_ID", updatable = false)
	public Long getTargetTypeId() {
		return targetTypeId;
	}

	public void setTargetTypeId(Long targetTypeId) {
		this.targetTypeId = targetTypeId;
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

	@Column(name = "SHARE_OF_RESERVATION", updatable = false)
	public Double getShareOfReservation() {
		return shareOfReservation;
	}

	public void setShareOfReservation(Double shareOfReservation) {
		this.shareOfReservation = shareOfReservation;
	}

	@ManyToOne
	@JoinColumn(name = "SALESORDER_ID", nullable = false, updatable = false)
	public SalesOrder getSalesOrder() {
		return salesOrder;
	}

	public void setSalesOrder(SalesOrder salesOrder) {
		this.salesOrder = salesOrder;
	}

	@Where(clause = "STATUS IN (1, 3)")
	@OneToMany(mappedBy = "orderLineItem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public Set<LineItemTargeting> getLineItemTargeting() {
		return lineItemTargeting;
	}

	public void setLineItemTargeting(Set<LineItemTargeting> lineItemTargeting) {
		this.lineItemTargeting = lineItemTargeting;
	}

	@Column(name = "TOTAL_QTY_EXPECTED", updatable = false)
	public Long getTotalQtyExpected() {
		return totalQtyExpected;
	}

	public void setTotalQtyExpected(Long totalQtyExpected) {
		this.totalQtyExpected = totalQtyExpected;
	}

	@Column(name = "DAILY_IMP_EXPECTED", updatable = false)
	public Long getDailyImpExpected() {
		return dailyImpExpected;
	}

	public void setDailyImpExpected(Long dailyImpExpected) {
		this.dailyImpExpected = dailyImpExpected;
	}

	@Column(name = "UNIT_ID", updatable = false)
	public Long getUnitId() {
		return unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

	@Column(name = "TOTAL_QTY_ACTUAL", updatable = false)
	public Long getTotalQtyActual() {
		return totalQtyActual;
	}

	public void setTotalQtyActual(Long totalQtyActual) {
		this.totalQtyActual = totalQtyActual;
	}

	@Column(name = "PRIORITY_ID", updatable = false)
	public Long getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Long priorityId) {
		this.priorityId = priorityId;
	}

	@Column(name = "CLUSTER_ID", updatable = false)
	public Long getClusterId() {
		return clusterId;
	}

	public void setClusterId(Long clusterId) {
		this.clusterId = clusterId;
	}

	@Column(name = "AVAILS_STATUS", updatable = false)
	public Long getAvailStatus() {
		return availStatus;
	}

	public void setAvailStatus(Long availStatus) {
		this.availStatus = availStatus;
	}

	@Column(name = "CHECK_AVAILS", updatable = false)
	public Long getCheckAvails() {
		return checkAvails;
	}

	public void setCheckAvails(Long checkAvails) {
		this.checkAvails = checkAvails;
	}

	@Column(name = "GROSS_RATE", updatable = false)
	public Double getGrossRate() {
		return grossRate;
	}

	public void setGrossRate(Double grossRate) {
		this.grossRate = grossRate;
	}

	@Column(name = "TEAR_CREATE", updatable = false)
	public Long getTearCreate() {
		return tearCreate;
	}

	public void setTearCreate(Long tearCreate) {
		this.tearCreate = tearCreate;
	}

	@Column(name = "CREATIVE_SOURCE_ID", updatable = false)
	public Long getCreativeSourceId() {
		return creativeSourceId;
	}

	public void setCreativeSourceId(Long creativeSourceId) {
		this.creativeSourceId = creativeSourceId;
	}

	@Column(name = "GLTYPE_ID", updatable = false)
	public Long getGlTypeId() {
		return glTypeId;
	}

	public void setGlTypeId(Long glTypeId) {
		this.glTypeId = glTypeId;
	}

	@Column(name = "EVG_MONTHS", updatable = false)
	public Long getEvgMonths() {
		return evgMonths;
	}

	public void setEvgMonths(Long evgMonths) {
		this.evgMonths = evgMonths;
	}

	@Column(name = "VENDOR_DELV_DISCREPANCY", updatable = false)
	public Double getVendorDlvryDscrp() {
		return vendorDlvryDscrp;
	}

	public void setVendorDlvryDscrp(Double vendorDlvryDscrp) {
		this.vendorDlvryDscrp = vendorDlvryDscrp;
	}

	@Column(name = "CREATE_DATE", updatable = false)
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "SPLIT_FLAG", updatable = false)
	public Long getSplitFlag() {
		return splitFlag;
	}

	public void setSplitFlag(Long splitFlag) {
		this.splitFlag = splitFlag;
	}

	@Column(name = "LAST_MOD_BY", updatable = false)
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

	@Column(name = "RATE", updatable = false)
	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	@Column(name = "VENDOR_ID", updatable = false)
	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	@Column(name = "SERVING_VENDOR_ID", updatable = false)
	public Long getServingVendorId() {
		return servingVendorId;
	}

	public void setServingVendorId(Long servingVendorId) {
		this.servingVendorId = servingVendorId;
	}

	@Column(name = "PREV_LINEITEM_REF", updatable = false)
	public Long getParentLineItemId() {
		return parentLineItemId;
	}

	public void setParentLineItemId(Long parentLineItemId) {
		this.parentLineItemId = parentLineItemId;
	}

	@Column(name = "CLUSTER_TARGETS", updatable = false)
	public String getClusterTargets() {
		return clusterTargets;
	}

	public void setClusterTargets(String clusterTargets) {
		this.clusterTargets = clusterTargets;
	}

	@Column(name = "LIT_PERIOD", updatable = false)
	public Long getFrequencyCapType() {
		return frequencyCapType;
	}

	public void setFrequencyCapType(Long frequencyCapType) {
		this.frequencyCapType = frequencyCapType;
	}

	@Column(name = "LIT_ADS_SERVED", updatable = false)
	public Long getFrequencyCapValue() {
		return frequencyCapValue;
	}

	public void setFrequencyCapValue(Long frequencyCapValue) {
		this.frequencyCapValue = frequencyCapValue;
	}

	@Column(name = "BONUS_FLAG", updatable = false)
	public Boolean isBonusFlag() {
		return bonusFlag;
	}

	public void setBonusFlag(Boolean bonusFlag) {
		this.bonusFlag = bonusFlag;
	}

	@Column(name = "REVENUETYPE_ID", updatable = false)
	public Long getRevenueTypeId() {
		return revenueTypeId;
	}

	public void setRevenueTypeId(Long revenueTypeId) {
		this.revenueTypeId = revenueTypeId;
	}

	@Column(name = "COMMENTS", updatable = false)
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@Column(name = "VIEWABLE_BILLING_TYPE", updatable = false)
	public Integer getViewabilityLevel() {
		return viewabilityLevel;
	}

	public void setViewabilityLevel(Integer viewabilityLevel) {
		this.viewabilityLevel = viewabilityLevel;
	}
	
}
