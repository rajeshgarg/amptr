/**
 * JavaScript for manage product screen
 * 
 * @version 2.0
 * @author amandeep.singh
 */
$(document).ready(function() {
	
	jQuery(".numeric", "#manageProductContainer").numeric({
		decimal: false,
		negative: false
	});
	
	jQuery(".numericdecimal", "#manageProductContainer").numeric({
		negative: false
	});
	
	/**
	 * Product attachment grid and attachment dialog setup
	 */
	initDocUploadHTML('productAttachement', 'PRODUCT');
	$("#salestarget").attr("disabled",true);
	var productAttachementGridOpts = initAttachementGridOpts('productAttachement');
	
	$("#productAttachementTable").jqGrid(productAttachementGridOpts).navGrid('#productAttachementPager', {
		edit: false, search: false, addfunc: function(rowid){
			jQuery("#productAttachementForm #id").val('-1');
			clearCSSErrors("#productAttachementForm");
			jQuery("#productAttachementForm #fileName").val("");
			jQuery("#productAttachementDialog").dialog("open");
		}
	});

	var productAttachementFormManager = new FormManager();
	productAttachementFormManager.gridName = 'productAttachementTable';
	productAttachementFormManager.formName = 'productAttachementForm';

	var productAttachement = new ModalDialog();	

	/**
	 * Register save button functionality on product attachment dialog
	 */
	productAttachement.buttons.Save = function(){
		$("#productAttachementForm  #componentId").val($("#productId").val());
		if (jQuery("#productAttachementForm #id").val() != -1) {
			var id = $("#productAttachementTable").jqGrid('getGridParam', 'selrow');
			var ret = $("#productAttachementTable").jqGrid('getRowData', id);
			$("#productAttachementForm #fileName").val(ret.fileName);
		}

		var productAttachementForm = new JQueryAjaxForm(productAttachementFormManager);
		productAttachementForm.dataType = 'xml';
		productAttachementForm.doCustomProcessingAfterValidationFailedXML = function(){
			$("#productAttachementMessage").text("");
			$(".ui-dialog-buttonset button:contains('Save')").button("enable");
		};
		productAttachementForm.doCustomProcessingAfterFormSucsessXML = function(){
			$(productAttachementFormManager.formIdentifier()).resetForm();
			$("#productAttachementMessage").text(resourceBundle['message.generic.documentUploadSuccess']).css("color", "green");
			$(".ui-dialog-buttonset button:contains('Save')").button("enable");
		};
		productAttachementForm.doCustomProcessingAfterFormError = function(){
			$("#productAttachementMessage").text("");
			$(".ui-dialog-buttonset button:contains('Save')").button("enable");
		};

		productAttachementForm.submit();
		$(".ui-dialog-buttonset button:contains('Save')").button("disable");
	};

	/**
	 * Register close button functionality on product attachment dialog
	 */
	productAttachement.buttons.Close = function(){
		$( this ).dialog( "close" );
	};
	
	productAttachement.close = function(){
		$("#productAttachementForm").resetForm();
		$("#productAttachementMessage").text("");
		$("#productAttachementTable").trigger("reloadGrid");
	};
	$( "#productAttachementDialog").dialog(productAttachement);
	enableDialogButton("productAttachementDialog");
	/**
	 * Initialised Product Attribute JQGrid
	 */
	var productAttGridOpts = new JqGridBaseOptions();
	productAttGridOpts.colNames = ['AttributeId', 'Attribute Name', 'Attribute Description', 'Attribute Value', 'Assoc Id', 'Action'];
	productAttGridOpts.colModel = [
		{name:'attributeId', index:'attributeId', width:100, key:true, hidden:true},
		{name:'attributeName', index:'attributeName', width:200, sortable:true},
		{name:'attributeDescription', index:'attributeDescription', width:200, sortable:true},
		{name:'attributeValue', index:'attributeValue', width:100, sortable:true}	,
		{name:'assocId', index:'assocId', width:100, sortable:true, hidden:true} ,
		{name:'attrProdAssocAct', index:'attrProdAssocAct', width:100, fixed:true, sortable:false, resize:false}
	];
	productAttGridOpts.pager = $('#productAttributePager');	
	productAttGridOpts.sortname = 'assocId';	
	productAttGridOpts.editurl = '../manageProduct/updateattributesgriddata.action';
	productAttGridOpts.afterGridCompleteFunction = function() {
		var ids = $('#productAttributeTable').jqGrid('getDataIDs');
		for(var i = 0;i < ids.length; i++){
			var cl = ids[i];
			be = "<div style='padding-left:25%'>" +
					"<div style='float:left;' title='Edit'>" +
						"<a onclick='editProductAttributeAssoc("+ cl +")'>" +
							"<span class='ui-icon ui-icon-pencil' style='cursor:pointer;'/>" +
						"</a>" +
					"</div>" +
					"<div style='float:left;margin-left:10%' title='Delete'>" +
						"<a onclick='deleteProductAttributeAssoc("+ cl +")'>" +
							"<span class='ui-icon ui-icon-trash' style='cursor:pointer;'/>" +
						"</a>" +
					"</div>" +
				"</div>"; 
			jQuery('#productAttributeTable').jqGrid('setRowData', cl, {attrProdAssocAct:be});
		}
	};

	$("#productAttributeTable").jqGrid(productAttGridOpts).navGrid('#productAttributePager', {
		search: false, edit: false, del: false, addfunc: function(rowid){
			var salesTargetId = getSalesTargetId();
			if (salesTargetId == '') {
				return;
			}
			showProductAttributes($("#productId").val(), "product", "", "add", salesTargetId);
		},
		afterRefresh: function(){
			clearCSSErrors('#productAttributeContainer');
		}
	});

	
	/**
	 * Initialised Product JQGrid
	 */
	var productGridOpts = new JqGridBaseOptions();
	productGridOpts.url = "../manageProduct/loadgriddata.action";
	productGridOpts.colNames = ['Product ID', 'Name', 'Display Name', 'Type', 'Class', 'Note', 'Description', 'Creative Names', 'Has Documents'];
	productGridOpts.colModel = [
		{name:'productId', index:'productId', key:true, hidden:true}, 
		{name:'productName', index:'productName', sortable:true, key:false, width:250}, 
		{name:'displayName', index:'displayName', sortable:true, key:false, width:250}, 
		{name:'typeName', index:'typeName', sortable:true, key:false, width:120}, 
		{name:'className', index:'className', sortable:true, key:false, width:120}, 
		{name:'note', index:'note', sortable:true, key:false, width:200}, 
		{name:'productDescription', index:'productDescription', hidden:true, key:false, width:200}, 
		{name:'creativeName', index:'creativeName', sortable:false, key:false, width:200},
		{name:'hasDocument', index:'hasDocument', sortable:false, key:false, formatter:attachedDocumentFormatter, width:120}
	];
	productGridOpts.afterGridCompleteFunction = function(){
		var topRowId = $("#productTable").getDataIDs()[0];
		if (topRowId == '' || topRowId == undefined) {
			$('#productTab', '#manageProductContainer').hide();
		}
	};

	productGridOpts.onSelectRow = function(id){
		clearCSSErrors('#productAttributeContainer');
		$('#productTab', '#manageProductContainer').show();
		clearCSSErrors('#productDetail');
		var ajaxReq = new AjaxRequest();
		ajaxReq.url = "../manageProduct/getSalesTarget.action?productId=" + id;
		ajaxReq.success = function(result, status, xhr){
			var select = $('#salestarget', '#manageProductContainer');
			var lineItemSelect = $('#lineItemSalesTarget', '#productAttributeContainer');

			$('option', select).remove();
			$('option', lineItemSelect).remove();

			$(lineItemSelect).append("<option value='-1'>Any</option>");
			$(lineItemSelect).append("<option disabled>---------------------------------</option>");
			$.each(result, function(i, item){
				$(select).append('<option value="' + item.salesTargetId + '" >' + item.salesTargeDisplayName + '</option>');
				$(lineItemSelect).append('<option value="' + item.salesTargetId + '" >' + item.salesTargeDisplayName + '</option>');
			});

			var gsr = jQuery("#productTable").jqGrid('getGridParam', 'selrow');
			if (gsr) {
				jQuery("#productTable").jqGrid('GridToForm', gsr, "#productDetail");
				$('input[name=note]').attr("title",$('input[name=note]').val());
			}
			else {
				alert("Please select Row");
			}
			$('#productAttributeTable').setGridParam({
				url: '../manageProduct/attributesgriddata.action?productId=' + id + '&salestargetId=-1'
			});
			$('#productAttributeTable').trigger("reloadGrid");

			$('#productAttachementTable').setGridParam({
				url: '../document/documentgriddata.action?documentFor=PRODUCT&componentId=' + id,
				editurl: '../document/deletedocument.action?documentFor=PRODUCT&componentId=' + id
			});
			$('#productAttachementTable').trigger("reloadGrid");
			getProductPlacement();
		};
		ajaxReq.submit();
		getProductDetails(id);
	};
	
	productGridOpts.pager = $('#productPager');
	productGridOpts.sortname = "productId";
	productGridOpts.caption = resourceBundle['label.generic.manageProduct'];
	$("#productTable").jqGrid(productGridOpts).navGrid('#productPager', {
		edit: false, add: false, del: false, search: false,
		beforeRefresh: function(){
			jQuery("#productSearchOption", "#manageProductContainer").val('productName');
			jQuery("#productSearchValue", "#manageProductContainer").val('');
			reloadJqGridAfterAddRecord('productTable', "productId");
		}
	});

	/**
	 * Initialised product screen tabs
	 */
    $("#productTab", "#manageProductContainer").tabs();

	/**
	 * Initialised search panel on product grid
	 */
	initGridSearchOptions("productTable", "productSearchPanel", "manageProductContainer");

	/**
	 * Enable auto search on product grid
	 */
	enableAutoSearch(jQuery("#productSearchValue", "#manageProductContainer"), function() {
		jQuery("#productTable").jqGrid('setGridParam', {
			url: "../manageProduct/loadgriddata.action", page: 1,
			postData: {
				searchField: jQuery("#productSearchOption", "#manageProductContainer").val(),
				searchString: $.trim(jQuery("#productSearchValue", "#manageProductContainer").val()),
				searchOper: 'cn'
			}
		}).trigger("reloadGrid");
	});
	
	/**
	 * Register change event on attribute search option select box
	 */
	jQuery("#productSearchOption", "#manageProductContainer").bind("change", function () {
		jQuery("#productSearchValue", "#manageProductContainer").val('').focus();
	});
	
	/**
	 * Initialised product attribute assoc dialog
	 */
	var productAttributeFormManager = new FormManager();	
	productAttributeFormManager.formName = 'ManageProductAttribute';

	var productAttributeDialog = new ModalDialog();	
	productAttributeDialog.height = 250;
	productAttributeDialog.width = 500;
	productAttributeDialog.buttons.Save = function(){
		var produrl = "../manageAttribute/saveAttribute.action?attributeType=PRODUCT";
		$("#ManageProductAttribute").attr("action", produrl);
		var productAttributeForm = new JQueryAjaxForm(productAttributeFormManager);
		productAttributeForm.doCustomProcessingAfterFormSucsesJson = function(jsonResponse, XMLHTTPRequest){
			$("#product-attribute-form").dialog("close");
			jQuery("#productAttributeTable").delRowData("0");
			addRowProdAttributesGrid(jsonResponse.objectMap.gridKeyColumnValue);
			document.ManageProductAttribute.reset();
			clearCSSErrors('#ManageProductAttribute');
			clearCSSErrors('#productAttributeContainer');
		};
		productAttributeForm.submit();
	};
	productAttributeDialog.buttons.Close = function(){
		clearCSSErrors('#ManageProductAttribute');
		$("#attributeName").val("");
		$("#attributeDescription").val("");
		$("#attributeOptionalValue").val("");
		$(this).dialog("close");
	};
	$( "#product-attribute-form").dialog(productAttributeDialog);
	enableDialogButton("product-attribute-form");
	/**
	 * Initialised product creative assoc dialog
	 */
	var addEditProductCreativeDiv = new ModalDialog();
	addEditProductCreativeDiv.width = 700;
	addEditProductCreativeDiv.height = 260;
	addEditProductCreativeDiv.resizable = false;
	
	addEditProductCreativeDiv.buttons.Save = function(){
		var productCreativeFormManager = new FormManager();
		productCreativeFormManager.gridName = 'productCreativeTable';
		productCreativeFormManager.formName = 'addProductCreativeForm';
		
		var productCreativeForm = new JQueryAjaxForm(productCreativeFormManager);
		productCreativeForm.doCustomProcessingAfterFormSucsesJson = function(jsonResponse, XMLHTTPRequest){
			reloadJqGridAfterAddRecord(productCreativeFormManager.gridName, 'associationId');
			$(".ui-dialog-buttonset button:contains('Save')").button("enable");
			$("#addEditProductCreativeDiv").dialog( "close" );
		};
		productCreativeForm.doCustomProcessingAfterValidationFailedJson = function(jsonResponse, XMLHTTPRequest){
			$(".ui-dialog-buttonset button:contains('Save')").button("enable");
		};	
		productCreativeForm.submit();
		$(".ui-dialog-buttonset button:contains('Save')").button("disable");
	};

	addEditProductCreativeDiv.buttons.Close = function(){
		$(".ui-dialog-buttonset button:contains('Save')").button("enable");
		$(this).dialog("close");
	};
	$("#addEditProductCreativeDiv").dialog(addEditProductCreativeDiv);	
	enableDialogButton("addEditProductCreativeDiv");
	 /**
	  * Creating multiselect plugin for multiple creatives
	  */
	$("#creatives","#containerProductDetail").multiselect({
		selectedList: 1,
		position: {
		      my: 'bottom',
		      at: 'top'
		   }
	}).multiselectfilter();

	var instanceFormManager = new FormManager();
	instanceFormManager.gridName = 'productTable';
	instanceFormManager.formName = 'productDetail';

	/**
	 * Register click event on Save Button
	 */
	jQuery('#productSaveData', '#productDetail').click(function() {
		$('#creativesLength').val($("#creatives").find("option").length);
		var creativeNames = $("#creatives",'#productDetail').multiselect("getChecked").map(function() {
			return $(this).next().html();
		}).get().join(', ');
		var productDetailForm = new JQueryAjaxForm(instanceFormManager);
		productDetailForm.doCustomProcessingAfterFormSucsesJson = function(jsonResponse, XMLHTTPRequest){
            if (creativeNames == "") {
                jQuery("#productTable").setCell($('#productId', "#containerProductDetail").val(), "creativeName");
            }
            else {
                jQuery("#productTable").setCell($('#productId', "#containerProductDetail").val(), "creativeName", creativeNames);
            }
			jQuery("#runtimeDialogDiv").model({theme: 'Success', message: resourceBundle['message.generic.productSavedSuccessfully']});
		};
		productDetailForm.submit();
	});

	/**
	 * Register click event on Reset Button
	 */
	jQuery('#productResetData', '#productDetail').click(function (){
		clearCSSErrors('#productDetail');
		getProductDetails($("#productTable").jqGrid('getGridParam', 'selrow'));
	});	
});

