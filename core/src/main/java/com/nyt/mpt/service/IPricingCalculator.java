/**
 *
 */
package com.nyt.mpt.service;

import java.util.Map;

import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.Proposal;

/**
 * Calculate base price for a lineItem
 * @author rakesh.tewari
 *
 */
public interface IPricingCalculator {

	/**
	 * <p>
	 * Returns a map which contains calculated base price of a lineItem and base
	 * price calculation step
	 * <p>
	 * STEP FOR BASE PRICE CALCULATION
	 * <p>
	 * <ul>
	 * <li>For pricing calculator we take object of Line Item and Sales Category
	 * as a input parameters.</li>
	 * <li>From line item object we get product and Sales targets.</li>
	 * <li>Based on Sales Category we search rate profile .</li>
	 * <li>If rate profile exists than we search product and sales target
	 * combination in current rate profile</li>
	 * <ul>
	 * <li>If product and sales target combination exists than we use base price
	 * of rate profile to calculate price.</li>
	 * <li>If product and sales target combination does not exist than we search
	 * product and sales target combination from default rate profile, if exist
	 * than calculate base price</li>
	 * <li>If product and sales target combination does not exist in default
	 * rate profile than base price would be 'NA' and no further calculations
	 * will be done.</li>
	 * </ul>
	 * <li>If we found base price for all the product sales Target combinations
	 * then we calculate the base price of a line item using formula defined
	 * below(Weight based).</li>
	 * <li>Now we check whether Line item have targets applied.</li>
	 * <li>If targets are available than find all section of sales target from
	 * db.</li>
	 * <li>Find all the tier for filtered section from step 7</li>
	 * <li>From set of tier find highest level tier</li>
	 * <li>From the selected tier we will check which all target type are
	 * defined, if target type and elements combination of a line Item exist
	 * than we will pick its premium.</li>
	 * <li>in case of geo targeting (state, dma, country, region) we will pick
	 * the minimum premium, for all other targeting we will pick the highest
	 * premium</li>
	 * <li>After getting all the premiums for targeting we will apply the
	 * formula defined below(apply max premium).</li>
	 * <li>Finally utility will return the calculated base price</li> </ul>
	 * <p>
	 * Weight based formula to calculate base price
	 * <ul>
	 * <li>Base Price = (BP1* ST1w1+-----+BPn*STnWn)/(ST1W1+----STnWn)</li>
	 * <li>Where</li>
	 * <li>BP1 = Base Price of rate profile</li>
	 * <li>ST1w1 = Sales target weight</li>
	 * </ul>
	 * 
	 * <p>
	 * Formula to apply premium on base price
	 * <ul>
	 * <li>Base Price = BP + ((BP * Premium) / 100)</li>
	 * <li>Where</li>
	 * <li>BP = Calculate base price</li>
	 * <li>Premium = Premium on </li>
	 * </ul>
	 * 
	 * @param lineItem
	 *            {@link LineItem}
	 * @param salesCategoryId
	 *            sales category id of the {@link Proposal}
	 * @param priceType 
	 * @return a map which contains calculated base price of a lineItem and base
	 *         price calculation step
	 */
	Map<String, Object> getLineItemPrice(LineItem lineItem, Long salesCategoryId, String priceType);

	/**
	 * <p>
	 * Return calculated base price without applying any premium for a {@link LineItem}
	 * and sales category.
	 * <p>
	 * Weight based formula to calculate base price
	 * <ul>
	 * <li>Base Price = (BP1* ST1w1+-----+BPn*STnWn)/(ST1W1+----STnWn)</li>
	 * <li>Where</li>
	 * <li>BP1 = Base Price of rate profile</li>
	 * <li>ST1w1 = Sales target weight</li>
	 * </ul>
	 * 
	 * @param lineItem
	 *            {@link LineItem}
	 * @param salesCategoryId
	 *            sales category id of a {@link Proposal}
	 * @return calculated base price without applying any premium for a lineItem
	 *         and sales category.
	 */
	Double getLineItemBasePrice(final LineItem lineItem, final Long salesCategoryId);
	
	/**
	 * Return premium for {@link LineItem} targets
	 * @param lineItem
	 * @return
	 */
	Double getLineItemPremium(final LineItem lineItem); 
}
