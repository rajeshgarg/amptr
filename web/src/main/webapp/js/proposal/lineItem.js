/**
 * This Java Script file contains all the methods related to LineItems in the Build Proposal and Manage Package screen
 */

$(document).ready(function() {
	// Initialise the Targeting Type Element's multiselect
	$("#sosTarTypeElement", "#lineItemTargeting").multiselect({
		selectedList: 2,
	}).multiselectfilter();
	// Bind the check that negative values are not permitted
	jQuery("#sor", "#lineItemTargeting").numeric({
		negative: false
	});
});

/**
 * Function for adding the new targeting to the lineItem Targeting Table
 */
function addTargetsToLineItem(){
	if (validateAddTargets()) {
		var valueToPrint = "";
		var selectedElements = [];
		var level = "--";
		var not= "--";
		var operation = "--";
		var previousRowId = $("#geoTargetsummary" , "#lineItemTargeting").find("tr:last").attr("id");
		var rowBeforeLastRowId = $("#geoTargetsummary" , "#lineItemTargeting").find("tr:last").prev('tr').attr("id");
		var rowName = "geoTargetsummary";
		if($('#geoTargetsummary tr').length == 1){
			var rowCount = 1;
		}else{
			var rowCount = Number(previousRowId.substring(rowName.length)) + 1;
			var prevRowCount = Number(rowBeforeLastRowId.substring(rowName.length)) + 1;
			if(prevRowCount > rowCount){
				rowCount = prevRowCount;
			}
		}
			
		var rowId = "geoTargetsummary" + rowCount;
			
		if("Frequency Cap" == $('#' + previousRowId + " td:nth-child(2)").html()){
			operation = "<select onChange='setPlacementNameAndTargetingString();' id='targetAction_" + rowId + "'><option value='and'>And</option> <option value='or'>Or</option></select>";
			$('#targetAction_' +rowId).val("and");
		}else{
			var prevRowOperation = "<select id='targetAction_" + previousRowId + "'><option value='and'>And</option> <option value='or'>Or</option></select>";
			$('#targetAction_' +previousRowId).val("and");
			$('#' + previousRowId + " td:nth-child(7)").html(prevRowOperation);
		}
			
		if ("Frequency Cap" != $("#sosTarTypeId option:selected").text()) {
			not = "<input type='checkbox' id='not_" + rowId + "' >";	
		}
			
		if("Behavioral" == $("#sosTarTypeId option:selected").text()){
			var level = "<select id='revenueScienceSegId_" + rowId + "' size='1'><option value='LEVEL1'>1</option> <option value='LEVEL2'>2</option></select>"
			$('#revenueScienceSegId_' +rowId).val("LEVEL1");
		}
			
		$("#sosTarTypeElement :selected").each(function(){
			selectedElements.push($(this).text());
		});
			
		valueToPrint = "<tr id='" + rowId + "'><td width='12%'>" + not + "</td><td width='18%'>" + $("#sosTarTypeId option:selected").text() + "</td><td style='display : none'>" + $("#sosTarTypeId").val();
		if ("Zip Codes" == $("#sosTarTypeId option:selected").text() || "Behavioral" == $("#sosTarTypeId option:selected").text()) {
			valueToPrint = valueToPrint + "</td><td width='50%' title='" + $("#tarTypeElementText").val() + "' style=' white-space: nowrap;  overflow: hidden; text-overflow: ellipsis;'>" + $("#tarTypeElementText").val() + "</td><td style='display : none'>" + $("#tarTypeElementText").val() + "</td>";
		}else {
			valueToPrint = valueToPrint + "</td><td width='50%' title='" + selectedElements.join(", ") + "' style=' white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>" + selectedElements.join(", ") + "</td><td style='display : none'>" + $("#sosTarTypeElement").val() + "</td>";
		}
			
		valueToPrint = valueToPrint + "<td width='10%'>" + level + "</td>" + "</td><td width='10%'>" +  operation +"</td>" + "<td width='10%'>" + "<a style='float:left;margin-left:5px' title='Edit' onClick=\"editLineItemTarget('" + rowId + "')\"> <span class='ui-icon ui-icon-pencil' style='cursor:pointer;'/></a> <a style='float:left;margin-left:5px' title='Delete' onClick=\"deleteLineItemTarget('" + rowId + "')\"> <span class='ui-icon ui-icon-trash' style='cursor:pointer;'/></a></td></tr>";
			
		if("Frequency Cap" == $('#' + previousRowId + " td:nth-child(2)").html()){
			$("#geoTargetsummary" , "#lineItemTargeting").find("tr:last").before(valueToPrint);
		}else{
			$('#geoTargetsummary').append(valueToPrint);
		}
			
		resetTargetingFields();
		$("#not_" + rowId).bind("change", function () {
			setPlacementNameAndTargetingString();
		});
		
		$("#targetAction_" + previousRowId).bind("change", function () {
			setPlacementNameAndTargetingString();
		});
		
		if(level != "--"){
			$("#revenueScienceSegId_" + rowId).bind("change", function () {
				 $("#rateCardPrice").val("");
				 $("#offImpressionLink", "#lineItemFinancialFieldsContainer").hide();
			});
		}
		setBasePrice();
		setPlacementNameAndTargetingString();
		if ($("#lineItemType_r", "#lineItemTargeting").is(':checked') || $("#lineItemType_e", "#lineItemTargeting").is(':checked')) {
			$('#geoTargetsummary tbody tr').each(function(){
				$('td:eq(0),td:eq(5),td:eq(6)', this).hide();
			});
		}
	}
}
/**
 * Set rateCardPrice value on the basis of Price Type
 */
function setBasePrice(){
    if ($('#priceType').val() != "FLAT RATE") {
        $("#rateCardPrice").val("");
    } else {
        $("#rateCardPrice").val("NA");
    }
     $("#offImpressionLink", "#lineItemFinancialFieldsContainer").hide();
     // Enable the RateX check-box and its label in case of Manage Package LineItems only
	setRateXVal();
}

/**
 * While setting the LineItem's targeting data when LineItem's consolidated data gets loaded,
 * this function dynamically display rows of the targeting to the lineItem TargetingTable
 */
function displayTargetsOfLineItem(item){
	var valueToPrint = "";
	var rowCount = $('#geoTargetsummary tr').length;
	var rowId= "geoTargetsummary" + rowCount;
	var level = "--" ;
	var not = "--";
	var operation =  "<select id='targetAction_" + rowId + "'><option value='and'>And</option> <option value='or'>Or</option></select>";
	
	not = "<input type='checkbox' id='not_" + rowId + "' >";
	if("Behavioral" == item.sosTarTypeName){
		level = "<select id='revenueScienceSegId_" + rowId + "' size='1'><option value='LEVEL1'>1</option> <option value='LEVEL2'>2</option></select>";
	}else if("Frequency Cap" == item.sosTarTypeName){
		not = "--";
	}
	
	valueToPrint = "<tr id='"+rowId+"'><td width='12%'>"+ not +"</td>";
	valueToPrint = valueToPrint + "<td width='18%'>" + item.sosTarTypeName + "</td><td style='display : none'>"+ item.sosTarTypeId +"</td><td width='50%' title='"+item.sosTarTypeElementName+"' style=' white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>" + item.sosTarTypeElementName + "</td><td style='display : none'>"+ item.sosTarTypeElement +"</td><td width='10%'>" + level +"</td><td width='10%'>" + operation + "</td>" + "<td width='10%' >" + "<a style='float:left;margin-left:5px' title='Edit' onClick=\"editLineItemTarget('" + rowId + "')\"> <span class='ui-icon ui-icon-pencil' style='cursor:pointer;'/></a><a style='float:left;margin-left:5px' title='Delete' onClick=\"deleteLineItemTarget('" + rowId + "')\"> <span class='ui-icon ui-icon-trash' style='cursor:pointer;'/></a></td></tr>";
	$('#geoTargetsummary').append(valueToPrint);
	
	var select = 'targetAction_' + rowId; 
	if (item.operation != null) {
		$('#targetAction_' +rowId).val(item.operation);
	}else{
		$('#' +select).val("and");
	}
	
	if (item.negation != null ) {
		$('#not_' +rowId).attr('checked', true);
	}
	
	if(item.segmentLevel != null){
		$('#revenueScienceSegId_' +rowId).val(item.segmentLevel);
		$("#revenueScienceSegId_" + rowId).bind("change", function () {
			 $("#rateCardPrice").val("");
			 $("#offImpressionLink", "#lineItemFinancialFieldsContainer").hide();
		});
	}
	
	resetTargetingFields();
	$("#targetAction_" + rowId).bind("change", function () {
		setPlacementNameAndTargetingString();
	});
	
	$("#not_" + rowId).bind("change", function () {
		setPlacementNameAndTargetingString();
	});
	if ($("#lineItemType_r", "#lineItemTargeting").is(':checked') || $("#lineItemType_e", "#lineItemTargeting").is(':checked')) {
		$('td:eq(0),td:eq(5),td:eq(6)', '#' + rowId).hide();
	}
}


