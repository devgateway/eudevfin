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
		this.addCellTransformer( new CellTransformerImplementations.EXTRACTION_DATE_TRANS("Date (data as per date)") );
		
		this.addCellTransformer( new CellTransformerImplementations.REPORTING_YEAR("Reporting year") );
		
		this.addCellTransformer( new CellTransformerImplementations.DONOR_CODE("Reporting country / organisation") ) ;
		
		//this.addCellTransformer( new CellTransformerImplementations.DONOR("Donor") );
		
		// agency code
		this.addCellTransformer( new CellTransformerImplementations.AGENCY_CODE("Extending agency") );
		this.addCellTransformer( new CellTransformerImplementations.CRSID("CRS Identification num") );
		this.addCellTransformer( new CellTransformerImplementations.PROJECT_NUMBER("Donor project num") );
		
		this.addCellTransformer( new CellTransformerImplementations.LEVEL_OF_CERTAINTY("Level of certainty") );
		//missing certainty code
		this.addCellTransformer( new CellTransformerImplementations.RECIPIENT_CODE("Recipient code") );
		this.addCellTransformer( new CellTransformerImplementations.RECIPIENT("Recipient") );
		
		this.addCellTransformer( new CellTransformerImplementations.PRIORITY_NAME("Priority status of recipient") );
		
		
		this.addCellTransformer( new CellTransformerImplementations.PHASE_OUT("If phasing out, by which year") );
		this.addCellTransformer( new CellTransformerImplementations.CHANNEL("Channel of delivery name") );
		this.addCellTransformer( new CellTransformerImplementations.CHANNEL_CODE("Channel code") );
		
		this.addCellTransformer( new CellTransformerImplementations.BI_MULTI_CODE("Bi/Multi") );
		this.addCellTransformer( new CellTransformerImplementations.FLOW_CODE("Type of flow") );
		
		this.addCellTransformer( new CellTransformerImplementations.TYPE_OF_FINANCE_CODE("Type of finance") );
		this.addCellTransformer( new CellTransformerImplementations.TYPE_OF_AID_CODE("Type of aid") );
		this.addCellTransformer( new CellTransformerImplementations.SHORT_DESCRIPTION("Short description / Project title") );
		//missing exclusion
		this.addCellTransformer( new CellTransformerImplementations.PURPOSE_CODE("Sector / Purpose code") );
