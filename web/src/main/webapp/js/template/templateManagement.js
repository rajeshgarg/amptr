/**
 * Java Script for proposal template management
 * 
 * @author rakesh.tiwari
 */
var errorflag = false;
jQuery(document).ready(function(){
    /**
     * Initialised template JQGrid
     */
    var templateGrid = new JqGridBaseOptions();
	templateGrid.url = '../templateManagement/loadgriddata.action';
    templateGrid.colNames = ['Id', 'File Name', 'Template Name', 'Last Updated', 'Updated By', 'Download', 'UseExistingRow'];
    templateGrid.colModel = [
		{name: 'templateId', index: 'templateId', hidden: true, key: true},
		{name: 'templateName', index: 'templateName', width:200},
		{name: 'templateFileName', index: 'templateFileName', width:200},
		{name: 'lastUpdated', index: 'lastUpdated',sortable:false, width:100},
		{name: 'updatedBy', index: 'updatedBy', width:100},
		{name: 'downloadString', index:'downloadString', sortable:false, width:70},
		{name: 'useExistingRow', index: 'useExistingRow', hidden: true}
	];
    templateGrid.caption = resourceBundle['tab.generic.template.management'];
    templateGrid.pager = jQuery('#templateManagementPager');
    templateGrid.sortname = 'templateId';
    templateGrid.onSelectRow = function(id){
        jQuery('#templateManagementDetailFormTab-1 a', '#templateManagementContainer').show();
        jQuery('#templateManagementDetailFormTab-1 a', '#templateManagementContainer').text(resourceBundle['tab.generic.templatedetail']);
        jQuery('#templateId', '#templateManagementContainer').val(id);
        getSelectedTemplateData(id);
    };
    templateGrid.afterGridCompleteFunction = function(){
        var gsr = jQuery('#templateManagementTable').jqGrid('getGridParam', 'selrow');
        if (gsr != null) {
            jQuery('#templateManagementDetailContainer', '#templateManagementContainer').show();
        }
        else {
            jQuery('#templateManagementDetailContainer', '#templateManagementContainer').hide();
        }
    };
    jQuery('#templateManagementTable').jqGrid(templateGrid).navGrid('#templateManagementPager', {
        search: false, edit: false,
        addfunc: function(rowid){
			resetFormField();
            clearCSSErrors('#template-tabs-1');
            jQuery('#templateManagementDetailContainer', '#templateManagementContainer').show();
            resetRowSelectionOnAdding('#templateManagementTable', '#templateManagementContainer');
            jQuery('#templateManagementDetailFormTab-1 a', '#templateManagementContainer').text(resourceBundle['tab.generic.addnewtemplate']);
            jQuery('#template_upload_table > tbody > tr[id^=template_upload_table_]').remove();
			jQuery('#template_upload_table_pro > tbody > tr[id^=template_upload_table_]').remove();
            jQuery('#templateUploadDiv', '#templateManagementContainer').show();
            jQuery('#buttonDiv', "#templateManagementContainer").hide();
            jQuery('#templateTableDiv', '#templateManagementContainer').hide();
            jQuery('#customTemplateFile', '#templateManagementContainer').val("");
            jQuery('#useExistingRow', '#templateManagementForm').val(false);
        },
        delfunc: function(rowid){
            if (rowid == '0') {
				jQuery("#runtimeDialogDiv").model({message: resourceBundle['error.select.record.delete'], theme: 'Error', autofade: false});
            }
            else {
  			  jQuery("#templateManagementTable").jqGrid('setGridParam', {page: 1});
                jQuery('#templateManagementTable').delGridRow(rowid, {
                    url: '../templateManagement/deleteCustomTemplate.action?templateId=' + rowid
                });                
            }
        },
        beforeRefresh: function(){
			jQuery('#templateSearchOption', '#templateManagementContainer').val('templateFileName');
			jQuery('#templateSearchValue', '#templateManagementContainer').val('');
            reloadJqGridAfterAddRecord('templateManagementTable', 'templateId');
        }
    });
	
	/**
	 * Initialised template detail tabs
	 */
    jQuery('#templateManagementTab', '#templateManagementContainer').tabs();
	
	/**
	 * Initialised grid search and enable auto search
	 */
	initGridSearchOptions('templateManagementTable', 'templateSearchPanel', 'templateManagementContainer');
	
	enableAutoSearch(jQuery('#templateSearchValue', '#templateManagementContainer'), function() {
		jQuery('#templateManagementTable').jqGrid('setGridParam', {
			url: '../templateManagement/loadgriddata.action', page: 1,
			postData: {
				searchField: jQuery('#templateSearchOption', '#templateManagementContainer').val(),
				searchString: jQuery('#templateSearchValue', '#templateManagementContainer').val(),
				searchOper: 'cn'
			}
		}).trigger('reloadGrid');
	});
	
	jQuery('#templateSearchOption', '#templateManagementContainer').bind('change', function () {
		jQuery('#templateSearchValue', '#templateManagementContainer').val('').focus();
	});	
});

