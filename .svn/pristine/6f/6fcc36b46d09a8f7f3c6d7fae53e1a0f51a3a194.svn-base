/**
 * JavaScript for manage user screen
 * @author surendra.singh
 */
var detailGridFormIdentifier	= '#userDetailForm';
var userDetailTabLabelDefault	= resourceBundle["tab.generic.userDetails"];
var userDetailTabLabelForAdd 	= resourceBundle["tab.generic.addNewUser"];

jQuery(document).ready(function() {
	
	jQuery("#manageuserTab", "#manageUserContainer").tabs();

	var userGridOpte = new JqGridBaseOptions(); 
	userGridOpte.url = "../manageusers/loadmastergridData.action?showAllUser=false";
	userGridOpte.colNames =	['User ID', 'Login Name', 'First Name', 'Version', 'Last Name', 'E-mail', 'Role', 'Status', 'International User', 'Sales Category Id', 'Role Id', 'Address1', 'Address2', 'State', 'City', 'telephone', 'Zip', 'mobile', 'Fax', 'Global', 'Active', 'Display On WarRoom'];
	userGridOpte.colModel = [
		{name:'userId', index:'userId', hidden:true, key:true}, 
		{name:'loginName', index:'loginName', key:false}, 
		{name:'firstName', index:'firstName', key:false}, 
		{name:'version', index:'version', hidden:true, key:false}, 
		{name:'lastName', index:'lastName', key:false}, 
		{name:'email', index:'email', key:false, width:230}, 
		{name:'userRolesAsString', index:'userRolesAsString', key:false}, 
		{name:'statusString', index:'statusString', key:false, hidden:true}, 
		{name:'globalUserString', index:'globalUserString', key:false}, 
		{name:'salesCategoryId', index:'salesCategoryId', hidden:true, key:false}, 
		{name:'roleId', index:'roleId', hidden:true, key:false}, 
		{name:'address1', index:'address1', hidden:true, key:false}, 
		{name:'address2', index:'address2', hidden:true, key:false}, 
		{name:'state', index:'state', hidden:true, key:false}, 
		{name:'city', index:'city', hidden:true, key:false}, 
		{name:'telephone', index:'telephone', hidden:true, key:false}, 
		{name:'zip', index:'zip', hidden:true, key:false}, 
		{name:'mobile', index:'mobile', hidden:true, key:false}, 
		{name:'faxNo', index:'faxNo', hidden:true, key:false}, 
		{name:'global', index:'global', hidden:true, key:false}, 
		{name:'active', index:'active', hidden:true, key:false},
		{name:'displayOnWarRoom', index:'displayOnWarRoom', hidden:true, key:false}
	];
    userGridOpte.onSelectRow = function(id){
        var gsr = jQuery("#usersMasterGrid").jqGrid('getGridParam', 'selrow');
        /**
         *  Clear all the CSS error classes on select new user
         */
        clearCSSErrors('#userDetailForm');
        if (gsr) {
            jQuery("#usersMasterGrid").jqGrid('GridToForm', gsr, detailGridFormIdentifier);
            jQuery('#manageuserTab', '#manageUserContainer').show();
            onChangeRole();
        }
        // Change the label of TAB.  
        jQuery("#userDetailFormTab-1 a").text(userDetailTabLabelDefault);
        jQuery("#userSearchFormDivIdentifier", "#userDetailFormDivIdentifier").hide();
        
        setUserSalesTarget(gsr);
        resetDisplayOnWarRoomVal();
    };
    userGridOpte.afterGridCompleteFunction = function(){
        if (jQuery('#usersMasterGrid').getDataIDs().length == 0) {
            document.getElementById("manageuserTab").style.display = 'none';
        }
    };
	userGridOpte.pager = jQuery('#userGridPager');
	userGridOpte.sortname = "userId";
	userGridOpte.caption = resourceBundle['tab.generic.manageUsers'];
	
	/**
	 * Master Grid declaration
	 */ 
    jQuery("#usersMasterGrid").jqGrid(userGridOpte).navGrid('#userGridPager', {
        edit: false, del: false, search: false,
        addfunc: function(rowid){
        	jQuery('#manageuserTab', '#manageUserContainer').show();
            resetRowSelectionOnAdding("#usersMasterGrid", "#hiddenuserId");
            clearCSSErrors('#userDetailForm');
            document.userDetailForm.reset();
            onChangeRole();
            jQuery("#hiddenuserId").val(0);
            jQuery("#userDetailFormTab-1 a").text(userDetailTabLabelForAdd);
            setUserSalesTarget(0);
            setUserDefaultAddress();
            jQuery("#userSearchFormDivIdentifier", "#userDetailFormDivIdentifier").show();
            jQuery('input[type!=hidden]:first', '#userDetailForm').focus();
            jQuery("#manageuserTab", "#manageUserContainer").tabs('option', 'selected', 0);
			$("#displayOnWarRoom", "#manageUserContainer").prop('checked', false);
			$("#displayWarRoomDiv", "#manageUserContainer").hide();
			$("#displayWarRoomCBDiv", "#manageUserContainer").hide();
        },
        beforeRefresh: function(){
            jQuery("#userSearchOption", "#manageUserContainer").val('loginName');
            jQuery("#userSearchValue", "#manageUserContainer").val('');
            reloadJqGridAfterAddRecord('usersMasterGrid', "userId");
        }
    });
	
	var instanceFormManager = new FormManager();
	instanceFormManager.gridName = 'usersMasterGrid';
	instanceFormManager.formName = 'userDetailForm';
		
	/**
	 * Register Ajax Forms Submit on save user
	 */
    jQuery('#saveUserDetailButton').click(function(){
        var userDetailForm = new JQueryAjaxForm(instanceFormManager);
        userDetailForm.doCustomProcessingAfterFormSucsesJson = function(jsonResponse, XMLHTTPRequest){
            jQuery("#runtimeDialogDiv").model({theme: 'Success', message: resourceBundle['message.generic.userSavedSuccessfully']});
            var userId = jQuery("#hiddenuserId").val();
            if (userId == 0) {
                reloadJqGridAfterAddRecord(instanceFormManager.gridName, "userId");
            }
            else {
                jQuery("#userRolesAsString", '#userDetailForm').val(jQuery('#roleId option:selected', '#userDetailForm').text());
                jQuery("#globalUserString", '#userDetailForm').val(jQuery('#global option:selected', '#userDetailForm').text());
                jQuery("#statusString", '#userDetailForm').val(jQuery('#active option:selected', '#userDetailForm').text());
                jQuery("#usersMasterGrid").jqGrid('FormToGrid', userId, "#userDetailForm");
                if($("#displayOnWarRoom", "#manageUserContainer").attr('checked')){
               		 jQuery("#usersMasterGrid").setCell(userId,"displayOnWarRoom" , true );
                }else{
              		 jQuery("#usersMasterGrid").setCell(userId,"displayOnWarRoom" , false );
                }
            }
        };
        jQuery("#selectedSalesCatList > option").attr("selected", true);
        userDetailForm.submit();
    });

	/**
	 * Reset form
	 */ 
    jQuery("#resetUserDetailFormButton").click(function(){
        if (jQuery("#userDetailFormTab-1 a").text() == userDetailTabLabelForAdd) {
            document.userDetailForm.reset();
            setUserSalesTarget(-1);
            jQuery('input[type!=hidden]:first', '#userDetailForm').focus();
        }
        else {
            setUserSalesTarget(jQuery('#hiddenuserId', '#userDetailForm').val());
            resetDetailForm('#userDetailForm', '#usersMasterGrid');
        }
		resetDisplayOnWarRoomVal();
        onChangeRole();
        clearCSSErrors('#userDetailForm');
    });
	
	jQuery('#roleId').change(function() {
		onChangeRole();
	});
	
	/**
	 * Dialog for lookup user result
	 */
	var lookupModal = new ModalDialog();
	lookupModal.width = 600;
	lookupModal.height = 300;
	lookupModal.buttons = [{
		text: "Select User",
		click : function(){
			var selected = jQuery('input[name=lookupUserRadio]:checked', '#lookupuserSearchResult').val();
			if(selected > -1){
				jQuery("#firstName", "#userDetailForm").val(jQuery("#fname_"+selected, "#lookupuserSearchResult").text());
				jQuery("#lastName", "#userDetailForm").val(jQuery("#lname_"+selected, "#lookupuserSearchResult").text());
				jQuery("#email", "#userDetailForm").val(jQuery("#email_"+selected, "#lookupuserSearchResult").text());
				jQuery("#loginName", "#userDetailForm").val(jQuery("#login_"+selected, "#lookupuserSearchResult").text());
				jQuery("#contactNo", "#userDetailForm").val(jQuery("#contactNo_"+selected, "#lookupuserSearchResult").val());
				jQuery(this).dialog("close");
			} else {
				jQuery("#errorUserSelect", "div[aria-labelledby=ui-dialog-title-lookupuserSearchResult]").show();
			}
		}
	}, {
		text: "Close",
		click: function(){
			jQuery(this).dialog("close");
		}
	}];	
	jQuery( "#lookupuserSearchResult").dialog(lookupModal);
	enableDialogButton("lookupuserSearchResult");
	jQuery(".ui-dialog-buttonpane", "div[aria-labelledby=ui-dialog-title-lookupuserSearchResult]")
    	.append("<div id='errorUserSelect' class='model-dialog-error'>"+ resourceBundle['error.ldap.select.user'] +"</div>");
	
	/**
	 * Initialised search panel on user grid
	 */
	initGridSearchOptions("usersMasterGrid", "userSearchPanel", "manageUserContainer");

	/**
	 * Enable auto search on user grid
	 */
	enableAutoSearch(jQuery("#userSearchValue", "#manageUserContainer"), function() {
		var showAllUser = jQuery("#showAllUsers:checked", "#manageUserContainer").val();
		if(showAllUser != 'true') {
			showAllUser = 'false';
		}
		jQuery("#usersMasterGrid").jqGrid('setGridParam', {
			url: '../manageusers/loadmastergridData.action?showAllUser='+showAllUser, page: 1,
			postData: {
				searchField: jQuery("#userSearchOption", "#manageUserContainer").val(),
				searchString: jQuery.trim(jQuery("#userSearchValue", "#manageUserContainer").val()),
				searchOper: 'cn'
			}
		}).trigger("reloadGrid");
	});
	
	/**
	 * Register change event on user search option select box
	 */
	jQuery("#userSearchOption", "#manageUserContainer").bind("change", function () {
		jQuery("#userSearchValue", "#manageUserContainer").val('').focus();
	});
	
	/**
	 * Enable enter key on lookup name
	 */
	enableAutoSearch(jQuery("#prexcname", "#manageUserContainer"), function() {
		lookupUser();
	});

    $("#roleId", "#manageUserContainer").change(function(){
        if ($("#roleId option:selected").text() == "Proposal Planner") {
            $("#displayWarRoomDiv", "#manageUserContainer").show();
			$("#displayWarRoomCBDiv", "#manageUserContainer").show();
        }
        else {
	        $("#displayOnWarRoom", "#manageUserContainer").prop('checked', false);
            $("#displayWarRoomDiv", "#manageUserContainer").hide();
			 $("#displayWarRoomCBDiv", "#manageUserContainer").hide();
        }
    });	
});

