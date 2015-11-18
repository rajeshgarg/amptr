/**
 * 
 */
package com.nyt.mpt.util.dto;

import java.util.LinkedHashSet;
import java.util.Set;

import com.nyt.mpt.domain.LineItemTarget;

/**
 * @author surendra.singh
 *
 */
public class LineItemDTO {

	private long lineItemID;
	
	private String[] sosSalesTargetId;
	
	private String sosProductId;
	
	private String startDate;

	private String endDate;
	
	private Set<LineItemTarget> geoTargetSet = new LinkedHashSet<LineItemTarget>();

	public long getLineItemID() {
		return lineItemID;
	}

	public void setLineItemID(long lineItemID) {
		this.lineItemID = lineItemID;
	}

	public String[] getSosSalesTargetId() {
		return sosSalesTargetId;
	}

	public void setSosSalesTargetId(String[] sosSalesTargetId) {
		this.sosSalesTargetId = sosSalesTargetId;
	}

	public String getSosProductId() {
		return sosProductId;
	}

	public void setSosProductId(String sosProductId) {
		this.sosProductId = sosProductId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public void setGeoTargetSet(Set<LineItemTarget> geoTargetSet) {
		this.geoTargetSet = geoTargetSet;
	}

	public Set<LineItemTarget> getGeoTargetSet() {
		return geoTargetSet;
	}
}
