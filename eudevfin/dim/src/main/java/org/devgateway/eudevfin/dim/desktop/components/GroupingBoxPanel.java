/**
 * *****************************************************************************
 * Copyright (c) 2014 Development Gateway. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the GNU
 * Public License v3.0 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************
 */
/**
 *
 */
package org.devgateway.eudevfin.dim.desktop.components;

import de.agilecoders.wicket.core.markup.html.bootstrap.form.InputBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.auth.common.util.AuthUtils;
import org.devgateway.eudevfin.dim.desktop.components.util.GroupingSearchListGenerator;
import org.devgateway.eudevfin.financial.service.CustomFinancialTransactionService;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;
import org.devgateway.eudevfin.ui.common.components.BootstrapCancelButton;
import org.devgateway.eudevfin.ui.common.components.BootstrapSubmitButton;
import org.devgateway.eudevfin.ui.common.components.CheckBoxField;
import org.devgateway.eudevfin.ui.common.components.DropDownField;
import org.devgateway.eudevfin.ui.common.components.TableListPanel;
import org.devgateway.eudevfin.ui.common.forms.GroupingBoxPanelForm;
import org.devgateway.eudevfin.ui.common.providers.AreaChoiceProvider;
import org.devgateway.eudevfin.ui.common.providers.CategoryProviderFactory;
import org.devgateway.eudevfin.ui.common.providers.OrganizationChoiceProvider;
import org.devgateway.eudevfin.ui.common.providers.PredefinedStringProvider;

/**
 * @author mihai
 *
 */
public class GroupingBoxPanel extends Panel {

    private static final long serialVersionUID = 6025430438643716484L;
    private TableListPanel<?> resultsPanel;
    private GroupingSearchListGenerator listGenerator;
    private WebMarkupContainer searchWrapperPanel;

    @SpringBean
    private CustomFinancialTransactionService txService;
    private boolean superUser;

    @SpringBean
    private CategoryProviderFactory categoryFactory;

    /**
     * @param id
     * @param generalSearchListGenerator
     * @param categoryFactory
     * @param areaProvider
     * @param organizationProvider
     */
    public GroupingBoxPanel(String id, TableListPanel<?> resultsPanel, GroupingSearchListGenerator generalSearchListGenerator,
            CategoryProviderFactory categoryFactory, OrganizationChoiceProvider organizationProvider, AreaChoiceProvider areaProvider) {
        super(id);
        this.resultsPanel = resultsPanel;
        this.listGenerator = generalSearchListGenerator;
        this.searchWrapperPanel = new WebMarkupContainer("search-results-panel-wrapper");
        this.searchWrapperPanel.setOutputMarkupId(true);

        this.populate(null);
        this.setOutputMarkupId(true);
    }

    protected void populate(String searchString) {
        this.resultsPanel.setVisible(false);
        this.searchWrapperPanel.add(this.resultsPanel);
        this.add(this.searchWrapperPanel);

        superUser = AuthUtils.currentUserHasRole(AuthConstants.Roles.ROLE_SUPERVISOR);

        final GroupingBoxPanelForm boxPanelForm = new GroupingBoxPanelForm();
        CompoundPropertyModel<GroupingBoxPanelForm> boxPanelFormModel = new CompoundPropertyModel<GroupingBoxPanelForm>(boxPanelForm);
        Form<?> form = new Form<>("searchForm", boxPanelFormModel);
        form.setOutputMarkupId(false);

        final DropDownField<String> crsIdSearch = new DropDownField<>("crsIdSearch", new RWComponentPropertyModel<String>("crsIdSearch"),
                new PredefinedStringProvider(txService.findDistinctCRSId()), "desktop.search.crsid");

        crsIdSearch.setSize(InputBehavior.Size.Medium);
        crsIdSearch.removeSpanFromControlGroup();
        form.add(crsIdSearch);

        final DropDownField<String> donorIdSearch = new DropDownField<>("donorIdSearch", new RWComponentPropertyModel<String>("donorIdSearch"),
                new PredefinedStringProvider(txService.findDistinctDonorProjectNumber()), "desktop.search.donorid");
        donorIdSearch.setSize(InputBehavior.Size.Medium);
        donorIdSearch.removeSpanFromControlGroup();
        form.add(donorIdSearch);

        final CheckBoxField active = new CheckBoxField("active", new RWComponentPropertyModel<Boolean>("active"), "desktop.search.active");
        active.removeSpanFromControlGroup();
        form.add(active);

        BootstrapSubmitButton submitButton = new BootstrapSubmitButton("submit", new StringResourceModel("desktop.searchbutton", this, null)) {

            private static final long serialVersionUID = -1342816632002116152L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                // Access the updated model object:
                if ((boxPanelForm.getCrsIdSearch() != null && boxPanelForm.getCrsIdSearch().length() > 1)
                        || (boxPanelForm.getDonorIdSearch() != null && boxPanelForm.getDonorIdSearch().length() > 1)) {
                    GroupingBoxPanel.this.listGenerator.setSearchBoxPanelForm(boxPanelForm);
                    GroupingBoxPanel.this.resultsPanel.generateListOfItems(1);
                    GroupingBoxPanel.this.resultsPanel.setVisible(true);
                } else {
                    GroupingBoxPanel.this.resultsPanel.setVisible(false);
                }
                target.add(GroupingBoxPanel.this.searchWrapperPanel);
            }

        };
        form.add(submitButton);

        BootstrapCancelButton resetButton = new BootstrapCancelButton("reset", new StringResourceModel("desktop.resetbutton", this, null)) {

            private static final long serialVersionUID = -7554180087300408868L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                crsIdSearch.getField().setDefaultModelObject(null);
                donorIdSearch.getField().setDefaultModelObject(null);
                active.getField().setDefaultModelObject(null);
                target.add(crsIdSearch.getField());
                target.add(donorIdSearch.getField());
                target.add(active.getField());
                GroupingBoxPanel.this.resultsPanel.setVisible(false);
                target.add(GroupingBoxPanel.this.searchWrapperPanel);
            }

        };
        form.add(resetButton);

        this.add(form);
    }

}
