/**
 * 
 */
package com.nyt.mpt.dao.impl;

import static com.nyt.mpt.util.ConstantStrings.UNCHECKED;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.type.StandardBasicTypes;

import com.nyt.mpt.dao.ICalendarReservationDao;
import com.nyt.mpt.dao.IProposalDAO;
import com.nyt.mpt.dao.ISalesTargetDAO;
import com.nyt.mpt.domain.CampaignObjective;
import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.LineItemExceptions;
import com.nyt.mpt.domain.LineItemReservations;
import com.nyt.mpt.domain.LineItemSalesTargetAssoc;
import com.nyt.mpt.domain.LineItemTarget;
import com.nyt.mpt.domain.Notes;
import com.nyt.mpt.domain.Proposal;
import com.nyt.mpt.domain.ProposalOption;
import com.nyt.mpt.domain.ProposalVersion;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.CustomDbOrder;
import com.nyt.mpt.util.DateUtil;
import com.nyt.mpt.util.EntityFormPropertyMap;
import com.nyt.mpt.util.NumberUtil;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.StringUtil;
import com.nyt.mpt.util.enums.ProposalStatus;
import com.nyt.mpt.util.enums.ReservationStatus;
import com.nyt.mpt.util.enums.SearchOption;
import com.nyt.mpt.util.filter.FilterCriteria;
import com.nyt.mpt.util.filter.RangeFilterCriteria;
import com.nyt.mpt.util.reservation.ReservationTO;
import com.nyt.mpt.util.reservation.SalesCalendarReservationDTO;

/**
 * @author amandeep.singh
 *
 */
public class ProposalDAO extends GenericDAOImpl implements IProposalDAO {
	
