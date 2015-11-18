/**
 * 
 */
package com.nyt.mpt.util.enums;

/**
 * Constants for various help codes. Resource Names are defined in Messages.properties
 * 
 * @author Shishir Srivastava
 * 
 */
public enum UserHelpCodes {
	/**
	 * Represent Help message - An error has occurred. Please place/hover the cursor over highlighted field to see the details.
	 */
	DefaultValidationErrorHeaderMessage ("validation.error"),

	/**
	 * Represent Help message - mandatory field
	 */
	HelpMandatoryInputMissing ("help.error.mandatory"),
	
	HelpMandatoryWeekdaysMissing("help.error.mandatory.weekdays"),
	
	HelpMandatoryFrequencyMissing("help.error.mandatory.frequency"),
	
	HelpEmailScheduleDaysOutsideDateRange("help.email.days.outside.date"),
	
	HelpMandatoryDateMissing("help.error.mandatory.date"),
	/**
	 * Represent Help message - XSS Attack Characters
	 */
	HelpXSSAttackCharacters ("help.error.xssAttackCharacters"),
	/**
	 * Represent Help message - XSS Attack Characters in sheetName
	 */
	HelpXSSAttackCharactersInSheet ("error.contains.xss"),
	/**
	 * Represent Help message - Please enter valid data in the field.
	 */
	HelpMandatorySelectMissing ("help.error.select.mandatory"),
	/**
	 * Represent Help message - Please remove some Sales Targets.
	 */
	HelpMaximumSalesTargetGroup ("help.error.salestarget.maximum"),
	/**
	 * Represent Help message - Please enter unique creative name.
	 */
	DuplicateCreativeName ("help.error.duplicate.creative"),
	/**
	 * Represent Help message - Please enter unique email details duration.
	 */
	DuplicateEmailDetailsName ("help.error.duplicate.emailDetails"),
	/**
	 * Represent Help message - Please enter unique Line Item Name
	 */
	DuplicateLineItemName ("help.error.duplicate.lineItem"),
	/**
	 * Represent Help message - Please provide unique combination of Product & Sales Target.
	 */
	DuplicateProductAndSalesTarget ("help.error.duplicate.lineItem.product"),
	/**
	 * Represent Help message - filesize.
	 */
	HelpFileSize("help.error.filesize"),
	/**
	 * Represent Help message - Please provide unique Rate Card Group name.
	 */
	DuplicateProductGroupName("help.error.duplicate.productgroup"),

	/**
	 * Represent Help message - selected sales target is already associated with different pricing rule
	 */
	SalesTargetAlreadyAssociated("help.associated.salesTargetAlreadyAssociated"),

	/**
	 * Represent Help message - Please enter unique package name.
	 */
	DuplicatePackageName ("help.error.duplicate.package"),
	/**
	 * Represent Help message - Please provide unique attribute name.
	 */
	DuplicateName("help.error.duplicategeneric"),
	/**
	/** 
	 * Represent Help message - Please provide unique rate profile name. 
	 */
	DuplicateRateProfile("help.error.duplicateRateProfile"),
	
	/** 
	 * Represent Help message - Please provide different target type.
	 */
	DuplicateLayerinfRule("help.error.duplicate.layeringrule"),
	/**
	 * Represent Help message - Please enter unique login name.
	 */
	DuplicateLoginName ("help.error.duplicateLoginName"),
	/**
	 * Represent Help message - Please enter unique Pricing Rule name.
	 */
	DuplicatePricingRule("help.error.duplicatePricingRule"),
	/**
	 * Represent Help message -  Selected Product and Rate Card Group combination already exists.
	 */
	DuplicateCombinationProductAndGroup("help.error.duplicateProductAndGroup"),

	/**
	 * Represent Help message -  Selected product and rate card group's sales target combination already exists.
	 */
	DuplicateProductAndGroupSalesTarget("help.error.duplicateProductAndGroupSalesTarget"),

