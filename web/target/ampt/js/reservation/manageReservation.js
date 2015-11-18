/**
 * 
 * @param {Object} "#reservationProductId"
 * @param {Object} "#manageReservationForm"
 */
jQuery(document).ready(function() {
	$("#reservationProductId","#manageReservationForm").multiselect({
		selectedList: 1,
		noneSelectedText: '',
		close : function(event, ui){
            setSalesTargetForSelectedProducts();
        }
	}).multiselectfilter();
	
	$("#reservationSalesTarget","#manageReservationForm").multiselect({
		selectedList: 1,
		noneSelectedText: ''
	}).multiselectfilter();
	
	/**
     * Date picker for start and end date
     */
    var reservationDates = jQuery("#reservationStartDate , #reservationEndDate", "#manageReservationsContainer").datepicker({
        showOn: "both",
        buttonText: "Select Date",
        buttonImage: "../images/calendar.gif",
        buttonImageOnly: true,
        onSelect: function(selectedDate){
            var option = this.id == "reservationStartDate" ? "minDate" : "maxDate", instance = $(this).data("datepicker");
            date = $.datepicker.parseDate(instance.settings.dateFormat ||
            			$.datepicker._defaults.dateFormat, selectedDate, instance.settings);
            reservationDates.not(this).datepicker("option", option, date);
        }
    });
    
    $("#manageReservationForm #reservationStartDateReset" ).bind( "click", function(event, ui) {
		$("#reservationStartDate").val("");
		var validTo = $("#reservationEndDate", "#manageReservationForm").val();
        $("#reservationEndDate", "#manageReservationForm").datepicker("option", "minDate", $("#reservationStartDate", "#manageReservationForm").val());
        $("#reservationEndDate", "#manageReservationForm").val(validTo);
	});
    
    $("#manageReservationForm #reservationEndDateReset" ).bind( "click", function(event, ui) {
		$("#reservationEndDate").val("");
		var validFrom = $("#reservationStartDate", "#manageReservationForm").val();
        $("#reservationStartDate", "#manageReservationForm").datepicker("option", "maxDate", $("#endDate", "#buildProposalParentDiv").val());
        $("#reservationStartDate", "#manageReservationForm").val(validFrom);
	});
    
    $("#advId", "#manageReservationForm").autoSearch({placeholder: "", url: "../proposalWorkflow/getAdvertiser.action", initUrl: "../proposalWorkflow/getAdvertiserById.action"});
    $("#targetProposal", "#manageReservationsContainer").select2();
    $("#reservationSalesCategoryId", "#manageReservationsContainer").select2();
    
    $("#reservationStatus", "#manageReservationForm").multiselect({
        selectedList: 2,
        noneSelectedText: ''
    }).multiselectfilter();
    
    var reservationStatus=[];
    $("#reservationStatus option","#manageReservationForm").each(function(){
        if ($(this).text() == 'CREATED' || $(this).text() == 'RENEWED') {
        	reservationStatus.push($(this).val());
        }
    });
    $("#reservationStatus", "#manageReservationForm").val(reservationStatus).multiselect("refresh");
    
    $("#manageReservationForm #expiryDateReset" ).bind( "click", function(event, ui) {
		$("#expiryDate").val("");
	});
    
    jQuery("#myReservation", "#manageReservationsContainer").attr('checked', 'checked');
    
    jQuery("#myReservation", "#manageReservationsContainer").bind("change", function () {
    	getReservationData();
    	var selectedSearchOption = jQuery("#reservationSearchOptions", "#manageReservationsContainer").val();
    	var myReservation = $("#myReservation:checked", "#manageReservationsContainer").val();
    	var daysToExpire = jQuery("#noOfExpiryDays", "#manageReservationsContainer").val();
		if(selectedSearchOption == 'sosLineItemId' || (myReservation != 'true' && daysToExpire == "")){
			getSOSReservationData();
		}else{
			$("#sosResrvtnDataTable").jqGrid("clearGridData", true);
		}
	});
    
    jQuery("#noOfExpiryDays", "#manageReservationsContainer").bind("change", function () {
    	getReservationData();
    	var selectedSearchOption = jQuery("#reservationSearchOptions", "#manageReservationsContainer").val();
    	var myReservation = $("#myReservation:checked", "#manageReservationsContainer").val();
    	var daysToExpire = jQuery("#noOfExpiryDays", "#manageReservationsContainer").val();
		if(selectedSearchOption == 'sosLineItemId' || (myReservation != 'true' && daysToExpire == "")){
			getSOSReservationData();
		}else{
			$("#sosResrvtnDataTable").jqGrid("clearGridData", true);
		}
	});
    
	/**
	 * Enable auto search on attribute grid
	 */
	enableAutoSearch(jQuery("#reservationSearchOptionValue", "#manageReservationsContainer"), function() {
		if($.trim($("#reservationSearchOptionValue", "#manageReservationsContainer").val()) != ""){
			var selectedSearchOption = jQuery("#reservationSearchOptions", "#manageReservationsContainer").val();
			if(selectedSearchOption == 'sosLineItemId'){
				jQuery("#myReservation", "#manageReservationsContainer").attr('checked', false);
				jQuery("#noOfExpiryDays", "#manageReservationsContainer").val('');
				getSOSReservationData();
				$("#amptResrvtnDataTable").jqGrid("clearGridData", true);
			}else{
				getReservationData();
				$("#sosResrvtnDataTable").jqGrid("clearGridData", true);
			}	
		}
	});
	
	jQuery("#reservationSearchOptions", "#manageReservationsContainer").bind("change", function () {
		jQuery("#reservationSearchOptionValue", "#manageReservationsContainer").val('').focus();
	});
	
	$("#searchFieldErrorContainer img", "#searchFieldsContainer").toggle(function(e){
		$("#searchFieldErrorShowhide", "#searchFieldsContainer").show();
        $(this).attr("src", "../css/images/minus.png");
    }, function(e){
    	$("#searchFieldErrorShowhide", "#searchFieldsContainer").hide();
        $(this).attr("src", "../css/images/plus.png");
    });
	
	var amptReservationDataGrid = new JqGridBaseOptions(); 
	amptReservationDataGrid.url = "../manageReservation/getAmptReservationData.action?myReservation=true";
	amptReservationDataGrid.colNames = ['<input type="checkbox" id="AmptReservationMultiSelect_Header"  name="AmptReservationMultiSelect" onClick="customAmptReservationSelect(this)">',
	                                    'Proposal Id','Proposal Name', 'productId','salesTargetId','Product','Sales Target','Line Item ID','Start Date','End Date','SOR', 
	                                      'Option','Expiry Date','Targeting String','lineItemTargetingData','assignedToUser','proposalStatus','productActive','salesTargetActive','Action','ProductClass Id'];
	amptReservationDataGrid.colModel = [
            {name:"customAmptReservationSelect", index:"customAmptReservationSelect", sortable:false, key:false, formatter:amptReservationMultiSelectFormatter, width:20},
	        {name:"proposalId",index:"proposalId",hidden:true},
	        {name:"proposalName", index:"proposalName", width:97, hidedlg: true, formatter:reservationSearchlinkFormatter},
	        {name:"productId",index:"productId",hidden:true},			
			{name:"salesTargetId",index:"salesTargetId",hidden:true},
	        {name:"productName",index:"productName", width:80},
			{name:"salesTarget",index:"salesTarget", width:70},
			{name:"lineItemId", index:"lineItemId", key:true, width:70},
			{name:"startDate",index:"startDate",width:50},
			{name:"endDate",index:"endDate",width:50},
			{name:"sor",index:"sor",width:30},
			{name:"optionName",index:"optionName",width:40},
			{name:"expiryDate",index:"expiryDate",width:50},		
			{name:"targetingString",index:"targetingString",sortable:false, width:70},
			{name:"lineItemTargetingData",index:"lineItemTargetingData",hidden:true},
			{name:"proposalAssignedToUserId",index:"proposalAssignedToUserId",hidden:true},
			{name:"proposalStatus",index:"proposalStatus",hidden:true},
			{name:"product_active",index:"product_active",hidden:true},
			{name:"salesTarget_active",index:"salesTarget_active",hidden:true},
			{name:'act', index:'act', sortable:false, key:false, width:60,align:"center"}, 
	        {name:"sosProductClassId",index:"sosProductClassId",hidden:true},
		];
	amptReservationDataGrid.height = 240;
	amptReservationDataGrid.rowNum = 10;
	amptReservationDataGrid.emptyrecords = "No records to view";
	amptReservationDataGrid.rowList = [10,40,100,200];
	amptReservationDataGrid.caption = resourceBundle['manageReservation.search.ampt.proposals'];
	amptReservationDataGrid.pager = jQuery('#amptResrvtnDataPager');
	amptReservationDataGrid.sortname = "lineItemId";
	amptReservationDataGrid.beforeRequest = function(){
		var padding = parseInt(jQuery(".ui-tabs-panel", "#tabs").css("padding-left"), 10) 
					+ parseInt(jQuery(".ui-tabs-panel", "#tabs").css("padding-right"), 10);
		jQuery(this).setGridWidth(jQuery("#tabs").width() - (padding + 5), true);
	};
	amptReservationDataGrid.gridComplete = function(){ 	
		jQuery(this).jqGrid('resetSelection');
		amptReservationDataGrid.afterGridCompleteFunction();
	};
	
	amptReservationDataGrid.afterGridCompleteFunction = function (){
		var lineItemIds = jQuery("#amptResrvtnDataTable").jqGrid('getDataIDs');
		for(var i = 0;i < lineItemIds.length; i++){
			var lineItemId = lineItemIds[i];
			var currentUser = $("#amptResrvtnDataTable").jqGrid('getCell', lineItemId, 'loggedInUserId');
			var assignedToUser = $("#amptResrvtnDataTable").jqGrid('getCell', lineItemId, 'proposalAssignedToUserId');
			var proposalStatus = $("#amptResrvtnDataTable").jqGrid('getCell', lineItemId, 'proposalStatus');
			var productActive = $("#amptResrvtnDataTable").jqGrid('getCell', lineItemId, 'product_active');
			var salesTargetActive = $("#amptResrvtnDataTable").jqGrid('getCell', lineItemId, 'salesTarget_active');
			var be = "<span class='grid-action-icons'>";
			var endDate = $("#amptResrvtnDataTable").jqGrid('getCell', lineItemId, 'endDate');
			if(((Date.parse($("#currentDate", "#manageReservationsContainer").val()) <= Date.parse(endDate))) && ($("#hasAdminRole", "#manageReservationsContainer").length > 0 ) && proposalStatus != 'REVIEW' && productActive == "Yes" && salesTargetActive == "Yes"){
				be = be + "<a onclick='moveReservedLineItemsData("+ lineItemId +")' title='Move Reservation' style='position:absolute;margin-left:0px;'>" +
					"<img src='../images/move_reservation.png' /></a>"; 
			}	 	
			be = be + "<a onclick='viewCalendarFromGrid("+ lineItemId +")' title='View calendar' style='position:absolute;margin-left:22px;'>" +
				"<img src='../images/full_calendar_small.png'/> </a>" ;
			
			if(((Date.parse($("#currentDate", "#manageReservationsContainer").val()) <= Date.parse(endDate))) && ($("#hasAdminRole", "#manageReservationsContainer").length > 0 ) && proposalStatus == 'INPROGRESS'){
				be = be + "<a onclick='deleteReservedLineItem("+ lineItemId +")' title='Delete Reservation' style='position:absolute;margin-left:44px;'>" +
					"<img src='../images/delete.png' /></a></span>";
			}
			jQuery('#amptResrvtnDataTable').jqGrid('setRowData', lineItemId, {act:be});
			$('td[aria-describedby=amptResrvtnDataTable_act]').removeAttr("title");
		}
		
		$("#AmptReservationMultiSelect_Header").attr("checked", false);
		customAmptReservationSelect($("#AmptReservationMultiSelect_Header"));
	};
	
	$("#amptResrvtnDataTable").jqGrid(amptReservationDataGrid).navGrid('#amptResrvtnDataPager',{		
		edit: false, del: false, search: false, add: false
	});
	
	 /* Adding a button on the Reservation Grid pager for Modifying the Expiry Date */
	if($("#hasAdminRole", "#manageReservationsContainer").length > 0){
	    $("#amptResrvtnDataTable").jqGrid('navButtonAdd', "#amptResrvtnDataPager", {
	        caption: "", id: "reservationExpiry_amptResrvtnDataTable", title: "Bulk modify Expiry Date", buttonicon: 'ui-icon-arrowrefresh-1-e',
	        onClickButton: function(){
		    	var isReservationRowSelected = false;
		        $("#amptResrvtnDataTable").find(':checkbox').each(function(){
		            if ($(this).is(":checked")) {
		            	isReservationRowSelected = true;
		            }
		        });
		        if (isReservationRowSelected) {
		        	var selectedIds = "";
		        	var selectedPropIds = "";
		        	var selectedStDates = "";
		        	$("#amptResrvtnDataTable").find(':checkbox').each(function(){
		                 if ($(this).is(":checked")) {
		                     selectedIds = selectedIds + $(this).val() + ",";		                     
		                     selectedPropIds = selectedPropIds + $("#amptResrvtnDataTable").getRowData($(this).val())['proposalId'] + ",";
		                     selectedStDates = selectedStDates + $("#amptResrvtnDataTable").getRowData($(this).val())['startDate'] + ",";
		                 }
		             });
		            selectedIds = $.trim(selectedIds).substring(0, selectedIds.length - 1);
		            selectedPropIds = $.trim(selectedPropIds).substring(0, selectedPropIds.length - 1);
		            selectedStDates = $.trim(selectedStDates).substring(0, selectedStDates.length - 1);
		        	validateAndUpdateExpiryDate(selectedIds, selectedPropIds, selectedStDates);
		        }
		        else {
					jQuery("#runtimeDialogDiv").model({theme: 'Error', autofade: false, message: resourceBundle['error.no.reservation.selected.for.expiryDate']});
		        }
	        }
	    });
	}

	var sosReservationDataGrid = new JqGridBaseOptions(); 
	sosReservationDataGrid.colNames = ['SOS Order Id', 'Advertiser','Product Id','Product','SalesTarget Id','Sales Target','Line Item ID','Start Date','End Date','SOR', 
	                                      'Targeting String','sosURL'];
	sosReservationDataGrid.colModel = [
	        {name:"sosOrderId", index:"sosOrderId", width:70},
	        {name:"advertiserName", index:"advertiserName", width:90},
	        {name:"productId",index:"productId",width:70},			
	        {name:"productName",index:"productName",sortable:false,width:90},
	        {name:"salesTargetId",index:"salesTargetId",width:70},
			{name:"salesTarget",index:"salesTarget", sortable:false,width:90},
			{name:"lineItemId", index:"lineItemId", key:true, width:70},
			{name:"startDate",index:"startDate",align:"center",width:60},
			{name:"endDate",index:"endDate",align:"center",width:60},
			{name:"sor",index:"sor",width:50},
			{name:"targetingString",index:"targetingString",sortable:false, width:90}, 
			{name:"sosURL",index:"sosURL",sortable:false, hidden:true}, 
		];
	sosReservationDataGrid.rowNum = 10;
	sosReservationDataGrid.emptyrecords = "No records to view";
	sosReservationDataGrid.rowList = [10,40,100,200];
	sosReservationDataGrid.caption = resourceBundle['manageReservation.search.sos.orders'];
	sosReservationDataGrid.pager = jQuery('#sosResrvtnDataPager');
	sosReservationDataGrid.sortname = "lineItemId";
	sosReservationDataGrid.gridComplete = function(){ 	jQuery(this).jqGrid('resetSelection'); };
	sosReservationDataGrid.onSelectRow = function(id){
		openOrder($("#sosResrvtnDataTable").jqGrid('getCell', id, 'sosOrderId'), $("#sosResrvtnDataTable").jqGrid('getCell', id, 'sosURL')); 
	};
	$("#sosResrvtnDataTable").jqGrid(sosReservationDataGrid).navGrid('#sosResrvtnDataPager',{		
		edit: false, del: false, search: false, add: false
	});
});


