/**
 * 
 */
package com.nyt.mpt.form;

/**
 * 
 * This InfoTip form bean is used for InfoTip
 * 
 * @author amandeep.singh
 * 
 */
public class InfoTipForm {

	private long id;

	private String type;

	private String name;

	private String value;

	private String salesTargetName;

	public long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(final String value) {
		this.value = value;
	}

	public String getSalesTargetName() {
		return salesTargetName;
	}

	public void setSalesTargetName(final String salesTargetName) {
		this.salesTargetName = salesTargetName;
	}
}
