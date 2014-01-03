/**
 * 
 */
package org.devgateway.eudevfin.financial.dao;

import java.util.List;

import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.repository.FinancialTransactionRepository;
import org.devgateway.eudevfin.financial.util.PagingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.integration.annotation.Header;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

/**
 * @author Alex
 *
 */
@Component
@Lazy(value=false)
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
	
	@ServiceActivator(inputChannel="findTransactionBySectorCodePageableChannel")
	public PagingHelper<FinancialTransaction> findBySectorCode(String sectorCode, 
			@Header("pageNumber")int pageNumber, @Header("pageSize")int pageSize) {
		
		int realPageNumber	= pageNumber-1;
		
		//Sort sort	= new Sort(Direction.ASC, "sectorName");
		PageRequest pageRequest = new PageRequest(realPageNumber, pageSize, null);
		
		return this.createPagingHelperFromPage( this.getRepo().findBySectorCode(sectorCode, pageRequest) );
		
	}
	
	@ServiceActivator(inputChannel="findTransactionByGeneralSearchPageableChannel")
	public PagingHelper<FinancialTransaction> findByGeneralSearch(String searchString,
			@Header("pageNumber")int pageNumber, @Header("pageSize")int pageSize ) {
		int realPageNumber	= pageNumber-1;
		PageRequest pageRequest = new PageRequest(realPageNumber, pageSize, null);
		
		
		return this.createPagingHelperFromPage( this.getRepo().findByTranslationsDescriptionContaining(searchString.toLowerCase(), pageRequest) ) ;
	}
	
	@Override
	FinancialTransactionRepository getRepo() {
		return repo;
	}
}
