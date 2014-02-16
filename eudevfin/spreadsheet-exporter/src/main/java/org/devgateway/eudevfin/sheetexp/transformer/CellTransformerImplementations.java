package org.devgateway.eudevfin.sheetexp.transformer;

import java.math.BigDecimal;

import org.devgateway.eudevfin.exchange.common.service.ExchangeRateUtil;
import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.SectorCategory;
import org.devgateway.eudevfin.sheetexp.dto.EntityWrapperInterface;
import org.devgateway.eudevfin.sheetexp.dto.MetadataCell;
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;
import org.joda.time.LocalDateTime;

public class CellTransformerImplementations {

	public static AbstractFssCellTransformer<LocalDateTime> EXTRACTION_DATE_TRANS = new AbstractFssCellTransformer<LocalDateTime>() {
		@Override
		public String getHeaderName() {
			return "ExtractionDate";
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

	};

	public static AbstractFssCellTransformer<String> DISBURSEMENT_YEAR_TRANS = new AbstractFssCellTransformer<String>() {
		@Override
		public String getHeaderName() {
			return "DisbursementYear";
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
	};

	public static AbstractFssCellTransformer<String> DONOR_CODE = new AbstractFssCellTransformer<String>() {
		@Override
		public String getHeaderName() {
			return "DonorCode";
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
	};

	public static AbstractFssCellTransformer<String> DONOR = new AbstractFssCellTransformer<String>() {
		@Override
		public String getHeaderName() {
			return "Donor";
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
	};

	public static AbstractFssCellTransformer<String> AGENCY_CODE = new AbstractFssCellTransformer<String>() {
		@Override
		public String getHeaderName() {
			return "AgencyCode";
		}

		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			String value = null;
			if (tx.getExtendingAgency() != null) {
				value = tx.getExtendingAgency().getName();
			}
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	};

	public static AbstractFssCellTransformer<String> CRSID = new AbstractFssCellTransformer<String>() {
		@Override
		public String getHeaderName() {
			return "CRSID";
		}

		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			String value = null;
			value = tx.getCrsIdentificationNumber();
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	};

	public static AbstractFssCellTransformer<String> PROJECT_NUMBER = new AbstractFssCellTransformer<String>() {
		@Override
		public String getHeaderName() {
			return "ProjectNumber";
		}

		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			String value = null;
			value = tx.getDonorProjectNumber();
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	};

	public static AbstractFssCellTransformer<String> RECIPIENT_CODE = new AbstractFssCellTransformer<String>() {
		@Override
		public String getHeaderName() {
			return "RecipientCode";
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
	};

	public static AbstractFssCellTransformer<String> RECIPIENT = new AbstractFssCellTransformer<String>() {
		@Override
		public String getHeaderName() {
			return "Recipient";
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
	};

	public static AbstractFssCellTransformer<String> REGION = new AbstractFssCellTransformer<String>() {
		@Override
		public String getHeaderName() {
			return "Region";
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
	};

	public static AbstractFssCellTransformer<String> PRIORITY_CODE = new AbstractFssCellTransformer<String>() {
		@Override
		public String getHeaderName() {
			return "Priority";
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
	};

	public static AbstractFssCellTransformer<String> PHASE_OUT = new AbstractFssCellTransformer<String>() {
		@Override
		public String getHeaderName() {
			return "PhaseOut";
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
	};

	public static AbstractFssCellTransformer<String> CHANNEL = new AbstractFssCellTransformer<String>() {
		@Override
		public String getHeaderName() {
			return "Channel";
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
	};

	public static AbstractFssCellTransformer<String> CHANNEL_CODE = new AbstractFssCellTransformer<String>() {
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
	};

	public static AbstractFssCellTransformer<String> BI_MULTI = new AbstractFssCellTransformer<String>() {
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
	};

	public static AbstractFssCellTransformer<String> FLOW_CODE = new AbstractFssCellTransformer<String>() {
		@Override
		public String getHeaderName() {
			return "FlowCode";
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
	};

	public static AbstractFssCellTransformer<String> FINANCE_T = new AbstractFssCellTransformer<String>() {
		@Override
		public String getHeaderName() {
			return "Finance_t";
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
	};

	public static AbstractFssCellTransformer<String> AID_T = new AbstractFssCellTransformer<String>() {
		@Override
		public String getHeaderName() {
			return "Aid_t";
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
	};

	public static AbstractFssCellTransformer<String> SHORT_DESCRIPTION = new AbstractFssCellTransformer<String>() {
		@Override
		public String getHeaderName() {
			return "ShortDescription";
		}

		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			final String value = tx.getShortDescription();
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	};

	public static AbstractFssCellTransformer<String> PURPOSE_CODE = new AbstractFssCellTransformer<String>() {
		@Override
		public String getHeaderName() {
			return "PurposeCode";
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
	};
	public static AbstractFssCellTransformer<String> MAIN_SECTOR = new AbstractFssCellTransformer<String>() {
		@Override
		public String getHeaderName() {
			return "MainSector";
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
	};

	public static AbstractFssCellTransformer<String> GEOGRAPHY = new AbstractFssCellTransformer<String>() {
		@Override
		public String getHeaderName() {
			return "Geography";
		}

		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			String value = null;
			if (tx.getRecipient() != null) {
				value = tx.getRecipient().getGeography();
			}
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	};

	public static AbstractFssCellTransformer<LocalDateTime> EXPECTED_START_DATE = new AbstractFssCellTransformer<LocalDateTime>() {
		@Override
		public String getHeaderName() {
			return "ExpectedStartDate";
		}

		@Override
		public MetadataCell<LocalDateTime> innerTransform(final FinancialTransaction tx) {
			final LocalDateTime value = tx.getExpectedStartDate();
			final MetadataCell<LocalDateTime> cell = new MetadataCell<LocalDateTime>(value);
			this.setDataTypeToDate(cell);
			return cell;
		}
	};

	public static AbstractFssCellTransformer<LocalDateTime> COMPLETION_DATE = new AbstractFssCellTransformer<LocalDateTime>() {
		@Override
		public String getHeaderName() {
			return "CompletionDate";
		}

		@Override
		public MetadataCell<LocalDateTime> innerTransform(final FinancialTransaction tx) {
			final LocalDateTime value = tx.getExpectedCompletionDate();
			final MetadataCell<LocalDateTime> cell = new MetadataCell<LocalDateTime>(value);
			this.setDataTypeToDate(cell);
			return cell;
		}
	};

	public static AbstractFssCellTransformer<String> LONG_DESCRIPTION = new AbstractFssCellTransformer<String>() {
		@Override
		public String getHeaderName() {
			return "LongDescription";
		}

		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			final String value = tx.getDescription();
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	};

	public static AbstractFssCellTransformer<String> GENDER = new AbstractFssCellTransformer<String>() {
		@Override
		public String getHeaderName() {
			return "Gender";
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
	};

	public static AbstractFssCellTransformer<String> ENVIRONMENT = new AbstractFssCellTransformer<String>() {
		@Override
		public String getHeaderName() {
			return "Environment";
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
	};

	public static AbstractFssCellTransformer<String> PDGG = new AbstractFssCellTransformer<String>() {
		@Override
		public String getHeaderName() {
			return "PDGG";
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
	};

	public static AbstractFssCellTransformer<String> TRADE_DEVELOPMENT = new AbstractFssCellTransformer<String>() {
		@Override
		public String getHeaderName() {
			return "TradeDevelopment";
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
	};

	public static AbstractFssCellTransformer<String> FTC = new AbstractFssCellTransformer<String>() {
		@Override
		public String getHeaderName() {
			return "FTC";
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
	};
	public static AbstractFssCellTransformer<String> PBA = new AbstractFssCellTransformer<String>() {
		@Override
		public String getHeaderName() {
			return "PBA";
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
	};

	public static AbstractFssCellTransformer<String> INVESTMENT_PROJECT = new AbstractFssCellTransformer<String>() {
		@Override
		public String getHeaderName() {
			return "InvestmentProject";
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
	};

	public static AbstractFssCellTransformer<String> ASSOC_FINANCE = new AbstractFssCellTransformer<String>() {
		@Override
		public String getHeaderName() {
			return "AssocFinance";
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
	};

	public static AbstractFssCellTransformer<String> BIODIVERSITY = new AbstractFssCellTransformer<String>() {
		@Override
		public String getHeaderName() {
			return "Biodiversity";
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
	};

	public static AbstractFssCellTransformer<String> CLIMATE_MITIGATION = new AbstractFssCellTransformer<String>() {
		@Override
		public String getHeaderName() {
			return "ClimateMitigation";
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
	};

	public static AbstractFssCellTransformer<String> CLIMATE_ADAPTATION = new AbstractFssCellTransformer<String>() {
		@Override
		public String getHeaderName() {
			return "ClimateAdaptation";
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
	};

	public static AbstractFssCellTransformer<String> DESERTIFICATION = new AbstractFssCellTransformer<String>() {
		@Override
		public String getHeaderName() {
			return "Desertification";
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
	};

	public static AbstractFssCellTransformer<String> CURRENCY_CODE = new AbstractFssCellTransformer<String>() {
		@Override
		public String getHeaderName() {
			return "CurrencyCode";
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
	};
	
	public static AbstractFssCellTransformer<LocalDateTime> COMMITMENT_DATE = new AbstractFssCellTransformer<LocalDateTime>() {
		@Override
		public String getHeaderName() {
			return "CommitmentDate";
		}

		@Override
		public MetadataCell<LocalDateTime> innerTransform(final FinancialTransaction tx) {
			final LocalDateTime value = tx.getCommitmentDate();
			final MetadataCell<LocalDateTime> cell = new MetadataCell<LocalDateTime>(value);
			this.setDataTypeToDate(cell);
			return cell;
		}
	};
	
	public static AbstractFssCellTransformer<String> TYPE_OF_REPAYMENT = new AbstractFssCellTransformer<String>() {
		@Override
		public String getHeaderName() {
			return "TypeOfRepayment";
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
	};
	
	public static AbstractFssCellTransformer<String> NUMBER_REPAYMENT = new AbstractFssCellTransformer<String>() {
		@Override
		public String getHeaderName() {
			return "NumberRepayment";
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
	};
	
	public static AbstractFssCellTransformer<BigDecimal> INTEREST1 = new AbstractFssCellTransformer<BigDecimal>() {
		@Override
		public String getHeaderName() {
			return "Interest1";
		}

		@Override
		public MetadataCell<BigDecimal> innerTransform(final FinancialTransaction tx) {
			final BigDecimal value = tx.getInterestRate();
			
			final MetadataCell<BigDecimal> cell = new MetadataCell<BigDecimal>(value);
			this.setDataTypeToNumber(cell);
			return cell;
		}
	};
	
	public static AbstractFssCellTransformer<BigDecimal> INTEREST2 = new AbstractFssCellTransformer<BigDecimal>() {
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
	};
	
	public static AbstractFssCellTransformer<LocalDateTime> REPAY_DATE1 = new AbstractFssCellTransformer<LocalDateTime>() {
		@Override
		public String getHeaderName() {
			return "RepayDate1";
		}

		@Override
		public MetadataCell<LocalDateTime> innerTransform(final FinancialTransaction tx) {
			final LocalDateTime value = tx.getFirstRepaymentDate();
			final MetadataCell<LocalDateTime> cell = new MetadataCell<LocalDateTime>(value);
			this.setDataTypeToDate(cell);
			return cell;
		}
	};
	
	public static AbstractFssCellTransformer<LocalDateTime> REPAY_DATE2 = new AbstractFssCellTransformer<LocalDateTime>() {
		@Override
		public String getHeaderName() {
			return "RepayDate2";
		}

		@Override
		public MetadataCell<LocalDateTime> innerTransform(final FinancialTransaction tx) {
			final LocalDateTime value = tx.getFinalRepaymentDate();
			final MetadataCell<LocalDateTime> cell = new MetadataCell<LocalDateTime>(value);
			this.setDataTypeToDate(cell);
			return cell;
		}
	};
	
	public static AbstractFssCellTransformer<String> NOTES = new AbstractFssCellTransformer<String>() {
		@Override
		public String getHeaderName() {
			return "Notes";
		}

		@Override
		public MetadataCell<String> innerTransform(final FinancialTransaction tx) {
			final CustomFinancialTransaction ctx	= (CustomFinancialTransaction) tx;
			final String value = ctx.getOtherComments();
			final MetadataCell<String> cell = new MetadataCell<String>(value);
			this.setDataTypeToString(cell);
			return cell;
		}
	};
	
	
	
	
	
	
	

	private static abstract class AbstractMoneyFssCellTransformer extends AbstractFssCellTransformer<BigMoney> {

		private final ExchangeRateUtil exchangeRateUtil;

		public AbstractMoneyFssCellTransformer(final ExchangeRateUtil exchangeRateUtil) {
			super();
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

		public COMMITMENT_LC(final ExchangeRateUtil exchangeRateUtil) {
			super(exchangeRateUtil);
		}

		@Override
		public String getHeaderName() {
			return "Commitment_LC";
		}

		@Override
		protected BigMoney getMoney(final FinancialTransaction tx) {
			return tx.getCommitments();
		}

		
	}

	public static class DISBURSEMENT_LC extends AbstractMoneyFssCellTransformer {


		public DISBURSEMENT_LC(final ExchangeRateUtil exchangeRateUtil) {
			super(exchangeRateUtil);
		}

		@Override
		public String getHeaderName() {
			return "Disbursement_LC";
		}
		@Override
		protected BigMoney getMoney(final FinancialTransaction tx) {
			return tx.getAmountsExtended();
		}
	}

	public static class RECEIVED extends AbstractMoneyFssCellTransformer {

		public RECEIVED(final ExchangeRateUtil exchangeRateUtil) {
			super(exchangeRateUtil);
		}

		@Override
		public String getHeaderName() {
			return "Received";
		}

		@Override
		protected BigMoney getMoney(final FinancialTransaction tx) {
			return tx.getAmountsReceived();
		}
	}
	
	public static class AMOUNT_UNTIED extends AbstractMoneyFssCellTransformer {

		public AMOUNT_UNTIED(final ExchangeRateUtil exchangeRateUtil) {
			super(exchangeRateUtil);
		}

		@Override
		public String getHeaderName() {
			return "AmountUntied";
		}

		@Override
		protected BigMoney getMoney(final FinancialTransaction tx) {
			return tx.getAmountsUntied();
		}
	}
	
	public static class AMOUNT_PARTIALLY_UNTIED extends AbstractMoneyFssCellTransformer {

		public AMOUNT_PARTIALLY_UNTIED(final ExchangeRateUtil exchangeRateUtil) {
			super(exchangeRateUtil);
		}

		@Override
		public String getHeaderName() {
			return "AmountPartiallyUntied";
		}

		@Override
		protected BigMoney getMoney(final FinancialTransaction tx) {
			return tx.getAmountsPartiallyUntied();
		}
	}
	public static class AMOUNT_TIED extends AbstractMoneyFssCellTransformer {

		public AMOUNT_TIED(final ExchangeRateUtil exchangeRateUtil) {
			super(exchangeRateUtil);
		}

		@Override
		public String getHeaderName() {
			return "AmountTied";
		}

		@Override
		protected BigMoney getMoney(final FinancialTransaction tx) {
			return tx.getAmountsTied();
		}
	}
	public static class IRTC extends AbstractMoneyFssCellTransformer {

		public IRTC(final ExchangeRateUtil exchangeRateUtil) {
			super(exchangeRateUtil);
		}

		@Override
		public String getHeaderName() {
			return "IRTC";
		}

		@Override
		protected BigMoney getMoney(final FinancialTransaction tx) {
			return tx.getAmountOfIRTC();
		}
	}
	public static class EXPORT_CREDIT extends AbstractMoneyFssCellTransformer {

		public EXPORT_CREDIT(final ExchangeRateUtil exchangeRateUtil) {
			super(exchangeRateUtil);
		}

		@Override
		public String getHeaderName() {
			return "ExportCredit";
		}

		@Override
		protected BigMoney getMoney(final FinancialTransaction tx) {
			return tx.getAmountOfExportCreditInAFPackage();
		}
	}
	public static class INTEREST_RECEIVED extends AbstractMoneyFssCellTransformer {

		public INTEREST_RECEIVED(final ExchangeRateUtil exchangeRateUtil) {
			super(exchangeRateUtil);
		}

		@Override
		public String getHeaderName() {
			return "Interest";
		}

		@Override
		protected BigMoney getMoney(final FinancialTransaction tx) {
			return tx.getInterestReceived();
		}
	}
	
	public static class ARREARS_OF_INTEREST extends AbstractMoneyFssCellTransformer {

		public ARREARS_OF_INTEREST(final ExchangeRateUtil exchangeRateUtil) {
			super(exchangeRateUtil);
		}

		@Override
		public String getHeaderName() {
			return "Arrears_interest";
		}

		@Override
		protected BigMoney getMoney(final FinancialTransaction tx) {
			return tx.getArrearsOfInterest();
		}
	}
	
	public static class ARREARS_OF_PRINCIPAL extends AbstractMoneyFssCellTransformer {

		public ARREARS_OF_PRINCIPAL(final ExchangeRateUtil exchangeRateUtil) {
			super(exchangeRateUtil);
		}

		@Override
		public String getHeaderName() {
			return "Arrears_principal";
		}

		@Override
		protected BigMoney getMoney(final FinancialTransaction tx) {
			return tx.getArrearsOfPrincipal();
		}
	}
	
	public static class OUTSTANDING extends AbstractMoneyFssCellTransformer {

		public OUTSTANDING(final ExchangeRateUtil exchangeRateUtil) {
			super(exchangeRateUtil);
		}

		@Override
		public String getHeaderName() {
			return "Outstanding";
		}

		@Override
		protected BigMoney getMoney(final FinancialTransaction tx) {
			return tx.getPrincipalDisbursedOutstanding();
		}
	}
	
	public static class FUTURE_DS_PRINCIPAL extends AbstractMoneyFssCellTransformer {

		public FUTURE_DS_PRINCIPAL(final ExchangeRateUtil exchangeRateUtil) {
			super(exchangeRateUtil);
		}

		@Override
		public String getHeaderName() {
			return "Future_DS_principal";
		}

		@Override
		protected BigMoney getMoney(final FinancialTransaction tx) {
			final CustomFinancialTransaction ctx	= (CustomFinancialTransaction)tx;
			return ctx.getFutureDebtPrincipal();
		}
	}
	
	public static class FUTURE_DS_INTEREST extends AbstractMoneyFssCellTransformer {

		public FUTURE_DS_INTEREST(final ExchangeRateUtil exchangeRateUtil) {
			super(exchangeRateUtil);
		}

		@Override
		public String getHeaderName() {
			return "Future_DS_interest";
		}

		@Override
		protected BigMoney getMoney(final FinancialTransaction tx) {
			final CustomFinancialTransaction ctx	= (CustomFinancialTransaction)tx;
			return ctx.getFutureDebtInterest();
		}
	}
	
	public static class EXPERT_COMMITMENT extends AbstractMoneyFssCellTransformer {

		public EXPERT_COMMITMENT(final ExchangeRateUtil exchangeRateUtil) {
			super(exchangeRateUtil);
		}

		@Override
		public String getHeaderName() {
			return "Expert_commitment";
		}

		@Override
		protected BigMoney getMoney(final FinancialTransaction tx) {
			return tx.getProjectAmountExpertCommitments();
		}
	}
	public static class EXPERT_EXTENDED extends AbstractMoneyFssCellTransformer {

		public EXPERT_EXTENDED(final ExchangeRateUtil exchangeRateUtil) {
			super(exchangeRateUtil);
		}

		@Override
		public String getHeaderName() {
			return "Expert_extended";
		}

		@Override
		protected BigMoney getMoney(final FinancialTransaction tx) {
			return tx.getProjectAmountExpertExtended();
		}
	}
	
	
}
