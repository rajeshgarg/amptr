/**
 * Base class for JQGrid JSON object reader
 */
function JsonReader () {
	this.root = 'tableGrid.gridModel';
    this.page = 'tableGrid.page';
    this.total = 'tableGrid.total';
    this.records = 'tableGrid.records';
	this.repeatitems = false;
};

/**
 * Base class for all JQGrid 
 */
function JqGridBaseOptions () {
	this.datatype = 'json';
	this.contentType = 'application/json; charset=utf-8';
	this.height = 220;
	this.rowNum = 10;
	this.scrollrows = true;
	this.autowidth = true;
	this.emptyrecords = resourceBundle['label.generic.emptymessage'];
	this.forceFit = false;
	this.closeAfterAdd = true;
	this.mtype = 'POST';
	this.rowList = [10,15,20];
	this.scrollOffset = 0;
	this.viewrecords = true;
	this.altRows = true;
	this.loadui = 'disable';
	this.sortorder = 'desc';
	this.altclass = 'even_Table_Row';
	this.jsonReader = new JsonReader();
	this.gridComplete = function () {
		var topRowId = $(this).getDataIDs()[0];
		if(topRowId != ''){
			jQuery(this).setSelection(topRowId, true);
		}
		if (jQuery(this).hasClass("master")){
			jQuery(this).setGridWidth($("#tabs").width() - (tabPadding * 2), true);
		} else if(jQuery(this).hasClass("childGrid")){
			jQuery(this).setGridWidth($("#tabs").width() - (tabPadding * 4), true);
		} else if(jQuery(this).hasClass("secondChildGrid")){
			jQuery(this).setGridWidth($("#tabs").width() - (tabPadding * 7), true);
		} else if(jQuery(this).hasClass("childGridForBuildProposalLineItem")){
			jQuery(this).setGridWidth($("#tabs").width() - (tabPadding * 5), true);
		}
		this.p.afterGridCompleteFunction();
	};
	this.afterGridCompleteFunction = function (){
		/*
		 * Optional: In case of implementation, 
		 * user will responsible for calling in as last statement of gridComplete function of JQgrid.
		 */
	};
	this.handleDeleteError = handelIntegrationExceptionOnServer;
};

/**
 * Base Class for document upload dialog
 */
function ModalDialog () {
	this.autoOpen = false;
	this.resizable = false;
	this.height = 207;
	this.draggable = true;
	this.width = 690;
	this.modal = true;
	this.buttons = {
		Save: function(){
			/*
			 * Need to implement
			 */
		},
		Close: function(){
			/*
			 * Need to implement
			 */
		}
	};
}

/**
 * This template/Class should be used as a storage for all form related
 * identifier.
 */
function FormManager(){
	/**
	 * Public variable Declaration must be overridden
	 */ 
	this.formName = 'dummyForm';
	this.gridName = 'dummyForm';
	
	/**
	 * Public Method Declaration..........
	 */ 
	this.gridIdentifier = function(){
		return '#' + this.gridName;
	};
	this.formIdentifier = function (){
		return 	'#' + this.formName;
	};	
};

/**
 * Base Class for all AJAX form submit options.
 */
