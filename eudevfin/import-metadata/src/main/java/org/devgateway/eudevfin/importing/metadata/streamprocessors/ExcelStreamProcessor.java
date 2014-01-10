/**
 * 
 */
package org.devgateway.eudevfin.importing.metadata.streamprocessors;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.devgateway.eudevfin.financial.AbstractTranslateable;
import org.devgateway.eudevfin.importing.metadata.exception.EntityMapperGenerationException;
import org.devgateway.eudevfin.importing.metadata.exception.InvalidDataException;
import org.devgateway.eudevfin.importing.metadata.mapping.MapperInterface;

/**
 * @author Alex
 *
 */
public class ExcelStreamProcessor implements StreamProcessorInterface {

	private static final int METADATA_INFO_ROW_NUM = 1;

	private static final int MAPPER_CLASS_COL_NUM = 1;

	private static final int MAPPER_CLASS_ROW_NUM = 0;

	public final static Integer OFFSET 	= 1; 
	
	private HSSFWorkbook workbook;
	private HSSFSheet sheet;
	
	List<String> metadataInfoList;

	public ExcelStreamProcessor(InputStream is) {
		try {
			workbook 	= new HSSFWorkbook(is);
			sheet		= workbook.getSheetAt(0);
			
			this.generateMetadataInfoList();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/* (non-Javadoc)
	 * @see org.devgateway.eudevfin.importing.metadata.streamprocessors.StreamProcessorInterface#generateObjectList()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<? extends AbstractTranslateable> generateObjectList() {
		List<AbstractTranslateable> list	= new ArrayList<AbstractTranslateable>();
		
		int endRowNum	= this.sheet.getLastRowNum();
		for (int i=3; i<endRowNum; i++ ) {
			HSSFRow row 	= sheet.getRow(i);
			try {
				Class clazz	= Class.forName(this.getEntityClassName());
				MapperInterface<? extends AbstractTranslateable> mapper	= 
						(MapperInterface)clazz.newInstance();
				mapper.setMetainfos(this.metadataInfoList);
				AbstractTranslateable entity	= this.generateObject(mapper, row);
				if (entity != null)
					list.add(entity);
				else 
					break;
			} catch (ClassNotFoundException | InstantiationException
					| IllegalAccessException e) {
				
				e.printStackTrace();
				
				throw new EntityMapperGenerationException("Problems generating objects",e);
			}
		}
		return list;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private AbstractTranslateable generateObject(MapperInterface<? extends AbstractTranslateable> mapper, HSSFRow row) 
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		
		boolean allCellsAreNull	= true; 
		List<String> values	= new ArrayList<String>();
		for ( int j=OFFSET; j<this.metadataInfoList.size()+OFFSET; j++) {
			HSSFCell cell	= row.getCell(j);
			if (cell != null) {
				allCellsAreNull = false;
				if ( HSSFCell.CELL_TYPE_STRING == cell.getCellType() ) {
					values.add( cell.getStringCellValue() );
				}
				else if ( HSSFCell.CELL_TYPE_NUMERIC == cell.getCellType() ) {
					HSSFDataFormatter dataFormatter	= new HSSFDataFormatter();
					values.add( dataFormatter.formatCellValue(cell) );
					//values.add( new Double(cell.getNumericCellValue()).toString() );
				}
			}
			else
				values.add( null );
			
		}
		if ( allCellsAreNull ) 
						return null;
		AbstractTranslateable result	=  mapper.createEntity(values);
		return result;
	}

	/* (non-Javadoc)
	 * @see org.devgateway.eudevfin.importing.metadata.streamprocessors.StreamProcessorInterface#getEntityClassName()
	 */
	@Override
	public String getEntityClassName() {
		HSSFRow row 	= sheet.getRow(MAPPER_CLASS_ROW_NUM);
		HSSFCell cell	= row.getCell(MAPPER_CLASS_COL_NUM);
		if ( HSSFCell.CELL_TYPE_STRING == cell.getCellType() ) {
			return cell.getStringCellValue();
		}
		else
			throw new InvalidDataException("Expecting mapper name in cell B1");
	}
	
	private void generateMetadataInfoList () {
		this.metadataInfoList	= new ArrayList<String>();
		HSSFRow row 			= sheet.getRow(METADATA_INFO_ROW_NUM);
		short end				= row.getLastCellNum();
		for (short i=1; i<end; i++ ) {
			HSSFCell cell	= row.getCell((int)i);
			if ( cell != null ) {
				if ( HSSFCell.CELL_TYPE_STRING == cell.getCellType() ) {
					String value	=  cell.getStringCellValue();
					this.metadataInfoList.add(value);
				}
				else
					throw new InvalidDataException("Expecting mapper name in cell B1");
			}
			else 
				break;
		}
		
	}

}
