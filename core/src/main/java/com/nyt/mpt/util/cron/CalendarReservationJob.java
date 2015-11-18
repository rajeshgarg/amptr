package com.nyt.mpt.util.cron;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.Proposal;
import com.nyt.mpt.domain.Role;
import com.nyt.mpt.domain.SalesCategory;
import com.nyt.mpt.domain.User;
import com.nyt.mpt.service.IProductService;
import com.nyt.mpt.service.IProposalService;
import com.nyt.mpt.service.ISOSService;
import com.nyt.mpt.service.IUserService;
import com.nyt.mpt.service.impl.ProposalUtilService;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.DateUtil;
import com.nyt.mpt.util.MailUtil;
import com.nyt.mpt.util.enums.CronJobNameEnum;
import com.nyt.mpt.util.enums.LineItemProductTypeEnum;
import com.nyt.mpt.util.enums.ReservationStatus;
import com.nyt.mpt.util.filter.RangeFilterCriteria;
import com.nyt.mpt.util.security.SecurityUtil;

/**
 * This <code>CalendarReservationJob</code> class includes methods for sending alert mails when reservation gets expired within 3 days.
 * 
 * @author rakesh.tewari
 */

public class CalendarReservationJob extends AbstractCronJob {

	private IProposalService proposalService;

	private IUserService userService;

	private ISOSService sosService;

	private MailUtil mailUtil;

	private String applicationURL;

	private String calendarRreservationEmailFrom;
	
	private ProposalUtilService proposalUtilService;
	
	private IProductService productService;

	private static final Logger LOGGER = Logger.getLogger(CalendarReservationJob.class);

	/* (non-Javadoc)
	 * @see org.springframework.scheduling.quartz.QuartzJobBean#executeInternal(org.quartz.JobExecutionContext)
	 */
	@Override
	protected void executeInternal(final JobExecutionContext context) throws JobExecutionException {
		if (!isJobActiveOnServer(CronJobNameEnum.CALENDAR_RESERVATION_EXPIRY_JOB)) {
			return;
		}
		super.setUserForCronJobs();
		final List<User> userList = userService.getFilteredUserList(null, null, null, false);
		final Map<Long, String> salesCateMap = sosService.getSalesCategories();

		final String expiryDate = DateUtil.getGuiDateTimeString(DateUtil.getCurrentMidnightDate().getTime());
		LOGGER.info("AMPT RESERVATION(S) EXPIRED on :" + expiryDate);
		updateLineItemReservationAndSentMail(salesCateMap, ReservationStatus.RELEASED, expiryDate, userList);

		LOGGER.info("SENT ALERT MAIL: EXPIRATION in 3 days on :" + DateUtil.getGuiDateTimeString(DateUtil.getMidnightDateAfterThreeDay().getTime()));
		updateLineItemReservationAndSentMail(salesCateMap, ReservationStatus.RE_NEW, DateUtil.getGuiDateTimeString(DateUtil.getMidnightDateAfterThreeDay().getTime()), userList);
	}

	/**
	 * Updates LineItem Reservation and sends email
	 * @param salesCateMap
	 * @param status
	 * @param expiryDate
	 * @param userLst
	 */
	private void updateLineItemReservationAndSentMail(final Map<Long, String> salesCateMap, final ReservationStatus status, final String expiryDate, final List<User> userLst) {
		final List<LineItem> lineItemLst = proposalService.getProposalsForReservationSearch(getFilterCriteria(expiryDate, status));
		final List<LineItem> lineItemEmailLst = new ArrayList<LineItem>();
		if (lineItemLst != null && !lineItemLst.isEmpty()) {
			for (LineItem lineItem : lineItemLst) {
				if(LineItemProductTypeEnum.EMAIL.name().equals(lineItem.getProductType().name())){
					lineItemEmailLst.add(lineItem);
				}
			}
			if(!lineItemLst.isEmpty()){
				sentMailToUser(salesCateMap, status, getLineItemMapBySalesCategory(lineItemLst), userLst);
			}
			if(!lineItemEmailLst.isEmpty()){
				LOGGER.info("AMPT RESERVATION(S) EXPIRATION MAIL TO EMAIL PRODUCT MANAGERS");
				sentMailToEmailProductManagers(status, lineItemEmailLst);
			}
		}
	}

	/**
	 * Updates LineItems Reservation data
	 * @param 	lineItemLst
	 * 			List of {@link LineItem} for them it's updating the Reservation
	 */
	private void updateLineItemReservations(final List<LineItem> lineItemLst) {
		for (LineItem lineItem : lineItemLst) {
			lineItem.getReservation().setStatus(ReservationStatus.RELEASED);
			proposalService.updateLineItemReservations(lineItem.getReservation());
		}
	}

