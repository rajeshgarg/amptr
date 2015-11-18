/**
 * 
 */
jQuery(document).ready(function() {
	
	$("#product", "#manageEmailScheduleContainer").select2();
	
	$("#salesTarget", "#manageEmailScheduleContainer").select2();

	var emailScheduleGridOpts = new JqGridBaseOptions();
	emailScheduleGridOpts.colNames = ['scheduleDetailId', 'Start Date', 'End Date', 'Weekday(s)', 'Frequency', '', '','',''];
	emailScheduleGridOpts.colModel = [
		{name:'scheduleDetailId', index:'scheduleDetailId', hidden:true, key:true},
		{name:'emailStartDate', index:'emailStartDate', key:false},
		{name:'emailEndDate', index:'emailEndDate', key:false},
		{name:'weekDaysStr', index:'weekDaysStr', sortable:false, key:false},
		{name:'frequencyStr', index:'frequency', sortable:true, key:false},		
		{name:'weekDays', index:'weekDays', hidden:true, key:false},
		{name:'scheduleId', index:'scheduleId', hidden:true, key:false},
		{name:'frequency', index:'frequency', hidden:true, key:false},
		{name:'ends', index:'ends', hidden:true, key:false}
	];
	emailScheduleGridOpts.caption = "Scheduled Dates";
	emailScheduleGridOpts.pager = jQuery('#emailSendSchedulePager');
	emailScheduleGridOpts.sortname = "emailStartDate";
	emailScheduleGridOpts.sortorder = 'asc';
	emailScheduleGridOpts.afterGridCompleteFunction = function (){
		var topRowId = jQuery("#manageEmailSendScheduleTable").getDataIDs()[0];
		if(topRowId == '' || topRowId == undefined){
			jQuery('#emailSendScheduleDetailsContainer', '#manageEmailScheduleContainer').hide();
		}
		fetchEmailScheduleDates();
	};

	emailScheduleGridOpts.onSelectRow = function(id){
		setGridDataToForm(id);
	};
	jQuery("#manageEmailSendScheduleTable").jqGrid(emailScheduleGridOpts).navGrid('#emailSendSchedulePager', {edit: false, search: false,
		addfunc: function(rowid){
			if($("#salesTarget","#manageEmailScheduleContainer").val() == null || $("#product","#manageEmailScheduleContainer").val() == null){
				jQuery("#runtimeDialogDiv").model({ message: resourceBundle['error.productAndSales.check'], theme: 'Error', autofade: false});
				return;
			}
			setFormDataForAdd();
		},
		delfunc: function(rowid) {
			var scheduleDetailId = jQuery("#scheduleDetailId", "#emailSendScheduleDetail").val();
			if (scheduleDetailId == '0') {
				jQuery("#runtimeDialogDiv").model({message: "Please select a record to delete.", theme: 'Error', autofade: false});
			}
			else {
				jQuery("#manageEmailSendScheduleTable").jqGrid("setGridParam", {page: 1});
				jQuery("#manageEmailSendScheduleTable").delGridRow(scheduleDetailId, {
					url: "../emailSchedule/deleteEmailScheduleDetail.action?scheduleDetailId=" + scheduleDetailId
				});
			}
		},
		beforeRefresh: function() {
			jQuery("#emailScheduleSearchOption", "#manageEmailScheduleContainer").val("frequency");
			jQuery("#emailScheduleSearchValue", "#manageEmailScheduleContainer").val('');
			reloadJqGridAfterAddRecord('manageEmailSendScheduleTable', "scheduleDetailId");
		}
	});

	/**
	 * Initialised search panel on attribute grid
	 */
	initGridSearchOptions("manageEmailSendScheduleTable", "emailScheduleSearchPanel", "manageEmailScheduleContainer");
	
	/**
	 * Enable auto search on attribute grid
	 */
	enableAutoSearch(jQuery("#emailScheduleSearchValue", "#manageEmailScheduleContainer"), function() {
		var salesTargetId = $("#salesTarget option:selected", "#manageEmailScheduleContainer").val();
		var productId = $("#product option:selected", "#manageEmailScheduleContainer").val();
		jQuery("#manageEmailSendScheduleTable").jqGrid("setGridParam", {
			url: "../emailSchedule/loadGridData.action?salesTargetId=" + salesTargetId + "&productId=" + productId, page: 1,
			postData: {
				searchField: jQuery("#emailScheduleSearchOption", "#manageEmailScheduleContainer").val(),
				searchString: $.trim(jQuery("#emailScheduleSearchValue", "#manageEmailScheduleContainer").val()),
				searchOper: 'cn'
			}
		}).trigger("reloadGrid");
	});
	
	/**
	 * Bind change event on product dropdown box
	 */
    jQuery("#product", "#manageEmailScheduleContainer").bind("change", function(){
        jQuery("#emailScheduleSearchValue", "#manageEmailScheduleContainer").val('');
        var select = $("#salesTarget", "#manageEmailScheduleContainer");
        var productId = jQuery("#product", "#manageEmailScheduleContainer").val();
        if (productId == '') {
            $("option", select).remove();
            $("#manageEmailSendScheduleTable").jqGrid("clearGridData");
            $("#salesTarget", "#manageEmailScheduleContainer").select2("val", "-1").trigger("change");
        }
        else {
            var ajaxReq = new AjaxRequest();
            ajaxReq.url = "../emailSchedule/getSalesTargetList.action?productId=" + productId;
            ajaxReq.success = function(result, status, xhr){
                var options = "<option value=''></option>";
                $.each(sortMapDataByValue(result), function(i, value){
                    options += "<option value=" + value[1] + ">" + value[0] + "</option>";
                });
                $(select).html(options);
                $("#salesTarget", "#manageEmailScheduleContainer").select2("val", "-1").trigger("change");
            };
            ajaxReq.submit();
        }
    });

	var emailAvailableDatesArray;

	/**
	 * Bind change event on sales category dropdown box
	 */
	$("#salesTarget", "#manageEmailScheduleContainer").bind("change", function () {
		jQuery("#emailScheduleSearchValue", "#manageEmailScheduleContainer").val('');
		var productId = $("#product option:selected", "#manageEmailScheduleContainer").val();
		var salesTargetId = $("#salesTarget option:selected", "#manageEmailScheduleContainer").val();
		if (productId == '' || salesTargetId == '') {
			jQuery("#manageEmailSendScheduleTable").jqGrid('setGridParam', {
				url: '../emailSchedule/loadGridData.action?salesTargetId=' + -1 + "&productId=" + -1
			}).trigger("reloadGrid");
			$("#emailScheduleConfiguredDates", "#manageEmailScheduleContainer").datepicker("disable");
		}
		else {
			jQuery("#manageEmailSendScheduleTable").jqGrid('setGridParam', {
				url: '../emailSchedule/loadGridData.action?salesTargetId=' + salesTargetId + "&productId=" + productId
			}).trigger("reloadGrid");
			$("#emailScheduleConfiguredDates", "#manageEmailScheduleContainer").datepicker("enable");
			getEmailScheduleId();
		}
	});
	
	/**
	 * Configure date picker to show all configured dates for product - sales target combination
	 */
	jQuery("#emailScheduleConfiguredDates", "#manageEmailScheduleContainer").datepicker({
        showOn: "both", buttonImage: '../images/calendar.gif', buttonImageOnly: true, 
		numberOfMonths: 3, buttonText: "Scheduled Dates",
        beforeShowDay: function(date) {
			var string = jQuery.datepicker.formatDate('mm/dd/yy', date);
			return [emailAvailableDatesArray.indexOf(string) > -1];
        }
    }).datepicker("disable");

	/**
	 * Bind Click event on End Date radio button options
	 */
    $("#forever, #endOn", "#manageEmailScheduleContainer").click(function(){
        enableDisableEndDate();
    });

	/**
	 * Bind Click event for recurrence checkbox
	 */
    $("#recurrence", "#manageEmailScheduleContainer").click(function(){
        enableDisableRecurrence();
    });

	/**
	 * Configure date picker for start and end date
	 */
    jQuery("#emailStartDate, #emailEndDate", "#emailSendScheduleDetail").datepicker({
        showOn: "both", buttonText: "Select Date", buttonImage: '../images/calendar.gif',
        buttonImageOnly: true, numberOfMonths: 3
    });

	/**
	 * Bind click event for reset dates buttons
	 */
    $("#startFromReset", "#emailSendScheduleDetail").click(function(){
        jQuery.datepicker._clearDate('#emailStartDate', "#emailSendScheduleDetail");
    });

    $("#endFromReset", "#emailSendScheduleDetail").click(function(){
        jQuery.datepicker._clearDate('#emailEndDate', "#emailSendScheduleDetail");
    });
    
    var instanceFormManager = new FormManager();
	instanceFormManager.gridName = 'manageEmailSendScheduleTable';
	instanceFormManager.formName = 'emailSendScheduleDetail';

	jQuery('#emailScheduleSaveData', '#emailSendScheduleDetail').click(function() {
		removeEmailScheduleFormError();
		$("#productId", "#emailSendScheduleDetail").val($("#product").val());
		$("#salesTargetId", "#emailSendScheduleDetail").val($("#salesTarget").val());
		$("#productName", "#manageEmailScheduleContainer").val($("#product option:selected").text());
		$("#salesTargetName", "#manageEmailScheduleContainer").val($("#salesTarget option:selected").text());
		
		var emailScheduleDetailForm = new JQueryAjaxForm(instanceFormManager);
		emailScheduleDetailForm.doCustomProcessingAfterFormSucsesJson = function(jsonResponse, XMLHTTPRequest){
			jQuery("#runtimeDialogDiv").model({theme: 'Success', message: resourceBundle['message.generic.scheduleSavedSuccessfully']});
			var scheduleDetailId = jQuery("#scheduleDetailId", "#emailSendScheduleDetail").val();
			if (scheduleDetailId == 0) {
				reloadJqGridAfterAddRecord(instanceFormManager.gridName, "scheduleDetailId");
			}
			else {
				var returnedobj = jsonResponse.objectMap.gridKeyColumnValue;
				jQuery("#manageEmailSendScheduleTable","#manageEmailScheduleContainer").jqGrid('FormToGrid', scheduleDetailId, "#emailSendScheduleDetail");
                jQuery("#manageEmailSendScheduleTable", "#manageEmailScheduleContainer").setCell(scheduleDetailId, "emailEndDate", returnedobj.emailEndDate);
                jQuery("#manageEmailSendScheduleTable", "#manageEmailScheduleContainer").setCell(scheduleDetailId, "weekDaysStr", returnedobj.weekDaysStr);
                jQuery("#manageEmailSendScheduleTable", "#manageEmailScheduleContainer").setCell(scheduleDetailId, "weekDays", returnedobj.weekDays);
				jQuery("#manageEmailSendScheduleTable", "#manageEmailScheduleContainer").setCell(scheduleDetailId, "frequencyStr", returnedobj.frequencyStr);
				fetchEmailScheduleDates();
			}
		};
		emailScheduleDetailForm.submit();
	});

	jQuery("#emailScheduleResetData", "#emailSendScheduleDetail").click(function (){
		removeEmailScheduleFormError();
		if(jQuery("#scheduleDetailId", "#manageEmailScheduleContainer").val() == 0){
			setFormDataForAdd();
		} else {
			var gsr = jQuery("#manageEmailSendScheduleTable").jqGrid('getGridParam', 'selrow');
			setGridDataToForm(gsr);
		}
	});
	
	jQuery("#emailScheduleSearchOption", "#manageEmailScheduleContainer").bind("change", function() {
		jQuery("#emailScheduleSearchValue", "#manageEmailScheduleContainer").val('');
	});
	
	$("#emailSendScheduleTab", "#manageEmailScheduleContainer").tabs();
	
    function fetchEmailScheduleDates(){
		var productId = $("#product option:selected", "#manageEmailScheduleContainer").val();
		var salesTargetId = $("#salesTarget option:selected", "#manageEmailScheduleContainer").val();

        var ajaxReq = new AjaxRequest();
        ajaxReq.url = "../proposalWorkflow/getEmailAvailableDates.action?sosProductId=" + productId + "&sosSalesTargetId=" + salesTargetId;
        ajaxReq.success = function(result, status, xhr){
            emailAvailableDatesArray = result;
        }
        ajaxReq.submit();
    }
});

