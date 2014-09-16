/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
/**
 *
 */
package org.devgateway.eudevfin.sheetexp.iati.transformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.domain.ChannelCategory;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.devgateway.eudevfin.sheetexp.iati.domain.ActivityDate;
import org.devgateway.eudevfin.sheetexp.iati.domain.CodeEntityWithLanguage;
import org.devgateway.eudevfin.sheetexp.iati.domain.IatiActivity;
import org.devgateway.eudevfin.sheetexp.iati.domain.Location;
import org.devgateway.eudevfin.sheetexp.iati.domain.ParticipatingOrg;
import org.devgateway.eudevfin.sheetexp.iati.domain.ReportingOrg;
import org.devgateway.eudevfin.sheetexp.iati.domain.Sector;
import org.devgateway.eudevfin.sheetexp.iati.domain.StringWithLanguage;
import org.devgateway.eudevfin.sheetexp.iati.transformer.util.Conditions;
import org.joda.time.LocalDateTime;

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
					this.getIatiActivity().setTitle(new StringWithLanguage(title));
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
				final Organization org = this.getCtx().getExtendingAgency();
				final String orgRef = org.getDonorCode() + "-" + org.getAcronym();
				if ( value != null && !"".equals(value.trim()) ) {
					this.getIatiActivity().setIatiIdentifier(orgRef + "-" + value);
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
				final StringWithLanguage originalDescr = this.getIatiActivity().getDescription();
				if (originalDescr == null || originalDescr.getValue() == null) {
					this.getIatiActivity().setDescription(new StringWithLanguage(description));
				}
				else if ( !originalDescr.getValue().contains(description) ) {
					this.getIatiActivity().setDescription(new StringWithLanguage(originalDescr.getValue() + " - " + description) );
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

	public static class ReportingOrganization extends AbstractElementTransformer {

		public ReportingOrganization(final CustomFinancialTransaction ctx,
				final IatiActivity iatiActivity, final Map<String, Object> paramsMap) {
			super(ctx, iatiActivity, paramsMap);
		}

		@Override
		public void process() {
			final Organization org = this.getCtx().getExtendingAgency();
			if (org != null) {
				final String ref = org.getDonorCode() + "-" + org.getAcronym();
				final ReportingOrg existingOrg = this.getIatiActivity().getReportingOrg();
				if ( existingOrg == null || Conditions.SHOULD_OVERWRITE_TRANSACTION(this.getCtx()) ) {
					final ReportingOrg newOrg = new ReportingOrg("10",
							ref, org.getName());
					this.getIatiActivity().setReportingOrg(newOrg);
				}
			}

		}

	}

	public static class ExtendingOrganization extends AbstractElementTransformer {

		public ExtendingOrganization(final CustomFinancialTransaction ctx,
				final IatiActivity iatiActivity, final Map<String, Object> paramsMap) {
			super(ctx, iatiActivity, paramsMap);
		}

		@Override
		public void process() {
			final ParticipatingOrg newParticipatingOrg = this.getParticipatingOrg();
			if (newParticipatingOrg != null) {

				List<ParticipatingOrg> participatingOrgs = this.getIatiActivity().getParticipatingOrgs();
				if ( participatingOrgs == null ) {
					participatingOrgs = new ArrayList<>();
					this.getIatiActivity().setParticipatingOrgs(participatingOrgs);
				}
				boolean found = false;
				for (final ParticipatingOrg currentOrg: participatingOrgs) {
					if( this.getRole().equals(currentOrg.getRole()) ) {
						if ( newParticipatingOrg.getRef().equals(currentOrg.getRef()) ) {
							found = true;
						}
						else if (Conditions.IS_REVISION(this.getCtx())) {
							currentOrg.setRef(newParticipatingOrg.getRef());
							currentOrg.setValue(newParticipatingOrg.getValue());
						}
					}
				}
				if ( !found ) {
					participatingOrgs.add(newParticipatingOrg);
				}

			}

		}

		public String getRole(){
			return "Extending";
		}

		public ParticipatingOrg getParticipatingOrg() {
			final Organization org = this.getCtx().getExtendingAgency();
			if ( org != null) {
				final String ref = org.getDonorCode() + "-" + org.getAcronym();
				return new ParticipatingOrg(this.getRole(),ref, org.getName() );
			}
			return null;
		}

	}

	public static class ImplementingOrganization extends ExtendingOrganization {

		public ImplementingOrganization(final CustomFinancialTransaction ctx,
				final IatiActivity iatiActivity, final Map<String, Object> paramsMap) {
			super(ctx, iatiActivity, paramsMap);
		}

		@Override
		public String getRole() {
			return "Implementing";
		}

		@Override
		public ParticipatingOrg getParticipatingOrg() {
			final ChannelCategory implementingOrg = this.getCtx().getChannel();
			if (implementingOrg != null) {
				return new ParticipatingOrg(this.getRole(),
						implementingOrg.getDisplayableCode(), implementingOrg.getName() );
			}
			return null;

		}


	}

	public static class Sectors extends AbstractElementTransformer {

		public Sectors(final CustomFinancialTransaction ctx,
				final IatiActivity iatiActivity, final Map<String, Object> paramsMap) {
			super(ctx, iatiActivity, paramsMap);
		}

		@Override
		public void process() {
			final Category sectorCateg = this.getCtx().getSector();
			if ( sectorCateg != null ) {
				List<Sector> sectors = this.getIatiActivity().getSectors();
				if (sectors == null) {
					sectors = new ArrayList<>();
					this.getIatiActivity().setSectors(sectors);
				}
				if ( Conditions.IS_REVISION(this.getCtx()) ) {
					sectors.clear();
				}
				boolean found = false;
				for (final Sector tempSector: sectors) {
					if ( "DAC".equals(tempSector.getVocabulary())
							&& sectorCateg.getCode().equals(tempSector.getCode()) ) {
						found = true;
					}
				}
				if (!found) {
					sectors.add(new Sector("DAC", sectorCateg.getCode(), sectorCateg.getName()));
				}
			}

		}

	}

	public static class Collaboration extends AbstractElementTransformer {

		public Collaboration(final CustomFinancialTransaction ctx,
				final IatiActivity iatiActivity, final Map<String, Object> paramsMap) {
			super(ctx, iatiActivity, paramsMap);
		}

		@Override
		public void process() {
			final Category biMultiCateg = this.getCtx().getBiMultilateral();
			if (biMultiCateg != null) {
				final CodeEntityWithLanguage collabType = this.getIatiActivity().getCollaborationType();
				if ( collabType == null || Conditions.IS_REVISION(this.getCtx()) ) {
					this.getIatiActivity().setCollaborationType(
							new CodeEntityWithLanguage(biMultiCateg.getDisplayableCode(), biMultiCateg.getName()) );
				}
			}

		}
	}

	public static class Locations extends AbstractElementTransformer {

		public Locations(final CustomFinancialTransaction ctx,
				final IatiActivity iatiActivity, final Map<String, Object> paramsMap) {
			super(ctx, iatiActivity, paramsMap);
		}

		@Override
		public void process() {
			String locationName = this.getCtx().getGeoTargetArea();
			if ( locationName != null && locationName.trim().length() > 0 ) {
				locationName = locationName.trim();
				List<Location> locations = this.getIatiActivity().getLocations();
				if (locations == null) {
					locations = new ArrayList<>();
					this.getIatiActivity().setLocations(locations);
				}
				if ( Conditions.IS_REVISION(this.getCtx()) ) {
					locations.clear();
				}
				boolean found = false;
				for (final Location tempLocation: locations) {
					if ( tempLocation != null && locationName.equals(tempLocation.getName().getValue()) ) {
						found = true;
					}
				}
				if (!found) {
					locations.add( new Location(new StringWithLanguage(locationName)) );
				}
			}

		}

	}

	public static class ActivityPlannedStartDate extends AbstractElementTransformer {

		public ActivityPlannedStartDate(final CustomFinancialTransaction ctx,
				final IatiActivity iatiActivity, final Map<String, Object> paramsMap) {
			super(ctx, iatiActivity, paramsMap);
		}

		@Override
		public void process() {
			final LocalDateTime startTime = this.getTime();
			if (startTime!= null) {
				List<ActivityDate> dates = this.getIatiActivity().getActivityDates();
				if (dates == null) {
					dates = new ArrayList<>();
					this.getIatiActivity().setActivityDates(dates);
				}
				boolean found = false;
				for(final ActivityDate tempDate: dates) {
					if ( this.getDateType().equals(tempDate.getType()) ) {
						found = true;
						if (Conditions.IS_REVISION(this.getCtx()) ) {
							tempDate.setValue(startTime.toDate());
						}
					}
				}
				if ( !found ) {
					dates.add(new ActivityDate(this.getDateType(), startTime.toDate()) );
				}
			}
		}

		public LocalDateTime getTime() {
			return this.getCtx().getExpectedStartDate();
		}

		public String getDateType() {
			return "start-planned";
		}
	}
	public static class ActivityPlannedEndDate extends ActivityPlannedStartDate {

		public ActivityPlannedEndDate(final CustomFinancialTransaction ctx,
				final IatiActivity iatiActivity, final Map<String, Object> paramsMap) {
			super(ctx, iatiActivity, paramsMap);
		}

		@Override
		public LocalDateTime getTime() {
			return this.getCtx().getExpectedCompletionDate();
		}



		@Override
		public String getDateType() {
			return "end-planned";
		}

	}
}
