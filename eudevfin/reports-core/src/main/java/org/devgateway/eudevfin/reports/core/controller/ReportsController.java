package org.devgateway.eudevfin.reports.core.controller;

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

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import mondrian.olap.Connection;
import mondrian.olap.DriverManager;
import mondrian.olap.Util.PropertyList;
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
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.auth.common.util.AuthUtils;
import org.devgateway.eudevfin.common.locale.LocaleHelper;
import org.devgateway.eudevfin.financial.util.FinancialTransactionUtil;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.devgateway.eudevfin.reports.core.dao.RowReportDao;
import org.devgateway.eudevfin.reports.core.utils.ReportExporter;
import org.devgateway.eudevfin.reports.core.utils.ReportTemplate;
import org.joda.money.CurrencyUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ReportsController {
	@Autowired
	private DataSource cdaDataSource;

	@Autowired
	private RowReportDao rowReportDao;

    private static Logger logger = Logger.getLogger(ReportsController.class);

	private static final String REPORT_TYPE = "reportType";
	private static final String REPORT_TYPE_AQ = "aq";
	private static final String REPORT_TYPE_DAC1 = "dac1";
	private static final String REPORT_TYPE_DAC2A = "dac2a";
	private static final String OUTPUT_TYPE = "outputType";
	private static final String OUTPUT_TYPE_PDF = "pdf";
	private static final String OUTPUT_TYPE_EXCEL = "excel";
	private static final String OUTPUT_TYPE_HTML = "html";
	private static final String OUTPUT_TYPE_CSV = "csv";
	private static final String REPORT_YEAR = "reportYear";
	private static final String REPORT_CURRENCY = "reportCurrency";
	private static final String REPORT_DEFAULT_CURRENCY_CODE = "USD";

	@Autowired
	ApplicationContext applicationContext;

	/**
	 * Generate a report
	 */
	@PreAuthorize("hasRole('" + AuthConstants.Roles.ROLE_USER + "')")
    @RequestMapping(value = "/generate", method = {
    			RequestMethod.GET, RequestMethod.POST
    		})
    public ModelAndView generateReport(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView)  throws IOException {
		String reportType = request.getParameter(REPORT_TYPE);
		String outputType = request.getParameter(OUTPUT_TYPE);

		// create the Mondrian connection
		PropertyList propertyList = new PropertyList();
		propertyList.put("Provider", "mondrian");
		// don't add the Catalog here (but the Connect string must contain a property 'Catalog')
		// it will be added in Schema Processor
		propertyList.put("Catalog", "somePath");
        propertyList.put(RolapConnectionProperties.DynamicSchemaProcessor.toString(),
				"org.devgateway.eudevfin.reports.core.utils.SchemaProcessor");

		String currency = request.getParameter(REPORT_CURRENCY);
		if (currency == null || currency.equals("")) {
			currency = REPORT_DEFAULT_CURRENCY_CODE;
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

		// add default values
		if (reportType == null || reportType.equals("")) {
			reportType = REPORT_TYPE_AQ;
		}

		if (outputType == null || outputType.equals("")) {
			outputType = OUTPUT_TYPE_HTML;
		}

		switch (reportType) {
		case REPORT_TYPE_AQ:
            generateAdvanceQuestionnaire(request, response, connection, outputType, currency);
			break;
		case REPORT_TYPE_DAC1:
			generateDAC1(request, response, connection, outputType);
			break;
		case REPORT_TYPE_DAC2A:
			generateDAC2a(request, response, connection, outputType);
			break;
		default:
			break;
		}

		return null;
	}

	/**
	 * Create the Advance Questionnaire report
	 * 
	 * @param request
	 * @param response
     * @param connection the Mondrian connection
     * @param outputType the output for the report: HTML, Excel, PDF, CSV
	 */
    private void generateAdvanceQuestionnaire (HttpServletRequest request, HttpServletResponse response,
            Connection connection, String outputType, String currency) {
		String yearParam = request.getParameter(REPORT_YEAR);
		if (yearParam == null || yearParam.equals("")) {
            yearParam = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
		}
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
			String fileName = "";

			switch (outputType) {
			case OUTPUT_TYPE_PDF:
				reportExporter.exportPDF(jasperPrint, baos);
				fileName = "Advance Questionnaire.pdf";
                    response.setHeader("Content-Disposition", "inline; filename=" + fileName);
				response.setContentType("application/pdf");
				break;
			case OUTPUT_TYPE_EXCEL:
				reportExporter.exportXLS(jasperPrint, baos);
				fileName = "Advance Questionnaire.xls";
                    response.setHeader("Content-Disposition", "inline; filename=" + fileName);
				response.setContentType("application/vnd.ms-excel");
				break;
			case OUTPUT_TYPE_HTML:
				reportExporter.exportHTML(jasperPrint, baos);
				fileName = "Advance Questionnaire.html";
                    response.setHeader("Content-Disposition", "inline; filename=" + fileName);
				response.setContentType("text/html");
				break;
			case OUTPUT_TYPE_CSV:
				reportExporter.exportCSV(jasperPrint, baos);
				fileName = "Advance Questionnaire.csv";
                    response.setHeader("Content-Disposition", "inline; filename=" + fileName);
				response.setContentType("text/csv");
				break;
			default:
				break;
			}

			response.setContentLength(baos.size());

			// write to response stream
			this.writeReportToResponseStream(response, baos);
		} catch (Exception e) {
			response.setContentType("text/html");

			try {
				baos.write("No data/Data Invalid".getBytes());
				logger.error("Error creating the AQ report", e);
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}
			this.writeReportToResponseStream(response, baos);
		}
	}

	/**
	 * Create the DAC2a report
	 * 
	 * @param request
	 * @param response
     * @param connection the Mondrian connection
     * @param outputType the output for the report: HTML, Excel, PDF, CSV
	 */
    private void generateDAC2a (HttpServletRequest request, HttpServletResponse response, Connection connection, String outputType) {
		try {
			
			Map<String, Object> parameters = new HashMap<String, Object>();

			// Assign Connection
			parameters.put(JRMondrianQueryExecuterFactory.PARAMETER_MONDRIAN_CONNECTION, connection);

			// Assign Reporting Year
			String yearParam = request.getParameter(REPORT_YEAR);
			if (yearParam == null || yearParam.equals("")) {
	            yearParam = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
			}
			int reportYear = Integer.parseInt(yearParam);
			parameters.put("REPORTING_YEAR", reportYear);

			// Assign Locale
			Locale locale = LocaleContextHolder.getLocale();
			parameters.put(JRParameter.REPORT_LOCALE, locale);

			// Assign Resource Bundle
            ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("org/devgateway/eudevfin/reports/i18n", locale);
			parameters.put(JRParameter.REPORT_RESOURCE_BUNDLE, resourceBundle);

			// Assign Reporting Country
			String donorName = "";
            Organization organizationForCurrentUser = AuthUtils.getOrganizationForCurrentUser();
			if (organizationForCurrentUser != null) {
				donorName = organizationForCurrentUser.getDonorName();
			}
			parameters.put("REPORTING_COUNTRY", donorName);

			// Generate and Assign Sub Reports
			String inputStreamArea = generateSubReportCached("DAC2aArea", "org/devgateway/eudevfin/reports/core/dac2a/dac2a_template_area", "[Area].Members", false);
			String inputStreamChannel = generateSubReportCached("DAC2aChannel", "org/devgateway/eudevfin/reports/core/dac2a/dac2a_template_channel", "[Channel].Members", false);
			parameters.put("AREA_SUBREPORT_PATH", inputStreamArea);
			parameters.put("CHANNEL_SUBREPORT_PATH", inputStreamChannel);

			//Process the main report with the subreports
			ReportTemplate reportProcessor = new ReportTemplate();
			InputStream inputStream = reportProcessor.processTemplate(ReportsController.class.getClassLoader().getResourceAsStream("org/devgateway/eudevfin/reports/core/dac2a/dac2a_template.jrxml"), "[BiMultilateral].[Code].Members", rowReportDao, true, "DAC2a");
			JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters);

			//Write it to the output
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ReportExporter reportExporter = new ReportExporter();
			String fileName = "";

			switch (outputType) {
				case OUTPUT_TYPE_PDF:
					reportExporter.exportPDF(jasperPrint, baos);
					fileName = "DAC 2a.pdf";
	                    response.setHeader("Content-Disposition", "inline; filename=" + fileName);
					response.setContentType("application/pdf");
					break;
				case OUTPUT_TYPE_EXCEL:
					reportExporter.exportXLS(jasperPrint, baos);
					fileName = "DAC 2a.xls";
	                    response.setHeader("Content-Disposition", "inline; filename=" + fileName);
					response.setContentType("application/vnd.ms-excel");
					break;
				case OUTPUT_TYPE_HTML:
					reportExporter.exportHTML(jasperPrint, baos);
					fileName = "DAC 2a.html";
	                    response.setHeader("Content-Disposition", "inline; filename=" + fileName);
					response.setContentType("text/html");
					break;
				case OUTPUT_TYPE_CSV:
					reportExporter.exportCSV(jasperPrint, baos);
					fileName = "DAC 2a.csv";
	                    response.setHeader("Content-Disposition", "inline; filename=" + fileName);
					response.setContentType("text/csv");
					break;
				default:
					break;
			}

			response.setContentLength(baos.size());

			// write to response stream
			this.writeReportToResponseStream(response, baos);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}

	private void generateDAC1(HttpServletRequest request,
			HttpServletResponse response, Connection connection,
			String outputType) {
		try {
			InputStream inputStream = ReportsController.class
					.getClassLoader()
					.getResourceAsStream(
							"org/devgateway/eudevfin/reports/core/dac1/dac1_template.jrxml");
			
			String yearParam = request.getParameter(REPORT_YEAR);
			if (yearParam == null || yearParam.equals("")) {
	            yearParam = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
			}
			int reportYear = Integer.parseInt(yearParam);
			
			//Process template (injecting MDX; fields and text elements
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
			// put Reporting Country parameter
			String donorName = "";
            Organization organizationForCurrentUser = AuthUtils.getOrganizationForCurrentUser();

			if (organizationForCurrentUser != null) {
				donorName = organizationForCurrentUser.getDonorName();
			}
			parameters.put("REPORTING_COUNTRY", donorName);

			JasperDesign jasperDesign = JRXmlLoader.load(inputStreamProcessed);
			JasperReport jasperReport = JasperCompileManager
					.compileReport(jasperDesign);

			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ReportExporter reportExporter = new ReportExporter();
			String fileName = "";

			switch (outputType) {
			case OUTPUT_TYPE_PDF:
				reportExporter.exportPDF(jasperPrint, baos);
				fileName = "DAC 1.pdf";
				response.setHeader("Content-Disposition", "inline; filename="
						+ fileName);
				response.setContentType("application/pdf");
				break;
			case OUTPUT_TYPE_EXCEL:
				reportExporter.exportXLS(jasperPrint, baos);
				fileName = "DAC 1.xls";
				response.setHeader("Content-Disposition", "inline; filename="
						+ fileName);
				response.setContentType("application/vnd.ms-excel");
				break;
			case OUTPUT_TYPE_HTML:
				reportExporter.exportHTML(jasperPrint, baos);
				fileName = "DAC 1.html";
				response.setHeader("Content-Disposition", "inline; filename="
						+ fileName);
				response.setContentType("text/html");
				break;
			case OUTPUT_TYPE_CSV:
				reportExporter.exportCSV(jasperPrint, baos);
				fileName = "DAC 1.csv";
				response.setHeader("Content-Disposition", "inline; filename="
						+ fileName);
				response.setContentType("text/csv");
				break;
			default:
				break;
			}

			response.setContentLength(baos.size());

			// write to response stream
			this.writeReportToResponseStream(response, baos);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Generates a named Subreport passing it through the template processor to insert elements that make a report
	 * 
	 * @param reportName
	 * @param path
     * @param slicer
     * @returns path to the generated jrxml file to be passed on path to the subreport
	 */
	private String generateSubReport(String reportName, String path, String slicer) {
		InputStream inputStream = ReportsController.class.getClassLoader().getResourceAsStream(path + ".jrxml");
		ReportTemplate reportProcessor = new ReportTemplate();
		InputStream inputStreamProcessed = reportProcessor.processTemplate(inputStream,	slicer, rowReportDao, true, reportName);
        File tempFile;
		try {
			tempFile = File.createTempFile(reportName,"_processed.jrxml");
	        tempFile.deleteOnExit();
	        
	        try (FileOutputStream out = new FileOutputStream(tempFile)) {
	            IOUtils.copy(inputStreamProcessed, out);
	        }
	        return tempFile.getAbsolutePath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
    private String generateSubReportCached(String reportName, String path, String slicer, Boolean regenerate) {
    	//Check if the compiled report file already exists in temporary folder
    	String tmpDirPath = System.getProperty("java.io.tmpdir");
    	File f = new File(tmpDirPath + reportName + "_processed.jasper");
        if(f.exists() && !regenerate){
        	String cachedFilePath = tmpDirPath + reportName + "_processed.jasper";
        	logger.info("The report " + reportName + " is being cached from " + cachedFilePath);
            return cachedFilePath;
        }
        
    	logger.info("The report " + reportName + " hasn't been parsed and cached");
        InputStream inputStream = ReportsController.class.getClassLoader().getResourceAsStream(path + ".jrxml");

        ReportTemplate reportProcessor = new ReportTemplate();
        InputStream inputStreamProcessed = reportProcessor.processTemplate(inputStream,    slicer, rowReportDao, true, reportName);
        String newJrxmlFilename = tmpDirPath  + reportName + "_processed.jrxml";
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

	
	/**
	 * Writes the report to the output stream
	 */
    private void writeReportToResponseStream(HttpServletResponse response, ByteArrayOutputStream baos) {
		logger.debug("Writing report to the stream");
		try {
			// retrieve the output stream
			ServletOutputStream outputStream = response.getOutputStream();
			// write and flush to the output stream
			baos.writeTo(outputStream);
			outputStream.flush();
		} catch (Exception e) {
			logger.error("Unable to write report to the output stream");
		}
	}

}