function onChangeRole() {
	//TODO need to be implemented as per requirement if any.
}

function lookupUser(){
	clearCSSErrors('#userDetailForm');
	var userName = jQuery('#prexcname', '#userDetailForm').val();
	if (userName == '') {
		showLookupError(resourceBundle['error.ldap.lookup.mandatory']);
	} else {
		var ajaxReq = new AjaxRequest();
		ajaxReq.url = "../manageusers/searchUser.action?userName="+userName;
		ajaxReq.success = function(result, status, xhr){
			if(result.length == 0){
				showLookupError(resourceBundle['error.ldap.lookup.norecord']);
			} else if (result.length == 1) {
				var user = result[0];
				jQuery("#firstName", "#userDetailForm").val(user.firstName);
				jQuery("#lastName", "#userDetailForm").val(user.lastName);
				jQuery("#email", "#userDetailForm").val(user.email);
				jQuery("#contactNo", "#userDetailForm").val(user.contactNo);
				jQuery("#loginName", "#userDetailForm").val(user.loginName);
			} else {
				var table = "<table cellpadding='2' cellspacing='2' width='100%'><tr>" +
					"<th align='left' width='5%'>&nbsp;</th>" +
					"<th align='left' width='20%'>First Name</th>" +
					"<th align='left' width='20%'>Last Name</th>" +
					"<th align='left' width='25%'>Login Name</th>" +
					"<th align='left'>Email</th></tr>";
					
				jQuery.each(result, function(i, user){
					var tds = "<tr><td><input type='radio' name='lookupUserRadio' value='" + i + "'></input></td>";
					tds += "<td nowrap='nowrap' id='fname_" + i + "'>" + user.firstName + "</td>";
					tds += "<td nowrap='nowrap' id='lname_" + i + "'>" + user.lastName + "</td>";
					tds += "<td nowrap='nowrap' id='login_" + i + "'>" + user.loginName + "</td>";
					tds += "<td nowrap='nowrap' id='email_" + i + "'>" + user.email + "<input type='hidden' id='contactNo_" +i + "' value='" +user.contactNo +"'></td></tr>";
					table += tds;
				});
				table += "</tr></table>";
				jQuery("#lookupuserSearchResult").html(table);
				jQuery("#errorUserSelect", "div[aria-labelledby=ui-dialog-title-lookupuserSearchResult]").hide();
				jQuery("#lookupuserSearchResult").dialog("open");
			}
		};
		ajaxReq.error = function (){
			showLookupError(resourceBundle['error.ldap.lookup.limit.exceed']);
		};
		ajaxReq.submit();
	}
}
function showLookupError(message){
	jQuery('#messageHeaderDiv', '#userDetailForm').html(resourceBundle['validation.error']).addClass('error');
	jQuery("#prexcname", "#userDetailForm").addClass("errortip errorElement")
			.attr("title", message + '<BR><b>Help:</b> ' + resourceBundle['help.error.mandatory']);
	jQuery("#prexcname", "#userDetailForm").qtip({style: {
        classes: 'ui-tooltip-red' 
    },
    position:{my:  'left center',at: 'right center'}
    });
}

