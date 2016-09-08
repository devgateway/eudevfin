/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.repository.interfaces;

import java.util.List;
import org.devgateway.eudevfin.metadata.common.domain.Area;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.devgateway.eudevfin.projects.common.entities.Project;
import org.joda.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomProjectRepositoryCustom {

    public Page<Project> performSearch(
            String name,
            String type,
            LocalDateTime startDate,
            LocalDateTime stopDate,
            Organization financingInstitution,
            String implementingOrganization,
            Area geographicFocus, 
            String Status,
            Pageable pageable);

    public Page<Project> findAllProjects(Pageable pageable);
    
    public List<Project> findAllByReportDate(LocalDateTime date);
}
