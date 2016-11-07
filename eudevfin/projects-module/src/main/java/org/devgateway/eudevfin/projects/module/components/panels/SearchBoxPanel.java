/**
 * *****************************************************************************
 * Copyright (c) 2014 Development Gateway. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the GNU
 * Public License v3.0 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * *****************************************************************************
 */
/**
 *
 */
package org.devgateway.eudevfin.projects.module.components.panels;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.auth.common.util.AuthUtils;
import org.devgateway.eudevfin.metadata.common.domain.Area;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;
import org.devgateway.eudevfin.ui.common.components.BootstrapCancelButton;
import org.devgateway.eudevfin.ui.common.components.BootstrapSubmitButton;
import org.devgateway.eudevfin.ui.common.components.DropDownField;
import org.devgateway.eudevfin.ui.common.components.TableListPanel;
import org.devgateway.eudevfin.ui.common.components.TextInputField;
import org.devgateway.eudevfin.ui.common.models.YearToLocalDateTimeModel;
import org.devgateway.eudevfin.ui.common.providers.AreaChoiceProvider;
import org.devgateway.eudevfin.ui.common.providers.CategoryProviderFactory;
import org.devgateway.eudevfin.ui.common.providers.OrganizationChoiceProvider;
import org.devgateway.eudevfin.ui.common.providers.YearProvider;
import org.joda.time.LocalDateTime;

import de.agilecoders.wicket.core.markup.html.bootstrap.form.InputBehavior;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.devgateway.eudevfin.projects.module.components.util.GeneralSearchListGenerator;
import org.devgateway.eudevfin.projects.module.forms.SearchProjectForm;
import org.devgateway.eudevfin.projects.module.providers.ProjectTypeProvider;
import org.devgateway.eudevfin.projects.service.CustomProjectService;
import org.devgateway.eudevfin.ui.common.components.tabs.BootstrapJSTabbedPanel;
import org.devgateway.eudevfin.ui.common.components.tabs.ITabWithKey;

public class SearchBoxPanel extends Panel {

    private static final long serialVersionUID = 6025430438643716484L;
    private TableListPanel<?> resultsPanel;
    private BootstrapJSTabbedPanel<ITabWithKey> allTabPanels;
    private ProjectTableListPanel projectsPanel;
    private GeneralSearchListGenerator listGenerator;
    private WebMarkupContainer searchWrapperPanel;

    private CategoryProviderFactory categoryFactory;

    private OrganizationChoiceProvider organizationProvider;
    private AreaChoiceProvider areaProvider;

    @SpringBean
    private CustomProjectService txService;
    private boolean superUser;
    private DropDownField<Organization> extendingAgency;

    /**
     * @param id
     * @param generalSearchListGenerator
     * @param categoryFactory
     * @param areaProvider
     * @param organizationProvider
     */
    public SearchBoxPanel(String id, TableListPanel<?> resultsPanel, GeneralSearchListGenerator generalSearchListGenerator,
            CategoryProviderFactory categoryFactory, OrganizationChoiceProvider organizationProvider, AreaChoiceProvider areaProvider) {
        super(id);
        this.resultsPanel = resultsPanel;
        this.listGenerator = generalSearchListGenerator;
        this.searchWrapperPanel = new WebMarkupContainer("search-results-panel-wrapper");
        this.searchWrapperPanel.setOutputMarkupId(true);
        this.categoryFactory = categoryFactory;
        this.organizationProvider = organizationProvider;
        this.areaProvider = areaProvider;
        this.populate(null);
        this.setOutputMarkupId(true);
    }

