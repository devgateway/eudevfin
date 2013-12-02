/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.pages.transaction;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;
import org.devgateway.eudevfin.dim.core.RWComponentPropertyModel;
import org.devgateway.eudevfin.dim.core.components.DropDownField;
import org.devgateway.eudevfin.dim.core.components.TextAreaInputField;
import org.devgateway.eudevfin.dim.core.components.tabs.AbstractTabWithKey;
import org.devgateway.eudevfin.dim.core.components.tabs.ITabWithKey;
import org.devgateway.eudevfin.dim.core.permissions.PermissionAwareComponent;
import org.devgateway.eudevfin.dim.core.temporary.SB;
import org.devgateway.eudevfin.financial.Category;
import org.devgateway.eudevfin.financial.Organization;
import org.devgateway.eudevfin.financial.RecipientCategory;

/**
 * @author aartimon@developmentgateway.org
 * @since 01 November 2013
 */
public class BasicDataTab extends Panel implements PermissionAwareComponent {

    public static final String KEY = "tabs.basic";

    private BasicDataTab(String id) {
        super(id);
        addComponents();
    }

    private void addComponents() {
        DropDownField<RecipientCategory> recipient = new DropDownField<>("7recipient",
                new RWComponentPropertyModel<RecipientCategory>("recipient"), SB.recipientCategoryProvider);
        add(recipient);

//        DropDownField<String> cpa = new DropDownField<>("7bCPA", new RWComponentPropertyModel<String>("CPA"),
//                SB.yesNoProvider);
//        add(cpa);

        DropDownField<Organization> channelOfDelivery = new DropDownField<>("8channelDelivery",
                new RWComponentPropertyModel<Organization>("channelOfDelivery"), SB.organizationProvider);
        add(channelOfDelivery);

        //TODO: remove if channel of delivery is of type Org
        /*TextInputField<String> channelDelivery = new TextInputField<>("8channelDelivery",
                new RWComponentPropertyModel<String>("channelOfDelivery"));
        add(channelDelivery);*/

        DropDownField<String> channelCode = new DropDownField<>("9channelCode", new RWComponentPropertyModel<String>("channelCode"),
                SB.countryProvider);
        add(channelCode);

        DropDownField<Category> bilateralMultilateral = new DropDownField<>("10bilateralMultilateral",
                new RWComponentPropertyModel<Category>("biMultilateral"), SB.categoryProvider);
        add(bilateralMultilateral);

        DropDownField<Category> typeOfFlow = new DropDownField<>("11typeOfFlow", new RWComponentPropertyModel<Category>("typeOfFlow"),
                SB.categoryProvider);
        typeOfFlow.required();
        add(typeOfFlow);

        DropDownField<Category> typeOfFinance = new DropDownField<>("12typeOfFinance", new RWComponentPropertyModel<Category>("typeOfFinance"),
                SB.categoryProvider);
        typeOfFinance.required();
        add(typeOfFinance);

        DropDownField<Category> typeOfAid = new DropDownField<>("13typeOfAid", new RWComponentPropertyModel<Category>("typeOfAid"),
                SB.categoryProvider);
        add(typeOfAid);

        TextAreaInputField activityProjectTitle = new TextAreaInputField("14activityProjectTitle", new RWComponentPropertyModel<String>("shortDescription"));
        add(activityProjectTitle);

        DropDownField<Category> sectorPurposeCode = new DropDownField<>("15sectorPurposeCode", new RWComponentPropertyModel<Category>("sector"),
                SB.categoryProvider);
        add(sectorPurposeCode);
    }

    public static ITabWithKey newTab(Component askingComponent) {
        return new AbstractTabWithKey(new StringResourceModel(KEY, askingComponent, null), KEY) {
            private static final long serialVersionUID = -724508987522388955L;

            @Override
            public WebMarkupContainer getPanel(String panelId) {
                return new BasicDataTab(panelId);
            }
        };
    }

    @Override
    public String getPermissionKey() {
        return KEY;
    }

    @Override
    public void enableRequired() {
    }
}
