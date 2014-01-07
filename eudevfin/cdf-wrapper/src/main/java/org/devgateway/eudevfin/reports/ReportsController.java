package org.devgateway.eudevfin.reports;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

	@RequestMapping(value = "/advance_questionnaire", method = RequestMethod.GET)
    public ModelAndView generateAQReport(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView)  throws IOException {

		PropertyList propertyList = new PropertyList();
		propertyList.put("Provider", "mondrian");
		propertyList.put("Catalog",
				this.getClass().getResource("./financial.mondrian.xml")
						.toString());
		Connection conn = DriverManager.getConnection(propertyList, null,
				cdaDataSource);

		Map<String, Object> parameters = new HashMap<String, Object>();		
		
		InputStream inputStream;
		try {
			inputStream = ReportsController.class.getResourceAsStream("./advanced_questionnaire_master.jrxml");
			JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

			JasperReport bilateralToASubreport = JasperCompileManager.compileReport(
					JRXmlLoader.load(
							ReportsController.class.getResourceAsStream("./advanced_questionnaire_bilateral_type_of_aid.jrxml")));
			JasperReport bilateralToFSubreport = JasperCompileManager.compileReport(
					JRXmlLoader.load(
							ReportsController.class.getResourceAsStream("./advanced_questionnaire_bilateral_financial_instrument.jrxml")));

			parameters.put(JRMondrianQueryExecuterFactory.PARAMETER_MONDRIAN_CONNECTION, conn);
			parameters.put("bilateralToASubreport", bilateralToASubreport);
			parameters.put("bilateralToFSubreport", bilateralToFSubreport);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			ReportExporter reportExporter = new ReportExporter();
			reportExporter.exportXLS(jasperPrint, baos);
			
			String fileName = "advance_questionnaire.xls";
			response.setHeader("Content-Disposition", "inline; filename=" + fileName);
			response.setContentType("application/vnd.ms-excel");
			
			reportExporter.exportHTML(jasperPrint, baos);
			
			response.setContentLength(baos.size());
			 
			// write to response stream
			this.writeReportToResponseStream(response, baos);
		} catch (JRException e) {
			e.printStackTrace();
		}
		
        return null;
    }
	
	@RequestMapping(value = "/dac1_table", method = RequestMethod.GET)
    public ModelAndView generateDAC1Report(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView)  throws IOException {

		PropertyList propertyList = new PropertyList();
		propertyList.put("Provider", "mondrian");
		propertyList.put("Catalog",
				this.getClass().getResource("./financial.mondrian.xml")
						.toString());
		Connection conn = DriverManager.getConnection(propertyList, null,
				cdaDataSource);

		Map<String, Object> parameters = new HashMap<String, Object>();		
		
		InputStream inputStream;
		try {
			inputStream = ReportsController.class.getResourceAsStream("./dac1_master.jrxml");
			JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

//			JasperReport bilateralToASubreport = JasperCompileManager.compileReport(
//					JRXmlLoader.load(
//							ReportsController.class.getResourceAsStream("./advanced_questionnaire_bilateral_type_of_aid.jrxml")));
//			JasperReport bilateralToFSubreport = JasperCompileManager.compileReport(
//					JRXmlLoader.load(
//							ReportsController.class.getResourceAsStream("./advanced_questionnaire_bilateral_financial_instrument.jrxml")));

			parameters.put(JRMondrianQueryExecuterFactory.PARAMETER_MONDRIAN_CONNECTION, conn);
//			parameters.put("bilateralToASubreport", bilateralToASubreport);
//			parameters.put("bilateralToFSubreport", bilateralToFSubreport);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			ReportExporter reportExporter = new ReportExporter();
//			reportExporter.exportXLS(jasperPrint, baos);
			
//			String fileName = "DAC1_table.xls";
//			response.setHeader("Content-Disposition", "inline; filename=" + fileName);
//			response.setContentType("application/vnd.ms-excel");
			
			reportExporter.exportHTML(jasperPrint, baos);
			response.setContentType("text/html");
			
			response.setContentLength(baos.size());
			 
			// write to response stream
			this.writeReportToResponseStream(response, baos);
		} catch (JRException e) {
			e.printStackTrace();
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
