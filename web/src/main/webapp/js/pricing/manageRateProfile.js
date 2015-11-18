/**
 * JavaScript for manage List Rate screen
 *
 * @author sachin.ahuja
 * @version 2.0
 */

var sheepItForm = {};

$(document).ready(function() {
	
	/**
	 * Register select2 plugin for product select box and change event
	 */
	$("#productId", "#manageListRateContainer").select2();
	
	jQuery(".numericdecimal", "#manageListRateContainer").numeric({
		negative: false
	});
	
	$("#productId", "#manageListRateContainer").bind("change", function () {
		$('#productName', '#listRateDetail').val($("#productId option:selected", "#manageListRateContainer").text());
		$("#rateCardNotRoundedContainer","#manageListRateContainer").hide();
		/*
		 * For product select box: On change populate sales target for product
		 */
		if ($("#productId", "#listRateDetail").val() == "") {
			$("#salesTargetId > option").remove();
			$("#salesTargetId", "#listRateDetail").multiselect("refresh");
		} else {
			if($("#productId option:selected", "#manageListRateContainer").attr("data-type") == 'R'){
				$("#rateCardNotRoundedContainer","#manageListRateContainer").show();
			}
			setsosSalesTargetIdValue($("#productId", "#listRateDetail").val());
		}
	});
	
	/**
	 * Register select2 plugin for Sales Category select box and target sales category box for cloning
	 */
	$("#allSalesCategory", "#manageListRateContainer").select2();	
	$("#targetSalesCategory", "#manageListRateContainer").select2();	
	$("#targetSalesCategory_select2", "#manageListRateContainer").css("z-index", "0");	

	
	$("#allSalesCategory", "#manageListRateContainer").bind("change", function () {
		$('#salesCategoryId', '#listRateDetail').val($("#allSalesCategory", "#manageListRateContainer").val());
		$('#salesCategoryName', '#listRateDetail').val($("#allSalesCategory option:selected", "#manageListRateContainer").text());
		
		/**
		 * Reset search parameter and set current sales category id before reload
		 */
		jQuery("#rateProfileSearchOption", "#manageListRateContainer").val('productName');
		jQuery("#rateProfileSearchValue", "#manageListRateContainer").val('');
		
		jQuery("#manageListRateTable").jqGrid('setGridParam', {
			url: '../manageRateProfile/loadGridData.action?salesCategoryId=' + $("#allSalesCategory", "#manageListRateContainer").val()
		});
		
		reloadJqGridAfterAddRecord('manageListRateTable', "profileId");
	});
	
	$("#salesTargetId", "#manageListRateContainer").multiselect({
		selectedList: 2,
		click: function(event, ui) {
			var salesTargetIdCombo = $("#salesTargetId", "#listRateDetail");
			var isChecked = (ui.checked) ? true : false ;
			checkChildSalesTarget(salesTargetIdCombo, getChildSalesTarget(salesTargetIdCombo, ui.value), isChecked);				
        }
	}).multiselectfilter();
	
	
	/**
	 * Initialized List Rate JQGrid
	 */
	var listRateGridOpts = new JqGridBaseOptions();
	listRateGridOpts.url = "../manageRateProfile/loadGridData.action?salesCategoryId=";
	listRateGridOpts.colNames = ['Profile Id', 'Product Id', 'Product Name', 'Section', 'Sales Target Id', 'Sales Target', 'Base Price', 'Notes', 'Rate Card Not Rounded', 'Version'];
	listRateGridOpts.colModel = [
		{name:'profileId', index:'profileId', hidden:true, key:true}, 
		{name:'productId', index:'productId',  hidden:true, key:false}, 
		{name:'productName', index:'productName', key:false, width: 300, resizable:false}, 
		{name:'sectionNames', index:'sectionNames', key:false, width: 300, resizable:false}, 
		{name:'salesTargetIdStr', index:'salesTargetIdStr', hidden:true, key:false}, 
		{name:'salesTargetNamesStr', index:'salesTargetNamesStr', key:false, width: 350, sortable:false, resizable:false},
		{name:'basePrice', index:'basePrice', key:false, width:200, resizable:false, align:"right"},
		{name:'notes', index:'notes', sortable:false, key:false, width: 350, resizable:false},
		{name:'rateCardNotRounded', index:'rateCardNotRounded', hidden:true, key:false},
		{name:'version', index:'version', hidden:true, key:false}
	];
	
	listRateGridOpts.caption = resourceBundle['tab.generic.manageListRate'];
	listRateGridOpts.pager = jQuery('#manageListRatePager');
	listRateGridOpts.sortname = "profileId";
	listRateGridOpts.afterGridCompleteFunction = function (){
		var topRowId = jQuery("#manageListRateTable").getDataIDs()[0];
		if(topRowId == '' || topRowId == undefined){
			jQuery('#listRateDetailForm', '#manageListRateContainer').hide();
		}
	};
	
	listRateGridOpts.onSelectRow = function(id){
		clearCSSErrors('#listRateDetailForm');	
		if(id){
			jQuery("#manageListRateTable").jqGrid('GridToForm', id, "#listRateDetail");
			jQuery("textarea", "#manageListRateContainer").change();
			$("#productId", "#manageListRateContainer").trigger("change");
			jQuery('#listRateDetailForm', '#manageListRateContainer').show();
			if($('#manageListRateTable').getCell(id, 'rateCardNotRounded')  ==  "true"){
				$("#rateCardNotRounded","#manageListRateContainer").attr('checked', true);
			}else{
				$("#rateCardNotRounded","#manageListRateContainer").attr('checked', false);
			}
			sheepItForm.removeAllForms();
			ruleIndex = 1;
			getRateProfileDiscountData();
			if($("#clone_seasonalDiscount").is(':checked')){
				$("#clone_seasonalDiscount").attr('checked', false);
		  	}
			customDiscountSelect("#clone_seasonalDiscount");
		}
		jQuery("#listRateDetailFormTab-1 a").text(resourceBundle["tab.generic.rateProfileDetails"]);
	};
	
	jQuery("#manageListRateTable").jqGrid(listRateGridOpts).navGrid('#manageListRatePager', {edit: false,search:false,
		addfunc: function(rowid){
			resetRowSelectionOnAdding("#manageListRateTable", "#profileId");
			clearCSSErrors('#listRateDetailForm');
			document.listRateDetail.reset();
			jQuery("textarea", "#manageListRateContainer").change();
			jQuery("#productId", "#manageListRateContainer").trigger("change");
			jQuery("#profileId").val(0);
			jQuery('#listRateDetailForm', '#manageListRateContainer').show();
			jQuery("#listRateDetailFormTab-1 a").text(resourceBundle["tab.generic.addNewRateProfile"]);
			$("#rateCardNotRounded","#manageListRateContainer").attr('checked', false);
			sheepItForm.removeAllForms();
			ruleIndex = 1;
		},
		delfunc: function(rowid){
			var profileId = jQuery("#profileId", "#manageListRateContainer").val();
			jQuery("#manageListRateTable").jqGrid('setGridParam', {page: 1});
			jQuery('#manageListRateTable').delGridRow(profileId, {
				url: '../manageRateProfile/deleteRateProfile.action?profileId=' + profileId
			});
		},
		beforeRefresh: function(){
			/**
			 * Reset search parameter and set current sales category id before reload
			 */
			jQuery("#rateProfileSearchOption", "#manageListRateContainer").val('productName');
			jQuery("#rateProfileSearchValue", "#manageListRateContainer").val('');
			jQuery("#manageListRateTable").jqGrid('setGridParam', {
				url: '../manageRateProfile/loadGridData.action?salesCategoryId=' + $("#allSalesCategory", "#manageListRateContainer").val()
			});
			reloadJqGridAfterAddRecord('manageListRateTable', "profileId");
		}		
	});
	
	/**
	 * Apply tab plug-in in rate profile detail section
	 */
	jQuery("#listRateDetailForm", "#manageListRateContainer").tabs();
	
	/**
	 * Initialized search panel on rate profile grid
	 */
	initGridSearchOptions("manageListRateTable", "rateProfileSearchPanel", "manageListRateContainer");

	/**
	 * Enable auto search on rate profile grid
	 */
	enableAutoSearch(jQuery("#rateProfileSearchValue", "#manageListRateContainer"), function() {
		jQuery("#manageListRateTable").jqGrid('setGridParam', {
			url: '../manageRateProfile/loadGridData.action?salesCategoryId=' + $("#allSalesCategory", "#manageListRateContainer").val(), page: 1,
			postData: {
				searchField: jQuery("#rateProfileSearchOption", "#manageListRateContainer").val(),
				searchString: $.trim(jQuery("#rateProfileSearchValue", "#manageListRateContainer").val()),
				searchOper: 'cn'
			}
		}).trigger("reloadGrid");
	});
	
	/**
	 * Register change event on rate profile search option select box
	 */
	jQuery("#rateProfileSearchOption", "#manageListRateContainer").bind("change", function () {
		jQuery("#rateProfileSearchValue", "#manageListRateContainer").val('').focus();
	});
	
	jQuery("#notes", "#manageListRateContainer").limiter(500, $("#charsRemaining", "#manageListRateContainer"));

	var instanceFormManager = new FormManager();
	instanceFormManager.gridName = 'manageListRateTable';
	instanceFormManager.formName = 'listRateDetail';
	/**
	 * Register click event on Save Button
	 */
	jQuery('#listRateProfileSaveData', '#listRateDetail').click(function() {
		clearCSSErrors('#listRateDetail');
		getDataForDiscountsToSave();
		var listRateDetailForm = new JQueryAjaxForm(instanceFormManager);
		listRateDetailForm.doCustomProcessingAfterFormSucsesJson = function(jsonResponse, XMLHTTPRequest){
			jQuery("#runtimeDialogDiv").model({theme: 'Success', message: resourceBundle['message.generic.rateProfileSavedSuccessfully']});
			var profileId = jQuery("#profileId").val();
			if (profileId == 0) {
				reloadJqGridAfterAddRecord(instanceFormManager.gridName, "profileId");
			}
			else {
				jQuery("#manageListRateTable").jqGrid('FormToGrid', profileId, "#listRateDetail");
				var salesTargetId = $("#salesTargetId", "#listRateDetail").multiselect("getChecked").map(function(){
			        return this.value;
			    }).get();
				jQuery("#manageListRateTable").setCell(profileId, "salesTargetIdStr", salesTargetId);
				
				if($("#rateCardNotRounded","#manageListRateContainer").is(':checked')){
					jQuery("#manageListRateTable").setCell(profileId, "rateCardNotRounded", "true");
				}else{
					jQuery("#manageListRateTable").setCell(profileId, "rateCardNotRounded", "false");
				}
				
			    var salesTargetNames = $("#salesTargetId", "#listRateDetail").multiselect("getChecked").map(function(){
			        return $(this).next().html();
			    }).get().join(', ');
			    jQuery("#manageListRateTable").setCell(profileId, "salesTargetNamesStr", salesTargetNames);
				var sectionNames = jsonResponse.objectMap.gridKeyColumnValue;
				if(sectionNames == ""){
					sectionNames = null;
				}
				jQuery("#manageListRateTable").setCell(profileId, "sectionNames", sectionNames );
			}
			sheepItForm.removeAllForms();
			ruleIndex = 1;
			getRateProfileDiscountData();
			if($("#clone_seasonalDiscount").is(':checked')){
				$("#clone_seasonalDiscount").attr('checked', false);
		  	}
			customDiscountSelect("#clone_seasonalDiscount");
		};
		listRateDetailForm.submit();
	});
	
	/**
	 * Register click event on Reset Button
	 */
	jQuery('#listRateProfileResetData', '#listRateDetail').click(function (){
		clearCSSErrors('#listRateDetail');
		if(jQuery("#listRateDetailFormTab-1 a").text() == resourceBundle['tab.generic.addNewRateProfile']){
			document.listRateDetail.reset();
			$("#productId", "#manageListRateContainer").trigger("change");
			jQuery('input[type!=hidden]:first', '#listRateDetail').focus();
			sheepItForm.removeAllForms();
			ruleIndex = 1;
		} else {
			var gsr = jQuery("#manageListRateTable").jqGrid('getGridParam','selrow');
			if(gsr){
				jQuery("#manageListRateTable").jqGrid('GridToForm', gsr, "#listRateDetail");
				$("#productId", "#manageListRateContainer").trigger("change");
				sheepItForm.removeAllForms();
				ruleIndex = 1;
				getRateProfileDiscountData();
				if($("#clone_seasonalDiscount").is(':checked')){
					$("#clone_seasonalDiscount").attr('checked', false);
			  	}
				customDiscountSelect("#clone_seasonalDiscount");
			} else {
				alert("Please select Row");
			}	
		}
		jQuery("textarea", "#manageListRateContainer").change();
	});

	/**
	 * Register click event on Add Discounts Button
	 */
	jQuery('#addDiscounts', '#sheepItForm').click(function() {
		$('#seasonalDiscounTableContainer', '#sheepItForm').show();
	});
	/**
	 * Fetch sales target from selected product in package
	 * @param {Object} selectedProduct
	 */
	function setsosSalesTargetIdValue(selectedProduct){
		var categoryId = $('#salesCategoryId', '#listRateDetail').val();
		var profileId = jQuery("#profileId", "#manageListRateContainer").val();
	    var salesTargetIdCombo = $("#salesTargetId", "#listRateDetail");
	    var actionURL = "../manageRateProfile/getSalesTargetForProduct.action?productID=" + selectedProduct +"&salesCategoryId=" + categoryId + "&rateProfileId=" + profileId;
	    dynaLoadSalesTargetsForProduct(salesTargetIdCombo, actionURL, selectedProduct); 
	}

	/**
	 * Set sales targets option then multiselect refresh
	 * @param {Object} select
	 * @param {Object} actionURL
	 */
	function dynaLoadSalesTargetsForProduct(select, actionURL, selectedProduct){
		var ajaxReq = new AjaxRequest();
		ajaxReq.url = actionURL;
		ajaxReq.cache = false;
		ajaxReq.success = function(result, status, xhr){
			$("option", select).remove();
			sortListForProduct(result.objectMap.productSalesTargets, select, result.objectMap.childSaleTarget);
			
			var currentRow = jQuery('#manageListRateTable').jqGrid('getGridParam', 'selrow');
			if (currentRow) {
				if ($('#manageListRateTable').getCell(currentRow, 'productId') == selectedProduct) {
					$(select).val($('#manageListRateTable').getCell(currentRow, 'salesTargetIdStr').split(','));
				}
			}
			$(select).multiselect("refresh");
		};
		ajaxReq.submit();
	}
	
	/**
	 * Sort data for select box based on display value
	 * @param result
	 * @param select
	 * @return
	 */
	function sortListForProduct(result, select, childSaleTargetIds){
	    var arrVal = sortMapDataByValue(result);	    
	    var optString = "";
	    for (var i = 0; i < arrVal.length; i++) {
	        var parentValue = 'NA';
	        $.each(childSaleTargetIds, function(salesTargetId, parentSalesTargetId){
	            if (salesTargetId == arrVal[i][1]) {
	                parentValue = parentSalesTargetId;
	            }
	        });
	        optString = optString + '<option value="' + arrVal[i][1] + '" parent="' + parentValue + '">' + arrVal[i][0] + '</option>';
	    }
	    $(select).append(optString);
	}
    
    /**
	 * Start sales Category clone ModalDialog
	 */
    var cloneRateProfileDlg = new ModalDialog();
    cloneRateProfileDlg.height = 250;
    cloneRateProfileDlg.width = 560;
    cloneRateProfileDlg.resizable = false;
	
    cloneRateProfileDlg.buttons = [{
        text: "Clone",
        click: function(){
    		checkDataOnRateProfile();
    	}
    }, {
        text: "Ok",
        click: function() {
    		$(this).dialog("close");
            cloneRateProfile();
        }
    }, {
        text: "Cancel",
        click: function(){			
            $(this).dialog("close");
        }
    }];
	$("#rateProfileCloneForm").dialog(cloneRateProfileDlg);
	enableDialogButton("rateProfileCloneForm");
	
	/**
	 * Register click event on Clone Button
	 */
	jQuery('#rateProfileCloneBtn', '#manageListRateContainer').click(function () {
		var salesCategoryName = $("#allSalesCategory option:selected", "#manageListRateContainer").text();
		if (salesCategoryName == '') {
			$('#cloneSalesCategoryFrom', '#rateProfileCloneForm').html('Default');
		} else {
			$('#cloneSalesCategoryFrom', '#rateProfileCloneForm').html(salesCategoryName);
		}
		$('#sourceSalesCategoryId', '#rateProfileCloneForm').val($("#allSalesCategory", "#manageListRateContainer").val());
		
		$("#rateProfileCloneForm > table").show();
		$("#targetRateProfileExist", "#rateProfileCloneForm").hide();
		
		// hide OK button and show CLONE button in Clone Dialog
		$(".ui-dialog-buttonset button:contains('Ok')").hide();		
		$(".ui-dialog-buttonset button:contains('Clone')").show();
		$(".ui-dialog-buttonpane #errorDiv").hide();
		// remove all options from target sales category and clone from source
		$("#targetSalesCategory option", "#rateProfileCloneForm").remove();
		$('#select1 option').clone().appendTo('#select2');
		$('#allSalesCategory option', '#manageListRateContainer').clone().appendTo('#targetSalesCategory', '#rateProfileCloneForm');
		// remove selected parent sales category and initialize to default in target sales category box
		$("#targetSalesCategory option[value=" + $('#sourceSalesCategoryId', '#rateProfileCloneForm').val() + "]", "#rateProfileCloneForm").remove();
		$("#targetSalesCategory", "#rateProfileCloneForm").val($("#allSalesCategory option[value='']", "#manageListRateContainer").val());
		$("#targetSalesCategory", "#rateProfileCloneForm").trigger("change");
		
		$("#rateProfileCloneForm").dialog("open");
		$(".ui-dialog-buttonset button:contains('Clone')").button("enable");
		$(".ui-dialog-buttonset button:contains('Ok')").button("enable");
	});
	
	     
	sheepItForm = $('#sheepItForm').sheepIt({
        separator: '',
        allowRemoveLast: true,
        allowRemoveCurrent: true,
        allowRemoveAll: true,
        allowAdd: true,
        allowAddN: true,
		controlsSelector: '#sheepItForm_controls',
		addSelector: "#sheepItForm_add",
        maxFormsCount: 5,
        minFormsCount: 0,
        iniFormsCount: 0,
        continuousIndex: true
    });
	
	$('#cloneDiscounts','#seasonalDiscounTableContainer').click(function() {
		cloneDiscount();
	});
	
	$('#deleteDiscounts','#seasonalDiscounTableContainer').click(function() {
		deleteDiscount();
	});
	
	$("#discountProductId", "#cloneDiscountDetail").select2();
    $("#discountSalesCategoryId", "#cloneDiscountDetail").select2();
    
    $("#discountSalesCategoryId", "#cloneDiscountDetails").bind("change", function () {
    	searchRateProfiles();
    });
    
    $("#discountProductId", "#cloneDiscountDetails").bind("change", function () {
    	searchRateProfiles();
    });
    enableDialogButton("rateProfileCloneForm");
});

