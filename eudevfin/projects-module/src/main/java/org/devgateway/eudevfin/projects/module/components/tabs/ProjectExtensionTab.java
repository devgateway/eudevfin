/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.module.components.tabs;

import java.util.Collection;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.eudevfin.projects.common.entities.ProjectFileWrapper;
import org.devgateway.eudevfin.projects.module.components.fields.MultiProjectFileUploadField;
import static org.devgateway.eudevfin.projects.module.components.tabs.ConstraintsTab.KEY;
import org.devgateway.eudevfin.projects.module.components.util.ProjectUtil;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;
import org.devgateway.eudevfin.ui.common.components.DateInputField;
import org.devgateway.eudevfin.ui.common.components.TextAreaInputField;
import org.devgateway.eudevfin.ui.common.models.DateToLocalDateTimeModel;
import org.joda.time.LocalDateTime;

/**
 *
 * @author stcu
 */
public class ProjectExtensionTab extends CommonProjectTab {
    public static final String KEY = "tabs.projectExtension";
    
    public ProjectExtensionTab(String id,PageParameters parameters) {
        super(id);
        
        addComponents();
    }
    
    private void addComponents() {
        // 22. Project Current Date 
        DateInputField currentDate = new DateInputField("22currentDate", new DateToLocalDateTimeModel(new RWComponentPropertyModel<LocalDateTime>("extendCurrentDate")));
        add(currentDate);

        // 23. Project Extend Date
        DateInputField extendDate = new DateInputField("23extendDate", new DateToLocalDateTimeModel(new RWComponentPropertyModel<LocalDateTime>("extendDate")));
        add(extendDate);
        
        // 24. Project Extend Date
        TextAreaInputField contactMonitoringEmail = new TextAreaInputField("24reason", new RWComponentPropertyModel<String>("extendedReason"));
        contactMonitoringEmail.maxContentLength(128);
        contactMonitoringEmail.setRows(ProjectUtil.MAX_AREA_ROWS);
        add(contactMonitoringEmail);
        
        // 25. Upload
        MultiProjectFileUploadField transactionDocumentation = new MultiProjectFileUploadField("25upload", new RWComponentPropertyModel<Collection<ProjectFileWrapper>>("extendedDocumentation"));
        transactionDocumentation.maxFiles(10);
        add(transactionDocumentation);
    }
    
    @Override
    public String getPermissionKey() {
        return KEY;
    }

    @Override
    public void enableRequired() {
    }
    
}
