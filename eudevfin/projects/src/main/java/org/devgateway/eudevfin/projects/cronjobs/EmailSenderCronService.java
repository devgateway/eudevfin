/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.cronjobs;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.devgateway.eudevfin.projects.common.entities.Project;
import org.devgateway.eudevfin.projects.common.entities.ProjectReport;
import org.devgateway.eudevfin.projects.service.CustomProjectService;
import org.devgateway.eudevfin.projects.service.ProjectReportService;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.mail.MailException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class EmailSenderCronService {
    private JavaMailSender mailSender;
    private SimpleMailMessage reportingMailMessage;
    private SimpleMailMessage monitoringMailMessage;
    private PropertyPlaceholderConfigurer reportsProperties;
    
    private Map<String, String> values = new HashMap<String, String>();

    @Autowired
    private CustomProjectService projectService;

    @Autowired
    private ProjectReportService reportService;

    private boolean isEmailSent = true;

    private final int DAYS_IN_ADVANCE = 10;

    public EmailSenderCronService() {
        populateKeys();
    }

    /**
     * Annotations doesn't work properly so i have used xml config for cron
     * jobs.
     */
    public void sendEmails() {
        List<Project> projects = projectService.findAllByReportDate(getDateAdvanced());

        if (projects != null && projects.size() > 0) {
            for (Project project : projects) {
                Set<ProjectReport> reports = project.getProjectReports();

                for (ProjectReport report : reports) {
                    if (report.getReportDate().equals(getDateAdvanced()) && !report.isEmailSent()) {
                        MimeMessage message = mailSender.createMimeMessage();

                        try {
                            MimeMessageHelper helper = new MimeMessageHelper(message, true);

                            if (project.getMonitoringEmail() != null) {
                                helper.setFrom(monitoringMailMessage.getFrom());
                                helper.setSubject(monitoringMailMessage.getSubject());
                                helper.setTo(project.getMonitoringEmail());
                                setMonitoringMessage(helper, project, report);
                                mailSender.send(message);
                                isEmailSent &= true;
                                Logger.getLogger("LOG").log(Level.INFO, "{0} ReportMail", project.getMonitoringEmail());
                            }

                            if (project.getReportingEmail() != null) {
                                helper.setFrom(reportingMailMessage.getFrom());
                                helper.setSubject(reportingMailMessage.getSubject());
                                helper.setTo(project.getReportingEmail());
                                setReportingMessage(helper, project, report);
                                mailSender.send(message);
                                isEmailSent &= true;
                                Logger.getLogger("LOG").log(Level.INFO, "{0} ReportMail", project.getReportingEmail());
                            }

                            if (isEmailSent) {
                                Logger.getLogger("LOG").log(Level.INFO, "isEmailSent {0}", isEmailSent);
                                report.setEmailSent(isEmailSent);
                                reportService.save(report);
                            }
                            
                        } catch (MessagingException | MailException | IOException e) {
                            isEmailSent &= false;
                            Logger.getLogger("LOG").log(Level.INFO, "error {0}", e.toString());
                            throw new MailParseException(e);
                        } 
                    }
                }
            }
        }
    }

    /**
     * Calculates the date in advance with DAYS_IN_ADVANCE. It takes in
     * consideration the year, month and days.
     *
     * @return the future LocalDateTime
     */
    private LocalDateTime getDateAdvanced() {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        //getTime() returns the current date in default time zone
        Date date = calendar.getTime();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a calendar object and set year and month
        int daysInMonth = getMaximumDays(currentYear, currentMonth);
        int advancedDays = currentDay + DAYS_IN_ADVANCE;

        if (advancedDays > daysInMonth) {
            if (currentMonth == 12) {
                currentMonth = 0;
                ++currentYear;
            } else {
                ++currentMonth;
            }

            advancedDays -= daysInMonth;
        }

        return new LocalDateTime(currentYear, currentMonth, advancedDays, 0, 0, 0);
    }

    /**
     * Gets the total
     *
     * @param year
     * @param month
     * @return
     */
    private int getMaximumDays(int year, int month) {
        Calendar gregCal = new GregorianCalendar(year, year, 1);
        return gregCal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public void setReportingMailMessage(SimpleMailMessage reportingMailMessage) {
        this.reportingMailMessage = reportingMailMessage;
    }
    
    public void setMonitoringMailMessage(SimpleMailMessage monitoringMailMessage) {
        this.monitoringMailMessage = monitoringMailMessage;
    }


    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    private void setMonitoringMessage(MimeMessageHelper helper, Project project, ProjectReport report) throws MessagingException, IOException {
        String monDetailsEn = project.getMonitoringDetails()==null ? "" : " to " + project.getMonitoringDetails();
        String monDetailsRo = project.getMonitoringDetails()==null ? "" : " catre " + project.getMonitoringDetails();
        helper.setText(String.format(
                    monitoringMailMessage.getText(),
                    values.get((String)report.getType()), project.getName(), 
                    report.getFormattedReportingPeriodStart().toString(), 
                    report.getFormattedReportingPeriodEnd().toString(),
                    report.getFormattedReportDate().toString(), monDetailsEn,
                    project.getReportingEmail(), 
                    values.get((String)report.getType()), project.getName(), 
                    report.getFormattedReportingPeriodStart().toString(), 
                    report.getFormattedReportingPeriodEnd().toString(),
                    report.getFormattedReportDate().toString(), monDetailsRo,
                    project.getReportingEmail()));
    }
    
    private void setReportingMessage(MimeMessageHelper helper, Project project, ProjectReport report) throws MessagingException, IOException {
        String repDetailsEn = project.getReportingDetails()==null ? "" : " " + project.getReportingDetails();
        String repDetailsRo = project.getReportingDetails()==null ? "" : " " + project.getReportingDetails();
        helper.setText(String.format(
                    reportingMailMessage.getText(),
                    values.get((String)report.getType()), project.getName(), 
                    report.getFormattedReportingPeriodStart().toString(), 
                    report.getFormattedReportingPeriodEnd().toString(),
                    report.getFormattedReportDate().toString(), repDetailsEn,
                    project.getMonitoringEmail(),
                    values.get((String)report.getType()), project.getName(), 
                    report.getFormattedReportingPeriodStart().toString(), 
                    report.getFormattedReportingPeriodEnd().toString(),
                    report.getFormattedReportDate().toString(), repDetailsRo,
                    project.getMonitoringEmail()));
        helper.setCc(project.getMonitoringEmail());
    }

    private void populateKeys() {
        values.put("report.type.annual", "Annual report");
        values.put("report.type.biAnnual", "Bi-annual report");
        values.put("report.type.quarterly", "Quarterly report");
        values.put("report.type.preTranche", "Pre-tranche report");
        values.put("report.type.finalReport", "Final report");
        values.put("report.type.auditReport", "Audit report");
        values.put("report.type.evaluationReport", "Evaluation report");
        values.put("report.type.other", "Other report");
    }
    
    private String getCurrentime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}