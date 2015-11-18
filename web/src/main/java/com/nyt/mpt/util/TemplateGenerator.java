/**
 * 
 */
package com.nyt.mpt.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.AccessController;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.FileCopyUtils;

import sun.security.action.GetPropertyAction;

import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.ProductCreativeAssoc;
import com.nyt.mpt.domain.ProposalVersion;
import com.nyt.mpt.domain.TemplateMetaData;
import com.nyt.mpt.domain.TemplateSheetMetaData;
import com.nyt.mpt.form.TemplateManagementForm;
import com.nyt.mpt.service.IProductService;
import com.nyt.mpt.service.IProposalService;
import com.nyt.mpt.service.ITemplateService;
import com.nyt.mpt.template.TemplateAttributeVO;
import com.nyt.mpt.template.TemplateSheetVO;
import com.nyt.mpt.template.TemplateVO;
import com.nyt.mpt.template.TemplateValueFormateType;
import com.nyt.mpt.util.enums.ErrorCodes;
import com.nyt.mpt.util.enums.ErrorMessageType;
import com.nyt.mpt.util.exception.SOSViolationException;
import com.nyt.mpt.util.exception.TemplateGenerationException;

/**
 * This class is used for generate template object
 * @author manish.kesarwani
 */
public class TemplateGenerator {

	private static final String N_A = "N/A";

	private static final Logger LOGGER = Logger.getLogger(TemplateGenerator.class);

	private IProposalService proposalService;
	private ITemplateService templateService;
	private String templatesSource;
	private IProductService productService;
	private static final String FILE_SEPARATOR = System.getProperty("file.separator");

	/**
	 * This method is used to generate template object
	 * 
	 * @param optionId
	 * @param proposalVersionId
	 * @param templateId
	 * @return
	 * @throws IOException
	 */
	public TemplateWorkBook generateTemplate(final long optionId, final long proposalVersionId, final long templateId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Generating template. ProposalId:" + optionId + " proposalVersionId:" + proposalVersionId + " templateId:" + templateId);
		}
		// Get the proposal Version from Database
		final ProposalVersion proposalVersion = proposalService.getproposalVersions(optionId, proposalVersionId).get(0);
		final TemplateVO mediaTemplateVO = getExportTemplateVO(optionId, proposalVersionId, templateId);
		// Generating Template
		final TemplateWorkBook templateWorkBook = generateTemplateWorkBook(mediaTemplateVO, templateId, proposalVersion);

		// Evaluating formulas in Excel sheet
		LOGGER.info("Evaluating formuals in excel sheet");
		final Workbook generatedWorkbook = templateWorkBook.getTemplateWorkBook();
		final FormulaEvaluator evaluator = generatedWorkbook.getCreationHelper().createFormulaEvaluator();
		for (int sheetNum = 0; sheetNum < generatedWorkbook.getNumberOfSheets(); sheetNum++) {
			final Sheet sheet = generatedWorkbook.getSheetAt(sheetNum);
			for (Row r : sheet) {
				for (Cell c : r) {
					if (c.getCellType() == Cell.CELL_TYPE_FORMULA) {
						evaluator.evaluateFormulaCell(c);
					}
				}
			}
		}

