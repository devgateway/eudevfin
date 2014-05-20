package org.devgateway.eudevfin.metadata.common.domain.translation;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.devgateway.eudevfin.common.dao.translation.AbstractTranslation;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
@Entity
@Audited
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
    name="category_trn_type",
    discriminatorType= DiscriminatorType.STRING)
@DiscriminatorValue("CategoryTrn")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