	/**
	 * Returns {@link RangeFilterCriteria} for 'expiryDate' and Reservation Status ('RE_NEW', 'HOLD')
	 * @param expiryDate
	 * @param status 
	 * @return
	 */
	private List<RangeFilterCriteria> getFilterCriteria(final String expiryDate, final ReservationStatus status) {
		final List<RangeFilterCriteria> criteriaLst = new ArrayList<RangeFilterCriteria>();

		final RangeFilterCriteria dateFilterCriteria = new RangeFilterCriteria();
		dateFilterCriteria.setSearchField("expiryDate");
		dateFilterCriteria.setSearchString(expiryDate);
		if (status.equals(ReservationStatus.RELEASED)) {
			dateFilterCriteria.setSearchOper("le");
		} else {
			dateFilterCriteria.setSearchOper("eq");
		}
		criteriaLst.add(dateFilterCriteria);

		final RangeFilterCriteria statusFilterCriteria = new RangeFilterCriteria();
		statusFilterCriteria.setSearchField(ConstantStrings.STATUS);
		statusFilterCriteria.setSearchString(String.valueOf(ReservationStatus.RE_NEW) + ConstantStrings.COMMA + String.valueOf(ReservationStatus.HOLD));
		criteriaLst.add(statusFilterCriteria);

		return criteriaLst;
	}
	
	/**
	 * Sends Email to Product Managers
	 * @param status
	 * @param lineItemEmailLst
	 */
	private void sentMailToEmailProductManagers(final ReservationStatus status, final List<LineItem> lineItemEmailLst) {
		final StringBuffer textMsg = new StringBuffer();
		for (LineItem lineItem : lineItemEmailLst) {
			if (textMsg != null && textMsg.length() > 0) {
				textMsg.append("\n\n\n");
			}
			textMsg.append(createTextMsg(lineItem));
		}
		final List<User> userLst = userService.getUserBasedOnRoleList(new String[] { SecurityUtil.EMAIL_PRODUCT_MANAGER });
		final StringBuffer mail = new StringBuffer();
		if (userLst != null && !userLst.isEmpty()) {
			for (User userDB : userLst) {
				if (mail.length() > 0) {
					mail.append(ConstantStrings.COMMA);
				}
				mail.append(userDB.getEmail());
			}
			
			
			if (textMsg != null && textMsg.length() > 0) {
				sendMail(mail.toString(), getSubjectOfMail(status, ConstantStrings.EMPTY_STRING), ConstantStrings.EMPTY_STRING, textMsg);
			}
		}
	}

	/**
	 * Sends Email to List of users
	 * @param 	salesCateMap
	 * 			Map of Sales Categories which has information to create the subject for the Email
	 * @param 	status
	 * 			{@link ReservationStatus} to check the status of the Reservation
	 * @param 	reserveLineItemMap
	 * 			Includes the Ids of Sales Categories and List of corresponding {@link LineItem}
	 * @param 	userLst
	 * 			List of {@link User}
	 */
	private void sentMailToUser(final Map<Long, String> salesCateMap, final ReservationStatus status, final Map<Long, List<LineItem>> reserveLineItemMap, final List<User> userLst) {
		final Set<Long> proposalSalesCategoriesSet = reserveLineItemMap.keySet();
		for (Long salesCategory : proposalSalesCategoriesSet) {
			final String mailTo = getMailIdBasedSalesCategory(salesCategory, userLst);
			LOGGER.info("Email id of all planner based on sales category, email id : " + mailTo);
			final List<LineItem> lineItemLst = reserveLineItemMap.get(salesCategory);
			if (lineItemLst != null && !lineItemLst.isEmpty()) {
				if (status.equals(ReservationStatus.RELEASED)) {
					LOGGER.info("Update Reservation Status by 'RELEASED'");
					updateLineItemReservations(lineItemLst);
				}
				final StringBuffer textMsg = new StringBuffer();
				boolean isHomePageResrvtn =  false;
				List<Long> productClassIdLst = productService.getProductClassIdLstByDisplayName();
				for (LineItem lineItem : lineItemLst) {
					if (textMsg != null && textMsg.length() > 0) {
						textMsg.append("\n\n\n");
					}
					textMsg.append(createTextMsg(lineItem));
					if(productClassIdLst.contains(lineItem.getSosProductClass())){
						isHomePageResrvtn = true;
					}
				}
				
				String  mailCc  = "";
				if(isHomePageResrvtn){
					final List<String> roles = new ArrayList<String>();
					roles.add(SecurityUtil.ADMIN);
					roles.add(SecurityUtil.PRODUCT_OWNER);
		 
					mailCc=proposalUtilService.emailIdOfUserBasedOnRole(roles);
				}else{
					mailCc = getAllADMUser().toString();
				}
				
				if (textMsg != null && textMsg.length() > 0) {
					//sendMail(mailTo, getSubjectOfMail(status, salesCateMap.get(salesCategory)), getAllADMUser().toString(), textMsg);
					sendMail(mailTo, getSubjectOfMail(status, salesCateMap.get(salesCategory)), mailCc, textMsg);
				}
			}
		}
	}

