package org.devgateway.eudevfin.sheetexp.util;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.financial.service.CustomFinancialTransactionService;
import org.devgateway.eudevfin.sheetexp.dto.EntityWrapperInterface;
import org.devgateway.eudevfin.sheetexp.dto.HeaderEntityWrapper;
import org.devgateway.eudevfin.sheetexp.integration.api.SpreadsheetTransformerService;
import org.devgateway.eudevfin.sheetexp.ui.model.Filter;
import org.devgateway.eudevfin.ui.common.temporary.SB;
import org.joda.time.LocalDateTime;

public class TransformationStarter {
	
	private List<EntityWrapperInterface<?>> finalList;
	private Filter filter;

	public TransformationStarter prepareTransformation(final Filter filter, final CustomFinancialTransactionService txService) {
		final LocalDateTime now = LocalDateTime.now();
		final HeaderEntityWrapper<String> header = new HeaderEntityWrapper<String>(filter.getExportType(),
				now, "en");
		
		this.finalList = new ArrayList<EntityWrapperInterface<?>>();
		this.finalList.add(header);
		
		final List<CustomFinancialTransaction> txList = txService.findByReportingYearAndDraftFalseAndFormTypeNotIn(
				filter.getYear(),
				Arrays.asList(new String[] { SB.BILATERAL_ODA_ADVANCE_QUESTIONNAIRE,
						SB.MULTILATERAL_ODA_ADVANCE_QUESTIONNAIRE }));
		new EntityWrapperListHelper<CustomFinancialTransaction>(txList, filter.getExportType(), now, "en")
				.addToWrappedList(this.finalList);
		
		this.filter	= filter;
		return this;
		
	}

	public void executeTransformation(final HttpServletResponse response, final SpreadsheetTransformerService transformerService) {
		try {
			final String filename	= this.filter.getExportType() + "-export.xls";
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "inline; filename=" + filename);
			final OutputStream out				= response.getOutputStream();
			transformerService.createSpreadsheetOnStream(this.finalList, this.finalList.size(),
					new BufferedOutputStream(out) );
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
