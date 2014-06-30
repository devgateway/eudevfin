/**
 * 
 */
package org.devgateway.eudevfin.sheetexp.util;

/**
 * @author Alex
 *
 */
public class MetadataConstants {

	public static final String COLUMN_NAME = "column-name";
	public static final String HEADER = "header";
	public static final String TRUE = "true";

	public static enum DATA_TYPES	{
		STRING("string"), NUMBER("number"), DATE("date"), MONEY("money");
		
		private final String type;
		
		private DATA_TYPES(final String type) {
			this.type	= type;
		}

		public String getType() {
			return this.type;
		}
		
	}

	public static final String DATA_TYPE = "data-type";
	
	public static final String REPORT_TYPE_PARAM = "reportType";
	
	public static final String FSS_REPORT_TYPE = "fss";
	public static final String CRS_REPORT_TYPE = "crs";
	public static final String IATI_REPORT_TYPE = "iati";
}
