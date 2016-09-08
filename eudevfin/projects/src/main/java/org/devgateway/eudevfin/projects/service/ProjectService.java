/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.service;

import java.util.List;
import org.devgateway.eudevfin.common.service.BaseEntityService;
import org.devgateway.eudevfin.common.spring.integration.NullableWrapper;
import org.devgateway.eudevfin.projects.common.entities.Project;


public interface ProjectService extends BaseEntityService<Project> {
    public List<Project> findByName(String name);
    public List<Project> findByType(String type);    
    public NullableWrapper<Project> save(Project p);
    public NullableWrapper<Project> findOne(Long id);
}
