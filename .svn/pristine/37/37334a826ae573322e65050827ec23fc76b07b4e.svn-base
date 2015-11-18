/**
 * 
 */
package com.nyt.mpt.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.nyt.mpt.domain.Summary;
import com.nyt.mpt.form.DFPLineItemPojo;
import com.nyt.mpt.util.enums.ErrorCodes;
import com.nyt.mpt.util.enums.ErrorMessageType;
import com.nyt.mpt.util.exception.DFPWrapperExcepion;

/**
 * @author Gurditta.Garg
 * 
 */
public class DfpGateway {

	private static final Logger LOGGER = Logger.getLogger(DfpGateway.class);
	private String dfpURL;
	private String dfpUsername;
	private String dfpPassword;
	/**
	 * @param dfpLineItemPojo
	 * @return
	 */
	public Summary getInventoryForeCast(DFPLineItemPojo dfpLineItemPojo) {
		String url = dfpURL + "forecast/getForecast.action?j_username="+dfpUsername+"&j_password="+dfpPassword;
		String jsonString = ConstantStrings.EMPTY_STRING;
		final HttpPost httpUriRequest = new HttpPost(url);
		httpUriRequest.setHeader("Accept", "application/json");
		httpUriRequest.setHeader("Content-Type", "application/json");
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			jsonString = objectMapper.writeValueAsString(dfpLineItemPojo);
			LOGGER.info("Input string for DFP - " + jsonString);
		} catch (JsonGenerationException e) {
			LOGGER.error("Error during Parsing JSON - " + e);
		} catch (JsonMappingException e) {
			LOGGER.error("Error during Mapping JSON - " + e);
		} catch (IOException e) {
			LOGGER.error("Error during Converting JSON to DFPLineItemPojo - " + e);
		}
		HttpEntity entity;
		try {
			entity = new ByteArrayEntity(jsonString.getBytes("UTF-8"));
			httpUriRequest.setEntity(entity);
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("Error during Converting toByteArray - " + e);
		}
		return runHttpClient(httpUriRequest);
	}
	
	/**
	 * @param httpUriRequest
	 * @return
	 */
	private Summary runHttpClient(HttpUriRequest httpUriRequest) {
		String result = ConstantStrings.EMPTY_STRING;
		HttpClient httpClient = new DefaultHttpClient();
		BasicResponseHandler responseHandler = new BasicResponseHandler();
		HttpResponse response = null;
		try {
			response = httpClient.execute(httpUriRequest);
			LOGGER.info("Response Status - " + response.getStatusLine().toString());
			result = responseHandler.handleResponse(response);
			LOGGER.info("Response Data - " + result);
		} catch (Exception exception) {
			LOGGER.error("Error occured while fetching forecasting data from DFP - " + exception.getMessage());
			if(response != null) {
				generateDFPException(response);
			} else {
				throw new DFPWrapperExcepion(DFPWrapperExcepion.getCustomeBusinessError(ErrorCodes.dfpWrapperErrorMessage, ErrorMessageType.DFP_WRAPPER_EXCEPTION, null));
			}
		}
		return StringUtils.isBlank(result) ? null : convertJsonToObject(result);
	}

	/**
	 * @param response
	 */
	private void generateDFPException(final HttpResponse response) {
		try {
			HttpEntity entity = response.getEntity();
			final ObjectMapper mapper = new ObjectMapper();
			String responseString = EntityUtils.toString(entity, "UTF-8");
			JsonNode node = mapper.readTree(responseString);
			StringBuffer messageArgument = new StringBuffer();
			if (node != null) {
				if (node.findValue("errors") != null && node.findValue("warnings") != null && node.findValue("code") != null) {
					messageArgument.append("Response Type").append(ConstantStrings.OPENING_BRACKET).append(node.findValue("code").getValueAsText()).append(ConstantStrings.CLOSING_BRACKET)
					.append(ConstantStrings.SPACE).append(ConstantStrings.HYPHEN).append(ConstantStrings.SPACE);
					for (JsonNode jsonNode : node.findValue("errors")) {
						messageArgument.append(jsonNode.getValueAsText());
					}
					for (JsonNode jsonNode : node.findValue("warnings")) {
						messageArgument.append(jsonNode.getValueAsText());
					}
				}
			}
			final String[] messageArguments = { messageArgument.toString() };
			throw new DFPWrapperExcepion(DFPWrapperExcepion.getCustomeBusinessError(ErrorCodes.dfpWrapperErrorMessage, ErrorMessageType.DFP_WRAPPER_EXCEPTION, messageArguments));
		} catch (ParseException exception) {
			LOGGER.error("ParseException while parsing DFP Wrapper response");
			throw new DFPWrapperExcepion(DFPWrapperExcepion.getCustomeBusinessError(ErrorCodes.dfpWrapperErrorMessage, ErrorMessageType.DFP_WRAPPER_EXCEPTION, null));
		} catch (IOException exception) {
			LOGGER.error("IOException while parsing DFP Wrapper response");
			throw new DFPWrapperExcepion(DFPWrapperExcepion.getCustomeBusinessError(ErrorCodes.dfpWrapperErrorMessage, ErrorMessageType.DFP_WRAPPER_EXCEPTION, null));
		}
	}
	
	/**
	 * Coverts the jsonString to Summary Object
	 * @param jsonString
	 * @return
	 */
	private Summary convertJsonToObject(final String jsonString) {		
		final ObjectMapper mapper = new ObjectMapper();
		final Summary summary = new Summary();
		summary.setExceededForecastWindow("true"); // hard-coded as a default value is required 
		if(StringUtils.isNotBlank(jsonString)) {
			JsonNode node = null;
			try {
				node = mapper.readTree(jsonString);
			} catch (JsonProcessingException e) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Error while parsing Targeting of a LineItem");
				}
			} catch (IOException e) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Error while parsing Targeting of a LineItem");
				}
			}
			if (node != null) {
				if (node.findValue("availableUnits") != null && node.findValue("matchedUnits") != null) {
					summary.setAvailable(node.findValue("availableUnits").getValueAsText());
					summary.setCapacity(node.findValue("matchedUnits").getValueAsText());
					summary.setInputJson(node.findValue("forecastInputVO").toString());
				}
			}
		}
		return summary;
	}
	
	public void setDfpURL(String dfpURL) {
		this.dfpURL = dfpURL;
	}

	public void setDfpUsername(String dfpUsername) {
		this.dfpUsername = dfpUsername;
	}

	public void setDfpPassword(String dfpPassword) {
		this.dfpPassword = dfpPassword;
	}
}
