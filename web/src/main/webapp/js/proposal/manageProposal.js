/**
 * 
 * @author surendra.singh
 */
jQuery(document).ready(function() {
	
    jQuery("input[class=numeric]", "#manageProposalForm").numeric({
        negative: false
    });
    
    jQuery("input[class=numericDecimal]", "#manageProposalForm").numeric({
        negative: false
     });
    
    jQuery("input[class=numericDecimal]", "#manageProposalContainer").numeric({
        negative: false
     });
    
    jQuery("#myProposal","#manageProposalForm").attr('checked', 'checked');
	
    jQuery("input[class=numbersOnly]", "#manageProposalForm").numeric({
        negative: false,
        decimal: false
        
     });

    /**
     * Date picker for requested from and requested on
     */ 
    jQuery("#_requestedOnFrom, #_requestedOnTo", "#manageProposalContainer").datepicker({
        showOn: "both",
        buttonText: "Select Date",
        buttonImage: "../images/calendar.gif",
        buttonImageOnly: true
    });
    
    /**
     * Date picker for due on from
     */
    jQuery("#_dueOnFrom, #_dueOnTo", "#manageProposalContainer").datetimepicker({
        showOn: "both",
        buttonText: "Select Date",
        buttonImage: "../images/calendar.gif",
        buttonImageOnly: true,
        hour: 0,
    	minute: 00
    });
    
    jQuery("#dueOnFrom", "#manageProposalContainer").bind("click", function(event, ui){
        jQuery("#_dueOnFrom", "#manageProposalContainer").val("");
    });
    
    jQuery("#dueOnTo", "#manageProposalContainer").bind("click", function(event, ui){
        jQuery("#_dueOnTo", "#manageProposalContainer").val("");
    });
    
    jQuery("#requestedOnFrom", "#manageProposalContainer").bind("click", function(event, ui){
        jQuery("#_requestedOnFrom", "#manageProposalContainer").val("");
    });
    
    jQuery("#requestedOnTo", "#manageProposalContainer").bind("click", function(event, ui){
        jQuery("#_requestedOnTo", "#manageProposalContainer").val("");
    });
	
	var proposalGridOpte = new JqGridBaseOptions();
	proposalGridOpte.url = "../manageProposal/searchProposal.action?myProposal=true";
	proposalGridOpte.colNames =	['Proposal ID', 'Proposal Version', 'Proposal Name', 'Planner Name', 'Advertiser',  'Sales Category', 'Due Date', 'Account Manager', 'Budget ($)', 'Status', 'Priority'];
	proposalGridOpte.height = 230;
	proposalGridOpte.colModel = [
		{name:'id', index:'id', hidden:true, key:true}, 
		{name:'proposalVersion', index:'proposalVersion', hidden:true, key:false}, 
		{name:'proposalName', index:'proposalName', key:false, width:200}, 
		{name:'userName', index:'userName', key:false},
		{name:'advertiserName', index:'advertiserName', sortable:false, key:false},
		{name:'salescategory', index:'salescategory', sortable:false, key:false},
		{name:'dueOn', index:'dueOn', key:false}, 
		{name:'accountManager', index:'accountManager', key:false},
		{name:'budget', index:'budget', sortable:true, key:false, width:100, align:"right"},
		{name:'proposalStatusDisplayName', index:'proposalStatusDisplayName', sortable:false, key:false},
		{name:'criticalityDisplayName', index:'criticalityDisplayName', hidden:true, key:false}
	];
	proposalGridOpte.pager = jQuery('#manageProposalPager');
	proposalGridOpte.sortname = "id";
	proposalGridOpte.height = 350;
	proposalGridOpte.rowNum = 15;
	proposalGridOpte.rowList = [15,20,25];
	proposalGridOpte.emptyrecords = resourceBundle['label.dashboard.emptymessage'];
	proposalGridOpte.caption = resourceBundle['label.generic.searchResult'];
	proposalGridOpte.beforeRequest = function(){
		var padding = parseInt(jQuery(".ui-tabs-panel", "#tabs").css("padding-left"), 10) 
					+ parseInt(jQuery(".ui-tabs-panel", "#tabs").css("padding-right"), 10);
		jQuery(this).setGridWidth(jQuery("#tabs").width() - (padding + 5), true);
	};
	proposalGridOpte.gridComplete = function(){
		jQuery(this).jqGrid('resetSelection');
	};
	
	/**
	 * Open proposal when user click on any row in JQGrid
	 * @param {Object} id
	 */
	proposalGridOpte.onSelectRow = function(id){
		openProposal(id);
	};

    jQuery("#manageProposalTable", "#manageProposalContainer").jqGrid(proposalGridOpte).navGrid('#manageProposalPager', {
		edit: false, del: false, search: false, add: false
    });
	
	jQuery("#myProposal", "#manageProposalContainer").click(function() {
        gridReload();
    });
	
	/**
	 * Enable auto search on attribute grid
	 */
	enableAutoSearch(jQuery("#proposalSearchOptionValue", "#manageProposalContainer"), function() {
		gridReload();
	});
	
	/**
	 * Register change event on search option select box
	 */
	jQuery("#proposalSearchOptions", "#manageProposalContainer").bind("change", function () {
		jQuery("#proposalSearchOptionValue", "#manageProposalContainer").val('').focus();
	});
	
	$("#advertiserName", "#manageProposalContainer").autoSearch({placeholder: "", url: "../proposalWorkflow/getAdvertiser.action", initUrl: "../proposalWorkflow/getAdvertiserById.action"});
	$("#agencyName", "#manageProposalContainer").autoSearch({placeholder: "", minimumInputLength: 2, url: "../proposalWorkflow/getAgency.action", initUrl: "../proposalWorkflow/getAgencyById.action"});
});

