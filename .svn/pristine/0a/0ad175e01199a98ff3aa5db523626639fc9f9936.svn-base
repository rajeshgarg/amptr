/**
 * 
 */
package com.nyt.mpt.util.enums;

/**
 * Constants for various error codes.
 * 
 * Resource Names are defined in Messages.properties,
 * 
 * @author Shishir Srivastava
 * 
 * */
public enum ErrorCodes {
	/**
	 * Represent error message - {0} is mandatory.
	 */
	MandatoryInputMissing("error.mandatory"),
	/**
	 * Represent error message - file not found
	 */
	FileNotFound("error.file.not.found"),
	/**
	 * Represent error message - Duplicate creative name "{0}".
	 */
	DuplicateCreativeEntry("error.duplicate.creative"),
	/**
	 * Represent error message - Duplicate package name "{0}".
	 */
	DuplicatePackageName("error.duplicate.package"),

	/**
	 * Represent error message - Only numeric digits allowed in {0}.
	 */
	NumericDigit("error.numeric.digit"),
	/**
	 * Represent error message - Maximum allowed limit for {0} is {1}.
	 */
	NumericDigitMaxVal("error.numeric.max.value"),
	/**
	 * Represent error message - {0} should be greater than zero.
	 */
	NumericDigitMinVal("error.greater.than.zero"),
	/**
	 * Represent error message - Maximum allowed character for {0} is {1}.
	 */
	ExceedMaxAllowedCharacter("error.exceed.max.length"),
	/**
	 * Represent error message - Only {0} decimal digits allowed in {1}.
	 */
	NumericDecimalValue("error.numeric.decimal.value"),
	/**
	 * Represent error message - Please select a file whose size is within the
	 * allowed limit.
	 */
	FileSizeExceeds("error.filesize"),
	/**
	 * Represent error message - File name exceeds the maximum character
	 * allowed.
	 */
	FileLengthExceeds("error.filelength"),
	/**
	 * Represent error message - Specified file type is not allowed.
	 */
	FileTypeMisMatch("error.filetype"),
	
