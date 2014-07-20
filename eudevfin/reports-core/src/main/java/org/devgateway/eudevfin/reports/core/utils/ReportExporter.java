/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.reports.core.utils;

import java.io.ByteArrayOutputStream;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
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
	
	/**
	 * Exports a report to HTML format.
	 * 
	 * @param jp the JasperPrint object
	 * @param baos the ByteArrayOutputStream where the report will be written
	 */
	public void exportHTML(JasperPrint jp, ByteArrayOutputStream baos) throws JRException {
		// create a JRHtmlExporter instance
		JRHtmlExporter exporter = new JRHtmlExporter();
		 
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
		
		// HTML specific parameters
		exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE);
		 
		// retrieve the exported report in HTML format
		exporter.exportReport();
	}
	
	/**
	 * Exports a report to PDF format.
	 * 
	 * @param jp the JasperPrint object
	 * @param baos the ByteArrayOutputStream where the report will be written
	 */
	public void exportPDF(JasperPrint jp, ByteArrayOutputStream baos) throws JRException {
		// create a JRPdfExporter instance
		JRPdfExporter exporter = new JRPdfExporter();
		
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
		 
		// retrieve the exported report in PDF format
		exporter.exportReport();
	}
	
	/**
	 * Exports a report to CSV format.
	 * 
	 * @param jp the JasperPrint object
	 * @param baos the ByteArrayOutputStream where the report will be written
	 */
	public void exportCSV(JasperPrint jp, ByteArrayOutputStream baos) throws JRException {
		// create a JRCsvExporter instance
		JRCsvExporter exporter = new JRCsvExporter();
		
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
		 
		// retrieve the exported report in CSV format
		exporter.exportReport();
	}
}
