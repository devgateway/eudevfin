package org.devgateway.eudevfin.reports;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
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
import mondrian.olap.Query;
import mondrian.olap.Result;
import mondrian.olap.Util.PropertyList;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.olap.JRMondrianQueryExecuterFactory;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

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
	
	@RequestMapping(value = "/mondrian", method = RequestMethod.GET)
    public ModelAndView generateMondrianReport(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView)  throws IOException {
		logger.info(">>> generate raport with Mondrian");
				
		PropertyList propertyList = new PropertyList();
		propertyList.put("Provider", "mondrian");
		propertyList.put("Catalog",
				this.getClass().getResource("./financial.mondrian.xml")
						.toString());
		Connection conn = DriverManager.getConnection(propertyList, null,
				cdaDataSource);
		
		// used to test the connection
		Query query = conn.parseQuery(
			    "SELECT NON EMPTY {Hierarchize({[Measures].[Commitments Amount]})} ON COLUMNS," +
			    " NON EMPTY CrossJoin({[BiMultilateral].[Bilateral en]}, [Type of Aid].[Name].Members) ON ROWS " +
			    "FROM [Financial]");
		@SuppressWarnings("deprecation")
		Result result = conn.execute(query);
		logger.info(result.getAxes().length);
		
		// parameters is used for passing extra parameters 
		Map<String, Object> parameters = new HashMap<String, Object>();		
		
		InputStream inputStream;
		try {
			inputStream = ReportsController.class.getResourceAsStream("./jasper_template_mondrian.jrxml");
			// retrieve the report template
			JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
			// compile the report layout
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			
			// retrieve and compile the sub-report
			JasperReport subreport1 = JasperCompileManager.compileReport(
					JRXmlLoader.load(
							ReportsController.class.getResourceAsStream("./report1_subreport3.jrxml")));

			parameters.put(JRMondrianQueryExecuterFactory.PARAMETER_MONDRIAN_CONNECTION, conn);
			parameters.put("Subreport1", subreport1);
			// pass query parameters
			parameters.put("DIM_NAME", "Name");
			
			// set locale
			Locale locale = new Locale("ro");
			parameters.put(JRParameter.REPORT_LOCALE, locale);
			
			// set resource bundle
			try {
				URL[] urls = {this.getClass().getResource("./").toURI().toURL()};
				ClassLoader loader = new URLClassLoader(urls);
				ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("i18n", locale, loader); 
				parameters.put(JRParameter.REPORT_RESOURCE_BUNDLE, resourceBundle);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}  
			
			// creates the JasperPrint object, it needs a JasperReport layout and extra params
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters);
			
			// this is the stream where the data will be written
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			// export XLS to output stream
			ReportExporter reportExporter = new ReportExporter();
			reportExporter.exportXLS(jasperPrint, baos);
			
			// set the response properties
			String fileName = "Report.xls";
			response.setHeader("Content-Disposition", "inline; filename=" + fileName);
			
			// make sure to set the correct content type
			response.setContentType("application/vnd.ms-excel");
			response.setContentLength(baos.size());
			 
			// write to response stream
			this.writeReportToResponseStream(response, baos);
		} catch (JRException e) {
			e.printStackTrace();
		}
		
        return null;
    }
	
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
		// TODO: Find a way of sharing the Schema files across the app, probably by asking the CDA Wrapper where the file is and having a default value.
		propertyList.put("Catalog",
				this.getClass().getResource("./financial.mondrian.xml").toString());
		Connection connection = DriverManager.getConnection(propertyList, null, cdaDataSource);
		
		// add default values
		if (reportType == null || reportType.equals("")) {
			reportType = REPORT_TYPE_AQ;
		}
		
		if (outputType == null || outputType.equals("")) {
			outputType = OUTPUT_TYPE_HTML;
		}
		
		switch (reportType) {
			case REPORT_TYPE_AQ:
				generateAdvancedQuestionnaire(request, response, connection, outputType);
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
			Connection connection, String outputType) {
		String yearParam = request.getParameter(REPORT_YEAR);
		int reportYear = Integer.parseInt(yearParam);
		
		try {
			InputStream inputStream = ReportsController.class.getResourceAsStream("./aq/aq_master.jrxml");
			
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(JRMondrianQueryExecuterFactory.PARAMETER_MONDRIAN_CONNECTION, connection);
			parameters.put("FIRST_YEAR", reportYear - 1);
			parameters.put("SECOND_YEAR", reportYear);

			InputStream parsedInputStream = parseInputStream(inputStream, parameters);
			
			JasperDesign jasperDesign = JRXmlLoader.load(parsedInputStream);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			JasperReport bilateralToAYear = JasperCompileManager.compileReport(
					JRXmlLoader.load(
							ReportsController.class.getResourceAsStream("./aq/aq_master_bilateral_toa_year.jrxml")));
			parameters.put("bilateralToAYear", bilateralToAYear);

			JasperReport bilateralToFYear = JasperCompileManager.compileReport(
					JRXmlLoader.load(
							ReportsController.class.getResourceAsStream("./aq/aq_master_bilateral_tof_year.jrxml")));
			parameters.put("bilateralToFYear", bilateralToFYear);

			JasperReport multilateralYear = JasperCompileManager.compileReport(
					JRXmlLoader.load(
							ReportsController.class.getResourceAsStream("./aq/aq_master_multilateral_org_year.jrxml")));
			parameters.put("multilateralYear", multilateralYear);

			JasperReport memoRegion = JasperCompileManager.compileReport(
					JRXmlLoader.load(
							ReportsController.class.getResourceAsStream("./aq/aq_master_memo_region_year.jrxml")));
			parameters.put("memoRegion", memoRegion);

			JasperReport memoSector = JasperCompileManager.compileReport(
					JRXmlLoader.load(
							ReportsController.class.getResourceAsStream("./aq/aq_master_memo_sector_year.jrxml")));
			parameters.put("memoSector", memoSector);

			JasperReport memoDebtRelief = JasperCompileManager.compileReport(
					JRXmlLoader.load(
							ReportsController.class.getResourceAsStream("./aq/aq_master_memo_debtrelief_year.jrxml")));
			parameters.put("memoDebtRelief", memoDebtRelief);

			JasperReport memoTotalODA = JasperCompileManager.compileReport(
					JRXmlLoader.load(
							ReportsController.class.getResourceAsStream("./aq/aq_master_memo_totaloda_year.jrxml")));
			parameters.put("memoTotalODA", memoTotalODA);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
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
		} catch (JRException e) {
			e.printStackTrace();
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
			InputStream inputStream = ReportsController.class.getResourceAsStream("./dac1_master.jrxml");
			
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(JRMondrianQueryExecuterFactory.PARAMETER_MONDRIAN_CONNECTION, connection);
			
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
	
	private InputStream parseInputStream(InputStream inputStream, Map<String, Object> parameters) {
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(inputStream));
        InputStream parsedInputStream = null;

        StringBuilder sb = new StringBuilder();
        String inline = "";
        try {
			while ((inline = inputReader.readLine()) != null) {
			  sb.append(inline);
			  sb.append(System.getProperty("line.separator"));
			}
			String originalXML = sb.toString();
			
			for(String key : parameters.keySet()) {
				String token = "@@" + key + "@@";
				String value;
				Object obj = parameters.get(key);
				if(obj.getClass() == Integer.class){
					value = ((Integer)obj).toString();
				}
				else
				{
					value = obj.toString();
				}
				originalXML = originalXML.replaceAll(token, value);
			}
			
			parsedInputStream = new ByteArrayInputStream(originalXML.getBytes("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		return parsedInputStream;
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
