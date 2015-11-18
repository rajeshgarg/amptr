<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div id=lineItemTargeting>
	<!--  Header  Div for error message -->
	<div id="messageHeaderDiv"></div>
	   		
	<!--  Header  Div for LineItem Exceptions -->
	<div id="lineItemExceptionsContainer" style="display: none;">
		<img id="exptimgId" src="<%= request.getContextPath() %>/images/minus.png" align="left" /> Line Item Exceptions
		<div id="lineItemExceShowhide">
			<ol id="lineItemExceptions" type="disc"></ol>
		</div>
	</div>
	<div class="line-item-type">
		<span class="item-std-reserve">
			<input type="radio" name="lineItemType" id="lineItemType_s" onclick="changeLineItemType('S', true)" checked="checked"></input>
			<label style="vertical-align:1px;" class="label-bold">Standard</label>
			<i class=""></i>
		</span>	
		<span  class="item-std-reserve">
			<input type="radio" name="lineItemType" id="lineItemType_r" onclick="changeLineItemType('R', true)"></input>
			<label style="vertical-align:1px;" class="label-bold">Reservable</label>
			<i class=""></i>
		</span>
		<span  class="item-std-reserve">
			<input type="radio" name="lineItemType" id="lineItemType_e" onclick="changeLineItemType('E', true)"></input>
			<label style="vertical-align:1px;" class="label-bold">Email</label>
			<i class=""></i>
		</span>
	</div>
	<table cellpadding="2" cellspacing="2" width="100%" border="0" class="basic-content-align table-bottom" style="table-layout:fixed;" id="lineItemfieldsContainer">
		<tr>
			<td width="1%"></td>
			<td width="14%"></td>
			<td width="12%"></td>
            <td width="8%"></td>
			<td width="12%"></td>
			<td width="2%"></td>
			<td width="2%"></td>
			<td width="15%"></td>
			<td width="12%"></td>
			<td width="8%"></td>
			<td width="7%"></td>
			<td width="5%"></td>
			<td width="2%"></td>
		</tr>
		<tr>
			<td width="1%"></td>
			<td width="14%">
				<span class="mandatory">* </span><label class="label-bold"><spring:message code="label.generic.product"/>:</label>
			</td>
			<td colspan="3" width="32%">						
				<form:select path="sosProductId" id="sosProductId" cssStyle="width:99%;">
					<form:option value=""></form:option>											
				</form:select>
			</td>
			<td width="2%">
				<i title='<spring:message code="label.generic.product.creatives.summary"/>'><img class="view-creative" id="productCreativeSummary" src="/ampt/css/images/view-creative.png" style="display: inline;" /></i>
			</td>
			<td width="2%"></td>
			<td width="15%">
				<span class="mandatory">* </span><label class="label-bold"><spring:message code="label.generic.salesTarget"/>:</label>
			</td>
			<td colspan="4" width="32%" class="multiselect-sales-target">							
				<div id="sosSalesTargetId_custom" class="multi-select-box">   
               		<form:select path="sosSalesTargetId" id="sosSalesTargetId" onfocus="getSosSalesTargetId()" multiple="multiple" cssStyle="width: 100%;" size="5"></form:select>
				</div>
			</td>
			<td width="2%">&nbsp;</td>
		</tr>					
		<tr>
			<td width="1%"></td>
			<td><span id="flightInfoText"><label class="label-bold">&nbsp;&nbsp;&nbsp;<spring:message code="label.generic.flight"/>:</label></span></td>	
			<td colspan="3">
				<span id="flightInfoText"><form:input path="flight" id="flight" maxlength="30" cssStyle="width: 97.6%;" /></span>
			</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>
				<span id="flightDate"><label id="flightStartDate" class="label-bold" style="padding-left: 10px;">&nbsp;&nbsp;&nbsp;<spring:message code="label.generic.startDate"/>:</label></span>
			</td>
			<td>
				<span id="flightDate">
					<form:input id="startDate" path="startDate" readonly="true" cssStyle="width: 70%;" cssClass="input-textbox-readonly" tabindex="-1" onfocus="this.blur()" maxlength="13" />
					<a style='cursor:pointer;' id="startFromReset">
						<span class="reset-decoration" style="vertical-align:-2px !important;" title='<spring:message code="label.generic.clear"/>'></span>
					</a>
				</span>
			</td>
			<td id="flightEndDateLabel" align="right" style="padding-right: 5px;" >
				<span id="flightDate">
					<label class="label-bold"><spring:message code="label.generic.endDate"/>:</label>
				</span>	
			</td>
			<td id="flightEndDate" colspan="2">
				<span id="flightDate">
					<form:input id="endDate" path="endDate" readonly="true" cssStyle="width: 70%;" cssClass="input-textbox-readonly" tabindex="-1" onfocus="this.blur()" maxlength="13" />
					<a style='cursor:pointer;' id="endFromReset">
						<span class="reset-decoration" style="vertical-align:-2px !important;" title='<spring:message code="label.generic.clear"/>'></span>
					</a>
				</span>
			</td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td colspan="1"></td>
			<td colspan="1" style="vertical-align: middle;">
				<span class="mandatory">* </span><label class="label-bold"><spring:message code="label.generic.placementName"/>:</label>
				<div id="charsRemainingPlacement" class="chars-remaining" style="padding-left:10px"></div>
			</td>	
			<td colspan="10">
				<form:textarea path="placementName" id="placementName" cssStyle="width:99.5%;" rows="2"></form:textarea>
			</td>	
			<td colspan="1" align="left" style="vertical-align:top;padding-top:9px !important;">
				<img src='<%= request.getContextPath() %>/images/info-icon.png' class="top-169 tip-toolright float-left" title="<spring:message code='help.attribute.placementname'/>" />
			</td>
		</tr>
	</table>
	<table class="build-prop-targ" style="width:100%; table-layout: auto" id="lineItemTargetingTable">	
		<tr id="geoTargetPricing">
			<td colspan="15">
				<h3 class="normalHead"><label class="label-bold"><spring:message code="label.generic.targeting"/></label></h3>
			</td>
		</tr>
		<tr>
			<td colspan="7">
				<table style="width:100%;" class="targeting-table">
					<tr>
						<td>
							<span class="mandatory">&nbsp;</span>
						</td>
						<td style="width:15%;">
							<label class="label-bold"><spring:message code="label.generic.target.type"/>:</label>
						</td>
						<td style="width:300px;">
							<form:select path="sosTarTypeId" id="sosTarTypeId" cssStyle="width:150px">
								<form:option value=""><spring:message code="label.generic.blankSelectOption"/></form:option>
							</form:select>
							<img src='<%= request.getContextPath() %>/images/info-icon.png' class="top-169 tip-tool" title="<spring:message code='help.lineItem.dfp.TargetingString'/>" style="margin-left:6px;"/>
						</td>
						<td>
							<span class="mandatory">&nbsp;</span>
						</td>
					</tr>
					<tr>	
						<td>
							<span class="mandatory">&nbsp;</span>
						</td>
						<td style="width:15%;">
							<label class="label-bold"><spring:message code="label.generic.TargetTypeElments"/>:</label>
						</td>
						<td>
							<div id="sosTarTypeElement_custom" class="multi-select-box" style="display:inline-block;width:305px;position:relative">
								<select id="sosTarTypeElement" multiple="multiple"></select>
							</div>
							<textarea style="display:none;width:299px;" rows="6" id="tarTypeElementText"></textarea>
						</td>
						<td>
							<input type="button" id="addTargetToLineItem" onclick="addTargetsToLineItem()" class="btn-sec" value="ADD" />
							<input type="button" id="updateTargetToLineItem" class="btn-sec" value="UPDATE" style="display: none;" />
							<input type="button" id="targetResetData" value ="RESET" class="btn-sec" onclick="resetTargetingFields()"/>
						</td>
					</tr>
					<tr>	
						<td>
							<span class="mandatory">&nbsp;</span>
						</td>
						<td style="width:15%;">
							<label class="label-bold"><spring:message code="label.generic.TargetString"/>:</label>
						</td>
						<td>
							<div id="targetingString" readonly="true" class="input-textbox-readonly"></div>							
						</td>
						<td style="text-align:left;vertical-align:top;" >
							<img src='<%= request.getContextPath() %>/images/info-icon.png' class="top-169 tip-tool" title="<spring:message code='help.lineItem.TargetingString'/>" style="margin-left:6px;"/>
						</td>
					</tr>
				</table>
			</td>
			<td colspan="8"  rowspan="4" valign="top" align="left" width="52%" style="vertical-align: top; padding-left: 2px; border-left: 1px solid #9FC1F8;">
				<div id="geoTargetsummaryContainer" class="geoTargetSize">
					<table cellspacing='2' cellpadding='2' width='100%' id="geoTargetsummary" style="table-layout: fixed;">
						<tr id ="geoTargetsummaryHeader">
							<th width='11%'>Exclude</th>
							<th width='17%'><spring:message code="label.generic.target.type"/></th>
							<th style="display: none;">Target Type Id</th>
							<th width='38%'><spring:message code="label.generic.TargetTypeElments"/></th>
							<th style="display: none;">Elements Id</th>
							<th width='8%'><spring:message code="label.generic.level"/></th>
							<th width='14%'><spring:message code="label.generic.Actions"/></th>
							<th width='12%' style="text-align:center;"><spring:message code="label.generic.action"/></th>							
						</tr>
					</table>
				</div>
			</td>
		</tr>
	</table>
	<table class="build-prop-targ" style="width:100%;text-align:left;padding-bottom:5px;" id="lineItemCalendarContainer" >	
		<tr>
			<td colspan="5">
				<h3 class="normalHead"><label class="label-bold">Reservation</label></h3>
			</td>
		</tr>
		<tr>
			<td style="padding-left:30px;width:17%">
				<a class="anchor-link" id="productSalesTargetValue" onclick="getProductSalesTargetDefaultValue();"><spring:message code="label.default.productsales" /></a>
			</td>
			<td style="width:26%">
				<span class="mandatory" id="sorMandatory">* </span>
				<label id="sorLabel" class="label-bold"><spring:message code="label.reservation.sor"/>:</label>	
				<input type="text" id="emailReservationStatus" style="display: none;width:75px;"  onfocus="this.blur()" tabindex="-1" class="input-textbox-readonly" />
				<form:input id="sor" path="sor" maxlength="6" cssStyle="width:16%;"></form:input>
				<input type="button" id="calculateSOR" class="button-get-avails" style="display:none;" onclick="calculateSORValue();" value='<spring:message code="label.calculate.sor" />' />
			</td>
			<td style="width:17%">
				<label id="viewCalendar" class="label-bold" style="padding-right:5px;">View Calendar:</label>				
				<img src='<%= request.getContextPath() %>/css/images/calendar-full.png' id="viewCalendar" class="view-calendar" onclick="viewCalander();" />
			</td>
			<td style="width:14%" class="hideinpackage" >
				<div id="reserveChkBoxContainer">
					<label class="label-bold" style="padding:0;"><spring:message code="label.reservation.reserve"/>:</label>
					<span id="reservedChkBoxSpan">
						<form:checkbox id="reserved" path="reserved" style="vertical-align:-2px;margin: 0;" onclick="fillExpirationDate();"/>
					</span>
				</div>
			</td>
			<td class="hideinpackage" style="width:26%" >
				<div id="reserveChkBoxContainer" class="reservation-expiry-date">
					<label class="label-bold" style="padding-right:3px;"><spring:message code="label.reservation.expiryDate"/>:</label>
					<form:input id="reservationExpiryDate" path="reservationExpiryDate" onfocus="this.blur()" tabindex="-1" cssClass="input-textbox-readonly" cssStyle="width:32%;" readonly="true"></form:input>
					<c:if test="${!readOnlyView}">
						<sec:authorize access="hasAnyRole('PLR','AOP','PAN', 'EPM')">
							<img src='<%= request.getContextPath() %>/images/renew-btn.png' id="renewReservation" style="display:none;position:relative;top:4px;" class="date-btn-alignment" onclick="renewExpirationDate();"/>
						</sec:authorize>
					</c:if>
					<img src='<%= request.getContextPath() %>/images/info-icon.png' class="tip-toolright" style="position:absolute;right:35px;margin-top:2px;" title="<spring:message code='help.reservation.renew'/>" />			
				</div>
				<input type="button" id="reqForReservation" style="display:none;float: right;margin:0px 50px -7px 0px"class="button-get-avails" onclick="reqAdminForReservation()" value='<spring:message code="label.request.for.reservation" />' />
			</td>
		</tr>
		<form:hidden path="sorExceededHidden" id="sorExceededHidden" value="false"/>
	</table>
	<table cellpadding="2" cellspacing="2" width="100%" border="0" class="build-prop-targ" style="table-layout:auto;" id="lineItemPricingContainer">	
		<tr id="geoPricing">
			<td colspan="7">
				<h3 class="normalHead"><label class="label-bold"><spring:message code="label.generic.rateCard"/></label></h3>
			</td>
		</tr>
		<tr>

			<td id="proposal_buttons" width="10%" style="vertical-align: middle;">&nbsp;&nbsp;<span class="mandatory">* </span><label class="label-bold"><spring:message code="label.pricingrule.RateCardPrice"/>:</label></td>
			<td id="proposal_buttons" style="padding: 8px 0 4px !important;" width="17%">
				<form:input id="rateCardPrice" path="rateCardPrice" cssClass="input-textbox-readonly" tabindex="-1" cssStyle="width:46%;" readonly="true"></form:input>
				<c:if test="${!readOnlyView}">
					<input type="button" id="calculateBasePrice" class="button-get-avails" value="Check Price" style="float:none;"/>
				</c:if>
			</td>
			<td style="vertical-align: middle;  width: 5%;">
				<label class="label-bold"><spring:message code="label.generic.viewability"/>:</label>
			</td>
			<td style="width: 12%;">
				<form:select id="isViewable" path="isViewable" style=" width: 82%;">
					<form:options items="${isViewableOptions}"></form:options>
				</form:select>
			</td>
			<td width="25%" id="pricingStatusInfo" style="display: none;">
				<label class="label-bold" style="width: 15%"><spring:message code='label.generic.pricingStatus'/>:&nbsp;&nbsp;</label>
				<form:input id="pricingStatus" path="pricingStatus" cssClass="input-textbox-readonly" tabindex="-1" cssStyle="width:53%;vertical-align:-1px;" readonly="true"></form:input>
				<span style="margin-left:6px;"><a href="javascript:void(0)" id="pricingStatusHelp" class="pricing-help" title="Pricing Rules" rel="pricingHelp.htm"></a></span>
			</td>
			
			<td width="15%" id="rateXInfo"  style="padding-left:28px; display: none;" >
				<label class="label-bold" id="ratexLable"><spring:message code='label.generic.ratex'/>:</label>
				<form:checkbox id="rateX" path="rateX" value="false" disabled="true" style="vertical-align:-1px;" /> 
				<img id="ratex_help" src='<%= request.getContextPath() %>/images/info-icon.png' class="tip-tool" title="<spring:message code='help.pricing.ratex'/>" />
			</td>
			<td id="proposal_buttons" class="sales-target-column" style="vertical-align:middle;" width="15%">
				<a class="help-link" id="pricingCalculationStep" title='<spring:message code="label.pricing.calculation.step"/>'>
					<spring:message code="label.pricing.calculation.step" /> 
				</a>
				<form:hidden path="priceCalSummary" id="priceCalSummary" />
			</td>
		</tr>
	</table>
	<table cellpadding="2" cellspacing="2" width="100%" border="0" class="build-prop-targ" style="table-layout:auto;" id="lineItemFinancialFieldsContainer">
		<tr id="lineItemFinancialDetails">
			<td colspan="22">
				<h3 class="normalHead"><label class="label-bold"><spring:message code="label.generic.financial.details"/></label></h3>
			</td>
		</tr>
		<tr id="lineItemFinancialDetails">
			<td colspan="22">
				<div id="messageHeaderFinancialDiv"></div>
			</td>
		</tr>
		<tr>						
			<form:hidden path="packageId" id="packageId" />
			<form:hidden path="targetingString" id="targetingStringValue" />
			<form:hidden path="productName" id="productName"/>
			<form:hidden path="sosSalesTargetName" id="sosSalesTargetName"/>
			<form:hidden path="displayFlight" id="displayFlight"/>
			<form:hidden path="lineItemID" id="lineItemID"/>
			<form:hidden path="version" id="version"/>	
			<form:hidden path="proposalID" id="proposalID"  />
			<form:hidden id="optionId" path="optionId" />
			<form:hidden id="lineItemTargetingData"  path="lineItemTargetingData" />
			<form:hidden path="product_active" id="product_active" />	
			<form:hidden path="salesTarget_active" id="salesTarget_active" />
			<form:hidden path="productType" id="productType" />
			<form:hidden path="sosProductClassName" id="sosProductClassName" />

			<form:hidden path="gridLineItemIds" id="gridLineItemIds"/>
			<td colspan="1" style="width:2%" align="right">
				<span class="mandatory">*</span>	 
			</td>
			<td colspan="2" width="10%">
				<span id="ImpressionsCountInfo">	
					<label class="label-bold">
					<spring:message code='label.generic.sequenceNo'/>:
				</label>
				</span>
			</td>
			<td colspan="6" width="18%">
				<span id="ImpressionsCountInfo">
					<form:input cssStyle="width: 97.6%;" path="lineItemSequence" id="lineItemSequence" readonly="true" cssClass="input-textbox-readonly" />
				</span>
			</td>
			<td colspan="2" align="right"><span class="mandatory">*</span></td>
            <td colspan="2" width="13%"  align="left" class="pad-right-3">
            	<label class="label-bold">
               		<spring:message code="label.generic.spec.type"/>:
               	</label>
           	</td>
			<td width="18%" colspan="2" class="multiselect-spectype" style="vertical-align:top;">							
				<div id="specType_custom" class="multi-select-box" style="width:94%;float:left;">   
                   	<form:select path="specType" id="specType"  multiple="multiple" size="5" >
                   		<form:options items="${lineItemSpecType}" />
                   	</form:select>
				</div>	
			</td>
			<td width="2%">&nbsp;</td>
			<td width="1%">&nbsp;</td>		
			<td width="14%" colspan="2" class="pad-right-3">
				<label class="label-bold"><spring:message code="label.generic.priceType"/>:</label>
			</td>	
			<td width="18%" colspan="2">
				<form:select id="priceType" path="priceType" cssStyle="width: 97%" onchange="calculateDiscountPercent();">
					<form:options items="${allPriceType}" />
				</form:select>
			</td>	
			<td width="2%">&nbsp;</td>			
		</tr>
		<tr>
			<td align="right"><span id="tr_rate" class="mandatory">*</span></td>
			<td colspan="2"><label id="tr_rate" class="label-bold"><spring:message code="label.generic.netCPM"/>:</label></td>	
			<td colspan="6">
				<span id="tr_rate">
					<form:input path="rate" maxlength="10" id="rate" cssClass="numericdecimal" cssStyle="width: 97.6%;" onblur="calculateDiscountPercent()" />
				</span>
			</td>
			<td width="1%">
				<img id="tr_rate" src='<%= request.getContextPath() %>/images/info-icon.png' class="tip-tool" title="<spring:message code='help.pricing.rule.price'/>" />
			</td>
			<td align="right" width="1%"><span class="mandatory">*</span></td>
			<td colspan="2">
				<span id="ImpressionsCountInfo">	
					<label id="ImpressionsCountLabel" class="label-bold"><spring:message code="label.generic.impressionTotal"/>:</label>
				</span>
			</td>
			<td colspan="2" >
				<span id="ImpressionsCountInfo">
					<form:input path="impressionTotal" cssClass="numeric" id="impressionTotal"  cssStyle="width: 94%;" />
				</span>				
			</td>
			<td><a href="javascript:void(0)" class="help-link" style="display:none;" id="offImpressionLink"><img src='<%= request.getContextPath() %>/images/impression_icon.png' class="tip-tool" style="cursor:pointer !important" /></a></td>
			<td width="1%" align="right"><span class="mandatory">*</span></td>	
			<td colspan="2" class="pad-right-3">
				<label class="label-bold"><spring:message code="label.generic.totalInvestment"/>:</label>
			</td>	
			<td colspan="2">
				<form:input path="totalInvestment" maxlength="16" id="totalInvestment" cssClass="numericdecimal"  cssStyle="width: 94%;" />
			</td>	
			<td align="left" style="vertical-align:top;margin-left:0px !important;padding: 7px 0 0 0 !important;">
				<img src='<%= request.getContextPath() %>/images/info-icon.png' class="tip-toolright" title="<spring:message code='help.pricing.rule.price'/>" />
			</td>
		</tr>
		<tr id="availsDtls">
			<td align="right"><span>&nbsp;</span></td>
			<td colspan="2"><label class="label-bold"><spring:message code="label.generic.avails"/>:</label></td>	
			<td colspan="2">
				<form:input path="avails" maxlength="12" id="avails" readonly="true" cssClass="input-textbox-readonly" cssStyle="width:94%;" tabindex="-1" />
				<div id="availProgress" style="display: none" ></div>
			</td>
			<td colspan="4" align="left" width="10%">
				<span style="vertical-align:text-bottom">
					<input id="availsCheck" class="button-get-avails" type="button" value="Fetch Avails" style="margin-left:2px;"/>
					<a id="clearAvails" style="display:inline;vertical-align:middle;cursor:pointer"><img src='<%= request.getContextPath() %>/images/resetP.png' class="reset-decoration clear-align-2" title="<spring:message code='help.clear.avails'/>" /></a>
				</span>
			</td>	
			<td colspan="2">&nbsp;</td>			
			<td colspan="2"><label class="label-bold"><spring:message code="label.generic.availsPopulatedDate"/>:</label></td>	
			<td colspan="2">
				<form:input path="availsPopulatedDate" maxlength="10" id="availsPopulatedDate" readonly="true" cssClass="input-textbox-readonly" cssStyle="width: 94%;" tabindex="-1" />
			</td>
			<td>&nbsp;</td>
			<td width="1%">&nbsp;</td>	
			<td colspan="2" style="vertical-align:middle;" class="pad-right-3">
				<span id="ImpressionsCountSOV">	
					<label class="label-bold"><spring:message code="label.generic.totalOfferedImpressions"/>:</label>
				</span>	
			</td>	
			<td colspan="2">
				<span id="ImpressionsCountSOV">
					<form:input path="totalPossibleImpressions" maxlength="12" id="totalPossibleImpressions" readonly="true" cssClass="input-textbox-readonly"  tabindex="-1" cssStyle="width: 94%;" />
				</span>	
			</td >
			<td colspan="1" style="vertical-align:middle;"></td>
		</tr>
		<tr>
			<td colspan="1" align="right">&nbsp;</td>
			<td colspan="2" style="vertical-align: middle;" >
				<label id="hideOnReservation" class="label-bold" ><spring:message code="label.generic.sov"/>:</label>
			</td>
			<td colspan="6" >
				<form:input  path="sov" maxlength="6" id="sov" cssClass="numericdecimal" cssStyle="width: 97.6% !important;"   />
			</td>
			<td colspan="1" align="left" style="vertical-align:top;padding: 7px 0 0 0 !important;" >
				<img id="sovtooltip" src='<%= request.getContextPath() %>/images/info-icon.png' class="tip-tool" title="<spring:message code='help.pricing.rule.price'/>" />
			</td>
			<td colspan="1" align="right"><span>&nbsp;&nbsp;</span></td>
			<td colspan="2"><label class="label-bold"><spring:message code="label.generic.discountPercentage"/>:</label></td>	
			<td colspan="2">
				<input id="discountPercentage" type="text" readonly="true" class="input-textbox-readonly" style="width: 94%;" tabindex="-1"/>
			</td>
			<td align="left" style="vertical-align:top;margin-left:0px !important;padding: 7px 0 0 0 !important;">
				<img src='<%= request.getContextPath() %>/images/info-icon.png' class="tip-tool" title="<spring:message code='help.discount.percentage'/>" />
			</td>
			<td colspan="1" align="right"><span>&nbsp;&nbsp;</span></td>
			<td colspan="2"><label class="label-bold"><spring:message code="label.generic.lineitemid"/>:</label></td>	
			<td colspan="2">
				<input id="lineItemIdVal" type="text" readonly="true" class="input-textbox-readonly" style="width: 94% !important;" tabindex="-1"/>
			</td>
			<td colspan="5" >
				&nbsp;
			</td>
		</tr>
		<tr id="lineItemSosId" style="display:none;">
			<td colspan="1" align="right"><span>&nbsp;&nbsp;</span></td>
			<td colspan="2"><label class="label-bold"><spring:message code="label.generic.packageName"/>:</label></td>	
			<td colspan="6">
				<form:input path="packageName" id="lineItemPackageName" type="text" readonly="true" class="input-textbox-readonly" cssStyle="width: 97.6% !important;" tabindex="-1"/>
			</td>
			<td colspan="2" align="right"><span>&nbsp;&nbsp;</span></td>
			<td colspan="2"><label id="tr_sosLineItemId" class="label-bold"><spring:message code="label.lineItem.sosId"/>:</label></td>	
			<td colspan="2">
				<span id="tr_sosLineItemId">
					<form:input path="sosLineItemID" id="sosLineItemIdVal" type="text" readonly="true" class="input-textbox-readonly" cssStyle="width: 94% !important;" tabindex="-1"/>
				</span>
			</td>
			<td colspan="15" >
				&nbsp;
			</td>
			
		</tr>				
		<tr>			
			<td colspan="1" align="right">&nbsp;</td>
			<td colspan="2" style="vertical-align: middle;">
				<label class="label-bold"><spring:message code="label.generic.comments"/>:</label>
				<div id="charsRemainingComments" class="chars-remaining"></div>
			</td>	
			<td width="17%" colspan="18">
				<form:textarea path="comments" id="comments" cssStyle="width: 99%;" rows="2" />
			</td>			
			<td colspan="1" align="left" style="vertical-align:top;padding: 7px 0 0 0 !important;">
				<img src='<%= request.getContextPath() %>/images/info-icon.png' style="margin-left:0px" class="tip-toolright" title="<spring:message code='help.attribute.description'/>" />
			</td>			
		</tr>
	</table>	
	<input type="hidden" id="flagForOnChange" value="true" />
	<input type="hidden" id="oldInvestment" value="" />
	<select  id="allProducts" style="display: none;">
		<c:forEach  items="${allProducts}"  var="entry" >
			<optgroup  label="${entry.key}"> 	
				<c:forEach  var="product" items="${entry.value}">
					<c:choose>
						<c:when test="${product.reservable == 'Y'}">
							<option is-viewable="${product.isViewable}" data-type="R" value="${product.productId}">${product.displayName}</option>
						</c:when>
						<c:when test="${product.reservable == 'E'}">
							<option is-viewable="${product.isViewable}" data-type="E" value="${product.productId}">${product.displayName}</option>
						</c:when>
						<c:otherwise>
							<option is-viewable="${product.isViewable}" data-type="S" value="${product.productId}">${product.displayName}</option>
						</c:otherwise>
					</c:choose>
				</c:forEach>	
			</optgroup>
		</c:forEach>
	</select>
	<select  id="targetTypeCriteria" style="display: none;">
		<c:forEach var="tarType" items="${targetTypeCriteria}" varStatus="loop">
			<c:if test="${tarType.key == 5 || tarType.key == 8 || tarType.key == 40 || tarType.key == 35}">
				<option data-type="R" value="${tarType.key}">${tarType.value}</option>
			</c:if>
			<c:if test="${tarType.key == 1 || tarType.key == 8 || tarType.key == 5 || tarType.key == 6 || tarType.key == 35 || tarType.key == 2 || tarType.key == 4 || tarType.key == 3 || tarType.key == 7}">
				<option data-type="E" value="${tarType.key}">${tarType.value}</option>
			</c:if>
			<option data-type="S" value="${tarType.key}">${tarType.value}</option>			
		</c:forEach>	
	</select>
	
	<div id="chooseGoalSeek" title="Confirmation" style="display: none;">
		<table>
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td colspan="2"><spring:message	code="help.goal.seek.confirmation.message" /></td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td width="8%" style="padding:2px "><input type="radio" name="goalSeekValue" id="goalSeekValue" value="impressionTotal"></td>
				<td style="vertical-align:middle"><spring:message code="label.generic.impressionTotal" /></td>
			</tr>
			<tr>
				<td style="padding:2px "><input type="radio" name="goalSeekValue" id="goalSeekValue" value="rate"></td>
				<td style="vertical-align:middle"><spring:message code="label.generic.priceType.CPM.VALUE" /></td>
			</tr>
		</table>
	</div>
