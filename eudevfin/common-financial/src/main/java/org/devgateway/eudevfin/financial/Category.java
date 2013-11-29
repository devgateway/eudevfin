package org.devgateway.eudevfin.financial;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.devgateway.eudevfin.financial.translate.CategoryTranslation;
import org.devgateway.eudevfin.financial.translate.CategoryTrnInterface;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
    name="CategoryType",
    discriminatorType= DiscriminatorType.STRING)
@DiscriminatorValue("Category")
public class Category extends AbstractTranslateable<CategoryTranslation>
					implements CategoryTrnInterface, Serializable{

	private static final long serialVersionUID = -6173469233250737236L;

	private String code;
	
	@ManyToOne
	//@JoinColumn(name="PARENT_CATEGORY_ID")
	private Category parentCategory;
	
	@OneToMany(mappedBy="parentCategory", cascade=CascadeType.ALL)
	private Set<Category> children;
	
	@ManyToMany
	@JoinTable(name="CATEGORY_TAGS")
	private Set<Category> tags;
	

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

	@Override
	public String toString() {
		return "Category [code=" + code + ", getName()=" + getName() + "]";
	}



	public String getCode() {
		return code;
	}



	public void setCode(String code) {
		this.code = code;
	}



	public Set<Category> getChildren() {
		return children;
	}



	public void setChildren(Set<Category> children) {
		this.children = children;
	}



	public Set<Category> getTags() {
		return tags;
	}

	

	public void setTags(Set<Category> tags) {
		this.tags = tags;
	}
	
	

}