	/**
	 * Represent Help message -  Please re-load rate card group.
	 */
	RateCardNotAvailable("help.notavailable.rateCardNotAvailable"),
	/**
	 * Represent Help message - Proposal Name should be unique.
	 */
	DuplicateProposalName("help.error.duplicateproposalname"),
	/**
	 * Represent Help message -  Requested Date cannot be before Due Date.
	 */
	ProposalDueDateHelp("help.proposal.duedate"),
	/**
	 * Represent Help message - Requested Date cannot be before Due Date.
	 */
	ProposalReqDateHelp("help.proposal.reqdate"),
	/**
	 * Represent Help message -  End Date cannot be before Start Date.
	 */
	ProposalStartDateHelp("help.proposal.startdate"),
	/**
	 * Represent Help message - End Date cannot be before Start Date.
	 */
	ProposalEndDateHelp("help.proposal.enddate"),
	/**
	 * Represent Help message - flight
	 */
	LineItemFlightHelp("help.lineitem.flight"),
	/**
	 * Represent Help message - start Date
	 */
	LineItemStartDateHelp("help.lineitem.startDate"),
	/**
	 * Represent Help message - end Date
	 */
	LineItemEndDateHelp("help.lineitem.endDate"),
	/**
	 * Represent Help message - Provide Flight Information as text or Enter Start Date with End Date
	 */
	LineItemImpressionSOVHelp("help.lineitem.impressionSOV"),
	/**
	 * Represent Help message - Please provide Start Date with End Date.
	 */
	LineItemFlightRedundanthelp("help.lineitem.flight.redundantinfo"),
	/**
	 * Represent Help message - Please provide Width2 with Height2.
	 */
	creativeWidthHeightHelp("help.creative.width.height.together"),
	/**
	 * Represent Help message - Please provide different SOS Order I
	 */
	DuplicatesSosOrderId ("help.error.duplicate.sosOrderId"),
	/**
	 * Represent Help message - Option Name should be unique.
	 */
	DuplicateOptionName("help.error.duplicateoptionname"),
	/**
	 * Represent Help message - Please upload valid template file.
	 */
	CorruptTemplateFile("help.corrupt.template.file"),

	/**
	 * Represent Help message - Please provide token with AMPT_ as prefixed.
	 */
	HelpPrefixedTokenMmissing("help.prefixed.token.missing"),

	/**
	 * Represent Help message - Please provide different template file name
	 */
	DuplicateTemplateNameFileName("help.duplicate.template.file.name"),
	/**
	 * Represent Help message - Please provide different template name
	 */
	DuplicateTemplateName("help.error.duplicate.template"),
	/**
	 * Represent Help message - Please upload valid file format in the field.
	 */
	HelpMandatoryFileTypeMissing("help.error.file.type"),
	
	/**
	 * Represent Help message - Creative Template is reserve for system.
	 */
	HelpReserveForSystem("help.error.reserveForSystem"),
	/**
	 * Represent Help message - Tier name should be unique.
	 */
	HelpDuplicateTierName("help.error.duplicate.Tier.Name"),
	/**
	 * Represent Help message - Tier level should be unique.
	 */
	HelpDuplicateTierLevel("help.error.duplicate.Tier.Level"),
	/**
	 * Represent Help message - Tier level should be unique.
	 */
	HelpDuplicatePremiumElement("help.error.duplicate.Tier.Elements"),
	/**
	 * Represent Help message - File Name should be same while editing a template.
	 */
	HelpNeedSameFileName("help.template.with.same.file.name"),
	/**
	 * Represent Help message - Calculate base price.
	 */
	HelpMandatoryToCalculateBasePriceMissing("help.calculate.base.price"),
	/**
	 * Represent Help message - Line item targeting.
	 */
	HelpTargetingString ("help.error.targeting"),
	/** 
	 * Please reload to see all the creatives.
	 */
	InvalidCreatives("help.error.invalid.creatives"),
	/**
	 * Represent Help message - Default option should be selected for edit.
	 */
	HelpSelectDefaultOption("help.error.select.valid.option"),
	/**
	 * Help message when advertiser name is blank on SOS order Details 
	 */
	AdvertiserNameHelp("help.advertiser.name"),
	/**
	 * Help message for duplicate investment in added value 
	 */
	DuplicateInvestmentHelp("help.error.duplicate.avinvestment"),
	
	/**
	 * Help message for percentage in added value 
	 */
	AvPercentageHelp("help.error.avpercentage"),
	/**
	 * Help message for Line Item send date
	 */
	LineItemsendDateHelp("help.lineitem.sendDate"),
	/**
	 * Represent the email is already reserved or over-booked
	 */
	sorExceededForEmail("help.sor.exceeded.email"),
	/**
	 * Represent reservable line item has already exceed the sor
	 */
	sorExceededForReservable("help.sor.exceeded.reservable"),
	/**
	 * Represent Help message - start Date
	 */
	LineItemReservationExpiryDateHelp("help.lineitem.reservation.expiryDate"),
	/**
	 * Represent expiry date less than minimum reservation start date
	 */
	HelpGreaterExpiryDate ("help.error.reservation.greaterExpiryDate"),
	
	/**
	 * Represent start date less than current date
	 */
	HelpPastStartDate ("help.error.reservation.pastStartDate"),
	/**
	 * Represent start date less than current date
	 */
	HelpExpiryDateBeyondOneYear("help.error.reservation.expiryDateBeyondOneYear"),
	
	HelpExpiryDateBeforeCurrent("help.error.reservation.ExpiryDatePast"),
	/**
	 * Represent Help message - Send Date
	 */
	LineItemSendDateHelp("help.lineitem.sendDate"),
	/**
	 * Represent Help message - start and expiry date
	 */
	LineItemEmailReservationExpiryDateHelp("help.email.lineitem.reservation.expiryDate"),;
	

	private String resourceName;

	private UserHelpCodes(final String resourceName) {
		this.resourceName = resourceName;
	}

	public final String getResourceName() {
		return resourceName;
	}
}