</div>
<div id="arrangeSequence" style="display: none;">
	<div class="custom-tooltip">
		<p>
			<input type="radio" name="moveupdown" checked="checked" id="moveUp"><label class="label-bold move-up">Up &uarr;</label>
			<input type="radio" name="moveupdown" id="moveDown"><label class="label-bold move-down">Down &darr;</label>
		</p>
		<p>	
			<input type="text" placeholder="2 Places" id="movePlacesTxt" name="movePlacesTxt" maxlength="4" class="numeric" title="Only integer value allowed"/>
			<input type="button" id="movePlacesBtn" class="button-get-avails" value="Move">
		</p>
		<p>
			<a href="javascript:void(0)" class="avail-status-helplink" id="moveToTop">Move to Top</a>
			<span class="link-divider"></span>
			<a href="javascript:void(0)" class="avail-status-helplink" id="moveToBottom">Move to Bottom</a>
		</p>
	</div>
</div>
<script>
	$(document).ready(function () {
		$('.tip-tool[title]').qtip({position:{my:  'left center',at: 'right center'}, style: {classes: 'ui-tooltip-blue'} });
		$('.tip-toolright[title]').qtip({position:{my:  'right center',at: 'left center'},style: {classes: 'ui-tooltip-blue'}});
	});
</script>