/**
 * Schedule Section weight calculation job at mid night
 */
function scheduleSectionWeightCalJob() {
    var ajaxReq = new AjaxRequest();
    ajaxReq.url = '../manageRateProfile/scheduleSectionWeightCalJob.action';
    ajaxReq.success = function(result, status, xhr){
        jQuery("#runtimeDialogDiv").model({theme: 'Success', message: 'Job scheduled successfully.'});
    };
    ajaxReq.submit();
}

/**
 * Check scheduled Job
 */
function checkScheduledSectionWeightCalJob(){
    var ajaxReq = new AjaxRequest();
    ajaxReq.url = '../manageRateProfile/checkScheduledSectionWeightCalJob.action';
    ajaxReq.success = function(result, status, xhr){
        if (result.objectMap.jobScheduled) {
			if (result.objectMap.jobScheduledBy) {
                $("#weightCalJobMessage", "#sectionWeightCalJobInfo").html(resourceBundle["label.job.scheduled"] 
						+ "<b> " + result.objectMap.jobScheduledBy + "</b> at " + result.objectMap.nextFireTime);
			} else {
				$("#weightCalJobMessage", "#sectionWeightCalJobInfo").html(resourceBundle["label.job.quarter.scheduled"] + " at " +result.objectMap.nextFireTime);
			}			
            $("#nextFireTime", "#sectionWeightCalJobInfo").text(result.objectMap.nextFireTime);
            $("#sectionWeightCalJobInfo").model({width: 320, autofade: false});
        }
        else {
            $("#nextFireTime", "#sectionWeightCalJobConfirm").text(result.objectMap.nextFireTime);
            $("#sectionWeightCalJobConfirm").model({width: 320, autofade: false,
                buttons: [{
                    text: "Yes",
                    click: function(){
                        $(this).dialog("close");
                        scheduleSectionWeightCalJob();
                    }
                }, {
                    text: "No",
                    click: function(){
                        $(this).dialog("close");
                    }
                }]
            });
        }
    };
    ajaxReq.submit();
}

