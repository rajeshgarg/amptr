/**
 * @author Manish.Kesarwani
 */
$(document).ready(function() {
	$("#proposalTemplate","#generateTemplateParentDiv").select2();
	$( "#downloadExcel" ).dialog({
		dialogClass: 'alert',
		autoOpen: false,
		resizable: false,
		showTitlebar: true,
		modal: true
	});
	$("#proposalTemplate option","#generateTemplateParentDiv").each(function () {
        if ($(this).text() == 'NYT_TIMES.xlsx') {
      	  $("#proposalTemplate","#generateTemplateParentDiv").val($(this).val()).trigger("change");
        }
	});
});

/**
 * This method is used to export media template 
 */
function generateMediaTemplate() {
	
	var proposalID = $("#id", "#generateTemplateParentDiv").val();
	var optionId = $("#optionId", "#generateTemplateParentDiv").val();
	var proposalversion = $("#proposalVersion", "#generateTemplateParentDiv").val();
	var templateId = $("#proposalTemplate", "#generateTemplateParentDiv").val();
	
	if (templateId == null && $("#proposalTemplateCheck", "#proposalTemplateForm").attr('checked')) {
		$('#proposalTemplate').addClass("errorElement errortip").attr("title", resourceBundle['error.generic.proposal.template.mandatory']+ '<BR><b>Help:</b> ' + resourceBundle['help.generic.upload.template']);
		renderErrorQtip("#generateTemplateParentDiv");	
	}
	else {
		$( "#downloadExcel" ).dialog( "open" );
		var ajaxReq = new AjaxRequest();
		ajaxReq.dataType = 'html';
		ajaxReq.url = "./manageProposal/validateMediaTemplate.action?id=" + proposalID + "&proposalVersion=" + proposalversion + "&proposalTemplate=" 
							+ templateId + "&templateType=" + getTemplateType() + "&optionId=" + optionId;
    	ajaxReq.success = function(result, status, xhr){
			var obj = jQuery.parseJSON(result);
			if (xhr.getResponseHeader('REQUIRES_AUTH') === '1') {
				$("#downloadExcel").dialog("close");
				$("#sessionTimeout").dialog("open");
			} if(!obj.objectMap.Valid){
				$("#downloadExcel").dialog("close");
				$('#proposalTemplate_select2').addClass("errorElement errortip").attr("title", resourceBundle['error.generic.old.proposal.template'] + '<BR><b>Help:</b> ' + resourceBundle['help.generic.refresh.page']);
				renderErrorQtip("#generateTemplateParentDiv");	
			} else {
				window.location.href = "./manageProposal/generateMediaTemplate.action?id=" + proposalID +  "&proposalVersion=" + proposalversion +
            "&proposalTemplate=" + templateId + "&templateType=" + getTemplateType() + "&optionId=" + optionId;
            
            setTimeout(function(){
                $("#downloadExcel").dialog("close");
            }, 1500);
			}
		};
		
		this.error = function(result, status, xhr){
			$("#downloadExcel").dialog("close");
			handelSOSViolationExceptionOnServer(result);
		};
		ajaxReq.submit();
	}
}

/**
 * 
 * @param {Object} XMLHTTPRequest
 */
function handelSOSViolationExceptionOnServer(XMLHTTPRequest){
	var errorObj = eval('(' + XMLHTTPRequest.responseText + ')').errorObject;	
	if(errorObj.messageType == 'CONFIRM'){
        new ModalDialogConfirm(errorObj.errorMessage, "proposalTemplateForm", sosViolationOkConfirm);
	} else {
		jQuery("#runtimeDialogDiv").model({theme: 'Error', autofade: false, message: errorObj.errorMessage});
	}
}

/**
 * This Message is Executed on Pressing Yes of ModalDialogConfirm
 */
function sosViolationOkConfirm () {
    var optionId = $("#optionId", "#generateTemplateParentDiv").val();
    var proposalID = $("#generateTemplateParentDiv #id").val();
    var proposalversion = $("#generateTemplateParentDiv #proposalVersion").val();
    var templateId = $("#generateTemplateParentDiv #proposalTemplate").val();
    $("#confirmSavedDialogDiv").dialog("close");
    
	window.location.href = "./manageProposal/generateMediaTemplate.action?id=" + proposalID + "&proposalVersion=" + proposalversion + 
    	"&proposalTemplate=" + templateId + "&optionId=" + optionId;
}

