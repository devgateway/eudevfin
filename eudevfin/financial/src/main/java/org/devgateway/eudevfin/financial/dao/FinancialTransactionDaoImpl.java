/**
 * 
 */
package org.devgateway.eudevfin.financial.dao;

import java.util.ArrayList;
import java.util.List;

import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.repository.FinancialTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

/**
 * @author Alex
 *
 */
@Component
public class FinancialTransactionDaoImpl {
//	@PersistenceContext
//    private EntityManager em;
//	
//	@ServiceActivator(inputChannel="getTransactionChannel", outputChannel="replyGetTransactionChannel")
//	public List<FinancialTransaction> loadAllFinancialTransactions() {
//		Query query 	= em.createQuery("from FinancialTransaction as ft");
//		List<FinancialTransaction> result =  query.getResultList();
//		return result;
//	}
	@Autowired
	private FinancialTransactionRepository repo;
	
	@ServiceActivator(inputChannel="getTransactionChannel", outputChannel="replyGetTransactionChannel")
	public List<FinancialTransaction> loadAllFinancialTransactions() {
		ArrayList<FinancialTransaction> ret	= new ArrayList<>(); 
		Iterable<FinancialTransaction> result	= repo.findAll();
		for (FinancialTransaction financialTransaction : result) {
			ret.add(financialTransaction);
		}
		return ret;
	}
	
	@ServiceActivator(inputChannel="createTransactionChannel", outputChannel="replyCreateTransactionChannel")
	public FinancialTransaction saveFinancialTransaction(FinancialTransaction tx) {
		FinancialTransaction ft = repo.save(tx);
		return ft;
		
	}
}
