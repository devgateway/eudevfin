package org.devgateway.eudevfin.financial;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.financial.exception.CategoryOperationException;
import org.devgateway.eudevfin.financial.translate.CategoryTranslation;
import org.devgateway.eudevfin.financial.translate.CategoryTrnInterface;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
    name="category_type",
    discriminatorType= DiscriminatorType.STRING)
@DiscriminatorValue("Category")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"category_type", "code"}))
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Category extends AbstractTranslateable<CategoryTranslation>
					implements CategoryTrnInterface, Serializable{

	private static final long serialVersionUID 	= -6173469233250737236L;
	
	public static String PREFIX_SEPARATOR		= "##"; 

	/**
	 * The code should be unique within a certain type of category
	 */
	@Index(name="Category_code_idx")
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

	@Transient
	private List<Category> filteredChildren;


	public String getDisplayableCode() {
		String code		= this.getCode();
		if ( code != null && code.contains(PREFIX_SEPARATOR) ) {
			int index	= code.indexOf(PREFIX_SEPARATOR);
			return code.substring(index + PREFIX_SEPARATOR.length() );
		}
		else
			return code;
	}
	
	/**
	 * 
	 * @return true if this category is on the first level
	 * @throws CategoryOperationException if this is called on a ROOT category which should only be used for grouping
	 * other categories. This could point to a data problem.
	 */
	public boolean isLastAncestor() throws CategoryOperationException {
		if ( this.getParentCategory() == null )
			throw new CategoryOperationException("This function should not be called on root categories. "
					+ "Root categories are just for grouping, they shouldn't hold valuable info.");
		return this.getParentCategory().getParentCategory() == null;
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

	
	public List<Category> getFilteredChildren() {
		if ( filteredChildren == null )
			filteredChildren	= new ArrayList<Category>();
		return filteredChildren;
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
