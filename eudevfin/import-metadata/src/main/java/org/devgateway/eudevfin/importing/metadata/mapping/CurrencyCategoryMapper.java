/**
 * 
 */
package org.devgateway.eudevfin.importing.metadata.mapping;

import org.devgateway.eudevfin.financial.Category;
import org.devgateway.eudevfin.financial.CurrencyCategory;

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