/**
 * Cancel template upload
 */
function cancelCustomTemplate(){
	resetFormField();
    jQuery('#template_upload_table > tbody > tr[id^=template_upload_table_]').remove();
    jQuery('#template_upload_table_pro > tbody > tr[id^=template_upload_table_]').remove();
    jQuery('#customTemplateFile', '#templateManagementContainer').val('');
    jQuery('#templateId', '#templateManagementContainer').val('');
    jQuery('#templateUploadDiv', '#templateManagementContainer').hide();
    var gsr = jQuery('#templateManagementTable').jqGrid('getGridParam', 'selrow');
    if (gsr) {
        var rowData = jQuery('#templateManagementTable').getRowData(gsr);
        var templateId = rowData['templateId'];
        getSelectedTemplateData(templateId);
        jQuery('#templateManagementDetailFormTab-1 a', '#templateManagementContainer').text(resourceBundle['tab.generic.templatedetail']);
    }
    else {
        jQuery('#templateManagementTable').trigger('reloadGrid');
    }
}

/**
 * 
 */
function resetUploadedTemplate(){
    var gsr = jQuery('#templateManagementTable').jqGrid('getGridParam', 'selrow');
    if (gsr != null) {
        var rowData = jQuery('#templateManagementTable').getRowData(gsr);
        var templateId = rowData['templateId'];
        jQuery('#templateId', '#templateManagementContainer').val(templateId);
        getSelectedTemplateData(templateId);
    }
    else {
        jQuery('#templateId', '#templateManagementContainer').val(0);
        resetCustomTemplatefield('_pro');
		resetCustomTemplatefield('');
		$("#useExistingRowCB", "#templateManagementForm").prop('checked', false);
    }
}

/**
 * 
 * @param {Object} tableType
 */
function resetCustomTemplatefield(tableType){
    var tableRowCount = jQuery('#template_upload_table' + tableType + ' > tbody > tr', '#templateManagementContainer').length;
	var blanOption = '<option value="" >' + resourceBundle['label.generic.blankSelectOption'] + '</option>';
    clearCSSErrors('#templateTableDiv');
    for (var rowCount = 1; rowCount < tableRowCount; rowCount++) {
        var selectProposalAttribute = jQuery('#templateProposalAttribute' + tableType + '_' + rowCount, '#template_upload_table' + tableType);
        jQuery('option', selectProposalAttribute).remove();
        jQuery(selectProposalAttribute).append(blanOption);
        var selectProposalName = jQuery('#templateProposalName' + tableType + '_' + rowCount, '#template_upload_table' + tableType);
        jQuery(selectProposalName).val("");
    }
}

/**
 * 
 */
function validateCustomTemplate(){
    var templateId = 0;
    var gsr = jQuery('#templateManagementTable').jqGrid('getGridParam', 'selrow');
    if (gsr) {
        var rowData = jQuery('#templateManagementTable').getRowData(gsr);
        templateId = rowData['templateId'];
    }
	jQuery('#templateId', '#templateManagementContainer').val(templateId);
    var instanceFormAdManager = new FormManager();
    instanceFormAdManager.formName = 'templateManagementForm';
    var validateCustomTemplateForm = new JQueryAjaxForm(instanceFormAdManager);
    jQuery('#templateManagementForm', '#templateManagementContainer')
			.attr('action', '../templateManagement/validateNewDocument.action');
    validateCustomTemplateForm.dataType = 'xml';
    validateCustomTemplateForm.doCustomProcessingAfterFormSucsessXML = function(){
        uploadCustomTemplate(templateId);
    };
    validateCustomTemplateForm.submit(templateId);
}

/**
 * 
 * @param {Object} templateId
 */
