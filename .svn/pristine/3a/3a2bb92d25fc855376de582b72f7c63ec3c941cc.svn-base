/**
 *
 */
package com.nyt.mpt.dao.impl;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.nyt.mpt.dao.IProposalDAOSOS;
import com.nyt.mpt.domain.Advertiser;
import com.nyt.mpt.domain.Agency;
import com.nyt.mpt.domain.Currency;
import com.nyt.mpt.domain.CurrencyConversion;
import com.nyt.mpt.domain.CurrencyConversionRate;
import com.nyt.mpt.domain.ProductPlacement;
import com.nyt.mpt.domain.ProposalClients;
import com.nyt.mpt.domain.lineItemTransPeriod;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.CustomDbOrder;

/**
 * This DAO level class is used for Proposal SOS related operation
 *
 * @author amandeep.singh
 *
 */
public class ProposalDAOSOS extends GenericDAOImpl implements IProposalDAOSOS {

	private static final Logger LOGGER = Logger.getLogger(ProposalDAOSOS.class);

	/* (non-Javadoc)
	 * @see com.nyt.mpt.proposal.dao.IProposalDAOSOS#getAdvertiser()
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<Advertiser> getAdvertiser() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching list of all Advertiser");
		}
		final String hqlQuery = "SELECT proposalClients from ProposalClients proposalClients, AMPTDisplayLists ADL"
				+ " where proposalClients.id = ADL.sourceId AND ADL.displayFlag = :displayFlag AND ADL.sourceTable = :sourceTable"
				+ " AND proposalClients.typeID = :clientType ORDER BY LOWER(proposalClients.name)";
		final Session hibernateSession = getHibernateTemplate().getSessionFactory().getCurrentSession();
		final Query hibernateQuery = hibernateSession.createQuery(hqlQuery);
		hibernateQuery.setString("displayFlag", ConstantStrings.YES);
		hibernateQuery.setString("sourceTable", ConstantStrings.ADVERTISER_CAPS);
		hibernateQuery.setString("clientType", ConstantStrings.ADVERTISER);
		hibernateQuery.setCacheable(true);
		return (List<Advertiser>) hibernateQuery.list();
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAOSOS#getAdvertiserByName(java.lang.String)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<Advertiser> getAdvertiserByName(String advertiserName) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching list of all Advertiser containing:"+advertiserName);
		}
		final String hqlQuery = "SELECT proposalClients from ProposalClients proposalClients, AMPTDisplayLists ADL"
				+ " where proposalClients.id = ADL.sourceId AND ADL.displayFlag = :displayFlag AND ADL.sourceTable = :sourceTable"
				+ " AND proposalClients.typeID = :clientType AND LOWER(proposalClients.name) like :advertiserName ORDER BY LOWER(proposalClients.name)";
		final Session hibernateSession = getHibernateTemplate().getSessionFactory().getCurrentSession();
		final Query hibernateQuery = hibernateSession.createQuery(hqlQuery);
		hibernateQuery.setString("displayFlag", ConstantStrings.YES);
		hibernateQuery.setString("sourceTable", ConstantStrings.ADVERTISER_CAPS);
		hibernateQuery.setString("clientType", ConstantStrings.ADVERTISER);
		hibernateQuery.setString("advertiserName",'%' + advertiserName.toLowerCase() + '%'); 
		hibernateQuery.setCacheable(true);
		return (List<Advertiser>) hibernateQuery.list();
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAOSOS#getAdvertiserById(long)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public Advertiser getAdvertiserById(final long advertiserId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching Advertiser for advertiser Id - " + advertiserId);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(ProposalClients.class);
		criteria.add(Restrictions.eq("id", advertiserId));
		criteria.add(Restrictions.eq("typeID", ConstantStrings.ADVERTISER));
		criteria.addOrder(Order.asc(ConstantStrings.NAME).ignoreCase());
		final Criteria hibernateCriteria = criteria.getExecutableCriteria(getHibernateTemplate().getSessionFactory().getCurrentSession());
		hibernateCriteria.setCacheable(true);
		final List<Advertiser> advertiserList = findByCriteria(criteria);
		if (advertiserList == null || advertiserList.isEmpty()) {
			return null;
		}
		return advertiserList.get(0);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.proposal.dao.IProposalDAOSOS#getAgency()
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<Agency> getAgency() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching list of all Agency");
		}
		final String hqlQuery = "SELECT proposalClients from ProposalClients proposalClients, AMPTDisplayLists ADL"
				+ " where proposalClients.id = ADL.sourceId AND ADL.displayFlag = :displayFlag AND ADL.sourceTable = :sourceTable"
				+ " AND proposalClients.typeID = :clientType" + " ORDER BY LOWER(proposalClients.name)";
		final Session hibernateSession = getHibernateTemplate().getSessionFactory().getCurrentSession();
		final Query hibernateQuery = hibernateSession.createQuery(hqlQuery);
		hibernateQuery.setString("displayFlag", ConstantStrings.YES);
		hibernateQuery.setString("sourceTable", ConstantStrings.AGENCY_CAPS);
		hibernateQuery.setString("clientType", ConstantStrings.AGENCY);
		hibernateQuery.setCacheable(true);
		return (List<Agency>) hibernateQuery.list();
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.proposal.dao.IProposalDAOSOS#getAgencyByName()
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<Agency> getAgencyByName(String agencyName) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching list of all Agency containing:"+agencyName);
		}
		final String hqlQuery = "SELECT proposalClients from ProposalClients proposalClients, AMPTDisplayLists ADL"
				+ " where proposalClients.id = ADL.sourceId AND ADL.displayFlag = :displayFlag AND ADL.sourceTable = :sourceTable"
				+ " AND proposalClients.typeID = :clientType AND LOWER(proposalClients.name) like :agencyName ORDER BY LOWER(proposalClients.name)";
		final Session hibernateSession = getHibernateTemplate().getSessionFactory().getCurrentSession();
		final Query hibernateQuery = hibernateSession.createQuery(hqlQuery);
		hibernateQuery.setString("displayFlag", ConstantStrings.YES);
		hibernateQuery.setString("sourceTable", ConstantStrings.AGENCY_CAPS);
		hibernateQuery.setString("clientType", ConstantStrings.AGENCY);
		hibernateQuery.setString("agencyName",'%' + agencyName.toLowerCase() + '%'); 
		hibernateQuery.setCacheable(true);
		return (List<Agency>) hibernateQuery.list();
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAOSOS#getAgencyById(long)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public Agency getAgencyById(final long agencyId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching Agency for agency Id - " + agencyId);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(ProposalClients.class);
		criteria.add(Restrictions.eq("id", agencyId));
		criteria.add(Restrictions.eq("typeID", ConstantStrings.AGENCY));
		criteria.addOrder(Order.asc(ConstantStrings.NAME).ignoreCase());
		final Criteria hibernateCriteria = criteria.getExecutableCriteria(getHibernateTemplate().getSessionFactory().getCurrentSession());
		hibernateCriteria.setCacheable(true);
		final List<Agency> agencyList = findByCriteria(criteria);
		if (agencyList == null || agencyList.isEmpty()) {
			return null;
		}
		return agencyList.get(0);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAOSOS#isProductPlacementActive(long, long)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public boolean isProductPlacementActive(final long productId, Long[] salesTargetID) {
		final DetachedCriteria criteria = DetachedCriteria.forClass(ProductPlacement.class);
		criteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("product.id", productId));
		criteria.add(Restrictions.in("salesTarget.salesTargetId", salesTargetID));
		criteria.add(Restrictions.eq(ConstantStrings.STATUS, ConstantStrings.ACTIVE_STATUS));
		final List<ProductPlacement> placements = findByCriteria(criteria);
		if (placements != null && placements.size() == salesTargetID.length) {
			return true;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAOSOS#isProductPlacementActive(long, long)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public Map<Long, String> getCurrencies(){
		Map<Long, String> currencyMap = new LinkedHashMap<Long, String>();
		final DetachedCriteria criteria = DetachedCriteria.forClass(Currency.class);
		final CustomDbOrder order = new CustomDbOrder();
		order.setAscending(true);
		order.setFieldName("name");
		criteria.addOrder(order.getOrder());
		final Criteria hibernateCriteria = criteria.getExecutableCriteria(getHibernateTemplate().getSessionFactory().getCurrentSession());
		hibernateCriteria.setCacheable(true);
		final List<Currency> currencyLst = findByCriteria(criteria);
		if(currencyLst != null && currencyLst.size() >0){
			for (Currency currency : currencyLst) {
				currencyMap.put(currency.getId(), currency.getName());
			}
		}
		return currencyMap;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#getProposalOptionsById(java.lang.Long)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public long getCurrencyConversionId(final long currencyId ){
		long currencyConversionId = 0;
		final DetachedCriteria criteria = DetachedCriteria.forClass(CurrencyConversion.class);
		criteria.add(Restrictions.eq("fromCurrencyId", currencyId));
		criteria.add(Restrictions.eq("toCurrencyId", 1l));
		final List<CurrencyConversion> currencyConversionLst = findByCriteria(criteria);
		if(currencyConversionLst != null && currencyConversionLst.size() > 0){
			currencyConversionId = currencyConversionLst.get(0).getCurrencyConversionId();
		}
		return currencyConversionId;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#getProposalOptionsById(java.lang.Long)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public long getConversionPeriodId(Date startDate , Date endDate){
		long  periodId = 0;
		final DetachedCriteria criteria = DetachedCriteria.forClass(lineItemTransPeriod.class);
		criteria.createAlias("transPeriodType", "transPeriodType");
		criteria.add(Restrictions.eq("transPeriodType.id", 1l));
		criteria.add(Restrictions.eq("startDate", startDate));
		criteria.add(Restrictions.eq("endDate", endDate));
		
		final List<lineItemTransPeriod> lineItemTransPeriodLst = findByCriteria(criteria);
		if(lineItemTransPeriodLst != null && lineItemTransPeriodLst.size() > 0){
			periodId = lineItemTransPeriodLst.get(0).getPeriodId();
		}
		
		return periodId;
	}
	
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#getProposalOptionsById(java.lang.Long)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public double getCurrencyConversionRate(final long CurrencyConversionId , final long  periodId){
		double currencyExchangeRate = 0.0;
		final DetachedCriteria criteria = DetachedCriteria.forClass(CurrencyConversionRate.class);
		criteria.createAlias("currencyConversion", "currencyConversion");
		criteria.createAlias("transPeriod", "transPeriod");
		criteria.add(Restrictions.eq("transPeriod.periodId", periodId));
		criteria.add(Restrictions.eq("currencyConversion.currencyConversionId", CurrencyConversionId));
		final List<CurrencyConversionRate> currencyConversionRateLst = findByCriteria(criteria);
		if(currencyConversionRateLst != null && currencyConversionRateLst.size() > 0){
			currencyExchangeRate = currencyConversionRateLst.get(0).getExchangeRate();
		}
		return currencyExchangeRate;
	}
}
