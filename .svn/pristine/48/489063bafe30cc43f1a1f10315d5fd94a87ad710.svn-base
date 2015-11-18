/**
 * 
 */
package com.nyt.mpt.util.reservation;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.sos.OrderLineItem;
import com.nyt.mpt.util.ConstantStrings;

/**
 * This <code>ReservationInfo</code> class represent information of a particular
 * day in calendar
 * 
 * @author surendra.singh
 */
public class ReservationInfo {
	private String start;
	private int proposals;
	private int orders;
	private double sor;
	private boolean bookedForCurrenProposal;
	private boolean bookingAllowed;
	private List<OrderLineItem> orderLineItems;
	private List<LineItem> proposalLineItems;

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public int getProposals() {
		return proposals;
	}

	public void setProposals(int proposals) {
		this.proposals = proposals;
	}

	public int getOrders() {
		return orders;
	}

	public void setOrders(int orders) {
		this.orders = orders;
	}

	public double getSor() {
		return sor;
	}

	public void setSor(double sor) {
		this.sor = sor;
	}

	public String getTitle() {
		return ConstantStrings.EMPTY_STRING;
	}

	public boolean isBookedForCurrenProposal() {
		return bookedForCurrenProposal;
	}

	public void setBookedForCurrenProposal(boolean bookedForCurrenProposal) {
		this.bookedForCurrenProposal = bookedForCurrenProposal;
	}

	public boolean isBookingAllowed() {
		return bookingAllowed;
	}

	public void setBookingAllowed(boolean bookingAllowed) {
		this.bookingAllowed = bookingAllowed;
	}

	@JsonIgnore
	public List<OrderLineItem> getOrderLineItems() {
		return orderLineItems;
	}

	@JsonIgnore
	public void setOrderLineItems(List<OrderLineItem> orderLineItems) {
		this.orderLineItems = orderLineItems;
	}

	@JsonIgnore
	public List<LineItem> getProposalLineItems() {
		return proposalLineItems;
	}

	@JsonIgnore
	public void setProposalLineItems(List<LineItem> proposalLineItems) {
		this.proposalLineItems = proposalLineItems;
	}
}
