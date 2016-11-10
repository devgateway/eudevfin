/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.common.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import org.devgateway.eudevfin.financial.FileWrapper;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.metadata.common.domain.Area;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

@Entity
@javax.persistence.Table(name = Project.TABLE_NAME)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Project implements Serializable {

    public static final String TABLE_NAME = "PROJECT";
    private static final long serialVersionUID = -2917246983336492166L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id = null;

    protected String name;
    protected String type;

    @Column(name = "implementing_organization")
    protected String implementingOrganization;

    @Column(name = "implementing_partner")
    protected String implementingPartner;

    @Column(length = 3000)
    protected String objectives;
    
    @Column(length = 2048)
    protected String lessonLearn;

    @Column(name = "reporting_email")
    protected String reportingEmail;

    @Column(name = "reporting_details")
    protected String reportingDetails;

    @Column(name = "monitoring_email")
    protected String monitoringEmail;

    @Column(name = "monitoring_details")
    protected String monitoringDetails;

    @Column(name = "impl_constraints", length = 2048)
    protected String implConstraints;

    @Column(length = 2048)
    protected String genderQuality;

    @Column(name = "human_rights", length = 2048)
    protected String humanRights;
    
    @Column(length = 2048)
    protected String environment;

    @Column(name = "hiv_aids", length = 2048)
    protected String hivAids;

    @Column(length = 2048)
    protected String other;
    
    @Column(length = 4096)
    protected String monitoring;
    
    @Column(length = 4096)
    protected String visibility;

    @Column(name = "start_date")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime startDate;

    @Column(name = "stop_date")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime stopDate;
    
    @Column(name = "extend_current_date")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime extendCurrentDate;

    @Column(name = "extend_date")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime extendDate;
    
    @Column(name = "extended_reason", length = 4096)
    protected String extendedReason;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<ProjectFileWrapper> transactionDocumentation;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<ProjectFileWrapper> extendedDocumentation;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<FileWrapper> visibilityDocumentation;

    @ManyToOne
    private Organization extendingAgency;

    @ManyToOne
    private Category sector;
    
    protected String status = "draft";

    @OneToMany(fetch = FetchType.EAGER) //,mappedBy="group")	
    private Set<Area> areas = new HashSet<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProjectResult> projectResults = new LinkedHashSet<>();

    @Transient
    private Set<ProjectResult> tempResults = new LinkedHashSet<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProjectReport> projectReports = new LinkedHashSet<>();

    @Transient
    private Set<ProjectReport> tempReports = new LinkedHashSet<>();

    @ManyToMany(fetch = FetchType.EAGER) //,mappedBy="group")	
    @JoinTable(name = "PROJECT_TRANSACTIONS",
    joinColumns = {
        @JoinColumn(name = "projectId")},
    inverseJoinColumns = {
        @JoinColumn(name = "transactionId")})
    private Set<FinancialTransaction> projectTransactions = new LinkedHashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Organization getExtendingAgency() {
        return extendingAgency;
    }

    public void setExtendingAgency(Organization extendingAgency) {
        this.extendingAgency = extendingAgency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<Area> getAreas() {
        return areas;
    }

    public void setAreas(Set<Area> areas) {
        this.areas = areas;
    }

    public Set<ProjectResult> getProjectResults() {
        return projectResults;
    }

    public Set<ProjectResult> getTempResults() {
        return tempResults;
    }

    public void setProjectResults(Set<ProjectResult> results) {
        this.projectResults = results;
    }

    public Set<ProjectReport> getTempReports() {
        return tempReports;
    }

    public Set<ProjectReport> getProjectReports() {
        return projectReports;
    }

    public void setProjectReports(Set<ProjectReport> reports) {
        this.projectReports = reports;
    }

    public Set<FinancialTransaction> getProjectTransactions() {
        return projectTransactions;
    }

    public void setProjectTransactions(Set<FinancialTransaction> projectTransactions) {
        this.projectTransactions = projectTransactions;
    }

    public Category getSector() {
        return sector;
    }

    public void setSector(Category sector) {
        this.sector = sector;
    }

    public String getObjectives() {
        return objectives;
    }

    public void setObjectives(String objectives) {
        this.objectives = objectives;
    }

    public String getLessonLearn() {
        return lessonLearn;
    }

    public void setLessonLearn(String lessonLearn) {
        this.lessonLearn = lessonLearn;
    }

    public String getImplConstraints() {
        return implConstraints;
    }

    public void setImplConstraints(String implConstraints) {
        this.implConstraints = implConstraints;
    }

    public String getGenderQuality() {
        return genderQuality;
    }

    public void setGenderQuality(String genderQuality) {
        this.genderQuality = genderQuality;
    }

    public String getHumanRights() {
        return humanRights;
    }

    public void setHumanRights(String humanRights) {
        this.humanRights = humanRights;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getHivAids() {
        return hivAids;
    }

    public void setHivAids(String hivAids) {
        this.hivAids = hivAids;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getMonitoring() {
        return monitoring;
    }

    public void setMonitoring(String monitoring) {
        this.monitoring = monitoring;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public Set<FileWrapper> getVisibilityDocumentation() {
        return this.visibilityDocumentation;
    }

    public void setVisibilityDocumentation(final Set<FileWrapper> visibilityDocumentation) {
        if (this.visibilityDocumentation == null) {
            this.visibilityDocumentation = visibilityDocumentation;
        } else {
            this.visibilityDocumentation.retainAll(visibilityDocumentation);
            if (visibilityDocumentation != null) {
                this.visibilityDocumentation.addAll(visibilityDocumentation);
            }
        }
    }
    
    public Set<ProjectFileWrapper> getTransactionDocumentation() {
        return this.transactionDocumentation;
    }

    public void setTransactionDocumentation(final Set<ProjectFileWrapper> transactionDocumentation) {
        if (this.transactionDocumentation == null) {
            this.transactionDocumentation = transactionDocumentation;
        } else {
            this.transactionDocumentation.retainAll(transactionDocumentation);
            if (transactionDocumentation != null) {
                this.transactionDocumentation.addAll(transactionDocumentation);
            }
        }
    }
    
    public Set<ProjectFileWrapper> getExtendedDocumentation() {
        return this.extendedDocumentation;
    }

    public void setExtendedDocumentation(final Set<ProjectFileWrapper> extendedDocumentation) {
        if (this.extendedDocumentation == null) {
            this.extendedDocumentation = extendedDocumentation;
        } else {
            this.extendedDocumentation.retainAll(extendedDocumentation);
            if (extendedDocumentation != null) {
                this.extendedDocumentation.addAll(extendedDocumentation);
            }
        }
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getStopDate() {
        return stopDate;
    }

    public void setStopDate(LocalDateTime stopDate) {
        this.stopDate = stopDate;
    }
    
    public LocalDateTime getExtendCurrentDate() {
        return extendCurrentDate;
    }

    public void setExtendCurrentDate(LocalDateTime extendCurrentDate) {
        this.extendCurrentDate = extendCurrentDate;
    }
    
    public LocalDateTime getExtendDate() {
        return extendDate;
    }

    public void setExtendDate(LocalDateTime extendDate) {
        this.extendDate = extendDate;
    }
    
    public String getExtendedReason() {
        return extendedReason;
    }

    public void setExtendedReason(String extendedReason) {
        this.extendedReason = extendedReason;
    }

    public String getImplementingOrganization() {
        return implementingOrganization;
    }

    public void setImplementingOrganization(String implementingOrganization) {
        this.implementingOrganization = implementingOrganization;
    }

    public String getImplementingPartner() {
        return implementingPartner;
    }

    public void setImplementingPartner(String implementingPartner) {
        this.implementingPartner = implementingPartner;
    }

    public void addResult(ProjectResult result) {
        this.projectResults.add(result);
        this.tempResults.add(result);
    }

    public void addReport(ProjectReport report) {
        this.projectReports.add(report);
        this.tempReports.add(report);
    }

    public void addTransactions(FinancialTransaction transaction) {
        this.projectTransactions.add(transaction);
    }

    public String getReportingEmail() {
        return reportingEmail;
    }

    public void setReportingEmail(String reportingEmail) {
        this.reportingEmail = reportingEmail;
    }

    public String getReportingDetails() {
        return reportingDetails;
    }

    public void setReportingDetails(String reportingDetails) {
        this.reportingDetails = reportingDetails;
    }

    public String getMonitoringEmail() {
        return monitoringEmail;
    }

    public void setMonitoringEmail(String monitoringEmail) {
        this.monitoringEmail = monitoringEmail;
    }

    public String getMonitoringDetails() {
        return monitoringDetails;
    }

    public void setMonitoringDetails(String monitoringDetails) {
        this.monitoringDetails = monitoringDetails;
    }
    
    public void removeTransaction(FinancialTransaction transaction) {
        this.getProjectTransactions().remove(transaction);
        this.setProjectTransactions(projectTransactions);
    }

    @Override
    public String toString() {
        return "\nProject{"
                + "\n   id=" + id
                + "\n   name=" + name
                + "\n   type=" + type
                + "\n   implementingOrganization=" + implementingOrganization
                + "\n   implementingPartner=" + implementingPartner
                + "\n   sector=" + sector
                + "\n   objectives=" + objectives
                + "\n   lessonLearn=" + lessonLearn
                + "\n   implConstraints=" + implConstraints
                + "\n   genderQuality=" + genderQuality
                + "\n   humanRights=" + humanRights
                + "\n   environment=" + environment
                + "\n   hivAids=" + hivAids
                + "\n   other=" + other
                + "\n   monitoring=" + monitoring
                + "\n   visibility=" + visibility
                + "\n   startDate=" + startDate
                + "\n   stopDate=" + stopDate
                + "\n   extendCurrentDate=" + extendCurrentDate
                + "\n   extendDate=" + extendDate
                + "\n   projectResults size=" + projectResults.size()
                + "\n   projectReports size=" + projectReports.size()
                + "\n   projectTransactions size=" + projectTransactions.size()
                + '}';
    }
}