/**
 * Check/Uncheck child sales target for selected Parent Sales Target
 * @param {Object} salesTargetIdCombo
 * @param {Object} salesTargetIds
 * @param {Object} isChecked
 */
function checkChildSalesTarget(salesTargetIdCombo, salesTargetIds, isChecked){
    if (salesTargetIds.length > 0) {
        for (var i = 0; i < salesTargetIds.length; i++) {
        	checkChildSalesTarget(salesTargetIdCombo, getChildSalesTarget(salesTargetIdCombo, salesTargetIds[i]), isChecked);
            $("#salesTargetId option[value=" + salesTargetIds[i] + "]").attr('selected', isChecked);
            $(salesTargetIdCombo).multiselect("widget").find(":checkbox[value=" + salesTargetIds[i] + "]").attr('checked', isChecked);
        }
    }
    else {
        return;
    }
}

/**
 * Return array of child sales target
 * @param {Object} salesTargetIdCombo
 * @param {Object} salesTargetId
 */
function getChildSalesTarget(salesTargetIdCombo, salesTargetId){
    return $(salesTargetIdCombo).find("option[parent='" + salesTargetId + "']").map(function(){
        return this.value;
    });
}

/**
 * 
 * @return
 */
function checkDataOnRateProfile() {
	var ajaxReq = new AjaxRequest();
	ajaxReq.url = "../manageRateProfile/getRateProfilesForSalesCategory.action?salesCategoryId="
				 + $('#targetSalesCategory', '#rateProfileCloneForm').val()
				 + "&fromSalesCategoryId=" + $('#sourceSalesCategoryId', '#rateProfileCloneForm').val();
	ajaxReq.success = function(result, status, xhr) {
		if (result.objectMap.fromRateProfileDataNotExist) {			
			if ( $("#errorDiv", "div[aria-labelledby=ui-dialog-title-rateProfileCloneForm]").length == 0) {
			$(".ui-dialog-buttonpane", "div[aria-labelledby=ui-dialog-title-rateProfileCloneForm]").append("<div id='errorDiv' class='model-dialog-error'>" + resourceBundle['error.rateProfile.clone.Nodata'] + "</div>");
			}
	        $(".ui-dialog-buttonpane #errorDiv").show();
		}else if (result.objectMap.rateProfileDataExist) {
			$("#rateProfileCloneForm > table").hide();
			$(".ui-dialog-buttonset button:contains('Clone')").hide();
			$(".ui-dialog-buttonset button:contains('Ok')").show();	
			var targetSalesCategoryName = $("#targetSalesCategory option:selected", "#rateProfileCloneForm").text();
			if (targetSalesCategoryName != ''){
				$("#selectedSalesCategory", "#rateProfileCloneForm").text("\"" + targetSalesCategoryName + "\"");
			} else {
				$("#selectedSalesCategory", "#rateProfileCloneForm").text("\"Default\"");
			}
			$("#targetRateProfileExist", "#rateProfileCloneForm").show();
		} else {
			cloneRateProfile();
		}
	};
	$("#errorDiv", "div[aria-labelledby=ui-dialog-title-availStatusSummary]").hide();
	ajaxReq.submit();
	$(".ui-dialog-buttonset button:contains('Clone')").button("disable");
}

