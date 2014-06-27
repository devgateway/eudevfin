/**
 * 
 */
package org.devgateway.eudevfin.dim.pages;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.dim.pages.transaction.crs.TransactionPage;
import org.devgateway.eudevfin.dim.pages.transaction.custom.CustomTransactionPage;
import org.devgateway.eudevfin.ui.common.Constants;
import org.devgateway.eudevfin.ui.common.LocalComponentDetachableModel;
import org.devgateway.eudevfin.ui.common.components.BootstrapCancelButton;
import org.devgateway.eudevfin.ui.common.components.BootstrapSubmitButton;
import org.devgateway.eudevfin.ui.common.components.DropDownField;
import org.devgateway.eudevfin.ui.common.pages.HeaderFooter;
import org.devgateway.eudevfin.ui.common.providers.FormTypeProvider;
import org.wicketstuff.annotation.mount.MountPath;

import de.agilecoders.wicket.core.markup.html.bootstrap.form.InputBehavior;

/**
 * @author mihai
 * 
 */
@MountPath(value = "/reuse")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public class ReuseTransactionPage extends HeaderFooter<String> {

	private static final long serialVersionUID = 817361503195961105L;
	private BootstrapSubmitButton submitButton;

	public ReuseTransactionPage(final PageParameters parameters) {
		super(parameters);

		if (parameters.get(TransactionPage.PARAM_TRANSACTION_ID).isNull())
			setResponsePage(AggregateTransactionsPage.class);

		Form<?> form = new Form<>("form");

		final LocalComponentDetachableModel<String> formTypeModel = new LocalComponentDetachableModel<String>();

		final DropDownField<String> formType = new DropDownField<String>("formType", formTypeModel,
				new FormTypeProvider(this)) {
					private static final long serialVersionUID = -3340020821883808807L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				super.onUpdate(target);
				if(this.getDefaultModelObject()!=null) 
					submitButton.setEnabled(true);
				else 
					submitButton.setEnabled(false);
				target.add(submitButton);
			}
		};
		formType.setSize(InputBehavior.Size.Large);
		formType.hideLabel();
		formType.removeSpanFromControlGroup();
		form.add(formType);

		submitButton = new BootstrapSubmitButton("submit", new StringResourceModel("submit",
				this, null)) {

			private static final long serialVersionUID = -1259986211925465982L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				PageParameters pageParameters = new PageParameters();
				pageParameters.add(Constants.PARAM_TRANSACTION_TYPE,formType.getDefaultModelObject());
				pageParameters.add(TransactionPage.PARAM_TRANSACTION_ID,
						parameters.get(TransactionPage.PARAM_TRANSACTION_ID));
				pageParameters.add(TransactionPage.PARAM_REUSE, "");
				setResponsePage(CustomTransactionPage.class, pageParameters);

			}

		};
		submitButton.setEnabled(false);
		form.add(submitButton);

		BootstrapCancelButton cancelButton = new BootstrapCancelButton("cancel", new StringResourceModel("cancel", this,
				null)) {

			private static final long serialVersionUID = 1949821858073737294L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				setResponsePage(AggregateTransactionsPage.class);
			}

		};
		form.add(cancelButton);

		this.add(form);

	}

}
