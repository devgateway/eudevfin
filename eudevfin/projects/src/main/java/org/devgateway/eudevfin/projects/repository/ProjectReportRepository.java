/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.repository;

import org.devgateway.eudevfin.projects.common.entities.ProjectReport;
import org.devgateway.eudevfin.projects.repository.interfaces.CustomProjectReportRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectReportRepository extends JpaRepository<ProjectReport, Long>, CustomProjectReportRepository {
}
