package com.nyt.mpt.util;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.nyt.mpt.common.AbstractTest;

public class MailUtilTest extends AbstractTest {

	@Autowired
	private MailUtil mailUtil;
	
	@Test
	public void testSetMessageInfo() {
		Assert.notNull(mailUtil.setMessageInfo(getMailInfo()));
	}

	@Test
	public void testSetMessageInfo1() {
		Assert.notNull(mailUtil.setMessageInfo(getMailInfo(), tempFileForEmailAttachment(new Exception("This file contains test data"))));
	}
	
	@Test
	public void testSetMessageInfo2() {
		Assert.notNull(mailUtil.createMessageForSectionWeightJob(getMailInfo(), true));
	}
	
	@Test
	public void testSetMessageInfo3() {
		Assert.notNull(mailUtil.setMessageInfo(getMailInfo(), null, "AttachmentName"));
	}
	
	private Map<String, String> getMailInfo() {
		final Map<String, String> mailProps = new HashMap<String, String>();
		mailProps.put("mailTo", "test.mail@nytimes.com");
		mailProps.put("subject", "Test Subject for Mail Tesing");
		mailProps.put("mailFrom", "donotreply@nytimes.com");
		mailProps.put("textMsg", "Test Body for Mail Tesing");
		mailProps.put("cc", "cc.mail@nytimes.com");
		return mailProps;
	}
	
	private File tempFileForEmailAttachment(final Exception exception) {
		File file = null;
		PrintWriter printWriter = null;
		try {
			file = File.createTempFile("log", ".txt");
			printWriter = new PrintWriter(file);
		} catch (Exception e) {
			
		}
		exception.printStackTrace(printWriter);
		printWriter.close();
		return file;
	}
}
