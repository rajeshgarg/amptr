/**
 * 
 */
package com.nyt.mpt.util;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nyt.mpt.domain.LineItemSalesTargetAssoc;
import com.nyt.mpt.domain.LineItemTarget;

/**
 * This class is used to get target type element name, sales target assoc, sales target type assoc
 * @author amandeep.singh
 */
public class LineItemUtil {

	private ProposalHelper proposalHelper;

	/**
	 * Method returns the comma separated name of LineItem Target Elements
	 * @param lineItemTarget
	 * @return
	 */
	public String getTarTypeElementName(final LineItemTarget lineItemTarget) {
		final Map<Long, String> targetTypeElementMap = proposalHelper.getTargetTypeElement(lineItemTarget.getSosTarTypeId().toString());
		String tarTypeElementName = ConstantStrings.EMPTY_STRING;
		final String[] tarTypeElement = lineItemTarget.getSosTarTypeElementId().split(ConstantStrings.COMMA);
		for (int i = 0; i < tarTypeElement.length; i++) {
			tarTypeElementName = tarTypeElementName + targetTypeElementMap.get(Long.valueOf(tarTypeElement[i])) + ", ";
		}
		return tarTypeElementName.substring(0, tarTypeElementName.lastIndexOf(ConstantStrings.COMMA));
	}

	/**
	 * Get sales target id from sales target type association
	 * @param lineItemSalesTargetAssocs
	 * @return
	 */
	public static String[] getSalesTargetIdFromAssoc(final List<LineItemSalesTargetAssoc> lineItemSalesTargetAssocs) {
		final Set<String> salesTargetType = new HashSet<String>(lineItemSalesTargetAssocs.size());
		for (LineItemSalesTargetAssoc lineItemSalesTargetAssoc : lineItemSalesTargetAssocs) {
			salesTargetType.add(String.valueOf(lineItemSalesTargetAssoc.getSosSalesTargetId()));
		}
		return (String[]) salesTargetType.toArray();
	}

	public ProposalHelper getProposalHelper() {
		return proposalHelper;
	}

	public void setProposalHelper(final ProposalHelper proposalHelper) {
		this.proposalHelper = proposalHelper;
	}
}
