/**
 * 
 */
package com.nyt.mpt.domain.sos;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * This <code>SosNotes</code> class includes all the attributes related to
 * SosNotes and their getter and setter. The attributes have mapping with
 * <code>NOTES</code> table in the SOS database
 * 
 * @author amandeep.singh
 */
@Entity
@Table(name = "NOTES")
public class SosNotes {
	private long notesId;
	private String content;
	private long noteTypeId;
	private long parentId;
	private String noteableType;
	private Long createdBy;
	private String createDate;
	private String modifiedDate;
	private Long modifiedBy;

	@Id
	@Column(name = "ID", updatable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTES_SEQUENCE")
	@SequenceGenerator(name = "NOTES_SEQUENCE", sequenceName = "S_NOTES_ID", allocationSize = 1)
	public long getNotesId() {
		return notesId;
	}

	public void setNotesId(long notesId) {
		this.notesId = notesId;
	}

	@Column(name = "CONTENT", updatable = false)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "NOTE_TYPE_ID", updatable = false)
	public long getNoteTypeId() {
		return noteTypeId;
	}

	public void setNoteTypeId(long noteTypeId) {
		this.noteTypeId = noteTypeId;
	}

	@Column(name = "NOTABLE_ID", updatable = false)
	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	@Column(name = "CREATED_BY_UID", updatable = false)
	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "CREATED_AT", updatable = false)
	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	@Column(name = "UPDATED_AT", updatable = false)
	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@Column(name = "UPDATED_BY_UID", updatable = false)
	public Long getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Long modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	@Column(name = "NOTABLE_TYPE", updatable = false)
	public String getNoteableType() {
		return noteableType;
	}

	public void setNoteableType(String noteableType) {
		this.noteableType = noteableType;
	}
}
