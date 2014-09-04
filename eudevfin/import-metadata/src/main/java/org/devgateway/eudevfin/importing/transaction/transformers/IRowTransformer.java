/**
 *
 */
package org.devgateway.eudevfin.importing.transaction.transformers;

import java.util.List;

import org.devgateway.eudevfin.financial.CustomFinancialTransaction;

/**
 * @author alexandru-m-g
 *
 */
public interface IRowTransformer {
	public CustomFinancialTransaction transform(List<Object> srcList);

	public List<ICellTransformer<?>> getCellTransformerList();
}
