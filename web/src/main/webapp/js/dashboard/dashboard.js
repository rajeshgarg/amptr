/**
 * JavaScript for dashboard graph and data
 * 
 * @author surendra.singh
 */
var filterData = '';

var userIdArray = new Array();

var cal_view_date_selected = '';

var autoRefreshIn = autoRefreshInterval = 600;

$(document).ready(function() {

	/**
	 * Set timer for auto refresh dashboard
	 */
	setAutoRefreshTimer();
	
	/**
	 * Code for close the filter name input div if click outside the div
	 */
	var mouse_is_inside = false;
	
	$("#dashboardFilterNameDiv", "#dashboardContainer").hover(function(){
		mouse_is_inside = true;
	}, function(){
		mouse_is_inside = false;
	});
	$(document).click(function(){
		if (!mouse_is_inside) {
			$("#dashboardFilterNameDiv", "#dashboardContainer").slideUp(function(){
				$("#dashboardFilterNameDiv", "#dashboardContainer").css("display", "none");
			});
		}
	});

	/**
	 * Convert select box to multi-select & select2 plugin component
	 */
	$("#salesCategory, #user, #priority", "#dashboardContainer").multiselect({ selectedList: 3 }).multiselectfilter();

	$("#dueDate, #savedFilter", "#dashboardContainer").select2();

	/**
	 * Bind change event on saved filter select box :- When user change selection to saved filter, 
	 * system will load saved data from database else will remove all filter criteria
	 */
	$("#savedFilter", "#dashboardContainer").change(function(){
		reloadFilterData();
	});
	
	/**
	 * Bind click event on delete filter icon :- Delete the filter from database 
	 * if valid filter name selected and reset selection to default 
	 */
	$("#deleteFilter", "#dashboardContainer").click(function () {
		var filterId = $("#savedFilter", "#dashboardContainer").val();
		if(filterId == '') {
			return;
		}
		var ajaxRequest = new AjaxRequest();
		ajaxRequest.url = "../dashboard/deleteFilter.action?filterId=" + filterId;
		ajaxRequest.success = function(result, status, xhr){
			$("#savedFilter option[value=" + filterId + "]", "#dashboardContainer").remove();
			$("#savedFilter", "#dashboardContainer").select2('val', '').trigger("change");;
		};
		ajaxRequest.submit();
	});

	/**
 	 * 
 	 */
	$("#mySalesCategory", "#dashboardContainer").click(function () {
		var ajaxRequest = new AjaxRequest();
		ajaxRequest.url = "../dashboard/getUserSaleesCategory.action";
		ajaxRequest.success = function(result, status, xhr){
			$("#salesCategory", "#dashboardContainer").val(result).multiselect("refresh");
		};
		ajaxRequest.submit();
	});

	$("#applyDashboardFilterBtn", "#dashboardContainer").click (function (ev) {
		var searchFilter = getFilterDataAsRequestParam();
		filterData = searchFilter != '' ? '?' + searchFilter : '';
		
		initDashboard();
	});
	
	/**
	 * Bind click event on save btn :- Display div to enter filter name 
	 * @param {Object} ev
	 */
	$("#saveDashboardFilterBtn", "#dashboardContainer").click (function (ev) {
		/**
		 * If user has already selected a saved filter then populate the filterId and name for update
		 * else blank out both values.
		 */
		if ($("#savedFilter", "#dashboardContainer").val() != '') {
			$("#filterId", "#dashboardContainer").val($("#savedFilter", "#dashboardContainer").val());
			$("#dashboardFilterName", "#dashboardContainer").val($("#savedFilter option:selected", "#dashboardContainer").text());
		} else {
			$("#filterId", "#dashboardContainer").val(0);
			$("#dashboardFilterName", "#dashboardContainer").val('');
		}
		/**
		 * reset previous validation errors.
		 */
		$("#saveErorr", "#dashboardContainer").text("");
		$("#dashboardFilterNameDiv", "#dashboardContainer").slideDown();
		$("#dashboardFilterName", "#dashboardContainer").removeClass("errorElement").focus();
		ev.stopPropagation();
	});
	
	/**
	 * Bind click event on reset btn :- Revert all filter criteria to its initial state.
	 * If User has selected any saved filter then populate filter data from database else remove all filter condition
	 * @param {Object} ev
	 */
	$("#resetDashboardFilterBtn", "#dashboardContainer").click (function (ev) {
		reloadFilterData();
	});
	
	/**
	 * Bind click event on save-ok btn :- This will save filter data to the database
	 */
	$("#saveFilterOk", "#dashboardContainer").click(function() {
		saveFilterData();
	});

	$(".views", "#dashboardContainer").click(function() {
		if($(this).hasClass("selected-view")){
			$.noop();
		} else{
			$(this).find("input[type=radio]").attr("checked", "checked");
			$(this).siblings().removeClass("selected-view");
			$(this).addClass("selected-view");
			$('.dashboard-legend').show();
			initDashboard();
		}
	});

	/**
	 * Capture enter key press on filter name input box and save the filter data
	 */
	enableAutoSearch($("#dashboardFilterName", "#dashboardContainer"), function() {
		saveFilterData();
	});

	var dashboardGridOpt = new JqGridBaseOptions();
	dashboardGridOpt.colNames =	['Proposal ID',  'Proposal Name', 'Proposal Owner', 'Advertiser', 'Sales Category', 'Requested On', 'Submitted On', 'Due Date', 'Budget ($)', 'Account Manager' ];
	dashboardGridOpt.colModel = [
		{name:'id', index:'id', hidden:true, key:true}, 
		{name:'proposalName', index:'proposalName', width:200}, 
		{name:'userName', index:'userName'}, 
		{name:'advertiserName', index:'advertiserName', sortable: false},
		{name:'salescategory', index:'salescategory', sortable: false},
		{name:'requestedOn', index:'requestedOn', width: 120, hidden:true},
		{name:'pricingSubmittedDate', index:'pricingSubmittedDate', width: 120, hidden:true},
		{name:'dueOn', index:'dueOn', width: 120},
		{name:'budget', index:'budget', width:100, align:"right"},
		{name:'accountManager', index:'accountManager'}
	];
	dashboardGridOpt.emptyrecords = resourceBundle['label.dashboard.emptymessage'];
	dashboardGridOpt.pager = $('#dashboardDataGridPager');
	dashboardGridOpt.sortname = "proposalName";
	dashboardGridOpt.autowidth = true;
	dashboardGridOpt.beforeRequest = function(){
		jQuery(this).setGridWidth(jQuery(".dashboard-data-grid", "#dashboardContainer").width(), true);
	};
	dashboardGridOpt.gridComplete = function(){
		jQuery(this).jqGrid('resetSelection');
	};
	/**
	 * Open proposal when user click on any row in JQGrid
	 * @param {Object} id
	 */
	dashboardGridOpt.onSelectRow = function(id){
		openProposal(id);
	};

	$("#dashboardDataGrid").jqGrid(dashboardGridOpt).navGrid('#dashboardDataGridPager', {
		edit: false, del: false, search: false, add: false
	});

	/**
	 * Initialised the dashboard graph and load data in JQGrid
	 */
	initDashboard();
});

