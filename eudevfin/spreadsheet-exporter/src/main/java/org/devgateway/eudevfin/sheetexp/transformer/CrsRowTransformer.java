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
public class CrsRowTransformer extends AbstractRowTransformer<EntityWrapperInterface> {
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
		this.addCellTransformer( new CellTransformerImplementations.REPORTING_YEAR("Reporting Year") );
		this.addCellTransformer( new CellTransformerImplementations.COMMITMENT_DATE("Commitment Date") );
		this.addCellTransformer( new CellTransformerImplementations.DONOR("Reporting country / organisation") );
		this.addCellTransformer( new CellTransformerImplementations.AGENCY_NAME("Extending agency") );
		this.addCellTransformer( new CellTransformerImplementations.CRSID("CRS Identification Num") );
		this.addCellTransformer( new CellTransformerImplementations.PROJECT_NUMBER("Donor project Num") );
		this.addCellTransformer( new CellTransformerImplementations.NATURE_OF_SUBMISSION("Nature of submission") );
		this.addCellTransformer( new CellTransformerImplementations.RECIPIENT("Recipient") );
		this.addCellTransformer( new CellTransformerImplementations.CHANNEL("Channel") );
		this.addCellTransformer( new CellTransformerImplementations.CHANNEL_CODE("ChannelCode") );
		this.addCellTransformer( new CellTransformerImplementations.BI_MULTI_CODE("Bi_multi") );
		
		this.addCellTransformer( new CellTransformerImplementations.FLOW_CODE("Type of flow") );
		
		this.addCellTransformer( new CellTransformerImplementations.TYPE_OF_FINANCE_CODE("Type of finance") );
		this.addCellTransformer( new CellTransformerImplementations.TYPE_OF_AID_CODE("Type of aid") );
		this.addCellTransformer( new CellTransformerImplementations.SHORT_DESCRIPTION("Short description / Project title") );
		this.addCellTransformer( new CellTransformerImplementations.PURPOSE_CODE("Sector / Purpose code") );
		this.addCellTransformer( new CellTransformerImplementations.REGION("Geographical target area") );
		this.addCellTransformer( new CellTransformerImplementations.EXPECTED_START_DATE("ExpectedStartDate") );
		this.addCellTransformer( new CellTransformerImplementations.COMPLETION_DATE("CompletionDate") );
		this.addCellTransformer( new CellTransformerImplementations.LONG_DESCRIPTION("Description") );
		this.addCellTransformer( new CellTransformerImplementations.AID_TO_ENVIRONMENT_CODE("Aid to environment") );
		this.addCellTransformer( new CellTransformerImplementations.PDGG("PD/GG") );
		this.addCellTransformer( new CellTransformerImplementations.TRADE_DEVELOPMENT_CODE("Trade Development") );
		this.addCellTransformer( new CellTransformerImplementations.FTC_1_0("FTC") );
		this.addCellTransformer( new CellTransformerImplementations.PBA_1_0("PBA") );
		this.addCellTransformer( new CellTransformerImplementations.INVESTMENT_PROJECT_1_0("InvestmentProject") );
		this.addCellTransformer( new CellTransformerImplementations.ASSOC_FINANCE_1_0("AssocFinance") );
		this.addCellTransformer( new CellTransformerImplementations.BIODIVERSITY_CODE("Biodiversity") );
		this.addCellTransformer( new CellTransformerImplementations.CLIMATE_MITIGATION_CODE("Climate change - mitigation") );
		this.addCellTransformer( new CellTransformerImplementations.CLIMATE_ADAPTATION_CODE("Climate change - adaptation") );
		
		this.addCellTransformer( new CellTransformerImplementations.DESERTIFICATION_CODE("Desertification") );
		this.addCellTransformer( new CellTransformerImplementations.CURRENCY_CODE("Currency Code") );
		
		this.addCellTransformer( new CellTransformerImplementations.COMMITMENT_LC (this.exchangeRateUtil, "Commitment_LC") );
		this.addCellTransformer( new CellTransformerImplementations.AMOUNTS_EXTENDED (this.exchangeRateUtil, "Amounts Extended") );
		this.addCellTransformer( new CellTransformerImplementations.RECEIVED(this.exchangeRateUtil, "Received") );
		this.addCellTransformer( new CellTransformerImplementations.AMOUNT_TIED(this.exchangeRateUtil, "Amount Tied") );
		this.addCellTransformer( new CellTransformerImplementations.AMOUNT_PARTIALLY_UNTIED(this.exchangeRateUtil, "Amount Partially Untied") );
		this.addCellTransformer( new CellTransformerImplementations.AMOUNT_UNTIED (this.exchangeRateUtil, "Amount Untied") );
		this.addCellTransformer( new CellTransformerImplementations.IRTC (this.exchangeRateUtil, "IRTC") );
		this.addCellTransformer( new CellTransformerImplementations.EXPERT_COMMITMENT(this.exchangeRateUtil, "Amount of experts-commitment") );
		this.addCellTransformer( new CellTransformerImplementations.EXPERT_EXTENDED(this.exchangeRateUtil, "Amount of experts-extended") );
		this.addCellTransformer( new CellTransformerImplementations.EXPORT_CREDIT (this.exchangeRateUtil, "Amount of export credit") );
		this.addCellTransformer( new CellTransformerImplementations.TYPE_OF_REPAYMENT_CODE("Type (EPP:1,annuity:2,lump sum:3,other:5)") );
		this.addCellTransformer( new CellTransformerImplementations.NUMBER_OF_REPAYMENT_CODE("Number of repayment per annum") );
		
		this.addCellTransformer( new CellTransformerImplementations.INTEREST1("Interest rate") );
		this.addCellTransformer( new CellTransformerImplementations.INTEREST2("Second interest rate") );
		
		this.addCellTransformer( new CellTransformerImplementations.REPAY_DATE1("First repayment date") );
		this.addCellTransformer( new CellTransformerImplementations.REPAY_DATE2("Final repayment date") );
		
		this.addCellTransformer( new CellTransformerImplementations.INTEREST_RECEIVED (this.exchangeRateUtil, "Interest received") );
		
		this.addCellTransformer( new CellTransformerImplementations.OUTSTANDING (this.exchangeRateUtil, "Principal disbursed and still outstanding") );
		
		this.addCellTransformer( new CellTransformerImplementations.ARREARS_OF_PRINCIPAL (this.exchangeRateUtil, "Arrears of principal") );
		this.addCellTransformer( new CellTransformerImplementations.ARREARS_OF_INTEREST (this.exchangeRateUtil, "Arrears of interest") );
		
		this.addCellTransformer( new CellTransformerImplementations.RMNCH_CODE("RMNCH") );
		
		
	}
}
