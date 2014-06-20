/**
 * 
 */
package org.devgateway.eudevfin.financial.dao;

import java.util.List;

import org.devgateway.eudevfin.common.dao.AbstractDaoImpl;
import org.devgateway.eudevfin.common.spring.integration.NullableWrapper;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.repository.FinancialTransactionRepository;
import org.devgateway.eudevfin.financial.service.FinancialTransactionService;
import org.devgateway.eudevfin.metadata.common.domain.Area;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.joda.time.LocalDateTime;
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
	public NullableWrapper<FinancialTransaction> save(final FinancialTransaction tx) {
		return super.save(tx);
		
	}
	
	@Override
	@ServiceActivator(inputChannel="deleteTransactionChannel")
	public void delete(FinancialTransaction e) {
		// TODO Auto-generated method stub
		super.delete(e);
	}
	
	/* (non-Javadoc)
	 * @see org.devgateway.eudevfin.financial.dao.AbstractDaoImpl#findOne(java.lang.Long)
	 */
	@Override
	@ServiceActivator(inputChannel="findTransactionByIdChannel")
	public NullableWrapper<FinancialTransaction> findOne(final Long id) {
		return super.findOne(id);
	}

	public List<FinancialTransaction> findBySourceOrganizationId(final Long orgId) {
		return this.getRepo().findByExtendingAgencyId(orgId);
	}
	
	@ServiceActivator(inputChannel="findTransactionBySectorCodePageableChannel")
	public Page<FinancialTransaction> findBySectorCode(final String sectorCode, 
			@Header("pageable") final Pageable pageable) {	
		return  this.getRepo().findBySectorCode(sectorCode, pageable);
		
	}
	
	@ServiceActivator(inputChannel="findTransactionByGeneralSearchPageableChannel")
	public Page<FinancialTransaction> findByGeneralSearchPageable(final String searchString,
			@Header("pageable") final Pageable pageable) {

		return  this.getRepo().findByTranslationsDescriptionContaining(searchString.toLowerCase(), pageable)  ;
	}

	
	/**
	 * @see FinancialTransactionService#findBySearchFormPageable(Integer, Category, Area, String, String, Organization, String, Pageable)
	 * @param year
	 * @param sector
	 * @param recipient
	 * @param searchString
	 * @param formType
	 * @param extendingAgency
	 * @param locale
	 * @param pageable
	 * @return
	 */
	@ServiceActivator(inputChannel = "findTransactionBySearchFormPageableChannel")
	public Page<FinancialTransaction> findBySearchFormPageable(
			@Header(value = "year", required = false) LocalDateTime year,
			@Header(value = "sector", required = false) Category sector,
			@Header(value = "recipient", required = false) Area recipient,
			@Header(value = "searchString", required = false) String searchString,
			@Header(value = "formType", required = false) String formType,			
			@Header(value = "extendingAgency", required = false) Organization extendingAgency,
		    @Header(value = "locale", required = false) String locale, 
		    Pageable pageable) {
		return this.getRepo().findBySearchFormPageable(year,sector,recipient,searchString,formType,extendingAgency,pageable);
	}
	
	/**
	 * @see FinancialTransactionService#findByReportingYearAndTypeOfFlowCode(LocalDateTime,String)
	 * @param reportingYear
	 * @return
	 */
	@ServiceActivator(inputChannel="findTransactionByReportingYearAndTypeOfFlowNonFlow")
	public List<FinancialTransaction> findByReportingYearAndTypeOfFlowCode(final LocalDateTime reportingYear, @Header("typeOfFlowCode") final String typeOfFlowCode) {
		return this.getRepo().findByReportingYearAndTypeOfFlowCode(reportingYear,typeOfFlowCode);
	}
	
	
	@Override
	protected
	FinancialTransactionRepository getRepo() {
		return this.repo;
	}
}
