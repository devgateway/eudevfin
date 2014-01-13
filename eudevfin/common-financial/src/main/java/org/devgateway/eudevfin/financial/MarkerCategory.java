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
@DiscriminatorValue("Marker")
public class MarkerCategory extends Category {

	public MarkerCategory() {
		super();
	}

}
