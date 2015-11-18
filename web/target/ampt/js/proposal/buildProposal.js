/**
 * @author amandeep.singh
 */
var lineItemGridSortName = "lineItemID";
var emailAvailableDatesArray = new Array();
$(document).ready(function() {
	
	/* Binding click event with the Check price button on the Line Item screen */
    $('#lineItemdetailForm #calculateBasePrice').click(function() {
		calculateBasePrice();
	});
    
    /* Binding click event with the view Ad Formats icon besides product drop down on the line item screen */ 
    $('#productCreativeSummary', '#lineItem').click(function() {
    	showProductCreativeSummary();
	});
    
    /* Modal dialog for getting the user's confirmation while updating the selected line item(s) base price */
    var multiUpdateConfirmationDlg = new ModalDialog();
    multiUpdateConfirmationDlg.height = 170;
    multiUpdateConfirmationDlg.width = 360;
    multiUpdateConfirmationDlg.resizable = false;
    multiUpdateConfirmationDlg.buttons = [{
        text: "Yes",
        click: function(){
            $(this).dialog("close");
            updateMultiLineItemPrice();
        }
    }, {
        text: "No",
        click: function(){
            var select = $("#LineItemMultiSelect_Header");
            customLineItemSelect(select);
            $(this).dialog("close");
        }
    }];
    $("#multiLineItemPriceUpdateContainer").dialog(multiUpdateConfirmationDlg);
    enableDialogButton("multiLineItemPriceUpdateContainer");
	$("#sosProductId", "#lineItemdetailForm").select2();
    
    $(".searchable-product", "#lineItemdetailForm").blur(function(){
        $(".AddIncSearchChooser", "#lineItemdetailForm").hide();
    });

    /* Binding a Qtip for showing the pricing calculation steps after clicking the 'View Pricing Calculation' link on the Line Item screen */
	$("#pricingCalculationStep", "#lineItemdetailForm").qtip({
        content: {
            // Set the text to an image HTML string with the correct src URL to the loading image you want to use
            text: '<img src="./images/ajax-loader.gif" height="300px" width="650px" alt="Loading..." />',
            ajax: {
                url: '',
				type: 'POST',
				data: '', 
                once: false
            },
            title: {
                text: '<div style="height:18px;"><span style="float:left;"></span><span style="padding:4px 0 0 3px; float:left;">' + resourceBundle['label.generic.pricing.calculation.step'] + '</span></div>',
                button: 'Close'
            }
        },
        position: {
            at: 'left center', // Position the tool tip above the link
            my: 'right center',
            viewport: $(window), // Keep the tool tip on-screen at all times
            effect: false // Disable positioning animation
        },
        show: {
            event: 'click',
            solo: true // Only show one tool tip at a time
        },
        hide: 'unfocus',
        style: {
            classes: 'ui-tooltip-wiki ui-tooltip-tipped ui-tooltip-shadow'
        }
    });
	
	/* Binding a qtip with the View Ads Format icon besides the product drop down on the line item screen, for showing the selected Product's creative(s) summary within it. */ 
	$("#productCreativeSummary", "#lineItemdetailForm").qtip({
        content: {
            // Set the text to an image HTML string with the correct src URL to the loading image you want to use
            text: '<img src="./images/ajax-loader.gif" height="300px" width="650px" alt="Loading..." />',
            ajax: {
                url: '',
				type: 'POST',
				data: '', 
                once: false
            },
            title: {
                text: '<div style="height:18px;"><span style="float:left;"></span><span style="padding:4px 0 0 3px; float:left;">' + resourceBundle['label.generic.product.creatives.summary'] + '</span></div>',
                button: 'Close'
            }
        },
        position: {
        	at: 'bottom center', // Position the tool tip below the link
            my: 'top center',
            viewport: $(window), // Keep the tool tip on-screen at all times
            effect: false // Disable positioning animation
        },
        show: {
            event: 'click',
            solo: true // Only show one tool tip at a time
        },
        hide: 'unfocus',
        style: {
            classes: 'ui-tooltip-wiki ui-tooltip-tipped ui-tooltip-shadow'
        }
    });

	/* Binding a qtip with the icon besides the offered impressions field displayed only for Added Value line items, on the line item screen for showing the suggested impressions value. */
	$("#offImpressionLink", "#lineItemFinancialFieldsContainer").qtip({
        content: {
            // Set the text to an image HTML string with the correct src URL to the loading image you want to use
            text: '<img src="./images/ajax-loader.gif" height="200px" width="650px" alt="Loading..." />',
            ajax: {
                url: '',
				type: 'GET',
				data: '', 
                once: false,
                dataType: 'json',
                success: function(data){
					var displayHtml ="<div style='margin:5px 0 10px;font-size:16px;' class='label-bold'>"+data.objectMap.gridKeyColumnValue+"</div><div style='margin:5px 0;'><span>This value is what system is suggesting for this </br> line item to satify the added-value rules.</br></span></div>";					
                	this.set('content.text', displayHtml); 
					return false;
                 }
            },
            title: {
                text: 'Suggested impressions',
                button: 'Close'
            }
        },
        position: {
            at: 'right center', // Position the tool tip above the link
            my: 'left center',
            viewport: $(window), // Keep the tool tip on-screen at all times
            effect: false // Disable positioning animation
        },
        show: {
            event: 'click',
            solo: true // Only show one tool tip at a time
        },
        hide: 'unfocus',
        style: {
            classes: 'ui-tooltip-wiki ui-tooltip-tipped ui-tooltip-shadow'
        }
    });
	
	
	
	$('#lineItemdetailForm #STWeightSummary').click(function() {
		showSTWeightSummary();
	});
	
	/* Binding click event with the 'View Pricing Calculation' link on the Line Item screen */
    $('#lineItemdetailForm #pricingCalculationStep').click(function(){
        showPriceCalSummary();
    });
	
    /* Binding click event with the 'Avails Status' link above the line item grid on the Line Item screen. */
	$('#availStatus','#availStatuslink').click(function() {
		proposalAvailStatus();
	});
	
	$('#selectall').click(function() {
		selectProposalAllLineItemCheckbox();
	});
	
	/* Binding click event with the icon besides the offered impressions field displayed only for Added Value line items on the Line Item screen. */
	$('#offImpressionLink' , '#lineItemFinancialFieldsContainer').click(function(){
		showOfferedImpressions();
    });
	
	/* Binding the change event with the product drop down on the Line item screen, to be fired whenever value for the product drop down will be changed. */
    $("#sosProductId", "#lineItemdetailForm").change(function(){
    	$("#productCreativeSummary", "#lineItemdetailForm").qtip("hide");
    	$("#pricingCalculationStep", "#lineItemdetailForm").qtip("hide");
    	var productOptGrp = $("#sosProductId option:selected").parent("optgroup").attr("label");
    	if($("#sosProductId", "#lineItemdetailForm").val() != "" && $("#proposalOwnerRole", "#buildProposalParentDiv").val() == 'PLR' && $("#lineItemType_r", "#lineItemTargeting").is(':checked')
    			&& (productOptGrp == 'HOME PAGE' || productOptGrp == 'Display Cross Platform') && $("#flagForOnChange", "#buildProposalParentDiv").val() == 'true'){
            $("#reserveChkBoxContainer", "#lineItemCalendarContainer").show();
    		$("#reqForReservation", "#lineItemCalendarContainer").hide();
    		$("#reservationExpiryDate", "#lineItemTargeting").val('');
    		$("#reserved", "#lineItemdetailForm").removeAttr('checked');
    		$("#reserved", "#lineItemdetailForm").attr('disabled', 'disabled');
    		$("#reservedChkBoxSpan").addClass('disabled-checkbox');
        }else{
        	$("#reserved", "#lineItemdetailForm").removeAttr('checked').removeAttr('disabled');
        	$("#reservedChkBoxSpan").removeClass('disabled-checkbox')
        	$("#reservationExpiryDate", "#lineItemTargeting").val('');
        	$("#reserveChkBoxContainer", "#lineItemCalendarContainer").show();
			$("#reqForReservation", "#lineItemCalendarContainer").hide();
        }
    	
    	$('#lineItemdetailForm #sor').attr('readonly', false);
	    $('#lineItemdetailForm #sor').removeClass('input-textbox-readonly');
	    $('#lineItemdetailForm #calculateSOR').removeAttr("disabled");
        $('#lineItemdetailForm #calculateSOR').removeClass("disabled-btn");
    	$("#renewReservation", "#lineItemTargeting").hide();
    	expirationDate = "";
    	$("#reservationExpiryDate").datepicker("destroy");
    	
    	/* Checking if the value of the product drop down is changed manually but not by the code and then setting the value of the Sales target and spec type fields appropriately. */ 
        if ($("#flagForOnChange", "#buildProposalParentDiv").val() == 'true') {
            var gsr = $("#lineItemTable", "#buildProposalParentDiv").jqGrid('getGridParam', 'selrow');
            var productId = $("#lineItemTable", "#buildProposalParentDiv").jqGrid('getCell', gsr, 'sosProductId');
            clearAvails();
            
            if ($('#lineItemdetailForm #lineItemID').val() == 0) {
                $('#lineItemdetailForm #rate').val("");
            }
            
            $("#placementName", "#lineItemdetailForm").val("");
            jQuery("textarea", "#lineItemTargeting").change();
            $("#offImpressionLink", "#lineItemFinancialFieldsContainer").hide();
            if ($("#priceType", "#lineItemdetailForm").val() != "FLAT RATE") {
                $("#rateCardPrice", "#buildProposalParentDiv").val("");
            } else {
                $("#rateCardPrice", "#lineItemdetailForm").val("NA");
            }
            
			$("#priceCalSummary", "#lineItemdetailForm").val("");
			
			/* Reseting the sales target drop down values appropriately based on the product selected */
            if ($("#sosProductId", "#lineItemdetailForm").val() == "") {
            	
            	/* Clearing the Sales Target drop down data in case no product will be selected */
            	 $('#sosSalesTargetId > option').remove();
            	if ($("#lineItemType_s", "#lineItemTargeting").is(':checked')) {	               
	                $("#sosSalesTargetId", "#buildProposalParentDiv").multiselect("refresh");
            	}else{
            		$("#sosSalesTargetId", "#buildProposalParentDiv").select2('data', null);
            	}
            	
            	/* Setting the Spec type value to 'Rich Media' if the price type selected is 'CPM' or 'Custom Unit' else sets it's value to 'Standard' */
                if ($("#priceType", "#lineItemdetailForm").val() == "CPM" || $("#priceType", "#lineItemdetailForm").val() == "CUSTOM UNIT"){
                	$("#specType", "#buildProposalParentDiv").val('RICH_MEDIA');
                	$("#specType", "#buildProposalParentDiv").multiselect("refresh");
                }else{
                	$("#specType", "#buildProposalParentDiv").val('STANDARD');
					$("#specType", "#buildProposalParentDiv").multiselect("refresh");
                }
            } else {
            	/* Resetting the values of the Sales Target drop down with the list of Sales Target associated with the selected product */
                setsosSalesTargetIdValueOfProposal($("#sosProductId", "#lineItemdetailForm").val());
                
                /* In case the Price Type of the line item is 'CPM' or 'Custom Unit', fetching the list of all the Spec types associated with all the creatives associated with the selected
                 * product. In case spec type list fetched is empty, sets the value of the Spec type to 'Rich Media' otherwise all the Spec type values returned in the list will be selected.
                 * In case the Price type is not CPM and Custom Unit , spec type will be set to 'Standard'.
                 */
                if ($("#priceType", "#lineItemdetailForm").val() == "CPM" || $("#priceType", "#lineItemdetailForm").val() == "CUSTOM UNIT"){
	                var ajaxReq = new AjaxRequest();
	            	ajaxReq.url = "./proposalWorkflow/getProductCreativesSpecTypeLst.action?productId=" + $("#sosProductId", "#lineItemdetailForm").val();
	            	ajaxReq.success = function(result, status, xhr) {
	            		var specTypes = result.objectMap.gridKeyColumnValue;
	            		if(specTypes == ""){
	            			$("#specType", "#buildProposalParentDiv").val('RICH_MEDIA');
	    					$("#specType", "#buildProposalParentDiv").multiselect("refresh");
	            		}else{
	            			selectSpecType(specTypes);
	            		}
	            	};
	            	ajaxReq.submit();
    			}else{
    				var specType = ["STANDARD"];
    				selectSpecType(specType);
    			}
            }
            $('#isViewable').val($("option:selected", this).attr('is-viewable')=='Y' ? '1' : '0');
        }
        
        /* Hiding the view Ads format icon in case the product selected is blank else show it. */
        if($("#sosProductId", "#lineItemdetailForm").val() == ""){
        	$('#lineItemdetailForm #productCreativeSummary').hide();
        } else {
        	 $('#lineItemdetailForm #productCreativeSummary').show();
        }	
        
		enableDisableCalendar();
		enableLineItemDefaults();
    });  
	
    /**
     * Add dialog box for loading msg
     */ 
    $("#availProgress").dialog({
        dialogClass: 'alert',
        title: 'Fetching Avails...',
        autoOpen: false,
        resizable: false,
        showTitlebar: true,
        modal: true,
        height: 100,
        width: 225
    });
	
    /* Modal Dialog for the goal seek feature. Dialog is opened for getting the user confirmation on the field to be recalculated among 'Offered Impressions' and 'CPM', in case the total 
     * investment is changed by the user manually for the line item whose CPM and offered impressions are not null and is greater than zero. 
     */
    var calculateGoalSeekModal = new ModalDialog();
    calculateGoalSeekModal.width = 290;
	calculateGoalSeekModal.closeOnEscape = false;
    calculateGoalSeekModal.buttons = [{
        text: "Ok ",
        click: function(){
            var goalSeekValue = $('input[name=goalSeekValue]:checked').val();
            var rate = $("#rate", "#lineItemdetailForm").val();
            var impressionTotal = $("#impressionTotal", "#lineItemdetailForm").val();
            var totalInvestment = $("#totalInvestment", "#lineItemdetailForm").val();
            var parseTotalInvestment = totalInvestment.replace(/,/g, '');
            var parseImpressionTotal = impressionTotal.replace(/,/g, '');
            var parseRate = rate.replace(/,/g, '');
            var calculation = 0.0;
            if (goalSeekValue == "impressionTotal" && parseRate > 0.0) {
                calculation = (parseTotalInvestment * 1000) / parseRate;
                calculation = parseInt(calculation);
                var ImpressionTotalObject = $("#impressionTotal", "#lineItemdetailForm");
                ImpressionTotalObject.val(formatNumber(calculation));
                calculateSOV();
            } else if (goalSeekValue == "impressionTotal" && parseRate == 0.0) {
                var ImpressionTotalObject = $("#impressionTotal", "#lineItemdetailForm");
                ImpressionTotalObject.val(formatNumber("0"));
                calculateSOV();
            } else if (goalSeekValue == "rate" && parseImpressionTotal > 0.0) {
                calculation = (parseTotalInvestment * 1000) / parseImpressionTotal;
                calculation = roundoffto2places(calculation);
                var RateObject = $("#rate", "#lineItemdetailForm");
                RateObject.val(formatDecimal(calculation));
                calculateDiscountPercent();
            } else {
                var RateObject = $("#rate", "#lineItemdetailForm");
                RateObject.val(formatDecimal("0.00"));
            }
            $(this).dialog("close");
        }
    }];
    $("#chooseGoalSeek").dialog(calculateGoalSeekModal);
	$("#chooseGoalSeek").closest('.ui-dialog').find('.ui-dialog-titlebar-close').hide();
	enableDialogButton("chooseGoalSeek");
	
	/**
	 * Toggle feature for the Line item exceptions to show hide the exceptions on clicking its plus and minus icon respectively.
	 * @param {Object} e
	 */
    $("#lineItemExceptionsContainer img", "#lineItemdetailForm").toggle(function(e){
        $("#lineItemExceShowhide", "#lineItemdetailForm").hide();
        $(this).attr("src", "./images/redplus_icon.png");
    }, function(e){
        $("#lineItemExceShowhide", "#lineItemdetailForm").show();
        $(this).attr("src", "./images/minus.png");
    });

    /* Associating a date picker with the start and end date fields on the line item screens */
    var datesProposalLineItem = jQuery('#startDate, #endDate').datepicker({
        autoSize: true,
        showOn: "both",
        buttonText: "Select Date",
        buttonImage: './images/calendar.gif',
        buttonImageOnly: true,
        numberOfMonths: 3,
        onSelect: function(selectedDate){
        	date = $.datepicker.parseDate($(this).data("datepicker").settings.dateFormat || $.datepicker._defaults.dateFormat, selectedDate, $(this).data("datepicker").settings);
            clearAvails();
            $("#flight", $('#lineItemdetailForm')).val("");
            if($("#lineItemType_e", "#lineItemTargeting").is(':checked') ){
				$("#emailReservationStatus", "#lineItemTargeting").val("");
				$("#endDate","#buildProposalParentDiv").val($("#startDate","#buildProposalParentDiv").val());
			}
			enableDisableCalendar();
			$("#sor", "#lineItemTargeting").val('');
			var productOptGrp = $("#sosProductId option:selected").parent("optgroup").attr("label");
			if($("#sosProductId", "#lineItemdetailForm").val() != "" && $("#proposalOwnerRole", "#buildProposalParentDiv").val() == 'PLR' && $("#lineItemType_r", "#lineItemTargeting").is(':checked')
					&& (productOptGrp == 'HOME PAGE' || productOptGrp == 'Display Cross Platform')){
				$("#reserveChkBoxContainer", "#lineItemCalendarContainer").show();
	    		$("#reqForReservation", "#lineItemCalendarContainer").hide();
	    		$("#reservationExpiryDate", "#lineItemTargeting").val('');
	    		$("#reserved", "#lineItemdetailForm").removeAttr('checked');
	    		$("#reserved", "#lineItemdetailForm").attr('disabled', 'disabled');
	    		$("#reservedChkBoxSpan").addClass('disabled-checkbox');
			}else{
				$("#reserved", "#lineItemdetailForm").removeAttr('checked').removeAttr('disabled');
	    		$("#reservedChkBoxSpan").removeClass('disabled-checkbox');
				$("#reservationExpiryDate", "#lineItemTargeting").val('');
				$("#reserveChkBoxContainer", "#lineItemCalendarContainer").show();
				$("#reqForReservation", "#lineItemCalendarContainer").hide();
			}
			$('#lineItemdetailForm #sor').attr('readonly', false);
 	        $('#lineItemdetailForm #sor').removeClass('input-textbox-readonly');
 	        $('#lineItemdetailForm #calculateSOR').removeAttr("disabled");
            $('#lineItemdetailForm #calculateSOR').removeClass("disabled-btn");
			$("#renewReservation", "#lineItemTargeting").hide();
			expirationDate = "";
			if ($("#priceType", "#lineItemdetailForm").val() == "FLAT RATE") {
	            $("#rateCardPrice", "#buildProposalParentDiv").val("NA");
	        }  else {
	            $("#rateCardPrice", "#buildProposalParentDiv").val("");
	        }
        },
		beforeShowDay: function(date){
	    	if($("#lineItemType_e", "#lineItemTargeting").is(':checked') && $('#sosSalesTargetId','#lineItemTargeting').val() != "" && $('#sosSalesTargetId','#lineItemTargeting').val() != null) {  
	        	var string = jQuery.datepicker.formatDate('mm/dd/yy', date);
	        	return [ emailAvailableDatesArray.indexOf(string) > -1 ];
	        }else{
	        	return [true];
	        }
		}
    });
	
    var datesPackageLineItemSearch = jQuery('#validFrom, #validTo').datepicker({
        autoSize: true,
        showOn: "both",
        buttonText: "Select Date",
        buttonImage: './images/calendar.gif',
        buttonImageOnly: true,
        numberOfMonths: 3,
        onSelect: function(selectedDate){
        	date = $.datepicker.parseDate($(this).data("datepicker").settings.dateFormat || $.datepicker._defaults.dateFormat, selectedDate, $(this).data("datepicker").settings);
        }
    });
    
    $("#startFromSearchReset", "#buildProposalParentDiv").bind("click", function(event, ui){
        $("#validFrom", "#buildProposalParentDiv").val("");
    });
	
    $("#startToSearchReset", "#buildProposalParentDiv").bind("click", function(event, ui){
        $("#validTo", "#buildProposalParentDiv").val("");
    });
	
    /* Binding the click event to the reset button besides the start date field on the line item screen. Following actions are performed on trigering the click event on the reset buton :
       	1) Clears any value populated in the start date field.
       	2) Clears the Avails, Avails date and Impressions capacity fields.
       	3) Clears SOV field value in case both flight dates and flight information will be blank.
       	4) Clears the value of the Rate Card Price field in case price type is not Flat Rate else sets its value to 'NA'. 
     */
    $("#startFromReset", "#buildProposalParentDiv").bind("click", function(event, ui){
        $("#startDate", "#buildProposalParentDiv").val("");
        var sov = $("#sov", "#buildProposalParentDiv").val();
        clearAvails();
        if ($("#flight", "#buildProposalParentDiv").val() != "" &&
        		($("#startDate", "#buildProposalParentDiv").val() == "" || $("#endDate", "#buildProposalParentDiv").val() == "")) {
            $("#sov", "#buildProposalParentDiv").val(sov);
        }
        
        /* Show/Hides the view calendar icon and calculate SOR button based on any of the field among Product, Sales Target, Start date and End Date is null or not. */
		enableDisableCalendar();
		$("#sor", "#lineItemTargeting").val('');
		var productOptGrp = $("#sosProductId option:selected").parent("optgroup").attr("label");
		if($("#sosProductId", "#lineItemdetailForm").val() != "" && $("#proposalOwnerRole", "#buildProposalParentDiv").val() == 'PLR' && $("#lineItemType_r", "#lineItemTargeting").is(':checked')
				&& (productOptGrp == 'HOME PAGE' || productOptGrp == 'Display Cross Platform')){
			$("#reserveChkBoxContainer", "#lineItemCalendarContainer").show();
    		$("#reqForReservation", "#lineItemCalendarContainer").hide();
    		$("#reservationExpiryDate", "#lineItemTargeting").val('');
    		$("#reserved", "#lineItemdetailForm").removeAttr('checked');
    		$("#reserved", "#lineItemdetailForm").attr('disabled', 'disabled');
    		$("#reservedChkBoxSpan").addClass('disabled-checkbox');
		}else{
			$("#reserved", "#lineItemdetailForm").removeAttr('checked').removeAttr('disabled');
    		$("#reservedChkBoxSpan").removeClass('disabled-checkbox');
			$("#reservationExpiryDate", "#lineItemTargeting").val('');
			$("#reserveChkBoxContainer", "#lineItemCalendarContainer").show();
			$("#reqForReservation", "#lineItemCalendarContainer").hide();
		}
		$('#lineItemdetailForm #sor').attr('readonly', false);
	    $('#lineItemdetailForm #sor').removeClass('input-textbox-readonly');
	    $('#lineItemdetailForm #calculateSOR').removeAttr("disabled");
        $('#lineItemdetailForm #calculateSOR').removeClass("disabled-btn");
		$("#renewReservation", "#lineItemTargeting").hide();
		expirationDate = "";
		if ($("#priceType", "#lineItemdetailForm").val() == "FLAT RATE") {
            $("#rateCardPrice", "#buildProposalParentDiv").val("NA");
        }  else {
            $("#rateCardPrice", "#buildProposalParentDiv").val("");
        }
    });	
	
    /* Binding the click event to the reset button besides the end date field on the line item screen. Following actions are performed on trigering the click event on the reset buton :
   		1) Clears any value populated in the end date field.
   		2) Clears the Avails, Avails date and Impressions capacity fields.
   		3) Clears SOV field value in case both flight dates and flight information will be blank.
   		4) Clears the value of the Rate Card Price field in case price type is not Flat Rate else sets its value to 'NA'. 
     */
    $("#endFromReset", "#buildProposalParentDiv").bind("click", function(event, ui){
        $("#endDate", "#buildProposalParentDiv").val("");
        var sov = $("#sov", "#buildProposalParentDiv").val();
        clearAvails();
        if ($("#flight", "#buildProposalParentDiv").val() != "" && 
				($("#startDate", "#buildProposalParentDiv").val() == "" || $("#endDate", "#buildProposalParentDiv").val() == "")) {
            $("#sov", "#buildProposalParentDiv").val(sov);
        }
        
        /* Show/Hides the view calendar icon and calculate SOR button based on any of the field among Product, Sales Target, Start date and End Date is null or not. */
		enableDisableCalendar();
		$("#sor", "#lineItemTargeting").val('');
		var productOptGrp = $("#sosProductId option:selected").parent("optgroup").attr("label");
		if($("#sosProductId", "#lineItemdetailForm").val() != "" && $("#proposalOwnerRole", "#buildProposalParentDiv").val() == 'PLR' && $("#lineItemType_r", "#lineItemTargeting").is(':checked')
				&& (productOptGrp == 'HOME PAGE' || productOptGrp == 'Display Cross Platform')){
			$("#reserveChkBoxContainer", "#lineItemCalendarContainer").show();
    		$("#reqForReservation", "#lineItemCalendarContainer").hide();
    		$("#reservationExpiryDate", "#lineItemTargeting").val('');
    		$("#reserved", "#lineItemdetailForm").removeAttr('checked');
    		$("#reservedChkBoxSpan").addClass('disabled-checkbox');
    		$("#reserved", "#lineItemdetailForm").attr('disabled', 'disabled');
		}else{
			$("#reserved", "#lineItemdetailForm").removeAttr('checked').removeAttr('disabled');
			$("#reservationExpiryDate", "#lineItemTargeting").val('');
    		$("#reservedChkBoxSpan").removeClass('disabled-checkbox');
			$("#reserveChkBoxContainer", "#lineItemCalendarContainer").show();
			$("#reqForReservation", "#lineItemCalendarContainer").hide();
		}
		$('#lineItemdetailForm #sor').attr('readonly', false);
	    $('#lineItemdetailForm #sor').removeClass('input-textbox-readonly');
	    $('#lineItemdetailForm #calculateSOR').removeAttr("disabled");
        $('#lineItemdetailForm #calculateSOR').removeClass("disabled-btn");
		$("#renewReservation", "#lineItemTargeting").hide();
		expirationDate = "";
		if ($("#priceType", "#lineItemdetailForm").val() == "FLAT RATE") {
            $("#rateCardPrice", "#buildProposalParentDiv").val("NA");
        }  else {
            $("#rateCardPrice", "#buildProposalParentDiv").val("");
        }
    });
		
    $("input[class=numericdecimal]", "#lineItemdetailForm").numeric({
        negative: false
    });
    $("input[class=numeric]", "#lineItemdetailForm").numeric({
        negative: false
    });
    
    $("input[class=numericdecimal]", "#buildProposalSearchForm").numeric({
        negative: false
    });
    
    $("input[class=numeric]", "#buildProposalSearchForm").numeric({
        negative: false
    });
	 
    /* Modal dialog for getting the user confirmation for deleting the selected line items. */
	var multiDeleteConfirmationDlg = new ModalDialog();
	multiDeleteConfirmationDlg.height = 150;
	multiDeleteConfirmationDlg.width = 350;
	multiDeleteConfirmationDlg.resizable = false;
	multiDeleteConfirmationDlg.buttons = [{
	    text: "Yes",
	    click: function(){
	        $(this).dialog("close");
	        deleteLineItems();
	    }
	}, {
	    text: "No",
	    click: function(){
	        $(this).dialog("close");
	    }
	}];
	$("#multiLineItemDeleteContainer").dialog(multiDeleteConfirmationDlg);
	enableDialogButton("multiLineItemDeleteContainer");	
	
	/* Modal dialog for getting the user confirmation for splitting the selected line items. */
	var splitLineItemConfirmationDlg = new ModalDialog();
	splitLineItemConfirmationDlg.height = 150;
	splitLineItemConfirmationDlg.width = 350;
	splitLineItemConfirmationDlg.resizable = false;
	splitLineItemConfirmationDlg.buttons = [{
	    text: "Yes",
	    click: function(){
	        $(this).dialog("close");
	        splitLineItems();
	    }
	}, {
	    text: "No",
	    click: function(){
	        $(this).dialog("close");
	    }
	}];
	$("#splitLineItemsContainer").dialog(splitLineItemConfirmationDlg);
	enableDialogButton("splitLineItemsContainer");	
	
	
	/* Creating the JQ Grid For Proposal Line Items. */
    var proposalLineItemsGridOpte = new proposalJqGridBaseOptions(); 
    proposalLineItemsGridOpte.url = "./manageProposal/getLineItemsForProposal.action?optionId="+$("#optionId","#buildProposalSearchForm").val()+"&proposalVersion="+$("#proposalversion","#buildProposalSearchForm").val();
    proposalLineItemsGridOpte.colNames = [
		'<input type="checkbox" id="LineItemMultiSelect_Header"  name="LineItemMultiSelect" onClick="customLineItemSelect(this)">','Type','ID',
		'Sales TargetId','ProductId','packageId','Seq No.','Placement Name','CPM',
		'Offered Impressions','Total Investment','Start Date','End Date','lineItemExceptions','Reserved' ,'Pricing Status',
		'ProductType','Viewable'
    ];
    proposalLineItemsGridOpte.colModel = [
		{name:"customSelect", index:"customSelect", sortable:false, key:false, formatter:lineItemMultiSelectFormatter, width:35},
		{name:"lineItemType", index:"lineItemType", sortable:false, key:false, formatter:lineItemTypeFormatter, width:45},
		{name:"lineItemID",index:"lineItemID",width:40, align:"right" ,key:true},
		{name:"sosSalesTargetId",index:"sosSalesTargetId",hidden:true},
		{name:"sosProductId",index:"sosProductId",hidden:true},
		{name:"packageId",index:"packageId",hidden:true},
		{name:"lineItemSequence",index:"lineItemSequence", width: 55,align:"right"},
		{name:"placementName",index:"placementName",width:120},
		{name:"rate",index:"rate" , align:"right",width:50},
		{name:"impressionTotal",index:"impressionTotal" , align:"right",width:150},
		{name:"totalInvestment",index:"totalInvestment" , align:"right",width:120},
		{name:"startDate",index:"startDate",align:"center",width:80},
		{name:"endDate",index:"endDate",align:"center",width:80},
		{name:"lineItemExceptions",index:"lineItemExceptions",hidden:true},
		{name:"reserved",index:"reserved", sortable:false, formatter:reservationFormatter,width:80},
		{name:"pricingStatus",index:"pricingStatus",width:100, align:"left"},
		{name:"productType",index:"productType",hidden:true},
		{name:"viewableDisplayName",index:"viewableDisplayName",width:100, align:"left"}
	];
    proposalLineItemsGridOpte.caption = resourceBundle['label.buildproposal.accordiontext.lineitems'];
    proposalLineItemsGridOpte.pager = $("#lineItemPager");
    proposalLineItemsGridOpte.sortname = lineItemGridSortName;
	proposalLineItemsGridOpte.rowList = [10,20,40,100];
	proposalLineItemsGridOpte.height = 250;
	/* Function to be executed on selecting any row from the Proposal Line Item grid. */
    proposalLineItemsGridOpte.onSelectRow = function(id){
    	var gsr = $("#lineItemTable", "#buildProposalParentDiv").jqGrid('getGridParam', 'selrow');
        if (gsr) {
        	
        	/* Calling the function for fetching the details of the line item representing the row being selected from the grid and populates its details in the line item detail section
        	 * of the line item screen.
        	 */
			getLineItemConsolidatedData(gsr);
            $("#flagForOnChange", "#buildProposalParentDiv").val('false');
            if ($("#buildProposalDetailFormTab-4", "#buildProposalParentDiv").is(":visible")) {
                $("#buildProposalDetailFormTab-4", "#buildProposalParentDiv").hide();
                var index = $('#buildProposalTab a[href="#lineItem"]').parent().index();
                $('#buildProposalTab').tabs('select', index);//Code for moving the focus on to a tab from different tab
            }
            clearCSSErrors('#lineItemdetailForm');
            $('#messageHeaderFinancialDiv').html('');
            removeDivCssLineItem();
			
        }
        $("#buildProposalDetailFormTab-1 a", "#buildProposalParentDiv").text(resourceBundle['tab.generic.lineItemDetails']);
    };

    proposalLineItemsGridOpte.afterGridCompleteFunction = function(){
        if ($("#lineItemTable", "#buildProposalParentDiv").getDataIDs().length == 0) {
            $("#buildProposalTab").hide();
            $('#availStatuslink', '#proposalLineItemContainer').hide();
            $('#pricingStatusInfo', '#proposalLineItemContainer').val("");
        } else {
            $("#buildProposalTab").show();
            $('#availStatuslink', '#proposalLineItemContainer').show();
        }
        $('#pricingStatusInfo', '#proposalLineItemContainer').show();
        
        var rowIds = $("#lineItemTable", "#buildProposalParentDiv").jqGrid('getCol', 'lineItemID', false);
        for (var i = 0; i < rowIds.length; i++) {
            var colData = $("#lineItemTable", "#buildProposalParentDiv").getRowData(rowIds[i])['lineItemExceptions'];
            if (colData == 'Yes') {
                $("#" + rowIds[i], "#lineItemTable").find("td").addClass("expire-package");
            }
        }
        $("#LineItemMultiSelect_Header").attr("checked", false);
        loadHelpContent("pricingStatusHelp");
    };
	
    /* Enabling/Disabling the default add, edit, delete, search and reload buttons on the Proposal Line Item Grid's Pager. */
    $("#lineItemTable", "#buildProposalParentDiv").jqGrid(proposalLineItemsGridOpte).navGrid("#lineItemPager", {
        edit: false, search: false, del: false,
        /* Function to be executed on clicking the Add button on the Pager of the Line Items Grid. Used for creating a new line item. */ 
        addfunc: function(rowid){
        	
        	/* Clears the value of all the fields in the line item detail section and sets their respective default value if there is any. */
        	$('#targetingString','#lineItemTargeting').html('');
            $('#pricingSummary').html('');
            $('#creativeSummary').html('');
            var index = $('#buildProposalTab a[href="#lineItem"]').parent().index();
            $('#buildProposalTab').tabs('select', index);//Code for moving the focus on to a tab from different tab
            $("#buildProposalTab").show();
            $("#buildProposalDetailFormTab-4").show();
            resetRowSelectionOnAdding("#lineItemTable", "#buildProposalParentDiv");
            $("#searchType[value=package]", "#buildProposalParentDiv").attr('checked', 'checked');
            $("#lineItemID", "#lineItemdetailForm").val(0);
            $("#lineItemType_s").attr('disabled', false);
            $("#lineItemType_r").attr('disabled', false);
            $("#lineItemType_e").attr('disabled', false);
            $("#productType", "#lineItemFinancialFieldsContainer").val('S');
            resetFields();
            $('#lineItemdetailForm #cpm').attr('checked', 'checked');
            onClickPriceTypeRadio('CPM');
            var index = $('#buildProposalTab a[href="#lineItem"]').parent().index();
            $('#buildProposalTab').tabs('select', index);//Code for moving the focus on to a tab from different tab
            $("#buildProposalTab").show();
            $("#buildProposalDetailFormTab-4").show();
            $("#lineItemExceptionsContainer", "#lineItemdetailForm").hide();//To hide exception section
            resetSearchFields();
            clearCSSErrors('#lineItemdetailForm');
            $('#messageHeaderFinancialDiv').html('');
            removeDivCssLineItem();
            resetMultiSelectForProposal();
            reSetSpecType();
            $("option", $("#sosSalesTargetId", "#buildProposalParentDiv")).remove();
            $('#geoTargetsummary').find("tr:gt(0)").remove();//Removes all rows of a Target table except its header
            resetTargetingFields();
            if ($("#priceType", "#lineItemdetailForm").val() != "FLAT RATE") {
                $('#lineItemdetailForm #calculateBasePrice').removeAttr("disabled");
                $('#lineItemdetailForm #calculateBasePrice').removeClass("disabled-btn");
                $('#lineItemdetailForm #pricingCalculationStep').show();
            } else {
                $('#lineItemdetailForm #calculateBasePrice').attr("disabled", "disabled");
                $('#lineItemdetailForm #calculateBasePrice').addClass("disabled-btn");
                $('#lineItemdetailForm #pricingCalculationStep').hide();
            }
            $("#priceCalSummary", "#lineItemdetailForm").val("");
			$('#lineItemdetailForm #lineItemSosId').show();
			$('#lineItemdetailForm #tr_sosLineItemId').hide();
			$("#pricingStatus" , "#lineItemdetailForm").val("");
			enableDisableLineItemFields(false);
			jQuery("textarea", "#lineItemdetailForm").change();
			$("#offImpressionLink", "#lineItemFinancialFieldsContainer").hide();
			$('#lineItemdetailForm #productCreativeSummary').hide();
			$("#reserveChkBoxContainer", "#lineItemCalendarContainer").show();
    		$("#reqForReservation", "#lineItemCalendarContainer").hide();
    	},
    	
    	/* Function to be executed on clicking the Reload button on the Pager of the Line Items Grid. Used for reloading the Line items grid. */ 
		beforeRefresh: function(){
			jQuery("#lineItemSearchOption", "#proposalLineItemContainer").val('lineItemId');
			jQuery("#lineItemSearchValue", "#proposalLineItemContainer").val('');
			reloadJqGridAfterAddRecord("lineItemTable", lineItemGridSortName);
		}	
    });
	 
    /* Adding a button on the Line Item's Grid pager for deleting the line items selected from the grid. */
    $("#lineItemTable", "#buildProposalParentDiv").jqGrid('navButtonAdd', "#lineItemPager", {
        caption: "", id: "deleteLineItem_lineItemTable", title: "Delete Line Item", buttonicon: 'ui-icon-trash',
        onClickButton: function(){
            var isRowSelected = false;
            $("#lineItemTable", "#buildProposalParentDiv").find(':checkbox').each(function(){
                if ($(this).is(":checked")) {
                    isRowSelected = true;
                }
            });
            if (isRowSelected) {
                $("#multiLineItemDeleteContainer").dialog("open");
            }
            else {
				jQuery("#runtimeDialogDiv").model({theme: 'Error', autofade: false, message: resourceBundle['error.no.lineItem.selected.for.deletion']});
            }
        }
    });
    
    /* Adding a button on the Line Item's Grid pager for coping the line items selected from the grid within the same proposal option. */
	$("#lineItemTable", "#buildProposalParentDiv").jqGrid('navButtonAdd', "#lineItemPager", {
         caption: "", id: "copyLineItem_lineItemTable", title: "Copy Line Item", buttonicon: 'ui-icon-copy',
         onClickButton: function(){
             var selectedIds = "";
             var isRowSelected = false;
             $("#lineItemTable", "#buildProposalParentDiv").find(':checkbox').each(function(){
                 if ($(this).is(":checked")) {
                     selectedIds = selectedIds + $(this).val() + ",";
                 }
             });
             if (selectedIds.length > 0) {
                 selectedIds = $.trim(selectedIds).substring(0, selectedIds.length - 1);
                 var ajaxReq = new AjaxRequest();
                 var propId = $("#proposalID", "#buildProposalSearchForm").val();
                 var lineItemid = $("#lineItemID", "#lineItemdetailForm").val();
                 var propVersion = $("#proposalversion", "#buildProposalSearchForm").val();
                 ajaxReq.url = './proposalWorkflow/copyLineItem.action?proposalID=' + propId + '&lineItemIds=' + selectedIds + '&proposalversion=' + propVersion + '&optionId=' + $("#optionId", "#buildProposalSearchForm").val();
                 ajaxReq.success = function(result, status, xhr){
                     getNetCPMImpressionsOfProposal();
                     reloadJqGridAfterAddRecord("lineItemTable", lineItemGridSortName);
					 jQuery("#runtimeDialogDiv").model({theme: 'Success', message: resourceBundle['message.generic.lineItemCopiedSuccessfully']});
                     $("#LineItemMultiSelect_Header").attr("checked", false);
                 };
                 ajaxReq.submit();
             } else {
			 	jQuery("#runtimeDialogDiv").model({theme: 'Error', autofade: false, message: resourceBundle['error.no.lineItem.selected.for.copy']});
             }
         }
	});

	/* Adding a button on the Line Item's Grid pager for updating the base price of the line items selected from the grid. */
	$("#lineItemTable", "#buildProposalParentDiv").jqGrid('navButtonAdd', "#lineItemPager", {
	    caption: "", id: "updatePrice_lineItemTable", title: "Update Base Price", buttonicon: 'ui-icon-bulkPricing',
	    onClickButton: function(){
	        var selectedIds = "";
	        var isRowSelected = false;
	        $("#lineItemTable", "#buildProposalParentDiv").find(':checkbox').each(function(){
	            if ($(this).is(":checked")) {
	                selectedIds = selectedIds + $(this).val() + ",";
	            }
	        });
	        if (selectedIds.length > 0) {
	            $("#multiLineItemPriceUpdateContainer").dialog("open");
	        }
	        else {
				jQuery("#runtimeDialogDiv").model({theme: 'Error', autofade: false, message: resourceBundle['error.no.lineItem.selected.for.update']});
	        }
	    }
	});
	
	/**
	 * Line Item sequence update qTip
	 */
	$("#lineItemTable", "#buildProposalParentDiv").sequence({pagerId: "lineItemPager", doSequence: arrangeLineItemSequence});
	
	/* Adding a button on the Line Item's Grid pager for coping the line items selected from the grid from the current option to other options of the proposal. */
	$("#lineItemTable", "#buildProposalParentDiv").jqGrid('navButtonAdd', "#lineItemPager", {
	    caption: "", id: "copyInOtherOption_lineItemTable", title: "Copy To Other Option", buttonicon: 'ui-icon-transfer-e-w',
	    onClickButton: function(){
	        var selectedIds = "";
	        $("#lineItemTable", "#buildProposalParentDiv").find(':checkbox').each(function(){
	            if ($(this).is(":checked")) {
	                selectedIds = selectedIds + $(this).val() + ",";
	            }
	        });
	        if (selectedIds.length > 0) {
	        	copyLineItemInOtherOption();
	        }
	        else {
				jQuery("#runtimeDialogDiv").model({theme: 'Error', autofade: false, message: resourceBundle['error.no.lineItem.selected.for.copyPaste']});
	        }
	    }
	});
	
	/* Adding a button on the Line Item's Grid pager for splitting the line items selected from the grid. */
	$("#lineItemTable", "#buildProposalParentDiv").jqGrid('navButtonAdd', "#lineItemPager", {
        caption: "", id: "splitLineItem_lineItemTable", title: "Split Line Item", buttonicon: 'ui-icon-split-item',
        onClickButton: function(){
            var selectedIds = "";
            $("#lineItemTable", "#buildProposalParentDiv").find(':checkbox').each(function(){
                if ($(this).is(":checked")) {
                    selectedIds = selectedIds + $(this).val() + ",";
                }
            });
            if (selectedIds.length > 0) {
            	selectedIds = $.trim(selectedIds).substring(0, selectedIds.length - 1);
                var ajaxReq = new AjaxRequest();
                ajaxReq.url = './proposalWorkflow/validateSplitLineItemsData.action?lineItemIds=' + selectedIds;
                ajaxReq.success = function(result, status, xhr){
                	if(result.objectMap.islineItemsSplittingAllowed){
                		if(result.objectMap.lineItemFlightsSpanSameMon){
                			$("#splitLineItemsConfirmationMsg" ,  "#splitLineItemsContainer").html(resourceBundle['error.lineitems.filght.date.span.same.month']);
                			$("#splitLineItemsContainer").dialog('option','height',200);
                		}else{
                			$("#splitLineItemsConfirmationMsg" ,  "#splitLineItemsContainer").html(resourceBundle['label.line.items.split.confirmation']);
                			$("#splitLineItemsContainer").dialog('option','height',150);
                		}
                		$("#splitLineItemsContainer").dialog("open");
                	}else{
                		jQuery("#runtimeDialogDiv").model({theme: 'Error', autofade: false, width: 300, height: 170, message: resourceBundle['error.cannot.split.selected.lineItems']});
                	}
                };
                ajaxReq.submit();
            }
            else {
				jQuery("#runtimeDialogDiv").model({theme: 'Error', autofade: false, message: resourceBundle['error.no.lineItem.selected.for.splitting']});
            }
        }
    });
	
	$("#splitLineItem_lineItemTable").removeClass("ui-icon");
	
	/* Binding the change event (To be triggered whenever the value of the associated field changes) with the Line Item's flight information field */
    $("#flight", '#lineItemdetailForm').change(function(){
        if ($("#flight", $('#lineItemdetailForm')).val() != "") {
            $("#startDate", "#buildProposalParentDiv").val("");
            $("#endDate", "#buildProposalParentDiv").val("");
            clearAvails();
            if(!(($("#startDate", "#buildProposalParentDiv").val() == "") && ($("#endDate", "#buildProposalParentDiv").val() == ""))){
            if ($("#priceType", "#lineItemdetailForm").val() == "FLAT RATE") {
	            $("#rateCardPrice", "#buildProposalParentDiv").val("NA");
	        }  else {
	            $("#rateCardPrice", "#buildProposalParentDiv").val("");
	        }
        }
            if(!$("#lineItemType_s", "#lineItemTargeting").is(':checked')){
            	$("#calculateSOR","#lineItemTargeting").hide();
            	$("#sor", "#lineItemTargeting").val('');
    		}
        }
        else {
            if (($("#startDate", "#buildProposalParentDiv").val() == "" || $("#endDate", "#buildProposalParentDiv").val() == "")) {
                $("#sov", "#buildProposalParentDiv").val("");
            }
            if ($("#priceType", "#lineItemdetailForm").val() == "FLAT RATE") {
	            $("#rateCardPrice", "#buildProposalParentDiv").val("NA");
	        }  else {
	            $("#rateCardPrice", "#buildProposalParentDiv").val("");
	        }
        }
    });
	
    /* Binding the focus out event with the Line items CPM field. */
    $("#rate", "#lineItemdetailForm").blur(function(){
         var rate = $("#rate", "#lineItemdetailForm").val();
         var impressionTotal = $("#impressionTotal", "#lineItemdetailForm").val();
         var totalInvestment = $("#totalInvestment", "#lineItemdetailForm").val();
         var calculation = 0.0;
         var priceType = $('#lineItemdetailForm #priceType').val();
         if (rate != "" && (priceType == "CPM" ||  priceType == "PRE EMPTIBLE" || priceType == "CUSTOM UNIT")) {
             var parseRate = rate.replace(/,/g, '');
             if (impressionTotal != "" && totalInvestment == 0.0) {
                 var parseImpressionTotal = impressionTotal.replace(/,/g, '');
                 calculation = (parseRate * parseImpressionTotal) / 1000;
                 calculation = roundoffto2places(calculation);
                 var totalInvestmentObject = $("#totalInvestment", "#lineItemdetailForm");
                 totalInvestmentObject.val(formatDecimal(calculation));
             } else if (totalInvestment != "" && impressionTotal == "" && parseRate > 0.0) {
                 var parseTotalInvestment = totalInvestment.replace(/,/g, '');
                 calculation = (parseTotalInvestment * 1000) / parseRate;
                 calculation = parseInt(calculation);
                 var ImpressionTotalObject = $("#impressionTotal", "#lineItemdetailForm");
                 ImpressionTotalObject.val(formatNumber(calculation));
                 calculateSOV();
             } else if (totalInvestment != "" && impressionTotal != "") {
                 var parseImpressionTotal = impressionTotal.replace(/,/g, '');
                 calculation = (parseRate * parseImpressionTotal) / 1000;
                 calculation = roundoffto2places(calculation);
                 var totalInvestmentObject = $("#totalInvestment", "#lineItemdetailForm");
                 totalInvestmentObject.val(formatDecimal(calculation));
             }
         }
    });
	 
    /* Binding the focus out event with the Line items Offered Impressions field. */
    $("#impressionTotal", "#lineItemdetailForm").blur(function(){
        var rate = $("#rate", "#lineItemdetailForm").val();
        var impressionTotal = $("#impressionTotal", "#lineItemdetailForm").val();
        var totalInvestment = $("#totalInvestment", "#lineItemdetailForm").val();
        var calculation = 0.0;
        var priceType = $('#lineItemdetailForm #priceType').val();
        if (impressionTotal != "" && (priceType == "CPM" ||  priceType == "PRE EMPTIBLE" || priceType == "CUSTOM UNIT")) {
            var parseImpressionTotal = impressionTotal.replace(/,/g, '');
            if (rate != "" && totalInvestment == 0.0) {
                var parseRate = rate.replace(/,/g, '');
                calculation = (parseRate * parseImpressionTotal) / 1000;
                calculation = roundoffto2places(calculation);
                var totalInvestmentObject = $("#totalInvestment", "#lineItemdetailForm");
                totalInvestmentObject.val(formatDecimal(calculation));
            } else if (totalInvestment != "" && rate == 0.0 && parseImpressionTotal > 0.0) {
                var parseTotalInvestment = totalInvestment.replace(/,/g, '');
                calculation = (parseTotalInvestment * 1000) / parseImpressionTotal;
                calculation = roundoffto2places(calculation);
                var RateObject = $("#rate", "#lineItemdetailForm");
                RateObject.val(formatDecimal(calculation));
            } else if (totalInvestment != "" && rate != "") {
                var parseRate = rate.replace(/,/g, '');
                calculation = (parseRate * parseImpressionTotal) / 1000;
                calculation = roundoffto2places(calculation);
                var totalInvestmentObject = $("#totalInvestment", "#lineItemdetailForm");
                totalInvestmentObject.val(formatDecimal(calculation));
            }
        }
        calculateSOV();
    });
	 
     $("#totalInvestment", '#lineItemdetailForm').blur(function(){
         var rate = $("#rate", "#lineItemdetailForm").val();
         var impressionTotal = $("#impressionTotal", "#lineItemdetailForm").val();
         var totalInvestment = $("#totalInvestment", "#lineItemdetailForm").val();
         var calculation = 0.0;
         var priceType = $('#lineItemdetailForm #priceType').val();
         
         if (totalInvestment != "" && (priceType == "CPM" ||  priceType == "PRE EMPTIBLE" || priceType == "CUSTOM UNIT")) {
             var parseTotalInvestment = totalInvestment.replace(/,/g, '');
             var parseImpressionTotal = impressionTotal.replace(/,/g, '');
             var parseRate = rate.replace(/,/g, '');
             if (rate != "" && impressionTotal == "" && parseRate > 0.0) {
                 calculation = (parseTotalInvestment * 1000) / parseRate;
                 calculation = parseInt(calculation);
                 var ImpressionTotalObject = $("#impressionTotal", "#lineItemdetailForm");
                 ImpressionTotalObject.val(formatNumber(calculation));
                 calculateSOV();
             } else if (impressionTotal != "" && rate == 0.0 && parseImpressionTotal > 0.0) {
                 calculation = (parseTotalInvestment * 1000) / parseImpressionTotal;
                 calculation = roundoffto2places(calculation);
                 var RateObject = $("#rate", "#lineItemdetailForm");
                 RateObject.val(formatDecimal(calculation));
             } else if (rate != "" && impressionTotal != "") {
                 if (totalInvestmentValueChanged()) {
                     $('input:radio[name=goalSeekValue]:nth(0)').attr('checked', true);
                     $("#chooseGoalSeek").dialog("open");
                 }
             }
         }
     });
     
     $("#totalInvestment", '#lineItemdetailForm').focus(function(){
         $("#oldInvestment", "#buildProposalParentDiv").val(formatDecimal($(this).val()));
     });
	 
	
	$("#totalPossibleImpressions", '#lineItemdetailForm').focus(function(){
      	$("#totalPossibleImpressions", '#lineItemdetailForm').val($("#totalPossibleImpressions", '#lineItemdetailForm').val().replace(/,/g, ''));
	});

	$("#totalPossibleImpressions", '#lineItemdetailForm').blur(function(){
		$('#totalPossibleImpressions','#lineItemdetailForm').val(formatNumber($(this).val()));
	});
	 
	 
     $("#sov", $('#lineItemdetailForm')).blur(function(){
         var sov = $("#sov", '#lineItemdetailForm').val();
         var parseSOV = sov.replace(/,/g, '');
         var totalPossibleImpressions = $("#totalPossibleImpressions", '#lineItemdetailForm').val();
         var parseTotalPossibleImpressions = totalPossibleImpressions.replace(/,/g, '');
         if (!isNaN(parseSOV) && !isNaN(parseTotalPossibleImpressions) && parseSOV != 0 && parseTotalPossibleImpressions != 0) {
             var impressionTotal = (parseSOV * parseTotalPossibleImpressions) / 100;
             impressionTotal = parseInt(impressionTotal);
             var impressionTotalObject = $("#impressionTotal", '#lineItemdetailForm');
             impressionTotalObject.val(formatNumber(impressionTotal));
         }
         
         var priceTypeVal = $('#priceType', '#lineItemdetailForm').val();
         if (priceTypeVal == "CPM" ||  priceTypeVal == "PRE EMPTIBLE" || priceTypeVal == "CUSTOM UNIT") {
             var rate = $("#rate", "#lineItemdetailForm").val();
             var parseRate = rate.replace(/,/g, '');
             var impressionTotal2 = $("#impressionTotal", "#lineItemdetailForm").val();
             var parseImpressionTotal2 = impressionTotal2.replace(/,/g, '');
             if (!isNaN(parseRate) && !isNaN(parseImpressionTotal2)) {
                 var totInvestment = (parseRate * parseImpressionTotal2) / 1000;
                 totInvestment = roundoffto2places(totInvestment);
                 var totalInvestmentObject = $("#totalInvestment", "#lineItemdetailForm");
                 totalInvestmentObject.val(formatDecimal(totInvestment));
             }
         }
     });
    
    /* Binding the change event with the Line Item's Price Type field */
    $('#lineItemdetailForm #priceType').change(function(){
    	/* Calling the function to show/hide and enable/disable the pricing related fields including CPM, Total investment and Calculate Price button based on the price type selected. */
        onClickPriceTypeRadio($(this).val());
        $("#priceCalSummary", "#lineItemdetailForm").val("");
        $("#offImpressionLink", "#lineItemFinancialFieldsContainer").hide();
        if ($("#priceType", "#lineItemdetailForm").val() != "FLAT RATE") {
        	/* Setting the value of the spec type to 'Standard' if the price type is either 'Pre Emptible' or 'Added Value'. In case the Price Type of the line item is 'CPM' or 'Custom Unit', 
        	 * fetching the list of all the Spec types associated with all the creatives associated with the selected product. In case spec type list fetched is empty, sets the value of 
        	 * the Spec type to 'Rich Media' otherwise all the Spec type values returned in the list will be selected. 
        	 */
        	if($('#lineItemdetailForm #priceType').val() == "PRE EMPTIBLE" || $('#lineItemdetailForm #priceType').val() == "ADDED VALUE" ){
        		$("#specType", "#buildProposalParentDiv").val('STANDARD');
        		$("#specType", "#buildProposalParentDiv").multiselect("refresh");
			}else{
				if($("#sosProductId", "#lineItemdetailForm").val() != ""){
					var ajaxReq = new AjaxRequest();
	            	ajaxReq.url = "./proposalWorkflow/getProductCreativesSpecTypeLst.action?productId=" + $("#sosProductId", "#lineItemdetailForm").val();
	            	ajaxReq.success = function(result, status, xhr) {
	            		var specTypes = result.objectMap.gridKeyColumnValue;
	            		if(specTypes == ""){
	            			$("#specType", "#buildProposalParentDiv").val('RICH_MEDIA');
	    					$("#specType", "#buildProposalParentDiv").multiselect("refresh");
	            		}else{
	            			selectSpecType(specTypes);
	            		}
	            	};
	            	ajaxReq.submit();
				}else{
					$("#specType", "#buildProposalParentDiv").val('RICH_MEDIA');
					$("#specType", "#buildProposalParentDiv").multiselect("refresh");
				}
			}
        	
        	/* clearing the value of the Rate Card Price and setting the value of CPM and totalInvestment and Offered Impressions appropriately. */
            $("#rateCardPrice", "#lineItemdetailForm").val("");
            var impressionTotal = $("#impressionTotal", "#lineItemdetailForm").val();
            var totalInvestment = $("#totalInvestment", "#lineItemdetailForm").val();
            var calculation = 0.0;
            var parseTotalInvestment = totalInvestment.replace(/,/g, '');
            var parseImpressionTotal = impressionTotal.replace(/,/g, '');
            if (parseTotalInvestment >= 0.0 && parseImpressionTotal > 0.0) {
                var parseTotalInvestment = totalInvestment.replace(/,/g, '');
                calculation = (parseTotalInvestment * 1000) / parseImpressionTotal;
                calculation = roundoffto2places(calculation);
                $("#rate", "#lineItemdetailForm").val(formatDecimal(calculation));
            } else if (impressionTotal == "" || parseImpressionTotal == 0.0) {
                $("#rate", "#lineItemdetailForm").val(formatDecimal(calculation));
                $("#totalInvestment", "#lineItemdetailForm").val(formatDecimal(calculation));
                $("#impressionTotal", "#lineItemdetailForm").val(formatNumber(calculation));
            }
            $('#lineItemdetailForm #pricingCalculationStep').show();
        } else {
            $("#rateCardPrice", "#lineItemdetailForm").val("NA");
            $('#lineItemdetailForm #calculateBasePrice').attr("disabled", "disabled");
            $('#lineItemdetailForm #calculateBasePrice').addClass("disabled-btn");
            $('#lineItemdetailForm #pricingCalculationStep').hide();
            $("#specType", "#buildProposalParentDiv").val('STANDARD');
            $("#specType", "#buildProposalParentDiv").multiselect("refresh");
        }
    });	
		
    $('#lineItemdetailForm #ImpressionsCount').change(function(){
        onClickProposalImpressionCountInfo($(this).val());
    });
    
    /**
     * Call check Avails Validation of availsCheck  
     */
    $('#lineItemdetailForm #availsCheck').click(function(){
        if (validateLineItemTargeting()) {
            populateAvailsFromYieldex();
        }
    });
    
    /**
     * Clear avails and capacity only on clicking on clear avails link
     */
    $('#lineItemdetailForm #clearAvails').click(function(){
        $('#messageHeaderFinancialDiv').html('');
        var sov = $("#sov", "#buildProposalParentDiv").val();
        clearAvails();
        if ($("#flight", "#buildProposalParentDiv").val() != "" &&
        ($("#startDate", "#buildProposalParentDiv").val() == "" ||
        $("#endDate", "#buildProposalParentDiv").val() == "")) {
            $("#sov", "#buildProposalParentDiv").val(sov);
        }
    });
    
    /* Binding change event with the Line Items targeting type field. Event is triggered on changing the value of the target type drop dow on line item screen. */
    $('#sosTarTypeId').change(function(){
    	/* Calling function for resetting the Target elements based on teh target Type selected. */
        fillTargetTypeElementForLineItem();
    });
	
	/**
	 *  View line item detail start
	 */	
	var viewLineItemFormManager = new FormManager();
	viewLineItemFormManager.formName = 'viewLineItemAttributesForm';
	
	var viewLineItemDetailsDialog = new ModalDialog();
	viewLineItemDetailsDialog.height = 500;
	viewLineItemDetailsDialog.width = 900;	
    viewLineItemDetailsDialog.buttons = [{
        text: "Save",
        click: function(){
            var viewLineItemForm = new JQueryAjaxForm(viewLineItemFormManager);
            viewLineItemForm.submit();
        }
    }, {
        text: "Cancel",
        click: function(){
            $(this).dialog("close");
        }
    }];
	
	$( '#viewLineItemAttributes','#buildProposalParentDiv').dialog(viewLineItemDetailsDialog);
	enableDialogButton("viewLineItemAttributes");
    $("#lineItemSaveData", "#buildProposal_buttons").click(function(){
    	/* Calling the function to save the line items data on click of the save button on the line items screen. */
        lineItemSaveData();
    });
	
    /* Binding the click event with the line items reset button. It resets the values of all the fields of the line item screen and sets their values either to default in case of adding 
     * the new line item else resets the values of the fields to the values being last saved for the line item being updated.
     */
    $("#lineItemResetData", "#buildProposal_buttons").click(function(){
        clearCSSErrors('#lineItemdetailForm');
        $('#messageHeaderFinancialDiv').html('');
        removeDivCssLineItem();
        if ($('#buildProposalDetailFormTab-1 a', '#buildProposalParentDiv').text() == resourceBundle['tab.generic.addNewLineItem']) {
            resetFields();
            reSetSpecType();
            $('#geoTargetsummary').find("tr:gt(0)").remove();
			enableDisableCalendar();
			enableLineItemDefaults();
			jQuery("textarea", "#lineItemdetailForm").change();
        } else {
            var gsr = jQuery("#lineItemTable", "#buildProposalParentDiv").jqGrid('getGridParam', 'selrow');
            if (gsr) {
                $("#flagForOnChange", "#buildProposalParentDiv").val('false');
				getLineItemConsolidatedData(gsr);
            }
        }
    });
	
	    
    /* Creating the Grid for displaying the Package details shown while adding the line items from the package to the proposal. */
	var proposalPackageGridOpts = new proposalJqGridBaseOptions();
	proposalPackageGridOpts.url = "./manageProposal/searchPackageForBuild.action";
	proposalPackageGridOpts.colNames = ['Package ID', 'Package Name', 'Package Owner', 'Start Date', 'End date', 'Breakable', 'Total Budget', 'Line Items', 'Expired','Action'];
	proposalPackageGridOpts.colModel = [
		{name:'packageId', index:'packageId', hidden:true, key:true}, 
		{name:'packageName', index:'packageName', sortable:false, key:false, width: 300}, 
		{name:'packageOwner', index:'packageOwner', sortable:false, key:false, width: 150}, 
		{name:'validFrom', index:'validFrom', sortable:false, key:false, width: 150}, 
		{name:'validTo', index:'validTo', sortable:false, key:false, width: 150}, 
		{name:'breakableStr', index:'breakableStr', sortable:false, key:false, width: 150}, 
		{name:'budget', index:'budget', sortable:false, key:false, width: 150, align:"right"}, 
		{name:'lineItemCount', index:'lineItemCount', sortable:false, hidden:false, key:false, width: 150, align:"right"}, 
		{name:'expired', index:'expired', hidden:true, key:false},
		{name:'lookup', index:'lookup', sortable:false, hidden:false, key:false, formatter:packagelineitemlinkFormatter, width: 120, title:false} 
	];
    proposalPackageGridOpts.emptyrecords = resourceBundle['label.generic.emptymessagesearchgrid'];
    proposalPackageGridOpts.pager = $("#searchPackagePager");
    proposalPackageGridOpts.sortname = "packageId";
    proposalPackageGridOpts.selectOnLoad = false; 
    proposalPackageGridOpts.height=236;
	   
    /* Disabling all the default buttons on the pager of the grid */
    $("#searchPackageTable","#buildProposalSearchForm").jqGrid(proposalPackageGridOpts).navGrid("#searchPackagePager", {
		edit: false, del: false, search: false, add: false, refresh:false
    });
	    
    $( '#buildProposalTab').tabs();
	showHideLineItemGridIcons($("#isreadOnly","#buildProposalParentDiv").val());
	setMultiSelectSalesTargetId();
	setSpecTypeForProposal();
	
	$("#sosSalesTargetId", "#lineItemTargeting").bind("change", function () {
		if($("#flagForOnChange", "#buildProposalParentDiv").val() == 'true'){
			getPlacementName();
			$("#rateCardPrice", "#buildProposalParentDiv").val("");
			$("#sor", "#lineItemTargeting").val('');
			$("#offImpressionLink", "#lineItemFinancialFieldsContainer").hide();
			if($("#lineItemType_e", "#lineItemTargeting").is(':checked')){
				$("#startDate","#lineItemfieldsContainer").val("");
			}
			var productOptGrp = $("#sosProductId option:selected").parent("optgroup").attr("label");
			if($("#sosProductId", "#lineItemdetailForm").val() != "" && $("#proposalOwnerRole", "#buildProposalParentDiv").val() == 'PLR' && $("#lineItemType_r", "#lineItemTargeting").is(':checked')
					&& (productOptGrp == 'HOME PAGE' || productOptGrp == 'Display Cross Platform')){
				$("#reserveChkBoxContainer", "#lineItemCalendarContainer").show();
	    		$("#reqForReservation", "#lineItemCalendarContainer").hide();
	    		$("#reservationExpiryDate", "#lineItemTargeting").val('');
	    		$("#reserved", "#lineItemdetailForm").removeAttr('checked');
	    		$("#reserved", "#lineItemdetailForm").attr('disabled', 'disabled');
	    		$("#reservedChkBoxSpan").addClass('disabled-checkbox');
			}else{
				$("#reserved", "#lineItemdetailForm").removeAttr('checked').removeAttr('disabled');
				$("#reservationExpiryDate", "#lineItemTargeting").val('');
				$("#reserveChkBoxContainer", "#lineItemCalendarContainer").show();
				$("#reqForReservation", "#lineItemCalendarContainer").hide();
	    		$("#reservedChkBoxSpan").removeClass('disabled-checkbox');
			}
			$('#lineItemdetailForm #sor').attr('readonly', false);
		    $('#lineItemdetailForm #sor').removeClass('input-textbox-readonly');
		    $('#lineItemdetailForm #calculateSOR').removeAttr("disabled");
	        $('#lineItemdetailForm #calculateSOR').removeClass("disabled-btn");
			$("#renewReservation", "#lineItemTargeting").hide();
			expirationDate = "";
			$("#reservationExpiryDate").datepicker("destroy");
		}
		enableDisableCalendar();
		enableLineItemDefaults();
		if($("#lineItemType_e", "#lineItemTargeting").is(':checked') && $('#sosSalesTargetId','#lineItemTargeting').val() != "" && $('#sosSalesTargetId','#lineItemTargeting').val() != null) {			
			var ajaxReq = new AjaxRequest();
			ajaxReq.url = "./proposalWorkflow/getEmailAvailableDates.action?sosProductId=" + $('#sosProductId','#lineItemTargeting').val() + "&sosSalesTargetId=" + $('#sosSalesTargetId','#lineItemTargeting').val();
			ajaxReq.success = function(result, status, xhr){
				emailAvailableDatesArray = result;
			}
			ajaxReq.submit();
		}
	});
	
	/**
	 * Initialized search panel on line item grid
	 */
	initGridSearchOptions("lineItemTable", "lineItemSearchPanel", "buildProposalParentDiv");
	
	/**
	 * Enable auto search on line Item grid
	 */
	enableAutoSearch(jQuery("#lineItemSearchValue", "#buildProposalParentDiv"), function() {
		jQuery("#lineItemTable").jqGrid('setGridParam', {
			url: './manageProposal/getLineItemsForProposal.action?optionId='+$("#optionId","#buildProposalSearchForm").val()+'&proposalVersion='+$("#proposalversion","#buildProposalSearchForm").val(), page: 1,
			postData: {
				searchField: jQuery("#lineItemSearchOption", "#buildProposalParentDiv").val(),
				searchString: $.trim(jQuery("#lineItemSearchValue", "#buildProposalParentDiv").val()),
				searchOper: 'eq'
			}
		}).trigger("reloadGrid");
	});
	
	/**
	 * Register change event on line item search option select box
	 */
	jQuery("#lineItemSearchOption", "#buildProposalParentDiv").bind("change", function () {
		jQuery("#lineItemSearchValue", "#buildProposalParentDiv").val('').focus();
	});
	
	jQuery("#comments", "#buildProposalParentDiv").limiter(500, $("#charsRemainingComments", "#buildProposalParentDiv"));
	jQuery("#placementName", "#buildProposalParentDiv").limiter(4000, $("#charsRemainingPlacement", "#buildProposalParentDiv"));
	//jQuery("#tarTypeElementText", "#lineItemTargeting").limiter(4000, $("#charsRemainingTarTypeElements", "#lineItemTargeting"));
	//$("#charsRemainingTarTypeElements", "#lineItemTargeting").hide();
});


