/**
 * 
 */
package com.nyt.mpt.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


/**
 * This class is used for Advertiser info
 * 
 * @author amandeep.singh
 * 
 */
@Entity
@DiscriminatorValue("Advertiser")
public class Advertiser extends ProposalClients {
	
}
