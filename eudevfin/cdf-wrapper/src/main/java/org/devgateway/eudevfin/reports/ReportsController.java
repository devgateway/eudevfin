package org.devgateway.eudevfin.reports;

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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

@Controller
public class ReportsController {
	@Autowired
	private DataSource cdaDataSource;
	
	private static final Logger logger = Logger.getLogger(ReportsController.class);

	private static final String REPORT_TYPE = "reportType";
	private static final String REPORT_TYPE_AQ = "aq";
	private static final String REPORT_TYPE_DAC1 = "dac1";
	private static final String REPORT_TYPE_DAC2 = "dac2";
	private static final String OUTPUT_TYPE = "outputType";
	private static final String OUTPUT_TYPE_PDF = "pdf";
	private static final String OUTPUT_TYPE_EXCEL = "excel";
	private static final String OUTPUT_TYPE_HTML = "html";
	private static final String OUTPUT_TYPE_CSV = "csv";
	private static final String REPORT_YEAR = "reportYear";
	private static final String REPORT_CURRENCY = "reportCurrency";
	private static final String REPORT_DEFAULT_CURRENCY_CODE = "USD";
	
	/**
	 * Generate a report
	 */
	@RequestMapping(value = "/generate", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView generateReport(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView)  throws IOException {
		String reportType = request.getParameter(REPORT_TYPE);
		String outputType = request.getParameter(OUTPUT_TYPE);
		
		// create the Mondrian connection
		PropertyList propertyList = new PropertyList();
		propertyList.put("Provider", "mondrian");		
		propertyList.put("Catalog",
				this.getClass().getResource("/org/devgateway/eudevfin/cda/service/financial.mondrian.xml").toString());
		propertyList.put(RolapConnectionProperties.DynamicSchemaProcessor.toString(),
	            "org.devgateway.eudevfin.cda.utils.SchemaProcessor");
		
		String currency = request.getParameter(REPORT_CURRENCY);
		if (currency == null || currency.equals("")) {
			currency = REPORT_DEFAULT_CURRENCY_CODE;
		}
		propertyList.put("CURRENCY", currency);

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
				generateAdvancedQuestionnaire(request, response, connection, outputType, currency);
	            break;
	        case REPORT_TYPE_DAC1:
	        	generateDAC1(request, response, connection, outputType);
	            break;
	        case REPORT_TYPE_DAC2:
	        	generateDAC2(request, response, connection, outputType);
	            break;
	        default: 
	        	break;
		}
		
        return null;
    }
	
	/**
	 * Create the Advanced Questionnaire report
	 * 
	 * @param request
	 * @param response
	 * @param connection the Mondrian connection
	 * @param outputType the output for the report: HTML, Excel, PDF, CSV
	 */
	private void generateAdvancedQuestionnaire (HttpServletRequest request, HttpServletResponse response, 
			Connection connection, String outputType, String currency) {
		String yearParam = request.getParameter(REPORT_YEAR);
		if (yearParam == null || yearParam.equals("")){
			yearParam = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
		}
		int reportYear = Integer.parseInt(yearParam);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		try {
			InputStream inputStream = ReportsController.class.getResourceAsStream("./aq/aq_master.jasper");
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			// set locale
			Locale locale = LocaleContextHolder.getLocale();			
			parameters.put(JRParameter.REPORT_LOCALE, locale);
			
			// set resource bundle
			try {
				URL[] urls = {
						this.getClass().getResource("/org/devgateway/eudevfin/reports/").toURI().toURL()
				};
				ClassLoader loader = new URLClassLoader(urls);
				ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("i18n", locale, loader); 
				parameters.put(JRParameter.REPORT_RESOURCE_BUNDLE, resourceBundle);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

			//set connection
			parameters.put(JRMondrianQueryExecuterFactory.PARAMETER_MONDRIAN_CONNECTION, connection);
			
			//set yearly parameters
			parameters.put("FIRST_YEAR", reportYear - 1);
			parameters.put("SECOND_YEAR", reportYear);
			// TODO: Determine and get the values of exchange rates that will be used
			parameters.put("FIRST_YEAR_EXRATE", 1);
			parameters.put("SECOND_YEAR_EXRATE", 1);

			parameters.put("CURRENCY", currency);
			try {
				String subdirPath = new URI(this.getClass().getResource("./aq").toString()).getPath();
				parameters.put("SUBDIR_PATH", subdirPath);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}  
			
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(inputStream);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters);
			
			ReportExporter reportExporter = new ReportExporter();
			String fileName = "";
			
			switch (outputType) {
				case OUTPUT_TYPE_PDF:
					reportExporter.exportPDF(jasperPrint, baos);
					fileName = "Advanced Questionnaire.pdf";
					response.setHeader("Content-Disposition", "inline; filename=" + fileName);
					response.setContentType("application/pdf");
		            break;
		        case OUTPUT_TYPE_EXCEL:
		        	reportExporter.exportXLS(jasperPrint, baos);
					fileName = "Advanced Questionnaire.xls";
					response.setHeader("Content-Disposition", "inline; filename=" + fileName);
					response.setContentType("application/vnd.ms-excel");
		            break;
		        case OUTPUT_TYPE_HTML:
		        	reportExporter.exportHTML(jasperPrint, baos);
					fileName = "Advanced Questionnaire.html";
					response.setHeader("Content-Disposition", "inline; filename=" + fileName);
					response.setContentType("text/html");
		            break;
		        case OUTPUT_TYPE_CSV:
		        	reportExporter.exportCSV(jasperPrint, baos);
					fileName = "Advanced Questionnaire.csv";
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
				logger.error(e.getMessage());
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}
			this.writeReportToResponseStream(response, baos);
		}
	}
	
