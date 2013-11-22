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
import org.devgateway.eudevfin.dim.core.components.TextAreaInputField;
import org.devgateway.eudevfin.dim.core.components.TextInputField;

import java.util.Date;

/**
 * @author aartimon@developmentgateway.org
 * @since 01 November 2013
 */
public class SupplementaryDataTab extends Panel {


    public SupplementaryDataTab(String id) {
        super(id);
        addComponents();
    }

    private void addComponents() {
        TextInputField<String> geographicalTargetArea = new TextInputField<>("16geographicalTargetArea", new RWComponentPropertyModel<String>("geographicalTargetArea"));
        add(geographicalTargetArea);

        DateInputField startingDate = new DateInputField("17startingDate", new RWComponentPropertyModel<Date>("startingDate"), "17startingDate");
        add(startingDate);

        DateInputField completionDate = new DateInputField("18completionDate", new RWComponentPropertyModel<Date>("completionDate"), "18completionDate");
        add(completionDate);

        TextAreaInputField description = new TextAreaInputField("19description", new RWComponentPropertyModel<String>("description"), "19description");
        add(description);

        DropDownField<String> genderEquality = new DropDownField<>("20genderEquality", new RWComponentPropertyModel<String>("genderEquality"), "20genderEquality",
                StaticBinds.countryProvider);
        add(genderEquality);

        DropDownField<String> aidToEnvironment = new DropDownField<>("21aidToEnvironment", new RWComponentPropertyModel<String>("aidToEnvironment"), "21aidToEnvironment",
                StaticBinds.countryProvider);
        add(aidToEnvironment);

        DropDownField<String> pdGg = new DropDownField<>("22pdGg", new RWComponentPropertyModel<String>("pdGg"), "22pdGg",
                StaticBinds.countryProvider);
        add(pdGg);

        DropDownField<String> tradeDevelopment = new DropDownField<>("23tradeDevelopment", new RWComponentPropertyModel<String>("tradeDevelopment"), "23tradeDevelopment",
                StaticBinds.countryProvider);
        add(tradeDevelopment);

        DropDownField<String> freestandingTechnicalCooperation = new DropDownField<>("24freestandingTechnicalCooperation", new RWComponentPropertyModel<String>("freestandingTechnicalCooperation"), "24freestandingTechnicalCooperation",
                StaticBinds.countryProvider);
        add(freestandingTechnicalCooperation);

        DropDownField<String> programbasedApproach = new DropDownField<>("25programbasedApproach", new RWComponentPropertyModel<String>("programbasedApproach"), "25programbasedApproach",
                StaticBinds.countryProvider);
        add(programbasedApproach);

        DropDownField<String> investmentProject = new DropDownField<>("26investmentProject", new RWComponentPropertyModel<String>("investmentProject"), "26investmentProject",
                StaticBinds.countryProvider);
        add(investmentProject);

        DropDownField<String> associatedFinancing = new DropDownField<>("27associatedFinancing", new RWComponentPropertyModel<String>("associatedFinancing"), "27associatedFinancing",
                StaticBinds.countryProvider);
        add(associatedFinancing);

        DropDownField<String> biodiversity = new DropDownField<>("28biodiversity", new RWComponentPropertyModel<String>("biodiversity"), "28biodiversity",
                StaticBinds.countryProvider);
        add(biodiversity);

        DropDownField<String> ccMitigation = new DropDownField<>("29ccMitigation", new RWComponentPropertyModel<String>("ccMitigation"), "29ccMitigation",
                StaticBinds.countryProvider);
        add(ccMitigation);

        DropDownField<String> ccAdaptation = new DropDownField<>("30ccAdaptation", new RWComponentPropertyModel<String>("ccAdaptation"), "30ccAdaptation",
                StaticBinds.countryProvider);
        add(ccAdaptation);

        DropDownField<String> desertification = new DropDownField<>("31desertification", new RWComponentPropertyModel<String>("desertification"), "31desertification",
                StaticBinds.countryProvider);
        add(desertification);

        DropDownField<String> tbd96 = new DropDownField<>("96tbd", new RWComponentPropertyModel<String>("tbd96"), "96tbd",
                StaticBinds.countryProvider);
        add(tbd96);

        DropDownField<String> tbd97 = new DropDownField<>("97tbd", new RWComponentPropertyModel<String>("tbd97"), "97tbd",
                StaticBinds.countryProvider);
        add(tbd97);

        DropDownField<String> tbd98 = new DropDownField<>("98tbd", new RWComponentPropertyModel<String>("tbd98"), "98tbd",
                StaticBinds.countryProvider);
        add(tbd98);

        DropDownField<String> tbd99 = new DropDownField<>("99tbd", new RWComponentPropertyModel<String>("tbd99"), "99tbd",
                StaticBinds.countryProvider);
        add(tbd99);
    }

    public static ITab newTab(Component askingComponent){
        return new AbstractTab(new StringResourceModel("tabs.supplementary", askingComponent, null)){
            private static final long serialVersionUID = -724508987522388955L;

            @Override
            public WebMarkupContainer getPanel(String panelId) {
                return new SupplementaryDataTab(panelId);
            }
        };
    }
}
