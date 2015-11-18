/**
 * 
 */
package com.nyt.mpt.rateProfile;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.nyt.mpt.common.AbstractTest;
import com.nyt.mpt.domain.RateConfig;
import com.nyt.mpt.domain.RateProfile;
import com.nyt.mpt.domain.RateProfileSeasonalDiscounts;
import com.nyt.mpt.service.IRateProfileService;

/**
 * test cases for rate profile.
 * 
 * @author megha.vyas
 *
 */
public class RateProfileTest extends AbstractTest {
	@Autowired
	@Qualifier("rateProfileService")
	
	private IRateProfileService rateProfileService;
	private RateProfile rateProfileDB;
	
		  
	@Before 
	public void setup(){		
		super.setAuthenticationInfo();
		List<RateProfile> rateProfileLst = rateProfileService.getFilteredRateProfileList(null, null, null);		
		if(rateProfileLst != null && rateProfileLst.size() > 0){
			this.rateProfileDB = rateProfileLst.get(0);		
		}
	}
	
	@Test
	public void testCreateRateProfile(){
		if(rateProfileDB != null){
		RateProfile rateProfile = rateProfileService.createRateProfile(createRateProfile());
		Assert.assertTrue(rateProfile.getProfileId() > 0);
	}
	}
	
	@Test
	public void testGetRateProfileById(){
		if(rateProfileDB != null){
		RateProfile rateProfile= rateProfileService.getRateProfileById(rateProfileDB.getProfileId());
		Assert.assertTrue(rateProfile.isActive());
	}
	}
	
	
	@Test
	public void testUpdateRateProfile() {
		if(rateProfileDB != null){
			rateProfileDB.setBasePrice(1000);
			RateProfile rateProfile=new RateProfile();
			rateProfile= rateProfileService.updateRateProfile(rateProfileDB, true);
		Assert.assertTrue(rateProfile.getBasePrice() == 1000);
	}
	}
	
	@Test 
	public void testDeleteRateProfile(){
		if(rateProfileDB != null){
			rateProfileService.deleteRateProfile(rateProfileDB);
			Assert.assertTrue(!rateProfileDB.isActive());
		}
	}
	
	@Test
	public void testGetRateProfileBySalescategory(){
		if(rateProfileDB != null){
			List<RateProfile> listBySalesCategory= rateProfileService.getRateProfilesBySalesCategory(rateProfileDB.getSalesCategoryId());
			Assert.assertTrue(listBySalesCategory.size() > 0);
		}
	}
	
	@Test
	public void testIsDuplicateRateProfile(){
		if(rateProfileDB != null){
			Long salesTargetIds[] = new Long[1]; 
			Set<RateConfig> rateConfigSet = rateProfileDB.getRateConfigSet();
			for (RateConfig rateConfigDb : rateConfigSet) {
				salesTargetIds[0] = (rateConfigDb.getSalesTargetId());			
				break;
			}		
			boolean value = rateProfileService.isDuplicateRateProfile(rateProfileDB, salesTargetIds);
			Assert.assertTrue(!value);
			
			RateProfile rateProfile = createRateProfile();
			rateProfile.setSalesCategoryId(rateProfileDB.getSalesCategoryId());
			rateProfile.setSalesCategoryName(rateProfileDB.getSalesCategoryName());
			rateProfile.setProductId(rateProfileDB.getProductId());
			rateProfile.setProductName(rateProfileDB.getProductName());
			rateProfile.setRateConfigSet(rateProfileDB.getRateConfigSet());
			value = rateProfileService.isDuplicateRateProfile(rateProfile, salesTargetIds);
			Assert.assertTrue(value);
		}
	}
	
	@Test 
	public void testCreateCloneRateProfileForSalesCategory(){
		if(rateProfileDB != null){
			Long targetSalesCategoryId= 0L; 
			Set<RateConfig> rateConfigSet = rateProfileDB.getRateConfigSet();
			for (RateConfig rateConfigDb : rateConfigSet) {
				targetSalesCategoryId = rateConfigDb.getSalesTargetId();			
				break;
			}	
		    rateProfileService.createCloneRateProfileForSalesCategory(rateProfileDB.getSalesCategoryId(), targetSalesCategoryId, rateProfileDB.getSalesCategoryName());
			Assert.assertTrue(true);
		}
		
	}
	