    protected void populate(String searchString) {
        this.resultsPanel.setVisible(false);
        this.searchWrapperPanel.add(this.resultsPanel);
        this.add(this.searchWrapperPanel);

        superUser = AuthUtils.currentUserHasRole(AuthConstants.Roles.ROLE_SUPERVISOR);

        final SearchProjectForm boxPanelForm = new SearchProjectForm();
        CompoundPropertyModel<SearchProjectForm> boxPanelFormModel = new CompoundPropertyModel<SearchProjectForm>(boxPanelForm);
        Form<?> form = new Form<>("searchForm", boxPanelFormModel);
        form.setOutputMarkupId(false);

        final TextInputField<String> projectName = addProjectName(form);
        final DropDownField<String> projectType = addProjectType(form);
        final DropDownField<Integer> startDate = addYear(form, "startDate");
        final DropDownField<Integer> stopDate = addYear(form, "stopDate");
        final TextInputField<String> implOrganization = addImplOrganization(form);
        final DropDownField<Area> geographicFocus = addGeoFocus(form);

        extendingAgency = new DropDownField<>("financingInstitution",
                new RWComponentPropertyModel<Organization>("financingInstitution"), organizationProvider);
        //extendingAgency.setSize(InputBehavior.Size.Medium);
        extendingAgency.hideLabel();
        extendingAgency.removeSpanFromControlGroup();
        form.add(extendingAgency);

        BootstrapSubmitButton submitButton = new BootstrapSubmitButton("submit", new StringResourceModel("desktop.searchbutton", this, null)) {
            private static final long serialVersionUID = -1342816632002116152L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                boolean isVisible;
                if ((boxPanelForm.getProjectName() != null && boxPanelForm.getProjectName().length() > 0)
                        || (boxPanelForm.getImplementingOrganization() != null && boxPanelForm.getImplementingOrganization().length() > 0)
                        || boxPanelForm.getStartDate() != null || boxPanelForm.getStopDate() != null
                        || boxPanelForm.getProjectType() != null || boxPanelForm.getFinancingInstitution() != null
                        || boxPanelForm.getGeographicFocus() != null) {

                    SearchBoxPanel.this.listGenerator.setSearchBoxPanelForm(boxPanelForm);
                    SearchBoxPanel.this.resultsPanel.generateListOfItems(1);
                    SearchBoxPanel.this.resultsPanel.setVisible(true);

                    isVisible = false;
                } else {
                    SearchBoxPanel.this.resultsPanel.setVisible(false);
                    isVisible = true;
                }
                
                if (allTabPanels != null) {
                    SearchBoxPanel.this.allTabPanels.setVisible(isVisible);
                    target.add(SearchBoxPanel.this.allTabPanels);
                } else if (projectsPanel != null) {
                    SearchBoxPanel.this.projectsPanel.setVisible(isVisible);
                    target.add(SearchBoxPanel.this.projectsPanel);
                }
 
                target.add(SearchBoxPanel.this.searchWrapperPanel);
            }

        };
        form.add(submitButton);

