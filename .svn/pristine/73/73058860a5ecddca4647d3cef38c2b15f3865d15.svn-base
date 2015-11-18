/**
 * @author garima.garg
 */
$(document).ready(function() {
	/**
	 * Apply tab plug-in in tier detail section
	 */
	jQuery("#manageTierDetailForm", "#manageTierContainer").tabs();
	
	$("#targetTier", "#manageTierContainer").select2();
	$("#targetTier_select2", "#manageTierContainer").css("z-index", "0");
	
	jQuery("#level", "#tierDetail").numeric({
		negative: false
	});
	
	/**
	 * Creating the premium grid
	 */	
	var premiumGridOpts = new JqGridBaseOptions();
	premiumGridOpts.colNames = ['Id', 'Tier Id' , 'Target Type Id' , 'Target Type','Target Element Ids','Elements','Premium (%)', 'Action'];
	premiumGridOpts.colModel = [
	    {name:'id', index:'id', hidden:true, key:true},
	    {name:'premiumTierId', index:'premiumTierId', hidden:true},
	    {name:'targetTypeId', index:'targetTypeId', hidden:true},
    	{name:'targetName', index:'targetName'},
    	{name:'targetElementIds', index:'targetElementIds', hidden:true},
    	{name:'targetElements', index:'targetElements',sortable:false, width:300},
		{name:'premium', index:'premium', width:100, align:"right"},
		{name:'act', index:'act', sortable:false, resize:false, width:50}
	];	
	premiumGridOpts.pager = $('#premiumPager');
	premiumGridOpts.sortname = 'premiumID';
	premiumGridOpts.afterGridCompleteFunction = function() {
		var tierId = $('#tierId'  , '#tierDetail').val();
		var ids = jQuery('#premiumTable').jqGrid('getDataIDs');
		for(var i = 0;i < ids.length; i++){
			var cl = ids[i];
			be = "<div style='padding-left:25%'>" +
					"<div style='float:left;' title='Edit'>" +
						"<a onClick='editPremium("+ cl +"," + tierId +")'>" +
							"<span class='ui-icon ui-icon-pencil' style='cursor:pointer;'/>" +
						"</a>" +
					"</div>" +
					"<div style='float:left;margin-left:10%' title='Delete'>" +
						"<a onclick='deletePremium("+ cl +"," + tierId +")'>" +
							"<span class='ui-icon ui-icon-trash' style='cursor:pointer;'/>" +
						"</a>" +
					"</div>" +
				"</div>"; 
			jQuery('#premiumTable').jqGrid('setRowData', cl, {act:be});
		}
	};
	
	$("#premiumTable").jqGrid(premiumGridOpts).navGrid('#premiumPager',{		
		search:false,edit:false,del:false,
		addfunc:function (){			
			showPremiumDetails(false);
		}
	});
	
	var clonePremiumDlg = new ModalDialog();
    clonePremiumDlg.height = 230;
    clonePremiumDlg.width = 570;
    clonePremiumDlg.resizable = false;	
    clonePremiumDlg.buttons = [{
        text: "Clone",
        click: function(){
    		checkDataOnTier();
    	}
    }, {
        text: "Ok",
        click: function() {
    		$(this).dialog("close");
            clonePremium();
        }
    }, {
        text: "Cancel",
        click: function(){			
            $(this).dialog("close");
        }
    }];
	$("#tierPremiumCloneForm").dialog(clonePremiumDlg);
	enableDialogButton("tierPremiumCloneForm");
	/**
	 * Creating the master Tier Grid
	 */
	var listTierGridOpts = new JqGridBaseOptions();
	listTierGridOpts.url = "../manageTier/loadgriddata.action";
	listTierGridOpts.colNames = ['Tier Id', 'Name', 'Sections', 'Level'];
	listTierGridOpts.colModel = [
		{name:'tierId', index:'tierId', hidden:true, key:true}, 
		{name:'tierName', index:'tierName'}, 
		{name:'sectionNames', index:'sectionNames', width:300, sortable:false}, 
		{name:'level', index:'level', hidden:false, width:100}
	];
	
	listTierGridOpts.caption = resourceBundle['tab.generic.manageTier'];
	listTierGridOpts.pager = jQuery('#manageTierPager');
	listTierGridOpts.sortname = "tierId";
	listTierGridOpts.afterGridCompleteFunction = function (){
		var topRowId = jQuery("#manageTierTable").getDataIDs()[0];
		if(topRowId == '' || topRowId == undefined){
			jQuery('#manageTierDetailForm', '#manageTierContainer').hide();
			document.tierDetail.reset();
			$('#tierSections' , '#tierDetail').empty();
			$('#tierId'  , '#tierDetail').val('0');
		}
	};
	
	listTierGridOpts.onSelectRow = function(id){
		clearCSSErrors('#manageTierDetailForm');	
		if(id){
			jQuery("#manageTierTable").jqGrid('GridToForm', id, "#tierDetail");
			getTierSections();
			var tierId = $('#tierId'  , '#tierDetail').val();
			$('#tierSections' , '#tierDetail').empty();
			getSelectedTierSections(tierId);
			jQuery('#manageTierDetailForm', '#manageTierContainer').show();
			jQuery("#ManageTierDetailFormTab-1 a").text(resourceBundle["tab.generic.tierDetails"]);
			jQuery('#ManageTierDetailFormTab-2', '#manageTierDetailForm').show();
			
			$('#premiumTable').setGridParam({url:'../manageTier/loadPremiumGridData.action?tierId=' + tierId });
			$('#premiumTable').trigger("reloadGrid");
		}
	};
	
	jQuery("#manageTierTable").jqGrid(listTierGridOpts).navGrid('#manageTierPager', {edit: false,search:false,
		addfunc: function(rowid){
			resetRowSelectionOnAdding("#manageTierTable", "#tierid");
			clearCSSErrors('#manageTierDetailForm');
			document.tierDetail.reset();
			$('#tierSections' , '#tierDetail').empty();
			$('#tierId'  , '#tierDetail').val('0');
			getTierSections();
			jQuery('#manageTierDetailForm', '#manageTierContainer').show();
			jQuery("#ManageTierDetailFormTab-1 a").text(resourceBundle["tab.generic.addNewTier"]);
			jQuery('#ManageTierDetailFormTab-2', '#manageTierDetailForm').hide();
			jQuery('#ManageTierDetailFormTab-1 a').click();
		},
		delfunc: function(rowid){
			var tierId = jQuery('#tierId'  , '#tierDetail').val();
			jQuery("#manageTierTable").jqGrid('setGridParam', {page: 1});
			jQuery('#manageTierTable').delGridRow(tierId, {
				url: '../manageTier/deleteTier.action?tierId=' + tierId
			});
		},
		beforeRefresh: function(){
			jQuery("#tierSearchOption", "#manageTierContainer").val('tierName');
			jQuery("#tierSearchValue", "#manageTierContainer").val('');
			reloadJqGridAfterAddRecord('manageTierTable', "tierId");
		}		
	});
	
	$("#manageTierTable").jqGrid('navButtonAdd', "#manageTierPager", {caption: "", id: "clonePremium_manageTierTable", title: "Clone Premiums",
		onClickButton: function() {
			var gsr = $("#manageTierTable").jqGrid('getGridParam', 'selrow');
			if(gsr){
				var tierName = $('#tierName'  , '#tierDetail').val();
				var tierId = $('#tierId'  , '#tierDetail').val();
				$('#cloneTierFrom', '#tierPremiumCloneForm').html(tierName);
				
				$('#sourceTierId', '#tierPremiumCloneForm').val(tierId);
				
				$("#tierPremiumCloneForm > table").show();
				$("#targetTierPremiumExist", "#tierPremiumCloneForm").hide();
				
				// hide OK button and show CLONE button in Clone Dialog
				$(".ui-dialog-buttonset button:contains('Ok')").hide();		
				$(".ui-dialog-buttonset button:contains('Clone')").show();
				$(".ui-dialog-buttonpane #errorDiv").hide();
				// remove all options from target sales category and clone from source
				$("#targetTier option", "#tierPremiumCloneForm").remove();
				
				getTierNames();
				
				$("#tierPremiumCloneForm").dialog("open");
				clearCSSErrors("#tierPremiumCloneForm");
			} else {
				jQuery("#runtimeDialogDiv").model({theme: 'Error', height: 140, width: 250, message: resourceBundle['message.generic.tierSelect'], autofade: false});
			}
		} 
	});
	
	var instanceFormManager = new FormManager();
	instanceFormManager.gridName = 'manageTierTable';
	instanceFormManager.formName = 'tierDetail';
	/**
	 * Register click event on Save Button
	 */
	jQuery('#listTierSaveData', '#tierDetail').click(function() {
		$('#tierSections', '#tierDetail').find('option').each(function(){
			$(this).attr('selected', 'selected');
		});
		var tierDetailForm = new JQueryAjaxForm(instanceFormManager);
		tierDetailForm.doCustomProcessingAfterFormSucsesJson = function(jsonResponse, XMLHTTPRequest){
			jQuery("#runtimeDialogDiv").model({theme: 'Success', message: resourceBundle['message.generic.tierSavedSuccessfully']});
			var tierId = jQuery("#tierId").val();
			
			if (tierId == 0) {
				reloadJqGridAfterAddRecord(instanceFormManager.gridName, "tierId");
			}
			else {
				jQuery("#manageTierTable").jqGrid('FormToGrid', tierId, "#tierDetail");	
				var sections = $('#tierSections option', '#tierDetail').map(function(){
			        return this.text;
			    }).get().join(', ');
				jQuery("#manageTierTable").setCell(tierId, "sectionNames", sections);
				
			}
		};
		tierDetailForm.submit();
	});
	
	/**
	 * Initialised search panel on tier grid
	 */
	initGridSearchOptions("manageTierTable", "tierSearchPanel", "manageTierContainer");
	
	/**
	 * Enable auto search on tier grid
	 */
	enableAutoSearch(jQuery("#tierSearchValue", "#manageTierContainer"), function() {
		jQuery("#manageTierTable").jqGrid('setGridParam', {
			url: '../manageTier/loadgriddata.action', page: 1,
			postData: {
				searchField: jQuery("#tierSearchOption", "#manageTierContainer").val(),
				searchString: $.trim(jQuery("#tierSearchValue", "#manageTierContainer").val()),
				searchOper: 'cn'
			}
		}).trigger("reloadGrid");
	});
	
	/**
	 * Register change event on tier search option select box
	 */
	jQuery("#tierSearchOption", "#manageTierContainer").bind("change", function () {
		jQuery("#tierSearchValue", "#manageTierContainer").val('').focus();
	});
});