/**
 * Save filter data to the data base after performing the data validation
 */
function saveFilterData() {
	if (!validateFilterName()) {
		return;
	}
	var form = new FormManager();
	form.formName = 'dashboardFilterForm';

	var dashboardFilterForm = new JQueryAjaxForm(form);
	dashboardFilterForm.doCustomProcessingAfterFormSucsesJson = function(jsonResponse, XMLHTTPRequest) {
		/**
		 * After successful save close the div having filter name input box.
		 */
		$("#dashboardFilterNameDiv", "#dashboardContainer").slideUp();
		jQuery("#runtimeDialogDiv").model({theme: 'Success', message: resourceBundle['message.generic.filterSavedSuccessfully']});

		/**
		 * Add or update filter on saved filter dropdown box on UI
		 */
		if ($("#filterId", "#dashboardContainer").val() == 0) {
			var filterName = $.trim($("#dashboardFilterName", "#dashboardContainer").val());
			$("#savedFilter", "#dashboardContainer")
					.append("<option value='" + jsonResponse.objectMap.filterId + "'>" + filterName + "</option>");
			$("#savedFilter", "#dashboardContainer").select2("val", jsonResponse.objectMap.filterId).trigger("change");
		} else {
			var filterId = $("#filterId", "#dashboardContainer").val();
			var filterName = $.trim($("#dashboardFilterName", "#dashboardContainer").val());
			$("#savedFilter option[value=" + filterId + "]", "#dashboardContainer").text(filterName);
			$("#savedFilter", "#dashboardContainer").select2("val", jsonResponse.objectMap.filterId).trigger("change");
		}		
	};
	dashboardFilterForm.submit();
}

