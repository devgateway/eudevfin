/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/

package org.devgateway.eudevfin.dim.pages.transaction.crs;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.devgateway.eudevfin.financial.service.FinancialTransactionService;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.devgateway.eudevfin.metadata.common.util.CategoryConstants;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;
import org.devgateway.eudevfin.ui.common.components.DateInputField;
import org.devgateway.eudevfin.ui.common.components.DropDownField;
import org.devgateway.eudevfin.ui.common.components.PreviewableFormPanel;
import org.devgateway.eudevfin.ui.common.components.TextInputField;
import org.devgateway.eudevfin.ui.common.events.ReportingYearChangedEventPayload;
import org.devgateway.eudevfin.ui.common.models.DateToLocalDateTimeModel;
import org.devgateway.eudevfin.ui.common.models.YearToLocalDateTimeModel;
import org.devgateway.eudevfin.ui.common.permissions.PermissionAwareComponent;
import org.devgateway.eudevfin.ui.common.providers.CategoryProviderFactory;
import org.devgateway.eudevfin.ui.common.providers.OrganizationChoiceProvider;
import org.devgateway.eudevfin.ui.common.validators.Field4CrsIdCodeValidator;
import org.joda.time.LocalDateTime;

/**
 * @author aartimon@developmentgateway.org
 * @since 01 November 2013
 */
public class IdentificationDataTab extends PreviewableFormPanel implements PermissionAwareComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = -368263194801751715L;
	protected PageParameters parameters;
    public static final String KEY = "tabs.identification";

    private Long transactionId;

    @SpringBean
    private OrganizationChoiceProvider organizationProvider;
    
    @SpringBean
    private CategoryProviderFactory categoryFactory;

    @SpringBean
    private FinancialTransactionService financialTransactionService;

	public static final String VALIDATIONKEY_CRS_DUPLICATE = "validation.crsDuplicate";
	private TextInputField<Integer> crsId;


    public IdentificationDataTab(String id,PageParameters parameters) {
        super(id);
        this.parameters=parameters;

        // get the transactionId parameter - we will use it in crsID validation
        if (parameters.get(TransactionPage.PARAM_TRANSACTION_ID) != null &&
                !parameters.get(TransactionPage.PARAM_TRANSACTION_ID).equals(StringValue.valueOf((String) null))) {
            transactionId = parameters.get(TransactionPage.PARAM_TRANSACTION_ID).toLong();
        } else {
            transactionId = null;
        }

        addComponents();
    }

	private void addComponents() {
		TextInputField<Integer> reportingYear = new TextInputField<Integer>("1reportingYear",
				new YearToLocalDateTimeModel(new RWComponentPropertyModel<LocalDateTime>("reportingYear"))) {
			private static final long serialVersionUID = 1390304553363728058L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				Integer modelObject = this.getField().getModelObject();
				if (modelObject != null)
					send(getPage(), Broadcast.DEPTH, new ReportingYearChangedEventPayload(target, modelObject));
			}
		};

        reportingYear.typeInteger().required().range(1900, 2099).decorateMask("9999");
        add(reportingYear);

        DateInputField commitmentDate = new DateInputField("1bCommitmentDate", new DateToLocalDateTimeModel(new RWComponentPropertyModel<LocalDateTime>("commitmentDate")));
        add(commitmentDate);

        DropDownField<Organization> extendingAgency = new DropDownField<>("3extendingAgency",
                new RWComponentPropertyModel<Organization>("extendingAgency"), organizationProvider);
        extendingAgency.required();

        //see TransactionPage#initializeFinancialTransaction
        extendingAgency.getField().setEnabled(false);

        add(extendingAgency);

    
        TextInputField<String> donorProjectNumber = new TextInputField<>("5donorProjectNumber",
                new RWComponentPropertyModel<String>("donorProjectNumber"));
        donorProjectNumber.typeString();
        add(donorProjectNumber);

        DropDownField<Category> natureOfSubmission = new DropDownField<Category>("6natureSubmission",
                new RWComponentPropertyModel<Category>("natureOfSubmission"), categoryFactory.get(CategoryConstants.NATURE_OF_SUBMISSION_TAG)) {
        	@Override
        	protected void onUpdate(AjaxRequestTarget target) {
				crsId.getField().clearInput();
				target.add(crsId);	
        	}
        };
        natureOfSubmission.required();
        add(natureOfSubmission);
    
        crsId = new TextInputField<Integer>("4crsId", new RWComponentPropertyModel<Integer>("crsIdentificationNumber"));
        crsId.getField().add(new Field4CrsIdCodeValidator(financialTransactionService, transactionId, natureOfSubmission) {
        	@Override
        	protected ValidationError decorate(ValidationError error, IValidatable<Integer> validatable) {
				error.addKey(VALIDATIONKEY_CRS_DUPLICATE);
				return error;
        	}
        });
        crsId.typeInteger();
        add(crsId);

        
	}

    @Override
    public String getPermissionKey() {
        return KEY;
    }

    @Override
    public void enableRequired() {
    }


}
