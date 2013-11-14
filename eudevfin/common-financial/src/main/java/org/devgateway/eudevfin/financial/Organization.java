package org.devgateway.eudevfin.financial;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.devgateway.eudevfin.financial.translate.OrganizationTranslation;
import org.hibernate.envers.Audited;

@Entity @Audited
public class Organization extends AbstractTranslateable<OrganizationTranslation>{

	
	public String code;
	
	public String getName() {
		return (String) this.get("name");
	}

	public void setName(String name) {
		this.set("name", name);
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return String.format("Org name is %s, id %d",  this.getName(), this.id);
	}

	@Override
	protected OrganizationTranslation newTranslationInstance() {
		return new OrganizationTranslation();
	}
	
	
	
}
