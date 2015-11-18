/**
 * 
 */
package com.nyt.mpt.util;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.nyt.mpt.util.exception.BusinessException;
import com.nyt.mpt.util.exception.NytRuntimeException;

/**
 * @author surendra.singh
 * 
 */
public final class MailUtil {

	private JavaMailSender mailSender;
	private String notification;
	private Properties envMailProperties;
	
	private static final Logger LOGGER = Logger.getLogger(MailUtil.class);

	/**
	 * @param mailMap
	 * @param mailSender
	 */
	public MailUtil(JavaMailSender mailSender) {
		super();
		this.mailSender = mailSender;
	}

	/**
	 * @param message
	 */
	public final void sendMail(final MimeMessage message) {
		if(StringUtils.isNotBlank(notification) && "true".equalsIgnoreCase(notification)){
			try {
				mailSender.send(message);
			} catch (MailException ex) {
				LOGGER.error("Exception occured while sending E-mail : " + ex);
				throw ex;
			}
			try {
				LOGGER.info("E-mail send successfully for - " + message.getSubject());
			} catch (MessagingException e) {
				LOGGER.error("Exception occured while getting the subject header of the mail sent");
			}
		}
	}

	/**
	 * @return
	 * @throws UnknownHostException 
	 */
	public MimeMessage createMessageForSectionWeightJob(Map<String, String> mailProps, boolean success) {
		final MimeMessage message = mailSender.createMimeMessage();
		try {
			LOGGER.info("Preparing email for weight target calculation job ");
			final MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
			messageHelper.setTo(mailProps.get("mailTo"));
			messageHelper.setSubject(getEnvironment() + "Section weight calculation job - " + (success ? "Success!!" : "Failed!!"));
			messageHelper.setFrom(mailProps.get("mailFrom"));
			try {
				messageHelper.setText("The cron job to calculate the weight of the pricing sections " + (success ? "executed successfully" : "failed") + " on "
						+ DateUtil.getGuiDateString(DateUtil.getCurrentDate()) + "\n\n(Generated "
						+ DateUtil.getGuiDateTimeString(System.currentTimeMillis()) + " by " + InetAddress.getLocalHost().getCanonicalHostName() + " )");
			} catch (UnknownHostException e) {
				LOGGER.error(e);
			}
		} catch (MessagingException e) {
			LOGGER.error("Exception occured while creating E-mail message : " + e);
			throw new NytRuntimeException(e);
		}
		return message;
	}

	/**
	 * Preparing email when proposal was send by 'Pricing Admin' or 'Proposal Planner' for review"
	 * @param mailProps
	 * @return
	 */
	public MimeMessage setMessageInfo(Map<String, String> mailProps) {
		final MimeMessage message = mailSender.createMimeMessage();
		try {
			LOGGER.info("Preparing email when proposal was send by 'Pricing Admin' or 'Proposal Planner' for review");
			final MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
			final String[] mail = mailProps.get("mailTo").split(ConstantStrings.COMMA);
			if(mail != null && mail.length > 0 && StringUtils.isNotBlank(mail[0])){
				messageHelper.setTo(mail);
			}
			messageHelper.setSubject(getEnvironment() + mailProps.get("subject"));
			messageHelper.setFrom(mailProps.get("mailFrom"));
			final StringBuffer msgContent = new StringBuffer(mailProps.get("textMsg")).append("\t\n\t\n\t\n***").append(ConstantStrings.SPACE).append(ConstantStrings.AUTO_GENERATED_MAIL);
			messageHelper.setText(msgContent.toString());
			if(mailProps.containsKey("cc") && StringUtils.isNotBlank(mailProps.get("cc"))){
				String[] mailcc = mailProps.get("cc").split(ConstantStrings.COMMA);
				messageHelper.setCc(mailcc);
			}
		} catch (MessagingException e) {
			LOGGER.error("Exception occured while creating E-mail message : " + e);
			throw new NytRuntimeException(e);
		}
		return message;
	}
	
	/**
	 * Preparing email when reservation is overbooked
	 * @param mailProps
	 * @return
	 */
	public MimeMessage setHtmlMessageInfo(Map<String, String> mailProps) {
		final MimeMessage message = mailSender.createMimeMessage();
		try {
			LOGGER.info("Preparing email when proposal was send by 'Pricing Admin' or 'Proposal Planner' for review");
			final MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
			final String[] mail = mailProps.get("mailTo").split(ConstantStrings.COMMA);
			if(mail != null && mail.length > 0 && StringUtils.isNotBlank(mail[0])){
				messageHelper.setTo(mail);
			}
			messageHelper.setSubject(getEnvironment() + mailProps.get("subject"));
			messageHelper.setFrom(mailProps.get("mailFrom"));
			final StringBuffer msgContent = new StringBuffer(mailProps.get("textMsg")).append(ConstantStrings.NEW_HTML_LINE+ConstantStrings.NEW_HTML_LINE+"***").append(ConstantStrings.SPACE).append(ConstantStrings.AUTO_GENERATED_MAIL);
			messageHelper.setText(msgContent.toString(),true);
			if(mailProps.containsKey("cc")){
				String[] mailcc = mailProps.get("cc").split(ConstantStrings.COMMA);
				messageHelper.setCc(mailcc);
			}
		} catch (MessagingException e) {
			LOGGER.error("Exception occured while creating E-mail message : " + e);
			throw new NytRuntimeException(e);
		}
		return message;
	}
	