	/**
	 * Represent error message - CreativeSpecTemplate.xlsx is reserve for system
	 */
	ReserveForSystem("error.reserveForSystem"),
	/**
	 * Represent error message - File name does not match with the old file
	 * name.
	 */
	FileNameChanged("error.oldfilename"),
	/**
	 * Represent error message - Duplicate "{0}".
	 */
	DuplicateEntryGeneral("error.duplicategeneric"),
	/**
	 * Represent error message - Only positive numeric digits allowed in {0}.
	 */
	PositiveNumericDigit("error.positive.numeric.digit"),
	/**
	 * Represent error message - Duplicate "{0}" for same type.
	 */
	DuplicateAttribute("error.duplicateAttribute"),
	/**
	 * Represent error message - Duplicate "{0}" for same Creative.
	 */
	DuplicateAssoc("error.duplicateAssoc"),
	/**
	 * Represent error message - Duplicate "{0}" or "{1}" for same Rate Profile.
	 */
	DuplicateRateProfile("error.duplicateRateProfile"),
	/**
	 * Represent error message - Selected row has references in other modules.
	 * This action might corrupt the data. <br>
	 * <br>
	 * Do you still want to update?
	 */
	ObjectAssociationExistNeedConfirmation("object.association.exist.confirmation"),
	/**
	 * Represent error message - Selected row has references in other modules. <br>
	 * <br>
	 * Please remove the association prior to deleting.
	 */
	ObjectAssociationExistCanNotDelete("object.association.exist.nodelete"),
	/**
	 * Represent error message - Duplicate proposal name.
	 */
	DuplicateProposalName("error.duplicate.proposal"),
	/**
	 * Represent error message - Due Date & Requested Date combination is wrong.
	 */
	ProposalDueDateError("error.proposal.duedate"),
	/**
	 * Represent error message - Due Date & Requested Date combination is wrong.
	 */
	ProposalReqDateError("error.proposal.reqdate"),
	/**
	 * Represent error message - Start Date & End Date combination is wrong.
	 */
	ProposalStartDateError("error.proposal.startdate"),
	/**
	 * Represent error message - Start Date & End Date combination is wrong.
	 */
	ProposalEndDateError("error.proposal.enddate"),
	/**
	 * Represent error message - Please provide atleast Flight Information or
	 * Start Date with End Date.
	 */
	LineItemFlightError("error.lineitem.flight"),
	/**
	 * Represent error message - Start Date with End Date needs to be supplied.
	 */
	LineItemFlightRedundantError("error.lineitem.flight.redundantinfo"),
	/**
	 * Represent error message - CPM Price is less than the Rate Card Price
	 */
	pricingException("error.lineitem.pricingException"),
	/**
	 * Represent error message - Pricing Rule is not defined
	 */
	noPricingException("error.lineitem.noPricingException"),
	/**
	 * Represent error message - Copied from expired Proposal
	 */
	expiredProposalException("error.lineitem.expiredProposalException"),
	/**
	 * Represent error message - Copied from expired Package
	 */
	expiredPackageException("error.lineitem.expiredPackageException"),
	/**
	 * Represent error message - Copied from unbreakable Package
	 */
	unbreakablePackageException("error.lineitem.unbreakablePackageException"),
	/**
	 * Represent error message - Only alphabets, numbers, spaces and [ . ; , / _
	 * - @ $ % ( )] are allowed.
	 */
	containsXSSCharacters("error.contains.xss"),
	/**
	 * Represent error message - Only alphabets, numbers, spaces and [ . ; , / _
	 * - @ $ % ( ) ' &] are allowed.
	 */
	containsXSSCharactersCampaignName("error.contains.xss.campaignname"),
	/**
	 * Represent error message - Contains XSS Attack characters
	 */
	containsXSSAttackCharacters("error.contains.xssAttackCharacters"),
	/**
	 * Represent error message - Contains XSS Attack characters in sheet name
	 */
	containsXSSAttackCharactersInSheetName("error.contains.sheet.xssAttackCharacters"),
	/**
	 * Represent error message - Rich Media has been selected for CPM < 20$
	 */
	richMediaWithLessCPM("error.lineitem.richMediaWithLessCPMException"),
	/**
	 * Represent error message - Product has been expired
	 */
	expiredProductException("error.lineitem.expiredProductException"),
	/**
	 * Represent error message - Sales Target has been expired
	 */
	expiredSalesTargetException("error.lineitem.expiredSalesTargetException"),
	/**
	 * Represent error message - Sales Target & Product has been expired
	 */
	expiredSalesTargetProductException("error.lineitem.expiredSalesTargetProductException"),
	/**
	 * Represent error message - Proposal is assigned to some other user you can
	 * not change it.
	 */
	proposalNotAssigned("error.proposal.notAssigned"),
	/**
	 * Represent error message - Avails is not available for given combination.
	 */
	availsNotAvailable("error.avails.notAvailable"),

	/**
	 * Avails mandatory input missing
	 */
	availsMandatroyAttributeMissing("error.avails.mandatoryAttributeMissing"),
	/**
	 * Represent error message - ADX Data is Not Available for Selected Product.
	 */
	adxProductDataNotAvailable("error.avails.adxProductDataNotAvailable"),
	/**
	 * Represent error message - ADX Data is Not Available for Selected Sales
	 * Target.
	 */
	adxSalesTargetDataNotAvailable("error.avails.adxSalesTargetDataNotAvailable"),
	/**
	 * Represent error message - Exceptional Behaviour occured please consult
	 * your system administrator.
	 */
	runtimeExceptionMessage("error.runTime.exception"),
	/**
	 *Represent error message - We cannot send email as email server is not responding.
	 * Please try after sometime. 
	 */
	runtimeMailExceptionMessage("error.runTime.mail.exception"),
	/**
	 * Represent error message - There is some error in generating proposal.
	 * Please contact your system administrator.
	 */
	templateNotGenerated("error.proposalTemplate.notGenerated"),
	/**
	 * Represent error message - Width2 with Height2 needs to be supplied.
	 */
	creativeWidthHeight("error.creative.width.height.together"),
	/**
	 * Represent error message - invalid Sequence No
	 */
	LineItemSequenceError("error.lineitem.lineItemSequence"),

	/**
	 * Represent error message - sov is greater than hundred.
	 */
	sovGreaterThanHundred("error.lineitem.sov.greater.than.hundred"),
	/**
	 * Represent error message - Corrupt template file.
	 */
	CorruptTemplateFile("error.corrupt.template.file"),
	/**
	 * Represent error message - Token not found in template .
	 */
	TokenNotFoundInTemplateFile("error.token.not.found.in.template.file"),

