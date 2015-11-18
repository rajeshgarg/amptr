/**
 * 
 */
package com.nyt.mpt.util;

import com.nyt.mpt.util.enums.MessageType;

/**
 * This class is used for UI Status
 * @author Shishir.Srivastava
 */
public class UIStatus {

	private String status;
	private String linkText;
	private String href;
	private MessageType messageType;

	public String getStatus() {
		return status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	public String getLinkText() {
		return linkText;
	}

	public void setLinkText(final String linkText) {
		this.linkText = linkText;
	}

	public String getHref() {
		return href;
	}

	public void setHref(final String href) {
		this.href = href;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(final MessageType messageType) {
		this.messageType = messageType;
	}
}