	/**
	 * set mail info with file
	 * @param mailProps
	 * @param file
	 * @return
	 */
	public MimeMessage setMessageInfo(Map<String, String> mailProps, File file) {
		final MimeMessage message = mailSender.createMimeMessage();
		try {
			final MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
			String[] mail = null;
			if(StringUtils.isNotBlank(mailProps.get("mailTo"))){
				mail = mailProps.get("mailTo").split(ConstantStrings.COMMA);
			}
			if (mail != null && mail.length > 0 && StringUtils.isNotBlank(mail[0])) {
				messageHelper.setTo(mail);
			}
			messageHelper.setSubject(getEnvironment() + mailProps.get("subject"));
			messageHelper.setFrom(mailProps.get("mailFrom"));
			final StringBuffer msgContent = new StringBuffer(mailProps.get("textMsg")).append("\t\n\t\n\t\n***").append(ConstantStrings.SPACE).append(ConstantStrings.AUTO_GENERATED_MAIL);
			messageHelper.setText(msgContent.toString());
			if (mailProps.containsKey("cc") && mailProps.get("cc") != null) {
				String[] mailcc = mailProps.get("cc").split(ConstantStrings.COMMA);
				messageHelper.setCc(mailcc);
			}
			if (file != null) {
				messageHelper.addAttachment("log.txt", file);
			}
		} catch (MessagingException e) {
			LOGGER.error("Exception occured while creating E-mail message : " + e);
			throw new BusinessException(ConstantStrings.EMAIL_MESSAGE_NOT_CREATED_EXCEPTION, new CustomBusinessError(), e);
		}
		return message;
	}
	
	/**
	 * set mail info with data source
	 * @param mailProps
	 * @param file
	 * @return
	 */
	public MimeMessage setMessageInfo(Map<String, String> mailProps, DataSource dataSourse, String attachmentName) {
		final MimeMessage message = mailSender.createMimeMessage();
		try {
			final MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
			final String[] mail = mailProps.get("mailTo").split(ConstantStrings.COMMA);
			if (mail != null && mail.length > 0 && StringUtils.isNotBlank(mail[0])) {
				messageHelper.setTo(mail);
			}
			messageHelper.setSubject(getEnvironment() + mailProps.get("subject"));
			messageHelper.setFrom(mailProps.get("mailFrom"));
			final StringBuffer msgContent = new StringBuffer(mailProps.get("textMsg")).append("\t\n\t\n\t\n***").append(ConstantStrings.SPACE).append(ConstantStrings.AUTO_GENERATED_MAIL);
			messageHelper.setText(msgContent.toString());
			if (mailProps.containsKey("cc") && mailProps.get("cc") != null) {
				String[] mailcc = mailProps.get("cc").split(ConstantStrings.COMMA);
				messageHelper.setCc(mailcc);
			}
			if (dataSourse != null && attachmentName != null) {
				messageHelper.addAttachment(attachmentName , dataSourse);
			}
		} catch (MessagingException e) {
			LOGGER.error("Exception occured while creating E-mail message : " + e);
			throw new BusinessException("Exception occured while Attaching log.txt to E-mail message", new CustomBusinessError(), e);
		}
		return message;
	}
	public void setNotification(String notification) {
		this.notification = notification;
	}
	
	/**
	 * Get the Environment variable
	 */
	@JsonIgnore
	private String getEnvironment() {
		String environment = ConstantStrings.EMPTY_STRING;
		if(StringUtils.isNotBlank(envMailProperties.getProperty("env"))) {
			environment = ConstantStrings.OPEN_SQUARE_BRAKET + "AMPT" + ConstantStrings.SPACE + envMailProperties.getProperty("env").toUpperCase() + ConstantStrings.CLOSE_SQUARE_BRAKET + ConstantStrings.SPACE + ConstantStrings.HYPHEN + ConstantStrings.SPACE;
		}
		return environment;
	}

	public void setEnvMailProperties(Properties envMailProperties) {
		this.envMailProperties = envMailProperties;
	}

}