/**
 * Function resets the Targeting fields from the lineItem Targeting Table
 */
function resetTargetingFields(){
	clearCSSErrors("#lineItemTargetingTable", "#lineItemTargeting");
	$("#sosTarTypeId").val("");
	$('#sosTarTypeId').attr('disabled', false);
	$('option', $("#sosTarTypeElement", "#lineItemTargeting")).remove();
	$("#tarTypeElementText").hide();
	$("#sosTarTypeElement_custom").show();
	$("#tarTypeElementText").val("");
	$("#sosTarTypeElement", "#lineItemTargeting").val('');
	$("#sosTarTypeElement", "#lineItemTargeting").multiselect("refresh");
	$("#addTargetToLineItem").show();
	$("#updateTargetToLineItem").hide();
	$("#targetResetData").val('RESET');
	if($("#lineItemTargeting").find(".errortip").length > 0 ){
    	$('#messageHeaderDiv', '#lineItemTargeting').html(resourceBundle['validation.error']).addClass('error');
    }else if($("#lineItemTargeting").find(".errortip").length == 0){
    	$('#messageHeaderDiv', '#lineItemTargeting').html('');
    }
}

/**
 * Function dynamically delete rows from the lineItem Targeting Table
 */
function deleteLineItemTarget(trId){
	if($("#pricingStatus" , "#lineItemTargeting").val() == "Pricing Approved"){
		jQuery("#runtimeDialogDiv").model({theme: 'Error', autofade: false, message: resourceBundle['error.cannot.delete.targeting']});
	} else {
		var lastRowId = $("#geoTargetsummary" , "#lineItemTargeting").find("tr:last").attr("id");
		if(trId == lastRowId){
			var rowId = $("#geoTargetsummary" , "#lineItemTargeting").find("tr:last").prev('tr').attr("id");
			$('#' + rowId + " td:nth-child(7)").html('--');
		}
		var targetTypeText = $('#' + trId + " td:nth-child(2)").html();
		$('#' + trId).remove();
		resetTargetingFields();
		
		setPlacementNameAndTargetingString();
		setBasePrice();
	}
}


/**
 * Function edits the row of a lineItem Targeting Table
 */
function editLineItemTarget(trId){
	$("#targetResetData").val('CANCEL');
	$("#sosTarTypeId").val($("#" + trId + " td:nth-child(3)").html());
	$('#sosTarTypeId').attr('disabled', true);
	if ("Zip Codes" == $("#sosTarTypeId option:selected").text() || "Behavioral" == $("#sosTarTypeId option:selected").text()) {
		$("#tarTypeElementText").show();
		$("#sosTarTypeElement_custom").hide();
		$("#tarTypeElementText").val($("#" + trId + " td:nth-child(5)").text());
	}
	else {
		$("#tarTypeElementText").hide();
		$("#sosTarTypeElement_custom").show();
		fillTargetTypeElementForLineItem($("#" + trId + " td:nth-child(5)").html());
	}
	$("#addTargetToLineItem").hide();
	$("#updateTargetToLineItem").show();
	if($("#pricingStatus" , "#lineItemTargeting").val() == "Pricing Approved"){
		$("#updateTargetToLineItem", "#lineItemdetailForm").attr("disabled" , true);
		$("#updateTargetToLineItem", "#lineItemTargeting").removeClass("btn-sec");
		$("#updateTargetToLineItem", "#lineItemTargeting").addClass("disabled-btn");
	}else{
		$("#updateTargetToLineItem", "#lineItemdetailForm").attr("disabled" , false);
		$("#updateTargetToLineItem", "#lineItemTargeting").removeClass("disabled-btn");
		$("#updateTargetToLineItem", "#lineItemTargeting").addClass("btn-sec");
	}
	$("#updateTargetToLineItem").attr("onclick","updateTargetsToLineItem('"+trId+"')")
}

/**
 * Update the values in lineItem Targeting Table based on rowId
 */
function updateTargetsToLineItem(trId){
	if (validateAddTargets()) {
		var not = "--";
		var selectedElements = [];
		$('#' + trId + " td:nth-child(2)").html($("#sosTarTypeId option:selected").text());
		$('#' + trId + " td:nth-child(3)").html($("#sosTarTypeId").val());
	
		if ("Zip Codes" == $("#sosTarTypeId option:selected").text() || "Behavioral" == $("#sosTarTypeId option:selected").text()) {
			$('#' + trId + " td:nth-child(4)").attr("title", $("#tarTypeElementText").val());
			$('#' + trId + " td:nth-child(4)").html($("#tarTypeElementText").val());
			$('#' + trId + " td:nth-child(5)").html($("#tarTypeElementText").val());
		}
		else {
			$("#sosTarTypeElement :selected").each(function(){
				selectedElements.push($(this).text());
			});
			$('#' + trId + " td:nth-child(4)").attr("title", selectedElements.join(", "));
			$('#' + trId + " td:nth-child(4)").html(selectedElements.join(", "));
			$('#' + trId + " td:nth-child(5)").html($("#sosTarTypeElement").val().join(","));
		}
		resetTargetingFields();
		setPlacementNameAndTargetingString();
		setBasePrice();
	}
}


/**
 * Function creates a JsonString from the lineItem Targeting Table to pass it to server 
 */
function getDataForTargetingToSave(){
	var lineItemTargetings = new Array();
	var objId ="";
	var action = "";
	var not = "";
	var rowId = $("#geoTargetsummary" , "#lineItemTargeting").find("tr:last").attr("id");
	
	$("#lineItemTargetingData").val("");
    $("#geoTargetsummary tr").each(function(){
		objId = $(this).attr("id");
        if (objId != "geoTargetsummaryHeader") {
			 var object = new Object();
			 var level="";
			 if(rowId != objId){
				 action = $('#targetAction_' +objId + " option:selected").val();
			 }else{
				 action = "";
			 }
			 object.action = action;
		     object.targetTypeId = $("#" + objId + " td:nth-child(3)").html();
		     object.targetTypeElement = $("#" + objId + " td:nth-child(5)").html();
		     if ($('#not_' +objId).is(':checked')) {
					not = "Not";
		     }else{
		     	not = "";
		     }
		     
		     object.not = not;
		     if("Behavioral" == $("#" + objId + " td:nth-child(2)").html()){
		    	 level = $('#revenueScienceSegId_' +objId + " option:selected").val();
		     }
		     object.segmentLevel = level;
		     lineItemTargetings.push(object);
            $("#lineItemTargetingData").val(JSON.stringify(lineItemTargetings));
        }
    });
}


/**
 * Fill TargetTypeElement while choosing TargetType from the lineItem Targeting Table e.g. Adx DMA, Age, Countries etc
 */
