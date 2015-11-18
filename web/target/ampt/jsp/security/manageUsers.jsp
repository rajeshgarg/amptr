<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<script type="text/javascript" src="../js/security/manageUsers.js?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />"></script>

<div id="manageUserContainer">
	<table id="usersMasterGrid" class="master"></table>
	<div id="userGridPager"></div>
	<div class="spacer7"></div>
	<div id="manageuserTab">
		<jsp:include page="userDetails.jsp"></jsp:include>
	</div>
	<div id="lookupuserSearchResult" style="display: none" title="Lookup Result"></div>
	<div class="ui-jqgrid-titlebar-close" style="right: 0px; display: none" id="userSearchPanel">
		<input id="showAllUsers" type="checkbox" name="showAllUsers" value="true" style="vertical-align: middle;" onclick="reloadUserMasterGrid()"/>
		<label style="padding-left:2px;padding-right:15px;"><spring:message code="label.generic.show.inactive.user"/></label>
		<select id="userSearchOption">
			<option value="loginName"><spring:message code="label.generic.loginName"/></option>
			<option value="firstName"><spring:message code="label.generic.firstName"/></option>
			<option value="lastName"><spring:message code="label.generic.lastName"/></option>
			<option value="email"><spring:message code="label.generic.email"/></option>			
		</select>
		<input id="userSearchValue" type="text" size="20" class="search-icon search-input"/>
	</div>
</div>

