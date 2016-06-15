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
package org.devgateway.eudevfin.financial.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.metadata.common.domain.Area;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.joda.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

/**
 * @author mihai
 *
 */
public class CustomFinancialTransactionRepositoryImpl implements CustomFinancialTransactionRepositoryCustom {

    @PersistenceContext
    EntityManager em;

	// @Query("select tx from FinancialTransaction tx join tx.translations trn where 
    // (lower(trn.description) like %:searchString% or lower(trn.shortDescription) like %:searchString%) or "
    // +
    // "(:year not null and tx.reportingYear=:year) or tx.sector=:sector or tx.recipient=:recipient or tx.formType=:formType or tx.extendingAgency=:extendingAgency")
    //
    /* (non-Javadoc)
     * @see org.devgateway.eudevfin.financial.repository.SearchCustomFinancialTransaction#findBySearchFormPageable(org.joda.time.LocalDateTime, org.devgateway.eudevfin.metadata.common.domain.Category, org.devgateway.eudevfin.metadata.common.domain.Area, java.lang.String, java.lang.String, org.devgateway.eudevfin.metadata.common.domain.Organization, org.springframework.data.domain.Pageable)
     */
    public void setQueriesParameter(Query q1, Query q2, String name, Object value) {
        q1.setParameter(name, value);
        q2.setParameter(name, value);
    }

    @Override
    public Page<CustomFinancialTransaction> performSearch(LocalDateTime year, Category sector,
            Area recipient, String searchString, String formType, Organization extendingAgency, Pageable pageable) {
        StringBuilder queryBuilder = new StringBuilder(" FROM CustomFinancialTransaction tx join tx.translations trn WHERE 1=1");

        if (year != null) {
            queryBuilder.append(" AND tx.reportingYear=:year");
        }
        if (sector != null) {
            queryBuilder.append(" AND tx.sector=:sector");
        }
        if (recipient != null) {
            queryBuilder.append(" AND tx.recipient=:recipient");
        }
        if (searchString != null) {
            queryBuilder.append(" AND (lower(trn.description) like :searchString or lower(trn.shortDescription) like :searchString )");
        }
        if (formType != null) {
            queryBuilder.append(" AND tx.formType=:formType");
        }
        if (extendingAgency != null) {
            queryBuilder.append(" AND tx.extendingAgency=:extendingAgency");
        }

        Query query = em.createQuery("SELECT tx " + queryBuilder.toString());
        query.setMaxResults(pageable.getPageSize());
        query.setFirstResult(pageable.getOffset());

        StringBuilder countBuilder = new StringBuilder("SELECT count(tx) ").append(queryBuilder);
        Query countQuery = em.createQuery(countBuilder.toString());

        if (year != null) {
            setQueriesParameter(query, countQuery, "year", year);
        }
        if (sector != null) {
            setQueriesParameter(query, countQuery, "sector", sector);
        }
        if (recipient != null) {
            setQueriesParameter(query, countQuery, "recipient", recipient);
        }
        if (searchString != null) {
            setQueriesParameter(query, countQuery, "searchString", "%" + searchString.toLowerCase() + "%");
        }
        if (formType != null) {
            setQueriesParameter(query, countQuery, "formType", formType);
        }
        if (extendingAgency != null) {
            setQueriesParameter(query, countQuery, "extendingAgency", extendingAgency);
        }

        long maxResults = (Long) countQuery.getSingleResult();

        @SuppressWarnings("rawtypes")
        List resultList = query.getResultList();

        @SuppressWarnings("unchecked")
        PageImpl<CustomFinancialTransaction> result = new PageImpl<CustomFinancialTransaction>(resultList, pageable, maxResults);
        return result;
    }

    @Override
    public Page<CustomFinancialTransaction> performSearchByDonorIdCrsIdActive(String donorIdSearch, String crsIdSearch, Boolean active,
            String locale, Pageable pageable) {
        StringBuilder queryBuilder = new StringBuilder(" FROM CustomFinancialTransaction tx WHERE 1=1");

        if (crsIdSearch != null) {
            queryBuilder.append(" AND tx.crsIdentificationNumber like :crsIdSearch");
        }
        if (donorIdSearch != null) {
            queryBuilder.append(" AND tx.donorProjectNumber like :donorIdSearch");
        }
        if (active) {
            queryBuilder.append(" AND tx.expectedCompletionDate > :systemDate");
        }

        Query query = em.createQuery("SELECT tx " + queryBuilder.toString());
        query.setMaxResults(pageable.getPageSize());
        query.setFirstResult(pageable.getOffset());

        StringBuilder countBuilder = new StringBuilder("SELECT count(tx) ").append(queryBuilder);
        Query countQuery = em.createQuery(countBuilder.toString());

        if (crsIdSearch != null) {
            setQueriesParameter(query, countQuery, "crsIdSearch", crsIdSearch);
        }
        if (donorIdSearch != null) {
            setQueriesParameter(query, countQuery, "donorIdSearch", donorIdSearch);
        }
        if (active) {
            setQueriesParameter(query, countQuery, "systemDate", LocalDateTime.now());
        }

        long maxResults = (Long) countQuery.getSingleResult();
        @SuppressWarnings("rawtypes")
        List resultList = query.getResultList();

        @SuppressWarnings("unchecked")
        PageImpl<CustomFinancialTransaction> result = new PageImpl<CustomFinancialTransaction>(resultList, pageable, maxResults);
        return result;
    }

    @Override
    public Page<FinancialTransaction> findTransactionsByProjectID(Long id, Pageable pageable) {
        String sql = "FROM Project tx JOIN tx.projectTransactions pr WHERE 1=1 AND "
                + "tx.id=:id";

        Query query = em.createQuery("SELECT pr " + sql);
        query.setMaxResults(pageable.getPageSize());
        query.setFirstResult(pageable.getOffset());

        Query countQuery = em.createQuery("SELECT count(pr) " + sql);

        if (id != null) {
            setQueriesParameter(query, countQuery, "id", id);
        }

        long maxResults = (Long) countQuery.getSingleResult();

        @SuppressWarnings("rawtypes")
        List resultList = query.getResultList();

        @SuppressWarnings("unchecked")
        PageImpl<FinancialTransaction> result = new PageImpl<FinancialTransaction>(resultList, pageable, maxResults);

        return result;
    }

}
