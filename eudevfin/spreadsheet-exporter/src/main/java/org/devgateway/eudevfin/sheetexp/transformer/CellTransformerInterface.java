/**
 * 
 */
package org.devgateway.eudevfin.sheetexp.transformer;

import org.devgateway.eudevfin.sheetexp.dto.MetadataCell;

/**
 * @author Alex
 *
 */
public interface CellTransformerInterface<SrcType,ReturnType> {
	public MetadataCell<ReturnType> transform(SrcType src);
}
