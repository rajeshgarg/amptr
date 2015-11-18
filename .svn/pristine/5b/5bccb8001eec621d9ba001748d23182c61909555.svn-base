<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"   pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
	<head>
		<title>Advertising Media Planning Tool :: Help</title>
		<link rel="Shortcut Icon" type="image/x-icon" href="http://css.nyt.com/images/icons/nyt.ico" />
		<link rel="stylesheet" href="../css/jquery.ui.base.css" type="text/css" />
		<script type="text/javascript" src="../js/plugins/jquery/jquery-1.6.1.js"></script>
		<script>
			$(document).ready(function(){
				$('#templateHelpAccordion h3').click(function() {
					$(this).children().toggleClass("active");
					$(this).next().toggle('slow');
					return false;
				});
			});
		</script>	
	</head>
	<body>
	<div id="edit-proposal-container">		
		<div id="header">		
			<dl>
				<dt class="hdr-lt logo-nyt">&nbsp;&nbsp;&nbsp;&nbsp;
					<div class="logo"></div>
					<div class="logo-text">Template Help</div>
				</dt>
				<dt class="hdr-rt">						
					<span class="close" onclick="window.close();" style="cursor: pointer">
						<a id="close-button" title="Close Help">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>
					</span>						 
				</dt>
			</dl>
		</div>
		<div style="clear:both"></div>
		<div id="templateHelpAccordion" class="proposalOuterContainer">
		<div>
			<h3><span class="inactive"></span>Help List</h3>
		  		<div class="help-header" style="display:none">
		    		<ul>
						<li>The Auto Config Tokens listed below can be pasted directly
							into a new template to indicate to AMPT where to export proposal
							and line item data. Please insert only one token per cell. If
							needed, you can create your own tokens using the 'MP_' and 'MPLI_'
							prefixes.
						</li>
						<li>Tokens starting with prefix 'MP_' signify proposal-level
							information.
						</li>
						<li>Tokens starting with prefix 'MPLI_' signify line item
							information.
						</li>
						<li>Proposal-level data will be exported into the cell in
							which the corresponding token has been placed. For example, if you
							copy-paste 'MP_Proposal Start Date' into cell A1, the exported
							file will contain the proposal's assigned start date in cell A1.
						</li>
						<li>Line item tokens should be inserted into the cell directly
							under the relevant column header. For example, if column D is
							labeled 'Start Date' and you copy-paste 'MPLI_LINEITEM_startDate'
							into cell D10, the exported file will contain the start date of
							the first line item in cell D10, the start date of the second line
							item in D11, and so on.
						</li>
						<li>Since each line item of your plan will be exported into a
							new row, only the row corresponding with the first line item can
							contain any tokens with the prefix 'MPLI_.' For example, if the
							first line item should be entered into row 10, only row 10 should
							have 'MPLI_' tokens. If any cell in row 10 is left blank, each
							subsequent cell in the same column will also be blank.
						</li>
						<li>After entering the relevant tokens into a template and
							uploading it into AMPT, you have the opportunity to preview the
							relationship between each token and its corresponding attribute.
							If you have used tokens from the list below, you will not need to
							make any adjustments and can give your template a name and save
							it. The new template will be available from the "Export Template"
							tab in the proposal screen.
						</li>
					</ul>
		  		</div>
		  	</div>
		  	<div>	
			  	<h3><span class="inactive active"></span>Help Table</h3>
			  	<div class="editProposalContainer" id="proposalContainer">
				    <table id="templateHelpTable" style="table-layout: fixed; width: 100%; ">
						<tr>
							<td class="help-row-bg" align="center" width="3%"><label class="label-bold">Type</label></td>		
							<td class="help-row-bg" align="center" width="6%"><label class="label-bold">Attribute</label></td>		
							<td class="help-row-bg" align="center" width="7%"><label class="label-bold">Auto Config Token</label></td>
						</tr>
						<c:forEach var="templateType" items="${allTemplateHeads}" varStatus="loop">			
							<tr>			
								<td>${templateType.head}</td>
								<td>${templateType.attribute}</td>
								<td>${templateType.key}</td>
							</tr> 
						</c:forEach>
					</table>
		 		</div>
			</div>
		</div>	
	</div>	
	</body>
</html>