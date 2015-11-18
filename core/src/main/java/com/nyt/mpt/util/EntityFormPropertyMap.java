/**
 * 
 */
package com.nyt.mpt.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This class is used to map form property with entity.
 *
 * @author surendra.singh
 *
 */
public class EntityFormPropertyMap {

	private static final String PROPOSAL_STATUS = "proposalStatus";

	public static final Map<String, String> DB_COLUMN_MAP = new HashMap<String, String>();

	static {
		DB_COLUMN_MAP.put("id", "id");
		DB_COLUMN_MAP.put("proposalID", "id");
		DB_COLUMN_MAP.put("proposalName", ConstantStrings.NAME);
		DB_COLUMN_MAP.put("agencyName", "sosAgencyId");
		DB_COLUMN_MAP.put("salescategory", "sosSalesCategoryId");
		DB_COLUMN_MAP.put("advertiserName", "sosAdvertiserId");
		DB_COLUMN_MAP.put("industry", "sosIndustryId");
		DB_COLUMN_MAP.put("cpm", "propVersion.cpm");
		DB_COLUMN_MAP.put("budget", "propOption.budget"); // Please don't change alias name
		DB_COLUMN_MAP.put("impressions", "propVersion.impressions");
		DB_COLUMN_MAP.put(PROPOSAL_STATUS, PROPOSAL_STATUS);
		DB_COLUMN_MAP.put("dueOn", "dueDate");
		DB_COLUMN_MAP.put("requestedOn", "dateRequested");
		DB_COLUMN_MAP.put("pricingSubmittedDate", "pricingSubmittedDate");
		DB_COLUMN_MAP.put("criticality", "criticality");
		DB_COLUMN_MAP.put("criticalityDisplayName", "criticality");
		DB_COLUMN_MAP.put("assignedUser", "assignedUser.userId");
		DB_COLUMN_MAP.put("userName", "assignedUser.loginName");
		DB_COLUMN_MAP.put("userRole", "userRole.roleName");
		DB_COLUMN_MAP.put("cmpObjective", "cmpObjectiveSet.cmpObjId");
		DB_COLUMN_MAP.put("dueDateFromTo", "dueDate");
		DB_COLUMN_MAP.put("requestedDateFromTo", "dateRequested");
		DB_COLUMN_MAP.put("status", PROPOSAL_STATUS);
		DB_COLUMN_MAP.put("accountManager", "accountManager");
		DB_COLUMN_MAP.put("sosOrderId", "sosOrderId");
		DB_COLUMN_MAP.put("sosLineItemId", "propLineItem.sosLineItemID");
	}

	public static final Map<String, String> LINE_ITEM_COLUMN_MAP = new HashMap<String, String>();

	static {
		LINE_ITEM_COLUMN_MAP.put(ConstantStrings.NAME, "lineItemName");
		LINE_ITEM_COLUMN_MAP.put("comments", "comments");
		LINE_ITEM_COLUMN_MAP.put("startDate", "startDate");
		LINE_ITEM_COLUMN_MAP.put("endDate", "endDate");
		LINE_ITEM_COLUMN_MAP.put("flight", "flight");
		LINE_ITEM_COLUMN_MAP.put("partiallyCopiedDisplay", "partiallyCopiedUnbreakPackage");
		LINE_ITEM_COLUMN_MAP.put("lineItemID", "lineItemID");
		LINE_ITEM_COLUMN_MAP.put("orderNumber", "orderNumber");
		LINE_ITEM_COLUMN_MAP.put("placementName", "placementName");
		LINE_ITEM_COLUMN_MAP.put("impressionTotal", "impressionTotal");
		LINE_ITEM_COLUMN_MAP.put("totalInvestment", "totalInvestment");
		LINE_ITEM_COLUMN_MAP.put("rate", "rate");
		LINE_ITEM_COLUMN_MAP.put("lineItemSequence", "lineItemSequence");
		LINE_ITEM_COLUMN_MAP.put("productName", "productName");
		LINE_ITEM_COLUMN_MAP.put("sor", "sor");
		LINE_ITEM_COLUMN_MAP.put("reservationExpiryDate", "lineItemReservations.expirationDate");
		LINE_ITEM_COLUMN_MAP.put("sosSalesTargetName", "salesTargetAssocs.sosSalesTargetName");
		LINE_ITEM_COLUMN_MAP.put("reservationCreationDate", "createdDate");
		LINE_ITEM_COLUMN_MAP.put("pricingStatus", "pricingStatus");
		LINE_ITEM_COLUMN_MAP.put("lastRenewedOn", "lineItemReservations.lastRenewedOn");
		LINE_ITEM_COLUMN_MAP.put("viewableDisplayName", "viewabilityLevel");
	}

