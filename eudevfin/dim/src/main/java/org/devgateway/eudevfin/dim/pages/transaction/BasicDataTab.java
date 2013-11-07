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
import org.apache.wicket.model.ComponentPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.devgateway.eudevfin.dim.core.StaticBinds;
import org.devgateway.eudevfin.dim.core.components.DropDownField;
import org.devgateway.eudevfin.dim.core.components.TextInputField;

/**
 * @author aartimon@developmentgateway.org
 * @since 01 November 2013
 */
public class BasicDataTab extends Panel{
    private BasicDataTab(String id) {
        super(id);

        DropDownField<String> recipient = new DropDownField<String>("7recipient", new ComponentPropertyModel<String>("recipient"), "7recipient",
                StaticBinds.countryProvider);
        add(recipient);

        DropDownField<String> cpa = new DropDownField<String>("7bCPA", new ComponentPropertyModel<String>("CPA"), "7bCPA",
                StaticBinds.yesNoProvider);
        add(cpa);

        TextInputField<String> channelDelivery = new TextInputField<String>("8channelDelivery", new ComponentPropertyModel<String>("channelDelivery"), "8channelDelivery");
        add(channelDelivery);

        DropDownField<String> channelCode= new DropDownField<String>("9channelCode", new ComponentPropertyModel<String>("channelCode"), "9channelCode",
                StaticBinds.countryProvider);
        add(channelCode);




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
