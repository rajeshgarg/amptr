/**
 *
 */
package com.nyt.mpt.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nyt.mpt.domain.Document;
import com.nyt.mpt.form.DocumentForm;
import com.nyt.mpt.util.CustomBindingResult;
import com.nyt.mpt.util.DocumentHelper;
import com.nyt.mpt.util.TableGrid;
import com.nyt.mpt.util.XMLResponseForAjaxCall;
import com.nyt.mpt.util.enums.ErrorCodes;
import com.nyt.mpt.util.enums.UserHelpCodes;
import com.nyt.mpt.util.exception.ProposalAccessException;
import com.nyt.mpt.validator.DocumentValidator;

/**
 * This <code>DocumentController</code> has all the methods to save {@link Document} in AMPT database in MP_DOCUMENTS table as well as in File System, 
 * to show the document grid and give the option to download the document from the grid, delete the document from AMPT database in MP_DOCUMENTS table as well as from File System
 * @author surendra.singh
 */

@Controller
@RequestMapping("/document/*")
public class DocumentController extends AbstractBaseController {

	private DocumentHelper documentHelper;

	private static final Logger LOGGER = Logger.getLogger(DocumentController.class);

	/**
	 * Returns {@link XMLResponseForAjaxCall} when the document has been saved in the MP_DOCUMENTS table as well as in the File System
	 * @param 	response
	 * 			To construct the response ans send it back to the user
	 * @param 	document
	 * 			{@link Document} which is going to be saved in the MP_DOCUMENTS table as well as in the File System
	 * @return
	 * 			Returns {@link XMLResponseForAjaxCall} when the document has been saved
	 * @throws 	IOException
	 * 			Throws the Exception the there is error occurred while creating the {@link File} in the File System
	 */
	@ResponseBody
	@RequestMapping("/savedocument")
	public XMLResponseForAjaxCall saveAsNewDocument(final HttpServletResponse response, @ModelAttribute final DocumentForm document) throws IOException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Saving new Document with id: " + document.getId());
		}
		final XMLResponseForAjaxCall ajaxResponse = new XMLResponseForAjaxCall(getMessageSource());
		final CustomBindingResult results = new CustomBindingResult("myUploadForm", document);
		new DocumentValidator().validate(document, results);
		try {
			if (results.hasErrors()) {
				return constructResponse(response, ajaxResponse, results);
			} else {
				documentHelper.saveOrUpdateDocument(document);
			}
		} catch (ProposalAccessException pe) {
			results.rejectValue("ProposalException", ErrorCodes.proposalNotAssigned, "ProposalException", UserHelpCodes.HelpMandatoryInputMissing);
			return constructResponse(response, ajaxResponse, results);
		}
		return ajaxResponse;
	}

	/**
	 * Returns {@link ModelAndView} to show the documents and its related information in the document grid 
	 * @param 	tblGrid
	 * 			{@link TableGrid} which has all the related information about the table grid
	 * @param 	componentId
	 * 			This is the id of the object the {@link Document} is related to. MP_DOCUMENTS table has <code>componentId</code> as one of it's column
	 * @param 	documentFor
	 * 			This is to check for what entity the document is attached for
	 * @return
	 * 		Returns {@link ModelAndView} to show the documents in the grid
	 */
	@RequestMapping("/documentgriddata")
	public ModelAndView loadMasterGridData(final HttpServletRequest request, @ModelAttribute final TableGrid<DocumentForm> tblGrid, @RequestParam("componentId") final long componentId,
			@RequestParam("documentFor") final String documentFor) {
		final ModelAndView view = new ModelAndView("documentGridData");
		setGridData(tblGrid, componentId, documentFor, request.getContextPath());
		view.addObject(tblGrid);
		return view;
	}

	/**
	 * To download the file from the grid and pop up to if you want save it to the file system or not
	 * @param 	response
	 * 			This is the {@link HttpServletResponse} which has all the {@link File} attributes
	 * @param 	document
	 * 			Which document is going to be down-loaded from database and file system
	 * @param 	version
	 * 			Which version of the document will be down-loaded as there might be more than one version of a single document file
	 * @throws 	IOException
	 * 			This may occur when the <code>document</code> will be written to new <code>file</code>
	 */
	@RequestMapping("/downloadfile")
	public void downloadFile(final HttpServletResponse response, @ModelAttribute final DocumentForm document, @RequestParam("version") final int version) throws IOException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Downloading file with id: " + document.getId());
		}
		final File file = documentHelper.getDocumentFile(document, version);
		response.setContentType(new MimetypesFileTypeMap().getContentType(file));
		response.setContentLength((int) file.length());
		response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
		FileCopyUtils.copy(new FileInputStream(file), response.getOutputStream());
	}

	/**
	 * Returns <code>success</code> is documents gets deleted successfully
	 * @param 	document
	 * 			Which document will be going to delete from the MP_DOCUMENTS table as well as in the File System
	 * @return
	 * 			Returns <code>success</code> is documents gets deleted successfully
	 * @throws 	IOException
	 * 			This may occur while deleting the {@link Document} from the File System
	 */
	@ResponseBody
	@RequestMapping("/deletedocument")
	public String deleteFile(@ModelAttribute final DocumentForm document) throws IOException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Deleting file with id: " + document.getId());
		}
		documentHelper.deleteDocument(document);
		return "success";
	}

	private void setGridData(final TableGrid<DocumentForm> tblGrid, final long componentId, final String documentFor, final String contextPath) {
		documentHelper.setComponentDocuments(tblGrid, componentId, documentFor, contextPath);
	}

	public DocumentHelper getDocumentHelper() {
		return documentHelper;
	}

	public void setDocumentHelper(final DocumentHelper documentHelper) {
		this.documentHelper = documentHelper;
	}
}