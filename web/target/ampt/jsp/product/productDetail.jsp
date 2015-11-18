<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<form:form id="productDetail" name="productDetail" action="../manageProduct/saveProductDetails.action" method="post" modelAttribute="productDetailForm">
	<div id="containerProductDetail">
		<div id="messageHeaderDiv" class="error"></div>
		<table width="100%" cellpadding="2" cellspacing="2" class="basic-content-align productDetail-table">
			<tr>
				<td width="5%">&nbsp;
					<input type="hidden" id="productId" name="productId" />
					<form:input type="hidden" id="creativesLength" path="creativesLength"/>
				</td>
				<td width="10%">
					<label class="label-bold">
						<spring:message code="label.generic.name"/>:
					</label>
				</td>
				<td width="30%">
					<input type="text" size="40" readonly="readonly" class="input-textbox-readonly " name="productName" onfocus="this.blur()"/>
				</td>
				<td width="10%">
					<label class="label-bold">
						<spring:message code="label.generic.displayName"/>:
					</label>
				</td>
				<td width="30%">
					<input type="text" size="40" readonly="readonly" class="input-textbox-readonly" name="displayName" onfocus="this.blur()" />
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>
					<label class="label-bold">						
						<spring:message code="label.generic.description"/>:
					</label>
				</td>
				<td>
					<input type="text" size="40" name=" productDescription" readonly="readonly" class="input-textbox-readonly" onfocus="this.blur()" />
				</td>
				<td>
					<label class="label-bold">
						<spring:message code="label.generic.class"/>:
					</label>
				</td>
				<td>
					<input type="text" name="className" readonly="readonly" class="input-textbox-readonly" onfocus="this.blur()" />
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>
					<label class="label-bold">
						<spring:message code="label.generic.note"/>:
					</label>
				</td>
				<td>
					<input type="text" size="40" name="note" readonly="readonly" class="input-textbox-readonly" onfocus="this.blur()" />
				</td>
				<td>
					<label class="label-bold">
						<spring:message code="label.generic.type"/>:
					</label>
				</td>
				<td>
					<input type="text" name="typeName" readonly="readonly" class="input-textbox-readonly" onfocus="this.blur()" />
				</td>
			</tr>
			<tr>
				<td align="right"><span class="mandatory">&nbsp;</span></td>
				<td>
					<label class="label-bold" style="vertical-align: -5px;">
						<spring:message code="label.generic.productCreatives"/>:
					</label>
				</td>
				<td style="vertical-align: top;">
					<div id="creatives_custom" class="multi-select-box">
						<form:select id="creatives" path="creatives" multiple="true">
							<form:options items="${allCreatives}"></form:options>
						</form:select>
					</div>
					<a href="javaScript:reloadCreatives()" style="float:left; padding: 5px;">
						<img src="../images/reload.png" width="16px" height="16px" title="Reload Product Creatives"/>
					</a>
				</td>
				<td rowspan="3" style="vertical-align: top;">
					<label class="label-bold">
						<spring:message code="label.generic.salesTarget"/>:
					</label>
				</td>
				<td rowspan="3" style="vertical-align: top">
					<select class="salestarget-dropdown" name="salestarget" id="salestarget" size="6"></select>						
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				
			</tr>
			<tr>
				<td>&nbsp;</td>
						
			</tr>
		</table>
		<div align="center">
			<table align="center" width="20%">
				<tr>
					<td><input type="button" value="SAVE" id="productSaveData" class="save-btn"/></td>					
					<td><input type="button" value="RESET" id="productResetData" class="reset-btn"/></td>
				</tr>	
			</table>	
		</div>
	</div>
</form:form>	
