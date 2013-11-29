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
import org.devgateway.eudevfin.dim.core.components.DateInputField;
import org.devgateway.eudevfin.dim.core.components.DropDownField;
import org.devgateway.eudevfin.dim.core.components.TextAreaInputField;
import org.devgateway.eudevfin.dim.core.components.TextInputField;
import org.devgateway.eudevfin.dim.core.components.tabs.AbstractTabWithKey;
import org.devgateway.eudevfin.dim.core.components.tabs.ITabWithKey;
import org.devgateway.eudevfin.dim.core.permissions.PermissionAwareComponent;
import org.devgateway.eudevfin.dim.core.temporary.SB;

import java.util.Date;

/**
 * @author aartimon@developmentgateway.org
 * @since 01 November 2013
 */
public class SupplementaryDataTab extends Panel implements PermissionAwareComponent {


    public static final String KEY = "tabs.supplementary";

    public SupplementaryDataTab(String id) {
        super(id);
        addComponents();
    }

    private void addComponents() {
        TextInputField<String> geographicalTargetArea = new TextInputField<>("16geographicalTargetArea", new RWComponentPropertyModel<String>("geographicalTargetArea"));
        add(geographicalTargetArea);

        DateInputField startingDate = new DateInputField("17startingDate", new RWComponentPropertyModel<Date>("startingDate"));
        add(startingDate);

        DateInputField completionDate = new DateInputField("18completionDate", new RWComponentPropertyModel<Date>("completionDate"));
        add(completionDate);

        TextAreaInputField description = new TextAreaInputField("19description", new RWComponentPropertyModel<String>("description"));
        add(description);

        DropDownField<String> genderEquality = new DropDownField<>("20genderEquality", new RWComponentPropertyModel<String>("genderEquality"),
                SB.countryProvider);
        add(genderEquality);

        DropDownField<String> aidToEnvironment = new DropDownField<>("21aidToEnvironment", new RWComponentPropertyModel<String>("aidToEnvironment"),
                SB.countryProvider);
        add(aidToEnvironment);

        DropDownField<String> pdGg = new DropDownField<>("22pdGg", new RWComponentPropertyModel<String>("pdGg"),
                SB.countryProvider);
        add(pdGg);

        DropDownField<String> tradeDevelopment = new DropDownField<>("23tradeDevelopment", new RWComponentPropertyModel<String>("tradeDevelopment"),
                SB.countryProvider);
        add(tradeDevelopment);

        DropDownField<String> freestandingTechnicalCooperation = new DropDownField<>("24freestandingTechnicalCooperation", new RWComponentPropertyModel<String>("freestandingTechnicalCooperation"),
                SB.countryProvider);
        add(freestandingTechnicalCooperation);

        DropDownField<String> programbasedApproach = new DropDownField<>("25programbasedApproach", new RWComponentPropertyModel<String>("programbasedApproach"),
                SB.countryProvider);
        add(programbasedApproach);

        DropDownField<String> investmentProject = new DropDownField<>("26investmentProject", new RWComponentPropertyModel<String>("investmentProject"),
                SB.countryProvider);
        add(investmentProject);

        DropDownField<String> associatedFinancing = new DropDownField<>("27associatedFinancing", new RWComponentPropertyModel<String>("associatedFinancing"),
                SB.countryProvider);
        add(associatedFinancing);

        DropDownField<String> biodiversity = new DropDownField<>("28biodiversity", new RWComponentPropertyModel<String>("biodiversity"),
                SB.countryProvider);
        add(biodiversity);

        DropDownField<String> ccMitigation = new DropDownField<>("29ccMitigation", new RWComponentPropertyModel<String>("ccMitigation"),
                SB.countryProvider);
        add(ccMitigation);

        DropDownField<String> ccAdaptation = new DropDownField<>("30ccAdaptation", new RWComponentPropertyModel<String>("ccAdaptation"),
                SB.countryProvider);
        add(ccAdaptation);

        DropDownField<String> desertification = new DropDownField<>("31desertification", new RWComponentPropertyModel<String>("desertification"),
                SB.countryProvider);
        add(desertification);

        DropDownField<String> tbd96 = new DropDownField<>("96tbd", new RWComponentPropertyModel<String>("tbd96"),
                SB.countryProvider);
        add(tbd96);

        DropDownField<String> tbd97 = new DropDownField<>("97tbd", new RWComponentPropertyModel<String>("tbd97"),
                SB.countryProvider);
        add(tbd97);

        DropDownField<String> tbd98 = new DropDownField<>("98tbd", new RWComponentPropertyModel<String>("tbd98"),
                SB.countryProvider);
        add(tbd98);

        DropDownField<String> tbd99 = new DropDownField<>("99tbd", new RWComponentPropertyModel<String>("tbd99"),
                SB.countryProvider);
        add(tbd99);
    }

    public static ITabWithKey newTab(Component askingComponent) {
        return new AbstractTabWithKey(new StringResourceModel(KEY, askingComponent, null), KEY) {
            private static final long serialVersionUID = -724508987522388955L;

            @Override
            public WebMarkupContainer getPanel(String panelId) {
                return new SupplementaryDataTab(panelId);
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
