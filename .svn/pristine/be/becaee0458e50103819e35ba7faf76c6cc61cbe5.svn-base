/**
 * 
 */
package com.nyt.mpt.form;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import com.nyt.mpt.domain.Creative;
import com.nyt.mpt.util.NumberUtil;
import com.nyt.mpt.util.enums.CreativeType;

/**
 * This creative form bean is used for Creative info
 * 
 * @author amandeep.singh
 * 
 */
@SuppressWarnings("serial")
public class CreativeForm extends BaseForm<Creative> implements Serializable {

	private long creativeId;

	private String name;

	private String description;

	private String width;

	private String height;

	private String type;

	private String typeStr;

	private boolean hasDocument;

	private Map<String, String> creativeTypesMap;

	private String width2;

	private String height2;

	public CreativeForm() {
		this.creativeTypesMap = new TreeMap<String, String>();
		for (CreativeType creativeType : CreativeType.values()) {
			creativeTypesMap.put(creativeType.name(), creativeType.getDisplayValue());
		}
	}

	public long getCreativeId() {
		return creativeId;
	}

	public void setCreativeId(final long creativeId) {
		this.creativeId = creativeId;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name != null ? name.trim() : name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description != null ? description.trim() : description;
	}

	public String getType() {
		return type;
	}

	public void setType(final String type) {
		this.type = type != null ? type.trim() : type;
	}

	@Override
	public Creative populate(final Creative bean) {
		bean.setActive(isActive());
		bean.setVersion(getVersion());

		bean.setCreativeId(this.creativeId);
		bean.setType(this.type);
		if (StringUtils.isNotBlank((this.name))) {
			bean.setName(this.name.trim());
		}
		bean.setWidth(NumberUtil.intValue(this.width.trim()));
		bean.setHeight(NumberUtil.intValue(this.height.trim()));
		if (StringUtils.isNotBlank(this.width2)) {
			bean.setWidth2(NumberUtil.intValue(this.width2.trim()));
		}
		if (StringUtils.isNotBlank(this.height2)) {
			bean.setHeight2(NumberUtil.intValue(this.height2.trim()));
		}
		if (StringUtils.isNotBlank(this.description)) {
			bean.setDescription(this.description.trim());
		}
		return bean;
	}

	@Override
	public void populateForm(final Creative bean) {
		this.setActive(bean.isActive());
		this.setVersion(bean.getVersion());

		this.setCreativeId(bean.getCreativeId());
		this.setDescription(bean.getDescription());
		this.setName(bean.getName());
		this.setWidth(NumberUtil.formatLong(bean.getWidth(), true));
		this.setHeight(NumberUtil.formatLong(bean.getHeight(), true));
		if (bean.getWidth2() != null) {
			this.setWidth2(NumberUtil.formatLong(bean.getWidth2(), true));
		}
		if (bean.getHeight2() != null) {
			this.setHeight2(NumberUtil.formatLong(bean.getHeight2(), true));
		}
		this.setType(bean.getType());
		this.setTypeStr(CreativeType.findByName(bean.getType()).getDisplayValue());
	}

	public boolean getHasDocument() {
		return hasDocument;
	}

	public void setHasDocument(final boolean hasDocument) {
		this.hasDocument = hasDocument;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(final String width) {
		this.width = width != null ? width.trim() : width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(final String height) {
		this.height = height != null ? height.trim() : height;
	}

	public String getWidth2() {
		return width2;
	}

	public void setWidth2(final String width2) {
		this.width2 = width2 != null ? width2.trim() : width2;
	}

	public String getHeight2() {
		return height2;
	}

	public void setHeight2(final String height2) {
		this.height2 = height2 != null ? height2.trim() : height2;
	}

	public Map<String, String> getCreativeTypesMap() {
		return creativeTypesMap;
	}

	public boolean isActive() {
		return super.isActive();
	}

	public void setActive(final boolean active) {
		super.setActive(active);
	}

	public int getVersion() {
		return super.getVersion();
	}

	public void setVersion(final int version) {
		super.setVersion(version);
	}

	public String getTypeStr() {
		return typeStr;
	}

	public void setTypeStr(final String typeStr) {
		this.typeStr = typeStr != null ? typeStr.trim() : typeStr;
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
