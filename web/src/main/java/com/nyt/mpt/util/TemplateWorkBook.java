/**
 * 
 */
package com.nyt.mpt.util;

import org.apache.poi.ss.usermodel.Workbook;

/**
 * This class is used for Template Work Book
 * 
 * @author manish.kesarwani
 * 
 */
public class TemplateWorkBook {

	private Workbook templateWorkBook;
	private String templateFileName;
	private String templateOutputFileName;

	public String getTemplateFileName() {
		return templateFileName;
	}

	public void setTemplateFileName(final String templateFileName) {
		this.templateFileName = templateFileName;
	}

	public String getTemplateOutputFileName() {
		return templateOutputFileName;
	}

	public void setTemplateOutputFileName(final String templateOutputFileName) {
		this.templateOutputFileName = templateOutputFileName;
	}

	public Workbook getTemplateWorkBook() {
		return templateWorkBook;
	}

	public void setTemplateWorkBook(final Workbook templateWorkBook) {
		this.templateWorkBook = templateWorkBook;
	}
}
