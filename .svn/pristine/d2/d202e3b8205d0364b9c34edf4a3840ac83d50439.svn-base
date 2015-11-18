package com.nyt.mpt.util;

import org.junit.Test;
import org.springframework.util.Assert;

import com.nyt.mpt.util.enums.BehavioralTargetTypeEnum;
import com.nyt.mpt.util.enums.CreativeType;
import com.nyt.mpt.util.enums.Criticality;
import com.nyt.mpt.util.enums.CronJobNameEnum;
import com.nyt.mpt.util.enums.DateCriteriaEnum;
import com.nyt.mpt.util.enums.DocumentForEnum;
import com.nyt.mpt.util.enums.DocumentTypeEnum;
import com.nyt.mpt.util.enums.EmailReservationStatus;
import com.nyt.mpt.util.enums.EmailScheduleFrequency;
import com.nyt.mpt.util.enums.ErrorMessageType;
import com.nyt.mpt.util.enums.LineItemExceptionEnum;
import com.nyt.mpt.util.enums.LineItemPriceTypeEnum;
import com.nyt.mpt.util.enums.LineItemProductTypeEnum;
import com.nyt.mpt.util.enums.PriceType;
import com.nyt.mpt.util.enums.PricingStatus;
import com.nyt.mpt.util.enums.ProposalStatus;
import com.nyt.mpt.util.enums.ReservationStatus;
import com.nyt.mpt.util.enums.SearchOption;
import com.nyt.mpt.util.enums.SfProposalRevisionTypeEnum;
import com.nyt.mpt.util.enums.UserFilterTypeEnum;
import com.nyt.mpt.util.enums.Weekdays;

public class EnumTest {

	@Test
	public void testBehavioralTargetTypeEnum() {
		Assert.notNull(BehavioralTargetTypeEnum.LEVEL1);
		Assert.notNull(BehavioralTargetTypeEnum.LEVEL2);
	}