function setUserSalesTarget(userId){
	var ajaxReq = new AjaxRequest();
	ajaxReq.url = "../manageusers/loadSalesCategories.action?userId="+userId;
	ajaxReq.success = function(result, status, xhr){
		var selectedSelect = jQuery('#selectedSalesCatList');
        var availSelect = jQuery('#availSalList');
        jQuery('option', selectedSelect).remove();
        jQuery('option', availSelect).remove();
        jQuery.each(result, function(i, item){
            if (i == "avaiableSalCatList") {
                jQuery.each(item, function(j, avail){
                    if (j == 0) {
                        jQuery(availSelect).append('<option selected="selected" value="' + avail.salesCategoryId + '" >' + avail.salesCategoryName + '</option>');
                    }
                    else {
                        jQuery(availSelect).append('<option value="' + avail.salesCategoryId + '" >' + avail.salesCategoryName + '</option>');
                    }
                });
            }
            if (i == "selectedSalCatListList") {
                jQuery.each(item, function(k, select){
                    if (k == 0) {
                        jQuery(selectedSelect).append('<option selected="selected" value="' + select.salesCategoryId + '" >' + select.salesCategoryName + '</option>');
                    } else {
                        jQuery(selectedSelect).append('<option value="' + select.salesCategoryId + '" >' + select.salesCategoryName + '</option>');
                    }
                });
            }
        });
	};
	ajaxReq.submit();
}

