/**
 * 
 */
package com.nyt.mpt.form;

/**
 * This is base form bean for all form bean
 * 
 * @author amandeep.singh
 * 
 */
public abstract class BaseForm<T> {

	public abstract void populateForm(T bean);

	public abstract T populate(T bean);

	private boolean active = true;

	private int version;

	private boolean forceUpdate;

	public boolean isActive() {
		return active;
	}

	public void setActive(final boolean active) {
		this.active = active;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(final int version) {
		this.version = version;
	}

	public boolean isForceUpdate() {
		return forceUpdate;
	}

	public void setForceUpdate(final boolean forceUpdate) {
		this.forceUpdate = forceUpdate;
	}
}