function JQueryAjaxForm(FormManager){
	this.resetForm	= false;
	this.FormManager  = FormManager;
	 
	/* possible option are json and xml */
	this.dataType = 'json';
	this.beforeSubmit = function(){
		clearCSSErrors(FormManager.formIdentifier());
	};
	/**
	 * Callback method once a server send Response Code 200 i.e. AJAX
	 * request complete without any error.
	 */
 	this.success = function(responseText, statusText, XMLHTTPRequest, $form ){
		jQuery.logThis(['XMLHTTPRequest', XMLHTTPRequest]);
		jQuery.logThis(['Form submitted ',$form]);
		
		/* For json response handling */
		if(this.dataType == 'json'){
			var jsonResponse= responseText;
			jQuery.logThis(['jsonResponse ',jsonResponse]);
			if(jsonResponse.validationFailed){
				renderError(FormManager.formIdentifier(), jsonResponse);
				this.doCustomProcessingAfterValidationFailedJson(jsonResponse, XMLHTTPRequest);
			} else {
				clearCSSErrors(FormManager.formIdentifier());
				this.doCustomProcessingAfterFormSucsesJson(jsonResponse ,XMLHTTPRequest);
			}
		}
		
		/* For XML response handling */
		if(this.dataType == 'xml'){
			jQuery.logThis(["Is Validation Failed "+($(XMLHTTPRequest.responseXML).find('validationFailed').text())]);
			if($(XMLHTTPRequest.responseXML).find('validationFailed').text()== "true"){
				jQuery.logThis(["XMLresponse ", XMLHTTPRequest.responseXML]);
				renderValidationErrorFromXMLResponse(FormManager.formIdentifier(), XMLHTTPRequest.responseXML);
				this.doCustomProcessingAfterValidationFailedXML(XMLHTTPRequest.responseXML, XMLHTTPRequest);
			} else {	
				this.doCustomProcessingAfterFormSucsessXML(XMLHTTPRequest.responseXML, XMLHTTPRequest);
			}
		}
		if(this.dataType == 'html'){
			this.doCustomProcessingAfterFormSucsessHTML(responseText, statusText, XMLHTTPRequest);
		}
	}; 
	/**
	 * Callback method in case server send HTTPRESponse other then 200.
	 * 
	 */
 	this.error = function(XMLHTTPRequest, textStatus, errorThrown){
		jQuery.logThis(['errorThrown',errorThrown]);
		jQuery.logThis(['textStatus',textStatus]);
		jQuery.logThis(['Server Response ',XMLHTTPRequest.responseText]);
		if (XMLHTTPRequest.getResponseHeader('REQUIRES_AUTH') === '1') {
			
		}
		else {
			handelIntegrationExceptionOnServer(XMLHTTPRequest, this.FormManager.formName);
		}
		this.doCustomProcessingAfterFormError(XMLHTTPRequest);
	};
	
	/**
	 * Implement this method id custom code has to write in case form submit result
	 * in server failed
	 */
	this.doCustomProcessingAfterFormSucsessXML = function( XMLHTTPRequest ){
		jQuery("#runtimeDialogDiv").model({theme: 'Success', message: resourceBundle['message.generic.dataSavedSuccessfully']});
		$(FormManager.gridIdentifier()).trigger("reloadGrid");
	};	
	this.doCustomProcessingAfterFormSucsessHTML = function (responseText, statusText, XMLHTTPRequest) {	};
	
    /**
     * Implement this method if custom code has to write in case form submit
     * result in success and server response was in json.
     */	
	this.doCustomProcessingAfterFormSucsesJson = function(jsonResponse, XMLHTTPRequest ){
		alert("   Request Sucess  ...Temppory alert should be replaced by a generric fade in message ");
	}; 
	 
	/**
	 * Implement this method id custom code has to write in case form submit
	 * result in server failed
	 */ 	
	 this.doCustomProcessingAfterValidationFailedXML = function( XMLHTTPRequest ){ };	
	 
	/**
	 * Implement this method id custom code has to write in case form submit
	 * result in server failed
	 */ 		
	this.doCustomProcessingAfterValidationFailedJson = function(jsonResponse, XMLHTTPRequest ){	};
	
	 /**
	 * Implement this method if custom code has to write in case form submit
	 * result in success and server response was in xml. 
	 */ 
	this.doCustomProcessingAfterFormError = function( XMLHTTPRequest ){	};
	
	/**
	 * Submit Ajax Form
	 */
	this.submit = function (){
		jQuery(this.FormManager.formIdentifier()).ajaxSubmit(this);
	};
}; 

/**
 * Base class for all AJAX request from application 
 */
function AjaxRequest() {
	this.url = "Need to override";
	this.cache = false;
	this.dataType = 'json';
	this.success = function(result, status, xhr){
		/**
		 * Need to override
		 */
	};
	this.error = function(result, status, xhr){
		if (result.getResponseHeader('REQUIRES_AUTH') === '1') {
		
		}
		else {
			handelIntegrationExceptionOnServer(result);
		}
	};
	this.submit = function(){
		jQuery.ajax(this);
	};
}

