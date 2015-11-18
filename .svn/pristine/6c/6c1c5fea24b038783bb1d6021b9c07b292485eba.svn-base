<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title><spring:message code="title.generic.login"/></title>
		<link rel="shortcut icon" href="http://css.nyt.com/images/icons/nyt.ico" />
		<link rel="stylesheet" type="text/css" href="./css/login.css?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />"></link>

		<script type="text/javascript" src="./js/plugins/jquery/jquery-1.6.1.js"></script>
		<script type="text/javascript" src="./js/security/login.js?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />"></script>
		<style type="text/css">
			body {
				margin: 0px;
				padding: 0px;
				height: 100%;
				scroll: no;
				background: #EEF1F4;
				font: bold .8em/1.7em Tahoma, Tahoma, Verdana, Arial, Helvetica, sans-serif;
			}
		</style>
		<script type="text/javascript">
			$(document).ready(function() {
				if ($.browser.msie) {
					$("#bestViewBrowser").show();
				}
				$('form:first *:input[type!=hidden]:first').focus();
			});
		</script>
	</head>
	<body>
		<div id="bestViewBrowser" style="display: none;"><spring:message code="generic.best.browser.viewed"/></div>
		<div id="mainDiv">
	  		<div id="loginDiv">
	    		<div id="toptitle"><img src="./images/nyt-new-logo.png" width="212" height="52" align="absmiddle"/></div>
	    			<div id="loginformDiv">
	     				<form name="frmLogin" action="<c:url value='/j_spring_security_check'/>" method="post">
					        <table width="100%" cellpadding="0" cellspacing="0">
					        	<tbody>
					            	<tr>
					              		<td colspan="2" class="labelBold"><spring:message code="message.generic.login"/></td>
					            	</tr>
					            	<tr>
					              		<td width="40%" class="LabelNormalright"><spring:message code="label.generic.userID"/>&nbsp;:&nbsp;</td>
					              		<td width="60%"><input class="textbox" type="text" name="j_username" id="j_username" style="padding: 1px 3px 1px 3px;" onkeypress="handleEnterKey(event)"/></td>
					            	</tr>
						            <tr>
						            	<td width="40%" class="LabelNormalright"><spring:message code="label.generic.password"/>&nbsp;:&nbsp;</td>
						            	<td width="60%"><input class="textbox" type="password" name="j_password" id="j_password" style="padding: 1px 3px 1px 3px;" onkeypress="handleEnterKey(event)" /></td>
						            </tr>
					            	<tr height="10">
					              		<td colspan="2" class="alert" id="alerts" style="visibility: hidden;">
				              				<div align="center" style="padding:0px; margin:0px"><spring:message code="message.generic.invalidCredential"/></div>
					              		</td>
					            	</tr>
					            	<tr>
					              		<td colspan="2" class="LabelNormalcenter" height="26">
						              		<input name="button" type="button" class="buttonGeneric" value="Login" onclick="LoginMe();"/>
						                	<input name="button" type="button" class="buttonGeneric"  value="Reset" onclick="resetMe();"/>
					                	</td>
					            	</tr>
					            	<tr>
					            		<td colspan="2" class="LabelNormalcenter ">
					            			<a href="#" onclick="showReservationCalendar()" class="login-link"><spring:message code="tab.generic.reservationCalenar"/></a>
					            		</td>
					            	</tr>
					            	<tr>
					              		<td colspan="2" height="3"></td>
					            	</tr>
					        	</tbody>
					        </table>
	      				</form>
	    			</div>
	    		<div id="bottomtitle"> &copy; <spring:message code="label.generic.NYTLoginFooter"/> </div>
	  		</div>
		</div>
	</body>
</html>