/**
 * 
 */
function enableDisableRecurrence () {
    if ($("#recurrence", "#manageEmailScheduleContainer").is(":checked")) {
        jQuery("#recurrenceFieldset", "#manageEmailScheduleContainer").find('input, select').removeAttr("disabled");
    }
    else {
		jQuery("select[id=frequency]", "#recurrenceFieldset").val('').attr("disabled", "disabled");
		jQuery("input[type=checkbox]", "#recurrenceFieldset").removeAttr("checked").attr("disabled", "disabled");
    }
}

function enableDisableEndDate() {
    if ($("#forever", "#manageEmailScheduleContainer").is(":checked")) {
        $("#emailEndDate", "#manageEmailScheduleContainer").val("");
        $("#emailEndDate", "#manageEmailScheduleContainer").datepicker("disable");
    }
    else {
        $("#emailEndDate", "#manageEmailScheduleContainer").datepicker("enable");
    }
}

function setGridDataToForm (id) {
	removeEmailScheduleFormError();
	if(id){
		jQuery("#manageEmailSendScheduleTable").jqGrid("GridToForm", id, "#emailSendScheduleDetail");
		var weekDays = jQuery("#manageEmailSendScheduleTable").getRowData(id)['weekDays'];
		if (weekDays == null || weekDays == '') {
			if ($("#recurrence", "#emailSendScheduleDetail").is(":checked")) {
				$("#recurrence", "#emailSendScheduleDetail").removeAttr("checked");
			}
			enableDisableRecurrence();
		} else {
			if (!$("#recurrence", "#emailSendScheduleDetail").is(":checked")) {
				$("#recurrence", "#emailSendScheduleDetail").attr("checked", "checked");
			}
			enableDisableRecurrence();
			$("input[name=weekDays]", "#emailSendScheduleDetail").val(weekDays.split(','));
		}
		var foreverData = jQuery("#manageEmailSendScheduleTable").getRowData(id)['ends'];
		if(foreverData == "forever") {
			jQuery("#forever", "#emailSendScheduleDetail").attr("checked", "checked");
		 	jQuery.datepicker._clearDate('#emailEndDate', "#manageEmailScheduleContainer");
		} else {
			 jQuery("#endOn", "#emailSendScheduleDetail").attr("checked", "checked");
		}
		enableDisableEndDate();
		jQuery("#emailSendScheduleDetailsContainer", "#manageEmailScheduleContainer").show();
	}
	jQuery("#emailSendScheduleFormTab-1 a").text(resourceBundle["tab.generic.emailSendScheduleDetail"]);
}