function uploadCustomTemplate(templateId){
    jQuery('#template_upload_table > tbody > tr[id^=template_upload_table_]').remove();
    var instanceFormAdManager = new FormManager();
    instanceFormAdManager.formName = 'templateManagementForm';
    var uploadCustomTemplateForm = new JQueryAjaxForm(instanceFormAdManager);
    jQuery('#templateManagementContainer #templateManagementForm').attr('action', '../templateManagement/uploadCustomTemplate.action?templateId=' + templateId);
    uploadCustomTemplateForm.dataType = 'html';
    uploadCustomTemplateForm.doCustomProcessingAfterFormSucsessHTML = function(responseText, statusText, XMLHTTPRequest){
        jQuery('#customTemplateFile', '#templateManagementContainer').val("");
        jQuery('#templateUploadDiv', '#templateManagementContainer').hide();
        jQuery('#buttonDiv').show();
        jQuery('#templateTableDiv', '#templateManagementContainer').show();
        jQuery('#templateTableDiv').html(responseText);
        if(jQuery('#tmpFile','#templateTableDiv').val() != ''){
	        jQuery('#templateName', '#uploadFileInfoTable').val(jQuery('#fileName', '#uploadFileInfoTable').val());
	        if (templateId > 0) {
	            /*
	             * Edit condition based on this move template from temp dir to template dir
	             */
	            jQuery('#editAction', '#templateManagementContainer').val(true);
				jQuery('#editUplodedTemplate', '#templateManagementContainer').show();
	        }
	        else {
				jQuery('#editUplodedTemplate', '#templateManagementContainer').hide();
				jQuery('#editAction', '#templateManagementContainer').val(false);
	        }
			jQuery("#runtimeDialogDiv").model({theme: 'Success', message: resourceBundle['message.generic.template.upload.successfully']});
		}else{
			jQuery("#runtimeDialogDiv").model({theme: 'Error', autofade: false, message: resourceBundle['error.template.upload'], buttons: [{
                text: "Ok", click: function(){
                    $(this).dialog("close");
                    jQuery('#templateManagementTable').trigger('reloadGrid');
                }
            }]});
		}
    };
    uploadCustomTemplateForm.submit();
}

/**
 * Read table data and create JSON string
 * @return
 */
function submitForm(){
    errorflag = false;
    clearCSSErrors('#templateTableDiv');
    var valueTableArray = new Array();
    valueTableArray = addTemplate(valueTableArray, '_pro');
    valueTableArray = addTemplate(valueTableArray, '');
    if (!errorflag) {
        saveCustomTemplate(valueTableArray);
    }
    else {
		jQuery("#tabs").parent().animate({ scrollTop: 0 }, "slow");
    }
}

/**
 * 
 * @param {Object} valueTableArray
 * @param {Object} tableType
 */
function addTemplate(valueTableArray, tableType){
	var tableRowCount = jQuery('#template_upload_table' + tableType + ' > tbody > tr', '#templateManagementContainer').length;
    for (var rowCount = 1; rowCount < tableRowCount; rowCount++) {
        var object = new Object();
        object.tokenName = jQuery('#templateAttribute' + tableType + '_' + rowCount, '#template_upload_table' + tableType).text();
	    if (jQuery('#templateProposalName' + tableType + '_' + rowCount, '#template_upload_table'+ tableType ).val() != "" &&
		             jQuery('#templateProposalName' + tableType + '_' + rowCount, '#template_upload_table' + tableType).val() != resourceBundle['label.generic.blankSelectOption']) {
            object.proposalHead = jQuery('#templateProposalName' + tableType + '_' + rowCount, '#template_upload_table' + tableType).val();
        }
        else {
            jQuery('#messageHeaderDiv', '#templateManagementForm').html(resourceBundle['validation.error']).addClass('header-error');
            jQuery('#templateProposalName' + tableType + '_' + rowCount, '#templateTableDiv').addClass('errorElement errortip').attr('title', 'Proposal head is mandatory.' + '<BR><b>Help:</b> ' + resourceBundle['message.generic.head.field']);
            renderErrorQtip('#templateManagementContainer');
            errorflag = true;
        }
        if (jQuery('#templateProposalAttribute' + tableType + '_' + rowCount, '#template_upload_table' + tableType).val() != "" && jQuery('#templateProposalAttribute' + tableType + '_' + rowCount, '#template_upload_table' + tableType).val() != resourceBundle['label.generic.blankSelectOption']) {
            object.proposalAttribute = jQuery('#templateProposalAttribute' + tableType + '_' + rowCount, '#template_upload_table' + tableType).val();
        }
        else {
            jQuery('#messageHeaderDiv', '#templateManagementForm').html(resourceBundle['validation.error']).addClass('header-error');
            jQuery('#templateProposalAttribute' + tableType + '_' + rowCount, '#templateTableDiv').addClass('errorElement errortip').attr("title", 'Proposal attribute is mandatory.' + '<BR><b>Help:</b> ' + resourceBundle['message.generic.attribute.field']);
            renderErrorQtip("#templateTableDiv");
            errorflag = true;
        }
		object.fontSize = jQuery('#templateAttributeFontSize' + tableType + '_' + rowCount, '#template_upload_table' + tableType).val();
        
        object.rowNum = jQuery('#templateSheetRowNum' + tableType + '_' + rowCount, '#template_upload_table' + tableType).val();
        object.colNum = jQuery('#templateSheetColNum' + tableType + '_' + rowCount, '#template_upload_table' + tableType).val();
        object.templateMetaDataAttributesID = jQuery('#templateMetaDataAttributesID' + tableType + '_' + rowCount, '#template_upload_table' + tableType).val();
        valueTableArray.push(object);
    }
    return valueTableArray;
}

