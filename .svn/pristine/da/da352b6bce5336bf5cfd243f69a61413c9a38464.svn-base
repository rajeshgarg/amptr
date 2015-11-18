package com.nyt.mpt.document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.nyt.mpt.common.AbstractTest;
import com.nyt.mpt.domain.Creative;
import com.nyt.mpt.domain.Document;
import com.nyt.mpt.service.impl.CreativeService;
import com.nyt.mpt.service.impl.DocumentService;
import com.nyt.mpt.util.DocumentUtil;
import com.nyt.mpt.util.enums.DocumentForEnum;

/**
 * JUnit test for Document service
 * @author megha.vyas
 *
 */
public class DocumentServiceTest extends AbstractTest {
	@Autowired
	@Qualifier("documentService")
	private DocumentService documentService;

	@Autowired
	@Qualifier("creativeService")
	private CreativeService creativeService;
	
	@Autowired
	@Qualifier("docUtil")
	private DocumentUtil docUtil;

	private Document documentDB = null;
	private List<Document> documentList = null;

	@Before
	public void setup() {
		super.setAuthenticationInfo();
		final List<Creative> creativeList = creativeService.getCreativeList(true);
		if (creativeList != null && !creativeList.isEmpty()) {
			for (Creative creative : creativeList) {
				documentList = documentService.getFilteredDocuments(creative.getCreativeId(), null, null, null);
				if (!documentList.isEmpty()) {
					documentDB = documentList.get(0);
					break;
				}
			}

		}
	}

	/**
	 * Test for getDocumentById that Returns document for documentId
	 */
	@Test
	public void testGetDocumentById() {
		Assert.assertEquals(documentDB, documentService.getDocumentById(documentDB.getId()));
	}

	/**
	 * Test for saveDocument that Saves a new document
	 */
	@Test
	public void testSaveDocument() {
		final long docId = documentService.saveDocument(documentDB);
		Assert.assertEquals(documentDB.getId(), docId);
	}

	/**
	 * Test for updateDocument that Update existing document
	 */
	@Test
	public void testUpdateDocument() {
		if (documentDB != null) {
			Assert.assertEquals(documentDB.getId(), documentService.updateDocument(documentDB));
		}
	}

	/**
	 * Test for getFilteredDocuments that Return all document for type and
	 * component
	 */
	@Test
	public void testGetFilteredDocuments() {
		if (documentDB != null) {
			final List<Document> docList = documentService.getFilteredDocuments(documentDB.getComponentId(), null, null, null);
			if (docList != null) {
				Assert.assertTrue(docList.size() >= 0);
			}
		}
	}

	/**
	 * Test for getFilteredDocumentsCount that Return count of documents based
	 * on componentId and filter criteria.
	 */
	@Test
	public void testGetFilteredDocumentsCount() {
		if (documentDB != null) {
			Assert.assertTrue(documentService.getFilteredDocumentsCount(documentDB.getComponentId(), null) > 0);
		}
	}

	/**
	 * Test for getCreativeList that Return list of creative based on status
	 * (active/inactive)
	 */
	@Test
	public void testGetDocumentsForComponents() {
		List<Creative> creativeList = new ArrayList<Creative>();
		final List<Long> componentIds = new ArrayList<Long>();
		Assert.assertTrue(documentService.getDocumentsForComponents(componentIds, DocumentForEnum.CREATIVE.name()).isEmpty());

		creativeList = creativeService.getCreativeList(true);

		for (Creative creative : creativeList) {
			List<Document> docLst = documentService.getFilteredDocuments(creative.getCreativeId(), null, null, null);
			if(!docLst.isEmpty()){
				componentIds.add(creative.getCreativeId());
				break;
			}
		}
		
		final Map<Long, List<Document>> docMap = documentService.getDocumentsForComponents(componentIds, DocumentForEnum.CREATIVE.name());
		if (docMap != null && !docMap.isEmpty()) {
			Assert.assertTrue(docMap.size() >= 0);
		}
	}

	/**
	 * Test for deleteDocument that Delete the document from database
	 */
	@Test
	public void testDeleteDocument() {
		Assert.assertTrue(documentService.deleteDocument(documentDB));
	}
	
	@Test
	public void testCalculateFilePath(){
		String path = docUtil.calculateFilePath(1256, "PROPOSAL", 58975);
		Assert.assertTrue(path.length() > 0);
	}
	
	@Test
	public void testCalculateVersionPath(){
		String path = docUtil.calculateFilePath(1256, "PROPOSAL", 58975);
		String versionPath = docUtil.calculateVersionPath(path);
		Assert.assertTrue(versionPath.length() > 0);
	}
}