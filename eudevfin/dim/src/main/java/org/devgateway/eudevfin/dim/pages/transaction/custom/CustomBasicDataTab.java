/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.pages.transaction.custom;

import java.math.BigDecimal;
import java.util.Collection;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.eudevfin.dim.pages.transaction.crs.BasicDataTab;
import org.devgateway.eudevfin.financial.FileWrapper;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.util.CategoryConstants;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;
import org.devgateway.eudevfin.ui.common.components.DropDownField;
import org.devgateway.eudevfin.ui.common.components.MultiFileUploadField;
import org.devgateway.eudevfin.ui.common.components.PermissionAwareContainer;
import org.devgateway.eudevfin.ui.common.components.TextAreaInputField;
import org.devgateway.eudevfin.ui.common.components.TextInputField;
import org.devgateway.eudevfin.ui.common.components.VisibilityAwareContainer;
import org.devgateway.eudevfin.ui.common.events.CofinancingField14UpdateBehavior;
import org.devgateway.eudevfin.ui.common.events.CurrencyChangedEventPayload;
import org.devgateway.eudevfin.ui.common.events.CurrencyUpdateBehavior;
import org.devgateway.eudevfin.ui.common.events.Field14aChangedEventPayload;
import org.devgateway.eudevfin.ui.common.models.BigMoneyModel;
import org.devgateway.eudevfin.ui.common.models.YearToLocalDateTimeModel;
import org.devgateway.eudevfin.ui.common.providers.CategoryProviderFactory;
import org.devgateway.eudevfin.ui.common.providers.CurrencyUnitProviderFactory;
import org.devgateway.eudevfin.ui.common.providers.OrganizationChoiceProvider;
import org.devgateway.eudevfin.ui.common.temporary.SB;
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;
import org.joda.time.LocalDateTime;

import com.vaynberg.wicket.select2.ChoiceProvider;

/**
 * Basic Data Tab extension for the EU-DEVFIN Form
 *
 * @author aartimon
 * @since 11/12/13
 */
public class CustomBasicDataTab extends BasicDataTab {

	private static final long serialVersionUID = 958677952375077385L;

	@SpringBean
    private CategoryProviderFactory categoryFactory;

    public CustomBasicDataTab(final String id, final PageParameters parameters) {
        super(id, parameters);
        this.addComponents();
    }

    private void addComponents() {
        //nothing to extend yet
    }

    @Override
    protected void addExtensionPanel1() {
        this.add(new Extension1("extensionPanel1", "extension1", this));
    }

    @Override
    protected void addExtensionPanel2() {
        this.add(new Extension2("extensionPanel2", "extension2", this));
    }

    @Override
    protected void addExtensionPanel3() {
        this.add(new Extension3("extensionPanel3", "extension3", this));
    }

    public class Extension1 extends Fragment {
        public Extension1(final String id, final String markupId, final MarkupContainer markupProvider) {
            super(id, markupId, markupProvider);
            final DropDownField<Boolean> cpa = new DropDownField<>("7bCPA", new RWComponentPropertyModel<Boolean>("cpa"),
                    SB.boolProvider);
            this.add(cpa);

        }
    }

    @SuppressWarnings("WicketForgeJavaIdInspection")
    public class Extension2 extends Fragment {

        @SpringBean
        private OrganizationChoiceProvider organizationProvider;

        @SpringBean
        private CurrencyUnitProviderFactory currencyUnitProviderFactory;

