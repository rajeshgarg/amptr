<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<script type="text/javascript" src="../js/product/manageProduct.js?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />"></script>

<div id="manageProductContainer">
	<table id="productTable" class="master"></table>
	<div id="productPager"></div>
	<div class="spacer7"></div>
	<div id="productTab">
		<ul>
			<li><a href="#product-tabs-1"><spring:message code="tab.generic.productDetails" /></a></li>
			<li><a href="#product-tabs-3"><spring:message code="tab.generic.manageAttribute" /></a></li>
			<li><a href="#product-tabs-4"><spring:message code="tab.generic.manageDocument" /></a></li>
		</ul>
		<div id="product-tabs-1"><jsp:include page="productDetail.jsp"></jsp:include></div>
		<div id="product-tabs-3">
			<div id="productAttributeContainer">
				<div id="messageHeaderDiv"></div>
				<table >
					<tr>
						<td><label class="label-bold">
							<spring:message code="label.generic.salesTarget"/>:
						</label></td>
						<td><select id="lineItemSalesTarget" name="lineItemSalesTarget" onchange="loadLineItemAttribute();"></select></td>
					</tr>
				</table>
				<div class="spacer7"></div>
				<div><table id="productAttributeTable" class="childGrid"></table></div>
				<div id="productAttributePager"></div>	
				<div id="product-attribute-form" style="display: none" title="Attribute Details"></div>				
			</div>
		</div>
		<div id="product-tabs-4"><div id="productAttachement"></div></div>
	</div>
	<div class="ui-jqgrid-titlebar-close" style="right: 0px; display: none" id="productSearchPanel">
		<select id="productSearchOption">
			<option value="productName"><spring:message code="label.generic.name"/></option>
			<option value="typeName"><spring:message code="label.generic.type"/></option>
			<option value="className"><spring:message code="label.generic.class"/></option>
			<option value="creativeName"><spring:message code="label.generic.creativeName"/></option>
		</select>
		<input id="productSearchValue" type="text" size="20" class="search-icon search-input"/>
	</div>
</div>
<script>
	$('.tip-tool[title]').qtip({position:{my:  'left center',at: 'right center'}, style: {classes: 'ui-tooltip-blue'} });
	$('.tip-toolright[title]').qtip({position:{my:  'right center',at: 'left center'},style: {classes: 'ui-tooltip-blue'}});
</script>