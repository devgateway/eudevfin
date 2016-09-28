/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.dao;

import org.devgateway.eudevfin.common.dao.AbstractDaoImpl;
import org.devgateway.eudevfin.common.spring.integration.NullableWrapper;
import org.devgateway.eudevfin.projects.common.entities.ProjectReport;
import org.devgateway.eudevfin.projects.common.entities.ProjectResult;
import org.devgateway.eudevfin.projects.repository.ProjectResultRepository;
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
public class ProjectResultsImpl extends AbstractDaoImpl<ProjectResult, Long, ProjectResultRepository> {

    @Autowired
    private ProjectResultRepository repo;

    @Override
    protected ProjectResultRepository getRepo() {
        return this.repo;
    }

    @ServiceActivator(inputChannel = "saveProjectResultChannel")
    public NullableWrapper<ProjectResult> saveProject(ProjectResult u) {
        return newWrapper(this.repo.save(u));
    }

    @Override
    @ServiceActivator(inputChannel = "deleteProjectResultChannel")
    public void delete(ProjectResult result) {
        super.delete(result);
    }

    /**
     * @see PersistedUserService#findOne(Long)
     */
    @Override
    @ServiceActivator(inputChannel = "findOneProjectResultChannel")
    public NullableWrapper<ProjectResult> findOne(Long id) {
        return super.findOne(id);
    }

    @ServiceActivator(inputChannel = "findAllByProjectIDPageableChannel")
    public Page<ProjectResult> findAllByProjectIDPageable(@Header(value = "pid", required = false) Long pid, Pageable pageable) {
        return this.getRepo().findAllByProjectID(pid, pageable);
    }

}