/**
 * Register click event on Reset Button
 */
jQuery('#listTierResetData', '#tierDetail').click(function (){
	clearCSSErrors('#tierDetail');
	if(jQuery("#ManageTierDetailFormTab-1 a").text() == resourceBundle['tab.generic.addNewTier']){
		document.tierDetail.reset();
		$('#tierSections' , '#tierDetail').empty();
		$('#tierId'  , '#tierDetail').val('0');		
		jQuery('input[type!=hidden]:first', '#tierDetail').focus();
	} else {
		var tierId = jQuery("#manageTierTable").jqGrid('getGridParam','selrow');
		if(tierId){
			jQuery("#manageTierTable").jqGrid('GridToForm', tierId, "#tierDetail");
			getSelectedTierSections(tierId);
		} else {
			alert("Please select Row");
		}	
	}
	getTierSections();
});

function getTierSections(){
	var ajaxReq = new AjaxRequest();
	ajaxReq.url = "../manageTier/getTierSections.action";
	ajaxReq.success = function(result, status, xhr){
		var tierSections = result.objectMap.gridKeyColumnValue;
		$('option', $('#availTierSections')).remove();
		sortList(tierSections, $('#availTierSections'));
	};
	ajaxReq.submit();
}

function getSelectedTierSections(tierId){
	var tierId = jQuery("#tierId").val();
	var ajaxReq = new AjaxRequest();
	ajaxReq.url = "../manageTier/getSelectedTierSections.action?tierId=" + tierId ;
	ajaxReq.success = function(result, status, xhr){
		var tierSections = result.objectMap.gridKeyColumnValue;
		$('option', $('#tierSections')).remove();
		sortList(tierSections, $('#tierSections' , '#tierDetail'));
	};
	ajaxReq.submit();
}

