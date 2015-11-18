/**
 * 
 */
package com.nyt.mpt.form;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

import com.nyt.mpt.domain.TemplateMetaData;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.DateUtil;

/**
 * @author rakesh.tewari
 * 
 */
public class TemplateManagementForm extends BaseForm<TemplateMetaData> implements Serializable {

	private static final long serialVersionUID = 1L;

	private long templateId;
	private String templateName;
	private String lastUpdated;
	private String updatedBy;
	private String templateFileName;
	private String templateJsonData;
	private String sheetName;
	private MultipartFile customTemplateFile;
	private String sheetID;
	private String fileName;
	private String tmpFile;
	private String downloadString;
	private boolean editAction = false;// true for edit and false for update
	private boolean useExistingRow = false;

	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.form.BaseForm#populate(java.lang.Object)
	 */
	@Override
	public TemplateMetaData populate(final TemplateMetaData bean) {
		bean.setActive(isActive());
		return bean;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.form.BaseForm#populateForm(java.lang.Object)
	 */
	@Override
	public void populateForm(final TemplateMetaData bean) {
		this.templateId = bean.getTemplateId();
		this.templateName = bean.getTemplateName();
		this.templateFileName = bean.getTemplateFileName();
		this.setLastUpdated((bean.getModifiedDate() == null) ? ConstantStrings.EMPTY_STRING : DateUtil.getGuiDateString(bean.getModifiedDate().getTime()));
		this.updatedBy = bean.getModifiedBy();
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(final String templateName) {
		this.templateName = templateName == null ? templateName : templateName.trim();
	}

	public String getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(final String lastUpdated) {
		this.lastUpdated = lastUpdated == null ? lastUpdated : lastUpdated.trim();
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(final String updatedBy) {
		this.updatedBy = updatedBy == null ? updatedBy : updatedBy.trim();
	}

	public MultipartFile getCustomTemplateFile() {
		return customTemplateFile;
	}

	public void setCustomTemplateFile(final MultipartFile customTemplateFile) {
		this.customTemplateFile = customTemplateFile;
	}

	public long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(final long templateId) {
		this.templateId = templateId;
	}

	public String getTemplateJsonData() {
		return templateJsonData;
	}

	public void setTemplateJsonData(final String templateJsonData) {
		this.templateJsonData = templateJsonData;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(final String sheetName) {
		this.sheetName = sheetName;
	}

	public String getSheetID() {
		return sheetID;
	}

	public void setSheetID(final String sheetID) {
		this.sheetID = sheetID == null ? sheetID : sheetID.trim();
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(final String fileName) {
		this.fileName = fileName == null ? fileName : fileName.trim();
	}

	public String getTemplateFileName() {
		return templateFileName;
	}

	public void setTemplateFileName(final String templateFileName) {
		this.templateFileName = templateFileName == null ? templateFileName : templateFileName.trim();
	}

	public void setTmpFile(final String tmpFile) {
		this.tmpFile = tmpFile;
	}

	public String getTmpFile() {
		return tmpFile;
	}

	public String getDownloadString() {
		return downloadString;
	}

	public void setDownloadString(final String downloadString) {
		this.downloadString = downloadString != null ? downloadString.trim() : downloadString;
	}

	public boolean isEditAction() {
		return editAction;
	}

	public void setEditAction(final boolean action) {
		this.editAction = action;
	}

	public void setUseExistingRow(final boolean useExistingRow) {
		this.useExistingRow = useExistingRow;
	}

	public boolean isUseExistingRow() {
		return useExistingRow;
	}
}
