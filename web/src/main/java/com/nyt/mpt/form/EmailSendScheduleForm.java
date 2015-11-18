/**
 * 
 */
package com.nyt.mpt.form;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.nyt.mpt.domain.EmailSchedule;
import com.nyt.mpt.domain.EmailScheduleDetails;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.DateUtil;
import com.nyt.mpt.util.enums.EmailScheduleFrequency;
import com.nyt.mpt.util.enums.Weekdays;

/**
 * This <code>EmailSendScheduleForm</code> has all the information related to Email Schedule
 * 
 * @author suvigya
 */

public class EmailSendScheduleForm extends BaseForm<EmailSchedule> {

	private String scheduleId;

	private String scheduleDetailId;

	private String salesTargetId;

	private String productId;

	private boolean recurrence;
	
	private String weekDays;

	private String frequency;
	
	private String emailStartDate;

	private String ends;
	
	private String emailEndDate;

	private String frequencyStr;
	
	private String weekDaysStr;
	
	private String productName;
	
	private String salesTargetName;

	public String getSalesTargetId() {
		return salesTargetId;
	}

	public void setSalesTargetId(String salesTargetId) {
		this.salesTargetId = salesTargetId != null ? salesTargetId.trim() : salesTargetId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId != null ? productId.trim() : productId;
	}

	public String getWeekDays() {
		return weekDays;
	}

	public void setWeekDays(String weekDays) {
		this.weekDays = weekDays != null ? weekDays.trim() : weekDays;
	}

	public String getEmailStartDate() {
		return emailStartDate;
	}

	public void setEmailStartDate(String startDate) {
		this.emailStartDate = startDate != null ? startDate.trim() : startDate;
	}

	public String getEmailEndDate() {
		return emailEndDate;
	}

	public void setEmailEndDate(String endDate) {
		this.emailEndDate = endDate != null ? endDate.trim() :endDate;
	}

	public String getScheduleId() {
		if (StringUtils.isEmpty(scheduleId)) {
			scheduleId = "0";
		}
		return scheduleId;
	}

	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId != null ? scheduleId.trim() : scheduleId;
	}

	public String getScheduleDetailId() {
		return scheduleDetailId;
	}

	public void setScheduleDetailId(String scheduleDetailId) {
		this.scheduleDetailId = scheduleDetailId != null ? scheduleDetailId.trim() : scheduleDetailId;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency != null ? frequency.trim() : frequency;
	}

	public String getWeekDaysStr() {
		return weekDaysStr;
	}

	public void setWeekDaysStr(String weekDaysStr) {
		this.weekDaysStr = weekDaysStr != null ? weekDaysStr.trim() : weekDaysStr;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName != null ? productName.trim() : productName;
	}

	public String getSalesTargetName() {
		return salesTargetName;
	}

	public void setSalesTargetName(String salesTargetName) {
		this.salesTargetName = salesTargetName != null ? salesTargetName.trim() : salesTargetName;
	}

	public String getFrequencyStr() {
		return frequencyStr;
	}

	public void setFrequencyStr(String frequencyStr) {
		this.frequencyStr = frequencyStr;
	}

	public boolean isRecurrence() {
		return recurrence;
	}

	public void setRecurrence(boolean recurrence) {
		this.recurrence = recurrence;
	}

	public String getEnds() {
		return ends;
	}

	public void setEnds(String ends) {
		this.ends = ends;
	}
	
	/**
	 * @param bean
	 */
	public void populateScheduleDetails(final EmailScheduleDetails bean) {
		this.setScheduleDetailId(String.valueOf(bean.getEmailScheduleDetailsId()));
		this.setScheduleId(String.valueOf(bean.getEmailSchedule().getEmailScheduleId()));
		this.setProductId(String.valueOf(bean.getEmailSchedule().getProductId()));
		this.setSalesTargetId(String.valueOf(bean.getEmailSchedule().getSalesTargetId()));
		this.setFrequency(bean.getFrequency());
		this.setWeekDays(bean.getWeekdays());
		if (bean.getStartDate() != null) {
			this.setEmailStartDate(DateUtil.getGuiDateString(bean.getStartDate()));
		}
		
		if (bean.isForever()) {
			this.setEnds("forever");
			this.setEmailEndDate("Never");
		} else {
			this.setEnds("endOn");
			this.setEmailEndDate(DateUtil.getGuiDateString(bean.getEndDate()));
		}
		
		if (StringUtils.isNotBlank(bean.getWeekdays())) {
			final List<String> weekDayList = new ArrayList<String>();
			final Map<String, String> weekDayMap = Weekdays.getWeekDayMap();
			for (String weekDay : bean.getWeekdays().split(ConstantStrings.COMMA)) {
				weekDayList.add(weekDayMap.get(weekDay));
			}
			this.setWeekDaysStr(StringUtils.join(weekDayList, ConstantStrings.COMMA + ConstantStrings.SPACE));
		}
		
		if (StringUtils.isNotBlank(bean.getFrequency())) {
			this.setFrequencyStr(EmailScheduleFrequency.findByName(bean.getFrequency()).getDisplayName());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.form.BaseForm#populate(java.lang.Object)
	 */
	@Override
	public EmailSchedule populate(EmailSchedule bean) {
		final List<EmailScheduleDetails> scheduleDetailsList = new ArrayList<EmailScheduleDetails>();
		bean.setEmailScheduleId(Long.valueOf(this.getScheduleId()));
		bean.setProductId(Long.valueOf(this.getProductId()));
		bean.setSalesTargetId(Long.valueOf(this.getSalesTargetId()));
		bean.setProdcutName(this.getProductName());
		bean.setSalesTargetName(this.getSalesTargetName());
		bean.setEmailSchedules(scheduleDetailsList);
		
		final EmailScheduleDetails detail = new EmailScheduleDetails();
		if(this.recurrence) {
			detail.setWeekdays(this.getWeekDays());
			detail.setFrequency(this.getFrequency());
		}
		
		detail.setStartDate(DateUtil.parseToDate(this.getEmailStartDate()));
		if (StringUtils.isNotBlank(this.getEnds()) && "forever".equals(this.getEnds())) {
			detail.setEndDate(DateUtil.parseToDate("01/01/2050"));
			detail.setForever(true);
		} else {
			detail.setEndDate(DateUtil.parseToDate(this.getEmailEndDate()));
		}
		detail.setEmailScheduleDetailsId(StringUtils.isNotBlank(this.getScheduleDetailId()) ? Long.valueOf(this.getScheduleDetailId()) : 0L);
		detail.setEmailSchedule(bean);
		scheduleDetailsList.add(detail);
		return bean;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.form.BaseForm#populateForm(java.lang.Object)
	 */
	@Override
	public void populateForm(EmailSchedule bean) {
		
	}
}
