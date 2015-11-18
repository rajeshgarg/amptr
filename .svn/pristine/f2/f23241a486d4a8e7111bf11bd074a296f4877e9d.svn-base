/**
 * @author amandeep.singh
 */

$(document).ready(function() {	
	$(".expire-package").parent().parent().parent().css("width","98.7%");
	$(".expire-package").parent().addClass("expire-package");
});

 function toggleCurrency(event , toggleButton){
	$(".toggle-div label").removeClass("selected-curr");
	event.target.className = "selected-curr";
	var usdSymbol = " (<b>&#36;</b>)";
	var eurSymbol = " (<b>&euro;</b>)";
	var cnySymbol = " (<b>&#165;</b>)";
	var brazilianSymbol = " (<b>&#8354;</b>)";
	var toggleCurrency = $(toggleButton).text().split(" ")[0];
	convertAmountsToSelectedCurrency(toggleCurrency);
	if(toggleCurrency == 'EUR'){
		$("#usdSymbol", "#summaryParentDiv").html(eurSymbol);
	}else if(toggleCurrency == 'CNY'){
		$("#usdSymbol", "#summaryParentDiv").html(cnySymbol);
	}else if(toggleCurrency == 'USD'){
		$("#usdSymbol", "#summaryParentDiv").html(usdSymbol);
	}else if(toggleCurrency == 'BEL'){
		$("#usdSymbol", "#summaryParentDiv").html(brazilianSymbol);
	}
 }
 
 function convertAmountsToSelectedCurrency(currency){
	 var conversionRate = Number($("#exchangeRate","#summaryParentDiv").val());
	 if(currency == 'USD'){
		 $("#InvestmentInProposalCurrency", "#summaryParentDiv").hide();
		 $("#budgetInProposalCurrency", "#summaryParentDiv").hide();
		 $("#cpmInProposalCurrency", "#summaryParentDiv").hide();
		 $("#remBudgetInProposalCurrency", "#summaryParentDiv").hide();
		 $("#totalInvestment", "#summaryParentDiv").show();
		 $("#proposalBudget", "#summaryParentDiv").show();
		 $("#proposalCPM", "#summaryParentDiv").show();
		 $("#proposalRemBudget", "#summaryParentDiv").show();
		 
		 $(".lineItemCurrencyClass", "#LineItemAccordian").hide();
		 $(".lineItemValueInDollar", "#LineItemAccordian").show();
		 $(".totalInvestmentInCurrency", "#LineItemAccordian").hide();
		 $(".totalInvestmentInDollar", "#LineItemAccordian").show();
		 
	 }else{
		 $("#totalInvestment", "#summaryParentDiv").hide();
		 $("#proposalBudget", "#summaryParentDiv").hide();
		 $("#proposalCPM", "#summaryParentDiv").hide();
		 $("#proposalRemBudget", "#summaryParentDiv").hide();
		 convertAmountToSelectedCurrency($("#proposalBudget", "#summaryParentDiv").text() , $("#budgetInProposalCurrency", "#summaryParentDiv"));
		 var offerdBudget = 0.00;
		 var netImpressions = 0.00;
		 $(".lineItemValueInDollar", "#LineItemAccordian").hide();
		 $(".totalInvestmentInDollar", "#LineItemAccordian").hide();
		 $(".lineItemValueInDollar").each(function(){
		 		if(isNaN($(this).text().replace(/,/g, '')) || $.trim($(this).text()) == ''){
		 			$(this).siblings(".lineItemCurrencyClass").text($(this).text());
		 			$(this).siblings(".lineItemCurrencyClass").show();
		 		} else {
		 			convertAmountToSelectedCurrency($(this).text(), $(this).siblings(".lineItemCurrencyClass"));
		 		}
		 		/*Set Line Items Information*/
		 		var lineItemId = $(this).siblings(".lineItemCurrencyClass").next( ".lineItemIDClass" ).text();
		 		if(lineItemId != 0 || lineItemId != ""){
		 			var rate = $("#rate_"+lineItemId, "#LineItemAccordian").text();
		 			var impressionTotal = $("#impressionTotal_"+lineItemId, "#LineItemAccordian").text();
		 			var conversionRate = Number($("#exchangeRate", "#summaryParentDiv").val());
		 			var totalInvestmentInCurrency = 0.00;
		 			netImpressions = netImpressions + Number(impressionTotal.replace(/,/g, ''));
		 			if(isNaN(rate.replace(/,/g, '')) || $.trim(rate) == ''){
		 				$("#totalInvestmentInCurrency_"+lineItemId).show();
		 				/* Calculate totalInvestment for line item when 'Price Type' is 'Flat Rate'*/
		 				if($("#totalInvestmentInDollar_"+lineItemId).text() != 0.00){
		 					totalInvestmentInCurrency = roundOffNumber($("#totalInvestmentInDollar_"+lineItemId).text().replace(/,/g, '')/conversionRate, 2);
		 					numberFormater(totalInvestmentInCurrency, $("#totalInvestmentInCurrency_"+lineItemId));
		 					offerdBudget = offerdBudget + Number(totalInvestmentInCurrency);
		 				}
		 			} else {
		 				/* Apply conversion rate in rate*/
		 				rate = roundOffNumber(rate.replace(/,/g, '')/conversionRate, 2);
		 				//impressionTotal = impressionTotal.replace(/,/g, '');
		 				/* Calculate totalInvestment for line item*/
		 				totalInvestmentInCurrency = roundOffNumber((impressionTotal.replace(/,/g, '') * rate) / 1000, 2);
		 				numberFormater(totalInvestmentInCurrency, $("#totalInvestmentInCurrency_"+lineItemId));
		 				$("#totalInvestmentInCurrency_"+lineItemId).show();
		 				/* Calculate offerdBudget for option*/		 			
		 				offerdBudget = offerdBudget + Number(totalInvestmentInCurrency);
		 			}
		 		}
		 	
		 });
		 /* Total Investment in currency*/
		 numberFormater(roundOffNumber(offerdBudget, 2), $("#InvestmentInProposalCurrency", "#summaryParentDiv"));
		 $("#InvestmentInProposalCurrency", "#summaryParentDiv").show();
		 /* Net CPM  in currency*/
		 if(netImpressions != 0){
			 numberFormater(roundOffNumber((roundOffNumber(offerdBudget, 2) / netImpressions) * 1000, 2), $("#cpmInProposalCurrency", "#summaryParentDiv"));
		 } else {
			 $("#cpmInProposalCurrency", "#summaryParentDiv").text('0.00');
		 }
		 $("#cpmInProposalCurrency", "#summaryParentDiv").show();
		 /* Remaining Budget in currency*/
		 numberFormater(roundOffNumber(($("#budgetInProposalCurrency", "#summaryParentDiv").text().replace(/,/g, '') - offerdBudget), 2),  $("#remBudgetInProposalCurrency", "#summaryParentDiv"));
		 $("#remBudgetInProposalCurrency", "#summaryParentDiv").show();
	 }
 }
 
 function numberFormater(value, select){
		if(value == '') {
			$(select).text('0.00');
			return;
		}
		if (parseFloat(value, 10) == 0) {
			$(select).text('0.00');
		}
		$(select).text(formatDecimal(value));
 }
 
 function roundOffNumber(value, places) {
	   var multiplier = Math.pow(10, places);
	   return (Math.round(value * multiplier) / multiplier);
 }
 
 function convertAmountToSelectedCurrency(value, select) {
	var conversionRate = Number($("#exchangeRate", "#summaryParentDiv").val());
	numberFormater(roundOffNumber(value.replace(/,/g, '')/conversionRate, 2), select);
	$(select).show();
 }