/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.module.components.tabs;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.auth.common.util.AuthUtils;
import org.devgateway.eudevfin.ui.common.components.PreviewableFormPanel;
import org.devgateway.eudevfin.ui.common.permissions.PermissionAwareComponent;

public class CommonProjectTab extends PreviewableFormPanel implements PermissionAwareComponent {

    private boolean editable;

    public CommonProjectTab(String id) {
        super(id);
        verifyEditing();
    }

    private void verifyEditing() {
        if (AuthUtils.currentUserHasRole(AuthConstants.Roles.ROLE_PROJECTS_MFA)
                || AuthUtils.currentUserHasRole(AuthConstants.Roles.ROLE_SUPERVISOR)) {
            editable = true;
        } else if (AuthUtils.currentUserHasRole(AuthConstants.Roles.ROLE_PROJECTS_NGO)) {
            editable = false;
        }
    }

    @Override
    public String getPermissionKey() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void enableRequired() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean isEditable() {
        return editable;
    }

    @Override
    public MarkupContainer add(Component... childs) {
        for (Component component : childs) {
            component.setEnabled(editable);
        }
        
        return super.add(childs); 
    }
}
