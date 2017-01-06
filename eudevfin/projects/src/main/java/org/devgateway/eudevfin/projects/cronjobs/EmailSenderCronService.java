/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.cronjobs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
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
import org.springframework.mail.MailException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class EmailSenderCronService {

    private JavaMailSender mailSender;
    private SimpleMailMessage simpleMailMessage;

    @Autowired
    private CustomProjectService projectService;

    @Autowired
    private ProjectReportService reportService;

    private boolean isEmailSent = true;

    private final int DAYS_IN_ADVANCE = 7;

    public EmailSenderCronService() {
    }

    /**
     * Annotations doesn't work properly so i have used xml config for cron
     * jobs.
     */
    public void sendEmails() {
        Logger.getLogger("LOG").log(Level.INFO, "{0} sendEmails", getCurrentime());
        List<Project> projects = projectService.findAllByReportDate(getDateAdvanced());

        if (projects != null && projects.size() > 0) {
            for (Project project : projects) {
                Set<ProjectReport> reports = project.getProjectReports();

                for (ProjectReport report : reports) {
                    if (report.getReportDate().equals(getDateAdvanced()) && !report.isEmailSent()) {

                        if (project.getMonitoringEmail() != null) {
                            Logger.getLogger("LOG").log(Level.INFO, "project.getMonitoringEmail() {0}", project.getMonitoringEmail());
                            sendMail(project.getMonitoringEmail(), report.getType(), report.getFormattedReportDate().toString());
                        }

                        if (project.getReportingEmail() != null) {
                            Logger.getLogger("LOG").log(Level.INFO, "project.getReportingEmail() {0}", project.getReportingEmail());
                            sendMail(project.getReportingEmail(), report.getType(), report.getFormattedReportDate().toString());
                        }

                        if (isEmailSent) {
                            Logger.getLogger("LOG").log(Level.INFO, "isEmailSent {0}", isEmailSent);
                            report.setEmailSent(isEmailSent);
                            reportService.save(report);
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

    public void setSimpleMailMessage(SimpleMailMessage simpleMailMessage) {
        this.simpleMailMessage = simpleMailMessage;
    }

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMail(String to, String dear, String content) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(simpleMailMessage.getFrom());
            helper.setTo(to);
            helper.setSubject(simpleMailMessage.getSubject());
            helper.setText(String.format(
                    simpleMailMessage.getText(), dear, content));
            mailSender.send(message);
            isEmailSent &= true;
        } catch (MessagingException | MailException e) {
            isEmailSent &= false;
            Logger.getLogger("LOG").log(Level.INFO, "error {0}", isEmailSent);
            throw new MailParseException(e);
        }
    }

    private String getCurrentime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}