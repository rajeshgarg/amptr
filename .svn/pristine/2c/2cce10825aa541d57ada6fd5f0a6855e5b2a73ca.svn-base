<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script>
$(function () { // this line makes sure this code runs on page load	
	$('.checkall').click(function () {
		$(this).parents('div').find(':checkbox').filter(function() { return !this.disabled; }).attr('checked', this.checked);
	});
});


function selectUnselect(obj){
	if(!$(obj).is(':checked')){		
  	  	$("#selectAllOptions","#proposalOptionListing").attr('checked', false);
  	}  	
  	if($('[name=lookupOptionId]:not(:checked)',"#proposalOptionListing").size() == 0){
  		$("#selectAllOptions","#proposalOptionListing").attr('checked', 'checked');
  	}
}
</script>

<div id="proposalOptionListing" >
<table width="100%" cellpadding="2" cellspacing="2" class="table-layout">
	<tr>
		<td class="help-row-bg" align="left" width="15%"><input type="checkbox" checked="checked" id="selectAllOptions"  class="checkall" ></input><label class="label-bold">&nbsp;</label></td>		
		<td class="help-row-bg" align="center"><label class="label-bold"><spring:message code="label.basicinfo.optionName"/></label></td>
	</tr>
	<c:forEach var="option" items="${proposalOptions}" varStatus="loop"> 	
	<tr>
		<td><input type="checkbox" checked="checked" name="lookupOptionId" id="lookupOptionId" onclick="selectUnselect(this)" value="${option.optionId}"></input></td>
		<td style="padding: 0 0 0 5px; vertical-align: top; ">${option.optionName}
		 <c:if test="${option.defaultOption == true}">
		 	<span class="pop-default-icon"></span>
		 </c:if>
		 </td>
	</tr>
	</c:forEach>
</table>
</div>

