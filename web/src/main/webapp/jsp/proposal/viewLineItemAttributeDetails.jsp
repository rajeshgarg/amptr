<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<script>
	var renderfragmentID='${renderFragmentId}';
</script>

<script type="text/javascript" src="../js/proposal/buildProposal.js?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />"></script>



			
			<div id="newProposalaccordion" class="ui-accordion ui-widget ui-helper-reset ui-accordion-icons">
				
	<div
		class="ui-accordion-content ui-helper-reset ui-widget-content ui-corner-bottom ui-accordion-content-active">
	<div id="messageHeaderDiv" class="error"></div>
	<table width="100%">
		<tbody>
			<tr>
				<td colspan="5" height="5"></td>
			</tr>



			<c:forEach var="mediaPlanLineItemAttributeVO" items="${lineItemAttributeList}" varStatus="loop">
				<tr>
					<td>${mediaPlanLineItemAttributeVO.attributeName}</td>
					<td>${mediaPlanLineItemAttributeVO.attributeValue}</td>
					<td><input type="text" name='${mediaPlanLineItemAttributeVO.attributeName}'  /></td>

				</tr>
			</c:forEach>
			
			


			<tr>
				<td colspan="5" height="5"><input type="hidden" " value='${lineItemId}'  /></td>
			</tr>
			
			
		</tbody>
	</table>
	</div>


	<br>
			<script>
					function buildProposal(){
						var index = $tabs.tabs('option', 'selected');
						$tabs.tabs( "url" , index , "proposal.html");
						$tabs.tabs( "load" , index);
					}
					
					function searchProposalPage(){
						var index = $tabs.tabs('option', 'selected');
						$tabs.tabs( "url" , index , "ManageProposal.html");
						$tabs.tabs( "load" , index);
					}
				</script>
			</div>	
		
				

