/**
 * @author amandeep.singh
 */

$(document).ready(function() {
	if(attrType=="PRODUCT"){
		parentContainer='#product-attribute-form';		
	}
	else{
		parentContainer='#attribute-form';	
	}	
	$(parentContainer +' #attributeType').val(attrType);
	formObj="attributeassoc"+$(parentContainer +' #attributeType').val();
	
	$(parentContainer+' #assocaction').attr('checked', 'checked');	
	if($(parentContainer +' #operation').val()=="edit"){
		$(parentContainer+' #hidAttributeId').val($(parentContainer+' #attributeId').val());
		$(parentContainer+' #attributeId').attr('disabled', 'disabled');
		$(parentContainer+' #assocaction').attr('disabled', 'disabled');
		$(parentContainer+' #addaction').attr('disabled', 'disabled');		
		$(parentContainer+' #attributeAction').hide();
		$('#attributeAssocDetails_'+attrType,parentContainer).show();
	}
	else{
		$(parentContainer+' #hidAttributeId').val($(parentContainer+' #attributeId').val());
		$(parentContainer+' #attributeId').removeAttr("disabled");		
		$(parentContainer+' #assocaction').removeAttr("disabled");		
		$(parentContainer+' #addaction').removeAttr("disabled");		
		$(parentContainer+' #attributeAction').show();
	}
	jQuery("#attributeDescription", "#attributeassoc" + attrType).limiter(500, $("#charsRemainingCreativeAttr", "#attributeassoc" + attrType));
	$("#charsRemainingCreativeAttr", "#attributeassoc" + attrType).hide();
});

	function setAction(obj) {
		if (obj == "add") {
			$(parentContainer+' #attributeNameLabel').show();
			$(parentContainer+' #attributeSelectLabel').hide();
			$(parentContainer+' #attributeDescription').removeAttr("disabled");
			$("#charsRemainingCreativeAttr", "#attributeassoc" + attrType).show();
		} else {
			$(parentContainer+' #attributeNameLabel').hide();
			$(parentContainer+' #attributeSelectLabel').show();
			$(parentContainer+' #attributeDescription').attr('disabled', 'disabled');
			$("#charsRemainingCreativeAttr", "#attributeassoc" + attrType).hide();
		}
		clearCSSErrors(parentContainer+' #attributeAssocContainer');
		$(parentContainer+' #attributeDescription').val("");
		$(parentContainer+' #attributeValue').val("");
		$(parentContainer+' #attributeName').val("");
		$(parentContainer+' #attributeId').val("");
		$('#attributeAssocDetails_'+attrType,parentContainer).hide();
		jQuery("textarea", "#attributeassoc" + attrType).change();
	}

	function fillAttributeDetails(select){
		if($(select).val() == ''){
			$(parentContainer+' #attributeValue').val('');	
			$(parentContainer+' #attributeDescription').val('');
			$('#attributeAssocDetails_'+attrType,parentContainer).hide();
			return;
		}
		var url = "../manageAttribute/getAttributeDetails.action";	
		$(parentContainer+' #'+formObj).attr("action",url); 	
		//alert($(parentContainer+' #attributeId').val());	 	
		var attributeAssocDetail = new FormManager();
		attributeAssocDetail.formName = formObj;
		var attributeAssocDetailForm = new JQueryAjaxForm(attributeAssocDetail);
		attributeAssocDetailForm.doCustomProcessingAfterFormSucsesJson = function(jsonResponse, XMLHTTPRequest){
			 var returnedId = jsonResponse.objectMap.gridKeyColumnValue;
			 $(parentContainer+' #attributeValue').val(returnedId.attributeOptionalValue);	
			 $(parentContainer+' #attributeDescription').val(returnedId.attributeDescription);		
			 $('#attributeAssocDetails_'+attrType,parentContainer).show();
		};
		attributeAssocDetailForm.submit();
	}