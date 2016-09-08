/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.repository;

import org.devgateway.eudevfin.projects.repository.interfaces.CustomProjectRepositoryCustom;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.devgateway.eudevfin.metadata.common.domain.Area;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.devgateway.eudevfin.projects.common.entities.Project;
import org.joda.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class CustomProjectRepositoryImpl implements CustomProjectRepositoryCustom {

    @PersistenceContext
    EntityManager em;

    public void setQueriesParameter(Query q1, Query q2, String name, Object value) {
        q1.setParameter(name, value);
        q2.setParameter(name, value);
    }

    @Override
    public Page<Project> findAllProjects(Pageable pageable) {
        Query query = em.createQuery("FROM Project");
        query.setMaxResults(pageable.getPageSize());
        query.setFirstResult(pageable.getOffset());

        @SuppressWarnings("rawtypes")
        List resultList = query.getResultList();

        Query countQuery = em.createQuery("SELECT count(*) FROM Project");

        long maxResults = (Long) countQuery.getSingleResult();

        @SuppressWarnings("unchecked")
        PageImpl<Project> result = new PageImpl<Project>(resultList, pageable, maxResults);

        return result;
    }

    @Override
    public Page<Project> performSearch(String name, String type, LocalDateTime startDate, LocalDateTime stopDate,
            Organization financingInstitution, String implementingOrganization, Area geographicFocus,
            String status, Pageable pageable) {

        StringBuilder queryBuilder = new StringBuilder("FROM Project pr JOIN pr.areas ar WHERE 1=1");

        LocalDateTime startFrom = null, stopFrom = null, startTo = null, stopTo = null;

        if (name != null) {
            queryBuilder.append(" AND (lower(pr.name) like :name)");
        }

        if (type != null) {
            queryBuilder.append(" AND pr.type=:type");
        }

        if (startDate != null) {
            startFrom = new LocalDateTime(startDate.getYear(), 1, 1, 0, 0);
            startTo = new LocalDateTime(startDate.getYear(), 12, 31, 0, 0);
            queryBuilder.append(" AND pr.startDate BETWEEN :startFrom AND :startTo");
        }

        if (stopDate != null) {
            stopFrom = new LocalDateTime(stopDate.getYear(), 1, 1, 0, 0);
            stopTo = new LocalDateTime(stopDate.getYear(), 12, 31, 0, 0);
            queryBuilder.append(" AND pr.stopDate BETWEEN :stopFrom AND :stopTo");
        }

        if (financingInstitution != null) {
            queryBuilder.append(" AND pr.extendingAgency=:financingInstitution");
        }

        if (implementingOrganization != null) {
            queryBuilder.append(" AND (lower(pr.implementingOrganization) like :implOrg)");
        }

        if (geographicFocus != null) {
            queryBuilder.append(" AND ar.code=:code");
        }
        
        if (status != null) {
            queryBuilder.append(" AND pr.status=:status");
        }

        Query query = em.createQuery("SELECT pr " + queryBuilder.toString());
        query.setMaxResults(pageable.getPageSize());
        query.setFirstResult(pageable.getOffset());

        Query countQuery = em.createQuery("SELECT count(pr) " + queryBuilder.toString());

        if (name != null) {
            setQueriesParameter(query, countQuery, "name", "%" + name.toLowerCase() + "%");
        }
        if (type != null) {
            setQueriesParameter(query, countQuery, "type", type);
        }
        if (startDate != null) {
            setQueriesParameter(query, countQuery, "startFrom", startFrom);
            setQueriesParameter(query, countQuery, "startTo", startTo);
        }
        if (stopDate != null) {
            setQueriesParameter(query, countQuery, "stopFrom", stopFrom);
            setQueriesParameter(query, countQuery, "stopTo", stopTo);
        }
        if (financingInstitution != null) {
            setQueriesParameter(query, countQuery, "financingInstitution", financingInstitution);
        }
        if (implementingOrganization != null) {
            setQueriesParameter(query, countQuery, "implOrg", "%"
                    + implementingOrganization.toLowerCase() + "%");
        }
        if (geographicFocus != null) {
            setQueriesParameter(query, countQuery, "code", geographicFocus.getCode());
        }
        if (status != null) {
            setQueriesParameter(query, countQuery, "status", status);
        }

        long maxResults = (Long) countQuery.getSingleResult();

        @SuppressWarnings("rawtypes")
        List resultList = query.getResultList();

        @SuppressWarnings("unchecked")
        PageImpl<Project> result = new PageImpl<Project>(resultList, pageable, maxResults);

        return result;
    }

    @Override
    public List<Project> findAllByReportDate(LocalDateTime date) {
//        LocalDateTime from = new LocalDateTime(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(), 0, 0);
//        LocalDateTime to = new LocalDateTime(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(), 0, 0);

        String queryBuilder = "FROM Project tx JOIN tx.projectReports pr WHERE 1=1 AND pr.reportDate BETWEEN :begin AND :end AND pr.emailSent=false";
        Query query = em.createQuery("SELECT tx " + queryBuilder);

        if (date != null) {
            query.setParameter("begin", date.toLocalDate().toDateTimeAtStartOfDay().toLocalDateTime());
            query.setParameter("end", date.toLocalDate().plusDays(1).toDateTimeAtStartOfDay().toLocalDateTime());
        }

        @SuppressWarnings("rawtypes")
        List resultList = query.getResultList();

        return resultList;
    }
}
