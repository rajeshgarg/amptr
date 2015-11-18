package com.nyt.mpt.domain;

import org.apache.commons.lang.StringUtils;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Use to capture summary information for avails from yieldex
 * 
 * @author manish.kesarwani
 * 
 */
@XStreamAlias("summary")
public class Summary {

	private String available;
	private String capacity;
	private String startDate;
	private String endDate;
	private String exceededForecastWindow;
	private String unmetDemand;
	private String daysInForecast;
	private String daysOutsideAvailabilityThreshold;
	private String evenAvailability;
	private String inputJson;

	public String getAvailable() {
		return available;
	}

	public void setAvailable(String available) {
		this.available = available;
	}

	public String getCapacity() {
		return capacity;
	}

	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getExceededForecastWindow() {
		this.exceededForecastWindow = this.exceededForecastWindow.equals("true") ? "True" : "False";
		return exceededForecastWindow;
	}

	public void setExceededForecastWindow(String exceededForecastWindow) {
		this.exceededForecastWindow = exceededForecastWindow;
	}

	public String getDaysInForecast() {
		return daysInForecast;
	}

	public void setDaysInForecast(String daysInForecast) {
		this.daysInForecast = daysInForecast;
	}

	public String getDaysOutsideAvailabilityThreshold() {
		return daysOutsideAvailabilityThreshold;
	}

	public void setDaysOutsideAvailabilityThreshold(String daysOutsideAvailabilityThreshold) {
		this.daysOutsideAvailabilityThreshold = daysOutsideAvailabilityThreshold;
	}

	public String getEvenAvailability() {
		return evenAvailability;
	}

	public void setEvenAvailability(String evenAvailability) {
		this.evenAvailability = evenAvailability;
	}

	public String getUnmetDemand() {
		return unmetDemand;
	}

	public void setUnmetDemand(String unmetDemand) {
		this.unmetDemand = unmetDemand;
	}

	public String getInputJson() {
		return inputJson;
	}

	public void setInputJson(String inputJson) {
		if(StringUtils.isNotBlank(inputJson)){
			this.inputJson = inputJson;
		}
	}

	@Override
	public String toString() {
		return "Summary [available=" + this.available + ", capacity=" + this.capacity + ", daysInForecast=" + this.daysInForecast
				+ ", daysOutsideAvailabilityThreshold=" + this.daysOutsideAvailabilityThreshold + ", endDate=" + this.endDate + ", evenAvailability="
				+ this.evenAvailability + ", exceededForecastWindow=" + this.exceededForecastWindow + ", startDate=" + this.startDate + ", unmetDemand="
				+ this.unmetDemand + ", inputJson=" + this.inputJson + "]";
	}
}
