/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.repository;

import org.devgateway.eudevfin.projects.common.entities.ProjectResult;
import org.devgateway.eudevfin.projects.repository.interfaces.CustomProjectResultRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectResultRepository extends JpaRepository<ProjectResult, Long>, CustomProjectResultRepository {
}
