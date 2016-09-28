/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.module.providers;

import com.vaynberg.wicket.select2.Response;
import com.vaynberg.wicket.select2.TextChoiceProvider;
import java.util.Collection;
import org.apache.wicket.Component;
import org.apache.wicket.model.StringResourceModel;

/**
 *
 * @author alcr
 */
public class ReportTypeProvider extends TextChoiceProvider<String> {
    private static final long serialVersionUID = 4554262931187087047L;
    private Component component;

    public ReportTypeProvider(Component component) {
        this.component = component;
    }

    protected String getDisplayText(String choice) {
        return getTranslation(choice);
    }

    protected Object getId(String choice) {
        return choice;
    }

    private String getTranslation(String key) {
        return new StringResourceModel(key, component, null).getObject();
    }

    public void query(String term, int page, Response<String> response) {
        response.add("report.type.annual");
        response.add("report.type.biAnnual");
        response.add("report.type.quarterly");     
        response.add("report.type.preTranche");       
        response.add("report.type.finalReport");
        response.add("report.type.auditReport");
        response.add("report.type.evaluationReport");
        response.add("report.type.other");
    }

    public Collection<String> toChoices(Collection<String> ids) {
        return ids;
    }
}
