/**
 *
 */
package com.nyt.mpt.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import com.nyt.mpt.domain.Advertiser;
import com.nyt.mpt.domain.Agency;
import com.nyt.mpt.domain.CampaignObjective;
import com.nyt.mpt.domain.CountryRegionMap;
import com.nyt.mpt.domain.EmailSchedule;
import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.LineItemReservations;
import com.nyt.mpt.domain.LineItemSalesTargetAssoc;
import com.nyt.mpt.domain.LineItemTarget;
import com.nyt.mpt.domain.Product;
import com.nyt.mpt.domain.Proposal;
import com.nyt.mpt.domain.ProposalOption;
import com.nyt.mpt.domain.ProposalVersion;
import com.nyt.mpt.domain.Role;
import com.nyt.mpt.domain.SalesTarget;
import com.nyt.mpt.domain.SalesTargetAmpt;
import com.nyt.mpt.domain.SalesTargetType;
import com.nyt.mpt.domain.TemplateMetaData;
import com.nyt.mpt.domain.User;
import com.nyt.mpt.domain.sos.OrderLineItem;
import com.nyt.mpt.form.LineItemForm;
import com.nyt.mpt.form.ProductForm;
import com.nyt.mpt.service.IEmailScheduleService;
import com.nyt.mpt.service.IProductService;
import com.nyt.mpt.service.IProposalSOSService;
import com.nyt.mpt.service.IProposalService;
import com.nyt.mpt.service.ISOSService;
import com.nyt.mpt.service.ISalesTargetService;
import com.nyt.mpt.service.ITargetingService;
import com.nyt.mpt.service.ITemplateService;
import com.nyt.mpt.service.IUserService;
import com.nyt.mpt.util.enums.LineItemProductTypeEnum;
import com.nyt.mpt.util.enums.ReservationStatus;
import com.nyt.mpt.util.enums.Weekdays;
import com.nyt.mpt.util.security.SecurityUtil;

/**
 * This class is used as proposal helper for proposal related operations
 * @author surendra.singh
 */
public class ProposalHelper {

	private IProposalService proposalService;

	private IUserService userService;

	private IProductService productService;

	private ISOSService sosService;

	private ITemplateService templateService;

	private IProductService adProductService;

	private ISalesTargetService salesTargetService;

	private IProposalSOSService proposalSOSService;

	private ITargetingService targetingService;

	private static final String ZIP_CODES = "Zip Codes";

	private MailUtil mailUtil;

	private String applicationURL;
	
	private IEmailScheduleService emailScheduleService;

	/**
	 * Fetch all the sales categories
	 * @return
	 */
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public Map<Long, String> getSalesTarget() {
		final List<SalesTarget> salesTargets = salesTargetService.getSalesTarget();
		if (salesTargets == null || salesTargets.isEmpty()) {
			return Collections.EMPTY_MAP;
		} else {
			final Map<Long, String> salesTargetMap = new LinkedHashMap<Long, String>(salesTargets.size());
			for (SalesTarget salesTarget : salesTargetService.getSalesTarget()) {
				salesTargetMap.put(salesTarget.getSalesTargetId(), salesTarget.getSalesTargeDisplayName());
			}
			return salesTargetMap;
		}
	}

	/**
	 * @return
	 */
	public Map<Long, String> getSalesCategory() {
		return sosService.getSalesCategories();
	}

	/**
	 * This method is used to get all the available templates from database
	 * While generating media plan, User can select one of the available
	 * templates
	 * @return
	 */
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public Map<Long, String> getProposalTemplate() {
		final List<TemplateMetaData> metaDatas = templateService.getActiveMediaPlanTemplates();
		if (metaDatas == null || metaDatas.isEmpty()) {
			return Collections.EMPTY_MAP;
		} else {
			final Map<Long, String> templateMap = new LinkedHashMap<Long, String>(metaDatas.size());
			for (TemplateMetaData mediaTemplate : metaDatas) {
				templateMap.put(mediaTemplate.getTemplateId(), mediaTemplate.getTemplateFileName());
			}
			return templateMap;
		}
	}

	/**
	 * This method is used to get all predefined campaign objectives from
	 * database User can select one campaign objective from drop down or can
	 * write custom objective
	 * @return
	 */
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public Map<Long, String> getCampainObjectives() {
		final List<CampaignObjective> objectives = proposalService.getCampaignObjectives();
		if (objectives == null || objectives.isEmpty()) {
			return Collections.EMPTY_MAP;
		} else {
			final Map<Long, String> objectiveMap = new LinkedHashMap<Long, String>(objectives.size());
			for (CampaignObjective campaignObjective : objectives) {
				objectiveMap.put(campaignObjective.getCmpObjId(), campaignObjective.getCmpObjText());
			}
			return objectiveMap;
		}
	}

	/**
	 * Return Array of campaign objectives for a Proposal
	 * @param proposalId
	 * @return
	 */
	public long[] getCampaignObjectivesByProposalId(final long proposalId) {
		final Set<CampaignObjective> campaignObj = proposalService.getCampaignObjectivesByProposalId(proposalId);
		if (campaignObj == null || campaignObj.isEmpty()) {
			return new long[0];
		} else {
			int index = 0;
			final long[] campaignObjective = new long[campaignObj.size()];
			for (CampaignObjective objective : campaignObj) {
				campaignObjective[index++] = objective.getCmpObjId();
			}
			return campaignObjective;
		}
	}

	/**
	 * Fetch all the advertiser info
	 * @return
	 */
	public Map<Long, String> getAdvertiser() {
		final Map<Long, String> advertiserMap = new LinkedHashMap<Long, String>();
		for (Advertiser advertiser : proposalSOSService.getAdvertiser()) {
			advertiserMap.put(advertiser.getId(), advertiser.getName());
		}
		return advertiserMap;
	}

	/**
	 * Fetch all the advertiser based on name
	 * @param advertiserName
	 * @return
	 */
	public List<KeyValuePairPojo> getAdvertiserByName(final String advertiserName) {
		final List<KeyValuePairPojo> advertisers = new ArrayList<KeyValuePairPojo>();
		if(StringUtils.isNotBlank(advertiserName)){
			for (Advertiser advertiser : proposalSOSService.getAdvertiserByName(advertiserName.trim())) {
				advertisers.add(new KeyValuePairPojo(advertiser.getId(), advertiser.getName()));
			}
		}
		return advertisers;
	}

	/**
	 * Fetch all Agency info
	 * @return
	 */
	public Map<Long, String> getAgency() {
		final Map<Long, String> agencyMap = new LinkedHashMap<Long, String>();
		for (Agency agency : proposalSOSService.getAgency()) {
			agencyMap.put(agency.getId(), agency.getName());
		}
		return agencyMap;
	}

	/**
	 * Fetch all the agency based on name
	 * @param advertiserName
	 * @return
	 */
	public List<KeyValuePairPojo> getAgencyByName(final String agencyName) {
		final List<KeyValuePairPojo> advertisers = new ArrayList<KeyValuePairPojo>();
		if(StringUtils.isNotBlank(agencyName)){
			for (Agency agency : proposalSOSService.getAgencyByName(agencyName.trim())) {
				advertisers.add(new KeyValuePairPojo(agency.getId(), agency.getName()));
			}
		}
		return advertisers;
	}

	/**
	 * @return
	 */
	public Map<Long, String> getUsers() {
		final Map<Long, String> userMap = new LinkedHashMap<Long, String>();
		for (User user : userService.getUserList()) {
			userMap.put(user.getUserId(), user.getUsername());
		}
		return userMap;
	}

	/**
	 * @return
	 */
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public Map<Long, String> getUsersBasedOnRole(final String[] proposalAssignTo) {
		final List<User> users = userService.getUserBasedOnRoleList(proposalAssignTo);
		if (users == null || users.isEmpty()) {
			return Collections.EMPTY_MAP;
		} else {
			final Map<Long, String> userMap = new LinkedHashMap<Long, String>(users.size());
			for (User user : users) {
				userMap.put(user.getUserId(), user.getFullName());
			}
			return userMap;
		}
	}