/**
 * 
 * @return
 */
function cloneRateProfile () {
	var ajaxRequest = new AjaxRequest();
	ajaxRequest.url = "../manageRateProfile/copyRateProfiles.action?sourceSalesCategoryId="
			+ $("#allSalesCategory", "#manageListRateContainer").val()
			+ '&targetSalesCategoryId=' + $('#targetSalesCategory', '#rateProfileCloneForm').val()
			+ '&targetSalesCategoryName=' + $("#targetSalesCategory option:selected", "#rateProfileCloneForm").text();
	
	ajaxRequest.cache = false;
	ajaxRequest.success = function(result, status, xhr) {
		$("#rateProfileCloneForm").dialog("close");
		jQuery("#runtimeDialogDiv").model({theme: 'Success', message: resourceBundle['message.generic.rateProfileClonedSuccessfully']});
	};
	ajaxRequest.submit();
	$(".ui-dialog-buttonset button:contains('Ok')").button("disable");
}

$('#addDiscounts' , '#sheepItForm').bind('click', function() {
	$("#sheepItForm_noforms_template" , "#seasonalDiscounTable").hide();
	if(sheepItForm.canAddForm()){
		sheepItForm.addForm();
		clearCSSErrors('#listRateDetail');
		var lastRowId = $("#seasonalDiscounTable" , "#sheepItForm").find("tr:last").prev('tr').attr("id");
		var rowId = lastRowId.substring(Number(lastRowId.length - 1));
		var datesProposalLineItem = jQuery('#sheepItForm_' + rowId + '_startDate, #sheepItForm_' + rowId + '_endDate','#seasonalDiscounTable').datepicker({
	        autoSize: true,
	        showOn: "both",
	        buttonText: "Select Date",
	        buttonImage: '../images/calendar.gif',
	        buttonImageOnly: true,
	        numberOfMonths: 3,
	        onSelect: function(selectedDate){
	            var option = this.id.indexOf("startDate") >=0 ? "minDate" : "maxDate", instance = $(this).data("datepicker");
	            date = $.datepicker.parseDate(instance.settings.dateFormat ||
	            			$.datepicker._defaults.dateFormat, selectedDate, instance.settings);
	            datesProposalLineItem.not(this).datepicker("option", option, date);
	        }
		});
		
		$("#sheepItForm_" + rowId + "_startReset" , "#seasonalDiscounTable").bind("click", function(event, ui){
			$("#sheepItForm_" + rowId + "_startDate" , "#seasonalDiscounTable").val("");
			$("#sheepItForm_" + rowId + "_endDate" , "#seasonalDiscounTable").datepicker("option", "minDate",  new Date());
		});
		
		$("#sheepItForm_" + rowId + "_endReset" , "#seasonalDiscounTable").bind("click", function(event, ui){
			$("#sheepItForm_" + rowId + "_endDate" , "#seasonalDiscounTable").val("");
			$("#sheepItForm_" + rowId + "_startDate" , "#seasonalDiscounTable").datepicker("option", "maxDate", $("#sheepItForm_" + rowId + "_endDate" , "#seasonalDiscounTable").val());
		});
		
		var ruleNo = "Rule " + ruleIndex;
		$("#sheepItForm_" + rowId + "_profileId" , "#seasonalDiscounTable").text('0');
		$("#sheepItForm_" + rowId + "_ruleNo" , "#seasonalDiscounTable").text(ruleNo);
		$("#sheepItForm_" + rowId + "_startDate" , "#seasonalDiscounTable").datepicker("option", "minDate", new Date());
		$("#sheepItForm_" + rowId + "_endDate" , "#seasonalDiscounTable").datepicker("option", "minDate", new Date());
		ruleIndex = ruleIndex  + 1;
	} else {
		jQuery("#runtimeDialogDiv").model({theme: 'Error', autofade: false, width: 415, message: resourceBundle['error.cannot.add.more.seasonalDiscount']});
	}
});