        BootstrapCancelButton resetButton = new BootstrapCancelButton("reset", new StringResourceModel("desktop.resetbutton", this, null)) {
            private static final long serialVersionUID = -7554180087300408868L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                startDate.getField().setDefaultModelObject(null);
                stopDate.getField().setDefaultModelObject(null);
                extendingAgency.getField().setDefaultModelObject(superUser ? null : AuthUtils.getOrganizationForCurrentUser());
                projectName.getField().setDefaultModelObject(null);
                implOrganization.getField().setDefaultModelObject(null);
                projectType.getField().setDefaultModelObject(null);
                geographicFocus.getField().setDefaultModelObject(null);
                target.add(startDate.getField());
                target.add(stopDate.getField());
                target.add(projectName.getField());
                target.add(extendingAgency.getField());
                target.add(implOrganization.getField());
                target.add(projectType.getField());
                target.add(geographicFocus.getField());

                if (allTabPanels != null) {
                    SearchBoxPanel.this.allTabPanels.setVisible(true);
                    target.add(SearchBoxPanel.this.allTabPanels);
                } else if (projectsPanel != null) {
                    SearchBoxPanel.this.projectsPanel.setVisible(true);
                    target.add(SearchBoxPanel.this.projectsPanel);
                }
                
                SearchBoxPanel.this.resultsPanel.setVisible(false);
                target.add(SearchBoxPanel.this.searchWrapperPanel);
            }

        };
        form.add(resetButton);

        this.add(form);
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        if (!superUser) {
            extendingAgency.setEnabled(false);
            extendingAgency.getField().setDefaultModelObject(AuthUtils.getOrganizationForCurrentUser());
        }
    }

    /**
     * Creates and adds the project name field into form.
     *
     * @param form the form in which it add the field
     * @return the field created
     */
    private TextInputField<String> addProjectName(Form<?> form) {
        final TextInputField<String> projectName = new TextInputField<String>("projectName",
                new RWComponentPropertyModel<String>("projectName"), "desktop.search");
        projectName.typeString();
        projectName.setSize(InputBehavior.Size.Large);
        projectName.hideLabel();
        projectName.removeSpanFromControlGroup();
        form.add(projectName);

        return projectName;
    }

    /**
     * Creates and adds the start and stop year field into form.
     *
     * @param form the form in which it add the field
     * @param yearID the wicketID which can be startYear or stopYear
     * @return the field created
     */
    private DropDownField<Integer> addYear(Form<?> form, String yearID) {
        List<Integer> years = new ArrayList<Integer>();

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        //getTime() returns the current date in default time zone
        Date date = calendar.getTime();
        int currentYear = 2007;
        int totalTime = currentYear + 20;

        for (int i = currentYear; i < totalTime; i++) {
            years.add(i);
        }

        final DropDownField<Integer> yearField = new DropDownField<Integer>(yearID,
                new YearToLocalDateTimeModel(new RWComponentPropertyModel<LocalDateTime>(
                                yearID)), new YearProvider(years));
        yearField.setSize(InputBehavior.Size.Medium);
        yearField.removeSpanFromControlGroup();
        yearField.hideLabel();
        form.add(yearField);

        return yearField;
    }

    /**
     * Creates and adds the areas field into form.
     *
     * @param form the form in which it add the field
     * @return the field created
     */
    private DropDownField<Area> addGeoFocus(Form<?> form) {
        final DropDownField<Area> areas = new DropDownField<Area>("geographicFocus",
                new RWComponentPropertyModel<Area>("geographicFocus"), areaProvider);
        areas.setSize(InputBehavior.Size.Large);
        areas.hideLabel();
        areas.removeSpanFromControlGroup();
        form.add(areas);

        return areas;
    }

    /**
     * Creates and adds the status field into form.
     *
     * @param form the form in which it add the field
     * @return the field created
     */
    private TextInputField<String> addImplOrganization(Form<?> form) {
        final TextInputField<String> institution = new TextInputField<String>("implementingOrganization",
                new RWComponentPropertyModel<String>("implementingOrganization"), "implementingOrganization");
        institution.typeString();
        institution.setSize(InputBehavior.Size.Large);
        institution.hideLabel();
        institution.removeSpanFromControlGroup();
        form.add(institution);

        return institution;
    }

    /**
     * Creates and adds the areas field into form.
     *
     * @param form the form in which it add the field
     * @return the field created
     */
    private DropDownField<String> addProjectType(Form<?> form) {
        ProjectTypeProvider provider = new ProjectTypeProvider(this);

        final DropDownField<String> projectType = new DropDownField<>("projectType",
                new RWComponentPropertyModel<String>("projectType"), provider);
        projectType.setSize(InputBehavior.Size.Medium);
        projectType.hideLabel();
        projectType.removeSpanFromControlGroup();
        form.add(projectType);

        return projectType;
    }

    /**
     * Adds a reference of AllProjectsPanel
     *
     * @param allProjectsPanel
     */
    public void setTabPanels(BootstrapJSTabbedPanel<ITabWithKey> allProjectsPanel) {
        this.allTabPanels = allProjectsPanel;
        this.allTabPanels.setOutputMarkupId(true);
        this.allTabPanels.setOutputMarkupPlaceholderTag(true);
    }
    
        /**
     * Adds a reference of AllProjectsPanel
     *
     * @param allProjectsPanel
     */
    public void setProjectPanel(ProjectTableListPanel projectsPanel) {
        this.projectsPanel = projectsPanel;
        this.projectsPanel.setOutputMarkupId(true);
        this.projectsPanel.setOutputMarkupPlaceholderTag(true);
    }
}
