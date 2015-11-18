/**
 *
 */
package com.nyt.mpt.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nyt.mpt.domain.Role;
import com.nyt.mpt.domain.SalesCategory;
import com.nyt.mpt.domain.User;
import com.nyt.mpt.form.SalesCategoryForm;
import com.nyt.mpt.form.UserForm;
import com.nyt.mpt.service.ISOSService;
import com.nyt.mpt.service.IUserService;
import com.nyt.mpt.util.AjaxFormSubmitResponse;
import com.nyt.mpt.util.Constants;
import com.nyt.mpt.util.CustomBindingResult;
import com.nyt.mpt.util.TableGrid;
import com.nyt.mpt.util.enums.ErrorCodes;
import com.nyt.mpt.util.enums.UserHelpCodes;
import com.nyt.mpt.validator.UserFormValidator;

/**
 * This <code>ManageUsersController</code> will handle all the request related to {@link User} and {@link Role} operation
 * @author Shishir.Srivastava
 */

@Controller
@RequestMapping("/manageusers/*")
public class ManageUsersController extends AbstractBaseController {

	private static final String USER_FORM = "userForm";

	private static final String LOGIN_NAME = "loginName";

	private static final Logger LOGGER = Logger.getLogger(ManageUsersController.class);

	private IUserService userService;

	private ISOSService sosService;

	private Properties defaultUserAddress;