/**
 * Fetch sales target from selected product
 * @param {Object} selectedProduct 
 */
function setsosSalesTargetIdValueOfProposal(selectedProduct) {
    var salesTargetIdCombo = $("#reservationSalesTarget" , "#manageReservationForm");
	var actionURL = "../manageReservation/getSalesTargetForProduct.action";
	dynaLoadSalesTargets(salesTargetIdCombo, actionURL, selectedProduct);
}

function setSalesTargetForSelectedProducts(){
	if ($("#reservationProductId", "#manageReservationForm").val() == "" || $("#reservationProductId", "#manageReservationForm").val() == null) {
		setSalesTarget();
	} else {
		setsosSalesTargetIdValueOfProposal($("#reservationProductId", "#manageReservationForm").val());
	}
}

/**
 * Dynamic load sales target based on product                                                                                                                                                                                                                                                                                                         
 * @param {Object} select
 * @param {Object} actionURL
 */
function dynaLoadSalesTargets(select, actionURL, selectedProductIds){
    var ajaxReq = new AjaxRequest();
    ajaxReq.url = actionURL;
    ajaxReq.type = 'POST';
	ajaxReq.data = { "productID": selectedProductIds.toString() };
    ajaxReq.cache = false;
    ajaxReq.success = function(result, status, xhr){
    	$("#reservationSalesTarget", "#manageReservationForm").empty();
    	$("#reservationSalesTarget", "#manageReservationForm").multiselect("refresh");
        sortListForProposalLineItem(result.objectMap.gridKeyColumnValue, $("#reservationSalesTarget" , "#manageReservationForm"));
    };
    ajaxReq.submit();
}