function searchProposal(){
	var proposalname_mask = $("#proposalname", "#buildProposalSearchForm").val(); 
	var cpmFrom_mask = $("#cpmFrom", "#buildProposalSearchForm").val();
	var cpmTo_mask = $("#cpmTo", "#buildProposalSearchForm").val();
	var agencyName_mask = $("#agencyName", "#buildProposalSearchForm").val();
	var budgetFrom_mask = $("#budgetFrom", "#buildProposalSearchForm").val();
	var budgetTo_mask = $("#budgetTo", "#buildProposalSearchForm").val();
	var salescategory_mask = $("#salescategory", "#buildProposalSearchForm").val();
	var impressionFrom_mask = $("#impressionFrom", "#buildProposalSearchForm").val();
	var impressionTo_mask = $("#impressionTo", "#buildProposalSearchForm").val();
	var advertiserName_mask = $("#advertiserName", "#buildProposalSearchForm").val();
	var industry_mask = $("#industry", "#buildProposalSearchForm").val();
	var packageName_mask = $("#packageName", "#buildProposalSearchForm").val();
	var packageBudgetFrom_mask = $("#packageBudgetFrom", "#buildProposalSearchForm").val();
	var packageBudgetTo_mask = $("#packageBudgetTo", "#buildProposalSearchForm").val();
	var packageValidFrom_mask =  $('#validFrom').val();
	var packageValidTo_mask =  $('#validTo').val();
	var assignto_mask = $("#assignedUserId", "#buildProposalSearchForm").val();
	var cmpobjective_mask = jQuery("#campaignObjectiveObj", "#buildProposalSearchForm").val();
	var proposalStatus_mask = jQuery("#proposalStatus", "#buildProposalSearchForm").val();
	var searchUrl = "";
		searchUrl = './manageProposal/searchPackageForBuild.action?packageName='+encodeURIComponent(packageName_mask)+'&budgetFrom='+packageBudgetFrom_mask+'&searchType=package'
					+'&proposalID='+$("#proposalID","#buildProposalSearchForm").val()+'&proposalversion='+$("#proposalversion","#buildProposalSearchForm").val()
					+'&budgetTo='+packageBudgetTo_mask+'&validFrom='+packageValidFrom_mask+'&validTo='+packageValidTo_mask
					+'&expiredPackages='+$("#expiredPackages", "#buildProposalSearchForm").is(':checked');
		
		$("#searchPackageTable","#buildProposalSearchForm").jqGrid('setGridParam',{url:searchUrl, page:1}).trigger("reloadGrid");
		$("#searchPackageGridContainer","#buildProposalSearchForm").show();		
}

