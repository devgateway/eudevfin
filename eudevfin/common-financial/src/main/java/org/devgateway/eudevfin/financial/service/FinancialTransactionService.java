package org.devgateway.eudevfin.financial.service;

import java.util.List;

import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.Payload;

/**
 * @author Alex
 *
 */
public interface FinancialTransactionService {
	
	@Gateway(requestChannel="createTransactionChannel",replyChannel="replyCreateTransactionChannel")
	FinancialTransaction createFinancialTransaction(FinancialTransaction t);
	
	@Gateway(requestChannel="getTransactionChannel", replyChannel="replyGetTransactionChannel")
	@Payload("new java.util.Date()")
	List<FinancialTransaction> getAllFinancialTransactions();
	
	@Gateway(requestChannel="getTransactionChannel")
	FinancialTransaction getTransacationById(long id);

}