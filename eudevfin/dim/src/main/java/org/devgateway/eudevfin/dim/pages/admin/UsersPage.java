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

package org.devgateway.eudevfin.dim.pages.admin;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.auth.common.domain.PersistedAuthority;
import org.devgateway.eudevfin.auth.common.domain.PersistedUser;
import org.devgateway.eudevfin.auth.repository.PersistedUserRepository;
import org.devgateway.eudevfin.dim.core.RWComponentPropertyModel;
import org.devgateway.eudevfin.dim.core.components.CheckBoxField;
import org.devgateway.eudevfin.dim.core.components.MultiSelectField;
import org.devgateway.eudevfin.dim.core.components.PasswordInputField;
import org.devgateway.eudevfin.dim.core.components.TextInputField;
import org.devgateway.eudevfin.dim.core.models.PasswordEncryptModel;
import org.devgateway.eudevfin.dim.core.pages.HeaderFooter;
import org.devgateway.eudevfin.dim.providers.PersistedAuthorityChoiceProvider;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author mihai
 * @since 17.12.2013
 */
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_SUPERVISOR)
@MountPath(value = "/users")
public class UsersPage extends HeaderFooter {

	@SpringBean
	private PersistedUserRepository persistedUserRepository;
	
	@SpringBean
	private PersistedAuthorityChoiceProvider authorityChoiceProvider;

	private static final long serialVersionUID = -4276784345759050002L;
	private static final Logger logger = Logger.getLogger(UsersPage.class);

	@SuppressWarnings("unchecked")
	public UsersPage(final PageParameters parameters) {
		Form form = new Form("form");
		Long userId = null;
		final PersistedUser persistedUser;

		if (!parameters.get("userId").isNull()) {
			userId = parameters.get("userId").toLong();
			persistedUser = persistedUserRepository.findOne(userId);
		} else {
			persistedUser = new PersistedUser();
		}

		CompoundPropertyModel<? extends PersistedUser> model = new CompoundPropertyModel<PersistedUser>(
				persistedUser);
		setModel(model);

		TextInputField<String> userName = new TextInputField<String>(
				"username", new RWComponentPropertyModel<String>("username"));

		PasswordInputField password = new PasswordInputField("password",
				new PasswordEncryptModel(new RWComponentPropertyModel<String>(
						"password")));

		CheckBoxField enabled = new CheckBoxField("enabled",
				new RWComponentPropertyModel<Boolean>("enabled"));

		MultiSelectField<PersistedAuthority> authorities = new MultiSelectField<>(
				"authorities",
				new RWComponentPropertyModel<Collection<PersistedAuthority>>(
				"authorities"), authorityChoiceProvider);

		form.add(userName);
		form.add(password);
		form.add(enabled);
		form.add(authorities);

		form.add(new IndicatingAjaxButton("submit", Model.of("Submit")) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				logger.info("Submitted ok!");
				logger.info("Object:" + getModel().getObject());
				persistedUserRepository.save(persistedUser);
			}
		});

		add(form);
	}

}