function onClickPriceTypeRadio(priceType){
    if (priceType == 'CPM' || priceType == 'PRE EMPTIBLE' || priceType == 'CUSTOM UNIT') {
        $('#lineItemdetailForm #totalInvestment').attr('readonly', false);
        $('#lineItemdetailForm #totalInvestment').removeClass('input-textbox-readonly');
        $('#lineItemdetailForm #rate').attr('readonly', false);
        $('#lineItemdetailForm #rate').removeClass('input-textbox-readonly');
        $('#lineItemdetailForm #tr_rate').show();
		$('#lineItemdetailForm #calculateBasePrice').removeAttr("disabled");
        $('#lineItemdetailForm #calculateBasePrice').removeClass("disabled-btn");
        $('#lineItemdetailForm #pricingCalculationStep').show();
    } else if (priceType == 'ADDED VALUE') {
        $('#lineItemdetailForm #rate').val(formatDecimal("0.00"));
        $('#lineItemdetailForm #totalInvestment').val(formatDecimal("0.00"));
        $('#lineItemdetailForm #totalInvestment').addClass('input-textbox-readonly');
        $('#lineItemdetailForm #totalInvestment').attr('readonly', true);
        $('#lineItemdetailForm #rate').attr('readonly', true);
        $('#lineItemdetailForm #rate').addClass('input-textbox-readonly');
        $('#lineItemdetailForm #tr_rate').show();
        $('#lineItemdetailForm #calculateBasePrice').removeAttr("disabled");
        $('#lineItemdetailForm #calculateBasePrice').removeClass("disabled-btn");
        $('#lineItemdetailForm #pricingCalculationStep').show();
		/*$('#lineItemdetailForm #calculateBasePrice').attr("disabled", "disabled");
        $('#lineItemdetailForm #calculateBasePrice').addClass("disabled-btn");
        $('#lineItemdetailForm #pricingCalculationStep').hide();*/
    }else if (priceType == 'FLAT RATE') {
        $('#lineItemdetailForm #tr_rate').hide();
        $('#lineItemdetailForm #rate').val("");
        $('#lineItemdetailForm #totalInvestment').attr('readonly', false);
        $('#lineItemdetailForm #totalInvestment').removeClass('input-textbox-readonly');
        $('#rateCardPrice', '#lineItemdetailForm').val("NA");
        $("#offImpressionLink", "#lineItemFinancialFieldsContainer").hide();
		$('#lineItemdetailForm #calculateBasePrice').attr("disabled", "disabled");
        $('#lineItemdetailForm #calculateBasePrice').addClass("disabled-btn");
        $('#lineItemdetailForm #pricingCalculationStep').hide();
    }
}

