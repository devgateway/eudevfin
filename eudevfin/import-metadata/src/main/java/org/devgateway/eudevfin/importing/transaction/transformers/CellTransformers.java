/**
 *
 */
package org.devgateway.eudevfin.importing.transaction.transformers;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.importing.transaction.transformers.TransactionRowTransformer.ServicesWrapper;
import org.devgateway.eudevfin.metadata.common.domain.Area;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.domain.ChannelCategory;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.devgateway.eudevfin.metadata.common.domain.SectorCategory;
import org.devgateway.eudevfin.metadata.common.util.CategoryConstants;
import org.devgateway.eudevfin.ui.common.temporary.SB;
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;
import org.joda.time.LocalDateTime;
import org.springframework.util.StringUtils;

/**
 * @author alexandru-m-g
 *
 */
public class CellTransformers {

	public static final String DONOR_CODE 		= "donor-code";
	public static final String CURRENCY_UNIT 	= "currency-unit";


	public static Logger logger = Logger.getLogger(CellTransformers.class);

	public static class CommitmentDate implements ICellTransformer<LocalDateTime> {

		@Override
		public LocalDateTime populateField(final Object src, final CustomFinancialTransaction ctx
				, final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			//			final LocalDateTime srcDate = LocalDateTime.fromDateFields( (Date)src );
			final LocalDateTime srcDate = (LocalDateTime) src;
			ctx.setCommitmentDate(srcDate);
			return srcDate;
		}

	}

	public static class ExpectedStartDate implements ICellTransformer<LocalDateTime> {

		@Override
		public LocalDateTime populateField(final Object src, final CustomFinancialTransaction ctx
				, final Map<String, Object> context, final ServicesWrapper servicesWrapper) {

			final LocalDateTime expectedStartDate = (LocalDateTime) src;
			ctx.setExpectedStartDate(expectedStartDate);
			return expectedStartDate;
		}

	}

	public static class ExpectedCommpletionDate implements ICellTransformer<LocalDateTime> {

		@Override
		public LocalDateTime populateField(final Object src, final CustomFinancialTransaction ctx
				, final Map<String, Object> context, final ServicesWrapper servicesWrapper) {

			final LocalDateTime expectedCompletionDate = (LocalDateTime) src;
			ctx.setExpectedCompletionDate(expectedCompletionDate);
			return expectedCompletionDate;
		}

	}

	public static class FirstRepaymentDate implements ICellTransformer<LocalDateTime> {

		@Override
		public LocalDateTime populateField(final Object src, final CustomFinancialTransaction ctx
				, final Map<String, Object> context, final ServicesWrapper servicesWrapper) {

			final LocalDateTime date= (LocalDateTime) src;
			ctx.setFirstRepaymentDate(date);
			return date;
		}

	}

	public static class FinalRepaymentDate implements ICellTransformer<LocalDateTime> {

		@Override
		public LocalDateTime populateField(final Object src, final CustomFinancialTransaction ctx
				, final Map<String, Object> context, final ServicesWrapper servicesWrapper) {

			final LocalDateTime date= (LocalDateTime) src;
			ctx.setFinalRepaymentDate(date);
			return date;
		}

	}

	public static class ReportingYear implements ICellTransformer<LocalDateTime> {