	@Test
	public void testGetFilteredRateProfileList(){
		Assert.assertTrue(rateProfileService.getFilteredRateProfileList(null, null, null).size() >= 0);
	}
	
	@Test 
	public void testGetFilteredRateProfileListCount(){
		Assert.assertTrue(rateProfileService.getFilteredRateProfileListCount(null) >= 0);
	}
	
	
	@Test
	public void testCreateCopySeasonalDiscounts(){
		Long discountIds [] = null;
		Long targetProfileIds [] = new Long [1];
		RateProfile rateProfileWithSeasonalDiscount = null;
		List<RateProfile> rateProfileLst = rateProfileService.getFilteredRateProfileList(null, null, null);
		for (RateProfile rateProfile : rateProfileLst) {
			if(rateProfileWithSeasonalDiscount == null && rateProfile.getSeasonalDiscountsLst() != null && !rateProfile.getSeasonalDiscountsLst().isEmpty()){
				rateProfileWithSeasonalDiscount = rateProfile;
				discountIds = new Long [rateProfile.getSeasonalDiscountsLst().size()];
				int i=0;
				for (RateProfileSeasonalDiscounts seasonalDiscount : rateProfile.getSeasonalDiscountsLst()) {
					discountIds [i++] = seasonalDiscount.getDiscountId();
				}
			}else{
				targetProfileIds[0] = rateProfile.getProfileId();
				if(rateProfileWithSeasonalDiscount != null){
					break;
				}
			}
		}
		rateProfileService.createCopySeasonalDiscounts(discountIds, targetProfileIds);
		Assert.assertTrue(true);
	}
	
	@Test
	public void testGetProfileSeasonalDiscountLst(){
		RateProfile rateProfileWithSeasonalDiscount = null;
		List<RateProfile> rateProfileLst = rateProfileService.getFilteredRateProfileList(null, null, null);
		for (RateProfile rateProfile : rateProfileLst) {
			if(rateProfile.getSeasonalDiscountsLst() != null && !rateProfile.getSeasonalDiscountsLst().isEmpty()){
				rateProfileWithSeasonalDiscount = rateProfile;
				break;
			}
		}
		Assert.assertFalse(rateProfileService.getProfileSeasonalDiscountLst(rateProfileWithSeasonalDiscount.getProfileId()).isEmpty());
	}
	
	@Test
	public void testGetRateProfilesBySalesCategoryAndProduct(){
		if(rateProfileDB != null){
			Assert.assertFalse(rateProfileService.getRateProfilesBySalesCategoryAndProduct(rateProfileDB.getSalesCategoryId(), rateProfileDB.getProductId()).isEmpty());
		}
	}
		
	private RateProfile createRateProfile(){
		RateProfile rateProfileNew = new RateProfile();
		rateProfileNew.setProfileId(0L);
		rateProfileNew.setSalesCategoryId(null);
		rateProfileNew.setSalesCategoryName("");
		rateProfileNew.setProductId(rateProfileDB.getProductId());
		rateProfileNew.setProductName(rateProfileDB.getProductName());
		
		Set<RateConfig> rateConfigSet = new LinkedHashSet<RateConfig>();
		RateConfig rconfig = new RateConfig();
		rconfig.setConfigId(0);
		rconfig.setRateProfile(rateProfileNew);		
		for (RateConfig rateConfigDb : rateProfileDB.getRateConfigSet()) {
			rconfig.setSalesTargetId(rateConfigDb.getSalesTargetId());
			rconfig.setSalesTargetName(rateConfigDb.getSalesTargetName());
			break;
		}
		rateConfigSet.add(rconfig);
		rateProfileNew.setRateConfigSet(rateConfigSet);
		rateProfileNew.setBasePrice(200);
		rateProfileNew.setNotes("2423");		
		rateProfileNew.setVersion(0);
		rateProfileNew.setSectionNames(rateProfileDB.getSectionNames());
		return rateProfileNew;
	}
	

}
