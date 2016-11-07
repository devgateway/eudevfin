/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.service;

import java.util.List;
import org.devgateway.eudevfin.common.service.BaseEntityService;
import org.devgateway.eudevfin.metadata.common.domain.Area;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.devgateway.eudevfin.projects.common.entities.Project;
import org.joda.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.integration.annotation.Header;

public interface CustomProjectService extends BaseEntityService<Project> {

    public Page<Project> findBySearchFormPageable(
            @Header(value = "name", required = false) String name,
            @Header(value = "type", required = false) String type,
            @Header(value = "startDate", required = false) LocalDateTime startDate,
            @Header(value = "stopDate", required = false) LocalDateTime stopDate,
            @Header(value = "financingInstitution", required = false) Organization financingInstitution,
            @Header(value = "implementingOrganization", required = false) String implementingOrganization,
            @Header(value = "geographicFocus", required = false) Area geographicFocus,
            @Header(value = "status", required = false) String status,
            Pageable pageable);

    public Page<Project> findAllProjectsPageable(Pageable pageable);
    
    public List<Project> findAllByReportDate(LocalDateTime date);
}
