/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.module.components.fields;

import org.apache.wicket.model.IModel;

import java.util.Collection;
import org.devgateway.eudevfin.projects.common.entities.ProjectFileWrapper;
import org.devgateway.eudevfin.ui.common.components.AbstractInputField;

public class MultiProjectFileUploadField extends AbstractInputField<Collection<ProjectFileWrapper>, MultiProjectFileUploadComponent> {
    
    public MultiProjectFileUploadField(String id, IModel<Collection<ProjectFileWrapper>> model) {
        super(id, model);
    }

    public MultiProjectFileUploadField maxFiles(int maxFiles) {
        field.maxFiles(maxFiles);
        return this;
    }

    @Override
    protected MultiProjectFileUploadComponent newField(String id, IModel<Collection<ProjectFileWrapper>> model) {
        return new MultiProjectFileUploadComponent(id, model);
    }
    
    @Override
    protected void onConfigure() {    	
    	super.onConfigure();
    	if(isFormInPreview()) {
            readOnlyRendition.setVisible(false);
            xPenderController.setVisible(true);    		
    	}
    }
}
