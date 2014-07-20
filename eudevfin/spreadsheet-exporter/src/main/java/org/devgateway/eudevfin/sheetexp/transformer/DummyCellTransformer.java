/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
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
