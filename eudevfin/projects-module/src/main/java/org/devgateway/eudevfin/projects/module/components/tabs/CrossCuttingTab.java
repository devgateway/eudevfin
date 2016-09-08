/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.module.components.tabs;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.eudevfin.projects.module.validator.WordsValidator;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;
import org.devgateway.eudevfin.ui.common.components.TextAreaInputField;

/**
 *
 * @author stcu
 */
public class CrossCuttingTab extends CommonProjectTab {
    public static final String KEY = "tabs.crosscutting";
    public static int MAX_STRING_LENGTH = 2000;
    public static int MAX_WORDS = 200;

    public CrossCuttingTab(String id, PageParameters parameters) {
        super(id);
        
        addComponents();
    }

    private void addComponents() {
        
        Label tabDescription = new Label("crossCuttingDescription", new StringResourceModel("crossCuttingDescription", this, null, null));
        add(tabDescription);
        
         // 13 Cross-cutting issues: Gender equality
        TextAreaInputField genderEquality = new TextAreaInputField("17genderEquality",
                new RWComponentPropertyModel<String>("genderQuality"));
        genderEquality.maxContentLength(MAX_STRING_LENGTH);
        genderEquality.getField().add(new WordsValidator(MAX_WORDS));
        genderEquality.add(new AttributeAppender("class", "add-left-margin"));
        add(genderEquality);

        // 14 Cross-cutting issues: Human rights and good governance
        TextAreaInputField humanRights = new TextAreaInputField("18humanRights",
                new RWComponentPropertyModel<String>("humanRights"));
        humanRights.maxContentLength(MAX_STRING_LENGTH);
        humanRights.getField().add(new WordsValidator(MAX_WORDS));
        humanRights.add(new AttributeAppender("class", "add-left-margin"));
        add(humanRights);

        // 15 Cross-cutting issues: Sustainable environment
        TextAreaInputField sustainableEnvironment = new TextAreaInputField("19sustainableEnvironment",
                new RWComponentPropertyModel<String>("environment"));
        sustainableEnvironment.maxContentLength(MAX_STRING_LENGTH);
        sustainableEnvironment.getField().add(new WordsValidator(MAX_WORDS));
        sustainableEnvironment.add(new AttributeAppender("class", "add-left-margin"));
        add(sustainableEnvironment);

        // 16 Cross-cutting issues: HIV/AIDS
        TextAreaInputField HIVOrAids = new TextAreaInputField("20HIVOrAids",
                new RWComponentPropertyModel<String>("hivAids"));
        HIVOrAids.maxContentLength(MAX_STRING_LENGTH);
        HIVOrAids.getField().add(new WordsValidator(MAX_WORDS));
        HIVOrAids.add(new AttributeAppender("class", "add-left-margin"));
        add(HIVOrAids);

        // 17 Cross-cutting issues: Other
        TextAreaInputField other = new TextAreaInputField("21Other",
                new RWComponentPropertyModel<String>("other"));
        other.maxContentLength(MAX_STRING_LENGTH);
        other.getField().add(new WordsValidator(MAX_WORDS));
        other.add(new AttributeAppender("class", "add-left-margin"));
        add(other);
    }

    @Override
    public String getPermissionKey() {
        return KEY;
    }

    @Override
    public void enableRequired() {
    }
}
