/**
 * 
 */
package org.devgateway.eudevfin.financial.dao;

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
	@ServiceActivator(inputChannel="findAllAsListTransactionChannel")
	public List<FinancialTransaction> findAllAsList() {
		return super.findAllAsList();
	}
	
	@Override
	@ServiceActivator(inputChannel="saveTransactionChannel")
	public FinancialTransaction save(FinancialTransaction tx) {
		return super.save(tx);
		
	}
	
	/* (non-Javadoc)
	 * @see org.devgateway.eudevfin.financial.dao.AbstractDaoImpl#findOne(java.lang.Long)
	 */
	@Override
	@ServiceActivator(inputChannel="findTransactionByIdChannel")
	public FinancialTransaction findOne(Long id) {
		return super.findOne(id);
	}

	public List<FinancialTransaction> findBySourceOrganizationId(Long orgId) {
		return getRepo().findByReportingOrganizationId(orgId);
	}

	@Override
	FinancialTransactionRepository getRepo() {
		return repo;
	}
}