	/**
	 * Return a map of role and all active users have same role
	 *  
	 * e.g - <"Media Planner", 	<<100, "ABC">, <101, "XYZ">>
	 * 		 <"Planner", 		<<102, "LMN">, <103, "PQR">>
	 * @return
	 */
	public Map<String, Map<Long, String>> getProposalReviewer(final String[] proposalReviewer) {
		final Map<String, Map<Long, String>> roleUserMap = new TreeMap<String, Map<Long, String>>();
		final List<User> userList = userService.getUserBasedOnRoleList(proposalReviewer);
		for (User user : userList) {
			final Role role = (Role) user.getUserRoles().iterator().next();
			if (roleUserMap.containsKey(role.getDisplayName())) {
				roleUserMap.get(role.getDisplayName()).put(user.getUserId(), user.getFullName());
			} else {
				final Map<Long, String> userMap = new LinkedHashMap<Long, String>();
				userMap.put(user.getUserId(), user.getFullName());
				roleUserMap.put(role.getDisplayName(), userMap);
			}
		}
		return roleUserMap;
	}

	/**
	 * @param optionId
	 * @return
	 */
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public Map<Long, Long> getVersions(final long optionId) {
		final Map<Long, Long> versionMap = new LinkedHashMap<Long, Long>();
		final ProposalOption option = proposalService.getOptionbyId(optionId);
		if (option == null) {
			return Collections.EMPTY_MAP;
		} else {
			for (ProposalVersion proposalVersion : option.getProposalVersions()) {
				versionMap.put(proposalVersion.getProposalVersion(), proposalVersion.getProposalVersion());
			}
			return versionMap;
		}
	}

	/**
	 * @return
	 */
	public Map<Long, String> getAllProducts() {
		final Map<Long, String> productMap = new LinkedHashMap<Long, String>();
		for (Product product : productService.getAllProducts()) {
			productMap.put(product.getId(), product.getDisplayName());
		}
		return productMap;
	}

	public Map<String, List<ProductForm>> getAllProductsMap() {
		final Map<String, List<ProductForm>> productClassMap = new TreeMap<String, List<ProductForm>>();
		for (Product product : productService.getAllProducts()) {
			final ProductForm prForm = new ProductForm();
			prForm.setProductId(product.getId());
			prForm.setDisplayName(product.getDisplayName());
			prForm.setReservable((ConstantStrings.YES.equals(product.getReservable())) ? ConstantStrings.YES : ConstantStrings.NO);
			if (productClassMap.containsKey(product.getClassName().getProductClassName())) {
				productClassMap.get(product.getClassName().getProductClassName()).add(prForm);
			} else {
				final List<ProductForm> productFormList = new ArrayList<ProductForm>();
				productFormList.add(prForm);
				productClassMap.put(product.getClassName().getProductClassName(), productFormList);
			}
		}
		return productClassMap;
	}
	
	public Map<String, List<ProductForm>> getSalesCalendarProductsMap() {
		final Map<String, List<ProductForm>> productClassMap = new TreeMap<String, List<ProductForm>>();
		for (Product product : productService.getAllProducts()) {			
			if((("HOME PAGE").equals(product.getClassName().getProductClassName()) || ("Display Cross Platform").equals(product.getClassName().getProductClassName())) && ConstantStrings.YES.equals(product.getReservable())){
				final ProductForm prForm = new ProductForm();
				prForm.setProductId(product.getId());
				prForm.setDisplayName(product.getDisplayName());
				if (productClassMap.containsKey(product.getClassName().getProductClassName())) {
					productClassMap.get(product.getClassName().getProductClassName()).add(prForm);
				} else {
					final List<ProductForm> productFormList = new ArrayList<ProductForm>();
					productFormList.add(prForm);
					productClassMap.put(product.getClassName().getProductClassName(), productFormList);
				}
			}
		}
		return productClassMap;
	}
	
	/**
	 * Returns the map of countries based on regions
	 * @return
	 */
	public Map<String, List<KeyValuePairPojo>> getCountriesBasedOnRegions() {
		final Map<String, List<KeyValuePairPojo>> countryRegionMap = new TreeMap<String, List<KeyValuePairPojo>>();
		for (CountryRegionMap countryRegion : targetingService.getAllCountryRegionData()) {			
				final KeyValuePairPojo countryForm = new KeyValuePairPojo(countryRegion.getId().getCountryId(),countryRegion.getCountryName());
				if (countryRegionMap.containsKey(countryRegion.getRegioName())) {
					countryRegionMap.get(countryRegion.getRegioName()).add(countryForm);
				} else {
					final List<KeyValuePairPojo> countriesList = new ArrayList<KeyValuePairPojo>();
					countriesList.add(countryForm);
					countryRegionMap.put(countryRegion.getRegioName(), countriesList);
				}
		}
		
		for(String countryRegion : countryRegionMap.keySet()){
			Collections.sort(countryRegionMap.get(countryRegion), new CountryNameComparator());
		}
		return countryRegionMap;
	}
	
