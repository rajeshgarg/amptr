/**
 * 
 */
package com.nyt.mpt.form;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.nyt.mpt.domain.Tier;
import com.nyt.mpt.domain.TierSectionAssoc;
import com.nyt.mpt.util.ConstantStrings;

/**
 * @author Gurditta.Garg
 *
 */
public class TierForm extends BaseForm<Tier> implements Serializable {

	private static final long serialVersionUID = 1L;

	private String tierName;

	private Long tierId;

	private String level;

	private String sectionNames;

	private String[] selectedSectionIds;

	private String[] unselectedSectionIds;

	/**
	 * @return the tierName
	 */
	public String getTierName() {
		return tierName;
	}

	/**
	 * @param tierName the tierName to set
	 */
	public void setTierName(final String tierName) {
		this.tierName = tierName != null ? tierName.trim() : tierName;
	}

	/**
	 * @return the tierId
	 */
	public Long getTierId() {
		return tierId;
	}

	/**
	 * @param tierId the tierId to set
	 */
	public void setTierId(final Long tierId) {
		this.tierId = tierId;
	}

	/**
	 * @return the level
	 */
	public String getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(final String level) {
		this.level = level != null ? level.trim() : level;
	}

	/**
	 * @return the sectionName
	 */
	public String getSectionNames() {
		return sectionNames;
	}

	/**
	 * @param sectionName the sectionName to set
	 */
	public void setSectionNames(final String sectionNames) {
		this.sectionNames = sectionNames != null ? sectionNames.trim() : sectionNames;
	}

	/**
	 * @return the selectedSectionIds
	 */
	public String[] getSelectedSectionIds() {
		return selectedSectionIds;
	}

	/**
	 * @param selectedSectionIds the selectedSectionIds to set
	 */
	public void setSelectedSectionIds(final String[] selectedSectionIds) {
		this.selectedSectionIds = selectedSectionIds;
	}

	/**
	 * @return the unselectedSectionIds
	 */
	public String[] getUnselectedSectionIds() {
		return unselectedSectionIds;
	}

	/**
	 * @param unselectedSectionIds the unselectedSectionIds to set
	 */
	public void setUnselectedSectionIds(final String[] unselectedSectionIds) {
		this.unselectedSectionIds = unselectedSectionIds;
	}

	@Override
	public void populateForm(final Tier bean) {
		String tierSections = ConstantStrings.EMPTY_STRING;
		this.setTierId(bean.getTierId());
		this.setTierName(bean.getTierName());
		this.setLevel(String.valueOf(bean.getTierLevel()));
		if (bean.getTierSectionAssocLst() != null && !bean.getTierSectionAssocLst().isEmpty()) {
			for (TierSectionAssoc tierSectionAssoc : bean.getTierSectionAssocLst()) {
				tierSections += tierSectionAssoc.getSectionName() + ", ";
			}
			tierSections = tierSections.trim().substring(0, tierSections.trim().length() - 1);
		}
		this.setSectionNames(tierSections);
	}

	@Override
	public Tier populate(final Tier bean) {
		bean.setTierId((this.getTierId() == 0) ? null : this.getTierId());
		bean.setTierName(this.getTierName());
		if (!StringUtils.isBlank(this.getLevel())) {
			bean.setTierLevel(Long.parseLong(this.getLevel()));
		} else {
			bean.setTierLevel(null);
		}
		return bean;
	}
}
