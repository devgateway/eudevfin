package org.devgateway.eudevfin.sheetexp.transformer;

import java.util.List;

import org.devgateway.eudevfin.sheetexp.dto.MetadataRow;
/**
 * 
 * @author Alex
 *
 * @param <T>
 */
public interface RowTransformerInterface<T> {
	
	public MetadataRow transform(T src);

	public void addCellTransformer(CellTransformerInterface<T,?> cellTransformer);
	
	public List<CellTransformerInterface<T,?>> getCellTransformerList();
}
