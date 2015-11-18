/**
 * 
 */
package com.nyt.mpt.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.nyt.mpt.dao.IProductDAOSOS;
import com.nyt.mpt.domain.sos.Product;
import com.nyt.mpt.util.ConstantStrings;

/**
 * @author amandeep.singh
 *
 */
public class ProductDAOSOS extends GenericDAOImpl implements IProductDAOSOS {
	
	private static final String ID = "id";

	private static final String DISPLAY_NAME = "displayName";

	private static final Logger LOGGER = Logger.getLogger(ProductDAOSOS.class);

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAOSOS#getProductListByIDs(java.lang.Long[])
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<Product> getProductListByIDs(Long[] ids) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching Product list by Ids - " + ids);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(Product.class);
		criteria.add(Restrictions.eq(ConstantStrings.ACTIVE, true));
		criteria.add(Restrictions.in(ID, ids));
		criteria.addOrder(Order.asc(DISPLAY_NAME).ignoreCase());
		return findByCriteria(criteria);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAOSOS#isSOSProductPlacementActive(long, long)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<com.nyt.mpt.domain.sos.ProductPlacement> getActiveProductPlacement(final long productId, Long[] salesTargetID) {
		final DetachedCriteria criteria = DetachedCriteria.forClass(com.nyt.mpt.domain.sos.ProductPlacement.class);
		criteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("product.id", productId));
		criteria.add(Restrictions.in("salesTarget.salesTargetId", salesTargetID));
		criteria.add(Restrictions.eq(ConstantStrings.ACTIVE, true));
		return findByCriteria(criteria);
	}

}
