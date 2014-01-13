/**
 * 
 */
package org.devgateway.eudevfin.financial.translate;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

/**
 * @author Alex
 *
 */
@Entity @Audited
@DiscriminatorValue("ChannelCategoryTrn")
public class ChannelCategoryTranslation extends CategoryTranslation implements ChannelCategoryTrnInterface {

	private String acronym;
	@Override
	public String getAcronym() {
		return acronym;
	}

	@Override
	public void setAcronym(String acronym) {
		this.acronym	= acronym;
		
	}

	
}
