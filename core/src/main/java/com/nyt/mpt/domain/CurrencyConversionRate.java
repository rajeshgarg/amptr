/**
 * 
 */
package com.nyt.mpt.domain;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Where;

/**
 * This class is used for providing Currency conversion rate details
 * @author Garima.garg
 *
 */
@Entity
@Immutable
@Table(name = "CURRENCY_CONVERSION_RATE")
@Where(clause = "DELETE_DATE is null")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class CurrencyConversionRate implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private long id;
	
	private CurrencyConversion currencyConversion;

	private lineItemTransPeriod transPeriod;
	
	private double exchangeRate;
	
	private Date deleteDate;

	@Id
	@Column(name = "SOS_CURRENCY_CONV_RATE_ID")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name="SOS_CURRENCY_CONVERSION_ID", nullable = false, insertable = true, updatable = false)
	public CurrencyConversion getCurrencyConversion() {
		return currencyConversion;
	}

	public void setCurrencyConversion(CurrencyConversion currencyConversion) {
		this.currencyConversion = currencyConversion;
	}

	@ManyToOne
	@JoinColumn(name="SOS_PERIOD_ID", nullable = false, insertable = true, updatable = false)
	public lineItemTransPeriod getTransPeriod() {
		return transPeriod;
	}

	public void setTransPeriod(lineItemTransPeriod transPeriod) {
		this.transPeriod = transPeriod;
	}

	@Column(name = "EXCHANGE_RATE")
	public double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	@Column(name = "DELETE_DATE")
	public Date getDeleteDate() {
		return deleteDate;
	}

	public void setDeleteDate(Date deleteDate) {
		this.deleteDate = deleteDate;
	}

}
