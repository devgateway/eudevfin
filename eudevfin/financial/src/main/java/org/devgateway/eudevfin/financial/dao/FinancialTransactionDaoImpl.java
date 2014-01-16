/**
 * 
 */
package org.devgateway.eudevfin.financial.dao;

import java.util.List;

import org.devgateway.eudevfin.common.dao.AbstractDaoImpl;
import org.devgateway.eudevfin.common.spring.integration.NullableWrapper;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.repository.FinancialTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.integration.annotation.Header;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

/**
 * @author Alex
 *
 */
@Component
@Lazy(value=false)
public class FinancialTransactionDaoImpl extends AbstractDaoImpl<FinancialTransaction, Long, FinancialTransactionRepository> {

	@Autowired
	private FinancialTransactionRepository repo;
	
	@Override
	@ServiceActivator(inputChannel="findAllAsListTransactionChannel")
	public List<FinancialTransaction> findAllAsList() {
		return super.findAllAsList();
	}
	
	@Override
	@ServiceActivator(inputChannel="saveTransactionChannel")
	public NullableWrapper<FinancialTransaction> save(FinancialTransaction tx) {
		return super.save(tx);
		
	}
	
	/* (non-Javadoc)
	 * @see org.devgateway.eudevfin.financial.dao.AbstractDaoImpl#findOne(java.lang.Long)
	 */
	@Override
	@ServiceActivator(inputChannel="findTransactionByIdChannel")
	public NullableWrapper<FinancialTransaction> findOne(Long id) {
		return super.findOne(id);
	}

	public List<FinancialTransaction> findBySourceOrganizationId(Long orgId) {
		return getRepo().findByReportingOrganizationId(orgId);
	}
	
	@ServiceActivator(inputChannel="findTransactionBySectorCodePageableChannel")
	public Page<FinancialTransaction> findBySectorCode(String sectorCode, 
			@Header("pageable") Pageable pageable) {	
		return  this.getRepo().findBySectorCode(sectorCode, pageable) ;
		
	}
	
	@ServiceActivator(inputChannel="findTransactionByGeneralSearchPageableChannel")
	public Page<FinancialTransaction> findByGeneralSearchPageable(String searchString,
			@Header("pageable") Pageable pageable) {

		return  this.getRepo().findByTranslationsDescriptionContaining(searchString.toLowerCase(), pageable)  ;
	}
	
	@Override
	protected
	FinancialTransactionRepository getRepo() {
		return repo;
	}
}
