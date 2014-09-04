/**
 *
 */
package org.devgateway.eudevfin.importing.transaction;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.importing.processors.IStreamProcessor;
import org.devgateway.eudevfin.importing.transaction.storage.TransactionStorage;
import org.devgateway.eudevfin.importing.transaction.streamprocessors.ExcelTxStreamProcessor;
import org.devgateway.eudevfin.importing.transaction.transformers.TransactionRowTransformer;
import org.devgateway.eudevfin.ui.common.temporary.SB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author alexandru-m-g
 *
 */
@Component
public class TransactionImporterEngine {

	@Autowired
	TransactionRowTransformer rowTransformer;

	@Autowired
	TransactionStorage txStorage;

	public void process(final InputStream is) {
		final IStreamProcessor processor = new ExcelTxStreamProcessor(is);
		final List<FinancialTransaction> transactions = new ArrayList<FinancialTransaction>();
		while ( processor.hasNextObject() ) {
			final List<Object> transactionParams = (List<Object>) processor.generateNextObject();
			final CustomFinancialTransaction ctx = this.rowTransformer.transform(transactionParams);
			ctx.setFormType(SB.BILATERAL_ODA_CRS);
			transactions.add(ctx);
		}
		this.txStorage.storeTransactionList(transactions);

	}
}
