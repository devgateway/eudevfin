/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

/**
 * 
 */
package org.devgateway.eudevfin.sheetexp.ui;

import java.util.Calendar;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.financial.service.CustomFinancialTransactionService;
import org.devgateway.eudevfin.sheetexp.iati.util.IatiTransformationStarter;
import org.devgateway.eudevfin.sheetexp.integration.api.SpreadsheetTransformerService;
import org.devgateway.eudevfin.sheetexp.ui.model.Filter;
import org.devgateway.eudevfin.sheetexp.util.ITransformationStarter;
import org.devgateway.eudevfin.sheetexp.util.MetadataConstants;
import org.devgateway.eudevfin.sheetexp.util.TransformationStarter;
import org.devgateway.eudevfin.ui.common.components.DropDownField;
import org.devgateway.eudevfin.ui.common.pages.HeaderFooter;
import org.devgateway.eudevfin.ui.common.providers.YearProvider;
import org.wicketstuff.annotation.mount.MountPath;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;

/**
 * @author Alex
 * 
 */
@MountPath(value = "/spreadsheets")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public class ExportSpreadsheetsPage extends HeaderFooter {

	private static Logger logger = Logger.getLogger(ExportSpreadsheetsPage.class);
	@SpringBean
	private SpreadsheetTransformerService transformerService;

	@SpringBean
	private CustomFinancialTransactionService txService;

	public ExportSpreadsheetsPage(final PageParameters parameters) {
		super(parameters);
		
		this.generalInit(parameters.get(MetadataConstants.REPORT_TYPE_PARAM).toString(MetadataConstants.FSS_REPORT_TYPE));
	}



	public ExportSpreadsheetsPage() {
		super();

		this.generalInit(MetadataConstants.FSS_REPORT_TYPE);

	}



	private void generalInit(final String exportType) {
		final Filter filter = new Filter();
		filter.setYear(Calendar.getInstance().get(Calendar.YEAR) - 1);
		filter.setExportType (exportType);

		final Model<Filter> formModel	= Model.of(filter);
		final Form<Filter> form = new Form<Filter>("export-filter-form", formModel) {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@Override
			protected void onSubmit() {
                /**
                 * TODO: check {@link org.devgateway.eudevfin.ui.common.components.MultiFileUploadFormComponent}
                 */

                this.getTransformationStarter().prepareTransformation(this.getModelObject(), ExportSpreadsheetsPage.this.txService)
                        .executeTransformation((HttpServletResponse) this.getResponse().getContainerResponse(),
								ExportSpreadsheetsPage.this.transformerService);

                RequestCycle.get().scheduleRequestHandlerAfterCurrent(null);

			}
			
			private ITransformationStarter getTransformationStarter() {
				if ( MetadataConstants.IATI_REPORT_TYPE.equals(this.getModelObject().getExportType()) )  {
					return new IatiTransformationStarter();
				} else {
					return new TransformationStarter();
				}
			}
			

		};

		form.add(this.createYearField(formModel));

		form.add(this.createButton(exportType));

		this.add(form);
	}



	private DropDownField<Integer> createYearField(final Model<Filter> formModel) {
		final DropDownField<Integer> year = new DropDownField<Integer>("year-filter", new PropertyModel<Integer>(
				formModel, "year"), new YearProvider(this.txService.findDistinctReportingYears()));
		year.required();
		year.removeSpanFromControlGroup();
		if ( MetadataConstants.IATI_REPORT_TYPE.equals(formModel.getObject().getExportType()) ) {
			year.setVisible(false);
		}
		return year;
	}



	private Button createButton(final String exportType) {
		StringResourceModel stringResourceModel;
		if ( MetadataConstants.IATI_REPORT_TYPE.equals(exportType) ) {
			stringResourceModel	= new StringResourceModel("iati.filter-form.submit", this, null);
		}
		else if ( MetadataConstants.CRS_REPORT_TYPE.equals(exportType) ) {
			stringResourceModel	= new StringResourceModel("crs.filter-form.submit", this, null);
		}
		else{
			stringResourceModel	= new StringResourceModel("fss.filter-form.submit", this, null);
		}
		final BootstrapButton button = new BootstrapButton("filter-form-submit", stringResourceModel, Buttons.Type.Primary);
		button.setLabel(Model.of(""));
		return button;
	}
	
}