function getProductDetails (productId) {
	var ajaxRequst = new AjaxRequest();
	ajaxRequst.url = "../manageProduct/getProductDetails.action?productId=" + productId;
	ajaxRequst.success = function(result, status, xhr){
		$("#creatives",'#containerProductDetail').multiselect("uncheckAll");
		var creativeIds = new Array;
		if(result.creativeIds != ''){
			$("#creatives",'#containerProductDetail').val(result.creativeIds.split(","));
		}		
		$("#creatives","#containerProductDetail").multiselect("refresh");
	};
	ajaxRequst.submit();
}

/**
 * 
 * @param {Object} obj
 */
function getProductPlacement(obj){
	var productId = jQuery("#productId").val();
	var salestargetId = jQuery("#salestarget").val();
	if (salestargetId == "" || salestargetId == null) {
		$("#placement").val("");
	}
	else {
		var ajaxReq = new AjaxRequest();
		ajaxReq.url = "../manageProduct/getProductPlacement.action?productId=" + productId + "&salestargetId=" + salestargetId;
		ajaxReq.success = function(result, status, xhr){
			$.each(result, function(i, item){
				$("#placementName", "#lineItemdetailForm").val(item);
			});
		};
		ajaxReq.submit();
	}
}

/**
 * 
 */
function loadLineItemAttribute(){
	var salesTargetId = $('#lineItemSalesTarget', '#manageProductContainer').val();
	var productId = $('#productId', '#manageProductContainer').val();
	if (productId != '0') {
		$('#productAttributeTable').setGridParam({
			url: '../manageProduct/attributesgriddata.action?productId=' + productId + '&salestargetId=' + (salesTargetId == '' ? 0 : salesTargetId)
		});
		$('#productAttributeTable').trigger("reloadGrid");
	}
}