		return templateWorkBook;
	}

	/**
	 * This method is used to generate proposal creative specification
	 * @param optionID
	 * @param proposalVersionId
	 * @param templateId
	 * @return
	 * @throws IOException
	 */
	public TemplateWorkBook generateCreativeSpec(final long optionID, final long proposalVersionId, final long templateId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Generating Proposal Creative Spec. ProposalId:" + optionID + " proposalVersionId:" + proposalVersionId + " templateId:" + templateId);
		}
		// Get the proposal Version from Database
		final ProposalVersion proposalVersion = proposalService.getproposalVersions(optionID, proposalVersionId).get(0);
		// Generating creative spec template
		final TemplateVO mediaTemplateVO = getCreativeTemplateVO(optionID, proposalVersionId, templateId);
		final TemplateWorkBook templateWorkBook = generateCreativeWorkBook(mediaTemplateVO, templateId, proposalVersion);

		return templateWorkBook;
	}

	/**
	 * This method is used to validate template object
	 * @param optionID
	 * @param proposalVersionId
	 * @param templateId
	 * @return
	 */
	public TemplateVO getExportTemplateVO(final long optionID, final long proposalVersionId, final long templateId) {
		// Get the proposal Version from Database
		final List<ProposalVersion> propoVersionLst = proposalService.getproposalVersions(optionID, proposalVersionId);
		final ProposalVersion proposalVersion = propoVersionLst.get(0);
		// Generating media template basic structure, which contains proposal data along with line item header
		TemplateVO mediaTemplateVO = getMediaTemplateBasicStructure(optionID, proposalVersion, templateId);
		// Populate Media Template Object
		mediaTemplateVO = populateMediaTemplate(mediaTemplateVO, proposalVersion);
		return mediaTemplateVO;
	}

	/**
	 * This method is used to get Template VO Object for creative
	 * @param optionID
	 * @param proposalVersionId
	 * @param templateId
	 * @param validate
	 * @return
	 */
	public TemplateVO getCreativeTemplateVO(final long optionID, final long proposalVersionId, final long templateId) {
		// Get the proposal Version from Database
		final ProposalVersion proposalVersion = proposalService.getproposalVersions(optionID, proposalVersionId).get(0);
		// Generating media template basic structure, which contains proposal data along with line item header
		TemplateVO mediaTemplateVO = templateService.generateCreativeSpecObject(optionID, proposalVersion, templateId);
		// Populate Media Template Object
		mediaTemplateVO = populateCreativeSpec(mediaTemplateVO, proposalVersion);
		return mediaTemplateVO;
	}

	/**
	 * This method is used to generate Media Template Basic structure which contains Proposal Head
	 * @param optionID
	 * @param templateId
	 * @return
	 * @throws IOException
	 */
	private TemplateVO getMediaTemplateBasicStructure(final long optionID, final ProposalVersion proposalVersion, final long templateId) {
		return templateService.generateMediaTemplateObject(optionID, proposalVersion, templateId);
	}

	/**
	 * Use to populate line item data in MediaPlanTemplateVO object
	 * @param mediaTemplateVO
	 * @param proposalVersion
	 * @return TemplateVO
	 */
	private TemplateVO populateMediaTemplate(TemplateVO mediaTemplateVO, final ProposalVersion proposalVersion) {
		final SortingCriteria sortingCriteria = new SortingCriteria("lineItemSequence", "asc");
		final List<LineItem> propLineItemLst = proposalService.getProposalLineItems(proposalVersion.getProposalOption().getId(), proposalVersion.getProposalVersion(), null, null, sortingCriteria);
		int counter = -1;
		for (LineItem lineItem : propLineItemLst) {
			counter = counter + 1;
			mediaTemplateVO = templateService.populateLineItemAttributesList(mediaTemplateVO, lineItem, counter);
		}

		if (!mediaTemplateVO.getErrorMessages().isEmpty()) {
			LOGGER.error("SOS Violation Error Occured");
			throw new SOSViolationException(SOSViolationException.getCustomeBusinessErrors(mediaTemplateVO.getErrorMessages()));
		}
		return mediaTemplateVO;
	}

	/**
	 * Use to populate creative specification
	 * @param mediaTemplateVO
	 * @param proposalVersion
	 * @return
	 */
	private TemplateVO populateCreativeSpec(TemplateVO mediaTemplateVO, final ProposalVersion proposalVersion) {
		final List<LineItem> propLineItemLst = proposalService.getProposalLineItems(proposalVersion.getProposalOption().getId(), proposalVersion.getProposalVersion(), null, null, null);
		int counter = 0;
		for (LineItem lineItem : propLineItemLst) {
			final List<ProductCreativeAssoc> creativeAssocList = productService.getProductCreativesForTemplates(lineItem);
			for (ProductCreativeAssoc productCreativeAssoc : creativeAssocList) {
				counter = counter + 1;
				mediaTemplateVO = templateService.populateCreativeSpecList(mediaTemplateVO, lineItem, counter, productCreativeAssoc);
			}
			if (!creativeAssocList.isEmpty()) {
				counter = counter + 1;
			}
		}
		if (!mediaTemplateVO.getErrorMessages().isEmpty()) {
			LOGGER.error("SOS Violation Error Occured");
			throw new SOSViolationException(SOSViolationException.getCustomeBusinessErrors(mediaTemplateVO.getErrorMessages()));
		}
		return mediaTemplateVO;
	}

	/**
	 * 
	 * @param templateName
	 * @param templateStream
	 * @return
	 * @throws IOException
	 */
	private Workbook getWorkBook(final String templateName, final FileInputStream templateStream) throws IOException {
		if ("XLS".equalsIgnoreCase(templateName.substring(templateName.lastIndexOf('.') + 1))) {
			return new HSSFWorkbook(templateStream);
		} else {
			return new XSSFWorkbook(templateStream);
		}
	}

	/**
	 * Use to format a cell data in generated media template
	 * @param attributeVo
	 * @param book
	 * @param forlineitem
	 * @return
	 */
	private CellStyle getCellStyle(final TemplateAttributeVO attributeVo, final Workbook book, final boolean forlineitem) {
		// Style the cell with borders all around.
		final CellStyle style = book.createCellStyle();
		// Setting cell style for line item column
		if (forlineitem) {
			style.setBorderBottom(CellStyle.BORDER_THIN);
			style.setBorderLeft(CellStyle.BORDER_THIN);
			style.setBorderRight(CellStyle.BORDER_THIN);
			style.setBorderTop(CellStyle.BORDER_THIN);
		}
		// Setting Cell font size
		if (attributeVo.getFontSize() != null && attributeVo.getFontSize() < 255) {
			final Font font = book.createFont();
			font.setFontHeightInPoints(attributeVo.getFontSize().shortValue());
			if (attributeVo.getFontWeight() != null) {
				font.setBoldweight(attributeVo.getFontWeight().shortValue());
				style.setFillPattern(HSSFCellStyle.FINE_DOTS);
				style.setFillBackgroundColor(HSSFColor.GREY_50_PERCENT.index);
			}
			style.setFont(font);
		}
		style.setWrapText(true);
		CreationHelper createHelper = null;
		if (attributeVo.getFormat() != null && attributeVo.getFormat().length() > 0) {
			createHelper = book.getCreationHelper();
		}

		final TemplateValueFormateType formateType = TemplateValueFormateType.findByName(attributeVo.getFormat());
		style.setDataFormat(createHelper.createDataFormat().getFormat(formateType.getFormat()));
		return style;
	}

	/**
	 * Use to format cell data in Number format in generated media template
	 * @param mediaPlanAttributeVo
	 * @return
	 */
	private Double getNumericCellValue(final TemplateAttributeVO mediaPlanAttributeVo) {
		Double cellValue = 0.0;
		if (mediaPlanAttributeVo.getAttributeValue() != null && mediaPlanAttributeVo.getAttributeValue().length() > 0) {
			cellValue = new Double(mediaPlanAttributeVo.getAttributeValue());
		}
		return cellValue;

	}

	/**
	 * Use to format cell data in Date format in generated media template
	 * @param mediaPlanAttributeVo
	 * @return
	 */
	private Date getDateCellValue(final TemplateAttributeVO mediaPlanAttributeVo) {
		try {
			if (mediaPlanAttributeVo.getAttributeValue() != null && mediaPlanAttributeVo.getAttributeValue().length() > 0) {
				return DateUtils.parseDate(mediaPlanAttributeVo.getAttributeValue(), new String[] { "yyyy-MM-dd hh:mm:ss.s" });
			}

		} catch (ParseException e) {
			LOGGER.error("ParseException", e);
		}
		return null;
	}

	/**
	 * Method is used to generate excel based on Row and Columns
	 * @param mediaTemplateVO
	 * @param templateId
	 * @return
	 * @throws IOException
	 */
	private TemplateWorkBook generateTemplateWorkBook(final TemplateVO mediaTemplateVO, final long templateId, final ProposalVersion proposalVersion) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Generating MediaTemplate for TemplateVO:" + mediaTemplateVO + " templateId:" + templateId);
		}
		final TemplateWorkBook templateWorkBook = new TemplateWorkBook();

		Workbook book = null;
		FileInputStream templateInputStream = null;
		String outputFileName = null;

		try {
			final TemplateMetaData mediaTemplate = templateService.getActiveMediaPlanTemplateById(templateId);
			// Setting template file Name
			final String templateName = mediaTemplate.getTemplateName();
			templateWorkBook.setTemplateFileName(templateName);

			// Locating to actual file
			final String templateFileName = templatesSource + FILE_SEPARATOR + templateName;

			// calculating output file name
			outputFileName = proposalVersion.getProposalOption().getProposal().getName() + templateName.substring(templateName.indexOf('.'));
			templateWorkBook.setTemplateOutputFileName(outputFileName);

			templateInputStream = new FileInputStream(templateFileName);
			book = getWorkBook(templateName, templateInputStream);
			book = removeTemplateCellTokens(book);// Remove token from template if exist
			LOGGER.info("Populating proposal and line item data into media template");
			final List<TemplateSheetMetaData> sheetMetaDataList = mediaTemplate.getTemplateSheetList();
			for (int i = 0; i < sheetMetaDataList.size(); i++) {
				final TemplateSheetMetaData sheetMetaData = sheetMetaDataList.get(i);
				final Sheet templateSheet = book.getSheet(sheetMetaData.getSheetName());

				final TemplateSheetVO templateSheetVO = mediaTemplateVO.getTemplateSheetMap().get(sheetMetaData.getSheetName());
				final List<TemplateAttributeVO> attributeList = templateSheetVO.getMediaPlanProposalAttributesList();
				LOGGER.info("Populating proposal data into media template for sheet sheet Name: " + templateSheet.getSheetName());
				for (int j = 0; j < attributeList.size(); j++) {
					final TemplateAttributeVO mediaAttributeVo = attributeList.get(j);
					final Row mediaTemplateRow = templateSheet.getRow(mediaAttributeVo.getRowNum());
					final Cell templateCell = mediaTemplateRow.getCell(mediaAttributeVo.getColNum(), Row.CREATE_NULL_AS_BLANK);
					// Setting NA if attribute value is not defined
					if (null == mediaAttributeVo.getAttributeValue() || mediaAttributeVo.getAttributeValue().length() == 0) {
						mediaAttributeVo.setFormat(TemplateValueFormateType.TEXT.name());
						mediaAttributeVo.setAttributeValue(N_A);
					}

					templateCell.setCellStyle(getCellStyle(mediaAttributeVo, book, false));

					final TemplateValueFormateType formateType = TemplateValueFormateType.findByName(mediaAttributeVo.getFormat());
					if (formateType.isNumericType()) {
						templateCell.setCellValue(getNumericCellValue(mediaAttributeVo));
					} else if (formateType.isDateType()) {
						final Date date = getDateCellValue(mediaAttributeVo);
						if (date != null) {
							templateCell.setCellValue(date);
						}

					} else {
						templateCell.setCellValue(mediaAttributeVo.getAttributeValue());
					}
				}

				final TreeMap<Long, List<TemplateAttributeVO>> lineItemAttrList = templateSheetVO.getMediaPlanLineItemsAttributesList();
				final Set<Long> lineItemKeySet = lineItemAttrList.keySet();
				final Iterator<Long> keySetIterator = lineItemKeySet.iterator();
				LOGGER.info("Shifting row in excel sheet");
				if (!(mediaTemplate.isUseExistingRow() || lineItemAttrList.isEmpty() || lineItemAttrList.size() == 1 || templateName.equalsIgnoreCase(Constants.NYT_INTERNATIONAL_TEMPLATE))) {
					templateSheet.shiftRows(templateSheetVO.getMediaPlanLineItemsHeaderList().get(0).getRowNum(), templateSheet.getLastRowNum() + 1, lineItemAttrList.size(), true, false);
				}
				LOGGER.info("Populating line item data into media template");
				while (keySetIterator.hasNext()) {
					final Long key = keySetIterator.next();
					final List<TemplateAttributeVO> lineItemDataList = lineItemAttrList.get(key);
					if (lineItemDataList != null && !lineItemDataList.isEmpty()) {
						Row lineItemDataRow = templateSheet.getRow(lineItemDataList.get(0).getRowNum());
						if (lineItemDataRow == null) {
							lineItemDataRow = templateSheet.createRow(lineItemDataList.get(0).getRowNum());
						}
						for (int j = 0; j < lineItemDataList.size(); j++) {
							final TemplateAttributeVO mediaPlanAttributeVo = lineItemDataList.get(j);
							final Cell lineItemDataCell = lineItemDataRow.getCell(mediaPlanAttributeVo.getColNum(), Row.CREATE_NULL_AS_BLANK);

							// Setting NA if attribute value is not defined
							if (null == mediaPlanAttributeVo.getAttributeValue() || mediaPlanAttributeVo.getAttributeValue().length() == 0) {
								mediaPlanAttributeVo.setFormat(TemplateValueFormateType.TEXT.name());
								mediaPlanAttributeVo.setAttributeValue(N_A);
							}
							lineItemDataCell.setCellStyle(getCellStyle(mediaPlanAttributeVo, book, true));

							final TemplateValueFormateType formateType = TemplateValueFormateType.findByName(mediaPlanAttributeVo.getFormat());
							if (formateType.isNumericType()) {
								lineItemDataCell.setCellValue(getNumericCellValue(mediaPlanAttributeVo));
							} else if (formateType.isDateType()) {
								final Date date = getDateCellValue(mediaPlanAttributeVo);
								if (date != null) {
									lineItemDataCell.setCellValue(date);
								}

							} else {
								lineItemDataCell.setCellValue(mediaPlanAttributeVo.getAttributeValue());
							}
						}
					}
				}
			}
			templateWorkBook.setTemplateWorkBook(book);
		} catch (Exception e) {
			LOGGER.error("Exception in Generating Template", e);
			throw new TemplateGenerationException(TemplateGenerationException.getCustomeBusinessError(ErrorCodes.templateNotGenerated, ErrorMessageType.TEMPLATE_ERROR));

		} finally {
			try {
				if (templateInputStream != null) {
					templateInputStream.close();
				}
			} catch (IOException e) {
				LOGGER.error("Exception in Closing Input Stream", e);
			}
		}
		return templateWorkBook;
	}

	/**
	 * Method is used to generate creative specification
	 * @param mediaTemplateVO
	 * @param templateId
	 * @return
	 * @throws IOException
	 */
	private TemplateWorkBook generateCreativeWorkBook(final TemplateVO mediaTemplateVO, final long templateId, final ProposalVersion proposalVersion) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Generating MediaTemplate for TemplateVO:" + mediaTemplateVO + " templateId:" + templateId);
		}
		final TemplateWorkBook templateWorkBook = new TemplateWorkBook();

		Workbook book = null;
		FileInputStream templateInputStream = null;
		String outputFileName = null;

		try {
			final TemplateMetaData mediaTemplate = templateService.getMediaPlanTemplateById(templateId);
			// Setting template file Name
			final String templateName = mediaTemplate.getTemplateName();
			templateWorkBook.setTemplateFileName(templateName);

			// Locating to actual file
			final String templateFileName = templatesSource + FILE_SEPARATOR + templateName;

			// calculating output file name
			outputFileName = proposalVersion.getProposalOption().getProposal().getName() + ConstantStrings.UNDER_SCORE + ConstantStrings.CREATIVE_SPEC
					+ templateName.substring(templateName.indexOf('.'));
			templateWorkBook.setTemplateOutputFileName(outputFileName);

			templateInputStream = new FileInputStream(templateFileName);
			book = getWorkBook(templateName, templateInputStream);

			LOGGER.info("Populating proposal and line item data into media template");
			final List<TemplateSheetMetaData> templateSheetMetaDataList = mediaTemplate.getTemplateSheetList();
			for (int i = 0; i < templateSheetMetaDataList.size(); i++) {
				final TemplateSheetMetaData templateSheetMetaData = templateSheetMetaDataList.get(i);
				final Sheet mediaTemplateSheet = book.getSheet(templateSheetMetaData.getSheetName());

				final TemplateSheetVO templateSheetVO = mediaTemplateVO.getTemplateSheetMap().get(templateSheetMetaData.getSheetName());
				final List<TemplateAttributeVO> proposalAttributeList = templateSheetVO.getMediaPlanProposalAttributesList();
				LOGGER.info("Populating proposal data into media template for sheet sheet Name: " + mediaTemplateSheet.getSheetName());
				for (int j = 0; j < proposalAttributeList.size(); j++) {
					final TemplateAttributeVO mediaPlanAttributeVo = proposalAttributeList.get(j);
					final Row mediaTemplateRow = mediaTemplateSheet.getRow(mediaPlanAttributeVo.getRowNum());
					final Cell mediaTemplateCell = mediaTemplateRow.getCell(mediaPlanAttributeVo.getColNum(), Row.CREATE_NULL_AS_BLANK);
					// Setting NA if attribute value is not defined
					if (null == mediaPlanAttributeVo.getAttributeValue() || mediaPlanAttributeVo.getAttributeValue().length() == 0) {
						mediaPlanAttributeVo.setFormat(TemplateValueFormateType.TEXT.name());
						mediaPlanAttributeVo.setAttributeValue(N_A);
					}

					mediaTemplateCell.setCellStyle(getCellStyle(mediaPlanAttributeVo, book, false));

					final TemplateValueFormateType formateType = TemplateValueFormateType.findByName(mediaPlanAttributeVo.getFormat());
					if (formateType.isNumericType()) {
						mediaTemplateCell.setCellValue(getNumericCellValue(mediaPlanAttributeVo));
					} else if (formateType.isDateType()) {
						final Date date = getDateCellValue(mediaPlanAttributeVo);
						if (date != null) {
							mediaTemplateCell.setCellValue(date);
						}

					} else {
						mediaTemplateCell.setCellValue(mediaPlanAttributeVo.getAttributeValue());
					}
				}
				// Code to export header list in excel sheet in case of creative specification
				final List<TemplateAttributeVO> templateAttributeVO = templateSheetVO.getMediaPlanLineItemsHeaderList();
				Row lineItemDataRow1 = mediaTemplateSheet.getRow(templateAttributeVO.get(0).getRowNum());
				if (lineItemDataRow1 == null) {
					lineItemDataRow1 = mediaTemplateSheet.createRow(templateAttributeVO.get(0).getRowNum());
				}
				for (TemplateAttributeVO templateAttributeVO2 : templateAttributeVO) {
					final Cell headerCell = lineItemDataRow1.createCell(templateAttributeVO2.getColNum());
					headerCell.setCellValue(templateAttributeVO2.getAttributeName());
					templateAttributeVO2.setFontSize(11L);
					templateAttributeVO2.setFontWeight((long) Font.BOLDWEIGHT_BOLD);
					headerCell.setCellStyle(getCellStyle(templateAttributeVO2, book, true));
				}

				final TreeMap<Long, List<TemplateAttributeVO>> lineItemDataAttributeList = templateSheetVO.getMediaPlanLineItemsAttributesList();
				final Set<Long> lineItemKeySet = lineItemDataAttributeList.keySet();
				final Iterator<Long> keySetIterator = lineItemKeySet.iterator();

				LOGGER.info("Populating line item data into media template");
				while (keySetIterator.hasNext()) {
					final Long key = keySetIterator.next();
					final List<TemplateAttributeVO> lineItemAttributeDataList = lineItemDataAttributeList.get(key);
					if (lineItemAttributeDataList != null && !lineItemAttributeDataList.isEmpty()) {
						Row lineItemDataRow = mediaTemplateSheet.getRow(lineItemAttributeDataList.get(0).getRowNum());
						if (lineItemDataRow == null) {
							lineItemDataRow = mediaTemplateSheet.createRow(lineItemAttributeDataList.get(0).getRowNum());
						}
						for (int j = 0; j < lineItemAttributeDataList.size(); j++) {
							final TemplateAttributeVO mediaPlanAttributeVo = lineItemAttributeDataList.get(j);
							final Cell lineItemDataCell = lineItemDataRow.getCell(mediaPlanAttributeVo.getColNum(), Row.CREATE_NULL_AS_BLANK);

							// Setting NA if attribute value is not defined
							if (null == mediaPlanAttributeVo.getAttributeValue() || mediaPlanAttributeVo.getAttributeValue().length() == 0) {
								mediaPlanAttributeVo.setFormat(TemplateValueFormateType.TEXT.name());
								mediaPlanAttributeVo.setAttributeValue(N_A);
							}
							lineItemDataCell.setCellStyle(getCellStyle(mediaPlanAttributeVo, book, true));

							final TemplateValueFormateType formateType = TemplateValueFormateType.findByName(mediaPlanAttributeVo.getFormat());
							if (formateType.isNumericType()) {
								lineItemDataCell.setCellValue(getNumericCellValue(mediaPlanAttributeVo));
							} else if (formateType.isDateType()) {
								final Date date = getDateCellValue(mediaPlanAttributeVo);
								if (date != null) {
									lineItemDataCell.setCellValue(date);
								}

							} else {
								lineItemDataCell.setCellValue(mediaPlanAttributeVo.getAttributeValue());
							}
						}
					}
				}
			}
			templateWorkBook.setTemplateWorkBook(book);
		} catch (Exception e) {
			LOGGER.error("Exception in Generating Template", e);
			throw new TemplateGenerationException(TemplateGenerationException.getCustomeBusinessError(ErrorCodes.templateNotGenerated, ErrorMessageType.TEMPLATE_ERROR));
		} finally {
			try {
				if (templateInputStream != null) {
					templateInputStream.close();
				}
			} catch (IOException e) {
				LOGGER.error("Exception in Closing Input Stream", e);
			}
		}
		return templateWorkBook;
	}

	/**
	 * This method is used to populate all the column for custom template into TemplateVO with Row and Column
	 * @param customTemplateFile
	 * @param fileName
	 * @return
	 */
	public TemplateVO getTemplateVOFromCustomTemplate(final Workbook customTemplateFile, final String fileName) {
		final TemplateVO templateVO = new TemplateVO();
		templateVO.setName(fileName);

		final TemplateSheetVO templateSheetVO = new TemplateSheetVO();
		TemplateAttributeVO templateAttributeVO = null;

		final Sheet sheet = customTemplateFile.getSheetAt(0);
		templateSheetVO.setName(sheet.getSheetName());
		templateVO.getTemplateSheetMap().put(sheet.getSheetName(), templateSheetVO);
		final List<TemplateAttributeVO> proposalAttributeList = new ArrayList<TemplateAttributeVO>();
		final List<TemplateAttributeVO> lineItemAttributeList = new ArrayList<TemplateAttributeVO>();
		for (int i = 0; i <= sheet.getLastRowNum(); i++) {
			final Row row = sheet.getRow(i);
			if (row != null) {
				final int cellCount = row.getLastCellNum();

				for (int j = 0; j <= cellCount; j++) {
					if (row.getCell(j) != null && row.getCell(j).getCellType() == Cell.CELL_TYPE_STRING) {
						String ColumnValue = row.getCell(j).getStringCellValue();
						if (StringUtils.isNotBlank(ColumnValue)) {
							ColumnValue = ColumnValue.trim();
							if ((ColumnValue.startsWith(ConstantStrings.AMPT_PRO) || ColumnValue.startsWith(ConstantStrings.AMPT)) && checkValidToken(ColumnValue)) {
								templateAttributeVO = new TemplateAttributeVO();
								templateAttributeVO.setRowNum(i + 1);
								templateAttributeVO.setColNum(j + 1);
								if (ColumnValue.startsWith(ConstantStrings.AMPT_PRO)) {
									templateAttributeVO.setAttributeName(ColumnValue.replace(ConstantStrings.AMPT_PRO, ConstantStrings.EMPTY_STRING));
									templateAttributeVO.setProposalType(true);
									proposalAttributeList.add(templateAttributeVO);
								} else {
									templateAttributeVO.setAttributeName(ColumnValue.replace(ConstantStrings.AMPT, ConstantStrings.EMPTY_STRING));
									templateAttributeVO.setProposalType(false);
									lineItemAttributeList.add(templateAttributeVO);
								}
								templateVO.getTokenSet().add(templateAttributeVO.getAttributeName());
							}
						}
					}
				}
			}
		}
		if (proposalAttributeList != null && !proposalAttributeList.isEmpty()) {
			Collections.sort(proposalAttributeList, new TemplateAttributeComparator());
			templateSheetVO.getTemplateAttributesMap().put(ConstantStrings.PROPOSAL_HEADER, proposalAttributeList);
		}
		if (lineItemAttributeList != null && !lineItemAttributeList.isEmpty()) {
			Collections.sort(lineItemAttributeList, new TemplateAttributeComparator());
			templateSheetVO.getTemplateAttributesMap().put(ConstantStrings.PROPOSAL_LINEITEM, lineItemAttributeList);
		}
		return templateVO;
	}

	/**
	 * Save file in TemplateDir
	 * @param templateManagementForm
	 * @throws IOException
	 */
	public void copyFileInTemplateDir(final TemplateManagementForm templateManagementForm) throws IOException {
		LOGGER.info("Saving file to disk. template name: " + templateManagementForm.getFileName() + " location: " + templatesSource);
		File source = new File(getTempDir() + FILE_SEPARATOR + templateManagementForm.getTmpFile());
		File desc = new File(templatesSource);
		FileUtils.copyFileToDirectory(source, desc);
		source.delete();
		source = new File(templatesSource + FILE_SEPARATOR + templateManagementForm.getFileName());
		desc = new File(templatesSource + FILE_SEPARATOR + templateManagementForm.getTmpFile());
		if (source.exists()) {
			source.delete();
		}
		boolean flag = desc.renameTo(source);
		LOGGER.info("Flag while saving file to disk : "+flag);
		desc.delete();
	}

	/**
	 * Remove AMPT_PRO_ and AMPT_ token
	 * @param templateManagementForm
	 * @throws IOException
	 */
	public Workbook removeTemplateCellTokens(final Workbook book) throws IOException {
		final Sheet sheet = book.getSheetAt(0);
		Cell cell = null;
		for (int i = 0; i <= sheet.getLastRowNum(); i++) {
			final Row row = sheet.getRow(i);
			if (row != null) {
				final int cellCount = row.getLastCellNum();
				for (int j = 0; j <= cellCount; j++) {
					if (row.getCell(j) != null && row.getCell(j).getCellType() == Cell.CELL_TYPE_STRING) {
						String ColumnValue = row.getCell(j).getStringCellValue();
						ColumnValue = ColumnValue == null ? ColumnValue : ColumnValue.trim();
						if (ColumnValue.startsWith(ConstantStrings.AMPT_PRO) || ColumnValue.startsWith(ConstantStrings.AMPT)) {
							cell = row.getCell(j);
							if (ColumnValue.startsWith(ConstantStrings.AMPT_PRO)) {
								cell.setCellValue(ConstantStrings.EMPTY_STRING);
							} else {
								cell.setCellValue(ConstantStrings.EMPTY_STRING);
							}
						}
					}
				}
			}
		}
		return book;
	}

	/**
	 * Check custom template has token or nor
	 * @param customTemplateFile
	 * @param fileName
	 * @return
	 */
	public boolean isTemplateHashToken(final Workbook customTemplateFile, final String fileName) {
		boolean hasToken = false;
		final Sheet sheet = customTemplateFile.getSheetAt(0);
		for (int i = 0; i <= sheet.getLastRowNum(); i++) {
			final Row row = sheet.getRow(i);
			if (row != null) {
				final int cellCount = row.getLastCellNum();
				for (int j = 0; j <= cellCount; j++) {
					if (row.getCell(j) != null && row.getCell(j).getCellType() == Cell.CELL_TYPE_STRING) {
						final String ColumnValue = row.getCell(j).getStringCellValue();
						if ((ColumnValue.startsWith(ConstantStrings.AMPT_PRO) || ColumnValue.startsWith(ConstantStrings.AMPT)) && checkValidToken(ColumnValue)) {
							if (performsXSS(ColumnValue)) {
								return false;
							} else {
								hasToken = true;
							}
						}
					}
				}
			}
		}
		return hasToken;
	}

	/**
	 * Check either token is valid or not
	 * @param token
	 * @return
	 */
	private boolean checkValidToken(final String token) {
		if (token.startsWith(ConstantStrings.AMPT_PRO)) {
			return StringUtils.isNotBlank(token.replace(ConstantStrings.AMPT_PRO, ConstantStrings.EMPTY_STRING));
		} else if (token.startsWith(ConstantStrings.AMPT)) {
			return StringUtils.isNotBlank(token.replace(ConstantStrings.AMPT, ConstantStrings.EMPTY_STRING));
		}
		return false;
	}

	/**
	 * Method to prevent cross-side scripting
	 * @param string
	 * @return
	 */
	public boolean performsXSS(final String string) {
		final Pattern pattern = Pattern.compile("[^A-Za-z0-9.$@;,/ _%()-]+");
		final Matcher matcher = pattern.matcher(string);
		return matcher.find();
	}

	/**
	 * Save file in tmp dir
	 * @param templateManagementForm
	 * @throws IOException
	 */
	public String saveFileToTempDir(final TemplateManagementForm templateManagementForm) throws IOException {
		LOGGER.info("Save file in temp dir . template id: " + templateManagementForm.getTemplateId() + " location: " + getTempDir());
		String fileName = System.currentTimeMillis() + ConstantStrings.TMP;
		final File sourceFile = new File(getTempDir() + FILE_SEPARATOR + templateManagementForm.getCustomTemplateFile().getOriginalFilename() + ConstantStrings.TMP);
		FileCopyUtils.copy(templateManagementForm.getCustomTemplateFile().getInputStream(), new FileOutputStream(sourceFile));
		final File newFile = new File(getTempDir() + FILE_SEPARATOR + fileName);
		if (sourceFile.renameTo(newFile)) {
			sourceFile.delete();
		} else {
			LOGGER.info("File is not renamed in temp directory : "+templateManagementForm.getCustomTemplateFile().getOriginalFilename());
			fileName = ConstantStrings.EMPTY_STRING;
		}
		return fileName;
	}

	/**
	 * Return path of template
	 * @param templateName
	 * @return
	 */
	public File getTemplateFileByName(final String templateName) {
		return new File(templatesSource + FILE_SEPARATOR + templateName);
	}

	/**
	 * Return path of temporary dir
	 * @return
	 */
	private String getTempDir() {
		return ((String) AccessController.doPrivileged(new GetPropertyAction("java.io.tmpdir")));
	}

	/**
	 * Delete template from template directory
	 * @param fileName
	 */
	public void deleteTemplate(final String fileName) {
		final File source = new File(templatesSource + FILE_SEPARATOR + fileName);
		if (source.exists()) {
			source.delete();
		}
	}

	public void setProposalService(final IProposalService proposalService) {
		this.proposalService = proposalService;
	}

	public void setTemplatesBaseLocation(final String templatesBaseLocation) {
		this.templatesSource = templatesBaseLocation;
	}

	public void setTemplatesSource(final String templatesSource) {
		this.templatesSource = templatesSource;
	}

	public void setTemplateService(final ITemplateService templateService) {
		this.templateService = templateService;
	}

	public void setProductService(final IProductService productService) {
		this.productService = productService;
	}
}
