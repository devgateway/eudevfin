/*******************************************************************************
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors:
 *    mihai
 ******************************************************************************/

package org.devgateway.eudevfin.mcm.pages;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.authroles.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.PatternValidator;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.auth.common.domain.PersistedAuthority;
import org.devgateway.eudevfin.auth.common.domain.PersistedUser;
import org.devgateway.eudevfin.auth.common.domain.PersistedUserGroup;
import org.devgateway.eudevfin.auth.common.service.PersistedUserService;
import org.devgateway.eudevfin.mcm.models.PasswordEncryptModel;
import org.devgateway.eudevfin.mcm.providers.PersistedAuthorityChoiceProvider;
import org.devgateway.eudevfin.mcm.providers.PersistedUserGroupChoiceProvider;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;
import org.devgateway.eudevfin.ui.common.components.BootstrapCancelButton;
import org.devgateway.eudevfin.ui.common.components.BootstrapSubmitButton;
import org.devgateway.eudevfin.ui.common.components.CheckBoxField;
import org.devgateway.eudevfin.ui.common.components.DropDownField;
import org.devgateway.eudevfin.ui.common.components.MultiSelectField;
import org.devgateway.eudevfin.ui.common.components.PasswordInputField;
import org.devgateway.eudevfin.ui.common.components.TextInputField;
import org.devgateway.eudevfin.ui.common.pages.HeaderFooter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.wicketstuff.annotation.mount.MountPath;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;

/**
 * @author mihai
 * @since 17.12.2013
 */
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
@MountPath(value = "/account")
public class EditPersistedUserPage extends HeaderFooter {

	@SpringBean
	protected PersistedUserService userService;

	@SpringBean
	private PersistedAuthorityChoiceProvider authorityChoiceProvider;

	@SpringBean
	private PersistedUserGroupChoiceProvider userGroupChoiceProvider;

	protected final NotificationPanel feedbackPanel;

	private static final long serialVersionUID = -4276784345759050002L;
	private static final Logger logger = Logger.getLogger(EditPersistedUserPage.class);

	public class PasswordPatternValidator extends PatternValidator {

		// 1 digit, 1 lower, 1 upper, 1 symbol "@#$%", from 6 to 20
		private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";

		public PasswordPatternValidator() {
			super(PASSWORD_PATTERN);
		}

	}

	public class UniqueUsernameValidator extends Behavior implements IValidator<String> {

		private static final long serialVersionUID = -2412508063601996929L;
		private Long userId;

		public UniqueUsernameValidator(Long userId) {
			this.userId = userId;
		}

		@Override
		public void validate(IValidatable<String> validatable) {
			String username = validatable.getValue();
			PersistedUser persistedUser2 = userService.findByUsername(username).getEntity();
			if (persistedUser2 != null && !persistedUser2.getId().equals(userId)) {
				ValidationError error = new ValidationError();
				error.addKey("uniqueUsernameError");
				validatable.error(error);
			}
		}
	}

	/**
	 * Override this by elevated subclasses to provide a different user to be
	 * editable (for eg by reading a userId {@link PageParameters}
	 * 
	 * @param parameters
	 * @return
	 */
	public PersistedUser getEditablePersistedUser(final PageParameters parameters) {
		return (PersistedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	@SuppressWarnings("unchecked")
	public EditPersistedUserPage(final PageParameters parameters) {
		super(parameters);

		final PersistedUser persistedUser = getEditablePersistedUser(parameters);

		Form form = new Form("form");

		CompoundPropertyModel<? extends PersistedUser> model = new CompoundPropertyModel<>(persistedUser);
		setModel(model);

		TextInputField<String> userName = new TextInputField<>("username", new RWComponentPropertyModel<String>(
				"username")).required().typeString();

		TextInputField<String> firstName = new TextInputField<>("firstName", new RWComponentPropertyModel<String>(
				"firstName")).required().typeString();

		TextInputField<String> lastName = new TextInputField<>("lastName", new RWComponentPropertyModel<String>(
				"lastName")).required().typeString();

		TextInputField<String> email = new TextInputField<>("email", new RWComponentPropertyModel<String>("email"))
				.typeString();
		email.getField().add(EmailAddressValidator.getInstance());

		TextInputField<String> phone = new TextInputField<>("phone", new RWComponentPropertyModel<String>("phone"))
				.typeString();

		userName.getField().add(new UniqueUsernameValidator(persistedUser.getId()));

		PasswordInputField password = new PasswordInputField("password", new PasswordEncryptModel(
				new RWComponentPropertyModel<String>("password")));
		password.getField().setResetPassword(false);
		password.getField().add(new PasswordPatternValidator());

		PasswordInputField passwordCheck = new PasswordInputField("passwordCheck", new PasswordEncryptModel(
				new RWComponentPropertyModel<String>("passwordCheck")));
		passwordCheck.getField().setResetPassword(false);

		CheckBoxField enabled = new CheckBoxField("enabled", new RWComponentPropertyModel<Boolean>("enabled"));

		MultiSelectField<PersistedAuthority> authorities = new MultiSelectField<>("persistedAuthorities",
				new RWComponentPropertyModel<Collection<PersistedAuthority>>("persistedAuthorities"),
				authorityChoiceProvider);

		authorities.required();

		DropDownField<PersistedUserGroup> group = new DropDownField<>("group",
				new RWComponentPropertyModel<PersistedUserGroup>("group"), userGroupChoiceProvider);

		form.add(userName);
		MetaDataRoleAuthorizationStrategy.authorize(userName, Component.ENABLE, AuthConstants.Roles.ROLE_SUPERVISOR);

		form.add(firstName);
		form.add(lastName);

		form.add(group);
		MetaDataRoleAuthorizationStrategy.authorize(group, Component.ENABLE, AuthConstants.Roles.ROLE_SUPERVISOR);

		form.add(email);
		form.add(password);
		form.add(passwordCheck);
		form.add(phone);

		form.add(enabled);
		MetaDataRoleAuthorizationStrategy.authorize(enabled, Component.ENABLE, AuthConstants.Roles.ROLE_SUPERVISOR);

		form.add(authorities);
		MetaDataRoleAuthorizationStrategy.authorize(authorities, Component.ENABLE, AuthConstants.Roles.ROLE_SUPERVISOR);

		form.add(new BootstrapSubmitButton("submit", new StringResourceModel("button.submit", this, null, null)) {

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				super.onError(target, form);
				target.add(feedbackPanel);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				logger.info("Submitted ok!");
				logger.info("Object:" + getModel().getObject());
				userService.save(persistedUser);
				setResponsePage(ListPersistedUsersPage.class);
			}

		});

		form.add(new BootstrapCancelButton("cancel", new StringResourceModel("button.cancel", this, null, null)) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				logger.info("Cancel pressed");
				setResponsePage(ListPersistedUsersPage.class);
			}

		});

		add(form);
		form.add(new EqualPasswordInputValidator(password.getField(), passwordCheck.getField()));

		feedbackPanel = new NotificationPanel("feedback");
		feedbackPanel.setOutputMarkupId(true);
		feedbackPanel.hideAfter(Duration.seconds(3));
		add(feedbackPanel);
	}

}
