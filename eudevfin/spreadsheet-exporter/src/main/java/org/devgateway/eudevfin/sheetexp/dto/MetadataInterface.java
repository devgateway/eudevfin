/**
 * 
 */
package org.devgateway.eudevfin.sheetexp.dto;

import java.util.Map;

/**
 * @author Alex
 *
 */
public interface MetadataInterface {
	
	
	public Map<String, String> getMetadata() ;

	boolean isHeader();
	
	public boolean isValueTrue(String key);
	
}
