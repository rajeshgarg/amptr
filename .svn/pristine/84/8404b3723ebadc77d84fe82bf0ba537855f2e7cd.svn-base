/**
 * 
 */
package com.nyt.mpt.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.ProductCreativeAssoc;
import com.nyt.mpt.domain.ProposalHead;
import com.nyt.mpt.domain.ProposalHeadAttributes;
import com.nyt.mpt.domain.ProposalVersion;
import com.nyt.mpt.domain.TemplateJson;
import com.nyt.mpt.domain.TemplateMetaData;
import com.nyt.mpt.domain.TemplateSheetMetaData;
import com.nyt.mpt.template.TemplateVO;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.filter.FilterCriteria;

/**
 * This interface declare Template Service level  methods
 * @author manish.kesarwani
 *
 */
public interface ITemplateService {

	/**
	 * Returns the list of {@link TemplateMetaData} in ascending order and there
	 * status must be active. List of {@link TemplateMetaData} does not include
	 * any <tt>Creative Specs</tt> type <tt>Template</tt>.
	 * 
	 * @return list of {@link TemplateMetaData} in ascending order and there
	 *         status must be active.
	 */
	List<TemplateMetaData> getActiveMediaPlanTemplates();

	/**
	 * Returns active {@link TemplateMetaData} based on <tt>Template</tt> Id.
	 * {@link TemplateMetaData} contains all the detail data of
	 * {@link TemplateSheetMetaData} as well as
	 * {@link TemplateMetaDataAttributes}.
	 * 
	 * @param templateId
	 *            Configured {@link TemplateMetaData} id.
	 * @return {@link TemplateMetaData} based on active <tt>Template<tt> id.
	 */
	TemplateMetaData getActiveMediaPlanTemplateById(long templateId);

	/**
	 * Returns active {@link TemplateMetaData} based on <tt>Template</tt> name.
	 * {@link TemplateMetaData} contains all the detail of
	 * {@link TemplateSheetMetaData} as well as
	 * {@link TemplateMetaDataAttributes}.
	 * 
	 * @param templateName
	 *            Name of the <tt>Template</tt>
	 * @return active {@link TemplateMetaData} based on <tt>Template</tt> name.
	 */
	TemplateMetaData getMediaPlanTemplateByName(String templateName);

	/**
	 * Returns {@link TemplateVO}, this contains <tt>Template</tt> basic
	 * structure details based on templateId and set proposal level data.
	 * {@link TemplateVO} also contains product and sales target details of
	 * given option's line item.
	 * 
	 * @param optionID
	 *            <tt>Option</tt> id of <tt>Proposal</tt>.
	 * @param proposalVersion
	 *            Version <tt></tt> of the <tt>Option</tt>.
	 * @param templateId
	 *            Id of configured <tt>Template</tt>
	 * @return {@link TemplateVO}, this contains <tt>Template<tt> basic
	 *         structure details based on templateId.
	 */
	TemplateVO generateMediaTemplateObject(long optionID, ProposalVersion proposalVersion, long templateId);

	/**
	 * Returns {@link TemplateVO} in which method populate product data,
	 * creative data, creative attribute data, line item data and product sales
	 * target attribute data based on {@link LineItem} parameter.
	 * 
	 * @param mediaPlanTemplateVO
	 *            {@link TemplateVO} in which method populate data.
	 * @param lineitem
	 *            {@link LineItem} whose data would be populated in
	 *            {@link TemplateVO}.
	 * @param counter
	 *            Row number of the sheet.
	 * @return {@link TemplateVO} in which method populate data.
	 */
	TemplateVO populateLineItemAttributesList(TemplateVO mediaPlanTemplateVO, LineItem lineitem, int counter);