/**
 * Sort data for select box based on display value (Due to IE and chrome issue)
 * @param result
 * @param select
 * @return
 */
function sortListForProposalLineItem(result, select){
    var arrVal = sortMapDataByValue(result);
    for (var i = 0; i < arrVal.length ; i++) { 
		$(select).append($("<option></option>").val(arrVal[i][1]).html(arrVal[i][0]));
	}
	$(select).val('');
	$(select).multiselect("refresh");
}

function searchReservations(){
	if(validateSearchInputs()){
		$("#sosResrvDataGridContainer", "#manageReservationsContainer").show();
		getReservationData();		
		var myReservation = $("#myReservation:checked", "#manageReservationsContainer").val();
		var daysToExpire = jQuery("#noOfExpiryDays", "#manageReservationsContainer").val();
		if(myReservation != 'true' && daysToExpire == ""){
			getSOSReservationData();
		}
	}else{
		jQuery('#messageHeaderDiv', "#manageReservationsContainer").html(resourceBundle['validation.error']).addClass('error');
	}
}

function validateSearchInputs(){
	var returnFlag = true;
	clearCSSErrors("#searchFieldsContainer");
	var errorMsg = resourceBundle['error.reservation.search'];
	
	if(($("#reservationProductId", "#manageReservationForm").val() == "" || $("#reservationProductId", "#manageReservationForm").val() == null) && 
			($("#reservationSalesTarget", "#manageReservationForm").val() == "" || $("#reservationSalesTarget", "#manageReservationForm").val() == null) && 
			$("#advId", "#manageReservationForm").val() == "" && $("#reservationSalesCategoryId", "#manageReservationForm").val()== ""){
		$("#reservationProductId_custom", "#manageReservationForm").addClass("errorElement errortip").attr("title", errorMsg);
		$("#reservationSalesTarget_custom", "#manageReservationForm").addClass("errorElement errortip").attr("title", errorMsg);
		$("#advId_select2", "#manageReservationForm").addClass("errorElement errortip").attr("title", errorMsg);
		$("#reservationSalesCategoryId_select2", "#manageReservationForm").addClass("errorElement errortip").attr("title", errorMsg);
		$("#reservationProductId", "#manageReservationForm").addClass("errorElement errortip");
		$("#reservationSalesTarget", "#manageReservationForm").addClass("errorElement errortip");
		$("#advId", "#manageReservationForm").addClass("errorElement errortip");
		$("#reservationSalesCategoryId", "#manageReservationForm").addClass("errorElement errortip");
		renderErrorQtip("#manageReservationForm");
		returnFlag = false;
	}
	return returnFlag;
}

