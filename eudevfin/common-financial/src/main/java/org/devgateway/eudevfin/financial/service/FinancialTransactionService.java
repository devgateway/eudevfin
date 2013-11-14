package org.devgateway.eudevfin.financial.service;

import java.util.List;

import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * @author Alex
 *
 */
@Component
public interface FinancialTransactionService {
	
	FinancialTransaction createFinancialTransaction(FinancialTransaction t);
	
	@Payload("new java.util.Date()")
	List<FinancialTransaction> getAllFinancialTransactions();
	
	FinancialTransaction getTransacationById(long id);

}