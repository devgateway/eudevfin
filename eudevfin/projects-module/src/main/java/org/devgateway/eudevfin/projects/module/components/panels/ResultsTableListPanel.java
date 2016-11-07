/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.module.components.panels;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.eudevfin.projects.common.entities.ProjectResult;
import static org.devgateway.eudevfin.projects.module.components.tabs.ResultsTab.WICKETID_LIST_PANEL;
import org.devgateway.eudevfin.projects.module.components.util.ProjectResultsListGenerator;
import org.devgateway.eudevfin.projects.module.modals.ResultsTableModal;
import org.devgateway.eudevfin.projects.module.pages.NewProjectPage;
import org.devgateway.eudevfin.ui.common.components.TableListPanel;
import org.devgateway.eudevfin.ui.common.components.util.ListGeneratorInterface;

/**
 *
 * @author alcr
 */
public class ResultsTableListPanel extends TableListPanel<ProjectResult> {

    private PageParameters pageParameters;
    private ListGeneratorInterface<ProjectResult> listGenerator;

    public ResultsTableListPanel(String id, ListGeneratorInterface<ProjectResult> listGen) {
        super(id, listGen);
        listGenerator = listGen;
    }

    @Override
    protected void populateTable() {
        final ModalWindow modal = AddModalWindow(null);

        this.itemsListView = new ListView<ProjectResult>("projectResultList", items) {

            private static final long serialVersionUID = -8758662617501215830L;

            @Override
            protected void populateItem(ListItem<ProjectResult> listItem) {
                final ProjectResult result = listItem.getModelObject();

                AjaxLink linkToEdit = new AjaxLink("linkToEditResult") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        PageParameters parameters = new PageParameters().add(ResultsTableModal.PARAM_RESULT_ID, result.getId());
                        setParameters(parameters);
                        AddModalWindow(parameters);
                        modal.show(target);
                    }
                };

                linkToEdit.setBody(Model.of(result.getResult()));
                String statusName = new StringResourceModel(result.getStatus(), ResultsTableListPanel.this, null).getString();
                Label statusLabel = new Label("status", statusName);
                Label descriptionLabel = new Label("description", result.getDescription());

                listItem.add(statusLabel);
                listItem.add(descriptionLabel);
                listItem.add(linkToEdit);
            }
        };
        itemsListView.setOutputMarkupId(true);
        this.add(modal);
        this.add(itemsListView);

    }

    private ModalWindow AddModalWindow(PageParameters parameters) {

        if (parameters == null) {
            parameters = new PageParameters();
        }

        final ModalWindow modal = new ModalWindow("modal");

        modal.setCookieName("modal-1");
        modal.setPageCreator(new ModalWindow.PageCreator() {
            @Override
            public org.apache.wicket.Page createPage() {
                return new ResultsTableModal(getParameters(), ResultsTableListPanel.this.getPage().getPageReference(), modal);
            }
        });
        modal.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            public void onClose(AjaxRequestTarget target) {
                ResultsTableListPanel newComp = new ResultsTableListPanel(WICKETID_LIST_PANEL, new ProjectResultsListGenerator(NewProjectPage.project.getProjectResults()));
                getParent().replace(newComp);
                target.add(newComp);
            }
        });
        modal.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
            public boolean onCloseButtonClicked(AjaxRequestTarget target) {
                // Change the passValue variable when modal window is closed.
                //setPassValue("Modal window is closed by user.");
                return true;
            }
        });

        return modal;
    }

    public PageParameters getParameters() {
        return pageParameters;
    }

    public void setParameters(PageParameters params) {
        this.pageParameters = params;
    }
}
