/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.dao;

import java.util.List;
import org.devgateway.eudevfin.common.dao.AbstractDaoImpl;
import org.devgateway.eudevfin.metadata.common.domain.Area;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.devgateway.eudevfin.projects.common.entities.Project;
import org.devgateway.eudevfin.projects.repository.CustomProjectRepository;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.integration.annotation.Header;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

@Component
@Lazy(value = false)
public class CustomProjectImpl extends AbstractDaoImpl<Project, Long, CustomProjectRepository> {

    @Autowired
    private CustomProjectRepository repo;

    @Override
    protected CustomProjectRepository getRepo() {
        return this.repo;
    }

    @ServiceActivator(inputChannel = "findProjectBySearchFormPageableChannel")
    public Page<Project> findBySearchFormPageable(
            @Header(value = "name", required = false) String name,
            @Header(value = "type", required = false) String type,
            @Header(value = "startDate", required = false) LocalDateTime startDate,
            @Header(value = "stopDate", required = false) LocalDateTime stopDate,
            @Header(value = "financingInstitution", required = false) Organization financingInstitution,
            @Header(value = "implementingOrganization", required = false) String implementingOrganization,
            @Header(value = "geographicFocus", required = false) Area geographicFocus,
            @Header(value = "status", required = false) String status,
            Pageable pageable) {
        return this.getRepo().performSearch(name, type, startDate, stopDate, financingInstitution, 
                implementingOrganization, geographicFocus, status, pageable);
    }

    @ServiceActivator(inputChannel = "findAllProjectsPageableChannel")
    public Page<Project> findAllProjectsPageable(Pageable pageable) {
        return this.getRepo().findAllProjects(pageable);
    }
    
    @ServiceActivator(inputChannel = "findAllByReportDateChannel")
    public List<Project> findAllByReportDate(LocalDateTime date) {
        return this.getRepo().findAllByReportDate(date);
    }
}
