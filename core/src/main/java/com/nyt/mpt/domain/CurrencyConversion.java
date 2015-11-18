/**
 * 
 */
package com.nyt.mpt.domain;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Where;

/**
 * @author Garima.garg
 *
 */
@Entity
@Immutable
@Table(name = "CURRENCY_CONVERSION")
@Where(clause = "DELETE_DATE is null")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class CurrencyConversion implements Serializable{

	private static final long serialVersionUID = 1L;
	
    private long currencyConversionId;
	
	private Date createDate;
	
	private Date updateDate;
	
	private Date deleteDate; 
	
	private long fromCurrencyId;
	
	private long toCurrencyId;

	@Id
	@Column(name = "SOS_CURRENCY_CONVERSION_ID")
	public long getCurrencyConversionId() {
		return currencyConversionId;
	}

	public void setCurrencyConversionId(long currencyConversionId) {
		this.currencyConversionId = currencyConversionId;
	}

	@Column(name = "CREATE_DATE")
	public Date getCreateDate() {
		return createDate;
	}
	
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "UPDATE_DATE")
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Column(name = "DELETE_DATE")
	public Date getDeleteDate() {
		return deleteDate;
	}

	public void setDeleteDate(Date deleteDate) {
		this.deleteDate = deleteDate;
	}

	@Column(name = "SOS_FROM_CURRENCY_ID")
	public long getFromCurrencyId() {
		return fromCurrencyId;
	}

	public void setFromCurrencyId(long fromCurrencyId) {
		this.fromCurrencyId = fromCurrencyId;
	}

	@Column(name = "SOS_TO_CURRENCY_ID")
	public long getToCurrencyId() {
		return toCurrencyId;
	}

	public void setToCurrencyId(long toCurrencyId) {
		this.toCurrencyId = toCurrencyId;
	}
}
