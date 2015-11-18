/**
 * 
 */
package com.nyt.mpt.service.impl;

import java.util.Map;

import com.nyt.mpt.dao.ISOSDao;
import com.nyt.mpt.service.ISOSService;

/**
 * 
 * This service class is used to provide SOS information .
 * @author amandeep.singh
 *
 */
public class SOSService implements ISOSService {
	
	private ISOSDao sosDao;
	
	public void setSosDao(ISOSDao sosDao) {
		this.sosDao = sosDao;
	}

	@Override
	public Map<Long, String> getSalesCategories() {		
		return sosDao.getSalesCategories();
	}
	
	@Override
	public Map<String, Long> getSOSSalesCategories() {		
		return sosDao.getSOSSalesCategories();
	}
	
	@Override
	public Map<Long, String> getSalesCategoryById(long salesCategoryId) {		
		return sosDao.getSalesCategoryById(salesCategoryId);
	}
}
