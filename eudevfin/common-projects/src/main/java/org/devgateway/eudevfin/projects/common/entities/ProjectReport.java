/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.common.entities;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.devgateway.eudevfin.financial.FileWrapper;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

/**
 *
 * @author alcr
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProjectReport implements Serializable {

    private static final long serialVersionUID = -2917246983336492166L;

    @ManyToOne
    @JoinColumn(name = "project_id")
    public Project project;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id = null;

    @Column(name = "file_provided")
    protected boolean fileProvided;

    protected String type;
    
    @Column(name = "report_title")
    protected String reportTitle;

    @Column(name = "email_sent")
    protected boolean emailSent;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime reportDate;
    
    @Column(name = "reporting_period_start")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime reportingPeriodStart;
    
    @Column(name = "reporting_period_end")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime reportingPeriodEnd;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<FileWrapper> reportFiles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public boolean getFileProvided() {
        return fileProvided;
    }

    public void setFileProvided(boolean fileProvided) {
        this.fileProvided = fileProvided;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
     public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String title) {
        this.reportTitle = title;
    }

    public LocalDateTime getReportDate() {
        return reportDate;
    }

    public LocalDate getFormattedReportDate() {      
        return reportDate.toLocalDate();
    }

    public void setReportDate(LocalDateTime reportDate) {
        this.reportDate = reportDate;
    }
    
    public LocalDateTime getReportingPeriodStart() {
        return reportingPeriodStart;
    }

    public LocalDate getFormattedReportingPeriodStart() {
        return reportingPeriodStart.toLocalDate();
    }

    public void setReportingPeriodStart(LocalDateTime reportingPeriodStart) {
        this.reportingPeriodStart = reportingPeriodStart;
    }
    
     public LocalDateTime getReportingPeriodEnd() {
        return reportingPeriodEnd;
    }

    public LocalDate getFormattedReportingPeriodEnd() {
        return reportingPeriodEnd.toLocalDate();
    }

    public void setReportingPeriodEnd(LocalDateTime reportingPeriodEnd) {
        this.reportingPeriodEnd = reportingPeriodEnd;
    }

    public Set<FileWrapper> getReportFiles() {
        return this.reportFiles;
    }

    public void setReportFiles(final Set<FileWrapper> reportFiles) {
        if (this.reportFiles == null) {
            this.reportFiles = reportFiles;
        } else {
            this.reportFiles.retainAll(reportFiles);
            if (reportFiles != null) {
                this.reportFiles.addAll(reportFiles);
            }
        }
    }

    public boolean isEmailSent() {
        return emailSent;
    }

    public void setEmailSent(boolean emailSent) {
        this.emailSent = emailSent;
    }

    @Override
    public String toString() {
        return "\nProjectReport{"
                + "\n   id=" + id
                + "\n   fileProvided=" + fileProvided
                + "\n   type=" + type
                + "\n   date=" + reportDate
                + "\n   reportingPeriodStart=" + reportingPeriodStart
                + "\n   reportingPeriodEnd=" + reportingPeriodEnd
                + "\n   file=" + reportFiles.toString()
                + '}';
    }

}