	@Test
	public void testDateCriteriaEnum() {
		Assert.notNull(DateCriteriaEnum.DAY_AF_TOMORROW.getDisplayName());
		Assert.notNull(DateCriteriaEnum.DAY_BF_YESTERDAY.getDisplayName());
		Assert.notNull(DateCriteriaEnum.LAST_WEEK.getDisplayName());
		Assert.notNull(DateCriteriaEnum.THIS_WEEK.getDisplayName());
		Assert.notNull(DateCriteriaEnum.TO_DAY.getDisplayName());
		Assert.notNull(DateCriteriaEnum.TOMORROW.getDisplayName());
		Assert.notNull(DateCriteriaEnum.YESTERDAY.getDisplayName());
		Assert.notNull(DateCriteriaEnum.DAY_AF_TOMORROW.getDayDifferenceFormToDay());
		Assert.notNull(DateCriteriaEnum.DAY_BF_YESTERDAY.getDayDifferenceFormToDay());
		Assert.notNull(DateCriteriaEnum.LAST_WEEK.getDayDifferenceFormToDay());
		Assert.notNull(DateCriteriaEnum.THIS_WEEK.getDayDifferenceFormToDay());
		Assert.notNull(DateCriteriaEnum.TO_DAY.getDayDifferenceFormToDay());
		Assert.notNull(DateCriteriaEnum.TOMORROW.getDayDifferenceFormToDay());
		Assert.notNull(DateCriteriaEnum.YESTERDAY.getDayDifferenceFormToDay());
		Assert.notNull(DateCriteriaEnum.findByName(DateCriteriaEnum.TOMORROW.getDisplayName()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDateCriteriaEnumE() {
		Assert.notNull(DateCriteriaEnum.findByName("jUnit"));
	}

	@Test
	public void testDocumentForEnum() {
		Assert.notNull(DocumentForEnum.CREATIVE.getDisplayName());
		Assert.notNull(DocumentForEnum.PACKAGE.getDisplayName());
		Assert.notNull(DocumentForEnum.PRODUCT.getDisplayName());
		Assert.notNull(DocumentForEnum.PROPOSAL.getDisplayName());
		Assert.notNull(DocumentForEnum.findByName(DocumentForEnum.PROPOSAL.getDisplayName()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDocumentForEnumE() {
		Assert.notNull(DocumentForEnum.findByName("jUnit"));
	}

	@Test
	public void testDocumentTypeEnum() {
		Assert.notNull(DocumentTypeEnum.BMP.getDisplayName());
		Assert.notNull(DocumentTypeEnum.DOC.getDisplayName());
		Assert.notNull(DocumentTypeEnum.EXCEL.getDisplayName());
		Assert.notNull(DocumentTypeEnum.GIF.getDisplayName());
		Assert.notNull(DocumentTypeEnum.JPG.getDisplayName());
		Assert.notNull(DocumentTypeEnum.PDF.getDisplayName());
		Assert.notNull(DocumentTypeEnum.PNG.getDisplayName());
		Assert.notNull(DocumentTypeEnum.PPT.getDisplayName());
		Assert.notNull(DocumentTypeEnum.findByDisplayName(DocumentTypeEnum.PPT.getDisplayName()));
		Assert.notNull(DocumentTypeEnum.findByName(DocumentTypeEnum.BMP.toString()));
		Assert.isNull(DocumentTypeEnum.findByDisplayName(DocumentForEnum.PROPOSAL.getDisplayName()));
		Assert.isNull(DocumentTypeEnum.findByName(DocumentForEnum.PROPOSAL.toString()));
		Assert.notNull(DocumentTypeEnum.getDocType("Test.pdf"));
		Assert.isTrue(DocumentTypeEnum.isAllowedDocumentType("Test.pdf"));
		Assert.isTrue(!DocumentTypeEnum.isAllowedDocumentType("Test.txt"));
	}

	@Test
	public void testLineItemExceptionEnum() {
		Assert.notNull(LineItemExceptionEnum.EXPIREDPACKAGE.getDisplayName());
		Assert.notNull(LineItemExceptionEnum.EXPIREDPRODUCT.getDisplayName());
		Assert.notNull(LineItemExceptionEnum.EXPIREDPROPOSAL.getDisplayName());
		Assert.notNull(LineItemExceptionEnum.EXPIREDSALESTARGET.getDisplayName());
		Assert.notNull(LineItemExceptionEnum.EXPIREDSALESTARGETPRODUCT.getDisplayName());
		Assert.notNull(LineItemExceptionEnum.NOPRICING.getDisplayName());
		Assert.notNull(LineItemExceptionEnum.NOSOR.getDisplayName());
		Assert.notNull(LineItemExceptionEnum.PRICING.getDisplayName());
		Assert.notNull(LineItemExceptionEnum.RICHMEDIAWITHLESSCPM.getDisplayName());
		Assert.notNull(LineItemExceptionEnum.SOVISGREATERTHANHUNDRED.getDisplayName());
		Assert.notNull(LineItemExceptionEnum.TOTALINVESTMENT.getDisplayName());
		Assert.notNull(LineItemExceptionEnum.UNBREAKABLEPACKAGE.getDisplayName());
		Assert.notNull(LineItemExceptionEnum.findByName(LineItemExceptionEnum.NOSOR.toString()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testLineItemExceptionEnumE() {
		Assert.notNull(LineItemExceptionEnum.findByName("jUnit"));
	}

	@Test
	public void testLineItemPriceTypeEnum() {
		Assert.notNull(LineItemPriceTypeEnum.ADDEDVALUE.getDisplayName());
		Assert.notNull(LineItemPriceTypeEnum.CPM.getDisplayName());
		Assert.notNull(LineItemPriceTypeEnum.CUSTOMUNIT.getDisplayName());
		Assert.notNull(LineItemPriceTypeEnum.FLATRATE.getDisplayName());
		Assert.notNull(LineItemPriceTypeEnum.PREEMPTIBLE.getDisplayName());
		Assert.notNull(LineItemPriceTypeEnum.ADDEDVALUE.getOptionValue());
		Assert.notNull(LineItemPriceTypeEnum.CPM.getOptionValue());
		Assert.notNull(LineItemPriceTypeEnum.CUSTOMUNIT.getOptionValue());
		Assert.notNull(LineItemPriceTypeEnum.FLATRATE.getOptionValue());
		Assert.notNull(LineItemPriceTypeEnum.PREEMPTIBLE.getOptionValue());
		Assert.notNull(LineItemPriceTypeEnum.getAllPriceType());
	}

	@Test
	public void testLineItemProductTypeEnum() {
		Assert.notNull(LineItemProductTypeEnum.EMAIL.getShortName());
		Assert.notNull(LineItemProductTypeEnum.RESERVABLE.getShortName());
		Assert.notNull(LineItemProductTypeEnum.STANDARD.getShortName());
		Assert.notNull(LineItemProductTypeEnum.findByShortName(LineItemProductTypeEnum.STANDARD.getShortName()));
		Assert.notNull(LineItemProductTypeEnum.findByName(LineItemProductTypeEnum.STANDARD.toString()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testLineItemProductTypeEnumE() {
		Assert.notNull(LineItemProductTypeEnum.findByShortName("Z"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testLineItemProductTypeEnumE1() {
		Assert.notNull(LineItemProductTypeEnum.findByName("junit"));
	}

	@Test
	public void testSfProposalRevisionTypeEnum() {
		Assert.notNull(SfProposalRevisionTypeEnum.REVISION.getDisplayName());
		Assert.notNull(SfProposalRevisionTypeEnum.SALESORDER.getDisplayName());
	}

	@Test
	public void testUserFilterTypeEnum() {
		Assert.notNull(UserFilterTypeEnum.DASHBOARD.getName());
		Assert.notNull(UserFilterTypeEnum.MULTIPLE_CALENDAR.getName());
	}

	@Test
	public void testCronJobNameEnum() {
		Assert.notNull(CronJobNameEnum.CALENDAR_RESERVATION_EXPIRY_JOB.getName());
		Assert.notNull(CronJobNameEnum.DELETE_INACTIVE_PRODUCT_ASSOC_JOB.getName());
		Assert.notNull(CronJobNameEnum.SALESFORCE_PROPOSAL_INTEGRATION_JOB.getName());
		Assert.notNull(CronJobNameEnum.SECTION_WEIGHT_CALCULATION.getName());
		Assert.notNull(CronJobNameEnum.UPDATE_EXPIRED_PROPOSAL_STATUS.getName());
	}

	@Test
	public void testCreativeType() {
		Assert.notNull(CreativeType.HTML5.getDisplayValue());
		Assert.notNull(CreativeType.RICH_MEDIA.getDisplayValue());
		Assert.notNull(CreativeType.STANDARD.getDisplayValue());
		Assert.notNull(CreativeType.TEXT.getDisplayValue());
		Assert.notNull(CreativeType.VIDEO.getDisplayValue());
		Assert.notNull(CreativeType.findByName("HTML5"));
		Assert.notNull(CreativeType.getSpecTypeMap());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreativeTypeE() {
		Assert.notNull(CreativeType.findByName("junit"));
	}

	@Test
	public void testCriticality() {
		Assert.notNull(Criticality.REGULAR.getDisplayName());
		Assert.notNull(Criticality.URGENT.getDisplayName());
		Assert.notNull(Criticality.findByName(Criticality.REGULAR.toString()));
		Assert.notNull(Criticality.findByDisplayName(Criticality.REGULAR.getDisplayName()));
		Assert.notNull(Criticality.getCriticalityMap());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCriticalityE() {
		Assert.notNull(Criticality.findByName("junit"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCriticalityE1() {
		Assert.notNull(Criticality.findByDisplayName("junit"));
	}

	@Test
	public void testEmailReservationStatus() {
		Assert.notNull(EmailReservationStatus.AVAILABLE.getDisplayName());
		Assert.notNull(EmailReservationStatus.OVERBOOKED.getDisplayName());
		Assert.notNull(EmailReservationStatus.RESERVED.getDisplayName());
		Assert.notNull(EmailReservationStatus.SOLD.getDisplayName());
	}

	@Test
	public void testEmailScheduleFrequency() {
		Assert.notNull(EmailScheduleFrequency.BIWEEKLY.getDisplayName());
		Assert.notNull(EmailScheduleFrequency.WEEKLY.getDisplayName());
		Assert.notNull(EmailScheduleFrequency.findByName(EmailScheduleFrequency.WEEKLY.toString()));
		Assert.notNull(EmailScheduleFrequency.getFrequencyMap());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmailScheduleFrequencyE() {
		Assert.notNull(EmailScheduleFrequency.findByName("junit"));
	}

	@Test
	public void testErrorMessageType() {
		Assert.notNull(ErrorMessageType.CONFIRM.getDisplayName());
		Assert.notNull(ErrorMessageType.ERROR.getDisplayName());
		Assert.notNull(ErrorMessageType.PROPOSAL_ERROR.getDisplayName());
		Assert.notNull(ErrorMessageType.SOS_INTEGRATION_ERROR.getDisplayName());
		Assert.notNull(ErrorMessageType.SOS_VIOLATION.getDisplayName());
		Assert.notNull(ErrorMessageType.TEMPLATE_ERROR.getDisplayName());
		Assert.notNull(ErrorMessageType.WARNING.getDisplayName());
		Assert.notNull(ErrorMessageType.YIELDEX_AVAILS_ERROR.getDisplayName());
		Assert.notNull(ErrorMessageType.findByName(ErrorMessageType.ERROR.toString()));

	}

	@Test(expected = IllegalArgumentException.class)
	public void testErrorMessageTypeE() {
		Assert.notNull(ErrorMessageType.findByName("junit"));
	}

	@Test
	public void testPriceType() {
		Assert.notNull(PriceType.Gross.getDisplayName());
		Assert.notNull(PriceType.Net.getDisplayName());
		Assert.notNull(PriceType.getPriceTypeMap());
	}

	@Test
	public void testPricingStatus() {
		Assert.notNull(PricingStatus.PRICING_APPROVED.getDisplayName());
		Assert.notNull(PricingStatus.SYSTEM_APPROVED.getDisplayName());
		Assert.notNull(PricingStatus.UNAPPROVED.getDisplayName());
		Assert.notNull(PricingStatus.findByDisplayName("Unapproved"));
		Assert.notNull(PricingStatus.findByName(PricingStatus.PRICING_APPROVED.toString()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPricingStatusE() {
		Assert.notNull(PricingStatus.findByDisplayName("junit"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPricingStatusE1() {
		Assert.notNull(PricingStatus.findByName("junit"));
	}

	@Test
	public void testProposalStatus() {
		Assert.notNull(ProposalStatus.DELETED.getDisplayName());
		Assert.notNull(ProposalStatus.EXPIRED.getDisplayName());
		Assert.notNull(ProposalStatus.INPROGRESS.getDisplayName());
		Assert.notNull(ProposalStatus.PROPOSED.getDisplayName());
		Assert.notNull(ProposalStatus.REJECTED_BY_CLIENT.getDisplayName());
		Assert.notNull(ProposalStatus.REVIEW.getDisplayName());
		Assert.notNull(ProposalStatus.SOLD.getDisplayName());
		Assert.notNull(ProposalStatus.UNASSIGNED.getDisplayName());
		Assert.notNull(ProposalStatus.findByName(ProposalStatus.UNASSIGNED.toString()));
		Assert.notNull(ProposalStatus.getProposalStatusMap());

		Assert.notNull(ProposalStatus.getProposalStatusAccessMap(ProposalStatus.UNASSIGNED));
		Assert.notNull(ProposalStatus.getProposalStatusAccessMap(ProposalStatus.INPROGRESS));
		Assert.notNull(ProposalStatus.getProposalStatusAccessMap(ProposalStatus.REVIEW));
		Assert.notNull(ProposalStatus.getProposalStatusAccessMap(ProposalStatus.PROPOSED));
		Assert.notNull(ProposalStatus.getProposalStatusAccessMap(ProposalStatus.EXPIRED));
		Assert.notNull(ProposalStatus.getProposalStatusAccessMap(ProposalStatus.REJECTED_BY_CLIENT));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testProposalStatusE() {
		Assert.notNull(ProposalStatus.findByName("junit"));
	}

	@Test
	public void testReservationStatus() {
		Assert.notNull(ReservationStatus.DELETED.getDisplayName());
		Assert.notNull(ReservationStatus.HOLD.getDisplayName());
		Assert.notNull(ReservationStatus.RE_NEW.getDisplayName());
		Assert.notNull(ReservationStatus.RELEASED.getDisplayName());
		Assert.notNull(ReservationStatus.findByName(ReservationStatus.DELETED.toString()));
		Assert.notNull(ReservationStatus.getReservationStatusMap());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testReservationStatusE() {
		Assert.notNull(ReservationStatus.findByName("junit"));
	}

	@Test
	public void testSearchOption() {
		Assert.notNull(SearchOption.BEGINS_WITH.toString());
		Assert.notNull(SearchOption.BETWEEN.toString());
		Assert.notNull(SearchOption.CONTAIN.toString());
		Assert.notNull(SearchOption.EQUAL.toString());
		Assert.notNull(SearchOption.GREATER.toString());
		Assert.notNull(SearchOption.GREATER_EQUAL.toString());
		Assert.notNull(SearchOption.LESS.toString());
		Assert.notNull(SearchOption.LESS_EQUAL.toString());
		Assert.notNull(SearchOption.NOTEQUAL.toString());
	}

	@Test
	public void testWeekdays() {
		Assert.notNull(Weekdays.FRIDAY.getDisplayName());
		Assert.notNull(Weekdays.MONDAY.getDisplayName());
		Assert.notNull(Weekdays.SATURDAY.getDisplayName());
		Assert.notNull(Weekdays.SUNDAY.getDisplayName());
		Assert.notNull(Weekdays.THURSDAY.getDisplayName());
		Assert.notNull(Weekdays.TUESDAY.getDisplayName());
		Assert.notNull(Weekdays.WEDNESDAY.getDisplayName());

		Assert.notNull(Weekdays.FRIDAY.getShortCode());
		Assert.notNull(Weekdays.MONDAY.getShortCode());
		Assert.notNull(Weekdays.SATURDAY.getShortCode());
		Assert.notNull(Weekdays.SUNDAY.getShortCode());
		Assert.notNull(Weekdays.THURSDAY.getShortCode());
		Assert.notNull(Weekdays.TUESDAY.getShortCode());
		Assert.notNull(Weekdays.WEDNESDAY.getShortCode());

		Assert.notNull(Weekdays.findByCode(Weekdays.WEDNESDAY.getShortCode()));
		Assert.notNull(Weekdays.findByName(Weekdays.WEDNESDAY.toString()));
		Assert.notNull(Weekdays.getWeekDayMap());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWeekdaysE() {
		Assert.notNull(Weekdays.findByCode(9));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWeekdaysE1() {
		Assert.notNull(Weekdays.findByName("junit"));
	}

}