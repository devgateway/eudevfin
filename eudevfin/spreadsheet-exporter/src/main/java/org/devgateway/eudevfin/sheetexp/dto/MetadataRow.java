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
package org.devgateway.eudevfin.sheetexp.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.devgateway.eudevfin.sheetexp.util.MetadataConstants;

/**
 * @author Alex
 * 
 */
public class MetadataRow extends ArrayList<MetadataCell<?>> implements MetadataInterface{

	private Map<String, String> metadata;
	
	@Override
	public boolean isValueTrue(final String key) {
		return 
				MetadataConstants.TRUE.equals(this.getMetadata().get(key));
	}
	
	@Override
	public boolean isHeader() {
		 return 
			MetadataConstants.TRUE.equals(this.getMetadata().get(MetadataConstants.HEADER));
	}
	
	@Override
	public Map<String, String> getMetadata() {
		if ( this.metadata == null) 
			this.metadata	= new HashMap<String, String>();
		return this.metadata;
	}

	@Override
	public String toString() {
		final StringBuffer sb	= new StringBuffer("Row: ");
		for (final MetadataCell cell : this) {
			sb.append(cell.toString() + ",");
		}
		return sb.toString();
	}
	
	
}