        public Extension2(final String id, final String markupId, final MarkupContainer markupProvider) {
            super(id, markupId, markupProvider);

            final MultiFileUploadField bisUploadDocumentation = new MultiFileUploadField("14bisUploadDocumentation", new RWComponentPropertyModel<Collection<FileWrapper>>("uploadDocumentation"));
            bisUploadDocumentation.maxFiles(2);
            this.add(bisUploadDocumentation);

            final PermissionAwareContainer pac = new PermissionAwareContainer("14coFinancing");
            this.add(pac);

            final DropDownField<Boolean> projectCoFinanced = new DropDownField<Boolean>("14aProjectCoFinanced", new RWComponentPropertyModel<Boolean>("projectCoFinanced"),
                    SB.boolProvider) { 
    			@Override
    			protected void onUpdate(AjaxRequestTarget target) {
    				Boolean modelObject = this.getField().getModelObject();				
    				if (modelObject != null)
    					send(getPage(), Broadcast.DEPTH,
    							new Field14aChangedEventPayload(target,modelObject));
    			}
            };
            this.add(projectCoFinanced);


            final VisibilityAwareContainer firstAgencyGroup = new VisibilityAwareContainer("14group1stAgency");
            pac.add(firstAgencyGroup);
            
            
    		TextAreaInputField firstCoFinancingAgency = new TextAreaInputField("14b1stAgency",
    				new RWComponentPropertyModel<String>("firstCoFinancingAgency"));
    		firstCoFinancingAgency.maxContentLength(1024);
    		firstCoFinancingAgency.getField().add(new CofinancingField14UpdateBehavior());      
            firstAgencyGroup.add(firstCoFinancingAgency);

            
            TextAreaInputField secondCoFinancingAgency = new TextAreaInputField("14e2ndAgency",
    				new RWComponentPropertyModel<String>("secondCoFinancingAgency"));
            secondCoFinancingAgency.maxContentLength(1024);
            secondCoFinancingAgency.getField().add(new CofinancingField14UpdateBehavior());      
        	 pac.add(secondCoFinancingAgency);

            
        	 TextAreaInputField thirdCoFinancingAgency = new TextAreaInputField("14h3rdAgency",
    				new RWComponentPropertyModel<String>("thirdCoFinancingAgency"));
        	 thirdCoFinancingAgency.maxContentLength(1024);
        	 thirdCoFinancingAgency.getField().add(new CofinancingField14UpdateBehavior());  
        	 pac.add(thirdCoFinancingAgency);

            final ChoiceProvider<CurrencyUnit> currencyUnitProvider =
                    this.currencyUnitProviderFactory.
                            getCurrencyUnitProviderInstance(CurrencyUnitProviderFactory.ALL_SORTED_CURRENCIES_PROVIDER);

            final RWComponentPropertyModel<CurrencyUnit> firstAgencyCurrencyModel = new RWComponentPropertyModel<>("firstAgencyCurrency");
        
            final DropDownField<CurrencyUnit> firstAgencyCurrency = new DropDownField<CurrencyUnit>("14d1stAgencyCurrency", firstAgencyCurrencyModel,
                    currencyUnitProvider) {
                @Override
                protected void onUpdate(final AjaxRequestTarget target) {
                    this.send(CustomBasicDataTab.this, Broadcast.DEPTH, new CurrencyChangedEventPayload(target));
                }
            };

            firstAgencyCurrency.getField().add(new CofinancingField14UpdateBehavior());  
            firstAgencyGroup.add(firstAgencyCurrency);

            final TextInputField<BigDecimal> firstAgencyAmount = new TextInputField<>("14c1stAgencyAmount",
                    new BigMoneyModel(new RWComponentPropertyModel<BigMoney>("firstAgencyAmount"), firstAgencyCurrencyModel));
            firstAgencyAmount.typeBigDecimal().add(new CurrencyUpdateBehavior());
            firstAgencyAmount.getField().add(new CofinancingField14UpdateBehavior());  
            firstAgencyGroup.add(firstAgencyAmount);


            final RWComponentPropertyModel<CurrencyUnit> secondAgencyCurrencyModel = new RWComponentPropertyModel<>("secondAgencyCurrency");
            final DropDownField<CurrencyUnit> secondAgencyCurrency = new DropDownField<CurrencyUnit>("14g2ndAgencyCurrency", secondAgencyCurrencyModel,
                    currencyUnitProvider) {
                @Override
                protected void onUpdate(final AjaxRequestTarget target) {
                    this.send(CustomBasicDataTab.this, Broadcast.DEPTH, new CurrencyChangedEventPayload(target));
                }
            };
            secondAgencyCurrency.getField().add(new CofinancingField14UpdateBehavior());  
            pac.add(secondAgencyCurrency);

            final TextInputField<BigDecimal> secondAgencyAmount = new TextInputField<>("14f2ndAgencyAmount",
                    new BigMoneyModel(new RWComponentPropertyModel<BigMoney>("secondAgencyAmount"), secondAgencyCurrencyModel));
            secondAgencyAmount.typeBigDecimal().add(new CurrencyUpdateBehavior());
            secondAgencyAmount.getField().add(new CofinancingField14UpdateBehavior());  
            pac.add(secondAgencyAmount);


            final RWComponentPropertyModel<CurrencyUnit> thirdAgencyCurrencyModel = new RWComponentPropertyModel<>("thirdAgencyCurrency");
            final DropDownField<CurrencyUnit> thirdAgencyCurrency = new DropDownField<CurrencyUnit>("14j3rdAgencyCurrency", thirdAgencyCurrencyModel,
                    currencyUnitProvider) {
                @Override
                protected void onUpdate(final AjaxRequestTarget target) {
                    this.send(CustomBasicDataTab.this, Broadcast.DEPTH, new CurrencyChangedEventPayload(target));
                }
            };
            thirdAgencyCurrency.getField().add(new CofinancingField14UpdateBehavior());  
            pac.add(thirdAgencyCurrency);

            final TextInputField<BigDecimal> thirdAgencyAmount = new TextInputField<>("14i3rdAgencyAmount",
                    new BigMoneyModel(new RWComponentPropertyModel<BigMoney>("thirdAgencyAmount"), thirdAgencyCurrencyModel));
            thirdAgencyAmount.typeBigDecimal().add(new CurrencyUpdateBehavior());
            thirdAgencyAmount.getField().add(new CofinancingField14UpdateBehavior());  
            pac.add(thirdAgencyAmount);
        }
    }

    public class Extension3 extends Fragment {
        public Extension3(final String id, final String markupId, final MarkupContainer markupProvider) {
            super(id, markupId, markupProvider);

            final DropDownField<Category> recipientPriority = new DropDownField<>("7cPriorityStatus", new RWComponentPropertyModel<Category>("recipientPriority"),
                    CustomBasicDataTab.this.categoryFactory.get(CategoryConstants.PRIORITY_STATUS_TAG));
            this.add(recipientPriority);

            final TextInputField<Integer> reportingYear = new TextInputField<>("7dPhasingOutYear", new YearToLocalDateTimeModel(new RWComponentPropertyModel<LocalDateTime>("phasingOutYear")));
            reportingYear.typeInteger().range(1900, 2099).decorateMask("9999");
            this.add(reportingYear);

        }
    }
}
