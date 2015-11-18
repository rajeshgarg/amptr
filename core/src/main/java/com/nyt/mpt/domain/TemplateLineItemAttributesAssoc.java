package com.nyt.mpt.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.nyt.mpt.template.TemplateVO;

/**
 * This <code>TemplateLineItemAttributesAssoc</code> class includes all the
 * attributes related to Template LineItem Attributes and their getter and
 * setter. The attributes have mapping with <code>MP_LINE_TEMP_ATTR_ASSOC</code>
 * table in the AMPT database
 * 
 * @author manish.kesarwani
 */
@Entity
@Table(name = "MP_LINE_TEMP_ATTR_ASSOC")
public class TemplateLineItemAttributesAssoc implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;

	private long proposalId;

	private long proposalVersionId;

	private long templateId;

	private byte[] templateObject;

	private TemplateVO mediaTemplateVo;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TEMP_ATTR_ASSOC_SEQUENCE")
	@SequenceGenerator(name = "TEMP_ATTR_ASSOC_SEQUENCE", sequenceName = "MP_TEMP_ATTR_ASSOC_SEQUENCE", allocationSize = 1)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "MP_PROPOSAL_ID")
	public long getProposalId() {
		return proposalId;
	}

	public void setProposalId(long proposalId) {
		this.proposalId = proposalId;
	}

	@Column(name = "MP_PROPOSAL_VERSION_ID")
	public long getProposalVersionId() {
		return proposalVersionId;
	}

	public void setProposalVersionId(long proposalVersionId) {
		this.proposalVersionId = proposalVersionId;
	}

	@Column(name = "MP_TEMPLATE_ID")
	public long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(long templateId) {
		this.templateId = templateId;
	}

	// This method is used by hibernate to get the serialised Object
	@Column(name = "MP_TEMP_OBJ")
	public byte[] getTemplateObject() {
		return templateObject;
	}

	// This method is used by hibernate to set the serialised object
	public void setTemplateObject(byte[] templateObject) throws IOException, ClassNotFoundException {
		ObjectInputStream in = null;
		ByteArrayInputStream bais = new ByteArrayInputStream(templateObject);
		try {
			in = new ObjectInputStream(bais);
			TemplateVO dserializeMediaTemplateVo = (TemplateVO) in.readObject();
			this.mediaTemplateVo = dserializeMediaTemplateVo;
		} finally {
			if (in != null) {
				in.close();
			}
			if (bais != null) {
				bais.close();
			}
		}
	}

	@Transient
	public TemplateVO getMediaTemplateVo() {
		return mediaTemplateVo;
	}

	public void setMediaTemplateVo(TemplateVO mediaTemplateVo) throws IOException {
		ObjectOutputStream out = null;
		this.mediaTemplateVo = mediaTemplateVo;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			out = new ObjectOutputStream(baos);
			out.writeObject(mediaTemplateVo);
		} finally {
			if (out != null) {
				out.close();
			}
			if (baos != null) {
				baos.close();
			}
		}
		this.templateObject = baos.toByteArray();
	}
}
