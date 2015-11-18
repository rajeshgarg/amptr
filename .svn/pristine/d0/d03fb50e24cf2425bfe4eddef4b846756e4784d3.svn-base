/**
 * 
 */
package com.nyt.mpt.form;

import java.io.Serializable;

import com.nyt.mpt.domain.AudienceTargetType;
import com.nyt.mpt.domain.Tier;
import com.nyt.mpt.domain.TierPremium;
import com.nyt.mpt.util.ConstantStrings;

/**
 * @author garima.garg
 * 
 */
public class TierPremiumForm extends BaseForm<TierPremium> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private Long premiumTierId;

	private String targetTypeId;

	private String targetName;

	private String targetElementIds;

	private String targetElements;

	private String premium;

	private String[] targetTypeElements;

	private String hidTargetTypeId;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(final Long id) {
		this.id = id;
	}

	/**
	 * @return the premiumTierId
	 */
	public Long getPremiumTierId() {
		return premiumTierId;
	}

	/**
	 * @param premiumTierId the premiumTierId to set
	 */
	public void setPremiumTierId(final Long premiumTierId) {
		this.premiumTierId = premiumTierId;
	}

	/**
	 * @return the targetTypeId
	 */
	public String getTargetTypeId() {
		return targetTypeId;
	}

	/**
	 * @param targetTypeId the targetTypeId to set
	 */
	public void setTargetTypeId(final String targetTypeId) {
		this.targetTypeId = targetTypeId;
	}

	/**
	 * @return the targetName
	 */
	public String getTargetName() {
		return targetName;
	}

	/**
	 * @param targetName the targetName to set
	 */
	public void setTargetName(final String targetName) {
		this.targetName = targetName;
	}

	/**
	 * @return the targetElementIds
	 */
	public String getTargetElementIds() {
		return targetElementIds;
	}

	/**
	 * @param targetElementIds the targetElementIds to set
	 */
	public void setTargetElementIds(final String targetElementIds) {
		this.targetElementIds = targetElementIds;
	}

	/**
	 * @return the targetElements
	 */
	public String getTargetElements() {
		return targetElements;
	}

	/**
	 * @param targetElements the targetElements to set
	 */
	public void setTargetElements(final String targetElements) {
		this.targetElements = targetElements;
	}

	/**
	 * @return the premium
	 */
	public String getPremium() {
		return premium;
	}

	/**
	 * @param premium the premium to set
	 */
	public void setPremium(final String premium) {
		this.premium = premium;
	}

	/**
	 * @return the targetTypeElements
	 */
	public String[] getTargetTypeElements() {
		return targetTypeElements;
	}

	/**
	 * @param targetTypeElements the targetTypeElements to set
	 */
	public void setTargetTypeElements(final String[] targetTypeElements) {
		this.targetTypeElements = targetTypeElements;
	}

	/**
	 * @return the hidTargetTypeId
	 */
	public String getHidTargetTypeId() {
		return hidTargetTypeId;
	}

	/**
	 * @param hidTargetTypeId the hidTargetTypeId to set
	 */
	public void setHidTargetTypeId(final String hidTargetTypeId) {
		this.hidTargetTypeId = hidTargetTypeId;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public void populateForm(final TierPremium bean) {
		this.setId(bean.getId());
		this.setPremiumTierId(bean.getTierObj().getTierId());
		this.setTargetTypeId(String.valueOf(bean.getTargetType().getSosAudienceTargetTypeId()));
		this.setHidTargetTypeId(String.valueOf(bean.getTargetType()));
		this.setPremium(String.valueOf(bean.getPremium()));
		this.setTargetElementIds(bean.getTarTypeElementId());
		this.setTargetName(bean.getTargetType().getName());
	}

	@Override
	public TierPremium populate(final TierPremium bean) {
		bean.setId(this.getId());
		final AudienceTargetType targetType = new AudienceTargetType();
		targetType.setSosAudienceTargetTypeId(Long.valueOf(this.getHidTargetTypeId()));
		bean.setTargetType(targetType);
		final Tier tier = new Tier();
		tier.setTierId(this.getPremiumTierId());
		bean.setTierObj(tier);
		bean.setPremium(Double.valueOf(this.getPremium()));

		String targetElementIds = ConstantStrings.EMPTY_STRING;
		if (this.getTargetTypeElements().length != 0) {
			final String[] targetTypeElementIds = this.getTargetTypeElements();
			for (String elementId : targetTypeElementIds) {
				targetElementIds += elementId.trim() + ConstantStrings.COMMA;
			}
			targetElementIds = targetElementIds.trim().substring(0, targetElementIds.trim().length() - 1);
		}
		bean.setTarTypeElementId(targetElementIds);
		return bean;
	}
}
