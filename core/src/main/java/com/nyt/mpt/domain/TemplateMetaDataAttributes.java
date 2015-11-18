package com.nyt.mpt.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * This <code>TemplateMetaDataAttributes</code> class includes all the
 * attributes related to Template and their getter and setter. The attributes
 * have mapping with <code>MP_MEDIA_PLAN_ATTRIBUTES</code> table in the AMPT
 * database
 * 
 * @author manish.kesarwani
 * 
 */
@Entity
@Table(name = "MP_MEDIA_PLAN_ATTRIBUTES")
public class TemplateMetaDataAttributes implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;

	private TemplateSheetMetaData mediaTemplateSheet;

	private ProposalHeadAttributes proposalHeadAttributes;

	private String attributeName;

	private int rowNum;

	private String colNum;

	private String format;
	private Long fontSize;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TEMPLATE_ATTRIBUTE_SEQUENCE")
	@SequenceGenerator(name = "TEMPLATE_ATTRIBUTE_SEQUENCE", sequenceName = "MP_MEDIA_PLAN_ATTR_SEQ", allocationSize = 1)
	public long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}

	@Column(name = "ATTRIBUTE_NAME")
	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(final String attributeName) {
		this.attributeName = attributeName == null ? attributeName : attributeName.trim();
	}

	@Column(name = "ROW_NUMBER")
	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(final int rowNum) {
		this.rowNum = rowNum;
	}

	@Column(name = "COL_NUMBER")
	public String getColNum() {
		return colNum;
	}

	public void setColNum(final String colNum) {
		this.colNum = colNum;
	}

	@ManyToOne
	@JoinColumn(name = "MP_MEDIA_TEMPLATE_SHEET_ID", nullable = true, insertable = true, updatable = true)
	public TemplateSheetMetaData getMediaTemplateSheet() {
		return mediaTemplateSheet;
	}

	public void setMediaTemplateSheet(final TemplateSheetMetaData mediaTemplateSheet) {
		this.mediaTemplateSheet = mediaTemplateSheet;
	}

	@Column(name = "FORMAT")
	public String getFormat() {
		return format;
	}

	public void setFormat(final String format) {
		this.format = format;
	}

	@Column(name = "FONT_SIZE")
	public Long getFontSize() {
		return fontSize;
	}

	public void setFontSize(final Long fontSize) {
		this.fontSize = fontSize;
	}

	@OneToOne()
	@JoinColumn(insertable = true, updatable = true, nullable = true, name = "HEAD_ATTRIBUTE_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	public ProposalHeadAttributes getProposalHeadAttributes() {
		return proposalHeadAttributes;
	}

	public void setProposalHeadAttributes(final ProposalHeadAttributes proposalHeadAttributes) {
		this.proposalHeadAttributes = proposalHeadAttributes;
	}
}
