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

	public TransformationResult process(final InputStream is) {
		final TransformationResult result = new TransformationResult(true, "", null);
		int i = 1;
		try {
			final IStreamProcessor processor = new ExcelTxStreamProcessor(is);
			final List<FinancialTransaction> transactions = new ArrayList<FinancialTransaction>();
			while ( processor.hasNextObject() ) {
				final List<Object> transactionParams = (List<Object>) processor.generateNextObject();
				final CustomFinancialTransaction ctx = this.rowTransformer.transform(transactionParams);
				transactions.add(ctx);
				i++;
			}
			this.txStorage.storeTransactionList(transactions);
		}
		catch(final Exception e) {
			result.setSuccess(false);
			result.setRowNum(i);
			result.setMessage( e.getMessage() );
		}
		return result;

	}

	public class TransformationResult {
		boolean success;
		String message;
		Integer rowNum;

		public TransformationResult(final boolean success, final String message,
				final Integer rowNum) {
			super();
			this.success = success;
			this.message = message;
			this.rowNum = rowNum;
		}
		public boolean isSuccess() {
			return this.success;
		}
		public void setSuccess(final boolean success) {
			this.success = success;
		}
		public String getMessage() {
			return this.message;
		}
		public void setMessage(final String message) {
			this.message = message;
		}
		public Integer getRowNum() {
			return this.rowNum;
		}
		public void setRowNum(final Integer rowNum) {
			this.rowNum = rowNum;
		}


	}
}
