/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/

package org.devgateway.eudevfin.mcm.pages;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.auth.common.domain.PersistedUser;
import org.devgateway.eudevfin.auth.common.domain.PersistedUserGroup;
import org.devgateway.eudevfin.auth.common.service.PersistedUserGroupService;
import org.devgateway.eudevfin.mcm.providers.PersistedUserGroupChoiceProvider;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;
import org.devgateway.eudevfin.ui.common.components.*;
import org.devgateway.eudevfin.ui.common.pages.HeaderFooter;
import org.devgateway.eudevfin.ui.common.providers.OrganizationChoiceProvider;
import org.devgateway.eudevfin.ui.common.providers.PersistedUserChoiceProvider;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.Collection;

/**
 * @author mihai
 * @since 17.12.2013
 */
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_SUPERVISOR)
@MountPath(value = "/group")
public class EditPersistedUserGroupPage extends HeaderFooter {

    @SpringBean
    private PersistedUserGroupService userGroupService;

    @SpringBean
    private PersistedUserGroupChoiceProvider userGroupChoiceProvider;

    @SpringBean
    private PersistedUserChoiceProvider userChoiceProvider;

    @SpringBean
    private OrganizationChoiceProvider organizationChoiceProvider;


    public static final String PARAM_GROUP_ID = "groupId";
    private static final long serialVersionUID = -4276784345759050002L;
    private static final Logger logger = Logger.getLogger(EditPersistedUserGroupPage.class);

    public class UniqueGroupNameValidator extends Behavior implements
            IValidator<String> {

        private Long groupId;

        public UniqueGroupNameValidator(Long groupId) {
            this.groupId = groupId;
        }

        @Override
        public void validate(IValidatable<String> validatable) {
            String groupName = validatable.getValue();
            PersistedUserGroup persistedGroup2 = userGroupService.findByName(groupName).getEntity();

            if (persistedGroup2 != null
                    && !persistedGroup2.getId().equals(groupId)) {
                ValidationError error = new ValidationError();
                error.addKey("uniqueUsernameError");
                validatable.error(error);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public EditPersistedUserGroupPage(final PageParameters parameters) {
        super(parameters);

        Form form = new Form("form");
        Long groupId = null;
        final PersistedUserGroup persistedUserGroup;

        if (!parameters.get(PARAM_GROUP_ID).isNull()) {
            groupId = parameters.get(PARAM_GROUP_ID).toLong();
            persistedUserGroup = userGroupService.findOne(groupId).getEntity();
        } else {
            persistedUserGroup = new PersistedUserGroup();
        }

        CompoundPropertyModel<? extends PersistedUserGroup> model = new CompoundPropertyModel<PersistedUserGroup>(
                persistedUserGroup);
        setModel(model);

        TextInputField<String> groupName = new TextInputField<String>(
                "name", new RWComponentPropertyModel<String>("name")).required();
        groupName.typeString();
        groupName.getField().add(new UniqueGroupNameValidator(persistedUserGroup.getId()));


        DropDownField<Organization> organization = new DropDownField<Organization>("organization",
                new RWComponentPropertyModel<Organization>("organization"), organizationChoiceProvider);

        organization.required();

        MultiSelectField<PersistedUser> users = new MultiSelectField<PersistedUser>(
                "users",
                new RWComponentPropertyModel<Collection<PersistedUser>>(
                        "users"), userChoiceProvider);
        users.setEnabled(false);

        form.add(new BootstrapSubmitButton("submit", new StringResourceModel("button.submit", this, null, null)) {

            private static final long serialVersionUID = -7833958712063599191L;

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                // TODO Auto-generated method stub
                super.onError(target, form);
            }

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                logger.info("Submitted ok!");
                logger.info("Object:" + getModel().getObject());
                userGroupService.save(persistedUserGroup);
                setResponsePage(ListPersistedUserGroupsPage.class);
            }

        });


        form.add(new BootstrapCancelButton("cancel", new StringResourceModel("button.cancel", this, null, null)) {
            private static final long serialVersionUID = 4352988032579728822L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                logger.info("Cancel pressed");
                setResponsePage(ListPersistedUserGroupsPage.class);
            }

        }.setDefaultFormProcessing(false));

        form.add(groupName);
        form.add(organization);
        form.add(users);
        add(form);
    }

}
