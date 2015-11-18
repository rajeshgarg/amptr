/**
 *
 */
package com.nyt.mpt.dao;

import java.util.List;

import com.nyt.mpt.domain.Document;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.filter.FilterCriteria;

/**
 * This <code>IDocumentDAO</code> contains all the methods declarations for the document related operations like : saving/deleting/updating documents, fetching documents etc 
 *
 * @author surendra.singh
 */

public interface IDocumentDAO {

	/**
	 * Returns {@link Document} by <code>documentId</code>
	 * @param 	documentId
	 * 			Contains Id for which we want the {@link Document}
	 * @return
	 * 			Returns {@link Document}
	 */
	Document getDocumentById(long documentId);

	/**
	 * Saves a new {@link Document}
	 * @param 	document
	 * 			{@link Document} to be saved in the AMPT
	 * @return long
	 * 			Contains id of the saved Document
	 */
	long saveDocument(Document document);

	/**
	 * Updates the existing document in the AMPT database
	 * @param 	document
	 * 			{@link Document} to be updated
	 * @return
	 */
	long updateDocument(Document document);

	/**
	 * Return List of {@link Document} as per <code>filterCriteria</code>, <code>pageCriteria</code> and <code>sortingCriteria</code>
	 * @param 	componentId
	 * 			Contains the id of the component for which the document was uploaded
	 * @param 	filterCriteria
	 * 			{@link FilterCriteria}
	 * @param 	pageCriteria
	 * 			{@link PaginationCriteria}
	 * @param 	sortingCriteria
	 * 			{@link SortingCriteria}
	 * @return
	 * 			Return List of {@link Document}
	 */
	List<Document> getFilteredDocuments(long componentId, FilterCriteria filterCriteria, PaginationCriteria pageCriteria,
			SortingCriteria sortingCriteria);

	/**
	 * Returns the count of the Documents as per <code>componentId</code>, <code>filterCriteria</code> to be used in the Document grid
	 * @param 	componentId
	 * 			Contains the id of the component for which the document was uploaded
	 * @param 	filterCriteria
	 * 			{@link FilterCriteria}
	 * @return
	 * 			Returns the integer value for the number of the documents
	 */
	int getFilteredDocumentsCount(long componentId, FilterCriteria filterCriteria);

	/**
	 * Deletes the document by <code>documentId</code>
	 * @param 	documentId
	 * 			Contains the id of the document to be deleted
	 * @return
	 */
	boolean deleteDocument(long documentId);

	/**
	 * Returns the List of {@link Document} by <code>componentIdList</code> and <code>type</code>
	 * @param 	componentIdList
	 * 			List of id of the components for which the documents were uploaded
	 * @param 	type
	 * 			Type of entity for which the document was saved
	 * @return
	 * 			Returns the List of {@link Document}
	 */
	List<Document> getDocumentsForComponents(List<Long> componentIdList, String type);
}
