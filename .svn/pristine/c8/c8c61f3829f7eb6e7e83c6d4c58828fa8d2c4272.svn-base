/**
 * 
 */
package com.nyt.mpt.util.reservation;

import java.util.ArrayList;
import java.util.List;

/**
 * This <code>ReservationListViewVO</code> class represent detail of a
 * particular day in calendar in a list view
 * 
 * @author manish.kesarwani
 */
public class ReservationListViewVO {

	private String viewDate;
	private Double totalSOR;
	private List<ReservationListViewDetailVO> salesOrderList = new ArrayList<ReservationListViewDetailVO>();
	private List<ReservationListViewDetailVO> proposalList = new ArrayList<ReservationListViewDetailVO>();

	public String getViewDate() {
		return viewDate;
	}

	public void setViewDate(final String viewDate) {
		this.viewDate = viewDate;
	}

	public Double getTotalSOR() {
		return totalSOR;
	}

	public void setTotalSOR(final Double totalSOR) {
		this.totalSOR = totalSOR;
	}

	public List<ReservationListViewDetailVO> getSalesOrderList() {
		return salesOrderList;
	}

	public void setSalesOrderList(final List<ReservationListViewDetailVO> salesOrderList) {
		this.salesOrderList = salesOrderList;
	}

	public List<ReservationListViewDetailVO> getProposalList() {
		return proposalList;
	}

	public void setProposalList(final List<ReservationListViewDetailVO> proposalList) {
		this.proposalList = proposalList;
	}

	public void addReservationProposal(final ReservationListViewDetailVO proposal) {
		this.proposalList.add(proposal);
	}

	public void addReservationSalesOrder(final ReservationListViewDetailVO salesOrder) {
		this.salesOrderList.add(salesOrder);
	}
}