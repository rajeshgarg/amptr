/**
 * @param formName
 * @param masterGridIdentifier
 * @return
 */
function resetDetailForm(formName,masterGridIdentifier) {
	var gsr = jQuery(masterGridIdentifier).jqGrid('getGridParam','selrow');
	if(gsr) {
		jQuery(masterGridIdentifier).jqGrid('GridToForm',gsr,formName);
	} else {
		alert("Please select Row");
	}
}
	

/**
* @param formIdentifier : elemnt id of form 
* @param jsonObject : server serponse in json.
* @return
*/
function renderError(formIdentifier, jsonObject) {
	jQuery('#messageHeaderDiv', formIdentifier).html(resourceBundle['validation.error']).addClass('error');
	var errorArray = jsonObject.errorList;
	jQuery.each(errorArray, function(i, error) {
		jQuery("#" + error.field, formIdentifier).addClass("errorElement errortip")
			.attr("title", error.errorMessageForUI + '<BR><b>Help:</b> ' + error.errorHelpMessageForUI);
	});
	renderErrorQtip(formIdentifier);
}

/** 
 * This method is rendering Error for Inline Validations
 * 
 * @param formIdentifier : element id of form
 * @param jsonObject : server response in JSON.
 * @return
 */
function renderErrorForInlineEditing(formIdentifier, jsonObject) {
    jQuery('#messageHeaderDiv', '#' + formIdentifier).html(resourceBundle['validation.error']).addClass('error');
    var errorArray = jsonObject.errorList;
    
    jQuery.each(errorArray, function(i, error){
        jQuery("#" + error.uiFieldIdentifier, "#" + formIdentifier).addClass("errortip errorElement")
			.attr("title", error.errorMessageForUI + '<BR><b>Help:</b> ' + error.errorHelpMessageForUI);
    });
    renderErrorQtip(formIdentifier);
} 

/**
 * This message will render validation error on UI.
 * Add a general message
 * highlight error and provide tool tip data.
 */
function renderValidationErrorFromXMLResponse(formIdentifier, xmlObject){
    var statusMessage = jQuery(xmlObject).find('status').text();
    //adding message to error header div 
    jQuery('#messageHeaderDiv', formIdentifier).html(statusMessage).addClass('error');
    
    jQuery(xmlObject).find("errorList").each(function(){
        jQuery("#" + jQuery(this).find('uiFieldIdentifier').text(), formIdentifier).addClass("errortip errorElement")
				.attr("title", jQuery(this).find('errorMessageForUI').text() + '<BR><b>Help:</b> ' + jQuery(this).find('errorHelpMessageForUI').text());
    });
    renderErrorQtip(formIdentifier);
}

/**
 * Create HTML for attachment and convert it to AjaxForm.
 * 
 * @param {Object} rootDiv
 * @param {Object} type
 */
