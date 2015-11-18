package com.nyt.mpt.multipleCalendar;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.nyt.mpt.common.AbstractTest;
import com.nyt.mpt.domain.User;
import com.nyt.mpt.domain.UserFilter;
import com.nyt.mpt.service.IMultipleCalendarService;
import com.nyt.mpt.service.IUserService;
import com.nyt.mpt.util.enums.UserFilterTypeEnum;

public class MultipleCalendarTest extends AbstractTest {

	@Autowired
	private IMultipleCalendarService multipleCalendarService;
	@Autowired
	private IUserService userService;
	UserFilter filter = new UserFilter();
	User usr;

	@Before
	public void setup() {
		super.setAuthenticationInfo();
		getDummyFilter();
	}

	@Test
	public void testgetUserFiltersByType() {
		final Map<Long, String> userMap = new LinkedHashMap<Long, String>();
		userMap.put(usr.getUserId(), usr.getFullName());
		List<UserFilter> usrFltrLst = multipleCalendarService.getUserFiltersByType(UserFilterTypeEnum.MULTIPLE_CALENDAR.name(), userMap.keySet());
		Assert.assertFalse(usrFltrLst.isEmpty());
	}

	@Test
	public void testgetFiltersById() {
		Assert.assertNotNull(multipleCalendarService.getFiltersById(filter.getFilterId()));
	}

	@Test
	public void testisDuplicateFilterName() {
		Assert.assertFalse(multipleCalendarService.isDuplicateFilterName(filter.getFilterName(), filter.getFilterId(), usr.getUserId()));
		Assert.assertTrue(multipleCalendarService.isDuplicateFilterName(filter.getFilterName(), 0L, usr.getUserId()));
	}

	@Test
	public void testUpdateFilter() {
		Assert.assertNotNull(multipleCalendarService.saveFilter(filter));
	}

	@Test
	public void testDeleteFilter() {
		multipleCalendarService.deleteFilter(filter.getFilterId());
		Assert.assertTrue(true);
	}

	private void getDummyFilter() {
		usr = userService.getUserList().get(0);
		filter.setFilterData("FilterData");
		filter.setFilterName("jUnit Filter");
		filter.setFilterType(UserFilterTypeEnum.MULTIPLE_CALENDAR.name());
		filter.setUserId(usr.getUserId());
		multipleCalendarService.saveFilter(filter);
		getTransactionManager().getSessionFactory().getCurrentSession().flush();
	}
}
