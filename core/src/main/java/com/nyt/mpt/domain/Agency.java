/**
 * 
 */
package com.nyt.mpt.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * This class is used for providing Agency info
 * 
 * @author amandeep.singh
 * 
 */
@Entity
@DiscriminatorValue("Agency")
public class Agency extends ProposalClients {

}