function setFormDataForAdd(){
    jQuery("#emailSendScheduleFormTab-1 a").text(resourceBundle["tab.generic.addNewEmailSchedule"]);
    jQuery("#emailSendScheduleDetailsContainer", "#manageEmailScheduleContainer").show();
    jQuery("#emailScheduleSearchValue", "#manageEmailScheduleContainer").val('');
    resetRowSelectionOnAdding("#manageEmailSendScheduleTable", "#scheduleId");
    removeEmailScheduleFormError();
	
    if ($("#recurrence", "#emailSendScheduleDetail").is(":checked")) {
        $("#recurrence", "#emailSendScheduleDetail").removeAttr("checked");
		enableDisableRecurrence();
    }
    jQuery.datepicker._clearDate('#emailStartDate', "#manageEmailScheduleContainer");
	jQuery.datepicker._clearDate('#emailEndDate', "#manageEmailScheduleContainer");
    jQuery("#scheduleDetailId", "#manageEmailScheduleContainer").val(0);
    jQuery("#forever", "#emailSendScheduleDetail").attr("checked", "checked");
	enableDisableEndDate();
}

function getEmailScheduleId(){
	var productId = jQuery("#product", "#manageEmailScheduleContainer").val();
	var salesTargetId = jQuery("#salesTarget", "#manageEmailScheduleContainer").val();
	if(salesTargetId == '' || productId == ''){
		return;
	}
	var ajaxReq = new AjaxRequest();
	ajaxReq.url = "../emailSchedule/getEmailScheduleId.action?productId="+productId +"&salesTargetId="+salesTargetId;
	ajaxReq.success = function(result, status, xhr){
		$("input[id=scheduleId]").val(result);
	};
	ajaxReq.submit();
}

function removeEmailScheduleFormError() {
	clearCSSErrors("#emailSendScheduleDetail");
    jQuery("#recurrenceFieldset", "#emailSendScheduleDetail").qtip("distroy").removeClass("errorElement errortip").removeAttr("title");
}