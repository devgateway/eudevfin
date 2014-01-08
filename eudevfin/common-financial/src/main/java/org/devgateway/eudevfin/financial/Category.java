package org.devgateway.eudevfin.financial;

import java.io.Serializable;
import java.util.HashSet;
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
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.devgateway.eudevfin.financial.translate.CategoryTranslation;
import org.devgateway.eudevfin.financial.translate.CategoryTrnInterface;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
    name="category_type",
    discriminatorType= DiscriminatorType.STRING)
@DiscriminatorValue("Category")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"category_type", "code"}))
public class Category extends AbstractTranslateable<CategoryTranslation>
					implements CategoryTrnInterface, Serializable{

	private static final long serialVersionUID = -6173469233250737236L;

	/**
	 * The code should be unique within a certain type of category
	 */
	private String code;
	
	@ManyToOne
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

	public Category getParentCategory() {
		return parentCategory;
	}



	public void setParentCategory(Category parentCategory) {
		this.parentCategory = parentCategory;
		if ( this.parentCategory.children == null ) {
			this.parentCategory.children = new HashSet<Category>();
		}
		this.parentCategory.children.add(this);
	}
	
	

}
