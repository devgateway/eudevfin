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
package org.devgateway.eudevfin.dim.pages;

import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.FormType;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.eudevfin.dim.core.components.InputField;
import org.devgateway.eudevfin.dim.core.components.PasswordInputField;
import org.devgateway.eudevfin.dim.core.pages.HeaderFooter;
import org.devgateway.eudevfin.dim.spring.SpringWicketWebSession;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath(value = "/login")
public final class LoginPage extends HeaderFooter {

	public LoginPage(final PageParameters parameters) {
		add(new LoginForm("loginform"));
	}

	class LoginForm extends BootstrapForm {

		private String username;
		private String password;

		public LoginForm(String id) {
			super(id);
            this.type(FormType.Inline);


            InputField<String> username = new InputField<String>("username", new PropertyModel<String>(this, "username"), "login.user");
            add(username);

            InputField<String> password = new PasswordInputField("password", new PropertyModel<String>(this, "password"), "login.password");
            add(password);


            //add(new RequiredTextField("username"));
			//add(new PasswordTextField("password"));
			//add(new FeedbackPanel("feedback"));
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
