/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.pages.transaction.crs;

import org.apache.wicket.markup.html.panel.Panel;
import org.devgateway.eudevfin.dim.core.components.DateInputField;
import org.devgateway.eudevfin.dim.core.components.DropDownField;
import org.devgateway.eudevfin.dim.core.components.TextAreaInputField;
import org.devgateway.eudevfin.dim.core.components.TextInputField;
import org.devgateway.eudevfin.dim.core.models.DateToLocalDateTimeModel;
import org.devgateway.eudevfin.dim.core.permissions.PermissionAwareComponent;
import org.devgateway.eudevfin.dim.core.temporary.SB;
import org.devgateway.eudevfin.financial.Category;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;
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

        DropDownField<Boolean> freestandingTechnicalCooperation = new DropDownField<>("24freestandingTechnicalCooperation",
                new RWComponentPropertyModel<Boolean>("freestandingTechnicalCooperation"), SB.boolProvider);
        add(freestandingTechnicalCooperation);

        DropDownField<Boolean> programmeBasedApproach = new DropDownField<>("25programmeBasedApproach",
                new RWComponentPropertyModel<Boolean>("programmeBasedApproach"), SB.boolProvider);
        add(programmeBasedApproach);

        DropDownField<Boolean> investmentProject = new DropDownField<>("26investmentProject",
                new RWComponentPropertyModel<Boolean>("investment"), SB.boolProvider);
        add(investmentProject);


        DropDownField<Boolean> associatedFinancing = new DropDownField<>("27associatedFinancing",
                new RWComponentPropertyModel<Boolean>("associatedFinancing"), SB.boolProvider);
        add(associatedFinancing);

        DropDownField<Category> biodiversity = new DropDownField<>("28biodiversity", new RWComponentPropertyModel<Category>("biodiversity"),
                SB.categoryProvider);
        add(biodiversity);

        DropDownField<Category> ccMitigation = new DropDownField<>("29ccMitigation", new RWComponentPropertyModel<Category>("climateChangeMitigation"),
                SB.categoryProvider);
        add(ccMitigation);

        DropDownField<Category> ccAdaptation = new DropDownField<>("30ccAdaptation", new RWComponentPropertyModel<Category>("climateChangeAdaptation"),
                SB.categoryProvider);
        add(ccAdaptation);

        DropDownField<Category> desertification = new DropDownField<>("31desertification", new RWComponentPropertyModel<Category>("desertification"),
                SB.categoryProvider);
        add(desertification);
    }

    @Override
    public String getPermissionKey() {
        return KEY;
    }

    @Override
    public void enableRequired() {
    }
}
