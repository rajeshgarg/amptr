$(document).ready(function() {
	
	$("input[class=numericdecimal]", "#planBudgetDetail").numeric({
		negative: false
	});
	
	/**
	 * Apply tab plug-in in Plan Budget detail section
	 */
	jQuery("#planBudgetDetailForm", "#addedValueRuleContainer").tabs();
	
	/**
	 * Creating the master Plan Budget Grid
	 */

	var listPlanBudgetOpts = new JqGridBaseOptions();
	listPlanBudgetOpts.url = "../manageAddedValue/loadgriddata.action";
	listPlanBudgetOpts.colNames = ['Plan Budget Id','Total Investment of Plan (>=)', 'Added Value Percentage Allowed', 'Notes'];
	listPlanBudgetOpts.colModel = [
		{name:'planBudgetId', index:'planBudgetId', hidden:true, key:true }, 
		{name:'totalInvestment', index:'totalInvestment', align:"right"}, 
		{name:'avPercentage', index:'avPercentage' , align:"right"}, 
		{name:'avNotes', index:'avNotes', width:300, sortable:false}, 
	];

	listPlanBudgetOpts.caption = resourceBundle['tab.generic.PlanBudget'];
	listPlanBudgetOpts.pager = jQuery('#planBudgetPager');
	listPlanBudgetOpts.sortname = "planBudgetId";
	listPlanBudgetOpts.afterGridCompleteFunction = function (){
		var topRowId = jQuery("#planBudgetTable").getDataIDs()[0];
		if(topRowId == '' || topRowId == undefined){
			jQuery('#planBudgetDetailForm', '#addedValueRuleContainer').hide();
			document.planBudgetDetail.reset();
		}
	};

	listPlanBudgetOpts.onSelectRow = function(id){
		clearCSSErrors('#planBudgetDetailForm');	
		if(id){
			jQuery("#planBudgetTable").jqGrid('GridToForm', id, "#planBudgetDetail");
			jQuery("textarea", "#addedValueRuleContainer").change();
			jQuery('#planBudgetDetailForm', '#addedValueRuleContainer').show();
			jQuery("#PlanBudgetDetailFormTab-1 a").text(resourceBundle["tab.generic.planbudget.details"]);
			jQuery('#PlanBudgetDetailFormTab-2', '#planBudgetDetailForm').show();
		}
	};
	
	/**
	 * Register click event on delete, add and refresh Button in jqGrid
	 */

	jQuery("#planBudgetTable").jqGrid(listPlanBudgetOpts).navGrid('#planBudgetPager', {edit: false,search:false,
		addfunc: function(rowid){
			resetRowSelectionOnAdding("#planBudgetTable", "#planBudgetId");
			clearCSSErrors('#planBudgetDetailForm');
			document.planBudgetDetail.reset();
			jQuery('#planBudgetId', '#planBudgetDetailForm').val(0);
			jQuery('#planBudgetDetailForm', '#addedValueRuleContainer').show();
			jQuery("#PlanBudgetDetailFormTab-1 a").text(resourceBundle["tab.generic.addNewPlanBudget"]);
			jQuery('#PlanBudgetDetailFormTab-1 a').click();
			jQuery("textarea", "#addedValueRuleContainer").change();
		},
		delfunc: function(rowid){
			var planBudgetId = jQuery("#planBudgetId").val();
			  jQuery("#planBudgetTable").jqGrid('setGridParam', {page: 1});
			  jQuery('#planBudgetTable').delGridRow(planBudgetId, {
				url: '../manageAddedValue/deleteAddedValuePlanBudget.action?planBudgetId=' + planBudgetId
			  });
		},
		beforeRefresh: function(){
			jQuery("#addedValueSearchOption", "#addedValueRuleContainer").val('totalInvestment');
			jQuery("#addedValueSearchValue", "#addedValueRuleContainer").val('');
			reloadJqGridAfterAddRecord('planBudgetTable', "planBudgetId");
		}
	});

	var instanceFormManager = new FormManager();
	instanceFormManager.gridName = 'planBudgetTable';
	instanceFormManager.formName = 'planBudgetDetail';
	
	/**
	 * Register click event on Save Button
	 */
	jQuery('#planBudgetSaveData', '#planBudgetDetailForm').click(function() {
		var planBudgetDetailForm = new JQueryAjaxForm(instanceFormManager);
		planBudgetDetailForm.doCustomProcessingAfterFormSucsesJson = function(jsonResponse, XMLHTTPRequest){
			jQuery("#runtimeDialogDiv").model({theme: 'Success', message: resourceBundle['message.generic.planBudgetSavedSuccessfully']});
			var planBudgetId = jQuery("#planBudgetId").val();
			if (planBudgetId == 0) {
				reloadJqGridAfterAddRecord(instanceFormManager.gridName, "planBudgetId");
			}
			else {			
				jQuery("#planBudgetTable").jqGrid('FormToGrid', planBudgetId, "#planBudgetDetail");
			}
		};
		planBudgetDetailForm.submit();
	});
		
	/**
	 * Register click event on Reset Button
	 */
	jQuery('#planBudgetResetData', '#planBudgetDetail').click(function (){
		clearCSSErrors('#planBudgetDetail');
		if(jQuery("#PlanBudgetDetailFormTab-1 a").text() == resourceBundle['tab.generic.addNewPlanBudget']){
			document.planBudgetDetail.reset();
			jQuery('#planBudgetId', '#planBudgetDetailForm').val(0);	
			jQuery('input[type!=hidden]:first', '#planBudgetDetail').focus();
		} else {
			var planBudgetId = jQuery("#planBudgetTable").jqGrid('getGridParam','selrow');
			if(planBudgetId){
				jQuery("#planBudgetTable").jqGrid('GridToForm', planBudgetId, "#planBudgetDetail");
			} else {
				alert("Please select Row");
			}	
		}
		jQuery("textarea", "#addedValueRuleContainer").change();
	});
	
	initGridSearchOptions("planBudgetTable", "addedValueSearchPanel", "addedValueRuleContainer");
	
	/**
	 * Enable auto search on added value grid
	 */
	enableAutoSearch(jQuery("#addedValueSearchValue", "#addedValueRuleContainer"), function() {
		jQuery("#planBudgetTable").jqGrid('setGridParam', {
			url: '../manageAddedValue/loadgriddata.action', page: 1,
			postData: {
				searchField: jQuery("#addedValueSearchOption", "#addedValueRuleContainer").val(),
				searchString: $.trim(jQuery("#addedValueSearchValue", "#addedValueRuleContainer").val()),
				searchOper: 'cn'
			}
		}).trigger("reloadGrid");
	});
	
	/**
	 * Register change event on attribute search option select box
	 */
	jQuery("#addedValueSearchOption", "#addedValueRuleContainer").bind("change", function () {
		jQuery("#addedValueSearchValue", "#addedValueRuleContainer").val('').focus();
	});
	
	jQuery("#avNotes", "#addedValueRuleContainer").limiter(500, $("#charsRemaining", "#addedValueRuleContainer"));
});