function fillTargetTypeElementForLineItem(elementsId){
	var url = "";
	var tarTypeElem = "";
	var flag = false;
	clearCSSErrors("#lineItemTargetingTable", "#lineItemTargeting");
	$("#tarTypeElementText").hide();
	if ($('#sosTarTypeId').val() == "") {
		$("#sosTarTypeElement_custom").show();
		$('#sosTarTypeElement >option').remove();
		$("#sosTarTypeElement", "#lineItemTargeting").val('');
		$("#sosTarTypeElement", "#lineItemTargeting").multiselect("refresh");
	}else if("Zip Codes" == $("#sosTarTypeId option:selected").text() || "Behavioral" == $("#sosTarTypeId option:selected").text()){
		$("#tarTypeElementText").show();
		$("#sosTarTypeElement_custom").hide();
		$("#tarTypeElementText").focus();
		$("#tarTypeElementText").val("");
	}else {
		$("#sosTarTypeElement_custom").show();
		var ajaxReq = new AjaxRequest();
		ajaxReq.url = $("#contextPath").val() +"/proposalWorkflow/getTargetTypeElements.action?sosTarTypeId=" + $('#sosTarTypeId').val() + "&lineItemID=" + $('#lineItemID').val();
		ajaxReq.success = function(result, status, xhr){	
			var returnedId = result.objectMap.gridKeyColumnValue;
			var select = $("#sosTarTypeElement", "#lineItemTargeting");
			$('option', select).remove();
			sortList(returnedId, select);
			if(elementsId != null){
				var selectedElements = elementsId.split(",");
				$(select).val(selectedElements);
			}
			$('#sosTarTypeElement').animate({
				scrollTop: $('#sosTarTypeElement').offset(0)
			}, 'fast');
			$(select).multiselect("refresh");
		};
		ajaxReq.submit();
	}
}

/**
 * It validates the targeting details entered by user in the lineItem Targeting Table fields
 */
function validateAddTargets(){
    	var returnFlag = true;
    	clearCSSErrors("#lineItemTargetingTable", "#lineItemTargeting");
    	// Target Type should not be null while adding the targeting to the lineItem Targeting Table
    	if ($('#sosTarTypeId').val() == "") {
			$('#sosTarTypeId').addClass("errorElement errortip").attr("title", "Target Type is mandatory." + '<BR><b>Help:</b> ' + "Please select valid data in the field.");
			renderErrorQtip("#lineItemTargeting");
			returnFlag = false;
		}
		// Zip Codes and Behavioral textboxes should not be null or should be valid, if they are choosen as the Target Type
		if ($('#sosTarTypeElement').val() == null && "Zip Codes" != $("#sosTarTypeId option:selected").text() && "Behavioral" != $("#sosTarTypeId option:selected").text()) {
			$('#sosTarTypeElement_custom').addClass("errorElement errortip").attr("title", "Elements is mandatory." + '<BR><b>Help:</b> ' + "Please select valid data in the field.");
			renderErrorQtip("#lineItemTargeting");
			$('#sosTarTypeElement_custom').focus();
			returnFlag = false;
		}
		
		if ("Zip Codes" == $("#sosTarTypeId option:selected").text()) {
			if ($("#tarTypeElementText").val() == "") {
				$('#tarTypeElementText').addClass("errorElement errortip").attr("title", "Elements is mandatory." + '<BR><b>Help:</b> ' + "Please select valid data in the field.");
				renderErrorQtip("#lineItemTargeting");
				returnFlag = false;
			}// regex for valid values only
			else {
				var regexp = /^[0-9]+([\,\-\][0-9]+)?$/g;
				var result = regexp.test($("#tarTypeElementText").val());
				if (!result) {
					$('#tarTypeElementText').addClass("errorElement errortip").attr("title", "Only numbers and [ ,  - ] are allowed." + '<BR><b>Help:</b> ' + "Please select valid data in the field.");
					renderErrorQtip("#lineItemTargeting");
					returnFlag = false;
				}
			}
			// textbox length should not exceeds 4000
			if($("#tarTypeElementText").val().length > 4000){
				$('#tarTypeElementText').addClass("errorElement errortip").attr("title", "Only 4000 characters are allowed." + '<BR><b>Help:</b> ' + "Please enter valid data in the field.");
				renderErrorQtip("#lineItemTargeting");
				returnFlag = false;
			}
		}
		
		if ("Behavioral" == $("#sosTarTypeId option:selected").text()) {
			if ($.trim($("#tarTypeElementText").val()) == "") {
				$('#tarTypeElementText').addClass("errorElement errortip").attr("title", "Elements is mandatory." + '<BR><b>Help:</b> ' + "Please select valid data in the field.");
				renderErrorQtip("#lineItemTargeting");
				returnFlag = false;
			}// regex for valid values only
			else {
				var regexp = /^([A-Za-z0-9.$@;,\/ _%()-]+)?$/g;
				var result = regexp.test($("#tarTypeElementText").val());
				if (!result) {
					$('#tarTypeElementText').addClass("errorElement errortip").attr("title", "Only alphabets, numbers, spaces and [ . ; , / _ - @ $ % ( )] are allowed." + '<BR><b>Help:</b> ' + "Please select valid data in the field.");
					renderErrorQtip("#lineItemTargeting");
					returnFlag = false;
				}
			}
			// textbox length should not exceeds 4000
			if($("#tarTypeElementText").val().length > 4000){
				$('#tarTypeElementText').addClass("errorElement errortip").attr("title", "Only 4000 characters are allowed." + '<BR><b>Help:</b> ' + "Please enter valid data in the field.");
				renderErrorQtip("#lineItemTargeting");
				returnFlag = false;
			}
		}
		
		// Frequency Cap validation
		if ("Frequency Cap" == $("#sosTarTypeId option:selected").text()) {
			var lastRowId = $("#geoTargetsummary", "#lineItemTargeting").find("tr:last").attr("id");
			// Frequency Cap can not be added twice
			if ("Frequency Cap" == $('#' + lastRowId + " td:nth-child(2)").html() && $('#sosTarTypeId').is(':disabled') != true) {
				resetTargetingFields();
				jQuery("#runtimeDialogDiv").model({theme: 'Error', autofade: false, message: resourceBundle['error.adding.frequencyCap.targeting']});
				returnFlag = false;
			}
			else {
				var selectedElements = [];
				$("#sosTarTypeElement :selected").each(function(){
					selectedElements.push($(this).text());
				});
				// only one Frequency Cap can be selected
				if (selectedElements.length > 1) {
					$('#sosTarTypeElement_custom').addClass("errorElement errortip").attr("title", "Multiple elements cannot be selected for the Frequency Cap Targeting." + '<BR><b>Help:</b> ' + "Please select valid data in the field.");
					renderErrorQtip("#lineItemTargeting");
					$('#sosTarTypeElement_custom').focus();
					returnFlag = false;
				}
			}
		}
		// validation for Reservation Product type
		if (returnFlag && $("#lineItemType_r", "#lineItemTargeting").is(':checked')) {
			returnFlag = validateTargetsForReservation();
		}
		// validation for Email Product type
		if (returnFlag && $("#lineItemType_e", "#lineItemTargeting").is(':checked')) {
			returnFlag = validateTargetsForEmailReservation();
		}
		
		if(!returnFlag){
			$('#messageHeaderDiv', '#lineItemTargeting').html(resourceBundle['validation.error']).addClass('error');
		}else{
			$('#messageHeaderDiv', '#lineItemTargeting').html('');
		}
		return returnFlag;
}


/**
 * It generates and sets the placement name.
 */