function getSelectedTargetTypeElements(selectedTargetIds, premiumId){
    var targetTypeId = $("#targetTypeId option:selected").val();
    $('#hidTargetTypeId', '#tierPremiumDetail').val(targetTypeId);
    var targetName = $("#targetTypeId option:selected").text();
    $('#hidTargetName', '#tierPremiumDetail').val(targetName);
    clearCSSErrors('#tierPremiumDetail');
    
    if (targetTypeId != "") {
    	var select = $("#targetTypeElements", "#tierPremiumDetail");
		var actionUrl = "../manageTier/getSelectedTargetTypeElements.action?targetTypeId=" + targetTypeId + "&targetName=" + targetName + "&tierId=" + $('#tierId', '#tierDetail').val() + "&premiumId=" + premiumId;
		if(selectedTargetIds != null){
			var targetElements = selectedTargetIds.split(",");
		}		
		dynaLoadTargetTypeElements(targetElements, select ,actionUrl);
    }else{
    	var select = $("#targetTypeElements", "#tierPremiumDetail");
    	$('option', select).remove();
    	$(select).multiselect("refresh");    	
    }
}

function dynaLoadTargetTypeElements(selectedAttribute , select , actionUrl){
	
	var ajaxReq = new AjaxRequest();
	ajaxReq.url = actionUrl;
	ajaxReq.cache = false;
	ajaxReq.success = function(result, status, xhr){
		$('option', select).remove();
		var targetTypeElements = result.objectMap.gridKeyColumnValue;
		sortList(targetTypeElements, select);
		if(selectedAttribute != null){
			$(select).val(selectedAttribute);
		}
		
		$(select).multiselect("refresh");
	};
	ajaxReq.submit();
}


