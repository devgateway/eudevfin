/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.pages.transaction.custom;

import java.math.BigDecimal;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.eudevfin.dim.core.models.BigMoneyModel;
import org.devgateway.eudevfin.dim.core.models.YearToLocalDateTimeModel;
import org.devgateway.eudevfin.dim.pages.transaction.crs.BasicDataTab;
import org.devgateway.eudevfin.dim.providers.CategoryProviderFactory;
import org.devgateway.eudevfin.dim.providers.OrganizationChoiceProvider;
import org.devgateway.eudevfin.financial.Category;
import org.devgateway.eudevfin.financial.Organization;
import org.devgateway.eudevfin.financial.util.CategoryConstants;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;
import org.devgateway.eudevfin.ui.common.components.DropDownField;
import org.devgateway.eudevfin.ui.common.components.PermissionAwareContainer;
import org.devgateway.eudevfin.ui.common.components.TextInputField;
import org.devgateway.eudevfin.ui.common.events.CurrencyChangedEvent;
import org.devgateway.eudevfin.ui.common.events.CurrencyUpdateBehavior;
import org.devgateway.eudevfin.ui.common.temporary.SB;
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;
import org.joda.time.LocalDateTime;

import de.agilecoders.wicket.core.markup.html.bootstrap.form.InputBehavior;

/**
 * Basic Data Tab extension for the EU-DEVFIN Form
 *
 * @author aartimon
 * @since 11/12/13
 */
public class CustomBasicDataTab extends BasicDataTab {

    @SpringBean
    private CategoryProviderFactory categoryFactory;

    public CustomBasicDataTab(String id,PageParameters parameters) {
        super(id, parameters);
        addComponents();
    }

    private void addComponents() {
        //nothing to extend yet
    }

    @Override
    protected void addExtensionPanel1() {
        add(new Extension1("extensionPanel1", "extension1", this));
    }

    @Override
    protected void addExtensionPanel2() {
        add(new Extension2("extensionPanel2", "extension2", this));
    }

    @Override
    protected void addExtensionPanel3() {
        add(new Extension3("extensionPanel3", "extension3", this));
    }

    public class Extension1 extends Fragment {
        public Extension1(String id, String markupId, MarkupContainer markupProvider) {
            super(id, markupId, markupProvider);
            DropDownField<Boolean> cpa = new DropDownField<>("7bCPA", new RWComponentPropertyModel<Boolean>("cpa"),
                    SB.boolProvider);
            add(cpa);

            TextInputField<Integer> reportingYear = new TextInputField<>("7dPhasingOutYear", new YearToLocalDateTimeModel(new RWComponentPropertyModel<LocalDateTime>("phasingOutYear")));
            reportingYear.typeInteger().required().range(1900, 2099).decorateMask("9999");
            add(reportingYear);


        }
    }

    public class Extension2 extends Fragment {

        @SpringBean
        private OrganizationChoiceProvider organizationProvider;

        public Extension2(String id, String markupId, MarkupContainer markupProvider) {
            super(id, markupId, markupProvider);

            //TODO: fid out what's with the upload doc
            TextInputField<String> bisUploadDocumentation = new TextInputField<String>("14bisUploadDocumentation", Model.of(""));
            add(bisUploadDocumentation);

            PermissionAwareContainer pac = new PermissionAwareContainer("14coFinancing");
            add(pac);

            DropDownField<Boolean> projectCoFinanced = new DropDownField<>("14aProjectCoFinanced", new RWComponentPropertyModel<Boolean>("projectCoFinanced"),
                    SB.boolProvider);
            add(projectCoFinanced);

            DropDownField<Organization> firstCoFinancingAgency = new DropDownField<>("14b1stAgency",
                    new RWComponentPropertyModel<Organization>("firstCoFinancingAgency"), organizationProvider);
            firstCoFinancingAgency.hideLabel().setSize(InputBehavior.Size.Small);
            pac.add(firstCoFinancingAgency);

            DropDownField<Organization> secondCoFinancingAgency = new DropDownField<>("14e2ndAgency",
                    new RWComponentPropertyModel<Organization>("secondCoFinancingAgency"), organizationProvider);
            secondCoFinancingAgency.hideLabel().setSize(InputBehavior.Size.Small);
            pac.add(secondCoFinancingAgency);

            DropDownField<Organization> thirdCoFinancingAgency = new DropDownField<>("14h3rdAgency",
                    new RWComponentPropertyModel<Organization>("thirdCoFinancingAgency"), organizationProvider);
            thirdCoFinancingAgency.hideLabel().setSize(InputBehavior.Size.Small);
            pac.add(thirdCoFinancingAgency);

            RWComponentPropertyModel<CurrencyUnit> firstAgencyCurrencyModel = new RWComponentPropertyModel<>("firstAgencyCurrency");
            DropDownField<CurrencyUnit> firstAgencyCurrency = new DropDownField<CurrencyUnit>("14d1stAgencyCurrency", firstAgencyCurrencyModel,
                    SB.currencyProvider) {
                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                    send(CustomBasicDataTab.this, Broadcast.DEPTH, new CurrencyChangedEvent(target));
                }
            };
            firstAgencyCurrency.hideLabel().setSize(InputBehavior.Size.Small).required();
            pac.add(firstAgencyCurrency);

