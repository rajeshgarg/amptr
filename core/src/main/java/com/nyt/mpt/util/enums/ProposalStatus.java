/**
 * 
 */
package com.nyt.mpt.util.enums;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

/**
 * Provide status of proposal
 * 
 * @author amandeep.singh
 * 
 */
public enum ProposalStatus {
	/**
	 * Represent status of proposal is "Unassigned"
	 */
	UNASSIGNED {

		@Override
		public String getDisplayName() {
			return "Unassigned";
		}
	},
	/**
	 * Represent status of proposal is "In Progress"
	 */
	INPROGRESS {

		@Override
		public String getDisplayName() {
			return "In Progress";
		}
	},
	/**
	 * Represent status of proposal is "Proposed"
	 */
	PROPOSED {

		@Override
		public String getDisplayName() {
			return "Proposed";
		}
	},
	/**
	 * Represent status of proposal is "Deleted"
	 */
	DELETED {

		@Override
		public String getDisplayName() {
			return "Deleted";
		}
	},
	/**
	 * Represent status of proposal is "Under Review"
	 */
	REVIEW {

		@Override
		public String getDisplayName() {
			return "Under Review";
		}
	},
	/**
	 * Represent status of proposal is "Ready To Bridge"
	 */
	SOLD {

		@Override
		public String getDisplayName() {
			return "Sold";
		}
	},
	/**
	 * Represent status of proposal is "Expired"
	 */
	EXPIRED {

		@Override
		public String getDisplayName() {
			return "Expired";
		}
	},
	/**
	 * Represent status of proposal is "Rejected By Client"
	 */
	REJECTED_BY_CLIENT {

		@Override
		public String getDisplayName() {
			return "Rejected By Client";
		}
	};

	private static final Boolean FALSE = false ;
	private static final Boolean TRUE = true ;

	public abstract String getDisplayName();

	/**
	 * @param currency
	 * @return
	 */
	public static ProposalStatus findByName(String proposalStatus) {
		if (StringUtils.isNotBlank(proposalStatus)) {
			for (ProposalStatus propStat : ProposalStatus.values()) {
				if (proposalStatus.equals(propStat.name())) {
					return propStat;
				}
			}
		}
		throw new IllegalArgumentException("No Status enum found for given Status name: " + proposalStatus);
	}

	/**
	 * @return
	 */
	public static Map<String, String> getProposalStatusMap() {
		final Map<String, String> proposalStatusMap = new TreeMap<String, String>();
		for (ProposalStatus proposalStatus : ProposalStatus.values()) {
			if(!proposalStatus.name().equals(ProposalStatus.DELETED.name())){
				proposalStatusMap.put(proposalStatus.name(), proposalStatus.getDisplayName());
			}
		}
		return proposalStatusMap;
	}
	
	/**
	 * Method check which status of a proposal is applicable under different status
	 * 
	 * @param proposalStatus
	 * @return
	 */
	@SuppressWarnings("incomplete-switch")
	public static Map<String, Boolean> getProposalStatusAccessMap(ProposalStatus proposalStatus){
		final Map<String, Boolean> proposalAccessMap = createAllStatusMap();
		if (proposalStatus != null) {
			switch (proposalStatus) {
			case UNASSIGNED:
				proposalAccessMap.put(ProposalStatus.INPROGRESS.name(), TRUE);
				proposalAccessMap.put(ProposalStatus.DELETED.name(), TRUE);
				break;
			case INPROGRESS:
				proposalAccessMap.put(ProposalStatus.PROPOSED.name(), TRUE);
				proposalAccessMap.put(ProposalStatus.DELETED.name(), TRUE);
				break;
			case PROPOSED:
				proposalAccessMap.put(ProposalStatus.INPROGRESS.name(), TRUE);
				proposalAccessMap.put(ProposalStatus.REVIEW.name(), TRUE);
				proposalAccessMap.put(ProposalStatus.REJECTED_BY_CLIENT.name(), TRUE);
				break;
			case REVIEW:
				proposalAccessMap.put(ProposalStatus.INPROGRESS.name(), TRUE);
				proposalAccessMap.put(ProposalStatus.SOLD.name(), TRUE);
				break;
			case EXPIRED:
				proposalAccessMap.put(ProposalStatus.INPROGRESS.name(), TRUE);
				break;
			case REJECTED_BY_CLIENT:
				proposalAccessMap.put(ProposalStatus.INPROGRESS.name(), TRUE);
				break;
			}
		}
		return proposalAccessMap;
	}

	private static Map<String, Boolean> createAllStatusMap() {
		final Map<String, Boolean> proposalAccessMap = new TreeMap<String, Boolean>();
		proposalAccessMap.put(ProposalStatus.UNASSIGNED.name(), FALSE);
		proposalAccessMap.put(ProposalStatus.INPROGRESS.name(), FALSE);
		proposalAccessMap.put(ProposalStatus.PROPOSED.name(), FALSE);
		proposalAccessMap.put(ProposalStatus.DELETED.name(), FALSE);
		proposalAccessMap.put(ProposalStatus.REVIEW.name(), FALSE);
		proposalAccessMap.put(ProposalStatus.SOLD.name(), FALSE);
		proposalAccessMap.put(ProposalStatus.EXPIRED.name(), FALSE);
		proposalAccessMap.put(ProposalStatus.REJECTED_BY_CLIENT.name(), FALSE);
		return proposalAccessMap;
	}
}
