/**
 * 
 */
package com.nyt.mpt.form;

import java.io.Serializable;

import com.nyt.mpt.domain.Proposal;
import com.nyt.mpt.util.Constants;

/**
 * This <code>ReservationForm</code> contains all the attributes related to Reservation
 * 
 * @author garima.garg
 */
public class ReservationForm extends BaseForm<Proposal> implements Serializable {

	private static final long serialVersionUID = 1L;

	private String proposalId;

	private String proposalName;

	private String salesCategoryId;

	private String salesCategoryName;

	private String sosOrderId;

	private String advertiserId;

	private String[] reservationStatus;

	private String displayReservationStatus;

	private String expiryDate;

	private String startDate;

	private String endDate;

	private String salesTargetId;

	private String salesTarget;
	
	private String[] salesTargetIdLst;

	private String productId;
	
	private String[] productIdLst;

	private String productName;

	private Long lineItemId;

	private Double sor;

	private String flightDate;

	private String myReservation;

	private String advertiserName;

	private String optionName;

	private String targetingString;

	private String daysToExpire;

	private String lineItemTargetingData;

	private String proposalAssignedToUserId;

	private String sosURL;

	private String proposalStatus;

	private String sosProductId;

	private String sosSalesTargetId;

	private String sosTarTypeId;

	private String lineItemData;

	private String targetingData;

	private String sosLineItemId;
	
	private Long sosProductClassId;
	
	private String product_active = Constants.YES;

	private String salesTarget_active = Constants.YES;

	public String getTargetingData() {
		return targetingData;
	}

	public void setTargetingData(final String targetingData) {
		this.targetingData = targetingData;
	}

	public String getLineItemData() {
		return lineItemData;
	}

	public void setLineItemData(final String lineItemData) {
		this.lineItemData = lineItemData;
	}

	public String getSosTarTypeId() {
		return sosTarTypeId;
	}

	public void setSosTarTypeId(final String sosTarTypeId) {
		this.sosTarTypeId = sosTarTypeId;
	}

	public String getSosProductId() {
		return sosProductId;
	}

	public void setSosProductId(final String sosProductId) {
		this.sosProductId = sosProductId;
	}

	public String getSosSalesTargetId() {
		return sosSalesTargetId;
	}

	public void setSosSalesTargetId(final String sosSalesTargetId) {
		this.sosSalesTargetId = sosSalesTargetId;
	}

	public String getProposalId() {
		return proposalId;
	}

	public void setProposalId(final String proposalId) {
		this.proposalId = proposalId;
	}

	public String getProposalName() {
		return proposalName;
	}

	public void setProposalName(final String proposalName) {
		this.proposalName = proposalName;
	}

	public String getSalesCategoryId() {
		return salesCategoryId;
	}

	public void setSalesCategoryId(final String salesCategoryId) {
		this.salesCategoryId = salesCategoryId;
	}

	public String getSalesCategoryName() {
		return salesCategoryName;
	}

	public void setSalesCategoryName(final String salesCategoryName) {
		this.salesCategoryName = salesCategoryName;
	}

	public String getSosOrderId() {
		return sosOrderId;
	}

	public void setSosOrderId(final String sosOrderId) {
		this.sosOrderId = sosOrderId;
	}

	public String getAdvertiserId() {
		return advertiserId;
	}

	public void setAdvertiserId(final String advertiserId) {
		this.advertiserId = advertiserId;
	}

	public String[] getReservationStatus() {
		return reservationStatus;
	}

	public void setReservationStatus(final String[] reservationStatus) {
		this.reservationStatus = reservationStatus;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(final String expiryDate) {
		this.expiryDate = expiryDate;
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

	public String getSalesTargetId() {
		return salesTargetId;
	}

	public void setSalesTargetId(final String salesTargetId) {
		this.salesTargetId = salesTargetId;
	}

	public String getSalesTarget() {
		return salesTarget;
	}

	public void setSalesTarget(final String salesTarget) {
		this.salesTarget = salesTarget;
	}

	public String[] getSalesTargetIdLst() {
		return salesTargetIdLst;
	}

	public void setSalesTargetIdLst(String[] salesTargetIdLst) {
		this.salesTargetIdLst = salesTargetIdLst;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(final String productId) {
		this.productId = productId;
	}

	public String[] getProductIdLst() {
		return productIdLst;
	}

	public void setProductIdLst(String[] productIdLst) {
		this.productIdLst = productIdLst;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(final String productName) {
		this.productName = productName;
	}

	public Long getLineItemId() {
		return lineItemId;
	}

	public void setLineItemId(final Long lineItemId) {
		this.lineItemId = lineItemId;
	}

	public Double getSor() {
		return sor;
	}

	public void setSor(final Double sor) {
		this.sor = sor;
	}

	public String getFlightDate() {
		return flightDate;
	}

	public void setFlightDate(final String flightDate) {
		this.flightDate = flightDate;
	}

	public String getMyReservation() {
		return myReservation;
	}

	public void setMyReservation(final String myReservation) {
		this.myReservation = myReservation;
	}

	public String getAdvertiserName() {
		return advertiserName;
	}

	public void setAdvertiserName(final String advertiserName) {
		this.advertiserName = advertiserName;
	}

	public String getOptionName() {
		return optionName;
	}

	public void setOptionName(final String optionName) {
		this.optionName = optionName;
	}

	public String getTargetingString() {
		return targetingString;
	}

	public void setTargetingString(final String targetingString) {
		this.targetingString = targetingString;
	}

	public String getDaysToExpire() {
		return daysToExpire;
	}

	public void setDaysToExpire(final String daysToExpire) {
		this.daysToExpire = daysToExpire;
	}

	public String getLineItemTargetingData() {
		return lineItemTargetingData;
	}

	public void setLineItemTargetingData(final String lineItemTargetingData) {
		this.lineItemTargetingData = lineItemTargetingData;
	}

	public String getProposalAssignedToUserId() {
		return proposalAssignedToUserId;
	}

	public void setProposalAssignedToUserId(final String proposalAssignedToUserId) {
		this.proposalAssignedToUserId = proposalAssignedToUserId;
	}

	public String getProposalStatus() {
		return proposalStatus;
	}

	public void setProposalStatus(final String proposalStatus) {
		this.proposalStatus = proposalStatus;
	}

	@Override
	public void populateForm(final Proposal bean) {
	}

	@Override
	public Proposal populate(final Proposal bean) {
		return null;
	}

	public String getSosURL() {
		return sosURL;
	}

	public void setSosURL(final String sosURL) {
		this.sosURL = sosURL;
	}

	public String getDisplayReservationStatus() {
		return displayReservationStatus;
	}

	public void setDisplayReservationStatus(final String displayReservationStatus) {
		this.displayReservationStatus = displayReservationStatus;
	}

	public String getSosLineItemId() {
		return sosLineItemId;
	}

	public void setSosLineItemId(String sosLineItemId) {
		this.sosLineItemId = sosLineItemId;
	}

	public String getProduct_active() {
		return product_active;
	}

	public void setProduct_active(String productActive) {
		product_active = productActive;
	}

	public String getSalesTarget_active() {
		return salesTarget_active;
	}

	public void setSalesTarget_active(String salesTargetActive) {
		salesTarget_active = salesTargetActive;
	}

	public Long getSosProductClassId() {
		return sosProductClassId;
	}

	public void setSosProductClassId(Long sosProductClassId) {
		this.sosProductClassId = sosProductClassId;
	}
	
}
