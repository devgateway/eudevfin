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
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.devgateway.eudevfin.metadata.common.util.CategoryConstants;
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
			final Integer year = Integer.parseInt( src.toString() );
			final LocalDateTime reportingYear = new LocalDateTime(year, 1, 1, 0, 0);
			ctx.setReportingYear(reportingYear);
			return reportingYear;
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
			final String code = CategoryConstants.NATURE_OF_SUBMISSION_PREFIX + (String) src;
			if ( !StringUtils.isEmpty(code)) {
				final Category natureOfSubmission = servicesWrapper.getCategoryService().
						findByCodeAndClass(code, Category.class, false).getEntity();
				ctx.setNatureOfSubmission(natureOfSubmission);
				return natureOfSubmission;
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

}