function saveTierPremium(isEdit , premiumId){
	var instanceFormManager = new FormManager();
	instanceFormManager.gridName = 'premiumTable';
	instanceFormManager.formName = 'tierPremiumDetail';
	
	var premiumDetailForm = new JQueryAjaxForm(instanceFormManager);
	premiumDetailForm.doCustomProcessingAfterFormSucsesJson = function(jsonResponse, XMLHTTPRequest){
		jQuery("#runtimeDialogDiv").model({theme: 'Success', message: resourceBundle['message.generic.premiumSavedSuccessfully']});
		
		$("#addTierPremium").dialog("close");
		if(!isEdit){
			reloadJqGridAfterAddRecord('premiumTable', "premiumID");
		}else{
            var sections = $('#targetTypeElements option:selected', '#tierPremiumDetail').map(function(){
                return this.text;
        	}).get().join(',');
            if(sections == ""){
            	sections = "Default";
            }
        	jQuery("#premiumTable").setCell(premiumId, "targetElements", sections);
       		
       		var elements = $('#targetTypeElements', '#tierPremiumDetail').val();
       		jQuery("#premiumTable").setCell(premiumId, "targetElementIds", elements);
       		
       		var premium = $('#premium', '#tierPremiumDetail').val();
       		jQuery("#premiumTable").setCell(premiumId, "premium", premium);
		}
	};
	premiumDetailForm.doCustomProcessingAfterValidationFailedJson = function(jsonResponse, XMLHTTPRequest){
		$(".ui-dialog-buttonset button:contains('Save')").button("enable");
	};
	premiumDetailForm.submit();
	$(".ui-dialog-buttonset button:contains('Save')").button("disable");
}

function closeTierPremium(){
	$("#addTierPremium").dialog("close");
}

function editPremium(rowId, tierId){
	showPremiumDetails(true, rowId);
}

function deletePremium(rowId, tierId){
	  $("#premiumTable").jqGrid('setGridParam', {page: 1});
	jQuery('#premiumTable').delGridRow(rowId, {
		url: '../manageTier/deleteTierPremium.action?premiumRowId=' + rowId
	});
}

function showPremiumDetails(isEdit, premiumId){
    var ajaxReq = new AjaxRequest();
    ajaxReq.dataType = 'html';
    ajaxReq.url = "../manageTier/viewPremiumDetail.action";
    ajaxReq.success = function(result, status, xhr){
        $("#addTierPremium").html(result);
        var addEditPremium = new ModalDialog();
        addEditPremium.width = 750;
        addEditPremium.height = 300;
		addEditPremium.title = 'Premium';
        addEditPremium.resizable = false;
        addEditPremium.draggable = true;
        addEditPremium.buttons.Save = function(){
            saveTierPremium(isEdit, premiumId);
        }
        addEditPremium.buttons.Close = function(){
            closeTierPremium();
        }
        
        $("#addTierPremium").dialog(addEditPremium);
        enableDialogButton("addTierPremium");
        $('#premiumTierId').val($('#tierId', '#tierDetail').val());
        
        if (isEdit) {
            jQuery("#premiumTable").jqGrid('GridToForm', premiumId, "#tierPremiumDetail");
            var rowData = $("#premiumTable").jqGrid('getRowData', premiumId);
            $('#targetTypeId').val(rowData.targetTypeId);
            $('#targetTypeId').attr('disabled', true);
            $('#hidTargetTypeId', '#tierPremiumDetail').val(rowData.targetTypeId);
            $('#hidTargetName', '#tierPremiumDetail').val(rowData.targetName);
            getSelectedTargetTypeElements(rowData.targetElementIds, premiumId);
        }
        
        $("#addTierPremium").dialog("open");
        jQuery("input[class=numericdecimal]", "#addEditTierPremiumDiv").numeric({
            negative: false
        });
        
        $("#targetTypeElements", "#addEditTierPremiumDiv").multiselect({
    		selectedList: 2,
    		noneSelectedText: 'Default'
    	}).multiselectfilter();
		$(".ui-dialog-buttonset button:contains('Save')").button("enable");
    };
    ajaxReq.submit();
}

