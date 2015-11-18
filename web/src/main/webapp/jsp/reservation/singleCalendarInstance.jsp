<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<style>#ui-datepicker-div{z-index:9 !important;}</style>
	<c:forEach  items="${randomIdArray}"  var="randomId" >
	<div class="calendar-box" id="calendarInstance${randomId}">
		<div class="multipleCal"> 
			<form:form id="multipleCalendarForm" name="multipleCalendarForm" action="" modelAttribute="reservationForm">
				<form:hidden id="targetingData"  path="targetingData" />
				<table cellpadding="2" cellspacing="8" style="width:100%">
					<tr>
						<td style="width:21%">
							<span class="mandatory">*</span>
							<label class="label-bold"><spring:message code="label.generic.product"/>:</label>
						</td>
						<td style="width:79%">
						  <div id="sosProductIdDiv">
							<form:select  id="sosProductId" path ="sosProductId" cssStyle="width: 100%;" onchange="setSalesTargetRandom(${randomId})">
								<form:option value=""></form:option>	
								<c:forEach  items="${allProducts}"  var="entry" >
									<optgroup  label="${entry.key}"> 	
									<c:forEach  var="product" items="${entry.value}">
										<form:option value="${product.productId}" data-productType="${product.productType}">${product.displayName}</form:option>
									</c:forEach>	
									</optgroup>
								</c:forEach>
							</form:select>
						  </div>
						</td>
					</tr>
					<tr>
						<td>
							<span class="mandatory">*</span>
							<label class="label-bold"><spring:message code="label.generic.salesTarget"/>:</label></td>
						<td>
						  <div id="sosSalesTargetIdDiv">
							<form:select id="sosSalesTargetId" path="sosSalesTargetId" Style="width: 100%;">
							</form:select>
						  </div>
						</td>
					</tr>						
					<tr>
						<td>
							<span class="mandatory">*</span>
							<label class="label-bold"><spring:message code="label.generic.startDate"/>:</label>
						</td>
						<td>
							<span id="flightDate">
								<input type="text" id="startDate${randomId}" style="width:20%;" tabindex="-1" onfocus="this.blur()" maxlength="13" readonly="readonly" class="input-textbox-readonly" />
								<a href="javascript:void(0)" id="startFromReset${randomId}" onclick="resetStartDate(${randomId})" class="reset-decoration" title='<spring:message code="label.generic.clear"/>'></a>
							</span>
						</td>
					</tr>
					<tr>
						<td>
							<span class="mandatory">*</span>
							<label class="label-bold"><spring:message code="label.generic.endDate"/>:</label>
						</td>
						<td>
							<span id="flightDate">
								<input type="text" id="endDate${randomId}" style="width:20%;" tabindex="-1" onfocus="this.blur()" maxlength="13" readonly="readonly" class="input-textbox-readonly" />
								<a href="javascript:void(0)" id="endFromReset${randomId}" onclick="resetEndDate(${randomId})" class="reset-decoration" title='<spring:message code="label.generic.clear"/>'></a>
							</span>
						</td>
					</tr>
				</table>
				<table style="width:100%; table-layout: auto" class="build-prop-targ ext">	
					<tbody>
						<tr>
							<td>
								<h3 class="normalHead"><label class="label-bold"><spring:message code="label.generic.targeting"/></label></h3>
							</td>
						</tr>
						<tr>
							<td>
								<table id="targetingSection" class="targeting-table" style="width:100%;" cellpadding="2" cellspacing="8" >
									<tbody>
										<tr>			
											<td style="width:17%;">
												<label class="label-bold"><spring:message code="label.generic.target.type"/>:</label>
											</td>
											<td style="width:82%;">
												<form:select  id="sosTarTypeId" path="sosTarTypeId" Style="width:50%" onchange="fillTargetTypeElement('',${randomId})">
													<form:option value=""><spring:message code="label.generic.blankSelectOption"/></form:option>
													<form:options items="${targetTypeCriteria}" />										
												</form:select>
											</td>
										</tr>
										<tr>	
											<td>
												<label class="label-bold">Elements:</label>
											</td>
											<td>
												<div id="sosTarTypeElement${randomId}_custom" class="multi-select-box" style="display:inline-block;width:70%;">
													<select id="sosTarTypeElement${randomId}" multiple="multiple" ></select>
												</div>							
												<input type="button" id="addTargetToLineItem" onclick="addTargets(${randomId})" class="btn-sec" value="ADD" />
												<input type="button" id="updateTargetToLineItem" class="btn-sec" value="UPDATE" style="display: none;" />
												<input type="button" id="targetResetData" value ="RESET" class="btn-sec" onclick="resetTargetingFields(${randomId})"/>
											</td>
										</tr>
									</tbody>
								</table>
							</td>
						</tr>
						<tr>							
							<td valign="top" align="left" style="vertical-align: top; padding-left: 2px;">
                                <div id="geoTargetsummaryContainer" class="geoTargetSize" style="display: none">
                                	<table cellspacing='2' cellpadding='2' width='100%' id="geoTargetsummary" style="table-layout: fixed;">
                                    	<tr id ="geoTargetsummaryHeader">                                                                      
                                        	<th width='17%'><spring:message code="label.generic.target.type"/></th>
                                            <th style="display: none;">Target Type Id</th>
                                            <th width='22%'><spring:message code="label.generic.TargetTypeElments"/></th>
                                            <th style="display: none;">Elements Id</th>
                                            <th width='10%' style="text-align:center;"><spring:message code="label.generic.action"/></th>                                              
                                        </tr>
                                     </table>
                                </div>
                            </td>
						</tr>
					</tbody>
				</table>
				<div class="sorDiv">
					<!-- <input id="sorbutton" class="save-btn" style="width:22%;" type="button" onclick="$('#sor').show();$('#calculateSOR').show()" value="View Sor"/> -->	
					<input type="text" id="sor" readonly="readonly" onfocus="this.blur()" class="input-textbox-readonly" style="width:75px;margin-right:8px;"></input>
					<a id="calculateSOR" class="anchor-link" onclick="calculateSORValue(${randomId});" style="display:inline-block;font-weight:700;">Calculate SOR</a>
					<input type="button" id="sorbutton" class="save-btn" value="View Calendar" onclick="viewCalander(${randomId});" />
				</div>
			</form:form>
			<div id="loader" style="display: none" class="main_loader_sec">
            	<div class="loader_block">                      
                <div class="loader_text"><spring:message code="label.generic.loader"/></div>
                </div>
           </div>
		</div>
		<div id="calendarDiv" class="calendarBlock"> <label class="label-bold"><spring:message code="label.view.calendar"/></label></div>
		<div id="calendarDetailDiv${randomId}" class="reservation-detail" style="display:none;width:94%;"></div>
		<span class="closeCal"><a title="Close Calendar" class="rem" onclick="removeSelectedCalendarDiv('calendarInstance${randomId}')"></a></span>
	</div>
</c:forEach>