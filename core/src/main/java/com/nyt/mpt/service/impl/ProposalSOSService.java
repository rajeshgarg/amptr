/**
 * 
 */
package com.nyt.mpt.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nyt.mpt.dao.IProductDAO;
import com.nyt.mpt.dao.IProductDAOSOS;
import com.nyt.mpt.dao.IProposalDAOSOS;
import com.nyt.mpt.dao.ISalesTargetDAO;
import com.nyt.mpt.domain.Advertiser;
import com.nyt.mpt.domain.Agency;
import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.LineItemSalesTargetAssoc;
import com.nyt.mpt.domain.Product;
import com.nyt.mpt.domain.SalesTarget;
import com.nyt.mpt.domain.sos.ProductPlacement;
import com.nyt.mpt.service.IProposalSOSService;
import com.nyt.mpt.template.ReferenceDataMap;

/**
 * @author amandeep.singh
 *
 */
public class ProposalSOSService implements IProposalSOSService {

	private IProposalDAOSOS proposalDaoSOS;
	private IProductDAO productDao;
	private ISalesTargetDAO salesTargetDAO;
	private IProductDAOSOS productDaoSOS;
		
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalServiceSOS#getAdvertiser()
	 */
	@Override
	public List<Advertiser> getAdvertiser() {
		return proposalDaoSOS.getAdvertiser();
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalSOSService#getAdvertiserByName(java.lang.String)
	 */
	@Override
	public List<Advertiser> getAdvertiserByName(String advertiserName) {
		return proposalDaoSOS.getAdvertiserByName(advertiserName);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalSOSService#getAdvertiserByName(java.lang.String)
	 */
	@Override
	public List<Agency> getAgencyByName(String agencyName) {
		return proposalDaoSOS.getAgencyByName(agencyName);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalServiceSOS#getAdvertiserById(long)
	 */
	@Override
	public Advertiser getAdvertiserById(final long advertiserId) {
		return proposalDaoSOS.getAdvertiserById(advertiserId);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalServiceSOS#getAgency()
	 */
	@Override
	public List<Agency> getAgency() {
		return proposalDaoSOS.getAgency();
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalServiceSOS#getAgencyById(long)
	 */
	@Override
	public Agency getAgencyById(final long agencyId) {
		return proposalDaoSOS.getAgencyById(agencyId);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalServiceSOS#isProductPlacementActive(long, java.lang.Long[])
	 */
	@Override
	public boolean isProductPlacementActive(final long productId, final Long[] salesTargetID) {
		return proposalDaoSOS.isProductPlacementActive(productId, salesTargetID);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalServiceSOS#isSOSProductPlacementActive(long, java.lang.Long[])
	 */
	@Override
	public List<ProductPlacement> getActiveProductPlacement(final long productId, final Long[] salesTargetID) {
		return productDaoSOS.getActiveProductPlacement(productId, salesTargetID);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalServiceSOS#getReferenceDataMapFromLineItemList(java.util.List)
	 */
	@Override
	public ReferenceDataMap getReferenceDataMapFromLineItemList(final List<LineItem> lineItemsList) {
		final ReferenceDataMap referenceDataMap = new ReferenceDataMap();
		if (lineItemsList != null && !lineItemsList.isEmpty()) {
			final List<Long> salesTargetIdList = new ArrayList<Long>();
			final List<Long> productIdList = new ArrayList<Long>(lineItemsList.size());

			for (LineItem lineItem : lineItemsList) {
				final List<LineItemSalesTargetAssoc> salesTargetAssocsLst = lineItem.getLineItemSalesTargetAssocs();
				for (LineItemSalesTargetAssoc salesTargetAssoc : salesTargetAssocsLst) {
					salesTargetIdList.add(salesTargetAssoc.getSosSalesTargetId());
				}
				productIdList.add(lineItem.getSosProductId());
			}
			populateSalesTargetInRefernceDataMap(referenceDataMap, salesTargetIdList);
			populateProductInRefernceDataMap(referenceDataMap, productIdList);
		}
		return referenceDataMap;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalServiceSOS#getReferenceDataMapFromLineItemAssocList(java.util.List)
	 */
	@Override
	public ReferenceDataMap getReferenceDataMapFromLineItemAssocList(final List<LineItem> proposalLineItemLst) {
		final ReferenceDataMap referenceDataMap = new ReferenceDataMap();
		if (proposalLineItemLst != null && !proposalLineItemLst.isEmpty()) {
			final List<Long> salesTargetIdList = new ArrayList<Long>(proposalLineItemLst.size());
			final List<Long> productIdList = new ArrayList<Long>(proposalLineItemLst.size());

			for (LineItem lineItem : proposalLineItemLst) {
				final List<LineItemSalesTargetAssoc> salesTargetAssocsLst = lineItem.getLineItemSalesTargetAssocs();
				for (LineItemSalesTargetAssoc lineItemSalesTargetAssoc : salesTargetAssocsLst) {
					salesTargetIdList.add(lineItemSalesTargetAssoc.getSosSalesTargetId());
				}
				productIdList.add(lineItem.getSosProductId());
			}
			populateSalesTargetInRefernceDataMap(referenceDataMap, salesTargetIdList);
			populateProductInRefernceDataMap(referenceDataMap, productIdList);
		}
		return referenceDataMap;
	}
	
	/**
	 * Populates the SalesTarget Map of ReferenceDataMap
	 * 
	 * @param referenceDataMap
	 * @param salesTargetIdList
	 */
	private void populateSalesTargetInRefernceDataMap(ReferenceDataMap referenceDataMap, final List<Long> salesTargetIdList) {
		final List<SalesTarget> salesTargetList = salesTargetDAO.getActiveInActiveSalesTargetBySalesTargetIds(salesTargetIdList);
		for (SalesTarget salesTarget : salesTargetList) {
			referenceDataMap.getSalesTargetMap().put(salesTarget.getSalesTargetId(), salesTarget);
		}
	}

	/**
	 * Populates the Product Map of ReferenceDataMap
	 * 
	 * @param referenceDataMap
	 * @param productIdList
	 */
	private void populateProductInRefernceDataMap(ReferenceDataMap referenceDataMap, final List<Long> productIdList) {
		final List<Product> productList = productDao.getActiveInActiveProductsByProductIds(productIdList);
		for (Product product : productList) {
			referenceDataMap.getProductMap().put(product.getId(), product);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalServiceSOS#getReferenceDataMapFromLineItemAssocList(java.util.List)
	 */
	@Override
	public Map<Long, String> getCurrencies(){
		return proposalDaoSOS.getCurrencies();
	}
	
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalServiceSOS#getReferenceDataMapFromLineItemAssocList(java.util.List)
	 */
	@Override
	public Map <String,Double> getCurrencyConversionRate() throws ParseException{
		double conversionRate = 0;
		Map<Long, String> currencyMap = getCurrencies();
		Map <String,Double> conversionRateMap = new HashMap<String,Double>();		
		for (Long currencyId : currencyMap.keySet()) {
			conversionRate = 0;
			if(currencyId == 1){
				/* Setting conversion Rate as 1 for USD currency */
				conversionRate = 1;
			}else{
				long currencyConversionId = proposalDaoSOS.getCurrencyConversionId(currencyId);
				long periodId = 0;
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
				Calendar cal = Calendar.getInstance();
				
				for(int i =0 ; i < 24 ; i ++){
					cal.set(Calendar.DATE, 1);
					Date startDate =  (Date) dateFormat.parse(dateFormat.format(cal.getTime()));
					cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
					Date endDate = (Date) dateFormat.parse(dateFormat.format(cal.getTime()));
					periodId =  proposalDaoSOS.getConversionPeriodId(startDate, endDate);
					if(periodId > 0){
						conversionRate = proposalDaoSOS.getCurrencyConversionRate(currencyConversionId, periodId);
						if(conversionRate > 0){
							break;
						}
					}
					cal.add(Calendar.MONTH, -1);
				}
			}
			conversionRateMap.put(currencyMap.get(currencyId), conversionRate);
		}
		return conversionRateMap;
	}
	
	public void setProposalDaoSOS(final IProposalDAOSOS proposalDaoSOS) {
		this.proposalDaoSOS = proposalDaoSOS;
	}

	public void setProductDao(final IProductDAO productDao) {
		this.productDao = productDao;
	}

	public void setSalesTargetDAO(final ISalesTargetDAO salesTargetDAO) {
		this.salesTargetDAO = salesTargetDAO;
	}

	public void setProductDaoSOS(IProductDAOSOS productDaoSOS) {
		this.productDaoSOS = productDaoSOS;
	}

}