package org.devgateway.eudevfin.financial.translate;

import javax.persistence.Entity;

import org.devgateway.eudevfin.financial.Category;
import org.hibernate.envers.Audited;
@Entity
@Audited
public class CategoryTranslation extends AbstractTranslation<Category> implements CategoryTrnInterface {
	
	private String name;

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
