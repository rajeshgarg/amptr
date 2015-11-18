<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
	<head>
		<script>
		$(document).ready(function() {
		 	$('#backToBasic', '#'+'${renderFragmentId}').click(function() {		
		 		$('#_eventId').val("eventbasicInfo"); 		 	 	 
		 	 	$('#lineItemDetailsForm' , '#' +'${renderFragmentId}').ajaxSubmit({
		 	 	 url: 	'${flowExecutionUrl}'+'&_eventId='+$('#_eventId').val(),
			      success: function(responseText, statusText, xhr, $form  ) { 
								if(statusText == "success"){
									$('#'+'${renderFragmentId}').html(responseText);
							 	} 
				 },
				 error: function(XMLHttpRequest,xhr, $form){
						 alert("error");
						 $('#'+'${renderFragmentId}').html(XMLHttpRequest.responseText);
					 
						}
				});
			});
		});
		</script>
	</head>
	<body>	
		<div id=${renderFragmentId}>
			<form:form id="lineItemDetailsForm" 	action="${flowExecutionUrl}" modelAttribute="proposalForm">
						<form:errors path="*" cssClass=""></form:errors>				
						<div class="tabbedPane ui-tabs ui-widget ui-widget-content ui-corner-all page-header"> Line Item Details </div>
		        		<table class="page-header1" width="100%" cellpadding="5" cellspacing="2">       
		         			<tr><td class="style1">Proposal ID :</td><td><form:input id="id" path="id" size="10" /></td></tr>
		        			<tr><td class="style1">Proposal Name :</td><td><form:input id="proposalName" path="proposalName"  size="10" /></td></tr>
		        			<tr><td class="style1">Proposal Version :</td><td><form:input id="proposalVersion" path="proposalVersion"  size="10" /></td></tr>
						    <tr><td class="style1"  nowrap="nowrap">Total allocated Budget ($) :</td><td><form:input id="budget" path="budget" size="10" /></td></tr>
						    <tr><td class="style1"  nowrap="nowrap">Budget Used so far ($) : </td><td><form:input id="budget" path="budget" size="10" /></td></tr>
							<tr>
								<td>
									<input type="button" id="backToBasic" value="Basic Info" name="eventbasicInfo" />
									<input type="hidden" " value="eventbasicInfo" id="_eventId" />
								</td>
							</tr>
		        		</table>
					</form:form>	
				
				
				<input type="button" onClick="stateChangeRequest('${flowExecutionUrl}','eventSearchFromPackage')" value="Search Package/Proposal"/> 
				<input type="button" onClick="stateChangeRequest('${flowExecutionUrl}','eventSearchFromProposal')" value=""/>  
				
				<input type="button" onClick="stateChangeRequest('${flowExecutionUrl}','eventRejectProposal')" value="Reject"/> 
				<input type="button" onClick="stateChangeRequest('${flowExecutionUrl}','eventReviewProposal')" value="Review"/> 	 
		</div>	
	</body>
</html>
