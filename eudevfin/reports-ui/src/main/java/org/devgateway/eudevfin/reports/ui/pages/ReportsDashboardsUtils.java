package org.devgateway.eudevfin.reports.ui.pages;

import com.googlecode.wickedcharts.highcharts.options.Options;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.devgateway.eudevfin.auth.common.util.AuthUtils;
import org.devgateway.eudevfin.financial.util.FinancialTransactionUtil;
import org.devgateway.eudevfin.reports.core.domain.QueryResult;
import org.joda.money.CurrencyUnit;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Utils functions for custom dashboards
 *
 * @author idobre
 * @since 5/13/14
 */
public class ReportsDashboardsUtils {
    private static final int MILLION = 1000000;

    private static final String isCountry = "Country";
    private static final String isGeography = "Geography";
    private static final String isSector = "Sector";
    private static final String isParentSector = "ParentSector";

    /*
     * since the custom reports tables are similar we use only one function to process the rows
     */
    public static ListView<String[]> processTableRows (List<String[]> rows, QueryResult result, String rowId, String typeOfTable) {
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

            // format the amounts as #,###.##
            // and other values like percentages
            DecimalFormat df = new DecimalFormat("#,###.##");
            for (int i = 0; i < resultSet.size(); i++) {
                if (resultSet.get(i).size() > 1 && resultSet.get(i).get(1) != null) {
                    String item = df.format(Float.parseFloat(resultSet.get(i).get(1))); // amounts (first year)
                    resultSet.get(i).set(1, item);
                }

                if (resultSet.get(i).size() > 2 && resultSet.get(i).get(2) != null) {
                    String item = df.format(Float.parseFloat(resultSet.get(i).get(2)) * 100) + '%'; // percentages (first year)
                    resultSet.get(i).set(2, item);
                }


                if (resultSet.get(i).size() > 3 && resultSet.get(i).get(3) != null) {
                    String item = df.format(Float.parseFloat(resultSet.get(i).get(3))); // amounts (second year)
                    resultSet.get(i).set(3, item);
                }

                if (resultSet.get(i).size() > 4 && resultSet.get(i).get(4) != null) {
                    String item = df.format(Float.parseFloat(resultSet.get(i).get(4)) * 100) + '%'; // percentages (second year)
                    resultSet.get(i).set(4, item);
                }
            }

            /*
             * country/sector table are processes differently
             */
            if(typeOfTable.equals(isCountry)) {
                // find which row is a country or a geography
                for (int i = 0; i < resultSet.size(); i++) {
                    String region = resultSet.get(i).get(0);
                    if (resultSet.get(i).get(resultSet.get(i).size() - 1).toLowerCase().
                            equals(isCountry.toLowerCase())) {
                        resultSet.get(i).add(1, region);
                        resultSet.get(i).set(0, null);
                    } else {
                        if (resultSet.get(i).get(resultSet.get(i).size() - 1).toLowerCase().
                                equals(isGeography.toLowerCase())) {
                            resultSet.get(i).add(1, null);
                        }
                    }
                }

                // set 'Total' as Geography
                resultSet.get(resultSet.size() - 1).set(6, isGeography.toLowerCase());
            }
            if(typeOfTable.equals(isSector)) {
                // find which row is a sector or a parent-sector
                for (int i = 0; i < resultSet.size(); i++) {
                    String sector = resultSet.get(i).get(0);
                    if (resultSet.get(i).get(resultSet.get(i).size() - 1).toLowerCase().
                            equals(isSector.toLowerCase())) {
                        resultSet.get(i).add(1, sector);
                        resultSet.get(i).set(0, null);
                    } else {
                        if (resultSet.get(i).get(resultSet.get(i).size() - 1).toLowerCase().
                                equals(isParentSector.toLowerCase())) {
                            resultSet.get(i).add(1, null);
                        }
                    }
                }

                // set 'Total' as Geography
                resultSet.get(resultSet.size() - 1).set(6, isParentSector.toLowerCase());
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
                if (row[row.length - 1].toLowerCase().equals(isGeography.toLowerCase()) ||
                        row[row.length - 1].toLowerCase().equals(isParentSector.toLowerCase())) {
                    item.add(new AttributeModifier("class", "geography"));
                }

                item.add(new Label("col0", row[0]));
                item.add(new Label("col1", row[1]));
                item.add(new Label("col2", row[2]));
                item.add(new Label("col3", row[3]));
                item.add(new Label("col4", row[4]));
                item.add(new Label("col5", row[5]));
            }
        };

        return tableRows;
    }

    /*
     * since the custom reports charts are similar we use only one function to process the rows
     */
    public static List<List<Float>> processChartRows (QueryResult result, Options options) {
        List<List<Float>> resultSeries = new ArrayList<>();
        List<String> resultCategories = new ArrayList<>();

        List<Float> firstYearList = new ArrayList<>();
        resultSeries.add(firstYearList);
        List<Float> secondYearList = new ArrayList<>();
        resultSeries.add(secondYearList);

        for (List<String> item : result.getResultset()) {
            resultCategories.add(item.get(0));

            if (item.size() > 1 && item.get(1) != null) {
                resultSeries.get(0).add(Float.parseFloat(item.get(1)) / MILLION);
            } else {
                resultSeries.get(0).add((float) 0);
            }

            if (item.size() > 2 && item.get(2) != null) {
                resultSeries.get(1).add(Float.parseFloat(item.get(2)) / MILLION);
            } else {
                resultSeries.get(1).add((float) 0);
            }
        }

        options.getxAxis().get(0).setCategories(new ArrayList<>(resultCategories));

        return resultSeries;
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
}
