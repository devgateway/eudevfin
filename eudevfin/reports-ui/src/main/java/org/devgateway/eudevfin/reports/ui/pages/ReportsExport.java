/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.reports.ui.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.financial.service.CustomFinancialTransactionService;
import org.devgateway.eudevfin.ui.common.pages.HeaderFooter;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath(value = "/exportreports")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public class ReportsExport extends HeaderFooter<Object> {
	
	private final String REPORT_AQ = "AQ";
	private final String REPORT_CRS = "CRS";
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
        DropDownChoice<Integer> year = new DropDownChoice<Integer>("reportYear", this.txService.findDistinctReportingYears(), new IChoiceRenderer<Integer>()
                {
                    /**
					 * 
					 */
					private static final long serialVersionUID = 8801460052416367398L;

					public Object getDisplayValue(Integer value)
                    {
                        return value;
                    }

                    public String getIdValue(Integer object, int index)
                    {
                        return String.valueOf(years.get(index));
                    }
                });
        add(year);

        List<String> dataSources = new ArrayList<String>();
        dataSources.add(REPORT_AQ);
        dataSources.add(REPORT_CRS);
        
        DropDownChoice<String> dataSource = new DropDownChoice<String>("dataSource", dataSources, new IChoiceRenderer<String>()
                {

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
    }
	
	@Override
    public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
	}
}
