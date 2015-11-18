package com.nyt.mpt.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;

import com.nyt.mpt.dao.IAddedValueDao;
import com.nyt.mpt.domain.AddedValueBudget;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.filter.FilterCriteria;

public class AddedValueDao extends GenericDAOImpl implements IAddedValueDao {

	private static final String TOTAL_INVESTMENT = "totalInvestment";
	private static final Logger LOGGER = Logger.getLogger(AddedValueDao.class);
	private static final Map<String, String> FIELD_COLUMN_MAP = new HashMap<String, String>();
	
	private static final String AV_BUDGET_ID = "id";
	
	static {
		FIELD_COLUMN_MAP.put("planBudgetId", "id");	
		FIELD_COLUMN_MAP.put(TOTAL_INVESTMENT, TOTAL_INVESTMENT);	
		FIELD_COLUMN_MAP.put("avNotes", "notes");	
		FIELD_COLUMN_MAP.put("avPercentage", "avPercentage");	
		
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IAddedValueDao#getFilteredPlanBudgetCount(com.nyt.mpt.util.filter.FilterCriteria)
	 */
	@Override
	public int getFilteredPlanBudgetCount(FilterCriteria filterCriteria){
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching filtered addedValueBudget list count with filterCriteria: " + filterCriteria);
		}
		final DetachedCriteria criteria = constructFilterCriteriaForAvBudget(filterCriteria);
		return getCount(criteria);
	}
	
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IAddedValueDao#getFilteredPlanBudgetList(com.nyt.mpt.util.filter.FilterCriteria, com.nyt.mpt.util.PaginationCriteria, com.nyt.mpt.util.SortingCriteria)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<AddedValueBudget> getFilteredPlanBudgetList(final FilterCriteria filterCriteria, final PaginationCriteria pageCriteria, final SortingCriteria sortCriteria){
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching filtered addedValueBudget list with filterCriteria: " + filterCriteria);
		}
		DetachedCriteria criteria = null;		
		try{
			criteria  = constructFilterCriteriaForAvBudget(filterCriteria);
		}
		catch (NumberFormatException ex) {
			return Collections.EMPTY_LIST;
		}
		if (sortCriteria != null) {
			sortCriteria.setSortingField(FIELD_COLUMN_MAP.get(sortCriteria.getSortingField()));
			addSortingCriteria(criteria, sortCriteria);
			sortCriteria.setSortingField(FIELD_COLUMN_MAP.get("planBudgetId"));
		}
		return findByCriteria(criteria,pageCriteria);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IAddedValueDao#saveAddedValueBudget(com.nyt.mpt.domain.AddedValueBudget)
	 */
	@Override
	public AddedValueBudget saveAddedValueBudget(final AddedValueBudget addedValueBudget){
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("saving or updating AddedValueBudget object with id: " + addedValueBudget.getId());
		}
		saveOrUpdate(addedValueBudget);
		return addedValueBudget;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IAddedValueDao#getAddedValueBudget(long)
	 */
	@Override
	public AddedValueBudget getAddedValueBudget(long addedValueBudgetid){

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetch AddedValueBudget object with id:" + addedValueBudgetid);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(AddedValueBudget.class);
		criteria.add(Restrictions.eq(AV_BUDGET_ID, addedValueBudgetid));
		return (AddedValueBudget) findByCriteria(criteria).get(0);
	
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IAddedValueDao#deleteAddedValueBudget(com.nyt.mpt.domain.AddedValueBudget)
	 */
	@Override
	public void deleteAddedValueBudget(final AddedValueBudget addedValueBudget){

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Deletiing AddedValueBudget object");
		}
		delete(addedValueBudget);
	
	}
	
	/**
	 * make a filterCriteria for added value table grid
	 * @param filterCriteria
	 * @return
	 */
	private DetachedCriteria constructFilterCriteriaForAvBudget(final FilterCriteria filterCriteria) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Constructing filter criteria for AddedValueBudget. Filter Criteria: " + filterCriteria);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(AddedValueBudget.class);
		criteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		if (filterCriteria != null && StringUtils.isNotBlank(filterCriteria.getSearchField())) {
			if(TOTAL_INVESTMENT.equals(filterCriteria.getSearchField())) {
			try {
				if(StringUtils.isNotBlank(filterCriteria.getSearchString())) {
					criteria.add(Restrictions.eq(FIELD_COLUMN_MAP.get(filterCriteria.getSearchField()), Double.valueOf(filterCriteria.getSearchString())));
				}
			} catch (NumberFormatException ex) {
				LOGGER.info("Invalid search input for " + FIELD_COLUMN_MAP.get(filterCriteria.getSearchField()) + " - "	+ filterCriteria.getSearchString());
				throw ex;
			}	
		}else{
			try {
				if(StringUtils.isNotBlank(filterCriteria.getSearchString())) {
					criteria.add(Restrictions.eq(FIELD_COLUMN_MAP.get(filterCriteria.getSearchField()), Double.valueOf(filterCriteria.getSearchString())));
				}
			} catch (NumberFormatException ex) {
				LOGGER.info("Invalid search input for " + FIELD_COLUMN_MAP.get(filterCriteria.getSearchField()) + " - "	+ filterCriteria.getSearchString());
				throw ex;
				}	
			}
		}
		return criteria;
	}


	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IAddedValueDao#isDuplicateInvestment(double, java.lang.Long)
	 */	
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public boolean isDuplicateInvestment(double investment, Long id) {
		final DetachedCriteria criteria = DetachedCriteria.forClass(AddedValueBudget.class);
		if (id == 0) {
			criteria.add(Restrictions.eq(TOTAL_INVESTMENT, investment));
		} else {
			criteria.add(Restrictions.ne(AV_BUDGET_ID, id));
			criteria.add(Restrictions.eq(TOTAL_INVESTMENT, investment));
		}
		final List<AddedValueBudget> list = findByCriteria(criteria);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Count for Investment '" + investment + "' is: " + list.size());
		}
		if (list.isEmpty()) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IAddedValueDao#getNearByAvDiscountPercent(java.lang.Double, long)
	 */
	@Override
	public List<AddedValueBudget> getNearByAvDiscountPercent(Double investment, long id) {
		List<AddedValueBudget> addedValueBudgetLst = new ArrayList<AddedValueBudget>();		
		final Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT TOTAL_INVESTMENT AS INVESTMENT, AV_PERCENTAGE AS PERCENTAGE FROM {h-schema}MP_ADDED_VALUE_BUDGET ");
		sql.append("WHERE TOTAL_INVESTMENT = ");
		sql.append("(select min(TOTAL_INVESTMENT) FROM {h-schema}MP_ADDED_VALUE_BUDGET where TOTAL_INVESTMENT > :investment ");
		if (id != 0) {
			sql.append("and ID <> :avBudgetId ");
		}
		sql.append(ConstantStrings.CLOSING_BRACKET);
		sql.append("or TOTAL_INVESTMENT =  ");
		sql.append("(select max(TOTAL_INVESTMENT) FROM {h-schema}MP_ADDED_VALUE_BUDGET where TOTAL_INVESTMENT < :investment ");
		if (id != 0) {
			sql.append("and ID <> :avBudgetId ");
		}
		sql.append(ConstantStrings.CLOSING_BRACKET);
		sql.append("order by 1 desc");
		final SQLQuery query = session.createSQLQuery(sql.toString());
		query.setDouble("investment", investment);
		if (id != 0) {
			query.setLong("avBudgetId", id);
		}
		query.addScalar("INVESTMENT", StandardBasicTypes.DOUBLE);
		query.addScalar("PERCENTAGE", StandardBasicTypes.DOUBLE);
		final ScrollableResults result = query.scroll();
		while (result.next()) {
			AddedValueBudget addedValueBudget = new AddedValueBudget();
			final Object[] arr = result.get();
			addedValueBudget.setTotalInvestment((Double) arr[0]); 
			addedValueBudget.setAvPercentage((Double) arr[1]); 	
			addedValueBudgetLst.add(addedValueBudget);
		}
		return addedValueBudgetLst;
	}
	
}
