/**
 * 
 */
package com.nyt.mpt.template;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Used for developing Template Sheet
 * @author manish.kesarwani
 */
public class TemplateSheetVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;

	// This will contains all attributes read from custom template
	private List<TemplateAttributeVO> allTemplateAttributeList = new ArrayList<TemplateAttributeVO>();

	// This will contain Proposal header information
	private List<TemplateAttributeVO> mediaPlanProposalAttributesList = new ArrayList<TemplateAttributeVO>();

	// This will contain Line item column Name only
	private List<TemplateAttributeVO> mediaPlanLineItemsHeaderList = new ArrayList<TemplateAttributeVO>();

	private TreeMap<Long, List<TemplateAttributeVO>> mediaPlanLineItemsAttributesList = new TreeMap<Long, List<TemplateAttributeVO>>();
	
	private TreeMap<String, List<TemplateAttributeVO>> templateAttributesMap = new TreeMap<String, List<TemplateAttributeVO>>();

	public List<TemplateAttributeVO> getMediaPlanProposalAttributesList() {
		return mediaPlanProposalAttributesList;
	}

	public void setMediaPlanProposalAttributesList(final List<TemplateAttributeVO> mediaPlanProposalAttributesList) {
		this.mediaPlanProposalAttributesList = mediaPlanProposalAttributesList;
	}

	public List<TemplateAttributeVO> getMediaPlanLineItemsHeaderList() {
		return mediaPlanLineItemsHeaderList;
	}

	public void setMediaPlanLineItemsHeaderList(final List<TemplateAttributeVO> mediaPlanLineItemsHeaderList) {
		this.mediaPlanLineItemsHeaderList = mediaPlanLineItemsHeaderList;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public TreeMap<Long, List<TemplateAttributeVO>> getMediaPlanLineItemsAttributesList() {
		return mediaPlanLineItemsAttributesList;
	}

	public void setMediaPlanLineItemsAttributesList(final TreeMap<Long, List<TemplateAttributeVO>> mediaPlanLineItemsAttributesList) {
		this.mediaPlanLineItemsAttributesList = mediaPlanLineItemsAttributesList;
	}

	public List<TemplateAttributeVO> getAllTemplateAttributeList() {
		return allTemplateAttributeList;
	}

	public void setAllTemplateAttributeList(final List<TemplateAttributeVO> allTemplateAttributeList) {
		this.allTemplateAttributeList = allTemplateAttributeList;
	}

	public TreeMap<String, List<TemplateAttributeVO>> getTemplateAttributesMap() {
		return templateAttributesMap;
	}

	public void setTemplateAttributesMap(TreeMap<String, List<TemplateAttributeVO>> templateAttributesMap) {
		this.templateAttributesMap = templateAttributesMap;
	}
}
