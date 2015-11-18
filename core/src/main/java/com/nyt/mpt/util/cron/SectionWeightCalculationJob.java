/**
 * 
 */
package com.nyt.mpt.util.cron;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.nyt.mpt.domain.CronJobSchedule;
import com.nyt.mpt.domain.InventoryDetail;
import com.nyt.mpt.domain.SalesTarget;
import com.nyt.mpt.domain.SalesTargetAmpt;
import com.nyt.mpt.service.ISalesTargetService;
import com.nyt.mpt.service.IYieldexService;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.DateUtil;
import com.nyt.mpt.util.MailUtil;
import com.nyt.mpt.util.YieldexHelper;
import com.nyt.mpt.util.dto.LineItemDTO;
import com.nyt.mpt.util.enums.CronJobNameEnum;
import com.nyt.mpt.util.exception.YieldexAvailsException;

/**
 * @author surendra.singh
 *
 */
public class SectionWeightCalculationJob extends AbstractCronJob {

	private static final String SECTION_ROS = "nytimes.com";

	private MailUtil mailUtil;

	private Map<String, String> mailProps;

	private YieldexHelper helper;

	private IYieldexService yieldexService;

	private ISalesTargetService salesTargetService;

	private static final Logger LOGGER = Logger.getLogger(SectionWeightCalculationJob.class);

	/* (non-Javadoc)
	 * @see org.springframework.scheduling.quartz.QuartzJobBean#executeInternal(org.quartz.JobExecutionContext)
	 */
	@Override
	protected void executeInternal(final JobExecutionContext context) throws JobExecutionException {
		if (!isJobActiveOnServer(CronJobNameEnum.SECTION_WEIGHT_CALCULATION)) {
			return;
		}

		final Date date = DateUtil.getCurrentMidnightDate();
		final CronJobSchedule jobSchedule = new CronJobSchedule(CronJobNameEnum.SECTION_WEIGHT_CALCULATION.name(), date);
		if (!DateUtil.isFirstDayOfQuarter(date) && getCronService().getScheduledJob(jobSchedule) == null) {
			return;
		}

		LOGGER.info("Section weight calculation job started - " + DateUtil.getGuiDateTimeString(DateUtil.getCurrentDate().getTime()));
		boolean success = true;
		try {
			updateSalesTargetWeight(getSalesTargetList(salesTargetService.getSalesTarget()));
		} catch (Exception e) {
			success = false;
			LOGGER.error("Exception occurred while calculating section weight - " + e);
		} finally {
			LOGGER.info("Sending email for weight target calculation job ...");
			mailUtil.sendMail(mailUtil.createMessageForSectionWeightJob(this.mailProps, success));
			LOGGER.info("Section weight calculation job finished - " + DateUtil.getGuiDateTimeString(DateUtil.getCurrentDate().getTime()));
		}
	}

	/**
	 * @param salesTargetList
	 * @return
	 */
	private List<SalesTargetAmpt> getSalesTargetList(final List<SalesTarget> salesTargetList) {
		long capicityROS = 0;
		final List<SalesTargetAmpt> amptSalesTargets = new ArrayList<SalesTargetAmpt>(salesTargetList.size());
		final LineItemDTO form = getLineItemDTOForCurrentQurter();
		String yieldexURL;
		for (SalesTarget salesTarget : salesTargetList) {
			yieldexURL = ConstantStrings.EMPTY_STRING;
			form.setSosSalesTargetId(new String[] {String.valueOf(salesTarget.getSalesTargetId())});
			try {
				yieldexURL = helper.getYieldexURLForAvails(form);
			} catch (YieldexAvailsException e) {
				LOGGER.info("ADX_PPG_ID not found for the sales target - " + salesTarget.getSalesTargetName() + "( " + salesTarget.getSalesTargetId() + " )");
				continue;
			}
			final InventoryDetail inventoryDetail = yieldexService.getInventoryDetail(yieldexURL);
			final SalesTargetAmpt amptSalesTarget = getAmptSaleTargetObject(salesTarget, inventoryDetail);
			if (SECTION_ROS.equals(salesTarget.getSalesTargetName())) {
				capicityROS = amptSalesTarget.getCapacity().longValue();
			}
			amptSalesTargets.add(amptSalesTarget);
		}
		calculateWeight(amptSalesTargets, capicityROS);
		return amptSalesTargets;
	}

	/**
	 * @return
	 */
	private LineItemDTO getLineItemDTOForCurrentQurter() {
		final LineItemDTO form = new LineItemDTO();
		Calendar calendar = Calendar.getInstance();
		switch (calendar.get(Calendar.MONTH)) {
			case Calendar.JANUARY: case Calendar.FEBRUARY: case Calendar.MARCH:
				calendar.set(Calendar.MONTH, Calendar.JANUARY);
				break;
			case Calendar.APRIL: case Calendar.MAY:	case Calendar.JUNE:
				calendar.set(Calendar.MONTH, Calendar.APRIL);
				break;
			case Calendar.JULY: case Calendar.AUGUST: case Calendar.SEPTEMBER:
				calendar.set(Calendar.MONTH, Calendar.JULY);
				break;
			case Calendar.OCTOBER: case Calendar.NOVEMBER: case Calendar.DECEMBER:
				calendar.set(Calendar.MONTH, Calendar.OCTOBER);
				break;
			default:
				break;
		}
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		form.setStartDate(DateUtil.getGuiDateString(calendar.getTime()));
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 3);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 1);
		form.setEndDate(DateUtil.getGuiDateString(calendar.getTime()));
		return form;
	}

	/**
	 * @param salesTarget
	 * @param inventoryDetail
	 * @return
	 */
	private SalesTargetAmpt getAmptSaleTargetObject(final SalesTarget salesTarget, final InventoryDetail inventoryDetail) {
		final SalesTargetAmpt amptSalesTarget = new SalesTargetAmpt();
		amptSalesTarget.setSalesTargetId(salesTarget.getSalesTargetId());
		amptSalesTarget.setSalesTargetName(salesTarget.getSalesTargetName());
		amptSalesTarget.setSalesTargeDisplayName(salesTarget.getSalesTargeDisplayName());
		amptSalesTarget.setCapacity(Long.valueOf(inventoryDetail.getSummary().getCapacity()));
		return amptSalesTarget;
	}

	/**
	 * @param amptSalesTargets
	 * @param capicityROS
	 */
	private void calculateWeight(final List<SalesTargetAmpt> amptSalesTargets, final long capicityROS) {
		for (SalesTargetAmpt amptSalesTarget : amptSalesTargets) {
			amptSalesTarget.setWeight((amptSalesTarget.getCapacity() * 100.0) / capicityROS);
			LOGGER.info("Capacity & weight of sales target - " + amptSalesTarget.getSalesTargetName() + " is - " + amptSalesTarget.getCapacity() + ", " + amptSalesTarget.getWeight());
		}
	}

	private void updateSalesTargetWeight(final List<SalesTargetAmpt> amptSalesTargets) {
		super.setUserForCronJobs();
		salesTargetService.saveSalesTargets(amptSalesTargets);
	}

	public void setMailUtil(final MailUtil mailUtil) {
		this.mailUtil = mailUtil;
	}

	public void setMailProps(final Map<String, String> mailProps) {
		this.mailProps = mailProps;
	}

	public void setHelper(final YieldexHelper helper) {
		this.helper = helper;
	}

	public void setYieldexService(final IYieldexService yieldexService) {
		this.yieldexService = yieldexService;
	}

	public void setSalesTargetService(final ISalesTargetService salesTargetService) {
		this.salesTargetService = salesTargetService;
	}
}