function resetFields(){
    document.getElementById("lineItemdetailForm").reset();
    $("#buildProposalDetailFormTab-1 a", "#buildProposalParentDiv").text(resourceBundle['tab.generic.addNewLineItem']);
    resetMultiSelectForProposal();
    $("#sosProductId", "#buildProposalParentDiv").select2("val", "");
    $('#lineItemdetailForm #priceType').val('CPM');
	onClickPriceTypeRadio('CPM') ;
	$("#lineItemType_s").attr('checked', 'checked');
	changeLineItemType('S', false);
    //Setting default value of start date and end date
    var startDate = $('#proposalStartDate', '#buildProposalParentDiv').text();
    var endDate = $('#proposalEndDate', '#buildProposalParentDiv').text();
    $("#startDate", "#buildProposalParentDiv").val(startDate);
    $("#endDate", "#buildProposalParentDiv").val(endDate);
    fillSequenceNo();
    $("#reserveChkBoxContainer", "#lineItemCalendarContainer").show();
	$("#reqForReservation", "#lineItemCalendarContainer").hide();
	$('#lineItemdetailForm', '#sor').attr('readonly', false);
    $('#lineItemdetailForm', '#sor').removeClass('input-textbox-readonly');
    $('#lineItemdetailForm', '#calculateSOR').removeAttr('disabled');
}

function addToproposal(){
    var copyPropsals = "";
    var version;

        var id = $("#searchPackageTable", "#buildProposalSearchForm").jqGrid('getGridParam', 'selarrrow');
        $('#buildProposalSearchForm #packageId').val(id);
    
    var proposalSearchFormManager = new FormManager();
    proposalSearchFormManager.formName = 'buildProposalSearchForm';
    var proposalSearchForm = new JQueryAjaxForm(proposalSearchFormManager);
    proposalSearchForm.doCustomProcessingAfterFormSucsesJson = function(){
        jQuery("#runtimeDialogDiv").model({theme: 'Success', message: resourceBundle['message.generic.packageAddedSuccessfully']});
        reloadlineItemGrid();
    };
    proposalSearchForm.submit();
}

