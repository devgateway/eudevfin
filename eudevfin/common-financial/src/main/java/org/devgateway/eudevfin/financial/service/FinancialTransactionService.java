package org.devgateway.eudevfin.financial.service;

import java.util.List;

import org.devgateway.eudevfin.common.service.BaseEntityService;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.metadata.common.domain.Area;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
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
	
	
	public Page<FinancialTransaction> findBySearchFormPageable(
			@Header(value = "year", required = false) LocalDateTime year,
			@Header(value = "sector", required = false) Category sector,
			@Header(value = "recipient", required = false) Area recipient,
			@Header(value = "searchString", required = false) String searchString,
			@Header(value = "formType", required = false) String formType,			
			@Header(value = "extendingAgency", required = false) Organization extendingAgency,
		    @Header(value = "locale", required = false) String locale, 
		    Pageable pageable);
}