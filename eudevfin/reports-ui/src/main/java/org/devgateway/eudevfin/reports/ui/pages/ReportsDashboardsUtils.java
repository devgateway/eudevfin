/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.reports.ui.pages;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.eudevfin.auth.common.util.AuthUtils;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.service.FinancialTransactionService;
import org.devgateway.eudevfin.financial.util.FinancialTransactionUtil;
import org.devgateway.eudevfin.reports.core.domain.QueryResult;
import org.joda.money.CurrencyUnit;
import org.springframework.context.i18n.LocaleContextHolder;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Utils functions for custom dashboards
 *
 * @author idobre
 * @since 5/13/14
 */

public class ReportsDashboardsUtils {
    private static final Logger logger = Logger.getLogger(ReportsDashboardsUtils.class);

    /*
     * since the custom reports tables are similar we use only one function to process the rows
     */
    // it's used in country sector dashboards
    public static ListView<String[]> processTableRows (List<String[]> rows, QueryResult result, String rowId,
                                                       final String currencyParam, final String typeOfTable) {
        List <List<String>> resultSet = result.getResultset();

        if(resultSet.size() != 0 && resultSet.get(0).size() > 3) {
            // check if we have data for the 'first year' or 'second year'
            // and add null values
            if (resultSet.get(0).size() == 4) {
                for (int i = 0; i < resultSet.size(); i++) {
                    if (result.getMetadata().get(1).getColName().equals("First Year")) {
                        resultSet.get(i).add(3, null);
                        resultSet.get(i).add(4, null);
                    } else {
                        resultSet.get(i).add(1, null);
                        resultSet.get(i).add(2, null);
                    }
                }
            }

            // this is usually happening when we filter by both region and geography
            if (resultSet.get(0).size() == 5) {
                for (int i = 0; i < resultSet.size(); i++) {
                    if (result.getMetadata().get(2).getColName().equals("First Year %")) {
                        resultSet.get(i).add(4, null);
                    } else {
                        resultSet.get(i).add(2, null);
                    }
                }
            }

            // delete empty rows
            List <List<String>> newResultSet = new ArrayList<>();
            for (int i = 0; i < resultSet.size(); i++) {
                if ((resultSet.get(i).size() > 1 && resultSet.get(i).get(1) != null) ||
                        (resultSet.get(i).size() > 3 && resultSet.get(i).get(3) != null)) {
                    newResultSet.add(resultSet.get(i));
                }
            }
            resultSet = newResultSet;

            // format the amounts as #,###.##
            // and other values like percentages
            for (int i = 0; i < resultSet.size(); i++) {
                if (resultSet.get(i).size() > 1 && resultSet.get(i).get(1) != null) {
                    String item = AmountFormat(Float.parseFloat(resultSet.get(i).get(1))); // amounts (first year)
                    resultSet.get(i).set(1, item);
                }

                if (resultSet.get(i).size() > 2 && resultSet.get(i).get(2) != null) {
                    String item = AmountFormat(Float.parseFloat(resultSet.get(i).get(2)) * 100) + '%'; // percentages (first year)
                    resultSet.get(i).set(2, item);
                }


                if (resultSet.get(i).size() > 3 && resultSet.get(i).get(3) != null) {
                    String item = AmountFormat(Float.parseFloat(resultSet.get(i).get(3))); // amounts (second year)
                    resultSet.get(i).set(3, item);
                }

                if (resultSet.get(i).size() > 4 && resultSet.get(i).get(4) != null) {
                    String item = AmountFormat(Float.parseFloat(resultSet.get(i).get(4)) * 100) + '%'; // percentages (second year)
                    resultSet.get(i).set(4, item);
                }
            }

            /*
             * country/sector table are processes differently
             */
            if(typeOfTable.equals(ReportsConstants.isCountry)) {
                // find which row is a country or a geography
                for (int i = 0; i < resultSet.size(); i++) {
                    String region = resultSet.get(i).get(0);
                    if (resultSet.get(i).get(resultSet.get(i).size() - 1).toLowerCase().
                            equals(ReportsConstants.isCountry.toLowerCase())) {
                        resultSet.get(i).add(1, region);
                        resultSet.get(i).set(0, null);
                    } else {
                        if (resultSet.get(i).get(resultSet.get(i).size() - 1).toLowerCase().
                                equals(ReportsConstants.isGeography.toLowerCase())) {
                            resultSet.get(i).add(1, null);
                        }
                    }
                }

                // set 'Total' as Geography
                resultSet.get(resultSet.size() - 1).set(6, ReportsConstants.isGeography.toLowerCase());
            }
            if(typeOfTable.equals(ReportsConstants.isSector)) {
                // find which row is a sector or a parent-sector
                for (int i = 0; i < resultSet.size(); i++) {
                    String sector = resultSet.get(i).get(0);
                    if (resultSet.get(i).get(resultSet.get(i).size() - 1).toLowerCase().
                            equals(ReportsConstants.isSector.toLowerCase())) {
                        resultSet.get(i).add(1, sector);
                        resultSet.get(i).set(0, null);
                    } else {
                        if (resultSet.get(i).get(resultSet.get(i).size() - 1).toLowerCase().
                                equals(ReportsConstants.isParentSector.toLowerCase())) {
                            resultSet.get(i).add(1, null);
                        }
                    }
                }

                // set 'Total' as Geography
                resultSet.get(resultSet.size() - 1).set(6, ReportsConstants.isParentSector.toLowerCase());
            }

            for (List<String> item : resultSet) {
                rows.add(item.toArray(new String[item.size()]));
            }
        }

        ListView<String[]> tableRows = new ListView<String[]>(rowId, rows) {
            @Override
            protected void populateItem(ListItem<String[]> item) {
                String[] row = item.getModelObject();

                // use different color for geography items
                if (row[row.length - 1].toLowerCase().equals(ReportsConstants.isGeography.toLowerCase()) ||
                        row[row.length - 1].toLowerCase().equals(ReportsConstants.isParentSector.toLowerCase())) {
                    item.add(new AttributeModifier("class", "geography"));
                }

                // add links to second level dashboards
                PageParameters pageParameters = new PageParameters();
                if (currencyParam != null) {
                    pageParameters.add(ReportsConstants.ISNATIONALCURRENCY_PARAM, currencyParam);
                }

                BookmarkablePageLink link = null;
                if(typeOfTable.equals(ReportsConstants.isCountry)) {
                    // for country table create links only for Countries and not for Geography
                    item.add(new Label("col0", row[0]));

                    if (row[1] != null) {
                        pageParameters.add(ReportsConstants.RECIPIENT_PARAM, row[1]);
                    }
                    link = new BookmarkablePageLink("link", CountryDashboards.class, pageParameters);

                    item.add(link);
                    link.add(new Label("linkName", row[1]));

                    // don't make the TOTAL row a link
                    Label totalRow = new Label("total", row[1]);
                    item.add(totalRow);
                    if (row[1] != null && row[1].equals("TOTAL")) {
                        link.setVisibilityAllowed(Boolean.FALSE);
                    } else {
                        totalRow.setVisibilityAllowed(Boolean.FALSE);
                    }
                } else {
                    // for sector table create links only for ParentSector
                    if (row[0] != null) {
                        pageParameters.add(ReportsConstants.SECTOR_PARAM, row[0]);
                    }
                    if(typeOfTable.equals(ReportsConstants.isSector)) {
                        link = new BookmarkablePageLink("link", SectorDashboards.class, pageParameters);
                    }

                    item.add(link);
                    link.add(new Label("linkName", row[0]));

                    item.add(new Label("col1", row[1]));
                }

                item.add(new Label("col2", row[2]));
                item.add(new Label("col3", row[3]));
                item.add(new Label("col4", row[4]));
                item.add(new Label("col5", row[5]));
            }
        };

        return tableRows;
    }

