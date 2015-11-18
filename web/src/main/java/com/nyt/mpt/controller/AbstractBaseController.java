package com.nyt.mpt.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validator;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.nyt.mpt.util.AjaxFormSubmitResponse;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.Constants;
import com.nyt.mpt.util.CustomBusinessError;
import com.nyt.mpt.util.XMLResponseForAjaxCall;
import com.nyt.mpt.util.enums.ErrorCodes;
import com.nyt.mpt.util.enums.ErrorMessageType;
import com.nyt.mpt.util.exception.BusinessException;
import com.nyt.mpt.util.exception.IntegrityValidatorException;
import com.nyt.mpt.util.exception.ProposalAccessException;
import com.nyt.mpt.util.exception.SOSViolationException;
import com.nyt.mpt.util.exception.CustomCheckedException;
import com.nyt.mpt.util.exception.TemplateGenerationException;
import com.nyt.mpt.util.exception.YieldexAvailsException;

/**
 * This <code>AbstractBaseController</code> class is base class for all the controllers. This includes methods for handling Exceptions.
 * Also contains methods to construct response and thus returns {@link AjaxFormSubmitResponse}
 * 
 * @author Shishir.Srivastava
 */
@Component
public abstract class AbstractBaseController {

	private static final String ERROR_OBJECT = "errorObject";

	protected MessageSource messageSource;

	private boolean sucess = true;

	private Validator validator;

	private static final Logger LOGGER = Logger.getLogger(AbstractBaseController.class);

	/**
	 * Handles RuntimeException for UI
	 * @param exception
	 * @param response
	 * @return
	 */
	@ExceptionHandler(RuntimeException.class)
	public ModelAndView handleRuntimeException(final RuntimeException exception, final HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		response.setHeader(ConstantStrings.CONTENT_TYPE, ConstantStrings.APPLICATION_JSON);
		final CustomBusinessError custBusinessErr = new CustomBusinessError();
		custBusinessErr.setMessageType(ErrorMessageType.ERROR.name());
		custBusinessErr.setErrorKey(ErrorCodes.runtimeExceptionMessage.getResourceName());
		custBusinessErr.setErrorMessage(getMessageSource().getMessage(custBusinessErr.getErrorKey(), custBusinessErr.getArguments(), Constants.NO_KEY_DEFINED, null));
		final ModelAndView view = new ModelAndView();
		view.addObject(ERROR_OBJECT, custBusinessErr);
		view.setViewName(ERROR_OBJECT);
		LOGGER.error("RuntimeException occured - ", exception);
		return view;
	}
	
	@ExceptionHandler(Exception.class)
	public ModelAndView handleException(final Exception exception, final HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		response.setHeader(ConstantStrings.CONTENT_TYPE, ConstantStrings.APPLICATION_JSON);
		final CustomBusinessError custBusinessErr = new CustomBusinessError();
		custBusinessErr.setMessageType(ErrorMessageType.ERROR.name());
		custBusinessErr.setErrorKey(ErrorCodes.runtimeExceptionMessage.getResourceName());
		custBusinessErr.setErrorMessage(getMessageSource().getMessage(custBusinessErr.getErrorKey(), custBusinessErr.getArguments(), Constants.NO_KEY_DEFINED, null));
		final ModelAndView view = new ModelAndView();
		view.addObject(ERROR_OBJECT, custBusinessErr);
		view.setViewName(ERROR_OBJECT);
		LOGGER.error("Exception occured - ", exception);
		return view;
	}

	/**
	 * Handles BusinessException for UI
	 * @param exception
	 * @param response
	 * @return
	 */
	@ExceptionHandler(BusinessException.class)
	public ModelAndView handleBusinessException(final BusinessException exception, final HttpServletResponse response) {
		return getErrorObject(exception, response);
	}

	/**
	 * Handles SalesforceConnectionException for UI
	 * @param exception
	 * @param response
	 * @return
	 */
	@ExceptionHandler(CustomCheckedException.class)
	public ModelAndView handleSalesForceException(final CustomCheckedException exception, final HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		response.setHeader(ConstantStrings.CONTENT_TYPE, ConstantStrings.APPLICATION_JSON);
		final CustomBusinessError custBusinessErr = exception.getCustomBusinessError();
		custBusinessErr.setErrorMessage(getMessageSource().getMessage(custBusinessErr.getErrorKey(), custBusinessErr.getArguments(), Constants.NO_KEY_DEFINED, null));
		final ModelAndView view = new ModelAndView();
		view.addObject(ERROR_OBJECT, custBusinessErr);
		view.setViewName(ERROR_OBJECT);
		LOGGER.error("CustomCheckedException occured - ", exception);
		return view;
	}

