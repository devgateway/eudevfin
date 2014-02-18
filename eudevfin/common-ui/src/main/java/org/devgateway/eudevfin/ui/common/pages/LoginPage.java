/*******************************************************************************
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors:
 *    aartimon
 ******************************************************************************/
package org.devgateway.eudevfin.ui.common.pages;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.eudevfin.ui.common.components.PasswordInputField;
import org.devgateway.eudevfin.ui.common.components.TextInputField;
import org.devgateway.eudevfin.ui.common.spring.SpringWicketWebSession;
import org.wicketstuff.annotation.mount.MountPath;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;

@MountPath(value = "/login")
public final class LoginPage extends HeaderFooter {
    private static final long serialVersionUID = 8126072556090927364L;
    private static final Logger logger = Logger.getLogger(LoginPage.class);

    public LoginPage(final PageParameters parameters) {
		add(new LoginForm("loginform"));
	}

	class LoginForm extends BootstrapForm {

        private static final long serialVersionUID = 2066636625524650473L;
        private String username;
		private String password;

		public LoginForm(String id) {
			super(id);
            //this.type(FormType.Horizontal);

            add(new NotificationPanel("feedback"));

            TextInputField<String> username = new TextInputField<>("username", new PropertyModel<String>(this, "username"), "login.user");
            username.getField().setRequired(true);
            add(username);

            PasswordInputField password = new PasswordInputField("password", new PropertyModel<String>(this, "password"), "login.password");
            password.getField().setResetPassword(false);
            add(password);
            
            IndicatingAjaxButton submit = new IndicatingAjaxButton("submit",Model.of("Submit")) {
            	@Override
            	protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
            		SpringWicketWebSession session = SpringWicketWebSession.getSpringWicketWebSession();
					if (session.signIn(LoginForm.this.username, LoginForm.this.password)) {
					//	continueToOriginalDestination(); -- this is buggy, we don't use it right now, problems in some IEs (11), we should try in newer versions of wicket
						setResponsePage(getApplication().getHomePage());
					} else {
						logger.warn("Authentication failed for user:"+LoginForm.this.username);
					}
				}
            };
			add(submit);
		}
	}

}
