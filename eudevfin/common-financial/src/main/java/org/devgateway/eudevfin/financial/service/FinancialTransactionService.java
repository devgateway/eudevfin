package org.devgateway.eudevfin.financial.service;

import java.util.List;

import org.devgateway.eudevfin.common.service.BaseEntityService;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.joda.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.integration.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * @author Alex
 *
 */
@Component
public interface FinancialTransactionService extends BaseEntityService<FinancialTransaction> {
	
	public Page<FinancialTransaction> findBySectorCode(String sectorCode, 
			@Header("pageable") Pageable pageable);
	
	public List<FinancialTransaction> findByReportingYearAndTypeOfFlowCode(LocalDateTime reportingYear, @Header("typeOfFlowCode")  String typeOfFlowCode);	
}