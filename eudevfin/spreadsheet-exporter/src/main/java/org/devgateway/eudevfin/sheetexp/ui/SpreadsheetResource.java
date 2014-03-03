package org.devgateway.eudevfin.sheetexp.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.request.Response;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.AbstractResource;
import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.financial.service.CustomFinancialTransactionService;
import org.devgateway.eudevfin.sheetexp.dto.EntityWrapperInterface;
import org.devgateway.eudevfin.sheetexp.dto.HeaderEntityWrapper;
import org.devgateway.eudevfin.sheetexp.integration.api.SpreadsheetTransformerService;
import org.devgateway.eudevfin.sheetexp.util.EntityWrapperListHelper;
import org.devgateway.eudevfin.sheetexp.util.MetadataConstants;
import org.joda.time.LocalDateTime;

public class SpreadsheetResource extends AbstractResource {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1204610887693645674L;
	private final SpreadsheetTransformerService transformerService;
	private final CustomFinancialTransactionService txService;
	private final Integer year;

	public SpreadsheetResource(final Integer year, final SpreadsheetTransformerService transformerService,
			final CustomFinancialTransactionService txService) {
		super();
		this.transformerService = transformerService;
		this.txService 	= txService;
		this.year		= year; 
	}

	@Override
	public ResourceResponse newResourceResponse(final Attributes attributes) {
		//final PageParameters pageParameters = attributes.getParameters();

		final ResourceResponse resourceResponse = new ResourceResponse();

		resourceResponse.setContentType("application/vnd.ms-excel");
		resourceResponse.setFileName("fss.xls");

		// if (resourceResponse.dataNeedsToBeWritten(attributes)){

		resourceResponse.setWriteCallback(new WriteCallback() {

			@Override
			public void writeData(final Attributes attributes) throws IOException {
				final Response response = attributes.getResponse();

				/* TEST DATA */
				// final List<FinancialTransaction> txList = new ArrayList<FinancialTransaction>();
				// final FinancialTransaction tx1 = new FinancialTransaction();
				// final FinancialTransaction tx2 = new FinancialTransaction();
				//
				// txList.add(tx1);
				// txList.add(tx2);
				//
				// final LocalDateTime now = LocalDateTime.now();
				// final HeaderEntityWrapper<String> header = new HeaderEntityWrapper<String>(MetadataConstants.FSS_REPORT_TYPE, now);
				//
				// final List<EntityWrapperInterface<?>> finalList = new ArrayList<EntityWrapperInterface<?>>();
				// finalList.add(header);
				// new EntityWrapperListHelper<FinancialTransaction>(txList, MetadataConstants.FSS_REPORT_TYPE,
				// now).addToWrappedList(finalList);

				final LocalDateTime now = LocalDateTime.now();
				final HeaderEntityWrapper<String> header = new HeaderEntityWrapper<String>(MetadataConstants.FSS_REPORT_TYPE,
						now, "en");

				final List<EntityWrapperInterface<?>> finalList = new ArrayList<EntityWrapperInterface<?>>();
				finalList.add(header);

				final List<CustomFinancialTransaction> txList = SpreadsheetResource.this.txService
						.findByReportingYearAndDraftFalse(SpreadsheetResource.this.year);
				new EntityWrapperListHelper<CustomFinancialTransaction>(txList, MetadataConstants.FSS_REPORT_TYPE, now, "en")
						.addToWrappedList(finalList);

				SpreadsheetResource.this.transformerService.createSpreadsheetOnStream(finalList, finalList.size(),
						response.getOutputStream());

			}

		});
		return resourceResponse;
	}

}
