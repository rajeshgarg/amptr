package com.nyt.mpt.form;

import com.nyt.mpt.domain.Audit;
import com.nyt.mpt.util.DateUtil;

/**
 * @author rakesh.tewari
 * 
 */
public class AuditForm extends BaseForm<Audit> {

	private long id;

	private String auditDate;

	private String who;

	private String message;

	/*
	 * (non-Javadoc)
	 * @see com.nyt.mpt.form.BaseForm#populate(java.lang.Object)
	 */
	@Override
	public Audit populate(final Audit bean) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.nyt.mpt.form.BaseForm#populateForm(java.lang.Object)
	 */
	@Override
	public void populateForm(final Audit bean) {
		this.setId(bean.getId());
		this.setAuditDate(DateUtil.getGuiDateTimeString(bean.getAuditDate().getTime()));
		this.setWho(bean.getWho());
		this.setMessage(bean.getMessage());
	}

	public long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public String getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(final String auditDate) {
		this.auditDate = auditDate;
	}

	public String getWho() {
		return who;
	}

	public void setWho(final String who) {
		this.who = who;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}
}