function initDocUploadHTML(rootDiv, type){
	jQuery("#"+rootDiv).html(
		"<div>" +
			"<table id=\"" +rootDiv +"Table\" class='childGrid'></table>" +
			"<div id=\"" +rootDiv +"Pager\"></div>" +
		"</div>" +
		"<div style=\"display: none\" id=\"" +rootDiv +"Dialog\" title=\""+resourceBundle['title.generic.uploadFile']+"\">" +
		
			"<form id=\"" +rootDiv +"Form\" enctype=\"multipart/form-data\" action=\"../document/savedocument.action\" method=\"POST\">" +
				"<div id='messageHeaderDiv'> </div>"+
				"<table width=\"100%\" align=\"center\" class=\"table-layout-fixed\">" +
					"<tr>" +
						"<td width=\"7%\" class=\"align-right\"><span class=\"mandatory\">*&nbsp;</span></td>" +
						"<td width=\"13%\"><label class=\"label-bold\">"+resourceBundle['label.generic.document']+":<\label></td>" +
						"<td width=\"80%\"><input name=\"file\" id=\"attachement\" type=\"file\" size=\"30\"/>&nbsp;" +
						"<img src='"+$("#contextPath").val()+"/images/info-icon.png' class='tip-tool' title='"+resourceBundle['message.tiptext.supportedDocumentForUpload']+"' /></td>" +
					"</tr>" +
					"<tr>" +
						"<td>&nbsp;</td>" +
						"<td><label class=\"label-bold\">"+resourceBundle['label.generic.purpose']+":<label></td>" +
						"<td><input name=\"description\" id=\"description\" type=\"text\" style=\"width:90%\" maxlength=\"500\"/></td>" +
					"</tr>" +
					"<tr><td colspan=\"3\"><div class=\"spacer7\"></div></td></tr>" +
					"<tr>" +
						"<td colspan=\"3\" align=\"center\">" +
							"<div id=\"" +rootDiv +"Message\" align=\"left\"></div>" +
							"<input type=\"hidden\" name=\"id\" id=\"id\" />" +
							"<input type=\"hidden\" name=\"documentFor\" id=\"documentFor\" value=\""+ type +"\" />" +
							"<input type=\"hidden\" name=\"componentId\" id=\"componentId\"/>" +
							"<input type=\"hidden\" name=\"fileName\" id=\"fileName\"/>" +
						"</td>" +
					"</tr>" +
				"</table>" +
			"</form>" +
		"</div>"
	);
	jQuery("#" +rootDiv +"Form").ajaxForm();
}

/**
 * Initialised attachment JQgrid options
 * 
 * @param {Object} rootDiv
 */
function initAttachementGridOpts(rootDiv) {
	var attachementGridOpts = new JqGridBaseOptions();
	attachementGridOpts.colNames = ['Id', 'Document Name',' Purpose', 'Document Type', 'Size (KB)', 'Document Version', 'Modified By', 'Modified On', 'Action'];
	attachementGridOpts.colModel = [
      	{name:'id',index:'id', sortable:true, hidden:true, key:true, hidedlg:true},
		{name:'fileName',index:'fileName', sortable:true, width:185},
		{name:'description',index:'description', sortable:false, width:185},
		{name:'fileType',index:'fileType',sortable:true},
		{name:'fileSize',index:'fileSize', sortable:true, width:75, align:"right"},
		{name:'downloadString',index:'downloadString', sortable:false, hidedlg:true},
		{name:'modifiedBy',index:'modifiedBy', sortable:true},
		{name:'modifiedOn',index:'modifiedOn', sortable:true, width:100},
		{name:'add',index:'add', sortable:false, hidedlg:true, width:110, align:'center'},
	];
	attachementGridOpts.gridComplete = function() {
		setGridAlternateStyle(this);
		var ids = jQuery("#"+rootDiv+"Table").jqGrid('getDataIDs');
		for(var i = 0;i < ids.length; i++){
			var cl = ids[i];						
			be = "<a style=\"cursor:pointer;\" onClick=\"jQuery('#"+rootDiv+"Form #id').val('"+cl+"'); jQuery('#"+rootDiv+"Table').setSelection("+cl+", true);clearCSSErrors('#"+rootDiv+"Form');	jQuery('#"+rootDiv+"Dialog').dialog('open');\">Update Version</a>"; 
			jQuery("#"+rootDiv+"Table").jqGrid('setRowData',ids[i],{add:be});
		}
		var topRowId = $(this).getDataIDs()[0];
		if(topRowId != ''){
			jQuery(this).setSelection(topRowId, true);
		}
		jQuery(this).setGridWidth($("#tabs").width() - (tabPadding * 5), true);
		attachementGridOpts.afterGridCompleteFunction();
	};
	attachementGridOpts.pager = jQuery("#"+rootDiv+"Pager"),
	attachementGridOpts.sortname = 'id';
	return attachementGridOpts;
}

function setGridAlternateStyle(grid){
	$("tr:odd", grid).addClass("odd_Table_Row");
   	$("tr:even", grid).addClass("even_Table_Row");
}

/**
 * This method will clear all validation error and message before any activity.
 * 
 * @param {Object} formIdentifier
 */
