/**
 * @author amandeep.singh
 */
var parentContainer = '';

$(document).ready(function(){
    if (attrType == "PRODUCT") {
        parentContainer = '#product-attribute-form';
    }
    else {
        parentContainer = '#attribute-form';
    }
    $('#attributeAssocDetails_' + attrType, parentContainer).qtip({
        content: {
            // Set the text to an image HTML string with the correct src URL to the loading image you want to use
            text: '<img src="../images/ajax-loader.gif" height="20px" width="20px" alt="Loading..." />',
            ajax: {
                url: '',
                once: false
            },
            title: {
                text: 'Attribute Association', // Give the tool tip a title using each elements text
                button: true
            }
        },
        position: {
            at: 'top center', // Position the tool tip above the link
            my: 'bottom center',
            viewport: $(window), // Keep the tool tip on-screen at all times
            effect: false // Disable positioning animation
        },
        show: {
            event: 'click',
            solo: true // Only show one tool tip at a time
        },
        hide: 'unfocus',
        style: {
            classes: 'ui-tooltip-custom ui-tooltip-tipped'
        }
    });
});

function attributeAssocDetails(){
    $('#attributeAssocDetails_' + attrType, parentContainer).qtip("option", "content.ajax.url", "");
    $('#attributeAssocDetails_' + attrType, parentContainer).qtip("option", "content.text", "<img src='../images/ajax-loader.gif' height='20px' width='20px' alt='Loading...' />");
    
    var attributeId = $('#attributeId', parentContainer).val();
    var url = "../manageAttribute/getAssocDetails.action?id=" + attributeId + "&type=" + attrType;
    if (attributeId != "") {
        $('#attributeAssocDetails_' + attrType, parentContainer).qtip("option", "content.ajax.url", url);
    }
}