(function ( $ ) {
	$.fn.autoSearch = function(options) {
		
		var settings = $.extend({
			placeholder: "Search ...",
			allowClear: true,
		    minimumInputLength: 3,
		    ajax: {
		        url: options.url,
		        dataType: "json",
		        data: function(term, page){
		            return { name: term, page_limit: 10 };
		        },
		        results: function(data, page){
		            return { results: data };
		        }
		    },
		    initSelection: function(element, callback){
		        var id = $(element).val();
		        if ((id != null) && id !== "") {
		            $.ajax(options.initUrl + "?id=" + id, {
		                dataType: "json"
		            }).done(function(data){
		                callback(data);
						if(data.name == null || data.name == ""){
							$(element).select2("val", "");
						}
		            });
		        }
		    },
		    formatResult: function(obj){
		        return obj.name;
		    },
		    formatSelection: function(obj){
		        return obj.name;
		    }
		}, options );
		
		this.select2(settings);
	}
})(jQuery);

(function ( $ ) {
	$.fn.sequence = function(options) {
	
		$(this).jqGrid('navButtonAdd', "#" + options.pagerId, {
	        caption: "", id: "arrangeLineItemSequence", title: "", buttonicon: 'ui-icon-transferthick-e-w'
	    });
		
		$("#arrangeLineItemSequence").find("div").attr('title', 'Re-arrange Sequence');
		
		$("#arrangeLineItemSequence").qtip({
			hide: 'unfocus', show: { event: 'click', solo: true },
			content: $("#arrangeSequence"),
			position: {my:  'bottom center', at: 'top center'},
			style: {
	            classes: 'ui-tooltip-tipped ui-tooltip-shadow'
	        }, 
			events: {
		        show: function() {
		           $("#movePlacesTxt", "#arrangeSequence").val('');
				   $("#moveUp", "#arrangeSequence").attr("checked", "checked");
				   $("#movePlacesBtn", "#arrangeSequence").attr("disabled", "disabled").addClass("normal-btn-disabled").removeClass("button-get-avails");
		        }
		    }
	    });
		
		$("input[class=numeric]", "#arrangeSequence").numeric({ negative: false });
		
		$("#movePlacesTxt", "#arrangeSequence").bind("keyup", function(e){
			if ( $ (this).val == "" ){
				$("#movePlacesBtn", "#arrangeSequence").attr("disabled", "disabled").removeClass("button-get-avails").addClass("normal-btn-disabled");
			} else {
				$("#movePlacesBtn", "#arrangeSequence").removeAttr("disabled").removeClass("normal-btn-disabled").addClass("button-get-avails");
			}
		});
		
		$("#movePlacesBtn", "#arrangeSequence").bind("click", function () {
			var movePlaces = $("#moveUp", "#arrangeSequence").is(":checked") ? "-" : "+";
			if (isNaN($("#movePlacesTxt", "#arrangeSequence").val())) {
				$("#movePlacesTxt", "#arrangeSequence").val('');
				$("#movePlacesBtn", "#arrangeSequence").attr("disabled", "disabled").removeClass("button-get-avails").addClass("normal-btn-disabled");
				return;
			}
			movePlaces += $("#movePlacesTxt", "#arrangeSequence").val();
			if($.isFunction(options.doSequence)){
				options.doSequence(movePlaces);
				$("#arrangeLineItemSequence").qtip("hide");
			}
		});
		
		$("#moveToTop", "#arrangeSequence").bind("click", function () {
			if($.isFunction(options.doSequence)){
				options.doSequence("-9999");
				$("#arrangeLineItemSequence").qtip("hide");
			}
		});
		
		$("#moveToBottom", "#arrangeSequence").bind("click", function () {
			if($.isFunction(options.doSequence)){
				options.doSequence("9999");
				$("#arrangeLineItemSequence").qtip("hide");
			}
		});
	}
})(jQuery);