function clearCSSErrors(formIdentifier){
	$('.errortip', formIdentifier).qtip("destroy");
	
	jQuery('input', formIdentifier).each(function(){
		jQuery(this).removeClass('errorElement').removeClass('errortip').removeAttr("title");
	});
	jQuery('select', formIdentifier).each(function(){
		jQuery(this).removeClass('errorElement').removeClass('errortip').removeAttr("title");
	});
	jQuery('textarea', formIdentifier).each(function(){
		jQuery(this).removeClass('errorElement').removeClass('errortip').removeAttr("title");
	});
	jQuery('div', formIdentifier).each(function(){
		jQuery(this).removeClass('errorElement').removeClass('errortip').removeAttr("title");
	});
	jQuery('span', formIdentifier).each(function(){
		jQuery(this).removeClass('errorElement').removeClass('errortip').removeAttr("title");
	});
	jQuery('#messageHeaderDiv', formIdentifier).html('');
}

function listBoxTransferData(availableList, selectedList){
	jQuery('#addIt').click(function(){
		jQuery('#'+selectedList).prepend(jQuery('#'+availableList + " option:selected:not(:empty)"));  
	});

	jQuery('#delIt').click(function(){	
		jQuery('#'+availableList).prepend(jQuery('#'+selectedList + " option:selected:not(:empty)"));  
	});
}

/** 
 * It DeSelect the selected row on adding new Row.
 * 
 * @param {Object} gridIdentifier
 * @param {Object} rowId
 */
function resetRowSelectionOnAdding(gridIdentifier, rowId){
	jQuery(gridIdentifier).jqGrid('resetSelection');
}

/**
 * Append JSON data to the select box returned form the server.
 * @param {Object} select
 * @param {Object} actionURL
 */
function loadData(select, actionURL){
	if(jQuery('option', select).length > 1){
		return;
	}
	var ajaxReq = new AjaxRequest();
	ajaxReq.url = actionURL;
	ajaxReq.cache = true;
	ajaxReq.success = function(result, status, xhr){
		sortList(result , select);
	};
	ajaxReq.submit();
}

function imposeMaxLength(Event, Object, MaxLen){
    return (Object.value.length <= MaxLen) || (Event.keyCode == 8 || Event.keyCode == 46 || (Event.keyCode >= 35 && Event.keyCode <= 40));
}

function imposeMaxLengthBlur(Object, MaxLen){
    if (Object.value.length > MaxLen) {
        if (confirm("Entered description exceeds the maximun limit.")) {
            Object.value = Object.value.substring(0, MaxLen);
        }
        else {
            Object.value = "";
        }
        Object.focus();
    }
}

//Methods for Line items
function fillProduct(obj, product){	
	if($(obj).val()!=''){
		dynaLoad($(product), '../managepackage/getAllProductsForSalesTarget.action?salesTargetID='+$(obj).val());
	} else {
		$('option', $(product)).remove();
		$($(product)).append('<option value="" >--Select--</option>');
	}		
	
}

function fillSalesTarget(obj,select,product){
	if($(obj).val()!=''){
		dynaLoad($(select), '../managepackage/getAllSalesTargetForTargetType.action?saleTargetType='+$(obj).val());
	} else {
		$('option', $(select)).remove();
		$($(select)).append('<option value="" >--Select--</option>');
	}
	$('option', $(product)).remove();
	$($(product)).append('<option value="" >--Select--</option>');
}

function fillSalesTargetMulti(obj,select,product){
	if($(obj).val()!=''){
		dynaLoad($(select), '../managepackage/getAllProductsForSalesTarget.action?saleTargetType='+obj);
	} else {
		$('option', $(select)).remove();
		$($(select)).append('<option value="" >--Select--</option>');
	}
	$('option', $(product)).remove();
	$($(product)).append('<option value="" >--Select--</option>');
}

function fillProductOnClick(obj,select){
	var selectedVal = $(obj).val();	
	if($('option', obj).length > 1){
		return;
	} else {		
		dynaLoadSelection(selectedVal,$(obj), '../managepackage/getAllProductsForSalesTarget.action?salesTargetID='+$(select).val());
	}	
}

