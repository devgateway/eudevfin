/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
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