	/**
	 * get date Map in form of Month Year
	 * @return
	 */
	public Map<String,String> getDatesMap(){
		final Map<String,String> datesMap = new LinkedHashMap <String,String>();
		String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
		Calendar cal = Calendar.getInstance();
		int start_point = 0;
		int end_point = 24;
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + start_point);
		while (start_point <= end_point){			
			datesMap.put(monthNames[cal.get(Calendar.MONTH)] + ConstantStrings.SPACE + cal.get(Calendar.YEAR), monthNames[cal.get(Calendar.MONTH)] + ConstantStrings.SPACE + cal.get(Calendar.YEAR));
			cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
			start_point++;
		}
		return datesMap;
	}
	
	/**
	 * get weekday Map in starting from Sunday
	 * @return
	 */
	public Map<String,String> getWeekdayMap(){
		String[] weekdayNames = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday","Sunday"};
		Map<String, String> map = new LinkedHashMap<String, String>();
		for(int i=0;i<weekdayNames.length;i++) {
			map.put(weekdayNames[i],weekdayNames[i]);
		}
		return map;
	}
	
	/**
	 * seperated for Email products
	 * @return
	 */
	public Map<String, List<ProductForm>> getAllProductsByClassMap() {
		final Map<String, List<ProductForm>> productClassMap = new TreeMap<String, List<ProductForm>>();
		for (Product product : productService.getAllProducts()) {
			final ProductForm prForm = new ProductForm();
			prForm.setProductId(product.getId());
			prForm.setDisplayName(product.getDisplayName());
			prForm.setIsViewable(product.getIsViewable());
			if(13 == product.getAvailsSytemId() && ConstantStrings.YES.equals(product.getReservable())){//For Email products
				prForm.setReservable("E");
			}else{
				prForm.setReservable((ConstantStrings.YES.equals(product.getReservable())) ? ConstantStrings.YES : ConstantStrings.NO);
			}
			if (productClassMap.containsKey(product.getClassName().getProductClassName())) {
				productClassMap.get(product.getClassName().getProductClassName()).add(prForm);
			} else {
				final List<ProductForm> productFormList = new ArrayList<ProductForm>();
				productFormList.add(prForm);
				productClassMap.put(product.getClassName().getProductClassName(), productFormList);
			}
		}
		return productClassMap;
	}
	
	/**
	 * seperated for Fetch Avails products
	 * @return
	 */
	public Map<String, List<ProductForm>> getAllProductsMapByAvailsId() {
		final Map<String, List<ProductForm>> productClassMap = new TreeMap<String, List<ProductForm>>();
		for (Product product : productService.getAllProducts()) {
			final ProductForm prForm = new ProductForm();
			prForm.setProductId(product.getId());
			prForm.setDisplayName(product.getDisplayName());
			if(15 == product.getAvailsSytemId()){//For DFP
				prForm.setTypeName("DFP");
			}else if(16 == product.getAvailsSytemId()){//For Both
				prForm.setTypeName("BOTH");
			}
			prForm.setReservable((ConstantStrings.YES.equals(product.getReservable())) ? ConstantStrings.YES : ConstantStrings.NO);
			if (productClassMap.containsKey(product.getClassName().getProductClassName())) {
				productClassMap.get(product.getClassName().getProductClassName()).add(prForm);
			} else {
				final List<ProductForm> productFormList = new ArrayList<ProductForm>();
				productFormList.add(prForm);
				productClassMap.put(product.getClassName().getProductClassName(), productFormList);
			}
		}
		return productClassMap;
	}

	/**
	 * @return
	 */
	public Map<Long, String> getReservableProducts(final boolean isReservable) {
		final Map<Long, String> productMap = new LinkedHashMap<Long, String>();
		for (Product product : productService.getAllReservableProducts(isReservable)) {
			productMap.put(product.getId(), product.getDisplayName());
		}
		return productMap;
	}

	/**
	 * @return
	 */
	public Map<Long, String> getTargetTypeCriteria() {
		return targetingService.getTargetTypeCriteria();
	}

	/**
	 * @param sosTarTypeId
	 * @return
	 */
	public Map<Long, String> getTargetTypeElement(final String sosTarTypeId) {
		return targetingService.getTargetTypeElement(Long.valueOf(sosTarTypeId));
	}

	/**
	 * @return
	 */
	public Map<Long, String> getProductClass() {
		return productService.getProductClass();
	}

	/**
	 * @return
	 */
	public Map<String, Long> getProductsClassMap() {
		return productService.getProductsClassMap();
	}
	
	/**
	 * @return
	 */
	public Map<Long, String> getAllSalesTargetType() {
		final Map<Long, String> salesTargetTypeMap = new LinkedHashMap<Long, String>();
		for (SalesTargetType salesTargetType : productService.getAllSalesTargetType()) {
			salesTargetTypeMap.put(salesTargetType.getSalestargetTypeId(), salesTargetType.getSalesTargetTypeName());
		}
		return salesTargetTypeMap;
	}

	/**
	 * @param targetTypeId
	 * @return
	 */
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public Map<Long, String> getAllSalesTargetForTargetType(final long targetTypeId) {
		final List<SalesTarget> salestargetLst = productService.getSalesTargetByType(targetTypeId);
		if (salestargetLst == null || salestargetLst.isEmpty()) {
			return Collections.EMPTY_MAP;
		} else {
			final Map<Long, String> salesTargetMap = new LinkedHashMap<Long, String>(salestargetLst.size());
			for (SalesTarget salesTarget : salestargetLst) {
				salesTargetMap.put(salesTarget.getSalesTargetId(), salesTarget.getSalesTargeDisplayName());
			}
			return salesTargetMap;
		}
	}

	/**
	 * @param salesTargetID
	 * @return
	 */
	public Map<Long, String> getAllProductsForSalesTarget(final long salesTargetID) {
		return productService.getAllProductsForSalesTarget(salesTargetID);
	}

	/**
	 * @param saleTargetTypeIds
	 * @return
	 */
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public Map<Long, String> getAllSalesTargetForMultiTargetType(final Long[] saleTargetTypeIds) {
		final List<SalesTarget> salestargetLst = productService.getAllSalesTargetForMultiTargetType(saleTargetTypeIds);
		if (salestargetLst == null || salestargetLst.isEmpty()) {
			return Collections.EMPTY_MAP;
		} else {
			final Map<Long, String> salesTargetMap = new LinkedHashMap<Long, String>(salestargetLst.size());
			for (SalesTarget salesTarget : salestargetLst) {
				salesTargetMap.put(salesTarget.getSalesTargetId(), salesTarget.getSalesTargeDisplayName());
			}
			return salesTargetMap;
		}
	}

	/**
	 * @param productId
	 * @return
	 */
	public Map<Long, String> getAllSalesTargetForMultiProduct(final List<Long> productIdLst) {
		final Map<Long, String> salesTargetMap = new LinkedHashMap<Long, String>();
		if (productIdLst != null && !productIdLst.isEmpty()) {
			final List<SalesTarget> salestargetLst = productService.getAllSalesTargetForMultiProduct(productIdLst);
			for (SalesTarget salesTarget : salestargetLst) {
				salesTargetMap.put(salesTarget.getSalesTargetId(), salesTarget.getSalesTargeDisplayName());
			}
		}
		return salesTargetMap;
	}

	
	public Map<Long, String> getAllSalesTargetForProductID(final Long productId) {
		final Map<Long, String> salesTargetMap = new LinkedHashMap<Long, String>();
		if (productId != null) {
			final List<SalesTarget> salestargetLst = productService.getSalesTargetFromProductID(productId);
			for (SalesTarget salesTarget : salestargetLst) {
				salesTargetMap.put(salesTarget.getSalesTargetId(), salesTarget.getSalesTargeDisplayName());
			}
		}
		return salesTargetMap;
	}
	
	
	/**
	 * This method is used get sales target and sales target type map based on
	 * sales target type Key will be sales target id and value will be sales
	 * target type
	 * @param targetTypeIds
	 * @return
	 */
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public Map<Long, Long> getSalesTargetTypeAssocMap(final Long[] targetTypeIds) {
		final List<SalesTarget> salestargetLst = productService.getAllSalesTargetForMultiTargetType(targetTypeIds);
		if (salestargetLst == null || salestargetLst.isEmpty()) {
			return Collections.EMPTY_MAP;
		} else {
			final Map<Long, Long> salesTargetMap = new LinkedHashMap<Long, Long>();
			for (SalesTarget salesTarget : salestargetLst) {
				salesTargetMap.put(salesTarget.getSalesTargetId(), salesTarget.getSalesTargetType().getSalestargetTypeId());
			}
			return salesTargetMap;
		}
	}

	/**
	 * This method is used to get comma separated sales target name
	 * @param salesTargetMap
	 * @param bean
	 * @return
	 */
	public String getSalesTargetNameFromSalesTargetAssocList(final Map<Long, SalesTarget> salesTargetMap, final LineItem bean) {
		final Set<Long> salesTargetIdSet = convertSalesTargetAssocsToIdsSet(bean.getLineItemSalesTargetAssocs());
		if (salesTargetIdSet == null || salesTargetIdSet.isEmpty()) {
			return null;
		} else {
			final StringBuffer targetTypeName = new StringBuffer();
			for (Long salesTargetId : salesTargetIdSet) {
				if (salesTargetId != null && salesTargetMap.get(salesTargetId) != null) {
					if (targetTypeName.length() != 0) {
						targetTypeName.append(ConstantStrings.COMMA);
					}
					targetTypeName.append(salesTargetMap.get(salesTargetId).getSalesTargeDisplayName());
				}
			}
			return targetTypeName.toString();
		}
	}

	/**
	 * Used to check sales target active status, return true if all the sales
	 * target are in active status
	 * @param salesTargetMap
	 * @param bean
	 * @return
	 */
	public boolean getSalesTargetStatusFromSalesTargetAssocList(final Map<Long, SalesTarget> salesTargetMap, final LineItem lineItem) {
		boolean activeStatus = true;
		if (lineItem.getLineItemSalesTargetAssocs() != null && !lineItem.getLineItemSalesTargetAssocs().isEmpty()) {
			for (LineItemSalesTargetAssoc lineItemSalesTargetAssoc : lineItem.getLineItemSalesTargetAssocs()) {
				if (lineItemSalesTargetAssoc.getSosSalesTargetId() != null && salesTargetMap.get(lineItemSalesTargetAssoc.getSosSalesTargetId()) != null) {
					if ((!Constants.ACTIVE_STATUS.equalsIgnoreCase(salesTargetMap.get(lineItemSalesTargetAssoc.getSosSalesTargetId()).getStatus()))
							|| (salesTargetMap.get(lineItemSalesTargetAssoc.getSosSalesTargetId()).getDeleteDate() != null)) {

						activeStatus = false;
					}
				}
			}
		}
		return activeStatus;
	}

	/**
	 * Used to convert sales target association list into sales target ids array
	 * @param targetAssocsList
	 * @return
	 */
	public Long[] convertSalesTargetAssocsToIds(final List<LineItemSalesTargetAssoc> targetAssocsList) {
		final Long[] salesTargetIds = new Long[targetAssocsList.size()];
		int index = 0;
		for (LineItemSalesTargetAssoc targetAssoc : targetAssocsList) {
			if (targetAssoc.getSosSalesTargetId() != null) {
				salesTargetIds[index++] = targetAssoc.getSosSalesTargetId();
			}
		}
		return salesTargetIds;
	}

	/**
	 * Used to convert sales target association list into sales target id's array
	 * @param targetAssocs
	 * @return
	 */
	public Set<Long> convertSalesTargetAssocsToIdsSet(final List<LineItemSalesTargetAssoc> targetAssocs) {
		final Set<Long> salesTargetIdSet = new HashSet<Long>();
		for (LineItemSalesTargetAssoc targetAssoc : targetAssocs) {
			salesTargetIdSet.add(targetAssoc.getSosSalesTargetId());
		}
		return salesTargetIdSet;
	}

	/**
	 * @param saleTargetTypeLst
	 * @return
	 */
	public Map<Long, String> getAllProductsForMultiSalesTarget(final List<Long> saleTargetTypeLst) {
		return productService.getAllProductsForMultiSalesTarget(saleTargetTypeLst);
	}

	/**
	 * This method is used to get proposal level discount
	 * @param lineItemLst
	 * @return
	 */
	public double getProposalLevelDiscount(final List<LineItem> lineItemLst) {
		if (lineItemLst == null || lineItemLst.isEmpty()) {
			return 0;
		}
		double lineItemRateCardInvestment = 0.0;
		double lineItemCMPInvestment = 0.0;
		for (LineItem lineItem : lineItemLst) {
			if (lineItem.getRateCardPrice() == null || lineItem.getRateCardPrice().doubleValue() == 0) {
				return 0;
			} else {
				lineItemRateCardInvestment = lineItemRateCardInvestment + lineItem.getRateCardPrice() * lineItem.getImpressionTotal();
				lineItemCMPInvestment = lineItemCMPInvestment + lineItem.getRate() * lineItem.getImpressionTotal();

			}
		}
		if (lineItemRateCardInvestment > lineItemCMPInvestment) {
			return ((lineItemRateCardInvestment - lineItemCMPInvestment) / lineItemRateCardInvestment) * 100;
		} else {
			return 0;
		}
	}

	/**
	 * This method is used to get the product placement for a line item
	 * @param productId
	 * @param salesTargetIds
	 * @param lineItemId
	 * @return
	 */
	public String getProductPlacement(final Long productId, final Long[] salesTargetIds, final Long lineItemId) {
		final List<Long> salesTargetList = new ArrayList<Long>(1);
		salesTargetList.add(salesTargetIds[0]);
		final String productPlacement = adProductService.getLineItemProductPlacement(productId, salesTargetList);
		if (StringUtils.isEmpty(productPlacement)) {
			return ConstantStrings.NA;
		}
		final Long[] newSalesTargetIds = new Long[salesTargetIds.length - 1];
		System.arraycopy(salesTargetIds, 1, newSalesTargetIds, 0, newSalesTargetIds.length);

		final StringBuffer placementName = new StringBuffer(productPlacement);
		if (newSalesTargetIds.length > 0) {
			final List<SalesTarget> salesTargetListFromDB = salesTargetService.getSalesTargetListByIDs(newSalesTargetIds);
			for (SalesTarget salesTarget : salesTargetListFromDB) {
				if (placementName.length() > 0) {
					placementName.append(ConstantStrings.COMMA);
					placementName.append(ConstantStrings.SPACE);
				}
				placementName.append(salesTarget.getSalesTargeDisplayName());
			}
		}

		final StringBuffer geoTargetName = new StringBuffer();
		final List<LineItemTarget> geoTargetLst = proposalService.getProposalGeoTargets(lineItemId);
		if (geoTargetLst != null && !geoTargetLst.isEmpty()) {
			final Map<Long, String> targetTypeMap = getTargetTypeCriteria();
			String tarTypeElement = ConstantStrings.EMPTY_STRING;
			for (LineItemTarget geoTarget : geoTargetLst) {
				final String sosTarTypeName = targetTypeMap.get(geoTarget.getSosTarTypeId());
				if (ZIP_CODES.equals(sosTarTypeName)) {
					tarTypeElement = geoTarget.getSosTarTypeElementId();
				} else {
					tarTypeElement = getTarTypeElementName(geoTarget);
				}
				if (geoTargetName.length() > 0) {
					geoTargetName.append(ConstantStrings.COMMA);
					geoTargetName.append(ConstantStrings.SPACE);
				}
				geoTargetName.append(ConstantStrings.OPEN_SQUARE_BRAKET).append(sosTarTypeName).append(ConstantStrings.COLON).append(tarTypeElement).append(ConstantStrings.CLOSE_SQUARE_BRAKET);
			}
		}
		if (placementName.length() > 0 && geoTargetName.length() > 0) {
			placementName.append(ConstantStrings.SPACE);
			placementName.append(ConstantStrings.TARGETED_TO);
			placementName.append(ConstantStrings.SPACE);
			placementName.append(geoTargetName.toString());
		}
		return placementName.toString();
	}

	/**
	 * Method returns the comma separated name of LineItem Target Elements
	 * @param lineItemTarget
	 * @return
	 */
	public String getTarTypeElementName(final LineItemTarget lineItemTarget) {
		final Map<Long, String> targetTypeElementMap = getTargetTypeElement(lineItemTarget.getSosTarTypeId().toString());
		final StringBuffer tarTypeElementName = new StringBuffer();
		final String[] tarTypeElement = lineItemTarget.getSosTarTypeElementId().split(ConstantStrings.COMMA);
		for (int i = 0; i < tarTypeElement.length; i++) {
			tarTypeElementName.append(targetTypeElementMap.get(Long.valueOf(tarTypeElement[i]))).append(ConstantStrings.COMMA).append(ConstantStrings.SPACE);
		}
		return tarTypeElementName.substring(0, tarTypeElementName.lastIndexOf(ConstantStrings.COMMA));
	}

	/**
	 * @param saleTargetTypeLst
	 * @return
	 */
	public Map<Long, Long> getParentSalesTargetId(final List<Long> saleTargetTypeLst) {
		return productService.getParentSalesTargetId(saleTargetTypeLst);
	}

	/**
	 * Return a list of selected Sales Target based on Ids
	 * @param ids
	 * @return List<SalesTargetAmpt>
	 */
	public List<SalesTargetAmpt> getSalesTarget(final Long[] ids) {
		final List<SalesTarget> salestargetLst = salesTargetService.getSalesTargetListByIDs(ids);
		final List<SalesTargetAmpt> salesTargetAmptLst = salesTargetService.getSalesTarget(ids);
		final List<SalesTargetAmpt> salesTargetAmpt = new ArrayList<SalesTargetAmpt>();
		boolean flag = true;
		for (SalesTarget salesTarget : salestargetLst) {
			final SalesTargetAmpt salesTargetObj = new SalesTargetAmpt();
			flag = true;
			for (SalesTargetAmpt salestargetampt : salesTargetAmptLst) {
				if (salestargetampt.getSalesTargetId() == salesTarget.getSalesTargetId()) {
					flag = false;
				}
			}
			if (flag) {
				salesTargetObj.setSalesTargetId(-1L);
				salesTargetObj.setSalesTargeDisplayName(salesTarget.getSalesTargeDisplayName());
				salesTargetObj.setWeight(0D);
				salesTargetObj.setCapacity(0L);
				salesTargetAmpt.add(salesTargetObj);
			}
		}
		if (salesTargetAmpt != null && !salesTargetAmpt.isEmpty()) {
			salesTargetAmptLst.addAll(salesTargetAmpt);
		}
		return salesTargetAmptLst;
	}
	
	/**
	 * Function use to send mail when Admin did bulk renew reservation
	 * @param reservationLst
	 * @param proposalLst
	 */
	public void sendMailForBulkRenewReservation(List<LineItemReservations> reservationLst, List<Proposal> proposalLst){
		String msg = creatTextMsgForBulkRenewReservation(reservationLst, proposalLst, ReservationStatus.RE_NEW);
		String subject = createSubjectForBulkRenewReservation(ReservationStatus.RE_NEW);
		final List<String> roles = new ArrayList<String>(3);
		roles.add(SecurityUtil.ADMIN);
		List<Long> productClassIdLst = productService.getProductClassIdLstByDisplayName();
		for (LineItemReservations reservation : reservationLst) {
			LineItem lineItem = reservation.getProposalLineItem();
			if(LineItemProductTypeEnum.EMAIL.name().equals(lineItem.getProductType().name())){
				roles.add(SecurityUtil.EMAIL_PRODUCT_MANAGER);
				break;
			}
			
			if((reservation != null  && productClassIdLst.contains(reservation.getProposalLineItem().getSosProductClass()))){
				roles.add(SecurityUtil.PRODUCT_OWNER);
			}
		}
		sendMailToUserForBulkReservations(proposalLst, subject, msg, roles);
	}
	
	/**
	 * Create email subject of bulk renew reservation
	 * @param status
	 * @return
	 */
	private String createSubjectForBulkRenewReservation(final ReservationStatus status) {
		final StringBuffer subject = new StringBuffer(ConstantStrings.RESERVATION_EMAIL_KEYWORD);
		return subject.append(ConstantStrings.SPACE).append("BULK").append(ConstantStrings.SPACE).append(status.getDisplayName()).toString();
	}

	
	/**
	 * Create email body of bulk renew reservation
	 * @param reservationLst
	 * @param proposalLst
	 * @param status
	 * @return
	 */
	private String creatTextMsgForBulkRenewReservation(List<LineItemReservations> reservationLst, List<Proposal> proposalLst, final ReservationStatus status) {
		final StringBuffer textMsg = new StringBuffer(173);
		for (LineItemReservations reservation : reservationLst) {
			LineItem lineItem = reservation.getProposalLineItem();
			textMsg.append("LineItemID: ").append(lineItem.getLineItemID())
				.append(ConstantStrings.NEW_AND_TAB_LINE).append("Ad Unit: ").append(StringUtils.trimToEmpty(lineItem.getProductName()))
				.append(ConstantStrings.NEW_AND_TAB_LINE).append("Sales Target: ").append(lineItem.getLineItemSalesTargetAssocs().get(0).getSosSalesTargetName())
				.append(ConstantStrings.NEW_AND_TAB_LINE).append("Target: ")
				.append(((StringUtils.isNotBlank(lineItem.getTargetingString())) ? lineItem.getTargetingString() : ConstantStrings.EMPTY_STRING))
				.append(ConstantStrings.NEW_AND_TAB_LINE).append("Flight: ").append(DateUtil.getGuiDateString(lineItem.getStartDate()))
				.append(ConstantStrings.HYPHEN).append(DateUtil.getGuiDateString(lineItem.getEndDate()));
			if (ReservationStatus.DELETED.getDisplayName().equals(status.getDisplayName())) {
				textMsg.append(ConstantStrings.NEW_AND_TAB_LINE).append("Reservation Created On: ").append(
						((lineItem.getReservation() == null) ? ConstantStrings.EMPTY_STRING : DateUtil.getGuiDateString(lineItem.getReservation().getCreatedDate())));
			} else {
				textMsg.append(ConstantStrings.NEW_AND_TAB_LINE).append("Reservation Expiry Date: ").append(
						((lineItem.getReservation() == null) ? ConstantStrings.EMPTY_STRING : DateUtil.getGuiDateString(lineItem.getReservation().getExpirationDate())));
			}
			textMsg.append(ConstantStrings.NEW_AND_TAB_LINE).append("SOR: ").append(((lineItem.getSor() == null) ? ConstantStrings.NA : lineItem.getSor()));
			for (Proposal proposal : proposalLst) {
				if(proposal.getId() == lineItem.getProposalId()){
					textMsg.append(ConstantStrings.NEW_AND_TAB_LINE).append("Sales Category: ").append((StringUtils.isNotBlank(getSalesCategory().get(proposal.getSosSalesCategoryId())) ? 
							getSalesCategory().get(proposal.getSosSalesCategoryId()) : ConstantStrings.EMPTY_STRING));
					textMsg.append(ConstantStrings.NEW_AND_TAB_LINE).append("Proposal Name: ").append((StringUtils.isNotBlank(proposal.getName()) ? 
							proposal.getName() : ConstantStrings.EMPTY_STRING));
					textMsg.append(ConstantStrings.NEW_AND_TAB_LINE).append("Account Manager: ").append((StringUtils.isNotBlank(proposal.getAccountManager()) ? 
							proposal.getAccountManager() : ConstantStrings.EMPTY_STRING));					
					textMsg.append(ConstantStrings.NEW_AND_TAB_LINE).append(applicationURL).append(ConstantStrings.PROPOSAL_URL).append(proposal.getId());
					break;
				}
			}
			textMsg.append(ConstantStrings.NEW_AND_TAB_LINE).append(ConstantStrings.NEW_AND_TAB_LINE);
		}
		return textMsg.toString();
	}
	
	/**
	 * send mail when Admin did bulk renew reservation
	 * @param proposalLst
	 * @param subject
	 * @param textMsg
	 * @param roles
	 */
	private void sendMailToUserForBulkReservations(final List<Proposal> proposalLst, final String subject, final String textMsg, final List<String> roles) {
		final StringBuffer mail = new StringBuffer();
		final List<User> userLst = userService.getUserBasedOnRoleList(roles.toArray(new String[roles.size()]));
		if (userLst != null && !userLst.isEmpty()) {
			for (User userDB : userLst) {
				if (mail.length() > 0) {
					mail.append(ConstantStrings.COMMA);
				}
				mail.append(userDB.getEmail());
			}
			for (Proposal proposal : proposalLst) {
				if (StringUtils.isNotBlank(proposal.getReservationEmails())) {
					mail.append(ConstantStrings.COMMA).append(proposal.getReservationEmails());
				}
			}
			mailUtil.sendMail(mailUtil.setMessageInfo(getMailInfoForReservation(mail.toString(), subject, SecurityUtil.getUser().getEmail(), textMsg)));
		}
	}
	
	public void sendMailForOverBooking(LineItem lineItem, Map<String, Object> sorMap) {
		final List<String> roles = new ArrayList<String>(3);
		roles.add(SecurityUtil.ADMIN);
		if(LineItemProductTypeEnum.EMAIL.name().equals(lineItem.getProductType().name())){
			roles.add(SecurityUtil.EMAIL_PRODUCT_MANAGER);
		}
		List<Long> productClassIdLst = productService.getProductClassIdLstByDisplayName();
		if((lineItem != null && lineItem.getReservation() != null &&  productClassIdLst.contains(lineItem.getSosProductClass()))){
			roles.add(SecurityUtil.PRODUCT_OWNER);
		}
		final Proposal proposal = proposalService.getProposalbyId(lineItem.getProposalId());
		sendMailToUserForOverBooking(proposal, creatHtmlMsgForOverBookingReservation(proposal, lineItem, sorMap), roles);
	}
	
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	private String creatHtmlMsgForOverBookingReservation(final Proposal proposal, final LineItem lineItem, final Map<String, Object> sorMap) {
		final StringBuffer textMsg = new StringBuffer(73);
		textMsg.append("<HTML><BODY>");
		
		textMsg.append("LineItemID: ").append(lineItem.getLineItemID())
			.append(ConstantStrings.NEW_HTML_LINE).append("Ad Unit: ").append(StringUtils.trimToEmpty(lineItem.getProductName()))
			.append(ConstantStrings.NEW_HTML_LINE).append("Sales Target: ").append(lineItem.getLineItemSalesTargetAssocs().get(0).getSosSalesTargetName())
			.append(ConstantStrings.NEW_HTML_LINE).append("Target: ")
			.append(((StringUtils.isNotBlank(lineItem.getTargetingString())) ? lineItem.getTargetingString() : ConstantStrings.EMPTY_STRING))
			.append(ConstantStrings.NEW_HTML_LINE).append("Flight: ").append(DateUtil.getGuiDateString(lineItem.getStartDate()))
			.append(ConstantStrings.HYPHEN).append(DateUtil.getGuiDateString(lineItem.getEndDate()));
		
		textMsg.append(ConstantStrings.NEW_HTML_LINE).append("Reservation Expiry Date: ").append(
					((lineItem.getReservation() == null) ? ConstantStrings.EMPTY_STRING : DateUtil.getGuiDateString(lineItem.getReservation().getExpirationDate())));
		textMsg.append(ConstantStrings.NEW_HTML_LINE).append("SOR: ").append(((lineItem.getSor() == null) ? ConstantStrings.NA : lineItem.getSor()));
		textMsg.append(ConstantStrings.NEW_HTML_LINE).append("Account Manager: ").append((StringUtils.isNotBlank(proposal.getAccountManager()) ? 
				proposal.getAccountManager() : ConstantStrings.EMPTY_STRING));
		if(sorMap.containsKey("PROPOSALLI") || sorMap.containsKey("ORDERLI")){
			textMsg.append(ConstantStrings.NEW_HTML_LINE).append(ConstantStrings.NEW_HTML_LINE).append(ConstantStrings.NEW_HTML_LINE);
			textMsg.append("<b><u>Previously booked or put on hold:</u></b>");
		}
		Map <Long,Long> removeDuplicateMap = new HashMap<Long, Long>();
		if(sorMap.containsKey("PROPOSALLI")){			
			textMsg.append(ConstantStrings.NEW_HTML_LINE).append(ConstantStrings.NEW_HTML_LINE);
			textMsg.append("<b>AMPT Reservations</b>").append(ConstantStrings.NEW_HTML_LINE);
			textMsg.append("<table style='border: 1px solid black;  border-collapse: collapse;'>");
			textMsg.append("<tr>")
			.append("<th style='border: 1px solid black;  border-collapse: collapse; padding: 5px;'>").append("LineItemID").append("</th>")
			.append("<th style='border: 1px solid black;  border-collapse: collapse; padding: 5px;'>").append("SOR").append("</th>")
			.append("<th style='border: 1px solid black;  border-collapse: collapse; padding: 5px;'>").append("Ad Unit").append("</th>")
			.append("<th style='border: 1px solid black;  border-collapse: collapse; padding: 5px;'>").append("Sales Target").append("</th>");
			textMsg.append("</tr>");
			for (LineItem lineItemObj : (List<LineItem>)sorMap.get("PROPOSALLI")) {
				if(!removeDuplicateMap.containsKey(lineItemObj.getLineItemID())){
					textMsg.append("<tr>");
					textMsg.append("<td style='border: 1px solid black;  border-collapse: collapse; padding: 5px;'>").append(lineItemObj.getLineItemID()).append("</td>");
					textMsg.append("<td style='border: 1px solid black;  border-collapse: collapse; padding: 5px;'>").append(lineItemObj.getSor()).append("</td>");
					textMsg.append("<td style='border: 1px solid black;  border-collapse: collapse; padding: 5px;'>").append(lineItemObj.getProductName()).append("</td>");
					textMsg.append("<td style='border: 1px solid black;  border-collapse: collapse; padding: 5px;'>").append(lineItemObj.getLineItemSalesTargetAssocs().get(0).getSosSalesTargetName()).append("</td>");
					textMsg.append("</tr>");
					removeDuplicateMap.put(lineItemObj.getLineItemID(), lineItemObj.getLineItemID());
				}
			}
			textMsg.append("</table>");			
		}
		removeDuplicateMap.clear();
		if(sorMap.containsKey("ORDERLI")){
			textMsg.append(ConstantStrings.NEW_HTML_LINE).append(ConstantStrings.NEW_HTML_LINE);
			textMsg.append("<b>SOS Bookings</b>").append(ConstantStrings.NEW_HTML_LINE);
			textMsg.append("<table style='border: 1px solid black;  border-collapse: collapse;'>");
			textMsg.append("<tr>")
			.append("<th style='border: 1px solid black;  border-collapse: collapse; padding: 5px;'>").append("LineItemID").append("</th>")
			.append("<th style='border: 1px solid black;  border-collapse: collapse; padding: 5px;'>").append("SOR").append("</th>")
			.append("<th style='border: 1px solid black;  border-collapse: collapse; padding: 5px;'>").append("Ad Unit").append("</th>")
			.append("<th style='border: 1px solid black;  border-collapse: collapse; padding: 5px;'>").append("Sales Target").append("</th>");
			textMsg.append("</tr>");
			Map<Long,String> productMap = getAllProducts();
			Map<Long,String> salesTargetMap = getSalesTarget();
			for (OrderLineItem orderLineItemObj : (List<OrderLineItem>)sorMap.get("ORDERLI")) {
				if(!removeDuplicateMap.containsKey(orderLineItemObj.getLineItemId())){
					textMsg.append("<tr>");
					textMsg.append("<td style='border: 1px solid black;  border-collapse: collapse; padding: 5px;'>").append(orderLineItemObj.getLineItemId()).append("</td>");
					textMsg.append("<td style='border: 1px solid black;  border-collapse: collapse; padding: 5px;'>").append(orderLineItemObj.getShareOfReservation()).append("</td>");
					textMsg.append("<td style='border: 1px solid black;  border-collapse: collapse; padding: 5px;'>").append(productMap.containsKey(orderLineItemObj.getProductId()) ? productMap.get(orderLineItemObj.getProductId()) : orderLineItemObj.getProductId()).append("</td>");
					textMsg.append("<td style='border: 1px solid black;  border-collapse: collapse; padding: 5px;'>").append(salesTargetMap.containsKey(orderLineItemObj.getSalesTargetId()) ? salesTargetMap.get(orderLineItemObj.getSalesTargetId()) : orderLineItemObj.getSalesTargetId()).append("</td>");
					textMsg.append("</tr>");
					removeDuplicateMap.put(orderLineItemObj.getLineItemId(),orderLineItemObj.getLineItemId());
				}
			}
			textMsg.append("</table>");
		}
		textMsg.append(ConstantStrings.NEW_HTML_LINE).append(ConstantStrings.NEW_HTML_LINE).append(applicationURL).append(ConstantStrings.PROPOSAL_URL).append(proposal.getId());
		textMsg.append("</BODY></HTML>");
		return textMsg.toString();
	}
	
	private void sendMailToUserForOverBooking(final Proposal proposal, final String textMsg, final List<String> roles) {
		final StringBuffer subject = new StringBuffer(ConstantStrings.RESERVATION_EMAIL_KEYWORD);
		subject.append(ConstantStrings.SPACE).append("OVERBOOKED").append(ConstantStrings.COLON).append(ConstantStrings.SPACE)
				.append(getSalesCategory().get(proposal.getSosSalesCategoryId())).append(ConstantStrings.COLON).append(ConstantStrings.SPACE).append(proposal.getName()).toString();
		final StringBuffer mail = new StringBuffer();
		final List<User> userLst = userService.getUserBasedOnRoleList(roles.toArray(new String[roles.size()]));
		if (userLst != null && !userLst.isEmpty()) {
			for (User userDB : userLst) {
				if (mail.length() > 0) {
					mail.append(ConstantStrings.COMMA);
				}
				mail.append(userDB.getEmail());
			}
			if (StringUtils.isNotBlank(proposal.getReservationEmails())) {
				mail.append(ConstantStrings.COMMA).append(proposal.getReservationEmails());
			}
			mailUtil.sendMail(mailUtil.setHtmlMessageInfo(getMailInfoForReservation(mail.toString(), subject.toString(), proposal.getAssignedUser().getEmail(), textMsg)));
		}
	}

	/**
	 * Send Emails while reservation status change
	 * 
	 * @param request
	 * @param lineItemOld
	 * @param lineItemNew
	 */
	public void sendMailForReservation(final HttpServletRequest request, final LineItem lineItemOld, final LineItem lineItemNew) {
		final List<String> roles = new ArrayList<String>(3);
		roles.add(SecurityUtil.ADMIN);
		if(LineItemProductTypeEnum.EMAIL.name().equals(lineItemNew.getProductType().name())){
			roles.add(SecurityUtil.EMAIL_PRODUCT_MANAGER);
		}
		List<Long> productClassIdLst = productService.getProductClassIdLstByDisplayName();
		if((lineItemOld != null && lineItemOld.getReservation() != null &&  productClassIdLst.contains(lineItemOld.getSosProductClass())) || ((lineItemNew != null && lineItemNew.getReservation() != null &&  productClassIdLst.contains(lineItemNew.getSosProductClass())))){
			roles.add(SecurityUtil.PRODUCT_OWNER);
		}
		final Proposal proposal = proposalService.getProposalbyId(lineItemNew.getProposalId());
		if (lineItemOld == null) {
			if (lineItemNew.getReservation() != null) {
				sendMailToUserForReservation(proposal, createSubjectForReservation(ReservationStatus.HOLD, proposal), creatTextMsgForReservation(proposal, lineItemNew, ReservationStatus.HOLD), roles);
			}
		} else {
			if (lineItemOld.getReservation() == null) {
				if (lineItemNew.getReservation() != null) {
					sendMailToUserForReservation(proposal, createSubjectForReservation(ReservationStatus.HOLD, proposal), creatTextMsgForReservation(proposal, lineItemNew, ReservationStatus.HOLD), roles);
				}
			} else if (lineItemNew.getReservation() == null) {
				sendMailToUserForReservation(proposal, createSubjectForReservation(ReservationStatus.DELETED, proposal), creatTextMsgForReservation(proposal, lineItemOld, ReservationStatus.DELETED), roles);
			} else if (lineItemNew.getReservation() != null ) {
				if(isReservationDetailsUpdated(lineItemOld,lineItemNew)){
					if( !productClassIdLst.contains(lineItemOld.getSosProductClass())){
						roles.remove(SecurityUtil.PRODUCT_OWNER);
					}
					sendMailToUserForReservation(proposal, createSubjectForReservation(ReservationStatus.DELETED, proposal), creatTextMsgForReservation(proposal, lineItemOld, ReservationStatus.DELETED), roles);
					if(!productClassIdLst.contains(lineItemNew.getSosProductClass())){
						roles.remove(SecurityUtil.PRODUCT_OWNER);
					}else{
						roles.add(SecurityUtil.PRODUCT_OWNER);
					}
					sendMailToUserForReservation(proposal, createSubjectForReservation(ReservationStatus.HOLD, proposal), creatTextMsgForReservation(proposal, lineItemNew, ReservationStatus.HOLD), roles);
				}else if(!DateUtils.isSameDay(lineItemNew.getReservation().getExpirationDate(), lineItemOld.getReservation().getExpirationDate())){
					sendMailToUserForReservation(proposal, createSubjectForReservation(ReservationStatus.RE_NEW, proposal), creatTextMsgForReservation(proposal, lineItemNew, ReservationStatus.RE_NEW), roles);
				}
			}
		}

	}
	
	/**
	 * To decide whether Reservation level details is changed or not while updating a Line Item
	 * @param lineItemOld
	 * @param lineItemNew
	 * @return
	 */
	private boolean isReservationDetailsUpdated(final LineItem lineItemOld, final LineItem lineItemNew){
		boolean returnFlag = false;
		if(!lineItemOld.getSosProductId().equals(lineItemNew.getSosProductId())){
			returnFlag = true;
		}else if(!lineItemOld.getLineItemSalesTargetAssocs().get(0).getSosSalesTargetId().equals(lineItemNew.getLineItemSalesTargetAssocs().get(0).getSosSalesTargetId())){
			returnFlag = true;
		}else if(!DateUtils.isSameDay(lineItemOld.getStartDate(), lineItemNew.getStartDate()) || !DateUtils.isSameDay(lineItemOld.getEndDate(), lineItemNew.getEndDate())){
			returnFlag = true;
		}/*else if(!StringUtils.equals(lineItemOld.getTargetingString(), lineItemNew.getTargetingString())){
			returnFlag = true;
		}*/
		return returnFlag;		
	}

	/**
	 * Actually Triggers a mail
	 * @param proposal
	 * @param subject
	 * @param textMsg
	 * @param roles
	 */
	private void sendMailToUserForReservation(final Proposal proposal, final String subject, final String textMsg, final List<String> roles) {
		final StringBuffer mail = new StringBuffer();
		final List<User> userLst = userService.getUserBasedOnRoleList(roles.toArray(new String[roles.size()]));
		if (userLst != null && !userLst.isEmpty()) {
			for (User userDB : userLst) {
				if (mail.length() > 0) {
					mail.append(ConstantStrings.COMMA);
				}
				mail.append(userDB.getEmail());
			}
			if (StringUtils.isNotBlank(proposal.getReservationEmails())) {
				mail.append(ConstantStrings.COMMA).append(proposal.getReservationEmails());
			}
			mailUtil.sendMail(mailUtil.setMessageInfo(getMailInfoForReservation(mail.toString(), subject, proposal.getAssignedUser().getEmail(), textMsg)));
		}
	}

	/**
	 * Method decides which cc,to
	 * @param mailcc
	 * @param subject
	 * @param mailFrom
	 * @param textMsg
	 * @return
	 */
	private Map<String, String> getMailInfoForReservation(final String mailcc, final String subject, final String mailFrom, final String textMsg) {
		final Map<String, String> mailProps = new HashMap<String, String>();
		if (StringUtils.isNotBlank(SecurityUtil.getUser().getEmail())) {
			mailProps.put("mailTo", SecurityUtil.getUser().getEmail());
		}
		mailProps.put("subject", subject);
		mailProps.put("mailFrom", mailFrom);
		mailProps.put("textMsg", textMsg);
		mailProps.put("cc", mailcc.trim());
		return mailProps;
	}

	/**
	 * Create text message for sending
	 * @param proposal
	 * @param request
	 * @param lineItem
	 * @param deleted
	 * @return
	 */
	private String creatTextMsgForReservation(final Proposal proposal, final LineItem lineItem, final ReservationStatus status) {
		final StringBuffer textMsg = new StringBuffer(73);
		textMsg.append("LineItemID: ").append(lineItem.getLineItemID())
			.append(ConstantStrings.NEW_AND_TAB_LINE).append("Ad Unit: ").append(StringUtils.trimToEmpty(lineItem.getProductName()))
			.append(ConstantStrings.NEW_AND_TAB_LINE).append("Sales Target: ").append(lineItem.getLineItemSalesTargetAssocs().get(0).getSosSalesTargetName())
			.append(ConstantStrings.NEW_AND_TAB_LINE).append("Target: ")
			.append(((StringUtils.isNotBlank(lineItem.getTargetingString())) ? lineItem.getTargetingString() : ConstantStrings.EMPTY_STRING))
			.append(ConstantStrings.NEW_AND_TAB_LINE).append("Flight: ").append(DateUtil.getGuiDateString(lineItem.getStartDate()))
			.append(ConstantStrings.HYPHEN).append(DateUtil.getGuiDateString(lineItem.getEndDate()));
		if (ReservationStatus.DELETED.getDisplayName().equals(status.getDisplayName())) {
			textMsg.append(ConstantStrings.NEW_AND_TAB_LINE).append("Reservation Created On: ").append(
					((lineItem.getReservation() == null) ? ConstantStrings.EMPTY_STRING : DateUtil.getGuiDateString(lineItem.getReservation().getCreatedDate())));
		} else {
			textMsg.append(ConstantStrings.NEW_AND_TAB_LINE).append("Reservation Expiry Date: ").append(
					((lineItem.getReservation() == null) ? ConstantStrings.EMPTY_STRING : DateUtil.getGuiDateString(lineItem.getReservation().getExpirationDate())));
		}
		textMsg.append(ConstantStrings.NEW_AND_TAB_LINE).append("SOR: ").append(((lineItem.getSor() == null) ? ConstantStrings.NA : lineItem.getSor()));
		textMsg.append(ConstantStrings.NEW_AND_TAB_LINE).append("Account Manager: ").append((StringUtils.isNotBlank(proposal.getAccountManager()) ? 
				proposal.getAccountManager() : ConstantStrings.EMPTY_STRING));
		textMsg.append(ConstantStrings.NEW_AND_TAB_LINE).append(ConstantStrings.NEW_AND_TAB_LINE).append(applicationURL).append(ConstantStrings.PROPOSAL_URL).append(proposal.getId());
		return textMsg.toString();
	}

	/**
	 * Create subject for sending
	 * @param status
	 * @param proposal
	 * @return
	 */
	private String createSubjectForReservation(final ReservationStatus status, final Proposal proposal) {
		final StringBuffer subject = new StringBuffer(ConstantStrings.RESERVATION_EMAIL_KEYWORD);
		return subject.append(ConstantStrings.SPACE).append(status.getDisplayName()).append(ConstantStrings.COLON).append(ConstantStrings.SPACE)
				.append(getSalesCategory().get(proposal.getSosSalesCategoryId())).append(ConstantStrings.COLON).append(ConstantStrings.SPACE).append(proposal.getName()).toString();
	}

	/**
	 * @param lineItemList
	 * @param lineItemToMoveList
	 * @param positionToMove
	 */
	public List<LineItem> arrangeLineItemSequence(final List<LineItem> lineItemList, final List<Long> lineItemToMoveList, final int positionToMove) {
		/**
		 * Sort Line Item according to current sequence number and re-generate sequence
		 */
		sortAndReGenerateSequence(lineItemList);

		final long[] lineItemIdsArr = new long[lineItemList.size()];
		final long[] lineItemToMove = new long[lineItemToMoveList.size()];

		/**
		 * ArrayList to hold line items to move, in ascending order of sequence number
		 */
		for (int i = 0, j = 0; i < lineItemList.size(); i++) {
			final long lineItemId = lineItemList.get(i).getLineItemID();
			if (lineItemToMoveList.contains(lineItemId)) {
				lineItemToMove[j++] = lineItemId;
			}
			lineItemIdsArr[i] = lineItemId;
		}

		final long[] finalSeqArr = reArrangeLineItems(lineItemIdsArr, lineItemToMove, positionToMove);
		for (int i = 0; i < finalSeqArr.length; i++) {
			for (LineItem lineItem : lineItemList) {
				if (finalSeqArr[i] == lineItem.getLineItemID()) {
					lineItem.setLineItemSequence(i + 1);
					break;
				}
			}
		}
		return lineItemList;
	}

	/**
	 * @param lineItemIdsArr
	 * @param lineItemId
	 * @param positionToMove
	 */
	private long[] reArrangeLineItems(final long[] lineItemIdsArr, final long[] lineItemToMove, final int positionToMove) {
		final long[] finalSeqArr = new long[lineItemIdsArr.length];
		for (int j = 0; j < lineItemToMove.length; j++) {
			for (int i = 0; i < lineItemIdsArr.length; i++) {
				final long lineItem = lineItemToMove[j];
				if (lineItem == lineItemIdsArr[i]) {
					final int newSeq = i + positionToMove;
					if (positionToMove > 0) {
						if (newSeq + lineItemToMove.length - j >= finalSeqArr.length) {
							finalSeqArr[finalSeqArr.length - (lineItemToMove.length - j)] = lineItem;
						} else {
							finalSeqArr[newSeq] = lineItem;
						}
					} else {
						if (newSeq <= 0) {
							finalSeqArr[j] = lineItem;
						} else {
							finalSeqArr[newSeq] = lineItem;
						}
					}
					lineItemIdsArr[i] = 0;
					break;
				}
			}
		}
		for (int i = 0; i < lineItemIdsArr.length; i++) {
			if (lineItemIdsArr[i] != 0) {
				for (int j = 0; j < finalSeqArr.length; j++) {
					if (finalSeqArr[j] == 0) {
						finalSeqArr[j] = lineItemIdsArr[i];
						break;
					}
				}
			}
		}
		return finalSeqArr;
	}

	private void sortAndReGenerateSequence(final List<LineItem> lineItemList) {
		Collections.sort(lineItemList, new Comparator<LineItem>() {
			@Override
			public int compare(final LineItem o1, final LineItem o2) {
				return Integer.valueOf(o1.getLineItemSequence()).compareTo(Integer.valueOf(o2.getLineItemSequence()));
			}
		});
		int i = 0;
		for (LineItem lineItem : lineItemList) {
			lineItem.setLineItemSequence(++i);
		}
	}

	/**
	 * Convert the String array to Long Array for sales Target Id's
	 * @param salestargets
	 * @return
	 */
	public Long[] getSalesTragetIdsArray(final String[] salestargets) {
		Long[] salestargetIds = new Long[salestargets.length];
		for (int i = 0; i < salestargets.length; i++) {
			salestargetIds[i] = Long.valueOf(salestargets[i]);
		}
		return salestargetIds;
	}
	
	/**
	 * Checks whether send date is valid or not
	 * @param lineItemForm
	 * @return
	 */
	public boolean isValidStartDate(LineItemForm lineItemForm) {
		boolean returnFlag = true;
		if(StringUtils.isNotBlank(lineItemForm.getSosProductId()) &&  lineItemForm.getSosSalesTargetId().length > 0){
			EmailSchedule emailScheduleObj = emailScheduleService.getEmailScheduleByDate(Long.valueOf(lineItemForm.getSosProductId()),Long.valueOf(lineItemForm.getSosSalesTargetId()[0]),DateUtil.parseToDate(lineItemForm.getStartDate()));
			if(emailScheduleObj == null){
				returnFlag = false;
			}else{
				Calendar c1 = Calendar.getInstance();
		        c1.setTime(DateUtil.parseToDate(lineItemForm.getStartDate()));
		        Weekdays weekday = Weekdays.findByCode(c1.get(Calendar.DAY_OF_WEEK));
		        if(StringUtils.isNotBlank(emailScheduleObj.getEmailSchedules().get(0).getWeekdays())){
		        	returnFlag = StringUtils.contains(emailScheduleObj.getEmailSchedules().get(0).getWeekdays(), weekday.name());
		        }
			}
		}
		return returnFlag;
	}

	public void setProposalService(final IProposalService proposalService) {
		this.proposalService = proposalService;
	}

	public void setUserService(final IUserService userService) {
		this.userService = userService;
	}

	public void setProductService(final IProductService productService) {
		this.productService = productService;
	}

	public void setSosService(final ISOSService sosService) {
		this.sosService = sosService;
	}

	public void setTemplateService(final ITemplateService templateService) {
		this.templateService = templateService;
	}

	public void setAdProductService(final IProductService adProductService) {
		this.adProductService = adProductService;
	}

	public void setSalesTargetService(final ISalesTargetService salesTargetService) {
		this.salesTargetService = salesTargetService;
	}

	public void setProposalSOSService(final IProposalSOSService proposalSOSService) {
		this.proposalSOSService = proposalSOSService;
	}

	public void setTargetingService(final ITargetingService targetingService) {
		this.targetingService = targetingService;
	}

	public void setMailUtil(final MailUtil mailUtil) {
		this.mailUtil = mailUtil;
	}

	public void setApplicationURL(final String applicationURL) {
		this.applicationURL = applicationURL;
	}

	/**
	 * Called from workflowHandler
	 * @return
	 */
	public String getApplicationURL() {
		return applicationURL;
	}

	public void setEmailScheduleService(IEmailScheduleService emailScheduleService) {
		this.emailScheduleService = emailScheduleService;
	}

}
