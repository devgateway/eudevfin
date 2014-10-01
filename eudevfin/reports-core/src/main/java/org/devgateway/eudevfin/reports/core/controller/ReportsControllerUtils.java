package org.devgateway.eudevfin.reports.core.controller;

import mondrian.olap.Connection;
import mondrian.olap.DriverManager;
import mondrian.olap.Util;
import mondrian.rolap.RolapConnectionProperties;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.olap.JRMondrianQueryExecuterFactory;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.devgateway.eudevfin.auth.common.util.AuthUtils;
import org.devgateway.eudevfin.common.locale.LocaleHelper;
import org.devgateway.eudevfin.financial.util.FinancialTransactionUtil;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.devgateway.eudevfin.reports.core.dao.RowReportDao;
import org.devgateway.eudevfin.reports.core.utils.ReportExporter;
import org.devgateway.eudevfin.reports.core.utils.ReportTemplate;
import org.joda.money.CurrencyUnit;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author idobre
 * @since 10/1/14
 */

@Component
public class ReportsControllerUtils {
    private static Logger logger = Logger.getLogger(ReportsControllerUtils.class);

    /**
     * Create a Mondrian connection
     *
     * @param applicationContext
     * @param cdaDataSource
     * @param request
     * @return
     */
    public static Connection createMondrianConnection(ApplicationContext applicationContext,
            DataSource cdaDataSource,
            HttpServletRequest request) {
        // create the Mondrian connection
        Util.PropertyList propertyList = new Util.PropertyList();
        propertyList.put("Provider", "mondrian");
        // don't add the Catalog here (but the Connect string must contain a property 'Catalog')
        // it will be added in Schema Processor
        propertyList.put("Catalog", "somePath");
        propertyList.put(RolapConnectionProperties.DynamicSchemaProcessor.toString(),
                "org.devgateway.eudevfin.reports.core.utils.SchemaProcessor");

        String currency = request.getParameter(ReportsController.REPORT_CURRENCY);
        if (currency == null || currency.equals("")) {
            currency = ReportsController.REPORT_DEFAULT_CURRENCY_CODE;
        }
        propertyList.put("CURRENCY", currency);

        LocaleHelper localeHelper = (LocaleHelper) applicationContext.getBean("localeHelperSession");
        String locale = (localeHelper.getLocale() != null) ? localeHelper.getLocale() : "en";
        propertyList.put("LOCALE", locale);

        // add the country currency parameter
        String countryCurrency = "";
        CurrencyUnit currencyForCountryIso = FinancialTransactionUtil
                .getCurrencyForCountryIso(AuthUtils
                        .getIsoCountryForCurrentUser());
        if (currencyForCountryIso != null) {
            countryCurrency = currencyForCountryIso.getCode();
        }
        propertyList.put("COUNTRY_CURRENCY", countryCurrency);

        Connection connection = DriverManager.getConnection(propertyList, null,
                cdaDataSource);

        return connection;
    }

