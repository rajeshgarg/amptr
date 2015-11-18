<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
	<head>
		<script>
			$(document).ready(function() {
			 	$('#backToBasic', '#'+'${renderFragmentId}').click(function() {		
			 		$('#_eventId').val("eventbasicInfo"); 		 	 	 
			 	 	$('#searchProposalForm' , '#' +'${renderFragmentId}').ajaxSubmit({
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

	<div id=${renderFragmentId}>		
		<!--<form action="../manageProposal.action?_flowId=proposalDetailVersion-flow"></form>			
		This is Search From Proposal  Page.
		<br/>-->
		
		
		<body>
		<br/>
			Proposal Id :		${proposalForm.id} 
			<br><br>
			Version : 		${proposalForm.proposalVersion}
			<br><br>
			Render Div :		${renderFragmentId}
			<br><br>
			Proposal Budget :${proposalForm.budget}
			<br><br>
			Proposal name :	${proposalForm.proposalName}
			<br><br>
			
		  <form:form id="searchProposalForm" 	action="${flowExecutionUrl}" modelAttribute="SearchProposalForm">
   			 
        	<div class="tabbedPane ui-tabs ui-widget ui-widget-content ui-corner-all page-header"> Search Proposals</div>
        	
            	<table width="100%" border="0" cellpadding="5" cellspacing="10" class="tabbedPane ui-tabs ui-widget ui-widget-content ui-corner-all">
	                <tr>
	                    <td width="15%" class="typ_black typ_align_right">Proposal name:</td>
	                    <td colspan=""><form:input id="proposalname" path="proposalname"  size="10" /></td>
	                    <td class="style1">Search keywords:</td>
	                    <td><form:input path="keywords"  id="keywords" size="20" /></td>	                     
	                </tr>
	                <tr>
	                    <td class="typ_black typ_align_right" nowrap="nowrap">Contains products:</td>
	                    <td>
	                       <form:select path="product" id="product">
	                            <option value=""><spring:message code="label.generic.blankSelectOption"/></option>
	                        </form:select>
	                    </td>	                    
	                    <td class="style1"></td>
	                    <td></td>
	                </tr>
	                <tr>
	                    <td class="typ_black typ_align_right" nowrap="nowrap">Budget range from:</td>
	                    <td><form:input path="budgetFrom" id="budgetFrom" size="7" />
	                        <form:select path="currency" id="currency">
								<option value=""><spring:message code="label.generic.blankSelectOption"/></option>		
								<form:options items="${allCurrencies}" />								
							</form:select>
	                    </td>
	                    <td class="style1">To:</td>
	                    <td><form:input path="budgetTo" id="budgetTo" size="7" />
	                        <form:select path="currency" id="currency">
								<option value=""><spring:message code="label.generic.blankSelectOption"/></option>		
								<form:options items="${allCurrencies}" />								
							</form:select>
	                    </td>
	                </tr>
	                <tr>
	                    <td class="typ_black typ_align_right">Sales Category: </td>
	                    <td>
	                       <form:select path="salescategory" id="salescategory">
								<option value=""><spring:message code="label.generic.blankSelectOption"/></option>
								<form:options items="${allSalesCategories}" />
							</form:select>
	                    </td>
	                    <td class="style1">Industry Category:</td>
	                    <td>
	                        <form:select path="industry" id="industry">
								<option value=""><spring:message code="label.generic.blankSelectOption"/></option>
								<form:options items="${allIndusries}" />
							</form:select>
	                    </td>
	                </tr>
	                <tr>
	                    <td class="typ_black typ_align_right">Date range from: </td>
	                    <td><form:input path="proposalDateFrom" id="proposalDateFrom" size="7" /></td>
	                    <td class="style1">To:</td>
	                    <td><form:input path="proposalDateTo" id="proposalDateTo" size="7" /></td>
	                </tr>
	                <tr>
	                    <td class="typ_black typ_align_right">Agency name:</td>
	                    <td>
	                        <form:select path="agencyName" id="agencyName">
								<option value=""><spring:message code="label.generic.blankSelectOption"/></option>
								<form:options items="${allAgencies}" />
							</form:select>
	                    </td>
	                    <td class="style1">Advertiser name:</td>
	                    <td>
	                        <form:select path="advertiserName" id="advertiserName">
								<option value=""><spring:message code="label.generic.blankSelectOption"/></option>
								<form:options items="${allAdvertisers}" />
							</form:select>
	                    </td>
	                </tr>    
	            </table>           
	            <div class="spacer7"></div>
	            <table>
	                <tr>
	                	<td>	
		                    <div align="Center">
		                        <input type="button" id="name" value="Search" class="" value="" onClick="showSearchResult()"/>&nbsp;&nbsp;<input type="button" id="name1" class="" value="Reset" />
		                    </div>
						</td>	                   
	                </tr>
            	</table>
           		<div class="spacer7"></div>

            <div id="manageproTab" class="tabbedPaneInternal">
                <ul>
                    <li>
                        <a href="#manageproTab-tabs-1">Search Results</a>
                    </li>
                </ul>
                <div id="manageproTab-tabs-1">
                    <div id="searchResults"></div>               
            	</div>
            </div>	
            <div class="spacer7"></div>
             <div align="center">             
	              <input type="button" id="backToBasic" value="Basic Info" name="eventbasicInfo" />&nbsp;
	              <input type="button" value="Save and go to search package" onClick="goToNextSearchPackage();" style="width:180px"/>&nbsp;
	              <input type="button" value="Save and go to main screen" onClick="goToNextPropMain();" style="width:180px"/>&nbsp;
	              <input type="button" value="Skip to main screen" onClick="goToPrevNp();" style="width:140px"/>&nbsp;
	              <input type="hidden" " value="eventbasicInfo" id="_eventId" />
	        </div>
	      </form:form>  
        </body>
	</div>	
</html>