/*******************************************************************************
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors:
 *    aartimon
 ******************************************************************************/

package org.devgateway.eudevfin.dim.pages.transaction;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;
import org.devgateway.eudevfin.dim.core.RWComponentPropertyModel;
import org.devgateway.eudevfin.dim.core.StaticBinds;
import org.devgateway.eudevfin.dim.core.components.DropDownField;
import org.devgateway.eudevfin.dim.core.components.TextAreaInputField;
import org.devgateway.eudevfin.dim.core.components.TextInputField;

/**
 * @author aartimon@developmentgateway.org
 * @since 01 November 2013
 */
public class BasicDataTab extends Panel{
    private BasicDataTab(String id) {
        super(id);
        addComponents();
    }

    private void addComponents() {
        DropDownField<String> recipient = new DropDownField<>("7recipient", new RWComponentPropertyModel<String>("recipient"), "7recipient",
                StaticBinds.countryProvider);
        add(recipient);

        DropDownField<String> cpa = new DropDownField<>("7bCPA", new RWComponentPropertyModel<String>("CPA"), "7bCPA",
                StaticBinds.yesNoProvider);
        add(cpa);

        TextInputField<String> channelDelivery = new TextInputField<>("8channelDelivery", new RWComponentPropertyModel<String>("channelDelivery"));
        add(channelDelivery);

        DropDownField<String> channelCode = new DropDownField<>("9channelCode", new RWComponentPropertyModel<String>("channelCode"), "9channelCode",
                StaticBinds.countryProvider);
        add(channelCode);

        DropDownField<String> bilateralMultilateral = new DropDownField<>("10bilateralMultilateral", new RWComponentPropertyModel<String>("bilateralMultilateral"), "10bilateralMultilateral",
                StaticBinds.countryProvider);
        add(bilateralMultilateral);

        DropDownField<String> typeOfFlow = new DropDownField<>("11typeOfFlow", new RWComponentPropertyModel<String>("typeOfFlow"), "11typeOfFlow",
                StaticBinds.countryProvider);
        add(typeOfFlow);

        DropDownField<String> typeOfFinance = new DropDownField<>("12typeOfFinance", new RWComponentPropertyModel<String>("typeOfFinance"), "12typeOfFinance",
                StaticBinds.countryProvider);
        add(typeOfFinance);

        DropDownField<String> typeOfAid = new DropDownField<>("13typeOfAid", new RWComponentPropertyModel<String>("typeOfAid"), "13typeOfAid",
                StaticBinds.countryProvider);
        add(typeOfAid);

        TextAreaInputField activityProjectTitle = new TextAreaInputField("14activityProjectTitle", new RWComponentPropertyModel<String>("activityProjectTitle"), "14activityProjectTitle");
        add(activityProjectTitle);

        DropDownField<String> sectorPurposeCode = new DropDownField<>("15sectorPurposeCode", new RWComponentPropertyModel<String>("sectorPurposeCode"), "15sectorPurposeCode",
                StaticBinds.countryProvider);
        add(sectorPurposeCode);
    }

    public static ITab newTab(Component askingComponent){
        return new AbstractTab(new StringResourceModel("tabs.basic", askingComponent, null)){
            private static final long serialVersionUID = -724508987522388955L;

            @Override
            public WebMarkupContainer getPanel(String panelId) {
                return new BasicDataTab(panelId);
            }
        };
    }
}