function showReservationSearch(){
    if ($(".search-prop-cont", "#manageReservationsContainer").is(":visible")) {
        $(".search-prop-cont", "#manageReservationsContainer").slideUp();
        $("#searchReservationCont", "#manageReservationsContainer").removeClass("search-active");
    }
    else {
        $(".search-prop-cont", "#manageReservationsContainer").slideDown();
        $("#searchReservationCont", "#manageReservationsContainer").addClass("search-active");
        jQuery("#myReservation", "#manageReservationsContainer").attr('checked', false);
    }
	jQuery("#manageReservationForm", "#manageReservationsContainer").get(0).reset();
	resetSearchFields();
	var reservationStatus=[];
    $("#reservationStatus option","#manageReservationForm").each(function(){
        if ($(this).text() == 'CREATED' || $(this).text() == 'RENEWED') {
        	reservationStatus.push($(this).val());
        }
    });
    $("#reservationStatus", "#manageReservationForm").val(reservationStatus).multiselect("refresh");
    var now = new Date();
    now.setDate(now.getDate()+1);
    $("#reservationStartDate", "#manageReservationForm").datepicker('setDate', now);
    var validTo = $("#reservationEndDate", "#manageReservationForm").val();		
	$("#reservationEndDate", "#manageReservationForm").datepicker("option", "minDate", $("#reservationStartDate", "#manageReservationForm").val());	
	$("#reservationEndDate", "#manageReservationForm").val(validTo);
}

function isAMPTReservationToBeCalled(){
	var selectedSearchOption = jQuery("#reservationSearchOptions", "#manageReservationsContainer").val();
	var selectedSearchOptionValue = $.trim(jQuery("#reservationSearchOptionValue", "#manageReservationsContainer").val());
    if(selectedSearchOption == 'sosLineItemId'  && selectedSearchOptionValue != ""){
    	$("#amptResrvtnDataTable").jqGrid("clearGridData", true);
    	return false;
    } else {
		return true;
	}
}

function getReservationData(){
	if(isAMPTReservationToBeCalled()){
		var resStatus = "";
		var selProductIds = "";
		var selSalesTargetIds= "";
		if($("#reservationStatus", "#manageReservationForm").val() != null){
			resStatus = $("#reservationStatus", "#manageReservationForm").val();
		}
		if(jQuery("#reservationProductId", "#manageReservationForm").val() != null){
			selProductIds = $("#reservationProductId", "#manageReservationForm").val().toString();
		}
		
		if(jQuery("#reservationSalesTarget", "#manageReservationForm").val() != null){
			selSalesTargetIds = $("#reservationSalesTarget", "#manageReservationForm").val().toString();
		}
		var myReservation = $("#myReservation:checked", "#manageReservationsContainer").val();
		if (myReservation != 'true') {
			myReservation = 'false';
		}
		var postData = {
			"advertiserId": jQuery("#advId", "#manageReservationForm").val(),
			"myReservation": myReservation, 
			"reservationStatus": resStatus.toString(),
			"startDate": jQuery("#reservationStartDate", "#manageReservationForm").val(),
			"endDate": jQuery("#reservationEndDate", "#manageReservationForm").val(),
			"productIdLst": selProductIds,
			"salesTargetIdLst": selSalesTargetIds,
			"daysToExpire": jQuery("#noOfExpiryDays", "#manageReservationsContainer").val(),
			"salesCategoryId": jQuery("#reservationSalesCategoryId","#manageReservationForm").val()
		};
	
		var selectedSearchOption = jQuery("#reservationSearchOptions", "#manageReservationsContainer").val();
		var searchInputValue = jQuery("#reservationSearchOptionValue", "#manageReservationsContainer").val();
	
		var searchUrl = '../manageReservation/getAmptReservationData.action';
		if (selectedSearchOption != "" && searchInputValue != "") {
			searchUrl += '?' + selectedSearchOption + '=' + encodeURIComponent(searchInputValue);
		}
		jQuery("#amptResrvtnDataTable").jqGrid('setGridParam', {url:searchUrl, postData:postData, page:1}).trigger("reloadGrid");
	}
}


function isSOSReservationToBeCalled(){
	var selectedSearchOption = jQuery("#reservationSearchOptions", "#manageReservationsContainer").val();
	var selectedSearchOptionValue = $.trim(jQuery("#reservationSearchOptionValue", "#manageReservationsContainer").val());
    var myReservation = $("#myReservation:checked", "#manageReservationsContainer").val();
    var daysToExpire = jQuery("#noOfExpiryDays", "#manageReservationsContainer").val();
    if(myReservation == 'true' || daysToExpire != "" || (selectedSearchOption != 'sosLineItemId'  && selectedSearchOptionValue != "")){
    	$("#sosResrvtnDataTable").jqGrid("clearGridData", true);
    	return false;
    } else {
		return true;
	}
}

