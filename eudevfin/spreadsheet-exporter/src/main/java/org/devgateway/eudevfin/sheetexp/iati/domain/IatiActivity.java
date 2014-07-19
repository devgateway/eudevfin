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
package org.devgateway.eudevfin.sheetexp.iati.domain;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * @author alexandru-m-g
 *
 */
@XStreamAlias("iati-activity")
public class IatiActivity {
	
	private String title;
	
	private String description;
	
	@XStreamAlias("iati-identifier")
	private String iatiIdentifier;
	
	@XStreamAlias("other-identifier")
	private OtherIdentifier otherIdentifier;
	
	@XStreamAlias("reporting-org")
	private ReportingOrg reportingOrg;
	
	@XStreamAlias("recipient-country")
	private CodeEntity recipientCountry;
	
	private List<ParticipatingOrg> participatingOrgs; 
	
	@XStreamImplicit
	private List<Sector> sectors;
	
	@XStreamAlias("collaboration-type")
	private CodeEntity collaborationType;
	
	@XStreamImplicit
	private List<Location> locations;
	
	@XStreamImplicit
	private List<PolicyMarker> policyMarkers;
	
	@XStreamImplicit
	private List<ActivityDate> activityDates;
	
	@XStreamAlias("crs-add")
	private CrsAdd crsAdd;
	
	@XStreamImplicit
	private List<Transaction> transactions;
	
	@XStreamImplicit
	private List<PlannedDisbursement> plannedDisbursements;

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public String getIatiIdentifier() {
		return this.iatiIdentifier;
	}

	public void setIatiIdentifier(final String iatiIdentifier) {
		this.iatiIdentifier = iatiIdentifier;
	}


	public ReportingOrg getReportingOrg() {
		return this.reportingOrg;
	}

	public void setReportingOrg(final ReportingOrg reportingOrg) {
		this.reportingOrg = reportingOrg;
	}

	public OtherIdentifier getOtherIdentifier() {
		return this.otherIdentifier;
	}

	public void setOtherIdentifier(final OtherIdentifier otherIdentifier) {
		this.otherIdentifier = otherIdentifier;
	}

	public List<Sector> getSectors() {
		return this.sectors;
	}

	public void setSectors(final List<Sector> sectors) {
		this.sectors = sectors;
	}

	public CodeEntity getRecipientCountry() {
		return this.recipientCountry;
	}

	public void setRecipientCountry(final CodeEntity recipientCountry) {
		this.recipientCountry = recipientCountry;
	}

	public List<ParticipatingOrg> getParticipatingOrgs() {
		return this.participatingOrgs;
	}

	public void setParticipatingOrgs(final List<ParticipatingOrg> participatingOrgs) {
		this.participatingOrgs = participatingOrgs;
	}

	public CodeEntity getCollaborationType() {
		return this.collaborationType;
	}

	public void setCollaborationType(final CodeEntity collaborationType) {
		this.collaborationType = collaborationType;
	}

	public List<Location> getLocations() {
		return this.locations;
	}

	public void setLocations(final List<Location> locations) {
		this.locations = locations;
	}

	public List<PolicyMarker> getPolicyMarkers() {
		return this.policyMarkers;
	}

	public void setPolicyMarkers(final List<PolicyMarker> policyMarkers) {
		this.policyMarkers = policyMarkers;
	}

	public List<ActivityDate> getActivityDates() {
		return this.activityDates;
	}

	public void setActivityDates(final List<ActivityDate> activityDates) {
		this.activityDates = activityDates;
	}

	public CrsAdd getCrsAdd() {
		return this.crsAdd;
	}

	public void setCrsAdd(final CrsAdd crsAdd) {
		this.crsAdd = crsAdd;
	}

	public List<Transaction> getTransactions() {
		return this.transactions;
	}

	public void setTransactions(final List<Transaction> transactions) {
		this.transactions = transactions;
	}

	public List<PlannedDisbursement> getPlannedDisbursements() {
		return this.plannedDisbursements;
	}

	public void setPlannedDisbursements(
			final List<PlannedDisbursement> plannedDisbursements) {
		this.plannedDisbursements = plannedDisbursements;
	}


}
