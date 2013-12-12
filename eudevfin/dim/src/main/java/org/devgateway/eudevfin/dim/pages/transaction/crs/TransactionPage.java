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
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.devgateway.eudevfin.dim.core.Constants;
import org.devgateway.eudevfin.dim.core.components.tabs.BootstrapJSTabbedPanel;
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

    private static final HashMap<String, RoleActionMapping> componentPermissions = newPermissions();

    @SuppressWarnings("unchecked")
    public TransactionPage() {

        //TODO: check that transactionType in the request parameters is the same as the loaded transaction's type

        FinancialTransaction financialTransaction = new FinancialTransaction();
        financialTransaction.setCurrency(SB.currencies[0]);
        final CompoundPropertyModel<FinancialTransaction> model = new CompoundPropertyModel<>(financialTransaction);


        setModel(model);

        Form form = new Form("form");
        add(form);

        List<ITabWithKey> tabList = getTabs();

        BootstrapJSTabbedPanel<ITabWithKey> bc = new BootstrapJSTabbedPanel<>("bc", tabList).
                positionTabs(BootstrapJSTabbedPanel.Orientation.RIGHT);
        form.add(bc);

        form.add(new IndicatingAjaxButton("submit", Model.of("Submit")) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                logger.info("Submitted ok!");
                logger.info("Object:" + model.getObject());
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

    protected List<ITabWithKey> getTabs() {
        List<ITabWithKey> tabList = new ArrayList<>();
        tabList.add(IdentificationDataTab.newTab(this));
        tabList.add(BasicDataTab.newTab(this));
        tabList.add(SupplementaryDataTab.newTab(this));
        tabList.add(VolumeDataTab.newTab(this));
        tabList.add(ForLoansOnlyTab.newTab(this));
        return tabList;
    }

    @Override
    protected void onAfterRenderChildren() {
        super.onAfterRenderChildren();

    }

    @Override
    public HashMap<String, RoleActionMapping> getPermissions() {
        return componentPermissions;
    }

    protected static HashMap<String, RoleActionMapping> newPermissions() {
        HashMap<String, RoleActionMapping> permissions = new HashMap<>();
        /**
         * Identification Data
         */
        permissions.put("1bCommitmentDate", new RoleActionMapping().required(SB.allODA()));
        permissions.put("3extendingAgency", new RoleActionMapping().required(SB.allODA()).required(SB.allOOF()));
        permissions.put("4crsId", new RoleActionMapping().required(SB.allODA()).required(SB.allOOF()).required(SB.allPriv()));
        permissions.put("5donorProjectNumber", new RoleActionMapping().required(SB.allODA()).required(SB.allOOF()).required(SB.NON_ODA_PRIVATE_GRANTS));
        permissions.put("6natureSubmission", new RoleActionMapping().required(SB.allODA()).required(SB.allOOF()).required(SB.allPriv()));
        /**
         * Basic Data
         */
        permissions.put("7recipient", new RoleActionMapping().required(SB.biODA()).required(SB.allOOF()).required(SB.allPriv()));
        permissions.put("8channelDelivery", new RoleActionMapping().required(SB.allODA()).required(SB.NON_ODA_OOF_NON_EXPORT).render(SB.NON_ODA_OOF_EXPORT));
        permissions.put("9channelCode", new RoleActionMapping().required(SB.allODA()).required(SB.NON_ODA_OOF_NON_EXPORT).render(SB.NON_ODA_OOF_EXPORT));
        permissions.put("10bilateralMultilateral", new RoleActionMapping().required(SB.allODA()).required(SB.allOOF()).required(SB.allPriv()));
        permissions.put("13typeOfAid", new RoleActionMapping().required(SB.allODA()));
        permissions.put("14activityProjectTitle", new RoleActionMapping().required(SB.allODA()).required(SB.NON_ODA_OOF_NON_EXPORT).render(SB.NON_ODA_PRIVATE_GRANTS).required(SB.NON_ODA_OTHER_FLOWS));
        permissions.put("15sectorPurposeCode", new RoleActionMapping().required(SB.biODA()).required(SB.allOOF()).render(SB.NON_ODA_PRIVATE_GRANTS).required(SB.NON_ODA_OTHER_FLOWS));
        /**
         * Supplementary Data
         */
        //render the tab just for bilateral ODA and OOF non-export
        permissions.put(SupplementaryDataTab.KEY, new RoleActionMapping().render(SB.biODA()).render(SB.NON_ODA_OOF_NON_EXPORT));
        //set field permissions for when the tab is rendered
        permissions.put("16geographicalTargetArea", new RoleActionMapping().required(SB.biODA()).render(SB.NON_ODA_OOF_NON_EXPORT));
        permissions.put("17startingDate", new RoleActionMapping().required(SB.biODA()).render(SB.NON_ODA_OOF_NON_EXPORT));
        permissions.put("18completionDate", new RoleActionMapping().required(SB.biODA()).render(SB.NON_ODA_OOF_NON_EXPORT));
        permissions.put("19description", new RoleActionMapping().required(SB.biODA()).render(SB.NON_ODA_OOF_NON_EXPORT));
        permissions.put("20genderEquality", new RoleActionMapping().required(SB.biODA()).render(SB.NON_ODA_OOF_NON_EXPORT));
        permissions.put("21aidToEnvironment", new RoleActionMapping().required(SB.biODA()).render(SB.NON_ODA_OOF_NON_EXPORT));
        permissions.put("22pdGg", new RoleActionMapping().required(SB.biODA()).render(SB.NON_ODA_OOF_NON_EXPORT));
        permissions.put("23tradeDevelopment", new RoleActionMapping().required(SB.biODA()));
        permissions.put("24freestandingTechnicalCooperation", new RoleActionMapping().required(SB.biODA()));
        permissions.put("25programbasedApproach", new RoleActionMapping().required(SB.biODA()));
        permissions.put("26investmentProject", new RoleActionMapping().required(SB.biODA()));
        permissions.put("27associatedFinancing", new RoleActionMapping().required(SB.biODA()).render(SB.NON_ODA_OOF_NON_EXPORT));
        permissions.put("28biodiversity", new RoleActionMapping().required(SB.biODA()).render(SB.NON_ODA_OOF_NON_EXPORT));
        permissions.put("29ccMitigation", new RoleActionMapping().required(SB.biODA()).render(SB.NON_ODA_OOF_NON_EXPORT));
        permissions.put("30ccAdaptation", new RoleActionMapping().required(SB.biODA()).render(SB.NON_ODA_OOF_NON_EXPORT));
        permissions.put("31desertification", new RoleActionMapping().required(SB.biODA()).render(SB.NON_ODA_OOF_NON_EXPORT));
        /**
         * Volume Data
         */
        permissions.put("33commitments", new RoleActionMapping().required(SB.allODA()).required(SB.allOOF()));
        permissions.put("35amountsReceived", new RoleActionMapping().required(SB.allODA()).required(SB.allOOF()).required(SB.NON_ODA_PRIVATE_MARKET));
        permissions.put("36amountUntied", new RoleActionMapping().required(SB.biODA()));
        permissions.put("37amountPartiallyUntied", new RoleActionMapping().required(SB.biODA()));
        permissions.put("38amountTied", new RoleActionMapping().required(SB.biODA()));
        permissions.put("39amountOfIRTC", new RoleActionMapping().required(SB.biODA()));
        permissions.put("40amountOfExpertsCommitments", new RoleActionMapping().render(SB.biODA()));
        permissions.put("41amountOfExpertsExtended", new RoleActionMapping().render(SB.biODA()));
        permissions.put("42amountOfExportCredit", new RoleActionMapping().required(SB.biODA()));
        /**
         * For Loans only
         */
        //render the tab for only the ODA and OOF
        permissions.put(ForLoansOnlyTab.KEY, new RoleActionMapping().render(SB.allODA()).render(SB.allOOF()));
        //set field permissions for when tab is rendered
        permissions.put("44typeOfRepayment", new RoleActionMapping().required(SB.allODA()).render(SB.NON_ODA_OOF_NON_EXPORT));
        permissions.put("45numberOfRepayments", new RoleActionMapping().required(SB.allODA()).render(SB.NON_ODA_OOF_NON_EXPORT));
        permissions.put("46interestRate", new RoleActionMapping().required(SB.allODA()).render(SB.NON_ODA_OOF_NON_EXPORT));
        permissions.put("47secondInterestRate", new RoleActionMapping().required(SB.allODA()).render(SB.NON_ODA_OOF_NON_EXPORT));
        permissions.put("48firstRepaymentDate", new RoleActionMapping().required(SB.allODA()).render(SB.NON_ODA_OOF_NON_EXPORT));
        permissions.put("49finalRepaymentDate", new RoleActionMapping().required(SB.allODA()).render(SB.NON_ODA_OOF_NON_EXPORT));
        permissions.put("50interestReceived", new RoleActionMapping().required(SB.allODA()).required(SB.allOOF()));
        permissions.put("51principalDisbursed", new RoleActionMapping().required(SB.biODA()).required(SB.NON_ODA_OOF_NON_EXPORT));
        permissions.put("52arrearsOfPrincipals", new RoleActionMapping().required(SB.biODA()).required(SB.NON_ODA_OOF_NON_EXPORT));
        permissions.put("53arrearsOfInterest", new RoleActionMapping().required(SB.biODA()).required(SB.NON_ODA_OOF_NON_EXPORT));

        return permissions;
    }
}