function getSOSReservationData(){
	if(isSOSReservationToBeCalled()){
		var selProductIds= "";
		var selSalesTargetIds= "";
		if(jQuery("#reservationProductId", "#manageReservationForm").val() != null){
			selProductIds = $("#reservationProductId", "#manageReservationForm").val().toString();
		}
		
		if(jQuery("#reservationSalesTarget", "#manageReservationForm").val() != null){
			selSalesTargetIds = $("#reservationSalesTarget", "#manageReservationForm").val().toString();
		}
		var postData = {
				"advertiserId": jQuery("#advId", "#manageReservationForm").val(),
				"productIdLst": selProductIds,
				"startDate": jQuery("#reservationStartDate", "#manageReservationForm").val(),
				"endDate": jQuery("#reservationEndDate", "#manageReservationForm").val(),
				"salesTargetIdLst": selSalesTargetIds,
				"salesCategoryId": jQuery("#reservationSalesCategoryId","#manageReservationForm").val()
			};
		var searchUrl = '../manageReservation/getSosReservationData.action';
		var selectedSearchOption = jQuery("#reservationSearchOptions", "#manageReservationsContainer").val();
		var searchInputValue = jQuery("#reservationSearchOptionValue", "#manageReservationsContainer").val();
		if (selectedSearchOption != "" && searchInputValue != "" && selectedSearchOption == 'sosLineItemId') {
			searchUrl += '?' + selectedSearchOption + '=' + encodeURIComponent(searchInputValue);
		}
		jQuery("#sosResrvtnDataTable").jqGrid('setGridParam',{url:searchUrl, postData:postData, page:1}).trigger("reloadGrid");
	}
}


function resetSearchFields(){
	clearCSSErrors("#searchFieldsContainer");
	document.manageReservationForm.reset();
	$("#advId", "#manageReservationForm").select2("val", "");
	$("#reservationProductId", "#manageReservationForm").val('');
	$("#reservationProductId", "#manageReservationForm").multiselect("refresh");
	$("#reservationSalesTarget", "#manageReservationForm").val('');
	$("#reservationSalesTarget", "#manageReservationForm").multiselect("refresh");
	$("#reservationSalesCategoryId", "#manageReservationForm").select2("val", "");
	var reservationStatus=[];
    $("#reservationStatus option","#manageReservationForm").each(function(){
        if ($(this).text() == 'CREATED' || $(this).text() == 'RENEWED') {
        	reservationStatus.push($(this).val());
        }
    });
    $("#reservationStatus", "#manageReservationForm").val(reservationStatus).multiselect("refresh");
	setSalesTarget();
	var now = new Date();
	now.setDate(now.getDate()+1);
    $("#reservationStartDate", "#manageReservationForm").datepicker('setDate', now);
	var validFrom = $("#reservationStartDate", "#manageReservationForm").val();
	var validTo = $("#reservationEndDate", "#manageReservationForm").val();		
	
	$("#reservationStartDate", "#manageReservationForm").datepicker("option", "maxDate", $("#reservationEndDate", "#manageReservationForm").val());
	$("#reservationStartDate", "#manageReservationForm").val(validFrom);		
	$("#reservationEndDate", "#manageReservationForm").datepicker("option", "minDate", $("#reservationStartDate", "#manageReservationForm").val());	
	$("#reservationEndDate", "#manageReservationForm").val(validTo);
	var resStatus = "";
	if($("#reservationStatus", "#manageReservationForm").val() != null){
		resStatus = $("#reservationStatus", "#manageReservationForm").val();
	}
	
	var myReservation = $("#myReservation:checked", "#manageReservationsContainer").val();
	if (myReservation != 'true') {
		myReservation = 'false';
	}
	var searchUrl = '../manageReservation/getAmptReservationData.action';
	var postData = {
		"advertiserId": jQuery("#advId", "#manageReservationForm").val(),
		"myReservation": myReservation, 
		"reservationStatus": resStatus.toString(),
		"startDate": jQuery("#reservationStartDate", "#manageReservationForm").val(),
		"endDate": jQuery("#reservationEndDate", "#manageReservationForm").val(),
		"productIdLst": (jQuery("#reservationProductId", "#manageReservationForm").val() == null) ? "" : jQuery("#reservationProductId", "#manageReservationForm").val(),
		"salesTargetIdLst": (jQuery("#reservationSalesTarget", "#manageReservationForm").val() == null) ? "" : jQuery("#reservationSalesTarget", "#manageReservationForm").val(),
		"daysToExpire": jQuery("#noOfExpiryDays", "#manageReservationsContainer").val(),
		"salesCategoryId": jQuery("#reservationSalesCategoryId","#manageReservationForm").val()
	};
	jQuery("#amptResrvtnDataTable").jqGrid('setGridParam', {url:searchUrl, postData:postData, page:1});
}

