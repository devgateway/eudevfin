/**
 * 
 */
package org.devgateway.eudevfin.sheetexp.iati.transformer;

import java.util.Map;

import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.sheetexp.iati.domain.IatiActivity;
import org.devgateway.eudevfin.sheetexp.iati.transformer.util.Conditions;

/**
 * @author alexandru-m-g
 *
 */
public class BasicElementTransformers {
	public static class Title extends AbstractElementTransformer {

		public Title(final CustomFinancialTransaction ctx, final IatiActivity iatiActivity,
				final Map<String, Object> paramsMap) {
			super(ctx, iatiActivity, paramsMap);
		}

		@Override
		public void process() {
			if ( this.getIatiActivity().getTitle() == null || 
					( Conditions.SHOULD_OVERWRITE_TRANSACTION(this.getCtx()) &&
					  Conditions.IS_NEWER_OR_SAME_REP_YEAR(this.getCtx(), this.getLatestYear())) ){
				
				final String title	= this.getCtx().getShortDescription();
				if ( title != null && !"".equals(title.trim()) ) {
					this.getIatiActivity().setTitle(title);
				}
			}
		}
		
	}
	public static class IatiIdentifier extends AbstractElementTransformer {

		public IatiIdentifier(final CustomFinancialTransaction ctx, final IatiActivity iatiActivity,
				final Map<String, Object> paramsMap) {
			super(ctx, iatiActivity, paramsMap);
		}

		@Override
		public void process() {
			
			if ( this.getIatiActivity().getIatiIdentifier() == null || 
					( Conditions.SHOULD_OVERWRITE_TRANSACTION(this.getCtx()) &&
					  Conditions.IS_NEWER_OR_SAME_REP_YEAR(this.getCtx(), this.getLatestYear())) ){
			
				final String value	= this.getCtx().getCrsIdentificationNumber();
				if ( value != null && !"".equals(value.trim()) ) {
					this.getIatiActivity().setIatiIdentifier(value);
				}
			}
			
		}
		
	}
	
	public static class Description extends AbstractElementTransformer {

		public Description(final CustomFinancialTransaction ctx, final IatiActivity iatiActivity,
				final Map<String, Object> paramsMap) {
			super(ctx, iatiActivity, paramsMap);
		}

		@Override
		public void process() {
			
			String description = this.getCtx().getDescription();
			
			if ( description != null && description.trim().length() > 0 ) {
				description = description.trim();
				final String originalDescr = this.getIatiActivity().getDescription();
				if (originalDescr == null) {
					this.getIatiActivity().setDescription(description);
				}
				else if ( !originalDescr.contains(description) ) {
					this.getIatiActivity().setDescription(originalDescr + " - " + description );
				}
			}
			
		}
		
	}
	
	public static class GenderEqualityMarker extends AbstractMarkerTransformer {
		public GenderEqualityMarker(final CustomFinancialTransaction ctx,
				final IatiActivity iatiActivity, final Map<String, Object> paramsMap) {
			super(ctx, iatiActivity, paramsMap);
		}

		@Override
		protected String getPolicyMarkerCode() {
			return "1";
		}

		@Override
		protected Category findMarkerCategory() {
			return this.getCtx().getGenderEquality();
		}
		
	}
	
	public static class AidToEnvironmentMarker extends AbstractMarkerTransformer {
		public AidToEnvironmentMarker(final CustomFinancialTransaction ctx,
				final IatiActivity iatiActivity, final Map<String, Object> paramsMap) {
			super(ctx, iatiActivity, paramsMap);
		}

		@Override
		protected String getPolicyMarkerCode() {
			return "2";
		}

		@Override
		protected Category findMarkerCategory() {
			return this.getCtx().getAidToEnvironment();
		}
		
	}
	
	public static class PDGGMarker extends AbstractMarkerTransformer {
		public PDGGMarker(final CustomFinancialTransaction ctx,
				final IatiActivity iatiActivity, final Map<String, Object> paramsMap) {
			super(ctx, iatiActivity, paramsMap);
		}

		@Override
		protected String getPolicyMarkerCode() {
			return "3";
		}

		@Override
		protected Category findMarkerCategory() {
			return this.getCtx().getPdgg();
		}
		
	}
	public static class TradeDevelopmentMarker extends AbstractMarkerTransformer {
		public TradeDevelopmentMarker(final CustomFinancialTransaction ctx,
				final IatiActivity iatiActivity, final Map<String, Object> paramsMap) {
			super(ctx, iatiActivity, paramsMap);
		}