	/**
	 * Returns {@link TemplateVO} in which method populate creative data,
	 * creative attribute data and line item data for <tt>Creative Specs</tt>
	 * template.
	 * 
	 * @param templateVO
	 *            {@link TemplateVO} in which method populate creative data,
	 *            creative attribute data and line item data.
	 * @param lineitem
	 *            {@link LineItem} whose data would be populated in
	 *            {@link TemplateVO}.
	 * @param counter
	 *            Row number of the sheet.
	 * @param productCreativeAssoc
	 *            Used to fetch creative attributes
	 * @return {@link TemplateVO} in which method populate creative data,
	 *         creative attribute data and line item data.
	 */
	TemplateVO populateCreativeSpecList(final TemplateVO templateVO, final LineItem lineitem, final int counter, ProductCreativeAssoc productCreativeAssoc);

	/**
	 * Returns {@link TemplateVO} based on {@link TemplateMetaData} id.
	 * {@link TemplateVO} contains data of {@link Proposal} based on
	 * {@link ProposalVersion}.
	 * 
	 * @param proposalId
	 *            {@link Proposal} id whose data user want to export.
	 * @param proposalVersion
	 *            Version of {@link ProposalOption}.
	 * @param templateId
	 *            Configured template id. In this format user want to export
	 *            data.
	 * @return {@link TemplateVO} based on {@link TemplateMetaData},
	 *         {@link Proposal} and {@link ProposalVersion} id.
	 */
	TemplateVO generateCreativeSpecObject(final long proposalId, final ProposalVersion proposalVersion, final long templateId);

	/**
	 * Returns a map of {@link ProposalHeadAttributes} id as key and attribute
	 * display name as value if head id of {@link ProposalHeadAttributes} (i.e.
	 * {@link ProposalHead} id) is equal to the parameter otherwise method
	 * return null.
	 * 
	 * @param Id
	 *            This is the id of {@link ProposalHead}.
	 * @return A map of {@link ProposalHeadAttributes} id as key and attribute
	 *         display name as value.
	 */
	Map<Long, String> getProposalHeadAttributesMap(Long Id);

	/**
	 * Returns list of {@link ProposalHeadAttributes}.
	 * {@link ProposalHeadAttributes} includes details of line item and proposal
	 * attributes. (i.e. attribute name, auto configKey etc.)
	 * 
	 * @return list of {@link ProposalHeadAttributes}.
	 */
	List<ProposalHeadAttributes> getProposalHeadAttributes();

	/**
	 * Returns list of {@link ProposalHead}. {@link ProposalHead} includes
	 * details of line item and proposal head. (i.e User, Creative, Proposal,
	 * Product, Line Item Attribute, Line Item, Creative Attribute, Package)
	 * 
	 * @return list of {@link ProposalHead}.
	 */
	List<ProposalHead> getProposalHeadList();

	/**
	 * Delete configured {@link TemplateMetaData} from Data base.
	 * 
	 * @param customTemplate
	 *            contains configured <tt>Template</tt> details. (i.e. name of
	 *            file, sheet meta data, attributes meta data etc.)
	 */
	void deleteCustomTemplate(TemplateMetaData customTemplate);

	/**
	 * Save {@link TemplateMetaData} after setting {@link TemplateJson} in
	 * {@link TemplateMetaDataAttributes} and {@link TemplateSheetMetaData} data
	 * in {@link TemplateMetaData}.
	 * 
	 * @param templateSheetData
	 *            List of {@link TemplateJson}. {@link TemplateJson} contains
	 *            details of configured <tt>Template</tt> sheet's attributes.
	 *            (i.e. row number, column number etc.)
	 * @param customTemplateVO
	 *            Contains configured <tt>Template</tt> details. (i.e. name of
	 *            file etc.)
	 * @param templateSheetMetaData
	 *            Contains configured <tt>Template</tt> sheet meta data.(i.e.
	 *            name of sheet etc.)
	 * @param editAction
	 *            If it's value is true then system will delete all the related
	 *            value of {@link TemplateMetaDataAttributes}.
	 */
	void saveCustomTemplateMetaData(List<TemplateJson> templateSheetData, TemplateMetaData customTemplateVO, TemplateSheetMetaData templateSheetMetaData, boolean editAction);

