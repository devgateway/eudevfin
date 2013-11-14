/**
 * 
 */
package org.devgateway.eudevfin.financial.translate;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.devgateway.eudevfin.financial.Organization;
import org.hibernate.envers.Audited;

/**
 * @author alex
 *
 */
@Entity
@Audited
@Table(name="ORGANIZATION_TRANSLATION",
		uniqueConstraints=@UniqueConstraint(columnNames={"PARENT_ID","LOCALE"}))
public class OrganizationTranslation extends AbstractTranslation {
	@ManyToOne(  optional= false )
	@JoinColumn(name="PARENT_ID")
	private Organization parent;
	
	private String name;

	public Organization getParent() {
		return parent;
	}

	public void setParent(Organization parent) {
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

	
	
}