	/**
	 * Represent error message - Duplicate Duplicate Template File Name.
	 */
	DuplicateTemplateFileName("error.duplicate.template.file.name"),

	/**
	 * Represent error message - Duplicate Duplicate Template.
	 */
	DuplicateTemplateEntry("error.duplicate.template"),

	/**
	 * Represent error message - Duplicate Tier name.
	 */
	DuplicateTierName("error.duplicate.Tier.Name"),
	/**
	 * Represent error message - Duplicate Tier level.
	 */
	DuplicateTierLevel("error.duplicate.Tier.Level"),
	/**
	 * Represent error message - Duplicate Tier level.
	 */
	DuplicatePremiumElement("error.duplicate.Tier.Elements"),
	/**
	 * Represent error message - Only positive numeric digits allowed in {0}.
	 */
	PositiveDecimalDigit("error.positive.decimal.digit"),
	/**
	 * Represent error message - File Name not matched while Editing a Template
	 */
	TemplateFileNameNotMatch("error.template.file.name.not.matched"),
	/**
	 * Represent error message - Only alphabets, numbers, and _ are allowed.
	 */
	containsAlphanumericCharacters("error.contains.alphanumeric"),
	/**
	 * Represent error message - Only numbers and comma are allowed in the premium target elements.
	 */
	PremiumLessThanEqualZero("error.tier.premium.less.than.zero"),
	
	/**
	 *  Represent error message when yieldex is not responding
	 */
	yieldexException("error.yieldex.notResonding"),
	/**
	 * Represent error message - Bad Gate Way.
	 */
	yieldexBadGateWayException("error.yieldex.bad.gate.way"),
	/**
	 * Represent error message - Only non HTML characters are allowed.
	 */
	ContainsXSSContent("error.contains.xss.attacks"),
	/**
	 * Represent error message - url length exception
	 */
	yieldexUrlLengthException("error.yieldex.url.length.exception"),
	/**
	 * All the creatives may not be present.
	 */
	InvalidCreatives("error.invalid.creatives"),
	/**
	 * Represent error message - Only default option can be edit in "Under Review" status of proposal.
	 */
	OptionNotEditableForProposalUnderReview("error.option.not.editable.under.review"),
	/**
	 * Represent error message - Only integers allowed in {0}.
	 */
	IntegerNumber("error.lineitem.sosOrderNo"),
	/**
	 * Represent error message - Only positive integers allowed in {0}.
	 */
	PositiveNumber("error.positive.integer"),
	/**
	 * value of Total investment in the line item may not be right. 
	 */
	totalInvestmentException("error.invalid.totalInvestment"),
	/**
	 * Represent the sor is blank
	 */
	nosor("error.invalid.nosor"),
	
	SorLimit("error.invalidrange.sor"),
	 
	InvalidEmail("error.invalid.email"),
	
	InvalidEmailString("error.invalid.emaildata"),
	
	staleObjectStateExceptionMessage("error.staleObjectState.exception"),
	
	DatesMandatory("error.expirydate.mandatory"),
	EmailDatesMandatory("error.email.expirydate.mandatory"),
	/**
	 * Represent error message - Start Date with End Date needs to be supplied.
	 */
	LineItemReservationPastEndDateError("error.lineitem.reservation.past.end.date"),
	/**
	 * Represent error message - Start Date is mandatory.
	 */
	RateProfileDiscountStartDateMandatory("error.rateProfile.discount.startDate"),
	/**
	 * Represent error message - End Date is mandatory.
	 */
	RateProfileDiscountEndDateMandatory("error.rateProfile.discount.endDate"),
	/**
	 * Represent error message - Discount Percent is mandatory.
	 */
	RateProfileDiscountPercentMandatory("error.rateProfile.discount.percent"),
	/**
	 * Represent error message - Discount Percent is mandatory.
	 */
	RateProfileDiscountDateRangeConflict("error.rateProfile.discount.dateRange.conflict"),
	/**
	 * Represent error message - Seasonal discount must be greater than zero.
	 */
	DiscountLessThanEqualZero("error.seasonal.discount.less.than.zero"),
	
	/**
	 * Represent error message - Duplicate Investment value
	 */
	DuplicateInvestmentHelp("error.duplicate.avinvestment"),
	
