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
public class FinancialTransactionDaoImpl extends AbstractDaoImpl<FinancialTransaction, FinancialTransactionRepository> {

	@Autowired
	private FinancialTransactionRepository repo;
	
	@Override
	@ServiceActivator(inputChannel="getTransactionChannel", outputChannel="replyGetTransactionChannel")
	public List<FinancialTransaction> findAllAsList() {
		return super.findAllAsList();
	}
	
	@Override
	@ServiceActivator(inputChannel="createTransactionChannel", outputChannel="replyCreateTransactionChannel")
	public FinancialTransaction save(FinancialTransaction tx) {
		return super.save(tx);
		
	}
	
	public List<FinancialTransaction> findBySourceOrganizationId(Long orgId) {
		return getRepo().findBySourceOrganizationId(orgId);
	}

	@Override
	FinancialTransactionRepository getRepo() {
		return repo;
	}
}
