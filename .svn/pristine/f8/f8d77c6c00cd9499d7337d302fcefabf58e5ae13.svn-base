/**
 * 
 */
package com.nyt.mpt.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.nyt.mpt.dao.ITierDAO;
import com.nyt.mpt.domain.Tier;
import com.nyt.mpt.domain.TierPremium;
import com.nyt.mpt.domain.TierSectionAssoc;
import com.nyt.mpt.service.ITierService;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.filter.FilterCriteria;

/**
 * @author garima.garg
 *
 */
public class TierService implements ITierService {

	private static final Logger LOGGER = Logger.getLogger(TierService.class);

	private ITierDAO tierDAO;

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ITierService#getFilteredTierCount(com.nyt.mpt.util.filter.FilterCriteria)
	 */
	@Override
	public int getFilteredTierCount(final FilterCriteria filterCriteria) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("fetching count of tier from db");
		}
		return tierDAO.getFilteredTierCount(filterCriteria);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ITierService#getFilteredTierList(com.nyt.mpt.util.filter.FilterCriteria, com.nyt.mpt.util.PaginationCriteria, com.nyt.mpt.util.SortingCriteria)
	 */
	@Override
	public List<Tier> getFilteredTierList(final FilterCriteria filterCriteria,
			final PaginationCriteria pageCriteria, final SortingCriteria sortCriteria) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("fetching List of tier from db");
		}
		return tierDAO.getFilteredTierList(filterCriteria, pageCriteria, sortCriteria);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ITierService#saveAttribute(com.nyt.mpt.domain.Tier)
	 */
	@Override
	public Tier saveTier(final Tier tier) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Saving tier with Id: " + tier.getTierId());
		}
		/* Update */
		if (tier.getTierId() != null) {
			Tier tierObj = getTierById(tier.getTierId());
			
			if (tierObj.getTierSectionAssocLst() != null && !tierObj.getTierSectionAssocLst().isEmpty()) {
				for (TierSectionAssoc tierSectionAssoc : tierObj.getTierSectionAssocLst()) {
					tierDAO.deleteTierSection(tierSectionAssoc);
				}
			}
			tierObj.setTierSectionAssocLst(tier.getTierSectionAssocLst());
			tierObj.setTierLevel(tier.getTierLevel());
			tierObj.setTierName(tier.getTierName());
			return tierDAO.saveTier(tierObj);
		} else {
			return tierDAO.saveTier(tier);
		}
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ITierService#deleteTier(com.nyt.mpt.domain.Tier)
	 */
	@Override
	public Long deleteTier(final long tierId) {
		final Tier tierDB = getTierById(tierId);
		tierDB.setActive(false);
		return tierDAO.saveTier(tierDB).getTierId();
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ITierService#isDuplicateTierName(java.lang.String, long)
	 */
	@Override
	public boolean isDuplicateTierName(final String tierName, final Long tierID) {
		return tierDAO.isDuplicateTierName(tierName, tierID);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ITierService#isDuplicateTierLevel(java.lang.Long, java.lang.Long)
	 */
	@Override
	public boolean isDuplicateTierLevel(final long tierLevel, final Long tierID) {
		return tierDAO.isDuplicateTierLevel(tierLevel, tierID);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ITierService#getFilteredPremiumCount(com.nyt.mpt.util.filter.FilterCriteria)
	 */
	@Override
	public int getFilteredTierPremiumCount(long tierId, FilterCriteria filterCriteria) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching filtered premium count with filterCriteria: " + filterCriteria);
		}
		return tierDAO.getFilteredTierPremiumCount(tierId, filterCriteria);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ITierService#getFilteredTierPremiumList(java.lang.Long, com.nyt.mpt.util.filter.FilterCriteria, com.nyt.mpt.util.PaginationCriteria, com.nyt.mpt.util.SortingCriteria)
	 */
	@Override
	public List<TierPremium> getFilteredTierPremiumList(Long tierId, FilterCriteria filterCriteria, PaginationCriteria pageCriteria, SortingCriteria sortCriteria) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching filtered premium list with filterCriteria: " + filterCriteria);
		}
		return tierDAO.getFilteredTierPremiumList(tierId, filterCriteria, pageCriteria, sortCriteria);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ITierService#saveTierPremium(com.nyt.mpt.domain.TierPremium, java.lang.Long)
	 */
	@Override
	public Tier saveTierPremium(TierPremium tierPremium, long tierId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Saving premium with Id:" + tierPremium.getId());
		}
		final Tier tier = getTierById(tierId);
		if (tierPremium.getId() != null) {
			for (TierPremium tierPremiumDB : tier.getTierPremiumLst()) {
				if (tierPremiumDB.getId().equals(tierPremium.getId())) {
					tierPremiumDB.setPremium(tierPremium.getPremium());
					tierPremiumDB.setTarTypeElementId(tierPremium.getTarTypeElementId());
					break;
				}
			}
		} else {
			tierPremium.setTierObj(tier);
			List<TierPremium> tierPremLst = tier.getTierPremiumLst();
			tierPremLst.add(tierPremium);
			tier.setTierPremiumLst(tierPremLst);
		}
		return tierDAO.saveTier(tier);
	}

	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ITierService#isDuplicatePremiumElement(java.lang.Long, java.lang.String, java.lang.String[], java.lang.Long)
	 */
	public boolean  isDuplicatePremiumElement(TierPremium tierPremiumBean , StringBuilder duplicateElementsStr ){
		boolean result = false;
		String[] targetElementIds;
		if (StringUtils.isNotBlank(tierPremiumBean.getTarTypeElementId())) {
			targetElementIds = tierPremiumBean.getTarTypeElementId().split(ConstantStrings.COMMA);
		} else {
			targetElementIds = new String[0];
		}
		List<String> tarElements = new ArrayList<String>(targetElementIds.length);
		for (String tarEle : targetElementIds) {
			tarElements.add(tarEle.trim());
		}

		List<TierPremium> premiumList = tierDAO.getPremiumListByTierId(tierPremiumBean.getTierObj().getTierId());
		if (premiumList != null && !premiumList.isEmpty()) {
			for (TierPremium tierPremium : premiumList) {
				if (!(tierPremium.getId().equals(tierPremiumBean.getId()))
						&& (tierPremium.getTargetType().getSosAudienceTargetTypeId() == tierPremiumBean.getTargetType().getSosAudienceTargetTypeId())) {
					String premiumTargetElement = tierPremium.getTarTypeElementId();
					if (StringUtils.isBlank(premiumTargetElement) && targetElementIds.length == 0) {
						duplicateElementsStr.append("Default");
						return true;
					} else if (StringUtils.isNotBlank(premiumTargetElement)) {
						String[] targetElementsArr = premiumTargetElement.split(ConstantStrings.COMMA);
						for (String tarEle : targetElementsArr) {
							if (tarElements.contains(tarEle.trim())) {
								duplicateElementsStr.append(tarEle.trim() + ", ");
								result = true;
							}
						}
					}
				}
			}
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ITierService#deleteTierPremium(com.nyt.mpt.domain.TierPremium)
	 */
	@Override
	public Long deleteTierPremium(final long premiumId) {
		TierPremium tierPremium = getPremiumById(premiumId);
		tierPremium.setActive(false);
		return tierDAO.saveTierPremium(tierPremium).getId();
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IRateProfileService#createRateProfileCloneByID(java.lang.Long, java.lang.Long, java.lang.String)
	 */
	@Override
	public void createClonePremiumForTier(final long sourceTierId, final long targetTierId) {
		final List<TierPremium> premiumList = tierDAO.getPremiumListByTierId(targetTierId);
		for (TierPremium tierPremium : premiumList) {
			tierPremium.setActive(false);
			tierDAO.saveTierPremium(tierPremium);
		}

		final List<TierPremium> sourcePremiumList = tierDAO.getPremiumListByTierId(sourceTierId);
		Tier tier = getTierById(targetTierId);
		List<TierPremium> tierPremLst = tier.getTierPremiumLst();

		for (TierPremium tierPremiumDb : sourcePremiumList) {
			TierPremium tierPremium = new TierPremium();
			tierPremium.setTierObj(tier);
			tierPremium.setPremium(tierPremiumDb.getPremium());
			tierPremium.setTargetType(tierPremiumDb.getTargetType());
			tierPremium.setTarTypeElementId(tierPremiumDb.getTarTypeElementId());
			tierPremLst.add(tierPremium);
		}

		tier.setTierPremiumLst(tierPremLst);
		tierDAO.saveTier(tier);
	}
	
	@Override
	public Tier getTierById(final long tierId){
		return tierDAO.getTierById(tierId);
	}
	
	public TierPremium getPremiumById(final long premiumId){
		return tierDAO.getPremiumById(premiumId);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ITierService#getFilteredTierPremiumList(java.lang.Long, com.nyt.mpt.util.filter.FilterCriteria, com.nyt.mpt.util.PaginationCriteria, com.nyt.mpt.util.SortingCriteria)
	 */
	@Override
	public List<TierPremium> getPremiumListByTierId(long tierId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching premium list of a tier, with tier Id : " + tierId);
		}
		return tierDAO.getPremiumListByTierId(tierId);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ITierService#getTierSectionAssocList(java.util.Set)
	 */
	@Override
	public List<Tier> getTierSectionAssocList(final Set<Long> sectionIDSet) {
		return tierDAO.getTierSectionAssocList(sectionIDSet);
	}
	
	public void setTierDAO(final ITierDAO tierDAO) {
		this.tierDAO = tierDAO;
	}
}