function getPlacementName(){
	var placementName = $("#sosProductId option[value=" + $("#sosProductId", "#lineItemTargeting").val() + "]", "#lineItemTargeting").text() + " in ";
	var salesTargets = $('#sosSalesTargetId option:selected', '#lineItemTargeting').map(function(){
         return this.text;
 	}).get().join(', ');
	if(salesTargets != ""){
		placementName = placementName + salesTargets;
		var targets ="";
		var isBracesClosed=false;
		var rowId = $("#geoTargetsummary" , "#lineItemTargeting").find("tr:last").attr("id");
		 var secondLastrowId = $("#geoTargetsummary", "#lineItemTargeting").find("tr:last").prev().attr("id");
		$("#geoTargetsummary" , "#lineItemTargeting").find("tr").each(function(){
			objId = $(this).attr("id");
	        if (objId != "geoTargetsummaryHeader") {
	        	if(targets == ""){
	        		 if ($('#' + objId + " td:nth-child(2)").text() != 'Frequency Cap') {
	                        targets = "(" + $('#' + objId + " td:nth-child(2)").html();
	                    }
	                    else {
	                        targets = $('#' + objId + " td:nth-child(2)").html();
	                    }
	        	}else{
	        		if ($('#' + objId + " td:nth-child(2)").text() != 'Frequency Cap') {
                        targets = targets + "(" + $('#' + objId + " td:nth-child(2)").html();
                    }
                    else {
                        targets = targets + $('#' + objId + " td:nth-child(2)").html();
                    }
	        	}
	        	
	        	if ($('#not_' +objId).is(':checked')) {
	        		if ($('#' + objId + " td:nth-child(2)").text() != 'Frequency Cap') {
                        targets = targets + " not in " + $('#' + objId + " td:nth-child(4)").html() + ")";
                    }
                    else {
                        targets = targets + " not in " + $('#' + objId + " td:nth-child(4)").html();
                    }
	        	}else{
	        		 if ($('#' + objId + " td:nth-child(2)").text() != 'Frequency Cap') {
	                        targets = targets + " in " + $('#' + objId + " td:nth-child(4)").html() + ")";
	                    }
	                    else {
	                        targets = targets + " in " + $('#' + objId + " td:nth-child(4)").html();
	                    }
	        	}
	        	
	        	if(objId != rowId){
	        		 if ($('#' + rowId + " td:nth-child(2)").text() == 'Frequency Cap' && objId == secondLastrowId) {
						    var indexOr = targets.indexOf("or");
                 if (indexOr != -1 ) {
						isBracesClosed=true;
                     targets = targets + "}   with   ";
                 }else{
						 targets = targets + "   with   ";
					}
                 }
                 else {
                     if ($('#targetAction_' + objId + " option:selected").val() == "or") {
                         var firstChar = targets.charAt(0);
                         if (firstChar != '{') {
                             targets = "{" + targets;
                         }
                         targets = targets + "}   " + $('#targetAction_' + objId + " option:selected").val() + "   {";
                     }
                     else {
                         targets = targets + " " + $('#targetAction_' + objId + " option:selected").val() + " ";
                     }
                 }
             }
         }
		});
		
		if(targets != ""){
			  var indexOr = targets.indexOf("or");
	            if (indexOr != -1 && !isBracesClosed) {
	                targets = targets + "}";
	            }
	            placementName = placementName + " and " + targets;
		}
		$("#placementName", "#lineItemTargeting").val(placementName);
		jQuery("textarea", "#lineItemdetailForm").change();
	}
}

/**
 * Generates and sets the placement name and targeting String
 */
function setPlacementNameAndTargetingString(){
	var placementName = "";
	var targets ="";
	var isBracesClosed=false;
	var salesTargets = $('#sosSalesTargetId option:selected', '#lineItemTargeting').map(function(){
         return this.text;
 	}).get().join(', ');	
	var rowId = $("#geoTargetsummary" , "#lineItemTargeting").find("tr:last").attr("id");
	var secondLastrowId = $("#geoTargetsummary", "#lineItemTargeting").find("tr:last").prev().attr("id");
	$("#geoTargetsummary" , "#lineItemTargeting").find("tr").each(function(){
		objId = $(this).attr("id");
		if (objId != "geoTargetsummaryHeader" ) {
			var targetType = $('#' + objId + " td:nth-child(2)").text();
			var targetEle = $('#' + objId + " td:nth-child(4)").text();
			if(targets == "" && $('#' + objId + " td:nth-child(2)").text() != 'Frequency Cap'){
		   		targets = "(" +$('#' + objId + " td:nth-child(2)").text();
		    } else 
                if (targets != "" && $('#' + objId + " td:nth-child(2)").text() != 'Frequency Cap') {
                    targets = targets + "(" + $('#' + objId + " td:nth-child(2)").text();
                }
                else {
                    targets = targets + $('#' + objId + " td:nth-child(2)").text();
                }
		        	
		    if ($('#not_' +objId).is(':checked')) {
		    	 if ($('#' + objId + " td:nth-child(2)").text() != 'Frequency Cap') {
	                    targets = targets + " not in " + $('#' + objId + " td:nth-child(4)").text() + ")";
	                }
	                else {
	                    targets = targets + " not in " + $('#' + objId + " td:nth-child(4)").text();
	                }
		    }else{
		    	 if ($('#' + objId + " td:nth-child(2)").text() != 'Frequency Cap') {
	                    targets = targets + " in " + $('#' + objId + " td:nth-child(4)").text() + ")";
	                }
	                else {
	                    targets = targets + " in " + $('#' + objId + " td:nth-child(4)").text();
	                }
		    }
		        	
		    if(objId != rowId){
		    	if ($('#' + rowId + " td:nth-child(2)").text() == 'Frequency Cap' && objId == secondLastrowId) {
                    var indexOr = targets.indexOf("<strong>or");
                    if (indexOr != -1 ) {
						isBracesClosed=true;
                        targets = targets + "}   <strong>with</strong>   ";
                    }else{
						 targets = targets + "   <strong>with</strong>   ";
					}
                   
                }
                else {
                    if ($('#targetAction_' + objId + " option:selected").val() == "or") {
                        var firstChar = targets.charAt(0);
                        if (firstChar != '{' && firstChar != '<') {
                            targets = "<p>{" + targets;
                        }
                        targets = targets + "}   </p><strong>" + $('#targetAction_' + objId + " option:selected").val() + "</strong><p>   {";
                    }
                    else {
                    
                        targets = targets + " <strong>" + $('#targetAction_' + objId + " option:selected").val() + "</strong> ";
                    }
                }
            }
        }
	});
	 if (targets != "") {
	        var indexOr = targets.indexOf("<strong>or");
	        if (!isBracesClosed && indexOr!=-1) {
	            targets = targets + "}</p>";
	        }
	    }
	if(salesTargets != ""){
		placementName = $("#sosProductId option[value=" + $("#sosProductId", "#lineItemTargeting").val() + "]", "#lineItemTargeting").text() + " in " + salesTargets;
		if(targets != ""){
			  var targetsForPlacements = targets;
	            var paraIndex = targetsForPlacements.indexOf("<p>");
	            if (paraIndex != -1) {
	                targetsForPlacements = targetsForPlacements.replace(/<p>/g, "");
	                targetsForPlacements = targetsForPlacements.replace(/<\/p>/gi, "");
	            }
	            var strongIndex = targetsForPlacements.indexOf("<strong>");
	            if (strongIndex != -1) {
	            targetsForPlacements = targetsForPlacements.replace(/<strong>/gi, "");
	            targetsForPlacements = targetsForPlacements.replace(/<\/strong>/gi, "");
	            }
	            placementName = placementName + " and " + targetsForPlacements;
	        }
	        $("#placementName", "#lineItemTargeting").val(placementName);
	}
	
	if(targets != ""){
		$("#targetingString", "#lineItemTargeting").html(targets);
	}else{
		$("#targetingString", "#lineItemTargeting").html("");
	}
	jQuery("textarea", "#lineItemTargeting").change();
}

/**
 * Validates if the operation preceeding the Frequency Cap targeting is 'AND' operation or not.
 * @returns {Boolean} Returns true, if the preceeing operation is 'AND' else returns false.
 */
function validateLineItemTargeting(){
	var returnFlag = true;
	var rowCount = $('#geoTargetsummary tr').length;
	if(rowCount > 2){
		var lastRowId = $("#geoTargetsummary" , "#lineItemTargeting").find("tr:last").attr("id");
		if("Frequency Cap" == $("#" + lastRowId + " td:nth-child(2)").html()){
			var prevRowId = $("#geoTargetsummary" , "#lineItemTargeting").find("tr:last").prev('tr').attr("id");
			if($('#targetAction_' +prevRowId).val() == "or"){
				jQuery("#runtimeDialogDiv").model({theme: 'Error', autofade: false, message: resourceBundle['error.operation.preceeding.frequencyCap.targeting']});
				returnFlag = false;
			}
		}
	}
	
	return returnFlag;
}

/**
 * This function calculates the discount on the change of price type, goal seek feature, while setting line item details, while calculating BasePrice
 */
