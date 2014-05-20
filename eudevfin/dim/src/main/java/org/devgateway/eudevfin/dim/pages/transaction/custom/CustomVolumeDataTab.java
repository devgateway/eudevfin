/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.pages.transaction.custom;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.ComponentPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.ValidationError;
import org.devgateway.eudevfin.dim.pages.transaction.crs.VolumeDataTab;
import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;
import org.devgateway.eudevfin.ui.common.components.DropDownField;
import org.devgateway.eudevfin.ui.common.components.PermissionAwareContainer;
import org.devgateway.eudevfin.ui.common.components.TextInputField;
import org.devgateway.eudevfin.ui.common.components.VisibilityAwareContainer;
import org.devgateway.eudevfin.ui.common.events.CurrencyUpdateBehavior;
import org.devgateway.eudevfin.ui.common.models.BigMoneyModel;
import org.devgateway.eudevfin.ui.common.providers.CurrencyUnitProviderFactory;
import org.devgateway.eudevfin.ui.common.validators.DuplicateCurrencyValidator;
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;

import com.vaynberg.wicket.select2.ChoiceProvider;
import com.vaynberg.wicket.select2.Select2Choice;

/**
 * @author aartimon
 * @since 16/12/13
 */
@SuppressWarnings("WicketForgeJavaIdInspection")
public class CustomVolumeDataTab extends VolumeDataTab {
    public CustomVolumeDataTab(String id, PageParameters parameters) {
        super(id, parameters);
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

    private class Extension1 extends Fragment {
        public Extension1(String id, String markupId, MarkupContainer markupProvider) {
            super(id, markupId, markupProvider);

            TextInputField<String> budgetCode = new TextInputField<>("34bBudgetCode", new RWComponentPropertyModel<String>("budgetCode"));
            budgetCode.typeString();
            add(budgetCode);

            TextInputField<String> budgetLine = new TextInputField<>("34cBudgetLine", new RWComponentPropertyModel<String>("budgetLine"));
            budgetLine.typeString();
            add(budgetLine);

            TextInputField<String> budgetActivity = new TextInputField<>("34dBudgetActivity", new RWComponentPropertyModel<String>("budgetActivity"));
            budgetActivity.typeString();
            add(budgetActivity);
        }
    }

    private class Extension2 extends Fragment {
        public Extension2(String id, String markupId, MarkupContainer markupProvider) {
            super(id, markupId, markupProvider);

            ComponentPropertyModel<CurrencyUnit> readOnlyCurrencyModel = new ComponentPropertyModel<>("currency");

            PermissionAwareContainer budgetMTEFTable = new PermissionAwareContainer("budgetMTEFTable");
            add(budgetMTEFTable);

            VisibilityAwareContainer disbursementGroup = new VisibilityAwareContainer("disbursementGroup");
            budgetMTEFTable.add(disbursementGroup);

            VisibilityAwareContainer codeGroup = new VisibilityAwareContainer("codeGroup");
            budgetMTEFTable.add(codeGroup);

            VisibilityAwareContainer lineGroup = new VisibilityAwareContainer("lineGroup");
            budgetMTEFTable.add(lineGroup);

            VisibilityAwareContainer activityGroup = new VisibilityAwareContainer("activityGroup");
            budgetMTEFTable.add(activityGroup);


            TextInputField<BigDecimal> disbursement = new TextInputField<>("disbursement",
                    new BigMoneyModel(new RWComponentPropertyModel<BigMoney>("budgetMTEFDisbursement"), readOnlyCurrencyModel));
            disbursement.typeBigDecimal().add(new CurrencyUpdateBehavior());
            disbursementGroup.add(disbursement);

            TextInputField<BigDecimal> disbursementP1 = new TextInputField<>("disbursementP1",
                    new BigMoneyModel(new RWComponentPropertyModel<BigMoney>("budgetMTEFDisbursementP1"), readOnlyCurrencyModel));
            disbursementP1.typeBigDecimal().add(new CurrencyUpdateBehavior());
            disbursementGroup.add(disbursementP1);

            TextInputField<BigDecimal> disbursementP2 = new TextInputField<>("disbursementP2",
                    new BigMoneyModel(new RWComponentPropertyModel<BigMoney>("budgetMTEFDisbursementP2"), readOnlyCurrencyModel));
            disbursementP2.typeBigDecimal().add(new CurrencyUpdateBehavior());
            disbursementGroup.add(disbursementP2);

            TextInputField<BigDecimal> disbursementP3 = new TextInputField<>("disbursementP3",
                    new BigMoneyModel(new RWComponentPropertyModel<BigMoney>("budgetMTEFDisbursementP3"), readOnlyCurrencyModel));
            disbursementP3.typeBigDecimal().add(new CurrencyUpdateBehavior());
            disbursementGroup.add(disbursementP3);

            TextInputField<BigDecimal> disbursementP4 = new TextInputField<>("disbursementP4",
                    new BigMoneyModel(new RWComponentPropertyModel<BigMoney>("budgetMTEFDisbursementP4"), readOnlyCurrencyModel));
            disbursementP4.typeBigDecimal().add(new CurrencyUpdateBehavior());
            disbursementGroup.add(disbursementP4);

            TextInputField<String> code = new TextInputField<>("code", new RWComponentPropertyModel<String>("budgetMTEFCode"));
            code.typeString();
            codeGroup.add(code);

            TextInputField<String> codeP1 = new TextInputField<>("codeP1", new RWComponentPropertyModel<String>("budgetMTEFCodeP1"));
            codeP1.typeString();
            codeGroup.add(codeP1);

            TextInputField<String> codeP2 = new TextInputField<>("codeP2", new RWComponentPropertyModel<String>("budgetMTEFCodeP2"));
            codeP2.typeString();
            codeGroup.add(codeP2);

            TextInputField<String> codeP3 = new TextInputField<>("codeP3", new RWComponentPropertyModel<String>("budgetMTEFCodeP3"));
            codeP3.typeString();
            codeGroup.add(codeP3);

            TextInputField<String> codeP4 = new TextInputField<>("codeP4", new RWComponentPropertyModel<String>("budgetMTEFCodeP4"));
            codeP4.typeString();
            codeGroup.add(codeP4);


            TextInputField<String> line = new TextInputField<>("line", new RWComponentPropertyModel<String>("budgetMTEFLine"));
            line.typeString();
            lineGroup.add(line);

            TextInputField<String> lineP1 = new TextInputField<>("lineP1", new RWComponentPropertyModel<String>("budgetMTEFLineP1"));
            lineP1.typeString();
            lineGroup.add(lineP1);

            TextInputField<String> lineP2 = new TextInputField<>("lineP2", new RWComponentPropertyModel<String>("budgetMTEFLineP2"));
            lineP2.typeString();
            lineGroup.add(lineP2);

            TextInputField<String> lineP3 = new TextInputField<>("lineP3", new RWComponentPropertyModel<String>("budgetMTEFLineP3"));
            lineP3.typeString();
            lineGroup.add(lineP3);

            TextInputField<String> lineP4 = new TextInputField<>("lineP4", new RWComponentPropertyModel<String>("budgetMTEFLineP4"));
            lineP4.typeString();
            lineGroup.add(lineP4);


            TextInputField<String> activity = new TextInputField<>("activity", new RWComponentPropertyModel<String>("budgetMTEFActivity"));
            activity.typeString();
            activityGroup.add(activity);

            TextInputField<String> activityP1 = new TextInputField<>("activityP1", new RWComponentPropertyModel<String>("budgetMTEFActivityP1"));
            activityP1.typeString();
            activityGroup.add(activityP1);

            TextInputField<String> activityP2 = new TextInputField<>("activityP2", new RWComponentPropertyModel<String>("budgetMTEFActivityP2"));
            activityP2.typeString();
            activityGroup.add(activityP2);

            TextInputField<String> activityP3 = new TextInputField<>("activityP3", new RWComponentPropertyModel<String>("budgetMTEFActivityP3"));
            activityP3.typeString();
            activityGroup.add(activityP3);

            TextInputField<String> activityP4 = new TextInputField<>("activityP4", new RWComponentPropertyModel<String>("budgetMTEFActivityP4"));
            activityP4.typeString();
            activityGroup.add(activityP4);


        }
    }

    private class Extension3 extends Fragment {

        @SpringBean
        private CurrencyUnitProviderFactory currencyUnitProviderFactory;

        private IFormValidator otherCurrencyValidator;


        @Override
        protected void onInitialize() {
            super.onInitialize();
            Form<?> form = this.findParent(Form.class);
            form.add(otherCurrencyValidator);
        }


        public Extension3(String id, String markupId, MarkupContainer markupProvider) {
            super(id, markupId, markupProvider);

//            final TextInputField<BigDecimal> exchangeRate = new TextInputField<>("32cExchangeRate",
//                    new RWComponentPropertyModel<BigDecimal>("fixedRate"));
//            exchangeRate.typeBigDecimal().add(new CurrencyUpdateBehavior());
//            exchangeRate.getField().setEnabled(false);
//            add(exchangeRate);

            ChoiceProvider<CurrencyUnit> currencyUnitProvider =
                    this.currencyUnitProviderFactory.
                            getCurrencyUnitProviderInstance(CurrencyUnitProviderFactory.ALL_SORTED_CURRENCIES_PROVIDER);


            final DropDownField<CurrencyUnit> otherCurrency = new DropDownField<CurrencyUnit>("32bOtherCurrency",
                    new RWComponentPropertyModel<CurrencyUnit>("otherCurrency"), currencyUnitProvider) {
//                @Override
//                protected void onUpdate(AjaxRequestTarget target) {
//                    //send(getPage(), Broadcast.DEPTH, new CurrencyChangedEvent(target));
//                    if (getDefaultModelObject() != null)
//                        exchangeRate.getField().setEnabled(true);
//                    else
//                        exchangeRate.getField().setEnabled(false);
//                    target.add(exchangeRate.getField());
//                }
            };
            
            //this is disabled until further notice ODAEU-113
            //otherCurrency.getField().setEnabled(false);
            
            otherCurrency.getField().add(new DuplicateCurrencyValidator(otherCurrency.getField()));
   //         otherCurrency.add(new CurrencyUpdateBehavior());
            add(otherCurrency);

            this.otherCurrencyValidator = new IFormValidator() {

                @Override
                public FormComponent<CurrencyUnit>[] getDependentFormComponents() {
                    List<FormComponent<CurrencyUnit>> list = new ArrayList<FormComponent<CurrencyUnit>>();
                    list.add(CustomVolumeDataTab.this.getCurrency().getField());
                    list.add(otherCurrency.getField());

                    return list.toArray(new FormComponent[0]);
                }

                @Override
                public void validate(Form<?> form) {
                    FormComponent<CurrencyUnit>[] components = this.getDependentFormComponents();
                    Select2Choice<CurrencyUnit> defaultCurrencyComp = (Select2Choice<CurrencyUnit>) components[0];
                    Select2Choice<CurrencyUnit> otherCurrencyComp = (Select2Choice<CurrencyUnit>) components[1];
                    CurrencyUnit defaultCurrencyUnit = ((CustomFinancialTransaction) form.getInnermostModel().getObject()).getCurrency();
                    if (defaultCurrencyUnit.equals(otherCurrencyComp.getModelObject())) {
                        ValidationError error = new ValidationError();
                        error.addKey("currencies.duplicate");
                        otherCurrencyComp.error(error);
                    }

                }

            };


        }
    }
}
