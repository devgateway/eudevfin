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
public class DummyCellTransformer<T> implements CellTransformerInterface<T,String> {

	@Override
	public MetadataCell<String> transform(final T src) {
		final MetadataCell<String> cell	= new MetadataCell<String>("Dummy data");
		cell.getMetadata().put(MetadataConstants.DATA_TYPE, MetadataConstants.DATA_TYPES.STRING.getType());
		return cell;
	}

}
