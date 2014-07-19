/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.sheetexp.util;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.financial.service.CustomFinancialTransactionService;
import org.devgateway.eudevfin.sheetexp.dto.EntityWrapperInterface;
import org.devgateway.eudevfin.sheetexp.dto.HeaderEntityWrapper;
import org.devgateway.eudevfin.sheetexp.integration.api.SpreadsheetTransformerService;
import org.devgateway.eudevfin.sheetexp.ui.model.Filter;
import org.devgateway.eudevfin.ui.common.temporary.SB;
import org.joda.time.LocalDateTime;

public class TransformationStarter implements ITransformationStarter {
	
	private static Logger logger = Logger.getLogger(TransformationStarter.class);
	
	private List<EntityWrapperInterface<?>> finalList;
	private Filter filter;

	/* (non-Javadoc)
	 * @see org.devgateway.eudevfin.sheetexp.util.ITransformationStarter#prepareTransformation(org.devgateway.eudevfin.sheetexp.ui.model.Filter, org.devgateway.eudevfin.financial.service.CustomFinancialTransactionService)
	 */
	@Override
	public ITransformationStarter prepareTransformation(final Filter filter, final CustomFinancialTransactionService txService) {
		final LocalDateTime now = LocalDateTime.now();
		final HeaderEntityWrapper<String> header = new HeaderEntityWrapper<String>(filter.getExportType(),
				now, "en");
		
		this.filter	= filter;
		this.finalList = new ArrayList<EntityWrapperInterface<?>>();
		this.finalList.add(header);
		
		final List<CustomFinancialTransaction> txList = txService.findByReportingYearAndApprovedTrueAndFormTypeIn(
				filter.getYear(), this.getAllowedFormTypes() );
		new EntityWrapperListHelper<CustomFinancialTransaction>(txList, filter.getExportType(), now, "en")
				.addToWrappedList(this.finalList);
		
		return this;
		
	}

	private Collection<String> getAllowedFormTypes() {
		Collection<String> retCol	= null;
		if ( MetadataConstants.CRS_REPORT_TYPE.equals(this.filter.getExportType()) ) {
			retCol	= Arrays.asList(new String[] { SB.BILATERAL_ODA_CRS, SB.MULTILATERAL_ODA_CRS,
					SB.NON_ODA_OOF_EXPORT, SB.NON_ODA_OOF_NON_EXPORT, SB.NON_ODA_OTHER_FLOWS,
					SB.NON_ODA_PRIVATE_GRANTS, SB.NON_ODA_PRIVATE_MARKET});
		} else if (MetadataConstants.FSS_REPORT_TYPE.equals(this.filter.getExportType())) {
			retCol	= Arrays.asList( new String[]{SB.BILATERAL_ODA_FORWARD_SPENDING} );
		}
		else {
			logger.error("Export type needs to be either CRS or FSS");
		}
		return retCol;
	}

	/* (non-Javadoc)
	 * @see org.devgateway.eudevfin.sheetexp.util.ITransformationStarter#executeTransformation(javax.servlet.http.HttpServletResponse, org.devgateway.eudevfin.sheetexp.integration.api.SpreadsheetTransformerService)
	 */
	@Override
	public void executeTransformation(final HttpServletResponse response, final SpreadsheetTransformerService transformerService) {
		try {
			final String filename	= this.filter.getExportType() + "-export.xls";
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "inline; filename=" + filename);
			final OutputStream out				= response.getOutputStream();
			transformerService.createSpreadsheetOnStream(this.finalList, this.finalList.size(),
					new BufferedOutputStream(out), this.filter.getExportType().toUpperCase() );
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
