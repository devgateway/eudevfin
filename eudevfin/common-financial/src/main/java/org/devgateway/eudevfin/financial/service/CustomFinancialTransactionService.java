/**
 * *****************************************************************************
 * Copyright (c) 2014 Development Gateway. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the GNU
 * Public License v3.0 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************
 */
/**
 *
 */
package org.devgateway.eudevfin.financial.service;

import java.util.Collection;
import java.util.List;

import org.devgateway.eudevfin.auth.common.domain.PersistedUserGroup;
import org.devgateway.eudevfin.common.service.BaseEntityService;
import org.devgateway.eudevfin.common.spring.integration.NullableWrapper;
import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.metadata.common.domain.Area;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.joda.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.integration.annotation.Header;
import org.springframework.integration.annotation.Payload;

/**
 * @author Alex,mihai
 *
 */
public interface CustomFinancialTransactionService extends BaseEntityService<CustomFinancialTransaction> {

    public Page<CustomFinancialTransaction> findByDraftAndPersistedUserGroupPageable(Boolean draft,
            @Header("persistedUserGroup") PersistedUserGroup persistedUserGroup, @Header("pageable") Pageable pageable);

    public Page<CustomFinancialTransaction> findByApprovedAndPersistedUserGroupPageable(Boolean approved,
            @Header("persistedUserGroup") PersistedUserGroup persistedUserGroup, @Header("pageable") Pageable pageable);

    public Page<CustomFinancialTransaction> findByDraftPageable(final Boolean draft,
            @Header("pageable") final Pageable pageable);

    public Page<CustomFinancialTransaction> findByApprovedPageable(final Boolean draft,
            @Header("pageable") final Pageable pageable);

    public List<CustomFinancialTransaction> findByReportingYearAndDraftFalse(final Integer year);

    public List<CustomFinancialTransaction> findByReportingYearAndDraftFalseAndFormTypeNotIn(final Integer year,
            @Header("notFormType") Collection<String> notFormType);

    public List<CustomFinancialTransaction> findByReportingYearAndApprovedTrueAndFormTypeIn(final Integer year,
            @Header("notFormType") Collection<String> notFormType);

    public List<CustomFinancialTransaction> findByApprovedTrueAndFormTypeInOrderByCrsIdAscCreatedDateAsc(Collection<String> notFormType);

    @Payload("new java.util.Date()")
    public List<Integer> findAllDistinctReportingYears();

    @Payload("new java.util.Date()")
    public List<Integer> findDistinctStartingYears();

    @Payload("new java.util.Date()")
    public List<Integer> findDistinctCompletitionYears();

    @Payload("new java.lang.String()")
    public List<String> findDistinctReportingGeopraphy();

    @Payload("new java.lang.String()")
    public List<String> findDistinctDonorProjectNumber();

    @Payload("new java.lang.String()")
    public List<String> findDistinctCRSId();

    public Page<FinancialTransaction> findBySearchFormPageable(
            @Header(value = "year", required = false) LocalDateTime year,
            @Header(value = "sector", required = false) Category sector,
            @Header(value = "recipient", required = false) Area recipient,
            @Header(value = "searchString", required = false) String searchString,
            @Header(value = "formType", required = false) String formType,
            @Header(value = "extendingAgency", required = false) Organization extendingAgency,
            @Header(value = "locale", required = false) String locale,
            Pageable pageable);

    public Page<FinancialTransaction> findByDonorIdCrsIdActive(
            @Header(value = "donorIdSearch", required = false) String donorIdSearch,
            @Header(value = "crsIdSearch", required = false) String crsIdSearch,
            @Header(value = "active", required = false) Boolean active,
            @Header(value = "locale", required = false) String locale,
            Pageable pageable);
   
    public Page<FinancialTransaction> findTransactionsByProjectIDPageable(
            @Header(value = "pid", required = false) Long pid, 
            Pageable pageable);
    
    public List<Integer> findUsedOrgByGeographicFocusAndFinancingInstitution();
	
}
