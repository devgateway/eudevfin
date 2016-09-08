/**
 * *****************************************************************************
 * Copyright (c) 2014 Development Gateway. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the GNU
 * Public License v3.0 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * *****************************************************************************
 */
package org.devgateway.eudevfin.projects.module.components.util;

import java.util.logging.*;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.auth.common.util.AuthUtils;
import org.devgateway.eudevfin.common.service.PagingHelper;
import org.devgateway.eudevfin.projects.common.entities.Project;
import org.devgateway.eudevfin.projects.module.forms.SearchProjectForm;
import org.devgateway.eudevfin.projects.service.CustomProjectService;
import org.devgateway.eudevfin.ui.common.components.util.ListGeneratorInterface;
import org.springframework.data.domain.PageRequest;

public class GeneralSearchListGenerator implements ListGeneratorInterface<Project> {

    private static final long serialVersionUID = 2851842899904434912L;
    private SearchProjectForm searchProjectForm;
    private final CustomProjectService projectService;
    private boolean isCustom;
    private String projectStatus; 

    /**
     * Constructor
     *
     * @param custom if is true will make a search, if false will get all
     * projects.
     * @param projectService the service that is in charge with interrogation of
     * db.
     */
    public GeneralSearchListGenerator(boolean custom,
            CustomProjectService projectService, String status) {
        super();
        this.projectService = projectService;
        this.isCustom = custom;
        this.projectStatus = status;
    }

    @Override
    public PagingHelper<Project> getResultsList(int pageNumber, int pageSize) {
        Logger.getLogger("LOG").log(Level.INFO, "GetResult Before Check");

        boolean isAdmin = AuthUtils.currentUserHasRole(AuthConstants.Roles.ROLE_SUPERVISOR);

        if (!isCustom && isAdmin) {
            return PagingHelper.createPagingHelperFromPage(projectService.findAllProjectsPageable(new PageRequest(pageNumber - 1, pageSize)));
        }

        if (!isCustom && !isAdmin) {
            return PagingHelper.createPagingHelperFromPage(
                    this.projectService.findBySearchFormPageable(null, null, null, null,
                            AuthUtils.getOrganizationForCurrentUser(), null, null, projectStatus,
                            new PageRequest(pageNumber - 1, pageSize)));
        }

        if (searchProjectForm != null) {
            Logger.getLogger("LOG").log(Level.INFO, "GetResult SerchProjectForm");
            return PagingHelper.createPagingHelperFromPage(
                    this.projectService.findBySearchFormPageable(
                            searchProjectForm.getProjectName(),
                            searchProjectForm.getProjectType(),
                            searchProjectForm.getStartDate(),
                            searchProjectForm.getStopDate(),
                            searchProjectForm.getFinancingInstitution(),
                            searchProjectForm.getImplementingOrganization(),
                            searchProjectForm.getGeographicFocus(), null,
                            new PageRequest(pageNumber - 1, pageSize)));
        } else {
            Logger.getLogger("LOG").log(Level.INFO, "GetResult Is null");
            return null;
        }
    }

    /**
     * @return the searchBoxPanelForm
     */
    public SearchProjectForm getSearchProjectForm() {
        return searchProjectForm;
    }

    /**
     * @param searchBoxPanelForm the searchBoxPanelForm to set
     */
    public void setSearchBoxPanelForm(SearchProjectForm searchBoxPanelForm) {
        this.searchProjectForm = searchBoxPanelForm;
    }
}
