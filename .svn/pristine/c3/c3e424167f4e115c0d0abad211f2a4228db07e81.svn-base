/**
 *
 */
package com.nyt.mpt.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.nyt.mpt.domain.ClusterSalesTarget;
import com.nyt.mpt.domain.CountryRegionMap;
import com.nyt.mpt.domain.LineItemTarget;
import com.nyt.mpt.domain.PrimaryPageGroup;
import com.nyt.mpt.domain.Product;
import com.nyt.mpt.domain.ProductPosition;
import com.nyt.mpt.domain.SalesTarget;
import com.nyt.mpt.service.IProductService;
import com.nyt.mpt.service.ISalesTargetService;
import com.nyt.mpt.service.ITargetingService;
import com.nyt.mpt.util.dto.LineItemDTO;

/**
 * Yield-ex helper used to build yield-ex formated query string for avails
 *
 * @author manish.kesarwani
 *
 */
public class YieldexHelper {
	
	private static final String OPENING_BRACKET = " (";
	private static final String CLOSING_BRACKET = " )";
	
	private static final String PAR_DAY = "1";
	private static final String PAR_WEEK = "0.1429";
	
	private IProductService productService;
	private ISalesTargetService salesTargetService;
	private ITargetingService targetingService;
	
	private String yieldexUserName;
	private String yieldexPassword;
	private String yieldexBaseUrl;

	private static final Logger LOGGER = Logger.getLogger(YieldexHelper.class);
	
