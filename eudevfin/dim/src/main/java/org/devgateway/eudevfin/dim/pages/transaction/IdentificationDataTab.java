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
import org.devgateway.eudevfin.dim.core.components.DateInputField;
import org.devgateway.eudevfin.dim.core.components.DropDownField;
import org.devgateway.eudevfin.dim.core.components.TextInputField;

import java.util.Date;

/**
 * @author aartimon@developmentgateway.org
 * @since 01 November 2013
 */
public class IdentificationDataTab extends Panel {

    private IdentificationDataTab(String id) {
        super(id);
        addComponents();
    }

    private void addComponents() {
        TextInputField<Integer> reportingYear = new TextInputField<>("1reportingYear", new RWComponentPropertyModel<Integer>("reportingYear")
        );
        reportingYear.typeInteger().required().range(1900, 2099).decorateMask("9999");
        add(reportingYear);

        DateInputField commitmentDate = new DateInputField("1bCommitmentDate", new RWComponentPropertyModel<Date>("commitmentDate"));
        add(commitmentDate);

        DropDownField<String> reportingCountry = new DropDownField<>("2reportingCountry", new RWComponentPropertyModel<String>("reportingCountry"),
                StaticBinds.countryProvider);

        add(reportingCountry);

        TextInputField<String> extendingAgency = new TextInputField<>("3extendingAgency", new RWComponentPropertyModel<String>("extendingAgency")
        );
        add(extendingAgency);

        TextInputField<Integer> crsId = new TextInputField<>("4crsId", new RWComponentPropertyModel<Integer>("crsId")
        );
        crsId.typeInteger();
        add(crsId);

        TextInputField<Integer> donorProjectNumber = new TextInputField<>("5donorProjectNumber", new RWComponentPropertyModel<Integer>("donorProjectNumber")
        );
        donorProjectNumber.typeInteger();
        add(donorProjectNumber);

        TextInputField<Integer> natureSubmission = new TextInputField<>("6natureSubmission", new RWComponentPropertyModel<Integer>("natureSubmission")
        );
        natureSubmission.typeInteger();
        add(natureSubmission);
    }

    public static ITab newTab(Component askingComponent) {
        return new AbstractTab(new StringResourceModel("tabs.identification", askingComponent, null)) {
            private static final long serialVersionUID = -724508987522388955L;

            @Override
            public WebMarkupContainer getPanel(String panelId) {
                return new IdentificationDataTab(panelId);
            }
        };
    }

}
