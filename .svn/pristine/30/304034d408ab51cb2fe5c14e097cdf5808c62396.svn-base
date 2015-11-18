package com.nyt.mpt.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.nyt.mpt.util.ChangeTrackedDomain;

/**
 * This <code>Document</code> class includes all the attributes related to
 * Document and their getter and setter. The attributes have mapping with
 * <code>MP_DOCUMENTS</code> table in the AMPT database
 * 
 * @author surendra.singh
 */

@Entity
@Table(name = "MP_DOCUMENTS")
public class Document extends ChangeTrackedDomain {

	private static final long serialVersionUID = 1L;

	private long id;
	
	private String name;
	
	private String fileName;

	private String description;
	
	private String fileType;
	
	private long fileSize;
	
	private long componentId; 
	  
	private String documentFor; 

	private boolean active = true;
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DOCUMENTS_SEQUENCE")
	@SequenceGenerator(name = "DOCUMENTS_SEQUENCE", sequenceName = "MP_DOCUMENTS_SEQUENCE", allocationSize = 1)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "IS_ACTIVE")
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Column(name = "AD_COMPONENT_ID")
	public long getComponentId() {
		return componentId;
	}

	public void setComponentId(long componentId) {
		this.componentId = componentId;
	}

	@Column(name = "DOCUMENT_FOR", length = 100)
	public String getDocumentFor() {
		return documentFor;
	}

	public void setDocumentFor(String documentFor) {
		this.documentFor = documentFor;
	}

	@Column(name = "FILE_NAME", length = 100)
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name = "DESCRIPTION", length = 500)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "FILE_TYPE", length = 100, nullable = false)
	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	@Column(name = "FILE_SIZE")
	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
}
