package org.devgateway.eudevfin.reports.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.Color;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.sql.DataSource;

import mondrian.olap.Connection;
import mondrian.olap.DriverManager;
import mondrian.olap.Util.PropertyList;
import mondrian.rolap.RolapConnectionProperties;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.fill.JRTemplatePrintText;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.SplitTypeEnum;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.olap.JRMondrianQueryExecuterFactory;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/commonAuthContext.xml",
		"classpath:META-INF/commonContext.xml",
		"classpath:META-INF/authContext.xml",
		"classpath:META-INF/financialContext.xml",
		"classpath:META-INF/commonFinancialContext.xml",
		"classpath:META-INF/importMetadataContext.xml",
		"classpath:META-INF/cdaContext.xml" })

public class ReportsSimpleTest {
	protected static Logger logger = Logger.getLogger(ReportsSimpleTest.class);

	@Autowired
	private DataSource cdaDataSource;

	@Test
	public void testReport() {
		JasperPrint jp = buildReport();
		assertNotNull(jp);
	}

	private JasperPrint buildReport() {
		try {

			PropertyList properties = getPropertyList();
			Connection connection = DriverManager.getConnection(properties,
					null, cdaDataSource);
			InputStream inputStream = ReportsSimpleTest.class
					.getResourceAsStream("../aq/aq_master.jasper");
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(JRParameter.REPORT_LOCALE, new Locale("en"));
			parameters
					.put(JRMondrianQueryExecuterFactory.PARAMETER_MONDRIAN_CONNECTION,
							connection);
			parameters.put("FIRST_YEAR", 2010);
			parameters.put("SECOND_YEAR", 2011);
			parameters.put("CURRENCY", "USD");
			try {
				String subdirPath = new URI(this.getClass()
						.getResource("../aq").toString()).getPath();
				parameters.put("SUBDIR_PATH", subdirPath);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			parameters.put("REPORTING_COUNTRY", "Donor Name");
			JasperReport jasperReport = (JasperReport) JRLoader
					.loadObject(inputStream);
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters);

			return jasperPrint;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private PropertyList getPropertyList() {
		PropertyList propertyList = new PropertyList();
		propertyList.put("Provider", "mondrian");
		propertyList
				.put("Catalog",
						this.getClass()
								.getResource(
										"/org/devgateway/eudevfin/cda/service/financial.mondrian.xml")
								.toString());
		propertyList.put(
				RolapConnectionProperties.DynamicSchemaProcessor.toString(),
				"org.devgateway.eudevfin.cda.utils.SchemaProcessor");
		propertyList.put("CURRENCY", "USD");
		propertyList.put("LOCALE", "en");
		propertyList.put("COUNTRY_CURRENCY", "EUR");

		return propertyList;
	}

}