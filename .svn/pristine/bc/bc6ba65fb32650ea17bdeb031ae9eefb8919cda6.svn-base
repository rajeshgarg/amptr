/**
 * 
 */
package com.nyt.mpt.form;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.nyt.mpt.domain.Notes;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.DateUtil;
import com.nyt.mpt.util.security.SecurityUtil;

/**
 * This form bean is used for the proposal notes information.
 * 
 * @author garima.garg
 * 
 */
public class NotesForm extends BaseForm<Notes> implements Serializable {

	private static final long serialVersionUID = 1L;

	private long proposalId;

	private long notesId;

	private String notesDescription;

	private String notesRole;

	private String notesCreationDate;

	private String notesCreatedBy;

	private String notesSectionHeader;

	private boolean pushedInSalesforce = false;

	/**
	 * @return the proposalId
	 */
	public long getProposalId() {
		return proposalId;
	}

	/**
	 * @param proposalId the proposalId to set
	 */
	public void setProposalId(final long proposalId) {
		this.proposalId = proposalId;
	}

	/**
	 * @return the notesId
	 */
	public long getNotesId() {
		return notesId;
	}

	/**
	 * @param notesId the notesId to set
	 */
	public void setNotesId(final long notesId) {
		this.notesId = notesId;
	}

	/**
	 * @return the notesDescription
	 */
	public String getNotesDescription() {
		return notesDescription;
	}

	/**
	 * @param notesDescription the notesDescription to set
	 */
	public void setNotesDescription(final String notesDescription) {
		this.notesDescription = StringUtils.isBlank(notesDescription) ? ConstantStrings.EMPTY_STRING : notesDescription.trim();
	}

	/**
	 * @return the notesRole
	 */
	public String getNotesRole() {
		return notesRole;
	}

	/**
	 * @param notesRole the notesRole to set
	 */
	public void setNotesRole(final String notesRole) {
		this.notesRole = notesRole;
	}

	/**
	 * @return the notesCreationDate
	 */
	public String getNotesCreationDate() {
		return notesCreationDate;
	}

	/**
	 * @param notesCreationDate the notesCreationDate to set
	 */
	public void setNotesCreationDate(final String notesCreationDate) {
		this.notesCreationDate = notesCreationDate;
	}

	/**
	 * @return the notesCreatedBy
	 */
	public String getNotesCreatedBy() {
		return notesCreatedBy;
	}

	/**
	 * @param notesCreatedBy the notesCreatedBy to set
	 */
	public void setNotesCreatedBy(final String notesCreatedBy) {
		this.notesCreatedBy = notesCreatedBy;
	}

	/**
	 * @return the notesSectionHeader
	 */
	public String getNotesSectionHeader() {
		return notesSectionHeader;
	}

	/**
	 * @param notesSectionHeader the notesSectionHeader to set
	 */
	public void setNotesSectionHeader(final String notesSectionHeader) {
		this.notesSectionHeader = notesSectionHeader;
	}

	@Override
	public void populateForm(final Notes notesBean) {
		this.setNotesId(notesBean.getId());
		this.setProposalId(notesBean.getProposalId());
		this.setNotesDescription(notesBean.getDescription());
		this.setNotesRole(notesBean.getRole());
		this.setNotesCreatedBy(notesBean.getCreatedByUserName());
		this.setNotesCreationDate(DateUtil.getGuiDateTimeString(notesBean.getModifiedDate().getTime()));
		if (notesBean.getModifiedDate().compareTo(notesBean.getCreatedDate()) == 0) {
			this.setNotesSectionHeader("Notes added on " + this.getNotesCreationDate() + " by " + notesBean.getCreatedByUserName() + " (" + notesBean.getRole() + ")");
		} else {
			this.setNotesSectionHeader("Notes updated on " + this.getNotesCreationDate() + " by " + notesBean.getCreatedByUserName() + " (" + notesBean.getRole() + ")");
		}
		if (notesBean.isPushedInSalesforce()) {
			this.setNotesSectionHeader(this.getNotesSectionHeader() + " | Pushed to SalesForce.");
		}
		if (StringUtils.isNotBlank(notesBean.getSfProposalRevisionId()) || "System.Cron".equalsIgnoreCase(notesBean.getCreatedBy())) {
			notesBean.setPushedInSalesforce(true);
		}
		this.setPushedInSalesforce(notesBean.isPushedInSalesforce());
	}

	@Override
	public Notes populate(final Notes bean) {
		bean.setId(this.getNotesId());
		bean.setProposalId(this.getProposalId());
		bean.setDescription(this.getNotesDescription());
		bean.setRole(SecurityUtil.getUser().getUserRoles().iterator().next().getDisplayName());
		bean.setCreatedByUserName(SecurityUtil.getUser().getFullName());
		bean.setPushedInSalesforce(this.isPushedInSalesforce());
		return bean;
	}

	public void setPushedInSalesforce(final boolean pushedInSalesforce) {
		this.pushedInSalesforce = pushedInSalesforce;
	}

	public boolean isPushedInSalesforce() {
		return pushedInSalesforce;
	}
}
