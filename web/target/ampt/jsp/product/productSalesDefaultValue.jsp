<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
	<head>
	</head>
	<body>
		<table class="line-item-tooltip-table">
			<c:forEach var="attributes" items="${attributesMap}" varStatus="loop">
				<tr>
					<td width="45%">${attributes.key}</td>
					<td width="55%">${attributes.value}</td>
				</tr>		
			</c:forEach>			
		</table>
	</body>
</html>