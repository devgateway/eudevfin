package org.devgateway.eudevfin.sheetexp.integration;

import org.devgateway.eudevfin.sheetexp.dto.EntityWrapperInterface;
import org.devgateway.eudevfin.sheetexp.dto.MetadataRow;
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
