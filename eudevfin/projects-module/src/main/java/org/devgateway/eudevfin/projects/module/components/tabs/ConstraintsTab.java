/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.module.components.tabs;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.eudevfin.projects.module.validator.WordsValidator;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;
import org.devgateway.eudevfin.ui.common.components.PreviewableFormPanel;
import org.devgateway.eudevfin.ui.common.components.TextAreaInputField;
import org.devgateway.eudevfin.ui.common.permissions.PermissionAwareComponent;

/**
 *
 * @author alcr
 */
public class ConstraintsTab extends PreviewableFormPanel implements PermissionAwareComponent {

    public static final String KEY = "tabs.constraints";
    public static int MAX_STRING_LENGTH = 2000;
    public static int MAX_WORDS = 200;

    public ConstraintsTab(String id, PageParameters parameters) {
        super(id);

        addComponents();
    }

    private void addComponents() {

        // 11. Project objectives
        TextAreaInputField implementingConstraints = new TextAreaInputField("15implementingConstraints",
                new RWComponentPropertyModel<String>("implConstraints"));
        implementingConstraints.maxContentLength(MAX_STRING_LENGTH);
        implementingConstraints.getField().add(new WordsValidator(MAX_WORDS));
        add(implementingConstraints);

        // 12. Lessons learnt
        TextAreaInputField lessonsLearnt = new TextAreaInputField("16lessonsLearnt",
                new RWComponentPropertyModel<String>("lessonLearn"));
        lessonsLearnt.maxContentLength(MAX_STRING_LENGTH);
        lessonsLearnt.getField().add(new WordsValidator(MAX_WORDS));
        add(lessonsLearnt);

       
    }

    @Override
    public String getPermissionKey() {
        return KEY;
    }

    @Override
    public void enableRequired() {
    }
}