/**
 * Function creates a JsonString to pass to server
 */
function getDataForDiscountsToSave(){
	var rateProfileDiscounts = new Array();
	var objId ="";
	var not = false;
	$("#rateProfileDiscountData").val("");
    $("#seasonalDiscounTable tr").each(function(){
    	objId = $(this).attr("id");
        if (objId != "sheepItForm_noforms_template" && objId != "seasonalDiscountHead") { 
        	var rowIndex = objId.substring(Number(objId.length - 1));
        	var object = new Object();
			object.startDate = $('#sheepItForm_' + rowIndex + '_startDate').val();
		    object.endDate = $('#sheepItForm_' + rowIndex + '_endDate').val();
		    object.discount = $('#sheepItForm_' + rowIndex + '_seasonalDiscount').val();
		    if ($('#sheepItForm_' + rowIndex + '_not_seasonalDiscount').is(':checked')) {
				not = true;
		    }else{
		    	not = false;
		    }
		    object.not = not;
		    object.rowIndex = rowIndex;
		    object.discountSeqNo = $('#sheepItForm_' + rowIndex + '_ruleNo').text();
		    rateProfileDiscounts.push(object);
            $("#rateProfileDiscountData").val(JSON.stringify(rateProfileDiscounts));
        }
    });
}

function getRateProfileDiscountData(){
	var profileId = jQuery("#profileId", "#manageListRateContainer").val();
	var ajaxReq = new AjaxRequest();
	ajaxReq.url = '../manageRateProfile/getProfileDiscounts.action?rateProfileId=' + profileId ;
	ajaxReq.success = function(result, status, xhr){
		var returnedJson = result.objectMap.gridKeyColumnValue;
		if (returnedJson != "") {
			$('#seasonalDiscounTableContainer', '#sheepItForm').show();
	    	var discounts = eval(returnedJson);
	        $.each(discounts, function(index, item){
	        	displayDiscountsOfRateProfile(item);
	        });
	     }
	};
	ajaxReq.submit();
}

