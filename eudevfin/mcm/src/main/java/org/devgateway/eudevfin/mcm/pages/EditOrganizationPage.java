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
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Args;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.util.time.Duration;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.auth.common.util.AuthUtils;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.devgateway.eudevfin.metadata.common.service.OrganizationService;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;
import org.devgateway.eudevfin.ui.common.components.BootstrapCancelButton;
import org.devgateway.eudevfin.ui.common.components.BootstrapDeleteButton;
import org.devgateway.eudevfin.ui.common.components.BootstrapSubmitButton;
import org.devgateway.eudevfin.ui.common.components.TextInputField;
import org.devgateway.eudevfin.ui.common.components.util.MondrianCDACacheUtil;
import org.devgateway.eudevfin.ui.common.pages.HeaderFooter;
import org.wicketstuff.annotation.mount.MountPath;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationMessage;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;

/**
 * @author mihai
 */
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_SUPERVISOR)
@MountPath(value = "/org")
public class EditOrganizationPage extends HeaderFooter<Organization> {

	@SpringBean
	private OrganizationService organizationService;
	
    @SpringBean
    MondrianCDACacheUtil mondrianCacheUtil;

	protected final NotificationPanel feedbackPanel;

	private static final long serialVersionUID = -4276784345759050002L;
	private static final Logger logger = Logger.getLogger(EditOrganizationPage.class);
	public static final String PARAM_ORG_ID = "orgId";
	public static final String PARAM_ORG_ID_VALUE_NEW = "new";

	public class UniqueOrganizationCodeAndDonorCodeValidator extends AbstractFormValidator {

		private static final long serialVersionUID = 4021513760568758277L;
		private Long id;
		private final FormComponent<?>[] components;

		public UniqueOrganizationCodeAndDonorCodeValidator(FormComponent<?> code, FormComponent<?> donorCode, Long id) {
			Args.notNull(code, "Code");
			Args.notNull(donorCode, "Donor Code");
			components = new FormComponent[] { code, donorCode };
			this.id = id;
		}

		@Override
		public FormComponent<?>[] getDependentFormComponents() {
			return components;
		}

		@Override
		public void validate(Form<?> form) {
			final FormComponent<?> codeComponent = components[0];
			final FormComponent<?> donorCodeComponent = components[1];

			Organization organizationForCurrentUser = AuthUtils.getOrganizationForCurrentUser();
			
			String code = codeComponent.getInput();
			String donorCode = donorCodeComponent.getInput();

			Organization org2 = organizationService.findByCodeAndDonorCode(code, organizationForCurrentUser.getDonorCode()).getEntity();
			if (org2 != null && !org2.getId().equals(id)) {
				form.error(new StringResourceModel("uniqueOrgCodeError", EditOrganizationPage.this, null, null).getString());
			}

		}
	}

	public EditOrganizationPage(final PageParameters parameters) {
		super(parameters);

		final Organization org;

		if (Strings.isEqual(parameters.get(PARAM_ORG_ID).toString(), PARAM_ORG_ID_VALUE_NEW)) {
			org = new Organization();
			org.setDac(false);
			Organization organizationForCurrentUser = AuthUtils.getOrganizationForCurrentUser();
			org.setDonorCode(organizationForCurrentUser.getDonorCode());
			org.setDonorName(organizationForCurrentUser.getDonorName());

		} else {
			long orgId = parameters.get(PARAM_ORG_ID).toLong();
			org = organizationService.findOne(orgId).getEntity();
		}

		Form<Organization> form = new Form<Organization>("form");

		CompoundPropertyModel<Organization> model = new CompoundPropertyModel<>(org);
		setModel(model);

		TextInputField<String> name = new TextInputField<>("name", new RWComponentPropertyModel<String>("name"))
				.required().typeString();
		form.add(name);

		TextInputField<String> acronym = new TextInputField<>("acronym",
				new RWComponentPropertyModel<String>("acronym")).required().typeString();
		form.add(acronym);

		TextInputField<String> code = new TextInputField<>("code", new RWComponentPropertyModel<String>("code"))
				.required().typeString();
		form.add(code);

		TextInputField<String> donorName = new TextInputField<>("donorName", new RWComponentPropertyModel<String>(
				"donorName")).required().typeString();
		donorName.getField().setEnabled(false);
		form.add(donorName);

		form.add(new BootstrapSubmitButton("submit",
				new StringResourceModel("button.submit", this, null, (Object) null)) {
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
                mondrianCacheUtil.flushMondrianCDACache();
				setResponsePage(ListOrganizationsPage.class);
			}

		});
		
		form.add(new BootstrapDeleteButton("delete", new StringResourceModel("button.delete", this, null)) {
			private static final long serialVersionUID = 6700688063444818086L;

			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				 Organization org = (Organization) form.getInnermostModel().getObject();
		            logger.debug("Object:" + org);
		            logger.info("Deleting!");
		            target.add(feedbackPanel);
		            try {
		                if (org.getId() == null) return;
		                organizationService.delete(org);
		                info(new NotificationMessage(new StringResourceModel("notification.deleted", EditOrganizationPage.this, null)));
		                mondrianCacheUtil.flushMondrianCDACache();
		            } catch (Exception e) {
		                logger.error("Exception while trying to delete:", e);
		                error(new NotificationMessage(new StringResourceModel("notification.cannotdelete", EditOrganizationPage.this, null)));
		                return;
		            }
		            setResponsePage(ListOrganizationsPage.class);
		            logger.info("Deleted ok!");
			};
		});

		form.add(new BootstrapCancelButton("cancel",
				new StringResourceModel("button.cancel", this, null, (Object) null)) {

			private static final long serialVersionUID = 6700688063444818086L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				logger.info("Cancel pressed");
				setResponsePage(ListOrganizationsPage.class);
			}

		});

		form.add(new UniqueOrganizationCodeAndDonorCodeValidator(code.getField(), donorName.getField(), org.getId()));

		add(form);

		feedbackPanel = new NotificationPanel("feedback");
		feedbackPanel.setOutputMarkupId(true);
		feedbackPanel.hideAfter(Duration.seconds(4));
		add(feedbackPanel);
	}

}
