/**
 * *****************************************************************************
 * Copyright (c) 2014 Development Gateway. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the GNU
 * Public License v3.0 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * *****************************************************************************
 */
package org.devgateway.eudevfin.projects.module.forms;

import java.io.Serializable;
import org.devgateway.eudevfin.metadata.common.domain.Area;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.joda.time.LocalDateTime;

public class SearchProjectForm implements Serializable {

    private static final long serialVersionUID = 7823208128059387955L;
    private String projectName;
    private String projectType;
    private LocalDateTime startDate;
    private LocalDateTime stopDate;
    private Organization financingInstitution;
    private String implementingOrganization;
    private Area geographicFocus;

    public void reset() {
        projectName = null;
        projectType = null;
        startDate = null;
        stopDate = null;
        financingInstitution = null;
        implementingOrganization = null;
        geographicFocus = null;
    }

    public String getImplementingOrganization() {
        return implementingOrganization;
    }

    public void setImplementingOrganization(String implementingOrganization) {
        this.implementingOrganization = implementingOrganization;
    }

    public Organization getFinancingInstitution() {
        return financingInstitution;
    }

    public void setFinancingInstitution(Organization financingInstitution) {
        this.financingInstitution = financingInstitution;
    }

    public Area getGeographicFocus() {
        return geographicFocus;
    }

    public void setGeographicFocus(Area geographicFocus) {
        this.geographicFocus = geographicFocus;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getStopDate() {
        return stopDate;
    }

    public void setStopDate(LocalDateTime stopDate) {
        this.stopDate = stopDate;
    }
}