	/**
	 * Create the DAC 1 report
	 * 
	 * @param request
	 * @param response
	 * @param connection the Mondrian connection
	 * @param outputType the output for the report: HTML, Excel, PDF, CSV
	 */
	private void generateDAC1 (HttpServletRequest request, HttpServletResponse response, 
			Connection connection, String outputType) {
		try {
			InputStream inputStream = ReportsController.class.getResourceAsStream("./dac1/dac1_master.jrxml");
			
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(JRMondrianQueryExecuterFactory.PARAMETER_MONDRIAN_CONNECTION, connection);
			try {
				String subdirPath = new URI(this.getClass().getResource("./dac1").toString()).getPath();
				parameters.put("SUBDIR_PATH", subdirPath);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			
			// set locale
			Locale locale = LocaleContextHolder.getLocale();			
			parameters.put(JRParameter.REPORT_LOCALE, locale);
			
			// set resource bundle
			try {
				URL[] urls = {
						this.getClass().getResource("/org/devgateway/eudevfin/reports/").toURI().toURL()
				};
				ClassLoader loader = new URLClassLoader(urls);
				ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("i18n", locale, loader); 
				parameters.put(JRParameter.REPORT_RESOURCE_BUNDLE, resourceBundle);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			
			JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
	
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters);
	
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ReportExporter reportExporter = new ReportExporter();
			String fileName = "";
			
			switch (outputType) {
				case OUTPUT_TYPE_PDF:
					reportExporter.exportPDF(jasperPrint, baos);
					fileName = "DAC 1.pdf";
					response.setHeader("Content-Disposition", "inline; filename=" + fileName);
					response.setContentType("application/pdf");
		            break;
		        case OUTPUT_TYPE_EXCEL:
		        	reportExporter.exportXLS(jasperPrint, baos);
					fileName = "DAC 1.xls";
					response.setHeader("Content-Disposition", "inline; filename=" + fileName);
					response.setContentType("application/vnd.ms-excel");
		            break;
		        case OUTPUT_TYPE_HTML:
		        	reportExporter.exportHTML(jasperPrint, baos);
					fileName = "DAC 1.html";
					response.setHeader("Content-Disposition", "inline; filename=" + fileName);
					response.setContentType("text/html");
		            break;
		        case OUTPUT_TYPE_CSV:
		        	reportExporter.exportCSV(jasperPrint, baos);
					fileName = "DAC 1.csv";
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
	
	/**
	 * Create the DAC 2 report2
	 * 
	 * @param request
	 * @param response
	 * @param connection the Mondrian connection
	 * @param outputType the output for the report: HTML, Excel, PDF, CSV
	 */
	private void generateDAC2 (HttpServletRequest request, HttpServletResponse response, 
			Connection connection, String outputType) {
		
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