	private static final String PROPOSAL = "proposal";
	private static final String PROPOSAL_VERSION = "proposalVersion";
	private static final String PROPOSAL_VERSIONS = "proposalVersions";
	private static final String PROPOSAL_OPTIONS = "proposalOptions";
	private static final String PROPOSAL_OPTION = "proposalOption";
	private static final String USER_ROLE = "userRole";
	private static final String ASSIGNED_USER = "assignedUser";
	private static final String PROPOSAL_LINEITEM = "proposalLineItem";
	private ISalesTargetDAO salesTargetDAO;
	private static final String LINEITEM_ID = "lineItemId";
	private static final String SOS_LINEITEM_ID = "sosLineItemId";
	private static final Logger LOGGER = Logger.getLogger(ProposalDAO.class);
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#getAllProposalList()
	 */
	@Override
	@SuppressWarnings(UNCHECKED)
	public List<Proposal> getAllProposalList(){
		final DetachedCriteria criteria = DetachedCriteria.forClass(Proposal.class).addOrder(Order.asc(ConstantStrings.NAME));
		final List <ProposalStatus> statusList = new ArrayList<ProposalStatus>(6);
		statusList.add(ProposalStatus.findByName(ProposalStatus.DELETED.name()));
		statusList.add(ProposalStatus.findByName(ProposalStatus.PROPOSED.name()));
		statusList.add(ProposalStatus.findByName(ProposalStatus.EXPIRED.name()));
		statusList.add(ProposalStatus.findByName(ProposalStatus.SOLD.name()));
		statusList.add(ProposalStatus.findByName(ProposalStatus.REJECTED_BY_CLIENT.name()));
		criteria.add(Restrictions.not(Restrictions.in(EntityFormPropertyMap.DB_COLUMN_MAP.get("status"), statusList)));			
		return findByCriteria(criteria);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#getProposalList(java.util.List, com.nyt.mpt.util.PaginationCriteria, com.nyt.mpt.util.SortingCriteria)
	 */
	@Override
	@SuppressWarnings(UNCHECKED)
	public List<Proposal> getProposalList(final List<RangeFilterCriteria> criteriaLst, final PaginationCriteria pgCriteria, final SortingCriteria sortCriteria) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching Proposal List. FilterCriteriaList: " + criteriaLst);
		}
		final DetachedCriteria criteria = getCriteriaForSearchProposal(criteriaLst);
		try {
			getFilterCriteriaForSearchProposal(criteria, criteriaLst);
		} catch (NumberFormatException ex) {
			return Collections.EMPTY_LIST;
		}
		if (sortCriteria != null) {
			//update the sorting field name in sorting criteria with DB column name 
			sortCriteria.setSortingField(EntityFormPropertyMap.DB_COLUMN_MAP.get(sortCriteria.getSortingField()));
			super.addSortingCriteria(criteria, sortCriteria);
		}
		return findByCriteria(criteria, pgCriteria);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#getProposalListCount(java.util.List)
	 */
	@Override
	public int getProposalListCount(final List<RangeFilterCriteria> filterCriteriaLst) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching Search Proposal List count. FilterCriteriaList: " + filterCriteriaLst);
		}
		final DetachedCriteria criteria = getCriteriaForSearchProposal(filterCriteriaLst);
		try {
			getFilterCriteriaForSearchProposal(criteria, filterCriteriaLst);
		} catch (NumberFormatException ex) {
			return 0;
		}
		return getCount(criteria);
	}
	
	/**
	 * Return Criteria for Search Proposal without filer criteria
	 * @param filterCriteriaLst
	 * @return
	 */
	private DetachedCriteria getCriteriaForSearchProposal(final List<RangeFilterCriteria> filterCriteriaLst) {
		// This code is used to create inner join criteria with max proposal version
		final DetachedCriteria innerJoinCriteria = DetachedCriteria.forClass(ProposalVersion.class, "proposaVersion");
		innerJoinCriteria.setProjection(Property.forName("proposaVersion.proposalVersion").max());
		innerJoinCriteria.add(Property.forName("proposaVersion.proposalOption.id").eqProperty("propOption.id"));
		final DetachedCriteria criteria = DetachedCriteria.forClass(Proposal.class, "propo");
		criteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		criteria.createAlias(ASSIGNED_USER, ASSIGNED_USER, CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("assignedUser.userRoles", USER_ROLE, CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("proposalOptions", "propOption");
		criteria.add(Restrictions.eq("propOption.defaultOption",true));
		criteria.createAlias("propOption.proposalVersions", "propVersion", CriteriaSpecification.INNER_JOIN);
		// Applying inner join for proposal version
		criteria.add(Property.forName("propVersion.proposalVersion").eq(innerJoinCriteria));
		if (filterCriteriaLst != null && !filterCriteriaLst.isEmpty()) {
			for (RangeFilterCriteria rangeFilterCriteria : filterCriteriaLst) {
				if ("cmpObjective".equals(rangeFilterCriteria.getSearchField())) {
					criteria.createAlias("propo.campaignObjectiveSet", "cmpObjectiveSet");
				}
				if ("sosLineItemId".equalsIgnoreCase(rangeFilterCriteria.getSearchField())) {
					criteria.createAlias("propVersion.proposalLineItemSet", "propLineItem", CriteriaSpecification.LEFT_JOIN);
				}
			}
		}
		criteria.add(Restrictions.eq(ConstantStrings.ACTIVE, true));
		return criteria;
	}
		
	/**
	 * Return Filter Criteria for search proposal screen
	 * @param criteria
	 * @param filterCriterias
	 * @return
	 */
	private void getFilterCriteriaForSearchProposal(final DetachedCriteria criteria, final List<RangeFilterCriteria> filterCriterias) {
		if (filterCriterias != null) {
			for (RangeFilterCriteria filterCriteria : filterCriterias) {
				if ("id".equalsIgnoreCase(filterCriteria.getSearchField())) {
					criteria.add(Restrictions.ne(EntityFormPropertyMap.DB_COLUMN_MAP.get(filterCriteria.getSearchField()),
							NumberUtil.longValue(filterCriteria.getSearchString())));
				}
				if ("proposalID".equalsIgnoreCase(filterCriteria.getSearchField())) {
					try {
						criteria.add(Restrictions.eq(EntityFormPropertyMap.DB_COLUMN_MAP.get(filterCriteria.getSearchField()), Long.valueOf(filterCriteria.getSearchString())));
					} catch (NumberFormatException ex) {
						LOGGER.info("Invalid search input for " + filterCriteria.getSearchField() + " - " + filterCriteria.getSearchString());
						throw ex;
					}
				}
				if ("proposalName".equalsIgnoreCase(filterCriteria.getSearchField())) {
					criteria.add(Restrictions.ilike(EntityFormPropertyMap.DB_COLUMN_MAP.get(filterCriteria.getSearchField()),
							filterCriteria.getSearchString(), MatchMode.ANYWHERE));
				}
				if (ConstantStrings.STATUS.equalsIgnoreCase(filterCriteria.getSearchField())) {
					criteria.add(Restrictions.eq(EntityFormPropertyMap.DB_COLUMN_MAP.get(filterCriteria.getSearchField()),
							ProposalStatus.findByName(filterCriteria.getSearchString())));
				}

				if ("agencyName".equalsIgnoreCase(filterCriteria.getSearchField())) {
					criteria.add(Restrictions.eq(EntityFormPropertyMap.DB_COLUMN_MAP.get(filterCriteria.getSearchField()),
							NumberUtil.longValue(filterCriteria.getSearchString())));
				}

				if ("salescategory".equalsIgnoreCase(filterCriteria.getSearchField())) {
					criteria.add(Restrictions.eq(EntityFormPropertyMap.DB_COLUMN_MAP.get(filterCriteria.getSearchField()),
							NumberUtil.longValue(filterCriteria.getSearchString())));
				}

				if ("advertiserName".equalsIgnoreCase(filterCriteria.getSearchField())) {
					criteria.add(Restrictions.eq(EntityFormPropertyMap.DB_COLUMN_MAP.get(filterCriteria.getSearchField()),
							NumberUtil.longValue(filterCriteria.getSearchString())));
				}

				if ("cpm".equalsIgnoreCase(filterCriteria.getSearchField())) {
					if (StringUtils.isNotBlank(filterCriteria.getSearchStringFrom())) {
						criteria.add(Restrictions.ge(EntityFormPropertyMap.DB_COLUMN_MAP.get(filterCriteria.getSearchField()),
								NumberUtil.doubleValue(filterCriteria.getSearchStringFrom())));
					}

					if (StringUtils.isNotBlank(filterCriteria.getSearchStringTo())) {
						criteria.add(Restrictions.le(EntityFormPropertyMap.DB_COLUMN_MAP.get(filterCriteria.getSearchField()),
								NumberUtil.doubleValue(filterCriteria.getSearchStringTo())));
					}
				}

				if ("budget".equalsIgnoreCase(filterCriteria.getSearchField())) {
					if (StringUtils.isNotBlank(filterCriteria.getSearchStringFrom())) {
						criteria.add(Restrictions.ge(EntityFormPropertyMap.DB_COLUMN_MAP.get(filterCriteria.getSearchField()),
								NumberUtil.doubleValue(filterCriteria.getSearchStringFrom())));
					}
					if (StringUtils.isNotBlank(filterCriteria.getSearchStringTo())) {
						criteria.add(Restrictions.le(EntityFormPropertyMap.DB_COLUMN_MAP.get(filterCriteria.getSearchField()),
								NumberUtil.doubleValue(filterCriteria.getSearchStringTo())));
					}
				}

				if ("impressions".equalsIgnoreCase(filterCriteria.getSearchField())) {
					if (StringUtils.isNotBlank(filterCriteria.getSearchStringFrom())) {
						criteria.add(Restrictions.ge(EntityFormPropertyMap.DB_COLUMN_MAP.get(filterCriteria.getSearchField()),
								NumberUtil.longValue(filterCriteria.getSearchStringFrom())));
					}

					if (StringUtils.isNotBlank(filterCriteria.getSearchStringTo())) {
						criteria.add(Restrictions.le(EntityFormPropertyMap.DB_COLUMN_MAP.get(filterCriteria.getSearchField()),
								NumberUtil.longValue(filterCriteria.getSearchStringTo())));
					}
				}

				if (ASSIGNED_USER.equalsIgnoreCase(filterCriteria.getSearchField())) {
					criteria.add(Restrictions.eq(EntityFormPropertyMap.DB_COLUMN_MAP.get(filterCriteria.getSearchField()),
							NumberUtil.longValue(filterCriteria.getSearchString())));
				}

				if ("proposalStatus".equalsIgnoreCase(filterCriteria.getSearchField())) {
					final List<ProposalStatus> statusList = new ArrayList<ProposalStatus>();
					statusList.add(ProposalStatus.INPROGRESS);
					statusList.add(ProposalStatus.PROPOSED);
					statusList.add(ProposalStatus.SOLD);
					statusList.add(ProposalStatus.REVIEW);
					criteria.add(Restrictions.in(EntityFormPropertyMap.DB_COLUMN_MAP.get(filterCriteria.getSearchField()), statusList));
				}

				if ("dueDateFromTo".equalsIgnoreCase(filterCriteria.getSearchField())) {
					if (StringUtils.isNotBlank(filterCriteria.getSearchStringFrom())) {
						criteria.add(Restrictions.ge(EntityFormPropertyMap.DB_COLUMN_MAP.get(filterCriteria.getSearchField()),
								DateUtil.parseToDateTime(filterCriteria.getSearchStringFrom())));
					}
					if (StringUtils.isNotBlank(filterCriteria.getSearchStringTo())) {
						criteria.add(Restrictions.le(EntityFormPropertyMap.DB_COLUMN_MAP.get(filterCriteria.getSearchField()),
								DateUtil.parseToDateTime(filterCriteria.getSearchStringTo())));
					}
				}

				if ("requestedDateFromTo".equalsIgnoreCase(filterCriteria.getSearchField())) {
					if (StringUtils.isNotBlank(filterCriteria.getSearchStringFrom())) {
						criteria.add(Restrictions.ge(EntityFormPropertyMap.DB_COLUMN_MAP.get(filterCriteria.getSearchField()),
								DateUtil.parseToDate(filterCriteria.getSearchStringFrom())));
					}

					if (StringUtils.isNotBlank(filterCriteria.getSearchStringTo())) {
						criteria.add(Restrictions.le(EntityFormPropertyMap.DB_COLUMN_MAP.get(filterCriteria.getSearchField()),
								DateUtil.parseToDate(filterCriteria.getSearchStringTo())));
					}
				}

				if ("cmpObjective".equalsIgnoreCase(filterCriteria.getSearchField())) {
					criteria.add(Restrictions.eq(EntityFormPropertyMap.DB_COLUMN_MAP.get(filterCriteria.getSearchField()),
							NumberUtil.longValue(filterCriteria.getSearchString())));
				}

				if ("sosOrderId".equalsIgnoreCase(filterCriteria.getSearchField())) {
					criteria.add(Restrictions.eq(EntityFormPropertyMap.DB_COLUMN_MAP.get(filterCriteria.getSearchField()),
							NumberUtil.longValue(filterCriteria.getSearchString())));
				}
				if ("sosLineItemId".equalsIgnoreCase(filterCriteria.getSearchField())) {
					criteria.add(Restrictions.eq(EntityFormPropertyMap.DB_COLUMN_MAP.get(filterCriteria.getSearchField()),
							NumberUtil.longValue(filterCriteria.getSearchString())));
				}
			}
		}
		criteria.add(Restrictions.ne(EntityFormPropertyMap.DB_COLUMN_MAP.get("status"),
				ProposalStatus.findByName(ProposalStatus.DELETED.name())));
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#saveProposal(com.nyt.mpt.domain.Proposal)
	 */
	@Override
	public Proposal saveProposal(final Proposal proposal) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Saving Proposal with proposalId: " + proposal.getId());
		}
		update(proposal);
		return proposal;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#saveOption(com.nyt.mpt.domain.ProposalOption)
	 */
	@Override
	public ProposalOption saveOption(final ProposalOption option) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Saving option with id: " + option.getId());
		}
		update(option);
		return  option;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#addOption(com.nyt.mpt.domain.ProposalOption)
	 */
	@Override
	public ProposalOption addOption(final ProposalOption option) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Adding option with id: " + option.getId());
		}
		save(option);
		return  option;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#addProposal(com.nyt.mpt.domain.Proposal)
	 */
	@Override
	public long addProposal(final Proposal proposal) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Adding Proposal with id: " + proposal.getId());
		}
		save(proposal);
		return proposal.getId();
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#addOptionVersion(com.nyt.mpt.domain.ProposalVersion)
	 */
	@Override
	public ProposalVersion addOptionVersion(final ProposalVersion propVersion) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Adding Version with id: " + propVersion.getId());
		}
		save(propVersion);
		return propVersion;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#getProposalbyId(java.lang.Long)
	 */
	@Override
	@SuppressWarnings(UNCHECKED)
	public Proposal getProposalbyId(final Long proposalId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching Proposal by id. ProposalId: " + proposalId);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(Proposal.class);
		criteria.createAlias(PROPOSAL_OPTIONS, PROPOSAL_OPTION, CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("proposalOption.proposalVersions", PROPOSAL_VERSION, CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("proposalVersion.proposalLineItemSet", PROPOSAL_LINEITEM, CriteriaSpecification.LEFT_JOIN);
		criteria.add(Restrictions.eq("id", proposalId));
		criteria.add(Restrictions.eq("proposalOption.active", true));
		criteria.add(Restrictions.eq("proposalVersion.active", true));		
		final List<Proposal> proposalList = findByCriteria(criteria);
		if (proposalList == null || proposalList.isEmpty()) {
			return null;
		}
		Hibernate.initialize(proposalList.get(0).getAssignedUser());
		Hibernate.initialize(proposalList.get(0).getAssignedByUser());
		return proposalList.get(0);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#getOptionbyId(java.lang.Long)
	 */
	@Override
	@SuppressWarnings(UNCHECKED)
	public ProposalOption getOptionbyId(final Long optionId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching Option by id. optionId: " + optionId);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(ProposalOption.class);
		criteria.createAlias(PROPOSAL_VERSIONS, PROPOSAL_VERSION, CriteriaSpecification.LEFT_JOIN);
		criteria.add(Restrictions.eq("id", optionId));
		final List<ProposalOption> optionList = findByCriteria(criteria);
		if (optionList == null || optionList.isEmpty()) {
			return null;
		}
		return optionList.get(0);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#addLineItemsOfProposal(com.nyt.mpt.domain.LineItem)
	 */
	@Override
	public Long addLineItemsOfProposal(final LineItem lineItem) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Adding LineItems Of Proposal for lineItem id: " + lineItem.getLineItemID());
		}
		save(lineItem);
		return lineItem.getLineItemID();
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#deleteLineItemException(com.nyt.mpt.domain.LineItemExceptions)
	 */
	@Override
	public Long deleteLineItemException(final LineItemExceptions exceptions) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Deleting LineItem Exception for lineItemException id: " + exceptions.getId());
		}
		delete(exceptions);
		return exceptions.getId();
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#getProposalGeoTargets(java.lang.Long)
	 */
	@Override
	@SuppressWarnings(UNCHECKED)
	public List<LineItemTarget> getProposalGeoTargets(final Long elementId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching Proposal Geo Targets. elementId:" + elementId);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(LineItemTarget.class);
		criteria.add(Restrictions.eq("proposalLineItem.lineItemID", elementId));
		criteria.add(Restrictions.eq(ConstantStrings.ACTIVE, true));
		criteria.addOrder(Order.asc("id"));
		return findByCriteria(criteria);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#getproposalVersions(long, long)
	 */
	@Override
	@SuppressWarnings(UNCHECKED)
	public List<ProposalVersion> getproposalVersions(final long optionId, final long proposalversion) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching Proposal Versions. Id: " + optionId + " Version:" + proposalversion);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(ProposalVersion.class);
		criteria.createAlias(PROPOSAL_OPTION, PROPOSAL_OPTION);
		criteria.createAlias("proposalOption.proposal", PROPOSAL);
		criteria.add(Restrictions.eq("proposalOption.id", optionId));
		criteria.add(Restrictions.eq(PROPOSAL_VERSION, proposalversion));
		criteria.setFetchMode("proposalLineItemAssocSet", FetchMode.JOIN);
		criteria.setFetchMode("proposal.assignedUser", FetchMode.JOIN);
		final List<ProposalVersion> propVerLst = findByCriteria(criteria);		
		for (ProposalVersion proposalVersion : propVerLst) {
			Hibernate.initialize(proposalVersion.getProposalOption().getProposal().getAssignedUser());
		}
		return propVerLst;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#getproposalVersion(long)
	 */
	@Override
	@SuppressWarnings(UNCHECKED)
	public ProposalVersion getproposalVersion(final long proposalversionId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching Proposal Version proposalversionId: " + proposalversionId);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(ProposalVersion.class);
		criteria.add(Restrictions.eq("id", proposalversionId));
		final List<ProposalVersion> propVerLst = findByCriteria(criteria);
		if (propVerLst == null || propVerLst.isEmpty()) {
			return null;
		} else {
			for (ProposalVersion proposalVersion : propVerLst) {
				Hibernate.initialize(proposalVersion.getProposalOption().getProposal());
			}
			return propVerLst.get(0);
		}
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#getLineItems(java.lang.String)
	 */
	@Override
	@SuppressWarnings(UNCHECKED)
	public List<LineItem> getLineItems(final String lineItemId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching Line Item for lineitem id: " + lineItemId);
		}
		
		final String[] lineItemIds = lineItemId.split(ConstantStrings.COMMA);
		final DetachedCriteria criteria = DetachedCriteria.forClass(LineItem.class);
		criteria.add(Restrictions.in("lineItemID", StringUtil.convertStringArrayToLongArray(lineItemIds)));
		criteria.add(Restrictions.eq(ConstantStrings.ACTIVE, true));
		final List<LineItem> lineItemLst = findByCriteria(criteria);
		for (LineItem lineItem : lineItemLst) {
			final Set<LineItemExceptions> exceptionsSet = lineItem.getLineItemExceptions();
			for (LineItemExceptions lineItemExceptions : exceptionsSet) {
				Hibernate.initialize(lineItemExceptions);
			}
			final List<LineItemSalesTargetAssoc> targetAssocs = lineItem.getLineItemSalesTargetAssocs();
			for (LineItemSalesTargetAssoc lineItemSalesTargetAssoc : targetAssocs) {
				Hibernate.initialize(lineItemSalesTargetAssoc);
				Hibernate.initialize(lineItemSalesTargetAssoc.getProposalLineItem());
			}
			final Set<LineItemTarget> geoTargetAssocs = lineItem.getGeoTargetSet();
			for (LineItemTarget lineItemTarget : geoTargetAssocs) {
				Hibernate.initialize(lineItemTarget);
			}
			Hibernate.initialize(lineItem.getProposalVersion());
		}
		return lineItemLst;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#deleteLineItemSalesTargetAssoc(com.nyt.mpt.domain.LineItemSalesTargetAssoc)
	 */
	@Override
	public Long deleteLineItemSalesTargetAssoc(final LineItemSalesTargetAssoc salesTargetAssoc) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Deleting LineItem sales target association: " + salesTargetAssoc.getId());
		}
		delete(salesTargetAssoc);
		return salesTargetAssoc.getId();
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#editLineItemsOfProposal(com.nyt.mpt.domain.LineItem)
	 */
	@Override
	public Long editLineItemsOfProposal(final LineItem lineItem) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Editting LineItems Of Proposal for proposalLineItem with line item id:" + lineItem.getLineItemID());
		}
		update(lineItem);
		return lineItem.getLineItemID();
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#deleteProposalGeoTargets(com.nyt.mpt.domain.LineItemTarget)
	 */
	@Override
	public Long deleteProposalGeoTargets(final LineItemTarget lineItemTarget) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Deleting Proposal's GeoTargets for lineItemTarget id: " + lineItemTarget.getId());
		}
		delete(lineItemTarget);
		return lineItemTarget.getId();
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#getFilteredProposalLineItems(java.lang.Long, java.lang.Long, com.nyt.mpt.util.filter.FilterCriteria, com.nyt.mpt.util.PaginationCriteria, com.nyt.mpt.util.SortingCriteria)
	 */
	@Override
	@SuppressWarnings(UNCHECKED)
	public List<LineItem> getFilteredProposalLineItems(final Long optionId, final Long version, final FilterCriteria filterCriteria,
			final PaginationCriteria pgCriteria, final SortingCriteria sortCriteria) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching Filtered Proposal Line Items. Id: " + optionId + "FilterCriteria:" + filterCriteria);
		}
		DetachedCriteria criteria = null;
		try{
			criteria = constructFilterCriteriaForLineItems(filterCriteria, optionId, version);
		}catch (NumberFormatException ex) {
			return Collections.EMPTY_LIST;
		}
		addSortingCriteria(criteria, sortCriteria, EntityFormPropertyMap.LINE_ITEM_COLUMN_MAP);
		final List<LineItem> lineItemAssocList = findByCriteria(criteria, pgCriteria);
		for (LineItem proposalLineItem : lineItemAssocList) {
			final Set<LineItemExceptions> exceptionsSet = proposalLineItem.getLineItemExceptions();
			if(exceptionsSet!=null && !exceptionsSet.isEmpty()) {
				for (LineItemExceptions lineItemExceptions : exceptionsSet) {
					Hibernate.initialize(lineItemExceptions);
				}
			}
			final List<LineItemSalesTargetAssoc> salesTargetAssocs = proposalLineItem.getLineItemSalesTargetAssocs();
			if(salesTargetAssocs!=null && !salesTargetAssocs.isEmpty()) {
				for (LineItemSalesTargetAssoc lineItemSalesTargetAssoc : salesTargetAssocs) {
					Hibernate.initialize(lineItemSalesTargetAssoc);
					Hibernate.initialize(lineItemSalesTargetAssoc.getProposalLineItem());
				}
			}
			final Set<LineItemTarget> geoTargetset = proposalLineItem.getGeoTargetSet();
			if(geoTargetset!=null && !geoTargetset.isEmpty()) {
				for (LineItemTarget lineItemTarget : geoTargetset) {
					Hibernate.initialize(lineItemTarget);
				}
			}
			Hibernate.initialize(proposalLineItem.getReservation());
			Hibernate.initialize(proposalLineItem.getProposalVersion());
			Hibernate.initialize(proposalLineItem.getPackageObj());
		}
		return lineItemAssocList;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#getFilteredProposalLineItemsCount(java.lang.Long, java.lang.Long, com.nyt.mpt.util.filter.FilterCriteria)
	 */
	@Override
	public int getFilteredProposalLineItemsCount(final Long optionId, final Long version, final FilterCriteria criteria) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching filtered Package line items count. FilterCriteria: " + criteria);
		}
		return getCount(constructFilterCriteriaForLineItems(criteria, optionId, version));
	}

	/**
	 * Construct filter criteria for line item
	 * @param filterCriteria
	 * @param proposalId
	 * @param version
	 * @return
	 */
	private DetachedCriteria constructFilterCriteriaForLineItems(final FilterCriteria filterCriteria, final Long optionId, final Long version) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Constructing FilterCriteria For LineItems. FilterCriteria: " + filterCriteria);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(LineItem.class);
		criteria.add(Restrictions.eq(ConstantStrings.ACTIVE, true));
		criteria.createAlias(PROPOSAL_VERSION, "propVersion");
		criteria.createAlias("propVersion.proposalOption", "propOption");
		criteria.add(Restrictions.eq("propOption.id", optionId));
		criteria.add(Restrictions.eq("propVersion.proposalVersion", version));
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		if (filterCriteria != null && StringUtils.isNotBlank(filterCriteria.getSearchField())) {
			if (StringUtils.isNotBlank(filterCriteria.getSearchString()) &&  LINEITEM_ID.equals(filterCriteria.getSearchField())) {
				try{
					criteria.add(Restrictions.eq("lineItemID", Long.valueOf(filterCriteria.getSearchString())));
				}catch (NumberFormatException ex) {
					LOGGER.info("Invalid search input for " +filterCriteria.getSearchField() + " - "
							+ filterCriteria.getSearchString());
					throw ex;
				}
			}else if(StringUtils.isNotBlank(filterCriteria.getSearchString())&& SOS_LINEITEM_ID.equals(filterCriteria.getSearchField())){
				try{
					criteria.add(Restrictions.eq("sosLineItemID", Long.valueOf(filterCriteria.getSearchString())));
				}catch (NumberFormatException ex) {
					LOGGER.info("Invalid search input for " + filterCriteria.getSearchField() + " - "
							+ filterCriteria.getSearchString());
					throw ex;
				}
			}
		}
		return criteria;
	}
	
	/**
	 * Adding Sorting Criteria
	 * @param criteria
	 * @param sortingCriteria
	 * @param map
	 */
	private void addSortingCriteria(final DetachedCriteria criteria, final SortingCriteria sortingCriteria, final Map<String, String> map) {
		if (sortingCriteria == null) {
			return;
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Adding SortingCriteria for criteria: " + criteria);
		}
		final CustomDbOrder order = new CustomDbOrder();
		order.setAscending(sortingCriteria.getSortingOrder().equals(CustomDbOrder.ASCENDING));
		order.setFieldName(map.get(sortingCriteria.getSortingField()));
		if ("lineItemSequence".equals(sortingCriteria.getSortingField())) {
			final Order orderBy = order.isAscending() ? Order.asc("createdDate") : Order.desc("createdDate");
			criteria.addOrder(order.getOrder()).addOrder(orderBy);
		} else {
			criteria.addOrder(order.getOrder());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#getLineItemsOnBaseOfId(java.lang.Long[])
	 */
	@Override
	@SuppressWarnings(UNCHECKED)
	public List<LineItem> getLineItemsOnBaseOfId(final Long[] lineItems) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching line Items on base of Id. LineItems: " + lineItems);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(LineItem.class).addOrder(Order.asc("lineItemSequence"));
		criteria.add(Restrictions.in("lineItemID", lineItems));
		final List<LineItem> lineItemLst = findByCriteria(criteria);
		for (LineItem lineItem : lineItemLst) {
			final List<LineItemSalesTargetAssoc> assocsList = lineItem.getLineItemSalesTargetAssocs();
			for (LineItemSalesTargetAssoc salesTargetAssoc : assocsList) {
				Hibernate.initialize(salesTargetAssoc);
			}
			Hibernate.initialize(lineItem.getGeoTargetSet());
			Hibernate.initialize(lineItem.getLineItemSalesTargetAssocs());
		}
		return lineItemLst;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#getMaxSequenceNoForLineItem(long)
	 */
	@Override
	public int getMaxSequenceNoForLineItem(final long versionId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Getting Max seq No foror Proposal Version Id: " + versionId);
		}
		final String queryString = "Select NVL(max(SEQ_NUMBER),0) AS SEQNO "
				+ "from {h-schema}MP_LINE_ITEMS LI "
				+ "where LI.IS_ACTIVE = 1 and LI.PROPOSAL_VERSION_ID = :proposalVersionID";
		final Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		final SQLQuery query = session.createSQLQuery(queryString);
		query.setLong("proposalVersionID", versionId);
		query.addScalar("SEQNO", StandardBasicTypes.INTEGER);
		return (Integer) query.uniqueResult();
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#getCampaignObjectives()
	 */
	@Override
	@SuppressWarnings(UNCHECKED)
	public List<CampaignObjective> getCampaignObjectives() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching Campaign Objectives.");
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(CampaignObjective.class);
		criteria.addOrder(Order.asc("cmpObjText"));
		return findByCriteria(criteria);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#getCampaignObjectivesByProposalId(long)
	 */
	@Override
	@SuppressWarnings(UNCHECKED)
	public Set<CampaignObjective> getCampaignObjectivesByProposalId(final long proposalId) {
		final DetachedCriteria criteria = DetachedCriteria.forClass(Proposal.class);
		criteria.createAlias("campaignObjectiveSet", "campaignObjectiveSet", CriteriaSpecification.LEFT_JOIN);
		criteria.add(Restrictions.eq("id", proposalId));

		final List<Proposal> proposalList = findByCriteria(criteria);
		if (proposalList.isEmpty()) {
			return null;
		}
		return proposalList.get(0).getCampaignObjectiveSet();
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#proposalCountWithSameName(java.lang.String, java.lang.Long)
	 */
	@Override
	public int proposalCountWithSameName(final String proposalName, final Long proposalId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Check for proposal with same name. Proposal Name: " + proposalName);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(Proposal.class);
		if (proposalId == 0) {
			criteria.add(Restrictions.eq(ConstantStrings.NAME, proposalName).ignoreCase());
			criteria.add(Restrictions.ne("proposalStatus", ProposalStatus.DELETED));
			criteria.add(Restrictions.eq("active", true));
		} else {
			criteria.add(Restrictions.ne("id", proposalId));
			criteria.add(Restrictions.eq(ConstantStrings.NAME, proposalName).ignoreCase());
			criteria.add(Restrictions.ne("proposalStatus", ProposalStatus.DELETED));
			criteria.add(Restrictions.eq("active", true));
		}
		return findByCriteria(criteria).size();
	
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#getLineItemExceptions(java.lang.Long)
	 */
	@Override
	@SuppressWarnings(UNCHECKED)
	public List<LineItemExceptions> getLineItemExceptions(final Long lineItemID) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching line item exceptions. LineItemId: " + lineItemID);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(LineItemExceptions.class);
		criteria.add(Restrictions.eq("lineitemId.lineItemID", lineItemID));
		return findByCriteria(criteria);
	}

		
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#getProposalbyIdForSosIntegration(java.lang.Long)
	 */
	@Override
	@SuppressWarnings(UNCHECKED)
	public Proposal getProposalbyIdForSosIntegration(Long proposalId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Retreiving Proposal with default option and latest version for proposalID: " + proposalId);
		}
		final DetachedCriteria criteria = getCriteriaForSosIntegration();
		criteria.add(Restrictions.eq("id", proposalId));
		criteria.createAlias("propVersion.proposalLineItemSet", "lineItem", CriteriaSpecification.LEFT_JOIN);
	//	criteria.createAlias("lineItem.geoTargetSet", "geoTarget", CriteriaSpecification.LEFT_JOIN);
	//	criteria.createAlias("lineItem.lineItemSalesTargetAssocs", "salesTarget", CriteriaSpecification.LEFT_JOIN);
		criteria.add(Restrictions.eq("lineItem.active", true));
		
		//criteria.add(Subqueries.lt(Long.valueOf(0), innerJoinCriteria));
		final List<Proposal> proposalLst = findByCriteria(criteria);
		return (proposalLst != null && !proposalLst.isEmpty()) ? proposalLst.get(0) : null;
	}
	
	private DetachedCriteria getCriteriaForSosIntegration() {
		// This code is used to create inner join criteria with max proposal version
		final DetachedCriteria innerJoinCriteria = DetachedCriteria.forClass(ProposalVersion.class, "proposaVersion");
		innerJoinCriteria.setProjection(Property.forName("proposaVersion.proposalVersion").max());
		innerJoinCriteria.add(Property.forName("proposaVersion.proposalOption.id").eqProperty("propOption.id"));

		final DetachedCriteria criteria = DetachedCriteria.forClass(Proposal.class, "propo");
		criteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		criteria.createAlias("proposalOptions", "propOption", CriteriaSpecification.LEFT_JOIN);
		criteria.add(Restrictions.eq("propOption.defaultOption",true));
		criteria.createAlias("propOption.proposalVersions", "propVersion", CriteriaSpecification.LEFT_JOIN);

		// Applying inner join for proposal version
		criteria.add(Property.forName("propVersion.proposalVersion").eq(innerJoinCriteria));		
		criteria.add(Restrictions.eq(ConstantStrings.ACTIVE, true));
		return criteria;
	}

	@Override
	public Long updateProposalVersion(final ProposalVersion version) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Updating Proposal Version. ProposalVersion id:" + version.getId());
		}
		update(version);
		return version.getId();
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#getLineItemById(long)
	 */
	@Override
	@SuppressWarnings(UNCHECKED)
	public LineItem getLineItemById(final long lineItemId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching Line Item for lineitem id: " + lineItemId);
		}
	
		final DetachedCriteria criteria = DetachedCriteria.forClass(LineItem.class);
		criteria.add(Restrictions.eq("lineItemID", lineItemId));
		criteria.add(Restrictions.eq(ConstantStrings.ACTIVE, true));
		final List<LineItem> lineItemLst =  findByCriteria(criteria);
		LineItem lineItem = null;
		if(!lineItemLst.isEmpty()){
			lineItem = lineItemLst.get(0);
			Hibernate.initialize(lineItem.getProposalVersion());
			Hibernate.initialize(lineItem.getReservation());
			for (LineItemExceptions lineItemExceptions : lineItem.getLineItemExceptions()) {
				Hibernate.initialize(lineItemExceptions);
			}
	
			for (LineItemSalesTargetAssoc lineItemSalesTargetAssoc : lineItem.getLineItemSalesTargetAssocs()) {
				Hibernate.initialize(lineItemSalesTargetAssoc);
				Hibernate.initialize(lineItemSalesTargetAssoc.getProposalLineItem());
			}
			
			for (LineItemTarget lineItemTarget : lineItem.getGeoTargetSet()) {
				Hibernate.initialize(lineItemTarget);
			}
		}
		return lineItem;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#getProposalForClone(java.lang.Long)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public Proposal getProposalForClone(final Long proposalId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching Proposal by id. ProposalId: " + proposalId);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(Proposal.class);
		criteria.add(Restrictions.eq("id", proposalId));
		final List<Proposal> proposalList = findByCriteria(criteria);
		if (proposalList == null || proposalList.isEmpty()) {
			return null;
		}
		return proposalList.get(0);	
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#getProposalOptionsById(java.lang.Long)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<ProposalOption> getProposalOptionsById(Long proposalId) {
		final DetachedCriteria criteria = DetachedCriteria.forClass(ProposalOption.class);
		criteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		criteria.createAlias(PROPOSAL_VERSIONS, PROPOSAL_VERSION, CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("proposalVersion.proposalLineItemSet", PROPOSAL_LINEITEM, CriteriaSpecification.LEFT_JOIN);
		criteria.add(Restrictions.eq(ConstantStrings.ACTIVE, true));
		criteria.add(Restrictions.eq("proposal.id", proposalId));
		criteria.add(Restrictions.eq("proposalVersion.active", true));
		criteria.addOrder(Order.asc("id"));
		return findByCriteria(criteria);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#getProposalOptionsById(java.lang.Long)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public Notes getNotesById(Long notesId) {
		final DetachedCriteria criteria = DetachedCriteria.forClass(Notes.class);
		criteria.add(Restrictions.eq("id", notesId));
		List<Notes> notesLst = findByCriteria(criteria);
		if (notesLst == null || notesLst.isEmpty()) {
			return null;
		}
		return notesLst.get(0);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#saveOption(com.nyt.mpt.domain.ProposalOption)
	 */
	@Override
	public Notes saveNotes(final Notes notes) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Saving Notes with id: " + notes.getId());
		}
		update(notes);
		return notes;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#addOption(com.nyt.mpt.domain.ProposalOption)
	 */
	@Override
	public Notes addNotes(final Notes notes) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Adding Notes with id: " + notes.getId());
		}
		save(notes);
		return notes;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#addOption(com.nyt.mpt.domain.ProposalOption)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<Notes> getProposalNotes(final long proposalId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching Notes of proposal with id: " + proposalId);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(Notes.class).addOrder(Order.desc("id"));
		criteria.add(Restrictions.eq("proposalId", proposalId));
		criteria.add(Restrictions.eq("active", true));
		return findByCriteria(criteria);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#addOption(com.nyt.mpt.domain.ProposalOption)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<Proposal> getProposalsForUpdation(Date expiryProposedDate, List<ProposalStatus> statusLst){
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching all the proposals to be marked expired");
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(Proposal.class);
		criteria.add(Restrictions.eq("active", true));
		criteria.add(Restrictions.in("proposalStatus", statusLst));
		criteria.add(Restrictions.le("lastProposedDate", expiryProposedDate));
		return findByCriteria(criteria);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#getAllReservedLineItemsForProposal(java.lang.Long, java.util.List, com.nyt.mpt.util.filter.FilterCriteria, com.nyt.mpt.util.PaginationCriteria, com.nyt.mpt.util.SortingCriteria)
	 */
	@Override
	@SuppressWarnings(UNCHECKED)
	public List<LineItem> getAllReservedLineItemsForProposal(final Long proposalId, final List<Long> proposalVersionIdLst, final FilterCriteria filterCriteria, final PaginationCriteria pgCriteria, final SortingCriteria sortCriteria) {
		final DetachedCriteria criteria = constructFilterCriteriaForReservedLineItems(proposalId, proposalVersionIdLst);
		criteria.createAlias("lineItemSalesTargetAssocs", "salesTargetAssocs");
		if (sortCriteria != null) {
			//update the sorting field name in sorting criteria with DB column name 
			sortCriteria.setSortingField(EntityFormPropertyMap.LINE_ITEM_COLUMN_MAP.get(sortCriteria.getSortingField()));
			super.addSortingCriteria(criteria, sortCriteria);
		}
		final List<LineItem> lineItemAssocList = findByCriteria(criteria, pgCriteria);
		for (LineItem proposalLineItem : lineItemAssocList) {			
			final List<LineItemSalesTargetAssoc> salesTargetAssocs = proposalLineItem.getLineItemSalesTargetAssocs();
			for (LineItemSalesTargetAssoc lineItemSalesTargetAssoc : salesTargetAssocs) {
				Hibernate.initialize(lineItemSalesTargetAssoc);
			}
			Hibernate.initialize(proposalLineItem.getProposalVersion());
			final Set<LineItemTarget> geoTargetAssocs = proposalLineItem.getGeoTargetSet();
			for (LineItemTarget lineItemTarget : geoTargetAssocs) {
				Hibernate.initialize(lineItemTarget);
			}
		}
		return lineItemAssocList;
	}

	/**
	 * Get criteria for reserved line items
	 * @param proposalId
	 * @param proposalVersionIdLst
	 * @return
	 */
	private DetachedCriteria constructFilterCriteriaForReservedLineItems(final Long proposalId, final List<Long> proposalVersionIdLst) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Get criteria for reserved line items");
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(LineItem.class);
		criteria.add(Restrictions.eq(ConstantStrings.ACTIVE, true));
		criteria.add(Restrictions.eq("proposalId", proposalId));
		criteria.createAlias("reservation", "lineItemReservations", CriteriaSpecification.LEFT_JOIN);
		criteria.add(Restrictions.in("lineItemReservations.status", new ReservationStatus[] {ReservationStatus.HOLD, ReservationStatus.RE_NEW, ReservationStatus.RELEASED}));
		criteria.createAlias("proposalVersion", "proposalVersion", CriteriaSpecification.LEFT_JOIN);
		criteria.add(Restrictions.in("proposalVersion.id", proposalVersionIdLst));
		return criteria;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#getReservedLineItemsCount(java.lang.Long)
	 */
	@Override
	public int getReservedLineItemsCount(final Long proposalId, final List<Long> proposalVersionIdLst) {
		return getCount(constructFilterCriteriaForReservedLineItems(proposalId, proposalVersionIdLst));
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#deleteReservation(com.nyt.mpt.domain.LineItemReservations)
	 */
	@Override
	public Long deleteReservation(LineItemReservations reservation) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Deleting LineItem Reservations for reservation id: " + reservation.getId());
		}
		delete(reservation);
		return reservation.getId();
	}
	
	private ICalendarReservationDao reservationDao;	
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#getReservedProposalsForCalendar(com.nyt.mpt.domain.sos.CalendarVO)
	 */
	@SuppressWarnings(UNCHECKED)
	public List<Proposal> getReservedProposalsForCalendar(final ReservationTO calendarVO) {
		final DetachedCriteria criteria = getCriteriaForReservation(calendarVO);
		criteria.createAlias("lineItem.reservation", "reservation", CriteriaSpecification.LEFT_JOIN);
		criteria.add(Restrictions.in("reservation.status", new ReservationStatus[] {ReservationStatus.HOLD, ReservationStatus.RE_NEW}));
		return findByCriteria(criteria);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#getProposedProposalsForCalendar(com.nyt.mpt.domain.sos.CalendarVO)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Proposal> getProposedProposalsForCalendar(final ReservationTO calendarVO){
		final DetachedCriteria criteria = getCriteriaForReservation(calendarVO);
		criteria.createAlias("lineItem.reservation", "reservation", CriteriaSpecification.LEFT_JOIN);
		final List<Proposal> proposals = findByCriteria(criteria);
		for (Proposal proposal : proposals) {
			for (ProposalOption option : proposal.getProposalOptions()) {
				final ProposalVersion version = option.getLatestVersion();
				for (Iterator iterator = version.getProposalLineItemSet().iterator(); iterator.hasNext();) {
					final LineItem lineItem = (LineItem) iterator.next();
					if (lineItem.getReservation() == null) {
						continue;
					} else if (ReservationStatus.HOLD == lineItem.getReservation().getStatus()
							|| ReservationStatus.RE_NEW == lineItem.getReservation().getStatus()) {
						iterator.remove();
					}
				}
			}
		}
		return proposals;
	}

	/**
	 * @param calendarVO
	 * @return
	 */
	private DetachedCriteria getCriteriaForReservation(final ReservationTO calendarVO) {
		final DetachedCriteria criteria = DetachedCriteria.forClass(Proposal.class).setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		criteria.createAlias("assignedUser", "assignedUser", CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("proposalOptions", "propOption", CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("propOption.proposalVersions", "propVersion", CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("propVersion.proposalLineItemSet", "lineItem", CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("lineItem.lineItemSalesTargetAssocs", "salesTargets", CriteriaSpecification.LEFT_JOIN);
		
		criteria.add(Restrictions.ne("proposalStatus", ProposalStatus.DELETED));
		criteria.add(Restrictions.isNull("sosOrderId"));
		criteria.add(Restrictions.eq("propOption.active", true));
		criteria.add(Restrictions.eq("lineItem.active", true));
		criteria.add(Restrictions.gt("lineItem.sor", 0.0D));
		criteria.add(Restrictions.le("lineItem.startDate", calendarVO.getEndDate()));
		criteria.add(Restrictions.ge("lineItem.endDate", calendarVO.getStartDate()));
		final List<Long> productIdList = new ArrayList<Long>();
		productIdList.add(calendarVO.getProductId());
		criteria.add(Restrictions.in("lineItem.sosProductId", reservationDao.getConflictingProductIdList(productIdList)));
		final List<Long> salesTargetList = new ArrayList<Long>();
		salesTargetList.add(calendarVO.getSalesTargetId());
		criteria.add(Restrictions.in("salesTargets.sosSalesTargetId", salesTargetDAO.getSalesTargetParentOrChild(salesTargetList)));
		
		final DetachedCriteria criteriaMaxVersion = DetachedCriteria.forClass(ProposalVersion.class, "proposalVersion");
		criteriaMaxVersion.setProjection(Property.forName("proposalVersion.proposalVersion").max());
		criteriaMaxVersion.add(Property.forName("proposalVersion.proposalOption.id").eqProperty("propOption.id"));
		criteria.add(Property.forName("propVersion.proposalVersion").eq(criteriaMaxVersion));
		return criteria;
	}

	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ICalendarReservationDao#getSalesOrderForCalendar()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<LineItem> getProposalsForReservationSearch(final List<RangeFilterCriteria> criteriaLst, final PaginationCriteria pgCriteria, final SortingCriteria sortCriteria) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching sales order for reservation search: ");
		}
		
		final DetachedCriteria criteria = getCriteriaForSearchReservation();
		try {
			getAMPTFilterCriteriaForSearchProposal(criteria, criteriaLst);
		} catch (NumberFormatException ex) {
			return Collections.EMPTY_LIST;
		}
		if (sortCriteria != null) {
			//update the sorting field name in sorting criteria with DB column name 
			sortCriteria.setSortingField(EntityFormPropertyMap.RESERVATION_SEARCH_COLUMN_MAP.get(sortCriteria.getSortingField()));
			super.addSortingCriteria(criteria, sortCriteria);
		}
		
 		final List<LineItem> lineItemlLst = findByCriteria(criteria, pgCriteria);
 		if(lineItemlLst != null && lineItemlLst.size() > 0){
 			for (LineItem lineItem : lineItemlLst) {
				Hibernate.initialize(lineItem.getGeoTargetSet());
			}
 		}
		return lineItemlLst;
	}
	
	/**
	 * Return Filter Criteria for search reservation screen
	 * @param criteria
	 * @param filterCriterias
	 */
	private void getAMPTFilterCriteriaForSearchProposal(final DetachedCriteria criteria, final List<RangeFilterCriteria> filterCriterias) {
		if (filterCriterias != null) {
			for (RangeFilterCriteria filterCriteria : filterCriterias) {
				if ("proposalName".equalsIgnoreCase(filterCriteria.getSearchField())) {
					criteria.add(Restrictions.ilike(EntityFormPropertyMap.RESERVATION_SEARCH_COLUMN_MAP.get(filterCriteria.getSearchField()),
							filterCriteria.getSearchString(), MatchMode.ANYWHERE));
				}
				if ("proposalId".equalsIgnoreCase(filterCriteria.getSearchField())) {
					try {
						criteria.add(Restrictions.eq(EntityFormPropertyMap.RESERVATION_SEARCH_COLUMN_MAP.get(filterCriteria.getSearchField()), Long.valueOf(filterCriteria.getSearchString())));
					} catch (NumberFormatException ex) {
						LOGGER.info("Invalid search input for " + filterCriteria.getSearchField() + " - " + filterCriteria.getSearchString());
						throw ex;
					}
				}
				if ("salescategoryId".equalsIgnoreCase(filterCriteria.getSearchField())) {
					criteria.add(Restrictions.eq(EntityFormPropertyMap.RESERVATION_SEARCH_COLUMN_MAP.get(filterCriteria.getSearchField()),
							NumberUtil.longValue(filterCriteria.getSearchString())));
				}
				if ("advertiserId".equalsIgnoreCase(filterCriteria.getSearchField())) {
					criteria.add(Restrictions.eq(EntityFormPropertyMap.RESERVATION_SEARCH_COLUMN_MAP.get(filterCriteria.getSearchField()),
							NumberUtil.longValue(filterCriteria.getSearchString())));
				}
				if("productId".equalsIgnoreCase(filterCriteria.getSearchField())){
						criteria.add(Restrictions.in(EntityFormPropertyMap.RESERVATION_SEARCH_COLUMN_MAP.get(filterCriteria.getSearchField()),
						StringUtil.convertStringToLongList(filterCriteria.getSearchString())));
				}
				if("salesTargetId".equalsIgnoreCase(filterCriteria.getSearchField())){
						criteria.add(Restrictions.in(EntityFormPropertyMap.RESERVATION_SEARCH_COLUMN_MAP.get(filterCriteria.getSearchField()),
							StringUtil.convertStringToLongList(filterCriteria.getSearchString())));
				}
				if ("date".equalsIgnoreCase(filterCriteria.getSearchField())){
					if (StringUtils.isNotBlank(filterCriteria.getSearchStringFrom())) {
						criteria.add(Restrictions.ge(EntityFormPropertyMap.RESERVATION_SEARCH_COLUMN_MAP.get("endDate"),
								DateUtil.parseToDate(filterCriteria.getSearchStringFrom())));
					}

					if (StringUtils.isNotBlank(filterCriteria.getSearchStringTo())) {
						criteria.add(Restrictions.le(EntityFormPropertyMap.RESERVATION_SEARCH_COLUMN_MAP.get("startDate"),
								DateUtil.parseToDate(filterCriteria.getSearchStringTo())));
					}
				}
				if (ConstantStrings.STATUS.equalsIgnoreCase(filterCriteria.getSearchField())) {
					final List<ReservationStatus> statusList = new ArrayList<ReservationStatus>();
					for(String status : filterCriteria.getSearchString().split(",")){
						statusList.add(ReservationStatus.findByName(status));
					}
					criteria.add(Restrictions.in(EntityFormPropertyMap.RESERVATION_SEARCH_COLUMN_MAP.get(filterCriteria.getSearchField()),statusList ));
				}
				if("reservationStatus".equalsIgnoreCase(filterCriteria.getSearchField())){
					final List<ReservationStatus> statusList = new ArrayList<ReservationStatus>();
					for(ReservationStatus status : ReservationStatus.values()){
						statusList.add(status);
					}
					criteria.add(Restrictions.in(EntityFormPropertyMap.RESERVATION_SEARCH_COLUMN_MAP.get(filterCriteria.getSearchField()),statusList ));
				}
				if ("expiryDate".equalsIgnoreCase(filterCriteria.getSearchField())) {
					if(StringUtils.isBlank(filterCriteria.getSearchOper()) || SearchOption.EQUAL.toString().equals(filterCriteria.getSearchOper())){
						criteria.add(Restrictions.eq(EntityFormPropertyMap.RESERVATION_SEARCH_COLUMN_MAP.get("expiryDate"), DateUtil.parseToDateTime(filterCriteria.getSearchString())));
					}else if (SearchOption.LESS_EQUAL.toString().equals(filterCriteria.getSearchOper())) {
							criteria.add(Restrictions.le(EntityFormPropertyMap.RESERVATION_SEARCH_COLUMN_MAP.get("expiryDate"), DateUtil.parseToDateTime(filterCriteria.getSearchString())));
					}
				}
				if ("myReservationStatus".equalsIgnoreCase(filterCriteria.getSearchField())) {
					final List<ReservationStatus> statusList = new ArrayList<ReservationStatus>();
					for(String status : filterCriteria.getSearchString().split(",")){
						statusList.add(ReservationStatus.findByName(status));
					}
					criteria.add(Restrictions.in(EntityFormPropertyMap.RESERVATION_SEARCH_COLUMN_MAP.get("reservationStatus"),statusList ));
				}
				if (ASSIGNED_USER.equalsIgnoreCase(filterCriteria.getSearchField())) {
					criteria.add(Restrictions.eq(EntityFormPropertyMap.RESERVATION_SEARCH_COLUMN_MAP.get(filterCriteria.getSearchField()),
							NumberUtil.longValue(filterCriteria.getSearchString())));
				}
				if("daysToExpire".equalsIgnoreCase(filterCriteria.getSearchField())){
					criteria.add(Restrictions.le(EntityFormPropertyMap.RESERVATION_SEARCH_COLUMN_MAP.get("expiryDate"),
							DateUtil.parseToDateTime(filterCriteria.getSearchString())));
				}
			}
		}
	}
	
	
	/**
	 * Return Criteria for Search Reservation without filer criteria
	 * @param filterCriteriaLst
	 * @return
	 */
	private DetachedCriteria getCriteriaForSearchReservation() {
		final DetachedCriteria criteria = DetachedCriteria.forClass(LineItem.class);
		criteria.createAlias("proposalVersion","propVersion");
		criteria.createAlias("propVersion.proposalOption", "propOption");
		criteria.createAlias("propOption.proposal", "proposal");
		criteria.createAlias("lineItemSalesTargetAssocs", "salesTargets", CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("reservation", "reservation", CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("proposal.assignedUser", ASSIGNED_USER, CriteriaSpecification.LEFT_JOIN);
		criteria.add(Restrictions.ne("proposal.proposalStatus", ProposalStatus.DELETED));
		criteria.add(Restrictions.isNull("proposal.sosOrderId"));
		criteria.add(Restrictions.eq("propOption.active", true));
		criteria.add(Restrictions.eq("active", true));
		criteria.add(Restrictions.gt("sor", 0.0D));
		
		final DetachedCriteria criteriaMaxVersion = DetachedCriteria.forClass(ProposalVersion.class, "proposalVersion");
		criteriaMaxVersion.setProjection(Property.forName("proposalVersion.proposalVersion").max());
		criteriaMaxVersion.add(Property.forName("proposalVersion.proposalOption.id").eqProperty("propOption.id"));
		criteria.add(Property.forName("propVersion.proposalVersion").eq(criteriaMaxVersion));
		
		return criteria;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#getProposalListCount(java.util.List)
	 */
	@Override
	public int getProposalsForReservationSearchCount(final List<RangeFilterCriteria> filterCriteriaLst) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching Search Proposal List count. FilterCriteriaList: " + filterCriteriaLst);
		}

		final DetachedCriteria criteria = getCriteriaForSearchReservation();
		try {	
			getAMPTFilterCriteriaForSearchProposal(criteria, filterCriteriaLst);
		} catch (NumberFormatException ex) {
			return 0;
		}
		return getCount(criteria);
	}
	
	public void setReservationDao(ICalendarReservationDao reservationDao) {
		this.reservationDao = reservationDao;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#getReservationBylineItemID(java.lang.Long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<LineItemReservations> getReservationBylineItemID(Long lineItemID) {
		final DetachedCriteria criteria = DetachedCriteria.forClass(LineItemReservations.class);
		criteria.createAlias("proposalLineItem", "lineItem");
		criteria.add(Restrictions.eq("lineItem.lineItemID", lineItemID));
		return findByCriteria(criteria);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#updateLineItemReservations(com.nyt.mpt.domain.LineItemReservations)
	 */
	@Override
	public LineItemReservations updateLineItemReservations(LineItemReservations lineItemReservations) {
		update(lineItemReservations);
		return lineItemReservations;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#getQualifyingLineItems()
	 */
	@Override
	public Map<String, String> getQualifyingLineItems() {
		Map<String, String> returnMap = new HashMap<String, String>();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Getting Qualifying Line Item types");
		}
		final String queryString = "Select PRICETYPE_NAME AS PRICETYPE from {h-schema}MP_QULIFYNG_LI_TYPE_FOR_AUDIT";
		final Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		final SQLQuery query = session.createSQLQuery(queryString);
		query.addScalar("PRICETYPE", StandardBasicTypes.STRING);
		final ScrollableResults result = query.scroll();
		while (result.next()) {
			final Object[] arr = result.get();
			returnMap.put((String) arr[0], (String) arr[0]);
		}
		return returnMap;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#getLineItemsList(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LineItem> getLineItemsList(final String lineItemId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching Line Item for lineitem id: " + lineItemId);
		}
		
		final String[] lineItemIds = lineItemId.split(ConstantStrings.COMMA);
		final DetachedCriteria criteria = DetachedCriteria.forClass(LineItem.class);
		criteria.add(Restrictions.in("lineItemID", StringUtil.convertStringArrayToLongArray(lineItemIds)));
		criteria.add(Restrictions.eq(ConstantStrings.ACTIVE, true));
		final List<LineItem> lineItemLst = findByCriteria(criteria);
		return lineItemLst;
	}
	
	public void setSalesTargetDAO(ISalesTargetDAO salesTargetDAO) {
		this.salesTargetDAO = salesTargetDAO;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#getOptionLstbyIds(java.lang.Long[])
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ProposalOption> getOptionLstbyIds(final Long[] optionIds) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching Options with ids. optionId(s): " + optionIds);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(ProposalOption.class);
		criteria.createAlias(PROPOSAL_VERSIONS, PROPOSAL_VERSION, CriteriaSpecification.LEFT_JOIN);
		criteria.add(Restrictions.in("id", optionIds));
		criteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		final List<ProposalOption> optionList = findByCriteria(criteria);
		if (optionList == null || optionList.isEmpty()) {
			return null;
		}
		return optionList;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * com.nyt.mpt.dao.IProposalDAO#getProposalListHavingLineItems(java.util
	 * .List, com.nyt.mpt.util.PaginationCriteria,
	 * com.nyt.mpt.util.SortingCriteria)
	 */
	@Override
	@SuppressWarnings(UNCHECKED)
	public List<Proposal> getProposalListHavingLineItems(final List<RangeFilterCriteria> criteriaLst, final PaginationCriteria pgCriteria, final SortingCriteria sortCriteria) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching Proposal List having line items. FilterCriteriaList: " + criteriaLst);
		}

		final DetachedCriteria criteria = getCriteriaForSearchProposalHavingLineItems(criteriaLst);
		getFilterCriteriaForSearchProposal(criteria, criteriaLst);
		if (sortCriteria != null) {
			// update the sorting field name in sorting criteria with DB column
			// name
			sortCriteria.setSortingField(EntityFormPropertyMap.DB_COLUMN_MAP.get(sortCriteria.getSortingField()));
			super.addSortingCriteria(criteria, sortCriteria);
		}
		final List<Proposal> proposalLst = findByCriteria(criteria, pgCriteria);
		for (Proposal proposal : proposalLst) {
			Hibernate.initialize(proposal.getAssignedByUser());
			Hibernate.initialize(proposal.getAssignedUser());
			Hibernate.initialize(proposal.getProposalOptions());
			for (ProposalOption option : proposal.getProposalOptions()) {
				Hibernate.initialize(option.getProposalVersions());
			}
		}
		return proposalLst;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#getReservationsBylineItemIDs(java.util.List)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<LineItemReservations> getReservationsBylineItemIDs(	List<Long> lineItemIDs) {
			final DetachedCriteria criteria = DetachedCriteria.forClass(LineItemReservations.class);
			criteria.createAlias("proposalLineItem", "lineItem");
			criteria.add(Restrictions.in("lineItem.lineItemID", lineItemIDs));
			return findByCriteria(criteria);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#getProposalsByID(java.util.List)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<Proposal> getProposalsByID(List<Long> proposalId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching Proposal by id. ");
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(Proposal.class);
		criteria.add(Restrictions.in("id", proposalId));
		return findByCriteria(criteria);
	}
	
	/**
	 * @param criteriaLst
	 * @return
	 */
	private DetachedCriteria getCriteriaForSearchProposalHavingLineItems(final List<RangeFilterCriteria> criteriaLst) {
		final DetachedCriteria criteria = getCriteriaForSearchProposal(criteriaLst);

		final DetachedCriteria innerJoinCriteria = DetachedCriteria.forClass(LineItem.class, "lineItem");
		innerJoinCriteria.createAlias("lineItem.proposalVersion", "propVer");
		innerJoinCriteria.add(Property.forName("propVer.id").eqProperty("propVersion.id"));
		innerJoinCriteria.add(Restrictions.eq("active", true));
		innerJoinCriteria.setProjection(Projections.count("lineItemID"));

		criteria.add(Subqueries.lt(Long.valueOf(0), innerJoinCriteria));
		return criteria;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#getReservedProposalsForSalesCalendar(com.nyt.mpt.util.reservation.SalesCalendarReservationDTO)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<Proposal> getReservedProposalsForSalesCalendar(SalesCalendarReservationDTO calendarVO) {
		final DetachedCriteria criteria = getCriteriaForSalesReservation(calendarVO);
		criteria.createAlias("lineItem.reservation", "reservation", CriteriaSpecification.LEFT_JOIN);
		criteria.add(Restrictions.in("reservation.status", new ReservationStatus[] {ReservationStatus.HOLD, ReservationStatus.RE_NEW}));
		return findByCriteria(criteria);
	}
	
	/**
	 * @param calendarVO
	 * @return
	 */
	private DetachedCriteria getCriteriaForSalesReservation(final SalesCalendarReservationDTO calendarVO) {
		final DetachedCriteria criteria = DetachedCriteria.forClass(Proposal.class).setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		criteria.createAlias("proposalOptions", "propOption", CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("propOption.proposalVersions", "propVersion", CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("propVersion.proposalLineItemSet", "lineItem", CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("lineItem.lineItemSalesTargetAssocs", "salesTargets", CriteriaSpecification.LEFT_JOIN);
		
		criteria.add(Restrictions.ne("proposalStatus", ProposalStatus.DELETED));
		criteria.add(Restrictions.isNull("sosOrderId"));
		criteria.add(Restrictions.eq("propOption.active", true));
		criteria.add(Restrictions.eq("lineItem.active", true));
		criteria.add(Restrictions.gt("lineItem.sor", 0.0D));
		criteria.add(Restrictions.le("lineItem.startDate", calendarVO.getEndDate()));
		criteria.add(Restrictions.ge("lineItem.endDate", calendarVO.getStartDate()));
		criteria.add(Restrictions.in("lineItem.sosProductId", reservationDao.getConflictingProductIdList(calendarVO.getProductIds())));
		if(calendarVO.getSalesTargetIds() != null && !calendarVO.getSalesTargetIds().isEmpty()){
			criteria.add(Restrictions.in("salesTargets.sosSalesTargetId", salesTargetDAO.getSalesTargetParentOrChild(calendarVO.getSalesTargetIds())));
		}
		final DetachedCriteria criteriaMaxVersion = DetachedCriteria.forClass(ProposalVersion.class, "proposalVersion");
		criteriaMaxVersion.setProjection(Property.forName("proposalVersion.proposalVersion").max());
		criteriaMaxVersion.add(Property.forName("proposalVersion.proposalOption.id").eqProperty("propOption.id"));
		criteria.add(Property.forName("propVersion.proposalVersion").eq(criteriaMaxVersion));
		return criteria;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#getProposedReservationsForCalendar(com.nyt.mpt.util.reservation.SalesCalendarReservationDTO)
	 */	
	@Override
	@SuppressWarnings("unchecked")
	public List<Proposal> getProposedReservationsForCalendar(SalesCalendarReservationDTO calendarVO) {
		final DetachedCriteria criteria = getCriteriaForSalesReservation(calendarVO);
		criteria.createAlias("lineItem.reservation", "reservation", CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("lineItem.geoTargetSet", "geoTarget", CriteriaSpecification.LEFT_JOIN);		
		final List<Proposal> proposals = findByCriteria(criteria);
		for (Proposal proposal : proposals) {
			getHibernateTemplate().getSessionFactory().getCurrentSession().evict(proposal);
			for (ProposalOption option : proposal.getProposalOptions()) {
				final ProposalVersion version = option.getLatestVersion();
				for (Iterator<LineItem> iterator = version.getProposalLineItemSet().iterator(); iterator.hasNext();) {
					final LineItem lineItem = (LineItem) iterator.next();
					if (lineItem.getReservation() == null) {
						continue;
					} else if (ReservationStatus.HOLD == lineItem.getReservation().getStatus()
							|| ReservationStatus.RE_NEW == lineItem.getReservation().getStatus()) {
						iterator.remove();
					}
				}
			}
		}
		return proposals;
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#getVulnerableHomepageReservations()
	 */
	@Override
	@SuppressWarnings(UNCHECKED)
	public List<LineItem> getVulnerableHomepageReservations(Long productClassId) {
		List<LineItem> lineItemLst = null;
	 	Calendar c = Calendar.getInstance();
		c.setTime(DateUtil.getCurrentDate());
		
		c.set(Calendar.HOUR_OF_DAY, c.getActualMinimum(Calendar.HOUR_OF_DAY));
		c.set(Calendar.MINUTE, c.getActualMinimum(Calendar.MINUTE));
		c.set(Calendar.SECOND, c.getActualMinimum(Calendar.SECOND));
		c.set(Calendar.MILLISECOND, c.getActualMinimum(Calendar.MILLISECOND));
		Date currentDate = c.getTime();
	 	c.add(Calendar.DATE, 30);
		Date advancedDate = c.getTime();
	 	final DetachedCriteria criteria = DetachedCriteria
				.forClass(LineItem.class);
		criteria.createAlias("proposalVersion","propVersion");
		criteria.createAlias("propVersion.proposalOption", "propOption");
		criteria.createAlias("propOption.proposal", "proposal");
		criteria.createAlias("reservation", "reservation");
		criteria.createAlias("lineItemSalesTargetAssocs", "salesTargets", CriteriaSpecification.LEFT_JOIN);
	 	criteria.add(Restrictions.ne("proposal.proposalStatus", ProposalStatus.DELETED));
		criteria.add(Restrictions.isNull("proposal.sosOrderId"));
		criteria.add(Restrictions.eq("propOption.active", true));
		criteria.add(Restrictions.eq("active", true));
		
		criteria.add(Restrictions.isNotNull("reservation"));
		criteria.add(Restrictions.ne("reservation.status", ReservationStatus.RELEASED));
		
		criteria.add(Restrictions.eq("sosProductClass", productClassId));
		criteria.add(Restrictions.ge("startDate", currentDate));
		criteria.add(Restrictions.le("startDate", advancedDate));
		
		final DetachedCriteria criteriaMaxVersion = DetachedCriteria.forClass(ProposalVersion.class, "proposalVersion");
		criteriaMaxVersion.setProjection(Property.forName("proposalVersion.proposalVersion").max());
		criteriaMaxVersion.add(Property.forName("proposalVersion.proposalOption.id").eqProperty("propOption.id"));
		criteria.add(Property.forName("propVersion.proposalVersion").eq(criteriaMaxVersion));
		
		criteria.addOrder(Order.desc("proposalId"));
		criteria.addOrder(Order.desc("lineItemID"));
	try{	
		lineItemLst = findByCriteria(criteria);
		for (LineItem lineItem : lineItemLst) {
			Hibernate.initialize(lineItem.getProposalVersion());
			Hibernate.initialize(lineItem.getLineItemSalesTargetAssocs());
			Hibernate.initialize(lineItem.getGeoTargetSet());
			Hibernate.initialize(lineItem.getProposalVersion().getProposalOption());
			Hibernate.initialize(lineItem.getProposalVersion().getProposalOption().getProposal());
			Hibernate.initialize(lineItem.getProposalVersion().getProposalOption().getProposal().getAssignedUser());
		}
	}catch(Exception e){
		LOGGER.error(e.getMessage());
	}
		return lineItemLst;

	}

	
}