(function ( $ ) {
	
	$.fn.model = function(options) {
		
		var local = {
			dialog: this,
			Success: "/images/success.png",
			Warning: "/images/warning.png",
			Error: "/images/error.png"
		};
		
		var opts = $.extend( {}, $.fn.model.defaults, options );
		
		if (opts.theme != undefined) {
			var htmlTemplate = '';
			
			if (opts.theme == 'Success') {
				htmlTemplate = "<br><br><center><table cellpadding='5' cellspacing='5'><tr><td><img src='"
						+ $("#contextPath").val() + local[opts.theme] + "'></td><td style='vertical-align:middle'>" + opts.message +"</td></tr></table></center>"
			} else {
				htmlTemplate = "<table cellpadding='5' cellspacing='5'><tr><td><img src='" 
						+ $("#contextPath").val() + local[opts.theme] + "'></td><td style='line-height:2; vertical-align: top;'>" + opts.message + "</td></tr></table>";			
			}
			$( local.dialog ).html(htmlTemplate);
		}
		
		$( local.dialog ).dialog({
			modal: true,
			dialogClass: 'alert',
			hide: 'fade', show: 'slide',
			autoOpen: opts.autoOpen,
			title: opts.theme,
			height: opts.height, 
			width: opts.width,
			draggable: opts.draggable, 
			showTitlebar: opts.showTitlebar,
			resizable: opts.resizable,
			buttons: opts.autofade == true ? null : opts.buttons,
			open: function(event, ui) {
				if (opts.autofade) {
					setTimeout(function() {
						$( local.dialog ).dialog( "close" );
					}, 1500);
				}
			}, 
			close: opts.close == undefined ? null : opts.close,
		});
	}
	
    $.fn.model.defaults = {
		message: '', autoOpen: true, autofade: true,
		height: 150, width: 300, draggable: true, showTitlebar: true, resizable: false,
		buttons : {
			OK: function(){
				$( this ).dialog( "close" );
			}
		}
    };
})(jQuery);

(function($) {
	$.fn.extend({
		limiter: function(limit, elem) {

			$(this).bind("keyup change", function() {
				setCount(this, elem);
			});

			function setCount(src, elem) {
				var chars = src.value.length;
				if (chars > limit) {
					$(elem).text((chars - limit) + " Char exceeded");
				}
				else {
					$(elem).text((limit - chars) + " Char remaining");
				}
			}
			setCount($(this)[0], elem);
		}
	});
})(jQuery);

/**
 * funcArguments: User can pass a variable or array in funcArguments 
 */
function ModalDialogConfirm (message, funcArguments, okFun) {
	$("#confirmSavedDialogDiv").html(
		"<table><tr><td><img src='"+$("#contextPath").val()+"/images/warning.png'></td><td style='line-height:2; vertical-align: top;'>"+ message +"</td></tr></table>");
	$("#confirmSavedDialogDiv").dialog({
		dialogClass: 'alert',
		show: 'slide',
		resizable: false,
		showTitlebar: true,
		modal: true,
		buttons : {
			Yes: function(){
				if($.isFunction(okFun)){
					okFun(funcArguments);
				}
				$( this ).dialog( "close" );
			},
			No: function () {
				$( this ).dialog( "close" );
			}
		}
	});
};

/**
 * funcArguments: User can pass a variable or array in funcArguments for No as well
 */
function ModalDialogConfirmWith2Funcs (message, func1Arguments, okFun, func2Arguments, cancelFun) {
	$("#confirmSavedDialogFunctionsDiv").html(
		"<table><tr><td><img src='"+$("#contextPath").val()+"/images/warning.png'></td><td style='line-height:2; vertical-align: top;'>"+ message +"</td></tr></table>");
	$("#confirmSavedDialogFunctionsDiv").dialog({
		dialogClass: 'alert',
		show: 'slide',
		resizable: false,
		showTitlebar: true,
		modal: true,
		buttons : {
			Yes: function(){
				if($.isFunction(okFun)){
					okFun(func1Arguments);
				}
				$( this ).dialog( "close" );
			},
			No: function () {
				if($.isFunction(cancelFun)){
					cancelFun(func2Arguments);
				}
				$( this ).dialog( "close" );
			}
		}
	});
};

