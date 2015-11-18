package com.nyt.mpt.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import com.nyt.mpt.util.ChangeTrackedDomain;

/**
 * This <code>TemplateMetaData</code> class includes all the attributes related
 * to Template Meta Data and their getter and setter. The attributes have
 * mapping with <code>MP_MEDIA_TEMPLATES</code> table in the AMPT database
 * 
 * @author manish.kesarwani
 */
@Entity
@Table(name = "MP_MEDIA_TEMPLATES")
public class TemplateMetaData extends ChangeTrackedDomain {

	private static final long serialVersionUID = 1L;

	private Long templateId;

	private String templateName;

	private boolean active = true;

	private String templateFileName;

	private boolean useExistingRow = false;

	private List<TemplateSheetMetaData> templateSheetList = new ArrayList<TemplateSheetMetaData>();

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TEMPLATE_SEQUENCE")
	@SequenceGenerator(name = "TEMPLATE_SEQUENCE", sequenceName = "MP_MEDIA_TEMPLATE_SEQUENCE", allocationSize = 1)
	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(final Long templateId) {
		this.templateId = templateId;
	}

	@Column(name = "TEMPLATE_NAME")
	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(final String templateName) {
		this.templateName = templateName;
	}

	@Column(name = "IS_ACTIVE")
	public boolean isActive() {
		return active;
	}

	public void setActive(final boolean active) {
		this.active = active;
	}

	@OneToMany(mappedBy = "mediaTemplate", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	public List<TemplateSheetMetaData> getTemplateSheetList() {
		return templateSheetList;
	}

	public void setTemplateSheetList(final List<TemplateSheetMetaData> templateSheetList) {
		this.templateSheetList = templateSheetList;
	}

	@Column(name = "TEMPLATE_FILE_NAME")
	public String getTemplateFileName() {
		return templateFileName;
	}

	public void setTemplateFileName(final String templateFileName) {
		this.templateFileName = templateFileName;
	}

	@Column(name = "IS_USEEXISTINGROW")
	public boolean isUseExistingRow() {
		return useExistingRow;
	}

	public void setUseExistingRow(boolean useExistingRow) {
		this.useExistingRow = useExistingRow;
	}
}