var ruleIndex = 1;
function displayDiscountsOfRateProfile(seasonalDiscount){
	sheepItForm.addForm();
	var lastRowId = $("#seasonalDiscounTable" , "#sheepItForm").find("tr:last").prev('tr').attr("id");
	var rowName = "sheepItForm_template";
	var rowId = lastRowId.substring(rowName.length);
	var form = sheepItForm.getForm(rowId);
	var ruleNo = "Rule " + ruleIndex;
	var datesProposalLineItem = jQuery('#sheepItForm_' + rowId + '_startDate, #sheepItForm_' + rowId + '_endDate','#seasonalDiscounTable').datepicker({
        autoSize: true,
        showOn: "both",
        buttonText: "Select Date",
        buttonImage: '../images/calendar.gif',
        buttonImageOnly: true,
        numberOfMonths: 3,
        onSelect: function(selectedDate){
            var option = this.id.indexOf("startDate") >=0 ? "minDate" : "maxDate", instance = $(this).data("datepicker");
            date = $.datepicker.parseDate(instance.settings.dateFormat ||
            			$.datepicker._defaults.dateFormat, selectedDate, instance.settings);
            datesProposalLineItem.not(this).datepicker("option", option, date);
        }
	});
	$("#sheepItForm_" + rowId + "_startReset" , "#seasonalDiscounTable").bind("click", function(event, ui){
		$("#sheepItForm_" + rowId + "_startDate" , "#seasonalDiscounTable").val("");
		$("#sheepItForm_" + rowId + "_endDate" , "#seasonalDiscounTable").datepicker("option", "minDate",  new Date());
	});
	$("#sheepItForm_" + rowId + "_endReset" , "#seasonalDiscounTable").bind("click", function(event, ui){
		$("#sheepItForm_" + rowId + "_endDate" , "#seasonalDiscounTable").val("");
		$("#sheepItForm_" + rowId + "_startDate" , "#seasonalDiscounTable").datepicker("option", "maxDate", $("#sheepItForm_" + rowId + "_endDate" , "#seasonalDiscounTable").val());
	});
	
	form.inject({'seasonalDiscount' : seasonalDiscount.discount});
	$("#sheepItForm_" + rowId + "_profileId" , "#seasonalDiscounTable").text(seasonalDiscount.discountId);
	$("#sheepItForm_" + rowId + "_ruleNo" , "#seasonalDiscounTable").text(ruleNo);
	$("#sheepItForm_" + rowId + "_startDate" , "#seasonalDiscounTable").val(seasonalDiscount.startDate);
	$("#sheepItForm_" + rowId + "_endDate" , "#seasonalDiscounTable").val(seasonalDiscount.endDate);
	$("#sheepItForm_" + rowId + "_endDate" , "#seasonalDiscounTable").datepicker("option", "minDate", $("#sheepItForm_" + rowId + "_startDate" , "#seasonalDiscounTable").val());
	$("#sheepItForm_" + rowId + "_startDate" , "#seasonalDiscounTable").datepicker("option", "maxDate", $("#sheepItForm_" + rowId + "_endDate" , "#seasonalDiscounTable").val());
	var months = new Array('01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12');
	var CurrentDate = new Date();
	var todayDate = months[CurrentDate.getMonth()] + "/" + CurrentDate.getDate() + "/" + CurrentDate.getFullYear();
	if(seasonalDiscount.startDate < todayDate){
		$("#sheepItForm_" + rowId + "_startDate" , "#seasonalDiscounTable").datepicker("option", "minDate", seasonalDiscount.startDate);
	}else{
		$("#sheepItForm_" + rowId + "_startDate" , "#seasonalDiscounTable").datepicker("option", "minDate", new Date());
	}
	if(seasonalDiscount.not == true){
		$("#sheepItForm_" + rowId + "_not_seasonalDiscount" ,"#seasonalDiscounTable").attr('checked', true);
	}else{
		$("#sheepItForm_" + rowId + "_not_seasonalDiscount" ,"#seasonalDiscounTable").attr('checked', false);
	}
	ruleIndex = ruleIndex + 1;
}

