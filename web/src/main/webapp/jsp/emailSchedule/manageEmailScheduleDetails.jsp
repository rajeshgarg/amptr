<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<form:form id="emailSendScheduleDetail" name="emailSendScheduleDetail" action="../emailSchedule/saveEmailSendSchedule.action" modelAttribute="emailSendScheduleForm">
	<div id="messageHeaderDiv" class="error"></div>
	<span>
		<form:hidden path="scheduleId" id="scheduleId"/>
		<form:hidden path="scheduleDetailId" id="scheduleDetailId"/>
		<form:hidden path="salesTargetId" id="salesTargetId"/>
		<form:hidden path="productId" id="productId"/>
		<form:hidden path="productName" id="productName"/>
		<form:hidden path="salesTargetName" id="salesTargetName"/>
	</span>
	<div class="dates-left">
		<p>
			<span class="mandatory">*&nbsp;</span>
			<label class="label-bold"><spring:message code="label.generic.startDate"/>:</label>
			<span id="emailScheduleDate">
				<form:input id="emailStartDate" path="emailStartDate" readonly="true" cssClass="input-textbox-readonly" size="8" onfocus="this.blur()" />
				<a href="javascript:void(0)" id="startFromReset">
					<span class="reset-decoration" style="vertical-align:-2px !important;" title='<spring:message code="label.generic.clear"/>'></span>
				</a>
			</span>
		</p>
		<div class="clearboth"></div>
		<p style="padding-top:10px;">
			<span class="mandatory">*&nbsp;</span>
			<label class="label-bold"><spring:message code="label.generic.endDate"/>:</label>
			<input type="radio" value="forever" id="forever" name="ends" checked="checked" style="margin-left:4px;vertical-align:text-bottom;">Never
			<input type="radio" value="endOn" id="endOn" name="ends" style="margin-left: 12px;vertical-align: text-bottom;">On
		</p>
		<p style="padding-left:91px;padding-top:8px;">	
			<form:input id="emailEndDate" path="emailEndDate" readonly="true" cssClass="input-textbox-readonly" onfocus="this.blur()" size="8"/>
			<a href="javascript:void(0)" id="endFromReset">
				<span class="reset-decoration" style="vertical-align:-2px !important;" title='<spring:message code="label.generic.clear"/>'></span>
			</a>
		</p>
	</div>
	<div class="dates-right">
		<input type="checkbox" id="recurrence" name="recurrence">
		<fieldset id="recurrenceFieldset">
			<h3>Recurrence Schedule</h3>
			<table>
				<tr>
					<td>
						<form:select path="frequency" id="frequency">
							<form:options items="${frequency}" />
						</form:select>							
					</td>
					<td>
						<form:checkboxes path="weekDays" id="weekDays" items="${weekDays}"></form:checkboxes>
					</td>
				</tr>
			</table>
		</fieldset>
	</div>
	<div class="clearboth"></div>
	<div id="manageEmailSchedule_buttons">
		<input type="button" id="emailScheduleSaveData" value="SAVE" class="save-btn"/>
		<input type="button" id="emailScheduleResetData" value="RESET" class="reset-btn"/>
  	</div>
</form:form>