function calculateDiscountPercent() {
	if ($("#priceType", "#lineItemFinancialFieldsContainer").val() != "FLAT RATE") {
		var cpm = $("#rate", "#lineItemFinancialFieldsContainer").val().replace(/,/g,"");
		var rateCardPrice = $("#rateCardPrice", "#lineItemPricingContainer").val().replace(/,/g,"");
		if (isNaN(cpm) || isNaN(rateCardPrice) || (cpm == "")
				|| (rateCardPrice == "") || (rateCardPrice == null)) {
			$("#discountPercentage").val("NA");
		} else {
			var discount = ((rateCardPrice - cpm) * 100) / rateCardPrice;			
			if(discount>=0){
			$("#discountPercentage").val(roundOffNumber(discount,2));
			}else{$("#discountPercentage").val("NA")}
		}
	} else {
		$("#discountPercentage").val("NA");
	}
	
	if($("#priceType","#lineItemdetailForm").val()!="ADDED VALUE"){
		$("#offImpressionLink", "#lineItemFinancialFieldsContainer").hide();
	}
}

function roundOffNumber(value, places) {
	   var multiplier = Math.pow(10, places);
	   return (Math.round(value * multiplier) / multiplier);
}

/**
 * This function does the required changes while toggling between the ProdcutTypes
 * @param type This is the type of LineItem Product i.e. S for Standard, R for Reservation, E for Email
 * @param isLineItemTypeChange A boolean value just to ensure if Product Type has been changed or not
 */
function changeLineItemType(type, isLineItemTypeChange){
	/**
	 * Code to add selection arrow on selected line item type 
	 */
	$(".line-item-type").find("i").removeClass("active-line-item");	
	$("#lineItemType_"+ type.toLowerCase()).siblings("i").addClass("active-line-item");
	if($("#lineItemID", "#lineItemFinancialFieldsContainer").val() == 0 && isLineItemTypeChange && $("#productType","#lineItemFinancialFieldsContainer").val() != type){
		 $('#targetingString', '#lineItemTargeting').html('');
		toggleLineItemType(type);		
	} else if ($("#lineItemID", "#lineItemFinancialFieldsContainer").val() != 0 && !isLineItemTypeChange && $("#productType","#lineItemFinancialFieldsContainer").val() != type){
		toggleLineItemType(type);
		if (type == 'R'){
			$("#lineItemType_s").attr('disabled', 'disabled');
			$("#lineItemType_e").attr('disabled', 'disabled');			
		} else if (type == 'S'){
			$("#lineItemType_r").attr('disabled', 'disabled');
			$("#lineItemType_e").attr('disabled', 'disabled');			
		} else{
			$("#lineItemType_r").attr('disabled', 'disabled');
			$("#lineItemType_s").attr('disabled', 'disabled');
		}
	} else if ($("#lineItemID", "#lineItemFinancialFieldsContainer").val() == 0 && !isLineItemTypeChange) {
		 $('#targetingString', '#lineItemTargeting').html('');
		toggleLineItemType(type);
	}
	if($("#sosProductId", "#lineItemTargeting").val() == ""){
		$("#productCreativeSummary", "#lineItemTargeting").hide();
	}
}

/**
 * Reset additional fields when the Reservable PorductType is choosen
 */
function resetonnClickofReservable(){
    $('#geoTargetsummary').find("tr:gt(0)").remove();//Removes all rows of a table except its header
	$("input[type=text][id!='lineItemSequence']", "#lineItemTargeting").val("");
	$("input[type=checkbox]", "#lineItemTargeting").attr('checked', false);
	$("#rateX", "#lineItemPricingContainer").attr('checked', false).attr('disabled', true);
	$("textarea", "#lineItemTargeting").val("");
	$("#sosProductId", "#lineItemTargeting").select2("val", "");
	$('#sosSalesTargetId > option').remove();
	$('#lineItemTargeting #priceType').val('CPM');
	resetPriceAndSpecType('CPM');
	enableDisableCalendar();
	enableLineItemDefaults();
	resetTargetingFields();
	expirationDate ="";
	if ($("#renewReservation", "#lineItemTargeting").length > 0) {
		$("#renewReservation", "#lineItemTargeting").hide();
	}
	else {
		$("#reservationExpiryDate").datepicker("destroy");
	}
}

/**
 * Retes fields related to pricetype and spectype
 * @param priceType
 */
function resetPriceAndSpecType(priceType){
    $("#specType").multiselect("uncheckAll");
    $("#specType").multiselect("widget").find(":checkbox").each(function(){
        if ($(this).val() == "RICH_MEDIA") {
            this.click();
        }
    });
    
	    $("#startDate", "#lineItemTargeting").val("");
	    var validTo = $("#endDate", "#lineItemTargeting").val();
	    $("#endDate", "#lineItemTargeting").val(validTo);
	    var sov = $("#sov", "#lineItemTargeting").val();
	    if ($("#flight", "#lineItemTargeting").val() != "" &&
	    		($("#startDate", "#lineItemTargeting").val() == "" || $("#endDate", "#lineItemTargeting").val() == "")) {
	        $("#sov", "#lineItemTargeting").val(sov);
	    }

    $("#endDate", "#lineItemTargeting").val("");
    var validFrom = $("#startDate", "#lineItemTargeting").val();
    $("#startDate", "#lineItemTargeting").val(validFrom);
    var sov = $("#sov", "#lineItemTargeting").val();
    if ($("#flight", "#lineItemTargeting").val() != "" && 
			($("#startDate", "#lineItemTargeting").val() == "" || $("#endDate", "#lineItemTargeting").val() == "")) {
        $("#sov", "#lineItemTargeting").val(sov);
    }

	    if (priceType == 'CPM') {
	        $('#lineItemTargeting #totalInvestment').attr('readonly', false);
	        $('#lineItemTargeting #totalInvestment').removeClass('input-textbox-readonly');
	        $('#lineItemTargeting #rate').attr('readonly', false);
	        $('#lineItemTargeting #rate').removeClass('input-textbox-readonly');
	        $('#lineItemTargeting #tr_rate').show();
			$('#lineItemTargeting #calculateBasePrice').removeAttr("disabled");
			
	        $('#lineItemTargeting #calculateBasePrice').removeClass("disabled-btn");
	        $('#lineItemTargeting #pricingCalculationStep').show();
	    } 
}

/**
 * This function is called when the viewCalander button is clicked from the UI and renders the Calander in the newWindow
 */
function viewCalander () {
	 try {
        var myParentWindow = window.opener;
        myParentWindow.viewCalanderWindow(getParamsForCalendar());
    } 
    catch (e) {
		var width = screen.width - 150;
		var height = screen.height - 150;	
	    var win = window.open($("#contextPath").val() + "/reservations/viewCalendar.action?" + getParamsForCalendar(), "ReservationCalendar", "left=50,top=0,width=" + width + ",height=" + height + ",toolbar=0,resizable=1,menubar=0,scrollbars=1,location=no");
		win.focus();
    }
}

/**
 * This fucntion calculates the SOR value when the calculateSOR button is clicked from the UI
 */
function calculateSORValue () {
	var ajaxReq = new AjaxRequest();
	
	ajaxReq.url = $("#contextPath").val() +"/reservations/calculateSOR.action?" + getParamsForCalendar() + "&calculateSOR=true";
	ajaxReq.success = function(result, status, xhr){
		if($("#lineItemType_e", "#lineItemTargeting").is(':checked')){
			$("#emailReservationStatus", "#lineItemTargeting").val(result.objectMap.reservationStatus);
		}		
		$("#sor", "#lineItemTargeting").val(result.objectMap.calculatedSOR);
	};
	ajaxReq.submit();
}

/**
 * Returns the parameters required for the Calander view
 * @returns {String}
 */
function getParamsForCalendar () {
	var proposalId = 0;
	var productId = $("#sosProductId", "#lineItemTargeting").val();
	var salesTargetId = $('#sosSalesTargetId', '#lineItemTargeting').val();
	
	var startDate = jQuery('#startDate', '#lineItemTargeting').val();
	var endDate = jQuery('#endDate', '#lineItemTargeting').val();
	
	if($("#proposalID", "#buildProposalSearchForm").length > 0){
		proposalId = $("#proposalID", "#buildProposalSearchForm").val();
	}
	getDataForTargetingToSave();
	var returnParams = "productId=" + productId + "&salesTargetId=" + salesTargetId + "&startDate=" + startDate + "&endDate=" + endDate + "&proposalId=" + proposalId + "&lineItemId=" + $("#lineItemID").val() + "&lineItemTargetingData=" + $("#lineItemTargetingData").val();
	return returnParams;
}