/**
 * 
 */
function getSalesTargetId(){
	var salesTargetId = $('#lineItemSalesTarget', '#manageProductContainer').val();
	if (salesTargetId == '') {
		jQuery("#runtimeDialogDiv").model({theme: 'Error', autofade: false, message: 'Please select sales target.'});
		return '';
	}
	else {
		return salesTargetId;
	}
}

/**
 * 
 * @param {Object} rowid
 */
function editProductAttributeAssoc(rowid){
	var salesTargetId = getSalesTargetId();
	if (salesTargetId == '') {
		return;
	}
	showProductAttributes($("#productId").val(), "product", rowid, "edit", salesTargetId);
}

/**
 * 
 * @param {Object} rowid
 */
function deleteProductAttributeAssoc(rowid){
	var productId = $("#productId").val();
	jQuery("#productAttributeTable").jqGrid('setGridParam', {page: 1});
	var salesTargetId = getSalesTargetId();
	if (salesTargetId == '') {
		return;
	}
	var attributeId = rowid;
	$('#productAttributeTable').delGridRow(rowid, {
		url: '../manageProduct/deleteproducattribute.action?productId=' + productId + '&attributeId=' + rowid + '&salestargetId=' + salesTargetId
	});
	clearCSSErrors('#productAttributeContainer');
}

