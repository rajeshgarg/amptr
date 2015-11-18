/**
 * 
 */
package com.nyt.mpt.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for marking the Documents update for proposal simultaneous access
 *
 * @author amandeep.singh
 *
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ValidateProposalDocument {

}
