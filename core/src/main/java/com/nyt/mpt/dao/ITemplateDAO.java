/**
 *
 */
package com.nyt.mpt.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nyt.mpt.domain.ProposalHead;
import com.nyt.mpt.domain.ProposalHeadAttributes;
import com.nyt.mpt.domain.TemplateMetaData;
import com.nyt.mpt.domain.TemplateMetaDataAttributes;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.filter.FilterCriteria;

/**
 * This <code>ITemplateDAO</code> interface includes all declarations for the
 * methods to get the <tt>Template</tt> data and set the configuration details.
 * 
 * @author rakesh.tewari
 * 
 */
public interface ITemplateDAO {

	/**
	 * Returns the List of {@link TemplateMetaData}
	 * 
	 * @return List of {@link TemplateMetaData}
	 */
	List<TemplateMetaData> getActiveMediaPlanTemplates();

	/**
	 * Returns {@link TemplateMetaData} from data base for a given template id.
	 * 
	 * @param templateId
	 *            Id of {@link TemplateMetaData}
	 * @return {@link TemplateMetaData} from data base for a given template id.
	 */
	TemplateMetaData getActiveMediaPlanTemplateById(final long templateId);

	/**
	 * Returns {@link TemplateMetaData} from data base for a given template name.
	 * 
	 * @param templateName
	 *            Name of {@link TemplateMetaData}
	 * @return {@link TemplateMetaData} from data base for a given template name.
	 */
	TemplateMetaData getMediaPlanTemplateByName(final String templateName);

	/**
	 * Returns the List of {@link ProposalHead}.
	 * 
	 * @return List of {@link ProposalHead}
	 */
	List<ProposalHead> getProposalHeadList();

	/**
	 * Returns the list of {@link ProposalHeadAttributes}
	 * 
	 * @return List of {@link ProposalHeadAttributes}
	 */
	List<ProposalHeadAttributes> getProposalHeadAttributes();

	/**
	 * Returns a map of {@link ProposalHeadAttributes} id as key and attribute
	 * display name as value if head id of {@link ProposalHead} is equal to the
	 * parameter otherwise method return null.
	 * 
	 * @param attributeId
	 *            Id of {@link ProposalHead}.
	 * @return map of {@link ProposalHeadAttributes} id as key and attribute
	 *         display name as value
	 */
	Map<Long, String> getProposalHeadAttributes(final Long attributeId);

	/**
	 * Returns map of {@link ProposalHead} id as key and display name as value.
	 * 
	 * @return Map of {@link ProposalHead} id as key and display name as value.
	 */
	Map<Long, String> getProposalHeadDisplayName();

	/**
	 * Returns list of {@link TemplateMetaData} based on {@link FilterCriteria},
	 * {@link PaginationCriteria} and {@link SortingCriteria}
	 * 
	 * @param filterCriteria
	 *            {@link FilterCriteria}
	 * @param pgCriteria
	 *            {@link PaginationCriteria}
	 * @param sortingCriteria
	 *            {@link SortingCriteria}
	 * @return List of {@link TemplateMetaData} based on {@link FilterCriteria},
	 *         {@link PaginationCriteria} and {@link SortingCriteria}
	 */
	List<TemplateMetaData> getMediaPlanTemplates(final FilterCriteria filterCriteria, final PaginationCriteria pgCriteria, final SortingCriteria sortingCriteria);

	/**
	 * Returns count of {@link TemplateMetaData} based on {@link FilterCriteria}
	 * 
	 * @param filterCriteria
	 *            {@link FilterCriteria}
	 * @return count of {@link TemplateMetaData} based on {@link FilterCriteria}
	 */
	int getMediaPlanTemplatesCount(final FilterCriteria filterCriteria);

	/**
	 * Returns {@link TemplateMetaData} after save or update in data base
	 * 
	 * @param templateMetaData
	 *            {@link TemplateMetaData}
	 * @return {@link TemplateMetaData} after save or update in data base
	 */
	TemplateMetaData saveCustomTemplate(final TemplateMetaData templateMetaData);

	/**
	 * Returns {@link ProposalHeadAttributes} after save or update in data base
	 * @param headAttributes {@link ProposalHeadAttributes}
	 * @return {@link ProposalHeadAttributes} after save or update in data base
	 */
	ProposalHeadAttributes saveHeadAttributes(final ProposalHeadAttributes headAttributes);

	/**
	 * Returns list of {@link ProposalHeadAttributes} based on attribute's
	 * display name of {@link ProposalHeadAttributes} and headName of
	 * {@link proposalHead}
	 * 
	 * @param filterOn
	 *            attribute's display name of {@link ProposalHeadAttributes}
	 * @param parameter
	 *            headName of {@link proposalHead}
	 * @return list of {@link ProposalHeadAttributes} based on attribute's
	 *         display name of {@link ProposalHeadAttributes} and headName of
	 *         {@link proposalHead}
	 */
	List<ProposalHeadAttributes> getHeadAttributesByParameter(final String attributeName, final String headName);

	/**
	 * Returns {@link TemplateMetaData} of based on {@link TemplateMetaData} id.
	 * 
	 * @param templateId
	 *            id of {@link TemplateMetaData}
	 * @return {@link TemplateMetaData} of based on {@link TemplateMetaData} id.
	 */
	TemplateMetaData getMediaPlanTemplateById(final long templateId);

	/**
	 * Returns list of {@link ProposalHead} based on head name of
	 * {@link proposalHead}
	 * 
	 * @param headName
	 *            Head name of {@link proposalHead}
	 * @return list of {@link ProposalHead} based on head name of
	 *         {@link proposalHead}
	 */
	List<ProposalHead> getProHeadListByName(final String headName);
	
	/**
	 * Delete {@link TemplateMetaDataAttributes} from data base.
	 * @param templateAttribute {@link TemplateMetaDataAttributes}
	 */
	void deleteTemplateAttribute(final TemplateMetaDataAttributes templateAttribute) ;

	/**
	 * Returns list of {@link ProposalHeadAttributes} based on set of token.
	 * 
	 * @param tokenSet
	 *            Set of token.
	 * @return List of {@link ProposalHeadAttributes} based on set of token.
	 */
	List<ProposalHeadAttributes> getProposalHeadAttributes(final Set<String> tokenSet);

	/**
	 * Delete {@link TemplateMetaData} based from Data base
	 * @param customTemplateDb
	 */
	void deleteCustomTemplate(final TemplateMetaData customTemplateDb);

	/**
	 * Returns {@link TemplateMetaData} id based on template name
	 * 
	 * @param templateName
	 *            Name of template
	 * @return {@link TemplateMetaData} id based on template name
	 */
	Long getTemplateIdByName(final String templateName);
}