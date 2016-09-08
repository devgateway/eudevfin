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

import java.util.logging.Level;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.eudevfin.projects.common.entities.Project;
import org.devgateway.eudevfin.projects.module.pages.NewProjectPage;
import org.devgateway.eudevfin.projects.module.pages.ViewCustomProjectPage;
import org.devgateway.eudevfin.ui.common.components.TableListPanel;
import org.devgateway.eudevfin.ui.common.components.tabs.AbstractTabWithKey;
import org.devgateway.eudevfin.ui.common.components.tabs.ITabWithKey;
import org.devgateway.eudevfin.ui.common.components.util.ListGeneratorInterface;
import org.devgateway.eudevfin.ui.common.models.ProxyModel;

public class ProjectTableListPanel<T extends Project> extends TableListPanel<T> {

    private static final long serialVersionUID = -3321918474405239409L;

    private static final Logger logger = Logger.getLogger(ProjectTableListPanel.class);

    /**
     * @param id
     * @param listGenerator
     */
    public ProjectTableListPanel(final String id,
            final ListGeneratorInterface<T> listGenerator) {
        super(id, listGenerator);
        // TODO Auto-generated constructor stub
    }

    public static <R extends Project> ITabWithKey newTab(
            final Component askingComponent,
            final String key,
            final ListGeneratorInterface<R> listGenerator) {

        return new AbstractTabWithKey(new StringResourceModel(key,
                askingComponent, null), key) {

                    private static final long serialVersionUID = -3378395518127823461L;

                    @Override
                    public WebMarkupContainer getPanel(final String panelId) {
                        return new ProjectTableListPanel<R>(panelId, listGenerator);
                    }
                };
    }

    protected Link getProjectEditLink(final Project project) {
        return new Link("project-edit-link") {
            private static final long serialVersionUID = 9084184744700618410L;

            @Override
            public void onClick() {
                logger.info("Clicked edit on " + this.getModelObject());
                final PageParameters pageParameters = new PageParameters();
                java.util.logging.Logger.getLogger("LOG").log(Level.INFO, "GetResult size " + project.getId());
                pageParameters.add(NewProjectPage.PARAM_PROJECT_ID, project.getId());

                this.setResponsePage(NewProjectPage.class, pageParameters);
            }
        };
    }

    protected void populateHeader() {
        this.add(generateLabel("txtable.tx.name", "project-name-label", this, null));
        this.add(generateLabel("txtable.tx.type", "project-type-label", this, null));
        this.add(generateLabel("txtable.tx.start", "project-start-year-label", this, null));
        this.add(generateLabel("txtable.tx.stop", "project-stop-year-label", this, null));
        this.add(generateLabel("txtable.tx.financing", "project-financing-label", this, null));
        this.add(generateLabel("txtable.tx.organization", "project-organization-label", this, null));
        this.add(generateLabel("txtable.tx.geofocus", "project-geofocus-label", this, null));
        this.add(generateLabel("txtable.tx.actions", "project-actions-label", this, null));

    }

    @Override
    protected void populateTable() {
        this.itemsListView = new ListView<T>("project-list", this.items) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final ListItem<T> ftListItem) {
                final Project project = ftListItem.getModelObject();
                final Label nameLabel = new Label("project-name", project.getName());
                ftListItem.add(nameLabel);

                String typeName = new StringResourceModel(project.getType() == null ? "unknown" : project.getType(), 
                        ProjectTableListPanel.this, null).getString();
                final Label formType = new Label("project-type", typeName);
                ftListItem.add(formType);

                int startDate = (project.getStartDate() == null ? 2001 : project.getStartDate().getYear());
                final Label startYearLabel = new Label("project-start-year", startDate);
                ftListItem.add(startYearLabel);

                int stopDate = (project.getStopDate() == null ? 2001 : project.getStopDate().getYear());
                final Label stopYearLabel = new Label("project-stop-year", stopDate);
                ftListItem.add(stopYearLabel);

                String orgName = (project.getExtendingAgency() == null ? "Unknown" : project.getExtendingAgency().getName());
                final Label status = new Label("project-financing", orgName);
                ftListItem.add(status);

                final Label orgLabel = new Label("project-organization", project.getImplementingOrganization());
                ftListItem.add(orgLabel);

                String areas = StringUtils.join(project.getAreas(), ", ");
                final Label area = new Label("project-geofocus", areas);
                ftListItem.add(area);

                final Link editLink = ProjectTableListPanel.this.getProjectEditLink(project);
                editLink.add(generateLabel("txtable.tx.edit-action", "project-edit-link-label", this, null));
                ftListItem.add(editLink);

                final Link viewLink = new Link("project-view-link") {
                    private static final long serialVersionUID = 9084184844700618410L;

                    @Override
                    public void onClick() {
                        final PageParameters pageParameters = new PageParameters();
                        pageParameters.add(ViewCustomProjectPage.PARAM_PROJECT_ID, project.getId());
                        this.setResponsePage(ViewCustomProjectPage.class, pageParameters);
                    }
                };

                viewLink.add(generateLabel("txtable.tx.view-action", "project-view-link-label", this, null));
                ftListItem.add(viewLink);
            }

        };

        this.add(this.itemsListView);
    }

    public static Label generateLabel(String trnKey, String wicketId, Component component, IModel<?> model) {
        ProxyModel<String> labelTxNameModel = new ProxyModel<String>(new StringResourceModel(trnKey, component, model, null));
        Label txNameLabel = new Label(wicketId, labelTxNameModel);
        return txNameLabel;
    }
}