/**
 * Validate the filter form data for :- valid name, duplicate filter name and empty filter criteria
 */
function validateFilterName() {
	var filterName = $.trim($("#dashboardFilterName", "#dashboardContainer").val());
	if (filterName == '') {
		$("#dashboardFilterName", "#dashboardContainer").addClass("errorElement").focus();
		$("#saveErorr", "#dashboardContainer").text("Filter name is mandatory");
		return false;
	} else if (duplicateFilterName(filterName)) {
		$("#dashboardFilterName", "#dashboardContainer").addClass("errorElement").focus();
		$("#saveErorr", "#dashboardContainer").text("Duplicate filter name");
		return false;
	} else if (emptyFilterCriteria()) {
		$("#dashboardFilterName", "#dashboardContainer").addClass("errorElement").focus();
		$("#saveErorr", "#dashboardContainer").text("Filter criteria is empty");
		return false;
	}
	return true;
}

/**
 * Validate duplicate filter name while saving filter data
 * @param {Object} filterName
 */
function duplicateFilterName(filterName) {
	var flag = false;
	var filterId = $("#filterId", "#dashboardContainer").val();
	$("#savedFilter option", "#dashboardContainer").each(function(i, option){
		if ($(option).val() != filterId && filterName == $(option).text()) {
			flag = true;
		}
	});
	return flag;	
}

/**
 * Validate filter data for null filtere criteria
 */
function emptyFilterCriteria() {
	if (getFilterDataAsRequestParam() == '') {
		return true;
	}
	return false;
}

/**
 * Return filter data as parameter string
 */
function getFilterDataAsRequestParam () {
	var filterData = '';
	var salesCategorieIds = $("#salesCategory", "#dashboardContainer").multiselect("getChecked").map(function(){
		return this.value;
	}).get().join(',');
	if (salesCategorieIds != '') {
		filterData += filterData == '' ? "salesCategory=" + salesCategorieIds : "&salesCategory=" + salesCategorieIds;
	}

	var userIds = $("#user", "#dashboardContainer").multiselect("getChecked").map(function(){
		return this.value;
	}).get().join(',');
	if (userIds != '') {
		filterData += filterData == '' ? "user=" + userIds : "&user=" + userIds;
	}

	var priority = $("#priority", "#dashboardContainer").multiselect("getChecked").map(function(){
		return this.value;
	}).get().join(',');
	if (priority != '') {
		filterData += filterData == '' ? "priority=" + priority : "&priority=" + priority;
	}

	var dueDate = $("#dueDate", "#dashboardContainer").select2('val');
	if (dueDate != '') {
		filterData += filterData == '' ? "dueDate=" + dueDate : "&dueDate=" + dueDate;
	}
	return filterData;
}

/**
 * System will load saved data from database if valid filter is selected else will remove all filter criteria
 */