    /**
     * Create the Advance Questionnaire report
     *
     * @param connection
     * @param yearParam
     * @param outputType
     * @param currency
     * @param dataSource
     * @return
     */
    public static ByteArrayOutputStream generateAdvanceQuestionnaire(Connection connection, String yearParam, String outputType,
            String currency, String dataSource) {
        int reportYear = Integer.parseInt(yearParam);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            InputStream inputStream = ReportsController.class.getClassLoader().
                    getResourceAsStream("org/devgateway/eudevfin/reports/core/aq/aq_master.jasper");
            Map<String, Object> parameters = new HashMap<String, Object>();
            Locale locale = connection.getLocale();
            parameters.put(JRParameter.REPORT_LOCALE, locale);

            // set resource bundle
            ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("org/devgateway/eudevfin/reports/i18n", locale);
            parameters.put(JRParameter.REPORT_RESOURCE_BUNDLE, resourceBundle);

            // set connection
            parameters.put(JRMondrianQueryExecuterFactory.PARAMETER_MONDRIAN_CONNECTION, connection);

            // set yearly parameters
            parameters.put("FIRST_YEAR", reportYear - 1);
            parameters.put("SECOND_YEAR", reportYear);
            parameters.put("EDITION_YEAR", reportYear + 1);
            parameters.put("FORM_DATASOURCE", getDataSourceString(dataSource));

            parameters.put("CURRENCY", currency);
            // add the path to sub-reports
            String subdirPath = "org/devgateway/eudevfin/reports/core/aq";
            parameters.put("SUBDIR_PATH", subdirPath);

            // put Reporting Country parameter
            String donorName = "";
            Organization organizationForCurrentUser = AuthUtils.getOrganizationForCurrentUser();

            if (organizationForCurrentUser != null) {
                donorName = organizationForCurrentUser.getDonorName();
            }
            parameters.put("REPORTING_COUNTRY", donorName);

            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(inputStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters);

            ReportExporter reportExporter = new ReportExporter();

            switch (outputType) {
                case ReportsController.OUTPUT_TYPE_PDF:
                    reportExporter.exportPDF(jasperPrint, baos);
                    break;
                case ReportsController.OUTPUT_TYPE_EXCEL:
                    reportExporter.exportXLS(jasperPrint, baos);
                    break;
                case ReportsController.OUTPUT_TYPE_HTML:
                    reportExporter.exportHTML(jasperPrint, baos);
                    break;
                case ReportsController.OUTPUT_TYPE_CSV:
                    reportExporter.exportCSV(jasperPrint, baos);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            try {
                baos.write("No data/Data Invalid".getBytes());
                logger.error("Error creating the AQ report", e);

                return baos;
            } catch (IOException ioEx) {
                ioEx.printStackTrace();
            }
        }

        return baos;
    }

    /**
     * Create the DAC1 report
     *
     * @param connection
     * @param rowReportDao
     * @param yearParam
     * @param outputType
     * @return
     */
    public static ByteArrayOutputStream generateDAC1(Connection connection, RowReportDao rowReportDao, String yearParam, String outputType) {
        int reportYear = Integer.parseInt(yearParam);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            InputStream inputStream = ReportsController.class
                    .getClassLoader()
                    .getResourceAsStream(
                            "org/devgateway/eudevfin/reports/core/dac1/dac1_template.jrxml");

            // Process template (injecting MDX; fields and text elements
            ReportTemplate reportProcessor = new ReportTemplate();
            InputStream inputStreamProcessed = reportProcessor.processTemplate(inputStream,	"[Type of Finance].[Code].Members", rowReportDao, false, "DAC1");

            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters
                    .put(JRMondrianQueryExecuterFactory.PARAMETER_MONDRIAN_CONNECTION,
                            connection);
            // set locale
            Locale locale = LocaleContextHolder.getLocale();
            parameters.put(JRParameter.REPORT_LOCALE, locale);

            // set resource bundle
            ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("org/devgateway/eudevfin/reports/i18n", locale);
            parameters.put(JRParameter.REPORT_RESOURCE_BUNDLE, resourceBundle);

            parameters.put("REPORTING_YEAR", reportYear);
            parameters.put("EDITION_YEAR", Calendar.getInstance().get(Calendar.YEAR));

            // put Reporting Country parameter
            String donorName = "";
            Organization organizationForCurrentUser = AuthUtils.getOrganizationForCurrentUser();

            if (organizationForCurrentUser != null) {
                donorName = organizationForCurrentUser.getDonorName();
            }
            parameters.put("REPORTING_COUNTRY", donorName);

            String serverInstance = donorName;
            String inputStreamMemoInterest = generateSubReportCached(rowReportDao, "DAC1MemoInterest", "org/devgateway/eudevfin/reports/core/dac1/dac1_template_interest", "[Type of Finance].[Code].Members", false, false, serverInstance);
            String inputStreamMemoExpert = generateSubReportCached(rowReportDao, "DAC1MemoExpert", "org/devgateway/eudevfin/reports/core/dac1/dac1_template_expert", "[Type of Finance].[Code].Members", false, false, serverInstance);

            parameters.put("INTEREST_SUBREPORT_PATH", inputStreamMemoInterest);
            parameters.put("EXPERT_SUBREPORT_PATH", inputStreamMemoExpert);
            parameters.put("KEYINDICATORS_SUBREPORT_PATH", "org/devgateway/eudevfin/reports/core/dac1/dac1_template_keyindicators.jasper");
            JasperDesign jasperDesign = JRXmlLoader.load(inputStreamProcessed);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters);

