<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<form:form action="../manageusers/saveuser.action" id="userDetailForm" name="userDetailForm" modelAttribute="userForm" method="post">
	<div id="userDetailFormDivIdentifier">
		<ul>
			<li id="userDetailFormTab-1"><a href="#manageuser-tabs-1">
				<spring:message code="tab.generic.userDetails"/></a>
			</li>
			<li id="userDetailFormTab-2"><a href="#manageuser-tabs-2">
				<spring:message code="tab.generic.addressDetail"/></a>
			</li>
		</ul>
		<div id="manageuser-tabs-1">
			<div id="messageHeaderDiv" class="error"></div>
			<fieldset>
				<legend class="label-bold"><spring:message code="label.generic.personalInfo"/></legend>
				<table width="100%" cellpadding="2" cellspacing="2" class="basic-content-align" style="table-layout:fixed">
					<tbody>
						<tr id="userSearchFormDivIdentifier" style="display: none;">
							<td width="5%"></td>
							<td width="10%"><label class="label-bold">
								<spring:message code="label.generic.search.ldap.user"/></label>:
							</td>
							<td width="30%">
								<input type="text" size="30" name="prexcname" id="prexcname" maxlength="50"/> 
								<a href="javaScript:lookupUser();"><img src="../images/lookupIcon1.png" title="Lookup User"></a> 
							</td>
							<td width="2%">&nbsp;</td>
							<td width="15%">&nbsp;</td>
							<td width="30%">&nbsp;</td>
						</tr>
						<tr>
							<td width="5%" align="right"><span class="mandatory">*&nbsp;</span></td>
							<td width="11%" id="firstNameLabel">
								<label class="label-bold">
									<spring:message code="label.generic.firstName"/>:
								</label>
							</td>
							<td width="30%">
								<form:hidden path="forceUpdate" id="forceUpdate"/>
								<form:input size="35" path="firstName" id="firstName" readonly="true" onfocus="this.blur()" cssClass="input-textbox-readonly user-name-input"/>
							</td>
							<td width="5%">&nbsp;</td>
							<td width="20%">
								<label class="label-bold">
									<spring:message code="label.generic.lastName"/>:
								</label>
							</td>
							<td>
								<form:input size="35" path="lastName" id="lastName" readonly="true" onfocus="this.blur()" cssClass="input-textbox-readonly user-lastname-input"/>
							</td>
						</tr>
						<tr>
							<td align="right"><span class="mandatory">*&nbsp;</span></td>
							<td>
								<label class="label-bold">
									<spring:message code="label.generic.loginName"/>:
								</label>
							</td>
							<td>
								<form:input size="35" path="loginName" id="loginName" readonly="true" onfocus="this.blur()" cssClass="input-textbox-readonly user-loginname-input"/>
							</td>
							<td>&nbsp;</td>
							<td width="20%">
								<label class="label-bold">
									<spring:message code="label.generic.email"/>:
								</label>
							</td>
							<td>
								<form:input size="35" path="email" id="email" readonly="true" onfocus="this.blur()" cssClass="input-textbox-readonly user-email-input"/>
							</td>
						</tr>
						<tr>
							<td align="right"><span class="mandatory">*&nbsp;</span></td>
							<td>
								<label class="label-bold">
									<spring:message code="label.generic.role"/>:
								</label>
							</td>
							<td>
								<form:select path="roleId" id="roleId" style="margin:0;">
									<option value=""><spring:message code="label.generic.blankSelectOption"/></option>
									<form:options items="${allRoles}" />
								</form:select>
							</td>
							<td>&nbsp;</td>
							<td width="20%">
								<label class="label-bold">
									<spring:message code="label.generic.internationalUser"/>:
								</label>
							</td>
							<td>
								<select name="global" id="global" style="margin:0;">
									<option value="false"><spring:message code="label.generic.no"/></option>
									<option value="true"><spring:message code="label.generic.yes"/></option>
								</select>
							</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td>
								<label class="label-bold">
									<spring:message code="label.generic.status"/>:
								</label>
							</td>
							<td>
								<form:select path="active" id="active" style="margin:0;">
									<option value="true"><spring:message code="label.generic.active"/></option>
									<option value="false"><spring:message code="label.generic.inactive"/></option>
								</form:select>
							</td>
							<td>&nbsp;</td>
							<td>
								<div id ="displayWarRoomDiv" style="display: none">
									<label class="label-bold">
										<spring:message code="label.generic.display.on.warRoom"/>:
									</label>
								</div>
	 						</td>
	 						<td>
	 							<div id = "displayWarRoomCBDiv" style="display:none;">
	 								<form:checkbox path="displayOnWarRoom" id="displayOnWarRoom" style="margin-left:0;vertical-align:-1px;" />
								</div>
	 						</td>
						</tr>
					</tbody>
				</table>
			</fieldset>
			<fieldset>
				<legend class="label-bold"><spring:message code="label.generic.salesCategory"/></legend>
				<table width="100%" cellpadding="2" cellspacing="2" class="basic-content-align" style="table-layout:fixed">
					<tbody>
						<tr id="userSalesCategoryRow">
							<td width="5%">&nbsp;</td>
							<td width="11%" style="vertical-align: middle;">
								<label class="label-bold">
									<spring:message code="label.generic.available"/>:
								</label>
							</td>
							<td width="30%">
								<select style="width: 87%" multiple="multiple" size="5" id="availSalList" name="availSalList"
									 ondblclick="moveSelectBoxOptions('selectedSalesCatList', 'availSalList');">
								</select>
							</td>
							<td width="7%" style="vertical-align: middle;">
								<div class="forward">
									<a id="addSalIt" class="fwd-btn-users" title="Select Sales Category" alt="Right" 
										href="javascript:void('0');" onClick="moveSelectBoxOptions('selectedSalesCatList', 'availSalList');" ></a>
									<a id="addAllSalIt" class="fwd-all-btn-users" title="Select All Sales Category" alt="Right" 
										href="javascript:void('0');" onClick="moveAllSelectBoxOptions('selectedSalesCatList', 'availSalList');" ></a>
								</div>
								<div>
									<a id="delDalIt" class="bckwrd-btn-users" title="Unselect Sales Category" alt="Left"
									 	href="javascript:void('0')" onClick="moveSelectBoxOptions('availSalList', 'selectedSalesCatList');"></a>
								 	<a id="delAllDalIt" class="bckwrd-all-btn-users" title="Unselect All Sales Category" alt="Left"
									 	href="javascript:void('0')" onClick="moveAllSelectBoxOptions('availSalList', 'selectedSalesCatList');"></a>
								</div>
		          			</td>
		          			<td width="11%" style="vertical-align: middle;">
								<label class="label-bold">
									<spring:message code="label.generic.selected"/>:
								</label>
							</td>
							<td width="30%">
								<select style="width: 87%;" multiple="multiple" size="5" id="selectedSalesCatList" name="selectedSalesCatList" 
									ondblclick="moveSelectBoxOptions('availSalList', 'selectedSalesCatList');">
							 	</select>
							 	<input type="hidden" id="userSalesCatRequired" value="false" name="userSalesCatRequired"></input>
							</td>
						</tr>
					</tbody>
				</table>
			</fieldset>
		</div>
		<div id="manageuser-tabs-2">
			<table width="98%" cellpadding="2" cellspacing="2" align="center" class="basic-content-align" style="padding:10px 0">
				<tr>
					<td width="5%">&nbsp;</td>
					<td width="12%" nowrap="nowrap">
						<label class="label-bold">
							<spring:message code="label.generic.address1"/>:
						</label>
					</td>
					<td width="25%">
						<form:input size="40" path="address1" id="address1" maxlength="50"/>
					</td>
					<td width="12%" nowrap="nowrap">
						<label class="label-bold">
							<spring:message code="label.generic.address2"/>:
						</label>
					</td>
					<td width="25%">
						<form:input size="40" path="address2" id="address2" maxlength="50"/>
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td>
						<label class="label-bold">
							<spring:message code="label.generic.state"/>:
						</label>
					</td>
					<td>
						<form:input size="30" path="state" id="state" maxlength="30"/>
					</td>
					<td>
						<label class="label-bold">
							<spring:message code="label.generic.city"/>:
						</label>
					</td>
					<td>
						<form:input size="30" path="city" id="city" maxlength="30"/>
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td>
						<label class="label-bold">
							<spring:message code="label.generic.zip"/>:
						</label>
					</td>
					<td>
						<form:input size="15" path="zip" id="zip" maxlength="10"/>
					</td>
					<td>
						<label class="label-bold">
							<spring:message code="label.generic.faxNo"/>:
						</label>
					</td>
					<td>
						<form:input size="15" path="faxNo" id="faxNo" maxlength="15"/>
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td nowrap="nowrap">
						<label class="label-bold">
							<spring:message code="label.generic.telephone"/>:
						</label>
					</td>
					<td>
						<form:input size="15" path="telephone" id="telephone" maxlength="15"/>
					</td>
					<td nowrap="nowrap">
						<label class="label-bold">
							<spring:message code="label.generic.mobile"/>:
						</label>
					</td>
					<td>
						<form:input size="15" path="mobile" id="mobile" maxlength="15"/>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<table width="98%" cellpadding="2" cellspacing="2" align="center"  class="basic-content-align">
		<tr>
			<td align="right" width="50%" class="padRght20">
				<form:hidden id="hiddenuserId" path="userId" />
				<form:hidden id="roleName" path="roleName" />
				<form:hidden id="userRolesAsString" path="userRolesAsString" />
				<form:hidden id="globalUserString" path="globalUserString" />
				<form:hidden id="statusString" path="statusString" />
				<form:hidden id="version" path="version" />
				<input type="button" value="SAVE" id="saveUserDetailButton" class="save-btn" />
			</td>
			<td>
				<input Type="button" value="RESET" id="resetUserDetailFormButton" class="reset-btn" />
			</td>
		</tr>
	</table>
	<div class="spacer7"></div>
</form:form>