		@Override
		public LocalDateTime populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			if ( src != null && src.toString().length() > 0 ) {
				final Integer year = Integer.parseInt( src.toString() );
				final LocalDateTime reportingYear = new LocalDateTime(year, 1, 1, 0, 0);
				ctx.setReportingYear(reportingYear);
				return reportingYear;
			}
			return null;
		}

	}

	public static class DonorCode implements ICellTransformer<String> {

		@Override
		public String populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			final String donorCode = (String) src;
			context.put(DONOR_CODE, donorCode);
			return donorCode;
		}

	}

	public static class ExtendingAgency implements ICellTransformer<Organization> {

		@Override
		public Organization populateField(final Object src,	final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			final String code = (String) src;
			final String donorCode = (String) context.get(DONOR_CODE);
			if ( !StringUtils.isEmpty(code) && !StringUtils.isEmpty(donorCode) ) {
				final Organization o = servicesWrapper.getOrgService().findByCodeAndDonorCode(code, donorCode).getEntity();
				ctx.setExtendingAgency(o);
				return o;
			}
			return null;
		}

	}

	public static class CrsId implements ICellTransformer<String> {

		@Override
		public String populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			final String crsId = (String) src;
			ctx.setCrsIdentificationNumber(crsId);
			return crsId;
		}

	}

	public static class DonorProjectNumber implements ICellTransformer<String> {

		@Override
		public String populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			final String donorProjectNumber = (String) src;
			ctx.setDonorProjectNumber(donorProjectNumber);
			return donorProjectNumber;
		}

	}

	public static class NatureOfSubmission implements ICellTransformer<Category> {

		@Override
		public Category populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			if ( !StringUtils.isEmpty(src)) {
				final String code = CategoryConstants.NATURE_OF_SUBMISSION_PREFIX + (String) src;
				final Category natureOfSubmission = servicesWrapper.getCategoryService().
						findByCodeAndClass(code, Category.class, false).getEntity();
				ctx.setNatureOfSubmission(natureOfSubmission);
				return natureOfSubmission;
			}
			return null;
		}

	}

	public static class TypeOfFlow implements ICellTransformer<Category> {

		@Override
		public Category populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			if ( !StringUtils.isEmpty(src)) {
				final String code = CategoryConstants.TYPE_OF_FLOW_PREFIX + (String) src;
				final Category typeOfFlow = servicesWrapper.getCategoryService().
						findByCodeAndClass(code, Category.class, false).getEntity();
				ctx.setTypeOfFlow(typeOfFlow);
				return typeOfFlow;
			}
			return null;
		}

	}

	public static class TypeOfFinance implements ICellTransformer<Category> {

		@Override
		public Category populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			if ( !StringUtils.isEmpty(src)) {
				final String code = CategoryConstants.TYPE_OF_FINANCE_PREFIX + (String) src;
				final Category typeOfFinance = servicesWrapper.getCategoryService().
						findByCodeAndClass(code, Category.class, false).getEntity();
				ctx.setTypeOfFinance(typeOfFinance);
				return typeOfFinance;
			}
			return null;
		}

	}

	public static class TypeOfAid implements ICellTransformer<Category> {

		@Override
		public Category populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			if ( !StringUtils.isEmpty(src)) {
				final String code = (String) src;
				final Category typeOfAid = servicesWrapper.getCategoryService().
						findByCodeAndClass(code, Category.class, false).getEntity();
				ctx.setTypeOfAid(typeOfAid);
				return typeOfAid;
			}
			return null;
		}

	}

	public static class Sectors implements ICellTransformer<Category> {

		@Override
		public Category populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			if ( !StringUtils.isEmpty(src)) {
				final String code = (String) src;
				final Category sector = servicesWrapper.getCategoryService().
						findByCodeAndClass(code, SectorCategory.class, false).getEntity();
				ctx.setSector(sector);
				return sector;
			}
			return null;
		}

	}

	public static class GenderEquality implements ICellTransformer<Category> {

		@Override
		public Category populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			if ( !StringUtils.isEmpty(src)) {
				final String code = CategoryConstants.MARKER_PREFIX + (String) src;
				final Category genderEquality = servicesWrapper.getCategoryService().
						findByCodeAndClass(code, Category.class, false).getEntity();
				ctx.setGenderEquality(genderEquality);
				return genderEquality;
			}
			return null;
		}

	}

	public static class AidToEnvironment implements ICellTransformer<Category> {

		@Override
		public Category populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			if ( !StringUtils.isEmpty(src)) {
				final String code = CategoryConstants.MARKER_PREFIX + (String) src;
				final Category aidToEnvironment = servicesWrapper.getCategoryService().
						findByCodeAndClass(code, Category.class, false).getEntity();
				ctx.setAidToEnvironment(aidToEnvironment);
				return aidToEnvironment;
			}
			return null;
		}

	}

	public static class Pdgg implements ICellTransformer<Category> {

		@Override
		public Category populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			if ( !StringUtils.isEmpty(src)) {
				final String code = CategoryConstants.MARKER_PREFIX + (String) src;
				final Category pdgg = servicesWrapper.getCategoryService().
						findByCodeAndClass(code, Category.class, false).getEntity();
				ctx.setPdgg(pdgg);
				return pdgg;
			}
			return null;
		}

	}

	public static class TradeDevelopment implements ICellTransformer<Category> {

		@Override
		public Category populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			if ( !StringUtils.isEmpty(src)) {
				final String code = CategoryConstants.MARKER_PREFIX + (String) src;
				final Category tradeDevelopment = servicesWrapper.getCategoryService().
						findByCodeAndClass(code, Category.class, false).getEntity();
				ctx.setTradeDevelopment(tradeDevelopment);
				return tradeDevelopment;
			}
			return null;
		}

	}

	public static class Biodiversity implements ICellTransformer<Category> {

		@Override
		public Category populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			if ( !StringUtils.isEmpty(src)) {
				final String code = CategoryConstants.MARKER_PREFIX + (String) src;
				final Category biodiversity = servicesWrapper.getCategoryService().
						findByCodeAndClass(code, Category.class, false).getEntity();
				ctx.setBiodiversity(biodiversity);
				return biodiversity;
			}
			return null;
		}

	}

	public static class ClimateChangeMitigation implements ICellTransformer<Category> {

		@Override
		public Category populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			if ( !StringUtils.isEmpty(src)) {
				final String code = CategoryConstants.MARKER_PREFIX + (String) src;
				final Category climateChangeMitigation = servicesWrapper.getCategoryService().
						findByCodeAndClass(code, Category.class, false).getEntity();
				ctx.setClimateChangeMitigation(climateChangeMitigation);
				return climateChangeMitigation;
			}
			return null;
		}

	}

	public static class ClimateChangeAdaptation implements ICellTransformer<Category> {

		@Override
		public Category populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			if ( !StringUtils.isEmpty(src)) {
				final String code = CategoryConstants.MARKER_PREFIX + (String) src;
				final Category climateChangeAdaptation = servicesWrapper.getCategoryService().
						findByCodeAndClass(code, Category.class, false).getEntity();
				ctx.setClimateChangeAdaptation(climateChangeAdaptation);
				return climateChangeAdaptation;
			}
			return null;
		}

	}

	public static class Desertification implements ICellTransformer<Category> {

		@Override
		public Category populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			if ( !StringUtils.isEmpty(src)) {
				final String code = CategoryConstants.MARKER_PREFIX + (String) src;
				final Category desertification = servicesWrapper.getCategoryService().
						findByCodeAndClass(code, Category.class, false).getEntity();
				ctx.setDesertification(desertification);
				return desertification;
			}
			return null;
		}

	}

	public static class TypeOfRepayment implements ICellTransformer<Category> {

		@Override
		public Category populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			if ( !StringUtils.isEmpty(src)) {
				final String code = CategoryConstants.TYPE_OF_REPAYMENT_PREFIX + (String) src;
				final Category typeOfRepayment = servicesWrapper.getCategoryService().
						findByCodeAndClass(code, Category.class, false).getEntity();
				ctx.setTypeOfRepayment(typeOfRepayment);
				return typeOfRepayment;
			}
			return null;
		}

	}   

	public static class NumberOfRepayments implements ICellTransformer<Category> {

		@Override
		public Category populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			if ( !StringUtils.isEmpty(src)) {
				final String code = CategoryConstants.NUM_OF_REPAYMENTS_PER_ANNUM_PREFIX + (String) src;
				final Category numberOfRepaymentsAnnum = servicesWrapper.getCategoryService().
						findByCodeAndClass(code, Category.class, false).getEntity();
				ctx.setNumberOfRepaymentsAnnum(numberOfRepaymentsAnnum);
				return numberOfRepaymentsAnnum;
			}
			return null;
		}

	}

	public static class Rmnch implements ICellTransformer<Category> {

		@Override
		public Category populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			if ( !StringUtils.isEmpty(src)) {
				final String code = CategoryConstants.RMNCH_PREFIX + (String) src;
				final Category rmnch = servicesWrapper.getCategoryService().
						findByCodeAndClass(code, Category.class, false).getEntity();
				ctx.setRmnch(rmnch);
				return rmnch;
			}
			return null;
		}

	}

	public static class InterestRate implements ICellTransformer<BigDecimal> {

		@Override
		public BigDecimal populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			if ( !StringUtils.isEmpty(src)) {
				final BigDecimal rate = new BigDecimal(Double.parseDouble((String)src));
				ctx.setInterestRate(rate);
				return rate;
			}
			return null;
		}

	}

	public static class SecondInterestRate implements ICellTransformer<BigDecimal> {

		@Override
		public BigDecimal populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			if ( !StringUtils.isEmpty(src)) {
				final BigDecimal rate = new BigDecimal(Double.parseDouble((String)src));
				ctx.setSecondInterestRate(rate);
				return rate;
			}
			return null;
		}

	}

	public static class FreestandingTechnicalCooperation implements ICellTransformer<Boolean> {

		@Override
		public Boolean populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			if ( !StringUtils.isEmpty(src)) {
				Boolean ftc = null;
				if ("1".equals(src) ) {
					ftc = true;
				} else if ("0".equals(src) ) {
					ftc = false;
				}
				ctx.setFreestandingTechnicalCooperation(ftc);
				return ftc;
			}
			return null;
		}

	}

	public static class ProgrammeBasedApproach implements ICellTransformer<Boolean> {

		@Override
		public Boolean populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			if ( !StringUtils.isEmpty(src)) {
				Boolean pba = null;
				if ("1".equals(src) ) {
					pba = true;
				} else if ("0".equals(src) ) {
					pba = false;
				}
				ctx.setProgrammeBasedApproach(pba);
				return pba;
			}
			return null;
		}

	}

	public static class InvestmentProject implements ICellTransformer<Boolean> {

		@Override
		public Boolean populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			if ( !StringUtils.isEmpty(src)) {
				Boolean investment = null;
				if ("1".equals(src) ) {
					investment = true;
				} else if ("0".equals(src) ) {
					investment = false;
				}
				ctx.setInvestment(investment);
				return investment;
			}
			return null;
		}

	}

	public static class AssociatedFinancing implements ICellTransformer<Boolean> {

		@Override
		public Boolean populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			if ( !StringUtils.isEmpty(src)) {
				Boolean af = null;
				if ("1".equals(src) ) {
					af = true;
				} else if ("0".equals(src) ) {
					af = false;
				}
				ctx.setAssociatedFinancing(af);
				return af;
			}
			return null;
		}

	}


	public static class Channel implements ICellTransformer<ChannelCategory> {

		@Override
		public ChannelCategory populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {

			if ( !StringUtils.isEmpty(src)) {
				final String code = (String) src;
				final ChannelCategory channel = servicesWrapper.getCategoryService().
						findByCodeAndClass(code, ChannelCategory.class, false).getEntity();
				ctx.setChannel(channel);
				return channel;
			}
			return null;
		}

	}

	public static class ChannelInstituteName implements ICellTransformer<String> {

		@Override
		public String populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {

			if ( !StringUtils.isEmpty(src)) {
				final String name = (String) src;
				ctx.setChannelInstitutionName(name);
				return name;
			}
			return null;
		}

	}

	public static class RecipientCountry implements ICellTransformer<Area> {

		@Override
		public Area populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			final String code = (String) src;
			if ( !StringUtils.isEmpty(code)) {
				final Area recipient = servicesWrapper.getAreaService().findAreaByCode(code).getEntity();
				ctx.setRecipient(recipient);
				return recipient;
			}
			return null;
		}

	}

	public static class ShortDescription implements ICellTransformer<String> {

		@Override
		public String populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			final String shortDescription = (String) src;
			ctx.setShortDescription(shortDescription);
			return shortDescription;
		}

	}

	public static class Description implements ICellTransformer<String> {

		@Override
		public String populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			final String description = (String) src;
			ctx.setDescription(description);
			return description;
		}

	}

	public static class GeographicalTargetArea implements ICellTransformer<String> {

		@Override
		public String populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			final String geoTargetArea = (String) src;
			ctx.setGeoTargetArea(geoTargetArea);
			return geoTargetArea;
		}

	}

	public static class BiMultilateral implements ICellTransformer<Category> {

		@Override
		public Category populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			final String code;
			if ( StringUtils.isEmpty(src) ) {
				code = CategoryConstants.BI_MULTILATERAL_PREFIX + 1  ;
			}
			else {
				code = CategoryConstants.BI_MULTILATERAL_PREFIX + (String) src;
			}
			final Category biMultilateral = servicesWrapper.getCategoryService().findByCodeAndClass(code, Category.class, false).getEntity();
			ctx.setBiMultilateral(biMultilateral);

			if ( CategoryConstants.BiMultilateral.BI_MULTILATERAL_2.equals(code)
					|| CategoryConstants.BiMultilateral.BI_MULTILATERAL_4.equals(code) ) {
				ctx.setFormType(SB.MULTILATERAL_ODA_CRS);
			}
			else {
				ctx.setFormType(SB.BILATERAL_ODA_CRS);
			}
			return biMultilateral;
		}
	}

	public static class CurrencyCode implements ICellTransformer<CurrencyUnit> {

		@Override
		public CurrencyUnit populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			final String currencyCode = (String) src;
			if ( !StringUtils.isEmpty(currencyCode) ){
				final CurrencyUnit currencyUnit = CurrencyUnit.ofNumericCode(Integer.parseInt(currencyCode));

				context.put(CURRENCY_UNIT, currencyUnit);
				ctx.setCurrency(currencyUnit);
				return currencyUnit;
			} else {
				throw new IllegalArgumentException("Currency code is mandatory");
			}

		}

	}

	public static class Commitments implements ICellTransformer<BigMoney> {

		@Override
		public BigMoney populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			final String amount = (String) src;
			if ( !StringUtils.isEmpty(amount) ){
				final CurrencyUnit currencyUnit = (CurrencyUnit) context.get(CURRENCY_UNIT);
				final BigMoney money = BigMoney.of(currencyUnit, Double.parseDouble(amount));
				ctx.setCommitments(money);
				return money;
			}

			return null;
		}

	}

	public static class AmountsExtended implements ICellTransformer<BigMoney> {

		@Override
		public BigMoney populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			final String amount = (String) src;
			if ( !StringUtils.isEmpty(amount) ){
				final CurrencyUnit currencyUnit = (CurrencyUnit) context.get(CURRENCY_UNIT);
				final BigMoney money = BigMoney.of(currencyUnit, Double.parseDouble(amount));
				ctx.setAmountsExtended(money);
				return money;
			}

			return null;
		}

	}

	public static class AmountsReceived implements ICellTransformer<BigMoney> {

		@Override
		public BigMoney populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			final String amount = (String) src;
			if ( !StringUtils.isEmpty(amount) ){
				final CurrencyUnit currencyUnit = (CurrencyUnit) context.get(CURRENCY_UNIT);
				final BigMoney money = BigMoney.of(currencyUnit, Double.parseDouble(amount));
				ctx.setAmountsReceived(money);
				return money;
			}

			return null;
		}

	}

	public static class AmountsUntied implements ICellTransformer<BigMoney> {

		@Override
		public BigMoney populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			final String amount = (String) src;
			if ( !StringUtils.isEmpty(amount) ){
				final CurrencyUnit currencyUnit = (CurrencyUnit) context.get(CURRENCY_UNIT);
				final BigMoney money = BigMoney.of(currencyUnit, Double.parseDouble(amount));
				ctx.setAmountsUntied(money);
				return money;
			}

			return null;
		}

	}

	public static class AmountsPartiallyUntied implements ICellTransformer<BigMoney> {

		@Override
		public BigMoney populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			final String amount = (String) src;
			if ( !StringUtils.isEmpty(amount) ){
				final CurrencyUnit currencyUnit = (CurrencyUnit) context.get(CURRENCY_UNIT);
				final BigMoney money = BigMoney.of(currencyUnit, Double.parseDouble(amount));
				ctx.setAmountsPartiallyUntied(money);
				return money;
			}

			return null;
		}

	}

	public static class AmountsTied implements ICellTransformer<BigMoney> {

		@Override
		public BigMoney populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			final String amount = (String) src;
			if ( !StringUtils.isEmpty(amount) ){
				final CurrencyUnit currencyUnit = (CurrencyUnit) context.get(CURRENCY_UNIT);
				final BigMoney money = BigMoney.of(currencyUnit, Double.parseDouble(amount));
				ctx.setAmountsTied(money);
				return money;
			}

			return null;
		}

	}

	public static class AmountOfIrtc implements ICellTransformer<BigMoney> {

		@Override
		public BigMoney populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			final String amount = (String) src;
			if ( !StringUtils.isEmpty(amount) ){
				final CurrencyUnit currencyUnit = (CurrencyUnit) context.get(CURRENCY_UNIT);
				final BigMoney money = BigMoney.of(currencyUnit, Double.parseDouble(amount));
				ctx.setAmountOfIRTC(money);
				return money;
			}

			return null;
		}

	}

	public static class AmountsOfExpertCommitment implements ICellTransformer<BigMoney> {

		@Override
		public BigMoney populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			final String amount = (String) src;
			if ( !StringUtils.isEmpty(amount) ){
				final CurrencyUnit currencyUnit = (CurrencyUnit) context.get(CURRENCY_UNIT);
				final BigMoney money = BigMoney.of(currencyUnit, Double.parseDouble(amount));
				ctx.setProjectAmountExpertCommitments(money);
				return money;
			}

			return null;
		}

	}

	public static class AmountsOfExpertExtended implements ICellTransformer<BigMoney> {

		@Override
		public BigMoney populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			final String amount = (String) src;
			if ( !StringUtils.isEmpty(amount) ){
				final CurrencyUnit currencyUnit = (CurrencyUnit) context.get(CURRENCY_UNIT);
				final BigMoney money = BigMoney.of(currencyUnit, Double.parseDouble(amount));
				ctx.setProjectAmountExpertExtended(money);
				return money;
			}

			return null;
		}

	}

	public static class AmountsOfExportCredit implements ICellTransformer<BigMoney> {

		@Override
		public BigMoney populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			final String amount = (String) src;
			if ( !StringUtils.isEmpty(amount) ){
				final CurrencyUnit currencyUnit = (CurrencyUnit) context.get(CURRENCY_UNIT);
				final BigMoney money = BigMoney.of(currencyUnit, Double.parseDouble(amount));
				ctx.setAmountOfExportCreditInAFPackage(money);
				return money;
			}

			return null;
		}

	}

	public static class InterestReceived implements ICellTransformer<BigMoney> {

		@Override
		public BigMoney populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			final String amount = (String) src;
			if ( !StringUtils.isEmpty(amount) ){
				final CurrencyUnit currencyUnit = (CurrencyUnit) context.get(CURRENCY_UNIT);
				final BigMoney money = BigMoney.of(currencyUnit, Double.parseDouble(amount));
				ctx.setInterestReceived(money);
				return money;
			}

			return null;
		}

	}

	public static class PrincipalDisbursdeOutstanding implements ICellTransformer<BigMoney> {

		@Override
		public BigMoney populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			final String amount = (String) src;
			if ( !StringUtils.isEmpty(amount) ){
				final CurrencyUnit currencyUnit = (CurrencyUnit) context.get(CURRENCY_UNIT);
				final BigMoney money = BigMoney.of(currencyUnit, Double.parseDouble(amount));
				ctx.setPrincipalDisbursedOutstanding(money);;
				return money;
			}

			return null;
		}

	}

	public static class ArrearsOfPrincipal implements ICellTransformer<BigMoney> {

		@Override
		public BigMoney populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			final String amount = (String) src;
			if ( !StringUtils.isEmpty(amount) ){
				final CurrencyUnit currencyUnit = (CurrencyUnit) context.get(CURRENCY_UNIT);
				final BigMoney money = BigMoney.of(currencyUnit, Double.parseDouble(amount));
				ctx.setArrearsOfPrincipal(money);
				return money;
			}

			return null;
		}

	}

	public static class ArrearsOfInterest implements ICellTransformer<BigMoney> {

		@Override
		public BigMoney populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			final String amount = (String) src;
			if ( !StringUtils.isEmpty(amount) ){
				final CurrencyUnit currencyUnit = (CurrencyUnit) context.get(CURRENCY_UNIT);
				final BigMoney money = BigMoney.of(currencyUnit, Double.parseDouble(amount));
				ctx.setArrearsOfInterest(money);
				return money;
			}

			return null;
		}

	}


}