	/**
	 * Returns comma separated Email ids of all Proposal Planner based on Sales Categories
	 * @param salesCategory
	 * @param userLst
	 * @return
	 * 			Returns comma separated Email ids
	 */
	private String getMailIdBasedSalesCategory(final Long salesCategory, final List<User> userLst) {
		LOGGER.info("Get email id of all proposal planner based on sales category : " + salesCategory);
		final StringBuffer stUserEmailID = new StringBuffer();
		for (User userDB : userLst) {
			final Set<Role> userRoles = userDB.getUserRoles();
			for (Role role : userRoles) {
				if (SecurityUtil.MEDIA_PLANNER.equals(role.getRoleName())) {
					final Set<SalesCategory> userSalesCategories = userDB.getSalesCategories();
					for (SalesCategory userSalesCategory : userSalesCategories) {
						if (salesCategory.equals(Long.valueOf(userSalesCategory.getSosSalesCategoryId()))) {
							if (stUserEmailID.length() > 0) {
								stUserEmailID.append(ConstantStrings.COMMA);
							}
							stUserEmailID.append(userDB.getEmail());
							break;
						}
					}
				}
			}
		}
		return stUserEmailID.toString();
	}

	/**
	 * Returns the subject of Email based on {@link ReservationStatus} and Sales Category
	 * @param status
	 * @param salesCategoryName
	 * @return
	 */
	private String getSubjectOfMail(final ReservationStatus status, final String salesCategoryName) {
		LOGGER.info("Get subject of mail based on reservationStatus status : " + status + " and sales category name" + salesCategoryName);
		final StringBuffer subject = new StringBuffer(ConstantStrings.EMPTY_STRING);
		if (status.equals(ReservationStatus.RELEASED)) {
			subject.append(ConstantStrings.RESERVATION_EMAIL_KEYWORD);
			subject.append("(S)");
			subject.append(ConstantStrings.SPACE);
			subject.append(ReservationStatus.RELEASED.getDisplayName());
			subject.append(ConstantStrings.SPACE);
			subject.append("ON");
			subject.append(ConstantStrings.SPACE);
			subject.append(DateUtil.getGuiDateString(DateUtil.getCurrentMidnightDate()));
			if (StringUtils.isNotBlank(salesCategoryName)) {
				subject.append(ConstantStrings.SPACE);
				subject.append(ConstantStrings.COLON);
				subject.append(ConstantStrings.SPACE);
				subject.append(salesCategoryName);
			}
		} else {
			subject.append(ConstantStrings.RESERVATION_EMAIL_KEYWORD);
			subject.append("(S)");
			subject.append(ConstantStrings.SPACE);
			subject.append(ConstantStrings.ALERT);
			if (StringUtils.isNotBlank(salesCategoryName)) {
				subject.append(ConstantStrings.SPACE);
				subject.append(ConstantStrings.COLON);
				subject.append(ConstantStrings.SPACE);
				subject.append(salesCategoryName);
			}
			subject.append(ConstantStrings.SPACE);
			subject.append("expiration in 3 days on");
			subject.append(ConstantStrings.SPACE);
			subject.append(ConstantStrings.COLON);
			subject.append(ConstantStrings.SPACE);
			subject.append(DateUtil.getGuiDateString(DateUtil.getMidnightDateAfterThreeDay()));
		}
		return subject.toString();
	}

	/**
	 * Returns the Email Ids of all the Administrators having the role <code>ADM</code>
	 * @return
	 */
	private StringBuffer getAllADMUser() {
		LOGGER.info("Return mail id of admin users");
		final StringBuffer mailCC = new StringBuffer();
		final List<String> roles = new ArrayList<String>();
		roles.add(SecurityUtil.ADMIN);
		final List<User> userADMLst = userService.getUserBasedOnRoleList(roles.toArray(new String[0]));
		if (userADMLst != null && !userADMLst.isEmpty()) {
			for (User userDB : userADMLst) {
				if (mailCC.length() > 0) {
					mailCC.append(ConstantStrings.COMMA);
				}
				mailCC.append(userDB.getEmail());
			}
		}
		return mailCC;
	}

