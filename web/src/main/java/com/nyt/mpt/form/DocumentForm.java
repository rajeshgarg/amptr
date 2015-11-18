/**
 *
 */
package com.nyt.mpt.form;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

import com.nyt.mpt.domain.Document;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.DateUtil;
import com.nyt.mpt.util.enums.DocumentTypeEnum;

/**
 * This <code>DocumentForm</code>> bean is used for document information
 * 
 * @author surendra.singh
 */

public class DocumentForm extends BaseForm<Document> implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final int BYTES_IN_KILO_BYTE = 1024;

	private long id;

	private String name;

	private String fileName;

	private String description;

	private String fileType;

	private long fileSize;

	private long componentId;

	private String documentFor;

	private MultipartFile file;

	private String downloadString;
	
	private String modifiedBy;
	
	private String modifiedOn;

	@Override
	public Document populate(final Document document) {
		document.setId(this.id);
		document.setName(this.name);
		if (this.file != null) {
			document.setFileName(this.file.getOriginalFilename());
			final DocumentTypeEnum docTypeEnum = DocumentTypeEnum.getDocType(this.file.getOriginalFilename());
			document.setFileType(docTypeEnum == null ? ConstantStrings.NA : docTypeEnum.name());
			document.setFileSize(this.file.getSize() / BYTES_IN_KILO_BYTE);
		}
		document.setDescription(this.description);
		document.setComponentId(this.componentId);
		document.setDocumentFor(this.documentFor);
		return document;
	}

	@Override
	public void populateForm(final Document bean) {
		this.id = bean.getId();
		this.name = bean.getName();
		this.fileName = bean.getFileName();
		this.fileSize = bean.getFileSize();
		final DocumentTypeEnum docTypeEnum = DocumentTypeEnum.findByName(bean.getFileType());
		this.fileType = docTypeEnum == null ? ConstantStrings.EMPTY_STRING : docTypeEnum.getDisplayName();
		this.description = bean.getDescription();
		this.componentId = bean.getComponentId();
		this.documentFor = bean.getDocumentFor();
		this.modifiedBy = bean.getModifiedBy();
		this.modifiedOn = DateUtil.getGuiDateString(bean.getModifiedDate());
	}

	public long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name == null ? name : name.trim();
	}

	public long getComponentId() {
		return componentId;
	}

	public void setComponentId(final long componentId) {
		this.componentId = componentId;
	}

	public String getDocumentFor() {
		return documentFor;
	}

	public void setDocumentFor(final String documentFor) {
		this.documentFor = documentFor == null ? documentFor : documentFor.trim();
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(final MultipartFile file) {
		this.file = file;
	}

	public String getDownloadString() {
		return downloadString;
	}

	public void setDownloadString(final String downloadString) {
		this.downloadString = downloadString == null ? downloadString : downloadString.trim();
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(final String fileName) {
		this.fileName = fileName == null ? fileName : fileName.trim();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description == null ? description : description.trim();
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(final String fileType) {
		this.fileType = fileType == null ? fileType : fileType.trim();
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(final long fileSize) {
		this.fileSize = fileSize;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(String modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
}
