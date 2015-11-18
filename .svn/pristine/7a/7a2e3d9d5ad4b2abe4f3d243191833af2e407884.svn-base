/**
 *
 */
package com.nyt.mpt.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.nyt.mpt.dao.IDocumentDAO;
import com.nyt.mpt.domain.Document;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.enums.SearchOption;
import com.nyt.mpt.util.filter.FilterCriteria;

/**
 * This DAO level class is used for Document related operation
 *
 * @author surendra.singh
 *
 */
public class DocumentDAO extends GenericDAOImpl implements IDocumentDAO {

	private static final String COMPONENT_ID = "componentId";

	private static final Logger LOGGER = Logger.getLogger(DocumentDAO.class);

	private static final Map<String, String> FIELD_COLUMN_MAP = new HashMap<String, String>();

	static {
		FIELD_COLUMN_MAP.put("id", "id");
		FIELD_COLUMN_MAP.put("fileName", "fileName");
		FIELD_COLUMN_MAP.put("description", "description");
		FIELD_COLUMN_MAP.put("fileType", "fileType");
		FIELD_COLUMN_MAP.put("fileSize", "fileSize");
		FIELD_COLUMN_MAP.put(COMPONENT_ID, COMPONENT_ID);
		FIELD_COLUMN_MAP.put("documentFor", "documentFor");
		FIELD_COLUMN_MAP.put("modifiedBy", "modifiedBy");
		FIELD_COLUMN_MAP.put("modifiedOn", "modifiedDate");
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.util.dao.IDocumentDAO#getDocumentById(long)
	 */
	@Override
	public Document getDocumentById(long documentId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching document by Id: " + documentId);
		}
		return load(Document.class, documentId);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.util.service.IDocumentService#getComponentDocuments(long, java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Document> getFilteredDocuments(long componentId, FilterCriteria filterCriteria,
			PaginationCriteria pageCriteria, SortingCriteria sortCriteria) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching filtered documents. FilterCriteria: " + filterCriteria + "ComponentId: " + componentId);
		}
		final DetachedCriteria criteria = constructFilterCriteria(filterCriteria, componentId);
		
		if (sortCriteria != null) {
			//update the sorting field name in sorting criteria with DB column name 
			sortCriteria.setSortingField(FIELD_COLUMN_MAP.get(sortCriteria.getSortingField()));
			addSortingCriteria(criteria, sortCriteria);
		}
		return findByCriteria(criteria, pageCriteria);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IDocumentDAO#getFilteredDocumentsCount(long, com.nyt.mpt.util.FilterCriteria)
	 */
	@Override
	public int getFilteredDocumentsCount(long componentId, FilterCriteria filterCriteria) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching filtered documents count. FilterCriteria: " + filterCriteria + "ComponentId: " + componentId);
		}
		return getCount(constructFilterCriteria(filterCriteria, componentId));
	}

	/**
	 * @param filterCriteria
	 * @param componentId
	 * @return
	 */
	private DetachedCriteria constructFilterCriteria(FilterCriteria filterCriteria, long componentId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Constructing Filter Criteria: " + filterCriteria);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(Document.class);
		criteria.add(Restrictions.eq(COMPONENT_ID, componentId));
		if (filterCriteria != null && StringUtils.isNotBlank(filterCriteria.getSearchField())) {
			if (SearchOption.CONTAIN.toString().equals(filterCriteria.getSearchOper())) {
				criteria.add(Restrictions.ilike(FIELD_COLUMN_MAP.get(filterCriteria.getSearchField()),
						filterCriteria.getSearchString(), MatchMode.ANYWHERE));
			} else {
				criteria.add(Restrictions.ilike(FIELD_COLUMN_MAP.get(filterCriteria.getSearchField()),
						filterCriteria.getSearchString(), MatchMode.EXACT));
			}
		}
		return criteria;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.util.dao.IDocumentDAO#saveDocument(com.nyt.mpt.util.Document)
	 */
	@Override
	public long saveDocument(Document document) {
		save(document);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Saving document with id: " + document.getId());
		}
		return document.getId();
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.util.dao.IDocumentDAO#updateDocument(com.nyt.mpt.util.Document)
	 */
	@Override
	public long updateDocument(Document document) {
		update(document);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Updating document with id: " + document.getId());
		}
		return document.getId();
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IDocumentDAO#deleteDocument(long)
	 */
	@Override
	public boolean deleteDocument(long documentId) {
		final Document documentDb = getDocumentById(documentId);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Deleting document with id: " + documentId);
		}
		delete(documentDb);
		return true;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IDocumentDAO#getDocumentsForComponents(java.util.List, java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Document> getDocumentsForComponents(List<Long> componentIdList, String type) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching documents for components. ComponentIdList: " + componentIdList + "Type : " + type);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(Document.class).addOrder(Order.asc(COMPONENT_ID));
		criteria.add(Restrictions.in(COMPONENT_ID, componentIdList));
		criteria.add(Restrictions.eq("documentFor", type));
		return findByCriteria(criteria);
	}
}