function cloneDiscount(){
	$("#cloneDiscountSearchFieldsCont" , "#cloneDiscountDetails").show();
    var isRowSelected = false;
    var rowCount = 0;
    var discountDataSavedCount = 0;
    $("#seasonalDiscounTable", "#listRateDetail").find(":checkbox[name=sheepItForm_clone_seasonalDiscount]").each(function(){
    	if ($(this).is(":checked")) {
    		rowCount = rowCount + 1;
            isRowSelected = true;
            var parendId = $(this).parent().parent().parent().attr("id");
            var rowName = "sheepItForm_template";
        	var rowId = parendId.substring(rowName.length);
        	var discountId = $("#sheepItForm_" + rowId + "_profileId" , "#seasonalDiscounTable").text();
        	if(discountId != '0'){
        		discountDataSavedCount = discountDataSavedCount + 1;
        	}
        }
    });
    
    if (isRowSelected) {
        if(discountDataSavedCount == rowCount){
        	var profileId = jQuery("#profileId", "#manageListRateContainer").val();
        	var productId = "";
        	var ajaxReq = new AjaxRequest();
        	ajaxReq.dataType = 'html';
        	ajaxReq.url = "../manageRateProfile/getRateProfileData.action?salesCategoryId=" + $("#allSalesCategory", "#manageListRateContainer").val() + "&profileId=" + profileId + "&productId=" + productId;
        	ajaxReq.success = function(result, status, xhr){
        		$("#cloneDiscountDataSummary").html(result);
        		
        		var cloneDiscountDlg = new ModalDialog();
        		cloneDiscountDlg.width = 1100;
        		cloneDiscountDlg.height = 635;
        		cloneDiscountDlg.title = 'Clone Seasonal Discounts';
        		cloneDiscountDlg.resizable = false;
        		cloneDiscountDlg.draggable = true;
        		cloneDiscountDlg.closeOnEscape = false;
        		cloneDiscountDlg.buttons = [{
        	        text: "Apply",
        	        click: function(){
        	        	$(".ui-dialog-buttonset button:contains('Clone')").button("disable");
        	        	cloneSelectedDiscounts();
        	    	}
        	    }, {
        	        text: "Cancel",
        	        click: function(){			
        	            $(this).dialog("close");
        	        }
        	    }];
        		
                $("#cloneDiscountDetails").dialog(cloneDiscountDlg);
                $("#cloneDiscountDetails").dialog("open");
                enableDialogButton("cloneDiscountDetails");
                resizeRateProfileDataTable();
                populateDiscountsToBeCloned();
        		$(".ui-dialog-buttonset button:contains('Clone')").show();
        		$(".ui-dialog-buttonpane #errorDiv").hide();
                $("#discountProductId", "#cloneDiscountDetails").val("");
                $("#discountSalesCategoryId", "#cloneDiscountDetails").val($("#allSalesCategory", "#manageListRateContainer").val());
                $("#discountProductId", "#cloneDiscountDetails").select2();
                $("#discountSalesCategoryId", "#cloneDiscountDetails").select2();
                
                $("#rateProfileDetailsTable tr").each(function(){
                	objId = $(this).attr("id");
	                $("#lookUpDiscounts_" + objId, "#rateProfileSummary").qtip({
	                    content: {
	                        // Set the text to an image HTML string with the correct src URL to the loading image you want to use
	                        text: '<img src="../images/ajax-loader.gif" height="200px" width="650px" alt="Loading..." />',
	                        ajax: {
	                            url: '',
	            				type: 'POST',
	             				data: '', 
	                             once: true
	                         },
	                         title: {
	                             text: '<div style="height:18px;"><span style="float:left;"></span><span style="padding:4px 0 0 3px; float:left;">' + resourceBundle['label.generic.rate.profile.seasonalDiscount'] + '</span></div>',
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
                });
                
        	}
        	ajaxReq.submit();
        } else {
			jQuery("#runtimeDialogDiv").model({theme: 'Error', autofade: false, message: resourceBundle['error.discount.data.not.saved']});
        }
    } else {
		jQuery("#runtimeDialogDiv").model({theme: 'Error', autofade: false, message: resourceBundle['error.no.discount.selected.for.cloning']});
    }
}


function cloneSelectedDiscounts(){
	$(".ui-dialog-buttonpane #errorDiv").hide();
	var isRowSelected = false;
	var rateProfileIds = "";
	$("#rateProfileDetailsTable", "#cloneDiscountDiv").find(":checkbox[name=rateProfile]").each(function(){
	    if ($(this).is(":checked")) {
	    	isRowSelected = true;
	    	var rateProfileId = $(this).parent().parent().attr("id");
	    	if(rateProfileIds == ""){
				rateProfileIds = rateProfileId + "," ;
			}else{
				rateProfileIds = rateProfileIds +  rateProfileId + ",";;
			}
	    }
	});
	
	rateProfileIds = rateProfileIds.substring(0 , rateProfileIds.length - 1);
	if(isRowSelected){
		var discountIds = "";
		$("#seasonalDiscounTable", "#listRateDetail").find(":checkbox[name=sheepItForm_clone_seasonalDiscount]").each(function(){
	    	if ($(this).is(":checked")) {
	            var parendId = $(this).parent().parent().parent().attr("id");
	            var rowName = "sheepItForm_template";
	        	var rowId = parendId.substring(rowName.length);
	        	var discountId = $("#sheepItForm_" + rowId + "_profileId" , "#seasonalDiscounTable").text();
	        	if(discountId != '0'){
	        		if(discountIds == ""){
	        			discountIds = discountId + "," ;
	        		}else{
	        			discountIds = discountIds + discountId + ",";
	        		}
	        	}
	        }
	    });
	    
	    discountIds =  discountIds.substring(0 , discountIds.length - 1);
		var ajaxReq = new AjaxRequest();
		ajaxReq.url = "../manageRateProfile/copySeasonalDiscounts.action?discountIds=" + discountIds + "&targetProfileIds=" + rateProfileIds;
		ajaxReq.success = function(result, status, xhr){
			jQuery("#runtimeDialogDiv").model({theme: 'Success', message: resourceBundle['message.generic.discountClonedSuccessfully']});
			$("#cloneDiscountDetails").dialog('close');
			clearCSSErrors('#listRateDetail');
			if($("#clone_seasonalDiscount").is(':checked')){
				$("#clone_seasonalDiscount").attr('checked', false);
		  	}
			customDiscountSelect("#clone_seasonalDiscount");
		};
		ajaxReq.submit();
	}else{
		if ( $("#errorDiv", "div[aria-labelledby=ui-dialog-title-cloneDiscountDetails]").length == 0) {
			$(".ui-dialog-buttonpane", "div[aria-labelledby=ui-dialog-title-cloneDiscountDetails]").append("<div id='errorDiv' class='model-dialog-error'>" + resourceBundle['error.no.rateProfile.selected.for.cloning'] + "</div>");
		}
		$(".ui-dialog-buttonpane #errorDiv").show();
		$(".ui-dialog-buttonset button:contains('Clone')").button("enable");
	}
}

function searchRateProfiles(){
	$(".ui-dialog-buttonpane #errorDiv").hide();
	var profileId = jQuery("#profileId", "#manageListRateContainer").val();
	var salesCategoryId = $("#discountSalesCategoryId", "#cloneDiscountDetails").val();
	var productId = $("#discountProductId", "#cloneDiscountDetails").val();
	var ajaxReq = new AjaxRequest();
	ajaxReq.dataType = 'html';
	ajaxReq.url = "../manageRateProfile/getRateProfileData.action?salesCategoryId=" + salesCategoryId + "&profileId=" + profileId +"&productId=" + productId;
	ajaxReq.success = function(result, status, xhr){
		$("#cloneDiscountDataSummary").html(result);
		resizeRateProfileDataTable();
		$("#rateProfileDetailsTable tr").each(function(){
      		objId = $(this).attr("id");
            $("#lookUpDiscounts_" + objId, "#rateProfileSummary").qtip({
                content: {
                    // Set the text to an image HTML string with the correct src URL to the loading image you want to use
                    text: '<img src="../images/ajax-loader.gif" height="200px" width="650px" alt="Loading..." />',
                    ajax: {
                        url: '',
        				type: 'POST',
         				data: '', 
                         once: true
                     },
                     title: {
                         text: '<div style="height:18px;"><span style="float:left;"></span><span style="padding:4px 0 0 3px; float:left;">' + resourceBundle['label.generic.rate.profile.seasonalDiscount'] + '</span></div>',
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
        });
	}
	ajaxReq.submit();
}


function getDiscountData(profileId){
    $("#lookUpDiscounts", "#rateProfileDetailsTable").qtip("option", "content.ajax.url", "");
    $("#lookUpDiscounts_" + profileId, "#rateProfileDetailsTable").qtip("option", "content.text", "<img src='../images/ajax-loader.gif' height='20px' width='20px' alt='Loading...' />");
	var url = "../manageRateProfile/getRateProfileDiscountData.action?profileId=" + profileId ;
    $("#lookUpDiscounts_" + profileId, "#rateProfileDetailsTable").qtip("option", "content.ajax.url", url);
}


function selectUnselectDiscount(obj){
	if(!$(obj).is(':checked')){
		$("#clone_seasonalDiscount").attr('checked', false);
  	}
	if($('[name=sheepItForm_clone_seasonalDiscount]:not(:checked)',"#listRateDetail").size() == 0){
  		$("#clone_seasonalDiscount").attr("checked", true);
  	}	
}


function customDiscountSelect(select){
    if ($(select).is(":checked")) {
        $("#seasonalDiscounTable", "#listRateDetail").find(":checkbox[name=sheepItForm_clone_seasonalDiscount]").each(function(){
            $(this).attr("checked", "checked");
        });
    } else {
        $("#seasonalDiscounTable", "#listRateDetail").find(":checkbox[name=sheepItForm_clone_seasonalDiscount]").each(function(){
            $(this).attr("checked", false);
        });
    }
}

function deleteDiscount(){
	$("#addDiscounts" , "#sheepItForm").removeAttr("disabled");
	$("#cloneDiscountSearchFieldsCont" , "#cloneDiscountDetails").show();
    var isRowSelected = false;
    var rowNo = 0;
    ruleIndex = 1;
    $("#seasonalDiscounTable tr").each(function(){
		objId = $(this).attr("id");
		var ruleNo = "Rule " + ruleIndex;
		if (objId != "sheepItForm_noforms_template"  && objId != "seasonalDiscountHead") { 
			var rowId = objId.substring(Number(objId.length - 1));
        	if($("#sheepItForm_" + rowId + "_clone_seasonalDiscount" , "#seasonalDiscounTable").is(":checked")){
        		isRowSelected = true;
        		sheepItForm.removeForm(rowNo);
        	}else{
        		rowNo = rowNo + 1;
        		$("#sheepItForm_" + rowId + "_ruleNo" , "#seasonalDiscounTable").text(ruleNo);
        		ruleIndex = ruleIndex + 1;
        	}
        }
	});
    
    if (!isRowSelected){
		jQuery("#runtimeDialogDiv").model({theme: 'Error', autofade: false, message: resourceBundle['error.no.discount.selected.for.deletion']});
    }
    
    if($("#clone_seasonalDiscount").is(':checked')){
		$("#clone_seasonalDiscount").attr('checked', false);
  	}
	customDiscountSelect("#clone_seasonalDiscount");
	
	if(!sheepItForm.hasForms()){
		$("#sheepItForm_noforms_template" , "#seasonalDiscounTable").show();
	}
}

function populateDiscountsToBeCloned(){
	$('#selectedDiscountsTable' , '#cloneDiscountDetails').find('tr').each(function(){
		rowId = $(this).attr("id");
		if(rowId != "selectedDiscountsTableHeader"){
			$("#" + rowId).remove();
		}
	});
	$("#seasonalDiscounTable", "#listRateDetail").find(":checkbox[name=sheepItForm_clone_seasonalDiscount]").each(function(){
    	if ($(this).is(":checked")) {
            var parendId = $(this).parent().parent().parent().attr("id");
            var rowName = "sheepItForm_template";
        	var rowId = parendId.substring(rowName.length);
        	var not = "N";
        	var discountId = $("#sheepItForm_" + rowId + "_profileId" , "#seasonalDiscounTable").text();
        	if ($('#sheepItForm_' + rowId + '_not_seasonalDiscount').is(':checked')) {
				not = "Y";
		    }else{
		    	not = "N";
		    }
        	
        	var valueToPrint = "<tr id='"+ discountId + "' ><td class='fval' style='text-align: center' width='10%'>" + not + "</td>"
    		+"<td class='fval' style='text-align: center' width='25%'>" + $("#sheepItForm_" + rowId + "_startDate" , "#seasonalDiscounTable").val() + "</td>"
    		+ "<td class='fval' style='text-align: center' width='25%'>" + $("#sheepItForm_" + rowId + "_endDate" , "#seasonalDiscounTable").val() + "</td>"
    		+"<td class='fval' style='text-align: center' width='25%'>" + $("#sheepItForm_" + rowId + "_seasonalDiscount" , "#seasonalDiscounTable").val() + "</td></tr>";
        	$('#selectedDiscountsTable' , '#cloneDiscountDetails').append(valueToPrint);
        }
    });
}


function resizeRateProfileDataTable(){
	if($("#rateProfileDetailsTable").height() > 200){
    	$("#rateProfileSummary .summary-table-header").width(1063);
	} else{
		$("#rateProfileSummary .summary-table-header").width(1080);
	}
}