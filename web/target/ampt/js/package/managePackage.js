/**
 * JavaScript for manage package screen
 *
 * @author amandeep.singh
 * @version 2.0
 */
var sectionId = resourceBundle['package.lineItem.section.targetType.id'];

var subSectionId = resourceBundle['package.lineItem.subSection.targetType.id'];

var emailAvailableDatesForPackageArray = new Array();

jQuery(document).ready(function() {
	
	$("#packageSalescategory","#ManagePackage").multiselect({
		selectedList: 2,
		position: {
	      my: 'bottom',
	      at: 'top'
		}
	}).multiselectfilter();
	
	
	jQuery("#packageSearchOption", "#managePackageContainer").val('packageName');
	
	$('#packageLineItemsAvailStatus').click(function() {
		packageLineItemsAvailStatus();
	});
	
    $('#packageLineItemsForm #calculateBasePrice').click(function(){
        calculatePrice();
    });
	
    var multiPkgUpdatePriceConfirmationDlg = new ModalDialog();
    multiPkgUpdatePriceConfirmationDlg.height = 210;
    multiPkgUpdatePriceConfirmationDlg.width = 360;
    multiPkgUpdatePriceConfirmationDlg.resizable = false;
    multiPkgUpdatePriceConfirmationDlg.buttons = [{
        text: "Yes",
        click: function(){
            $(this).dialog("close");
            updatePkgMultiLineItemPrice();
        }
    }, {
        text: "No",
        click: function(){
            var select = $("#PackageLineItemMultiSelect_Header");
            customLineItemSelect(select);
            $(this).dialog("close");
        }
    }];
    $("#multiPkgLineItemPriceUpdateContainer").dialog(multiPkgUpdatePriceConfirmationDlg);
    enableDialogButton("multiPkgLineItemPriceUpdateContainer");

	/**
	 * Register select2 plugin for product select box
	 */
	$("#sosProductId", "#packageLineItemsForm").select2();
	$("#sosProductId_select2", "#packageLineItemsForm").css("z-index", "0");

	/**
	 * Change event for product select box: On change populate sales target for product
	 */
	$("#sosProductId", "#packageLineItemsForm").change(function(){
		$("#sor","#packageLineItemsForm").val('');
		$("#productCreativeSummary", "#packageLineItemsForm").qtip("hide");
		$("#pricingCalculationStep", "#packageLineItemsForm").qtip("hide");
		if ($("#flagForOnChange", "#packageLineItemsForm").val() == "true") {
			clearPackageAvails();
			$("#placementName", "#packageLineItemsForm").val("");
			if ($("#priceType", "#packageLineItemsForm").val() != "FLAT RATE") {
               $("#rateCardPrice", "#packageLineItemsForm").val("");
               $("#discountPercentage","#lineItemFinancialFieldsContainer").val("");
            }else {
               $("#rateCardPrice", "#packageLineItemsForm").val("NA");
            }
			
			if ($("#sosProductId", "#packageLineItemsForm").val() != "" && $("#lineItemID", "#packageLineItemsForm").val() != "") {
				setsosSalesTargetIdValue($("#sosProductId", "#packageLineItemsForm").val());
				if ($("#priceType", "#packageLineItemsForm").val() == "CPM" || $("#priceType", "#packageLineItemsForm").val() == "CUSTOM UNIT"){
	                var ajaxReq = new AjaxRequest();
	            	ajaxReq.url = "../packagelineitems/getProductCreativesSpecTypeLst.action?productId=" + $("#sosProductId", "#packageLineItemsForm").val();
	            	ajaxReq.success = function(result, status, xhr) {
	            		var specTypes = result.objectMap.gridKeyColumnValue;
	            		if(specTypes == ""){
	            			$("#specType", "#packageLineItemsForm").val('RICH_MEDIA');
	    					$("#specType", "#packageLineItemsForm").multiselect("refresh");
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
			if($("#sosProductId", "#packageLineItemsForm").val() == ""){
				$("#placementName", "#packageLineItemsForm").val("");
				$("#sosSalesTargetId > option").remove();
				if ($("#lineItemType_s", "#lineItemTargeting").is(':checked')) {
					$("#sosSalesTargetId", "#packageLineItemsForm").multiselect("refresh");
				}else{
					$("#sosSalesTargetId", "#packageLineItemsForm").select2('data', null);
				}
				if ($("#priceType", "#packageLineItemsForm").val() == "CPM" || $("#priceType", "#packageLineItemsForm").val() == "CUSTOM UNIT"){
                	$("#specType", "#packageLineItemsForm").val('RICH_MEDIA');
                	$("#specType", "#packageLineItemsForm").multiselect("refresh");
                }else{
                	$("#specType", "#packageLineItemsForm").val('STANDARD');
					$("#specType", "#packageLineItemsForm").multiselect("refresh");
                }
			}
			if ($("#rateCardPrice" , "#lineItemPricingContainer").val() == "" || $("#rateCardPrice", "#lineItemPricingContainer").val() == "NA" || $("#rateCardPrice", "#lineItemPricingContainer").val() == "Not Defined") {
            	$("#rateX", "#lineItemPricingContainer").attr('checked', false).attr('disabled', true);
       		}
		}

		if($("#sosProductId", "#packageLineItemsForm").val() == ""){
        	$('#packageLineItemsForm #productCreativeSummary').hide();
        } else {
        	 $('#packageLineItemsForm #productCreativeSummary').show();
        }
		$('#isViewable', '#packageLineItemsForm').val($("option:selected", this).attr('is-viewable')=='Y' ? '1' : '0');
	});

	/**
	 * Register click event on sale target summary link to show sales target weighs
	 */
	$("#STWeightSummary", "#packageLineItemsForm").click(function() {
		showPackageSTWeightSummary();
	});
	
	$('#productCreativeSummary', '#packageLineItemsForm').click(function() {
    	showPkgProductCreativeSummary();
	});
		
    $("#pricingCalculationStep", "#packageLineItemsForm").click(function(){
        showPackagePriceCalSummary();
    });

	$("#sosSalesTargetId", "#lineItemTargeting").bind("change", function () {		
		if($("#flagForOnChange", "#packageLineItemsForm").val() == 'true'){
			getPlacementName();
			$("#rateCardPrice", "#packageLineItemsForm").val("");
			$("#sor", "#lineItemTargeting").val('');
            $("#rateX", "#lineItemPricingContainer").attr('checked', false).attr('disabled', true);
            if($("#lineItemType_e", "#lineItemTargeting").is(':checked')){
				 $("#startDate","#packageLineItems").val("");
			 }
		}
		enableLineItemDefaults();
		enableDisableCalendar();
		if($("#lineItemType_e", "#lineItemTargeting").is(':checked') && $('#sosSalesTargetId','#lineItemTargeting').val() != "" && $('#sosSalesTargetId','#lineItemTargeting').val() != null) {			
			var ajaxReq = new AjaxRequest();
			ajaxReq.url = "../proposalWorkflow/getEmailAvailableDates.action?sosProductId=" + $('#sosProductId','#lineItemTargeting').val() + "&sosSalesTargetId=" + $('#sosSalesTargetId','#lineItemTargeting').val();
			ajaxReq.success = function(result, status, xhr){
				emailAvailableDatesForPackageArray = result;
			};
			ajaxReq.submit();
		}
	});
	
	/**
	 * Add dialog box for fetch avails loading message
	 */ 
	$("#availProgress", "#packageLineItemsForm").dialog({
		dialogClass: 'alert',
		title: 'Fetching Avails...',
		autoOpen: false,
		resizable: false,
		showTitlebar: true,
		modal: true,
		height: 100,
		width: 210
	});

	/**
	 * Dialog box for goal seek feature
	 */
	var calculateGoalSeekForPackageModal = new ModalDialog();
	calculateGoalSeekForPackageModal.width = 290;
	calculateGoalSeekForPackageModal.closeOnEscape = false;
	calculateGoalSeekForPackageModal.buttons = [{
		text: "Ok",
		click: function(){
			var goalSeekValue = $('input[name=goalSeekValue]:checked').val();
			var rate = $("#rate", "#packageLineItemsForm").val();
			var impressionTotal = $("#impressionTotal", "#packageLineItemsForm").val();
			var totalInvestment = $("#totalInvestment", "#packageLineItemsForm").val();
			var priceType = $("#packageLineItemsForm #priceType").val();
			var parseTotalInvestment = totalInvestment.replace(/,/g, '');
			var parseImpressionTotal = impressionTotal.replace(/,/g, '');
			var parseRate = rate.replace(/,/g, '');
			var calculation = 0.0;
			if (goalSeekValue == "impressionTotal" && parseRate > 0.0) {
				calculation = (parseTotalInvestment * 1000) / parseRate;
				calculation = parseInt(calculation);
				$("#impressionTotal", "#packageLineItemsForm").val(formatNumber(calculation));
				calculateSOVFromPackage();
			}
			else 
				if (goalSeekValue == "impressionTotal" && parseRate == 0.0) {
					$("#impressionTotal", "#packageLineItemsForm").val(formatNumber("0"));
					calculateSOVFromPackage();
				}
				else {
					if (goalSeekValue == "rate" && parseImpressionTotal > 0.0) {
						calculation = (parseTotalInvestment * 1000) / parseImpressionTotal;
						calculation = roundoffto2places(calculation);
						$("#rate", "#packageLineItemsForm").val(formatDecimal(calculation));
						calculateDiscountPercent();
					}
					else {
						$("#rate", "#packageLineItemsForm").val(formatDecimal("0.00"));
					}
				}
			$(this).dialog("close");
		}
	}];
    $("#chooseGoalSeek").dialog(calculateGoalSeekForPackageModal);
    $("#chooseGoalSeek").closest('.ui-dialog').find('.ui-dialog-titlebar-close').hide();

	/**
	 * Register Date picker on package start and end date
	 */
	var datesLineItem = jQuery( "#startDate, #endDate", "#packageLineItems" ).datepicker({
		autoSize: true,
		showOn: "both",
		buttonText: "Select date",
		buttonImage: '../images/calendar.gif',
		buttonImageOnly: true,
		numberOfMonths: 3,
		onSelect: function( selectedDate ) {
			date = $.datepicker.parseDate($( this ).data( "datepicker" ).settings.dateFormat ||$.datepicker._defaults.dateFormat, selectedDate, $( this ).data( "datepicker" ).settings );
			/**
			 * Call clearPackageAvails() function on select of datepicker
			 */
			clearPackageAvails();
			$("#flight", "#packageLineItems").val("");
			removeDivCss();
			if($("#lineItemType_e", "#lineItemTargeting").is(':checked') ){
				$("#emailReservationStatus", "#lineItemTargeting").val("");
				$("#endDate","#packageLineItems").val($("#startDate","#packageLineItems").val());
			}
			enableDisableCalendar();
			$("#sor", "#lineItemTargeting").val('');
			if ($("#priceType", "#packageLineItemsForm").val() == "FLAT RATE") {
	            $("#rateCardPrice", "#packageLineItemsForm").val("NA");
	            }else{
	            $("#rateCardPrice", "#packageLineItemsForm").val("");	
	        }
		},
	    beforeShowDay: function(date){
	    	if($("#lineItemType_e", "#lineItemTargeting").is(':checked') && $('#sosSalesTargetId','#lineItemTargeting').val() != "" && $('#sosSalesTargetId','#lineItemTargeting').val() != null) {  
	        	var string = jQuery.datepicker.formatDate('mm/dd/yy', date);
	        	return [ emailAvailableDatesForPackageArray.indexOf(string) > -1 ];
	        }else{
	        	return [true];
	        }
	    }
	});
	 
	/**
	 * Register change event for flight
	 */
	$("#flight", "#packageLineItems").change(function(){
		if ($("#flight", "#packageLineItems").val() != "") {
			$("#startDate", "#packageLineItems").val("");
			$("#endDate", "#packageLineItems").val("");
			clearPackageAvails();
			if(!($("#endDate", "#packageLineItems").val() == "" && $("#startDate", "#packageLineItems").val() == "")){
				
			if ($("#priceType", "#packageLineItemsForm").val() == "FLAT RATE" ) {
	            $("#rateCardPrice", "#packageLineItemsForm").val("NA");
	            }else{
	            $("#rateCardPrice", "#packageLineItemsForm").val("");	
	            }
			}
			if(!$("#lineItemType_s", "#lineItemTargeting").is(':checked')){
            	$("#calculateSOR","#lineItemTargeting").hide();
            	$("#sor", "#lineItemTargeting").val('');
    		}
		}
		else {
			if ($("#endDate", "#packageLineItems").val() == "" || $("#startDate", "#packageLineItems").val() == "") {
				$("#sov", '#packageLineItemsForm').val("");
			}
			if ($("#priceType", "#packageLineItemsForm").val() == "FLAT RATE" ) {
	            $("#rateCardPrice", "#packageLineItemsForm").val("NA");
	            }else{
	            $("#rateCardPrice", "#packageLineItemsForm").val("");	
	            }
		}
	});

	$("input[class=numericdecimal]", "#packageLineItemsForm").numeric({
		negative: false
	});
	$("input[class=numeric]", "#packageLineItemsForm").numeric({
		negative: false
	});
	$("input[class=numericdecimal]", "#ManagePackage").numeric({
		negative: false
	});

	/**
	 * Initialised package attachment JQgrid and dialog
	 */
	initDocUploadHTML('packageAttachement', DOCUMENT_TYPE);
	var packageAttachementGridOpts = initAttachementGridOpts('packageAttachement');
	packageAttachementGridOpts.afterGridCompleteFunction = function(){
		if (jQuery('#packageAttachementTable').jqGrid('getGridParam', 'records') > 0) {
			jQuery('#packageMasterGrid').setCell(jQuery("#ManagePackage #packageId").val(), 'hasDocument', true);
		}
		else {
			jQuery('#packageMasterGrid').setCell(jQuery("#ManagePackage #packageId").val(), 'hasDocument', false);
		}
	};
	jQuery("#packageAttachementTable").jqGrid(packageAttachementGridOpts).navGrid('#packageAttachementPager', {
	edit: false, search: false,
		addfunc: function(rowid){
			jQuery("#packageAttachementForm #id").val('-1');
			clearCSSErrors('#packageAttachementForm');
			jQuery("#packageAttachementForm #fileName").val("");
			jQuery("#packageAttachementDialog").dialog("open");
		}
	});

	/**
	 * Model dialog for package file attachment
	 */
	var packageAttachementFormManager = new FormManager();
	packageAttachementFormManager.gridName = 'packageAttachementTable';
	packageAttachementFormManager.formName = 'packageAttachementForm';	
	
	var packageAttachement = new ModalDialog();	
	
	/**
	 * Register event on save button of attachment dialog box
	 */
	packageAttachement.buttons.Save = function(){
		jQuery("#packageAttachementForm #componentId").val(jQuery("#packageId").val());
		if (jQuery("#packageAttachementForm #id").val() != -1) {
			var id = jQuery("#packageAttachementTable").jqGrid('getGridParam', 'selrow');
			var ret = jQuery("#packageAttachementTable").jqGrid('getRowData', id);
			jQuery("#packageAttachementForm #fileName").val(ret.fileName);
		}

		var packageAttachementForm = new JQueryAjaxForm(packageAttachementFormManager);
		packageAttachementForm.dataType = 'xml';
		packageAttachementForm.doCustomProcessingAfterValidationFailedXML = function(){
			$("#packageAttachementMessage").text("");
			$(".ui-dialog-buttonset button:contains('Save')").button("enable");
		};
		packageAttachementForm.doCustomProcessingAfterFormSucsessXML = function(){
			$(packageAttachementFormManager.formIdentifier()).resetForm();
			$("#packageAttachementMessage").text(resourceBundle['message.generic.documentUploadSuccess']).css("color", "green");
			$(".ui-dialog-buttonset button:contains('Save')").button("enable");
		};
		packageAttachementForm.doCustomProcessingAfterFormError = function(){
			$("#packageAttachementMessage").text("");
			$(".ui-dialog-buttonset button:contains('Save')").button("enable");
		};
		packageAttachementForm.submit();
		$(".ui-dialog-buttonset button:contains('Save')").button("disable");
	};

	/**
	 * Register event on save button of attachment dialog box
	 */
	packageAttachement.buttons.Close = function(){
		jQuery(this).dialog("close");
	};
	
	packageAttachement.close = function(){
		jQuery("#packageAttachementForm").resetForm();
		jQuery("#packageAttachementMessage").text("");
		jQuery("#packageAttachementTable").trigger("reloadGrid");
	};
	jQuery("#packageAttachementDialog").dialog(packageAttachement);
	enableDialogButton("packageAttachementDialog");
	
	/**
	 * Modal dialog for line item delete confirmation.
	 */
	var multiDeleteConfirmationDlg = new ModalDialog();
	   multiDeleteConfirmationDlg.height = 150;
	   multiDeleteConfirmationDlg.width = 350;
	   multiDeleteConfirmationDlg.resizable = false;	
	   multiDeleteConfirmationDlg.buttons = [{
	        text: "Yes",
	        click: function() {
	        	$(this).dialog("close");
	            deleteLineItems();
	        }
	    }, {
	        text: "No",
	        click: function(){			
	            $(this).dialog("close");
	        }
	    }];
		$("#multiPkgLineItemDeleteContainer").dialog(multiDeleteConfirmationDlg);
		enableDialogButton("multiPkgLineItemDeleteContainer");
		
	
	/**
	 * Initialised Package Line Items JQgrid
	 */
	var id = jQuery("#ManagePackage #packageId").val();
	var packageLineItemsGridOpts = new JqGridBaseOptions();
	packageLineItemsGridOpts.colNames = ['<input type="checkbox" id="PackageLineItemMultiSelect_Header"  name="PackageLineItemMultiSelect" onClick="customLineItemSelect(this)">','ID'
	                                     , 'Package ID', 'Sequence No.', 'Placement Name', 'Sales Targets Id', 'CPM', 'Offered Impressions', 'Total Investment', 'Product ID'
										 , 'Start Date', 'End Date','Viewable', 'Action', 'Spec Type', 'Product Active', 'SalesTarget Active'] ;
	packageLineItemsGridOpts.colModel = [
	    {name:"customSelect", index:"customSelect", sortable:false, key:false, formatter:lineItemMultiSelectFormatter, width:50},
		{name:'lineItemID', index:'lineItemID', key:true, width:50, align:"right" }, 
		{name:'packageID', index:'packageID', hidden:true, key:false}, 
		{name:'lineItemSequence', index:'lineItemSequence', key:false, width:100,align:"right"},		 
		{name:'placementName', index:'placementName', key:false, search:false}, 
		{name:'sosSalesTargetId', index:'sosSalesTargetId', hidden:true, key:false}, 
		{name:'rate', index:'rate', key:false, width:70 , align:"right"}, 
		{name:'impressionTotal', index:'impressionTotal', key:false, width:150 , align:"right"}, 
		{name:'totalInvestment', index:'totalInvestment', key:false, width:120 , align:"right"}, 		
		{name:'sosProductId', index:'sosProductId', hidden:true, key:false}, 		 
		{name:'startDate', index:'startDate', key:false}, 
		{name:'endDate', index:'endDate', key:false},
		{name:"viewableDisplayName",index:"viewableDisplayName",width:100, align:"left"},
		{name:'act', index:'act', sortable:false, key:false, width:80}, 
		{name:'specType', index:'specType', hidden:true, key:false}, 
		{name:'product_active', index:'product_active', hidden:true, key:false}, 
		{name:'salesTarget_active', index:'salesTarget_active', hidden:true, key:false}
	];
	packageLineItemsGridOpts.pager = jQuery('#packageLineItemsPager');
	packageLineItemsGridOpts.sortname = "lineItemID";
	packageLineItemsGridOpts.rowList = [10,20,40,100];
	packageLineItemsGridOpts.afterGridCompleteFunction = function(){
		var ids = jQuery("#packageLineItemsTable").jqGrid('getDataIDs');
		for(var i = 0;i < ids.length; i++){
			var cl = ids[i];
			be = "<div style='padding-left:43%'>" +
					"<div style='float:left;' title='Edit'>" +
						"<a onclick='editLineItemsGridRow("+ cl +")'>" +
							"<span class='ui-icon ui-icon-pencil' style='cursor:pointer;'/>" +
						"</a>" +
					"</div>" +
				"</div>"; 
			jQuery('#packageLineItemsTable').jqGrid('setRowData', cl, {act:be});
		}
        if (ids.length > 0 && !($("#hasReadOnlyRights", "#managePackageContainer").length > 0)) {
        	$('#packageAvailStatuslink','#managePackageContainer').show();
        }
        else {
       		 $('#packageAvailStatuslink','#managePackageContainer').hide();	
        }
		var packId = jQuery("#ManagePackage #packageId").val();
		jQuery('#packageMasterGrid').setCell(packId, 'lineItemCount', jQuery('#packageLineItemsTable').jqGrid('getGridParam', 'records'));
		$("#PackageLineItemMultiSelect_Header").attr("checked", false);
	};
	
	packageLineItemsGridOpts.onSelectRow = function(id) {
    	var gsr = $("#packageLineItemsTable","#packageLineItemShowHide").jqGrid('getGridParam', 'selrow');
        if (!gsr) {
        	var topRowId = $(this).getDataIDs()[0];
    		if(topRowId != ''){
    			jQuery(this).setSelection(topRowId, true);
    		}
        }
    };
    
    
	$("#packageLineItemsTable").jqGrid(packageLineItemsGridOpts).navGrid('#packageLineItemsPager', {
		edit: false, search: false, del: false,
		addfunc: function(rowid) {
			createSTWeightSummaryQtip();
			createPriceCalSummaryQtip();
			createProductCreativeSummaryQtip();
			clearCSSErrors('#packageLineItemsForm');
			jQuery("#packageLineItemsTable").jqGrid('resetSelection');
			removeDivCss();
			resetHiddenFields();			
			 /**reseting salesTargetId Hidden Field on adding new*/
			$("#packageLineItemsForm #sosSalesTargetId").val('');
			document.getElementById("packageLineItems").reset();
			$('#packageLineItemsForm #priceType').val('CPM');
			onClickPriceTypeRadio($('#packageLineItemsForm #priceType').val());
			$("#packageId", "#packageLineItemsForm").val(jQuery("#ManagePackage #packageId").val());
			resetMultiSelectForPackage();
			$("#sosProductId", "#packageLineItemsForm").select2("val", "");
			$("#packageLineItemsForm").dialog("open");
			$("#reserveChkBoxContainer", "#lineItemCalendarContainer").hide();
			$(".AddIncSearchChooser", "#packageLineItemsForm").hide();
			$('button:first-child', '.ui-dialog-buttonset').focus();
			$("#sosProductId", "#packageLineItemsForm").val("");
			$('#packageLineItemsForm #lineItemID').val(0);
			$("#lineItemType_s").attr('disabled', false);
            $("#lineItemType_r").attr('disabled', false);
            $("#lineItemType_e").attr('disabled', false);
            $("#productType", "#lineItemFinancialFieldsContainer").val('S');
			changeLineItemType('S', false);
			$("#lineItemType_s").attr('checked', 'checked');
			$("#sosSalesTargetId > option").remove();
			$("#sosSalesTargetId", "#packageLineItemsForm").multiselect("refresh");
			fillSequenceNoForPackageLineItem();
			setSpecTypeForPackage();
			$("#endDate", "#packageLineItems").val(jQuery( "#validTo" , "#ManagePackage").val());
		 	$("#startDate", "#packageLineItems").val(jQuery( "#validFrom" , "#ManagePackage").val());
			$('#geoTargetsummary').find("tr:gt(0)").remove();
			resetTargetingFields();
			if ($("#priceType", "#packageLineItemsForm").val() == "CPM" || $("#priceType", "#packageLineItemsForm").val() == "PRE EMPTIBLE" || $("#priceType", "#packageLineItemsForm").val() == "CUSTOM UNIT") {
		         $('#packageLineItemsForm #pricingCalculationStep').show();
    			 $('#packageLineItemsForm #calculateBasePrice').removeAttr("disabled");
				 $('#packageLineItemsForm #calculateBasePrice').removeClass("disabled-btn");
		    } else {
        		$('#packageLineItemsForm #pricingCalculationStep').hide();
		        $('#packageLineItemsForm #calculateBasePrice').attr("disabled", "disabled");
        		$('#packageLineItemsForm #calculateBasePrice').addClass("disabled-btn");
		    }
			if ($("#rateCardPrice" , "#lineItemPricingContainer").val() == "" || $("#rateCardPrice", "#lineItemPricingContainer").val() == "NA" || $("#rateCardPrice", "#lineItemPricingContainer").val() == "Not Defined") {
            	$("#rateX", "#lineItemPricingContainer").attr('checked', false).attr('disabled', true);
       		}
			jQuery("textarea", "#packageLineItemsForm").change();
			$('#packageLineItemsForm #productCreativeSummary').hide();
			$("#rateXInfo", "#packageLineItemsForm").show();
		}
	});		

	$("#packageLineItemsTable","#packageLineItemShowHide").jqGrid('navButtonAdd', "#packageLineItemsPager", {caption: "", id: "deleteLineItem_packageLineItemsTable", title: "Delete Line Item", buttonicon: 'ui-icon-trash',
			onClickButton: function() {
				clearCSSErrors('#packageLineItemsForm');
				removeDivCss();
				var isRowSelected =  false;
				$("#packageLineItemsTable","#packageLineItemShowHide").find(':checkbox').each(function(){
					if($(this).is(":checked")){
						isRowSelected = true;
					}
				});
				if(isRowSelected) {
					$("#multiPkgLineItemDeleteContainer").dialog("open");
				} else {
					jQuery("#runtimeDialogDiv").model({theme: 'Error', autofade: false, message: resourceBundle['error.no.lineItem.selected.for.deletion']});
				}		
			}
	 });
	
	/**
	 * Add copy line item icon in package line item table
	 */
	$("#packageLineItemsTable").jqGrid('navButtonAdd', "#packageLineItemsPager", {
		caption: "", id: "copyLineItem_packageLineItemsTable",
		title: "Copy Line Item", buttonicon: 'ui-icon-copy',
		onClickButton: function(){
			var selectedIds = "";
			var isRowSelected =  false;
			$("#packageLineItemsTable","#packageLineItemShowHide").find(':checkbox').each(function(){
				if($(this).is(":checked")){
					selectedIds =  selectedIds + $(this).val() + ",";
				}
			});
			if(selectedIds.length > 0){
				selectedIds = $.trim(selectedIds).substring(0, selectedIds.length - 1);
				var ajaxReq = new AjaxRequest();
				ajaxReq.url = '../packagelineitems/copyLineItem.action?lineItemIds=' + selectedIds;
				ajaxReq.success = function(result, status, xhr){
					reloadJqGridAfterAddRecord("packageLineItemsTable", "lineItemID");
					jQuery("#runtimeDialogDiv").model({theme: 'Success', message: resourceBundle['message.generic.lineItemCopiedSuccessfully']});
					$("#copyLineItem_packageLineItemsTable").removeClass("ui-state-disabled");
					$("#PackageLineItemMultiSelect_Header").attr("checked", false);
				};
				ajaxReq.submit();
				$("#copyLineItem_packageLineItemsTable").addClass("ui-state-disabled");
				
			} else {
				jQuery("#runtimeDialogDiv").model({theme: 'Error', autofade: false, message: resourceBundle['error.no.lineItem.selected.for.copy']});
			}
		}
	});
	
    $("#packageLineItemsTable").jqGrid('navButtonAdd', "#packageLineItemsPager", {caption: "", id: "updatePrice_packageLineItemsTable",
        title: "Update Base Price", buttonicon: 'ui-icon-bulkPricing', onClickButton: function(){
            var selectedIds = "";
            var isRowSelected = false;
            $("#packageLineItemsTable", "#packageLineItemShowHide").find(':checkbox').each(function(){
                if ($(this).is(":checked")) {
                    selectedIds = selectedIds + $(this).val() + ",";
                }
            });
            if (selectedIds.length > 0) {
                $("#multiPkgLineItemPriceUpdateContainer").dialog("open");
            }
            else {
				jQuery("#runtimeDialogDiv").model({theme: 'Error', autofade: false, message: resourceBundle['error.no.lineItem.selected.for.update']});
            }
        }
    });
	
	/**
	 * Line Item sequence update qTip
	 */
	$("#packageLineItemsTable").sequence({pagerId: "packageLineItemsPager", doSequence: reArrangeLineItemSequence});
	
    /**
	 * Initialised package JQgrid
	 */
	var packageGridOpts = new JqGridBaseOptions();
	packageGridOpts.url = "../managepackage/loadmastergridData.action";
	packageGridOpts.colNames = ['Package ID', 'Package Name', 'Sales Category','Sales Category Id', 'Start Date', 'End date', 'Breakable', 'Total Budget ($)', 'Package Owner', 'Line Items', 'Expired', 'Owner ID', 'Break', 'Comments'];
	packageGridOpts.colModel = [
		{name:'packageId', index:'packageId', hidden:true, key:true}, 
		{name:'packageName', index:'packageName', sortable:true, key:false, width: 150}, 
		{name:'packageSalescategoryName', index:'packageSalescategoryName', sortable:false, hidden:false, key:false, hidedlg: false, search:false},
		{name:'packageSalescategory', index:'packageSalescategory', sortable:true, hidden:true, key:false, hidedlg: false, search:false},
		{name:'validFrom', index:'validFrom', sortable:true, key:false, width: 80}, 
		{name:'validTo', index:'validTo', sortable:true, key:false, width: 80}, 
		{name:'breakableStr', index:'breakableStr', sortable:true, key:false, width: 90},
		{name:'budget', index:'budget', sortable:true, key:false, width: 100, align:"right"}, 
		{name:'packageOwner', index:'packageOwner', sortable:true, key:false, width: 130},
		{name:'lineItemCount', index:'lineItemCount', sortable:false, key:false, width: 80}, 
		{name:'expired', index:'expired', sortable:false, key:false, width: 80}, 
		{name:'ownerId', index:'ownerId', hidden:true, key:false}, 
		{name:'breakable', index:'breakable', hidden:true, key:false}, 
		{name:'comments', index:'comments', hidden:true, key:false}
	];
	packageGridOpts.onSelectRow = function(id) {
		clearCSSErrors('#ManagePackage');	
		var gsr = jQuery("#packageMasterGrid").jqGrid('getGridParam','selrow');
		if(gsr){
			//GridToForm is used for setting the Grid Parameter to form provided name is same
			jQuery("#packageMasterGrid").jqGrid('GridToForm', gsr, "#ManagePackage");
			jQuery("textarea", "#ManagePackage").change();
			var colData = jQuery("#packageMasterGrid").getRowData(id)['packageSalescategory'];
			$('#packageSalescategory','#ManagePackage').val(colData.split(","));
			$('#packageSalescategory','#ManagePackage').multiselect("refresh");
		} else {
			alert("Please select Row");
		}
		jQuery("#package-tabs-1-lbl a").text(resourceBundle['tab.generic.packageDetails']);
		
		var packageId = jQuery("#ManagePackage #packageId").val();
		$("#packageLineItemShowHide").show();
		$("#package-tabs-2-lbl").show();
		
		$('#packageLineItemsTable').setGridParam({url:'../packagelineitems/loadLineItemsGridData.action?packageId='+packageId});
		$("#package-tabs-3-lbl").show();
		$('#packageAttachementTable').setGridParam({url:'../document/documentgriddata.action?componentId='+packageId+'&documentFor='+DOCUMENT_TYPE, 
													editurl: '../document/deletedocument.action?componentId='+packageId+'&documentFor='+DOCUMENT_TYPE});
		$('#packageAttachementTable').trigger("reloadGrid");//Refreshing the attachment grid
		
		$("#packageAttachement").show();
		$('#packageLineItemsTable').trigger("reloadGrid");
	};
	
	packageGridOpts.afterGridCompleteFunction = function() {
		//For colour the row if package is expired
		var rowIds = jQuery("#packageMasterGrid").jqGrid('getCol', 'packageId', false);
		for (var i = 0; i < rowIds.length; i++) {
			var colData = jQuery("#packageMasterGrid").getRowData(rowIds[i])['expired'];
			if(colData == 'Yes') {
				$("#" + rowIds[i] , "#packageMasterGrid").find("td").addClass("expire-package");
			}
		}
		if (jQuery('#packageMasterGrid').getDataIDs().length > 0) {
			document.getElementById("packageTab").style.display = 'block';
		} else {
			document.getElementById("packageTab").style.display = 'none';
		}
	};
	packageGridOpts.pager = jQuery('#packageMasterPager');
	packageGridOpts.sortname = "packageId";
	packageGridOpts.caption = resourceBundle['tab.generic.managePackage'];

	jQuery("#packageMasterGrid").jqGrid(packageGridOpts).navGrid('#packageMasterPager', {
		edit: false, search: false,
		addfunc: function(rowid){
			clearCSSErrors('#ManagePackage');
			resetRowSelectionOnAdding("#packageMasterGrid", "#packageId");
			$("#packageLineItemShowHide").hide();
			$("#packageTab").show();
			$("#package-tabs-2-lbl").hide();
			$("#package-tabs-3-lbl").hide();

			document.getElementById("packageAttachement").style.display = 'none';
			document.ManagePackage.reset();
			jQuery("textarea", "#ManagePackage").change();
			jQuery("#package-tabs-1-lbl a").text(resourceBundle['tab.generic.addNewPackage']);
			jQuery("#packageId", "#ManagePackage").val(0);
			var index = jQuery('#packageTab a[href="#package-tabs-1"]').parent().index();
			jQuery('#packageTab').tabs('select', index);
			$('button:first-child', '.ui-dialog-buttonset').focus();
		},
		delfunc: function(rowid){
			var packageId = jQuery("#ManagePackage #packageId").val();
			jQuery("#packageMasterGrid").jqGrid('setGridParam', {page: 1});
			jQuery('#packageMasterGrid').delGridRow(rowid, {
				url: '../managepackage/deletepackage.action?packageId=' + packageId
			});
		},
		beforeRefresh: function(){
			jQuery("#packageSearchOption", "#managePackageContainer").val('packageName');
			jQuery("#packageSearchValue", "#managePackageContainer").val('');
			reloadJqGridAfterAddRecord('packageMasterGrid', "packageId");
		}
	});

	$("#packageMasterGrid").jqGrid('navButtonAdd', "#packageMasterPager", {
		caption: "", id: "copyPackage_packageMasterTable",
		title: "Clone Package", buttonicon: 'ui-icon-clone',
		onClickButton: function(){
			var gsr = $("#packageMasterGrid").jqGrid('getGridParam', 'selrow');
			var packageId = $("#packageMasterGrid").jqGrid('getCell', gsr, 'packageId');
			
			var ajaxReq = new AjaxRequest();
			ajaxReq.url = '../managepackage/copyPackage.action?packageID=' + packageId;
			ajaxReq.success = function(result, status, xhr){
				jQuery("#packageMasterGrid").jqGrid('setGridParam', { page: 1 }).trigger("reloadGrid");
				jQuery("#runtimeDialogDiv").model({theme: 'Success', message: resourceBundle['message.generic.packageCopiedSuccessfully']});
			};
			if (gsr) {
				ajaxReq.submit();
			}
			else {
				jQuery("#runtimeDialogDiv").model({theme: 'Error', height: 140, width: 250, message: resourceBundle['message.generic.packageSelect'], autofade: false});
			}
		}
	});

	/**
	 * Initialised search panel on package grid
	 */
	initGridSearchOptions("packageMasterGrid", "managePackageSearchPanel", "managePackageContainer");

	/**
	 * Enable auto search on attribute grid
	 */
	enableAutoSearch(jQuery("#packageSearchValue", "#managePackageContainer"), function() {
		jQuery("#packageMasterGrid").jqGrid('setGridParam', {
			url: "../managepackage/loadmastergridData.action", page: 1,
			postData: {
				searchField: jQuery("#packageSearchOption", "#managePackageContainer").val(),
				searchString: $.trim(jQuery("#packageSearchValue", "#managePackageContainer").val()),
				searchOper: 'cn'
			}
		}).trigger("reloadGrid");
	});

	/**
	 * Register change event on attribute search option select box
	 */
	jQuery("#packageSearchOption", "#managePackageContainer").bind("change", function () {
		jQuery("#packageSearchValue", "#managePackageContainer").val('').focus();
	});
	
	jQuery("#comments", "#managePackageContainer").limiter(500, $("#charsRemaining", "#managePackageContainer"));

	jQuery("#comments", "#packageLineItemsForm").limiter(500, $("#charsRemainingComments", "#packageLineItemsForm"));
	jQuery("#placementName", "#packageLineItemsForm").limiter(4000, $("#charsRemainingPlacement", "#packageLineItemsForm"));

	/**
	 * Initialised package line item form
	 */
	var packageLineItemsFormManager = new FormManager();
	packageLineItemsFormManager.gridName = 'packageLineItemsTable';
	packageLineItemsFormManager.formName = 'packageLineItems';

	var packageLineItemsDialog = new ModalDialog();
	packageLineItemsDialog.height = 700;
	packageLineItemsDialog.width = 1100;
	packageLineItemsDialog.closeOnEscape = false;
	packageLineItemsDialog.resizable = false;
	packageLineItemsDialog.buttons.Save = function() {
		clearCSSErrors('#packageLineItemsForm');	
		removeDivCss();
		if (validateLineItemTargeting()) {
		var packageLineItemForm = new JQueryAjaxForm(packageLineItemsFormManager);

		/*
		 * Submit request when click on save button
		 */
		$('#packageLineItemsForm #packageLineItems').attr("action","../packagelineitems/saveLineItems.action");
		var targetingString = $('#targetingString', '#lineItemTargeting').html();
		var paraIndex = targetingString.indexOf("<p>");
		if (paraIndex != -1) {
			targetingString = targetingString.replace(/<p>/g, "");
			targetingString = targetingString.replace(/<\/p>/gi, "");
		}
		var strongIndex = targetingString.indexOf("<strong>");
		if (strongIndex != -1) {
			targetingString = targetingString.replace(/<strong>/gi, "");
			targetingString = targetingString.replace(/<\/strong>/gi, "");
		}
		$('#targetingStringValue').val(targetingString);
		$("#productName", "#packageLineItemsForm").val($("#sosProductId option[value=" + $("#sosProductId", "#packageLineItemsForm").val() + "]", "#packageLineItemsForm").text());
		$("#sosProductClassName", "#packageLineItemsForm").val($("#sosProductId option:selected", "#packageLineItemsForm").parent("optgroup").attr("label"));
		packageLineItemForm.doCustomProcessingAfterFormSucsesJson = function(jsonResponse, XMLHTTPRequest){
			$('#messageHeaderFinancialDiv').html('');
			$(".ui-dialog-buttonset button:contains('Save')").button("enable");
			var returnedId = jsonResponse.objectMap.gridKeyColumnValue;
			$("#STWeightSummary", "#packageLineItemsForm").qtip("destroy");
			$("#pricingCalculationStep", "#packageLineItemsForm").qtip("destroy");
			$("#packageLineItemsForm").dialog("close");
			if ($('#packageLineItemsForm #lineItemID').val() == 0) {
				reloadJqGridAfterAddRecord(packageLineItemsFormManager.gridName, "lineItemID");
			}
			else {
				$("#productName", "#packageLineItemsForm").val($("#sosProductId option[value=" + $("#sosProductId", "#packageLineItemsForm").val() + "]", "#packageLineItemsForm").text());
				$("#sosSalesTargetId", "#packageLineItemsForm").multiselect('refresh');
				$("#product_active", "#packageLineItemsForm").val("Yes");
				$("#salesTarget_active", "#packageLineItemsForm").val("Yes");
				$("#packageLineItemsTable").jqGrid('FormToGrid', returnedId, '#packageLineItems');

				var salesTargetIds = $("#sosSalesTargetId", "#packageLineItemsForm").multiselect("getChecked").map(function(){
					return this.value;
				}).get();
				$("#packageLineItemsTable").setCell(returnedId, "sosSalesTargetId", salesTargetIds);

				var specTypeValue = $("#specType", "#packageLineItemsForm").multiselect("getChecked").map(function(){
					return this.value;
				}).get();
				$("#packageLineItemsTable").setCell(returnedId, "specType", specTypeValue);
			
				if ($("#priceType", "#packageLineItemsForm").val() == "FLAT RATE") {
					$("#packageLineItemsTable").setCell(returnedId, "rate", "NA");
				}
				$("#packageLineItemsTable").setCell(returnedId, "viewableDisplayName", $('#packageLineItemsForm #isViewable option:selected').text());
			}
			if($("#flight", "#packageLineItems").val() != "") {
	        	$("#packageLineItemsTable").setCell(returnedId, "startDate", $("#flight", "#packageLineItems").val()); 		
	        }
			document.packageLineItems.reset();
		};

		packageLineItemForm.doCustomProcessingAfterValidationFailedJson = function (jsonResponse, XMLHTTPRequest){
			$('#messageHeaderFinancialDiv').html('');
			$(".ui-dialog-buttonset button:contains('Save')").button("enable");
			var errorArray = jsonResponse.errorList;
			jQuery.each(errorArray, function(i, error){
				if (error.field == "startDate" || error.field == "endDate" || error.field == "sosSalesTargetId_custom" || error.field == "specType_custom") {
					//error.field = "packageRenderFragmentId" + error.field;
					jQuery("#" + error.field, "#" + packageLineItemsFormManager.formName).addClass("errorElement errortip").attr("title", error.errorMessageForUI + '<BR><b>Help:</b> ' + error.errorHelpMessageForUI);
				}
				if(error.field == "geoTargeting") {
	            	 jQuery("#geoTargetsummaryContainer" , "#lineItemTargeting").addClass("errorElement errortip").attr("title", error.errorMessageForUI);
	            }
			});
			$('.errortip', "#" + packageLineItemsFormManager.formName).qtip({
				style: {
					classes: 'ui-tooltip-red'
				},
				position: {
					my: 'bottom center',
					at: 'top center'
				}
			});
		};
		if ($("#lineItemID", "#packageLineItemsForm").val() == "") {
			$("#lineItemID", "#packageLineItemsForm").val(0);
		}	

		var specTypeValue = $("#specType", "#packageLineItemsForm").multiselect("getChecked").map(function(){
			return this.value;
		}).get();
		if($("#lineItemType_e", "#lineItemTargeting").is(':checked')){
			$("#endDate", "#packageLineItems").val($("#startDate", "#packageLineItems").val());
		}
		if (($('#packageLineItemsForm #priceType').val() == "CPM" || $('#packageLineItemsForm #priceType').val() == "PRE EMPTIBLE" || $('#packageLineItemsForm #priceType').val() == "CUSTOM UNIT") && jQuery.inArray("RICH_MEDIA", specTypeValue) != -1 &&
					$("#rate", "#packageLineItemsForm").val() < 18 && $("#rate", "#packageLineItemsForm").val() != "") {

			$("#confirmSavedDialogDiv").html("<table><tr><td><img src='../images/warning.png'></td><td style='line-height:2; vertical-align: top;'>" 
								+ resourceBundle['label.lineItem.richmedia.confirm'] + "</td></tr></table>");
			$("#confirmSavedDialogDiv").dialog({
				dialogClass: 'alert',
				show: 'slide',
				resizable: false,
				showTitlebar: true,
				modal: true,
				buttons: {
					Yes: function(){
						getDataForTargetingToSave();
						packageLineItemForm.submit();
						$(this).dialog("close");
					},
					No: function(){
						$(this).dialog("close");
					}
				}
			});
		}
		else {
			getDataForTargetingToSave();
			packageLineItemForm.submit();
			$(".ui-dialog-buttonset button:contains('Save')").button("disable");
		}
		}
	};

	packageLineItemsDialog.buttons.Close = function(){
		$('#messageHeaderFinancialDiv').html('');
		$("#STWeightSummary", "#packageLineItemsForm").qtip("destroy");
		$("#pricingCalculationStep", "#packageLineItemsForm").qtip("destroy");
		$(".ui-dialog-buttonset button:contains('Save')").button("enable");
		$("#packageLineItemsForm").dialog("close");
		document.packageLineItems.reset();
		clearCSSErrors('#packageLineItemsForm');
		removeDivCss();
		resetMultiSelectForPackage();
	};

	$("#packageLineItemsForm" ).dialog(packageLineItemsDialog);
	enableDialogButton("packageLineItemsForm");
	removeDivCss();

	$('#packageLineItemsForm #availsCheck').click(function() {
		if(validateLineItemTargeting()){
			populatePackageAvailsFromYieldex();
		}
	});
	
	/**
	 * Register event for Clear Avails
	 */
	$('#packageLineItemsForm #clearAvails').click(function(){
		$('#messageHeaderFinancialDiv').html('');
		var sovFromPackage = $("#sov", "#packageLineItems").val();
		clearPackageAvails();
		if ($("#flight", "#packageLineItems").val() != "" && ($("#endDate", "#packageLineItems").val() == "" 
						|| $("#startDate", "#packageLineItems").val() == "")) {
			$("#sov", "#packageLineItems").val(sovFromPackage);
		}
	});

	/**
	 * Register event for price type change
	 */
	$("#priceType", "#packageLineItemsForm").change(function(){
		onClickPriceTypeRadio($(this).val());
		if ($(this).val() == "CPM" || $(this).val() == "PRE EMPTIBLE" || $(this).val() == "CUSTOM UNIT" || $(this).val() == "ADDED VALUE") {
			if($(this).val() == "PRE EMPTIBLE" || $(this).val() == "ADDED VALUE"){
				$("#specType", "#packageLineItemsForm").val('STANDARD');
				$("#specType", "#packageLineItemsForm").multiselect("refresh");
        	}else{
				if($("#sosProductId", "#packageLineItemsForm").val() != ""){
					var ajaxReq = new AjaxRequest();
	            	ajaxReq.url = "../packagelineitems/getProductCreativesSpecTypeLst.action?productId=" + $("#sosProductId", "#packageLineItemsForm").val();
	            	ajaxReq.success = function(result, status, xhr) {
	            		var specTypes = result.objectMap.gridKeyColumnValue;
	            		if(specTypes == ""){
	            			$("#specType", "#packageLineItemsForm").val('RICH_MEDIA');
	    					$("#specType", "#packageLineItemsForm").multiselect("refresh");
	            		}else{
	            			selectSpecType(specTypes);
	            		}
	            	};
	            	ajaxReq.submit();
				}else{
					$("#specType", "#packageLineItemsForm").val('RICH_MEDIA');
					$("#specType", "#packageLineItemsForm").multiselect("refresh");
				}
        	}
			var impressionTotal = $("#impressionTotal", "#packageLineItemsForm").val();
			var totalInvestment = $("#totalInvestment", "#packageLineItemsForm").val();
			var calculation = 0.0;
			var parseTotalInvestment = totalInvestment.replace(/,/g, '');
			var parseImpressionTotal = impressionTotal.replace(/,/g, '');

			if (parseTotalInvestment >= 0.0 && parseImpressionTotal > 0.0) {
				var parseTotalInvestment = totalInvestment.replace(/,/g, '');
				calculation = (parseTotalInvestment * 1000) / parseImpressionTotal;
				calculation = roundoffto2places(calculation);
				$("#rate", "#packageLineItemsForm").val(formatDecimal(calculation));
			}
			else if (impressionTotal == "" || parseImpressionTotal == 0.0) {
				$("#rate", "#packageLineItemsForm").val(formatDecimal(calculation));
				$("#totalInvestment", "#packageLineItemsForm").val(formatDecimal(calculation));
				$("#impressionTotal", "#packageLineItemsForm").val(formatNumber(calculation));
			}
			$('#packageLineItemsForm #calculateBasePrice').removeAttr("disabled");
			 $('#packageLineItemsForm #calculateBasePrice').removeClass("disabled-btn");
			$("#rateCardPrice", "#packageLineItemsForm").val("");
			 $('#packageLineItemsForm #pricingCalculationStep').show();
		} else {
			$('#packageLineItemsForm #calculateBasePrice').attr("disabled", "disabled");
            $("#rateCardPrice", "#packageLineItemsForm").val("NA");
            $('#packageLineItemsForm #calculateBasePrice').addClass("disabled-btn");
			$('#packageLineItemsForm #pricingCalculationStep').hide();
	        	$($("#specType", "#packageLineItemsForm")).val('STANDARD');
	    		$($("#specType", "#packageLineItemsForm")).multiselect("refresh");
		}
		if ($("#rateCardPrice" , "#lineItemPricingContainer").val() == "" || $("#rateCardPrice", "#lineItemPricingContainer").val() == "NA" || $("#rateCardPrice", "#lineItemPricingContainer").val() == "Not Defined") {
            	$("#rateX", "#lineItemPricingContainer").attr('checked', false).attr('disabled', true);
       	}
    });
    
    $("#ImpressionsCount", "#packageLineItemsForm").change(function(){
        onClickPackageImpressionCountInfo($(this).val());
    });
	
	jQuery( "#packageTab" ).tabs();
	
	/**
	 * Register datetime plugin for package valid from & valid to
	 */
	var dates = jQuery( "#ManagePackage #validFrom, #ManagePackage #validTo" ).datepicker({
		autoSize: true,
		showOn: "both",
		buttonText: "Select date",
		buttonImage: '../images/calendar.gif',
		buttonImageOnly: true,
		numberOfMonths: 3,
		onSelect: function( selectedDate ) {
			date = $.datepicker.parseDate($( this ).data( "datepicker" ).settings.dateFormat || $.datepicker._defaults.dateFormat,selectedDate, $( this ).data( "datepicker" ).settings );
		}
	});

	/**
	 * Package reset button click event
	 */
	jQuery("#packageResetDataBtn").click(function(){
		clearCSSErrors('#ManagePackage');
		if (jQuery("#package-tabs-1-lbl a").text() == resourceBundle['tab.generic.addNewPackage']) {
			document.ManagePackage.reset();
			$('button:first-child', '.ui-dialog-buttonset').focus();
		}
		else {
			var gsr = jQuery("#packageMasterGrid").jqGrid('getGridParam', 'selrow');
			if (gsr) {
				jQuery("#packageMasterGrid").jqGrid('GridToForm', gsr, "#ManagePackage");
			}
			else {
				alert("Please select Row");
			}
		}
		jQuery("textarea", "#ManagePackage").change();
	});

	$("#startFromReset", "#packageLineItems").bind("click", function(event, ui){
		$("#startDate", "#packageLineItems").val("");
		var sovFromPackage = $("#sov", "#packageLineItems").val();
		clearPackageAvails();
		if ($("#flight", "#packageLineItems").val() != "" && ($("#endDate", "#packageLineItems").val() == "" ||
				$("#startDate", "#packageLineItems").val() == "")) {
			$("#sov", "#packageLineItems").val(sovFromPackage);
		}
		enableDisableCalendar();
		$("#sor", "#lineItemTargeting").val('');
		if ($("#priceType", "#packageLineItemsForm").val() == "FLAT RATE") {
            $("#rateCardPrice", "#packageLineItemsForm").val("NA");
            }else{
            $("#rateCardPrice", "#packageLineItemsForm").val("");	
            }
	});

	$("#endFromReset", "#packageLineItems").bind("click", function(event, ui){
		$("#endDate", "#packageLineItems").val("");
		var sovFromPackage = $("#sov", "#packageLineItems").val();
		clearPackageAvails();
		if ($("#flight", "#packageLineItems").val() != "" && ($("#endDate", "#packageLineItems").val() == "" ||
				$("#startDate", "#packageLineItems").val() == "")) {
			$("#sov", "#packageLineItems").val(sovFromPackage);
		}
		$("#sor", "#lineItemTargeting").val('');
		enableDisableCalendar();
		if ($("#priceType", "#packageLineItemsForm").val() == "FLAT RATE") {
            $("#rateCardPrice", "#packageLineItemsForm").val("NA");
            }else{
            $("#rateCardPrice", "#packageLineItemsForm").val("");	
            }
	});

	$("#validFromReset").bind("click", function(event, ui){
		$("#ManagePackage #validFrom").val("");
	});

	$("#validToReset").bind("click", function(event, ui){
		$("#ManagePackage #validTo").val("");
	});

	var packageFormManager = new FormManager();
	packageFormManager.gridName = 'packageMasterGrid';
	packageFormManager.formName = 'ManagePackage';

	/**
	 * Package save button click event
	 */
	jQuery("#packageSaveDataBtn").click(function() {
		var salesCategorieNames = $("#packageSalescategory", "#ManagePackage").multiselect("getChecked").map(function(){
			return $(this).next().text();
		}).get().join(',');
		var salesCategorieIds = $("#packageSalescategory", "#ManagePackage").multiselect("getChecked").map(function(){
			return this.value;
		}).get().join(',');
	    $('#packageSalescategoryName',"#ManagePackage").val(salesCategorieNames);
		var packageDetailForm = new JQueryAjaxForm(packageFormManager);		
		packageDetailForm.doCustomProcessingAfterFormSucsesJson = function(jsonResponse, XMLHTTPRequest){
			jQuery("#runtimeDialogDiv").model({theme: 'Success', message: resourceBundle['message.generic.packageSavedSuccessfully']});
			var packageId = jQuery("#ManagePackage #packageId").val();
			if(packageId == 0){
				reloadJqGridAfterAddRecord(packageFormManager.gridName, "packageId");
			} else {
				var dateArr = jQuery("#ManagePackage #validTo").val().split("/");
				var enteredDate = new Date(dateArr[2], dateArr[0]-1, dateArr[1]);
				var today = new Date();
				//Set time to zero
				today.setHours(0,0,0,0);
				if (today > enteredDate) {
					$("#" + packageId , "#packageMasterGrid").find("td").addClass("expire-package");
					jQuery("#ManagePackage #expired").val("Yes");
				} else {
					$("#" + packageId , "#packageMasterGrid").find("td").removeClass("expire-package");
					jQuery("#ManagePackage #expired").val("No");
				}
				jQuery.logThis(jQuery("#ManagePackage #breakable option:selected").text());
				jQuery("#ManagePackage #packageOwner").val(jQuery("#ManagePackage #ownerId option:selected").text());
				jQuery("#ManagePackage #breakableStr").val(jQuery("#ManagePackage #breakable option:selected").text());
				jQuery("#packageMasterGrid").jqGrid("FormToGrid", packageId, "#ManagePackage");
				if(salesCategorieNames==''){
					jQuery("#packageMasterGrid").setCell($('#packageId',"#ManagePackage").val(), 'packageSalescategoryName');
					jQuery("#packageMasterGrid").setCell($('#packageId',"#ManagePackage").val(), 'packageSalescategory');
				}else{
					jQuery("#packageMasterGrid").setCell($('#packageId',"#ManagePackage").val(), 'packageSalescategoryName',salesCategorieNames);
					jQuery("#packageMasterGrid").setCell($('#packageId',"#ManagePackage").val(), 'packageSalescategory',salesCategorieIds);
				}
			}
		};
		packageDetailForm.submit();
	});

	setMultiSelectSalesTargetId();
	setSpecTypeForProposalForPackage();

	$("#rate", "#packageLineItemsForm").blur(function(){
		var rate = $("#rate", "#packageLineItemsForm").val();
		var impressionTotal = $("#impressionTotal", "#packageLineItemsForm").val();
		var totalInvestment = $("#totalInvestment", "#packageLineItemsForm").val();
		var calculation = 0.0;
		var priceType = $("#packageLineItemsForm #priceType").val();
		if (rate != "" && (priceType == "CPM" || priceType == "PRE EMPTIBLE" || priceType == "CUSTOM UNIT")) {
			var parseRate = rate.replace(/,/g, '');
			if (impressionTotal != "" && totalInvestment == 0.0) {
				var parseImpressionTotal = impressionTotal.replace(/,/g, '');
				calculation = (parseRate * parseImpressionTotal) / 1000;
				calculation = roundoffto2places(calculation);
				var totalInvestmentObject = $("#totalInvestment", "#packageLineItemsForm");
				totalInvestmentObject.val(formatDecimal(calculation));
			} 
			else if (totalInvestment != "" && impressionTotal == "" && parseRate > 0.0) {
				var parseTotalInvestment = totalInvestment.replace(/,/g, '');
				calculation = (parseTotalInvestment * 1000) / parseRate;
				calculation = parseInt(calculation);
				var ImpressionTotalObject = $("#impressionTotal", "#packageLineItemsForm");
				ImpressionTotalObject.val(formatNumber(calculation));
				calculateSOVFromPackage();
			} 
			else if (totalInvestment != "" && impressionTotal != "") {
				var parseImpressionTotal = impressionTotal.replace(/,/g, '');
				calculation = (parseRate * parseImpressionTotal) / 1000;
				calculation = roundoffto2places(calculation);
				var totalInvestmentObject = $("#totalInvestment", "#packageLineItemsForm");
				totalInvestmentObject.val(formatDecimal(calculation));
			}
		}
	});

	$("#impressionTotal", "#packageLineItemsForm").blur(function(){
		var rate = $("#rate", "#packageLineItemsForm").val();
		var impressionTotal = $("#impressionTotal", "#packageLineItemsForm").val();
		var totalInvestment = $("#totalInvestment", "#packageLineItemsForm").val();
		var calculation = 0.0;
		var priceType = $("#packageLineItemsForm #priceType").val();
		if (impressionTotal != "" && (priceType == "CPM" || priceType == "PRE EMPTIBLE" || priceType == "CUSTOM UNIT")) {
			var parseImpressionTotal = impressionTotal.replace(/,/g, '');
			if (rate != "" && totalInvestment == 0.0) {
				var parseRate = rate.replace(/,/g, '');
				calculation = (parseRate * parseImpressionTotal) / 1000;
				calculation = roundoffto2places(calculation);
				var totalInvestmentObject = $("#totalInvestment", "#packageLineItemsForm");
				totalInvestmentObject.val(formatDecimal(calculation));
			}
			else if (totalInvestment != "" && rate == 0.0 && parseImpressionTotal > 0.0) {
				var parseTotalInvestment = totalInvestment.replace(/,/g, '');
				calculation = (parseTotalInvestment * 1000) / parseImpressionTotal;
				calculation = roundoffto2places(calculation);
				var RateObject = $("#rate", "#packageLineItemsForm");
				RateObject.val(formatDecimal(calculation));
			}
			else if (totalInvestment != "" && rate != "") {
				var parseRate = rate.replace(/,/g, '');
				calculation = (parseRate * parseImpressionTotal) / 1000;
				calculation = roundoffto2places(calculation);
				var totalInvestmentObject = $("#totalInvestment", "#packageLineItemsForm");
				totalInvestmentObject.val(formatDecimal(calculation));
			}
		}
		calculateSOVFromPackage();
	});

	$("#totalInvestment", "#packageLineItemsForm").blur(function(){
		var rate = $("#rate", "#packageLineItemsForm").val();
		var impressionTotal = $("#impressionTotal", "#packageLineItemsForm").val();
		var totalInvestment = $("#totalInvestment", "#packageLineItemsForm").val();
		var calculation = 0.0;
		var priceType = $("#packageLineItemsForm #priceType").val();
		if (totalInvestment != "" && (priceType == "CPM" || priceType == "PRE EMPTIBLE" || priceType == "CUSTOM UNIT")) {
			var parseTotalInvestment = totalInvestment.replace(/,/g, '');
			var parseImpressionTotal = impressionTotal.replace(/,/g, '');
			var parseRate = rate.replace(/,/g, '');
			if (rate != "" && impressionTotal == "" && parseRate > 0.0) {
				calculation = (parseTotalInvestment * 1000) / parseRate;
				calculation = parseInt(calculation);
				var ImpressionTotalObject = $("#impressionTotal", "#packageLineItemsForm");
				ImpressionTotalObject.val(formatNumber(calculation));
				calculateSOVFromPackage();
			}
			else if (impressionTotal != "" && rate == 0.0 && parseImpressionTotal > 0.0) {
				calculation = (parseTotalInvestment * 1000) / parseImpressionTotal;
				calculation = roundoffto2places(calculation);
				var RateObject = $("#rate", "#packageLineItemsForm");
				RateObject.val(formatDecimal(calculation));
			}
			else if (rate != "" && impressionTotal != "") {
				if (totalInvestmentValueChangedForPackage()) {
					$('input:radio[name=goalSeekValue]:nth(0)').attr('checked', true);
					$("#chooseGoalSeek").dialog("open");
				}
			}
		}
	});

	$("#totalInvestment", "#packageLineItemsForm").focus(function(){
		 $("#oldInvestment", "#packageLineItemsForm").val(formatDecimal($(this).val()));
	});

	$("#sov", $("#packageLineItemsForm")).blur(function(){
		var sov = $("#sov", '#packageLineItemsForm').val();
		var parseSOV = sov.replace(/,/g, '');
		var totalPossibleImpressions = $("#totalPossibleImpressions", '#packageLineItemsForm').val();
		var parseTotPossibleImpression = totalPossibleImpressions.replace(/,/g, '');

		if (!isNaN(parseSOV) && !isNaN(parseTotPossibleImpression) && parseSOV != 0 && parseTotPossibleImpression != 0) {
			var impressionTotal = (parseSOV * parseTotPossibleImpression) / 100;
			impressionTotal = parseInt(impressionTotal);
			var impressionTotalObject = $("#impressionTotal", '#packageLineItemsForm');
			impressionTotalObject.val(formatNumber(impressionTotal));
		}
		
		var priceTypeVal = $('#priceType', '#packageLineItemsForm').val();
		if (priceTypeVal == "CPM" || priceTypeVal == "PRE EMPTIBLE" || priceTypeVal == "CUSTOM UNIT") {
			var rate = $("#rate", '#packageLineItemsForm').val();
			var parseRate = rate.replace(/,/g, '');
			var impressionTotal = $("#impressionTotal", '#packageLineItemsForm').val();
			var parseImpressionTotal = impressionTotal.replace(/,/g, '');
			if (!isNaN(parseRate) && !isNaN(parseImpressionTotal)) {
				var totInvestment = (parseRate * parseImpressionTotal) / 1000;
				totInvestment = roundoffto2places(totInvestment);
				var totInvestmentObject = $("#totalInvestment", '#packageLineItemsForm');
				totInvestmentObject.val(formatDecimal(totInvestment));
			}
		}
	});	
	
	$('#sosTarTypeId').change(function(){
		fillTargetTypeElementForLineItem();
	});
	
	showHideActionIcons();
});

/**
 * 
 * @param {Object} cl
 */
function editLineItemsGridRow(cl){
	$("#rateXInfo", "#packageLineItemsForm").show();
	createSTWeightSummaryQtip();
	createPriceCalSummaryQtip();
	createProductCreativeSummaryQtip();
	resetMultiSelectForPackage();
	clearCSSErrors('#packageLineItemsForm');
	removeDivCss();
	$('#packageLineItemsTable').jqGrid('setSelection', cl);
	$("#packageLineItemsForm").dialog("open");
	$("#packageLineItemsForm #lineItemID").val(cl);
	$("#flagForOnChange", "#packageLineItemsForm").val('false');
	$('button:first-child', '.ui-dialog-buttonset').focus();
	getLineItemConsolidatedData(cl);	
	var prod_active = $("#packageLineItemsTable").jqGrid('getCell', cl, 'product_active');
	var salesTarget_active = $("#packageLineItemsTable").jqGrid('getCell', cl, 'salesTarget_active');
	var packageId = jQuery("#ManagePackage #packageId").val();
	$("#packageLineItemsForm #packageId").val(packageId);	
	$('#messageHeaderFinancialDiv').html('');
	$("#reserveChkBoxContainer", "#lineItemCalendarContainer").hide();
	$("#reservationExpiryDate").hide();
}

/**
 * Show and hide based on price type
 * @param {Object} priceType
 */
function onClickPriceTypeRadio(priceType){
	if (priceType == "CPM" || priceType == "PRE EMPTIBLE" || priceType == "CUSTOM UNIT") {
		$('#packageLineItemsForm #totalInvestment').attr('readonly', false);
		$('#packageLineItemsForm #totalInvestment').removeClass('input-textbox-readonly');
		$('#packageLineItemsForm #rate').attr('readonly', false);
		$('#packageLineItemsForm #rate').removeClass('input-textbox-readonly');
		$('#packageLineItemsForm #tr_rate').show();
		$('#packageLineItemsForm #calculateBasePrice').removeAttr("disabled");
        $('#packageLineItemsForm #calculateBasePrice').removeClass("disabled-btn");
        $('#packageLineItemsForm #pricingCalculationStep').show();
	} 
	else if (priceType == "ADDED VALUE") {
		$('#packageLineItemsForm #rate').val(formatDecimal("0.00"));
		$('#packageLineItemsForm #totalInvestment').val(formatDecimal("0.00"));
		$('#packageLineItemsForm #rate').attr('readonly', true);
		$('#packageLineItemsForm #rate').addClass('input-textbox-readonly');
		$('#packageLineItemsForm #totalInvestment').attr('readonly', true);
		$('#packageLineItemsForm #totalInvestment').addClass('input-textbox-readonly');
		$('#packageLineItemsForm #tr_rate').show();
		$('#packageLineItemsForm #calculateBasePrice').removeAttr("disabled");
        $('#packageLineItemsForm #calculateBasePrice').removeClass("disabled-btn");
        $('#packageLineItemsForm #pricingCalculationStep').show();
	} 
	else if (priceType == "FLAT RATE") {
		$('#packageLineItemsForm #tr_rate').hide();
		$('#packageLineItemsForm #rate').val("");
		$('#packageLineItemsForm #totalInvestment').attr('readonly', false);
		$('#packageLineItemsForm #totalInvestment').removeClass('input-textbox-readonly');
		$('#packageLineItemsForm #calculateBasePrice').attr("disabled", "disabled");
        $('#packageLineItemsForm #calculateBasePrice').addClass("disabled-btn");
        $('#packageLineItemsForm #pricingCalculationStep').hide();
	}
}

/**
 * Reset hidden fields
 */
function resetHiddenFields(){
	$("#productName", "#packageLineItemsForm").val("");
	$("#displayFlight").val("");
	$('#packageLineItemsForm #lineItemID').val("");
	$("#sosSalesTargetId").val("");
}

/**
 *  check not a number 
 */
function checkIsNaN(){
	var totImpression = $("#impressionTotal", "#packageLineItemsForm").val();
	var parseTotImpression = totImpression.replace(/,/g, '');
	var netCPM = $("#rate", "#packageLineItemsForm").val();
	var parseNetCPM = netCPM.replace(/,/g, '');
	var totalInvestment = $("#totalInvestment", "#packageLineItemsForm").val();
	var parseTotalInvestment = totalInvestment.replace(/,/g, '');

	if (isNaN(parseTotImpression)) {
		$("#impressionTotal", "#packageLineItemsForm").val(-1);
	}
	if (isNaN(parseNetCPM)) {
		$("#rate", "#packageLineItemsForm").val(-1);
	}
	if (isNaN(parseTotalInvestment)) {
		$("#totalInvestment", "#packageLineItemsForm").val(-1);
	}
}



/**
 * 
 * @param {Object} salestargetId
 * @param {Object} prodObj
 */
function getProductSalesTargetPlacementForPackageLineItem(salestargetId, prodObj){
	var productId = jQuery(prodObj).val();
	if (salestargetId == null || salestargetId.length < 1 || productId == "" || productId == null) {
		$("#placementName", "#packageLineItemsForm").val("");
	}
	else {
		var lineItemID = $("#lineItemID", "#packageLineItemsForm").val();
		var ajaxReq = new AjaxRequest();
		ajaxReq.url = "../manageProduct/getLineItemProductPlacement.action?productId=" + productId + "&sosSalesTargetId=" + salestargetId + "&lineItemID=" + lineItemID;
		ajaxReq.success = function(result, status, xhr){
			$.each(result, function(i, item){
				$("#placementName", "#packageLineItemsForm").val(item);
			});
		};
		ajaxReq.submit();
	}
}

/**
 * 
 * @param {Object} flightType
 */
function onClickPackageFlightInfo(flightType) {
	if (flightType == "Flight Information") {
		$('#packageLineItemsForm #flightInfoText').show();
		$('#packageLineItemsForm #flightDate').hide();
		$('#packageLineItemsForm #startDate').val("");
		$('#packageLineItemsForm #endDate').val("");
	}
	else if (flightType == "Flight Dates") {
		$('#packageLineItemsForm #flightInfoText').hide();
		$('#packageLineItemsForm #flight').val("");
		$('#packageLineItemsForm #flightDate').show();
	}
}

/**
 * 
 * @param {Object} ImpressionCount
 */
function onClickPackageImpressionCountInfo(ImpressionCount){
	if (ImpressionCount == "IMPRESSIONS") {
		$('#packageLineItemsForm #ImpressionsCountInfo').show();
		$('#packageLineItemsForm #ImpressionsCountSOV').hide();
		$('#packageLineItemsForm #sov').val("");
	}
	else if (ImpressionCount == "SOV") {
		$('#packageLineItemsForm #ImpressionsCountInfo').hide();
		$('#packageLineItemsForm #impressionTotal').val("");
		$('#packageLineItemsForm #ImpressionsCountSOV').show();
	}
}


/**
 * Clear totalPossibleImpressions and avails on change of sales Target, Product start date and end date 
 */
function clearPackageAvails(){
	$('#totalPossibleImpressions', '#packageLineItemsForm').val("");
	$("#avails", '#packageLineItemsForm').val("");
	$("#availsPopulatedDate", '#packageLineItemsForm').val("");
	$("#sov", '#packageLineItemsForm').val("");
}

/**
 *  Check avails validation before fetch avails
 * @param {Object} availsPurpose
 */
function populatePackageAvailsFromYieldex(){ 
	$('#messageHeaderFinancialDiv').html('');
	removeDivCss();
	if ($("#lineItemID", "#packageLineItemsForm").val() == "") {
		$("#lineItemID", "#packageLineItemsForm").val(0);
	}
	addPackageFetchingAvailsText();
	$("#availProgress").dialog("open");
	var lineItemDetailFormsManager = new FormManager();
	lineItemDetailFormsManager.formName = 'packageLineItems';
	var lineItemDetailForm = new JQueryAjaxForm(lineItemDetailFormsManager);
	$('#packageLineItemsForm #packageLineItems').attr("action", "../avails/populateAvailsFromYieldex.action");
	lineItemDetailForm.doCustomProcessingAfterFormSucsesJson = function(jsonResponse, XMLHTTPRequest){
	var yieldexError = jsonResponse.objectMap.yieldexError;
	if (yieldexError != "") {
		$('#messageHeaderFinancialDiv', '#packageLineItemsForm').html(resourceBundle['error.generic.yieldex.error']+yieldexError).addClass('error');
		$('#messageHeaderFinancialDiv', '#packageLineItemsForm').show();
	} else {
		var summary = jsonResponse.objectMap.gridKeyColumnValue;
       	var availsPopulatedDate = jsonResponse.objectMap.gridKeyColumnAdditionalValue;
        
        if (summary.available != 0) {
           var available = summary.available.replace(/,/g, '');
           $('#avails', '#packageLineItemsForm').val(formatNumber(available));
        }
        else {
            var available = summary.available;
            $('#avails', '#packageLineItemsForm').val(formatNumber(available));
        }
        
        if (summary.capacity != 0) {
           var capacity = summary.capacity.replace(/,/g, '');
           $('#totalPossibleImpressions', '#packageLineItemsForm').val(formatNumber(capacity));
        }
        else {
           var capacity = summary.capacity;
           $('#totalPossibleImpressions', '#packageLineItemsForm').val(formatNumber(capacity));
        }

        $('#availsPopulatedDate', '#packageLineItemsForm').val(availsPopulatedDate);
        var offeredImpression = $('#impressionTotal', '#packageLineItemsForm').val().replace(/,/g, '');
		var capacity = (summary.capacity).replace(/,/g, '');
	    if (offeredImpression != 0 && capacity != 0) {
            var sov = (offeredImpression * 100) / capacity;
            sov = roundoffto2places(sov);
            $("#sov", '#packageLineItemsForm').val(formatDecimal(sov));
        }else if(capacity == 0){
			$("#sov", '#packageLineItemsForm').val("");
		}		
	}
	//Close dialog box and show loading image   
    $("#availProgress").dialog("close");	
		 
};
	lineItemDetailForm.doCustomProcessingAfterValidationFailedJson = function(jsonResponse, XMLHTTPRequest){
		var errorArray = jsonResponse.errorList;
		jQuery.each(errorArray, function(i, error){
			if (error.field == "startDate" || error.field == "endDate" || error.field == "sosSalesTargetId_custom") {
				//error.field = "packageRenderFragmentId" + error.field;
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
	lineItemDetailForm.error = function(XMLHTTPRequest, textStatus, errorThrown){
		 $("#availProgress").dialog("close");
        handlePackageYieldexAvailsExceptionOnServer(XMLHTTPRequest);
	};
	getDataForTargetingToSave();
	 $('#avails', '#packageLineItemsForm').val('');
	 $('#totalPossibleImpressions', '#packageLineItemsForm').val('');
	 $('#availsPopulatedDate', '#packageLineItemsForm').val('');
	lineItemDetailForm.submit();
}

/**
 * Show error message for yieldex exception
 * @param {Object} XMLHTTPRequest
 */
function handlePackageYieldexAvailsExceptionOnServer(XMLHTTPRequest){
	var errorObj = eval('(' + XMLHTTPRequest.responseText + ')').errorObject;
	jQuery("#runtimeDialogDiv").model({theme: 'Error', autofade: false, message: errorObj.errorMessage});
}

/**
 * Check required attributes for avails
 */
function isRequiredAttributeSetForAvailsFromPackage(){
	var startDate = $("#startDate", "#packageLineItems").val();
	var endDate = $("#endDate", "#packageLineItems").val();
	var sosSalesTargetId = $("#packageLineItemsForm #sosSalesTargetId").val();
	var sosProductId = $("#packageLineItemsForm #sosProductId").val();
	if (startDate != '' && endDate != '' && (sosSalesTargetId != '' || sosSalesTargetId != null) && (sosProductId != '' || sosProductId != null)) {
		return true;
	}
	else {
		return false;
	}
}

/**
 * Sort data for select box based on display value (Due to IE and chrome issue)
 * @param result
 * @param select
 * @return
 */
function sortListForPackage(result, select, childSaleTargetIds){
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

/**
 * Set sales targets option then multiselect refresh
 * @param {Object} select
 * @param {Object} actionURL
 */
function dynaLoadSalesTargetsForPackage(select, actionURL){
	var ajaxReq = new AjaxRequest();
	ajaxReq.url = actionURL;
	ajaxReq.cache = false;
	ajaxReq.success = function(result, status, xhr){
		$("option", select).remove();
		sortListForPackage(result.objectMap.gridKeyColumnValue, select, result.objectMap.childSaleTargetIds);
		 if ($("#lineItemType_s", "#lineItemTargeting").is(':checked')) {
			 $(select).multiselect("refresh");			 
		} else{
			$(select).trigger("change");
			$(select).select2('data', null);
		}
		enableDisableCalendar();
		enableLineItemDefaults();
	};
	ajaxReq.submit();
}

/**
 * Select multi select option and set other value   
 */
function setMultiSelectSalesTargetId(){
    var checkOnChange = false;
    $("#sosSalesTargetId", "#packageLineItemsForm").multiselect({
        selectedList: 1,
        uncheckAll: function(){
            clearPackageAvails();
            enableDisabledChildSaleTargets();
            $('#packageLineItemsForm #placementName').val("");
            if ($("#priceType", "#packageLineItemsForm").val() != "FLAT RATE") {
                $("#rateCardPrice", "#packageLineItemsForm").val("");
                $("#discountPercentage","#lineItemFinancialFieldsContainer").val("");
            } else {
                $("#rateCardPrice", "#packageLineItemsForm").val("NA");
            }
			if ($("#rateCardPrice" , "#lineItemPricingContainer").val() == "" || $("#rateCardPrice", "#lineItemPricingContainer").val() == "NA" || $("#rateCardPrice", "#lineItemPricingContainer").val() == "Not Defined") {
            	$("#rateX", "#lineItemPricingContainer").attr('checked', false).attr('disabled', true);
       		}
        },
        click: function(event, ui){
            enableDisabledChildSaleTargets();
			checkOnChange = true;
            var salesTargetIds = $(this).multiselect("getChecked").map(function(){
                return this.value;
            }).get();
            
            
            if (salesTargetIds == "" && salesTargetIds.length <= 0) {
                clearPackageAvails();
                $('#packageLineItemsForm #placementName').val("");
                if ($("#priceType", "#packageLineItemsForm").val() != "FLAT RATE") {
                    $("#rateCardPrice", "#packageLineItemsForm").val("");
                    $("#discountPercentage","#lineItemFinancialFieldsContainer").val("");
                } else {
                    $("#rateCardPrice", "#packageLineItemsForm").val("NA");
                }
            } else if ($("#flagForOnChange", "#packageLineItemsForm").val() == 'true') {
                    checkOnChange = false;
                    //Get product sales target placement name
                    if ($("#priceType", "#packageLineItemsForm").val() != "FLAT RATE") {
                        $("#rateCardPrice", "#packageLineItemsForm").val("");
                        $("#discountPercentage","#lineItemFinancialFieldsContainer").val("");
                    } else {
                        $("#rateCardPrice", "#packageLineItemsForm").val("NA");
                    }
                    //Clear avails on selecting date  
                    clearPackageAvails();
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
 * Fetch sales target from selected product in package
 * @param {Object} selectedProduct
 */
function setsosSalesTargetIdValue(selectedProduct){
    var salesTargetIdCombo = $("#sosSalesTargetId", "#packageLineItemsForm");
    var actionURL = "../managepackage/getSalesTargetForProduct.action?productID=" + selectedProduct;
    dynaLoadSalesTargetsForPackage(salesTargetIdCombo, actionURL); 
}

/**
 * Dynamic load sales target based on product from package
 * @param {Object} select
 * @param {Object} actionURL
 */
function dynaLoadProductForPackage(select, actionURL){
    var selectedProduct = $(select).val();
    var ajaxReq = new AjaxRequest();
    ajaxReq.url = actionURL;
    ajaxReq.cache = false;
    ajaxReq.success = function(result, status, xhr){
        $("option", select).remove();
        $(select).removeClass("input-textbox-readonly").append("<option value=''>--Select--</option>");
        sortList(result.objectMap.gridKeyColumnValue, select);
        $(select).val(selectedProduct);
        if ($(select).val() == "") {
            clearPackageAvails();
        }
    };
    ajaxReq.submit();
}


/**
 * Select, marked sales target as checked from grid
 * @param {Object} selectedGridRow
 */
function selectSalesTargetFromGridForPackage(selectedGridRow){
    var sosSalesTargetId = $("#packageLineItemsTable").jqGrid('getCell', selectedGridRow, 'sosSalesTargetId').split(",");
    var salesTargetObj = $("#sosSalesTargetId", "#packageLineItemsForm");
    if ($("#lineItemType_r", "#lineItemTargeting").is(':checked') || $("#lineItemType_e", "#lineItemTargeting").is(':checked')) {
		$("#sosSalesTargetId","#lineItemTargeting").val(sosSalesTargetId[0]);
		$('#lineItemTargeting #sosSalesTargetId').trigger("change");
	} else{
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

/**
 * Remove all option from multi select 
 * */
function resetMultiSelectForPackage(){
    var select = $("#sosSalesTargetId", "#packageLineItemsForm");
    $('option', select).remove();
    select.multiselect('refresh');
}

/**
 * Disable child sale targets
 */
function enableDisabledChildSaleTargets(){
    var salesTargetIdCombo = $("#sosSalesTargetId", "#packageLineItemsForm");
    
    $(salesTargetIdCombo).multiselect("widget").find(":checkbox").each(function(){
        $(this).attr("disabled", false);
    });
    
    var salesTargetIds = $(salesTargetIdCombo).multiselect("getChecked").map(function(){
        return this.value;
    }).get();
    
    if (salesTargetIds.length > 0) {
        for (var i = 0; i < salesTargetIds.length; i++) {
            enableDisableSalesTarget(salesTargetIdCombo, getChildSalesTarget(salesTargetIdCombo, salesTargetIds[i]));
        }
    }
}

/**
 * calculate SOV value based on impression total and total possible impression
 */
function calculateSOVFromPackage(){
    var totImpression = $("#impressionTotal", '#packageLineItemsForm').val();
    var parseTotImpression = totImpression.replace(/,/g, '');
    var totPossibleImpression = $("#totalPossibleImpressions", '#packageLineItemsForm').val();
    var parseTotPosibleImpression = totPossibleImpression.replace(/,/g, '');
    
    if (!isNaN(parseTotImpression) && !isNaN(parseTotPosibleImpression) && parseTotImpression != 0 && parseTotPosibleImpression != 0) {
        var sov = ((parseTotImpression * 100) / parseTotPosibleImpression);
        sov = roundoffto2places(sov);
        var sovObject = $("#sov", '#packageLineItemsForm');
        sovObject.val(formatDecimal(sov));
    }
}

/**
 * Uncheck and disable check box and option of multiselect  
 * @param {Object} salesTargetIdCombo
 * @param {Object} salesTargetIds
 */
function enableDisableSalesTarget(salesTargetIdCombo, salesTargetIds){
    if (salesTargetIds.length > 0) {
        for (var i = 0; i < salesTargetIds.length; i++) {
            enableDisableSalesTarget(salesTargetIdCombo, getChildSalesTarget(salesTargetIdCombo, salesTargetIds[i]));
            $("#sosSalesTargetId option[value=" + salesTargetIds[i] + "]").attr('selected', false);
            $(salesTargetIdCombo).multiselect("widget").find(":checkbox[value=" + salesTargetIds[i] + "]").attr('checked', false).attr('disabled', 'disabled');
        }
    }
    else {
        return;
    }
}

/**
 * Return array of sales target
 * @param {Object} salesTargetIdCombo
 * @param {Object} salesTargetId
 */
function getChildSalesTarget(salesTargetIdCombo, salesTargetId){
    return $(salesTargetIdCombo).find("option[parent='" + salesTargetId + "']").map(function(){
        return this.value;
    });
}

/**
 * Remove class of custom div
 */
function removeDivCss(){
    $("#sosSalesTargetId_custom", "#packageLineItemsForm").removeClass("errorElement").removeClass("errortip").removeAttr("oldtitle");
    $("#sosSalesTargetId_custom", "#packageLineItemsForm").qtip("destroy");
    $("#specType_custom", "#packageLineItemsForm").removeClass("errorElement").removeClass("errortip").removeAttr("oldtitle");
    $("#specType_custom", "#packageLineItemsForm").qtip("destroy");
}

/**
 * Function fills the sequence No while creating new Line Item
 */
function fillSequenceNoForPackageLineItem(){
    var ajaxReq = new AjaxRequest();
    var packId = $("#packageId", "#packageLineItemsForm").val();
    ajaxReq.url = '../packagelineitems/getNextSequenceNo.action?packageId=' + packId;
    ajaxReq.success = function(result, status, xhr){
        $('#lineItemSequence', '#packageLineItemsForm').val(result.objectMap.gridKeyColumnValue);
    };
    ajaxReq.submit();
}

/**
 * Function checks whether investment value is changed
 */
function totalInvestmentValueChangedForPackage(){
    var old_Investment = $("#oldInvestment", "#packageLineItemsForm").val();
    var new_Investment = $("#totalInvestment", '#packageLineItemsForm').val();
    if (old_Investment == new_Investment) {
        return false;
    }
    else {
        return true;
    }
}

/**
 * fetch Spec Type from selected product
 * @param {Object} gsr
 */
function setSpecTypeForPackage(gsr){
	if (gsr) {
		selectSpecTypeFromGridForPackage(gsr);
	} else {
		/**
		 * Set specType value by RICH_MEDIA value
		 */
		var specTypeObj = $("#specType", "#packageLineItemsForm");
		$(specTypeObj).multiselect("uncheckAll");
		$(specTypeObj).multiselect("widget").find(":checkbox").each(function() {
			if ($(this).val() == "RICH_MEDIA") {
				this.click();
			}
		});
	}
};

function setSpecTypeForProposalForPackage(){
	$("#specType", "#packageLineItemsForm").multiselect({
		selectedList: 1,
		header: false,
		height: "auto",
		position: {
	      my: 'bottom',
	      at: 'top'
		}
	}).multiselectfilter();
};

function selectSpecTypeFromGridForPackage(selectedGridRow) {
	var specTypeObj = $("#specType", "#packageLineItemsForm");
	$(specTypeObj).multiselect("uncheckAll");
	var specTypeArray = $("#packageLineItemsTable").jqGrid('getCell', selectedGridRow, 'specType').split(",");
	$(specTypeObj).multiselect("widget").find(":checkbox").each(function(){
		for(var i = 0; i < specTypeArray.length; i++) {
		    if ($(this).val() == specTypeArray[i]) {
				this.click();
                break;
           }
		}
    });
}

function addPackageFetchingAvailsText(){
	$('#availProgress').empty('');
	var currentDate = $.datepicker.formatDate('mm/dd/yy', new Date());
	var selectedDate = $("#startDate","#packageLineItems").val(); 
	if(Date.parse(selectedDate) >= Date.parse(currentDate)){
		$('#availProgress').html('<p style="padding:8px;margin:10px 0 0"><span class="fetching-avails">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><span>'+resourceBundle['label.fetching.avails']+'</span></p>');
	}else{
		$('#availProgress').html('<p style="padding:8px;margin:10px 0 0"><span class="warning-dialog">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><span style="white-space:nowrap">'+resourceBundle['label.fetching.past.date.avails']+'</span></p>');
	}
}

function showPackageSTWeightSummary(){
    $("#STWeightSummary", "#packageLineItemsForm").qtip("option", "content.ajax.url", "");
    $("#STWeightSummary", "#packageLineItemsForm").qtip("option", "content.text", "<img src='../images/ajax-loader.gif' height='20px' width='20px' alt='Loading...' />");
    var packageSalesTargetId = $("#sosSalesTargetId", "#packageLineItemsForm").multiselect("getChecked").map(function(){
        return this.value;
    }).get();
    var url = "../proposalWorkflow/getSTWeightSummary.action?sosSalesTargetId=" + packageSalesTargetId;
    $("#STWeightSummary", "#packageLineItemsForm").qtip("option", "content.ajax.url", url);
}

function createSTWeightSummaryQtip(){
	$("#STWeightSummary", "#packageLineItemsForm").qtip({
        content: {
            // Set the text to an image HTML string with the correct src URL to the loading image you want to use
            text: '<img src="../images/ajax-loader.gif" height="20px" width="20px" alt="Loading..." />',
            ajax: {
                url: '',
                once: false
            },
            title: {
                text: '<div style="height:18px;"><span style="float:left;"></span><span style="padding:4px 0 0 3px; float:left;">' + resourceBundle['label.salesTarget.weight'] + '</span></div>',
                button: true
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
}

function packageLineItemsAvailStatus(){
    var ajaxReq = new AjaxRequest();
    ajaxReq.dataType = 'html';
    ajaxReq.url = "../avails/getPackageAvailStatusSummary.action?packageId=" + $("#ManagePackage #packageId").val();
    ajaxReq.success = function(result, status, xhr){
        $("#packageLineItemsAvailStatusSummary").html(result);
        createAvailsStatusDialog();
    };
    ajaxReq.submit();
}


function createAvailsStatusDialog(){
    var packageLineItemsAvailsDialog = new ModalDialog();
    packageLineItemsAvailsDialog.height = 500;
    packageLineItemsAvailsDialog.width = 1100;
	packageLineItemsAvailsDialog.buttons = [{
		 text: "Refresh Avails",
		 click : function(){
		 	updateAvails();
		 }
	    },
		{
		 text: "Close",
		 click: function(){
            $("#packageLineItemsAvailStatusSummary").dialog("close");
         }  
		 }];   
    $("#packageLineItemsAvailStatusSummary").dialog(packageLineItemsAvailsDialog);
    $("#packageLineItemsAvailStatusSummary").dialog("open");
    enableDialogButton("packageLineItemsAvailStatusSummary");
}

function updateAvails(){
    var availsJsonData = new Array();
    $('[name=lineitem]:checked').each(function(){
        var lineItemData = new Object();
        lineItemData.lineItemId = $(this).val();
        lineItemData.avails = $("#avail_" + $(this).val()).text();
        lineItemData.capacity = $("#capacity_" + $(this).val()).text();
		lineItemData.availsDate = $("#availsDate_" + $(this).val()).text();
        availsJsonData.push(lineItemData);
    });

   if(availsJsonData.length >= 1){
	    var ajaxReq = new AjaxRequest();
    	ajaxReq.type = 'POST';
    	ajaxReq.url = "../managepackage/updateAvails.action?packageId=" + $("#ManagePackage #packageId").val() + "&availsJsonData=" + JSON.stringify(availsJsonData);
    	ajaxReq.success = function(result, status, xhr){
        	$("#packageLineItemsAvailStatusSummary").dialog("close");
			jQuery("#runtimeDialogDiv").model({theme: 'Success', message: resourceBundle['message.generic.lineItem.update.avail.successfully']});
			$("#errorDiv", "div[aria-labelledby=ui-dialog-title-packageLineItemsAvailStatusSummary]").hide();
    	};
    	ajaxReq.submit();
	}else{
		if ($("#errorDiv", "div[aria-labelledby=ui-dialog-title-packageLineItemsAvailStatusSummary]").length == 0) {
			$(".ui-dialog-buttonpane", "div[aria-labelledby=ui-dialog-title-packageLineItemsAvailStatusSummary]").append("<div id='errorDiv' class='model-dialog-error'>" + resourceBundle['error.ldap.select.LineItem.avails'] + "</div>");
		}
        $("#errorDiv", "div[aria-labelledby=ui-dialog-title-packageLineItemsAvailStatusSummary]").show();
	}
}

function customLineItemSelect(select){
	if($(select).is(":checked")){
		$("#packageLineItemsTable","#packageLineItemShowHide").find(':checkbox').each(function(){
			$(this).attr("checked", "checked");
		});
	}else{
		$("#packageLineItemsTable","#packageLineItemShowHide").find(':checkbox').each(function(){
			$(this).attr("checked", false);
		});
	}
}


function lineItemMultiSelectFormatter(cellvalue, options, rowObject){
	return "<div align='center'><input type='checkbox' id='multiLineItemSelect_" + rowObject.lineItemID + "' name='multiLineItemSelect' onClick= 'selectUnselectHeaderChkBox(this)' class='allAvails' value='" + rowObject.lineItemID + "'>";
}

function selectUnselectHeaderChkBox(select){
	if(!($(select).is(":checked"))){
		$("#PackageLineItemMultiSelect_Header").attr("checked", false);
	}
	if($('[name=multiLineItemSelect]:not(:checked)',"#packageLineItemsTable").size() == 0){
  		$("#PackageLineItemMultiSelect_Header").attr("checked", true);
  	}	
}

function deleteLineItems(){
	var selectedIds="";
	$("#packageLineItemsTable","#packageLineItemShowHide").find(':checkbox').each(function(){
		if($(this).is(":checked")){
			selectedIds =  selectedIds + $(this).val() + ",";
		}
	});
	selectedIds = $.trim(selectedIds).substring(0, selectedIds.length - 1);
	var ajaxReq = new AjaxRequest();
	ajaxReq.url = '../packagelineitems/deletePackageLineItem.action?lineItemIds=' + selectedIds;
	ajaxReq.success = function(result, status, xhr){
		reloadJqGridAfterAddRecord("packageLineItemsTable", "lineItemID");
		$("#PackageLineItemMultiSelect_Header").attr("checked", false);
	};
	ajaxReq.submit();
}

function updatePkgMultiLineItemPrice(){
    var selectedIds = "";
    $("#packageLineItemsTable", "#packageLineItemShowHide").find(':checkbox').each(function(){
        if ($(this).is(":checked")) {
            selectedIds = selectedIds + $(this).val() + ",";
        }
    });
    selectedIds = $.trim(selectedIds).substring(0, selectedIds.length - 1);
    var ajaxReq = new AjaxRequest();
    ajaxReq.url = '../managepackage/updateMultiLineItemPrice.action?lineItemIds=' + selectedIds;
    ajaxReq.success = function(result, status, xhr){
       	reloadJqGridAfterAddRecord("packageLineItemsTable", "lineItemID");
	   	jQuery("#runtimeDialogDiv").model({theme: 'Success', message: resourceBundle['message.generic.pricing.updated.Successfully']});
        $("#PackageLineItemMultiSelect_Header").attr("checked", false);
        var select = $("#PackageLineItemMultiSelect_Header");
        customLineItemSelect(select);
    };
    ajaxReq.submit();
}

function calculatePrice(){
	
	var salesCategory =$("#packageSalescategory", "#packageLineItemsForm").multiselect("getChecked").map(function(){
		return this.value;
	}).get();
		
	var packageLineItemsFormManager = new FormManager();
	packageLineItemsFormManager.gridName = 'packageLineItemsTable';
	packageLineItemsFormManager.formName = 'packageLineItems';
   
    var packageLineItemForm = new JQueryAjaxForm(packageLineItemsFormManager);
   	if(salesCategory==null || salesCategory=='') {
		$('#packageLineItemsForm #packageLineItems').attr("action", "../packagelineitems/calculatePrice.action");
	}else {
		$('#packageLineItemsForm #packageLineItems').attr("action", "../packagelineitems/calculatePrice.action?salesCategoryId=" +null);
	}
   
    packageLineItemForm.doCustomProcessingAfterFormSucsesJson = function(jsonResponse, XMLHTTPRequest){
	    var rateCardPrice = jsonResponse.objectMap.price;
        if (rateCardPrice == null) {
            if ($("#priceType", "#packageLineItemsForm").val() != "FLAT RATE") {
                rateCardPrice = "Not Defined";
            } else {
                rateCardPrice = "NA";
            }
        }else{
        	if($("#rate", "#packageLineItemsForm").val() == ""){
    			$("#rate", "#packageLineItemsForm").val(rateCardPrice);
    		}
        }
		$("#rateCardPrice", "#packageLineItemsForm").val(rateCardPrice);
		
		calculateDiscountPercent();
		var calculationStep = jsonResponse.objectMap.priceCalSummary;
		$("#priceCalSummary", "#packageLineItemsForm").val(calculationStep);
		if ($("#rateCardPrice" , "#lineItemPricingContainer").val() == "" || $("#rateCardPrice", "#lineItemPricingContainer").val() == "NA" || $("#rateCardPrice", "#lineItemPricingContainer").val() == "Not Defined") {
            $("#rateX", "#lineItemPricingContainer").attr('checked', false).attr('disabled', true);
        } else {
			if (!$("#rateX", "#lineItemPricingContainer").is(':checked')) {
				$("#rateX", "#lineItemPricingContainer").removeAttr('disabled').attr('checked', false);
			}
        }
    };
    
    packageLineItemForm.doCustomProcessingAfterValidationFailedJson = function(jsonResponse, XMLHTTPRequest){
        var errorArray = jsonResponse.errorList;
        jQuery.each(errorArray, function(i, error){
            if (error.field == "sosSalesTargetId_custom" || error.field == "specType_custom") {
                jQuery("#" + error.field, "#" + packageLineItemsFormManager.formName).addClass("errorElement errortip").attr("title", error.errorMessageForUI + '<BR><b>Help:</b> ' + error.errorHelpMessageForUI);
            }
        });
        $('.errortip', "#" + packageLineItemsFormManager.formName).qtip({
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
    packageLineItemForm.submit();
}

function showPackagePriceCalSummary(){
    $("#pricingCalculationStep", "#packageLineItemsForm").qtip("option", "content.ajax.url", "");
    $("#pricingCalculationStep", "#packageLineItemsForm").qtip("option", "content.text", "<img src='../images/ajax-loader.gif' height='20px' width='20px' alt='Loading...' />");
    var rateCardPrice = $("#rateCardPrice", "#packageLineItemsForm").val();
	var url = "../packagelineitems/getPricingCalculationStep.action";	
	var pkgDataVal = {
			"lineItemId": $('#packageLineItemsForm #lineItemID').val(),
			"rateCardPrice": $('#rateCardPrice', '#packageLineItemsForm').val(),
			"priceCalSummary": $('#priceCalSummary', '#packageLineItemsForm').val()
	};   
	$("#pricingCalculationStep", "#packageLineItemsForm").qtip("option", "content.ajax.data", pkgDataVal);
	$("#pricingCalculationStep", "#packageLineItemsForm").qtip("option", "content.ajax.url", url);
}

function createPriceCalSummaryQtip(){
	$("#pricingCalculationStep", "#packageLineItemsForm").qtip({
        content: {
            // Set the text to an image HTML string with the correct src URL to the loading image you want to use
            text: '<img src="../images/ajax-loader.gif" height="20px" width="20px" alt="Loading..." />',
            ajax: {
                url: '',
				type: 'POST',
				data: '', 
                once: false
            },
            title: {
                text: '<div style="height:18px;"><span style="float:left;"></span><span style="padding:4px 0 0 3px; float:left;">' + resourceBundle['label.generic.pricing.calculation.step'] + '</span></div>',
                button: true
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
}

/**
 * Method get the consolidated data for Line Item
 * 
 * @param {Object} selectedGridRow
 */
function getLineItemConsolidatedData(selectedGridRow){
    var salesTargetObj = $("#sosSalesTargetId", "#packageLineItemsForm");
    var productId = $("#packageLineItemsTable", "#packageLineItemShowHide").jqGrid('getCell', selectedGridRow, 'sosProductId');
	var lineItemId = $("#packageLineItemsTable", "#packageLineItemShowHide").jqGrid('getCell', selectedGridRow, 'lineItemID');
    var actionURL = "../packagelineitems/getLineItemsConsolidatedData.action?lineItemId=" + lineItemId +"&productID=" + productId;
    var ajaxReq = new AjaxRequest();
    ajaxReq.url = actionURL;
    ajaxReq.cache = false;
    ajaxReq.success = function(result, status, xhr){
    	document.getElementById("packageLineItems").reset();
    	$("#lineItemType_s").attr('disabled', false).attr('checked', false);
        $("#lineItemType_r").attr('disabled', false).attr('checked', false);
        $("#lineItemType_e").attr('disabled', false).attr('checked', false);
        $("#productType", "#lineItemFinancialFieldsContainer").val('');
		setLineItemDetails(result.objectMap.lineItemDetailData);
		setproductSalesTarget(result.objectMap.productSaleTargets, salesTargetObj, result.objectMap.childSaleTargetIds, selectedGridRow);
		setLineItemTargetData(result.objectMap.lineItemTargets);
        $("#priceCalSummary", "#packageLineItemsForm").val(result.objectMap.lineItemPricingSteps);
		setSpecTypeForPackage(selectedGridRow);	
		setPlacementNameAndTargetingString();
		$("#placementName","#packageLineItemsForm").val(result.objectMap.lineItemDetailData.placementName);
		jQuery("textarea", "#packageLineItemsForm").change();	
    };
    ajaxReq.submit();
}

/**
 * Method populates line Item Details
 * 
 * @param {Object} lineItemDetailData
 */
function setLineItemDetails(lineItemDetailData){
	$("#lineItemID","#packageLineItemsForm").val(lineItemDetailData.lineItemID);
	$('#targetingStringValue').val(lineItemDetailData.targetingString);
	$("#targetingString","#packageLineItemsForm").html(lineItemDetailData.targetingString);
	changeLineItemType(lineItemDetailData.productType, false);
	$("#sosProductId","#packageLineItemsForm").val(lineItemDetailData.sosProductId);
	$("#sosProductId", "#packageLineItemsForm").trigger("change");
	$("#flight","#packageLineItemsForm").val(lineItemDetailData.flight);
	if ($.trim(lineItemDetailData.flight) == "") {
		$("#startDate", "#packageLineItemsForm").val(lineItemDetailData.startDate);
	}else{
		$("#startDate", "#packageLineItemsForm").val("");
	}
	$("#endDate", "#packageLineItemsForm").val(lineItemDetailData.endDate);
	$("#placementName","#packageLineItemsForm").val(lineItemDetailData.placementName);
	$("#rateCardPrice","#packageLineItemsForm").val(lineItemDetailData.rateCardPrice);
	$("#lineItemSequence","#packageLineItemsForm").val(lineItemDetailData.lineItemSequence);
	$("#priceType","#packageLineItemsForm").val(lineItemDetailData.priceType);
	$("#rate","#packageLineItemsForm").val(lineItemDetailData.rate);
	$("#impressionTotal","#packageLineItemsForm").val(lineItemDetailData.impressionTotal);
	$("#totalInvestment","#packageLineItemsForm").val(lineItemDetailData.totalInvestment);
	$("#avails","#packageLineItemsForm").val(lineItemDetailData.avails);
	$("#availsPopulatedDate","#packageLineItemsForm").val(lineItemDetailData.availsPopulatedDate);
	$("#totalPossibleImpressions","#packageLineItemsForm").val(lineItemDetailData.totalPossibleImpressions);
	$("#sov","#packageLineItemsForm").val(lineItemDetailData.sov);
	$("#comments","#packageLineItemsForm").val(lineItemDetailData.comments);
	$("#product_active","#packageLineItemsForm").val(lineItemDetailData.product_active);
	$("#salesTarget_active","#packageLineItemsForm").val(lineItemDetailData.salesTarget_active);
	calculateDiscountPercent();	
    onClickPriceTypeRadio(lineItemDetailData.priceType) ;
	$("#priceCalSummary", "#packageLineItemsForm").val("");
    $('#pricingSummary').html('');
    if(lineItemDetailData.productType == "R" || lineItemDetailData.productType == "E"){
		if(lineItemDetailData.productType == "R"){
			$("#lineItemType_r").attr('checked', 'checked');
		}else{
			$("#lineItemType_e").attr('checked', 'checked');
		}
		$("#sor","#packageLineItemsForm").val(lineItemDetailData.sor);
		enableDisableCalendar();
		enableLineItemDefaults();
	}else if(lineItemDetailData.productType == "S"){
		$("#lineItemType_s").attr('checked', 'checked');
	}
	if ($("#rateCardPrice" , "#packageLineItemsForm").val() == "" || $("#rateCardPrice", "#packageLineItemsForm").val() == "NA" || $("#rateCardPrice", "#packageLineItemsForm").val() == "Not Defined") {
            $("#rateX", "#packageLineItemsForm").attr('checked', false).attr('disabled', true);
    } else {
		if(lineItemDetailData.rateX){
			$("#rateX", "#packageLineItemsForm").removeAttr('disabled').attr('checked', true);
		} else {
			$("#rateX", "#packageLineItemsForm").removeAttr('disabled').attr('checked', false);
		}
    }
	$("#lineItemIdVal","#packageLineItemsForm").val(lineItemDetailData.lineItemID);
	$('#isViewable').val(lineItemDetailData.isViewable);
}

function setproductSalesTarget(productSaleTargets, salesTargetObj, childSaleTargetIds, selectedGridRow){	
	$("option", salesTargetObj).remove();
    sortListForPackage(productSaleTargets, salesTargetObj, childSaleTargetIds);
    $(salesTargetObj).multiselect("refresh");
    selectSalesTargetFromGridForPackage(selectedGridRow);
    $("#flagForOnChange", "#packageLineItemsForm").val('true');
	
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

function resetStartEndDate(){
	if($("#lineItemType_e", "#lineItemTargeting").is(':checked')) {
		$("#endDate", "#packageLineItems").val(jQuery( "#validFrom" , "#ManagePackage").val());
	}else{
		$("#endDate", "#packageLineItems").val(jQuery( "#validTo" , "#ManagePackage").val());
	}
	$("#startDate", "#packageLineItems").val(jQuery( "#validFrom" , "#ManagePackage").val());
}

function reArrangeLineItemSequence(movePlaces){
    var selectedIds = "";
    $("#packageLineItemsTable", "#packageLineItemShowHide").find(':checkbox').each(function(){
        if ($(this).is(":checked")) {
            selectedIds = selectedIds + $(this).val() + ",";
        }
    });
    if (selectedIds == "") {
		jQuery("#runtimeDialogDiv").model({theme: 'Error', autofade: false, message: resourceBundle['error.lineitem.sequence.rearrange']});
		return;
    }
    var ajaxReq = new AjaxRequest();
    ajaxReq.url = "../managepackage/arrangeLineItemSequence.action?packageId=" + jQuery("#packageId", "#ManagePackage").val()
						 +"&positionToMove=" + movePlaces + "&lineItemsToMove=" + selectedIds;
    ajaxReq.success = function(result, status, xhr){
		reloadJqGridAfterAddRecord("packageLineItemsTable", "lineItemSequence", "asc");
    };
    ajaxReq.submit();
}


function showPkgProductCreativeSummary(){
	$("#productCreativeSummary", "#packageLineItemsForm").qtip("option", "content.ajax.url", "");
	$("#productCreativeSummary", "#packageLineItemsForm").qtip("option", "content.text", "<img src='../images/ajax-loader.gif' height='20px' width='20px' alt='Loading...' />");
	var url = "../proposalWorkflow/showProductCreativesSummary.action";
	var data = {
			"sosProductId": $("#sosProductId", "#packageLineItemsForm").val() == "" ? 0 : $("#sosProductId", "#packageLineItemsForm").val()
	};
	$("#productCreativeSummary", "#packageLineItemsForm").qtip("option", "content.ajax.data", data);
    $("#productCreativeSummary", "#packageLineItemsForm").qtip("option", "content.ajax.url", url);
}

function createProductCreativeSummaryQtip(){
	$("#productCreativeSummary", "#packageLineItemsForm").qtip({
        content: {
            // Set the text to an image HTML string with the correct src URL to the loading image you want to use
            text: '<img src="../images/ajax-loader.gif" height="300px" width="650px" alt="Loading..." />',
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
        	at: 'bottom center', // Position the tool tip above the link
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
}

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

/**
 * Method show hide the Action components based on Role i.e., 
 * Planner and Product owner has read only access for Manage Package
 */
function showHideActionIcons(){
	if($("#hasReadOnlyRights", "#managePackageContainer").length > 0 ){
		//Hide the icons of package grid
		$("#add_packageMasterGrid","#packageMasterPager").hide();
		$("#del_packageMasterGrid","#packageMasterPager").hide();
		$("#copyPackage_packageMasterTable","#packageMasterPager").hide();
		
		//Hide the icons of line item grid		
		$("#add_packageLineItemsTable","#packageLineItemsPager").hide();
		$("#deleteLineItem_packageLineItemsTable","#packageLineItemsPager").hide();
		$("#copyLineItem_packageLineItemsTable","#packageLineItemsPager").hide();
		$("#updatePrice_packageLineItemsTable","#packageLineItemsPager").hide();
		$("#arrangeLineItemSequence","#packageLineItemsPager").hide();
		$("#packageLineItemsTable", "#managePackageContainer").jqGrid('hideCol','act');
		
		//Hide the icons of document grid
		$("#add_packageAttachementTable","#packageAttachementPager").hide();
		$("#del_packageAttachementTable","#packageAttachementPager").hide();
		$("#packageAttachementTable", "#managePackageContainer").jqGrid('hideCol','add');
		
		//disable the save and reset button
		$("#packageSaveDataBtn", "#ManagePackage").removeClass("save-btn");
		$("#packageResetDataBtn", "#ManagePackage").removeClass("reset-btn");
		$("#packageSaveDataBtn", "#ManagePackage").addClass("disabled-btn");
		$("#packageResetDataBtn", "#ManagePackage").addClass("disabled-btn");
		$("#packageSaveDataBtn", "#ManagePackage").prop('disabled', true);
		$("#packageResetDataBtn", "#ManagePackage").prop('disabled', true);
	}else{
		$("#add_packageMasterGrid","#packageMasterPager").show();
		$("#del_packageMasterGrid","#packageMasterPager").show();
		$("#copyPackage_packageMasterTable","#packageMasterPager").show();
				
		$("#add_packageLineItemsTable","#packageLineItemsPager").show();
		$("#deleteLineItem_packageLineItemsTable","#packageLineItemsPager").show();
		$("#copyLineItem_packageLineItemsTable","#packageLineItemsPager").show();
		$("#updatePrice_packageLineItemsTable","#packageLineItemsPager").show();
		$("#arrangeLineItemSequence","#packageLineItemsPager").show();
		$("#packageLineItemsTable", "#managePackageContainer").jqGrid('showCol','act');
		
		$("#add_packageAttachementTable","#packageAttachementPager").show();
		$("#del_packageAttachementTable","#packageAttachementPager").show();
		$("#packageAttachementTable", "#managePackageContainer").jqGrid('showCol','add');
		
		$("#packageSaveDataBtn", "#ManagePackage").removeClass("disabled-btn");
		$("#packageResetDataBtn", "#ManagePackage").removeClass("disabled-btn");
		$("#packageSaveDataBtn", "#ManagePackage").addClass("save-btn");
		$("#packageResetDataBtn", "#ManagePackage").addClass("reset-btn");
		$("#packageSaveDataBtn", "#ManagePackage").prop('disabled', false);
		$("#packageResetDataBtn", "#ManagePackage").prop('disabled', false);		
	}
}
