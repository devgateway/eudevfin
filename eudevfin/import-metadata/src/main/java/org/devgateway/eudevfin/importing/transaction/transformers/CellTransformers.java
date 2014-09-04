/**
 *
 */
package org.devgateway.eudevfin.importing.transaction.transformers;

import java.util.Map;

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.importing.transaction.transformers.TransactionRowTransformer.ServicesWrapper;
import org.devgateway.eudevfin.metadata.common.domain.Area;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.domain.ChannelCategory;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.devgateway.eudevfin.metadata.common.util.CategoryConstants;
import org.devgateway.eudevfin.ui.common.temporary.SB;
import org.joda.time.LocalDateTime;
import org.springframework.util.StringUtils;

/**
 * @author alexandru-m-g
 *
 */
public class CellTransformers {

	public static final String DONOR_CODE = "donor-code";
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

	public static class BiMultilateral implements ICellTransformer<Category> {

		@Override
		public Category populateField(final Object src, final CustomFinancialTransaction ctx,
				final Map<String, Object> context, final ServicesWrapper servicesWrapper) {
			final String code;
			if ( StringUtils.isEmpty(src) ) {
				code = CategoryConstants.BI_MULTILATERAL_PREFIX + (String) src  ;
			}
			else {
				code = CategoryConstants.NATURE_OF_SUBMISSION_PREFIX + (String) src;
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


}
