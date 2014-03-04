/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.pages.transaction.crs;

import com.vaynberg.wicket.select2.ChoiceProvider;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.devgateway.eudevfin.dim.providers.AreaChoiceProvider;
import org.devgateway.eudevfin.dim.providers.CategoryProviderFactory;
import org.devgateway.eudevfin.financial.Area;
import org.devgateway.eudevfin.financial.Category;
import org.devgateway.eudevfin.financial.ChannelCategory;
import org.devgateway.eudevfin.financial.util.CategoryConstants;
import org.devgateway.eudevfin.ui.common.Constants;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;
import org.devgateway.eudevfin.ui.common.components.DropDownField;
import org.devgateway.eudevfin.ui.common.components.TextAreaInputField;
import org.devgateway.eudevfin.ui.common.permissions.PermissionAwareComponent;
import org.devgateway.eudevfin.ui.common.providers.OrganizationChoiceProvider;
import org.devgateway.eudevfin.ui.common.temporary.SB;
import org.devgateway.eudevfin.ui.common.validators.CodePatternCategoryValidator;

/**
 * @author aartimon@developmentgateway.org
 * @since 01 November 2013
 */
public class BasicDataTab extends Panel implements PermissionAwareComponent {
    private static final long serialVersionUID = 8923172292469016906L;
    public static final String KEY = "tabs.basic";
    public static final String REGEX_MULTILATERAL_CHANNEL_CODE = "4[0-9]{4}";
    public static final String ERRORKEY_MULTILATERAL_CHANNEL_CODE = "validation.multilateralChannelCode";
    protected PageParameters parameters;


    @SpringBean
    private OrganizationChoiceProvider organizationProvider;
    //@SpringBean
    //private ChannelCategoryChoiceProvider channelProvider;
    @SpringBean
    private CategoryProviderFactory categoryFactory;
    @SpringBean
    private AreaChoiceProvider areaProvider;

    public BasicDataTab(String id, PageParameters parameters) {
        super(id);
        this.parameters = parameters;
        addComponents();
        addExtensionPanel1();
        addExtensionPanel2();
        addExtensionPanel3();
    }

    protected void addExtensionPanel3() {
        add(new WebMarkupContainer("extensionPanel3").setVisibilityAllowed(false));
    }

    /**
     * Placeholder in order to extend the tab after the field with no 7
     */
    protected void addExtensionPanel1() {
        add(new WebMarkupContainer("extensionPanel1").setVisibilityAllowed(false));
    }

    /**
     * Placeholder in order to extend the tab after last field
     */
    protected void addExtensionPanel2() {
        add(new WebMarkupContainer("extensionPanel2").setVisibilityAllowed(false));
    }


    private void addComponents() {
        DropDownField<Area> recipient = new DropDownField<>("7recipient",
                new RWComponentPropertyModel<Area>("recipient"), areaProvider);
        add(recipient);

        DropDownField<ChannelCategory> channelOfDelivery = new DropDownField<>("8channelDelivery",
                new RWComponentPropertyModel<ChannelCategory>("channel"), (ChoiceProvider) categoryFactory.get(CategoryConstants.CHANNEL_TAG));

        String transactionType = parameters.get(Constants.PARAM_TRANSACTION_TYPE).toString("");
        if (!Strings.isEmpty(transactionType)
                && (Strings.isEqual(transactionType, SB.MULTILATERAL_ODA_ADVANCE_QUESTIONNAIRE) ||
                Strings.isEqual(transactionType, SB.MULTILATERAL_ODA_CRS)))
            channelOfDelivery.getField().add(new CodePatternCategoryValidator(REGEX_MULTILATERAL_CHANNEL_CODE) {
                private static final long serialVersionUID = 691836793052873358L;

                @Override
                protected ValidationError decorate(ValidationError error, IValidatable<Category> validatable) {
                    error.addKey(ERRORKEY_MULTILATERAL_CHANNEL_CODE);
                    return super.decorate(error, validatable);
                }
            });
        add(channelOfDelivery);

        DropDownField<Category> bilateralMultilateral = new DropDownField<>("10bilateralMultilateral",
                new RWComponentPropertyModel<Category>("biMultilateral"), categoryFactory.get(CategoryConstants.BI_MULTILATERAL_TAG));
        add(bilateralMultilateral);

        DropDownField<Category> typeOfFlow = new DropDownField<>("11typeOfFlow", new RWComponentPropertyModel<Category>("typeOfFlow"),
                categoryFactory.get(CategoryConstants.TYPE_OF_FLOW_TAG));
        typeOfFlow.required();
        add(typeOfFlow);

        DropDownField<Category> typeOfFinance = new DropDownField<>("12typeOfFinance", new RWComponentPropertyModel<Category>("typeOfFinance"),
                categoryFactory.get(CategoryConstants.ALL_TYPE_OF_FINANCE_TAG));
        typeOfFinance.required();
        add(typeOfFinance);

        DropDownField<Category> typeOfAid = new DropDownField<>("13typeOfAid", new RWComponentPropertyModel<Category>("typeOfAid"),
                categoryFactory.get(CategoryConstants.ALL_TYPE_OF_AID_TAG));
        add(typeOfAid);

        TextAreaInputField activityProjectTitle = new TextAreaInputField("14activityProjectTitle", new RWComponentPropertyModel<String>("shortDescription"));
        activityProjectTitle.maxContentLength(150);//max length for activity project title
        add(activityProjectTitle);

        DropDownField<Category> sectorPurposeCode = new DropDownField<>("15sectorPurposeCode", new RWComponentPropertyModel<Category>("sector"),
                categoryFactory.get(CategoryConstants.ALL_SECTOR_TAG));
        add(sectorPurposeCode);
    }

    @Override
    public String getPermissionKey() {
        return KEY;
    }

    @Override
    public void enableRequired() {
    }
}
