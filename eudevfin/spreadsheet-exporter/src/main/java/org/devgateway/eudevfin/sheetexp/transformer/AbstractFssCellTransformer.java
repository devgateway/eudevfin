/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.sheetexp.transformer;

import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.sheetexp.dto.EntityWrapperInterface;
import org.devgateway.eudevfin.sheetexp.dto.MetadataCell;
import org.devgateway.eudevfin.sheetexp.util.MetadataConstants;

public abstract class AbstractFssCellTransformer<ReturnType> implements
		CellTransformerInterface<EntityWrapperInterface, ReturnType> {
	
	private String headerName;
	
	public AbstractFssCellTransformer(final String headerName) {
		this.headerName = headerName;
	}

	/**
	 * @param headerName the headerName to set
	 */
	public void setHeaderName(final String headerName) {
		this.headerName = headerName;
	}

	/**
	 * @return the headerName
	 */
	public String getHeaderName() {
		return this.headerName;
	}


	@Override
	public MetadataCell<ReturnType> transform(final EntityWrapperInterface src) {
		final MetadataCell<ReturnType> cell	= this.innerTransform((FinancialTransaction)src.getEntity());
		cell.getMetadata().put(MetadataConstants.COLUMN_NAME, this.getHeaderName());
		return cell;
	}
	
	protected void setDataTypeToDate(final MetadataCell<ReturnType> mdCell) {
		mdCell.getMetadata().put(MetadataConstants.DATA_TYPE, MetadataConstants.DATA_TYPES.DATE.getType());
	}
	
	protected void setDataTypeToString(final MetadataCell<ReturnType> mdCell) {
		mdCell.getMetadata().put(MetadataConstants.DATA_TYPE, MetadataConstants.DATA_TYPES.STRING.getType());
	}
	
	protected void setDataTypeToNumber(final MetadataCell<ReturnType> mdCell) {
		mdCell.getMetadata().put(MetadataConstants.DATA_TYPE, MetadataConstants.DATA_TYPES.NUMBER.getType());
	}
	
	protected void setDataTypeToMoney(final MetadataCell<ReturnType> mdCell) {
		mdCell.getMetadata().put(MetadataConstants.DATA_TYPE, MetadataConstants.DATA_TYPES.MONEY.getType());
	}

	
	public abstract MetadataCell<ReturnType> innerTransform(FinancialTransaction tx) ;

}
