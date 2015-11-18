/**
 * Java Script for manage attribute screen
 * 
 * @version 2.0
 */
jQuery(document).ready(function() {

	/**
	 * Initialised attribute JQGrid
	 */
	var manageAttGridOpts = new JqGridBaseOptions();
	manageAttGridOpts.url = "../manageAttribute/loadgriddata.action";
	manageAttGridOpts.colNames = ['Attribute ID', 'Name', 'Type', 'Type Name', 'Description', 'Value', 'Version'];
	manageAttGridOpts.colModel = [
		{name:'attributeId', index:'attributeId', hidden:true, key:true}, 
		{name:'attributeName', index:'attributeName', sortable:true, key:false}, 
		{name:'attributeTypeStr', index:'attributeTypeStr', sortable:true, key:false}, 
		{name:'attributeType', index:'attributeType', hidden:true, key:false}, 
		{name:'attributeDescription', index:'attributeDescription', hidden:true, key:false}, 
		{name:'attributeOptionalValue', index:'attributeOptionalValue', sortable:false, key:false}, 
		{name:'version', index:'version', hidden:true, key:false}
	];
	manageAttGridOpts.caption = resourceBundle['tab.generic.manageAttribute'];
	manageAttGridOpts.pager = jQuery('#manageAttributePager');
	manageAttGridOpts.sortname = "attributeId";
	manageAttGridOpts.afterGridCompleteFunction = function (){
		var topRowId = jQuery("#manageAttributeTable").getDataIDs()[0];
		if(topRowId == '' || topRowId == undefined){
			jQuery('#manageAttributeDetailContainer', '#manageAttributeContainer').hide();
		}
	};
	manageAttGridOpts.onSelectRow = function(id){
		jQuery('#attributeType', '#attributeForm').attr('disabled', 'disabled');
		clearCSSErrors('#attributeForm');	
		if(id){
			jQuery("#manageAttributeTable").jqGrid('GridToForm', id, "#attributeForm");
			jQuery('#manageAttributeDetailContainer', '#manageAttributeContainer').show();
			jQuery("textarea", "#attributeForm").change();
		}
		jQuery("#attributeDetailFormTab-1 a").text(resourceBundle["tab.generic.attributeDetails"]);
	};

	jQuery("#manageAttributeTable").jqGrid(manageAttGridOpts).navGrid('#manageAttributePager', {edit: false,search:false,
		addfunc: function(rowid){
			jQuery('#attributeType', '#attributeForm').removeAttr("disabled");
			resetRowSelectionOnAdding("#manageAttributeTable", "#attributeId");
			clearCSSErrors('#attributeForm');
			document.attributeForm.reset();
			jQuery("textarea", "#attributeForm").change();
			jQuery("#attributeId").val(0);
			jQuery('input[type!=hidden]:first', '#attributeForm').focus();
			jQuery('#manageAttributeDetailContainer', '#manageAttributeContainer').show();
			jQuery("#attributeDetailFormTab-1 a").text(resourceBundle["tab.generic.addNewAttribute"]);
		},
		delfunc: function(rowid){
			var attributeId = jQuery("#attributeId", "#attributeForm").val();
			if (attributeId == '0') {
				jQuery("#runtimeDialogDiv").model({message: "Please select a record to delete.", theme: 'Error', autofade: false});
			}
			else {
				jQuery("#manageAttributeTable").jqGrid('setGridParam', {page: 1});
				jQuery('#manageAttributeTable').delGridRow(attributeId, {
					url: '../manageAttribute/deleteAttribute.action?attributeId=' + attributeId
				});
			}
		},
		beforeRefresh: function(){
			jQuery("#attributSearchOption", "#manageAttributeContainer").val('attributeName');
			jQuery("#attributSearchValue", "#manageAttributeContainer").val('');
			reloadJqGridAfterAddRecord('manageAttributeTable', "attributeId");
		}
	});

	var instanceFormManager = new FormManager();
	instanceFormManager.gridName = 'manageAttributeTable';
	instanceFormManager.formName = 'attributeForm';

	/**
	 * Register click event on Save Button
	 */
	jQuery('#attributeSaveData', '#attributeForm').click(function() {
		var attributeDetailForm = new JQueryAjaxForm(instanceFormManager);
		attributeDetailForm.doCustomProcessingAfterFormSucsesJson = function(jsonResponse, XMLHTTPRequest){
			jQuery("#runtimeDialogDiv").model({theme: 'Success', message: resourceBundle['message.generic.attributeSavedSuccessfully']});
			var attributeId = jQuery("#attributeId").val();
			if (attributeId == 0) {
				reloadJqGridAfterAddRecord(instanceFormManager.gridName, "attributeId");
			}
			else {
				jQuery("#attributeTypeStr", '#attributeForm').val(jQuery('#attributeType option:selected', '#attributeForm').text());
				jQuery("#manageAttributeTable").jqGrid('FormToGrid', attributeId, "#attributeForm");
			}
		};
		attributeDetailForm.submit();
	});

	/**
	 * Register click event on Reset Button
	 */
	jQuery('#attributeResetData', '#attributeForm').click(function (){
		clearCSSErrors('#attributeForm');
		if(jQuery("#attributeDetailFormTab-1 a").text() == resourceBundle['tab.generic.addNewAttribute']){
			document.attributeForm.reset();
			jQuery('input[type!=hidden]:first', '#attributeForm').focus();
		} else {
			var gsr = jQuery("#manageAttributeTable").jqGrid('getGridParam','selrow');
			if(gsr){
				jQuery("#manageAttributeTable").jqGrid('GridToForm', gsr, "#attributeForm");
			} else {
				alert("Please select Row");
			}	
		}
		jQuery("textarea", "#attributeForm").change();
	});
	
	/**
	 * Apply tab plug-in in attribute detail section
	 */
	jQuery("#manageAttributeTab", "#manageAttributeDetailContainer").tabs();

	/**
	 * Initialised search panel on attribute grid
	 */
	initGridSearchOptions("manageAttributeTable", "attributSearchPanel", "manageAttributeContainer");

	/**
	 * Enable auto search on attribute grid
	 */
	enableAutoSearch(jQuery("#attributSearchValue", "#manageAttributeContainer"), function() {
		jQuery("#manageAttributeTable").jqGrid('setGridParam', {
			url: '../manageAttribute/loadgriddata.action', page: 1,
			postData: {
				searchField: jQuery("#attributSearchOption", "#manageAttributeContainer").val(),
				searchString: $.trim(jQuery("#attributSearchValue", "#manageAttributeContainer").val()),
				searchOper: 'cn'
			}
		}).trigger("reloadGrid");
	});
	
	/**
	 * Register change event on attribute search option select box
	 */
	jQuery("#attributSearchOption", "#manageAttributeContainer").bind("change", function () {
		jQuery("#attributSearchValue", "#manageAttributeContainer").val('').focus();
	});
	
	jQuery("#attributeDescription", "#manageAttributeContainer").limiter(500, $("#charsRemaining", "#manageAttributeContainer"));
});
