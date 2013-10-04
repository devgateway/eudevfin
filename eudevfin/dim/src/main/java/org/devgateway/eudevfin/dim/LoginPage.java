package org.devgateway.eudevfin.dim;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.eudevfin.dim.spring.SpringWicketWebSession;

public final class LoginPage extends WebPage {

	public LoginPage(final PageParameters parameters) {
		add(new LoginForm("loginform"));
	}

	class LoginForm extends Form {

		private String username;
		private String password;

		public LoginForm(String id) {
			super(id);
			setModel(new CompoundPropertyModel(this));
			add(new RequiredTextField("username"));
			add(new PasswordTextField("password"));
			add(new FeedbackPanel("feedback"));
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