	/**
	 * This method is used to generate Yield-ex URL for avails
	 * @param form
	 * @return String
	 */
	public String getYieldexURLForAvails(final LineItemDTO form) {
		List<PrimaryPageGroup> pageGroupList = new ArrayList<PrimaryPageGroup>();

		Set<LineItemTarget> geoTargetSet = form.getGeoTargetSet();
		Map<Long, String> targetTypeNameMap = getTargetTypeWithAdxNameCriteria();
		
		StringBuffer frequencyCapValue = new StringBuffer();
		// Fetching primary page group list
		final List<SalesTarget> salesTargetList = salesTargetService.getSalesTargetListByIDs(StringUtil.convertStringArrayToLongArray(form.getSosSalesTargetId()));
		for (SalesTarget salesTarget : salesTargetList) {
			if(LOGGER.isDebugEnabled()) {
				LOGGER.debug("Fetching primary page group for sales target - " + salesTarget.getSalesTargeDisplayName());
			}
			if (salesTarget.getSalesTargetType() != null) {
				if (ConstantStrings.YIELDEX_CLUSTER_TYPE.equalsIgnoreCase(salesTarget.getSalesTargetType().getSalesTargetTypeName())) {
					final List<ClusterSalesTarget> clusuterSalesTargetIds = productService.getClusterSalesTargetsBySalesTargetId(salesTarget.getSalesTargetId());
					Long[] salesTargetIds = new Long[clusuterSalesTargetIds.size()];
					int i = 0;
					for (ClusterSalesTarget clusterSalesTarget : clusuterSalesTargetIds) {
						salesTargetIds[i++] = clusterSalesTarget.getClusterSalesTargetId().getSosSalesTargetId();
					}
					pageGroupList = productService.getPrimaryPageGroupBySalesTargetIds(salesTarget.getSalesTargetId(), salesTargetIds);
				} else if (ConstantStrings.YIELDEX_DISPLAY_ROS.equalsIgnoreCase(salesTarget.getSalesTargetType().getSalesTargetTypeName())) {
					final PrimaryPageGroup primaryPageGroup = new PrimaryPageGroup();
					primaryPageGroup.setPrimaryPageGroup(ConstantStrings.YIELDEX_SECTION_ROS);
					pageGroupList.add(primaryPageGroup);
				} else {
					final PrimaryPageGroup primaryPageGroup = productService.getPrimaryPageGroupBySalesTargetId(salesTarget.getSalesTargetId());
					pageGroupList.add(primaryPageGroup);
				}
			}
		}

		final StringBuffer queryString = new StringBuffer();
		if (pageGroupList != null && !pageGroupList.isEmpty()) {
			String[] queryStringValue = new String[pageGroupList.size()];
			int i = 0;
			for (PrimaryPageGroup pripageGroup : pageGroupList) {
				queryStringValue[i++] = pripageGroup.getPrimaryPageGroup();
			}
			getFormatedQueryString(queryString, ConstantStrings.YIELDEX_PAGE_GROUP, ConstantStrings.IN, queryStringValue);
		}

		List<ProductPosition> productPositions = null;
		if (StringUtils.isNotBlank(form.getSosProductId())) {
			if(LOGGER.isDebugEnabled()) {
				LOGGER.debug("Fetching product positions for product - " + form.getSosProductId());
			}
			productPositions = productService.getProductPositionsByProductId(Long.parseLong(form.getSosProductId()));
		}

		if (productPositions != null && !productPositions.isEmpty()) {
			String targetAction = ConstantStrings.AND;
			final String[] queryStringValue = new String[productPositions.size()];
			int i = 0;
			for (ProductPosition productPosition : productPositions) {
				queryStringValue[i++] = productPosition.getPosition().getName();
			}
			
			if (queryString != null && queryString.length() != 0) {
				queryString.append(targetAction + ConstantStrings.SPACE);
			}
			getFormatedQueryString(queryString, ConstantStrings.YIELDEX_POSITION, ConstantStrings.IN, queryStringValue);
		}
		
		//Store Query String based on product and sales target, It will be used in case of OR Condition
		String productSTQueryString =  queryString.toString();
		
		if (geoTargetSet != null && !geoTargetSet.isEmpty()) {
			String inClause = ConstantStrings.EMPTY_STRING;
			String targetAction = ConstantStrings.AND;
			String[] targetTypeValues = null;
			for (LineItemTarget lineItemTarget : geoTargetSet) {
				String targetTypeName = targetTypeNameMap.get(lineItemTarget.getSosTarTypeId());
				if (StringUtils.isNotBlank(targetTypeName.trim())) {
					// Target type element should be text area value for Behaviour and Postal Code
					if (ConstantStrings.POSTAL_CODE.equalsIgnoreCase(targetTypeName) || ConstantStrings.REVSCI.equalsIgnoreCase(targetTypeName)) {
						targetTypeValues = lineItemTarget.getSosTarTypeElementId().split(ConstantStrings.COMMA);
						
					} else if(ConstantStrings.REGION.equalsIgnoreCase(targetTypeName)) {
						//Code for Region Targeting
						String[] emmentNameForRegion = getTarTypeElementName(lineItemTarget, targetTypeName);
						targetTypeValues = salesTargetService.getCountryCodeByRegions(emmentNameForRegion);
						targetTypeName = ConstantStrings.COUNTRY;
						
					} else {
						targetTypeValues = getTarTypeElementName(lineItemTarget, targetTypeName);
					}

					if (ConstantStrings.FREQUENCY_CAP.equalsIgnoreCase(targetTypeName)) {
						frequencyCapValue.append(ConstantStrings.FORWARD_SLASH + ConstantStrings.FREQUENCY_CAP);
						for (String value : targetTypeValues) {
							frequencyCapValue.append(ConstantStrings.SEMICOLON + value);
						}
					} else {
						if (queryString != null && queryString.length() != 0) {
							queryString.append(targetAction + ConstantStrings.SPACE);
							
							if(targetAction.equalsIgnoreCase("OR")) {
								queryString.append(productSTQueryString);
								queryString.append(ConstantStrings.SPACE);
								queryString.append(ConstantStrings.AND);
								queryString.append(ConstantStrings.SPACE);
							}
						}
						// Passing In or Not In Clause in the Query
						inClause = ConstantStrings.NOT.equalsIgnoreCase(lineItemTarget.getNegation()) ? ConstantStrings.NOT_IN : ConstantStrings.IN;
						getFormatedQueryString(queryString, targetTypeName, inClause, targetTypeValues);
					}
				}
				// Adding Target Action from UI
				targetAction = lineItemTarget.getOperation();
			}
		}

		if (StringUtils.isNotBlank(form.getStartDate())) {
			queryString.append(ConstantStrings.FORWARD_SLASH);
			queryString.append(DateUtil.getYieldexDateString(DateUtil.parseToDate(form.getStartDate())));
		}

		if (StringUtils.isNotBlank(form.getEndDate())) {
			queryString.append(ConstantStrings.FORWARD_SLASH);
			queryString.append(DateUtil.getYieldexDateString(DateUtil.parseToDate(form.getEndDate())));
		}
		
		final StringBuffer yieldexURL = new StringBuffer(yieldexBaseUrl).append(StringUtils.lowerCase(queryString.toString()));
		if(frequencyCapValue != null && frequencyCapValue.length() > 0){
			yieldexURL.append(frequencyCapValue);
		}
		LOGGER.info("Constructed yieldex url - " + yieldexURL.toString());
		// Appending user id and password in base URL
		appendUserIdAndPassord(yieldexURL);

		return yieldexURL.toString();
	}