function moveReservedLineItemsData(lineItemId){
	var proposalId = $("#amptResrvtnDataTable").jqGrid('getCell', lineItemId, 'proposalId');
	var expiryDate = $("#amptResrvtnDataTable").jqGrid('getCell', lineItemId, 'expiryDate');
	var productClassId = $("#amptResrvtnDataTable").jqGrid('getCell', lineItemId, 'sosProductClassId');
	var startDate = $("#amptResrvtnDataTable").jqGrid('getCell', lineItemId, 'startDate');
	$("#moveReservationSrcExpiryDate", "#moveReservationsDialogue").val(expiryDate);
    var ajaxReq = new AjaxRequest();
    ajaxReq.url = "../manageReservation/getProposalsList.action?proposalId=" + proposalId +"&lineItemId=" + lineItemId;
    ajaxReq.success = function(result, status, xhr){
    	if (result.objectMap.pastEndDate == false) {
			var proposalsList = result.objectMap.gridKeyColumnValue;
			$("option", $("#targetProposal", "#moveReservationsDialogue")).remove();
			sortListForProposalLineItem(proposalsList, $("#targetProposal", "#moveReservationsDialogue"));
		}
		$("#targetProposal", "#manageReservationsContainer").trigger("change");
		var selectProposal = new ModalDialog();
		selectProposal.width = 520;
		selectProposal.height = 180;
		selectProposal.resizable = false;
		selectProposal.draggable = true;
		selectProposal.buttons = [{
            text: "Move",
            click: function(){
            	clearCSSErrors("#moveReservationsDialogue");
                var targetProposalId = $("#targetProposal", "#moveReservationsDialogue").val();
                if (targetProposalId != "") {
                	var targetExpiryDate = $("#moveReservationExpiryDate","#moveReservationsDialogue").val();
                	var srcProposalAssignedToUsr = $("#amptResrvtnDataTable").jqGrid('getCell', lineItemId, 'proposalAssignedToUserId');
                    var ajaxReq = new AjaxRequest();
                    ajaxReq.url = '../manageReservation/moveReservationData.action?targetProposalId=' + targetProposalId + "&lineItemId=" + lineItemId + "&srcProposalId=" + 
                    			proposalId + "&srcAssignedToUsr=" + srcProposalAssignedToUsr + "&expiryDate=" + targetExpiryDate;
                    ajaxReq.success = function(result, status, xhr){
	                    if (result.validationFailed == true) {
							 $('#moveReservationExpiryDate').addClass("errorElement errortip").attr("title", result.errorList[0].errorMessageForUI+ '<BR><b>Help:</b> ' + result.errorList[0].errorHelpMessageForUI);
							 $('#moveReservationExpiryDate').qtip({
							        style: {
							            classes: 'ui-tooltip-red' 
							        },
									position:{my: 'bottom center',at: 'top center'}
							    });
							    $(".ui-dialog-buttonset button:contains('Move')").button("enable");
				
				         }else{
		                        $("#amptResrvtnDataTable").trigger("reloadGrid");
		                        $("#moveReservationExpiryDate").datepicker("destroy");
		                        $("#moveReservationsDialogue").dialog("close");
		                        jQuery("#runtimeDialogDiv").model({theme: 'Success', message: resourceBundle['reservation.moved.successfully']});
                        }
                    };
                    ajaxReq.submit();
                    $(".ui-dialog-buttonset button:contains('Move')").button("disable");
                }else{
                	$("#targetProposal_select2", "#moveReservationsDialogue").attr("title", "Target Proposal is mandatory." + '<BR><b>Help:</b> ' + "Please select valid data in the field.");
                	$("#targetProposal", "#moveReservationsDialogue").addClass('errorElement');
                	$("#targetProposal_select2", "#moveReservationsDialogue").addClass('errorElement errortip');
                	renderErrorQtip("#moveReservationsDialogue");
                }
            }
        }, {
            text: "Close",
            click: function(){
            	$("#targetProposal_select2", "#moveReservationsDialogue").qtip("destroy");
            	$("#moveReservationExpiryDate").datepicker("destroy");
                $("#moveReservationsDialogue").dialog("close");
            }
        }];
        
        $("#moveReservationsDialogue").dialog(selectProposal);
        enableDialogButton("moveReservationsDialogue");
    	$("#targetProposal", "#moveReservationsDialogue").select2("val", "");
    	fillExpiryDate(productClassId,startDate);
        $("#moveReservationsDialogue").dialog("open");
        $("#moveReservationsDialogue").dialog({ closeOnEscape: false });
        $("#targetProposal_select2", "#moveReservationsDialogue").qtip("destroy");
		if (result.objectMap.pastEndDate == true) {
			$(".ui-dialog-buttonpane", "div[aria-labelledby=ui-dialog-title-moveReservationsDialogue]").append("<div id='errorPastEndDate' class='model-dialog-error'>" + resourceBundle['confirm.reservation.data.modify'] + "</div>");
			$(".ui-dialog-buttonset button:contains('Move')").button("disable");
		}
        clearCSSErrors("#moveReservationsDialogue");
        var valueToPrint = "<b style='padding-left:9px'>Note:</b><ol type='disc'> <li>The selected proposal will be assigned to the logged in user.</li><li>Selected reservation will be moved to the default option of the target proposal.</li></ol>";
    };
    ajaxReq.submit();
}

function deleteReservedLineItem(lineItemId){
	var errorObj = resourceBundle['confirm.delelte.reservation']; 	
	new ModalDialogConfirm(errorObj,lineItemId,deleteReservation);	
}

function deleteReservation(lineItemId){
	var proposalId=$("#amptResrvtnDataTable").jqGrid('getCell', lineItemId, 'proposalId');
	var ajaxReq = new AjaxRequest();
    ajaxReq.url = '../manageReservation/deleteReservedLineItem.action?lineItemId=' + lineItemId+'&proposalId='+proposalId;
    ajaxReq.success = function(result, status, xhr){
    	if(result.objectMap.gridKeyColumnValue){
	        jQuery("#runtimeDialogDiv").model({theme: 'Success', message: resourceBundle['reservation.deleted.successfully']});
	        $("#amptResrvtnDataTable").trigger("reloadGrid", [{
	            page: 1
	        }]);
	    } else{
			jQuery("#runtimeDialogDiv").model({theme: 'Error', autofade: false, message: resourceBundle['reservation.deleted.error']});
	    }
    };
    ajaxReq.submit();    
}

function viewCalendarFromGrid (lineItemId) {
	viewCalanderWindow(getParamsStrForCalendar(lineItemId));
}

function getParamsStrForCalendar(gsr){
    var proposalId = $("#amptResrvtnDataTable").jqGrid('getCell', gsr, 'proposalId');
    var productId = $("#amptResrvtnDataTable").jqGrid('getCell', gsr, 'productId');
    var salesTargetId = $("#amptResrvtnDataTable").jqGrid('getCell', gsr, 'salesTargetId');
    
    var startDate = $("#amptResrvtnDataTable").jqGrid('getCell', gsr, 'startDate');
    var endDate = $("#amptResrvtnDataTable").jqGrid('getCell', gsr, 'endDate');
    
    var lineItemTargetingJsonData = $("#amptResrvtnDataTable").jqGrid('getCell', gsr, 'lineItemTargetingData');
    var lineItemTargetingData = "";
    if (lineItemTargetingJsonData) {
        lineItemTargetingData = getTargetingJsonString(lineItemTargetingJsonData);
    }
    var returnParams = "productId=" + productId + "&salesTargetId=" + salesTargetId + "&startDate=" + startDate + "&endDate=" + endDate + "&proposalId=" + proposalId + "&lineItemTargetingData=" + lineItemTargetingData;
    return returnParams;
}

function getTargetingJsonString(lineItemTargetingData){
	var lineItemTargetings = new Array();
	var objId ="";
	var action = "";
	var not = "";
	var jsonObj = jQuery.parseJSON(lineItemTargetingData);
	
	var lineItemTargetingJsonData = "";
	$.each(jsonObj, function(i, item) {
		var object = new Object();		
		object.action = "";
		object.targetTypeId =  item.sosTarTypeId;
		object.targetTypeElement = item.sosTarTypeElement;		
		object.not = "";
		object.segmentLevel = "";
		
		lineItemTargetings.push(object);
	});
	
	return JSON.stringify(lineItemTargetings);
}

function setSalesTarget(){
	$("#reservationSalesTarget", "#manageReservationForm").empty();
	$("#reservationSalesTarget", "#manageReservationForm").multiselect("refresh");
	var ajaxReq = new AjaxRequest();
    ajaxReq.url = '../manageReservation/getSalesTarget.action';
    ajaxReq.cache = false;
    ajaxReq.success = function(result, status, xhr){
    	var arrVal = sortMapDataByValue(result);
    	for (var i = 0; i < arrVal.length ; i++) { 
    		$("#reservationSalesTarget").append($("<option></option>").val(arrVal[i][1]).html(arrVal[i][0]));
    	}
    	$("#reservationSalesTarget", "#manageReservationForm").val('');
    	$("#reservationSalesTarget", "#manageReservationForm").multiselect("refresh");
    };
    ajaxReq.submit();
}

