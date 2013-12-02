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
import org.devgateway.eudevfin.dim.core.models.DateToLocalDateTimeModel;
import org.devgateway.eudevfin.dim.core.permissions.PermissionAwareComponent;
import org.devgateway.eudevfin.dim.core.temporary.SB;
import org.devgateway.eudevfin.financial.Category;
import org.joda.time.LocalDateTime;

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
        TextInputField<String> geographicalTargetArea = new TextInputField<>("16geographicalTargetArea", new RWComponentPropertyModel<String>("geoTargetArea"));
        add(geographicalTargetArea);

        DateInputField startingDate = new DateInputField("17startingDate", new DateToLocalDateTimeModel(new RWComponentPropertyModel<LocalDateTime>("expectedStartDate")));
        add(startingDate);

        DateInputField completionDate = new DateInputField("18completionDate", new DateToLocalDateTimeModel(new RWComponentPropertyModel<LocalDateTime>("expectedCompletionDate")));
        add(completionDate);

        TextAreaInputField description = new TextAreaInputField("19description", new RWComponentPropertyModel<String>("description"));
        add(description);

        DropDownField<Category> genderEquality = new DropDownField<>("20genderEquality",
                new RWComponentPropertyModel<Category>("genderEquality"), SB.categoryProvider);
        add(genderEquality);

        DropDownField<Category> aidToEnvironment = new DropDownField<>("21aidToEnvironment",
                new RWComponentPropertyModel<Category>("aidToEnvironment"), SB.categoryProvider);
        add(aidToEnvironment);

        DropDownField<Category> pdGg = new DropDownField<>("22pdGg",
                new RWComponentPropertyModel<Category>("pdgg"), SB.categoryProvider);
        add(pdGg);

        DropDownField<Category> tradeDevelopment = new DropDownField<>("23tradeDevelopment",
                new RWComponentPropertyModel<Category>("tradeDevelopment"), SB.categoryProvider);
        add(tradeDevelopment);

        DropDownField<Category> freestandingTechnicalCooperation = new DropDownField<>("24freestandingTechnicalCooperation",
                new RWComponentPropertyModel<Category>("freestandingTechnicalCooperation"), SB.categoryProvider);
        add(freestandingTechnicalCooperation);

        DropDownField<Category> programbasedApproach = new DropDownField<>("25programbasedApproach",
                new RWComponentPropertyModel<Category>("programmeBasedApproach"), SB.categoryProvider);
        add(programbasedApproach);

        DropDownField<Category> investmentProject = new DropDownField<>("26investmentProject",
                new RWComponentPropertyModel<Category>("investment"), SB.categoryProvider);
        add(investmentProject);


        DropDownField<Category> associatedFinancing = new DropDownField<>("27associatedFinancing",
                new RWComponentPropertyModel<Category>("associatedFinancing"), SB.categoryProvider);
        add(associatedFinancing);

        DropDownField<Category> biodiversity = new DropDownField<>("28biodiversity", new RWComponentPropertyModel<Category>("biodiversity"),
                SB.categoryProvider);
        add(biodiversity);

        DropDownField<Category> ccMitigation = new DropDownField<>("29ccMitigation", new RWComponentPropertyModel<Category>("ccMitigation"),
                SB.categoryProvider);
        add(ccMitigation);

        DropDownField<Category> ccAdaptation = new DropDownField<>("30ccAdaptation", new RWComponentPropertyModel<Category>("ccAdaptation"),
                SB.categoryProvider);
        add(ccAdaptation);

        DropDownField<Category> desertification = new DropDownField<>("31desertification", new RWComponentPropertyModel<Category>("desertification"),
                SB.categoryProvider);
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