	/**
	 * This method is used to get formated query string for yield-ex 
	 * @param queryString
	 * @param keyword
	 * @param clause
	 * @param value
	 * @return StringBuffer
	 */
	private void getFormatedQueryString(final StringBuffer queryString, final String keyword, final String clause, final String[] value) {
		queryString.append(keyword);
		queryString.append(ConstantStrings.SPACE);
		queryString.append(clause);
		queryString.append(ConstantStrings.SPACE);

		final StringBuffer queryStringValues = new StringBuffer();
		if (value != null && value.length > 0) {
			queryString.append(OPENING_BRACKET);
			for (String stringValue : value) {
				if (queryStringValues.length() > 0) {
					queryStringValues.append(ConstantStrings.COMMA);
				}
				//Replacing few character according to YieldEx
				//percentage sign should be replaced with '%25' so that it can finally converted in '%2525'
				//Forward slash should be replaced with '%2F' so that it can be finally converted in '%252F'
				String replacedString = stringValue.replaceAll("%", "%25");
				replacedString = replacedString.replaceAll("/", "%2F");
			
				queryStringValues.append(ConstantStrings.SINGLE_QUOTE);
				queryStringValues.append(replacedString);
				queryStringValues.append(ConstantStrings.SINGLE_QUOTE);
			}
			queryString.append(queryStringValues.toString());
			queryString.append(CLOSING_BRACKET);
		}
		queryString.append(ConstantStrings.SPACE);
	}
	
	/**
	 * Used to append userId and password in query string
	 * @param queryString
	 * @return String
	 */
	private String appendUserIdAndPassord(final StringBuffer queryString) {
		queryString.append(ConstantStrings.QUERYSTRING_SYMBOL);
		queryString.append(ConstantStrings.USER_NAME);
		queryString.append(ConstantStrings.EQUAL);
		queryString.append(this.yieldexUserName);
		queryString.append(ConstantStrings.AND_SYMBOL);
		queryString.append(ConstantStrings.PASSWORD);
		queryString.append(ConstantStrings.EQUAL);
		queryString.append(this.yieldexPassword);
		return queryString.toString();
	}
	
	private Map<Long, String> targetTypeNameMap = new LinkedHashMap<Long, String>();
	
	/**
	 * This method is used to get Target Type
	 * @return Map<Long,String>
	 */
	private Map<Long,String> getTargetTypeWithAdxNameCriteria() {
		if(this.targetTypeNameMap.isEmpty()){
			this.targetTypeNameMap = targetingService.getTargetTypeWithAdxNameCriteria();			
		}		
		return this.targetTypeNameMap;
	}
	
