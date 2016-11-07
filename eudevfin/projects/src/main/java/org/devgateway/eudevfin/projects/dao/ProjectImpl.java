/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.dao;

import java.util.List;
import org.devgateway.eudevfin.common.dao.AbstractDaoImpl;
import org.devgateway.eudevfin.common.spring.integration.NullableWrapper;
import org.devgateway.eudevfin.projects.common.entities.Project;
import org.devgateway.eudevfin.projects.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

@Component
@Lazy(value = false)
public class ProjectImpl extends AbstractDaoImpl<Project, Long, ProjectRepository> {

    @Autowired
    private ProjectRepository repo;

    @Override
    protected ProjectRepository getRepo() {
        return repo;
    }

    @Override
    public List<Project> findAllAsList() {
        return super.findAllAsList();
    }

    @Override
    @ServiceActivator(inputChannel = "deleteProjectChannel")
    public void delete(Project e) {
        // TODO Auto-generated method stub
        super.delete(e);
    }

    @Override
    @ServiceActivator(inputChannel = "saveProjectChannel")
    public NullableWrapper<Project> save(Project u) {
        return super.save(u);
    }

    @Override
    @ServiceActivator(inputChannel = "findOneProjectChannel")
    public NullableWrapper<Project> findOne(final Long id) {
        return super.findOne(id);
    }

    @ServiceActivator(inputChannel = "findByNameChannel")
    public List<Project> findByName(String name) {
        return this.getRepo().findByName(name);
    }

    @ServiceActivator(inputChannel = "findByTypeChannel")
    public List<Project> findByType(String type) {
        return this.getRepo().findByType(type);
    }
}
