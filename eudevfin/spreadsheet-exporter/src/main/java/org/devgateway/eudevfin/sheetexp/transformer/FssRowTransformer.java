/**
 * 
 */
package org.devgateway.eudevfin.sheetexp.transformer;

import java.util.List;

import javax.annotation.PostConstruct;

import org.devgateway.eudevfin.exchange.common.service.ExchangeRateUtil;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.sheetexp.dto.EntityWrapperInterface;
import org.devgateway.eudevfin.sheetexp.dto.MetadataRow;
import org.devgateway.eudevfin.sheetexp.exception.SpreadsheetTransformationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Alex
 *
 */
@Component
public class FssRowTransformer extends AbstractRowTransformer<EntityWrapperInterface> {
	
	@Autowired
	ExchangeRateUtil exchangeRateUtil;

	@PostConstruct
	private void init() {
		this.createAllCellTransformers();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void createHeader(final EntityWrapperInterface src, final MetadataRow row) {
		super.createHeader(src, row);
		final List<CellTransformerInterface<EntityWrapperInterface, ?>> cellTransformerList	= this.getCellTransformerList();
		for (final CellTransformerInterface cellTransformerInterface : cellTransformerList) {
			final AbstractFssCellTransformer<String> cellTransformer	= 
					(AbstractFssCellTransformer<String>) cellTransformerInterface;
			row.add( this.createHeaderCell(cellTransformer.getHeaderName()) );
		}
	}
	
	
	@Override
	public MetadataRow transform(final EntityWrapperInterface src) throws SpreadsheetTransformationException {
		if ( src != null && src.getEntity() != null) {
			final FinancialTransaction tx	= (FinancialTransaction) src.getEntity();
			tx.setLocale(src.getLocale());
		}
		return super.transform(src);
	}


	private void createAllCellTransformers() {
		this.addCellTransformer( CellTransformerImplementations.EXTRACTION_DATE_TRANS );
		
		this.addCellTransformer( CellTransformerImplementations.DISBURSEMENT_YEAR_TRANS );
				
		this.addCellTransformer( CellTransformerImplementations.DONOR_CODE ) ;
		
		this.addCellTransformer( CellTransformerImplementations.DONOR );
		
		this.addCellTransformer( CellTransformerImplementations.AGENCY_CODE );
		this.addCellTransformer( CellTransformerImplementations.CRSID );
		this.addCellTransformer( CellTransformerImplementations.PROJECT_NUMBER );
		//missing certainty code
		this.addCellTransformer( CellTransformerImplementations.RECIPIENT_CODE );
		this.addCellTransformer( CellTransformerImplementations.RECIPIENT );
		this.addCellTransformer( CellTransformerImplementations.REGION );
		this.addCellTransformer( CellTransformerImplementations.PRIORITY_CODE );
		this.addCellTransformer( CellTransformerImplementations.PHASE_OUT );
		this.addCellTransformer( CellTransformerImplementations.CHANNEL );
		this.addCellTransformer( CellTransformerImplementations.CHANNEL_CODE );
		
		this.addCellTransformer( CellTransformerImplementations.BI_MULTI );
		this.addCellTransformer( CellTransformerImplementations.FLOW_CODE );
		
		this.addCellTransformer( CellTransformerImplementations.FINANCE_T );
		this.addCellTransformer( CellTransformerImplementations.AID_T );
		this.addCellTransformer( CellTransformerImplementations.SHORT_DESCRIPTION );
		//missing exclusion
		this.addCellTransformer( CellTransformerImplementations.PURPOSE_CODE );
		this.addCellTransformer( CellTransformerImplementations.MAIN_SECTOR );
		this.addCellTransformer( CellTransformerImplementations.GEOGRAPHY );
		this.addCellTransformer( CellTransformerImplementations.EXPECTED_START_DATE );
		this.addCellTransformer( CellTransformerImplementations.COMPLETION_DATE );
		
		this.addCellTransformer( CellTransformerImplementations.LONG_DESCRIPTION );
		this.addCellTransformer( CellTransformerImplementations.GENDER );
		this.addCellTransformer( CellTransformerImplementations.ENVIRONMENT );
		this.addCellTransformer( CellTransformerImplementations.PDGG );
		this.addCellTransformer( CellTransformerImplementations.TRADE_DEVELOPMENT );
		this.addCellTransformer( CellTransformerImplementations.FTC );
		this.addCellTransformer( CellTransformerImplementations.PBA );
		this.addCellTransformer( CellTransformerImplementations.INVESTMENT_PROJECT );
		this.addCellTransformer( CellTransformerImplementations.ASSOC_FINANCE );
		this.addCellTransformer( CellTransformerImplementations.BIODIVERSITY );
		this.addCellTransformer( CellTransformerImplementations.CLIMATE_MITIGATION );
		this.addCellTransformer( CellTransformerImplementations.CLIMATE_ADAPTATION );
		this.addCellTransformer( CellTransformerImplementations.DESERTIFICATION );
		this.addCellTransformer( CellTransformerImplementations.CURRENCY_CODE );
		
		this.addCellTransformer( new CellTransformerImplementations.COMMITMENT_LC (this.exchangeRateUtil) );
		this.addCellTransformer( new CellTransformerImplementations.DISBURSEMENT_LC (this.exchangeRateUtil) );
		this.addCellTransformer( new CellTransformerImplementations.RECEIVED(this.exchangeRateUtil) );
		this.addCellTransformer( new CellTransformerImplementations.AMOUNT_TIED(this.exchangeRateUtil) );
		this.addCellTransformer( new CellTransformerImplementations.AMOUNT_PARTIALLY_UNTIED(this.exchangeRateUtil) );
		this.addCellTransformer( new CellTransformerImplementations.AMOUNT_UNTIED (this.exchangeRateUtil) );
		this.addCellTransformer( new CellTransformerImplementations.IRTC (this.exchangeRateUtil) );
		this.addCellTransformer( new CellTransformerImplementations.EXPERT_COMMITMENT(this.exchangeRateUtil) );
		this.addCellTransformer( new CellTransformerImplementations.EXPERT_EXTENDED(this.exchangeRateUtil) );
		this.addCellTransformer( new CellTransformerImplementations.EXPORT_CREDIT (this.exchangeRateUtil) );
		
		this.addCellTransformer( CellTransformerImplementations.COMMITMENT_DATE );
		this.addCellTransformer( CellTransformerImplementations.TYPE_OF_REPAYMENT );
		this.addCellTransformer( CellTransformerImplementations.NUMBER_REPAYMENT );
		this.addCellTransformer( CellTransformerImplementations.INTEREST1 );
		this.addCellTransformer( CellTransformerImplementations.INTEREST2 );
		this.addCellTransformer( CellTransformerImplementations.REPAY_DATE1 );
		this.addCellTransformer( CellTransformerImplementations.REPAY_DATE2 );
		
		this.addCellTransformer( new CellTransformerImplementations.INTEREST_RECEIVED (this.exchangeRateUtil) );
		this.addCellTransformer( new CellTransformerImplementations.OUTSTANDING (this.exchangeRateUtil) );
		this.addCellTransformer( new CellTransformerImplementations.ARREARS_OF_PRINCIPAL (this.exchangeRateUtil) );
		this.addCellTransformer( new CellTransformerImplementations.ARREARS_OF_INTEREST (this.exchangeRateUtil) );
		this.addCellTransformer( new CellTransformerImplementations.FUTURE_DS_PRINCIPAL (this.exchangeRateUtil) );
		this.addCellTransformer( new CellTransformerImplementations.FUTURE_DS_INTEREST (this.exchangeRateUtil) );
		
		this.addCellTransformer( CellTransformerImplementations.NOTES );
	}
	
	
}
