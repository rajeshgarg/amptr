package com.nyt.mpt.template;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.nyt.mpt.domain.Product;
import com.nyt.mpt.domain.SalesTarget;
import com.nyt.mpt.util.ConstantStrings;

/**
 * 
 * used for Template Object
 * @author manish.kesarwani
 *
 */
public class TemplateVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String name;
	
	private boolean validate;
	
	private Map<String, String> errorMessages = new HashMap<String, String>();
	private Set<String> productSet = new HashSet<String>();
	private Set<String> salesTargetSet = new HashSet<String>();
	
	//This list contain Reference Data Map for SOS Data
	private ReferenceDataMap referenceDataMap = new ReferenceDataMap();
	
	//This list contains template sheets
	private Map<String, TemplateSheetVO> templateSheetMap = new HashMap<String, TemplateSheetVO>();
	
	//this set is used to store all token in custom template based on this set we  implement token autoconfig
	private Set<String> tokenSet = new HashSet<String>();		
	/**
	 * Validating Product status is active or not
	 * @param product
	 * @param productId
	 */
	public void validateProduct(Product product, long productId) {
		if(product == null || !ConstantStrings.ACTIVE_STATUS.equalsIgnoreCase(product.getStatus()) || product.getDeleteDate() != null){
			productSet.add((product == null) ? ConstantStrings.EMPTY_STRING : product.getDisplayName());
			errorMessages.put("message.generic.invalidProduct", convertMessage(productSet));
		}
	}
	
	/**
	 * Validating sales target status is active or not
	 * @param salesTarget
	 * @param salesTargetId
	 */
	public void validateSalesTarget(SalesTarget salesTarget, long salesTargetId) {
		if(!ConstantStrings.ACTIVE_STATUS.equalsIgnoreCase(salesTarget.getStatus()) || salesTarget.getDeleteDate() != null){
			salesTargetSet.add(salesTarget.getSalesTargeDisplayName());
			errorMessages.put("message.generic.invalidSalesTarget", convertMessage(salesTargetSet));
		}
	}
	
	private String convertMessage(Set<String> productSalesTargetString) {
		StringBuffer productSalesTargetBuffer = new StringBuffer();
		for (String productSalesTarget : productSalesTargetString) {
			if (productSalesTargetBuffer != null && productSalesTargetBuffer.length() > 0) {
				productSalesTargetBuffer.append("\n");
				productSalesTargetBuffer.append(",");
			}
			productSalesTargetBuffer.append(productSalesTarget);
		}
		return productSalesTargetBuffer.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, TemplateSheetVO> getTemplateSheetMap() {
		return templateSheetMap;
	}

	public void setTemplateSheetMap(Map<String, TemplateSheetVO> templateSheetMap) {
		this.templateSheetMap = templateSheetMap;
	}

	public ReferenceDataMap getReferenceDataMap() {
		return referenceDataMap;
	}

	public void setReferenceDataMap(ReferenceDataMap referenceDataMap) {
		this.referenceDataMap = referenceDataMap;
	}

	public Map<String, String> getErrorMessages() {
		return errorMessages;
	}

	public void setErrorMessages(Map<String, String> errorMessages) {
		this.errorMessages = errorMessages;
	}

	public boolean isValidate() {
		return validate;
	}

	public void setValidate(boolean validate) {
		this.validate = validate;
	}

	/**
	 * @return the tokenSet
	 */
	public Set<String> getTokenSet() {
		return tokenSet;
	}

	/**
	 * @param tokenSet the tokenSet to set
	 */
	public void setTokenSet(Set<String> tokenSet) {
		this.tokenSet = tokenSet;
	}
}
