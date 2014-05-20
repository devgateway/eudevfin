package org.devgateway.eudevfin.reports.ui.pages;

import com.googlecode.wickedcharts.highcharts.options.Options;
import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.devgateway.eudevfin.auth.common.util.AuthUtils;
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
     * since the custom reports tale are similar we use only one function to process the rows
     * and calculate the Total line for each category
     */
    public static ListView<String[]> processTableRowsWithTotal (List<String[]> rows, QueryResult result, String rowId) {
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

                    resultSet.get(i).set(0, null);
                    resultSet.add(i, newElement);

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
            resultSet.get(0).set(0, null);
            resultSet.add(0, newElement);

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

        ListView<String[]> tableRows = new ListView<String[]>(rowId, rows) {
            @Override
            protected void populateItem(ListItem<String[]> item) {
                String[] row = item.getModelObject();

                item.add(new Label("col0", row[0]));
                item.add(new Label("col1", row[1]));
                item.add(new Label("col2", row[2]));
                item.add(new Label("col3", row[3]));
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

            // check if we have data for both years
            if (result.getMetadata().get(1).getColName().equals("First Year")) {
                if (item.size() > 1 && item.get(1) != null) {
                    resultSeries.get(0).add(Float.parseFloat(item.get(1)));
                } else {
                    resultSeries.get(0).add((float) 0);
                }

                if (item.size() > 2 && item.get(2) != null) {
                    resultSeries.get(1).add(Float.parseFloat(item.get(2)));
                } else {
                    resultSeries.get(1).add((float) 0);
                }
            } else {
                // we don't have the first year data
                resultSeries.get(0).add((float) 0);
                resultSeries.get(1).add(Float.parseFloat(item.get(1)));
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

    public static String AmountFormat (double amount) {
        Locale locale = LocaleContextHolder.getLocale();
        DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(locale);
        df.applyPattern("#,###.##");

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
