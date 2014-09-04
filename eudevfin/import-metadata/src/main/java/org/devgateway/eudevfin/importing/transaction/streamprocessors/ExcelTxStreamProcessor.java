/**
 *
 */
package org.devgateway.eudevfin.importing.transaction.streamprocessors;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.devgateway.eudevfin.importing.processors.IStreamProcessor;
import org.joda.time.LocalDateTime;

/**
 * @author alexandru-m-g
 *
 */
public class ExcelTxStreamProcessor implements IStreamProcessor {

	private static final int ACTUAL_DATA_START_ROW_NUM = 1;
	private static final int NUM_OF_COLUMNS = 53;

	private final InputStream inputStream;
	private HSSFWorkbook workbook;
	private HSSFSheet sheet;
	private int endRowNum;
	private int currentRowNum = ACTUAL_DATA_START_ROW_NUM;

	public ExcelTxStreamProcessor(final InputStream is) {
		this.inputStream	= is;
		try {
			this.workbook 	= new HSSFWorkbook(is);
			this.sheet		= this.workbook.getSheetAt(0);
			this.endRowNum	= this.sheet.getLastRowNum();

		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Object> generateNextObject() {
		if ( this.currentRowNum <= this.endRowNum ) {
			final HSSFRow row 	= this.sheet.getRow(this.currentRowNum);
			try {
				return this.generateObject(row);

			} catch (final Exception e) {

				e.printStackTrace();

			}
			finally{
				this.currentRowNum++;
			}
		}
		return null;
	}

	private List<Object> generateObject(final HSSFRow row) {
		boolean allCellsAreNull = true;
		final List<Object> values = new ArrayList<>();
		for ( int j=0; j<NUM_OF_COLUMNS; j++) {
			final HSSFCell cell	= row.getCell(j);
			if (cell != null) {
				allCellsAreNull   = false;
				if ( HSSFCell.CELL_TYPE_STRING == cell.getCellType() ) {
					String val		= null;
					val	= cell.getStringCellValue() ;
					if ( val != null && val.trim().length() > 0 ) {
						values.add(val.trim());
					} else {
						values.add(null);
					}
				}
				else if ( HSSFDateUtil.isCellDateFormatted(cell) ) {
					final Date date = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
					final LocalDateTime localDateTime = LocalDateTime.fromDateFields(date);
					values.add(localDateTime);
				}
				else if ( HSSFCell.CELL_TYPE_NUMERIC == cell.getCellType() ) {
					//					final BigDecimal bdValue	=  new BigDecimal(cell.getNumericCellValue()) ;
					//					if (bdValue != null) {
					//						values.add(bdValue);
					//					}
					final Double d = cell.getNumericCellValue();
					values.add( d.toString() );
				}
				else{
					values.add("");
				}


			} else {
				values.add( null );
			}
		}
		System.out.println(values);
		if ( allCellsAreNull ) {
			return null;
		}

		return values;

	}

	@Override
	public boolean hasNextObject() {
		if ( this.currentRowNum <= this.endRowNum ) {
			return true;
		}
		return false;
	}

	@Override
	public void close() {
		try {
			this.inputStream.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}

	}



}