/**
 * 
 * @param {Object} id
 * @param {Object} type
 * @param {Object} attributeId
 * @param {Object} oper
 * @param {Object} salesTargetId
 */
function showProductAttributes(id, type, attributeId, oper, salesTargetId){
	var ajaxReq = new AjaxRequest();
	ajaxReq.dataType = 'html';
	ajaxReq.url = "../manageAttribute/getProductAttributes.action?id=" + id + "&attributeType=" + type + "&attributeId=" + attributeId + "&operation=" + oper + "&salestargetId=" + salesTargetId;
	ajaxReq.success = function(result, status, xhr){
		$("#product-attribute-form").html(result);
		var productAttributeDialog = new ModalDialog();
		productAttributeDialog.height = 330;
		productAttributeDialog.width = 700;
		productAttributeDialog.resizable = false;
		productAttributeDialog.buttons.Save = function(){
			var url = "../manageAttribute/saveProductAttributeAssoc.action";
			$("#product-attribute-form #" + formObj).attr("action", url);
			var productAttributeFormManager = new FormManager();
			productAttributeFormManager.formName = formObj;
			productAttributeFormManager.gridName = "productAttributeTable";
			var productAttributeAssocForm = new JQueryAjaxForm(productAttributeFormManager);

			productAttributeAssocForm.doCustomProcessingAfterFormSucsesJson = function(jsonResponse, XMLHTTPRequest){
				var returnedId = jsonResponse.objectMap.gridKeyColumnValue;
				if (oper == "edit") {
					$("#productAttributeTable").jqGrid('FormToGrid', returnedId, "#" + formObj);
				}
				else {
					reloadJqGridAfterAddRecord(productAttributeFormManager.gridName, 'assocId');
				}
				$(".ui-dialog-buttonset button:contains('Save')").button("enable");
				$("#product-attribute-form").dialog("close");

			};
			productAttributeAssocForm.doCustomProcessingAfterValidationFailedJson = function(jsonResponse, XMLHTTPRequest){
				$(".ui-dialog-buttonset button:contains('Save')").button("enable");
			};
			productAttributeAssocForm.submit();
			$(".ui-dialog-buttonset button:contains('Save')").button("disable");
		};

		productAttributeDialog.buttons.Close = function(){
			$(".ui-dialog-buttonset button:contains('Save')").button("enable");
			clearCSSErrors('#attributeAssocContainer');
			$(this).dialog("close");
		};

		$("#product-attribute-form").dialog(productAttributeDialog);
		$("#product-attribute-form").dialog("open");
		enableDialogButton("product-attribute-form");
		if (oper == "edit") {
			var gsr = $("#productAttributeTable").jqGrid('getGridParam', 'selrow');
			$("#productAttributeTable").jqGrid('GridToForm', gsr, "#" + formObj);
		}
	};
	ajaxReq.submit();
}

/**
 * Reloads the creatives
 */
function reloadCreatives() {
	var selectedValues=$("#creatives", "#containerProductDetail").val();	
	var request = new AjaxRequest();
	request.url = "../manageProduct/creativenames.action";
	request.success = function(result, status, xhr) {
		clearCSSErrors( "#productDetail" );
		$("#creatives", "#containerProductDetail").html("");
		if (result != null) {
		    sortList(result, $("#creatives", "#containerProductDetail"));		    
			$("#creatives", "#containerProductDetail").val(selectedValues);
			$("#creatives", "#containerProductDetail").multiselect("refresh");
		}
	};     
	request.submit();
}