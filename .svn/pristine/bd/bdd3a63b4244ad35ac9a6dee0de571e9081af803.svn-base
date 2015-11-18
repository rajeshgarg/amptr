<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<script type="text/javascript" src="../js/avails/fetchAvails.js?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />"></script>

<div id="manageFetchAvails">
	<form:form action="../avails/fetchAvails.action" id="manageFetchAvailsForm" name="manageFetchAvailsForm" method="post" modelAttribute="fetchAvailsForm">
		<div id="messageHeaderDiv" class="error" style="margin-left:25px;margin-top:12px;"></div>
		
		<div class="avail-system-type">
			<span class="item-std-reserve">
				<input type="radio" name="availSystemType" id="availSystemType_r" onclick="changeAvailSystemType('R', true)" checked="checked"></input>
				<label style="vertical-align:1px;" class="label-bold">ADX</label>
				<i class=""></i>
			</span>	
			<span  class="item-std-reserve">
				<input type="radio" name="availSystemType" id="availSystemType_d" onclick="changeAvailSystemType('D', true)"></input>
				<label style="vertical-align:1px;" class="label-bold">DFP</label>
				<i class=""></i>
			</span>
			<span  class="item-std-reserve">
				<input type="radio" name="availSystemType" id="availSystemType_b" onclick="changeAvailSystemType('B', true)"></input>
				<label style="vertical-align:1px;" class="label-bold">Both</label>
				<i class=""></i>
			</span>
		</div>		
		
		<table cellpadding="2" cellspacing="2" width="90%" border="0" class="basic-content-align table-bottom" style="table-layout:fixed;" id="fetchAvailsfieldsContainer">
			<tr>
				<td width="5%" align="right"><span class="mandatory">*</span></td>
				<td width="10%">
					<label class="label-bold"><spring:message code="label.generic.product"/>:</label>
				</td>
				<td width="30%">						
					<form:select path="sosProductId" id="fetchAvailsSOSProductId" cssStyle="width:99%;">
						<form:option value=""></form:option>
					</form:select>
				</td>
				<td width="5%" align="right"><span class="mandatory">*</span></td>
				<td width="13%">
					<label class="label-bold"><spring:message code="label.generic.salesTarget"/>:</label>
				</td>
				<td width="30%" class="multiselect-sales-target">							
					<div id="fetchAvailsSOSSalesTargetId_custom" class="multi-select-box">   
	               		<form:select path="sosSalesTargetId" id="fetchAvailsSOSSalesTargetId" multiple="multiple" cssStyle="width: 99%;"></form:select>
					</div>
				</td>
			</tr>		
			<tr>
				<td align="right"><span class="mandatory">*</span></td>
				<td>
					<label class="label-bold"><spring:message code="label.generic.startDate"/>:</label>
				</td>
				<td>
					<span id="flightDate">
						<form:input id="fetchAvailsStartDate" path="startDate" readonly="true" cssClass="input-textbox-readonly" onfocus="this.blur()" maxlength="13" />
						<a style='cursor:pointer;' id="startFromReset">
							<span class="reset-decoration" style="vertical-align:-2px !important;" title='<spring:message code="label.generic.clear"/>'></span>
						</a>
					</span>
				</td>
				<td align="right"><span class="mandatory">*</span></td>
				<td>
					<label class="label-bold"><spring:message code="label.generic.endDate"/>:</label>
				</td>
				<td>
					<span id="flightDate">
						<form:input id="fetchAvailsEndDate" path="endDate" readonly="true" cssClass="input-textbox-readonly" onfocus="this.blur()" maxlength="13" />
						<a style='cursor:pointer;' id="endFromReset">
							<span class="reset-decoration" style="vertical-align:-2px !important;" title='<spring:message code="label.generic.clear"/>'></span>
						</a>
					</span>
				</td>
			</tr>
			<tr id="dfpRow" style="display: none;">
				<td align="right"><span class="mandatory">*</span></td>
				<td>
					<label class="label-bold"><spring:message code="label.generic.priceType"/>:</label>
				</td>
				<td>
					<form:select id="fetchAvailspriceType" path="priceType" cssStyle="width: 99%">
						<form:options items="${allPriceType}" />
					</form:select>
				</td>
			</tr>
		</table>
		<select  id="allFetchAvailsProducts" style="display: none;">
		<c:forEach  items="${allProducts}"  var="entry" >
			<optgroup  label="${entry.key}"> 	
				<c:forEach  var="product" items="${entry.value}">
					<c:choose>
						<c:when test="${product.typeName == 'DFP'}">
							<option data-type="D" data-reserveable="${product.reservable}" value="${product.productId}">${product.displayName}</option>
						</c:when>
						<c:when test="${product.typeName == 'BOTH'}">
							<option data-type="B" data-reserveable="${product.reservable}" value="${product.productId}">${product.displayName}</option>
						</c:when>
						<c:otherwise>
							<option data-type="R" data-reserveable="${product.reservable}" value="${product.productId}">${product.displayName}</option>
						</c:otherwise>
					</c:choose>
				</c:forEach>	
			</optgroup>
		</c:forEach>
	</select>
	<form:hidden path="productType" id="productAvailType" value="" />
	<form:hidden path="type" id="availType" value="" />
		<table class="build-prop-targ" style="width:100%; table-layout: auto" id="fetchAvailsTargetingTable">	
			<tr id="geoTargetPricing">
				<td colspan="2">
					<h3 class="normalHead"><label class="label-bold"><spring:message code="label.generic.targeting"/></label></h3>
				</td>
			</tr>
			<tr>
				<td>
					<table style="width:100%;" class="targeting-table">
						<tr>
							<td><span>&nbsp;</span>
							</td>
							<td style="width:15%;">
								<label class="label-bold"><spring:message code="label.generic.target.type"/>:</label>
							</td>
							<td style="width:60%;">
								<form:select path="sosTarTypeId" id="fetchAvailsSOSTarTypeId" cssStyle="width:40%">
									<form:option value=""><spring:message code="label.generic.blankSelectOption"/></form:option>
									<form:options items="${targetTypeCriteria}" />		
								</form:select>
								<img id="infoIconFetchAvails" src='<%= request.getContextPath() %>/images/info-icon.png' class="top-169 tip-tool" title="<spring:message code='help.lineItem.dfp.TargetingString'/>" style="margin-left:6px;"/>
							</td>
							<td><span>&nbsp;</span></td>
						</tr>
						<tr>	
							<td><span>&nbsp;</span></td>
							<td>
								<label class="label-bold"><spring:message code="label.generic.TargetTypeElments"/>:</label>
							</td>
							<td>
								<div id="fetchAvailsSOSTarTypeElement_custom" class="multi-select-box" style="display:inline-block;width:100%;">
									<select id="fetchAvailsSOSTarTypeElement" multiple="multiple"></select>
								</div>
								<textarea style="display:none;width:98%;" rows="6" cols="32" id="fetchAvailsTarTypeElementText"></textarea>
							</td>
							<td>
								<input type="button" id="addTargetToFetchAvails" onclick="addTargetsToManageFetchAvails()" class="btn-sec" value="ADD" />
								<input type="button" id="updateTargetToFetchAvails" class="btn-sec" value="UPDATE" style="display: none;" />
								<input type="button" id="fetchAvailsTargetResetData" value ="RESET" class="btn-sec" onclick="resetTargetingFieldsManageFetchAvails()"/>
							</td>
						</tr>
					</table>
				</td>
				<td rowspan="4" valign="top" align="left" width="52%" style="vertical-align: top; padding-left: 2px; border-left: 1px solid #9FC1F8;">
					<div id="geoTargetsummaryContainer" class="geoTargetSize">
						<table cellspacing='2' cellpadding='2' width='100%' id="fetchAvailsGeoTargetsummary" style="table-layout: fixed;">
							<tr id ="geoTargetsummaryHeader">
								<th width='11%'>Exclude</th>
								<th width='17%'><spring:message code="label.generic.target.type"/></th>
								<th style="display: none;">Target Type Id</th>
								<th width='42%'><spring:message code="label.generic.TargetTypeElments"/></th>
								<th style="display: none;">Elements Id</th>
								<th width='8%'><spring:message code="label.generic.level"/></th>
								<th width='12%'><spring:message code="label.generic.Actions"/></th>
								<th width='10%' style="text-align:center;"><spring:message code="label.generic.action"/></th>							
							</tr>
						</table>
					</div>
				</td>
			</tr>
		</table>
		<table width="100%">
			<tr>
				<td align="center">
					<span style="vertical-align:text-bottom">
						<input id="manageFetchAvailsCheck" class="save-btn" type="button" value="Fetch Avails" />
						<input id="manageFetchAvailsReset" class="save-btn" type="button" value="Reset" />
					</span>					
				</td>	
			</tr>
		</table>
		<table id="yieldexError" width="100%">
			<tr>
				<td>
					<div id="fetchAvailsHeaderDiv"></div>
				</td>
			</tr>
		</table>
		<div id="consolidatedDetails" style="display: none;">
			<table id="consolidatedAvailsList" class="template-upload-doc-table" cellpadding="0" cellspacing="0" style="width:100%;margin:10px 0 0 0;"></table>
		</div>
		<table id="viewRowId" class="fetchAvtop" style="display: none;" width="40%">
			<tr>
				<td width="25%">
					<input id="radio_summary" type="radio" name="availsView" value="summary" onchange="generateView()" /><b>Summary</b>
				</td>
				<td width="25%">
					<input id="radio_day" type="radio" name="availsView" value="day" onchange="generateView()" /><b>Day View</b>
				</td>
				<td width="25%">
					<input id="radio_week" type="radio" name="availsView" value="week" onchange="generateView()" /><b>Week View</b>
				</td>
				<td width="25%">
					<input id="radio_month" type="radio" name="availsView" value="month" onchange="generateView()" /><b>Month View</b>
				</td>
			</tr>	
		</table>
		<table id="searchAvailsList" class="template-upload-doc-table" cellpadding="0" cellspacing="0" style="width:100%;margin:10px 0 0 0;"></table>
		<table id="yieldexURLTB" class="fetchAvbot" style="margin-top:25px; display: none;">
			<tr>
				<td style="padding-right:12px;"><b class="nowrap">Yieldex URL:-</b></td>
				<td id="yieldexURL"></td>
			</tr>
		</table>
		<div id="dfpDetails" style="display: none;">
			<table id="searchDfpAvailsList" class="template-upload-doc-table" cellpadding="0" cellspacing="0" style="width:100%;margin:10px 0 0 0;"></table>
			<br></br>
			<span id="dfpInputJson" title="DFP Inputs"></span>
		</div>
		
		<form:hidden id="fetchAvailsTargetingData"  path="lineItemTargetingData" />
		<div id="manageFetchAvailsProgress" style="display: none" ></div>
	</form:form>	
</div>

<script>
	$(document).ready(function () {
		$('.tip-tool[title]').qtip({position:{my:  'left center',at: 'right center'}, style: {classes: 'ui-tooltip-blue'} });
		$('.tip-toolright[title]').qtip({position:{my:  'right center',at: 'left center'},style: {classes: 'ui-tooltip-blue'}});
	});
</script>