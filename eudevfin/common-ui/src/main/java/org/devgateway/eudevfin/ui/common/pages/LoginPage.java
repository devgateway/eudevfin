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

import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.eudevfin.ui.common.components.AbstractInputField;
import org.devgateway.eudevfin.ui.common.components.PasswordInputField;
import org.devgateway.eudevfin.ui.common.components.TextInputField;
import org.devgateway.eudevfin.ui.common.spring.SpringWicketWebSession;
import org.wicketstuff.annotation.mount.MountPath;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;

@MountPath(value = "/login")
public final class LoginPage extends HeaderFooter {
    private static final long serialVersionUID = 8126072556090927364L;

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

            AbstractInputField<String> password = new PasswordInputField("password", new PropertyModel<String>(this, "password"), "login.password");
            add(password);

		}

		@Override
		protected void onSubmit() {
			SpringWicketWebSession session = SpringWicketWebSession
					.getSpringWicketWebSession();
			if (session.signIn(username, password)) {
				continueToOriginalDestination();
			} else {
				setResponsePage(getApplication().getHomePage());
			}
		}

	}
}
