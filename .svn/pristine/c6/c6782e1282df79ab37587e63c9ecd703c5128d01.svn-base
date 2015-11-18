<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<form:form id="listRateDetail" name="listRateDetail" action="../manageRateProfile/saveRateProfile.action" method="post" modelAttribute="rateProfileForm">
	<div>
		<div id="messageHeaderDiv" class="error"></div>
		<table width="100%" cellpadding="2" cellspacing="2" border="0">
			<tr>
				<td width="5%" align="right"><span class="mandatory">*&nbsp;</span></td>
				<td width="10%"><label class="label-bold"><spring:message code="label.generic.product"/>:</label></td>
				<td width="30%">						
					<form:select path="productId" id="productId" cssStyle="width: 350px;">
						<form:option value=""></form:option>
						<c:forEach  items="${allProducts}"  var="entry" >
							<optgroup  label="${entry.key}"> 	
							<c:forEach  var="product" items="${entry.value}">
								<c:choose>
									<c:when test="${product.reservable == 'Y'}">
										<form:option data-type="R" value="${product.productId}">${product.displayName}</form:option>
									</c:when>
									<c:otherwise>
										<option data-type="S" value="${product.productId}">${product.displayName}</option>
									</c:otherwise>
								</c:choose>
							</c:forEach>	
							</optgroup>
						</c:forEach>							
					</form:select>
				</td>
				<td>
					<span id="rateCardNotRoundedContainer" style="display: none;">
						<form:checkbox style="vertical-align:-2px;" value="true"  path ="rateCardNotRounded" id="rateCardNotRounded" />
						<label class="label-bold"><spring:message code="label.rateProfile.ratecardRounded" /></label>
					</span>
				</td>
			</tr>
			<tr>
				<td align="right"><span class="mandatory">*&nbsp;</span></td>
				<td><label class="label-bold"><spring:message code="label.generic.salesTarget"/>:</label></td>
				<td colspan="2" class="multiselect-sales-target multiselect-sales-target-selectbox">
					<div id="salesTargetId_custom" class="multi-select-box" style="width:350px">  		 	      	
			 	      	<form:select path="salesTargetId" id="salesTargetId" multiple="multiple" cssStyle="width: 8%;" size="5"></form:select>
					</div>
				</td>
			</tr>
			<tr>
				<td align="right"><span class="mandatory">*&nbsp;</span></td>
				<td>
					<label class="label-bold">
						<spring:message code="label.pricingrule.price"/>:
					</label>
				</td>
				<td colspan="2" >
					<form:input path="basePrice" id="basePrice" cssClass="numericdecimal" maxlength="10" size="15" />
					<img src='../images/info-icon.png' class="tip-tool" title="<spring:message code='help.pricing.rule.price'/>" />
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td style="vertical-align: middle;">
					<label class="label-bold"><spring:message code="label.generic.note"/>:</label>
					<div id="charsRemaining" class="chars-remaining"></div>
				</td>
				<td colspan="2" >
					<form:textarea path="notes" rows="5" cols="100" id="notes"/>
					<img src='../images/info-icon.png' class="top-170 tip-toolright" title="<spring:message code='help.rateProfile.notes'/>" />
				</td>
			</tr>	
			<tr>
				<form:hidden path="profileId" id="profileId"/>	
				<form:hidden path="productName" id="productName"/>
				<form:hidden id="salesCategoryId" path="salesCategoryId"/>
				<form:hidden id="salesCategoryName" path="salesCategoryName"/>
			</tr>
			<form:hidden id="rateProfileDiscountData"  path="rateProfileDiscountData" />			
		</table>

		<div id="sheepItForm">
			<div id="seasonalDiscounTableContainer" class="seasonal-discount-table-container">
			<h3 id="seasonalDiscountHeader" class="normalHead"><label class="label-bold"><spring:message code="label.generic.rateProfile.seasonal.details"/></label></h3>
			<table width="100%" id="seasonalDiscounTable" class="availsSummary-line-table">
				<thead>
					<tr id="seasonalDiscountHead">
						<th style="width:5%;">
							<input type='checkbox' id="clone_seasonalDiscount" onClick="customDiscountSelect(this);">
						</th>
						<th style="width:10%;">
							<label class="label-bold"><spring:message code="label.rateProfile.seasonalDiscount.ruleNo" /></label>
						</th>
						<th style="width:10%;">
							<span class="mandatory"></span>
							<label class="label-bold"><spring:message code="label.rateProfile.seasonalDiscount.Not"/></label>
						</th>
						<th style="width:25%;">
							<span class="mandatory">*</span>
							<label class="label-bold"><spring:message code="label.generic.startDate" /></label>
						</th>
						<th style="width:25%;">
							<span class="mandatory">*</span>
							<label class="label-bold"><spring:message code="label.generic.endDate" /></label>
						</th>
						<th style="width:25%;">
							<span class="mandatory">*</span>
							<label class="label-bold"><spring:message code="label.rateProfile.seasonalDiscount"/></label>
						</th>
					</tr>
				</thead>
		        <tbody>
					<tr id="sheepItForm_template">
						<td>
							<span>
								<input type='checkbox' id="sheepItForm_#index#_clone_seasonalDiscount" name="sheepItForm_clone_seasonalDiscount" onclick="selectUnselectDiscount(this)">
							</span>
						</td>
						<td>
							<label id="sheepItForm_#index#_ruleNo" class="label-bold"></label>
						</td>
						<td>
							<span>
								<input type='checkbox' id="sheepItForm_#index#_not_seasonalDiscount" name="sheepItForm_#index#_not_seasonalDiscount">
							</span>
						</td>
						<td>
							<span>
								<input type="text" class="discountDates" id="sheepItForm_#index#_startDate" name="id=sheepItForm_#index#_startDate" readonly="true" style="width:50%;" class="input-textbox-readonly" tabindex="-1" onfocus="this.blur()" maxlength="13" />
								<a style='cursor:pointer;' id="sheepItForm_#index#_startReset">
									<span class="reset-decoration" style="vertical-align:-2px !important;" title='<spring:message code="label.generic.clear"/>'></span>
								</a>
							</span>
						</td>
						<td>
							<span>
								<input type="text" class="discountDates" id="sheepItForm_#index#_endDate" name="id=sheepItForm_#index#_endDate" readonly="true" style="width:50%;height:17px;" class="input-textbox-readonly" tabindex="-1" onfocus="this.blur()" maxlength="13" />
								<a style='cursor:pointer;' id="sheepItForm_#index#_endReset">
									<span class="reset-decoration" style="vertical-align:-2px !important;" title='<spring:message code="label.generic.clear"/>'></span>
								</a>
							</span>
						</td>
						<td>
							<input type="text" id="sheepItForm_#index#_seasonalDiscount" name="sheepItForm_#index#_seasonalDiscount" maxlength="6" size="11"/>
						</td>
						<td style="display:none;" id="sheepItForm_#index#_profileId"></td>
					</tr>
		          	<!-- No forms template -->
		          	<tr id="sheepItForm_noforms_template">
		            	<td colspan="6" class="no-forms-template">No Seasonal Discounts configured</td>
		          	</tr><!-- /No forms template-->
		        </tbody><!-- /sheepIt Form -->
			</table>
			<div class="table-bottom-bar">
				<div id="addDiscounts">
					<a href="javascript:void(0)" class="ui-icon ui-icon-plus" title="Add Discounts"></a>
				</div>	
				<div id="deleteDiscounts">
					<a href="javascript:void(0)" class="ui-icon ui-icon-trash" title="Delete Discounts"></a>
				</div>
				<div id="cloneDiscounts">
					<a href="javascript:void(0)" class="ui-icon ui-icon-clone" title="Clone Discounts"></a>
				</div>
			</div>
			</div>
    	</div>
		<!-- /sheepIt Form -->
		<div align="center">
			<table align="center" width="20%">
				<tr>
					<td><input type="button" value="SAVE" id="listRateProfileSaveData" class="save-btn"/></td>					
					<td><input type="button" value="RESET" id="listRateProfileResetData" class="reset-btn"/></td>
				</tr>	
				<tr>
					<td align="right">&nbsp;</td>
				</tr>
			</table>	
		</div>
   </div>
   <div id="cloneDiscountDetails" title="Clone Discount" style="display: none" >
		<div>
			<div id="selectedDiscountsTableContainer" class="seasonal-discount-table-container">
			<h3 class="normalHead"><label class="label-bold"><spring:message code="label.generic.rateProfile.selected.seasonal.details"/></label></h3>
			<table width="100%" id="selectedDiscountsTable" class="availsSummary-line-table">
				<thead>
					<tr id="selectedDiscountsTableHeader">
						<th style="width:10%;">
							<span class="mandatory"></span>
							<label class="label-bold"><spring:message code="label.rateProfile.seasonalDiscount.Not"/></label>
						</th>
						<th style="width:25%;">
							<span class="mandatory"></span>
							<label class="label-bold"><spring:message code="label.generic.startDate" /></label>
						</th>
						<th style="width:25%;">
							<span class="mandatory"></span>
							<label class="label-bold"><spring:message code="label.generic.endDate" /></label>
						</th>
						<th style="width:25%;">
							<span class="mandatory"></span>
							<label class="label-bold"><spring:message code="label.rateProfile.seasonalDiscount"/></label>
						</th>
					</tr>
				</thead>
			</table>
			</div>
		</div>
		<div class="spacer7"></div>
		<div>
			<div>
			<fieldset class="seasonal-discount-filter-container">
				<legend id="seasonalDiscountHeader"><spring:message code="label.generic.filters"/></legend>
				<p>
					<label class="label-bold margin-left-50"><spring:message code="label.generic.salesCategory" />: </label>
					<form:select path="discountSalesCategoryId" id="discountSalesCategoryId" cssStyle="width:30%;" >
						<form:option value=""><spring:message code="label.generic.default" /></form:option>
						<form:options items="${allSalesCategory}" />
					</form:select>
					<label class="label-bold margin-left-50">
						<spring:message code="label.generic.product"/>:
					</label>
					<form:select path="discountProductId" id="discountProductId" cssStyle="width:30%;">
						<form:option value=""></form:option>
						<c:forEach  items="${allProducts}"  var="entry" >
							<optgroup  label="${entry.key}"> 	
								<c:forEach  var="product" items="${entry.value}">
									<form:option value="${product.productId}">${product.displayName}</form:option>
								</c:forEach>	
							</optgroup>
						</c:forEach>
					</form:select>
				</p>
			</fieldset>
			</div>
			<div class="spacer7"></div>
		</div>	
   		<div id="cloneDiscountDataSummary"></div>
   </div>
</form:form>	
