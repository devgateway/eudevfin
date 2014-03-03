/**
 * 
 */
package org.devgateway.eudevfin.sheetexp.transformer;

import org.devgateway.eudevfin.sheetexp.dto.MetadataCell;
import org.devgateway.eudevfin.sheetexp.util.MetadataConstants;

/**
 * @author Alex
 *
 */
public class HeaderCellTransformer implements CellTransformerInterface<String,String>{

	@Override
	public MetadataCell<String> transform(final String headerName) {
		final MetadataCell<String> cell	= new MetadataCell<String>(headerName);
		cell.getMetadata().put(MetadataConstants.HEADER, MetadataConstants.TRUE);
		cell.getMetadata().put(MetadataConstants.COLUMN_NAME, headerName);
		return cell;
	}

}
