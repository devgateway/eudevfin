/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.ui.common.pages;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.auth.common.domain.PersistedUser;
import org.devgateway.eudevfin.auth.common.service.PersistedUserService;
import org.devgateway.eudevfin.ui.common.components.PasswordInputField;
import org.devgateway.eudevfin.ui.common.components.TextInputField;
import org.devgateway.eudevfin.ui.common.spring.SpringWicketWebSession;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.List;

@MountPath(value = "/login")
public final class LoginPage extends HeaderFooter {
    private static final long serialVersionUID = 8126072556090927364L;
    private static final Logger logger = Logger.getLogger(LoginPage.class);

    @SpringBean
    PersistedUserService userService;

    public LoginPage(final PageParameters parameters) {
    	LoginForm loginForm = new LoginForm("loginform");
    	loginForm.setVisible(!AbstractAuthenticatedWebSession.get().isSignedIn());
        add(loginForm);
    }

    class LoginForm extends BootstrapForm {

        private static final long serialVersionUID = 2066636625524650473L;
        private String username;
        private String password;

        public LoginForm(String id) {
            super(id);

            add(new NotificationPanel("feedback"));

            TextInputField<String> username = new TextInputField<>("username", new PropertyModel<String>(this, "username"), "login.user");
            username.required().typeString();
            add(username);

            PasswordInputField password = new PasswordInputField("password", new PropertyModel<String>(this, "password"), "login.password");
            password.getField().setResetPassword(false);
            add(password);

            // get the email addresses for all super users
            List<PersistedUser> supervisorUsers = userService.findUsersWithPersistedAuthority(AuthConstants.Roles.ROLE_SUPERVISOR);
            String mailTo = "";
            for(int i = 0; i < supervisorUsers.size(); i++) {
                if(supervisorUsers.get(i).getEmail() != null) {
                    mailTo += supervisorUsers.get(i).getEmail() + ",";
                }
            }

            ExternalLink forgotPasswordLink = new ExternalLink("forgotPasswordLink",
                    "mailto:" + mailTo + "?subject=" + new StringResourceModel("login.forgotPassword.subject", LoginPage.this, null, null).getObject(),
                    new StringResourceModel("login.forgotPassword", LoginPage.this, null, null).getObject());
            add(forgotPasswordLink);

            IndicatingAjaxButton submit = new IndicatingAjaxButton("submit", Model.of("Submit")) {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    SpringWicketWebSession session = SpringWicketWebSession.getSpringWicketWebSession();
                    if (session.signIn(LoginForm.this.username, LoginForm.this.password)) {
                        //	continueToOriginalDestination(); -- this is buggy, we don't use it right now, problems in some IEs (11), we should try in newer versions of wicket
                        setResponsePage(getApplication().getHomePage());
                    } else {
                        target.add(LoginForm.this);
                    }
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(LoginForm.this);
                }
            };
            add(submit);
        }
    }

}
