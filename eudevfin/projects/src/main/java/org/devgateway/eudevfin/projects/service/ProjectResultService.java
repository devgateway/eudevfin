/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.service;

import org.devgateway.eudevfin.common.service.BaseEntityService;
import org.devgateway.eudevfin.common.spring.integration.NullableWrapper;
import org.devgateway.eudevfin.projects.common.entities.Project;
import org.devgateway.eudevfin.projects.common.entities.ProjectResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.integration.annotation.Header;

public interface ProjectResultService extends BaseEntityService<ProjectResult> {
    public NullableWrapper<ProjectResult> save(Project p);
    public NullableWrapper<ProjectResult> findOne(Long id);
    
    public Page<ProjectResult> findAllByProjectIDPageable(
            @Header(value = "pid", required = false) Long pid, 
            Pageable pageable);
}