function reloadJqGridAfterAddRecord(gridName, defaultSortColumnName, order) {
    $("#" + gridName).jqGrid('setGridParam', {
        sortname: defaultSortColumnName,
        sortorder: order == undefined ? 'desc' : order,
        postData: {
            searchField: '',
            searchString: '',
            searchOper: ''
        }
    });
    $(".s-ico", "#gview_" + gridName).css("display", "none");
    $("#" + gridName).trigger("reloadGrid", [{
        page: 1
    }]);
}

/**
* Method created for deleting the first record from the grid (only from UI)
* No physical deletion is done,just pass grid id for performing the task
* 
* Just call this function from your code once you got confirmation 
* from DB that new record is added and before adding new row to grid
*/

function deleteGridFirstRow(gridId){	
	var recordInPage = $('#'+gridId).getDataIDs().length;
	var maxRecordsInPage = $('#'+gridId).getGridParam('rowNum');
	var firstRecord = $('#'+gridId).getDataIDs()[0];	
    if(recordInPage > maxRecordsInPage){    	
    	$('#'+gridId).jqGrid("delRowData", firstRecord);
    }
}

/**
 * Function used for Scrolling the jqgrid to the selected record
 */
function scrollToRow (targetGrid, id) {
    var rowHeight = getGridRowHeight(targetGrid) || 23; // Default height
    var index = jQuery(targetGrid).getInd(id);
    jQuery(targetGrid).closest(".ui-jqgrid-bdiv").scrollTop(rowHeight * index);
}

function getGridRowHeight (targetGrid) {
    var height = null;
    try {
        height = jQuery(targetGrid).find('tbody').find('tr:first').outerHeight();
    }
    catch(e){
    	//catch and just suppress error
    }
    return height;
}

jQuery.logThis = function( text ){
	if( (window['console'] !== undefined) ){
		console.log( text );
	}
};

/**
 * Arguments: User can pass a variable or array in arguments 
 */
function handelIntegrationExceptionOnServer(XMLHTTPRequest, args){
	var errorObj = eval('(' + XMLHTTPRequest.responseText + ')').errorObject;	
	if(errorObj.messageType == 'CONFIRM'){
		new ModalDialogConfirm(errorObj.errorMessage, args, okConfirm);
	} else if(errorObj.messageType == 'SOS_VIOLATION') {
		$( "#downloadExcel" ).dialog( "close" );
		new ModalDialogConfirm(errorObj.errorMessage, args, sosViolationOkConfirm);
	} else if(errorObj.messageType == 'PROPOSAL_ERROR'){	
		jQuery("#runtimeDialogDiv").model({ theme: 'Error', autofade: false,
            message: errorObj.errorMessage, close: windowReloadOnConfirm,
            buttons: [{
                text: "Ok", click: function(){
                    $(this).dialog("close");
                    windowReloadOnConfirm();
                }
            }]
        });
	} else if (errorObj.messageType == 'SALESFORCE_CONNECTION_ERROR') {
        jQuery("#runtimeDialogDiv").model({ theme: 'Error', autofade: false,
        	width: 400, height: 200,
            message: errorObj.errorMessage, close: windowReloadOnConfirm,
            buttons: [{
                text: "Ok", click: function(){
                    $(this).dialog("close");
                    windowReloadOnConfirm();
                }
            }]
        });
	} else {
        jQuery("#runtimeDialogDiv").model({
            theme: 'Error', autofade: false, width: 320, height: 170, message: errorObj.errorMessage
        });
	}
}

function windowReloadOnConfirm(){
	window.location.reload(true);
}

/**
 * This Message is Executed on Pressing Yes of ModalDialogConfirm
 * @param {Object} formName
 */
function okConfirm (formName) {
	$('#forceUpdate', '#' + formName).val(true);
	$('input[class=save-btn]', '#' + formName).click();	
	$('#forceUpdate', '#' + formName).val(false);
}

function enableDialogButton(dialogDivId){
	$("#"+dialogDivId).dialog({
		beforeClose: function( event, ui ) { 
			$(".ui-dialog-buttonset button").show();
			$(".ui-dialog-buttonset button").button("enable");
	 	}
	});
}