/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.module.components.tabs;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationMessage;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.InputBehavior;
import java.util.Collection;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.devgateway.eudevfin.metadata.common.domain.Area;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.devgateway.eudevfin.metadata.common.util.CategoryConstants;
import org.devgateway.eudevfin.projects.module.components.util.ProjectUtil;
import org.devgateway.eudevfin.projects.module.pages.NewProjectPage;
import org.devgateway.eudevfin.projects.module.providers.ProjectTypeProvider;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;
import org.devgateway.eudevfin.ui.common.components.DateInputField;
import org.devgateway.eudevfin.ui.common.components.DropDownField;
import org.devgateway.eudevfin.ui.common.components.MultiSelectField;
import org.devgateway.eudevfin.ui.common.components.TextAreaInputField;
import org.devgateway.eudevfin.ui.common.models.DateToLocalDateTimeModel;
import org.devgateway.eudevfin.ui.common.providers.AreaChoiceProvider;
import org.devgateway.eudevfin.ui.common.providers.CategoryProviderFactory;
import org.devgateway.eudevfin.ui.common.providers.OrganizationChoiceProvider;
import org.joda.time.LocalDateTime;

/**
 *
 * @author alcr
 */
public class IdentificationDataTab extends CommonProjectTab {

    public static final String KEY = "tabs.identification";
    public static final String VALIDATIONKEY_PARENT_CODE = "validation.parentCode";

    private final NotificationPanel feedbackPanel;

    @SpringBean
    private OrganizationChoiceProvider organizationProvider;

    @SpringBean
    private CategoryProviderFactory categoryFactory;

    @SpringBean
    private AreaChoiceProvider areaProvider;

    protected PageParameters parameters;

    public IdentificationDataTab(String id, PageParameters parameters) {
        super(id);
        this.parameters = parameters;
        feedbackPanel = new NotificationPanel("feedback");
        addComponents();
    }

    private void addComponents() {

        // 1. Extended agency
        DropDownField<Organization> extendingAgency = new DropDownField<>("1extendingAgency",
                new RWComponentPropertyModel<Organization>("extendingAgency"), organizationProvider);
        extendingAgency.required();
        add(extendingAgency);

        // 2. Project name
        TextAreaInputField projectName = new TextAreaInputField("2projectName",
                new RWComponentPropertyModel<String>("name"));
        projectName.maxContentLength(500);
        projectName.setRows(ProjectUtil.MAX_AREA_ROWS);
        add(projectName);

        // 3. Project type  
        ProjectTypeProvider provider = new ProjectTypeProvider(this);
        DropDownField<String> projectType = new DropDownField<>("6projectType",
                new RWComponentPropertyModel<String>("type"), provider);
        projectType.setSize(InputBehavior.Size.Medium);
        add(projectType);

        // 4. Project startDate  
        DateInputField startDate = new DateInputField("4startDate", new DateToLocalDateTimeModel(new RWComponentPropertyModel<LocalDateTime>("startDate")));
        add(startDate);

        // 5. Project stopDate
        DateInputField stopDate = new DateInputField("5stopDate", new DateToLocalDateTimeModel(new RWComponentPropertyModel<LocalDateTime>("stopDate")));
        add(stopDate);

        // 6. Implementing organization
        TextAreaInputField implementingOrganization = new TextAreaInputField("3implementingOrganization",
                new RWComponentPropertyModel<String>("implementingOrganization"));
        implementingOrganization.maxContentLength(200);
        implementingOrganization.setRows(ProjectUtil.MAX_AREA_ROWS);
        add(implementingOrganization);

        // 7. Implementing partners
        TextAreaInputField implementingPartners = new TextAreaInputField("7implementingPartners",
                new RWComponentPropertyModel<String>("implementingPartner"));
        implementingPartners.maxContentLength(1000);
        implementingPartners.setRows(ProjectUtil.MAX_AREA_ROWS);
        add(implementingPartners);

        // 8. Geographic priorities        
        MultiSelectField<Area> areas = new MultiSelectField<>("8geographicPriorities",
                new RWComponentPropertyModel<Collection<Area>>("areas"), areaProvider);
        areas.required();
        areas.setEnabled(true);
        add(areas);

        // 9. Thematic area
        DropDownField<Category> thematicArea = new DropDownField<>("9thematicAreas",
                new RWComponentPropertyModel<Category>("sector"), categoryFactory.get(CategoryConstants.ALL_SECTOR_TAG));
        add(thematicArea);
        
        
         // 10. Contact Reporting details default
        TextAreaInputField contactReportingDetails = new TextAreaInputField("10contactReportingDetails", new RWComponentPropertyModel<String>("reportingDetails"));
        String details = NewProjectPage.project.getReportingDetails();

//        if (details != null && !details.isEmpty()) {
//            contactReportingDetails.getField().setEnabled(false);
//        } else {
//            contactReportingDetails.getField().setEnabled(true);
//        }
        contactReportingDetails.maxContentLength(300);
        contactReportingDetails.setRows(ProjectUtil.MAX_AREA_ROWS);
        add(contactReportingDetails);
                
        // 11. Contact Monitoring details default
        TextAreaInputField contactMonitoringDetails = new TextAreaInputField("11contactMonitoringDetails", new RWComponentPropertyModel<String>("monitoringDetails"));
        contactMonitoringDetails.maxContentLength(300);
        contactMonitoringDetails.setRows(ProjectUtil.MAX_AREA_ROWS);
        add(contactMonitoringDetails);

        // 12. Contact Reporting email
        final TextAreaInputField contactReportingEmail = new TextAreaInputField("12contactReportingEmail", new RWComponentPropertyModel<String>("reportingEmail"));
        String email = NewProjectPage.project.getReportingEmail();
        contactReportingEmail.required();
//        if (email != null && !email.isEmpty()) {
//            contactReportingEmail.getField().setEnabled(false);
//        } else {
//            contactReportingEmail.getField().setEnabled(true);
//            contactReportingEmail.required();
//        }
        contactReportingEmail.maxContentLength(128);
        contactReportingEmail.setRows(ProjectUtil.MAX_AREA_ROWS);
        add(contactReportingEmail);

        contactReportingEmail.getField().add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                validateEmail(target, contactReportingEmail);
            }
        });       

        // 13. Contact Monitoring email
        final TextAreaInputField contactMonitoringEmail = new TextAreaInputField("13contactMonitoringEmail", new RWComponentPropertyModel<String>("monitoringEmail"));
        contactMonitoringEmail.required();
        contactMonitoringEmail.maxContentLength(128);
        contactMonitoringEmail.setRows(ProjectUtil.MAX_AREA_ROWS);
        add(contactMonitoringEmail);

        contactMonitoringEmail.getField().add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                validateEmail(target, contactMonitoringEmail);
            }
        });

        

        feedbackPanel.setOutputMarkupId(true);
        feedbackPanel.hideAfter(Duration.seconds(4));
        add(feedbackPanel);
    }

    @Override
    public String getPermissionKey() {
        return KEY;
    }

    @Override
    public void enableRequired() {
    }

    public void validateEmail(AjaxRequestTarget target, TextAreaInputField field) {
        String email = field.getField().getModel().getObject();
        ProjectUtil util = new ProjectUtil();
        if (!util.isMailValid(email)) {
            field.getField().getModel().setObject("");
            error(new NotificationMessage(new StringResourceModel("notification.invalid.email",
                    IdentificationDataTab.this, null, null)));
            target.add(feedbackPanel);
        }
    }
}
