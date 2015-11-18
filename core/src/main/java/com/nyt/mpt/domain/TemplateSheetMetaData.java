package com.nyt.mpt.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * This <code>TemplateSheetMetaData</code> class includes all the attributes
 * related to Template Sheet and their getter and setter. The attributes have
 * mapping with <code>MP_MEDIA_TEMPLATES_SHEET</code> table in the AMPT database
 * 
 * @author manish.kesarwani
 * 
 */
@Entity
@Table(name = "MP_MEDIA_TEMPLATES_SHEET")
public class TemplateSheetMetaData implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long sheetId;
	private String sheetName;
	private TemplateMetaData mediaTemplate;

	private List<TemplateMetaDataAttributes> mediaPlanAttributes = new ArrayList<TemplateMetaDataAttributes>();

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TEMPLATE_SHEET_SEQUENCE")
	@SequenceGenerator(name = "TEMPLATE_SHEET_SEQUENCE", sequenceName = "MP_MEDIA_PLAN_SHEET_SEQ", allocationSize = 1)
	public Long getSheetId() {
		return sheetId;
	}

	public void setSheetId(Long sheetId) {
		this.sheetId = sheetId;
	}

	@Column(name = "SHEET_NAME")
	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	@OneToMany(mappedBy = "mediaTemplateSheet", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	public List<TemplateMetaDataAttributes> getMediaPlanAttributes() {
		return mediaPlanAttributes;
	}

	public void setMediaPlanAttributes(List<TemplateMetaDataAttributes> mediaPlanAttributes) {
		this.mediaPlanAttributes = mediaPlanAttributes;
	}

	@ManyToOne
	@JoinColumn(name = "MP_MEDIA_TEMPLATES_ID", nullable = true, insertable = true, updatable = true)
	public TemplateMetaData getMediaTemplate() {
		return mediaTemplate;
	}

	public void setMediaTemplate(TemplateMetaData mediaTemplate) {
		this.mediaTemplate = mediaTemplate;
	}
}
