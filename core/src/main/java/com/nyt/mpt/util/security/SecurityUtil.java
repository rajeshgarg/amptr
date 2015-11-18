/**
 *
 */
package com.nyt.mpt.util.security;

import org.springframework.security.core.context.SecurityContextHolder;

import com.nyt.mpt.domain.Role;
import com.nyt.mpt.domain.User;

/**
 * @author amandeep.singh
 *
 */
public final class SecurityUtil {

	public static final String EMAIL_PRODUCT_MANAGER = "EPM";

	public static final String SALES_GENERAL = "SGL";

	public static final String MEDIA_PLANNER = "PLR";

	public static final String PRICING_ADMIN = "PAN";

	public static final String AD_OPS = "AOP";

	public static final String ADMIN = "ADM";
	
	public static final String PRODUCT_OWNER = "POW";

	private static final String[] PROPOSAL_ASSIGN_TO = new String[] {MEDIA_PLANNER, ADMIN, PRICING_ADMIN, EMAIL_PRODUCT_MANAGER, PRODUCT_OWNER};
	
	private static final String[] PROPOSAL_REVIEWER = new String[] {ADMIN, AD_OPS, MEDIA_PLANNER, EMAIL_PRODUCT_MANAGER, PRODUCT_OWNER};
	
	private SecurityUtil() {
		super();
	}

	/**
	 * Returns the currently logged in User
	 *
	 * @return
	 */
	public static User getUser() {
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	/**
	 * Return Array of roles, who can edit the proposal in normal work-flow (proposal is not mark for review)  
	 * @return
	 */
	public static String[] getProposalAssignToList() {
		return SecurityUtil.PROPOSAL_ASSIGN_TO;
	}
	
	/**
	 * Return Array of roles, who can edit the proposal, when proposal is marked for review
	 * @return
	 */
	public static String[] getProposalReviewers() {
		return SecurityUtil.PROPOSAL_REVIEWER;
	}
	
	/**
	 * Check if user is pricing admin
	 * @return
	 */
	public static boolean isUserPricingAdmin(final User user) {
		if (user != null) {
			for (Role role : user.getUserRoles()) {
				if (SecurityUtil.PRICING_ADMIN.equalsIgnoreCase(role.getRoleName())) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Check if user is Proposal Planner
	 * @return
	 */
	public static boolean isUserProposalPlanner(final User user) {
		if (user != null) {
			for (Role role : user.getUserRoles()) {
				if (SecurityUtil.MEDIA_PLANNER.equalsIgnoreCase(role.getRoleName())) {
					return true;
				}
			}
		}
		return false;
	}	
}