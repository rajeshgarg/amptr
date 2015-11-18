/**
 * Java Script for manage creative screen
 * 
 * @version 2.0
 */
var top_rowid = "";

var creativeAttributeSortName = "createdDate";

jQuery(document).ready(function() {

	jQuery( "#creativeTab", "#manageCreativeContainer" ).tabs();	

	jQuery("#width , #height , #width2 , #height2", "#manageCreativeContainer").numeric({
		negative: false
	});

	/**
	 * Creative attachment Grid and attachment dialog setup
	 */
	initDocUploadHTML('creativeAttachement', 'CREATIVE');
	var creativeAttachementGridOpts = initAttachementGridOpts('creativeAttachement');
	creativeAttachementGridOpts.afterGridCompleteFunction = function(){
		if (jQuery('#creativeAttachementTable').jqGrid('getGridParam', 'records') > 0) {
			jQuery('#creativeTable').setCell(jQuery("#creativeForm #creativeId").val(), 'hasDocument', true);
		}
		else {
			jQuery('#creativeTable').setCell(jQuery("#creativeForm #creativeId").val(), 'hasDocument', false);
		}
	};
	jQuery("#creativeAttachementTable").jqGrid(creativeAttachementGridOpts).navGrid('#creativeAttachementPager', {
		edit: false, search: false,
		addfunc: function(rowid){
			jQuery("#creativeAttachementForm #id").val('-1');
			clearCSSErrors('#creativeAttachementForm');
			jQuery("#creativeAttachementForm #fileName").val("");
			jQuery("#creativeAttachementDialog").dialog("open");
		}
	});
	
	/**
	 * Register AJAX form for creative attachment upload dialog 
	 */
	var creativeAttachementFormManager = new FormManager();
	creativeAttachementFormManager.gridName = 'creativeAttachementTable';
	creativeAttachementFormManager.formName = 'creativeAttachementForm';
		
	var creativeAttachement = new ModalDialog();	

	/**
	 * Provide functionality for attachment dialog save button
	 */
	creativeAttachement.buttons.Save = function(){
		jQuery("#creativeAttachementForm #componentId").val(jQuery("#creativeId").val());
		if (jQuery("#creativeAttachementForm #id").val() != -1) {
			var id = jQuery("#creativeAttachementTable").jqGrid('getGridParam', 'selrow');
			var ret = jQuery("#creativeAttachementTable").jqGrid('getRowData', id);
			jQuery("#creativeAttachementForm #fileName").val(ret.fileName);
		}

		/**
		 * Register AJAX Forms Submit and provide various function
		 */
		var creativeAttachementForm = new JQueryAjaxForm(creativeAttachementFormManager);
		creativeAttachementForm.dataType = 'xml';
		creativeAttachementForm.doCustomProcessingAfterValidationFailedXML = function(){
			jQuery("#creativeAttachementMessage").text("");
			jQuery(".ui-dialog-buttonset button:contains('Save')").button("enable");
		};
		creativeAttachementForm.doCustomProcessingAfterFormSucsessXML = function(){
			jQuery(creativeAttachementFormManager.formIdentifier()).resetForm();
			jQuery("#creativeAttachementMessage").text(resourceBundle['message.generic.documentUploadSuccess']).css("color", "green");
			jQuery(".ui-dialog-buttonset button:contains('Save')").button("enable");
		};
		creativeAttachementForm.doCustomProcessingAfterFormError = function(){
			jQuery("#creativeAttachementMessage").text("");
			jQuery(".ui-dialog-buttonset button:contains('Save')").button("enable");
		};

		creativeAttachementForm.submit();
		jQuery(".ui-dialog-buttonset button:contains('Save')").button("disable");
    };
	
	/**
	 * Provide functionality for attachment dialog close button
	 */
	creativeAttachement.buttons.Close = function(){
		jQuery(this).dialog("close");
	};
	
	creativeAttachement.close = function () {
		jQuery("#creativeAttachementForm").resetForm();
		jQuery("#creativeAttachementMessage").text("");
		jQuery("#creativeAttachementTable").trigger("reloadGrid");
	};
	
	jQuery( "#creativeAttachementDialog").dialog(creativeAttachement);
	enableDialogButton("creativeAttachementDialog");
	
	/**
	 * Initialised creative attribute JQGrid
	 */
	var creativeAttGridOpts = new JqGridBaseOptions();
	creativeAttGridOpts.colNames = ['AttributeId','Attribute Name','Attribute Description','Version','Attribute Value','created Date','Action'];
	creativeAttGridOpts.colModel = [		          		
		{name:'attributeId', index:'attributeId', hidden:true, key:true},
		{name:'attributeName', index:'attributeName'},
		{name:'attributeDescription', index:'attributeDescription'},
		{name:'version', index:'version', hidden:true, sortable:false},
		{name:'attributeValue', index:'attributeValue', sortable:false},
		{name:'createdDate', index:'createdDate', hidden:true},
		{name:'attrAssocAct', index:'attrAssocAct', width:100, fixed:true, resize:false, sortable:false}
	];
	creativeAttGridOpts.pager = jQuery('#creativeAttributePager');
	creativeAttGridOpts.sortname = creativeAttributeSortName;
	creativeAttGridOpts.editurl = '../manageCreative/updateCreativeAttributesValue.action';	
	creativeAttGridOpts.afterGridCompleteFunction = function() {
		var ids = jQuery('#creativeAttributeTable').jqGrid('getDataIDs');
		for(var i = 0;i < ids.length; i++){
			var cl = ids[i];
			be = "<div style='padding-left:25%'>" +
					"<div style='float:left;' title='Edit'>" +
						"<a onclick='editCreativeAttributeAssoc("+ cl +")'>" +
							"<span class='ui-icon ui-icon-pencil' style='cursor:pointer;'/>" +
						"</a>" +
					"</div>" +
					"<div style='float:left;margin-left:10%' title='Delete'>" +
						"<a onclick='deleteCreativeAttributeAssoc("+ cl +")'>" +
							"<span class='ui-icon ui-icon-trash' style='cursor:pointer;'/>" +
						"</a>" +
					"</div>" +
				"</div>"; 
			jQuery('#creativeAttributeTable').jqGrid('setRowData', cl, {attrAssocAct:be});
		}
	};

	jQuery("#creativeAttributeTable").jqGrid(creativeAttGridOpts).navGrid('#creativeAttributePager',{search:false,edit:false,del:false,
		addfunc:function (rowid){
			showAttributes(jQuery("#creativeId").val(),"creative","","add");
		},
		afterRefresh:function(){
			clearCSSErrors('#creativeAttributeContainer');
		}
	});		
	
	/**
	 * Initialised JQgrid for creative
	 */
	var creativeGridOpts = new JqGridBaseOptions();
	creativeGridOpts.url = "../manageCreative/loadgriddata.action";
	creativeGridOpts.colNames = ['ID', 'Name', 'Type', 'Type Name', 'Version', 'Width1', 'Height1', 'Width2', 'Height2', 'Description', 'Has Documents'];
	creativeGridOpts.colModel = [
		{name:'creativeId', index:'creativeId', hidden:true, key:true}, 
		{name:'name', index:'name', sortable:true, key:false, width:30}, 
		{name:'typeStr', index:'typeStr', sortable:true, key:false, width:20}, 
		{name:'type', index:'type', sortable:true, hidden:true, key:false},
		{name:'version', index:'version', hidden:true, key:false}, 
		{name:'width', index:'width', sortable:true, key:false, align:"right", width:10}, 
		{name:'height', index:'height', sortable:true, key:false , align:"right", width:10}, 
		{name:'width2', index:'width2', sortable:true, key:false , align:"right", width:10}, 
		{name:'height2', index:'height2', sortable:true, hidden:false, key:false , align:"right", width:10}, 
		{name:'description', index:'description', hidden:true, key:false}, 
		{name:'hasDocument', index:'hasDocument', sortable:false, key:false, formatter:attachedDocumentFormatter, width:10}
	];
	creativeGridOpts.onSelectRow = function(id) {
		clearCSSErrors('#creativeForm');	
		clearCSSErrors('#creativeAttributeContainer');	
		var gsr = jQuery("#creativeTable").jqGrid('getGridParam','selrow');
		if(gsr){
			jQuery("#creativeTable").jqGrid('GridToForm',gsr, "#creativeForm");
			jQuery("#creativeTab", "#manageCreativeContainer").show();
			jQuery("textarea", "#manageCreativeContainer").change();
		} else {
			alert("Please select Row");
		}	
		jQuery("#creativeForm :input").removeAttr('disabled');
		jQuery("#creativeAttributeTab-1").show();
		
		var creativeid = jQuery("#creativeId").val();			
		jQuery('#creativeAttributeTable').setGridParam({url:'../manageCreative/attributesgriddata.action?creativeId='+creativeid});
		jQuery('#creativeAttributeTable').trigger("reloadGrid");
		
		jQuery("#creativeDocumentTab-1").show();
		jQuery('#creativeAttachementTable').setGridParam({url:'../document/documentgriddata.action?documentFor=CREATIVE&componentId='+creativeid, 
													 editurl: '../document/deletedocument.action?documentFor=CREATIVE&componentId='+creativeid});
		jQuery('#creativeAttachementTable').trigger("reloadGrid");
		
		jQuery("#detailFormTab-1 a").text(resourceBundle['tab.generic.creativeDetails']);
		document.getElementById("attributeGridHider").style.display = 'block';
	};
	creativeGridOpts.afterGridCompleteFunction = function(){
		if(jQuery('#creativeTable').getDataIDs().length == 0) {					
			document.getElementById("creativeTab").style.display = 'none';
		} else if(top_rowid == "") {
			document.getElementById("creativeTab").style.display = 'block';
		}
		if(top_rowid == "0" || top_rowid == 0){ 
			top_rowid == "";
		}		 
	};
	creativeGridOpts.caption = resourceBundle['label.generic.manageCreative'];
	creativeGridOpts.pager = jQuery('#creativePager');
	creativeGridOpts.sortname = "creativeId";
	
	jQuery("#creativeTable").jqGrid(creativeGridOpts).navGrid('#creativePager',{edit:false,search:false,
		addfunc:function (rowid){clearCSSErrors('#creativeForm');
			resetRowSelectionOnAdding("#creativeTable", "#creativeId");
			document.getElementById("creativeTab").style.display = 'block';
			document.creativeForm.reset();
			jQuery("textarea", "#manageCreativeContainer").change();
			jQuery("#detailFormTab-1 a").text(resourceBundle['tab.generic.addNewCreative']);
		    jQuery("#creativeForm :input").removeAttr('disabled');
			
			/**
			 * Value 0 is assigned forcefully at the time of adding to avoid Number format exception otherwise blank string is passed to the form
			 */	
			jQuery("#creativeId").val(0);	
			var index = jQuery('#creativeTab a[href="#creative-tabs-1"]').parent().index();
			
			/**
			 * Code for moving the focus on to a tab from different tab
			 */
			jQuery('#creativeTab').tabs('select', index);
			jQuery("#creativeAttributeTab-1").hide();
			jQuery("#creativeDocumentTab-1").hide();
			jQuery('input[type!=hidden]:first', '#creativeForm').focus();
		},
		delfunc:function(rowid){		
			var creativeId = jQuery("#creativeId", "#creativeForm").val();
			if (creativeId == '0') {
				jQuery("#runtimeDialogDiv").model({message: "Please select a record to delete.", theme: 'Error', autofade: false});
			}
			else {
				jQuery("#creativeTable").jqGrid('setGridParam', {page: 1});
				jQuery('#creativeTable').delGridRow(creativeId, {
					url: '../manageCreative/deletecreative.action?creativeId=' + creativeId
				});
			}
		},
		beforeRefresh: function(){
			jQuery("#creativeSearchOption", "#manageCreativeContainer").val('name');
			jQuery("#creativeSearchValue", "#manageCreativeContainer").val('');
			reloadJqGridAfterAddRecord('creativeTable', "creativeId");
		}
	});

	/**
	 * Register click event on Reset Button
	 */
	jQuery("#creativeResetData").click(function () {
		clearCSSErrors('#creativeForm');
		if(jQuery("#detailFormTab-1 a").text() == resourceBundle['tab.generic.addNewCreative']){
			document.creativeForm.reset();
			jQuery('input[type!=hidden]:first', '#creativeForm').focus();
		} else {
			var gsr = jQuery("#creativeTable").jqGrid('getGridParam','selrow');
			if(gsr){
				jQuery("#creativeTable").jqGrid('GridToForm', gsr, "#creativeForm");
			} else {
				alert("Please select Row");
			}
		}
		jQuery("textarea", "#manageCreativeContainer").change();
	});

	/**
	 * Register click event on Save Button
	 */
	jQuery("#creativeForm #creativeSaveData").click(function(){
		var instanceFormManager = new FormManager();
		instanceFormManager.gridName = 'creativeTable';
		instanceFormManager.formName = 'creativeForm';
		var creativeDetailForm = new JQueryAjaxForm(instanceFormManager);

		creativeDetailForm.doCustomProcessingAfterFormSucsesJson = function(jsonResponse, XMLHTTPRequest){
			jQuery("#runtimeDialogDiv").model({theme: 'Success', message: resourceBundle['message.generic.creativeSavedSuccessfully']});
			var creativeId = jQuery("#creativeId").val();
			if (creativeId == 0) {
				reloadJqGridAfterAddRecord(instanceFormManager.gridName, "creativeId");
			}
			else {
				jQuery("#typeStr", "#creativeForm").val(jQuery('#type option:selected', "#creativeForm").text());
				jQuery("#creativeTable").jqGrid('FormToGrid', creativeId, "#creativeForm");
			}
		};
		creativeDetailForm.submit();
	});

	/**
	 * Initialised search panel on creative grid
	 */
	initGridSearchOptions("creativeTable", "creativeSearchPanel", "manageCreativeContainer");
	
	/**
	 * Enable auto search on creative grid
	 */
	enableAutoSearch(jQuery("#creativeSearchValue", "#manageCreativeContainer"), function() {
		jQuery("#creativeTable").jqGrid('setGridParam', {
			url: "../manageCreative/loadgriddata.action", page: 1,
			postData: {
				searchField: jQuery("#creativeSearchOption", "#manageCreativeContainer").val(),
				searchString: $.trim(jQuery("#creativeSearchValue", "#manageCreativeContainer").val()),
				searchOper: 'cn'
			}
		}).trigger("reloadGrid");
	});

	/**
	 * Register change event on creative search option select box
	 */
	jQuery("#creativeSearchOption", "#manageCreativeContainer").bind("change", function () {
		jQuery("#creativeSearchValue", "#manageCreativeContainer").val('').focus();
	});
	
	jQuery("#description", "#manageCreativeContainer").limiter(500, $("#charsRemaining", "#manageCreativeContainer"));
});

