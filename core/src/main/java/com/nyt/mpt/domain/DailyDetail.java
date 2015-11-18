package com.nyt.mpt.domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Used to capture avails from yield-ex on daily basis 
 * @author manish.kesarwani
 *
 */
@XStreamAlias("dailyDetail")
public class DailyDetail {
	
	private String available;
	private String capacity;
	private String endDate;
	private String exceededForecastWindow;
	private String startDate;
	private String unmetDemand;
	private String daysInForecast;
	private String daysOutsideAvailabilityThreshold;
	private String evenAvailability;
	
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
	public String getAvailable() {
		return available;
	}
	public void setAvailable(String available) {
		this.available = available;
	}
	public String getExceededForecastWindow() {
		return exceededForecastWindow;
	}
	public void setExceededForecastWindow(String exceededForecastWindow) {
		this.exceededForecastWindow = exceededForecastWindow;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getUnmetDemand() {
		return unmetDemand;
	}
	public void setUnmetDemand(String unmetDemand) {
		this.unmetDemand = unmetDemand;
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
}
