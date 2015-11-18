/**
 *
 */
package com.nyt.mpt.service.impl;

import java.util.List;

import com.nyt.mpt.dao.ISalesTargetAmptDAO;
import com.nyt.mpt.dao.ISalesTargetDAO;
import com.nyt.mpt.dao.ISalesTargetDAOSOS;
import com.nyt.mpt.domain.SalesTarget;
import com.nyt.mpt.domain.SalesTargetAmpt;
import com.nyt.mpt.service.ISalesTargetService;


/**
 * This service class is used for Sales Target operation.
 * 
 * @author amandeep.singh
 * 
 */
public class SalesTargetService implements ISalesTargetService {

	private ISalesTargetDAO salesTargetDAO;

	private ISalesTargetAmptDAO salesTargetAmptDAO;
	
	private ISalesTargetDAOSOS salesTargetDAOSOS;

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ISalesTargetService#getSalesTargetListByIDs(java.lang.Long[])
	 */
	@Override
	public List<SalesTarget> getSalesTargetListByIDs(Long[] id) {
		return salesTargetDAO.getSalesTargetListByIDs(id);
	}
	
	@Override
	public List<com.nyt.mpt.domain.sos.SalesTarget> getSOSSalesTargetListByIDs(Long[] id) {
		return salesTargetDAOSOS.getSalesTargetListByIDs(id);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ISalesTargetService#getSalesTarget(java.lang.Long[])
	 */
	@Override
	public List<SalesTargetAmpt> getSalesTarget(Long[] ids) {
		return salesTargetAmptDAO.getSalesTarget(ids);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ISalesTargetService#saveSalesTargets(java.util.List)
	 */
	@Override
	public void saveSalesTargets(List<SalesTargetAmpt> salesTargetList) {
		salesTargetAmptDAO.saveSalesTargets(salesTargetList);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ISalesTargetService#getSalesTarget()
	 */
	@Override
	public List<SalesTarget> getSalesTarget() {
		return salesTargetDAO.getSalesTarget();
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ISalesTargetService#getActiveSalesTargetBySTTypeId(java.lang.Long)
	 */
	@Override
	public List<SalesTarget> getActiveSalesTargetBySTTypeId(List<Long> salesTargetTypeIdLst) {
		return salesTargetDAO.getActiveSalesTargetBySTTypeId(salesTargetTypeIdLst);
	}
	
	
	@Override
	public String[] getCountryCodeByRegions(String[] regions) {
		return salesTargetDAO.getCountryCodeByRegions(regions);
	}
	
	@Override
	public SalesTarget getInactiveSalesTargetById(Long salesTargetTypeId) {
		return salesTargetDAO.getInactiveSalesTargetById(salesTargetTypeId);
	}

	public void setSalesTargetAmptDAO(final ISalesTargetAmptDAO salesTargetAmptDAO) {
		this.salesTargetAmptDAO = salesTargetAmptDAO;
	}

	public void setSalesTargetDAO(final ISalesTargetDAO salesTargetDAO) {
		this.salesTargetDAO = salesTargetDAO;
	}

	public void setSalesTargetDAOSOS(ISalesTargetDAOSOS salesTargetDAOSOS) {
		this.salesTargetDAOSOS = salesTargetDAOSOS;
	}

}
