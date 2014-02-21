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
	
	private static final int ACTUAL_ATA_START_ROW_NUM = 3;

	public final static Integer OFFSET 	= 1; 
	
	private HSSFWorkbook workbook;
	private HSSFSheet sheet;
	
	private List<String> metadataInfoList;
	private String mapperClassName;
	
	private int currentRowNum = ACTUAL_ATA_START_ROW_NUM;

	private int endRowNum;
	
	private InputStream inputStream;

	public ExcelStreamProcessor(InputStream is) {
		this.inputStream	= is;
		try {
			workbook 	= new HSSFWorkbook(is);
			sheet		= workbook.getSheetAt(0);
			endRowNum	= this.sheet.getLastRowNum();
			
			this.generateMetadataInfoList();
			this.findMapperClassName();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public Object generateNextObject() {
		if ( currentRowNum <= endRowNum ) {
			HSSFRow row 	= sheet.getRow(currentRowNum);
			try {
				MapperInterface mapper			= this.instantiateMapper();
				Object entity	= this.generateObject(mapper, row);
				return entity;
			} catch (ClassNotFoundException | InstantiationException
					| IllegalAccessException e) {
				
				e.printStackTrace();
				
				throw new EntityMapperGenerationException("Problems generating object",e);
			}
			finally{
				currentRowNum++;
			}
		}
		return null;
		
	}
	
	@Override
	public boolean hasNextObject() {
		if ( currentRowNum <= endRowNum ) {
			return true;
		}
		return false;
		
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object generateObject(MapperInterface<?> mapper, HSSFRow row) 
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		
		boolean allCellsAreNull	= true; 
		List<String> values	= new ArrayList<String>();
		for ( int j=OFFSET; j<this.metadataInfoList.size()+OFFSET; j++) {
			HSSFCell cell	= row.getCell(j);
			if (cell != null) {
				allCellsAreNull = false;
				String val		= null;
				if ( HSSFCell.CELL_TYPE_STRING == cell.getCellType() ) {
					val	= cell.getStringCellValue() ;
				}
				else if ( HSSFCell.CELL_TYPE_NUMERIC == cell.getCellType() ) {
					HSSFDataFormatter dataFormatter	= new HSSFDataFormatter();
					val	= dataFormatter.formatCellValue(cell);
					//values.add( new Double(cell.getNumericCellValue()).toString() );
				}
				if ( val != null && val.trim().length() > 0 )
					values.add(val.trim());
				else
					values.add(null);
				
			}
			else
				values.add( null );
			
		}
		if ( allCellsAreNull ) 
						return null;
		Object result	=  mapper.createEntity(values);
		return result;
	}
	
	/**
	 * 
	 * @param mapperClassName the name of the mapper class that needs to be instantiated
	 * @param metadataInfoList the metainformation needed for each field of the mapping
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("rawtypes")
	private MapperInterface<?> instantiateMapper() {
		try {
			Class clazz = Class.forName(mapperClassName);
			MapperInterface<?> mapper = 
					(MapperInterface<?>) clazz.newInstance();
			mapper.setMetainfos(metadataInfoList);

			return mapper;
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException e) {
			e.printStackTrace();
			throw new EntityMapperGenerationException(
					"Problems generating objects", e);
		}
	}

	private void findMapperClassName() {
		HSSFRow row 	= sheet.getRow(MAPPER_CLASS_ROW_NUM);
		HSSFCell cell	= row.getCell(MAPPER_CLASS_COL_NUM);
		if ( HSSFCell.CELL_TYPE_STRING == cell.getCellType() ) {
			this.mapperClassName = cell.getStringCellValue();
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
	
	@Override
	public void close() {
		try {
			this.inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public List<String> getMetadataInfoList() {
		return metadataInfoList;
	}

	public void setMetadataInfoList(List<String> metadataInfoList) {
		this.metadataInfoList = metadataInfoList;
	}

	@Override
	public String getMapperClassName() {
		return mapperClassName;
	}

	public void setMapperClassName(String mapperClassName) {
		this.mapperClassName = mapperClassName;
	}


	
	

}