function showLineItems(proposalId, proposalVersion, ExpiredFlag){
    var partiallyCopiedUnbreakPackage = false;
	var priceType = "";
	var lineItemDetailFormsManager = new FormManager();
	lineItemDetailFormsManager.gridName = 'lineItemTable';
    var searchDialogWidth = $(window).width() - 80;

	var ajaxReq = new AjaxRequest();
    ajaxReq.dataType = 'html';
    priceType = "Net";
    ajaxReq.url = "./manageProposal/getLineItemsForProposalSearch.action?packageId=" + proposalId + "&searchType=package";   
    ajaxReq.success = function(result, status, xhr){
    	$('#searchProposalLineItems').html(result);
        var proposalLiSearchDialog = new ModalDialog();
        proposalLiSearchDialog.height = 400;
        proposalLiSearchDialog.width = searchDialogWidth;
        proposalLiSearchDialog.buttons = [{
            text: "Add to Proposal",
            click: function(){
                var lineItemNumber = "";
                if ($('#lineitemNumber').val() == "") {
                    lineItemNumber = 1;
                } else {
                    lineItemNumber = $('#lineitemNumber').val();
                }
                var allVals = [];
                var unselectedVals = [];
                $('[name=lookupLineItemId]', '#searchProposalLineItems').each(function(){
                    unselectedVals.push($(this).val());
                });
                $('[name=lookupLineItemId]:checked', '#searchProposalLineItems').each(function(){
                    allVals.push($(this).val());
                });
                // Managing validations on the line item pop up
                if ($('#lineitemNumber').val() == '0' || $('#lineitemNumber').val().indexOf('.') != -1) {
                    if (allVals.length == 0) {
                        $("#errorProposalSelect", "div[aria-labelledby=ui-dialog-title-searchProposalLineItems]").show();
                        $("#errorLineitemCount", "div[aria-labelledby=ui-dialog-title-searchProposalLineItems]").hide();
                    } else if (lineItemErrors(allVals.length)) {
                        $("#errorLineitemCount", "div[aria-labelledby=ui-dialog-title-searchProposalLineItems]").show();
                        $("#errorProposalSelect", "div[aria-labelledby=ui-dialog-title-searchProposalLineItems]").hide();
                    } else {
                        $("#errorLineitemCount", "div[aria-labelledby=ui-dialog-title-searchProposalLineItems]").hide();
                        $("#errorProposalSelect", "div[aria-labelledby=ui-dialog-title-searchProposalLineItems]").hide();
                    }
                    $("#errorLineItem", "div[aria-labelledby=ui-dialog-title-searchProposalLineItems]").show();
                } else if (allVals.length == 0) {
                    if ($('#lineitemNumber').val() == '0' || $('#lineitemNumber').val().indexOf('.') != -1) {
                        $("#errorLineItem", "div[aria-labelledby=ui-dialog-title-searchProposalLineItems]").show();
                    } else {
                        $("#errorLineItem", "div[aria-labelledby=ui-dialog-title-searchProposalLineItems]").hide();
                    }
                    $("#errorProposalSelect", "div[aria-labelledby=ui-dialog-title-searchProposalLineItems]").show();
                    $("#errorLineitemCount", "div[aria-labelledby=ui-dialog-title-searchProposalLineItems]").hide();
                } else if (lineItemErrors(allVals.length)) {
                    if ($('#lineitemNumber').val() == '0' || $('#lineitemNumber').val().indexOf('.') != -1) {
                        $("#errorLineItem", "div[aria-labelledby=ui-dialog-title-searchProposalLineItems]").show();
                    } else {
                        $("#errorLineItem", "div[aria-labelledby=ui-dialog-title-searchProposalLineItems]").hide();
                    }
                    $("#errorLineitemCount", "div[aria-labelledby=ui-dialog-title-searchProposalLineItems]").show();
                    $("#errorProposalSelect", "div[aria-labelledby=ui-dialog-title-searchProposalLineItems]").hide();
                } else {
                    if (proposalVersion == "No" && allVals.length != unselectedVals.length) {
                        partiallyCopiedUnbreakPackage = true;
                    }
                    if (ExpiredFlag == "Yes") {
                        ExpiredFlag = true;
                    } else {
                        ExpiredFlag = false;
                    }
                    var ajaxSubReq = new AjaxRequest();
                    ajaxSubReq.url = "./proposalWorkflow/addCopiedLineItemsToProposal.action?proposalID=" + $('#proposalID', '#buildProposalSearchForm').val() +
                    	"&proposalversion=" + $('#proposalversion', '#buildProposalSearchForm').val() + "&searchType=package" +
                    	"&copiedLineItemIds=" + allVals + "&partiallyCopiedUnbreakPackage=" + partiallyCopiedUnbreakPackage + "&copiedFromExpired=" + ExpiredFlag +
                    	"&optionId=" + $("#optionId", "#buildProposalSearchForm").val() + "&lineitemNumbers=" + lineItemNumber+"&priceType="+priceType;
                    ajaxSubReq.success = function(result, status, xhr){
                    	var flag = result.objectMap.gridKeyColumnValue;
                    	if(flag != true) {
                    	reloadJqGridAfterAddRecord(lineItemDetailFormsManager.gridName, lineItemGridSortName);
						jQuery("#runtimeDialogDiv").model({theme: 'Success', message: resourceBundle['message.active.lineItems']});
                        $(".ui-dialog-buttonset button:contains('Add')").button("enable");
                        $('#searchProposalLineItems').dialog("close");
                        var index = $('#buildProposalTab a[href="#lineItem"]').parent().index();
                        $('#buildProposalTab').tabs('select', index);//Code for moving the focus on to a tab from different tab
                        getNetCPMImpressionsOfProposal();
                      } else {
					  	  jQuery("#runtimeDialogDiv").model({theme: 'Error', autofade: false, message: resourceBundle['error.generic.addpkglineitems']});
                    	  $(".ui-dialog-buttonset button:contains('Add')").button("enable");
                      }
                    };
                    ajaxSubReq.submit();
                    $(".ui-dialog-buttonset button:contains('Add')").button("disable");
                }
            }
        }, {
            text: "Cancel",
            click: function(){
                $(".ui-dialog-buttonset button:contains('Add')").button("enable");
                $(this).dialog("close");
            }
        }];
      
        $('#searchProposalLineItems').dialog(proposalLiSearchDialog);
        $('#searchProposalLineItems').dialog("open");
       
        $('#searchProposalLineItems').dialog({
        	beforeClose : function( event, ui ) { 
        		$(".ui-dialog-buttonset button").show();
        		$(".ui-dialog-buttonset button").button("enable");
        		$(this).dialog("destroy").remove();
        		$('#searchProposalLineItemsDiv').html('');
        		$('#searchProposalLineItemsDiv').html('<div id="searchProposalLineItems" style="display: none" title="Line Items"></div>');
    	 	}
    	});
        var lineItemLabel = $("#lineItemMultipierLabel").html();
        var lineItem = '<span id="listItems"><label class="label-bold">' + lineItemLabel + ' : </label>';
        lineItem += '<input id="lineitemNumber" class="numeric" maxlength="2" size="5" name="lineitemNumber" value="1"/></span>';
        $('#listItems').remove();
        $(".ui-dialog-buttonpane", "div[aria-labelledby=ui-dialog-title-searchProposalLineItems]").append(lineItem);
        $("input[class=numeric]").numeric({
            negative: false
        });
        
        if ($('[name=lookupLineItemId]', '#searchProposalLineItems').length == 0) {
            $('#listItems', '#proposalLineItemListing').hide();
            $(".ui-dialog-buttonset button:contains('Add')").button("disable");
        }
        
        $(".ui-dialog-buttonpane", "div[aria-labelledby=ui-dialog-title-searchProposalLineItems]").append("<div id='errorProposalSelect' class='model-dialog-error'>" + resourceBundle['error.ldap.select.LineItem'] + "</div>");
        $("#errorProposalSelect", "div[aria-labelledby=ui-dialog-title-searchProposalLineItems]").hide();
        $(".ui-dialog-buttonpane", "div[aria-labelledby=ui-dialog-title-searchProposalLineItems]").append("<div id='errorLineItem' class='model-dialog-error'>" + resourceBundle['error.LineItem.count'] + "</div>");
        $("#errorLineItem", "div[aria-labelledby=ui-dialog-title-searchProposalLineItems]").hide();
        $(".ui-dialog-buttonpane", "div[aria-labelledby=ui-dialog-title-searchProposalLineItems]").append("<div id='errorLineitemCount' class='model-dialog-error'>" + resourceBundle['error.LineItem.count.exceed'] + "</div>");
        $("#errorLineitemCount", "div[aria-labelledby=ui-dialog-title-searchProposalLineItems]").hide();
    };
    ajaxReq.submit();
}

function reloadlineItemGrid(){
    $("#lineItemTable", "#buildProposalParentDiv").trigger("reloadGrid");
}

/**
 * Clear totalPossibleImpressions and avails on change of sales Target ,Product, start date and end date
 */
function clearAvails(){
	$("#totalPossibleImpressions","#buildProposalParentDiv").val("");		
	$("#avails","#buildProposalParentDiv").val("");	
	$("#availsPopulatedDate","#buildProposalParentDiv").val("");	
	$("#sov","#buildProposalParentDiv").val("");	
}

function populateAvailsFromYieldex(){
   	$('#messageHeaderFinancialDiv').html('');
    $("#availProgress").dialog("open");
	addFetchingAvailsText();
    
    var lineItemDetailFormsManager = new FormManager();
    lineItemDetailFormsManager.gridName = 'lineItemTable';
    lineItemDetailFormsManager.formName = 'lineItemdetailForm';
    var populateAvailsFrom = new JQueryAjaxForm(lineItemDetailFormsManager);
    $("#lineItemdetailForm").attr("action", "./avails/populateAvailsFromYieldex.action");
   
    populateAvailsFrom.doCustomProcessingAfterFormSucsesJson = function(jsonResponse, XMLHTTPRequest){
		var yieldexError = jsonResponse.objectMap.yieldexError;
		enableDisableFieldsToCaluclateFetchAvails(true);
		if (yieldexError != "") {
			$('#messageHeaderFinancialDiv', '#lineItemdetailForm').html(resourceBundle['error.generic.yieldex.error']+yieldexError).addClass('error');
			$('#messageHeaderFinancialDiv', '#lineItemdetailForm').show();
		} else {
			$('#messageHeaderFinancialDiv', '#lineItemdetailForm').html(yieldexError).removeClass('error');
			$('#messageHeaderFinancialDiv', '#lineItemdetailForm').hide();
			var summary = jsonResponse.objectMap.gridKeyColumnValue;
			var availsPopulatedDate = jsonResponse.objectMap.gridKeyColumnAdditionalValue;
			
			if (summary.available != 0) {
				var available = summary.available.replace(/,/g, '');
				$('#avails', '#lineItemdetailForm').val(formatNumber(available));
			} else {
				var available = summary.available;
				$('#avails', '#lineItemdetailForm').val(formatNumber(available));
			}
			
			if (summary.capacity != 0) {
				var capacity = summary.capacity.replace(/,/g, '');
				$('#totalPossibleImpressions', '#lineItemdetailForm').val(formatNumber(capacity));
			} else {
				var capacity = summary.capacity;
				$('#totalPossibleImpressions', '#lineItemdetailForm').val(formatNumber(capacity));
			}
			
			$('#availsPopulatedDate', '#lineItemdetailForm').val(availsPopulatedDate);
			var offeredImpression = $('#impressionTotal', '#lineItemdetailForm').val().replace(/,/g, '');
			var capacity = (summary.capacity).replace(/,/g, '');
			
			if (offeredImpression != 0 && capacity != 0) {
				var sov = (offeredImpression * 100) / capacity;
				sov = roundoffto2places(sov);
				$("#sov", "#lineItemdetailForm").val(formatDecimal(sov));
			}
			else if (capacity == 0) {
				$("#sov", "#lineItemdetailForm").val("");
			}
		}
        $("#availProgress").dialog("close");
    };
	
    populateAvailsFrom.doCustomProcessingAfterValidationFailedJson = function(jsonResponse, XMLHTTPRequest){
	    var errorArray = jsonResponse.errorList;
	    enableDisableFieldsToCaluclateFetchAvails(true);
        jQuery.each(errorArray, function(i, error){
			if (error.field == "startDate" || error.field == "endDate" || error.field == "sosSalesTargetId_custom" || error.field == "specType_custom") {
                jQuery("#" + error.field, "#" + lineItemDetailFormsManager.formName).addClass("errorElement errortip").attr("title", error.errorMessageForUI + '<BR><b>Help:</b> ' + error.errorHelpMessageForUI);
            }
        });
        $('.errortip', "#" + lineItemDetailFormsManager.formName).qtip({
            style: {
                classes: 'ui-tooltip-red'
            },
            position: {
                my: 'bottom center',
                at: 'top center'
            }
        });
		$("#availProgress").dialog("close");
    };
	
    populateAvailsFrom.error = function(XMLHTTPRequest, textStatus, errorThrown){
		$("#availProgress").dialog("close");
		enableDisableFieldsToCaluclateFetchAvails(true);
        handleYieldexAvailsExceptionOnServer(XMLHTTPRequest);
    };
    getDataForTargetingToSave();
    enableDisableFieldsToCaluclateFetchAvails(false);
    $('#avails', '#lineItemdetailForm').val('');
	 $('#totalPossibleImpressions', '#lineItemdetailForm').val('');
	 $('#availsPopulatedDate', '#lineItemdetailForm').val('');
    populateAvailsFrom.submit();
}

function enableDisableFieldsToCaluclateFetchAvails(flag) {
	if(flag && ($("#pricingStatus" , "#lineItemTargeting").val() == "Pricing Approved")) {
	    if($("#lineItemType_s").is(":checked")){
	    	$("#sosSalesTargetId", "#lineItemdetailForm").multiselect("disable");
	    } else {
	    	$("#sosSalesTargetId", "#lineItemdetailForm").select2("disable");
	    }
		$("#sosProductId", "#lineItemdetailForm").select2("disable");
	} else {
	    if($("#lineItemType_s").is(":checked")){
	    	$("#sosSalesTargetId", "#lineItemdetailForm").multiselect("enable");
	    } else {
	    	$("#sosSalesTargetId", "#lineItemdetailForm").select2("enable");
	    }
		$("#sosProductId", "#lineItemdetailForm").select2("enable");
	}
}

function handleYieldexAvailsExceptionOnServer(XMLHTTPRequest){
	var errorObj = eval('(' + XMLHTTPRequest.responseText + ')').errorObject;	
	jQuery("#runtimeDialogDiv").model({theme: 'Error', width: 320, height: 170, autofade: false, message: errorObj.errorMessage});
}

function getLineItemsExceptions(){
    var ajaxReq = new AjaxRequest();
    ajaxReq.url = "./proposalWorkflow/getLineItemsExceptions.action?lineItemID=" + $('#lineItemdetailForm #lineItemID').val();
    ajaxReq.success = function(result, status, xhr){
        var flag = false;
        var valueToPrint = "";
        var returnVal = result.objectMap.gridKeyColumnValue;
        if (returnVal != null) {
            $.each(returnVal, function(i, item){
                valueToPrint += "<li>" + resourceBundle[item] + "</li>";
                flag = true;
            });
            var id = $("#lineItemID", "#lineItemdetailForm").val();
            if (flag) {
                $("#lineItemExceptions", "#lineItemdetailForm").html(valueToPrint);
                $("#" + id, "#lineItemTable").find("td").addClass("expire-package");
                $("#lineItemExceptionsContainer", "#lineItemdetailForm").show();
            }
            else {
                $("#lineItemExceptionsContainer", "#lineItemdetailForm").hide();
                $("#" + id, "#lineItemTable").find("td").removeClass("expire-package");
            }
        }
    };
    ajaxReq.submit();
}

function resetSearchFields(){
	$("#searchPackageGridContainer","#buildProposalSearchForm").hide();
	$("input[type=text]", "#buildProposalSearchForm").val("");
	$("input[type=checkbox]", "#buildProposalSearchForm").attr('checked', false);
	$("select", "#buildProposalSearchForm").val("");	
}

function onClickProposalImpressionCountInfo(impressionCount){
    if (impressionCount == "IMPRESSIONS") {
    	$("#ImpressionsCountInfo", "#lineItemdetailForm").show();
        $("#ImpressionsCountSOV", "#lineItemdetailForm").hide();
        $("#sov", "#lineItemdetailForm").val("");        
    } else if (impressionCount == "SOV") {
    	$("#ImpressionsCountInfo", "#lineItemdetailForm").hide();
    	$("#impressionTotal", "#lineItemdetailForm").val("");
        $("#ImpressionsCountSOV", "#lineItemdetailForm").show();
    }
}

/**
 * Method to update Net Cpm and Net Impressions on UI
 */
function getNetCPMImpressionsOfProposal(){
	var ajaxReq = new AjaxRequest();
    ajaxReq.url = "./proposalWorkflow/getproposalVersion.action?optionId=" + $("#optionId","#buildProposalSearchForm").val() 
    			+ "&proposalVersion=" + $("#proposalversion","#buildProposalSearchForm").val();
	ajaxReq.success = function(result, status, xhr){
		$('#proposalNetImpression', '#buildProposalParentDiv').text(formatNumber(result.objectMap.gridKeyColumnValue.netImpressions));
		$('#proposalNetCpm', '#buildProposalParentDiv').text(formatNumber(result.objectMap.gridKeyColumnValue.netCpm));
		$('#proposalOfferedBudget', '#buildProposalParentDiv').text(formatNumber(result.objectMap.gridKeyColumnValue.offeredBudget));
		$('#proposalRemainingBudget', '#buildProposalParentDiv').text(formatNumber(result.objectMap.gridKeyColumnValue.remBudget));
	};
    ajaxReq.submit();
}

function processLineItemDelete(response, postdata){
    var success = true;
    var message = "";
    var new_id = "";
    if (status == "success") {
        getNetCPMImpressionsOfProposal();
    }
    return [success, message, new_id];
}

function isRequiredAttributeSetForAvails(){
    var startDate = $("#startDate", "#buildProposalParentDiv").val();
    var endDate = $("#endDate", "#buildProposalParentDiv").val();
    var sosSalesTargetId = $('#lineItemdetailForm #sosSalesTargetId').val();
    var sosProductId = $('#lineItemdetailForm #sosProductId').val();
    if (startDate != '' && endDate != '' && sosSalesTargetId != '' && sosProductId != '') {
        return true;
    } else {
        return false;
    }
}

/**
 * Sort data for select box based on display value (Due to IE and chrome issue)
 * @param result
 * @param select
 * @return
 */
function sortListForProposalLineItem(result, select, childSaleTargetIds){
    var arrVal = sortMapDataByValue(result);
    var optString = "";
	if ($("#lineItemType_r", "#lineItemTargeting").is(':checked') || $("#lineItemType_e", "#lineItemTargeting").is(':checked')) {
		 optString = optString + '<option value=""></option>';
	}
    for (var i = 0; i < arrVal.length; i++) {
        var parentValue = 'NA';
        $.each(childSaleTargetIds, function(j, value){
            if (j == arrVal[i][1]) {
                parentValue = value;
            }
        });
        optString = optString + '<option value="' + arrVal[i][1] + '" parent="' + parentValue + '">' + arrVal[i][0] + '</option>';
    }
    $(select).append(optString);
}

function setMultiSelectSalesTargetId(){
    var checkOnChange = false;
    $("#priceCalSummary", "#lineItemdetailForm").val("");
    $("#sosSalesTargetId", "#buildProposalParentDiv").multiselect({
        selectedList: 1,
		noneSelectedText: '',
        uncheckAll: function(){
	        $("#offImpressionLink", "#lineItemFinancialFieldsContainer").hide();
            clearAvails();
            enableDisabledChildSaleTargetsForProposal();
            $('#lineItemdetailForm #placementName').val("");
            if ($("#lineItemID", "#lineItemdetailForm").val() == 0) {
                $("#rate", "#buildProposalParentDiv").val("");
            }
            if ($("#priceType", "#lineItemdetailForm").val() != "FLAT RATE") {
                $("#rateCardPrice", "#buildProposalParentDiv").val("");
            }
            else {
                $("#rateCardPrice", "#buildProposalParentDiv").val("NA");
            }
        },
        click: function(event, ui){
        $("#offImpressionLink", "#lineItemFinancialFieldsContainer").hide();
            enableDisabledChildSaleTargetsForProposal();
            checkOnChange = true;
            var salesTargetIds = $(this).multiselect("getChecked").map(function(){
                return this.value;
            }).get();
            if (salesTargetIds == "" && salesTargetIds.length <= 0) {
                clearAvails();
                $("#rateCardPrice", "#buildProposalParentDiv").val("");
                $("#discountPercentage", "#lineItemFinancialFieldsContainer").val("");
                if ($("#lineItemID", "#lineItemdetailForm").val() == 0) {
                    $("#rate", "#buildProposalParentDiv").val("");
                }
                $("#placementName", "#lineItemdetailForm").val("");
                if ($("#priceType", "#lineItemdetailForm").val() == "FLAT RATE" ||
                $("#priceType", "#lineItemdetailForm").val() == "ADDED VALUE") {
                    $("#rateCardPrice", "#buildProposalParentDiv").val("");
                }
            }
            else if ($("#flagForOnChange", "#buildProposalParentDiv").val() == 'true') {
                if ($("#priceType", "#lineItemdetailForm").val() == "FLAT RATE" ||
                		$("#priceType", "#lineItemdetailForm").val() == "ADDED VALUE") {
                    $("#rateCardPrice", "#buildProposalParentDiv").val("NA");
                } else {
                    $("#rateCardPrice", "#buildProposalParentDiv").val("");
                }
                clearAvails();
            }
        },
        close: function(){
            if (checkOnChange == true) {
                checkOnChange = false;
            }
        }
    }).multiselectfilter();
    $("#sosSalesTargetId_custom  ul.ui-helper-reset li:first").hide();
}

/**
 * Fetch sales target from selected product
 * @param {Object} selectedProduct
 */
function setsosSalesTargetIdValueOfProposal(selectedProduct) {
    var salesTargetIdCombo = $("#sosSalesTargetId", "#buildProposalParentDiv");
	var actionURL = "./managepackage/getSalesTargetForProduct.action.action?productID=" + selectedProduct;
	dynaLoadSalesTargets(salesTargetIdCombo, actionURL);
}

/**
 * Dynamic load sales target based on product
 * @param {Object} select
 * @param {Object} actionURL
 */