function reloadFilterData() {
	var filterId = $("#savedFilter", "#dashboardContainer").val();
	if ($("#savedFilter", "#dashboardContainer").val() == '') {
		$("#dueDate", "#dashboardContainer").select2('val', '');
		$("#salesCategory, #user, #priority", "#dashboardContainer").val('').multiselect("refresh");
		$("#deleteFilter", "#dashboardContainer").removeClass("filter-delete").addClass("delete-filter-inactive");
		$("#applyDashboardFilterBtn", "#dashboardContainer").click();
	}
	else {
		var ajaxRequest = new AjaxRequest();
		ajaxRequest.url = "../dashboard/getFilterData.action?filterId=" + filterId;
		ajaxRequest.success = function(result, status, xhr){
			var form = result.objectMap.filter;
			$("#salesCategory", "#dashboardContainer").val(form.salesCategory).multiselect("refresh");
			$("#user", "#dashboardContainer").val(form.user).multiselect("refresh");
			$("#priority", "#dashboardContainer").val(form.priority).multiselect("refresh");
			$("#dueDate", "#dashboardContainer").select2('val', form.dueDate);
			$("#applyDashboardFilterBtn", "#dashboardContainer").click();
		};
		ajaxRequest.submit();
		$("#deleteFilter", "#dashboardContainer").addClass("filter-delete").removeClass("delete-filter-inactive");
	}
}

/**
 * Function initialised the dashboard graph based on the view selected and load JQGrid data
 */
function initDashboard() {
	if (jQuery("#plannerView").is(":checked")) {
		var ajaxRequest = new AjaxRequest();
		ajaxRequest.url = "../dashboard/getGraphData.action" + filterData;
		ajaxRequest.success = function(result, status, xhr){
			renderPlannerViewGraph(result);
			autoRefreshIn = autoRefreshInterval;
			if($("#user_name", "#header").text().indexOf("Pricing Admin") >= 0) {
				$("#dashboardDataGrid").showCol("pricingSubmittedDate");
				$("#dashboardDataGrid").hideCol("requestedOn");
			} else {
				$("#dashboardDataGrid").hideCol("pricingSubmittedDate");
				$("#dashboardDataGrid").showCol("requestedOn");
			}
			jQuery("#dashboardDataGrid").jqGrid('setGridParam',{url: "../dashboard/loadGridData.action" + filterData, page:1}).trigger("reloadGrid");
		}
		ajaxRequest.error = function(result, status, xhr) {
			autoRefreshIn = autoRefreshInterval;
			jQuery("#dashboardChart").html("<div class='error-message'>" + resourceBundle["error.dashboard.nodata"] + "</div>");
		}
		ajaxRequest.submit();
	} else if (jQuery("#statusView").is(":checked")) {
		var ajaxRequest = new AjaxRequest();
		ajaxRequest.url = "../dashboard/getGraphData.action" + filterData;
		ajaxRequest.success = function(result, status, xhr){
			renderStatusViewGraph(result);
			autoRefreshIn = autoRefreshInterval;
			jQuery("#dashboardDataGrid").jqGrid('setGridParam',{url: "../dashboard/loadGridData.action" + filterData, page:1}).trigger("reloadGrid");
		}
		ajaxRequest.error = function(result, status, xhr) {
			autoRefreshIn = autoRefreshInterval;
			jQuery("#dashboardChart").html("<div class='error-message'>" + resourceBundle["error.dashboard.nodata"] + "</div>");
		}
		ajaxRequest.submit();
	} else {
		cal_view_date_selected = '';
		renderCalendarForDashboard();
		$('.dashboard-legend').hide();
		autoRefreshIn = autoRefreshInterval;
		jQuery("#dashboardDataGrid").jqGrid('setGridParam',{url: "../dashboard/loadGridData.action" + filterData, page:1}).trigger("reloadGrid");
	}
}

/**
 * Render bar chart for planner view graph
 * @param result
 * @return
 */
