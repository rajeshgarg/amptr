/**
 * 
 */
package com.nyt.mpt.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.nyt.mpt.dao.IDocumentDAO;
import com.nyt.mpt.domain.Document;
import com.nyt.mpt.service.IDocumentService;
import com.nyt.mpt.util.DocumentUtil;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.annotation.ValidateProposalDocument;
import com.nyt.mpt.util.filter.FilterCriteria;

/**
 * Service class for all operation on document
 * @author surendra.singh
 */
public class DocumentService implements IDocumentService {

	private IDocumentDAO documentDAO;
	private DocumentUtil docUtil;
	
	private static final Logger LOGGER = Logger.getLogger(DocumentService.class);
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.util.service.IDocumentService#getDocumentById(long)
	 */
	@Override
	public Document getDocumentById(final long documentId) {
		return getDocumentDAO().getDocumentById(documentId);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.util.service.IDocumentService#saveDocument(com.nyt.mpt.util.Document)
	 */
	@Override
	@ValidateProposalDocument
	public long saveDocument(final Document document) {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Saving document with id: "+document.getId());
		}
		return getDocumentDAO().saveDocument(document);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.util.service.IDocumentService#updateDocument(com.nyt.mpt.util.Document)
	 */
	@Override
	@ValidateProposalDocument
	public long updateDocument(final Document document) {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Updating document - " + document.getId());
		}
		final Document docDB = getDocumentById(document.getId());
		docDB.setDescription(document.getDescription());
		docDB.setFileSize(document.getFileSize());
		docDB.setName(document.getName());
		
		return getDocumentDAO().updateDocument(docDB);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.util.service.IDocumentService#getComponentDocuments(long, java.lang.String)
	 */
	@Override
	public List<Document> getFilteredDocuments(final long componentId, final FilterCriteria filterCriteria, 
			final PaginationCriteria paginationCriteria, final SortingCriteria sortingCriteria) {
		return getDocumentDAO().getFilteredDocuments(componentId, filterCriteria, paginationCriteria, sortingCriteria);
	}
	
	@Override
	public int getFilteredDocumentsCount(final long componentId, final FilterCriteria filterCriteria) {
		return getDocumentDAO().getFilteredDocumentsCount(componentId, filterCriteria);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.util.service.IDocumentService#deleteDocument(com.nyt.mpt.util.Document)
	 */
	@Override
	@ValidateProposalDocument
	public boolean deleteDocument(final Document document) {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Deleting document with id: " + document.getId());
		}
		return getDocumentDAO().deleteDocument(document.getId());
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IDocumentService#getDocumentList(java.util.List, java.lang.String)
	 */
	@Override
	public Map<Long, List<Document>> getDocumentsForComponents(final List<Long> componentIdList, final String type) {
		final Map<Long, List<Document>> documentMap = new HashMap<Long, List<Document>>();
		if(componentIdList.isEmpty()){
			return documentMap;
		}
		for (Document document : documentDAO.getDocumentsForComponents(componentIdList, type)) {
			if(documentMap.get(document.getComponentId()) == null){
				documentMap.put(document.getComponentId(), new ArrayList<Document>());
			}
			documentMap.get(document.getComponentId()).add(document);
		}
		return documentMap;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IDocumentService#saveDocumentAndFile(com.nyt.mpt.domain.Document, byte[])
	 */
	@Override
	public void saveDocumentAndFile(final Document document, final byte[] body) throws IOException {
		saveDocument(document);
		final String location = docUtil.calculateVersionPath(docUtil.calculateFilePath(document.getComponentId(),
				document.getDocumentFor(), document.getId()));
		docUtil.saveFileToDisk(document, location, body);
	}
	
	public IDocumentDAO getDocumentDAO() {
		return documentDAO;
	}

	public void setDocumentDAO(final IDocumentDAO documentDAO) {
		this.documentDAO = documentDAO;
	}

	public void setDocUtil(DocumentUtil docUtil) {
		this.docUtil = docUtil;
	}
}