	/**
	 * Handles StaleObjectStateException for Optimistic locking
	 * @param exception
	 * @param response
	 * @return
	 */
	@ExceptionHandler(HibernateOptimisticLockingFailureException.class)
	public ModelAndView handleStaleObjectException(final HibernateOptimisticLockingFailureException exception, final HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		response.setHeader(ConstantStrings.CONTENT_TYPE, ConstantStrings.APPLICATION_JSON);
		final CustomBusinessError customBusinessError = new CustomBusinessError();
		customBusinessError.setMessageType(ErrorMessageType.ERROR.name());
		customBusinessError.setErrorKey(ErrorCodes.staleObjectStateExceptionMessage.getResourceName());
		customBusinessError.setErrorMessage(getMessageSource().getMessage(customBusinessError.getErrorKey(), customBusinessError.getArguments(), Constants.NO_KEY_DEFINED, null));
		final ModelAndView view = new ModelAndView();
		view.addObject(ERROR_OBJECT, customBusinessError);
		view.setViewName(ERROR_OBJECT);
		LOGGER.info("StaleObjectState Exception occured for entity: "
				+ exception.getPersistentClassName() + " with Id: " + exception.getIdentifier());
		return view;
	}

	/**
	 * Handles ProposalAccessException for UI
	 * @param exception
	 * @param response
	 * @return
	 */
	@ExceptionHandler(ProposalAccessException.class)
	public ModelAndView handleProposalAccessException(final ProposalAccessException exception, final HttpServletResponse response) {
		return getErrorObject(exception, response);
	}

	/**
	 * Handles IntegrityValidatorException for UI
	 * @param exception
	 * @param response
	 * @return
	 */
	@ExceptionHandler(IntegrityValidatorException.class)
	public ModelAndView handleIntegrityValidatorException(final IntegrityValidatorException exception, final HttpServletResponse response) {
		return getErrorObject(exception, response);
	}

	/**
	 * Handles SOS Violation
	 * @param exception
	 * @param response
	 * @return
	 */
	@ExceptionHandler(SOSViolationException.class)
	public ModelAndView handleSOSViolationException(final SOSViolationException exception, final HttpServletResponse response) {
		return getErrorObjects(exception, response);
	}

	/**
	 * Handles Yield-ex Avails Exception
	 * @param exception
	 * @param response
	 * @return
	 */
	@ExceptionHandler(YieldexAvailsException.class)
	public ModelAndView handleYieldexAvailsException(final YieldexAvailsException exception, final HttpServletResponse response) {
		return getErrorObject(exception, response);
	}

	/**
	 * Handle Template Generation Exception
	 * @param exception
	 * @param response
	 */
	@ExceptionHandler(TemplateGenerationException.class)
	public void handleTemplateGenerationException(final TemplateGenerationException exception, final HttpServletResponse response) {
		LOGGER.error("Error in generating media plan", exception);
		try {
			final HSSFWorkbook templateWorkbook = new HSSFWorkbook();
			final HSSFSheet templateSheet = templateWorkbook.createSheet(Constants.ERROR_SHEETNAME);
			final HSSFRow templateRow = templateSheet.createRow(6);
			final HSSFCell templateCell = templateRow.createCell(6);
			// Setting business Error
			final CustomBusinessError custBusinessErr = exception.getCustomBusinessError();
			templateCell.setCellValue(getMessageSource().getMessage(custBusinessErr.getErrorKey(), custBusinessErr.getArguments(), Constants.NO_KEY_DEFINED, null));

			response.setContentType(new MimetypesFileTypeMap().getContentType(Constants.ERROR_FILENAME));
			response.setHeader("Content-Disposition", "attachment; filename=\"" + Constants.ERROR_FILENAME + "\"");
			final OutputStream responseStream = response.getOutputStream();
			templateWorkbook.write(responseStream);
			responseStream.close();
		} catch (IOException e) {
			LOGGER.error("Error in Generating Error.xls", e);
		}
	}
	
	/**
	 * Handles Exception generated while sending a mail
	 * @param exception
	 * @param response
	 * @return
	 */
	@ExceptionHandler(MailException.class)
	public ModelAndView handleMailException(final MailException exception, final HttpServletResponse response){
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		response.setHeader(ConstantStrings.CONTENT_TYPE, ConstantStrings.APPLICATION_JSON);
		final CustomBusinessError custBusinessErr = new CustomBusinessError();
		custBusinessErr.setMessageType(ErrorMessageType.ERROR.name());
		custBusinessErr.setErrorKey(ErrorCodes.runtimeExceptionMessage.getResourceName());
		custBusinessErr.setErrorMessage(getMessageSource().getMessage(custBusinessErr.getErrorKey(), custBusinessErr.getArguments(), Constants.NO_KEY_DEFINED, null));
		final ModelAndView view = new ModelAndView();
		view.addObject(ERROR_OBJECT, custBusinessErr);
		view.setViewName(ERROR_OBJECT);
		LOGGER.error("Email exception occured - ", exception);
		return view;
		
	}