            TextInputField<BigDecimal> firstAgencyAmount = new TextInputField<>("14c1stAgencyAmount",
                    new BigMoneyModel(new RWComponentPropertyModel<BigMoney>("firstAgencyAmount"), firstAgencyCurrencyModel));
            firstAgencyAmount.typeBigDecimal().hideLabel().setSize(InputBehavior.Size.Small).add(new CurrencyUpdateBehavior());
            pac.add(firstAgencyAmount);


            RWComponentPropertyModel<CurrencyUnit> secondAgencyCurrencyModel = new RWComponentPropertyModel<>("secondAgencyCurrency");
            DropDownField<CurrencyUnit> secondAgencyCurrency = new DropDownField<CurrencyUnit>("14g2ndAgencyCurrency", secondAgencyCurrencyModel,
                    SB.currencyProvider) {
                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                    send(CustomBasicDataTab.this, Broadcast.DEPTH, new CurrencyChangedEvent(target));
                }
            };
            secondAgencyCurrency.hideLabel().setSize(InputBehavior.Size.Small).required();
            pac.add(secondAgencyCurrency);

            TextInputField<BigDecimal> secondAgencyAmount = new TextInputField<>("14f2ndAgencyAmount",
                    new BigMoneyModel(new RWComponentPropertyModel<BigMoney>("secondAgencyAmount"), secondAgencyCurrencyModel));
            secondAgencyAmount.typeBigDecimal().hideLabel().setSize(InputBehavior.Size.Small).add(new CurrencyUpdateBehavior());
            pac.add(secondAgencyAmount);


            RWComponentPropertyModel<CurrencyUnit> thirdAgencyCurrencyModel = new RWComponentPropertyModel<>("thirdAgencyCurrency");
            DropDownField<CurrencyUnit> thirdAgencyCurrency = new DropDownField<CurrencyUnit>("14j3rdAgencyCurrency", thirdAgencyCurrencyModel,
                    SB.currencyProvider) {
                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                    send(CustomBasicDataTab.this, Broadcast.DEPTH, new CurrencyChangedEvent(target));
                }
            };
            thirdAgencyCurrency.hideLabel().setSize(InputBehavior.Size.Small).required();
            pac.add(thirdAgencyCurrency);

            TextInputField<BigDecimal> thirdAgencyAmount = new TextInputField<>("14i3rdAgencyAmount",
                    new BigMoneyModel(new RWComponentPropertyModel<BigMoney>("thirdAgencyAmount"), thirdAgencyCurrencyModel));
            thirdAgencyAmount.typeBigDecimal().hideLabel().setSize(InputBehavior.Size.Small).add(new CurrencyUpdateBehavior());
            pac.add(thirdAgencyAmount);


        }
    }

    public class Extension3 extends Fragment {
        public Extension3(String id, String markupId, MarkupContainer markupProvider) {
            super(id, markupId, markupProvider);

            DropDownField<Category> recipientCode = new DropDownField<>("7aRecipientCode", new RWComponentPropertyModel<Category>("recipientCode"),
                    categoryFactory.get(CategoryConstants.RECIPIENT_TAG));
            recipientCode.required();
            add(recipientCode);

            DropDownField<Category> recipientPriority = new DropDownField<>("7cPriorityStatus", new RWComponentPropertyModel<Category>("recipientPriority"),
                    SB.categoryProvider);
            recipientPriority.required();
            add(recipientPriority);


        }
    }
}
