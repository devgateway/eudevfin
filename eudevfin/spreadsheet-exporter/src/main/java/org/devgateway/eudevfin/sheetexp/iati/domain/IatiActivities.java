package org.devgateway.eudevfin.sheetexp.iati.domain;

import java.util.Date;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("iati-activities")
public class IatiActivities {
	
	private Date generatedDate;
	
	@XStreamImplicit
	private List<IatiActivity> activities;

	public Date getGeneratedDate() {
		return this.generatedDate;
	}

	public void setGeneratedDate(final Date generatedDate) {
		this.generatedDate = generatedDate;
	}

	public List<IatiActivity> getActivities() {
		return this.activities;
	}

	public void setActivities(final List<IatiActivity> activities) {
		this.activities = activities;
	}
	
	
}
