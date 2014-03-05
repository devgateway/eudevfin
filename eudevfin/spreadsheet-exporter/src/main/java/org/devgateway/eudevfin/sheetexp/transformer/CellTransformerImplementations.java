package org.devgateway.eudevfin.sheetexp.transformer;

import java.math.BigDecimal;

import org.devgateway.eudevfin.exchange.common.service.ExchangeRateUtil;
import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.metadata.common.domain.SectorCategory;
import org.devgateway.eudevfin.sheetexp.dto.EntityWrapperInterface;
import org.devgateway.eudevfin.sheetexp.dto.MetadataCell;
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;
import org.joda.time.LocalDateTime;

public class CellTransformerImplementations {

	public static class EXTRACTION_DATE_TRANS extends AbstractFssCellTransformer<LocalDateTime> {
		
		public EXTRACTION_DATE_TRANS(final String headerName) {
			super(headerName);
		}

		@Override
		public MetadataCell<LocalDateTime> innerTransform(final FinancialTransaction tx) {
			return null;
		}

		@Override
		public MetadataCell<LocalDateTime> transform(final EntityWrapperInterface src) {
			final MetadataCell<LocalDateTime> cell = new MetadataCell<LocalDateTime>(src.getProcessStartTime());
			this.setDataTypeToDate(cell);
			return cell;
		}

	}

	public static class REPORTING_YEAR extends AbstractFssCellTransformer<String> {
		