	/**
	 * Represent error message - Percentage should be less than 100 and greater than 0.
	 */
	InvalidAvPercentage("error.invalidrange.percentage"),
	
	/**
	 *  Represent error message - Percentage should be in range of percentage value that corresponds to maximum and minimum value of investment
	 */
	InvalidAvPercentageRange("error.invalid.perentage"),
	/**
	 * Represent error message -Threshold value is mandatory.
	 */
	OptionThresholdValueMandatory("error.option.threshold.value"),
	/**
	 * Represent error message -Threshold value is greater than the total investment of the option
	 */
	OptionThresholdValueGreaterThanInvestment("error.option.threshold.value.greater.than.investment"),
	/**
	 * Represent error message - Seasonal discount must be greater than zero.
	 */
	ThresholdLessThanEqualZero("error.threshold.value.less.than.zero"),
	/**
	 * Represent error message - Maximum allowed limit for {0} is {1}.
	 */
	SeasonalDiscountMaxVal("error.seasonal.discount.max.value"),
	/**
	 * Represent error message - Offered impressions cannot be increased for pricing Approved Added value Line Items.
	 */
	OfferedImpressionsIncreased("error.impressions.increased"),
	/**
	 * Represent error message - Connection Error while pushing data to Salesforce.
	 */
	salesforceConnectionError("error.connection.error.salesforce"), 
	/**
	 * Represents error message- for duplicate filter name.
	 */
	DuplicateFilterName("error.duplicate.filtername"),
	/**
	 * Represent error message - Duplicate email details "{0}".
	 */
	DuplicateEmailDetailsEntry("error.duplicate.emailDetails"),
	
	/**
	 * Represent error message - Invalid send date.
	 */
	LineItemEmailReservationInvalidDate("error.lineitem.email.reservation.invalid.date"), 
	/**
	 * Represent error message - Invalid end date.
	 */
	LineItemFlightIncorrectEndDate("error.lineitem.invalid.end.date"),
	EmailScheduleDaysOutsideDateRange("error.email.days.outside.date"),
	/**
	 * Represent error message - Invalid start date.
	 */
	LineItemFlightIncorrectStartDate("error.lineitem.invalid.start.date"),
	/**
	 * Represent the email is already reserved or over-booked
	 */
	sorExceededForEmail("error.sor.exceeded.email"),
	/**
	 * Represent reservable line item has already exceed the sor
	 */
	sorExceededForReservable("error.sor.exceeded.reservable"),
	/**
	 * Represent DFP wrapper error message
	 */
	dfpWrapperErrorMessage("dfp.wrapper.error"),
	/**
	 * Represents DFP wrapper targeting validation error
	 */
	noTwoTargetingTogetherWithNot("negation.targeting.twice"),
	/**
	 * Represents DFP targeting error in case of OR
	 */
	dfpOrCantnotComeInTargeting("or.cant.targeting"),
	/**
	 * Represent DFP wrapper where end date cannot be before current date
	 */
	fetchAvailsEndDateCannotBefore("help.fetchavails.enddate"),
	/**
	 *  Represent DFP wrapper where end date cannot be before current date in case of bulk fetch
	 */
	bulkFetchAvailsEndDateCannotBefore("help.bulkfetchavails.enddate"), 
	
	DFPTargeting ("help.dfp.targeting"),
	/**
	 * Represent line item reservation with past expiry date
	 */
	LineItemReservationPastExpiryDateError("error.lineitem.reservation.past.expiry.date"),
	/**
	 * Represent line item reservation with start date before reservation expiry date.
	 */
	LineItemStartDatePriorExpiryDateError("error.lineitem.startDate.prior.expiry.date"),
	/**
	 * Represent line item reservation with start date before reservation expiry date.
	 */
	LineItemExpiryBeyondOeYear("error.lineitem.expiryDate.after.one.year"),
	
	LineItemExpiryBeforeCurrent("error.lineitem.expiryDate.before.current"),

	/**
	 * Represent expriy date greater than minimum start date of selected line items
	 */
	GreaterExpiryDateError("error.lineitem.reservation.greater.expiry.date"),
	
	/**
	 * Represent start date is in the past.
	 */
	PastStartDateError("error.lineitem.reservation.past.startDate");
	
	private String resourceName;

	private ErrorCodes(String resourceName) {
		this.resourceName = resourceName;
	}

	public final String getResourceName() {
		return resourceName;
	}
}
