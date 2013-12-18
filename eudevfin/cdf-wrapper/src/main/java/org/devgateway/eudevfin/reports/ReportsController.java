package org.devgateway.eudevfin.reports;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import mondrian.olap.Connection;
import mondrian.olap.DriverManager;
import mondrian.olap.Query;
import mondrian.olap.Result;
import mondrian.olap.Util.PropertyList;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.olap.JRMondrianQueryExecuterFactory;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ReportsController {
	
	@Autowired
	private DataSource cdaDataSource;
	
	private static final Logger logger = Logger.getLogger(ReportsController.class);
	
	@RequestMapping(value = "/pdf", method = RequestMethod.GET)
    public ModelAndView generatePdfReport(ModelAndView modelAndView) {
		logger.debug(">>> generate PDF report");
		
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		List<Person> personList = new ArrayList<Person>();
		Person p1 = new Person(1, "name 1", "lastName 1");
		Person p2 = new Person(2, "name 2", "lastName 2");
		Person p3 = new Person(3, "name 3", "lastName 3");
		personList.add(p1);
		personList.add(p2);
		personList.add(p3);
		
		JRDataSource jrdataSource = new JRBeanCollectionDataSource(personList);
		
		parameterMap.put("datasource", jrdataSource);
		modelAndView = new ModelAndView("pdfReport", parameterMap);
		
        return modelAndView;
    }
	
	@RequestMapping(value = "/xls", method = RequestMethod.GET)
    public String generateXlsReport(ModelMap model) {
		logger.debug(">>> generate XLS report");
		
		List<Person> personList = new ArrayList<Person>();
		Person p1 = new Person(1, "name 1", "lastName 1");
		Person p2 = new Person(2, "name 2", "lastName 2");
		Person p3 = new Person(3, "name 3", "lastName 3");
		personList.add(p1);
		personList.add(p2);
		personList.add(p3);
		
		JRDataSource jrdataSource = new JRBeanCollectionDataSource(personList);
		
		model.addAttribute("datasource", jrdataSource);
		
        return "xlsReport";
    }
	
	@RequestMapping(value = "/html", method = RequestMethod.GET)
    public ModelAndView generateHtmlReport(ModelAndView modelAndView) {
		logger.debug(">>> generate HTML report");
		
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		List<Person> personList = new ArrayList<Person>();
		Person p1 = new Person(1, "name 1", "lastName 1");
		Person p2 = new Person(2, "name 2", "lastName 2");
		Person p3 = new Person(3, "name 3", "lastName 3");
		personList.add(p1);
		personList.add(p2);
		personList.add(p3);
		
		JRDataSource jrdataSource = new JRBeanCollectionDataSource(personList);
		
		parameterMap.put("datasource", jrdataSource);
		modelAndView = new ModelAndView("htmlReport", parameterMap);
		
        return modelAndView;
    }
	
	@RequestMapping(value = "/csv", method = RequestMethod.GET)
    public ModelAndView generateCsvReport(ModelAndView modelAndView) {
		logger.debug(">>> generate CSV report");
		
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		List<Person> personList = new ArrayList<Person>();
		Person p1 = new Person(1, "name 1", "lastName 1");
		Person p2 = new Person(2, "name 2", "lastName 2");
		Person p3 = new Person(3, "name 3", "lastName 3");
		personList.add(p1);
		personList.add(p2);
		personList.add(p3);
		
		JRDataSource jrdataSource = new JRBeanCollectionDataSource(personList);
		
		parameterMap.put("datasource", jrdataSource);
		modelAndView = new ModelAndView("csvReport", parameterMap);
		
        return modelAndView;
    }
	
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

			parameters.put("Subreport1", subreport1);
			parameters.put(JRMondrianQueryExecuterFactory.PARAMETER_MONDRIAN_CONNECTION, conn);
			
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
