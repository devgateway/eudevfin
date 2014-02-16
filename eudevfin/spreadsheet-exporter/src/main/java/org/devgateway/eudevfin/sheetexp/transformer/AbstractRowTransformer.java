/**
 * 
 */
package org.devgateway.eudevfin.sheetexp.transformer;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.sheetexp.dto.EntityWrapperInterface;
import org.devgateway.eudevfin.sheetexp.dto.MetadataCell;
import org.devgateway.eudevfin.sheetexp.dto.MetadataRow;
import org.devgateway.eudevfin.sheetexp.exception.SpreadsheetTransformationException;
import org.devgateway.eudevfin.sheetexp.util.MetadataConstants;

/**
 * @author Alex
 * 
 *         This class should be extended by a class that at least populates the internal cell transformer list
 */
public abstract class AbstractRowTransformer<T extends EntityWrapperInterface> implements RowTransformerInterface<T> {

	private final Logger logger = Logger.getLogger(AbstractRowTransformer.class);

	private List<CellTransformerInterface<T, ?>> cellTransformerList = null;

	@Override
	public MetadataRow transform(final T src) throws SpreadsheetTransformationException {
		if (src != null) {
			final MetadataRow row = new MetadataRow();
			if (src.isHeader()) {
				this.createHeader(src, row);
			} else {
				if (src.getEntity() == null) {
					throw new SpreadsheetTransformationException("Entity is null !");
				}
				for (final CellTransformerInterface<T, ?> cellTransformer : this.cellTransformerList) {
					final MetadataCell<?> cell = cellTransformer.transform(src);
					row.add(cell);
				}
			}
			return row;
		} else {
			throw new SpreadsheetTransformationException("Wrapper is null");
		}
	}

	/**
	 * This function is called during the row transformation process in case the current entity that is being transformed turns out to be a
	 * header.
	 * 
	 * @param src
	 *            the entity being transformed. Note this is usually NOT an entity like a transaction or organization. Instead it's an
	 *            entity that represents the header.
	 * @param row
	 *            the resulting row
	 */
	protected void createHeader(final T src, final MetadataRow row) {
		row.getMetadata().put(MetadataConstants.HEADER, MetadataConstants.TRUE);
	}

	protected MetadataCell<String> createHeaderCell(final String value) {
		final MetadataCell<String> ret = new MetadataCell<String>(value);
		ret.getMetadata().put(MetadataConstants.HEADER, MetadataConstants.TRUE);
		ret.getMetadata().put(MetadataConstants.DATA_TYPE, MetadataConstants.DATA_TYPES.STRING.getType());
		return ret;
	}

	@Override
	public void addCellTransformer(final CellTransformerInterface<T, ?> cellTransformer) {
		if (this.cellTransformerList == null) {
			this.cellTransformerList = new ArrayList<CellTransformerInterface<T, ?>>();
		}
		this.cellTransformerList.add(cellTransformer);
	}

	@Override
	public List<CellTransformerInterface<T, ?>> getCellTransformerList() {
		return this.cellTransformerList;
	}

}