	/**
	 * Return model and view for manage user screen.
	 * @param 	userForm
	 * 			Default {@link UserForm} when user clicks on the manage user in the left menu navigation
	 */
	@RequestMapping("/showuserdetails")
	public ModelAndView showDetailPage(@ModelAttribute(USER_FORM) final UserForm userForm) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("User detail page for userid: " + userForm.getUserId());
		}
		final ModelAndView view = new ModelAndView("manageUserDetailPage");
		view.addObject(USER_FORM, userForm);
		setRoleList(view);
		return view;
	}

	/**
	 * Return a map of assigned and available sales category for the selected user from grid
	 * @param 	userId
	 * 			The userId of the selected user from grid
	 */
	@ResponseBody
	@RequestMapping("/loadSalesCategories")
	public Map<String, List<SalesCategoryForm>> loadSalesCategories(@RequestParam final long userId) {
		final Map<String, List<SalesCategoryForm>> salesCatListMap = new HashMap<String, List<SalesCategoryForm>>();
		final Map<Long, String> availSalesCatList = sosService.getSalesCategories();
		final List<SalesCategory> userSalesCat = userService.getUserSalesCategories(userId);

		final List<SalesCategoryForm> userSalesCatListForm = new ArrayList<SalesCategoryForm>();
		final List<SalesCategoryForm> availSalesCatListForm = new ArrayList<SalesCategoryForm>();

		for (Long salesCategoryId : availSalesCatList.keySet()) {
			boolean userCategory = false;
			for (SalesCategory salesCategory : userSalesCat) {
				if (salesCategory.getSosSalesCategoryId() == salesCategoryId) {
					final SalesCategoryForm form = new SalesCategoryForm();
					form.setSalesCategoryId(salesCategoryId);
					form.setSalesCategoryName(availSalesCatList.get(salesCategoryId));
					userSalesCatListForm.add(form);
					userCategory = true;
					break;
				}
			}
			if (!userCategory) {
				final SalesCategoryForm form = new SalesCategoryForm();
				form.setSalesCategoryId(salesCategoryId);
				form.setSalesCategoryName(availSalesCatList.get(salesCategoryId));
				availSalesCatListForm.add(form);
			}
		}
		salesCatListMap.put("selectedSalCatListList", userSalesCatListForm);
		salesCatListMap.put("avaiableSalCatList", availSalesCatListForm);
		return salesCatListMap;
	}

	/**
	 * This method is used to load user default address
	 */
	@ResponseBody
	@RequestMapping("/loadDefaultAddress")
	public Properties loadDefaultAddress() {
		return defaultUserAddress;
	}

	/**
	 * Add list of all available roles in the system to model which will be shown in the drop-down
	 */
	@JsonIgnore
	private void setRoleList(final ModelAndView view) {
		final List<Role> allRoles = userService.getAllAssignableRole();
		final Map<Long, String> roleMap = new LinkedHashMap<Long, String>(allRoles.size());
		for (Role role : allRoles) {
			roleMap.put(role.getRoleId(), role.getDisplayName());
		}
		view.addObject("allRoles", roleMap);
	}

	/**
	 * Returns {@link ModelAndView} of all the user and their related information in the table grid
	 */
	@RequestMapping("/loadmastergridData")
	public ModelAndView loadMasterGridData(@ModelAttribute final TableGrid<UserForm> tblGrid,
			@RequestParam(value = "showAllUser", required = true) final boolean showAllUser) {
		final ModelAndView view = new ModelAndView("manageUserGridData");
		setGridData(tblGrid, showAllUser);
		view.addObject(tblGrid);
		return view;
	}

	/**
	 * Search given users in LDAP and return list of all user matching given string
	 * @param 	userName
	 */
	@ResponseBody
	@RequestMapping("/searchUser")
	public List<User> searchUser(@RequestParam final String userName) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Searching user from ldap with username: " + userName);
		}
		return userService.searchUserFromLdap(userName);
	}

	/**
	 * Save {@link User} to AMPT database
	 * @param 	response
	 * 			{@link HttpServletResponse} which response back when user has been saved successfully
	 * @param 	userForm
	 * 			{@link UserForm} which has all the information from the UI which we've to save in the AMPT database as a {@link User} 
	 * @return
	 * 			Save {@link User} to AMPT database
	 */
	@ResponseBody
	@RequestMapping("/saveuser")
	public AjaxFormSubmitResponse saveUser(final HttpServletResponse response, @ModelAttribute(USER_FORM) final UserForm userForm) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Saving user with userId: " + userForm.getUserId());
		}
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final CustomBindingResult results = new CustomBindingResult(USER_FORM, userForm);
		new UserFormValidator().validate(userForm, results);
		boolean isUserExists = false;
		try {
			isUserExists = userService.isUserExists(userForm.getLoginName());
		} catch (UsernameNotFoundException e) {
			LOGGER.info("Username Not Found for given name :" + userForm.getLoginName());
			if (userForm.getUserId() != 0) {
				results.rejectValue(LOGIN_NAME, ErrorCodes.MandatoryInputMissing, LOGIN_NAME,
						new Object[] { "Login Name" }, UserHelpCodes.HelpMandatoryInputMissing);
			}
		}

		if (isUserExists && userForm.getUserId() == 0) {
			results.rejectValue(LOGIN_NAME, ErrorCodes.DuplicateEntryGeneral, LOGIN_NAME,
					new Object[] { "Login Name" }, UserHelpCodes.DuplicateLoginName);
		}

		if (results.hasErrors()) {
			return constructResponse(response, ajaxResponse, results);
		} else {
			Long returnId;
			final User user = userForm.populate(new User());
			if (user.getUserId() == 0) {
				returnId = userService.createUser(user);
			} else {
				returnId = userService.updateUser(user, userForm.isForceUpdate());
			}
			ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, returnId);
		}
		return ajaxResponse;
	}

	/**
	 * Sets the grid data based on {@link FilterCriteria}, {@link PaginationCriteria}, {@link SortingCriteria} and <code>showAllUser</code> flag
	 * @param 	tblGrid
	 * 			{@link TableGrid} has all the information related to the table grid
	 * @param 	showAllUser
	 * 			<code>true</code> if, and only if, we want to see all the Active and Inactive users otherwise Active users will be shown in the grid by default
	 */
	private void setGridData(final TableGrid<UserForm> tblGrid, final boolean showAllUser) {
		final List<User> listUser = userService.getFilteredUserList(tblGrid.getFilterCriteria(), tblGrid.getPaginationCriteria(), tblGrid.getSortingCriteria(), showAllUser);

		if (!listUser.isEmpty()) {
			final int totalRecords = userService.getUserFilteredListCount(tblGrid.getFilterCriteria(), showAllUser);
			tblGrid.setGridData(convertDToListToFormLsit(listUser), totalRecords);
		}
	}

	/**
	 * Convert List of {@User} to list of {@link UserForm} which will be shown in the grid
	 * @param dtoList
	 * @return
	 */
	private List<UserForm> convertDToListToFormLsit(final List<User> dtoList) {
		final List<UserForm> formList = new ArrayList<UserForm>();
		for (User dto : dtoList) {
			final UserForm form = new UserForm();
			form.populateForm(dto);
			formList.add(form);
		}
		return formList;
	}

	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(final IUserService userService) {
		this.userService = userService;
	}

	public ISOSService getSosService() {
		return sosService;
	}

	public void setSosService(final ISOSService sosService) {
		this.sosService = sosService;
	}

	public Properties getDefaultUserAddress() {
		return defaultUserAddress;
	}

	public void setDefaultUserAddress(final Properties userAddress) {
		this.defaultUserAddress = userAddress;
	}
}