/**
 * This function determines when to show calendar button and when to not it also resets sor value to blank when flight info or product is changed
 */
function enableDisableCalendar(){
	var productId = $("#sosProductId", "#lineItemTargeting").val();
	var salesTargetId = $('#sosSalesTargetId', '#lineItemTargeting').val();	
	var start = jQuery('#startDate', '#lineItemTargeting').val();
	var end = jQuery('#endDate', '#lineItemTargeting').val();
	if(productId != "" && (salesTargetId != "" && salesTargetId!=null) && start != "" && end != ""){
		$("#calculateSOR","#lineItemTargeting").show();
		$("#viewCalendar","#lineItemTargeting").show();
	}else{
		$("#calculateSOR","#lineItemTargeting").hide();
		$("#viewCalendar","#lineItemTargeting").hide();
		$("#emailReservationStatus", "#lineItemTargeting").val('');
	}
	$("#sosProductId", "#lineItemTargeting").change(function(){
		$("#sor", "#lineItemTargeting").val('');
	});
	//if(start == "" || end == "") {
//		$("#sor", "#lineItemTargeting").val('');
	//}
}

var expirationDate ="";
function fillExpirationDate () {
	if ($("#reserved", "#lineItemTargeting").is(':checked')) {
		var ownerRole = $("#proposalOwnerRole", "#buildProposalParentDiv").val();
		var productOptGrp = $("#sosProductId option:selected").parent("optgroup").attr("label");
		var months = new Array('01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12');
		var now = new Date();
		var mdy1 = $("#startDate" ,"#lineItemTargeting").val().split('/');
		var startDate1 = new Date(mdy1[2], mdy1[0]-1, mdy1[1]);
		var startDate = new Date($("#startDate" ,"#lineItemTargeting").val());
		if (expirationDate == "" || expirationDate == null) {
			now.setDate(now.getDate() + 14);
			var modifiedStartDate = new Date();
			modifiedStartDate.setYear(now.getFullYear()+1);
			if(productOptGrp && (productOptGrp == 'HOME PAGE' || productOptGrp == 'Display Cross Platform') && startDate > modifiedStartDate){
				var date = ((modifiedStartDate.getDate() < 10) ? "0" : "") + modifiedStartDate.getDate();
				$("#reservationExpiryDate", "#lineItemTargeting").val(months[modifiedStartDate.getMonth()] + "/" + date + "/" + modifiedStartDate.getFullYear());
			}
			else if(productOptGrp && (productOptGrp != 'HOME PAGE' && productOptGrp != 'Display Cross Platform') && startDate > now){
				var date = ((now.getDate() < 10) ? "0" : "") + now.getDate();
				$("#reservationExpiryDate", "#lineItemTargeting").val(months[now.getMonth()] + "/" + date + "/" + now.getFullYear());
			}
			else{
				var date = ((startDate.getDate() < 10) ? "0" : "") + startDate.getDate();
				$("#reservationExpiryDate", "#lineItemTargeting").val(months[startDate.getMonth()] + "/" + date + "/" + startDate.getFullYear());
			}
//			
//			
//			
//			if(startDate < now){
////				startDate.setDate(startDate.getDate() - 1);
//				var date = ((startDate.getDate() < 10) ? "0" : "") + startDate.getDate();
//				$("#reservationExpiryDate", "#lineItemTargeting").val(months[startDate.getMonth()] + "/" + date + "/" + startDate.getFullYear());
//			}else{
//				var date = ((now.getDate() < 10) ? "0" : "") + now.getDate();
//				$("#reservationExpiryDate", "#lineItemTargeting").val(months[now.getMonth()] + "/" + date + "/" + now.getFullYear());
//			}
				
		}else{
			$("#reservationExpiryDate", "#lineItemTargeting").val(expirationDate);
		}
	}else{
		$("#reservationExpiryDate","#lineItemTargeting").val('');
	}
	showHideRenewLink();
	/**
	 * Below code will show the calendar for Admin at creation of Resrvation Time
	 */
	if (!$("#renewReservation", "#lineItemTargeting").length > 0 && (expirationDate == "" || expirationDate == null) && $("#reserved", "#lineItemTargeting").is(':checked')) {
		$("#reservationExpiryDate").datepicker("destroy");
		createAdminDatePicker("calendar.gif");
	}
	expirationDate = "";
}

function showHideRenewLink(){
	if ($("#renewReservation", "#lineItemTargeting").length > 0) {
		$("#renewReservation", "#lineItemTargeting").hide();
	}
	else {
		$("#reservationExpiryDate").datepicker("destroy");
	}
	if($("#reservationExpiryDate","#lineItemTargeting").val() != ""){
		var now = new Date();
		now.setHours(0,0,0,0);
		var mdy = $("#reservationExpiryDate","#lineItemTargeting").val().split('/');
		var expiryDate = new Date(mdy[2], mdy[0]-1, mdy[1]);
		var mdy1 = $("#endDate" ,"#lineItemTargeting").val().split('/');
		var endDate = new Date(mdy1[2], mdy1[0]-1, mdy1[1]);
		
		if(parseInt((endDate - now) / (24 * 3600 * 1000)) >= 0){
			if ($("#renewReservation", "#lineItemTargeting").length > 0){
				var productOptGrp = $("#sosProductId option:selected").parent("optgroup").attr("label");
				if($("#proposalOwnerRole", "#buildProposalParentDiv").val() == 'PLR' && (productOptGrp == 'HOME PAGE' || productOptGrp == 'Display Cross Platform')){
					$("#renewReservation", "#lineItemTargeting").hide();
				}else{
					if (parseInt((expiryDate - now) / (24 * 3600 * 1000)) <= 3 && $("#lineItemIdVal").val() != '') {
						$("#renewReservation", "#lineItemTargeting").show();
					}
				}
			}else{
				createAdminDatePicker("renew-btn.png");
			}
		}
	}
}

function renewExpirationDate(){
	var reservation = $("#reservationExpiryDate","#lineItemTargeting").val().split("/");
	var months = new Array('01','02','03','04','05','06','07','08','09','10','11','12');
	var reservationDate = new Date(reservation[2], reservation[0] - 1, reservation[1]);
	var now = new Date();
	now.setHours(0,0,0,0);
	var productOptGrp = $("#sosProductId option:selected").parent("optgroup").attr("label");
	var mdy1 = $("#startDate" ,"#lineItemTargeting").val().split('/');
	var startDate = new Date(mdy1[2], mdy1[0]-1, mdy1[1]);
	if(reservationDate > now){
		reservationDate.setDate(reservationDate.getDate()+14);
		
		if(startDate < reservationDate){
			//startDate.setDate(startDate.getDate() - 1);
			var date = ((startDate.getDate() < 10) ? "0" : "") + startDate.getDate();
			$("#reservationExpiryDate", "#lineItemTargeting").val(months[startDate.getMonth()] + "/" + date + "/" + startDate.getFullYear());
		}else{
			var date = ((reservationDate.getDate()<10) ? "0" : "")+ reservationDate.getDate();
			$("#reservationExpiryDate","#lineItemTargeting").val(months[reservationDate.getMonth()] + "/" + date + "/" + reservationDate.getFullYear());
		}
	}else{
		now.setDate(now.getDate()+14);
		if(startDate < now){
			//startDate.setDate(startDate.getDate() - 1);
			var date = ((startDate.getDate() < 10) ? "0" : "") + startDate.getDate();
			$("#reservationExpiryDate", "#lineItemTargeting").val(months[startDate.getMonth()] + "/" + date + "/" + startDate.getFullYear());
		}else{
			var date = ((now.getDate() < 10) ? "0" : "") + now.getDate();
			$("#reservationExpiryDate", "#lineItemTargeting").val(months[now.getMonth()] + "/" + date + "/" + now.getFullYear());
		}
	}
	$("#reserved", "#lineItemdetailForm").attr('checked', 'checked');
	$("#reserved", "#lineItemdetailForm").removeAttr('disabled');
	$("#reservedChkBoxSpan").removeClass('disabled-checkbox');
	showHideRenewLink();
}