function fillSalesTargetOnClick(obj,select){
	var selectedVal = $(obj).val();	
	if($('option', obj).length > 1){
		return;
	} else {
		dynaLoadSelection(selectedVal,$(obj), '../managepackage/getAllSalesTargetForTargetType.action?saleTargetType='+$(select).val());
	}	
}


function dynaLoadSelection(selectedVal, select, actionURL){	
	var ajaxReq = new AjaxRequest();
	ajaxReq.url = actionURL;
	ajaxReq.cache = false;
	ajaxReq.success = function(result, status, xhr){
		$('option', select).remove();
		$(select).append('<option value="" >--Select--</option>');
		sortList(result , select);
		$(select).val(selectedVal);
	};
	ajaxReq.submit();
}

function dynaLoad(select, actionURL){	
	var ajaxReq = new AjaxRequest();
	ajaxReq.url = actionURL;
	ajaxReq.cache = false;
	ajaxReq.success = function(result, status, xhr){
		$('option', select).remove();
		$(select).append('<option value="" >--Select--</option>');
		sortList(result , select);
	};
	ajaxReq.submit();
}


function trimto2places(obj){
	var returnval="";
	obj=obj.toString();	
	if(obj.indexOf(".")>=0 && obj.substring(obj.indexOf(".")+1).length > 2){
		returnval = obj.substring(0,obj.indexOf(".")+1);		
		returnval = returnval+obj.substring(obj.indexOf(".")+1,obj.indexOf(".")+3);		
	}else{		
		returnval=obj;
	}	
	return parseFloat(returnval);
}

function renderErrorQtip(parent){
	$('.errortip',parent).qtip({
        style: {
            classes: 'ui-tooltip-red' 
        },
		position:{my: 'bottom center',at: 'top center'}
    });
}	

function roundoffto2places(obj){
	var result = (Math.round(obj * 100) / 100);
	return result;
}

/**
 * Sort data for select box based on display value (Due to IE and chrome issue)
 * @param result
 * @param select
 * @return
 */
function sortList(result, select) {
	var arrVal = sortMapDataByValue(result);
	var optString = "";
	for (var i = 0; i < arrVal.length ; i++) { 
		optString = optString + '<option value="' + arrVal[i][1] + '" >' + 	arrVal[i][0] + '</option>';
	}
	$(select).append(optString);
}

/**
 * Sort map data based on values
 *
 * @param result
 * @return
 */
function sortMapDataByValue(result) {
	var arrVal = new Array() ;
	var index = 0;
	$.each(result, function(i, value){
		arrVal[index] = new Array(2) ;
		arrVal[index][0] = value;
		arrVal[index][1] = i;
	    index = index + 1 ;
    });
	arrVal.sort(function(x, y){
		var firstVar = String(x[0]).toUpperCase();
	    var secondVar = String(y[0]).toUpperCase();
	    if (firstVar > secondVar) {
	    	return 1;
	    }
	    if (firstVar < secondVar) {
	    	return -1;
	    }
	    return 0;
	});
	return arrVal;
}

/**
 *   Function to sort select box elements
 */
function sortSelect(id) {
	var selElem = document.getElementById(id);
    var tmpAry = new Array();
    for (var i = 0; i < selElem.options.length; i++) {
        tmpAry[i] = new Array();
        tmpAry[i][0] = selElem.options[i].text.toLowerCase();
        tmpAry[i][1] = selElem.options[i].value;
        tmpAry[i][2] = selElem.options[i].text;
    }
    tmpAry.sort();
    while (selElem.options.length > 0) {
        selElem.options[0] = null;
    }
    for (var i = 0; i < tmpAry.length; i++) {
        var op = new Option(tmpAry[i][2], tmpAry[i][1]);
        selElem.options[i] = op;
    }		
    return;
}

/**
 *  To move elements from left to right and from right to left in select box options
 */
