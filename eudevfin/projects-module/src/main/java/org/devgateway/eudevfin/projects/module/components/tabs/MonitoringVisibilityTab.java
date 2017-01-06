/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.module.components.tabs;

import java.util.Collection;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.eudevfin.financial.FileWrapper;
import org.devgateway.eudevfin.projects.module.components.util.ProjectUtil;
import org.devgateway.eudevfin.projects.module.validator.WordsValidator;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;
import org.devgateway.eudevfin.ui.common.components.MultiFileUploadField;
import org.devgateway.eudevfin.ui.common.components.PreviewableFormPanel;
import org.devgateway.eudevfin.ui.common.components.TextAreaInputField;
import org.devgateway.eudevfin.ui.common.permissions.PermissionAwareComponent;

/**
 *
 * @author alcr
 */
public class MonitoringVisibilityTab extends PreviewableFormPanel implements PermissionAwareComponent {

    public static final String KEY = "tabs.monitoring";
    public static int MAX_STRING_LENGTH = 4096;
    public static int MAX_WORDS = 300;

    public MonitoringVisibilityTab(String id, PageParameters parameters) {
        super(id);

        addComponents();
    }

    private void addComponents() {

        // 18. Monitoring 
        TextAreaInputField monitoring = new TextAreaInputField("22monitoring",
                new RWComponentPropertyModel<String>("monitoring"));
        monitoring.maxContentLength(MAX_STRING_LENGTH);
        monitoring.getField().add(new WordsValidator(200));
        add(monitoring);

        // 19. Visibility
        TextAreaInputField visibility = new TextAreaInputField("23visibility",
                new RWComponentPropertyModel<String>("visibility"));
        visibility.maxContentLength(MAX_STRING_LENGTH);
        visibility.getField().add(new WordsValidator(500));
        add(visibility);

        // 20. Visibility upload
        MultiFileUploadField visibilityDocumentation = new MultiFileUploadField("24visibilityDocumentation", new RWComponentPropertyModel<Collection<FileWrapper>>("visibilityDocumentation"));
        visibilityDocumentation.maxFiles(10);
        add(visibilityDocumentation);
    }

    @Override
    public String getPermissionKey() {
        return KEY;
    }

    @Override
    public void enableRequired() {
    }
}
