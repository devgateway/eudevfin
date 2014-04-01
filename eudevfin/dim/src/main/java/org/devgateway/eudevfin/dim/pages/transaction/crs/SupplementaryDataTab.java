/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.pages.transaction.crs;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.eudevfin.dim.providers.CategoryProviderFactory;
import org.devgateway.eudevfin.financial.Category;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;
import org.devgateway.eudevfin.ui.common.components.DateInputField;
import org.devgateway.eudevfin.ui.common.components.DropDownField;
import org.devgateway.eudevfin.ui.common.components.TextAreaInputField;
import org.devgateway.eudevfin.ui.common.components.TextInputField;
import org.devgateway.eudevfin.ui.common.events.MarkersField13UpdateBehavior;
import org.devgateway.eudevfin.ui.common.models.DateToLocalDateTimeModel;
import org.devgateway.eudevfin.ui.common.permissions.PermissionAwareComponent;
import org.devgateway.eudevfin.ui.common.temporary.SB;
import org.joda.time.LocalDateTime;

/**
 * @author aartimon@developmentgateway.org
 * @since 01 November 2013
 */
public class SupplementaryDataTab extends Panel implements PermissionAwareComponent {
    public static final String KEY = "tabs.supplementary";
    protected PageParameters parameters;
    
    @SpringBean
    private CategoryProviderFactory categoryFactory;

    public SupplementaryDataTab(String id,PageParameters parameters) {
        super(id);
        this.parameters=parameters;
        addComponents();
    }

    private void addComponents() {
        TextInputField<String> geographicalTargetArea = new TextInputField<>("16geographicalTargetArea", new RWComponentPropertyModel<String>("geoTargetArea"));
        geographicalTargetArea.typeString();
        add(geographicalTargetArea);

        DateInputField startingDate = new DateInputField("17startingDate", new DateToLocalDateTimeModel(new RWComponentPropertyModel<LocalDateTime>("expectedStartDate")));
        add(startingDate);

        DateInputField completionDate = new DateInputField("18completionDate", new DateToLocalDateTimeModel(new RWComponentPropertyModel<LocalDateTime>("expectedCompletionDate")));
        add(completionDate);

        TextAreaInputField description = new TextAreaInputField("19description", new RWComponentPropertyModel<String>("description"));
        add(description);

        DropDownField<Category> genderEquality = new DropDownField<>("20genderEquality",
                new RWComponentPropertyModel<Category>("genderEquality"), SB.categoryProvider);
        genderEquality.getField().add(new MarkersField13UpdateBehavior());
        add(genderEquality);

        DropDownField<Category> aidToEnvironment = new DropDownField<>("21aidToEnvironment",
                new RWComponentPropertyModel<Category>("aidToEnvironment"), SB.categoryProvider);
        aidToEnvironment.getField().add(new MarkersField13UpdateBehavior());
        add(aidToEnvironment);

        DropDownField<Category> pdGg = new DropDownField<>("22pdGg",
                new RWComponentPropertyModel<Category>("pdgg"), SB.categoryProvider);
        pdGg.getField().add(new MarkersField13UpdateBehavior()); 
        add(pdGg);

        DropDownField<Category> tradeDevelopment = new DropDownField<>("23tradeDevelopment",
                new RWComponentPropertyModel<Category>("tradeDevelopment"), SB.categoryProvider);
        tradeDevelopment.getField().add(new MarkersField13UpdateBehavior()); 
        add(tradeDevelopment);

        DropDownField<Boolean> freestandingTechnicalCooperation = new DropDownField<>("24freestandingTechnicalCooperation",
                new RWComponentPropertyModel<Boolean>("freestandingTechnicalCooperation"), SB.boolProvider);
        freestandingTechnicalCooperation.getField().add(new MarkersField13UpdateBehavior());
        add(freestandingTechnicalCooperation);

        DropDownField<Boolean> programmeBasedApproach = new DropDownField<>("25programmeBasedApproach",
                new RWComponentPropertyModel<Boolean>("programmeBasedApproach"), SB.boolProvider);
        programmeBasedApproach.getField().add(new MarkersField13UpdateBehavior());
        add(programmeBasedApproach);

        DropDownField<Boolean> investmentProject = new DropDownField<>("26investmentProject",
                new RWComponentPropertyModel<Boolean>("investment"), SB.boolProvider);
        investmentProject.getField().add(new MarkersField13UpdateBehavior());
        add(investmentProject);


        DropDownField<Boolean> associatedFinancing = new DropDownField<>("27associatedFinancing",
                new RWComponentPropertyModel<Boolean>("associatedFinancing"), SB.boolProvider);
        associatedFinancing.getField().add(new MarkersField13UpdateBehavior());
        add(associatedFinancing);

        DropDownField<Category> biodiversity = new DropDownField<>("28biodiversity", new RWComponentPropertyModel<Category>("biodiversity"),
                SB.categoryProvider);
        biodiversity.getField().add(new MarkersField13UpdateBehavior());
        add(biodiversity);

        DropDownField<Category> ccMitigation = new DropDownField<>("29ccMitigation", new RWComponentPropertyModel<Category>("climateChangeMitigation"),
                SB.categoryProvider);
        ccMitigation.getField().add(new MarkersField13UpdateBehavior());
        add(ccMitigation);

        DropDownField<Category> ccAdaptation = new DropDownField<>("30ccAdaptation", new RWComponentPropertyModel<Category>("climateChangeAdaptation"),
                SB.categoryProvider);
        ccAdaptation.getField().add(new MarkersField13UpdateBehavior());
        add(ccAdaptation);

        DropDownField<Category> desertification = new DropDownField<>("31desertification", new RWComponentPropertyModel<Category>("desertification"),
                SB.categoryProvider);
        desertification.getField().add(new MarkersField13UpdateBehavior());
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
