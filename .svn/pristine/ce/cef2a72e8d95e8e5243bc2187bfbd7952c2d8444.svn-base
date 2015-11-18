/**
 *
 */
package com.nyt.mpt.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Source;

import org.apache.log4j.Logger;
import org.springframework.web.client.RestTemplate;
import org.springframework.xml.xpath.Jaxp13XPathTemplate;
import org.springframework.xml.xpath.NodeMapper;
import org.springframework.xml.xpath.XPathOperations;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.nyt.mpt.domain.DailyDetail;
import com.nyt.mpt.domain.InventoryDetail;
import com.nyt.mpt.domain.Summary;
import com.nyt.mpt.service.IYieldexService;
import com.nyt.mpt.util.enums.ErrorCodes;
import com.nyt.mpt.util.enums.ErrorMessageType;
import com.nyt.mpt.util.exception.YieldexAvailsException;

/**
 * Used to fetch avails from Yield-ex
 * 
 * @author manish.kesarwani
 *
 */
public class YieldexService implements IYieldexService {

	private RestTemplate restTemplate;
	
	private static final Logger LOGGER = Logger.getLogger(YieldexService.class);
	
	private XPathOperations xpath = new Jaxp13XPathTemplate();
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IYieldexService#getInventoryDetail(java.lang.String)
	 */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public InventoryDetail getInventoryDetail(String url) {
		InventoryDetail inventoryDetail = new InventoryDetail();
		try {
			final Source source = restTemplate.getForObject(url, Source.class);
			List<Summary> summaryList = xpath.evaluate("//summary", source, new NodeMapper() {
				
				/* (non-Javadoc)
				 * @see org.springframework.xml.xpath.NodeMapper#mapNode(org.w3c.dom.Node, int)
				 */
				public Object mapNode(Node node, int i) throws DOMException {
					final Element summaryElement = (Element) node;
					final Summary summary = new Summary();
					final List<String> mandotryFiledList = getMandatoryFieldsFromYieldEx();
					if (summaryElement.hasChildNodes()) {
						final NodeList nodeList = summaryElement.getChildNodes();
						for (int index = 0; index < nodeList.getLength(); index++) {
							final Node childNode = nodeList.item(index);
							if (childNode.getNodeName().equalsIgnoreCase("available")) {
								summary.setAvailable(childNode.getTextContent());
								mandotryFiledList.remove(childNode.getNodeName());
							} else if (childNode.getNodeName().equalsIgnoreCase("capacity")) {
								summary.setCapacity(childNode.getTextContent());
								mandotryFiledList.remove(childNode.getNodeName());
							} else if (childNode.getNodeName().equalsIgnoreCase("startDate")) {
								summary.setStartDate(childNode.getTextContent());
								mandotryFiledList.remove(childNode.getNodeName());
							} else if (childNode.getNodeName().equalsIgnoreCase("endDate")) {
								summary.setEndDate(childNode.getTextContent());
								mandotryFiledList.remove(childNode.getNodeName());
							} else if (childNode.getNodeName().equalsIgnoreCase("unmetDemand")) {
								summary.setUnmetDemand(childNode.getTextContent());
								mandotryFiledList.remove(childNode.getNodeName());
							} else if (childNode.getNodeName().equalsIgnoreCase("exceededForecastWindow")) {
								summary.setExceededForecastWindow(childNode.getTextContent());
								mandotryFiledList.remove(childNode.getNodeName());
							}
						}
					}
					if (mandotryFiledList.size() > 0) {
						StringBuffer mandatroyAttributesBuffer = new StringBuffer();
						for (String mandatoryField : mandotryFiledList) {
							if (mandatroyAttributesBuffer != null && mandatroyAttributesBuffer.length() > 0) {
								mandatroyAttributesBuffer.append(",");
							}
							mandatroyAttributesBuffer.append(mandatoryField);
						}
						final String[] messageArguments = { mandatroyAttributesBuffer.toString() };
						LOGGER.error("Mandatory attribute missing from yieldex - " + mandotryFiledList);
						throw new YieldexAvailsException(YieldexAvailsException.getCustomeBusinessError(ErrorCodes.availsMandatroyAttributeMissing,
								ErrorMessageType.YIELDEX_AVAILS_ERROR, messageArguments));
					}
					return summary;
				}
			});
			inventoryDetail.setSummary(summaryList.get(0));
			if (inventoryDetail != null && inventoryDetail.getSummary() != null) {
				LOGGER.info("Avails Information from Yieldex - " + inventoryDetail.getSummary().toString());
			}
		} catch (Exception e) {
			LOGGER.error("Error occured while fetching avails from yieldex - " + e.getMessage());
			throw new YieldexAvailsException(YieldexAvailsException.getCustomeBusinessError(ErrorCodes.availsNotAvailable,
					ErrorMessageType.YIELDEX_AVAILS_ERROR, new String[] {}));
		}
		return inventoryDetail;
	}
	
