/**
 * 
 */
package com.nyt.mpt.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.LineItemTarget;
import com.nyt.mpt.form.GeoTargetForm;

/**
 * @author amandeep.singh
 */
public class TargetJsonConverter {

	private static final String ZIP_CODES = "Zip Codes";
	private static final String BEHAVIOURAL = "Behavioral";

	private ProposalHelper proposalHelper;
	private LineItemUtil lineItemUtil;

	private static final String NOT = "not";
	private static final String ACTION = "action";
	private static final String TARGET_TYPE_ELEMENT = "targetTypeElement";
	private static final String TARGET_TYPE_ID = "targetTypeId";
	private static final String SEGMENT_LEVEL = "segmentLevel";

	private static final Logger LOGGER = Logger.getLogger(TargetJsonConverter.class);

	public static Set<LineItemTarget> convertJsonToObject(final String jsonString, final LineItem bean) {
		final Set<LineItemTarget> targetSet = new LinkedHashSet<LineItemTarget>();
		final ObjectMapper mapper = new ObjectMapper();
		JsonNode node = null;
		try {
			node = mapper.readTree(jsonString);
		} catch (JsonProcessingException e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Error while parsing Targeting of a LineItem");
			}
		} catch (IOException e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Error while parsing Targeting of a LineItem");
			}
		}
		if (node != null) {
			for (JsonNode targeting : node) {
				if (targeting.findValue(TARGET_TYPE_ID) != null && targeting.findValue(TARGET_TYPE_ELEMENT) != null && targeting.findValue(ACTION) != null 
						&& targeting.findValue(NOT) != null && targeting.findValue(SEGMENT_LEVEL) != null) {
					final LineItemTarget lineItemTarget = new LineItemTarget();
					lineItemTarget.setSosTarTypeId(Long.valueOf(targeting.findValue(TARGET_TYPE_ID).getTextValue()));
					lineItemTarget.setSosTarTypeElementId(targeting.findValue(TARGET_TYPE_ELEMENT).getTextValue());
					lineItemTarget.setOperation(targeting.findValue(ACTION).getTextValue());
					lineItemTarget.setNegation(targeting.findValue(NOT).getTextValue());
					lineItemTarget.setSegmentLevel(targeting.findValue(SEGMENT_LEVEL).getTextValue());
					lineItemTarget.setProposalLineItem(bean);
					targetSet.add(lineItemTarget);
				}
			}
		}
		return targetSet;
	}

	public String convertObjectToJson(final List<LineItemTarget> geoTargetLst) {
		List<GeoTargetForm> geoTargetFormLst = null;
		String returnString = ConstantStrings.EMPTY_STRING;
		final ObjectMapper objectMapper = new ObjectMapper();
		if (geoTargetLst != null && !geoTargetLst.isEmpty()) {
			geoTargetFormLst = getGeotargetFormList(geoTargetLst);
			try {
				returnString = objectMapper.writeValueAsString(geoTargetFormLst);
			} catch (JsonGenerationException e) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Error while creating Json for ADGroups for UI");
				}
			} catch (JsonMappingException e) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Error while creating Json for ADGroups for UI");
				}
			} catch (IOException e) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Error while creating Json for ADGroups for UI");
				}
			}
		}
		return returnString;
	}

	/**
	 * Return list of populated GeoTargetForm
	 * @param geoTargetLst
	 * @param pricingInfoMap
	 * @return
	 */
	private List<GeoTargetForm> getGeotargetFormList(final List<LineItemTarget> geoTargetLst) {
		final List<GeoTargetForm> geoTargetFormLst = new ArrayList<GeoTargetForm>();
		final Map<Long, String> targetTypeMap = proposalHelper.getTargetTypeCriteria();
		for (LineItemTarget geoTarget : geoTargetLst) {
			final GeoTargetForm geoTargetForm = new GeoTargetForm();
			geoTargetForm.populateForm(geoTarget);
			geoTargetForm.setSosTarTypeName(targetTypeMap.get(geoTarget.getSosTarTypeId()));
			if (ZIP_CODES.equals(geoTargetForm.getSosTarTypeName()) || BEHAVIOURAL.equals(geoTargetForm.getSosTarTypeName())) {
				geoTargetForm.setSosTarTypeElementName(geoTarget.getSosTarTypeElementId());
			} else {
				geoTargetForm.setSosTarTypeElementName(lineItemUtil.getTarTypeElementName(geoTarget));
			}
			geoTargetFormLst.add(geoTargetForm);
		}
		return geoTargetFormLst;
	}

	public void setProposalHelper(final ProposalHelper proposalHelper) {
		this.proposalHelper = proposalHelper;
	}

	public void setLineItemUtil(final LineItemUtil lineItemUtil) {
		this.lineItemUtil = lineItemUtil;
	}

}