function dynaLoadSalesTargets(select, actionURL){
    var ajaxReq = new AjaxRequest();
    ajaxReq.url = actionURL;
    ajaxReq.cache = false;
    ajaxReq.success = function(result, status, xhr){
        $("option", select).remove();
        sortListForProposalLineItem(result.objectMap.gridKeyColumnValue, $("#sosSalesTargetId", "#buildProposalParentDiv"), result.objectMap.childSaleTargetIds);
        if ($("#lineItemType_s", "#lineItemTargeting").is(':checked')) {
        	$(select).multiselect("refresh");			
		} else{
			 $(select).trigger("change");
			 $(select).select2('data', null);
		}
    };
    ajaxReq.submit();
}

/**
 * Method get the consolidated data for Line Item
 * 
 * @param {Object} selectedGridRow
 */
function getLineItemConsolidatedData(selectedGridRow){
    var salesTargetObj = $("#sosSalesTargetId", "#buildProposalParentDiv");
    var productId = $("#lineItemTable", "#buildProposalParentDiv").jqGrid('getCell', selectedGridRow, 'sosProductId');
	var lineItemId = $("#lineItemTable", "#buildProposalParentDiv").jqGrid('getCell', selectedGridRow, 'lineItemID');
    var actionURL = "./proposalWorkflow/getLineItemsConsolidatedData.action?lineItemId=" + lineItemId +"&productID=" + productId;
    var ajaxReq = new AjaxRequest();
    ajaxReq.url = actionURL;
    ajaxReq.cache = false;
    ajaxReq.success = function(result, status, xhr){
    	$("#lineItemType_s").attr('disabled', false).attr('checked', false);
        $("#lineItemType_r").attr('disabled', false).attr('checked', false);
        $("#lineItemType_e").attr('disabled', false).attr('checked', false);
        $("#productType", "#lineItemFinancialFieldsContainer").val('');
		setLineItemDetails(result.objectMap.lineItemDetailData);
		if (result.objectMap.lineItemDetailData.pricingStatus == "Pricing Approved") {
			enableDisableLineItemFields(true);
		} else {
	        enableDisableLineItemFields(false);
		}
		setproductSalesTarget(result.objectMap.productSaleTargets, salesTargetObj, result.objectMap.childSaleTargetIds, selectedGridRow);
		setLineItemException(result.objectMap.lineItemExceptions);
		setLineItemTargetData(result.objectMap.lineItemTargets);
        $("#priceCalSummary", "#lineItemdetailForm").val(result.objectMap.lineItemPricingSteps);
        setPlacementNameAndTargetingString();
        $("#placementName","#lineItemdetailForm").val(result.objectMap.lineItemDetailData.placementName);
        if(!isNaN($("#rateCardPrice","#lineItemdetailForm").val()) && $("#priceType","#lineItemdetailForm").val()=="ADDED VALUE"){
    		$("#offImpressionLink", "#lineItemFinancialFieldsContainer").show();
    	} else {
    		$("#offImpressionLink", "#lineItemFinancialFieldsContainer").hide();
    	}
		jQuery("textarea", "#lineItemdetailForm").change();
    };
    ajaxReq.submit();
}

/**
 * Method populates line Item Details
 * 
 * @param {Object} lineItemDetailData
 */
function setLineItemDetails(lineItemDetailData){
	$("#lineItemID", "#lineItemdetailForm").val(lineItemDetailData.lineItemID);
	changeLineItemType(lineItemDetailData.productType, false);
	$("#sosProductId","#lineItemdetailForm").val(lineItemDetailData.sosProductId);
	$('#lineItemdetailForm #sosProductId').trigger("change");
	$("#flight","#lineItemdetailForm").val(lineItemDetailData.flight);
	if ($.trim(lineItemDetailData.flight) == "") {
		$("#startDate", "#lineItemdetailForm").val(lineItemDetailData.startDate);
	}else{
		$("#startDate", "#lineItemdetailForm").val("");
	}
	$("#endDate", "#lineItemdetailForm").val(lineItemDetailData.endDate);
	$("#placementName","#lineItemdetailForm").val(lineItemDetailData.placementName);
	$("#rateCardPrice","#lineItemdetailForm").val(lineItemDetailData.rateCardPrice);
	$("#lineItemSequence","#lineItemdetailForm").val(lineItemDetailData.lineItemSequence);
	$("#priceType","#lineItemdetailForm").val(lineItemDetailData.priceType);
	$("#rate","#lineItemdetailForm").val(lineItemDetailData.rate);
	$("#impressionTotal","#lineItemdetailForm").val(lineItemDetailData.impressionTotal);
	$("#totalInvestment","#lineItemdetailForm").val(lineItemDetailData.totalInvestment);
	$("#avails","#lineItemdetailForm").val(lineItemDetailData.avails);
	$("#availsPopulatedDate","#lineItemdetailForm").val(lineItemDetailData.availsPopulatedDate);
	$("#totalPossibleImpressions","#lineItemdetailForm").val(lineItemDetailData.totalPossibleImpressions);
	$("#sov","#lineItemdetailForm").val(lineItemDetailData.sov);
	$("#comments","#lineItemdetailForm").val(lineItemDetailData.comments);
	$("#product_active","#lineItemdetailForm").val(lineItemDetailData.product_active);
	$("#salesTarget_active","#lineItemdetailForm").val(lineItemDetailData.salesTarget_active);
	$("#targetingString","#lineItemdetailForm").val(lineItemDetailData.targetingString);
	calculateDiscountPercent();	
    onClickPriceTypeRadio(lineItemDetailData.priceType) ;
	selectSpecType(lineItemDetailData.specType);
	$("#priceCalSummary", "#lineItemdetailForm").val("");
    $('#pricingSummary').html('');
    $('#creativeSummary').html('');
    $("#lineItemIdVal", "#lineItemdetailForm").val(lineItemDetailData.lineItemID);
	if(lineItemDetailData.productType == "R" || lineItemDetailData.productType == "E"){
		if(lineItemDetailData.productType == "R"){
			$("#lineItemType_r").attr('checked', 'checked');
		}else{
			$("#lineItemType_e").attr('checked', 'checked');
		}
		$("#sor","#lineItemdetailForm").val(lineItemDetailData.sor);
		$("#reservationExpiryDate", "#lineItemdetailForm").val(lineItemDetailData.reservationExpiryDate);
		expirationDate = lineItemDetailData.reservationExpiryDate;
		var productOptGrp = $("#sosProductId option:selected").parent("optgroup").attr("label");
		if (lineItemDetailData.reserved) {
			$("#reserveChkBoxContainer", "#lineItemCalendarContainer").show();
			$("#reqForReservation", "#lineItemCalendarContainer").hide();
			$("#reserved", "#lineItemdetailForm").attr('checked', 'checked');
			
			if($("#proposalOwnerRole", "#buildProposalParentDiv").val() == 'PLR' && $("#lineItemType_r", "#lineItemTargeting").is(':checked') && (productOptGrp == 'HOME PAGE' || productOptGrp == 'Display Cross Platform')){
				$("#reserved", "#lineItemdetailForm").attr('disabled', 'disabled');
	    		$("#reservedChkBoxSpan").addClass('disabled-checkbox');
				$('#lineItemdetailForm #sor').addClass('input-textbox-readonly');
	    		$('#lineItemdetailForm #sor').attr('readonly', true);
	    		$('#lineItemdetailForm #calculateSOR').attr("disabled", "disabled");
                $('#lineItemdetailForm #calculateSOR').addClass("disabled-btn");
			}else{
				$("#reserved", "#lineItemdetailForm").removeAttr('disabled');
	    		$("#reservedChkBoxSpan").removeClass('disabled-checkbox');
				$('#lineItemdetailForm #sor').attr('readonly', false);
	 	        $('#lineItemdetailForm #sor').removeClass('input-textbox-readonly');
		 	    $('#lineItemdetailForm #calculateSOR').removeAttr("disabled");
	            $('#lineItemdetailForm #calculateSOR').removeClass("disabled-btn");
			}
		}else{
			$("#reserved", "#lineItemdetailForm").removeAttr('checked');
			if(lineItemDetailData.reservationExpiryDate != "" && lineItemDetailData.reservationExpiryDate != null){
				$("#reserved", "#lineItemdetailForm").attr('disabled', 'disabled');
	    		$("#reservedChkBoxSpan").addClass('disabled-checkbox');
			}else{
				$("#reserved", "#lineItemdetailForm").removeAttr('disabled');
	    		$("#reservedChkBoxSpan").removeClass('disabled-checkbox');
				if($("#proposalOwnerRole", "#buildProposalParentDiv").val() == 'PLR' && $("#lineItemType_r", "#lineItemTargeting").is(':checked') && (productOptGrp == 'HOME PAGE' || productOptGrp == 'Display Cross Platform')){
		    		$("#reserveChkBoxContainer", "#lineItemCalendarContainer").hide();
					$("#reqForReservation", "#lineItemCalendarContainer").show();
				}else{
					$("#reserveChkBoxContainer", "#lineItemCalendarContainer").show();
					$("#reqForReservation", "#lineItemCalendarContainer").hide();
				}
			}
		}
		showHideRenewLink();
		enableDisableCalendar();
		enableLineItemDefaults();
	}else if(lineItemDetailData.productType == "S"){
		$("#lineItemType_s").attr('checked', 'checked');
	}
	$('#lineItemdetailForm #lineItemSosId').show();
	$("#lineItemPackageName", "#lineItemdetailForm").val(lineItemDetailData.packageName);
	$("#pricingStatus" , "#lineItemdetailForm").val(lineItemDetailData.pricingStatus);
	if(lineItemDetailData.sosLineItemID != null){
		$('#lineItemdetailForm #tr_sosLineItemId').show();
		$("#sosLineItemIdVal", "#lineItemdetailForm").val(lineItemDetailData.sosLineItemID);
	}else{
		$('#lineItemdetailForm #tr_sosLineItemId').hide();
	}
	$('#isViewable').val(lineItemDetailData.isViewable);
	if(lineItemDetailData.packageName == null || lineItemDetailData.packageName == ""){
		$("#isViewable", "#lineItemdetailForm").attr("disabled", false);
	}else{
		$("#isViewable", "#lineItemdetailForm").attr("disabled", true);
	}
}

function setproductSalesTarget(productSaleTargets, salesTargetObj, childSaleTargetIds, selectedGridRow){
	$("option", salesTargetObj).remove();
    sortListForProposalLineItem(productSaleTargets, salesTargetObj, childSaleTargetIds);
    $(salesTargetObj).multiselect("refresh");
    selectSalesTargetFromGrid(selectedGridRow);
    $("#flagForOnChange", "#buildProposalParentDiv").val('true');
}

/**
 * Method display's Targeting Data
 * 
 * @param {Object} returnedJson
 */
function setLineItemTargetData(returnedJson){
	$('#geoTargetsummary').find("tr:gt(0)").remove();//Removes all rows of a table except its header
    if (returnedJson != "") {
    	var targets = eval(returnedJson);
        $.each(targets, function(index, item){
        	displayTargetsOfLineItem(item);
        });
        var rowId = $("#geoTargetsummary", "#lineItemTargeting").find("tr:last").attr("id");
        $('#' + rowId + " td:nth-child(7)").html('--');
     }
     $('#sosTarTypeId').val("");
     $('#sosTarTypeElement >option').remove();
}

/**
 * Method display's Line Item Exceptions
 * @param {Object} returnVal
 */
function setLineItemException(returnVal){
	var flag = false;
    var valueToPrint = "";
    if (returnVal != null) {
    	$.each(returnVal, function(i, item){
        	valueToPrint += "<li>" + resourceBundle[item] + "</li>";
            flag = true;
        });

        var id = $("#lineItemID", "#lineItemdetailForm").val();
        if (flag) {
        	$("#lineItemExceptions", "#lineItemdetailForm").html(valueToPrint);
            $("#" + id, "#lineItemTable").find("td").addClass("expire-package");
            $("#lineItemExceptionsContainer", "#lineItemdetailForm").show();
        }else {
            $("#lineItemExceptionsContainer", "#lineItemdetailForm").hide();
            $("#" + id, "#lineItemTable").find("td").removeClass("expire-package");
        }
    }
}

/**
 * Select, marked sales target as checked from grid
 * @param {Object} selectedGridRow
 */
function selectSalesTargetFromGrid(selectedGridRow){
	var sosSalesTargetId = $("#lineItemTable", "#buildProposalParentDiv").jqGrid('getCell', selectedGridRow, 'sosSalesTargetId').split(",");
	var salesTargetObj = $("#sosSalesTargetId", "#buildProposalParentDiv");
	
	if ($("#lineItemType_r", "#lineItemTargeting").is(':checked') || $("#lineItemType_e", "#lineItemTargeting").is(':checked')) {
		$("#sosSalesTargetId","#buildProposalParentDiv").val(sosSalesTargetId[0]);
		$('#buildProposalParentDiv #sosSalesTargetId').trigger("change");
	}
	else {
		$(salesTargetObj).multiselect("widget").find(":checkbox").each(function(){
			for (var i = 0; i < sosSalesTargetId.length; i++) {
				if ($(this).val() == sosSalesTargetId[i]) {
					if (!$(this).is(":checked")) {
						this.click();
						break;
					}
				}
			}
		});
	}
}

function resetMultiSelectForProposal(){
	$('option', $("#sosSalesTargetId", "#buildProposalParentDiv")).remove();
	$("#sosSalesTargetId", "#buildProposalParentDiv").multiselect('refresh');
}

function enableDisabledChildSaleTargetsForProposal() {
    var salesTargetIdCombo = $("#sosSalesTargetId", "#buildProposalParentDiv");
    $(salesTargetIdCombo).multiselect("widget").find(":checkbox").each(function(){
		$(this).attr("disabled", false);
    });
	var salesTargetIds = $(salesTargetIdCombo).multiselect("getChecked").map(function(){
        return this.value;
    }).get();
	if (salesTargetIds.length > 0) {
		for (var i = 0; i < salesTargetIds.length; i++) {
			enableDisableSalesTargetForProposal(salesTargetIdCombo, getChildSalesTargetForProposal(salesTargetIdCombo, salesTargetIds[i]));
		}
	}
}

function enableDisableSalesTargetForProposal(salesTargetIdCombo, salesTargetIds) {
	if (salesTargetIds.length > 0) {
		for (var i = 0; i < salesTargetIds.length; i++) {
			enableDisableSalesTargetForProposal(salesTargetIdCombo, getChildSalesTargetForProposal(salesTargetIdCombo, salesTargetIds[i]));
            $("#sosSalesTargetId option[value="+salesTargetIds[i]+"]").attr('selected', false);
		   	$(salesTargetIdCombo).multiselect("widget").find(":checkbox[value=" + salesTargetIds[i] + "]").attr('checked', false).attr('disabled','disabled');
		}
	} else {
		return;
	}	
}

function getChildSalesTargetForProposal(salesTargetIdCombo, salesTargetId) {
	return $(salesTargetIdCombo).find("option[parent='" + salesTargetId + "']").map(function(){
        return this.value;
    });
}

function removeDivCssLineItem(){
	$("#sosSalesTargetId_custom", "#buildProposalParentDiv").removeClass("errorElement").removeClass("errortip").removeAttr("oldtitle");
	$("#sosSalesTargetId_custom", "#buildProposalParentDiv").qtip("destroy");
	$("#specType_custom", "#buildProposalParentDiv").removeClass("errorElement").removeClass("errortip").removeAttr("oldtitle");
	$("#specType_custom", "#buildProposalParentDiv").qtip("destroy");
}

/**
 * Parameter are required when click on next button but not required when click on save button flowurl, eventViewCampaignDoc
 * and their values are undefined when click on save button 
 *  
 * @param {Object} flowurl
 * @param {Object} eventViewCampaignDoc
 */
