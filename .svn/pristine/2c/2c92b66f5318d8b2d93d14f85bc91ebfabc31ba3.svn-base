/**
 * 
 */
package com.nyt.mpt.util;

import java.util.Comparator;
import org.apache.commons.lang.builder.CompareToBuilder;
import com.nyt.mpt.template.TemplateAttributeVO;

/**
 * Compare template attribute object based on proposal Type and line item Type.
 * @author rakesh.tewari
 * 
 */
public class TemplateAttributeComparator implements Comparator<TemplateAttributeVO> {

	/*
	 * (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(final TemplateAttributeVO templateAttributeVO1, final TemplateAttributeVO templateAttributeVO2) {
		if (templateAttributeVO1 == null && templateAttributeVO2 == null) {
			return 0;
		} else if (templateAttributeVO1 == null || templateAttributeVO2 == null) {
			return -1;
		} else {
			return new CompareToBuilder().append(templateAttributeVO1.getAttributeName(), templateAttributeVO2.getAttributeName()).toComparison();
		}
	}
}