function renderPlannerViewGraph(result) {
	$("#dashboardChart").empty().unbind('jqplotDataClick');
	
	if (result == '') {
		$("#dashboardChart").html("<div class='error-message'>" + resourceBundle["error.dashboard.nodata"] + "</div>");
	} else {
		if ($(result).size() < 21) {
			var planTicks = new Array();
			var redData = new Array();
			var greenData = new Array();
			var userArray = new Array();
			
			$(result).each(function(i, userData){
				planTicks[i] = userData.userName;
				redData[i] = userData.redCount;
				greenData[i] = userData.greenCount;
				userArray[i] = userData.userId;
			});
			userIdArray = userArray;

			var flage = (planTicks != null && planTicks.length >= 10) ? true : false ; 
			var plannerPlot = $.jqplot('dashboardChart', [redData, greenData], {
				stackSeries: true,
				captureRightClick: false,
				seriesColors: ["#FA1115", "#6AD126"],
				series: [{ label: 'Behind Schedule' }, { label: 'Ahead Of Schedule' }],
				seriesDefaults: {
					renderer: $.jqplot.BarRenderer,
					rendererOptions: { barMargin: 20 }
				},
				series : [
					{ pointLabels: { show: true, hideZeros: true, location: 's', labels: redData } },
					{ pointLabels: { show: true, hideZeros: true, location: 's', labels: greenData } }
				],
				axes: {
					xaxis: {
						renderer: $.jqplot.CategoryAxisRenderer,
						ticks: planTicks,
						rendererOptions:{
                            tickRenderer: $.jqplot.CanvasAxisTickRenderer ,
                            tickOptions: {
                                         angle: flage ? -45 : 0,
                                         fontSize: '8pt',
                                         fontFamily:'Arial',
                                         fontWeight:'bold'
                                       }
                            }
					},
						yaxis: {
						min: 0, padMin: 0,
						tickInterval: 5,
						tickOptions: { formatString: '%d' }
					}
				}
			});
            $('#dashboardChart').bind('jqplotDataClick', function(ev, seriesIndex, pointIndex, data){
                var tmpFilter = "userId=" + userIdArray[data[0] - 1] + "&status=" + (seriesIndex == 0 ? "BEHIND" : "AHEAD");
                tmpFilter = filterData == '' ? "?" + tmpFilter : filterData + "&" + tmpFilter;
                jQuery("#dashboardDataGrid").jqGrid('setGridParam', {url: "../dashboard/loadGridData.action" + tmpFilter, page: 1 }).trigger("reloadGrid");
            });
		} else {
			$("#dashboardChart").html("<div class='error-message'>" + resourceBundle["error.dashboard.maxUser"] + "</div>");
		}
	}
}

/**
 * Render Pie chart for status view graph 
 * @param {Object} result
 */
function renderStatusViewGraph(result) {
	$("#dashboardChart").empty().unbind('jqplotDataClick');

	if (result == '') {
		$("#dashboardChart").html("<div class='error-message'>" + resourceBundle["error.dashboard.nodata"] + "</div>");
	}
	else {
		var redData = 0;
		var greenData = 0;

		$(result).each(function(i, userData){
			redData += userData.redCount;
			greenData  += userData.greenCount;
		});

		if (redData == 0 && greenData == 0) {
			$("#graphForCriticality").html("<div class='error-message'>" + resourceBundle["error.dashboard.nodata"] + "</div>");
		}
		else {
			var s1 = [['Ahead Of Schedule', greenData], ['Behind Schedule', redData]];
			var proposalPlot = $.jqplot('dashboardChart', [s1], {
				seriesColors: ["#6AD126", "#FA1115"],
				seriesDefaults: {
					renderer: $.jqplot.PieRenderer,
					rendererOptions: {
						dataLabels: 'value',
						showDataLabels: true
					}
				}
			});
			$('#dashboardChart').bind('jqplotDataClick', function(ev, seriesIndex, pointIndex, data) {
				var tmpFilter = "status=" + (pointIndex == 1 ? "BEHIND" : "AHEAD");
                tmpFilter = filterData == '' ? "?" + tmpFilter : filterData + "&" + tmpFilter;
                jQuery("#dashboardDataGrid").jqGrid('setGridParam', {url: "../dashboard/loadGridData.action" + tmpFilter, page: 1 }).trigger("reloadGrid");
			});
		}
	}
}

