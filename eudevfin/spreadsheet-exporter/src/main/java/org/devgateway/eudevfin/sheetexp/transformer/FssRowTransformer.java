/**
 * 
 */
package org.devgateway.eudevfin.sheetexp.transformer;

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

	@Override
	public MetadataRow transform(final EntityWrapperInterface src) throws SpreadsheetTransformationException {
		if ( src != null && src.getEntity() != null) {
			final FinancialTransaction tx	= (FinancialTransaction) src.getEntity();
			tx.setLocale(src.getLocale());
		}
		return super.transform(src);
	}


	private void createAllCellTransformers() {
		this.addCellTransformer( new CellTransformerImplementations.EXTRACTION_DATE_TRANS("ExtractionDate") );
		
		//this maps to FSS -> Disbursement year
		this.addCellTransformer( new CellTransformerImplementations.REPORTING_YEAR("DisbursementYear") );
				
		this.addCellTransformer( new CellTransformerImplementations.DONOR_CODE("DonorCode") ) ;
		
		this.addCellTransformer( new CellTransformerImplementations.DONOR("Donor") );
		
		// agency code
		this.addCellTransformer( new CellTransformerImplementations.AGENCY_CODE("AgencyCode") );
		this.addCellTransformer( new CellTransformerImplementations.CRSID("CRSID") );
		this.addCellTransformer( new CellTransformerImplementations.PROJECT_NUMBER("ProjectNumber") );
		//missing certainty code
		this.addCellTransformer( new CellTransformerImplementations.RECIPIENT_CODE("RecipientCode") );
		this.addCellTransformer( new CellTransformerImplementations.RECIPIENT("Recipient") );
		this.addCellTransformer( new CellTransformerImplementations.REGION("Region") );
		this.addCellTransformer( new CellTransformerImplementations.PRIORITY_CODE("Priority") );
		this.addCellTransformer( new CellTransformerImplementations.PHASE_OUT("PhaseOut") );
		this.addCellTransformer( new CellTransformerImplementations.CHANNEL("Channel") );
		this.addCellTransformer( new CellTransformerImplementations.CHANNEL_CODE("ChannelCode") );
		
		this.addCellTransformer( new CellTransformerImplementations.BI_MULTI_CODE("Bi_multi") );
		this.addCellTransformer( new CellTransformerImplementations.FLOW_CODE("FlowCode") );
		
		this.addCellTransformer( new CellTransformerImplementations.TYPE_OF_FINANCE_CODE("Finance_t") );
		this.addCellTransformer( new CellTransformerImplementations.TYPE_OF_AID_CODE("Aid_t") );
		this.addCellTransformer( new CellTransformerImplementations.SHORT_DESCRIPTION("ShortDescription") );
		//missing exclusion
		this.addCellTransformer( new CellTransformerImplementations.PURPOSE_CODE("PurposeCode") );
		this.addCellTransformer( new CellTransformerImplementations.MAIN_SECTOR("MainSector") );
		this.addCellTransformer( new CellTransformerImplementations.GEOGRAPHY("Geography") );
		this.addCellTransformer( new CellTransformerImplementations.EXPECTED_START_DATE("ExpectedStartDate") );
		this.addCellTransformer( new CellTransformerImplementations.COMPLETION_DATE("CompletionDate") );
		
		this.addCellTransformer( new CellTransformerImplementations.LONG_DESCRIPTION("LongDescription") );
		this.addCellTransformer( new CellTransformerImplementations.GENDER("Gender") );
		this.addCellTransformer( new CellTransformerImplementations.ENVIRONMENT("Environment") );
		this.addCellTransformer( new CellTransformerImplementations.PDGG("PDGG") );
		this.addCellTransformer( new CellTransformerImplementations.TRADE_DEVELOPMENT_CODE("TradeDevelopment") );
		this.addCellTransformer( new CellTransformerImplementations.FTC_1_0("FTC") );
		this.addCellTransformer( new CellTransformerImplementations.PBA_1_0("PBA") );
		this.addCellTransformer( new CellTransformerImplementations.INVESTMENT_PROJECT_1_0("InvestmentProject") );
		this.addCellTransformer( new CellTransformerImplementations.ASSOC_FINANCE_1_0("AssocFinance") );
		this.addCellTransformer( new CellTransformerImplementations.BIODIVERSITY_CODE("Biodiversity") );
		this.addCellTransformer( new CellTransformerImplementations.CLIMATE_MITIGATION_CODE("ClimateMitigation") );
		this.addCellTransformer( new CellTransformerImplementations.CLIMATE_ADAPTATION_CODE("ClimateAdaptation") );
		this.addCellTransformer( new CellTransformerImplementations.DESERTIFICATION_CODE("Desertification") );
		this.addCellTransformer( new CellTransformerImplementations.CURRENCY_CODE("CurrencyCode") );
		
		this.addCellTransformer( new CellTransformerImplementations.COMMITMENT_LC (this.exchangeRateUtil, "Commitment_LC") );
		this.addCellTransformer( new CellTransformerImplementations.AMOUNTS_EXTENDED (this.exchangeRateUtil, "Disbursement_LC") );
		this.addCellTransformer( new CellTransformerImplementations.RECEIVED(this.exchangeRateUtil, "Received") );
		this.addCellTransformer( new CellTransformerImplementations.AMOUNT_TIED(this.exchangeRateUtil, "AmountTied") );
		this.addCellTransformer( new CellTransformerImplementations.AMOUNT_PARTIALLY_UNTIED(this.exchangeRateUtil, "AmountPartiallyUntied") );
		this.addCellTransformer( new CellTransformerImplementations.AMOUNT_UNTIED (this.exchangeRateUtil, "AmountUntied") );
		this.addCellTransformer( new CellTransformerImplementations.IRTC (this.exchangeRateUtil, "IRTC") );
		this.addCellTransformer( new CellTransformerImplementations.EXPERT_COMMITMENT(this.exchangeRateUtil, "Expert_commitment") );
		this.addCellTransformer( new CellTransformerImplementations.EXPERT_EXTENDED(this.exchangeRateUtil, "Expert_extended") );
		this.addCellTransformer( new CellTransformerImplementations.EXPORT_CREDIT (this.exchangeRateUtil, "ExportCredit") );
		
		this.addCellTransformer( new CellTransformerImplementations.COMMITMENT_DATE("CommitmentDate") );
		this.addCellTransformer( new CellTransformerImplementations.TYPE_OF_REPAYMENT_CODE("TypeOfRepayment") );
		this.addCellTransformer( new CellTransformerImplementations.NUMBER_OF_REPAYMENT_CODE("NumberRepayment") );
		this.addCellTransformer( new CellTransformerImplementations.INTEREST1("Interest1") );
		this.addCellTransformer( new CellTransformerImplementations.INTEREST2("Interest2") );
		this.addCellTransformer( new CellTransformerImplementations.REPAY_DATE1("RepayDate1") );
		this.addCellTransformer( new CellTransformerImplementations.REPAY_DATE2("RepayDate2") );
		
		this.addCellTransformer( new CellTransformerImplementations.INTEREST_RECEIVED (this.exchangeRateUtil, "Interest") );
		this.addCellTransformer( new CellTransformerImplementations.OUTSTANDING (this.exchangeRateUtil, "Outstanding") );
		this.addCellTransformer( new CellTransformerImplementations.ARREARS_OF_PRINCIPAL (this.exchangeRateUtil, "Arrears_principal") );
		this.addCellTransformer( new CellTransformerImplementations.ARREARS_OF_INTEREST (this.exchangeRateUtil, "Arrears_interest") );
		this.addCellTransformer( new CellTransformerImplementations.FUTURE_DS_PRINCIPAL (this.exchangeRateUtil, "Future_DS_principal") );
		this.addCellTransformer( new CellTransformerImplementations.FUTURE_DS_INTEREST (this.exchangeRateUtil, "Future_DS_interest") );
		
		this.addCellTransformer( new CellTransformerImplementations.NOTES("Notes") );
		
		this.addCellTransformer( new CellTransformerImplementations.RMNCH_CODE("RMNCH") );
	}
	
	
}
