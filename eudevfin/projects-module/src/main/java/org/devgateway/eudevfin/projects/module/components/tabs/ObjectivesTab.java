/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.module.components.tabs;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import static org.devgateway.eudevfin.projects.module.components.tabs.CrossCuttingTab.MAX_WORDS;
import org.devgateway.eudevfin.projects.module.validator.WordsValidator;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;
import org.devgateway.eudevfin.ui.common.components.TextAreaInputField;

/**
 *
 * @author alcr
 */
public class ObjectivesTab extends CommonProjectTab {
    
    public static final String KEY = "tabs.objectives";
    
    public ObjectivesTab(String id,PageParameters parameters) {
        super(id);
        
        addComponents();
    }
    
    private void addComponents() {
       // 10. Project objectives
       TextAreaInputField projectObjectives = new TextAreaInputField("14projectObjectives",
                               new RWComponentPropertyModel<String>("objectives"));
       projectObjectives.getField().add(new WordsValidator(MAX_WORDS));
       add(projectObjectives);
    }     
     
     @Override
    public String getPermissionKey() {
        return KEY;
    }
    
    @Override
    public void enableRequired() {
    }
}