function editCreativeAttributeAssoc(rowid){
	showAttributes(jQuery("#creativeId").val(), "creative", rowid, "edit");
}

function deleteCreativeAttributeAssoc(rowid){
	var creativeid = jQuery("#creativeId").val();
	var attributeId = rowid;
	jQuery("#creativeAttributeTable").jqGrid('setGridParam', {page: 1});
	jQuery('#creativeAttributeTable').delGridRow(rowid, {
		url: '../manageCreative/deleteCreativeAttribute.action?creativeId=' + creativeid + '&attributeId=' + attributeId
	});
}

function showAttributes(id, type, attributeId, oper){
	var url = "";
	var ajaxReq = new AjaxRequest();
	ajaxReq.dataType = 'html';
	ajaxReq.url = "../manageAttribute/getCreativeAttributes.action?id=" + id + "&attributeType=" + type + "&attributeId=" + attributeId + "&operation=" + oper;

	ajaxReq.success = function(result, status, xhr){
		jQuery("#attribute-form").html(result);
		var creativeAttributeDialog = new ModalDialog();
		creativeAttributeDialog.height = 330;
		creativeAttributeDialog.width = 700;
		creativeAttributeDialog.resizable = false;
		creativeAttributeDialog.title = "Attribute Details";
		creativeAttributeDialog.buttons.Save = function(){
			var url = "../manageAttribute/saveCreativeAttributeAssoc.action";
			jQuery("#attribute-form #" + formObj).attr("action", url);
			var attributeFormManager = new FormManager();
			attributeFormManager.formName = formObj;//formObj variable comming from attribute.jsp
			attributeFormManager.gridName = "creativeAttributeTable";
			var attributeAssocForm = new JQueryAjaxForm(attributeFormManager);

			attributeAssocForm.doCustomProcessingAfterFormSucsesJson = function(jsonResponse, XMLHTTPRequest){
			var returnedId = jsonResponse.objectMap.gridKeyColumnValue;
				if (oper == "edit") {
					jQuery("#creativeAttributeTable").jqGrid('FormToGrid', returnedId, "#" + formObj);
				}
				else {
					reloadJqGridAfterAddRecord(attributeFormManager.gridName, creativeAttributeSortName);
				}
				jQuery(".ui-dialog-buttonset button:contains('Save')").button("enable");
				jQuery("#attribute-form").dialog("close");
			};
			attributeAssocForm.doCustomProcessingAfterValidationFailedJson = function(jsonResponse, XMLHTTPRequest){
				jQuery(".ui-dialog-buttonset button:contains('Save')").button("enable");
			};
			attributeAssocForm.submit();
			jQuery(".ui-dialog-buttonset button:contains('Save')").button("disable");
		};

		creativeAttributeDialog.buttons.Close = function(){
			clearCSSErrors('#attributeAssocContainer');
			jQuery(".ui-dialog-buttonset button:contains('Save')").button("enable");
			jQuery(this).dialog("close");
		};

		jQuery("#attribute-form").dialog(creativeAttributeDialog);
		jQuery("#attribute-form").dialog("open");
		if (oper == "edit") {
			var gsr = jQuery("#creativeAttributeTable").jqGrid('getGridParam', 'selrow');
			jQuery("#creativeAttributeTable").jqGrid('GridToForm', gsr, "#" + formObj);
		}
	};
	ajaxReq.submit();
}