	public static final List<String> CLUSTER_SALEST_TARGET_LIST = new ArrayList<String>();
	static {
		CLUSTER_SALEST_TARGET_LIST.add("Invalid Cluster");
		CLUSTER_SALEST_TARGET_LIST.add("Predefined Display Section Cluster");
		CLUSTER_SALEST_TARGET_LIST.add("Predefined Display Sub Section Cluster");
		CLUSTER_SALEST_TARGET_LIST.add("NYTd Other");
		CLUSTER_SALEST_TARGET_LIST.add("Cluster");
		CLUSTER_SALEST_TARGET_LIST.add("Custom Display Section Cluster");
		CLUSTER_SALEST_TARGET_LIST.add("Custom Display Sub Section Cluster");

	}

	public static final Map<String, String> SALES_ORDER_MAP = new HashMap<String, String>();
	
	static{
		SALES_ORDER_MAP.put("sosOrderId", "salesOrder.salesOrderId");
		SALES_ORDER_MAP.put("advertiserName", "advertiser.customerId");
		SALES_ORDER_MAP.put("salesCategoryId", "salesOrder.territoryId");
		SALES_ORDER_MAP.put("lineItemId", "lineItemId");
		SALES_ORDER_MAP.put("startDate", "startDate");
		SALES_ORDER_MAP.put("endDate", "endDate");
		SALES_ORDER_MAP.put("productId", "productId");
		SALES_ORDER_MAP.put("salesTargetId", "salesTargetId");
		SALES_ORDER_MAP.put("sor", "shareOfReservation");
		SALES_ORDER_MAP.put("sosLineItemId", "lineItemId");
	}
	
	public static final Map<String, String> RESERVATION_SEARCH_COLUMN_MAP = new HashMap<String, String>();
	
	static{
		RESERVATION_SEARCH_COLUMN_MAP.put("proposalName", "proposal.name");
		RESERVATION_SEARCH_COLUMN_MAP.put("proposalId", "proposal.id");
		RESERVATION_SEARCH_COLUMN_MAP.put("salescategoryId", "proposal.sosSalesCategoryId");
		RESERVATION_SEARCH_COLUMN_MAP.put("advertiserId", "proposal.sosAdvertiserId");
		RESERVATION_SEARCH_COLUMN_MAP.put("productId", "sosProductId");
		RESERVATION_SEARCH_COLUMN_MAP.put("salesTargetId", "salesTargets.sosSalesTargetId");
		RESERVATION_SEARCH_COLUMN_MAP.put("amptLineItemId", "lineItemID");
		RESERVATION_SEARCH_COLUMN_MAP.put("startDate", "startDate");
		RESERVATION_SEARCH_COLUMN_MAP.put("endDate", "endDate");
		RESERVATION_SEARCH_COLUMN_MAP.put("status", "reservation.status");
		RESERVATION_SEARCH_COLUMN_MAP.put("reservationStatus", "reservation.status");
		RESERVATION_SEARCH_COLUMN_MAP.put("expiryDate", "reservation.expirationDate");
		RESERVATION_SEARCH_COLUMN_MAP.put("assignedUser", "assignedUser.userId");
		RESERVATION_SEARCH_COLUMN_MAP.put("lineItemId", "lineItemID");
		RESERVATION_SEARCH_COLUMN_MAP.put("sor", "sor");
		RESERVATION_SEARCH_COLUMN_MAP.put("salesTarget", "salesTargets.sosSalesTargetName");
		RESERVATION_SEARCH_COLUMN_MAP.put("productName", "productName");
		RESERVATION_SEARCH_COLUMN_MAP.put("advertiserName", "proposal.sosAdvertiserName");
		RESERVATION_SEARCH_COLUMN_MAP.put("optionName", "propOption.name");
		RESERVATION_SEARCH_COLUMN_MAP.put("sosLineItemId", "sosLineItemID");
	}
}