function moveSelectBoxOptions(fromID, toID) {
	$('#' + fromID).prepend($('#' + toID + ' option:selected'));	
	sortSelect(fromID);
}

function moveAllSelectBoxOptions(fromID, toID) {
	$('#' + fromID).prepend($('#' + toID + ' option'));	
	sortSelect(fromID);
}

/**
 * Format the number string according to Locale.US
 * '0,000.00' - (123,456.78) show comma and digits
 * 
 * @param {Object} nStr
 */
function formatNumber(nStr){
    nStr += '';
    x = nStr.split('.');
    x1 = x[0];
    x2 = x.length > 1 ? '.' + x[1] : '';
    var rgx = /(\d+)(\d{3})/;
    while (rgx.test(x1)) {
        x1 = x1.replace(rgx, '$1' + ',' + '$2');
    }
    return x1 + x2;
}


function formatDecimal(nStr) {
	var formattedNumber = formatNumber(nStr);
	if(formattedNumber.length == 0) {
		formattedNumber = '0';
	}
	var x = formattedNumber.split('.');
	if(x.length == 1) {
		formattedNumber = formattedNumber+".00";
	}else {
		var decimalValue = x[1];
		if(parseFloat(decimalValue) == 0 || decimalValue.length == 0)
		{
			decimalValue = '00';
		} else if(decimalValue.length == 1) {
			decimalValue = decimalValue + '0';
		}
		formattedNumber =  x[0]+ '.' + decimalValue;
	}
	return formattedNumber;
}

function setPaddingDecimalValues(obj) {
	var nStr = $(obj).val();	
	var paddedNumber = formatDecimal(nStr);
	$(obj).val(paddedNumber)
}

/**
 * Function to show loader
 */
function showLoader() {
	$.blockUI( $('#loader') );
}

/**
 * Function to hide loader
 */
function hideLoader() {
	$.unblockUI();
}

/**
 * Initialised search panel on JQGrid 
 * 
 * @param {Object} gridId
 * @param {Object} searchPanelId
 * @param {Object} parentContainer
 */
function initGridSearchOptions(gridId, searchPanelId, parentContainer) {
	$("#gview_" + gridId + " > div > a").remove();
	var searchDiv = $("#" + searchPanelId, "#" + parentContainer);
	$("div:first", "#gview_" + gridId).prepend($(searchDiv).show());
}

/**
 * Enable auto search feature on given text box
 * @param textBox
 * @param searchFunction
 */
function enableAutoSearch(textBox, searchFunction){
	$(textBox).bind("keyup", function(e){
		if (e.keyCode != 13) {
			return false;
		}
		if (jQuery.isFunction(searchFunction)) {
			searchFunction.call();
		}
	});
}

/**
 * Function to show help content
 */
function loadHelpContent(elementId) {
	var element = $("#" + elementId);
	console.log(element);
	$(element).qtip({
		content: {
			// Set the text to an image HTML string with the correct src URL to the loading image you want to use
			text: '<img class="throbber" src="./images/ajax-loader.gif" alt="Loading..." />',
			ajax: {
				url: "./jsp/help/" + $(element).attr('rel') // Use the rel attribute of each element for the url to load
			},
			title: {
				 // Give the tooltip a title using elements text
				text: '<div style="height:20px;"><span style="float:left;" class="pricing-help-logo"></span><span style="padding:3px 0 0 3px; float:left;">' + $(element).attr('title') + '</span></div>',
				button: true
			}
		},
		position: {
			at: 'left center', // Position the tooltip above the link
			my: 'right center',
			viewport: $(window), // Keep the tooltip on-screen at all times
			effect: false // Disable positioning animation
		},
		show: {
			event: 'click',
			solo: true // Only show one tooltip at a time
		},
		hide: 'unfocus',
		style: {
			classes: 'ui-tooltip-wiki ui-tooltip-light ui-tooltip-shadow'
		}
	}).click(function(event) {
		// Make sure it doesn't follow the link when we click it
		event.preventDefault(); 
	});	
}