            ReportExporter reportExporter = new ReportExporter();

            switch (outputType) {
                case ReportsController.OUTPUT_TYPE_PDF:
                    reportExporter.exportPDF(jasperPrint, baos);
                    break;
                case ReportsController.OUTPUT_TYPE_EXCEL:
                    reportExporter.exportXLS(jasperPrint, baos);
                    break;
                case ReportsController.OUTPUT_TYPE_HTML:
                    reportExporter.exportHTML(jasperPrint, baos);
                    break;
                case ReportsController.OUTPUT_TYPE_CSV:
                    reportExporter.exportCSV(jasperPrint, baos);
                    break;
                default:
                    break;
            }

        } catch (JRException e) {
            try {
                baos.write("No data/Data Invalid".getBytes());
                logger.error("Error creating the DAC2a report", e);

                return baos;
            } catch (IOException ioEx) {
                ioEx.printStackTrace();
            }
        }

        return baos;
    }

    /**
     * Create the DAC2a report
     *
     * @param connection
     * @param rowReportDao
     * @param yearParam
     * @param outputType
     * @return
     */
    public static ByteArrayOutputStream generateDAC2a (Connection connection, RowReportDao rowReportDao, String yearParam, String outputType) {
        int reportYear = Integer.parseInt(yearParam);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            Map<String, Object> parameters = new HashMap<String, Object>();

            // Assign Connection
            parameters.put(JRMondrianQueryExecuterFactory.PARAMETER_MONDRIAN_CONNECTION, connection);

            parameters.put("REPORTING_YEAR", reportYear);
            parameters.put("EDITION_YEAR", Calendar.getInstance().get(Calendar.YEAR));

            // Assign Locale
            Locale locale = LocaleContextHolder.getLocale();
            parameters.put(JRParameter.REPORT_LOCALE, locale);

            // Assign Resource Bundle
            ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle(
                    "org/devgateway/eudevfin/reports/i18n", locale);
            parameters.put(JRParameter.REPORT_RESOURCE_BUNDLE, resourceBundle);

            // Assign Reporting Country
            String donorName = "";
            Organization organizationForCurrentUser = AuthUtils.getOrganizationForCurrentUser();
            if (organizationForCurrentUser != null) {
                donorName = organizationForCurrentUser.getDonorName();
            }
            parameters.put("REPORTING_COUNTRY", donorName);

            // Generate and Assign Sub Reports
            long startTime = System.nanoTime();

            String serverInstance = donorName;

            String inputStreamArea = generateSubReportCached(
                    rowReportDao,
                    "DAC2aArea",
                    "org/devgateway/eudevfin/reports/core/dac2a/dac2a_template_area",
                    "[Area].[Code].Members", false, true, serverInstance);
            String inputStreamChannel = generateSubReportCached(
                    rowReportDao,
                    "DAC2aChannel",
                    "org/devgateway/eudevfin/reports/core/dac2a/dac2a_template_channel",
                    "[Channel].[Code].Members", false, true, serverInstance);
            parameters.put("AREA_SUBREPORT_PATH", inputStreamArea);
            parameters.put("CHANNEL_SUBREPORT_PATH", inputStreamChannel);
            long endTime = System.nanoTime();
            logger.info("Time to retrieve reports:" + (endTime - startTime));

            // Generate and Assign Sub Reports
            startTime = System.nanoTime();
            //Process the main report with the subreports
            ReportTemplate reportProcessor = new ReportTemplate();
            InputStream inputStream = reportProcessor
                    .processTemplate(
                            ReportsController.class
                                    .getClassLoader()
                                    .getResourceAsStream(
                                            "org/devgateway/eudevfin/reports/core/dac2a/dac2a_template.jrxml"),
                            "[BiMultilateral].[Code].Members", rowReportDao,
                            true, "DAC2a");
            JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            endTime = System.nanoTime();
            logger.info("Time to compile reports:" + (endTime - startTime));
            startTime = System.nanoTime();
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters);
            endTime = System.nanoTime();
            logger.info("Time to fill reports:" + (endTime - startTime));

            // Write it to the output
            ReportExporter reportExporter = new ReportExporter();

            switch (outputType) {
                case ReportsController.OUTPUT_TYPE_PDF:
                    reportExporter.exportPDF(jasperPrint, baos);
                    break;
                case ReportsController.OUTPUT_TYPE_EXCEL:
                    reportExporter.exportXLS(jasperPrint, baos);
                    break;
                case ReportsController.OUTPUT_TYPE_HTML:
                    reportExporter.exportHTML(jasperPrint, baos);
                    break;
                case ReportsController.OUTPUT_TYPE_CSV:
                    reportExporter.exportCSV(jasperPrint, baos);
                    break;
                default:
                    break;
            }
        } catch (JRException e) {
            try {
                baos.write("No data/Data Invalid".getBytes());
                logger.error("Error creating the DAC2a report", e);

                return baos;
            } catch (IOException ioEx) {
                ioEx.printStackTrace();
            }
        }

        return baos;
    }

    /**
     * Generates a named Subreport passing it through the template processor to insert elements that make a report.
     *
     * @param reportName Name of the report for fetching the related rows
     * @param path Path to the subreport
     * @param slicer Dimension by which the rows will be disaggregated
     * @param regenerate Specify if it ignores existing versions and regenerates the jrxml file
     * @param swapAxis Toggle rows/columns (for inverted reports)
     * @param serverInstance Specific server to avoid naming problems
     * @return path to the generated jrxml file to be passed on path to the subreport
     */
    public static String generateSubReportCached(RowReportDao rowReportDao, final String reportName, final String path,
            final String slicer, final Boolean regenerate, final boolean swapAxis,
            final String serverInstance) {
        //Check if the compiled report file already exists in temporary folder
        String tmpDirPath = System.getProperty("java.io.tmpdir");
        File f = new File(tmpDirPath + File.separator + reportName + "_" + serverInstance + "_processed.jasper");
        if (f.exists() && !regenerate) {
            String cachedFilePath = tmpDirPath
                    + File.separator + reportName + "_" + serverInstance + "_processed.jasper";
            logger.info("The report " + reportName + " is being cached from " + cachedFilePath);
            return cachedFilePath;
        }

        logger.info("The report " + reportName + " hasn't been parsed and cached");
        InputStream inputStream = ReportsController.class.getClassLoader().getResourceAsStream(path + ".jrxml");

        ReportTemplate reportProcessor = new ReportTemplate();
        InputStream inputStreamProcessed = reportProcessor.processTemplate(
                inputStream, slicer, rowReportDao, swapAxis, reportName);
        String newJrxmlFilename = tmpDirPath + File.separator + reportName
                + "_" + serverInstance + "_processed.jrxml";
        File processedFile = new File(newJrxmlFilename);
        logger.info("Creating file " + newJrxmlFilename);
        try {
            processedFile.createNewFile();
            processedFile.deleteOnExit();
            try (FileOutputStream out = new FileOutputStream(processedFile)) {
                IOUtils.copy(inputStreamProcessed, out);
            }
            String jrxmlFileName = processedFile.getAbsolutePath();
            String jasperFileName = processedFile.getAbsolutePath().replace(".jrxml", ".jasper");
            JasperCompileManager.compileReportToFile(jrxmlFileName, jasperFileName);
            logger.info("Compiling " + jrxmlFileName + " into " + jasperFileName);
            return jasperFileName;
        } catch (IOException | JRException e) {
            logger.error("Subreport Generation Failed:" + e.getMessage());
        }
        return null;
    }

    private static String getDataSourceString(String dataSource) {
        if (ReportsController.REPORT_DATASOURCE_CRS.equals(dataSource)) {
            return "{[Form Type].[bilateralOda.CRS], [Form Type].[multilateralOda.CRS]}";
        }
        else {
            return "{[Form Type].[bilateralOda.advanceQuestionnaire], [Form Type].[multilateralOda.advanceQuestionnaire]}";
        }
    }
}
