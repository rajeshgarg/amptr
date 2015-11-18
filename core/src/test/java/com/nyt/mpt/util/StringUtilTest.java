package com.nyt.mpt.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.nyt.mpt.common.AbstractTest;
import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.LineItemTarget;
import com.nyt.mpt.domain.sos.OrderLineItem;
import com.nyt.mpt.service.ICalendarReservationService;
import com.nyt.mpt.service.ITargetingService;
import com.nyt.mpt.util.enums.SearchOption;
import com.nyt.mpt.util.filter.RangeFilterCriteria;

public class StringUtilTest extends AbstractTest {
	
	@Autowired
	private ICalendarReservationService reservationService;
	@Autowired
	private ITargetingService targetingService;

	@Test
	public void testEscapeSqlCharacters() {
		String input = "1230%14560'78965%8976";
		String input2 = "123014560'789658976";
		Assert.notNull(StringUtil.escapeSqlCharacters(input));
		Assert.isTrue(StringUtil.escapeSqlCharacters("").isEmpty());
		Assert.isTrue(!StringUtil.escapeSqlCharacters(input2).isEmpty());
	}

	@Test
	public void testTrimSpacesForCommaSeperatedValues() {
		String inputString = "1230 ,14560, 78965 ,8976";
		Assert.isTrue(!StringUtil.trimSpacesForCommaSeperatedValues(inputString).contains(ConstantStrings.SPACE));
		Assert.isTrue(!StringUtil.trimSpacesForCommaSeperatedValues("").contains(ConstantStrings.SPACE));
	}

	@Test
	public void testConvertStringArrayToLongArray() {
		String[] stringArr = new String[] { "1230", "14560", "78965", "8976" };
		Assert.notEmpty(StringUtil.convertStringArrayToLongArray(stringArr));
	}

	@Test
	public void testConvertStringToLongList() {
		String input = "1230,14560,78965,8976";
		Assert.isTrue(StringUtil.convertStringToLongList(input).size() == 4);
	}

	@Test
	public void testGetListFromArray() {
		String[] stringArr = new String[] { "1230", "14560", "78965", "8976" };
		Assert.isTrue(StringUtil.getListFromArray(stringArr).size() == 4);
	}
	
	@Test
	public void testgenerateCampaignName() {
		Assert.notNull(StringUtil.generateCampaignName("This string is for testing campaign name havving length more than 46"));
		Assert.notNull(StringUtil.generateCampaignName("This string is for testing campaign name"));
		Assert.notNull(StringUtil.generateCampaignName(""));
	}
	
	@Test
	public void testgenerateProposalName() {
		Assert.notNull(StringUtil.generateProposalName("CampaignName", "advertiserName", DateUtil.getCurrentDate(), "salesCategoryName"));
		Assert.notNull(StringUtil.generateProposalName("CampaignName", "", DateUtil.getCurrentDate(), "salesCategoryName"));
	}
	
	@Test
	public void testgeneratePackageName() {
		Assert.notNull(StringUtil.generatePackageName("This string is for testing Package Name havving length more than 46"));
		Assert.notNull(StringUtil.generatePackageName("This string is for testing Package Name"));
		Assert.notNull(StringUtil.generatePackageName(""));
	}
	
	@Test
	public void testgetTargetingStringForOrder() {
		List<RangeFilterCriteria> filterCriterias = new ArrayList<RangeFilterCriteria>();
		RangeFilterCriteria filterCriteria = getFilterCriteriaFor("date", SearchOption.BETWEEN);
		filterCriteria.setSearchStringFrom(DateUtil.getGuiDateString(DateUtil.getPriorDateFromCurrentDate(365)));
		filterCriteria.setSearchStringTo(DateUtil.getGuiDateString(DateUtil.getCurrentDate()));
		filterCriterias.add(filterCriteria);
		final List<OrderLineItem> sosLineItemLst = reservationService.getSalesOrderForReservationSearch(filterCriterias, new PaginationCriteria(0, 50), null);
		for (OrderLineItem orderLineItem : sosLineItemLst) {
			if(orderLineItem.getLineItemTargeting() != null && !orderLineItem.getLineItemTargeting().isEmpty()) {
				StringUtil.getTargetingStringForOrder(orderLineItem);
				break;
			}
		}
	}
	
	
	@Test
	public void testGetTargetingString(){
		PaginationCriteria pgCriteria = new PaginationCriteria(1, 50);
		final List<RangeFilterCriteria> filterCriteriaLst = new ArrayList<RangeFilterCriteria>();
		RangeFilterCriteria filterCriteria = new RangeFilterCriteria();
		filterCriteria.setSearchField("date");
		filterCriteria.setSearchOper(SearchOption.BETWEEN.toString());
		filterCriteria.setSearchStringFrom(DateUtil.getGuiDateString(DateUtil.getPriorDateFromCurrentDate(90)));
		filterCriteria.setSearchStringTo(DateUtil.getCurrentDateTimeString("MM/dd/yyyy"));
		filterCriteriaLst.add(filterCriteria);
		List<LineItem> lineItemLst = reservationService.getProposalsForReservationSearch(filterCriteriaLst, pgCriteria, null);
		if(lineItemLst != null && !lineItemLst.isEmpty()){
			for (LineItem lineItem : lineItemLst) {
				if(lineItem.getGeoTargetSet() != null && !lineItem.getGeoTargetSet().isEmpty()){
					final Map<Long, String> targetTypeMap = targetingService.getTargetTypeCriteria();
					final Map<Long, String> targetElementMap = targetingService.getAllTargetTypeElement();
					final Map<String, String> targetingMap = new LinkedHashMap<String, String>();
					for (LineItemTarget targeting : lineItem.getGeoTargetSet()) {
						final String targetType = targetTypeMap.get(targeting.getSosTarTypeId());
						String targetedElement = ConstantStrings.EMPTY_STRING;
						int counter = 0;
						for (String targetElement : targeting.getSosTarTypeElementId().split(ConstantStrings.COMMA)) {
							if (counter > 0) {
								targetedElement += ConstantStrings.COMMA + ConstantStrings.SPACE;
							}
							targetedElement += targetElementMap.get(Long.valueOf(targetElement));
							counter++;
						}
						targetingMap.put(targetType, targetedElement);
					}
					Assert.isTrue(StringUtil.getTargetingString(targetingMap).length() > 0);
					return;
				}
			}
		}
	}
	
	private RangeFilterCriteria getFilterCriteriaFor(final String field, final SearchOption oper) {
		final RangeFilterCriteria filterCriteria = new RangeFilterCriteria();
		filterCriteria.setSearchField(field);
		filterCriteria.setSearchOper(oper.toString());
		return filterCriteria;
	}
}
