/**
 * 
 */
package com.nyt.mpt.util;

import java.util.Comparator;

import org.apache.commons.lang.builder.CompareToBuilder;

import com.nyt.mpt.form.ProposalForm;

/**
 * @author amandeep.singh
 */
public class OptionNameComparator implements Comparator<ProposalForm> {

	@Override
	public int compare(final ProposalForm proposalFormo1, final ProposalForm proposalFormo2) {
		if (proposalFormo1 == null && proposalFormo2 == null) {
			return 0;
		} else if (proposalFormo1 == null || proposalFormo2 == null) {
			return -1;
		} else {
			return new CompareToBuilder().append(proposalFormo1.getOptionName(), proposalFormo2.getOptionName()).toComparison();
		}
	}

}
