/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.reports.ui.pages;

import org.apache.log4j.Logger;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.auth.common.util.AuthUtils;
import org.devgateway.eudevfin.financial.service.CustomFinancialTransactionService;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.devgateway.eudevfin.ui.common.pages.HeaderFooter;
import org.wicketstuff.annotation.mount.MountPath;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@MountPath(value = "/exportreports")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public class ReportsExport extends HeaderFooter<Object> {
    private static Logger logger = Logger.getLogger(ReportsExport.class);

	private final String REPORT_AQ = "AQ";
    private final String REPORT_DATASOURCE_AQ = "Advance Questionnaire input form";
	private final String REPORT_DATASOURCE_CRS = "CRS++ input form";
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1829518959766029286L;

	@SpringBean
    private CustomFinancialTransactionService txService;

	public ReportsExport () {
        
    }
	public ReportsExport (final PageParameters parameters) {
		super(parameters);
		String reportType = parameters.get("reportType").toString("");
		
		pageTitle.setDefaultModel(new StringResourceModel("navbar.reports.export." + reportType, this, null, null));
		
        final List<Integer> years = this.txService.findDistinctReportingYears();
        // if the list of years is empty add the current year (ODAEU-322)
        if (years.size() == 0) {
            years.add(Calendar.getInstance().get(Calendar.YEAR) - 1);
        }
        DropDownChoice<Integer> year = new DropDownChoice<Integer>("reportYear", years,
                new IChoiceRenderer<Integer>() {
					private static final long serialVersionUID = 8801460052416367398L;

					public Object getDisplayValue(Integer value)
                    {
                        return value;
                    }

                    public String getIdValue(Integer object, int index) {
                        return String.valueOf(years.get(index));
                    }
                });
        add(year);

        List<String> dataSources = new ArrayList<String>();
        dataSources.add(REPORT_DATASOURCE_AQ);
        dataSources.add(REPORT_DATASOURCE_CRS);
        
        DropDownChoice<String> dataSource = new DropDownChoice<String>("dataSource", dataSources,
                new IChoiceRenderer<String>() {

					@Override
					public Object getDisplayValue(String object) {
						return object;
					}

					@Override
					public String getIdValue(String object, int index) {
						return object;
					}
                });
        
        
        WebMarkupContainer dataSourceGroup = new WebMarkupContainer("dataSourceGroup");
        if(!reportType.equalsIgnoreCase(REPORT_AQ)) {
            dataSourceGroup.setVisibilityAllowed(false);
        }
        dataSourceGroup.add(dataSource);
        add(dataSourceGroup);
        
        HiddenField<String> field = new HiddenField<String>("reportType", Model.of(""));
        field.setModelValue(new String[]{reportType});
		add(field);

        // get the 'Approved Reports' files
        // get the name of the Country
        String serverInstance = "";
        Organization organizationForCurrentUser = AuthUtils.getOrganizationForCurrentUser();
        if (organizationForCurrentUser != null) {
            serverInstance = organizationForCurrentUser.getDonorName();
        }

        // set the files path and names
        String tmpDirPath = System.getProperty("java.io.tmpdir");
        String dirPath = tmpDirPath + File.separator + serverInstance +  "Repository" +
                File.separator + reportType;

        File dir = new File(dirPath);

        List<File> listFiles = new ArrayList();
        if (dir.exists()) {
            for (final File reportFile : dir.listFiles()) {
                // check if there are files and that the name begins with the report type, for example 'DAC1_2013.pdf'
                if (reportFile.isFile() && reportFile.getName().startsWith(reportType.toUpperCase())) {
                    listFiles.add(reportFile);
                }
            }
        }

        Label downloadApprovedReports = new Label("downloadApprovedReports", new StringResourceModel("navbar.reports.downloadApprovedReports", this, null, null));
        add(downloadApprovedReports);

        add(new ListView<File>("listFiles", listFiles){
            public void populateItem(final ListItem<File> item) {
                final File downloadFile = item.getModelObject();
                IModel<File> fileModel = new Model(downloadFile);
                DownloadLink downloadLink = new DownloadLink("downloadLink", fileModel, downloadFile.getName());
                downloadLink.add(new Label("downloadText", downloadFile.getName()));
                item.add(downloadLink);
            }
        });

        if(listFiles == null || listFiles.size() == 0) {
            downloadApprovedReports.setVisibilityAllowed(false);
        }
    }
	
	@Override
    public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
	}
}
