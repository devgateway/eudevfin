/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.pages.transaction.crs;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.eudevfin.dim.providers.AreaChoiceProvider;
import org.devgateway.eudevfin.dim.providers.CategoryProviderFactory;
import org.devgateway.eudevfin.dim.providers.ChannelCategoryChoiceProvider;
import org.devgateway.eudevfin.dim.providers.OrganizationChoiceProvider;
import org.devgateway.eudevfin.financial.Area;
import org.devgateway.eudevfin.financial.Category;
import org.devgateway.eudevfin.financial.ChannelCategory;
import org.devgateway.eudevfin.financial.Organization;
import org.devgateway.eudevfin.financial.util.CategoryConstants;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;
import org.devgateway.eudevfin.ui.common.components.DropDownField;
import org.devgateway.eudevfin.ui.common.components.TextAreaInputField;
import org.devgateway.eudevfin.ui.common.permissions.PermissionAwareComponent;

/**
 * @author aartimon@developmentgateway.org
 * @since 01 November 2013
 */
public class BasicDataTab extends Panel implements PermissionAwareComponent {
    public static final String KEY = "tabs.basic";

    @SpringBean
    private OrganizationChoiceProvider organizationProvider;
    @SpringBean
    private ChannelCategoryChoiceProvider channelProvider;
    @SpringBean
    private CategoryProviderFactory categoryFactory;
    @SpringBean
    private AreaChoiceProvider areaProvider;

    public BasicDataTab(String id) {
        super(id);
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
                new RWComponentPropertyModel<ChannelCategory>("channel"), channelProvider);
        add(channelOfDelivery);

        DropDownField<Category> bilateralMultilateral = new DropDownField<>("10bilateralMultilateral",
                new RWComponentPropertyModel<Category>("biMultilateral"), categoryFactory.get(CategoryConstants.BI_MULTILATERAL_TAG));
        add(bilateralMultilateral);

        DropDownField<Category> typeOfFlow = new DropDownField<>("11typeOfFlow", new RWComponentPropertyModel<Category>("typeOfFlow"),
                categoryFactory.get(CategoryConstants.TYPE_OF_FLOW_TAG));
        typeOfFlow.required();
        add(typeOfFlow);

        DropDownField<Category> typeOfFinance = new DropDownField<>("12typeOfFinance", new RWComponentPropertyModel<Category>("typeOfFinance"),
                categoryFactory.get(CategoryConstants.TYPE_OF_FINANCE_TAG));
        typeOfFinance.required();
        add(typeOfFinance);

        DropDownField<Category> typeOfAid = new DropDownField<>("13typeOfAid", new RWComponentPropertyModel<Category>("typeOfAid"),
                categoryFactory.get(CategoryConstants.TYPE_OF_AID_TAG));
        add(typeOfAid);

        TextAreaInputField activityProjectTitle = new TextAreaInputField("14activityProjectTitle", new RWComponentPropertyModel<String>("shortDescription"));
        add(activityProjectTitle);

        DropDownField<Category> sectorPurposeCode = new DropDownField<>("15sectorPurposeCode", new RWComponentPropertyModel<Category>("sector"),
                categoryFactory.get(CategoryConstants.SECTOR_TAG));
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
