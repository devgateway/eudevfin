/**
 * 
 */
package org.devgateway.eudevfin.sheetexp.poi;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.devgateway.eudevfin.sheetexp.dto.MetadataCell;
import org.devgateway.eudevfin.sheetexp.dto.MetadataRow;
import org.devgateway.eudevfin.sheetexp.exception.PoiCreationException;
import org.devgateway.eudevfin.sheetexp.util.MetadataConstants;
import org.joda.money.BigMoney;
import org.joda.time.LocalDateTime;

/**
 * @author Alex
 *
 */
public class PoiExporter {
	
	private final OutputStream out;
	
	private final List<MetadataRow> sheet1Src;
	
	private final PoiObjectCreator poiCreator;
	
	private final String name;

	
	public PoiExporter(final OutputStream out, final List<MetadataRow> sheet1Src,
			final String name) {
		this.out = out;
		this.sheet1Src = sheet1Src;
		this.name = name;
		
		this.poiCreator 	= new PoiObjectCreator(this.name, "en");
	}

	public void export() {
		this.createHeaders();
		this.createBody();
		this.end();
	}

	private void createHeaders() {
		for (final MetadataRow mdRow : this.sheet1Src) {
			if ( mdRow.isHeader() ) {
				int columnNo	= 0;
				final Row row	= this.poiCreator.createRow();
				for (final MetadataCell<?> mdCell: mdRow) {
					final Cell cell	= row.createCell(columnNo);
					cell.setCellStyle(this.poiCreator.getHeaderCellStyle());
					cell.setCellValue(mdCell.toString());
					columnNo++;
				}
			} else {
				break;
			}
		}
	}

	private void createBody() {
		for (final MetadataRow mdRow : this.sheet1Src) {
			if ( mdRow.isHeader() ) {
				continue;
			}
			else {
				int columnNo	= 0;
				final Row row	= this.poiCreator.createRow();
				for (final MetadataCell<?> mdCell: mdRow) {
					final Cell cell	= row.createCell(columnNo);
					//cell.setCellValue(mdCell.toString());
					if ( mdCell.getValue() != null ) {
						this.populateCellValue(mdCell, cell);
					}
					columnNo++;
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void populateCellValue(final MetadataCell<?> mdCell, final Cell cell) {
		
		final String mdCellType	= mdCell.getMetadata().get(MetadataConstants.DATA_TYPE);
		
		if (MetadataConstants.DATA_TYPES.MONEY.getType().equals(mdCellType)) {
			final MetadataCell<BigMoney> bigMoneyMdCell	= (MetadataCell<BigMoney>) mdCell;
			final BigDecimal decimalValue	= bigMoneyMdCell.getValue().getAmountMinor().divide(new BigDecimal(100));
			
			cell.setCellStyle(
					this.poiCreator.getBodyNumericCellStyle()
			);
			
			cell.setCellValue(decimalValue.doubleValue());
		}
		else if ( MetadataConstants.DATA_TYPES.NUMBER.getType().equals(mdCellType) ) {
			final MetadataCell<BigDecimal> bigDecimalMdCell	= (MetadataCell<BigDecimal>) mdCell;
			
			cell.setCellStyle(
					this.poiCreator.getBodyNumericCellStyle()
			);
			
			cell.setCellValue( bigDecimalMdCell.getValue().doubleValue() );
		}
		else if ( MetadataConstants.DATA_TYPES.DATE.getType().equals(mdCellType) ) {
			final MetadataCell<LocalDateTime> dateTimeMdCell	= (MetadataCell<LocalDateTime>) mdCell;			
			
			cell.setCellStyle(
					this.poiCreator.getBodyDateCellStyle()
			);
			
			cell.setCellValue( dateTimeMdCell.getValue().toDate() );			
		}
		else if ( MetadataConstants.DATA_TYPES.STRING.getType().equals(mdCellType) ) {
			cell.setCellStyle(
					this.poiCreator.getBodyStringCellStyle()
			);
			
			cell.setCellValue(mdCell.getValue().toString());
		}
		else {
			throw new PoiCreationException("No such data type !");
		}
	}
	

	private void end() {
		final int numOfCols	= this.sheet1Src.get(0).size();
		for (int i=0; i<numOfCols; i++ ) {
			this.poiCreator.getSheet().autoSizeColumn(i);
		}
		try {
			this.poiCreator.getWorkbook().write(this.out);
		} catch (final IOException e) {
			e.printStackTrace();
		}
		finally{
			try {
				this.out.close();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
}
