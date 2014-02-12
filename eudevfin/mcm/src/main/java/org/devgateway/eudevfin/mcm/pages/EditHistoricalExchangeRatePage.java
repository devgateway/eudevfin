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
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.auth.common.domain.PersistedAuthority;
import org.devgateway.eudevfin.auth.common.domain.PersistedUser;
import org.devgateway.eudevfin.auth.common.domain.PersistedUserGroup;
import org.devgateway.eudevfin.exchange.common.service.HistoricalExchangeRateService;
import org.devgateway.eudevfin.mcm.models.PasswordEncryptModel;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;
import org.devgateway.eudevfin.ui.common.components.BootstrapSubmitButton;
import org.devgateway.eudevfin.ui.common.components.CheckBoxField;
import org.devgateway.eudevfin.ui.common.components.DropDownField;
import org.devgateway.eudevfin.ui.common.components.MultiSelectField;
import org.devgateway.eudevfin.ui.common.components.PasswordInputField;
import org.devgateway.eudevfin.ui.common.components.TextInputField;
import org.devgateway.eudevfin.ui.common.pages.HeaderFooter;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author mihai
 * @since 17.12.2013
 */
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_SUPERVISOR)
@MountPath(value = "/rate")
public class EditHistoricalExchangeRatePage extends HeaderFooter {

	@SpringBean
	private HistoricalExchangeRateService historicalExchangeRateService;
	
	private static final long serialVersionUID = -4276784345759050002L;
	private static final Logger logger = Logger.getLogger(EditHistoricalExchangeRatePage.class);
	public static final String PARAM_ID="exchangeRateId";
	
	public class UniqueUsernameValidator extends Behavior implements
			IValidator<String> {
	
		private Long userId;

		public UniqueUsernameValidator(Long userId) {
			this.userId=userId;
		}
		
		@Override
		public void validate(IValidatable<String> validatable) {
			String username = validatable.getValue();
			PersistedUser persistedUser2 = userService
					.findByUsername(username).getEntity();
			if (persistedUser2 != null
					&& !persistedUser2.getId().equals(userId)) {
				ValidationError error = new ValidationError();
				error.addKey("uniqueUsernameError");
				validatable.error(error);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public EditHistoricalExchangeRatePage(final PageParameters parameters) {
		super(parameters);
		
		Form form = new Form("form");
		Long userId = null;
		final PersistedUser persistedUser;

		if (!parameters.get(PARAM_ID).isNull()) {
			userId = parameters.get(PARAM_ID).toLong();
			persistedUser = userService.findOne(userId).getEntity();
		} else {
			persistedUser = new PersistedUser();
		}

		CompoundPropertyModel<? extends PersistedUser> model = new CompoundPropertyModel<PersistedUser>(
				persistedUser);
		setModel(model);

		TextInputField<String> userName = new TextInputField<String>(
				"username", new RWComponentPropertyModel<String>("username")).required();
		
		TextInputField<String> firstName = new TextInputField<String>(
				"firstName", new RWComponentPropertyModel<String>("firstName")).required();

		TextInputField<String> lastName = new TextInputField<String>(
				"lastName", new RWComponentPropertyModel<String>("lastName")).required();
		
		TextInputField<String> email = new TextInputField<String>(
				"email", new RWComponentPropertyModel<String>("email"));
		email.getField().add(EmailAddressValidator.getInstance());

		TextInputField<String> phone = new TextInputField<String>(
				"phone", new RWComponentPropertyModel<String>("phone"));
			
		userName.getField().add( new UniqueUsernameValidator(persistedUser.getId()));

		PasswordInputField password = new PasswordInputField("password",
				new PasswordEncryptModel(new RWComponentPropertyModel<String>(
						"password")));
		password.getField().setResetPassword(false);
	

		CheckBoxField enabled = new CheckBoxField("enabled",
				new RWComponentPropertyModel<Boolean>("enabled"));

		MultiSelectField<PersistedAuthority> authorities = new MultiSelectField<>(
				"persistedAuthorities",
				new RWComponentPropertyModel<Collection<PersistedAuthority>>(
				"persistedAuthorities"), authorityChoiceProvider);

		authorities.required();
		
		
		DropDownField<PersistedUserGroup> group = new DropDownField<PersistedUserGroup>("group",
				new RWComponentPropertyModel<PersistedUserGroup>("group"), userGroupChoiceProvider);		


		form.add(userName);
		form.add(firstName);		
		form.add(lastName);
		form.add(group);
		form.add(email);		
		form.add(password);
		form.add(phone);
		form.add(enabled);
		form.add(authorities);

		form.add(new BootstrapSubmitButton("submit", new StringResourceModel("button.submit", this, null, null)) {
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				super.onError(target, form);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				logger.info("Submitted ok!");
				logger.info("Object:" + getModel().getObject());
				userService.save(persistedUser);
				setResponsePage(ListPersistedUsersPage.class);
			}
			
		});
		
		form.add(new BootstrapSubmitButton("cancel", new StringResourceModel("button.cancel", this, null, null)) {			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				logger.info("Cancel pressed");
				setResponsePage(ListPersistedUsersPage.class);
			}
			
		}.setDefaultFormProcessing(false));
	
		add(form);
	}

}
