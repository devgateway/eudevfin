package org.devgateway.eudevfin.financial;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;

import org.devgateway.eudevfin.financial.translate.CategoryTranslation;
import org.devgateway.eudevfin.financial.translate.CategoryTrnInterface;
import org.hibernate.envers.Audited;

@Entity
@Audited
public class Category extends AbstractTranslateable<CategoryTranslation>
					implements CategoryTrnInterface, Serializable{

	private String code;
	
//	private Set<Category> children;
//	private Set<Category> tags;
	
	public String getCode() {
		return code;
	}



	public void setCode(String code) {
		this.code = code;
	}




	@Override
	protected CategoryTranslation newTranslationInstance() {
		return new CategoryTranslation();
	}



	@Override
	public String getName() {
		return (String)get("name");
	}



	@Override
	public void setName(String name) {
		set("name", name);
	}

}