/**
 * Function to set grid parameters and reload it on clicking search button
 */
function gridReload(){
	var rowIds = jQuery('#manageProposalTable').jqGrid('getDataIDs');
	for(var i = 0; i < rowIds.length; i++){
	    var currRow = rowIds[i];
	    jQuery('#manageProposalTable').jqGrid('delRowData', currRow);
	}  

	var myProposal_mask = jQuery("#myProposal:checked", "#manageProposalContainer").val();
	if (myProposal_mask != 'true') {
		myProposal_mask = 'false';
	}
	var postData = {
			"cpmFrom": jQuery("#cpmFrom", "#manageProposalForm").val(),
			"cpmTo": jQuery("#cpmTo", "#manageProposalForm").val(),
			"agencyName": jQuery("#agencyName", "#manageProposalForm").val(),
			"budgetFrom": jQuery("#budgetFrom", "#manageProposalForm").val(),
			"budgetTo": jQuery("#budgetTo", "#manageProposalForm").val(),
			"requestedDateFrom": jQuery("#_requestedOnFrom").val(),
			"requestedDateTo": jQuery("#_requestedOnTo").val(),
			"dueDateFrom": jQuery("#_dueOnFrom").val(),
			"dueDateTo": jQuery("#_dueOnTo").val(),
			"myProposal": myProposal_mask,
			"salescategory": jQuery("#salescategory", "#manageProposalForm").val(),
			"impressionFrom": jQuery("#impressionFrom", "#manageProposalForm").val(),
			"impressionTo": jQuery("#impressionTo", "#manageProposalForm").val(),
			"advertiserName": jQuery("#advertiserName", "#manageProposalForm").val(),
			"cmpObjectiveId": jQuery("#campaignObjectiveObj", "#manageProposalForm").val(),
			"assignedUserId": jQuery("#userId", "#manageProposalForm").val(),
			"proposalStatus": jQuery("#proposalStatus", "#manageProposalForm").val()
	};
	var selectedSearchOption = jQuery("#proposalSearchOptions", "#manageProposalContainer").val();
	var searchInputValue = jQuery("#proposalSearchOptionValue", "#manageProposalContainer").val();

	var searchUrl = '../manageProposal/searchProposal.action';
	if(selectedSearchOption != "" && searchInputValue != "") {
		searchUrl = searchUrl + '?' + selectedSearchOption + '=' + encodeURIComponent(searchInputValue);
	}
	jQuery("#manageProposalTable").jqGrid('setGridParam',{url:searchUrl, postData:postData, page:1}).trigger("reloadGrid");
}

function showProposalSearch(){
	$("#myProposal").attr("checked",false);
    if ($(".search-prop-cont", "#manageProposalContainer").is(":visible")) {
        $(".search-prop-cont", "#manageProposalContainer").slideUp();
        $("#searchPropCont", "#manageProposalContainer").removeClass("search-active");
    }
    else {
        $(".search-prop-cont", "#manageProposalContainer").slideDown();
        $("#searchPropCont", "#manageProposalContainer").addClass("search-active");
    }
	resetProposalsearchFields();
}

function resetProposalsearchFields(){
	document.manageProposalForm.reset();
	$("#advertiserName", "#manageProposalContainer").select2("val", "");
	$("#agencyName", "#manageProposalContainer").select2("val", "");
}