var expirationDate ="";
function fillExpiryDate (productClassId,lineItemStartDate) {
		if (expirationDate == "" || expirationDate == null) {
			var startDate = new Date(lineItemStartDate);
			var months = new Array('01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12');
			var now = new Date();
			now.setDate(now.getDate() + 14);
			var modifiedStartDate = new Date();
			modifiedStartDate.setYear(now.getFullYear()+1);
			if(productClassId && productClassId == '28' && startDate > modifiedStartDate){
				var date = ((modifiedStartDate.getDate() < 10) ? "0" : "") + modifiedStartDate.getDate();
				$("#moveReservationExpiryDate", "#moveReservationsDialogue").val(months[modifiedStartDate.getMonth()] + "/" + date + "/" + modifiedStartDate.getFullYear());
			}
			else if(productClassId && productClassId != '28' && startDate > now){
				var date = ((now.getDate() < 10) ? "0" : "") + now.getDate();
				$("#moveReservationExpiryDate", "#moveReservationsDialogue").val(months[now.getMonth()] + "/" + date + "/" + now.getFullYear());
			}
			else{
				var date = ((startDate.getDate() < 10) ? "0" : "") + startDate.getDate();
				$("#moveReservationExpiryDate", "#moveReservationsDialogue").val(months[startDate.getMonth()] + "/" + date + "/" + startDate.getFullYear());
			}
		}	else{
			$("#moveReservationExpiryDate", "#moveReservationsDialogue").val(expirationDate);
		}
		createAdmDatePicker("calendar.gif");
}

function createAdmDatePicker(imgName){
	$( "#moveReservationExpiryDate" ).datepicker({
		showOn: "both",
		buttonImage: "../images/calendar.gif",
		buttonText: "Select Date",
		buttonImageOnly: true,
		onSelect: function(selectedDate, inst){
			var mdy = selectedDate.split('/');
			var expiryDate = new Date(mdy[2], mdy[0] - 1, mdy[1]);
			var now = new Date();
			now.setHours(0,0,0,0);
			/*if (expiryDate < now) {
				$(this).val(inst.lastVal);				
				$('#moveReservationExpiryDate', "#moveReservationsDialogue").addClass("errorElement errortip").attr("title", "Expiration Date can't be less than current date." + '<BR><b>Help:</b> ' + "Please provide valid date in the field.");
				renderErrorQtip("#moveReservationsDialogue");
			}*/
		}
	});
		
	/*var CurrentDate = new Date();
	$("#moveReservationExpiryDate", "#moveReservationsDialogue").datepicker("option", "minDate", new Date());
	CurrentDate.setMonth(CurrentDate.getMonth() + 12);
	$("#moveReservationExpiryDate", "#moveReservationsDialogue").datepicker("option", "maxDate", CurrentDate);*/
}

function showHideSosGrid(){
	var daysToExpire = jQuery("#noOfExpiryDays", "#manageReservationsContainer").val();
	var myReservation = $("#myReservation:checked", "#manageReservationsContainer").val();
	if(daysToExpire == "" && myReservation != 'true'){
		if(validateSearchInputs()){
			$("#sosResrvDataGridContainer", "#manageReservationsContainer").show();
			getSOSReservationData();
		}else{
			clearCSSErrors("#searchFieldsContainer");
		}
	}else{
		$("#sosResrvtnDataTable").jqGrid("clearGridData", true);
		$("#sosResrvDataGridContainer", "#manageReservationsContainer").hide();
	}
}

/**
 * Checks all the custom checkboxes of Ampt Reservation grid if the header checkbox is checked else unchecks all the checkboxes.
 * @param select
 */
function customAmptReservationSelect(select){
    if ($(select).is(":checked")) {
        $("#amptResrvtnDataTable").find(':checkbox').each(function(){
            $(this).attr("checked", "checked");
        });
    } else {
        $("#amptResrvtnDataTable").find(':checkbox').each(function(){
            $(this).attr("checked", false);
        });
    }
}

/**
 * Formatter for creating the custom checkbox in the Ampt Reservation grid. 
 * @param cellvalue
 * @param options
 * @param rowObject
 * @returns {String}
 */
function amptReservationMultiSelectFormatter(cellvalue, options, rowObject){
	return "<div align='center'><input type='checkbox' id='customAmptReservation_" + rowObject.lineItemId + "' name='customAmptReservation' onClick= 'selectUnselectHeaderChkBoxForAmptReservation(this)' class='allAvails' value='" + rowObject.lineItemId + "'>";
}

/**
 * Function for checking the checkbox in the header of the AMPT Reservation grid if all the custom checkboxes are checked else uncheck the header checkbox. 
 * @param select
 */
function selectUnselectHeaderChkBoxForAmptReservation(select) {
	if(!($(select).is(":checked"))){
		$("#AmptReservationMultiSelect_Header").attr("checked", false);
	} 
	if($('[name=customAmptReservation]:not(:checked)',"#amptResrvtnDataTable").size() == 0){
  		$("#AmptReservationMultiSelect_Header").attr("checked", true);
  	}		
}

/**
 * function validate before updating the expiry date of Reservations
 * @param selectedIds
 * @param selectedPropIds
 * @return
 */
function validateAndUpdateExpiryDate(selectedIds, selectedPropIds, selectedStDates){
	 var ajaxReq = new AjaxRequest();
	 ajaxReq.url = '../manageReservation/validateReservation.action?lineItemIds=' + selectedIds;
     ajaxReq.success = function(result, status, xhr){
    	 var msg = getErrorMessage(result);
    	 if(msg == ''){
    		 var renewReservation = new ModalDialog();
    		 renewReservation.width = 300;
    		 renewReservation.height = 140;
    		 renewReservation.resizable = false;
    		 renewReservation.draggable = true;    		 
	    	 renewReservation.buttons = [{
	             text: "Renew",
	             click: function(){
	             	clearCSSErrors("#renewReservationsValidationDialogue");
	             	var newExpiryDate = $("#renewReservationExpiryDate","#renewReservationsValidationDialogue").val();
	             	renewReservations(selectedIds,selectedPropIds,newExpiryDate);
	             }
	         }, {
	             text: "Close",
	             click: function(){
	        		$("#renewReservationExpiryDate").qtip("destroy");
	        		$("#renewReservationExpiryDate").removeClass("errorElement");
	        		$("#renewReservationExpiryDate").attr('title',"");
	             	$("#renewReservationExpiryDate").datepicker("destroy");
	                $("#renewReservationsValidationDialogue").dialog("close");
	             }
	         }];
	    	 fillExpiryDateForRenewReservation(selectedStDates);
	    	 $("#renewReservationsValidationDialogue").dialog(renewReservation);
	    	 enableDialogButton("renewReservationsValidationDialogue");
	    	 $("#renewReservationsValidationDialogue").dialog("open");
	    	 $('#renewReservationsValidationDialogue').bind('dialogclose', function(event) {
	    		 $("#renewReservationExpiryDate").qtip("destroy");
        		$("#renewReservationExpiryDate").removeClass("errorElement");
        		$("#renewReservationExpiryDate").attr('title',"");
	    	 });
	    	 $(window).resize();
    	 }else{
    		 jQuery("#runtimeDialogDiv").model({theme: 'Error', autofade: false, width: 380, height: 190, message: msg});
    	 }
     };
     ajaxReq.submit();
}