		@Override
		protected String getPolicyMarkerCode() {
			return "4";
		}

		@Override
		protected Category findMarkerCategory() {
			return this.getCtx().getTradeDevelopment();
		}
		
	}
	public static class BiodiversityMarker extends AbstractMarkerTransformer {
		public BiodiversityMarker(final CustomFinancialTransaction ctx,
				final IatiActivity iatiActivity, final Map<String, Object> paramsMap) {
			super(ctx, iatiActivity, paramsMap);
		}

		@Override
		protected String getPolicyMarkerCode() {
			return "5";
		}

		@Override
		protected Category findMarkerCategory() {
			return this.getCtx().getBiodiversity();
		}
		
	}
	
	public static class ClimateMitigationMarker extends AbstractMarkerTransformer {
		public ClimateMitigationMarker(final CustomFinancialTransaction ctx,
				final IatiActivity iatiActivity, final Map<String, Object> paramsMap) {
			super(ctx, iatiActivity, paramsMap);
		}

		@Override
		protected String getPolicyMarkerCode() {
			return "6";
		}

		@Override
		protected Category findMarkerCategory() {
			return this.getCtx().getClimateChangeMitigation();
		}
	}
	public static class ClimateAdaptationMarker extends AbstractMarkerTransformer {
		public ClimateAdaptationMarker(final CustomFinancialTransaction ctx,
				final IatiActivity iatiActivity, final Map<String, Object> paramsMap) {
			super(ctx, iatiActivity, paramsMap);
		}

		@Override
		protected String getPolicyMarkerCode() {
			return "7";
		}

		@Override
		protected Category findMarkerCategory() {
			return this.getCtx().getClimateChangeAdaptation();
		}
		
	}
	public static class DesertificationMarker extends AbstractMarkerTransformer {
		public DesertificationMarker(final CustomFinancialTransaction ctx,
				final IatiActivity iatiActivity, final Map<String, Object> paramsMap) {
			super(ctx, iatiActivity, paramsMap);
		}

		@Override
		protected String getPolicyMarkerCode() {
			return "8";
		}

		@Override
		protected Category findMarkerCategory() {
			return this.getCtx().getDesertification();
		}
		
	}
	
	public static class FreestandingTechnicalCoop extends AbstractAidTypeFlagTransformer {

		public FreestandingTechnicalCoop(final CustomFinancialTransaction ctx,
				final IatiActivity iatiActivity, final Map<String, Object> paramsMap) {
			super(ctx, iatiActivity, paramsMap);
		}

		@Override
		protected Boolean getAidtypeFlag() {
			return this.getCtx().getFreestandingTechnicalCooperation();
		}

		@Override
		protected String getFlagCode() {
			return "1";
		}
		
	}
	
	public static class ProgramBasedApproach extends AbstractAidTypeFlagTransformer {

		public ProgramBasedApproach(final CustomFinancialTransaction ctx,
				final IatiActivity iatiActivity, final Map<String, Object> paramsMap) {
			super(ctx, iatiActivity, paramsMap);
		}

		@Override
		protected Boolean getAidtypeFlag() {
			return this.getCtx().getProgrammeBasedApproach();
		}

		@Override
		protected String getFlagCode() {
			return "2";
		}
		
	}
	
	public static class InvestmentProject extends AbstractAidTypeFlagTransformer {

		public InvestmentProject(final CustomFinancialTransaction ctx,
				final IatiActivity iatiActivity, final Map<String, Object> paramsMap) {
			super(ctx, iatiActivity, paramsMap);
		}

		@Override
		protected Boolean getAidtypeFlag() {
			return this.getCtx().getInvestment();
		}

		@Override
		protected String getFlagCode() {
			return "3";
		}
		
	}
	
	public static class AssociatedFinancing extends AbstractAidTypeFlagTransformer {

		public AssociatedFinancing(final CustomFinancialTransaction ctx,
				final IatiActivity iatiActivity, final Map<String, Object> paramsMap) {
			super(ctx, iatiActivity, paramsMap);
		}

		@Override
		protected Boolean getAidtypeFlag() {
			return this.getCtx().getAssociatedFinancing();
		}

		@Override
		protected String getFlagCode() {
			return "4";
		}
		
	}
	
	
	
}