/**
 * Save custom template data
 * @param valueTableArray
 * @return
 */
function saveCustomTemplate(valueTableArray){
    var rowid = jQuery('#templateManagementTable').jqGrid('getGridParam', 'selrow');
    if (rowid != null) {
        jQuery('#templateId', '#templateManagementContainer').val(rowid);
    }
    else {
        jQuery('#templateId', '#templateManagementContainer').val(0);
    }
    var editAction = jQuery('#editAction', '#templateManagementContainer').val();
    var myJsonString = JSON.stringify(valueTableArray);
    jQuery('#templateJsonData', '#templateManagementForm').val(myJsonString);
	
	if($("#useExistingRowCB", "#templateManagementForm").attr('checked')){
		jQuery('#useExistingRow', '#templateManagementForm').val(true);
	}else {
		jQuery('#useExistingRow', '#templateManagementForm').val(false);
	}
	
	jQuery('#sheetName', '#templateManagementContainer').val(jQuery('#hiddenSheetName', '#templateManagementForm').val());
	var instanceFormAdManager = new FormManager();
    instanceFormAdManager.formName = 'templateManagementForm';
    var saveCustomTemplateForm = new JQueryAjaxForm(instanceFormAdManager);
    jQuery('#templateManagementForm', '#templateManagementContainer').attr('action', '../templateManagement/saveCustomTemplate.action');
    saveCustomTemplateForm.dataType = 'xml';
    saveCustomTemplateForm.doCustomProcessingAfterFormSucsessXML = function(responseXml, XMLHTTPRequest){
     if (XMLHTTPRequest.responseText.indexOf("errorObject") >= 0){
	     var responseText = XMLHTTPRequest.responseText;
	     responseText = responseText.replace('<pre>', '');
	     responseText = responseText.replace('</pre>', '');
	     XMLHTTPRequest.responseText = responseText;
     	 handelIntegrationExceptionOnServer(XMLHTTPRequest);
     }else{
        jQuery("#runtimeDialogDiv").model({theme: 'Success', message: resourceBundle['message.generic.TemplateSavedSuccessfully']});
        jQuery('#templateJsonData', '#templateManagementForm').val('');
        if (jQuery('#templateId', '#templateManagementContainer').val() == 0) {
            jQuery('#templateManagementTable').trigger('reloadGrid');
        }
        else {
			var gsr = jQuery('#templateManagementTable').jqGrid('getGridParam', 'selrow');
            jQuery('#templateManagementTable').setCell(gsr, "templateFileName", jQuery('#templateName', '#uploadFileInfoTable').val());
			if(jQuery('#editAction', '#templateManagementContainer').val()=='true'){
				 getSelectedTemplateData(jQuery('#templateId', '#templateManagementContainer').val());
			}
        }
		jQuery('#templateManagementTable').setCell(rowid, "useExistingRow", jQuery('#useExistingRow', '#templateManagementForm').val());
        jQuery('#editAction', '#templateManagementContainer').val(false);
		jQuery('#templateManagementDetailFormTab-1 a', '#templateManagementContainer').text(resourceBundle['tab.generic.templatedetail']);
		}
    };
    saveCustomTemplateForm.submit();
}

/**
 * 
 * @param {Object} objectVar
 * @param {Object} attributeTypeKey
 * @param {Object} type
 */
