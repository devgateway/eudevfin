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

import com.vaynberg.wicket.select2.ChoiceProvider;
import com.vaynberg.wicket.select2.Response;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
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
import org.devgateway.eudevfin.sheetexp.integration.api.SpreadsheetTransformerService;
import org.devgateway.eudevfin.sheetexp.ui.model.Filter;
import org.devgateway.eudevfin.sheetexp.util.MetadataConstants;
import org.devgateway.eudevfin.sheetexp.util.TransformationStarter;
import org.devgateway.eudevfin.ui.common.components.DropDownField;
import org.devgateway.eudevfin.ui.common.pages.HeaderFooter;
import org.json.JSONException;
import org.json.JSONWriter;
import org.wicketstuff.annotation.mount.MountPath;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

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

	private List<Integer> possibleYears;
	
	

	public ExportSpreadsheetsPage(final PageParameters parameters) {
		super(parameters);
		
		this.generalInit(parameters.get(MetadataConstants.REPORT_TYPE_PARAM).toString(MetadataConstants.FSS_REPORT_TYPE));
	}



	public ExportSpreadsheetsPage() {
		super();

		this.generalInit(MetadataConstants.FSS_REPORT_TYPE);

	}



	private void generalInit(final String exportType) {
		this.possibleYears = this.txService.findDistinctReportingYears();
		Collections.sort(this.possibleYears);

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

                new TransformationStarter().prepareTransformation(this.getModelObject(), ExportSpreadsheetsPage.this.txService)
                        .executeTransformation((HttpServletResponse) this.getResponse().getContainerResponse(),
								ExportSpreadsheetsPage.this.transformerService);

                RequestCycle.get().scheduleRequestHandlerAfterCurrent(null);

			}

		};

		form.add(this.createYearField(formModel));

		form.add(this.createButton(exportType));

		this.add(form);
	}



	private DropDownField<Integer> createYearField(final Model<Filter> formModel) {
		final DropDownField<Integer> year = new DropDownField<Integer>("year-filter", new PropertyModel<Integer>(
				formModel, "year"), new ChoiceProvider<Integer>() {

			@Override
			public void query(final String term, final int page, final Response<Integer> response) {
				final List<Integer> ret = new ArrayList<Integer>();
				List<Integer> values = null;
				if (ExportSpreadsheetsPage.this.possibleYears != null && ExportSpreadsheetsPage.this.possibleYears.size() > 0) {
					values = ExportSpreadsheetsPage.this.possibleYears;
				} else {
					values = new ArrayList<Integer>(1);
					values.add(Calendar.getInstance().get(Calendar.YEAR) - 1);
				}
				for (final Integer el : values) {
					if (el.toString().startsWith(term)) {
						ret.add(el);
					}
				}
				response.addAll(ret);

			}

			@Override
			public void toJson(final Integer choice, final JSONWriter writer) throws JSONException {
				writer.key("id").value(choice.toString()).key("text").value(choice.toString());

			}

			@Override
			public Collection<Integer> toChoices(final Collection<String> ids) {
				final List<Integer> ret = new ArrayList<Integer>();
				if (ids != null) {
					for (final String id : ids) {
						try {
							final Integer parsedInt = Integer.parseInt(id);
							ret.add(parsedInt);
						} catch (final NumberFormatException e) {
							logger.error(e.getMessage());
						}
					}
				}
				return ret;
			}

		});
		year.required();
		year.removeSpanFromControlGroup();
		return year;
	}



	private Button createButton(final String exportType) {
		StringResourceModel stringResourceModel;
		if ( MetadataConstants.CRS_REPORT_TYPE.equals(exportType) ) {
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
