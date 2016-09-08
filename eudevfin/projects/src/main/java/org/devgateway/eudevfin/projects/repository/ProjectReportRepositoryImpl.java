/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.repository;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.devgateway.eudevfin.projects.common.entities.ProjectReport;
import org.devgateway.eudevfin.projects.repository.interfaces.CustomProjectReportRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;


public class ProjectReportRepositoryImpl implements CustomProjectReportRepository {

    @PersistenceContext
    EntityManager em;

    public void setQueriesParameter(Query q1, Query q2, String name, Object value) {
        q1.setParameter(name, value);
        q2.setParameter(name, value);
    }

    @Override
    public Page<ProjectReport> findReportsByProjectID(Long id, Pageable pageable) {
        String sql = "FROM Project tx JOIN tx.projectReports pr WHERE 1=1 AND "
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
        PageImpl<ProjectReport> result = new PageImpl<ProjectReport>(resultList, pageable, maxResults);

        return result;
    }

}
