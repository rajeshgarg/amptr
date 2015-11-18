/**
 * 
 */
package com.nyt.mpt.form;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.nyt.mpt.domain.Attribute;
import com.nyt.mpt.domain.AttributeType;
import com.nyt.mpt.util.NumberUtil;
import com.nyt.mpt.util.StringUtil;

/**
 * This class is used for attribute info
 * 
 * @author amandeep.singh
 * 
 */
@SuppressWarnings("serial")
public class AttributeForm extends BaseForm<Attribute> implements Serializable {

	private String attributeId;

	private long creativeId;

	private String attributeType;

	private String attributeTypeStr;

	private String attributeKey;

	private String attributeName;

	private String attributeDescription;

	private String attributeOptionalValue;

	private String attributeValue;

	private Map<String, String> attributTypes;

	public AttributeForm() {
		this.attributTypes = new HashMap<String, String>();
		for (AttributeType attributeType : AttributeType.values()) {
			attributTypes.put(attributeType.name(), attributeType.getDisplayName());
		}
	}

	public String getAttributeId() {
		if (StringUtils.isEmpty(attributeId)) {
			attributeId = "0";
		}
		return attributeId;
	}

	public void setAttributeId(final String attributeId) {
		this.attributeId = attributeId != null ? attributeId.trim() : attributeId;
	}

	public long getCreativeId() {
		return creativeId;
	}

	public void setCreativeId(final long creativeId) {
		this.creativeId = creativeId;
	}

	public String getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(final String attributeType) {
		this.attributeType = attributeType != null ? attributeType.trim() : attributeType;
	}

	public String getAttributeKey() {
		return attributeKey;
	}

	public void setAttributeKey(final String attributeKey) {
		this.attributeKey = attributeKey != null ? attributeKey.trim() : attributeKey;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(final String attributeName) {
		this.attributeName = attributeName != null ? attributeName.trim() : attributeName;
	}

	public String getAttributeDescription() {
		return attributeDescription;
	}

	public void setAttributeDescription(final String attributeDescription) {
		this.attributeDescription = attributeDescription != null ? attributeDescription.trim() : attributeDescription;
	}

	public String getAttributeOptionalValue() {
		return attributeOptionalValue;
	}

	public void setAttributeOptionalValue(final String attributeOptionalValue) {
		this.attributeOptionalValue = attributeOptionalValue != null ? attributeOptionalValue.trim() : attributeOptionalValue;
	}

	public boolean isActive() {
		return super.isActive();
	}

	public void setActive(final boolean active) {
		super.setActive(active);
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(final String attributeValue) {
		this.attributeValue = attributeValue != null ? attributeValue.trim() : attributeValue;
	}

	@Override
	public Attribute populate(final Attribute bean) {
		bean.setActive(isActive());
		bean.setVersion(getVersion());
		if (!StringUtils.isBlank(this.getAttributeDescription())) {
			bean.setAttributeDescription(this.getAttributeDescription());
		}
		bean.setAttributeId(NumberUtil.longValue(this.getAttributeId()));
		bean.setAttributeKey(StringUtils.replaceChars(this.getAttributeName(), ' ', '_'));
		if (!StringUtils.isBlank(this.getAttributeName())) {
			bean.setAttributeName(this.getAttributeName().trim());
		}
		bean.setAttributeOptionalValue(StringUtil.trimSpacesForCommaSeperatedValues(this.getAttributeOptionalValue()));
		bean.setAttributeType(attributeType);
		return bean;
	}

	@Override
	public void populateForm(final Attribute bean) {
		this.setActive(bean.isActive());
		this.setVersion(bean.getVersion());

		this.setAttributeDescription(bean.getAttributeDescription());
		this.setAttributeId(String.valueOf(bean.getAttributeId()));
		this.setAttributeKey(bean.getAttributeKey());
		this.setAttributeName(bean.getAttributeName());
		this.setAttributeType(bean.getAttributeType());
		this.setAttributeTypeStr(AttributeType.findByName(bean.getAttributeType()).getDisplayName());
		this.setAttributeOptionalValue(bean.getAttributeOptionalValue());
	}

	public int getVersion() {
		return super.getVersion();
	}

	public void setVersion(final int version) {
		super.setVersion(version);
	}

	public Map<String, String> getAttributTypes() {
		return attributTypes;
	}

	public String getAttributeTypeStr() {
		return attributeTypeStr;
	}

	public void setAttributeTypeStr(final String attributeTypeStr) {
		this.attributeTypeStr = attributeTypeStr != null ? attributeTypeStr.trim() : attributeTypeStr;
	}

	@Override
	public boolean isForceUpdate() {
		return super.isForceUpdate();
	}

	@Override
	public void setForceUpdate(final boolean forceUpdate) {
		super.setForceUpdate(forceUpdate);
	}
}