/**
 * 
 */
function renderCalendarForDashboard() {
	jQuery("#dashboardChart").empty().html("<div id='calendar' style='width:45%;height:45%;margin:0 auto'></div>");
	
	$("#calendar").fullCalendar({
		weekMode: 'liquid',
		aspectRatio: 2,
		header: {
			right: 'prev,next'
		},
		events: {
			type: "POST",
			endParam: "endDate",
			startParam: "startDate",
			url : "../dashboard/getCalendarData.action" + filterData
		},
		eventRender: function(event, element) {
			$(element).empty().removeClass('fc-event');
			if (event.proposals > 0) {
				$(element).append("<span class='calendar-center-data'>" + event.proposals + "</span>").bind("click", function () {
					cal_view_date_selected = $.datepicker.formatDate("yy-mm-dd", event.start);
					$("td", "#calendar").removeClass("selected-cal-date");
					$("td[data-date='" + cal_view_date_selected + "']").addClass('selected-cal-date');
					var date = $.datepicker.formatDate("mm/dd/yy", event.start);
					var filter = filterData == '' ? "?filterDate=" + date : filterData + "&filterDate=" + date;
					jQuery("#dashboardDataGrid").jqGrid('setGridParam', {
						url: "../dashboard/loadGridData.action" + filter,
						page: 1
					}).trigger("reloadGrid");
				});
			}
			if($(".fc-week").length == 6 && $.browser.webkit) {  // setting position in case of 6 rows in chrome and opera
				$("#dashboardChart .calendar-center-data").css("top", "-9px");
			}
			if($(".fc-week").length == 6 && ($.browser.mozilla || $.browser.msie)){	// setting position in case of 6 rows in IE and mozilla
				$("#dashboardChart .calendar-center-data").css("top", "-3px");
			}
		},
		viewDisplay : function(view) {
			if ($("#dueDate", "#dashboardContainer").select2('val') != '') {
				var currentDate = new Date();
				currentDate.setDate(currentDate.getDate() - 7);
				
				var start_date_string = currentDate.getMonth() + '/' + currentDate.getFullYear();
				
				currentDate.setDate(currentDate.getDate() + 7);
				var end_date_string = currentDate.getMonth() + '/' + currentDate.getFullYear();
				
				var cal_date_string = view.start.getMonth() + '/' + view.start.getFullYear();
				if(cal_date_string == start_date_string) { 
					jQuery(".fc-button-prev", "#dashboardContainer").addClass("fc-state-disabled"); 
				} else {
					jQuery(".fc-button-prev", "#dashboardContainer").removeClass("fc-state-disabled"); 
				}
	
				if(cal_date_string == end_date_string) { 
					jQuery(".fc-button-next", "#dashboardContainer").addClass("fc-state-disabled"); 
				} else { 
					jQuery(".fc-button-next", "#dashboardContainer").removeClass("fc-state-disabled"); 
				}
			}
			if (cal_view_date_selected != '') {
				$("td", "#calendar").removeClass("selected-cal-date");
				$("td[data-date='" + cal_view_date_selected + "']").addClass('selected-cal-date');
			}
		}
	});
}

/**
 * Set time interval for auto refresh dashboard in specific time interval.
 */
function setAutoRefreshTimer () {
	var interval = setTimeout(function(){
        var minutes = Math.floor(autoRefreshIn / 60), seconds = (autoRefreshIn - (Math.floor(autoRefreshIn / 60) * 60));
		if(minutes < 10){
			minutes = "0" + minutes;
		} if(seconds < 10){
			seconds = "0" + seconds;
		}
		$("#dashboardCounter", "#dashboardContainer").text(minutes + " : " + seconds);
		
		if (autoRefreshIn <= 0) {
			if ($("#dashboardContainer").is(":visible")) {
				initDashboard();
				dashboardRefreshed = true;
			} else {
				dashboardRefreshed = false;
			}
		} else {
			--autoRefreshIn;
		}
		setAutoRefreshTimer();
	}, 1000);
}