function getProposalAttribute(objectVar, attributeTypeKey, type){
    var selectAttribute = jQuery('#templateProposalAttribute_' + objectVar, '#templateTableDiv');
    var selectHead = jQuery('#templateProposalName_' + objectVar, '#templateTableDiv');
    if (type == 'proposal') {
        selectAttribute = jQuery('#templateProposalAttribute_pro_' + objectVar, '#templateTableDiv');
        selectHead = jQuery('#templateProposalName_pro_' + objectVar, '#templateTableDiv');
    }
    if (jQuery(selectHead).val() == "") {
        jQuery('option', selectAttribute).remove();
        jQuery(selectAttribute).append('<option value="" >--Select--</option>');
    }
    else {
        dynaLoadSelection(attributeTypeKey, selectAttribute, '../templateManagement/getProposalAttribute.action?headId=' + jQuery(selectHead).val());
    }
}

/**
 * 
 * @param {Object} id
 */
function getSelectedTemplateData(id){
  	jQuery('#templateId', '#templateManagementContainer').val(id);
    var instanceFormAdManager = new FormManager();
    instanceFormAdManager.formName = 'templateManagementForm';
    var uploadSelectedTemplateDataForm = new JQueryAjaxForm(instanceFormAdManager);
    jQuery('#templateManagementContainer #templateManagementForm').attr("action", '../templateManagement/getSelectedTemplateData.action');
    uploadSelectedTemplateDataForm.dataType = 'html';
    uploadSelectedTemplateDataForm.doCustomProcessingAfterFormSucsessHTML = function(responseText, statusText, XMLHTTPRequest){
        jQuery('#templateUploadDiv', '#templateManagementContainer').hide();
        jQuery('#buttonDiv').show();
        jQuery(' #templateTableDiv', '#templateManagementContainer').show();
        jQuery('#templateTableDiv').html(responseText);
        jQuery('#editAction', '#templateManagementContainer').val(false);
    };
    uploadSelectedTemplateDataForm.submit();
}

/**
 * 
 */
function editTemplate(){
	jQuery('#template_upload_table > tbody > tr[id^=template_upload_table_]').remove();
	jQuery('#template_upload_table_pro > tbody > tr[id^=template_upload_table_]').remove();
	jQuery('#templateManagementDetailContainer', '#templateManagementContainer').show();
    jQuery('#templateManagementDetailFormTab-1 a', '#templateManagementContainer').text('Edit Template');
    jQuery('#templateUploadDiv', '#templateManagementContainer').show();
    jQuery('#buttonDiv', "#templateManagementContainer").hide();
    jQuery(' #templateTableDiv', '#templateManagementContainer').hide();
    jQuery('#customTemplateFile', '#templateManagementContainer').val("");
	jQuery('#messageHeaderDiv', '#templateManagementForm').html('');
	var gsr = jQuery('#templateManagementTable').jqGrid('getGridParam', 'selrow');
    if (gsr != null) {
		var useExistingRowVal = jQuery('#templateManagementTable').jqGrid('getCell', gsr, 'useExistingRow');
		jQuery('#useExistingRow', '#templateManagementForm').val(useExistingRowVal);
	}
}


/**
 * Method used to display Help for Templates
 */
var childHelpWindow = "";
function viewTemplateHelp(){
	if (childHelpWindow == "") {
		childHelpWindow = "templateHelp";
		window["templateHelp"] = openNewHelpWindow('templateHelp');
	}
	else {
		try {
			if (window["templateHelp"].closed) {
				window["templateHelp"] = openNewHelpWindow('templateHelp');
			}
			else {
				window["templateHelp"].focus();
			}
		} 
		catch (e) {
			openNewHelpWindow('templateHelp');
		}
	}
}

function openNewHelpWindow(windowId){	
	var url = "../templateManagement/getTemplateHelp.action";
	var width = screen.width - 100;
	var height = screen.height - 100;
	return window.open(url, windowId, 'left=50,top=0,width='+width+',height='+height+',toolbar=0,resizable=1,menubar=0,scrollbars=1,location=no');
}

/**
 * set following fields when add new template and cancel upload    
 */

function resetFormField(){
    jQuery('#templateJsonData', '#templateManagementContainer').val('');
    jQuery('#templateId', '#templateManagementContainer').val(0);
    jQuery('#sheetID', '#templateManagementContainer').val('');
    jQuery('#sheetName', '#templateManagementContainer').val('');
}