	/**
	 * Returns a list of {@link TemplateMetaData} based on
	 * {@link FilterCriteria}, {@link PaginationCriteria} and
	 * {@link SortingCriteria} criteria. If all these parameters are null then
	 * method return all the data of {@link TemplateMetaData}.
	 * 
	 * @param filterCriteria
	 *            This criteria includes search options, search field and search
	 *            string for data filtering.
	 * @param paginationCriteria
	 *            This criteria includes firstResult the index of the first
	 *            result object to be retrieved (numbered from 0) and maxResults
	 *            the maximum number of result objects to retrieve (or <=0 for
	 *            no limit)
	 * @param sortingCriteria
	 *            This criteria includes an ordering to the result set. And the
	 *            sorting is based on sortingOrder and sortingField values.
	 * @return List of {@link TemplateMetaData} based on {@link FilterCriteria},
	 *         {@link PaginationCriteria} and {@link SortingCriteria} criteria.
	 */
	List<TemplateMetaData> getMediaPlanTemplates(FilterCriteria filterCriteria, PaginationCriteria paginationCriteria, SortingCriteria sortingCriteria);

	/**
	 * Returns count of configured <tt>Template</tt> based on
	 * {@link FilterCriteria}. This is the count of all the active configured
	 * <tt>Template</tt> except {@link ConstantStrings.CREATIVE_SPEC_TEMPALTE}.
	 * 
	 * @param filterCriteria
	 *            This filter criteria includes search options, search field and
	 *            search string for data filtering.
	 * @return Return count of configured <tt>Template<tt> based on
	 *         {@link FilterCriteria}.
	 */
	int getMediaPlanTemplatesCount(FilterCriteria filterCriteria);

	/**
	 * Returns map of {@link ProposalHead} id as key and displayName as value.
	 * 
	 * @return map of {@link ProposalHead} id as key and displayName as value.
	 */
	Map<Long, String> getProposalHeadDisplayName();

	/**
	 * Create a new persistent for {@link ProposalHeadAttributes} if not exist
	 * otherwise update the existing instance of the Object.
	 * 
	 * @param headAttributes
	 *            {@link ProposalHeadAttributes} object for save or update.
	 * @return {@link ProposalHeadAttributes} if not exist otherwise update the
	 *         existing
	 */
	ProposalHeadAttributes saveHeadAttributes(ProposalHeadAttributes headAttributes);

	/**
	 * Returns a list of {@link ProposalHeadAttributes} if displayAttributeName
	 * of {@link ProposalHeadAttributes} is equal to attributeName (parameter)
	 * and headName of {@link ProposalHead} is equal to headName (parameter).
	 * 
	 * @param attributeName
	 *            Name of displayAttributeName in {@link ProposalHeadAttributes}
	 * @param headName
	 *            Name of head in {@link ProposalHead}.
	 * @return a list of {@link ProposalHeadAttributes}.
	 */
	List<ProposalHeadAttributes> getHeadAttributesByParameter(String attributeName, String headName);

	/**
	 * Returns {@link TemplateMetaData} of a configured <tt>Template</tt>. If
	 * {@link TemplateMetaData} id is matched with the provided value then
	 * system return {@link TemplateMetaData} object otherwise system return
	 * null.
	 * 
	 * @param templateId
	 *            Id of {@link TemplateMetaData}.
	 * @return {@link TemplateMetaData} of a configured <tt>Template</tt> if id
	 *         is matched otherwise return null.
	 */
	TemplateMetaData getMediaPlanTemplateById(final long templateId);

	/**
	 * Returns a list of {@link ProposalHeadAttributes} based on set of
	 * autoConfigKey name (these are also called token).
	 * 
	 * @param tokenSet
	 *            A set of autoConfigKey name. And these tokens must have a
	 *            prefix 'MPLI_' or 'MP_'.
	 * @return list of {@link ProposalHeadAttributes} based on set of
	 *         autoConfigKey name.
	 */
	List<ProposalHeadAttributes> getProposalHeadAttributes(Set<String> tokenSet);

	/**
	 * Returns active {@link TemplateMetaData} Id if template name is matched
	 * with the name provided in parameter. Otherwise method returns null value.
	 * 
	 * @param templateName
	 *            Name of configured <tt>Template</tt>.
	 * @return Returns active {@link TemplateMetaData} Id.
	 */
	Long getTemplateIdByName(String templateName);
}
