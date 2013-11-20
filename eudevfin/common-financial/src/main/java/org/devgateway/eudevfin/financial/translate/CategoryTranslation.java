package org.devgateway.eudevfin.financial.translate;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.devgateway.eudevfin.financial.Category;
import org.hibernate.envers.Audited;
@Entity
@Audited
@Table(name="CATEGORY_TRANSLATION",
		uniqueConstraints=@UniqueConstraint(columnNames={"PARENT_ID","LOCALE"}))
public class CategoryTranslation extends AbstractTranslation implements CategoryTrnInterface {
	
	private String name;
	
	@ManyToOne(  optional= false )
	@JoinColumn(name="PARENT_ID")
	private Category parent;

	/* (non-Javadoc)
	 * @see org.devgateway.eudevfin.financial.translate.CategoryTrnInterface#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see org.devgateway.eudevfin.financial.translate.CategoryTrnInterface#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

}
