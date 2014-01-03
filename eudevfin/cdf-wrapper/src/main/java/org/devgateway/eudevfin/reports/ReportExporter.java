package org.devgateway.eudevfin.reports;

import java.io.ByteArrayOutputStream;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;

/**
  * An exporter for exporting the report in various formats: Excel, PDF, CSV
  */
public class ReportExporter {
	/**
	 * Exports a report to XLS (Excel) format.
	 * 
	 * @param jp the JasperPrint object
	 * @param baos the ByteArrayOutputStream where the report will be written
	 */
	public void exportXLS(JasperPrint jp, ByteArrayOutputStream baos) throws JRException {
		// create a JRXlsExporter instance
		JRXlsExporter exporter = new JRXlsExporter();
		 
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
		 
		// excel specific parameters
		exporter.setParameter(JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
		exporter.setParameter(JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
		exporter.setParameter(JRXlsAbstractExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.TRUE);
		 
		// retrieve the exported report in XLS format
		exporter.exportReport();
	}

	public void exportHTML(JasperPrint jp, ByteArrayOutputStream baos) throws JRException {
		// create a JRHtmlExporter instance
		JRHtmlExporter exporter = new JRHtmlExporter();
		 
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
		 
		// retrieve the exported report in HTML format
		exporter.exportReport();
	}

}