	private List<String> getMandatoryFieldsFromYieldEx() {
		List<String> mandotryFiledList = new ArrayList<String>();
		mandotryFiledList.add("available");
		mandotryFiledList.add("capacity");
		mandotryFiledList.add("startDate");
		mandotryFiledList.add("endDate");
		mandotryFiledList.add("unmetDemand");
		mandotryFiledList.add("exceededForecastWindow");
		return mandotryFiledList;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IYieldexService#getInventoryDetail(java.lang.String)
	 */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public InventoryDetail getInvWithDailyDetails(String url) {
		InventoryDetail inventoryDetail = new InventoryDetail();
		try {
			final Source source = restTemplate.getForObject(url, Source.class);
			
			List<DailyDetail> dailyDetailList = xpath.evaluate("//dailyDetail", source, new NodeMapper() {
				
				/* (non-Javadoc)
				 * @see org.springframework.xml.xpath.NodeMapper#mapNode(org.w3c.dom.Node, int)
				 */
				public Object mapNode(Node node, int i) throws DOMException {
					final Element dailyDetailElement = (Element) node;
					final DailyDetail dailyDetail = new DailyDetail();
					final List<String> mandotryFiledList = getMandatoryFieldsFromYieldEx();
					if (dailyDetailElement.hasChildNodes()) {
						final NodeList nodeList = dailyDetailElement.getChildNodes();
						for (int index = 0; index < nodeList.getLength(); index++) {
							final Node childNode = nodeList.item(index);
							if (childNode.getNodeName().equalsIgnoreCase("available")) {
								dailyDetail.setAvailable(childNode.getTextContent());
								mandotryFiledList.remove(childNode.getNodeName());
							} else if (childNode.getNodeName().equalsIgnoreCase("capacity")) {
								dailyDetail.setCapacity(childNode.getTextContent());
								mandotryFiledList.remove(childNode.getNodeName());
							} else if (childNode.getNodeName().equalsIgnoreCase("startDate")) {
								dailyDetail.setStartDate(childNode.getTextContent());
								mandotryFiledList.remove(childNode.getNodeName());
							} else if (childNode.getNodeName().equalsIgnoreCase("endDate")) {
								dailyDetail.setEndDate(childNode.getTextContent());
								mandotryFiledList.remove(childNode.getNodeName());
							} else if (childNode.getNodeName().equalsIgnoreCase("unmetDemand")) {
								dailyDetail.setUnmetDemand(childNode.getTextContent());
								mandotryFiledList.remove(childNode.getNodeName());
							} else if (childNode.getNodeName().equalsIgnoreCase("exceededForecastWindow")) {
								dailyDetail.setExceededForecastWindow(childNode.getTextContent());
								mandotryFiledList.remove(childNode.getNodeName());
							}
						}
					}
					if (mandotryFiledList.size() > 0) {
						StringBuffer mandatroyAttributesBuffer = new StringBuffer();
						for (String mandatoryField : mandotryFiledList) {
							if (mandatroyAttributesBuffer != null && mandatroyAttributesBuffer.length() > 0) {
								mandatroyAttributesBuffer.append(",");
							}
							mandatroyAttributesBuffer.append(mandatoryField);
						}
						final String[] messageArguments = { mandatroyAttributesBuffer.toString() };
						LOGGER.error("Mandatory attribute missing from yieldex - " + mandotryFiledList);
						throw new YieldexAvailsException(YieldexAvailsException.getCustomeBusinessError(ErrorCodes.availsMandatroyAttributeMissing,
								ErrorMessageType.YIELDEX_AVAILS_ERROR, messageArguments));
					}
					return dailyDetail;
				}
			});
			
			//Setting Daily details information
			inventoryDetail.setDailyDetail(dailyDetailList);
			
			if (inventoryDetail != null && inventoryDetail.getSummary() != null) {
				LOGGER.info("Avails Information from Yieldex - " + inventoryDetail.getSummary().toString());
			}
		} catch (Exception e) {
			LOGGER.error("Error occured while fetching avails from yieldex - " + e.getMessage());
			throw new YieldexAvailsException(YieldexAvailsException.getCustomeBusinessError(ErrorCodes.availsNotAvailable,
					ErrorMessageType.YIELDEX_AVAILS_ERROR, new String[] {}));
		}
		return inventoryDetail;
	}

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
}