		public REPORTING_YEAR(final String headerName) {
			super(headerName);
		}

		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			String value = null;
			if (tx.getReportingYear() != null) {
				value = String.valueOf(tx.getReportingYear().getYear());
			}
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;

		}
	}

	public static class DONOR_CODE extends AbstractFssCellTransformer<String> {
		public DONOR_CODE(final String headerName) {
			super(headerName);
		}

		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			String value = null;
			if (tx.getExtendingAgency() != null) {
				value = tx.getExtendingAgency().getDonorCode();
			}
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	}

	public static class DONOR extends AbstractFssCellTransformer<String> {
		public DONOR(final String headerName) {
			super(headerName);
		}

		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			String value = null;
			if (tx.getExtendingAgency() != null) {
				value = tx.getExtendingAgency().getDonorName();
			}
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	}

	public static class AGENCY_CODE extends AbstractFssCellTransformer<String> {
		public AGENCY_CODE(final String headerName) {
			super(headerName);
		}

		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			String value = null;
			if (tx.getExtendingAgency() != null) {
				value = tx.getExtendingAgency().getCode();
			}
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	}
	
	public static class AGENCY_NAME extends AbstractFssCellTransformer<String> {
		public AGENCY_NAME(final String headerName) {
			super(headerName);
		}

		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			String value = null;
			if (tx.getExtendingAgency() != null) {
				value = tx.getExtendingAgency().getCode();
			}
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	}

	public static class CRSID extends AbstractFssCellTransformer<String> {
		public CRSID(final String headerName) {
			super(headerName);
		}

		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			String value = null;
			value = tx.getCrsIdentificationNumber();
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	}

	public static class PROJECT_NUMBER extends AbstractFssCellTransformer<String> {

		public PROJECT_NUMBER(final String headerName) {
			super(headerName);
		}

		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			String value = null;
			value = tx.getDonorProjectNumber();
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	}

	public static class RECIPIENT_CODE extends AbstractFssCellTransformer<String> {
		public RECIPIENT_CODE(final String headerName) {
			super(headerName);
		}

		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			final CustomFinancialTransaction ctx = (CustomFinancialTransaction) tx;
			String value = null;
			if (ctx.getRecipientCode() != null) {
				value = ctx.getRecipientCode().getDisplayableCode();
			}
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	}

	public static class RECIPIENT extends AbstractFssCellTransformer<String> {
		public RECIPIENT(final String headerName) {
			super(headerName);
		}

		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			String value = null;
			if (tx.getRecipient() != null) {
				value = tx.getRecipient().getName();
			}
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	}

	public static class REGION extends AbstractFssCellTransformer<String> {
		public REGION(final String headerName) {
			super(headerName);
		}

		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			String value = null;
			if (tx.getRecipient() != null) {
				value = tx.getRecipient().getName();
			}
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	}

	public static class PRIORITY_CODE extends AbstractFssCellTransformer<String> {
		public PRIORITY_CODE(final String headerName) {
			super(headerName);
		}


		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			final CustomFinancialTransaction ctx = (CustomFinancialTransaction) tx;
			String value = null;
			if (ctx.getRecipientPriority() != null) {
				value = ctx.getRecipientPriority().getName();
			}
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	}

	public static class PHASE_OUT extends AbstractFssCellTransformer<String> {
		public PHASE_OUT(final String headerName) {
			super(headerName);
		}

		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			final CustomFinancialTransaction ctx = (CustomFinancialTransaction) tx;
			String value = null;
			if (ctx.getPhasingOutYear() != null) {
				value = String.valueOf(ctx.getPhasingOutYear().getYear());
			}
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	}

	public static class CHANNEL extends AbstractFssCellTransformer<String> {
		public CHANNEL(final String headerName) {
			super(headerName);
		}

		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			String value = null;
			if (tx.getChannel() != null) {
				value = tx.getChannel().getName();
			}
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	}

	public static class CHANNEL_CODE extends AbstractFssCellTransformer<String> {
		public CHANNEL_CODE(final String headerName) {
			super(headerName);
		}

		@Override
		public String getHeaderName() {
			return "ChannelCode";
		}

		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			String value = null;
			if (tx.getChannel() != null) {
				value = tx.getChannel().getDisplayableCode();
			}
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	}

	public static class BI_MULTI_CODE extends AbstractFssCellTransformer<String> {
		public BI_MULTI_CODE(final String headerName) {
			super(headerName);
		}

		@Override
		public String getHeaderName() {
			return "Bi_multi";
		}

		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			String value = null;
			if (tx.getBiMultilateral() != null) {
				value = tx.getBiMultilateral().getDisplayableCode();
			}
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	}

	public static class FLOW_CODE extends AbstractFssCellTransformer<String> {
		public FLOW_CODE(final String headerName) {
			super(headerName);
		}


		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			String value = null;
			if (tx.getTypeOfFlow() != null) {
				value = tx.getTypeOfFlow().getDisplayableCode();
			}
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	}

	public static class TYPE_OF_FINANCE_CODE extends AbstractFssCellTransformer<String> {
		public TYPE_OF_FINANCE_CODE(final String headerName) {
			super(headerName);
		}

		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			String value = null;
			if (tx.getTypeOfFinance() != null) {
				value = tx.getTypeOfFinance().getDisplayableCode();
			}
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	}

	public static class TYPE_OF_AID_CODE extends AbstractFssCellTransformer<String> {
		public TYPE_OF_AID_CODE(final String headerName) {
			super(headerName);
		}

		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			String value = null;
			if (tx.getTypeOfAid() != null) {
				value = tx.getTypeOfAid().getDisplayableCode();
			}
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	}
	
	public static class NATURE_OF_SUBMISSION extends AbstractFssCellTransformer<String> {
		public NATURE_OF_SUBMISSION(final String headerName) {
			super(headerName);
		}


		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			String value = null;
			if (tx.getNatureOfSubmission() != null) {
				value = tx.getNatureOfSubmission().getName();
			}
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	}

	public static class SHORT_DESCRIPTION extends AbstractFssCellTransformer<String> {
		public SHORT_DESCRIPTION(final String headerName) {
			super(headerName);
		}

		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			final String value = tx.getShortDescription();
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	}

	public static class PURPOSE_CODE extends AbstractFssCellTransformer<String> {
		public PURPOSE_CODE(final String headerName) {
			super(headerName);
		}

		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			String value = null;
			if (tx.getSector() != null) {
				value = tx.getSector().getDisplayableCode();
			}
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	}
	public static class MAIN_SECTOR extends AbstractFssCellTransformer<String> {
		public MAIN_SECTOR(final String headerName) {
			super(headerName);
		}

		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			MetadataCell<String> cell = null;
			SectorCategory sector = (SectorCategory) tx.getSector();
			while (sector != null) {
				if (sector.getParentCategory() != null && sector.getParentCategory().getParentCategory() == null) {
					cell = new MetadataCell<String>(sector.getName());
					this.setDataTypeToString(cell);
					return cell;
				}
				sector = (SectorCategory) sector.getParentCategory();
			}

			cell = new MetadataCell<String>(null);
			this.setDataTypeToString(cell);
			return cell;

		}
	}

	public static class GEOGRAPHY extends AbstractFssCellTransformer<String> {
		public GEOGRAPHY(final String headerName) {
			super(headerName);
		}

		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			String value = null;
			if (tx.getRecipient() != null) {
				value = tx.getRecipient().getGeographyCategory().getName();
			}
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	}

	public static class EXPECTED_START_DATE extends AbstractFssCellTransformer<LocalDateTime> {
		public EXPECTED_START_DATE(final String headerName) {
			super(headerName);
		}

		@Override
		public MetadataCell<LocalDateTime> innerTransform(final FinancialTransaction tx) {
			final LocalDateTime value = tx.getExpectedStartDate();
			final MetadataCell<LocalDateTime> cell = new MetadataCell<LocalDateTime>(value);
			this.setDataTypeToDate(cell);
			return cell;
		}
	}

	public static class COMPLETION_DATE extends AbstractFssCellTransformer<LocalDateTime> {
		public COMPLETION_DATE(final String headerName) {
			super(headerName);
		}

		@Override
		public MetadataCell<LocalDateTime> innerTransform(final FinancialTransaction tx) {
			final LocalDateTime value = tx.getExpectedCompletionDate();
			final MetadataCell<LocalDateTime> cell = new MetadataCell<LocalDateTime>(value);
			this.setDataTypeToDate(cell);
			return cell;
		}
	}

	public static class LONG_DESCRIPTION extends AbstractFssCellTransformer<String> {
		public LONG_DESCRIPTION(final String headerName) {
			super(headerName);
		}

		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			final String value = tx.getDescription();
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	}

	public static class GENDER extends AbstractFssCellTransformer<String> {
		public GENDER(final String headerName) {
			super(headerName);
		}

		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			String value = null;
			if (tx.getGenderEquality() != null) {
				value = tx.getGenderEquality().getDisplayableCode();
			}
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	}

	public static class ENVIRONMENT extends AbstractFssCellTransformer<String> {
		public ENVIRONMENT(final String headerName) {
			super(headerName);
		}

		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			String value = null;
			if (tx.getAidToEnvironment() != null) {
				value = tx.getAidToEnvironment().getDisplayableCode();
			}
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	}

	public static class PDGG extends AbstractFssCellTransformer<String> {
		public PDGG(final String headerName) {
			super(headerName);
		}

		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			String value = null;
			if (tx.getPdgg() != null) {
				value = tx.getPdgg().getDisplayableCode();
			}
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	}

	public static class TRADE_DEVELOPMENT_CODE extends AbstractFssCellTransformer<String> {
		public TRADE_DEVELOPMENT_CODE(final String headerName) {
			super(headerName);
		}

		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			String value = null;
			if (tx.getTradeDevelopment() != null) {
				value = tx.getTradeDevelopment().getDisplayableCode();
			}
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	}

	public static class FTC_1_0 extends AbstractFssCellTransformer<String> {
		public FTC_1_0(final String headerName) {
			super(headerName);
		}

		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			String value = null;
			if (tx.getFreestandingTechnicalCooperation() != null) {
				value = tx.getFreestandingTechnicalCooperation() ? "1" : "0";
			}
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	}
	public static class PBA_1_0 extends AbstractFssCellTransformer<String> {
		public PBA_1_0(final String headerName) {
			super(headerName);
		}

		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			String value = null;
			if (tx.getProgrammeBasedApproach() != null) {
				value = tx.getProgrammeBasedApproach() ? "1" : "0";
			}
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	}

	public static class INVESTMENT_PROJECT_1_0 extends AbstractFssCellTransformer<String> {
		public INVESTMENT_PROJECT_1_0(final String headerName) {
			super(headerName);
		}

		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			String value = null;
			if (tx.getInvestment() != null) {
				value = tx.getInvestment() ? "1" : "0";
			}
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	}

	public static class ASSOC_FINANCE_1_0 extends AbstractFssCellTransformer<String> {
		public ASSOC_FINANCE_1_0(final String headerName) {
			super(headerName);
		}

		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			String value = null;
			if (tx.getAssociatedFinancing() != null) {
				value = tx.getAssociatedFinancing() ? "1" : "0";
			}
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	}

	public static class BIODIVERSITY_CODE extends AbstractFssCellTransformer<String> {
		public BIODIVERSITY_CODE(final String headerName) {
			super(headerName);
		}

		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			String value = null;
			if (tx.getBiodiversity() != null) {
				value = tx.getBiodiversity().getDisplayableCode();
			}
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	}

	public static class CLIMATE_MITIGATION_CODE extends AbstractFssCellTransformer<String> {
		public CLIMATE_MITIGATION_CODE(final String headerName) {
			super(headerName);
		}

		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			String value = null;
			if (tx.getClimateChangeMitigation() != null) {
				value = tx.getClimateChangeMitigation().getDisplayableCode();
			}
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	}

	public static class CLIMATE_ADAPTATION_CODE extends AbstractFssCellTransformer<String> {
		public CLIMATE_ADAPTATION_CODE(final String headerName) {
			super(headerName);
		}

		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			String value = null;
			if (tx.getClimateChangeAdaptation() != null) {
				value = tx.getClimateChangeAdaptation().getDisplayableCode();
			}
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	}

	public static class DESERTIFICATION_CODE extends AbstractFssCellTransformer<String> {
		public DESERTIFICATION_CODE(final String headerName) {
			super(headerName);
		}

		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			String value = null;
			if (tx.getDesertification() != null) {
				value = tx.getDesertification().getDisplayableCode();
			}
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	}
	
	public static class AID_TO_ENVIRONMENT_CODE extends AbstractFssCellTransformer<String> {
		public AID_TO_ENVIRONMENT_CODE(final String headerName) {
			super(headerName);
		}

		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			String value = null;
			if (tx.getAidToEnvironment() != null) {
				value = tx.getAidToEnvironment().getDisplayableCode();
			}
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	}

	public static class CURRENCY_CODE extends AbstractFssCellTransformer<String> {
		public CURRENCY_CODE(final String headerName) {
			super(headerName);
		}

		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			String value = null;
			if (tx.getCurrency() != null) {
				value = tx.getCurrency().getNumeric3Code();
			}
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	}
	
	public static class COMMITMENT_DATE extends AbstractFssCellTransformer<LocalDateTime> {
		public COMMITMENT_DATE(final String headerName) {
			super(headerName);
		}

		@Override
		public MetadataCell<LocalDateTime> innerTransform(final FinancialTransaction tx) {
			final LocalDateTime value = tx.getCommitmentDate();
			final MetadataCell<LocalDateTime> cell = new MetadataCell<LocalDateTime>(value);
			this.setDataTypeToDate(cell);
			return cell;
		}
	}
	
	public static class TYPE_OF_REPAYMENT_CODE extends AbstractFssCellTransformer<String> {
		public TYPE_OF_REPAYMENT_CODE(final String headerName) {
			super(headerName);
		}

		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			String value = null;
			if (tx.getTypeOfRepayment() != null) {
				value = tx.getTypeOfRepayment().getDisplayableCode();
			}
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	}
	
	public static class NUMBER_OF_REPAYMENT_CODE extends AbstractFssCellTransformer<String> {
		public NUMBER_OF_REPAYMENT_CODE(final String headerName) {
			super(headerName);
		}

		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			String value = null;
			if (tx.getNumberOfRepaymentsAnnum() != null) {
				value = tx.getNumberOfRepaymentsAnnum().getDisplayableCode();
			}
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	}
	
	public static class INTEREST1 extends AbstractFssCellTransformer<BigDecimal> {
		public INTEREST1(final String headerName) {
			super(headerName);
		}

		@Override
		public MetadataCell<BigDecimal> innerTransform(final FinancialTransaction tx) {
			final BigDecimal value = tx.getInterestRate();
			
			final MetadataCell<BigDecimal> cell = new MetadataCell<BigDecimal>(value);
			this.setDataTypeToNumber(cell);
			return cell;
		}
	}
	
	public static class INTEREST2 extends AbstractFssCellTransformer<BigDecimal> {
		public INTEREST2(final String headerName) {
			super(headerName);
		}

		@Override
		public String getHeaderName() {
			return "Interest2";
		}

		@Override
		public MetadataCell<BigDecimal> innerTransform(final FinancialTransaction tx) {
			final BigDecimal value = tx.getSecondInterestRate();
			
			final MetadataCell<BigDecimal> cell = new MetadataCell<BigDecimal>(value);
			this.setDataTypeToNumber(cell);
			return cell;
		}
	}
	
	public static class REPAY_DATE1 extends AbstractFssCellTransformer<LocalDateTime> {
		public REPAY_DATE1(final String headerName) {
			super(headerName);
		}

		@Override
		public MetadataCell<LocalDateTime> innerTransform(final FinancialTransaction tx) {
			final LocalDateTime value = tx.getFirstRepaymentDate();
			final MetadataCell<LocalDateTime> cell = new MetadataCell<LocalDateTime>(value);
			this.setDataTypeToDate(cell);
			return cell;
		}
	}
	
	public static class REPAY_DATE2 extends AbstractFssCellTransformer<LocalDateTime> {
		public REPAY_DATE2(final String headerName) {
			super(headerName);
		}

		@Override
		public MetadataCell<LocalDateTime> innerTransform(final FinancialTransaction tx) {
			final LocalDateTime value = tx.getFinalRepaymentDate();
			final MetadataCell<LocalDateTime> cell = new MetadataCell<LocalDateTime>(value);
			this.setDataTypeToDate(cell);
			return cell;
		}
	}
	
	public static class NOTES extends AbstractFssCellTransformer<String> {
		public NOTES(final String headerName) {
			super(headerName);
		}

		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			final CustomFinancialTransaction ctx	= (CustomFinancialTransaction) tx;
			final String value = ctx.getOtherComments();
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	}
	
	
	
	
	
	
	

	static abstract class AbstractMoneyFssCellTransformer extends AbstractFssCellTransformer<BigMoney> {

		private final ExchangeRateUtil exchangeRateUtil;

		public AbstractMoneyFssCellTransformer(final ExchangeRateUtil exchangeRateUtil, final String headerName) {
			super(headerName);
			this.exchangeRateUtil = exchangeRateUtil;
		}
		@Override
		public MetadataCell<BigMoney> innerTransform(final FinancialTransaction tx) {
			final CustomFinancialTransaction ctx = (CustomFinancialTransaction) tx;
			BigMoney finalMoney = null;
			final CurrencyUnit localCU = ctx.getCurrency();
			final BigMoney money = this.getMoney(tx);
			if (localCU != null && money != null) {

				finalMoney = this.exchangeRateUtil.exchange(money, localCU, ctx.getFixedRate(), ctx.getCommitmentDate());
			}
			final MetadataCell<BigMoney> cell = new MetadataCell<BigMoney>(finalMoney);
			this.setDataTypeToMoney(cell);
			return cell;
		}
		
		protected abstract BigMoney getMoney(FinancialTransaction tx);
	}

	public static class COMMITMENT_LC extends AbstractMoneyFssCellTransformer {

		public COMMITMENT_LC(final ExchangeRateUtil exchangeRateUtil, final String headerName) {
			super(exchangeRateUtil, headerName);
		}

		@Override
		protected BigMoney getMoney(final FinancialTransaction tx) {
			return tx.getCommitments();
		}

		
	}

	public static class AMOUNTS_EXTENDED extends AbstractMoneyFssCellTransformer {

		public AMOUNTS_EXTENDED(final ExchangeRateUtil exchangeRateUtil, final String headerName) {
			super(exchangeRateUtil, headerName);
		}

		@Override
		protected BigMoney getMoney(final FinancialTransaction tx) {
			return tx.getAmountsExtended();
		}
	}

	public static class RECEIVED extends AbstractMoneyFssCellTransformer {


		public RECEIVED(final ExchangeRateUtil exchangeRateUtil, final String headerName) {
			super(exchangeRateUtil, headerName);
		}

		@Override
		protected BigMoney getMoney(final FinancialTransaction tx) {
			return tx.getAmountsReceived();
		}
	}
	
	public static class AMOUNT_UNTIED extends AbstractMoneyFssCellTransformer {

		public AMOUNT_UNTIED(final ExchangeRateUtil exchangeRateUtil, final String headerName) {
			super(exchangeRateUtil, headerName);
		}

		@Override
		protected BigMoney getMoney(final FinancialTransaction tx) {
			return tx.getAmountsUntied();
		}
	}
	
	public static class AMOUNT_PARTIALLY_UNTIED extends AbstractMoneyFssCellTransformer {

		public AMOUNT_PARTIALLY_UNTIED(final ExchangeRateUtil exchangeRateUtil, final String headerName) {
			super(exchangeRateUtil, headerName);
		}

		@Override
		protected BigMoney getMoney(final FinancialTransaction tx) {
			return tx.getAmountsPartiallyUntied();
		}
	}
	public static class AMOUNT_TIED extends AbstractMoneyFssCellTransformer {

		public AMOUNT_TIED(final ExchangeRateUtil exchangeRateUtil, final String headerName) {
			super(exchangeRateUtil, headerName);
		}

		@Override
		protected BigMoney getMoney(final FinancialTransaction tx) {
			return tx.getAmountsTied();
		}
	}
	public static class IRTC extends AbstractMoneyFssCellTransformer {

		public IRTC(final ExchangeRateUtil exchangeRateUtil, final String headerName) {
			super(exchangeRateUtil, headerName);
		}

		@Override
		protected BigMoney getMoney(final FinancialTransaction tx) {
			return tx.getAmountOfIRTC();
		}
	}
	public static class EXPORT_CREDIT extends AbstractMoneyFssCellTransformer {

		public EXPORT_CREDIT(final ExchangeRateUtil exchangeRateUtil, final String headerName) {
			super(exchangeRateUtil, headerName);
		}

		@Override
		protected BigMoney getMoney(final FinancialTransaction tx) {
			return tx.getAmountOfExportCreditInAFPackage();
		}
	}
	public static class INTEREST_RECEIVED extends AbstractMoneyFssCellTransformer {

		public INTEREST_RECEIVED(final ExchangeRateUtil exchangeRateUtil, final String headerName) {
			super(exchangeRateUtil, headerName);
		}

		@Override
		protected BigMoney getMoney(final FinancialTransaction tx) {
			return tx.getInterestReceived();
		}
	}
	
	public static class ARREARS_OF_INTEREST extends AbstractMoneyFssCellTransformer {

		public ARREARS_OF_INTEREST(final ExchangeRateUtil exchangeRateUtil, final String headerName) {
			super(exchangeRateUtil, headerName);
		}

		@Override
		protected BigMoney getMoney(final FinancialTransaction tx) {
			return tx.getArrearsOfInterest();
		}
	}
	
	public static class ARREARS_OF_PRINCIPAL extends AbstractMoneyFssCellTransformer {

		public ARREARS_OF_PRINCIPAL(final ExchangeRateUtil exchangeRateUtil, final String headerName) {
			super(exchangeRateUtil, headerName);
		}

		@Override
		protected BigMoney getMoney(final FinancialTransaction tx) {
			return tx.getArrearsOfPrincipal();
		}
	}
	
	public static class OUTSTANDING extends AbstractMoneyFssCellTransformer {
		public OUTSTANDING(final ExchangeRateUtil exchangeRateUtil, final String headerName) {
			super(exchangeRateUtil, headerName);
		}

		@Override
		protected BigMoney getMoney(final FinancialTransaction tx) {
			return tx.getPrincipalDisbursedOutstanding();
		}
	}
	
	public static class FUTURE_DS_PRINCIPAL extends AbstractMoneyFssCellTransformer {
		public FUTURE_DS_PRINCIPAL(final ExchangeRateUtil exchangeRateUtil, final String headerName) {
			super(exchangeRateUtil, headerName);
		}

		@Override
		protected BigMoney getMoney(final FinancialTransaction tx) {
			final CustomFinancialTransaction ctx	= (CustomFinancialTransaction)tx;
			return ctx.getFutureDebtPrincipal();
		}
	}
	
	public static class FUTURE_DS_INTEREST extends AbstractMoneyFssCellTransformer {

		public FUTURE_DS_INTEREST(final ExchangeRateUtil exchangeRateUtil, final String headerName) {
			super(exchangeRateUtil, headerName);
		}

		@Override
		protected BigMoney getMoney(final FinancialTransaction tx) {
			final CustomFinancialTransaction ctx	= (CustomFinancialTransaction)tx;
			return ctx.getFutureDebtInterest();
		}
	}
	
	public static class EXPERT_COMMITMENT extends AbstractMoneyFssCellTransformer {

		public EXPERT_COMMITMENT(final ExchangeRateUtil exchangeRateUtil, final String headerName) {
			super(exchangeRateUtil, headerName);
		}

		@Override
		protected BigMoney getMoney(final FinancialTransaction tx) {
			return tx.getProjectAmountExpertCommitments();
		}
	}
	public static class EXPERT_EXTENDED extends AbstractMoneyFssCellTransformer {

		public EXPERT_EXTENDED(final ExchangeRateUtil exchangeRateUtil, final String headerName) {
			super(exchangeRateUtil, headerName);
		}

		@Override
		protected BigMoney getMoney(final FinancialTransaction tx) {
			return tx.getProjectAmountExpertExtended();
		}
	}
	
	
}