	/**
	 * @param exception
	 * @param response
	 * @return
	 */
	private ModelAndView getErrorObjects(final BusinessException exception, final HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		response.setHeader(ConstantStrings.CONTENT_TYPE, ConstantStrings.APPLICATION_JSON);
		final List<CustomBusinessError> custBusinessErrs = exception.getCustomBusinessErrors();
		final StringBuffer errorMessages = new StringBuffer();
		for (CustomBusinessError customBusinessError : custBusinessErrs) {
			errorMessages.append(getMessageSource().getMessage(customBusinessError.getErrorKey(), customBusinessError.getArguments(), Constants.NO_KEY_DEFINED, null)).append("\n");
			LOGGER.warn(customBusinessError.toString());
		}
		final CustomBusinessError custBusinessErr = new CustomBusinessError();
		custBusinessErr.setErrorMessage(errorMessages.toString());
		custBusinessErr.setMessageType(custBusinessErrs.get(0).getMessageType());
		final ModelAndView view = new ModelAndView();
		view.addObject(ERROR_OBJECT, custBusinessErr);
		view.setViewName(ERROR_OBJECT);
		return view;
	}

	/**
	 * @param exception
	 * @param response
	 * @return
	 */
	private ModelAndView getErrorObject(final BusinessException exception, final HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		response.setHeader(ConstantStrings.CONTENT_TYPE, ConstantStrings.APPLICATION_JSON);
		final CustomBusinessError custBusinessErr = exception.getCustomBusinessError();
		custBusinessErr.setErrorMessage(getMessageSource().getMessage(custBusinessErr.getErrorKey(), custBusinessErr.getArguments(), Constants.NO_KEY_DEFINED, null));
		final ModelAndView view = new ModelAndView();
		view.addObject(ERROR_OBJECT, custBusinessErr);
		view.setViewName(ERROR_OBJECT);
		LOGGER.warn(exception.toString());
		return view;
	}

	/**
	 * Returns {@link AjaxFormSubmitResponse}
	 * @param response
	 * @param ajaxSubmitResp
	 * @param results
	 * @return
	 */
	public AjaxFormSubmitResponse constructResponse(final HttpServletResponse response, final AjaxFormSubmitResponse ajaxSubmitResp, final BindingResult results) {
		if (!results.getAllErrors().isEmpty()) {
			ajaxSubmitResp.setErrorList(results.getAllErrors());
			sucess = false;
		}
		return ajaxSubmitResp;
	}

	/**
	 * Returns {@link XMLResponseForAjaxCall}
	 * @param response
	 * @param ajaxSubmitResp
	 * @param results
	 * @return
	 */
	public XMLResponseForAjaxCall constructResponse(final HttpServletResponse response, final XMLResponseForAjaxCall ajaxSubmitResp, final BindingResult results) {
		if (!results.getAllErrors().isEmpty()) {
			ajaxSubmitResp.setErrorList(results.getAllErrors());
		}
		return ajaxSubmitResp;
	}

	/**
	 * Returns {@link AjaxFormSubmitResponse}
	 * @param response
	 * @param ajxFormSubmtResp
	 * @param exception
	 * @return
	 */
	public AjaxFormSubmitResponse constructResponse(final HttpServletResponse response, final AjaxFormSubmitResponse ajxFormSubmtResp, final Exception exception) {
		ajxFormSubmtResp.getObjectMap().put("Exception", exception);
		return ajxFormSubmtResp;
	}

	/**
	 * Returns {@link XMLResponseForAjaxCall}
	 * @param response
	 * @param ajxFormSubmtResp
	 * @param exception
	 * @return
	 */
	public XMLResponseForAjaxCall constructResponse(final HttpServletResponse response, final XMLResponseForAjaxCall ajxFormSubmtResp, final Exception exception) {
		return ajxFormSubmtResp;
	}

	@JsonIgnore
	public Validator getValidator() {
		return validator;
	}

	public void setValidator(final Validator validator) {
		this.validator = validator;
	}

	public boolean isSucess() {
		return sucess;
	}

	public void setSucess(final boolean sucess) {
		this.sucess = sucess;
	}

	@JsonIgnore
	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(final MessageSource messageSource) {
		this.messageSource = messageSource;
	}
}
