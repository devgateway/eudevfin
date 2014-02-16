/**
 * 
 */
package org.devgateway.eudevfin.financial.service;

import java.util.List;

import org.devgateway.eudevfin.common.service.BaseEntityService;
import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.integration.annotation.Header;

/**
 * @author Alex
 *
 */
public interface CustomFinancialTransactionService extends BaseEntityService<CustomFinancialTransaction> {
	public Page<CustomFinancialTransaction> findByDraftPageable (Boolean draft, @Header("pageable")Pageable pageable);
	
	public List<CustomFinancialTransaction> findByReportingYearAndDraftFalse(final Integer year);
}
