/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.pages.transaction.crs;

import de.agilecoders.wicket.core.markup.html.bootstrap.form.ControlGroup;
import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.devgateway.eudevfin.dim.core.Constants;
import org.devgateway.eudevfin.dim.core.components.tabs.BootstrapJSTabbedPanel;
import org.devgateway.eudevfin.dim.core.components.tabs.DefaultTabWithKey;
import org.devgateway.eudevfin.dim.core.components.tabs.ITabWithKey;
import org.devgateway.eudevfin.dim.core.pages.HeaderFooter;
import org.devgateway.eudevfin.dim.core.permissions.PermissionAwarePage;
import org.devgateway.eudevfin.dim.core.permissions.RoleActionMapping;
import org.devgateway.eudevfin.dim.core.temporary.SB;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@MountPath(value = "/transaction")
@AuthorizeInstantiation(Constants.ROLE_USER)
public class TransactionPage extends HeaderFooter implements PermissionAwarePage {
    private static final Logger logger = Logger.getLogger(TransactionPage.class);

    private static final CRSTransactionPermissionProvider componentPermissions = new CRSTransactionPermissionProvider();

    @SuppressWarnings("unchecked")
    public TransactionPage() {

        //TODO: check that transactionType in the request parameters is the same as the loaded transaction's type

        FinancialTransaction financialTransaction = getFinancialTransaction();
        financialTransaction.setCurrency(SB.currencies[0]);
        CompoundPropertyModel<? extends FinancialTransaction> model = new CompoundPropertyModel<>(financialTransaction);

        setModel(model);

        Form form = new Form("form");
        add(form);

        List<ITabWithKey> tabList = populateTabList();

        BootstrapJSTabbedPanel<ITabWithKey> bc = new BootstrapJSTabbedPanel<>("bc", tabList).
                positionTabs(BootstrapJSTabbedPanel.Orientation.RIGHT);
        form.add(bc);

        form.add(new IndicatingAjaxButton("submit", Model.of("Submit")) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                logger.info("Submitted ok!");
                logger.info("Object:" + getModel().getObject());
            }

            @Override
            protected void onError(final AjaxRequestTarget target, Form<?> form) {
                logger.info("Error detected!");

                final Model<Boolean> shownFirstSection = Model.of(Boolean.FALSE);

                // visit form children and add to the ajax request the invalid
                // ones
                form.visitChildren(FormComponent.class,
                        new IVisitor<FormComponent, Void>() {
                            @Override
                            public void component(FormComponent component,
                                                  IVisit<Void> visit) {
                                if (!component.isValid()) {
                                    Component parent = component.findParent(ControlGroup.class);
                                    target.add(parent);
                                    if (!shownFirstSection.getObject()) {
                                        target.focusComponent(component);
                                        target.appendJavaScript("$('#" + component.getMarkupId() + "').parents('[class~=\"tab-pane\"]').siblings().attr(\"class\", \"tab-pane\");");
                                        target.appendJavaScript("$('#" + component.getMarkupId() + "').parents('[class~=\"tab-pane\"]').attr(\"class\", \"tab-pane active\");");

                                        target.appendJavaScript("$('#" + component.getMarkupId() + "').parents('[class~=\"tabbable\"]').children('ul').find('li').attr('class', '');");
                                        target.appendJavaScript("var idOfSection = $('#" + component.getMarkupId() + "').parents('[class~=\"tab-pane\"]').attr('id');$('#" + component.getMarkupId() + "').parents('[class~=\"tabbable\"]').children('ul').find('a[href=\"#' + idOfSection + '\"]').parent().attr('class', 'active');");

                                        shownFirstSection.setObject(Boolean.TRUE);
                                    }
                                }
                            }
                        });
            }
        });
    }

    protected FinancialTransaction getFinancialTransaction() {
        return new FinancialTransaction();
    }

    private List<ITabWithKey> populateTabList() {
        List<Class<? extends Panel>> tabClasses = getTabs();
        ArrayList<ITabWithKey> tabs = new ArrayList<>();
        for (final Class<? extends Panel> p : tabClasses) {
            tabs.add(DefaultTabWithKey.of(p, this));
        }
        return tabs;
    }

    protected List<Class<? extends Panel>> getTabs() {
        List<Class<? extends Panel>> tabList = new ArrayList<>();
        tabList.add(IdentificationDataTab.class);
        tabList.add(BasicDataTab.class);
        tabList.add(SupplementaryDataTab.class);
        tabList.add(VolumeDataTab.class);
        tabList.add(ForLoansOnlyTab.class);
        return tabList;
    }

    @Override
    protected void onAfterRenderChildren() {
        super.onAfterRenderChildren();

    }

    @Override
    public HashMap<String, RoleActionMapping> getPermissions() {
        return componentPermissions.permissions();
    }
}