	/**
	 * This method is used to get target type element for countries
	 * @param sosTarTypeId
	 * @return
	 */
	private Map<Long, String> getTargetForCountries(final Long sosTarTypeId) {
		return targetingService.getTargetForCountries(sosTarTypeId);
	}
	
	/**
	 * This method is used to get target type element from line item target
	 * @param lineItemTarget
	 * @return String[]
	 */
	private String[] getTarTypeElementName(final LineItemTarget lineItemTarget, final String targetTypeName) {
		Map<Long, String> elementMap = null;
		elementMap = getTargetForCountries(lineItemTarget.getSosTarTypeId());
		final String[] tarTypeElement = lineItemTarget.getSosTarTypeElementId().split(ConstantStrings.COMMA);
		final List<String> elementNameList = new ArrayList<String>();
		for (int i = 0; i < tarTypeElement.length; i++) {
			if (elementMap != null) {
				final String elementName = elementMap.get(Long.valueOf(tarTypeElement[i]));
				// Condition for behaviour targeting, some behaviour has comma
				// separated value
				if (ConstantStrings.FREQUENCY_CAP.equalsIgnoreCase(targetTypeName)) {
					if (ConstantStrings.ONCE_DAY.equalsIgnoreCase(elementName)) {
						elementNameList.add(ConstantStrings.IMPRESSION_CAP_PER_DAY + ConstantStrings.EQUAL + PAR_DAY);
					} else if (ConstantStrings.ONCE_WEEK.equalsIgnoreCase(elementName)) {
						elementNameList.add(ConstantStrings.IMPRESSION_CAP_PER_DAY + ConstantStrings.EQUAL + PAR_WEEK);
					}
				} else if (ConstantStrings.REVSCI.equalsIgnoreCase(targetTypeName) && StringUtils.isNotBlank(elementName) && elementName.contains(ConstantStrings.COMMA)) {
					final String[] tarTypeForRevsci = elementName.split(ConstantStrings.COMMA);
					for (String tarType : tarTypeForRevsci) {
						elementNameList.add(tarType);
					}
				} else {
					elementNameList.add(elementName);
				}
			}
		}
		return elementNameList.toArray(new String[0]);
	}
	
	public Map<Long, String> getAllTargetTypeElement() {
		return targetingService.getAllTargetTypeElement();
	}
	
	public List<String> getAllCoutriesByRegions(Set<String> allRegionIds) {
		final List<CountryRegionMap> countryRegionMap = targetingService.getAllCountryRegionData();
		final List<String> countryLst = new ArrayList<String>();
		if (!allRegionIds.isEmpty()) {
			for (String regionId : allRegionIds) {
				for (CountryRegionMap countryRegion : countryRegionMap) {
					if (countryRegion.getId().getRegionId() == Long.valueOf(regionId)) {
						countryLst.add(countryRegion.getCountryName());
					}
				}
			}
		}
		return countryLst;
	}
	
	public Product getProductById(String sosProductId) {
		return productService.getProductById(Long.valueOf(sosProductId));
	}
	
	public String getTargetElementNameById(final String targetElementId) {
		return targetingService.getTargetElementNameById(targetElementId);
	}
	
	public void setYieldexUserName(final String yieldexUserName) {
		this.yieldexUserName = yieldexUserName;
	}

	public void setYieldexPassword(final String yieldexPassword) {
		this.yieldexPassword = yieldexPassword;
	}

	public void setYieldexBaseUrl(final String yieldexBaseUrl) {
		this.yieldexBaseUrl = yieldexBaseUrl;
	}

	public void setProductService(final IProductService productService) {
		this.productService = productService;
	}

	public void setSalesTargetService(final ISalesTargetService salesTargetService) {
		this.salesTargetService = salesTargetService;
	}

	public void setTargetingService(ITargetingService targetingService) {
		this.targetingService = targetingService;
	}
}
