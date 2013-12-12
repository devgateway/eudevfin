package org.devgateway.eudevfin.reports;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import mondrian.olap.Connection;
import mondrian.olap.DriverManager;
import mondrian.olap.Util.PropertyList;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
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
		
//		Map<String, Object> parameterMap = new HashMap<String, Object>();
		List<Person> personList = new ArrayList<Person>();
		Person p1 = new Person(1, "name 1", "lastName 1");
		Person p2 = new Person(2, "name 2", "lastName 2");
		Person p3 = new Person(3, "name 3", "lastName 3");
		personList.add(p1);
		personList.add(p2);
		personList.add(p3);
		
		JRDataSource jrdataSource = new JRBeanCollectionDataSource(personList);
		
		model.addAttribute("datasource", jrdataSource);
//		parameterMap.put("datasource", jrdataSource);
//		modelAndView = new ModelAndView("xlsReport", parameterMap);
		
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
		logger.debug(">>> generate Mondrian report");
		
		Map<String, Object> parameterMap = new HashMap<String, Object>();
//		JRDataSource jrdataSource = new JRBeanCollectionDataSource(personList);
		
//		Connection conn = DriverManager.getConnection(
//			"Provider=mondrian;" + 
//			"JdbcDrivers=­org­.­apache­.­derby­.­jdbc­.­EmbeddedDriver;" +
//			"Jdbc=jdbc:derby:memory:eudevfin;" +
//			"JdbcUser=app;" +
//			"JdbcPassword=;" +
//			"Catalog=" + this.getClass().getResource("./financial.mondrian.xml").toString() + ";",
//			null
//			);
		
		PropertyList propertyList = new PropertyList();
		propertyList.put("Provider", "mondrian");
		propertyList.put("Catalog",
				this.getClass().getResource("./financial.mondrian.xml")
						.toString());
		Connection conn = DriverManager.getConnection(propertyList, null,
				cdaDataSource);
		
		// used to test the connection
//		Query query = connection.parseQuery(
//			    "SELECT {[Measures].[Unit Sales], [Measures].[Store Sales]} on columns," +
//			    " {[Product].children} on rows " +
//			    "FROM [Sales] " +
//			    "WHERE ([Time].[1997].[Q1], [Store].[CA].[San Francisco])");
//		Result result = connection.execute(query);
//		result.print(new PrintWriter(System.out));
		
		Map<String, Object> parameters = new HashMap<String, Object>();
//		parameters.put(JRMondrianQueryExecuterFactory.PARAMETER_MONDRIAN_CONNECTION, conn);
		
		
		InputStream inputStream;
		try {
			inputStream = ReportsController.class.getResourceAsStream("./jasper_template_mondrian.jrxml");
			JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			
			parameters.put(JRMondrianQueryExecuterFactory.PARAMETER_MONDRIAN_CONNECTION, conn);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters);
			
			JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
//			JasperExportManager.exportReportToPdfFile(jasperPrint, "testjasper.pdf");
//			JasperViewer.viewReport(jasperPrint);
//			JasperPrintManager.printReport(jasperPrint, true);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JRException e) {
			e.printStackTrace();
		}
		
//		parameterMap.put("parameters", parameters);
//		modelAndView = new ModelAndView("mondrianReport", parameterMap);
		
        return null;
    }
}
