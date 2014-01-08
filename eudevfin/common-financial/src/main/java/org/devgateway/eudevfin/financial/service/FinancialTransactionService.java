package org.devgateway.eudevfin.financial.service;

import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.util.PagingHelper;
import org.springframework.integration.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * @author Alex
 *
 */
@Component
public interface FinancialTransactionService extends BaseEntityService<FinancialTransaction> {
	
	public PagingHelper<FinancialTransaction> findBySectorCode(String sectorCode, 
			@Header("pageNumber")int pageNumber, @Header("pageSize")int pageSize);
	
	public PagingHelper<FinancialTransaction> findByGeneralSearch(String searchString, 
			@Header("pageNumber")int pageNumber, @Header("pageSize")int pageSize);

}