function lineItemSaveData(flowurl, eventViewCampaignDoc){
    removeDivCssLineItem();
    if (validateLineItemTargeting()) {
    if($("#pricingStatus" , "#lineItemTargeting").val() == "Pricing Approved"){
		enableDisableLineItemFields(false);
	}
    $("#isViewable", "#lineItemdetailForm").attr("disabled", false);
    var lineItemDetailFormsManager = new FormManager();
    lineItemDetailFormsManager.gridName = 'lineItemTable';
    lineItemDetailFormsManager.formName = 'lineItemdetailForm';
    var lineItemDetailForm = new JQueryAjaxForm(lineItemDetailFormsManager);
    $("#proposalID", "#lineItemdetailForm").val($("#proposalID", "#buildProposalSearchForm").val());
	 $("#optionId", "#lineItemdetailForm").val($("#optionId", "#buildProposalSearchForm").val());
	 var targetingString=$('#targetingString','#lineItemTargeting').html();
	  var paraIndex = targetingString.indexOf("<p>");	
			  if(paraIndex !=-1){
			  	targetingString=targetingString.replace(/<p>/g,"");
				targetingString=targetingString.replace(/<\/p>/gi,"");
			  }
	  var strongIndex = targetingString.indexOf("<strong>");
           if (strongIndex != -1) {
			  targetingString=targetingString.replace(/<strong>/gi,"");
			  targetingString=targetingString.replace(/<\/strong>/gi,"");
			  }
			  $('#targetingStringValue').val(targetingString);
	
	var lineItemIds = "";
	$("#lineItemTable", "#buildProposalParentDiv").find(':checkbox').each(function(){
		lineItemIds = lineItemIds + $(this).val() + ",";
	});
    if (lineItemIds.length > 0) {
    	lineItemIds = $.trim(lineItemIds).substring(0, lineItemIds.length - 1);
    }
	$("#gridLineItemIds" , "#lineItemdetailForm").val(lineItemIds);
    $("#lineItemdetailForm").attr("action", "./proposalWorkflow/editLineItemsOfProposal.action?proposalversion=" + $("#proposalversion", "#buildProposalSearchForm").val());
    $("#productName", "#lineItemdetailForm").val($("#sosProductId option[value=" + $("#sosProductId", "#lineItemdetailForm").val() + "]", "#lineItemdetailForm").text());  
    $("#sosProductClassName", "#lineItemdetailForm").val($("#sosProductId option:selected", "#lineItemdetailForm").parent("optgroup").attr("label"));
	lineItemDetailForm.doCustomProcessingAfterFormSucsesJson = function(jsonResponse, XMLHTTPRequest){
	   $('#messageHeaderFinancialDiv').html('');
	   jQuery("#runtimeDialogDiv").model({theme: 'Success', message: resourceBundle['message.generic.lineItemSavedSuccessfully']});
	   $("#sorExceededHidden", "#lineItemCalendarContainer").val(false);
	   var lineItmFrm = jsonResponse.objectMap.gridKeyColumnValue;
	   var invid = $("#lineItemID", "#lineItemdetailForm").val();
	   if (invid == 0) {
	       reloadJqGridAfterAddRecord(lineItemDetailFormsManager.gridName, lineItemGridSortName);
	   }
	   else {
	       if ($("#priceType", "#lineItemdetailForm").val() == "FLAT RATE") {
	           $("#rate", "#lineItemdetailForm").val("NA");
	       }
	       $("#productName", "#lineItemdetailForm").val($("#sosProductId option[value=" + $("#sosProductId", "#lineItemdetailForm").val() + "]", "#lineItemdetailForm").text());
	       $("#product_active", "#lineItemdetailForm").val("Yes");
	       $("#salesTarget_active", "#lineItemdetailForm").val("Yes");
	       
	       //Since in versioning case new Line Item will be created so LineItemID is updated in forms   
	       $("#lineItemID", "#lineItemdetailForm").val(lineItmFrm.lineItemID);
	       $("#lineItemTable", "#buildProposalParentDiv").jqGrid('FormToGrid', invid, "#lineItemdetailForm");
	       var gsr = $("#lineItemTable", "#buildProposalParentDiv").jqGrid('getGridParam', 'selrow');	       
	       getLineItemsExceptions();
	       if ($("#orderNumber", "#lineItemdetailForm").is(':checked')) {
	           reloadJqGridAfterAddRecord(lineItemDetailFormsManager.gridName, lineItemGridSortName);
	       }
	       var salesTargetIds = $("#sosSalesTargetId", "#buildProposalParentDiv").multiselect("getChecked").map(function(){
	           return this.value;
	       }).get();
	       $("#lineItemTable").setCell(gsr, "sosSalesTargetId", salesTargetIds);
	       $("#lineItemTable").setCell(gsr, "lineItemID", lineItmFrm.lineItemID);
	       $("#lineItemTable").setCell(gsr, "pricingStatus", lineItmFrm.pricingStatus);
	       $("#lineItemTable").setCell(gsr, "viewableDisplayName", lineItmFrm.viewableDisplayName);
		   if($("#lineItemType_r", "#lineItemTargeting").is(':checked')) {
			   var productOptGrp = $("#sosProductId option:selected").parent("optgroup").attr("label");
			   if ($("#reservationExpiryDate", "#lineItemTargeting").val() == null || $("#reservationExpiryDate", "#lineItemTargeting").val() == "") {
				   $("#lineItemTable").setCell(gsr, "reserved", false);
				   if($("#proposalOwnerRole", "#buildProposalParentDiv").val() == 'PLR' && (productOptGrp == 'HOME PAGE' || productOptGrp == 'Display Cross Platform')){
					   $("#reserveChkBoxContainer", "#lineItemCalendarContainer").hide();
					   $("#reqForReservation", "#lineItemCalendarContainer").show();
				   }else{
					   $("#reserveChkBoxContainer", "#lineItemCalendarContainer").show();
					   $("#reqForReservation", "#lineItemCalendarContainer").hide();
				   }
			   }else{
				   $("#lineItemTable").setCell(gsr, "reserved", true);
				   $("#reserveChkBoxContainer", "#lineItemCalendarContainer").show();
				   $("#reqForReservation", "#lineItemCalendarContainer").hide();
				   if($("#proposalOwnerRole", "#buildProposalParentDiv").val() == 'PLR' && (productOptGrp == 'HOME PAGE' || productOptGrp == 'Display Cross Platform')){
					   $("#reserved", "#lineItemdetailForm").attr('disabled', 'disabled');
			    		$("#reservedChkBoxSpan").addClass('disabled-checkbox');
					   $('#lineItemdetailForm #sor').attr('readonly', true);
			 	       $('#lineItemdetailForm #sor').addClass('input-textbox-readonly');
			 	       $('#lineItemdetailForm #calculateSOR').attr("disabled", "disabled");
			 	       $('#lineItemdetailForm #calculateSOR').addClass("disabled-btn");
				   }else{
					   $("#reserved", "#lineItemdetailForm").removeAttr('disabled');
			    		$("#reservedChkBoxSpan").removeClass('disabled-checkbox');
					   $('#lineItemdetailForm #sor').attr('readonly', false);
			 	       $('#lineItemdetailForm #sor').removeClass('input-textbox-readonly');
			 	       $('#lineItemdetailForm #calculateSOR').removeAttr("disabled");
			 	       $('#lineItemdetailForm #calculateSOR').removeClass("disabled-btn");
				   }
			   }
		   }
		   	
		   showHideRenewLink();
		   if(lineItmFrm.pricingStatus == "Pricing Approved"){
				enableDisableLineItemFields(true);
			} else {
				enableDisableLineItemFields(false);
			}
		   var gridLineItemPricingData = jsonResponse.objectMap.gridLineItemsPricingData;
		   if(gridLineItemPricingData != null){
			   $.each(gridLineItemPricingData, function(i, value){
				   $("#lineItemTable").setCell(i, "pricingStatus", value);
			   });
		   }
	   }
	   $("#lineItemIdVal", "#lineItemdetailForm").val(lineItmFrm.lineItemID);
	   $("#lineItemPackageName", "#lineItemdetailForm").val(lineItmFrm.packageName);
	   if(lineItmFrm.sosLineItemID != ""){
		   $('#lineItemdetailForm #tr_sosLineItemId').show();
		   $("#sosLineItemIdVal", "#lineItemdetailForm").val(lineItmFrm.sosLineItemID);
	   }else{
		   $('#lineItemdetailForm #tr_sosLineItemId').hide();
	   }
	   getNetCPMImpressionsOfProposal();
	   $("#orderNumber", "#lineItemdetailForm").attr('checked', false);
	   expirationDate = $("#reservationExpiryDate", "#lineItemTargeting").val();
	   if(lineItmFrm.sosLineItemID != null){
			$('#lineItemdetailForm #tr_sosLineItemId').show();
			$("#sosLineItemIdVal", "#lineItemdetailForm").val(lineItmFrm.sosLineItemID);
		}else{
			$('#lineItemdetailForm #tr_sosLineItemId').hide();
		}
	   if($("#flight", "#buildProposalParentDiv").val() != ""){
		   $("#lineItemTable").setCell(gsr, "startDate", $("#flight", "#buildProposalParentDiv").val());
	   }
	   $('#pricingStatus', '#proposalLineItemContainer').val(lineItmFrm.pricingStatus);
	   if(lineItmFrm.packageName == null || lineItmFrm.packageName ==""){
		   $("#isViewable", "#lineItemdetailForm").attr("disabled", false);
	   }else{
		   $("#isViewable", "#lineItemdetailForm").attr("disabled", true);
	   }
	};
    
    lineItemDetailForm.doCustomProcessingAfterValidationFailedJson = function(jsonResponse, XMLHTTPRequest){
        if($("#pricingStatus" , "#lineItemTargeting").val() == "Pricing Approved"){
    		enableDisableLineItemFields(true);
    	}
		$('#messageHeaderFinancialDiv').html('');
		$('#buildProposalTab').tabs('select', 0);
        var errorArray = jsonResponse.errorList;
        jQuery.each(errorArray, function(i, error){
            if (error.field == "startDate" || error.field == "endDate" || error.field == "sosSalesTargetId_custom"  || error.field=="specType_custom") {
                //error.field =  error.field;
                jQuery("#" + error.field, "#" + lineItemDetailFormsManager.formName).addClass("errorElement errortip").attr("title", error.errorMessageForUI + '<BR><b>Help:</b> ' + error.errorHelpMessageForUI);
            }
            if(error.field == "sorExceeded") {
            	jQuery('#messageHeaderDiv', '#lineItemTargeting').html('');
            	new ModalDialogConfirm(error.errorMessageForUI + '<span>' + error.errorHelpMessageForUI + '</span>','',saveLineItemWithForceUpdate);
            }
            if(error.field == "geoTargeting") {
            	 jQuery("#geoTargetsummaryContainer" , "#lineItemTargeting").addClass("errorElement errortip").attr("title", error.errorMessageForUI);
            }
        });
        $('.errortip', "#" + lineItemDetailFormsManager.formName).qtip({
            style: {
                classes: 'ui-tooltip-red'
            },
            position: {
                my: 'bottom center',
                at: 'top center'
            }
        });
    };
	getDataForTargetingToSave();
	 if($("#lineItemType_e", "#lineItemTargeting").is(':checked')) {
		 $("#endDate","#buildProposalParentDiv").val($("#startDate","#buildProposalParentDiv").val());
	 }
    lineItemDetailForm.submit();
    }
}

function saveLineItemWithForceUpdate(){
	$("#sorExceededHidden", "#lineItemCalendarContainer").val(true);
	lineItemSaveData();
}

function setInvestmentValue() {
    var priceTypeVal = $('#priceType', '#lineItemdetailForm').val();
    if (priceTypeVal == "CPM"  || priceTypeVal == 'PRE EMPTIBLE' || priceTypeVal == 'CUSTOM UNIT') {
        var impressionTotal = $("#impressionTotal", "#lineItemdetailForm").val();
        var parseImpressionTotal = impressionTotal.replace(/,/g, '');
        var rate = $("#rate", "#lineItemdetailForm").val();
        var parseRate = rate.replace(/,/g, '');
        if(!isNaN(parseImpressionTotal) &&  !isNaN(parseRate)) {
			var totInvestment = (parseRate * parseImpressionTotal) / 1000;
        	totInvestment = roundoffto2places(totInvestment);
        	var totalInvestmentObject = $("#totalInvestment", "#lineItemdetailForm");
        	totalInvestmentObject.val(formatDecimal(totInvestment));	
		}
    }
}

function calculateSOV(){
    var totImpression = $("#impressionTotal", '#lineItemdetailForm').val();
    var parseTotImpression = totImpression.replace(/,/g, '');
    var totalPossibleImpressions = $("#totalPossibleImpressions", '#lineItemdetailForm').val();
    var parsetotalPossibleImpressions = totalPossibleImpressions.replace(/,/g, '');
    if (!isNaN(parseTotImpression) && !isNaN(parsetotalPossibleImpressions) && parseTotImpression != 0 && parsetotalPossibleImpressions != 0) {
        var sov = (((parseTotImpression) * 100) / parsetotalPossibleImpressions);
        sov = roundoffto2places(sov);
        var sovObject = $("#sov", "#lineItemdetailForm");
        sovObject.val(formatDecimal(sov));
    }
};

/**
 * Function fills the sequence No while creating new Line Item
 */
function fillSequenceNo(){
	var ajaxReq = new AjaxRequest();
	var optionId = $("#optionId","#buildProposalSearchForm").val();
	var propVerId =  $("#proposalversion","#buildProposalSearchForm").val();	
	ajaxReq.url = './proposalWorkflow/getNextSequenceNo.action?optionId='+optionId+'&proposalversion='+propVerId;
	ajaxReq.success = function(result, status, xhr){	
		$('#lineItemSequence','#lineItemdetailForm').val(result.objectMap.gridKeyColumnValue);
	};
	ajaxReq.submit();
}

/**
 * Function checks whether investment value is changed
 */
function totalInvestmentValueChanged(){
	 var old_Investment =$("#oldInvestment", "#buildProposalParentDiv").val();
	 var new_Investment =$("#totalInvestment", '#lineItemdetailForm').val();
	 if(old_Investment == new_Investment ) {
		 return false;
	 } else {
		 return true;
	 }
};


function setSpecTypeForProposal(){
	$("#specType", "#buildProposalParentDiv").multiselect({
		selectedList: 1,
		header: false
		
	}).multiselectfilter();
};
/**
 * Select check box of spec type
 * @param {Object} selectedGridRow
 */
function selectSpecType(specTypeVal){
    $("#specType").multiselect("uncheckAll");
    $("#specType").multiselect("widget").find(":checkbox").each(function(){
        for (var i = 0; i < specTypeVal.length; i++) {
            if ($(this).val() == specTypeVal[i]) {
                this.click();
                break;
            }
        }
    });
};

function reSetSpecType(){
    $("#specType").multiselect("uncheckAll");
    $("#specType").multiselect("widget").find(":checkbox").each(function(){
        if ($(this).val() == "RICH_MEDIA") {
            this.click();
        }
    });
}

function addFetchingAvailsText(){
    $('#availProgress').empty('');
    var currentDate = $.datepicker.formatDate('mm/dd/yy', new Date());
    var selectedDate = $('#startDate', "#buildProposalParentDiv").val();
    if (Date.parse(selectedDate) >= Date.parse(currentDate)) {
        $('#availProgress').append('<p style="padding:8px;margin:10px 0 0"><span class="fetching-avails">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><span>' + resourceBundle['label.fetching.avails'] + '</span></p>');
    } else {
        $('#availProgress').append('<p style="padding:8px;margin:10px 0 0"><span class="warning-dialog">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><span style="white-space:nowrap">' + resourceBundle['label.fetching.past.date.avails'] + '</span></p>');
    }
}

function showSTWeightSummary(){
    $("#STWeightSummary", "#lineItemdetailForm").qtip("option", "content.ajax.url", "");
    $("#STWeightSummary", "#lineItemdetailForm").qtip("option", "content.text", "<img src='./images/ajax-loader.gif' height='20px' width='20px' alt='Loading...' />");
    var sosSalesTargetId = $("#sosSalesTargetId", "#buildProposalParentDiv").multiselect("getChecked").map(function(){
        return this.value;
    }).get();
    var url = "./proposalWorkflow/getSTWeightSummary.action?sosSalesTargetId=" + sosSalesTargetId;
    $("#STWeightSummary", "#lineItemdetailForm").qtip("option", "content.ajax.url", url);
}

function proposalAvailStatus(){
	var ajaxReq = new AjaxRequest();
	ajaxReq.dataType = 'html';
	ajaxReq.url = "./avails/getAvailStatusSummary.action?proposalVersionID="+$("#proposalversion","#buildProposalSearchForm").val()+"&optionId="+$("#optionId","#buildProposalSearchForm").val();
	ajaxReq.success = function(result, status, xhr){
		$("#availStatusSummary").html(result);
		createProposalLineItemAvailsStatusDialog();
	}
	ajaxReq.submit();
}

function createProposalLineItemAvailsStatusDialog(){
    var proposalLineItemsAvailsStatusDialog = new ModalDialog();
    proposalLineItemsAvailsStatusDialog.height = 500;
    proposalLineItemsAvailsStatusDialog.width = 1100;
    proposalLineItemsAvailsStatusDialog.buttons = [{
        text: "Refresh Avails",
        click: function(){
            updateProposalLineItemAvails();
        }
    }, {
        text: "Close",
        click: function(){
	        $(".ui-dialog-buttonset button:contains('Refresh Avails')").button("enable");
            $("#availStatusSummary").dialog("close");
        }
    }];
    $("#availStatusSummary").dialog(proposalLineItemsAvailsStatusDialog);
    $("#availStatusSummary").dialog("open");
    enableDialogButton("availStatusSummary");
}

function updateProposalLineItemAvails() {
    var availsJsonData = new Array();
	var gsr = $("#lineItemTable", "#buildProposalParentDiv").jqGrid('getGridParam', 'selrow');
	var keyId = $("#lineItemTable", "#buildProposalParentDiv").jqGrid('getCell', gsr, 'lineItemID');
    $('[name=lineitem]:checked').each(function(){
        var lineItemData = new Object();
        lineItemData.lineItemId = $(this).val();
        lineItemData.avails = $("#avail_" + $(this).val()).text();
        lineItemData.capacity = $("#capacity_" + $(this).val()).text();
        lineItemData.availsDate = $("#availsDate_" + $(this).val()).text();
        availsJsonData.push(lineItemData);
		if (keyId == lineItemData.lineItemId) {
			updateAvailsOfCurrentLineItem(lineItemData.lineItemId, lineItemData.capacity, lineItemData.avails, lineItemData.availsDate);
		}
    });
    if (availsJsonData.length >= 1) {
        var ajaxReq = new AjaxRequest();
        ajaxReq.type = 'POST';
		ajaxReq.data = { "availsJsonData": JSON.stringify(availsJsonData) };
        ajaxReq.url = "./proposalWorkflow/updateAvails.action?proposalID="+$("#proposalID", "#buildProposalSearchForm").val()+"&proposalVersion=" + $("#proposalversion", "#buildProposalSearchForm").val() + "&optionID=" + $("#optionId", "#buildProposalSearchForm").val();
        ajaxReq.success = function(result, status, xhr){
        	$(".ui-dialog-buttonset button:contains('Refresh Avails')").button("enable");
		    $("#availStatusSummary").dialog("close");
			jQuery("#runtimeDialogDiv").model({theme: 'Success', message: resourceBundle['message.generic.lineItem.update.avail.successfully']});
            $("#errorDiv", "div[aria-labelledby=ui-dialog-title-availStatusSummary]").hide();
        };
        ajaxReq.submit();
		$(".ui-dialog-buttonset button:contains('Refresh Avails')").button("disable");
    } else {
        if ($("#errorDiv", "div[aria-labelledby=ui-dialog-title-availStatusSummary]").length == 0) {
            $(".ui-dialog-buttonpane", "div[aria-labelledby=ui-dialog-title-availStatusSummary]").append("<div id='errorDiv' class='model-dialog-error'>" + resourceBundle['error.ldap.select.LineItem.avails'] + "</div>");
        }
        $("#errorDiv", "div[aria-labelledby=ui-dialog-title-availStatusSummary]").show();
    }
}

function updateAvailsOfCurrentLineItem(lineItemId, capacity, avails, availsDate){
    var sov = 0.0;
    var impressionTotal = $("#impressionTotal", "#buildProposalParentDiv").val();
    var parseTotImpression = impressionTotal.replace(/,/g, '');
    var parsetotalPossibleImpressions = capacity.replace(/,/g, '');
    if (!isNaN(parseTotImpression) && !isNaN(parsetotalPossibleImpressions) && parseTotImpression != 0 && parsetotalPossibleImpressions != 0) {
        sov = (((parseTotImpression) * 100) / parsetotalPossibleImpressions);
        sov = roundoffto2places(sov);
    }   
	$("#avails","#buildProposalParentDiv").val(avails);
	$("#totalPossibleImpressions","#buildProposalParentDiv").val(capacity);
	$("#availsPopulatedDate","#buildProposalParentDiv").val(availsDate)
	$("#sov","#buildProposalParentDiv").val(sov)
}

/**
 * Checks all the custom checkboxes of line item grid if the header checkbox is checked else unchecks all the checkboxes.
 * @param select
 */
function customLineItemSelect(select){
    if ($(select).is(":checked")) {
        $("#lineItemTable", "#buildProposalParentDiv").find(':checkbox').each(function(){
            $(this).attr("checked", "checked");
        });
    } else {
        $("#lineItemTable", "#buildProposalParentDiv").find(':checkbox').each(function(){
            $(this).attr("checked", false);
        });
    }
}

/**
 * Formatter for creating the custom checkbox in the line item grid. 
 * @param cellvalue
 * @param options
 * @param rowObject
 * @returns {String}
 */
function lineItemMultiSelectFormatter(cellvalue, options, rowObject){
	return "<div align='center'><input type='checkbox' id='customLineItem_" + rowObject.lineItemID + "' name='customLineItem' onClick= 'selectUnselectHeaderChkBox(this)' class='allAvails' value='" + rowObject.lineItemID + "'>";
}

function lineItemTypeFormatter(cellvalue, options, rowObject){
	if(rowObject.productType == "R"){
		return "<div align='center'><img src='./images/line-item-r.png' />";
	}else if(rowObject.productType == "S"){
		return "<div align='center'><img src='./images/line-item-s.png' />";
	}else if(rowObject.productType == "E"){
		return "<div align='center'><img src='./images/line-item-e.png' />";
	}
}

/**
 * Function for deleting all the selected line items.
 */
function deleteLineItems(){
	var selectedIds="";
	$("#lineItemTable", "#buildProposalParentDiv").find(':checkbox').each(function(){
		if($(this).is(":checked")){
			selectedIds =  selectedIds + $(this).val() + ",";
		}
	});
	
	selectedIds = $.trim(selectedIds).substring(0, selectedIds.length - 1);	
	var ajaxReq = new AjaxRequest();
	ajaxReq.url = './proposalWorkflow/deleteProposalLineItems.action?lineItemIDs='+selectedIds+'&proposalversion='+$("#proposalversion","#buildProposalSearchForm").val()+'&proposalID='+$("#proposalID","#buildProposalSearchForm").val()+'&optionId='+$("#optionId","#buildProposalSearchForm").val();
	ajaxReq.success = function(result, status, xhr){
		getNetCPMImpressionsOfProposal();
		reloadJqGridAfterAddRecord("lineItemTable", lineItemGridSortName);
		processLineItemDelete(status);
		$("#LineItemMultiSelect_Header").attr("checked", false);
	};
	ajaxReq.submit();
}

