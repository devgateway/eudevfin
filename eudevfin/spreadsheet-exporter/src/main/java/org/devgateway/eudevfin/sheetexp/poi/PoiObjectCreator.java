/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/

package org.devgateway.eudevfin.sheetexp.poi;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * @author Alex
 *
 */
public class PoiObjectCreator {
	private static final Map<String, Locale> localeMap	= new HashMap<String, Locale>();
	static{
		localeMap.put("en", Locale.US);
	}
	
	private final HSSFWorkbook workbook;
	private final Sheet sheet;
	private int rowNum;
	
	private final CellStyle headerCellStyle;
	
	private final CellStyle bodyStringCellStyle;
	
	private final CellStyle bodyDateCellStyle;
	
	private final CellStyle bodyNumericCellStyle;
	
	private final Locale locale;
	

	public PoiObjectCreator(final String name, final String locale) {
		this.locale					= localeMap.get(locale);
		
		this.workbook 	= new HSSFWorkbook();
		this.sheet 		= this.workbook.createSheet();
		this.workbook.setSheetName(0, name);
		
		this.headerCellStyle		= this.createHeaderCellStyle();
		this.bodyStringCellStyle	= this.createBodyStringCellStyle();
		this.bodyDateCellStyle		= this.createBodyDateCellStyle();
		this.bodyNumericCellStyle	= this.createBodyNumericCellStyle();
		
		this.rowNum					= 0;
		
	}

	private CellStyle createBodyNumericCellStyle() {
		final CellStyle style	= this.workbook.createCellStyle();
		final Font font			= this.workbook.createFont();
		style.setFont(font);
		
//		final NumberFormat nf	= NumberFormat.getNumberInstance(this.locale);
//		final HSSFDataFormat hssfDataFormat	= this.workbook.createDataFormat();
		
		return style;
	}

	private CellStyle createBodyDateCellStyle() {
		final CellStyle style	= this.workbook.createCellStyle();
		final Font font			= this.workbook.createFont();
		style.setFont(font);
		
		final DateFormat df		= DateFormat.getDateInstance(DateFormat.SHORT, this.locale);
		final String pattern	= ((SimpleDateFormat)df).toPattern();	
		final HSSFDataFormat hssfDataFormat	= this.workbook.createDataFormat();
		
		style.setDataFormat( hssfDataFormat.getFormat( pattern ) );
		
		return style;
	}

	private CellStyle createBodyStringCellStyle() {
		final CellStyle style	= this.workbook.createCellStyle();
		final Font font			= this.workbook.createFont();
		style.setFont(font);
		style.setWrapText(true);
		return style;
	}

	private CellStyle createHeaderCellStyle() {
		final CellStyle style	= this.workbook.createCellStyle();
		final Font font			= this.workbook.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(font);
		return style;
	}
	
	public Row createRow() {
		final Row newRow	= this.sheet.createRow(this.rowNum);
		
		this.rowNum++;
		
		return newRow;
	}
	
	public HSSFWorkbook getWorkbook() {
		return this.workbook;
	}

	public Sheet getSheet() {
		return this.sheet;
	}

	public CellStyle getHeaderCellStyle() {
		return this.headerCellStyle;
	}

	public CellStyle getBodyStringCellStyle() {
		return this.bodyStringCellStyle;
	}

	public CellStyle getBodyDateCellStyle() {
		return this.bodyDateCellStyle;
	}

	public CellStyle getBodyNumericCellStyle() {
		return this.bodyNumericCellStyle;
	}


}
