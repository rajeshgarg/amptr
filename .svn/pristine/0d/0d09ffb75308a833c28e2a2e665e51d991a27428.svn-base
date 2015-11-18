<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
	<head>
		<title>Health Check Test</title>
		<style type="text/css">
			
			html, body, div, img, dl, dt {
				margin: 0; padding: 0; border: 0; outline: 0; font-size: 100%; vertical-align: baseline; background: transparent;
			}
			
			img {
				vertical-align: text-bottom !important;
			}
			
			.header {
				background: #053294 url(./images/header-banner.png) no-repeat -185px 0;	height: 35px !important;border-bottom: 5px solid #4A84CE;
			}
			
			.status {
				display: block;	width: 160px; margin: 150px auto; border: 1px groove #ccc; padding: 60px; border-radius: 10px; background: #efefef;
			}

			.clear {
				clear: both
			}
			
			.logo {
					background: url("./images/logo-min.png") no-repeat scroll 12px 11px transparent; float: left; margin-top: -7px; padding: 16px 36px 24px 9px; padding: 3px 36px 39px 9px\9;
			}
		</style>
	</head>
	<body>
		<div class="header">
				<dl>
					<dt>
						<div class="logo"></div>
					</dt>
				</dl>
			</div>
		<div class="clear"></div>
		<div id="healthStatus" class="status">
			<c:choose>
				<c:when test="${isApplicationLive == true}">
					<h2>AMPT is alive.</h2>
				</c:when>
				<c:otherwise>
					<h2>AMPT is dead.</h2>
				</c:otherwise>
			</c:choose>
		</div>
	</body>
</html>