/**
 * Function for checking the checkbox in the header of the line item grid if all the custom checkboxes are checked else uncheck the header checkbox. 
 * @param select
 */
function selectUnselectHeaderChkBox(select) {
	if(!($(select).is(":checked"))){
		$("#LineItemMultiSelect_Header").attr("checked", false);
	} 
	if($('[name=customLineItem]:not(:checked)',"#lineItemTable").size() == 0){
  		$("#LineItemMultiSelect_Header").attr("checked", true);
  	}		
}

function showHideLineItemGridIcons(flag){
    if (flag == "true") {
        $("#add_lineItemTable", "#lineItemGridHider").hide();
        $("#deleteLineItem_lineItemTable", "#lineItemGridHider").hide();
        $("#copyLineItem_lineItemTable", "#lineItemGridHider").hide();
        $("#updatePrice_lineItemTable", "#lineItemGridHider").hide();
		$("#arrangeLineItemSequence", "#lineItemGridHider").hide();
		$("#copyInOtherOption_lineItemTable", "#lineItemGridHider").hide();
		$("#splitLineItem_lineItemTable", "#lineItemGridHider").hide();
    } else {
        $("#add_lineItemTable", "#lineItemGridHider").show();
        $("#deleteLineItem_lineItemTable", "#lineItemGridHider").show();
        $("#copyLineItem_lineItemTable", "#lineItemGridHider").show();
        $("#updatePrice_lineItemTable", "#lineItemGridHider").show();
		$("#arrangeLineItemSequence", "#lineItemGridHider").show();
		$("#copyInOtherOption_lineItemTable", "#lineItemGridHider").show();
		$("#splitLineItem_lineItemTable", "#lineItemGridHider").show();
    }
}

function calculateBasePrice(){
	if($("#pricingStatus" , "#lineItemTargeting").val() == "Pricing Approved"){
		enableDisableLineItemFields(false);
	}
	var propPriceType = $('#propPriceType', '#buildProposalParentDiv').text();
	var agencyMargin = $('#propAgencyMargin', '#buildProposalParentDiv').text();
	
	var lineItemDetailFormsManager = new FormManager();
    lineItemDetailFormsManager.gridName = 'lineItemTable';
    lineItemDetailFormsManager.formName = 'lineItemdetailForm';
   
    var pricingCalculatorFrom = new JQueryAjaxForm(lineItemDetailFormsManager);   
    $("#lineItemdetailForm").attr("action", "./proposalWorkflow/calculateBasePrice.action?salesCategoryId=" + $("#salescategory", "#campaigndocumentaccordion").val() + '&proposalPriceType='+propPriceType + '&proposalAgencyMargin='+agencyMargin);
   
    pricingCalculatorFrom.doCustomProcessingAfterFormSucsesJson = function(jsonResponse, XMLHTTPRequest){
        var rateCardPrice = jsonResponse.objectMap.price;
        
        if (rateCardPrice == null) {
            if ($("#priceType", "#lineItemdetailForm").val() != "FLAT RATE") {
                rateCardPrice = "Not Defined";
            } else {
                rateCardPrice = "NA";
            }
            $("#offImpressionLink", "#lineItemFinancialFieldsContainer").hide();
        }else{
        	 if($("#rate", "#lineItemdetailForm").val() == "") {
     			$("#rate", "#lineItemdetailForm").val(rateCardPrice);
     		}
        	 if($("#priceType","#lineItemdetailForm").val()=="ADDED VALUE"){
        			$("#offImpressionLink", "#lineItemFinancialFieldsContainer").show();
        			$("#rate", "#lineItemdetailForm").val("0.00");
        		}else{
        			$("#offImpressionLink", "#lineItemFinancialFieldsContainer").hide();
        		}
        }
		 var calculationStep = jsonResponse.objectMap.priceCalSummary;
        $("#rateCardPrice", "#lineItemdetailForm").val(rateCardPrice);
		$("#priceCalSummary", "#lineItemdetailForm").val(calculationStep);
		calculateDiscountPercent();
		if($("#pricingStatus" , "#lineItemTargeting").val() == "Pricing Approved"){
			enableDisableLineItemFields(true);
		}
    };
    
    pricingCalculatorFrom.doCustomProcessingAfterValidationFailedJson = function(jsonResponse, XMLHTTPRequest){
        var errorArray = jsonResponse.errorList;
        jQuery.each(errorArray, function(i, error){
            if (error.field == "sosSalesTargetId_custom" || error.field == "specType_custom") {
                jQuery("#" + error.field, "#" + lineItemDetailFormsManager.formName).addClass("errorElement errortip").attr("title", error.errorMessageForUI + '<BR><b>Help:</b> ' + error.errorHelpMessageForUI);
            }
        });
        $('.errortip', "#" + lineItemDetailFormsManager.formName).qtip({
            style: {
                classes: 'ui-tooltip-red'
            },
            position: {
                my: 'bottom center',
                at: 'top center'
            }
        });
    };
    getDataForTargetingToSave();
    pricingCalculatorFrom.submit();
}

function updateMultiLineItemPrice(){
    var selectedIds = "";
    $("#lineItemTable", "#buildProposalParentDiv").find(':checkbox').each(function(){
        if ($(this).is(":checked")) {
            selectedIds = selectedIds + $(this).val() + ",";
        }
    });
    selectedIds = $.trim(selectedIds).substring(0, selectedIds.length - 1);
	var propPriceType = $('#propPriceType', '#buildProposalParentDiv').text();
	var propAgencyMargin = $('#propAgencyMargin', '#buildProposalParentDiv').text();
    
	var ajaxReq = new AjaxRequest();
    ajaxReq.url = './proposalWorkflow/updateAllLineItemPrice.action?proposalID='+$("#proposalID", "#buildProposalSearchForm").val()+'&lineItemIds=' + selectedIds + '&salesCategoryId=' + $("#salescategory", "#campaigndocumentaccordion").val() + '&proposalPriceType='+propPriceType+'&proposalAgencyMargin='+propAgencyMargin 
    				+ '&optionId=' + $("#optionId","#buildProposalSearchForm").val();
    ajaxReq.success = function(result, status, xhr){
        reloadJqGridAfterAddRecord("lineItemTable", lineItemGridSortName);
        jQuery("#runtimeDialogDiv").model({theme: 'Success', message: resourceBundle['message.generic.pricing.updated.Successfully']});
        $("#LineItemMultiSelect_Header").attr("checked", false);
        customLineItemSelect($("#LineItemMultiSelect_Header"));
    };
    ajaxReq.submit();
}

function showPriceCalSummary(){
    $("#pricingCalculationStep", "#lineItemdetailForm").qtip("option", "content.ajax.url", "");
    $("#pricingCalculationStep", "#lineItemdetailForm").qtip("option", "content.text", "<img src='./images/ajax-loader.gif' height='20px' width='20px' alt='Loading...' />");	
	var url = "./proposalWorkflow/getPricingCalculationStep.action";
	var data = {
			"lineItemId": $('#lineItemdetailForm #lineItemID').val(),
			"rateCardPrice": $('#rateCardPrice', '#lineItemdetailForm').val(),
			"priceCalSummary": $('#priceCalSummary', '#lineItemdetailForm').val(),
			"proposalPriceType": $('#propPriceType', '#buildProposalParentDiv').text(),
			"proposalAgencyMargin": $('#propAgencyMargin', '#buildProposalParentDiv').text()
	};
	$("#pricingCalculationStep", "#lineItemdetailForm").qtip("option", "content.ajax.data", data);
    $("#pricingCalculationStep", "#lineItemdetailForm").qtip("option", "content.ajax.url", url);
}

function showOfferedImpressions(){
	$("#offImpressionLink", "#lineItemFinancialFieldsContainer").qtip("option", "content.ajax.url", "");
	$("#offImpressionLink", "#lineItemFinancialFieldsContainer").qtip("option", "content.text", "<img src='./images/ajax-loader.gif' height='20px' width='20px' alt='Loading...' />");	
	
	var url = "./proposalWorkflow/getOffImpressions.action";
	var data = {
			"rateCardPrice":  $('#rateCardPrice', '#lineItemdetailForm').val(),
			 "optionId": $("#optionId","#buildProposalSearchForm").val(),
			 "lineItemId": $('#lineItemdetailForm #lineItemID').val()
	};
	$("#offImpressionLink", "#lineItemFinancialFieldsContainer").qtip("option", "content.ajax.data",data);
	
	$("#offImpressionLink", "#lineItemFinancialFieldsContainer").qtip("option", "content.ajax.url", url);
	
}

/**
 * Copies all the line items from a proposal or a package.
 */
function copyAlllineItems(proposalId, isBreakablePkg, expiredFlag, optionId){
	if (expiredFlag == "Yes") {
        expiredFlag = true;
    } else {
        expiredFlag = false;
    }
    var ajaxReq = new AjaxRequest();
    ajaxReq.url = "./manageProposal/getLineItems.action?packageId=" + proposalId + "&searchType=package" + "&lineitemNumbers=1&copiedFromExpired=" + expiredFlag 
    + "&version=" + $('#proposalversion', "#buildProposalSearchForm").val() + "&proposalValue=" + $('#proposalID', "#buildProposalSearchForm").val() 
    + "&parentOptionId=" + $("#optionId", "#buildProposalSearchForm").val() + "&isBreakablePkg=" + isBreakablePkg;
    
    ajaxReq.success = function(result, status, xhr){
        var flag = result.objectMap.gridKeyColumnValue;
        if (flag != true) {
            if (result.objectMap.isError == true) {
                jQuery("#runtimeDialogDiv").model({theme: 'Error', autofade: false, message: resourceBundle['error.copyAll.lineItems']});
            } else {
                jQuery("#runtimeDialogDiv").model({ theme: 'Success', message: resourceBundle['message.active.lineItems']});
                reloadlineItemGrid();
                getNetCPMImpressionsOfProposal();
            }
        }
        else {
			jQuery("#runtimeDialogDiv").model({theme: 'Error', autofade: false, message: resourceBundle['error.generic.addpkglineitems']});
        }
    };
    ajaxReq.submit();
}

/**
 * Returns true if the total count of line item is greater than 500
 */
function lineItemErrors(length) {
	var lineitemValue = $('#lineitemNumber').val();
	if (lineitemValue == '') {
		lineitemValue = 1;
	}
	return (lineitemValue * length > 300) ? true : false;
}

/**
 * Formatter to tell whether Line Item has reservation or not
 */
function reservationFormatter(cellvalue, options, rowObject){
	 if (cellvalue) {
		return "Yes";
	} else {
		return "No";
	}
}

function resetStartEndDate(){
	var startDate = $('#proposalStartDate', '#buildProposalParentDiv').text();
    var endDate = $('#proposalEndDate', '#buildProposalParentDiv').text();
    $("#startDate", "#buildProposalParentDiv").val(startDate);
    if($("#lineItemType_e", "#lineItemTargeting").is(':checked')) {
		$("#endDate", "#buildProposalParentDiv").val(startDate);
	}else{
		$("#endDate", "#buildProposalParentDiv").val(endDate);
	}
}


function enableDisableLineItemFields(disableFields){
	if(disableFields){
		$("#sosProductId", "#lineItemdetailForm").select2("disable");
		$("#addTargetToLineItem", "#lineItemdetailForm").attr("disabled" , true);
		$("#addTargetToLineItem", "#lineItemdetailForm").addClass("disabled-btn");
		$('#lineItemdetailForm #totalInvestment').addClass('input-textbox-readonly');
        $('#lineItemdetailForm #totalInvestment').attr('readonly', true);
        $('#lineItemdetailForm #rate').attr('readonly', true);
        $('#lineItemdetailForm #rate').addClass('input-textbox-readonly');
        $("#priceType", "#lineItemdetailForm").attr("disabled", true);
        if($("#lineItemType_s").is(":checked")){
        	$("#sosSalesTargetId", "#lineItemdetailForm").multiselect("disable");        	
        } else {
        	$("#sosSalesTargetId", "#lineItemdetailForm").select2("disable");
        }
	} else {
		$("#sosProductId", "#lineItemdetailForm").select2("enable");
		$("#addTargetToLineItem", "#lineItemdetailForm").attr("disabled" , false);
		$("#addTargetToLineItem", "#lineItemdetailForm").removeClass("disabled-btn");
        $("#priceType", "#lineItemdetailForm").attr("disabled", false);
        if($("#lineItemType_s").is(":checked")){
        	$("#sosSalesTargetId", "#lineItemdetailForm").multiselect("enable");
        } else {
        	$("#sosSalesTargetId", "#lineItemdetailForm").select2("enable");
        }
	}
}

function arrangeLineItemSequence(movePlaces){
    var selectedIds = "";
    $("#lineItemTable", "#buildProposalParentDiv").find(':checkbox').each(function(){
        if ($(this).is(":checked")) {
            selectedIds = selectedIds + $(this).val() + ",";
        }
    });
    if (selectedIds == "") {
		jQuery("#runtimeDialogDiv").model({theme: 'Error', autofade: false, message: resourceBundle['error.lineitem.sequence.rearrange']});
		return;
    }
    var ajaxReq = new AjaxRequest();
    ajaxReq.url = "./proposalWorkflow/arrangeLineItemSequence.action"
						+ "?optionId=" + $("#optionId", "#buildProposalSearchForm").val()
						+ "&proposalVersion=" + $("#proposalversion","#buildProposalSearchForm").val()
						+ "&positionToMove=" + movePlaces + "&lineItemsToMove=" + selectedIds;
    ajaxReq.success = function(result, status, xhr){
		reloadJqGridAfterAddRecord("lineItemTable", "lineItemSequence", "asc");
    };
    ajaxReq.submit();
}

function showProductCreativeSummary(){
	$("#productCreativeSummary", "#lineItemdetailForm").qtip("option", "content.ajax.url", "");
	$("#productCreativeSummary", "#lineItemdetailForm").qtip("option", "content.text", "<img src='./images/ajax-loader.gif' height='20px' width='20px' alt='Loading...' />");
	var url = "./proposalWorkflow/showProductCreativesSummary.action";
	var data = {
			"sosProductId": $("#sosProductId", "#lineItemdetailForm").val() == "" ? 0 : $("#sosProductId", "#lineItemdetailForm").val()
	};
	$("#productCreativeSummary", "#lineItemdetailForm").qtip("option", "content.ajax.data", data);
    $("#productCreativeSummary", "#lineItemdetailForm").qtip("option", "content.ajax.url", url);
}

function copyLineItemInOtherOption(){
	var ajaxReq = new AjaxRequest();
    ajaxReq.url = "./proposalWorkflow/getProposalOptionDetails.action?proposalID=" + $("#proposalID", "#buildProposalSearchForm").val() + "&optionID=" + $("#optionId", "#buildProposalSearchForm").val();
    ajaxReq.dataType = 'html';
    ajaxReq.success = function(result, status, xhr){
		var proposalOptionModalDialog = new ModalDialog();
        proposalOptionModalDialog.width = 510;
        proposalOptionModalDialog.height = 280;
        proposalOptionModalDialog.title = 'Copy Line Item';
        proposalOptionModalDialog.buttons = [{
            text: "Copy",
            click: function(){
        		copyLineItemInOption();
            }
        }, {
            text: "Close",
            click: function(){
                $("#copyPasteInOtherOptionDialogue").dialog("close");
            }
        }];
        $("#copyPasteInOtherOptionDialogue").dialog(proposalOptionModalDialog);
		$("#copyPasteInOtherOptionDialogue").html(result);
        $("#copyPasteInOtherOptionDialogue").dialog("open");
        $("#copyPasteInOtherOptionDialogue").dialog({
        	beforeClose : function( event, ui ) { 
        		$("#LineItemMultiSelect_Header").attr("checked", false);
        		customLineItemSelect($("#LineItemMultiSelect_Header"));
        		$(".ui-dialog-buttonset button").show();
        		$(".ui-dialog-buttonset button").button("enable");
        		$(this).dialog("destroy").remove();
        		$('#copyInOtherOptionParentDiv').html('');
        		$('#copyInOtherOptionParentDiv').html('<div id="copyPasteInOtherOptionDialogue" style="display: none" title="Copy Line Item"></div>');
    	 	}
    	});
		$(".ui-dialog-buttonpane", "div[aria-labelledby=ui-dialog-title-copyPasteInOtherOptionDialogue]").append("<div id='errorProposalOptionSelect' class='model-dialog-error'>" + resourceBundle['error.select.option'] + "</div>");
        $("#errorProposalOptionSelect", "div[aria-labelledby=ui-dialog-title-copyPasteInOtherOptionDialogue]").hide();
    };
    ajaxReq.submit();
}

function copyLineItemInOption(){
	var selectedOptionIds = []; 
	$('[name=proposalOptionId]:checked', '#copyPasteInOtherOptionDialogue').each(function(){
		selectedOptionIds.push($(this).val());
     });

    if (selectedOptionIds.length == 0) {
         $("#errorProposalOptionSelect", "div[aria-labelledby=ui-dialog-title-copyPasteInOtherOptionDialogue]").show();
    } else { 
        var lineItemIds = "";
        $("#lineItemTable", "#buildProposalParentDiv").find(':checkbox').each(function(){
            if ($(this).is(":checked")) {
            	lineItemIds = lineItemIds + $(this).val() + ",";
            }
        });
        $("#errorProposalOptionSelect", "div[aria-labelledby=ui-dialog-title-copyPasteInOtherOptionDialogue]").hide();
        var ajaxReq = new AjaxRequest();
        ajaxReq.url = './proposalWorkflow/copyAndPasteLineItem.action?proposalID=' + $("#proposalID", "#buildProposalSearchForm").val() + '&fromOptionId=' + $("#optionId", "#buildProposalSearchForm").val() + '&toOptionId=' + selectedOptionIds + "&lineItemId=" + lineItemIds;
        ajaxReq.success = function(result, status, xhr){
            $(".ui-dialog-buttonset button:contains('Copy')").button("enable");
            $("#copyPasteInOtherOptionDialogue").dialog("close");
            jQuery("#runtimeDialogDiv").model({theme: 'Success', message: resourceBundle['lineitem.copied.successfully.in.other.option']});
        };
        ajaxReq.submit();
        $(".ui-dialog-buttonset button:contains('Copy')").button("disable");
    }    
}


function splitLineItems(){
	var selectedIds = "";
    $("#lineItemTable", "#buildProposalParentDiv").find(':checkbox').each(function(){
    	if ($(this).is(":checked")) {
    		selectedIds = selectedIds + $(this).val() + ",";
    	}
    });
	selectedIds = $.trim(selectedIds).substring(0, selectedIds.length - 1);
    var ajaxReq = new AjaxRequest();
    var propId = $("#proposalID", "#buildProposalSearchForm").val();
    var propVersion = $("#proposalversion", "#buildProposalSearchForm").val();
    ajaxReq.url = './proposalWorkflow/splitLineItems.action?proposalID=' + propId + '&lineItemIds=' + selectedIds + '&proposalversion=' + propVersion + '&optionId=' + $("#optionId", "#buildProposalSearchForm").val();
    ajaxReq.success = function(result, status, xhr){
    	if(result.objectMap.lineItemsImpressionsTooLow){
    		jQuery("#runtimeDialogDiv").model({theme: 'Error', autofade: false, width: 310, height: 170, message: resourceBundle['error.Impressions.value.too.low.cannot.split.lineItems']});
    	}else{
    		getNetCPMImpressionsOfProposal();
            reloadJqGridAfterAddRecord("lineItemTable", lineItemGridSortName);
			jQuery("#runtimeDialogDiv").model({theme: 'Success', message: resourceBundle['message.generic.lineItemSplitedSuccessfully']});
            $("#LineItemMultiSelect_Header").attr("checked", false);	
    	}
    };
    ajaxReq.submit();
}