	/**
	 * Sends Email for given parameter
	 * @param mailTo
	 * @param subject
	 * @param carbnCopy
	 * @param textMsg
	 */
	private void sendMail(final String mailTo, final String subject, final String carbnCopy, final StringBuffer textMsg) {
		final Map<String, String> mailProps = new HashMap<String, String>();
		mailProps.put("mailTo", mailTo);
		mailProps.put("subject", subject);
		mailProps.put("mailFrom", calendarRreservationEmailFrom);
		mailProps.put("textMsg", textMsg.toString());
		if(StringUtils.isNotBlank(carbnCopy)){
			mailProps.put("cc", carbnCopy.trim());
		}
		mailUtil.sendMail(mailUtil.setMessageInfo(mailProps));
	}

	/**
	 * Create body for the Email
	 * @param lineItem
	 * @return
	 */
	private String createTextMsg(final LineItem lineItem) {
		final StringBuffer textMsg = new StringBuffer(150);
		final Proposal Proposal = lineItem.getProposalVersion().getProposalOption().getProposal();

		textMsg.append("\t\n Proposal Name: ").append(Proposal.getName());
		textMsg.append("\t\n LineItemID: ").append(lineItem.getLineItemID());
		textMsg.append("\t\n Ad Unit: ").append(lineItem.getProductName());
		textMsg.append("\t\n Sales Target : ").append(lineItem.getLineItemSalesTargetAssocs().get(0).getSosSalesTargetName());
		textMsg.append("\t\n Target: ").append(StringUtils.isBlank(lineItem.getTargetingString()) ? ConstantStrings.EMPTY_STRING : lineItem.getTargetingString());
		if (lineItem.getStartDate() != null && lineItem.getEndDate() != null) {
			textMsg.append("\t\n Flight: ").append(DateUtil.getGuiDateString(lineItem.getStartDate())).append(" - ").append(DateUtil.getGuiDateString(lineItem.getEndDate()));
		}

		final String accountManager = Proposal.getAccountManager();
		textMsg.append("\t\n Account Manager: ").append(StringUtils.isBlank(accountManager) ? ConstantStrings.EMPTY_STRING : accountManager);
		if (StringUtils.isNotBlank(applicationURL)) {
			textMsg.append("\t\n\t\n").append(applicationURL).append(ConstantStrings.PROPOSAL_URL).append(Proposal.getId());
		}
		return textMsg.toString();

	}

	/**
	 * Return map of List of {@link LineItem} and proposal Sales Category Id as key
	 * @param lineItemLst
	 * @return
	 */
	private Map<Long, List<LineItem>> getLineItemMapBySalesCategory(final List<LineItem> lineItemLst) {
		LOGGER.info("Return map of LineItem list, where proposal sales category as key");
		final Map<Long, List<LineItem>> reserveLineItemMap = new HashMap<Long, List<LineItem>>();
		for (LineItem lineItem : lineItemLst) {
			final Proposal proposal = lineItem.getProposalVersion().getProposalOption().getProposal();
			if (reserveLineItemMap.containsKey(proposal.getSosSalesCategoryId())) {
				reserveLineItemMap.get(proposal.getSosSalesCategoryId()).add(lineItem);
			} else {
				final List<LineItem> lineItemList = new ArrayList<LineItem>(2);
				lineItemList.add(lineItem);
				reserveLineItemMap.put(proposal.getSosSalesCategoryId(), lineItemList);
			}
		}
		return reserveLineItemMap;
	}

	public void setProposalService(final IProposalService proposalService) {
		this.proposalService = proposalService;
	}

	public void setUserService(final IUserService userService) {
		this.userService = userService;
	}

	public void setMailUtil(final MailUtil mailUtil) {
		this.mailUtil = mailUtil;
	}

	public void setSosService(final ISOSService sosService) {
		this.sosService = sosService;
	}

	public void setCalendarRreservationEmailFrom(final String calendarRreservationEmailFrom) {
		this.calendarRreservationEmailFrom = calendarRreservationEmailFrom;
	}

	public void setApplicationURL(final String applicationURL) {
		this.applicationURL = applicationURL;
	}

	 
	/**
	 * @param proposalUtilService the proposalUtilService to set
	 */
	public void setProposalUtilService(ProposalUtilService proposalUtilService) {
		this.proposalUtilService = proposalUtilService;
	}

	public void setProductService(IProductService productService) {
		this.productService = productService;
	}
}
