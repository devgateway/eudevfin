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

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.auth.common.util.AuthUtils;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.devgateway.eudevfin.metadata.common.service.OrganizationService;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;
import org.devgateway.eudevfin.ui.common.components.BootstrapCancelButton;
import org.devgateway.eudevfin.ui.common.components.BootstrapSubmitButton;
import org.devgateway.eudevfin.ui.common.components.TextInputField;
import org.devgateway.eudevfin.ui.common.pages.HeaderFooter;
import org.wicketstuff.annotation.mount.MountPath;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;

/**
 * @author mihai
 */
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_SUPERVISOR)
@MountPath(value = "/org")
public class EditOrganizationPage extends HeaderFooter<Organization> {

	@SpringBean
	private OrganizationService organizationService;

	protected final NotificationPanel feedbackPanel;

	private static final long serialVersionUID = -4276784345759050002L;
	private static final Logger logger = Logger.getLogger(EditOrganizationPage.class);
	public static final String PARAM_ORG_ID = "orgId";
	public static final String PARAM_ORG_ID_VALUE_NEW = "new";
	
	
	public class UniqueOrganizationCode extends Behavior implements IValidator<String> {
		private static final long serialVersionUID = -4967964480783526901L;
		private Long id;

		public UniqueOrganizationCode(Long id) {
			this.id = id;
		}

		@Override
		public void validate(IValidatable<String> validatable) {
			String code = validatable.getValue();
			Organization org2 = organizationService.findByCode(code).getEntity();
			if (org2 != null && !org2.getId().equals(id)) {
				ValidationError error = new ValidationError();
				error.addKey("uniqueOrgCodeError");
				validatable.error(error);
			}
		}
	}

	public EditOrganizationPage(final PageParameters parameters) {
		super(parameters);

		final Organization org;

		if (Strings.isEqual(parameters.get(PARAM_ORG_ID).toString(), PARAM_ORG_ID_VALUE_NEW)) {
			org = new Organization();
			org.setDac(false);
			Organization organizationForCurrentUser = AuthUtils
					.getOrganizationForCurrentUser();
			org.setDonorCode(organizationForCurrentUser.getDonorName());
			
		} else {
			long orgId = parameters.get(PARAM_ORG_ID).toLong();
			org = organizationService.findOne(orgId).getEntity();			
		}

		Form<Organization> form = new Form<Organization>("form");

		CompoundPropertyModel<Organization> model = new CompoundPropertyModel<>(org);
		setModel(model);

		TextInputField<String> name = new TextInputField<>("name", new RWComponentPropertyModel<String>(
				"name")).required().typeString();
		form.add(name);
		
		TextInputField<String> acronym = new TextInputField<>("acronym", new RWComponentPropertyModel<String>(
				"acronym")).required().typeString();
		form.add(acronym);
		
		TextInputField<String> code = new TextInputField<>("code", new RWComponentPropertyModel<String>(
				"code")).required().typeString();
		code.getField().add(new UniqueOrganizationCode(org.getId()));
		form.add(code);
		
		TextInputField<String> donorCode = new TextInputField<>("donorCode", new RWComponentPropertyModel<String>(
				"donorCode")).required().typeString();
		donorCode.getField().setEnabled(false);
		form.add(donorCode);

		form.add(new BootstrapSubmitButton("submit", new StringResourceModel("button.submit", this, null,(Object) null)) {
			private static final long serialVersionUID = 1748161296530970075L;

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

				organizationService.save(org);
				setResponsePage(ListOrganizationsPage.class);
			}

		});

		form.add(new BootstrapCancelButton("cancel", new StringResourceModel("button.cancel", this, null,(Object) null)) {

			private static final long serialVersionUID = 6700688063444818086L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				logger.info("Cancel pressed");
				setResponsePage(ListOrganizationsPage.class);
			}

		});

		add(form);

		feedbackPanel = new NotificationPanel("feedback");
		feedbackPanel.setOutputMarkupId(true);
		feedbackPanel.hideAfter(Duration.seconds(3));
		add(feedbackPanel);
	}

}
