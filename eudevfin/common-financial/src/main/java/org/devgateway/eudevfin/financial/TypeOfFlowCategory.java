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
@DiscriminatorValue("TypeOfFlow")
public class TypeOfFlowCategory extends Category {

	public TypeOfFlowCategory() {
		super();
	}

}