/**
 * Method Change the status of proposal to Complete
 */
function markProposalComplete(){
	var proposalID = $("#id", "#generateTemplateParentDiv").val();
	var ajaxReq = new AjaxRequest();
    ajaxReq.url =  "./manageProposal/proposalStatusComplete.action?proposalId=" + proposalID;
    ajaxReq.success = function(result, status, xhr){
    	$('#proposalHeaderStatus', '#generateTemplateParentDiv').text("Completed");
    	jQuery("#runtimeDialogDiv").model({theme: 'Success', message: resourceBundle['message.generic.proposalStatusChanged']});
    	$("#completeProposal", "#generateTemplateParentDiv").hide("fast");    	
    };
    ajaxReq.submit();
}

/**
 * This code is used to show and hide different export template option
 */
function showHideExportOptions(){
	clearCSSErrors("#proposalTemplateForm");
    if (!$("#proposalTemplateCheck", "#proposalTemplateForm").attr('checked') && !$("#creativeSpecsCheck", "#proposalTemplateForm").attr('checked')) {
        $('#creativeSpecsNote', "#proposalTemplateForm").hide();
        $('#proposalTemplateValues', "#proposalTemplateForm").hide();
        $('#selectOne', "#proposalTemplateForm").show();
        $('#proposalTemplateForm #generateMediaExport').attr("disabled", "disabled");
		$('#proposalTemplateForm #allOptionCheck').attr('checked',false);
		$('#proposalTemplateForm #allOptionCheck').attr("disabled", "disabled");
        $('#proposalTemplateForm #generateMediaExport').removeClass("save-btn").addClass("disabled-btn");
    } else if (!$("#proposalTemplateCheck", "#proposalTemplateForm").attr('checked') && $("#creativeSpecsCheck", "#proposalTemplateForm").attr('checked')) {
        $('#selectOne', "#proposalTemplateForm").hide();
        $('#proposalTemplateValues', "#proposalTemplateForm").hide();
        $('#creativeSpecsNote', "#proposalTemplateForm").show();
        $('#proposalTemplateForm #generateMediaExport').removeAttr("disabled");
        $('#proposalTemplateForm #generateMediaExport').removeClass("disabled-btn").addClass("save-btn");
		$('#proposalTemplateForm #allOptionCheck').removeAttr("disabled");
    } else {
        $('#selectOne', "#proposalTemplateForm").hide();
        $('#creativeSpecsNote', "#proposalTemplateForm").hide();
        $('#proposalTemplateValues', "#proposalTemplateForm").show();
        $('#proposalTemplateForm #generateMediaExport').removeAttr("disabled");
        $('#proposalTemplateForm #generateMediaExport').removeClass("disabled-btn").addClass("save-btn");
		$('#proposalTemplateForm #allOptionCheck').removeAttr("disabled");
    }
}

/**
 * This method is used to export template either creative spec or other template 
 */
function getTemplateType(){
    if ($("#allOptionCheck", "#proposalTemplateForm").attr('checked') &&
	    	$("#proposalTemplateCheck", "#proposalTemplateForm").attr('checked') &&
    		$("#creativeSpecsCheck", "#proposalTemplateForm").attr('checked')) {
        return 'A';
    }
    else if ($("#allOptionCheck", "#proposalTemplateForm").attr('checked') && $("#proposalTemplateCheck", "#proposalTemplateForm").attr('checked')) {
    	return 'AT';
    } else if ($("#allOptionCheck", "#proposalTemplateForm").attr('checked') && $("#creativeSpecsCheck", "#proposalTemplateForm").attr('checked')) {
        return 'AC';
    } else if ($("#proposalTemplateCheck", "#proposalTemplateForm").attr('checked') && $("#creativeSpecsCheck", "#proposalTemplateForm").attr('checked')) {
        return 'B';
    } else if ($("#proposalTemplateCheck", "#proposalTemplateForm").attr('checked')) {
        return 'T';
    } else if ($("#creativeSpecsCheck", "#proposalTemplateForm").attr('checked')) {
        return 'C';
 	}
}