/**
 * 
 * @return
 */
function clonePremium () {
	var tierId = $("#tierId").val();
	var ajaxRequest = new AjaxRequest();
	ajaxRequest.url = "../manageTier/copyPremiums.action?sourceTierId="
			+ tierId + '&targetTierId=' + $("#targetTier", "#tierPremiumCloneForm").val()
	
	ajaxRequest.cache = false;
	ajaxRequest.success = function(result, status, xhr) {
		$("#tierPremiumCloneForm").dialog("close");
		jQuery("#runtimeDialogDiv").model({theme: 'Success', message: resourceBundle['message.generic.premiumClonedSuccessfully']});
	};
	ajaxRequest.submit();
}


function getTierNames(){
	var tierId = jQuery("#tierId").val();
	if(tierId != ""){
		var ajaxReq = new AjaxRequest();
		ajaxReq.url = "../manageTier/getTierNames.action?tierId=" + tierId;
		ajaxReq.success = function(result, status, xhr){
			var tierNames = result.objectMap.gridKeyColumnValue;
			sortList(tierNames, $('#targetTier'));
			$("#targetTier", "#tierPremiumCloneForm").trigger("change");
		};
		ajaxReq.submit();
	}
}


function checkDataOnTier() {
	if("" == $("#targetTier option:selected").val() || $("#targetTier option:selected").val() == undefined){
		$('#targetTier_select2', '#tierPremiumCloneForm').addClass("errorElement errortip").attr("title", "Target Tier is mandatory." + '<BR><b>Help:</b> ' + "Please select valid data in the field.");
		$('#targetTier' , '#tierPremiumCloneForm').addClass("errorElement errortip");
		renderErrorQtip("#tierPremiumCloneForm");
	}else {
		var ajaxReq = new AjaxRequest();
		ajaxReq.url = "../manageTier/checkPremiumAvailabilityForTier.action?targetTierId="
					 + $('#targetTier', '#tierPremiumCloneForm').val()
					 + "&fromTierId=" + $('#sourceTierId', '#tierPremiumCloneForm').val();
		ajaxReq.success = function(result, status, xhr) {
			if (result.objectMap.premiumDataNotExistInSrcTier) {			
				if ( $("#errorDiv", "div[aria-labelledby=ui-dialog-title-tierPremiumCloneForm]").length == 0) {
					$(".ui-dialog-buttonpane", "div[aria-labelledby=ui-dialog-title-tierPremiumCloneForm]").append("<div id='errorDiv' class='model-dialog-error'>" + resourceBundle['error.tierPremium.clone.Nodata'] + "</div>");
				}
		        $(".ui-dialog-buttonpane #errorDiv").show();
			} else if (result.objectMap.premiumDataExistInTargetTier) {
				$("#tierPremiumCloneForm > table").hide();
				$(".ui-dialog-buttonset button:contains('Clone')").hide();
				$(".ui-dialog-buttonset button:contains('Ok')").show();	
				var targetTierName = $("#targetTier option:selected", "#tierPremiumCloneForm").text();
				$("#selectedTier", "#targetTierPremiumExist").text("\"" + targetTierName + "\"");
				
				$("#targetTierPremiumExist", "#tierPremiumCloneForm").show();
			} else {
				clonePremium();
			}
		};
		$("#errorDiv", "div[aria-labelledby=ui-dialog-title-availStatusSummary]").hide();
		ajaxReq.submit();
	}
}