function fillProductByType(type){
	$('option', $("#sosProductId","#lineItemTargeting")).remove();
	var optString = "<option value=''></option>";		
	$.each($('#allProducts optgroup'), function(i, optgroup){
		optString = optString + '<optgroup  label="' + $(optgroup).attr('label') +'">';		
		$.each($(optgroup).find(' option[data-type=\"'+type +'"]') , function(i, option){			
			optString = optString + '<option value="' + $(option).attr('value') + '" is-viewable="'+$(option).attr('is-viewable')+'">' + $(option).text() + '</option>';
		});
		optString = optString + '</optgroup>';
	});
	$("#sosProductId","#lineItemTargeting").append(optString);
}

function fillTargetCriteriaByType(type){
	$('option', $("#sosTarTypeId","#lineItemTargeting")).remove();
	var optString = "<option value=''>-- Select --</option>";	
	$.each($('#targetTypeCriteria option[data-type=\"'+type +'"]'), function(i, option){
		optString = optString + '<option value="' + $(option).attr('value') + '" >' + $(option).text() + '</option>';
	});
	$("#sosTarTypeId","#lineItemTargeting").append(optString);
}

/**
 * only one Among DMA, Countries and Regions
 */
function validateTargetsForReservation(){
	var returnFlag = true;
	if ("Frequency Cap" != $("#sosTarTypeId option:selected").text()) {
		 $("#geoTargetsummary tr").each(function(){
			objId = $(this).attr("id");
	        if (objId != "geoTargetsummaryHeader") {			
	            var targetType = $('#' + objId + " td:nth-child(2)").html();
	            if (("Adx DMA" == targetType || "Countries" == targetType || "Target Region" == targetType) && $('#sosTarTypeId').is(':disabled') != true) {		            
					if (targetType == $("#sosTarTypeId option:selected").text()) {
						jQuery("#runtimeDialogDiv").model({theme: 'Error', autofade: false, message: targetType + ' targeting cannot be added twice.'});
					}
					else {
						jQuery("#runtimeDialogDiv").model({theme: 'Error', autofade: false, message: resourceBundle['error.adding.targeting.reservable']});
					}
					resetTargetingFields();
					returnFlag = false;
	            }
			}	 
	    });
	}
	return returnFlag;
}

/**
 * only one Among DMA, Countries and Regions for Email Reservation
 */
function validateTargetsForEmailReservation(){
	var returnFlag = true;
	if ("Adx DMA" == $("#sosTarTypeId option:selected").text() || "Countries" == $("#sosTarTypeId option:selected").text() || "Target Region" == $("#sosTarTypeId option:selected").text()) {
		 $("#geoTargetsummary tr").each(function(){
			objId = $(this).attr("id");
	        if (objId != "geoTargetsummaryHeader") {			
	            var targetType = $('#' + objId + " td:nth-child(2)").html();
	            if (("Adx DMA" == targetType || "Countries" == targetType || "Target Region" == targetType) && $('#sosTarTypeId').is(':disabled') != true) {		            
					if (targetType == $("#sosTarTypeId option:selected").text()) {
						jQuery("#runtimeDialogDiv").model({theme: 'Error', autofade: false, message: targetType + ' targeting cannot be added twice.'});
					}
					else {
						jQuery("#runtimeDialogDiv").model({theme: 'Error', autofade: false, message: resourceBundle['error.adding.targeting.reservable']});
					}
					resetTargetingFields();
					returnFlag = false;
	            }
			}	 
	    });
	}
	return returnFlag;
}

function createAdminDatePicker(imgName){
	$( "#reservationExpiryDate" ).datepicker({
		showOn: "both",
		buttonImage: "./images/" + imgName,
		buttonText: "Select Date",
		buttonImageOnly: true,
		onSelect: function(selectedDate, inst){
			clearCSSErrors("#lineItemTargeting");
			var mdy = selectedDate.split('/');
			var expiryDate = new Date(mdy[2], mdy[0] - 1, mdy[1]);
			var now = new Date();
			now.setHours(0,0,0,0);
			/*if (expiryDate < now) {
				$(this).val(inst.lastVal);				
				$('#reservationExpiryDate', "#lineItemTargeting").addClass("errorElement errortip").attr("title", "Expiration Date can't be less than current date." + '<BR><b>Help:</b> ' + "Please provide valid date in the field.");
				renderErrorQtip("#lineItemTargeting");
			}else{*/
				$("#reserved", "#lineItemdetailForm").attr('checked', 'checked');
				$("#reserved", "#lineItemdetailForm").removeAttr('disabled');
	    		$("#reservedChkBoxSpan").removeClass('disabled-checkbox');
			//}
		}
	});

	/*if ($("#reservationExpiryDate", "#lineItemTargeting").val() != "") {
		var now = new Date();
		now.setHours(0,0,0,0);
		var mdy = $("#reservationExpiryDate", "#lineItemTargeting").val().split('/');
		var expiryDate = new Date(mdy[2], mdy[0] - 1, mdy[1]);
		if (expiryDate < now) {
			$("#reservationExpiryDate", "#lineItemTargeting").datepicker("option", "minDate", expiryDate);
		}else {
			$("#reservationExpiryDate", "#lineItemTargeting").datepicker("option", "minDate", new Date());
		}
	}
	else {
		$("#reservationExpiryDate", "#lineItemTargeting").datepicker("option", "minDate", new Date());
	}
	var CurrentDate = new Date();
	CurrentDate.setMonth(CurrentDate.getMonth() + 12);
	$("#reservationExpiryDate", "#lineItemTargeting").datepicker("option", "maxDate", CurrentDate);
	
	var mdy1 = $("#startDate" ,"#lineItemTargeting").val().split('/');
	var startDate = new Date(mdy1[2], mdy1[0]-1, mdy1[1]);
	startDate.setDate(startDate.getDate() - 1);
	$("#reservationExpiryDate", "#lineItemTargeting").datepicker("option", "maxDate", startDate);*/
}

/**
 * Returns data in the qtip when the Line Item Defaults link is clicked from the LineItem screen
 */
function getProductSalesTargetDefaultValue() {
	$("#productSalesTargetValue", "#lineItemTargeting").qtip(
			{
				content : {
					// Set the text to an image HTML string with the correct src URL to the loading image you want to use
					text : ' ',
					title : {
						text : 'Line Item Defaults',
						button : 'Close'
					}
				},
				position : {
					at : 'left center', // Position the tool tip above the link
					my : 'right center',
					viewport : $(window), // Keep the tool tip on-screen at all times
					effect : false
					// Disable positioning animation
				},
				show : {
					event : 'click',
					ready : true
					// Only show one tool tip at a time
				},
				hide : 'unfocus',
				style : {
					classes : 'ui-tooltip-wiki ui-tooltip-tipped ui-tooltip-shadow',
					width:'400'
				}
			}
	);
	var url = $("#contextPath").val() + "/manageProduct/getProductSalesTargetDefaultValue.action";
	var pkgDataVal = 'productId=' + $('#lineItemTargeting #sosProductId').val() +'&salestargetId=' + $('#sosSalesTargetId', '#lineItemTargeting').val();
	$("#productSalesTargetValue", "#lineItemTargeting").qtip("option", "content.ajax.data", pkgDataVal);
	$("#productSalesTargetValue", "#lineItemTargeting").qtip("option","content.ajax.url", url);

}

/**
 * Enables Line Item Defaults link when the ProductType is Reservable and Email
 */
function enableLineItemDefaults(){
	 if ($("#lineItemType_r", "#lineItemTargeting").is(':checked') || $("#lineItemType_e", "#lineItemTargeting").is(':checked')) {
		var productId = $("#sosProductId", "#lineItemTargeting").val();
		var salesTargetId = $('#sosSalesTargetId', '#lineItemTargeting').val();
		if(productId != "" && (salesTargetId != "" && salesTargetId != null)){
			$("#productSalesTargetValue","#lineItemTargeting").show();
			}else{
				$("#productSalesTargetValue","#lineItemTargeting").hide();
		}
	 }
}

/**
 * This function takes ProductType as an argument and accordingly enable/disable, hide/show fields on the LineItem screen  
 * @param type LineItem's ProductType i.e. S - Standard, R - Reservable, E - Email
 */
