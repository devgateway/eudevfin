/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.sheetexp.integration;

import org.devgateway.eudevfin.sheetexp.dto.EntityWrapperInterface;
import org.devgateway.eudevfin.sheetexp.dto.MetadataRow;
import org.devgateway.eudevfin.sheetexp.transformer.CrsRowTransformer;
import org.devgateway.eudevfin.sheetexp.transformer.DummyRowTransformer;
import org.devgateway.eudevfin.sheetexp.transformer.FssRowTransformer;
import org.devgateway.eudevfin.sheetexp.transformer.RowTransformerInterface;
import org.devgateway.eudevfin.sheetexp.util.MetadataConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Transformer {
	
//	private RowTransformerInterface<FinancialTransaction> rowTransformer;
	@Autowired
	private DummyRowTransformer dummyRowTransformer;
	@Autowired
	private FssRowTransformer fssRowTransformer;
	@Autowired
	private CrsRowTransformer crsRowTransformer;
	
	@org.springframework.integration.annotation.Transformer
	public MetadataRow transform(final EntityWrapperInterface<?> entityWrapper) {
		final RowTransformerInterface	rowTransformer	= this.getRowTransformer(entityWrapper.getReportType());
		
		final MetadataRow row		= rowTransformer.transform(entityWrapper);
		return row;
	}

	private RowTransformerInterface getRowTransformer(final String reportType) {
		RowTransformerInterface ret	= this.dummyRowTransformer;
		if ( MetadataConstants.FSS_REPORT_TYPE.equals(reportType) ) {
			ret	= this.fssRowTransformer;
		}
		else if ( MetadataConstants.CRS_REPORT_TYPE.equals(reportType) ){
			ret	= this.crsRowTransformer;
		}
		return ret;
	}

//	public RowTransformerInterface<FinancialTransaction> getRowTransformer() {
//		return this.rowTransformer;
//	}
//
//	public void setRowTransformer(
//			final RowTransformerInterface<FinancialTransaction> rowTransformer) {
//		this.rowTransformer = rowTransformer;
//	}
//	
	
}
