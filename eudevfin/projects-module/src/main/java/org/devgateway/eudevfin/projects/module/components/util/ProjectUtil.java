/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.module.components.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.devgateway.eudevfin.projects.common.entities.Project;

/**
 *
 * @author alcr
 */
public final class ProjectUtil {

    public static final String PROJECT_LAST_BY_DRAFT = "project.lastByDraft";
    public static final String PROJECT_LAST_BY_CLOSED = "project.lastByClosed";
    public static final String PROJECT_LAST_BY_APPROVED = "project.lastByApproved";

    public static final String PROJECT_DRAFT = "draft";
    public static final String PROJECT_CLOSED = "closed";
    public static final String PROJECT_APPROVED = "approved";

    public static int MAX_AREA_ROWS = 3;

    public static int LABEL_MAX_LENGTH = 10;

    private Pattern pattern;
    private Matcher matcher;

    private static final String EMAIL_PATTERN
            = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public ProjectUtil() {
        pattern = Pattern.compile(EMAIL_PATTERN);
    }

    /**
     * Initialize a previously newly created project
     *
     * @param project
     * @param organization
     * @param contactReporting
     */
    public static void initializeProject(Project project, Organization organization, String email, String details) {
        project.setExtendingAgency(organization);
        project.setReportingEmail(email);
        project.setReportingDetails(details);
    }

    /**
     * Validate hex with regular expression
     *
     * @param mail mail for validation
     * @return true valid hex, false invalid hex
     */
    public boolean isMailValid(final String mail) {
        matcher = pattern.matcher(mail);
        return matcher.matches();
    }

    public static int wordsCount(String s) {
        int wordCount = 0;
        boolean word = false;
        int endOfLine = s.length() - 1;

        for (int i = 0; i < s.length(); i++) {
            // if the char is a letter, word = true.
            if (Character.isLetter(s.charAt(i)) && i != endOfLine) {
                word = true;
                // if char isn't a letter and there have been letters before,
                // counter goes up.
            } else if (!Character.isLetter(s.charAt(i)) && word) {
                wordCount++;
                word = false;
                // last word of String; if it doesn't end with a non letter, it
                // wouldn't count without this.
            } else if (Character.isLetter(s.charAt(i)) && i == endOfLine) {
                wordCount++;
            }
        }
        return wordCount;
    }

}
