/**
 * 
 */
package org.devgateway.eudevfin.sheetexp.iati.transformer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.sheetexp.iati.domain.IatiActivity;

/**
 * @author alexandru-m-g
 *
 */
public class IatiActivityEngine {
	private static Logger logger = Logger.getLogger(IatiActivityEngine.class);
	
	private List<AbstractElementTransformer> elementTransformers; 
	
	private final IatiActivity iatiActivity;
	private final boolean newActivity;

	private final CustomFinancialTransaction ctx;
	private final int latestYear;
	
	
	public IatiActivityEngine(final CustomFinancialTransaction ctx) {
		this.iatiActivity = new IatiActivity();
		this.ctx = ctx;
		this.newActivity = true;
		this.latestYear = 0;
		
		this.initElementTransformers(this.ctx, this.iatiActivity);
	}

	public IatiActivityEngine(final CustomFinancialTransaction ctx, final IatiActivity currentActivity, final int latestYear) {
		this.iatiActivity = currentActivity;
		this.ctx = ctx;
		this.newActivity = false;
		this.latestYear = latestYear;
		
		this.initElementTransformers(this.ctx, this.iatiActivity);
	}
	
	private void initElementTransformers(final CustomFinancialTransaction ctx, final IatiActivity iatiActivity) {
		final Map<String, Object> params = new HashMap<>();
		params.put(AbstractElementTransformer.LATEST_YEAR, this.latestYear);
		final AbstractElementTransformer[] elementTransformers = {
				new BasicElementTransformers.IatiIdentifier(ctx, iatiActivity, params),
				new BasicElementTransformers.Title(ctx, iatiActivity, params),
				new BasicElementTransformers.Description(ctx, iatiActivity, params),
				new PlannedDisbursementTransformer(ctx, iatiActivity, params),
				new DisbursementTransactionTransformer(ctx, iatiActivity, params),
				new CommitmentTransactionTransformer(ctx, iatiActivity, params),
				new TiedCommitmentTransactionTransformer(ctx, iatiActivity, params),
				new UntiedCommitmentTransactionTransformer(ctx, iatiActivity, params),
				new PartiallyTiedCommitmentTransactionTransformer(ctx, iatiActivity, params),
				new BasicElementTransformers.GenderEqualityMarker(ctx, iatiActivity, params),
				new BasicElementTransformers.AidToEnvironmentMarker(ctx, iatiActivity, params),
				new BasicElementTransformers.PDGGMarker(ctx, iatiActivity, params),
				new BasicElementTransformers.TradeDevelopmentMarker(ctx, iatiActivity, params),
				new BasicElementTransformers.BiodiversityMarker(ctx, iatiActivity, params),
				new BasicElementTransformers.ClimateMitigationMarker(ctx, iatiActivity, params),
				new BasicElementTransformers.ClimateAdaptationMarker(ctx, iatiActivity, params),
				new BasicElementTransformers.DesertificationMarker(ctx, iatiActivity, params),
				new BasicElementTransformers.FreestandingTechnicalCoop(ctx, iatiActivity, params),
				new BasicElementTransformers.ProgramBasedApproach(ctx, iatiActivity, params),
				new BasicElementTransformers.InvestmentProject(ctx, iatiActivity, params),
				new BasicElementTransformers.AssociatedFinancing(ctx, iatiActivity, params),
				new BasicElementTransformers.ReportingOrganization(ctx, iatiActivity, params),
				new BasicElementTransformers.ExtendingOrganization(ctx, iatiActivity, params),
				new BasicElementTransformers.ImplementingOrganization(ctx, iatiActivity, params),
				new BasicElementTransformers.Sectors(ctx, iatiActivity, params),
				new BasicElementTransformers.Collaboration(ctx, iatiActivity, params),
				new BasicElementTransformers.Locations(ctx, iatiActivity, params),
				new BasicElementTransformers.ActivityPlannedStartDate(ctx, iatiActivity, params),
				new BasicElementTransformers.ActivityPlannedEndDate(ctx, iatiActivity, params)
		};
		this.elementTransformers = Arrays.asList(elementTransformers);
	}

	public IatiActivityEngine process() {
		for (final AbstractElementTransformer elementTransformer: this.elementTransformers) {
//			logger.info("Using element transformer " + elementTransformer.getClass().getName() );
			elementTransformer.process();
		}
		return this;
	}

	public IatiActivity getIatiActivity() {
		return this.iatiActivity;
	}
	

}
