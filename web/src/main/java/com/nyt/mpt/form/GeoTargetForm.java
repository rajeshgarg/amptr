/**
 * 
 */
package com.nyt.mpt.form;

import org.apache.commons.lang.StringUtils;

import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.LineItemTarget;
import com.nyt.mpt.util.ConstantStrings;

/**
 * This GeoTarget form bean is used for Target information
 * 
 * @author amandeep.singh
 * 
 */
public class GeoTargetForm extends BaseForm<LineItemTarget> {

	private String id;

	private String proposalId;

	private String primaryId;// Used for package

	private String lineItemID;

	private String sosTarTypeId;

	private String sosTarTypeElement;

	private String sosTarTypeName;

	private String sosTarTypeElementName;

	private String type;

	private String premium;

	private String appliedPremium;

	private String basePrice;

	private String pricingRuleName;

	private String baseRuleId;

	private boolean active = true;

	private String proposalLineItemAssocId;

	private String pricingRuleID;

	private String operation;

	private String negation;

	private String segmentLevel;

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id != null ? id.trim() : id;
	}

	public String getProposalId() {
		return proposalId;
	}

	public void setProposalId(final String proposalId) {
		this.proposalId = proposalId != null ? proposalId.trim() : proposalId;
	}

	public String getPrimaryId() {
		return primaryId;
	}

	public void setPrimaryId(final String primaryId) {
		this.primaryId = primaryId != null ? primaryId.trim() : primaryId;
	}

	public String getLineItemID() {
		return lineItemID;
	}

	public void setLineItemID(final String lineItemId) {
		this.lineItemID = lineItemId != null ? lineItemId.trim() : lineItemId;
	}

	public String getSosTarTypeId() {
		return sosTarTypeId;
	}

	public void setSosTarTypeId(final String sosTarTypeId) {
		this.sosTarTypeId = sosTarTypeId != null ? sosTarTypeId.trim() : sosTarTypeId;
	}

	public String getSosTarTypeElement() {
		return sosTarTypeElement;
	}

	public void setSosTarTypeElement(final String sosTarTypeElement) {
		this.sosTarTypeElement = sosTarTypeElement != null ? sosTarTypeElement.trim() : sosTarTypeElement;
	}

	public String getSosTarTypeName() {
		return sosTarTypeName;
	}

	public void setSosTarTypeName(final String sosTarTypeName) {
		this.sosTarTypeName = sosTarTypeName != null ? sosTarTypeName.trim() : sosTarTypeName;
	}

	public String getSosTarTypeElementName() {
		return sosTarTypeElementName;
	}

	public void setSosTarTypeElementName(final String sosTarTypeElementName) {
		this.sosTarTypeElementName = sosTarTypeElementName != null ? sosTarTypeElementName.trim() : sosTarTypeElementName;
	}

	public String getType() {
		return type;
	}

	public void setType(final String type) {
		this.type = type != null ? type.trim() : type;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(final boolean active) {
		this.active = active;
	}

	public String getPremium() {
		if (StringUtils.isBlank(premium)) {
			premium = ConstantStrings.NA;
		}
		return premium;
	}

	public void setPremium(final String premium) {
		this.premium = premium != null ? premium.trim() : premium;
	}

	public String getBasePrice() {
		if (StringUtils.isBlank(basePrice) || StringUtils.equals(basePrice, "0.0")) {
			basePrice = "Not Defined";
		}
		return basePrice;
	}

	public void setBasePrice(final String basePrice) {
		this.basePrice = basePrice != null ? basePrice.trim() : basePrice;
	}

	public String getPricingRuleName() {
		if (StringUtils.isBlank(pricingRuleName)) {
			pricingRuleName = "Not Defined";
		}
		return pricingRuleName;
	}

	public void setPricingRuleName(final String pricingRuleName) {
		this.pricingRuleName = pricingRuleName != null ? pricingRuleName.trim() : pricingRuleName;
	}

	public String getBaseRuleId() {
		return baseRuleId;
	}

	public void setBaseRuleId(final String pricingBaseRuleId) {
		this.baseRuleId = pricingBaseRuleId != null ? pricingBaseRuleId.trim() : pricingBaseRuleId;
	}

	public String getAppliedPremium() {
		return appliedPremium;
	}

	public void setAppliedPremium(final String maxPremium) {
		this.appliedPremium = maxPremium != null ? maxPremium.trim() : maxPremium;
	}

	public String getProposalLineItemAssocId() {
		return proposalLineItemAssocId;
	}

	public void setProposalLineItemAssocId(final String proposalLineItemAssocId) {
		this.proposalLineItemAssocId = proposalLineItemAssocId != null ? proposalLineItemAssocId.trim() : proposalLineItemAssocId;
	}

	@Override
	public LineItemTarget populate(final LineItemTarget bean) {
		bean.setActive(this.isActive());
		bean.setId(Long.parseLong((this.getId() != null && !this.getId().equals(ConstantStrings.EMPTY_STRING)) ? this.getId() : "0"));
		final LineItem lineItem = new LineItem();
		lineItem.setLineItemID(Long.valueOf(this.getLineItemID()));
		bean.setProposalLineItem(lineItem);
		bean.setSosTarTypeId(Long.parseLong((this.getSosTarTypeId() != null && !this.getSosTarTypeId().equals(ConstantStrings.EMPTY_STRING)) ? this.getSosTarTypeId() : "0"));
		bean.setSosTarTypeElementId(this.getSosTarTypeElement());
		return bean;
	}

	@Override
	public void populateForm(final LineItemTarget bean) {
		this.setActive(bean.isActive());
		this.setId(bean.getId().toString());
		this.setLineItemID(bean.getProposalLineItem().getLineItemID().toString());
		this.setSosTarTypeId(bean.getSosTarTypeId().toString());
		this.setSosTarTypeElement(bean.getSosTarTypeElementId());
		this.setOperation(bean.getOperation());
		this.setNegation(bean.getNegation());
		this.setSegmentLevel(bean.getSegmentLevel());
	}

	public void setPricingRuleID(final String pricingRuleID) {
		this.pricingRuleID = pricingRuleID == null ? pricingRuleID : pricingRuleID.trim();
	}

	public String getPricingRuleID() {
		return pricingRuleID;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(final String operation) {
		this.operation = operation;
	}

	public String getNegation() {
		return negation;
	}

	public void setNegation(final String negation) {
		this.negation = negation;
	}

	public String getSegmentLevel() {
		return segmentLevel;
	}

	public void setSegmentLevel(final String segmentLevel) {
		this.segmentLevel = segmentLevel;
	}
}
