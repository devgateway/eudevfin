/**
 * 
 */
package org.devgateway.eudevfin.financial;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

/**
 * @author Alex
 *
 */
@Entity
@Audited
@DiscriminatorValue("BiMultilateral")
public class BiMultilateralCategory extends Category {

	public BiMultilateralCategory() {
		super();
	}

}
