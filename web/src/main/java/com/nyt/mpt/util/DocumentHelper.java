/**
 * 
 */
package com.nyt.mpt.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.nyt.mpt.domain.Document;
import com.nyt.mpt.form.DocumentForm;
import com.nyt.mpt.service.IDocumentService;
import com.nyt.mpt.util.enums.DocumentForEnum;
import com.nyt.mpt.util.filter.FilterCriteria;

/**
 * This <code>DocumentHelper</code> class includes all the helping methods for {@link Document} operations for both AMPT database and File System
 * 
 * @author surendra.singh
 */

public class DocumentHelper {

	private DocumentUtil docUtil;

	private IDocumentService documentService;

	private static final String FILE_SEPARATOR = System.getProperty("file.separator");

	private static final Logger LOGGER = Logger.getLogger(DocumentHelper.class);

	/**
	 * Upload a new document to the component and insert or update record into database and saves it as {@link File} in the File System
	 * @param 	documentForm
	 * 			Contains all the information for saving/updating the {@link Document} in both AMPT database and File System
	 * @throws 	IOException
	 * 			Might occur while saving the document to the File System
	 */
	public void saveOrUpdateDocument(final DocumentForm documentForm) throws IOException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Saving or updating docuemnt with id - " + documentForm.getId());
		}
		final Document document = new Document();
		documentForm.populate(document);
		long documentId = 0;
		if (documentForm.getId() > 0) {
			documentId = getDocumentService().updateDocument(document);
		} else {
			documentId = getDocumentService().saveDocument(document);
		}
		final String location = docUtil.calculateVersionPath(docUtil.calculateFilePath(documentForm.getComponentId(), documentForm.getDocumentFor(), documentId));

		docUtil.saveFileToDisk(document, location, documentForm.getFile().getBytes());
	}

	/**
	 * Returns the {@link File} as per <code>document</code> and <code>version</code>
	 * @param 	document
	 * 			Contains all the information for fetching the {@link Document} in both AMPT database and File System
	 * @param 	version
	 * 			Which version of the document to be fetched
	 * @return
	 * @throws FileNotFoundException
	 */
	public File getDocumentFile(final DocumentForm document, final long version) throws FileNotFoundException {
		final String root = docUtil.calculateFilePath(document.getComponentId(), document.getDocumentFor(), document.getId());
		final File[] files = new File(root + FILE_SEPARATOR + version).listFiles();
		if (files != null) {
			return files[0];
		}
		LOGGER.error("File Not exist for:- Document Id - " + document.getId());
		throw new FileNotFoundException("File Not exist for :- Document Id: " + document.getId());
	}

	/**
	 * @param componentId
	 * @param docFor
	 * @param docId
	 * @return
	 * @throws FileNotFoundException
	 */
	public File getLatestDocumentFile(final long componentId, final DocumentForEnum docFor, final long docId) throws FileNotFoundException {
		final String root = docUtil.calculateFilePath(componentId, docFor.name(), docId);
		final Long[] version = StringUtil.convertStringArrayToLongArray(new File(root).list());
		if (version != null) {
			Arrays.sort(version, Collections.reverseOrder());
			final File[] files = new File(root + FILE_SEPARATOR + version[0]).listFiles();
			if (files != null) {
				return files[0];
			}
		}
		LOGGER.error("File Not exist for:- Document Id - " + docId);
		throw new FileNotFoundException("File Not exist for :- Document Id: " + docId);
	}

	/**
	 * Deletes the document from both AMPT database and File System
	 * @param 	documentForm
	 * 			Contains all the information for deleting the {@link Document} in both AMPT database and File System
	 * @throws 	IOException
	 * 			Might occur while deleting the document from the File System
	 */
	public void deleteDocument(final DocumentForm documentForm) throws IOException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Deleting document with id - " + documentForm.getId());
		}
		final Document document = new Document();
		documentForm.populate(document);
		if (getDocumentService().deleteDocument(document)) {
			docUtil.deleteDocumentFromDisk(documentForm.populate(document));
		}
	}

	/**
	 * Returns the List of {@link DocumentForm} to be shown in document's grid 
	 * @param 	tblGrid
	 * 			Sets {@link DocumentForm} in the {@link TableGrid} 
	 * @param 	componentId
	 * 			Contains the id of the component for which the document/(s) was/were uploaded
	 * @param 	type
	 * 			Entity for which the document was saved
	 * @param contextPath
	 * @return
	 * 			Returns the List of {@link DocumentForm}
	 */
	public List<DocumentForm> setComponentDocuments(final TableGrid<DocumentForm> tblGrid, final long componentId, final String type, final String contextPath) {
		final List<Document> documents = getDocumentService().getFilteredDocuments(componentId,
				new FilterCriteria("documentFor", type, "eq"), tblGrid.getPaginationCriteria(), tblGrid.getSortingCriteria());

		final int totalCount = getDocumentService().getFilteredDocumentsCount(componentId, new FilterCriteria("documentFor", type, "eq"));
		final List<DocumentForm> documentForms = new ArrayList<DocumentForm>();
		if (documents != null) {
			for (Document document : documents) {
				documentForms.add(convertDocumentToForm(contextPath, document));
			}
		}
		tblGrid.setGridData(documentForms, totalCount);
		return documentForms;
	}

	/**
	 * Returns the List {@link Document} as per <code>componentId</code> and <code>type</code>
	 * @param 	componentId
	 * 			Contains the id of the component for which the document/(s) was/were uploaded
	 * @param 	type
	 * 			Entity for which the document was saved
	 * @return
	 * 			Returns the List {@link Document}
	 */
	public List<Document> getDocumentList(final long componentId, final String type) {
		return getDocumentService().getFilteredDocuments(componentId, new FilterCriteria("documentFor", type, "eq"), null, null);
	}

	/**
	 * Converts {@link Document} to  {@link DocumentForm}
	 * @param contextPath
	 * @param document
	 * @return
	 */
	public DocumentForm convertDocumentToForm(final String contextPath, final Document document) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Converting document to form. Document Id - " + document.getId());
		}
		final DocumentForm form = new DocumentForm();
		form.populateForm(document);
		final String root = docUtil.calculateFilePath(document.getComponentId(), document.getDocumentFor(), document.getId());
		final Long[] version = StringUtil.convertStringArrayToLongArray(new File(root).list());
		final StringBuffer buffer = new StringBuffer();
		if (version != null) {
			Arrays.sort(version, Collections.reverseOrder());
			for (int i = 0; i < version.length; i++) {
				if (i > 0) {
					buffer.append(ConstantStrings.COMMA).append(ConstantStrings.SPACE);
				}
				buffer.append("<a href=").append(contextPath).append("/document/downloadfile.action?componentId=").append(document.getComponentId()).append("&documentFor=")
						.append(document.getDocumentFor()).append("&id=").append(document.getId()).append("&version=").append(version[i]).append('>')
						.append(version.length == 1 ? "Download" : version[i]).append("</a>");
			}
			form.setDownloadString(buffer.toString());
		}
		return form;
	}

	public IDocumentService getDocumentService() {
		return documentService;
	}

	public void setDocumentService(final IDocumentService documentService) {
		this.documentService = documentService;
	}

	public void setDocUtil(final DocumentUtil docUtil) {
		this.docUtil = docUtil;
	}
}
