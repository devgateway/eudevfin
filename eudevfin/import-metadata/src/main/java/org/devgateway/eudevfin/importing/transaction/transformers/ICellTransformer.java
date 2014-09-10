/**
 *
 */
package org.devgateway.eudevfin.importing.transaction.transformers;

import java.util.Map;

import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.importing.transaction.transformers.TransactionRowTransformer.ServicesWrapper;


/**
 * @author alexandru-m-g
 *
 */
public interface ICellTransformer<ReturnType> {

		public ReturnType populateField(Object src, CustomFinancialTransaction ctx, Map<String, Object> context, ServicesWrapper servicesWrapper);

}