function setUserDefaultAddress(){
	var ajaxReq = new AjaxRequest();
	ajaxReq.url = "../manageusers/loadDefaultAddress.action";
	ajaxReq.success = function(result, status, xhr){
		jQuery("#address1", "#userDetailForm").val(result.address1);
		jQuery("#address2", "#userDetailForm").val(result.address2);
		jQuery("#state", "#userDetailForm").val(result.state);
		jQuery("#city", "#userDetailForm").val(result.city);
		jQuery("#zip", "#userDetailForm").val(result.zip);
	};
	ajaxReq.submit();
}

/**
 * Reload UserMasterGrid based on user filter criteria like showAllUsers etc. 
 */
function reloadUserMasterGrid(){
	var showAllUser = jQuery("#showAllUsers:checked", "#manageUserContainer").val();
	if(showAllUser != 'true') {
		showAllUser = 'false';
	}
	showHideStatusColumn(showAllUser);
	
	var searchUrl = '../manageusers/loadmastergridData.action?showAllUser='+showAllUser;	
	jQuery("#usersMasterGrid").jqGrid('setGridParam',{url:searchUrl, page:1}).trigger("reloadGrid");
}

/**
 * Will show and hide Active column based on filter criteria.
 */		
function showHideStatusColumn(showAllUser) {
	var gridCurrentWidth = jQuery("#usersMasterGrid", "#manageUserContainer").width();
	if(showAllUser != 'true') {
		jQuery("#usersMasterGrid", "#manageUserContainer").jqGrid('hideCol', "statusString");
	} else {
		jQuery("#usersMasterGrid", "#manageUserContainer").jqGrid('showCol', "statusString");
	}
	jQuery("#usersMasterGrid", "#manageUserContainer").setGridWidth(gridCurrentWidth, true);
}

function resetDisplayOnWarRoomVal(){
     if ($("#roleId option:selected").text() == "Proposal Planner") {
        $("#displayWarRoomDiv", "#manageUserContainer").show();
		 $("#displayWarRoomCBDiv", "#manageUserContainer").show();
        var gsr = jQuery("#usersMasterGrid").jqGrid('getGridParam', 'selrow');
        if(gsr){
            var displayOnWarRoomVal = jQuery("#usersMasterGrid").jqGrid('getCell', gsr, 'displayOnWarRoom');
            if(displayOnWarRoomVal == 'true'){
             	$("#displayOnWarRoom", "#manageUserContainer").prop('checked', true);
            }else{
            	 $("#displayOnWarRoom", "#manageUserContainer").prop('checked', false);
            }
        }
    } else {
        $("#displayOnWarRoom", "#manageUserContainer").prop('checked', false);
        $("#displayWarRoomDiv", "#manageUserContainer").hide();
		$("##displayWarRoomCBDiv", "#manageUserContainer").hide();
    }
}