/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.dao;

import org.devgateway.eudevfin.common.dao.AbstractDaoImpl;
import org.devgateway.eudevfin.common.spring.integration.NullableWrapper;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.devgateway.eudevfin.projects.common.entities.ProjectReport;
import org.devgateway.eudevfin.projects.repository.ProjectReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.integration.annotation.Header;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

/**
 *
 * @author alcr
 */
@Component
@Lazy(value = false)
public class ProjectReportImpl extends AbstractDaoImpl<ProjectReport, Long, ProjectReportRepository> {

    @Autowired
    private ProjectReportRepository repo;

    @Override
    protected ProjectReportRepository getRepo() {
        return this.repo;
    }

    @ServiceActivator(inputChannel = "saveProjectReportChannel")
    public NullableWrapper<ProjectReport> saveProject(ProjectReport u) {
        return newWrapper(this.repo.save(u));
    }

    @Override
    @ServiceActivator(inputChannel = "deleteProjectReportChannel")
    public void delete(ProjectReport report) {
        super.delete(report);
    }

    /**
     * @see PersistedUserService#findOne(Long)
     */
    @Override
    @ServiceActivator(inputChannel = "findOneProjectReportChannel")
    public NullableWrapper<ProjectReport> findOne(Long id) {
        return super.findOne(id);
    }

    @ServiceActivator(inputChannel = "findReportsByProjectIDPageableChannel")
    public Page<ProjectReport> findReportsByProjectIDPageable(@Header(value = "pid", required = false) Long pid, Pageable pageable) {
        return this.getRepo().findReportsByProjectID(pid, pageable);
    }

}
