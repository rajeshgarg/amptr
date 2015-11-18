
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder,com.nyt.mpt.domain.User" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="/WEB-INF/nyt.tld" prefix="nyt"%>


<html lang="en">
	<head>
		<title><spring:message code="title.generic.homePageTitle"/></title>
		<link rel="shortcut icon" href="http://css.nyt.com/images/icons/nyt.ico" /> 
		<link rel="stylesheet" href="../css/jquery.ui.base.css?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />" type="text/css" />
		<link rel="stylesheet" href="../css/layout.help.css?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />" type="text/css" />
		<link rel="stylesheet" href="../css/override.css?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />" type="text/css" />
		
		<script type="text/javascript" src="../js/plugins/jquery/jquery-1.6.1.js"></script>
		<script type="text/javascript" src="../js/plugins/jquery/jquery-ui-1.8.7.js"></script>
		<script type="text/javascript" src="../js/plugins/jqgrid/grid.locale-en.js"></script>
		<script type="text/javascript" src="../js/plugins/jqgrid/jquery.jqGrid.src.js"></script>
		<script type="text/javascript" src="../js/plugins/multi-select/jquery.multiselect.filter.js"></script>
		<script type="text/javascript" src="../js/plugins/multi-select/multi-select.js"></script>
		
		<script type="text/javascript" src="../js/plugins/jquery.layout-latest.js"></script>
		<script type="text/javascript" src="../js/plugins/jquery.numeric.js"></script>
		<script type="text/javascript" src="../js/plugins/jquery.blockUI.js"></script>
		<script type="text/javascript" src="../js/plugins/jquery-ui-timepicker-addon.js"></script>
		<script type="text/javascript" src="../js/plugins/jquery.form.js"></script>
		<script type="text/javascript" src="../js/plugins/jquery.qtip.js"></script>
		<script type="text/javascript" src="../js/plugins/jquery.select2.js"></script>
		<script type="text/javascript" src="../js/plugins/jquery.sheepItPlugin.js"></script>
		
		<script type="text/javascript" src="../js/common/base-templates.js?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />"></script>
		<script type="text/javascript" src="../js/common/custom-scripts.js?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />"></script>
		<script type="text/javascript" src="../js/common/common-form.js?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />"></script>
		<script type="text/javascript" src="../js/proposal/manageProposalWindow.js?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />"></script>
		
		<script type="text/javascript">
			function attachedDocumentFormatter(cellvalue, options, rowObject){
		        if (cellvalue) {
					return "<div align='center'><img border='0' align='center' src='../images/attached.png' /></div>";
				} else {
					return "<div align='center'><span/></div>";
				}
		    };

		    var defaultTabTitle = '${defaultTab}';
		    var defaultTabUrl = '${defaultTabUrl}';	
		    
		    function reservationSearchlinkFormatter(cellvalue, options, rowObject){
		        if (cellvalue) {
			        return "<div><a onClick=\"openProposal('"+rowObject.proposalId+"')\" href='#' ><u>"+cellvalue+"</u></a></div>";
				} else {
					return "<div align='center'><span/></div>";
				}
		    };
		</script>
		
		<!-- Custom code for feedback -->	
		<script type="text/javascript" src="https://jira.em.nytimes.com/s/en_US-qkmn5g/729/6/1.2.4/_/download/batch/com.atlassian.jira.collector.plugin.jira-issue-collector-plugin:issuecollector/com.atlassian.jira.collector.plugin.jira-issue-collector-plugin:issuecollector.js?collectorId=3cdf020d"></script>
  
	    <!-- This is the script for specifying the custom trigger.  We've replaced 'myCustomTrigger' with 'feedback-button' -->
	 
	    <script type="text/javascript">
	        window.ATL_JQ_PAGE_PROPS =  {
	            "triggerFunction": function(showCollectorDialog) {
	                //Requires that jQuery is available!
	                jQuery("#feedback-button").click(function(e) {
	                    e.preventDefault();
	                    showCollectorDialog();
	                });
	            }
	        };
	    </script>
    <!-- Custom code for feedback Ends-->	
	</head>
	<body>
		<div id="wrapper">			
			<div id="container" style="min-height: 300px;">
				<div id="header" class="pane ui-layout-north">
					<dl>
						<dt class="hdr-lt"><img src='../images/logo.png' height="35"/></dt>
						<dt class="hdr-rt">
							<ul class="login-status-bar">
								<li>
									<a class="user-img"></a>
									<nyt:componentHandler bean="welcomeStringComponentHandler"/>
									<a id="user_name" class="user-name"><c:out value="${welcomeString}"/></a>
								</li>
								<li>
									<a href="javascript:void(0)" id="feedback-button" class="feedback-btn" title="Feedback"></a>
								</li>
								<li>
									<a href="<c:url value='/j_spring_security_logout' />" class="logout-btn" title="Logout"></a>
								</li>
							</ul>
						</dt>
					</dl>
				</div>
				<div class="pane ui-layout-center ui-layout-center-custom">
					<div id="tabs">
						<ul class="nav-heading">
							<li><a href="#tabs-1"><spring:message code="${defaultTab}"/></a></li>
						</ul>
						<div id="tabs-1"></div>
					</div>
				</div>
				<div class="pane ui-layout-west">
					<div class="content-div menu">
						<div id="menuAccordion" class="ui-accordion ui-widget ui-helper-reset ui-accordion-icons menu-container ">
							<sec:authorize access="hasAnyAuthority('DAB')">
								<h3 class="accordian-header ui-accordion-header ui-helper-reset ui-state-default ui-state-active ui-corner-top">
									<span class="accordian-icon"><img src="../images/dashboardP.png" width="18px" height="18px" style="cursor: auto !important;"/></span>
									<span class="accordian-txt"><a href="#"><spring:message code="tab.generic.dashboard"/></a></span>
								</h3>
								<div class="ui-accordion-content ui-helper-reset ui-widget-content ui-corner-bottom ui-accordion-content-active">
									<ul>										
										<li><a id="dashboard" onclick="addTab('tab.generic.warroom','../dashboard/viewdetail.action')" href="#">
											<spring:message code="tab.generic.warroom"/></a></li>
									</ul>
								</div>
							</sec:authorize>
							<sec:authorize access="hasAnyAuthority('CNP', 'MPL', 'MTE', 'ESS')">
								<h3 class="accordian-header ui-accordion-header ui-helper-reset ui-state-default ui-state-active ui-corner-top">
									<span class="accordian-icon"><img src="../images/proposal_newP.png" width="18px" height="18px" style="cursor: auto !important;"/></span> 
									<span class="accordian-txt"><a href="#"><spring:message code="tab.generic.proposal"/></a></span>
								</h3>
								<div class="ui-accordion-content ui-helper-reset ui-widget-content ui-corner-bottom ui-accordion-content-active">
									<ul>										
										<sec:authorize access="hasAuthority('MPL')">
											<li><a id="manageproposal" onclick="addTab('tab.generic.manageproposal','../manageProposal/viewdetail.action')" href="#">
												<spring:message code="tab.generic.manageproposal"/></a></li>
										</sec:authorize>
										<sec:authorize access="hasAuthority('MTE')">
											<li><a id="managetemplate" onclick="addTab('tab.generic.manageTemplates', '../templateManagement/viewTemplateManagement.action')" href="#">
												<spring:message code="tab.generic.manageTemplates"/></a></li>
										</sec:authorize>
										<sec:authorize access="hasAuthority('MPL')">
											<li><a id="managereservations" onclick="addTab('tab.generic.managereservations','../manageReservation/viewDetail.action')" href="#">
												<spring:message code="tab.generic.managereservations"/></a></li>
										</sec:authorize>
										<sec:authorize access="hasAuthority('MPL')">
											<li><a id="fetchavails" onclick="addTab('tab.generic.fetchAvails','../avails/viewDetail.action')" href="#">
												<spring:message code="tab.generic.fetchAvails"/></a></li>
										</sec:authorize>
										<sec:authorize access="hasAuthority('ESS')">
											<li><a id="manageemailsendschedule" onclick="addTab('tab.email.send.schedule', '../emailSchedule/viewDetail.action')" href="#">
												<spring:message code="tab.email.send.schedule"/></a></li>
										</sec:authorize>
									</ul>
								</div>
							</sec:authorize>
							<sec:authorize access="hasAnyAuthority('MCR', 'MPA', 'MPD', 'MAT')">
								<h3 class="accordian-header ui-accordion-header ui-helper-reset ui-state-default ui-state-active ui-corner-top">
									<span class="accordian-icon"><img src="../images/user_newP.png" width="18px" height="18px" style="cursor: auto !important;"/></span> 
									<span class="accordian-txt"><a href="#"><spring:message code="label.generic.adops"/></a></span>
								</h3>
								<div class="ui-accordion-content ui-helper-reset ui-widget-content ui-corner-bottom ui-accordion-content-active">
									<ul>
										<sec:authorize access="hasAuthority('MAT')">
											<li><a id="manageattribute" onclick="addTab('tab.generic.manageAttribute', '../manageAttribute/viewdetail.action')" href="#">
												<spring:message code="tab.generic.manageAttribute"/></a></li>
										</sec:authorize>
										<sec:authorize access="hasAuthority('MCR')">
											<li><a id="managecreative" onclick="addTab('tab.generic.manageCreative', '../manageCreative/viewdetail.action')" href="#">
												<spring:message code="tab.generic.manageCreative"/></a></li>
										</sec:authorize>
										
										<sec:authorize access="hasAuthority('MPD')">
											<li><a id="manageproduct" onclick="addTab('tab.generic.manageProduct', '../manageProduct/viewdetail.action')" href="#">
												<span><spring:message code="tab.generic.manageProduct"/></span></a></li>
										</sec:authorize>
										
										<sec:authorize access="hasAuthority('MPA')">
											<li><a id="managepackage" onclick="addTab('tab.generic.managePackage', '../managepackage/viewdetail.action')" href="#">
												<spring:message code="tab.generic.managePackage"/></a></li>
										</sec:authorize>
									</ul>
								</div>
							</sec:authorize>							
							<sec:authorize access="hasAnyAuthority('MST','MPR')">
								<h3 class="accordian-header ui-accordion-header ui-helper-reset ui-state-default ui-state-active ui-corner-top">
									<span class="accordian-icon"><img src="../images/priceP.png" width="18px" height="18px" style="cursor: auto !important;"/></span>
									<span class="accordian-txt"><a href="#"><spring:message code="label.generic.pricing"/></a></span>
								</h3>	
								<div class="ui-accordion-content ui-helper-reset ui-widget-content ui-corner-bottom ui-accordion-content-active">
									<ul>
										<sec:authorize access="hasAuthority('MPR')">
											<li><a id="managerateprofile" onclick="addTab('tab.generic.manageListRate', '../manageRateProfile/viewDetail.action')" href="#">
												<spring:message code="tab.generic.manageListRate"/></a></li>
											<li><a id="managepremium" onclick="addTab('tab.generic.manageTier', '../manageTier/viewDetail.action')" href="#">
												<spring:message code="tab.generic.manageTier"/></a></li>
											<li><a id="manageaddedvalue" onclick="addTab('tab.generic.manageaddedvaluerule', '../manageAddedValue/viewDetail.action')" href="#">
												<spring:message code="tab.generic.manageaddedvaluerule"/></a></li>
										</sec:authorize>
									</ul>
								</div>
							</sec:authorize>								
							<sec:authorize access="hasAnyAuthority('MUS')">
								<h3 class="accordian-header ui-accordion-header ui-helper-reset ui-state-default ui-state-active ui-corner-top">
									<span class="accordian-icon"><img src="../images/admin_newP.png" width="18px" height="18px" style="cursor: auto !important;"/></span> 
									<span class="accordian-txt"><a href="#"><spring:message code="label.generic.admin"/></a></span>
								</h3>	
								<div class="ui-accordion-content ui-helper-reset ui-widget-content ui-corner-bottom ui-accordion-content-active">
									<ul>					
										<sec:authorize access="hasAuthority('MUS')">
											<li><a id="manageusers" onclick="addTab('tab.generic.manageUsers', '../manageusers/showuserdetails.action')" href="#">
												<spring:message code="tab.generic.manageUsers"/></a></li>
										</sec:authorize>
									</ul>
								</div>
							</sec:authorize>	
								
						</div>
					</div>
					<span style="position: absolute;bottom: 5px;"><spring:message code="label.generic.NYTFooter"/></span>
				</div>
				<input type="hidden" id="contextPath" value="<%= request.getContextPath() %>" />
				<div style="display: none" id="confirmSavedDialogDiv" title="Warning"></div>
				<div style="display: none" id="confirmSavedDialogFunctionsDiv" title="Warning"></div>
				<div style="display: none" id="sessionTimeout" title="Session Timeout">
					<table cellpadding="2" cellspacing="5">
						<tr>
							<td>
								<img src='../images/sessiontimeout.png' height="32px" width="32px" /></td>
							<td style='line-height: 2; vertical-align: top;'>
								<spring:message	code="message.generic.sessionTimeout" /></td>
						</tr>
					</table>
				</div>
				<div id="loader" style="display: none" class="main_loader_sec">
					<div class="loader_block">
						<div class="loader_text"><spring:message code="label.generic.loader"/></div>
					</div>
				</div>
				<div style="display: none" id="runtimeDialogDiv"></div>
			</div>			
		</div>
	</body>
</html>