    // is used in Implementation status dashboards
    public static ListView<String[]> processTableRowsWithoutMainCategory (List<String[]> rows, QueryResult result, String rowId) {
        List <List<String>> resultSet = result.getResultset();

        if(resultSet.size() != 0 && resultSet.get(0).size() > 2) {
            // check if we have data for the 'first year' or 'second year'
            // and add null values
            if (resultSet.get(0).size() == 3) {
                for (int i = 0; i < resultSet.size(); i++) {
                    if (result.getMetadata().get(1).getColName().equals("First Year")) {
                        resultSet.get(i).add(3, null);
                        resultSet.get(i).add(4, null);
                    } else {
                        resultSet.get(i).add(1, null);
                        resultSet.get(i).add(2, null);
                    }
                }
            }

            // delete empty rows
            List <List<String>> newResultSet = new ArrayList<>();
            for (int i = 0; i < resultSet.size(); i++) {
                if ((resultSet.get(i).size() > 1 && resultSet.get(i).get(1) != null) ||
                        (resultSet.get(i).size() > 3 && resultSet.get(i).get(3) != null)) {
                    newResultSet.add(resultSet.get(i));
                }
            }
            resultSet = newResultSet;

            // format the amounts as #,###.##
            // and other values like percentages
            for (int i = 0; i < resultSet.size(); i++) {
                if (resultSet.get(i).size() > 1 && resultSet.get(i).get(1) != null) {
                    String item = AmountFormat(Float.parseFloat(resultSet.get(i).get(1))); // amounts (first year)
                    resultSet.get(i).set(1, item);
                }

                if (resultSet.get(i).size() > 2 && resultSet.get(i).get(2) != null) {
                    String item = AmountFormat(Float.parseFloat(resultSet.get(i).get(2)) * 100) + '%'; // percentages (first year)
                    resultSet.get(i).set(2, item);
                }


                if (resultSet.get(i).size() > 3 && resultSet.get(i).get(3) != null) {
                    String item = AmountFormat(Float.parseFloat(resultSet.get(i).get(3))); // amounts (second year)
                    resultSet.get(i).set(3, item);
                }

                if (resultSet.get(i).size() > 4 && resultSet.get(i).get(4) != null) {
                    String item = AmountFormat(Float.parseFloat(resultSet.get(i).get(4)) * 100) + '%'; // percentages (second year)
                    resultSet.get(i).set(4, item);
                }
            }

            int index = 0;
            for (List<String> item : resultSet) {
                if (index % 2 == 0 && !item.get(0).equals("TOTAL")) {
                    index++;
                    continue;
                }

                // add the transaction ID as first element
                // we need the ID to navigate to the second level dashboard
                if (item.get(0).equals("TOTAL")) {
                    item.add(0, null);
                    rows.add(item.toArray(new String[item.size()]));
                } else {
                    item.add(0, resultSet.get(index - 1).get(0));
                    rows.add(item.toArray(new String[item.size()]));
                }

                index++;
            }
        }

        ListView<String[]> tableRows = new ListView<String[]>(rowId, rows) {
            @Override
            protected void populateItem(ListItem<String[]> item) {
                String[] row = item.getModelObject();

                // add links to second level dashboards
                PageParameters pageParameters = new PageParameters();
                if (row[0] != null) {
                    pageParameters.add(ReportsConstants.PARAM_TRANSACTION_ID, row[0]);
                }
                BookmarkablePageLink link = null;
                try {
                    link = new BookmarkablePageLink("link", Class.forName("org.devgateway.eudevfin.dim.pages.transaction.custom.ViewCustomTransactionPage"), pageParameters);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                item.add(link);
                link.add(new Label("linkName", row[1]));

                // don't make the TOTAL row a link
                Label totalRow = new Label("total", row[1]);
                item.add(totalRow);
                if (row[1] != null && row[1].equals("TOTAL")) {
                    link.setVisibilityAllowed(Boolean.FALSE);
                } else {
                    totalRow.setVisibilityAllowed(Boolean.FALSE);
                }

                item.add(new Label("col1", row[2]));
                item.add(new Label("col2", row[3]));
                item.add(new Label("col3", row[4]));
                item.add(new Label("col4", row[5]));
            }
        };

        return tableRows;
    }

    /*
     * since the custom reports tale are similar we use only one function to process the rows
     * and calculate the Total line for each category
     */
    // it used in country and institution
    // institution and type of aid dashboards
    public static ListView<String[]> processTableRowsWithTotal (List<String[]> rows, QueryResult result, String rowId,
                                                                Boolean calculateTotal, final String currencyParam,
                                                                final String typeOfTable, final Boolean addSecondLink) {
        List <List<String>> resultSet = result.getResultset();

        if(resultSet.size() != 0) {
            // check if we have data for the 'first year' or 'second year'
            // and add null values
            if (resultSet.get(0).size() == 3) {
                for (int i = 0; i < resultSet.size(); i++) {
                    if (result.getMetadata().get(2).getColName().equals("First Year")) {
                        resultSet.get(i).add(3, null);
                    } else {
                        resultSet.get(i).add(2, null);
                    }
                }
            }

            // 'group by' operation
            // and calculate the 'Total' line for each entry
            float firstYear = 0;
            float secondYear = 0;
            for (int i = resultSet.size() - 1; i > 0; i--) {
                // calculate the total for each main category (for example institution)
                if (resultSet.get(i).size() > 2 && resultSet.get(i).get(2) != null) {
                    firstYear += Float.parseFloat(resultSet.get(i).get(2));
                }
                if (resultSet.get(i).size() > 3 && resultSet.get(i).get(3) != null) {
                    secondYear += Float.parseFloat(resultSet.get(i).get(3));
                }

                if(resultSet.get(i).get(0).equals(resultSet.get(i - 1).get(0))) {
                    resultSet.get(i).set(0, null);
                } else {
                    List<String> newElement = Arrays
                            .asList(new String[]{
                                    resultSet.get(i).get(0),
                                    "TOTAL",
                                    "" + firstYear,
                                    "" + secondYear
                            });

                    if (calculateTotal) {
                        resultSet.get(i).set(0, null);
                        resultSet.add(i, newElement);
                    }

                    firstYear = 0;
                    secondYear = 0;
                }
            }

            // calculate total for the first element
            if (resultSet.get(0).size() > 2 && resultSet.get(0).get(2) != null) {
                firstYear += Float.parseFloat(resultSet.get(0).get(2));
            }
            if (resultSet.get(0).size() > 3 && resultSet.get(0).get(3) != null) {
                secondYear += Float.parseFloat(resultSet.get(0).get(3));
            }

            List<String> newElement = Arrays
                    .asList(new String[]{
                            resultSet.get(0).get(0),
                            "TOTAL",
                            "" + firstYear,
                            "" + secondYear
                    });

            if (calculateTotal) {
                resultSet.get(0).set(0, null);
                resultSet.add(0, newElement);
            }

            // format the amounts as #,###.##
            // and other values like percentages
            for (int i = 0; i < resultSet.size(); i++) {
                if (resultSet.get(i).size() > 2 && resultSet.get(i).get(2) != null) {
                    String item = AmountFormat(Float.parseFloat(resultSet.get(i).get(2))); // amounts (first year)
                    resultSet.get(i).set(2, item);
                }

                if (resultSet.get(i).size() > 3 && resultSet.get(i).get(3) != null) {
                    String item = AmountFormat(Float.parseFloat(resultSet.get(i).get(3))); // amounts (second year)
                    resultSet.get(i).set(3, item);
                }
            }

            for (List<String> item : resultSet) {
                rows.add(item.toArray(new String[item.size()]));
            }
        }

        final ListView<String[]> tableRows = new ListView<String[]>(rowId, rows) {
            @Override
            protected void populateItem(ListItem<String[]> item) {
                String[] row = item.getModelObject();

                // add links to second level dashboards
                PageParameters pageParameters = new PageParameters();
                if (currencyParam != null) {
                    pageParameters.add(ReportsConstants.ISNATIONALCURRENCY_PARAM, currencyParam);
                }
                if (row[0] != null) {
                    pageParameters.add(ReportsConstants.INSTITUTION_PARAM, row[0]);
                }
                BookmarkablePageLink link = new BookmarkablePageLink("link", InstitutionDashboards.class, pageParameters);

                item.add(link);
                link.add(new Label("linkName", row[0]));

                if (addSecondLink) {
                    PageParameters pageParameters2 = new PageParameters();
                    if (currencyParam != null) {
                        pageParameters2.add(ReportsConstants.ISNATIONALCURRENCY_PARAM, currencyParam);
                    }
                    BookmarkablePageLink link2 = null;

                    if(typeOfTable.equals(ReportsConstants.isCountry)) {
                        if (row[1] != null) {
                            pageParameters2.add(ReportsConstants.RECIPIENT_PARAM, row[1]);
                        }
                        link2 = new BookmarkablePageLink("link2", CountryDashboards.class, pageParameters2);
                    } else {
                        if (typeOfTable.equals(ReportsConstants.isTypeOfAid)) {
                            if (row[1] != null) {
                                pageParameters2.add(ReportsConstants.TYPEOFAID_PARAM, row[1]);
                            }

                            link2 = new BookmarkablePageLink("link2", TypeOfAidDashboards.class, pageParameters2);
                        } else {
                            if (row[1] != null) {
                                pageParameters2.add(ReportsConstants.AGENCY_PARAM, row[1]);
                            }

                            link2 = new BookmarkablePageLink("link2", ChannelDashboards.class, pageParameters2);
                        }
                    }

                    item.add(link2);
                    link2.add(new Label("linkName", row[1]));

                    // don't make the TOTAL row a link
                    Label totalRow = new Label("total", row[1]);
                    item.add(totalRow);
                    if (row[1] != null && row[1].equals("TOTAL")) {
                        link2.setVisibilityAllowed(Boolean.FALSE);
                    } else {
                        totalRow.setVisibilityAllowed(Boolean.FALSE);
                    }
                } else {
                    item.add(new Label("col1", row[1]));
                }

                item.add(new Label("col2", row[2]));
                item.add(new Label("col3", row[3]));
            }
        };

        return tableRows;
    }

    // it's used in country dashboards
    public static ListView<String[]> processTableRowsOneYear (List<String[]> rows, QueryResult result, String rowId,
                                                              final String currencyParam, final String typeOfTable) {
        List <List<String>> resultSet = result.getResultset();

        if(resultSet.size() != 0 && resultSet.get(0).size() > 2) {
            // delete empty rows
            List <List<String>> newResultSet = new ArrayList<>();
            for (int i = 0; i < resultSet.size(); i++) {
                if ((resultSet.get(i).size() > 1 && resultSet.get(i).get(1) != null)) {
                    newResultSet.add(resultSet.get(i));
                }
            }
            resultSet = newResultSet;

            // format the amounts as #,###.##
            // and other values like percentages
            for (int i = 0; i < resultSet.size(); i++) {
                if (resultSet.get(i).size() > 1 && resultSet.get(i).get(1) != null) {
                    String item = AmountFormat(Float.parseFloat(resultSet.get(i).get(1))); // amounts (first year)
                    resultSet.get(i).set(1, item);
                }
            }

            if(typeOfTable.equals(ReportsConstants.isSector)) {
                // find which row is a sector or a parent-sector
                for (int i = 0; i < resultSet.size(); i++) {
                    String sector = resultSet.get(i).get(0);
                    if (resultSet.get(i).get(resultSet.get(i).size() - 1).toLowerCase().
                            equals(ReportsConstants.isSector.toLowerCase())) {
                        resultSet.get(i).add(1, sector);
                        resultSet.get(i).set(0, null);
                    } else {
                        if (resultSet.get(i).get(resultSet.get(i).size() - 1).toLowerCase().
                                equals(ReportsConstants.isParentSector.toLowerCase())) {
                            resultSet.get(i).add(1, null);
                        }
                    }
                }
            }

            for (List<String> item : resultSet) {
                rows.add(item.toArray(new String[item.size()]));
            }
        }

        ListView<String[]> tableRows = new ListView<String[]>(rowId, rows) {
            @Override
            protected void populateItem(ListItem<String[]> item) {
                String[] row = item.getModelObject();

                // use different color for geography items
                if (row[row.length - 1].toLowerCase().equals(ReportsConstants.isGeography.toLowerCase()) ||
                        row[row.length - 1].toLowerCase().equals(ReportsConstants.isParentSector.toLowerCase())) {
                    item.add(new AttributeModifier("class", "geography"));
                }

                // add links to second level dashboards
                PageParameters pageParameters = new PageParameters();
                if (currencyParam != null) {
                    pageParameters.add(ReportsConstants.ISNATIONALCURRENCY_PARAM, currencyParam);
                }
                BookmarkablePageLink link = null;
                if(typeOfTable.equals(ReportsConstants.isSector)) {
                    // for sector table create links only for ParentSector
                    if (row[0] != null) {
                        pageParameters.add(ReportsConstants.SECTOR_PARAM, row[0]);
                    }
                    link = new BookmarkablePageLink("link", SectorDashboards.class, pageParameters);

                    item.add(link);
                    link.add(new Label("linkName", row[0]));

                    item.add(new Label("col1", row[1]));
                }

                item.add(new Label("col2", row[2]));
            }
        };

        return tableRows;
    }

    // it's used in sector dashboards
    // and institution dashboards
    public static ListView<String[]> processTableRowsWithTotalOneYear (List<String[]> rows, QueryResult result, String rowId,
                                                                Boolean calculateTotal, final String currencyParam,
                                                                final String typeOfTable, final Boolean addSecondLink) {
        List <List<String>> resultSet = result.getResultset();

        if(resultSet.size() != 0) {
            // 'group by' operation
            // and calculate the 'Total' line for each entry
            float firstYear = 0;
            float grandTotal = 0;
            for (int i = resultSet.size() - 1; i > 0; i--) {
                // calculate the total for each main category (for example institution)
                if (resultSet.get(i).size() > 2 && resultSet.get(i).get(2) != null) {
                    firstYear += Float.parseFloat(resultSet.get(i).get(2));
                }

                if(resultSet.get(i).get(0).equals(resultSet.get(i - 1).get(0))) {
                    resultSet.get(i).set(0, null);
                } else {
                    List<String> newElement = Arrays
                            .asList(new String[]{
                                    resultSet.get(i).get(0),
                                    "TOTAL",
                                    "" + firstYear
                            });
                    grandTotal += firstYear;
                    if (calculateTotal) {
                        resultSet.get(i).set(0, null);
                        resultSet.add(i, newElement);
                    }

                    firstYear = 0;
                }
            }

            // calculate total for the first element
            if (resultSet.get(0).size() > 2 && resultSet.get(0).get(2) != null) {
                firstYear += Float.parseFloat(resultSet.get(0).get(2));
            }

            List<String> newElement = Arrays
                    .asList(new String[]{
                            resultSet.get(0).get(0),
                            "TOTAL",
                            "" + firstYear
                    });

            grandTotal += firstYear;
            if (calculateTotal) {
                resultSet.get(0).set(0, null);
                resultSet.add(0, newElement);

                newElement = Arrays
                        .asList(new String[]{
                                null,
                                "GRAND TOTAL",
                                "" + grandTotal
                        });
                resultSet.add(0, newElement);
            }

            // format the amounts as #,###.##
            // and other values like percentages
            for (int i = 0; i < resultSet.size(); i++) {
                if (resultSet.get(i).size() > 2 && resultSet.get(i).get(2) != null) {
                    String item = AmountFormat(Float.parseFloat(resultSet.get(i).get(2))); // amounts (first year)
                    resultSet.get(i).set(2, item);
                }
            }

            for (List<String> item : resultSet) {
                rows.add(item.toArray(new String[item.size()]));
            }
        }

        final ListView<String[]> tableRows = new ListView<String[]>(rowId, rows) {
            @Override
            protected void populateItem(ListItem<String[]> item) {
                String[] row = item.getModelObject();

                // add links to second level dashboards
                PageParameters pageParameters = new PageParameters();
                if (currencyParam != null) {
                    pageParameters.add(ReportsConstants.ISNATIONALCURRENCY_PARAM, currencyParam);
                }
                BookmarkablePageLink link = null;
                if(typeOfTable.equals(ReportsConstants.isCountry)) {
                    if (row[0] != null) {
                        pageParameters.add(ReportsConstants.RECIPIENT_PARAM, row[0]);
                    }

                    link = new BookmarkablePageLink("link", CountryDashboards.class, pageParameters);
                }

                item.add(link);
                link.add(new Label("linkName", row[0]));

                if (addSecondLink) {
                    PageParameters pageParameters2 = new PageParameters();
                    if (currencyParam != null) {
                        pageParameters2.add(ReportsConstants.ISNATIONALCURRENCY_PARAM, currencyParam);
                    }

                    if (row[1] != null) {
                        pageParameters2.add(ReportsConstants.SECTOR_PARAM, row[1]);
                    }
                    BookmarkablePageLink link2 = new BookmarkablePageLink("link2", SectorDashboards.class, pageParameters2);

                    item.add(link2);
                    link2.add(new Label("linkName", row[1]));

                    // don't make the TOTAL row a link
                    Label totalRow = new Label("total", row[1]);
                    item.add(totalRow);
                    if (row[1] != null && (row[1].equals("TOTAL") || row[1].equals("GRAND TOTAL"))) {
                        link2.setVisibilityAllowed(Boolean.FALSE);
                    } else {
                        totalRow.setVisibilityAllowed(Boolean.FALSE);
                    }
                } else {
                    item.add(new Label("col1", row[1]));
                }

                item.add(new Label("col2", row[2]));
            }
        };

        return tableRows;
    }

    // is used transaction list tables
    public static ListView<String[]> processTableRowsTransactions (FinancialTransactionService financialTransactionService,
                                                                   List<String[]> rows, QueryResult result, String rowId,
                                                                   final String currencyParam, final String typeOfTable) {
        List <List<String>> resultSet = result.getResultset();

        if(resultSet.size() != 0 && resultSet.get(0).size() > 0) {
            getMoreInfo(resultSet, financialTransactionService);

            // format the amounts as #,###.##
            // and other values like percentages
            for (int i = 0; i < resultSet.size(); i++) {
                if (resultSet.get(i).size() > 1 && resultSet.get(i).get(1) != null) {
                    String item = AmountFormat(Float.parseFloat(resultSet.get(i).get(1))); // amounts (total budget)
                    resultSet.get(i).set(1, item);
                }

                if (resultSet.get(i).size() > 2 && resultSet.get(i).get(2) != null) {
                    String item = AmountFormat(Float.parseFloat(resultSet.get(i).get(2))); // amounts (disbursement)
                    resultSet.get(i).set(2, item);
                }
            }

            int index = 0;
            for (List<String> item : resultSet) {
                if (index % 2 == 0) {
                    index++;
                    continue;
                }

                // add the transaction ID as first element
                // we need the ID to navigate to the second level dashboard
                item.add(0, resultSet.get(index - 1).get(0));
                rows.add(item.toArray(new String[item.size()]));

                index++;
            }
        }

        ListView<String[]> tableRows = new ListView<String[]>(rowId, rows) {
            @Override
            protected void populateItem(ListItem<String[]> item) {
                String[] row = item.getModelObject();

                // add links to second level dashboards
                PageParameters pageParameters = new PageParameters();
                if (row[0] != null) {
                    pageParameters.add(ReportsConstants.PARAM_TRANSACTION_ID, row[0]);
                }
                BookmarkablePageLink link = null;
                try {
                    link = new BookmarkablePageLink("link",
                            Class.forName("org.devgateway.eudevfin.dim.pages.transaction.custom.ViewCustomTransactionPage"), pageParameters);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                item.add(link);
                link.add(new Label("linkName", row[1]));

                // add links to other sub-reports
                PageParameters pageParametersCountry = new PageParameters();
                PageParameters pageParametersInstitution = new PageParameters();
                PageParameters pageParametersChannel = new PageParameters();
                if (currencyParam != null) {
                    pageParametersCountry.add(ReportsConstants.ISNATIONALCURRENCY_PARAM, currencyParam);
                    pageParametersInstitution.add(ReportsConstants.ISNATIONALCURRENCY_PARAM, currencyParam);
                    pageParametersChannel.add(ReportsConstants.ISNATIONALCURRENCY_PARAM, currencyParam);
                }
                if (row[5] != null) {
                    pageParametersCountry.add(ReportsConstants.RECIPIENT_PARAM, row[5]);
                }
                if (row[6] != null) {
                    pageParametersInstitution.add(ReportsConstants.INSTITUTION_PARAM, row[6]);
                }
                if (row[7] != null) {
                    pageParametersChannel.add(ReportsConstants.AGENCY_PARAM, row[7]);
                }

                // for each dashboard we display something else
                if(typeOfTable.equals(ReportsConstants.isCountry)) {
                    item.add(new Label("col1", row[2]));
                    item.add(new Label("col2", row[3]));
                    item.add(new Label("col3", row[4]));

                    BookmarkablePageLink col4 = new BookmarkablePageLink("col4", InstitutionDashboards.class, pageParametersInstitution);
                    item.add(col4);
                    col4.add(new Label("linkName", row[6]));

                    BookmarkablePageLink col5 = new BookmarkablePageLink("col5", ChannelDashboards.class, pageParametersChannel);
                    item.add(col5);
                    col5.add(new Label("linkName", row[7]));
                } else {
                    if(typeOfTable.equals(ReportsConstants.isSector)) {
                        item.add(new Label("col1", row[2]));
                        item.add(new Label("col2", row[3]));

                        BookmarkablePageLink col3 = new BookmarkablePageLink("col3", CountryDashboards.class, pageParametersCountry);
                        item.add(col3);
                        col3.add(new Label("linkName", row[5]));

                        BookmarkablePageLink col4 = new BookmarkablePageLink("col4", InstitutionDashboards.class, pageParametersInstitution);
                        item.add(col4);
                        col4.add(new Label("linkName", row[6]));

                        BookmarkablePageLink col5 = new BookmarkablePageLink("col5", ChannelDashboards.class, pageParametersChannel);
                        item.add(col5);
                        col5.add(new Label("linkName", row[7]));
                    } else {
                        if(typeOfTable.equals(ReportsConstants.isInstitution)) {
                            item.add(new Label("col1", row[2]));
                            item.add(new Label("col2", row[3]));
                            item.add(new Label("col3", row[4]));

                            BookmarkablePageLink col4 = new BookmarkablePageLink("col4", CountryDashboards.class, pageParametersCountry);
                            item.add(col4);
                            col4.add(new Label("linkName", row[5]));

                            BookmarkablePageLink col5 = new BookmarkablePageLink("col5", ChannelDashboards.class, pageParametersChannel);
                            item.add(col5);
                            col5.add(new Label("linkName", row[7]));
                        } else {
                            if(typeOfTable.equals(ReportsConstants.isTypeOfAid)) {
                                item.add(new Label("col1", row[2]));
                                item.add(new Label("col2", row[3]));
                                item.add(new Label("col3", row[4]));

                                BookmarkablePageLink col4 = new BookmarkablePageLink("col4", CountryDashboards.class, pageParametersCountry);
                                item.add(col4);
                                col4.add(new Label("linkName", row[5]));

                                BookmarkablePageLink col5 = new BookmarkablePageLink("col5", InstitutionDashboards.class, pageParametersInstitution);
                                item.add(col5);
                                col5.add(new Label("linkName", row[6]));

                                BookmarkablePageLink col6 = new BookmarkablePageLink("col6", ChannelDashboards.class, pageParametersChannel);
                                item.add(col6);
                                col6.add(new Label("linkName", row[7]));
                            } else {
                                if(typeOfTable.equals(ReportsConstants.isChannel)) {
                                    item.add(new Label("col1", row[2]));
                                    item.add(new Label("col2", row[3]));
                                    item.add(new Label("col3", row[4]));

                                    BookmarkablePageLink col4 = new BookmarkablePageLink("col4", CountryDashboards.class, pageParametersCountry);
                                    item.add(col4);
                                    col4.add(new Label("linkName", row[5]));

                                    BookmarkablePageLink col5 = new BookmarkablePageLink("col5", InstitutionDashboards.class, pageParametersInstitution);
                                    item.add(col5);
                                    col5.add(new Label("linkName", row[6]));
                                }
                            }
                        }
                    }
                }
            }
        };

        return tableRows;
    }

    // take more info from the database: sector, country, institution, channel
    private static void getMoreInfo(List<List<String>> resultSet, FinancialTransactionService financialTransactionService) {
        int index = 0;
        for (List<String> item : resultSet) {
            if (index % 2 != 0) {
                index++;
                continue;
            }

            long transactionId = Long.parseLong(item.get(0));
            FinancialTransaction financialTransaction = financialTransactionService.findOne(transactionId).getEntity();
            if (financialTransaction.getSector() != null) {
                resultSet.get(index + 1).add(financialTransaction.getSector().getName());
            } else {
                resultSet.get(index + 1).add(null);
            }
            if (financialTransaction.getRecipient() != null) {
                resultSet.get(index + 1).add(financialTransaction.getRecipient().getName());
            }
            else {
                resultSet.get(index + 1).add(null);
            }
            if (financialTransaction.getExtendingAgency() != null) {
                resultSet.get(index + 1).add(financialTransaction.getExtendingAgency().getName());
            } else {
                resultSet.get(index + 1).add(null);
            }
            if (financialTransaction.getChannel() != null) {
                resultSet.get(index + 1).add(financialTransaction.getChannel().getName());
            } else {
                resultSet.get(index + 1).add(null);
            }

            index++;
        }
    }

    /**
     * Analyzes passed 'pattern', if there is a fragment like "{some-key}", searches in the 'parameters' map,
     * gets its value and replaces this fragment by this value. If key was not found in the map, put the key name
     */
    public static String fillPattern (String pattern, Map parameters) {
        if (pattern == null || parameters == null) {
            return null;
        }

        StringBuffer sb = new StringBuffer(512);
        StringBuffer paramBuffer = new StringBuffer(128);
        StringBuffer targetBuffer = sb;

        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            if (c == '{') {
                targetBuffer = paramBuffer;
            } else {
                if (c == '}') {
                    String paramName = paramBuffer.toString();
                    targetBuffer = sb;
                    paramBuffer = new StringBuffer();
                    Object value = parameters.get(paramName);
                    if (value == null) {
                        value = "{" + paramName + "}";
                        sb.append(value);
                    } else {
                        sb.append(value);
                    }
                } else {
                    targetBuffer.append(c);
                }
            }
        }

        return sb.toString();
    }

    /*
     * function that returns the user currency (or country currency)
     */
    public static String getCurrency () {
        String countryCurrency = "";
        CurrencyUnit currencyForCountryIso = FinancialTransactionUtil
                .getCurrencyForCountryIso(AuthUtils
                        .getIsoCountryForCurrentUser());
        if (currencyForCountryIso != null) {
            countryCurrency = currencyForCountryIso.getCode();
        }

        return countryCurrency;
    }

    public static String AmountFormat (double amount) {
        Locale locale = LocaleContextHolder.getLocale();
        DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(locale);
        df.applyPattern("#,###.##");

        return df.format(amount);
    }

    public static String AmountFormatMorePrecision (double amount) {
        Locale locale = LocaleContextHolder.getLocale();
        DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(locale);
        df.applyPattern("#,###.####");

        return df.format(amount);
    }

    public static String twoDecimalFormat (String amount) {
        Locale locale = LocaleContextHolder.getLocale();
        DecimalFormat twoDForm = (DecimalFormat) NumberFormat.getNumberInstance(locale);
        twoDForm.applyPattern("#.##");

        return twoDForm.format(Float.parseFloat(amount));
    }

    public static float twoDecimalFormat (float amount) {
        Locale locale = LocaleContextHolder.getLocale();
        DecimalFormat twoDForm = (DecimalFormat) NumberFormat.getNumberInstance(locale);
        twoDForm.applyPattern("#.##");
        
        return Float.valueOf(twoDForm.format(amount));
    }
}
