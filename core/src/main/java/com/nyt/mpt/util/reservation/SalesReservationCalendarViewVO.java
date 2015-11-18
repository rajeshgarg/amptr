/**
 * 
 */
package com.nyt.mpt.util.reservation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author amandeep.singh
 *
 */
public class SalesReservationCalendarViewVO {
	private String viewDate;
	private Double totalSOR;
	private String availableSOR;
	private int proposedLineItemsCount;
	private List<SalesReservationCalendarDetailVO> salesOrderList = new ArrayList<SalesReservationCalendarDetailVO>();
	private List<SalesReservationCalendarDetailVO> proposalList = new ArrayList<SalesReservationCalendarDetailVO>();
	
	public String getViewDate() {
		return viewDate;
	}
	public void setViewDate(String viewDate) {
		this.viewDate = viewDate;
	}
	public Double getTotalSOR() {
		return totalSOR;
	}
	public void setTotalSOR(Double totalSOR) {
		this.totalSOR = totalSOR;
	}
	public String getAvailableSOR() {
		return availableSOR;
	}
	public void setAvailableSOR(String availableSOR) {
		this.availableSOR = availableSOR;
	}
	public List<SalesReservationCalendarDetailVO> getSalesOrderList() {
		return salesOrderList;
	}
	public void setSalesOrderList(
			List<SalesReservationCalendarDetailVO> salesOrderList) {
		this.salesOrderList = salesOrderList;
	}
	public List<SalesReservationCalendarDetailVO> getProposalList() {
		return proposalList;
	}
	public void setProposalList(List<SalesReservationCalendarDetailVO> proposalList) {
		this.proposalList = proposalList;
	}
	public void addReservationProposal(final SalesReservationCalendarDetailVO proposal) {
		this.proposalList.add(proposal);
		Collections.sort(this.proposalList);
	}

	public void addReservationSalesOrder(final SalesReservationCalendarDetailVO salesOrder) {
		this.salesOrderList.add(salesOrder);
	}
	public int getProposedLineItemsCount() {
		return proposedLineItemsCount;
	}
	public void setProposedLineItemsCount(int proposedLineItemsCount) {
		this.proposedLineItemsCount = proposedLineItemsCount;
	}
}
