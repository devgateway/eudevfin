package org.devgateway.eudevfin.sheetexp.util;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.financial.service.CustomFinancialTransactionService;
import org.devgateway.eudevfin.sheetexp.dto.EntityWrapperInterface;
import org.devgateway.eudevfin.sheetexp.dto.HeaderEntityWrapper;
import org.devgateway.eudevfin.sheetexp.integration.api.SpreadsheetTransformerService;
import org.devgateway.eudevfin.sheetexp.ui.model.Filter;
import org.joda.time.LocalDateTime;

public class TransformationStarter {
	
	List<EntityWrapperInterface<?>> finalList;

	public TransformationStarter prepareTransformation(final Filter filter, final CustomFinancialTransactionService txService) {
		final LocalDateTime now = LocalDateTime.now();
		final HeaderEntityWrapper<String> header = new HeaderEntityWrapper<String>(MetadataConstants.FSS_REPORT_TYPE,
				now, "en");
		
		this.finalList = new ArrayList<EntityWrapperInterface<?>>();
		this.finalList.add(header);
		
		final List<CustomFinancialTransaction> txList = txService.findByReportingYearAndDraftFalse(filter.getYear());
		new EntityWrapperListHelper<CustomFinancialTransaction>(txList, MetadataConstants.FSS_REPORT_TYPE, now, "en")
				.addToWrappedList(this.finalList);
		
		return this;
		
	}

	public void executeTransformation(final HttpServletResponse response, final SpreadsheetTransformerService transformerService) {
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "inline; filename=fss.xls");
			final OutputStream out				= response.getOutputStream();
			transformerService.createSpreadsheetOnStream(this.finalList, this.finalList.size(),
					new BufferedOutputStream(out) );
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
