package org.devgateway.eudevfin.reports.ui.pages;

import org.apache.log4j.Logger;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.financial.dao.CategoryDaoImpl;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.reports.core.service.QueryService;
import org.devgateway.eudevfin.reports.ui.components.PieChartNVD3;
import org.devgateway.eudevfin.reports.ui.components.Table;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author idobre
 * @since 4/16/14
 */
@MountPath(value = "/reportsinstitutiontypeofaiddashboards")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public class ReportsInstitutionTypeOfAidDashboards extends ReportsDashboards {
    private static final Logger logger = Logger.getLogger(ReportsInstitutionTypeOfAidDashboards.class);

    /*
     * variables used to dynamically create the MDX queries
     */
    // variables used for 'type of aid table'
    private String typeOfAidTableRowSetBilateral = "CrossJoin([Extending Agency].[Name].Members, [Type of Aid].[Name].Members)";
    private String typeOfAidTableRowSetInstitutionBilateral = "CrossJoin([Extending Agency].[Name].[__INSTITUTION__], [Type of Aid].[Name].Members)";
    private String typeOfAidTableRowSetMultilateral = "CrossJoin([Extending Agency].[Name].Members, [Channel].[Name].Members)";
    private String typeOfAidTableRowSetInstitutionMultilateral = "CrossJoin([Extending Agency].[Name].[__INSTITUTION__], [Channel].[Name].__CHANNEL__)";

    private String typeOfAidChartRowSetBilateral = "{Hierarchize({[Type of Aid].[Name].Members})}";
    private String typeOfAidChartRowSetMultilateral = "{Hierarchize({[Channel].[Name].Members})}";

    private String countryCurrency = "$";

    private int tableYear;
    // variables that holds the parameters received from filter
    private String currencyParam;
    private String agencyParam;
    private String typeOfFlowParam;
    private String institutionParam;
    private String yearParam;
    private String humanitarianAidParam;

    private Category bilateralCategory;
    private Category multilateralCategory;

    @SpringBean
    private CategoryDaoImpl catDao;

    @SpringBean
    QueryService CdaService;

    public ReportsInstitutionTypeOfAidDashboards(final PageParameters parameters) {
        // get a default year if it's not specified
        tableYear = Calendar.getInstance().get(Calendar.YEAR) - 1;

        // process the parameters received from the filters
        if(!parameters.get(ReportsConstants.ISNATIONALCURRENCY_PARAM).equals(StringValue.valueOf((String) null))) {
            currencyParam = parameters.get(ReportsConstants.ISNATIONALCURRENCY_PARAM).toString();
            if (currencyParam.equals("true")) {
                countryCurrency = ReportsDashboardsUtils.getCurrency();
            }
        }

        // process the parameters received from the filters
        if(!parameters.get(ReportsConstants.AGENCY_PARAM).equals(StringValue.valueOf((String) null))) {
            agencyParam = parameters.get(ReportsConstants.AGENCY_PARAM).toString();
        }
        if(!parameters.get(ReportsConstants.TYPEOFFLOW_PARAM).equals(StringValue.valueOf((String) null))) {
            typeOfFlowParam = parameters.get(ReportsConstants.TYPEOFFLOW_PARAM).toString();
        }
        if(!parameters.get(ReportsConstants.INSTITUTION_PARAM).equals(StringValue.valueOf((String) null))) {
            institutionParam = parameters.get(ReportsConstants.INSTITUTION_PARAM).toString();
        }
        if(!parameters.get(ReportsConstants.YEAR_PARAM).equals(StringValue.valueOf((String) null))) {
            yearParam = parameters.get(ReportsConstants.YEAR_PARAM).toString();
            tableYear = Integer.parseInt(yearParam);
        }
        if(!parameters.get(ReportsConstants.HUMANITARIANAID_PARAM).equals(StringValue.valueOf((String) null))) {
            humanitarianAidParam = parameters.get(ReportsConstants.HUMANITARIANAID_PARAM).toString();
        }

        bilateralCategory = catDao.findByCode("BI_MULTILATERAL##1").get(0);
        multilateralCategory = catDao.findByCode("BI_MULTILATERAL##2").get(0);

        /*
         * create the MDX queries based on the filters
         */
        if (institutionParam != null) {
            typeOfAidTableRowSetInstitutionBilateral = typeOfAidTableRowSetInstitutionBilateral.replaceAll("__INSTITUTION__", institutionParam);
            typeOfAidTableRowSetBilateral = typeOfAidTableRowSetInstitutionBilateral;

            typeOfAidTableRowSetInstitutionMultilateral = typeOfAidTableRowSetInstitutionMultilateral.replaceAll("__INSTITUTION__", institutionParam);

            if (agencyParam != null) {
                typeOfAidTableRowSetInstitutionMultilateral = typeOfAidTableRowSetInstitutionMultilateral.replaceAll("__CHANNEL__", "[" + agencyParam + "]");
            } else {
                typeOfAidTableRowSetInstitutionMultilateral = typeOfAidTableRowSetInstitutionMultilateral.replaceAll("__CHANNEL__", "Members");
            }

            typeOfAidTableRowSetMultilateral = typeOfAidTableRowSetInstitutionMultilateral;
        }

        addComponents();
    }

    private void addComponents() {
        addTypeOfAidTable();
        addTypeOfAidHumanitarianAidTable();
        addTypeOfAidChart();
        addTypeBiMultilateralChart();
    }

    private void addTypeOfAidTable () {
        Label title;
        if (typeOfFlowParam != null && typeOfFlowParam.equals(multilateralCategory.getName())) {
            title = new Label("typeOfAidTableTitle", "Multilateral ODA by institution - Net Disbursements " + (tableYear - 1) + "-" + tableYear +
                    " - " + countryCurrency + " - full amount");
        } else {
            title = new Label("typeOfAidTableTitle", "Bilateral ODA by institution - Net Disbursements " + (tableYear - 1) + "-" + tableYear +
                    " - " + countryCurrency + " - full amount");
        }
        add(title);

        Table table = new Table(CdaService, "typeOfAidTable", "typeOfAidTableRows", "customDashboardsTypeOfAidTable") {
            @Override
            public ListView<String[]> getTableRows () {
                super.getTableRows();

                this.rows = new ArrayList<>();
                this.result = this.runQuery();

                if (typeOfFlowParam != null && typeOfFlowParam.equals(multilateralCategory.getName())) {
                    return ReportsDashboardsUtils.processTableRowsWithTotal(this.rows, this.result, this.rowId, true,
                            currencyParam, ReportsConstants.isChannel, true);
                } else {
                    return ReportsDashboardsUtils.processTableRowsWithTotal(this.rows, this.result, this.rowId, true,
                            currencyParam, ReportsConstants.isTypeOfAid, true);
                }
            }
        };

        // add MDX queries parameters
        table.setParam("paramFIRST_YEAR", Integer.toString(tableYear - 1));
        table.setParam("paramSECOND_YEAR", Integer.toString(tableYear));
        if (currencyParam != null) {
            if (currencyParam.equals("true")) {
                table.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
            }
        }

        Label headerTitle;
        if (typeOfFlowParam != null && typeOfFlowParam.equals(multilateralCategory.getName())) {
            table.setParam("paramtypeOfAidTableRowSet", typeOfAidTableRowSetMultilateral);
            table.setParam("parambilateral", "Multilateral");
            headerTitle = new Label("typeOfFlow", new StringResourceModel("ReportsInstitutionTypeOfAidDashboards.multilateralAgency", this, null, null));
        } else {
            table.setParam("paramtypeOfAidTableRowSet", typeOfAidTableRowSetBilateral);
            headerTitle = new Label("typeOfFlow", new StringResourceModel("ReportsInstitutionTypeOfAidDashboards.typeOfAid", this, null, null));
            table.setParam("parambilateral", "Bilateral");
        }
        table.getTable().add(headerTitle);

        Label firstYear = new Label("firstYear", tableYear - 1);
        table.getTable().add(firstYear);
        Label secondYear = new Label("secondYear", tableYear);
        table.getTable().add(secondYear);

        add(table.getTable());
        table.addTableRows();

        Label currencyFirstYear = new Label("currencyFirstYear", countryCurrency);
        table.getTable().add(currencyFirstYear);
        Label currencySecondYear = new Label("currencySecondYear", countryCurrency);
        table.getTable().add(currencySecondYear);
    }

    private void addTypeOfAidHumanitarianAidTable () {
        Label title = new Label("typeOfAidHumanitarianAidTableTitle", new StringResourceModel("ReportsInstitutionTypeOfAidDashboards.humanitarianAidTableTitle", this, null, null));
        add(title);

        Table table = new Table(CdaService, "typeOfAidHumanitarianAidTable", "typeOfAidHumanitarianAidTableRows", "customDashboardsTypeOfAidHumanitarianAidTable") {
            @Override
            public ListView<String[]> getTableRows () {
                super.getTableRows();

                this.rows = new ArrayList<>();
                this.result = this.runQuery();

                List <List<String>> resultSet = result.getResultset();

                if(resultSet.size() != 0) {
                    // add a new null row to build the same format as the first table
                    for (int i = 0; i < resultSet.size(); i++) {
                        resultSet.get(i).add(1, null);
                    }
                    result.getMetadata().add(1, null);
                }

                return ReportsDashboardsUtils.processTableRowsWithTotal(this.rows, this.result, this.rowId, false,
                        currencyParam, ReportsConstants.isTypeOfAid, false);
            }
        };

        // add MDX queries parameters
        table.setParam("paramFIRST_YEAR", Integer.toString(tableYear - 1));
        table.setParam("paramSECOND_YEAR", Integer.toString(tableYear));
        if (currencyParam != null) {
            if (currencyParam.equals("true")) {
                table.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
            }
        }

        Label firstYear = new Label("firstYear", tableYear - 1);
        table.getTable().add(firstYear);
        Label secondYear = new Label("secondYear", tableYear);
        table.getTable().add(secondYear);

        add(table.getTable());
        table.addTableRows();

        Label currencyFirstYear = new Label("currencyFirstYear", countryCurrency);
        table.getTable().add(currencyFirstYear);
        Label currencySecondYear = new Label("currencySecondYear", countryCurrency);
        table.getTable().add(currencySecondYear);

        if (humanitarianAidParam == null || humanitarianAidParam.equals("false")) {
            table.getTable().setVisibilityAllowed(Boolean.FALSE);
            title.setVisibilityAllowed(Boolean.FALSE);
        }
    }

    private void addTypeOfAidChart () {
        Label title;
        if (typeOfFlowParam != null && typeOfFlowParam.equals(multilateralCategory.getName())) {
            title = new Label("typeOfAidChartTitle", "Multilateral ODA by institution - Net Disbursements " + (tableYear - 1) + "-" + tableYear +
                    " - " + countryCurrency + " - full amount");
        } else {
            title = new Label("typeOfAidChartTitle", "Bilateral ODA by institution - Net Disbursements " + (tableYear - 1) + "-" + tableYear +
                    " - " + countryCurrency + " - full amount");
        }
        add(title);

        PieChartNVD3 pieChartNVD3 = new PieChartNVD3(CdaService, "typeOfAidChart", "customDashboardsTypeOfAidChart");

        // add MDX queries parameters
        pieChartNVD3.setParam("paramFIRST_YEAR", Integer.toString(tableYear));

        if (currencyParam != null) {
            if (currencyParam.equals("true")) {
                pieChartNVD3.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
            }
        }
        if (institutionParam != null) {
            pieChartNVD3.setParam("paramextendingAgencies", "[Name].[" + institutionParam + "]");
        }
        if (typeOfFlowParam != null && typeOfFlowParam.equals(multilateralCategory.getName())) {
            pieChartNVD3.setParam("paramtypeOfAidChartRowSet", typeOfAidChartRowSetMultilateral);
            pieChartNVD3.setParam("parambilateral", "Multilateral");
        } else {
            pieChartNVD3.setParam("paramtypeOfAidChartRowSet", typeOfAidChartRowSetBilateral);
            pieChartNVD3.setParam("parambilateral", "Bilateral");
        }

        add(pieChartNVD3);
    }

    protected void addTypeBiMultilateralChart () {
        Label title = new Label("biMultilateralTitle", new StringResourceModel("ReportsInstitutionTypeOfAidDashboards.biMultilateralChart", this, null, null));
        add(title);

        PieChartNVD3 pieChartNVD3 = new PieChartNVD3(CdaService, "biMultilateralChart", "customDashboardsBiMultilateralChart");

        // add MDX queries parameters
        pieChartNVD3.setParam("paramFIRST_YEAR", Integer.toString(tableYear));

        if (currencyParam != null) {
            if (currencyParam.equals("true")) {
                pieChartNVD3.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
            }
        }

        add(pieChartNVD3);
    }
}