//		this.addCellTransformer( new CellTransformerImplementations.MAIN_SECTOR("MainSector") );
		this.addCellTransformer( new CellTransformerImplementations.GEOGRAPHY("Geographical target area") );
		this.addCellTransformer( new CellTransformerImplementations.EXPECTED_START_DATE("Expected starting date ") );
		this.addCellTransformer( new CellTransformerImplementations.COMPLETION_DATE("Expected completion date") );
		
		this.addCellTransformer( new CellTransformerImplementations.LONG_DESCRIPTION("Description") );
		this.addCellTransformer( new CellTransformerImplementations.GENDER("Gender equality") );
		this.addCellTransformer( new CellTransformerImplementations.ENVIRONMENT("Aid to environment") );
		this.addCellTransformer( new CellTransformerImplementations.PDGG("PD/GG") );
		this.addCellTransformer( new CellTransformerImplementations.TRADE_DEVELOPMENT_CODE("Trade Development") );
		this.addCellTransformer( new CellTransformerImplementations.FTC_1_0("FTC") );
		this.addCellTransformer( new CellTransformerImplementations.PBA_1_0("Sector programme") );
		this.addCellTransformer( new CellTransformerImplementations.INVESTMENT_PROJECT_1_0("Investment project") );
		this.addCellTransformer( new CellTransformerImplementations.ASSOC_FINANCE_1_0("AF") );
		this.addCellTransformer( new CellTransformerImplementations.BIODIVERSITY_CODE("Biodiversity") );
		this.addCellTransformer( new CellTransformerImplementations.CLIMATE_MITIGATION_CODE("Climate change mitigation") );
		this.addCellTransformer( new CellTransformerImplementations.CLIMATE_ADAPTATION_CODE("Climate change adaptation") );
		this.addCellTransformer( new CellTransformerImplementations.DESERTIFICATION_CODE("Desertification") );
		this.addCellTransformer( new CellTransformerImplementations.CURRENCY_CODE("CurrencyCode") );
		
		this.addCellTransformer( new CellTransformerImplementations.COMMITMENT_LC (this.exchangeRateUtil, "Commitments") );
		this.addCellTransformer( new CellTransformerImplementations.AMOUNTS_EXTENDED (this.exchangeRateUtil, "Amounts extended") );
		
		this.addCellTransformer( new CellTransformerImplementations.PLANNED_EXTENDED_CURR(this.exchangeRateUtil, "Amounts extended (current year preliminary)") );
		this.addCellTransformer( new CellTransformerImplementations.PLANNED_EXTENDED_P1(this.exchangeRateUtil, "Amount planned to be extended (current year+1)") );
		this.addCellTransformer( new CellTransformerImplementations.PLANNED_EXTENDED_P2(this.exchangeRateUtil, "Amount planned to be extended (current year+2)") );
		this.addCellTransformer( new CellTransformerImplementations.PLANNED_EXTENDED_P3(this.exchangeRateUtil, "Amount planned to be extended (current year+3)") );
		this.addCellTransformer( new CellTransformerImplementations.PLANNED_EXTENDED_P4(this.exchangeRateUtil, "Amount planned to be extended (current year+4)") );
		
		this.addCellTransformer( new CellTransformerImplementations.RECEIVED(this.exchangeRateUtil, "Amounts received (for loans: only principal)") );
		this.addCellTransformer( new CellTransformerImplementations.AMOUNT_UNTIED (this.exchangeRateUtil, "Amount untied") );
		this.addCellTransformer( new CellTransformerImplementations.AMOUNT_PARTIALLY_UNTIED(this.exchangeRateUtil, "Amount partially untied") );
		this.addCellTransformer( new CellTransformerImplementations.AMOUNT_TIED(this.exchangeRateUtil, "Amount tied") );
		this.addCellTransformer( new CellTransformerImplementations.IRTC (this.exchangeRateUtil, "Amount of IRTC") );
		this.addCellTransformer( new CellTransformerImplementations.EXPERT_COMMITMENT(this.exchangeRateUtil, "Amount of experts-commitments") );
		this.addCellTransformer( new CellTransformerImplementations.EXPERT_EXTENDED(this.exchangeRateUtil, "Amount of experts-extended") );
		this.addCellTransformer( new CellTransformerImplementations.EXPORT_CREDIT (this.exchangeRateUtil, "Amount of export credit") );
		
		this.addCellTransformer( new CellTransformerImplementations.COMMITMENT_DATE("Commitment date") );
		this.addCellTransformer( new CellTransformerImplementations.TYPE_OF_REPAYMENT_CODE("Type (EPP:1,annuity:2,lump sum:3,other:5)") );
		this.addCellTransformer( new CellTransformerImplementations.NUMBER_OF_REPAYMENT_CODE("Number of repayment per annum") );
		this.addCellTransformer( new CellTransformerImplementations.INTEREST1("Interest rate") );
		this.addCellTransformer( new CellTransformerImplementations.INTEREST2("Second interest rate") );
		this.addCellTransformer( new CellTransformerImplementations.REPAY_DATE1("First repayment date") );
		this.addCellTransformer( new CellTransformerImplementations.REPAY_DATE2("Final repayment date") );
		
		this.addCellTransformer( new CellTransformerImplementations.INTEREST_RECEIVED (this.exchangeRateUtil, "Interest received") );
		this.addCellTransformer( new CellTransformerImplementations.OUTSTANDING (this.exchangeRateUtil, "Principal disbursed and still outstanding") );
		this.addCellTransformer( new CellTransformerImplementations.ARREARS_OF_PRINCIPAL (this.exchangeRateUtil, "Arrears of principal (included in item 46)") );
		this.addCellTransformer( new CellTransformerImplementations.ARREARS_OF_INTEREST (this.exchangeRateUtil, "Arrears of interest") );
		this.addCellTransformer( new CellTransformerImplementations.FUTURE_DS_PRINCIPAL (this.exchangeRateUtil, "Future debt service: First year, principal") );
		this.addCellTransformer( new CellTransformerImplementations.FUTURE_DS_INTEREST (this.exchangeRateUtil, "Future debt service: First year, interest") );
		
		this.addCellTransformer( new CellTransformerImplementations.NOTES("Notes") );
		
		this.addCellTransformer( new CellTransformerImplementations.RMNCH_CODE("RMNCH") );
	}
	
	
}
