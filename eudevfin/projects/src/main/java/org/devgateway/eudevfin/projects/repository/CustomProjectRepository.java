/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.repository;

import org.devgateway.eudevfin.projects.repository.interfaces.CustomProjectRepositoryCustom;
import org.devgateway.eudevfin.projects.common.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomProjectRepository extends
		JpaRepository<Project, Long>, CustomProjectRepositoryCustom {
}
