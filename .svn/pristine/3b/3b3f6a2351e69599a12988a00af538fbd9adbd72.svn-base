<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
	<head>		
		<title>AMPT Proposal: ${proposalForm.proposalTitle}</title>
		<link rel="stylesheet" href="./css/jquery.ui.base.css" type="text/css" />
		<link rel="stylesheet" href="./css/override.css?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />" type="text/css" />
		<link rel="shortcut icon" href="http://css.nyt.com/images/icons/nyt.ico" />
		<script type="text/javascript" src="./js/plugins/jquery/jquery-1.6.1.js"></script>
		<script type="text/javascript" src="./js/plugins/jquery/jquery-ui-1.8.7.js"></script>
		<script type="text/javascript" src="./js/plugins/jquery.numeric.js"></script>
		<script type="text/javascript" src="./js/plugins/jquery.blockUI.js"></script>
		<script type="text/javascript" src="./js/plugins/jquery-ui-timepicker-addon.js"></script>
		<script type="text/javascript" src="./js/plugins/jquery.qtip.js"></script>		
		<script type="text/javascript" src="./js/plugins/jquery.form.js"></script>	
		<script type="text/javascript" src="./js/plugins/jquery.select2.js"></script>
		
		<script type="text/javascript" src="./js/plugins/jqgrid/grid.locale-en.js"></script>
		<script type="text/javascript" src="./js/plugins/jqgrid/jquery.jqGrid.src.js"></script>
		
		<script type="text/javascript" src="./js/plugins/multi-select/jquery.multiselect.filter.js"></script>
		<script type="text/javascript" src="./js/plugins/multi-select/multi-select.js"></script>
		
				
		<script type="text/javascript" src="./js/common/common-form.js?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />"></script>
		<script type="text/javascript" src="./js/common/base-templates.js?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />"></script>
		<script type="text/javascript" src="./js/proposal/proposalCommon.js?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />"></script>
		
		<script type="text/javascript">
			function lineitemlinkFormatter(cellvalue, options, rowObject){
				return "<div class='ext-line-item-actions'><a onClick=\"showLineItems('"+rowObject.id+"','"+rowObject.proposalVersion+"','"+rowObject.expired+"')\" class='look-up' title='Lookup line items'></a>&nbsp;&nbsp;&nbsp;<a onClick=\"copyAlllineItems('"+rowObject.id+"','"+rowObject.proposalVersion+"','"+rowObject.expired+"','"+rowObject.optionId+"')\" title='Copy all' class='copy-all'></a></div>";
		    }
		    
		    function packagelineitemlinkFormatter(cellvalue, options, rowObject){
		    	return "<div class='ext-line-item-actions'><a onClick=\"showLineItems('"+rowObject.packageId+"','"+rowObject.breakableStr+"','"+rowObject.expired+"')\" class='look-up' title='Lookup line items'></a>&nbsp;&nbsp;&nbsp;<a onClick=\"copyAlllineItems('"+rowObject.packageId+"','"+rowObject.breakableStr+"','"+rowObject.expired + "','"+"')\" class='copy-all' title='Copy all'></a></div>";
		    }
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
	</head>
	<body>
		<div id="edit-proposal-container">
			<div id="header">		
				<dl>
					<dt class="hdr-lt logo-nyt">&nbsp;&nbsp;&nbsp;&nbsp;
						<div class="logo"></div>
						<div class="logo-text" id="proposalNameInHeader"></div>
					</dt>
					<dt class="hdr-rt">						
						<span class="close">
							<a href="javascript:void(0)" id="close-button" title="Close Proposal"></a>
						</span>
						<span class="feedback">
							<a href="javascript:void(0)" id="feedback-button" title="Feedback"></a>
						</span> 
					</dt>
				</dl>
			</div>
			
			<!-- Proposal Data Container : Start -->
			<div id="proposalOuterContainer" class="proposalOuterContainer" >
				<jsp:include page="basicInfo.jsp"></jsp:include>
			</div>
			<!-- Proposal Data Container : End -->
			
			<input type="hidden" id="contextPath" value="<%= request.getContextPath() %>" />
			<input type="hidden" id="applicationURL" value="${applicationURL}" />
			<div style="display: none" id="confirmSavedDialogDiv" title="Warning"></div>
			<div style="display: none" id="submittedForReviewDialogDiv" title="Warning"></div>
			<div style="display: none" id="confirmSavedDialogFunctionsDiv" title="Warning"></div>
			<div style="display: none" id="sessionTimeout" title="Session Timeout">
				<table cellpadding="2" cellspacing="2">
					<tr>
						<td>
							<img src='./images/sessiontimeout.png' height="32px" width="32px" />
						</td>
						<td style='line-height: 2; vertical-align: top;'>
							<spring:message	code="message.generic.sessionTimeout" />
						</td>
					</tr>
				</table>
			</div>
			<div id="loader" style="display: none" class="main_loader_sec">
				<div class="loader_block">				
					<div class="loader_text"><spring:message code="label.generic.loader"/></div>
				</div>
			</div>
			<div style="display: none" id="runtimeDialogDiv"></div>
			<div id="downloadExcel" title="Generating Media Plan..." style="display: none;">
				<br><br><center><p>Generating media plan. Please wait...</p></center>
			</div>
		</div>
		<div style="display: none;" id="cloneProposalDialogue" title="Clone Proposal">
			<div id="cloneProposalContainer"></div>
		</div>
		<div style="display: none;" id="proposalReviewDialogue" title="Review Proposal">
			<div id="proposalReviewContainer"></div>
		</div>
		<div style="display: none;" id="optionsThresholdValueDialogue" title="Option Threshold Value">
			<div id="optionsThresholdValueContainer"></div>
		</div>		
	</body>
	<div id="blocker" style="background:rgba(0,0,0,.5);position:absolute;z-index:9999;height:0;width:100%;top:40px;"></div>			
	<script>
		$('.tip-tool[title]').qtip({position:{my:  'left center',at: 'right center'}, style: {classes: 'ui-tooltip-blue'} });
		$('.tip-toolright[title]').qtip({position:{my:  'right center',at: 'left center'},style: {classes: 'ui-tooltip-blue'}});
	</script>
</html>