function getErrorMessage(resultObj){
	 var lineItemPastFlightLst = resultObj.objectMap.FLIGHT;
	 var lineItemInactiveProdLst = resultObj.objectMap.INACTIVE;
	 var msg='';
	 if(lineItemInactiveProdLst != '' && lineItemInactiveProdLst != null){	    	 
    	 msg = msg + resourceBundle['error.reservation.proposed.expireProdSt'];
    	 msg = msg + "</br>";
    	 $.each(lineItemInactiveProdLst, function(i, item){
    		 msg = msg + item;
    		 msg = msg + "</br>";
         });	    	
	 }
	 if(lineItemPastFlightLst != '' && lineItemPastFlightLst != null){	    	 
    	 msg = msg + resourceBundle['error.lineitem.reservation.past.flight.date'];
    	 msg = msg + "</br>";
    	 $.each(lineItemPastFlightLst, function(i, item){
    		 msg = msg + item;
    		 msg = msg + "</br>";
         });
	 }
	 return msg;
}

/**
 * Renew the reservation
 * @param selectedIds
 * @param newExpiryDate
 * @return
 */
function renewReservations(selectedIds,selectedPropIds,newExpiryDate){
	 var ajaxSaveReq = new AjaxRequest();
	 ajaxSaveReq.url = '../manageReservation/updateExpiryDate.action?lineItemIds=' + selectedIds + '&proposalIds=' + selectedPropIds + '&expiryDate=' + newExpiryDate;
	 ajaxSaveReq.success = function(result, status, xhr){
		if (result.validationFailed == true) {
			 $('#renewReservationExpiryDate').addClass("errorElement errortip").attr("title", result.errorList[0].errorMessageForUI+ '<BR><b>Help:</b> ' + result.errorList[0].errorHelpMessageForUI);
			 $('#renewReservationExpiryDate').qtip({
			        style: {
			            classes: 'ui-tooltip-red' 
			        },
					position:{my: 'bottom center',at: 'top center'}
			    });
			    $(".ui-dialog-buttonset button:contains('Renew')").button("enable");

         }else{
		 $("#renewReservationExpiryDate").datepicker("destroy");
		 $("#renewReservationExpiryDate").qtip("destroy");
 		 $("#renewReservationExpiryDate").removeClass("errorElement");
 		 $("#renewReservationExpiryDate").attr('title',"");
         $("#renewReservationsValidationDialogue").dialog("close");
		 jQuery("#runtimeDialogDiv").model({theme: 'Success', message: resourceBundle['message.generic.ReservationRenewedSucess']});
		 var LineItemIds = selectedIds.split(',');
		 for(var i=0; i< LineItemIds.length; i++){
			 jQuery('#amptResrvtnDataTable').jqGrid('setRowData', LineItemIds[i], {expiryDate:newExpiryDate});
		 }
		 $("#amptResrvtnDataTable").find(':checkbox').each(function(){
	            $(this).attr("checked", false);
	      });
	 }
	 };
	 ajaxSaveReq.submit();
	 $(".ui-dialog-buttonset button:contains('Renew')").button("disable");
}

function fillExpiryDateForRenewReservation (selectedStDates) {
		var months = new Array('01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12');
		var now = new Date();
		now.setDate(now.getDate() + 14);
		var date = ((now.getDate() < 10) ? "0" : "") + now.getDate();
		var startDateArr = selectedStDates.split(',');
		var minStartDate = startDateArr[0];
		for(var i=1; i< startDateArr.length; i++){
			if(minStartDate > startDateArr[i]){
				minStartDate = startDateArr[i];
			}
		}
		minStartDate = minStartDate.split("/");
		var expiryDate = new Date(minStartDate[2], minStartDate[0] - 1, minStartDate[1]);
		expiryDate.setDate(expiryDate.getDate());
		if(now < expiryDate){
			$("#renewReservationExpiryDate", "#renewReservationsValidationDialogue").val(months[now.getMonth()] + "/" + date + "/" + now.getFullYear());
		}else{
			var date = ((expiryDate.getDate() < 10) ? "0" : "") + expiryDate.getDate();
			$("#renewReservationExpiryDate", "#renewReservationsValidationDialogue").val(months[expiryDate.getMonth()] + "/" + date + "/" + expiryDate.getFullYear());
		}
		createAdmDatePickerForRenewReservation();
}

function createAdmDatePickerForRenewReservation(){
	$( "#renewReservationExpiryDate" ).datepicker({
		showOn: "both",
		buttonImage: "../images/calendar.gif",
		buttonText: "Select Date",
		buttonImageOnly: true,
		onSelect: function(selectedDate, inst){
			var mdy = selectedDate.split('/');
			var expiryDate = new Date(mdy[2], mdy[0] - 1, mdy[1]);
			var now = new Date();
			now.setHours(0,0,0,0);
			/*if (expiryDate < now) {
				$(this).val(inst.lastVal);				
				$('#renewReservationExpiryDate', "#renewReservationsValidationDialogue").addClass("errorElement errortip").attr("title", "Expiration Date can't be less than current date." + '<BR><b>Help:</b> ' + "Please provide valid date in the field.");
				renderErrorQtip("#renewReservationsValidationDialogue");
			}*/
		}
	});
		
	/*var CurrentDate = new Date();
	$("#renewReservationExpiryDate", "#renewReservationsValidationDialogue").datepicker("option", "minDate", new Date());
	CurrentDate.setMonth(CurrentDate.getMonth() + 12);
	$("#renewReservationExpiryDate", "#renewReservationsValidationDialogue").datepicker("option", "maxDate", CurrentDate);*/
}