/**
 * 
 */
package org.devgateway.eudevfin.importing.metadata.mapping;

import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.domain.CurrencyCategory;

/**
 * @author Alex
 *
 */
public class CurrencyCategoryMapper extends CategoryMapper {

	@Override
	protected Category instantiate() {
		return new CurrencyCategory();
	}

}