function toggleLineItemType(type){
	resetonnClickofReservable();
	if(type == 'R'){
		$("#sosSalesTargetId","#lineItemfieldsContainer").multiselect().multiselectfilter("destroy");
		$("#sosSalesTargetId","#lineItemfieldsContainer").multiselect("destroy");
		$("#sosSalesTargetId","#lineItemfieldsContainer").removeAttr("multiple");
		$("#sosSalesTargetId","#lineItemfieldsContainer").removeAttr("size");
		$("#sosSalesTargetId_custom","#lineItemfieldsContainer").removeClass("multi-select-box");
		$("#sosSalesTargetId", "#lineItemfieldsContainer").select2();
		$('#geoTargetsummary tbody tr th:eq(0), #geoTargetsummary tbody tr th:eq(5), #geoTargetsummary tbody tr th:eq(6)').hide();
		$('#lineItemCalendarContainer','#lineItemTargeting').show();
		$("#productType","#lineItemFinancialFieldsContainer").val('R');
		$("#hideOnReservation","#lineItemFinancialFieldsContainer").hide();
		$("#sovtooltip","#lineItemFinancialFieldsContainer").hide(); 
		$("#sov","#lineItemFinancialFieldsContainer").hide();
		$("#flightStartDate","#lineItemfieldsContainer").text("Start Date:");
		$("#flightEndDate","#lineItemfieldsContainer").show();
		$("#flightEndDateLabel","#lineItemfieldsContainer").show();
		$("#flightSendDate","#lineItemfieldsContainer").hide();
		$("#sorLabel","#lineItemCalendarContainer").text("SOR:");
		$("#calculateSOR","#lineItemCalendarContainer").attr("value","Calculate SOR");
		$("#ImpressionsCountLabel","#lineItemFinancialFieldsContainer").text("Offered Impressions:");
		$("#availsDtls","#lineItemFinancialFieldsContainer").show();
		$("#emailReservationStatus","#lineItemCalendarContainer").hide();
		$("#sor","#lineItemCalendarContainer").show();
		$("#sorMandatory","#lineItemCalendarContainer").show();
		fillProductByType('R');
		fillTargetCriteriaByType('R');
		$("#reserved", "#lineItemdetailForm").removeAttr('disabled');
		$("#reservedChkBoxSpan").removeClass('disabled-checkbox');
		$("#lineItemType_r", "#lineItemTargeting").attr('checked','checked');
	}else if(type == 'S') {
		$("#sosSalesTargetId","#lineItemfieldsContainer").select2("destroy");
		$("#sosSalesTargetId","#lineItemfieldsContainer").attr("multiple" , "multiple");
		$("#sosSalesTargetId","#lineItemfieldsContainer").attr("size","5");
		$("#sosSalesTargetId_custom","#lineItemfieldsContainer").addClass("multi-select-box");
		$('#geoTargetsummary tbody tr th:eq(0), #geoTargetsummary tbody tr th:eq(5), #geoTargetsummary tbody tr th:eq(6)').show();
		$('#lineItemCalendarContainer','#lineItemTargeting').hide();
		$("#productType","#lineItemFinancialFieldsContainer").val('S');
		$("#hideOnReservation","#lineItemFinancialFieldsContainer").show();
		$("#sovtooltip","#lineItemFinancialFieldsContainer").show(); 
		$("#sov","#lineItemFinancialFieldsContainer").show();
		$("#flightStartDate","#lineItemfieldsContainer").text("Start Date:");
		$("#flightEndDate","#lineItemfieldsContainer").show();
		$("#flightEndDateLabel","#lineItemfieldsContainer").show();
		$("#flightSendDate","#lineItemfieldsContainer").hide();
		$("#ImpressionsCountLabel","#lineItemFinancialFieldsContainer").text("Offered Impressions:");
		$("#availsDtls","#lineItemFinancialFieldsContainer").show();
		fillProductByType('S');
		fillTargetCriteriaByType('S');
		setMultiSelectSalesTargetId();
		$("#lineItemType_s", "#lineItemTargeting").attr('checked','checked');
	}else{
		$("#sosSalesTargetId","#lineItemfieldsContainer").multiselect().multiselectfilter("destroy");
		$("#sosSalesTargetId","#lineItemfieldsContainer").multiselect("destroy");
		$("#sosSalesTargetId","#lineItemfieldsContainer").removeAttr("multiple");
		$("#sosSalesTargetId","#lineItemfieldsContainer").removeAttr("size");
		$("#sosSalesTargetId_custom","#lineItemfieldsContainer").removeClass("multi-select-box");
		$("#sosSalesTargetId", "#lineItemfieldsContainer").select2();
		$('#geoTargetsummary tbody tr th:eq(0), #geoTargetsummary tbody tr th:eq(5), #geoTargetsummary tbody tr th:eq(6)').hide();
		$('#lineItemCalendarContainer','#lineItemTargeting').show();
		$("#productType","#lineItemFinancialFieldsContainer").val('E');
		$("#hideOnReservation","#lineItemFinancialFieldsContainer").hide();
		$("#sovtooltip","#lineItemFinancialFieldsContainer").hide(); 
		$("#sov","#lineItemFinancialFieldsContainer").hide();
		$("#flightStartDate","#lineItemfieldsContainer").text("Send Date:");
		$("#flightEndDate","#lineItemfieldsContainer").hide();
		$("#flightEndDateLabel","#lineItemfieldsContainer").hide();
		$("#flightSendDate","#lineItemfieldsContainer").show();
		$("#sorLabel","#lineItemCalendarContainer").text("Availability:");
		$("#calculateSOR","#lineItemCalendarContainer").attr("value","Check Availability");		
		$('#priceType','#lineItemFinancialFieldsContainer').val('FLAT RATE');
		onClickPriceTypeRadio('FLAT RATE');
		$("#specType", "#lineItemFinancialFieldsContainer").val('STANDARD');
		$("#specType", "#lineItemFinancialFieldsContainer").multiselect("refresh");
		$("#ImpressionsCountLabel","#lineItemFinancialFieldsContainer").text("Subscribers:");
		$("#availsDtls","#lineItemFinancialFieldsContainer").hide();
		$("#emailReservationStatus","#lineItemCalendarContainer").show();
		$("#sor","#lineItemCalendarContainer").hide();
		$("#sorMandatory","#lineItemCalendarContainer").hide();
		fillProductByType('E');
		fillTargetCriteriaByType('E');		
		$("#lineItemType_e", "#lineItemTargeting").attr('checked','checked');
	}
	resetStartEndDate();
}

/**
 * This function hide/show RateX field for Proposal LineItem/ Package LineItem
 */
function setRateXVal(){
	if ($("#proposalID", "#buildProposalSearchForm").val() > 0) {
		$("#rateX", "#lineItemPricingContainer").attr('checked', false).attr('disabled', true);
		$("#rateX", "#lineItemPricingContainer").hide();
		$("#ratexLable", "#lineItemPricingContainer").hide(); 
		$("#ratex_help", "#lineItemPricingContainer").hide();
	}
    else {
		$("#ratexLable", "#lineItemPricingContainer").show();
		$("#rateX", "#lineItemPricingContainer").show();
		$("#ratex_help", "#lineItemPricingContainer").show();
		if ($("#rateCardPrice" , "#lineItemPricingContainer").val() == "" || $("#rateCardPrice", "#lineItemPricingContainer").val() == "NA" || $("#rateCardPrice", "#lineItemPricingContainer").val() == "Not Defined") {
            $("#rateX", "#lineItemPricingContainer").attr('checked', false).attr('disabled', true);
        } else {
            $("#rateX", "#lineItemPricingContainer").removeAttr('disabled').attr('checked', true);
        }
    }
}

function reqAdminForReservation(){
	var ajaxReq = new AjaxRequest();
	ajaxReq.url = $("#contextPath").val() +"/proposalWorkflow/sendReqMailForHomePageResrvtn.action?lineItemId=" + $('#lineItemID').val();
	ajaxReq.success = function(result, status, xhr){	
		jQuery("#runtimeDialogDiv").model({theme: 'Success', message: resourceBundle['message.generic.reservation.request.sent.Successfully']});
	};
	ajaxReq.submit();
}
