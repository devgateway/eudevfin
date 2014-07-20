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
package org.devgateway.eudevfin.sheetexp.iati.domain;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * @author alexandru-m-g
 *
 */
public class CrsAdd {
	
	@XStreamImplicit
	private List<AidtypeFlag> aidtypeFlags;

	public List<AidtypeFlag> getAidtypeFlags() {
		return this.aidtypeFlags;
	}

	public void setAidtypeFlags(final List<AidtypeFlag> aidtypeFlags) {
		this.aidtypeFlags = aidtypeFlags;
	}
	
	
}
