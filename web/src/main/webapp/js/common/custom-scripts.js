/**
 * Script for main screen, initialised the tab, load resource bundle etc.
 * 
 * @version 2.0
 * @author surendra.singh
 * 
 */
var tabPadding;
var resourceBundle;
var listItemsWidth = 0;
var dashboardRefreshed = true;
var arrNavigation = new Object();
var menuCloseEventTrigerred = false;
	/**
	 * Show session time out model dialog
	 * @param {Object} event
	 * @param {Object} request
	 * @param {Object} settings
	 */
    jQuery('body').bind('ajaxComplete', function(event, request, settings){
        if (request.getResponseHeader('REQUIRES_AUTH') === '1') {
            jQuery("#sessionTimeout").model({ autofade: false, height: 135, width: 325,
                buttons: {
                    Ok: function(){
                        jQuery(this).dialog("close");
                        window.location.reload();
                    }
                },
                close: function(event, ui){
                    window.location.reload();
                }
            });
        }
    });

	/**
	 * Register progress message DIV on all AJAX call from the application  
	 */
	jQuery('body').bind('ajaxStart', function(event, request, settings){
		showLoader();
	});
	
	jQuery('body').bind('ajaxStop', function(event, request, settings){
		hideLoader();
	});
	
		cache: true,
		async: false,
		ajaxOptions: {
			async: false
		},
		tabTemplate: "<li><a href='#{href}'>#{label}</a> <span class='ui-icon ui-icon-close' id='#{label}' title='Close'></span></li>",
		add: function(event, ui){
			jQuery(ui.panel).append();
			$tabs.tabs('select', '#' + ui.panel.id);

			/**
			 * Resize the tab width to fit in the UI
			 */
			collapseMenuOptions();
		}, 
		select : function (event, ui) {
			if($("div:first", ui.panel).attr("id") == "dashboardContainer") {
				if (!dashboardRefreshed) {
					dashboardRefreshed = true;
					initDashboard();
				}
			}
		}
	});
		var index = jQuery("li", $tabs).index(jQuery(this).parent());
		if (index > -1) {
			menuCloseEventTrigerred = true;
			$tabs.tabs("remove", index);
			if($(this).attr("id") == resourceBundle['tab.generic.managePackage']){
				$("#packageLineItemsForm").dialog("destroy").remove();
			}
			var editedTabTitle = jQuery(this).prev().text().split(' ').join('').toLowerCase();
			jQuery("#" + editedTabTitle, "#menuAccordion").parent().removeClass("activemenu");
			var currentClosedAnchorTab = jQuery(this).attr("id") + "_Nav";
			listItemsWidth -= arrNavigation[currentClosedAnchorTab];
			unCollapseMenuOptions();
		}
	});

	tabPadding = jQuery(".ui-tabs-panel").css("padding-left").replace("px", "");
	
	/**
	 * initialised main screen layout
	 */
			maxSize: 205,

	/**
	 * Register focus and focus out event in all numeric and decimal field to format the data   
	 */
	jQuery(".numeric").live("focus", function(){
		jQuery(this).val(jQuery(this).val().replace(/,/g, ''));
	}).live("focusout", function(){
		if(jQuery(this).val() == '') {
			return;
		}
		if (parseFloat(jQuery(this).val(), 10) == 0) {
			jQuery(this).val(0);
		}
		jQuery(this).val(formatNumber(jQuery(this).val()));
	});
		jQuery(this).val(jQuery(this).val().replace(/,/g, ''));
	}).live("focusout", function(){
		if(jQuery(this).val() == '') {
			return;
		}
		if (parseFloat(jQuery(this).val(), 10) == 0) {
			jQuery(this).val(0);
		}
		jQuery(this).val(formatDecimal(jQuery(this).val()));
	});

	/**
	var index = jQuery("#tabs > ul > li > a").filter(function(index){
		return jQuery.trim(jQuery(this).text()) == tab_title.trim();
	}).parent().index();

	/**
	if (index == -1) {
		$tabs.tabs("add", url, tab_title);
	} else {
		$tabs.tabs('select', index);
	}
	var ajaxReq = new AjaxRequest();
	ajaxReq.url = "../homepage/initResourceBundle.action";
	ajaxReq.success = function(result, status, xhr){

		/**
		 * Setting a Global array variable named "resourceBunldle" to access
		 * constants which is defined in messages.properties
		 */
		resourceBundle = result;

		/**
		 * Load user default tab data
		 */
		$tabs.tabs("url", 0, defaultTabUrl);
		$tabs.tabs("load", 0);

		var editedTabTitle = resourceBundle[defaultTabTitle].split(' ').join('').toLowerCase();
		jQuery("#" + editedTabTitle).addClass("tab-opened");
		jQuery("#" + editedTabTitle).parent().addClass("activemenu");
	};
	ajaxReq.submit();
}
	menuCloseEventTrigerred = true;
	jQuery('#tabs ul.nav-heading li a').removeClass('collapsed-tab');
	jQuery("#tabs ul.nav-heading li a").css("width", "auto");
	collapseMenuOptions('close');
	//reset the fields
	menuCloseEventTrigerred = false;
}
	if (typeof menuTabAction == 'undefined' || menuTabAction == '') {
		menuTabAction = 'open';
	}
	var layoutWidth;
	var collapsedWidth = 0;

	//removed padding from both left and right side
	var layoutWidth1 = parseInt(jQuery(".ui-layout-center-custom").css('width')) - 200;
	var currentIndex = parseFloat(jQuery('#tabs ul.nav-heading li.ui-state-active').index());

	var currentAddedTabWidth = parseFloat(jQuery('#tabs ul.nav-heading li:last').outerWidth());
	var currentAddedAnchorTabWidth = parseFloat(jQuery('#tabs ul.nav-heading li:last').children("a").outerWidth());

	//the width is added only if the event is not closed.
	if (menuTabAction != 'close' && menuCloseEventTrigerred == false) {
		listItemsWidth += currentAddedAnchorTabWidth;
	}

	var currentOpenedAnchorTab = jQuery('#tabs ul.nav-heading li:last a').html() + "_Nav";
	arrNavigation[currentOpenedAnchorTab] = currentAddedAnchorTabWidth;
	currentAddedTabWidth -= 15;
	layoutWidth -= currentAddedTabWidth;

	if (listItemsWidth > layoutWidth1) {
		//get dynamic width to apply on each li for shrink
		var totalLi = parseInt(jQuery('#tabs ul.nav-heading li').length);
		collapsedWidth = parseInt(layoutWidth1 / totalLi);
		collapsedWidth = collapsedWidth - 45;
		if (collapsedWidth > 0) {
			//append dynamic width
			jQuery('#tabs ul.nav-heading li a').each(function(){
				jQuery(this).width(collapsedWidth);
				jQuery(this).addClass('collapsed-tab');
			});
		}
		else {
			jQuery('#tabs ul.nav-heading li a').each(function(){
				jQuery(this).width(1);
				jQuery(this).addClass('collapsed-tab');
			});
		}
	}
	else {
		//reset the width to 0
		collapsedWidth = 0;
		jQuery('#tabs ul.nav-heading li a').removeClass('collapsed-tab');
		jQuery("#tabs ul.nav-heading li a").css("width", "auto");
	}
}