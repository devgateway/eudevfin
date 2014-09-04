/**
 *
 */
package org.devgateway.eudevfin.importing.transaction.storage;

import java.util.List;

import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.service.FinancialTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author alexandru-m-g
 *
 */
@Component
public class TransactionStorage {

	@Autowired
	FinancialTransactionService txService;

	@Transactional
	public void storeTransactionList(final List<FinancialTransaction> transactions) {
		for ( final FinancialTransaction tx: transactions ){
			this.txService.save(tx);
		}
	}
}
