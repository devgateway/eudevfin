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
package org.devgateway.eudevfin.importing.metadata.mapping;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.domain.ChannelCategory;

/**
 * @author Alex
 *
 */
public class ChannelCategoryMapper extends CategoryMapper {
	private static Logger logger	= Logger.getLogger(ChannelCategoryMapper.class);

	private static final String NOT_MCD_CONST = "Not MCD";
	private static final String MCD_CONST = "MCD";
	/**
	 * 
	 */
	public ChannelCategoryMapper() {
		super();
	}
	
	@Override
	protected ChannelCategory instantiate() {
		return new ChannelCategory();
	}

	@Override
	public void __insertParent(Category newCateg, String parentCode) {
		if ( parentCode!=null && parentCode.length()>0 && !parentCode.equals(newCateg.getCode()) ){
			super.__insertParent(newCateg, parentCode);
		}
	}

	public void __computeCoefficient(ChannelCategory newChannelCategory, String coefficient) {
		if ( coefficient != null && coefficient.length() > 0 ) {
			try {
				BigDecimal bigCoeff	= new BigDecimal(Float.parseFloat(coefficient));
				newChannelCategory.setCoefficient(bigCoeff);
			}
			catch(NumberFormatException e) {
				logger.info( String.format("Couldn't parse coefficient %s for channel category with code %s  ", coefficient, newChannelCategory.getCode()) );
			}
		}
	}
	
	public void __computeDac2a3a(ChannelCategory newChannelCategory, String dac2a3a) {
		if  ( dac2a3a != null && dac2a3a.length() > 0 ) {
			try {
				int dac2a3aInt	= Integer.parseInt(dac2a3a);
				newChannelCategory.setDac2a3a(dac2a3aInt);
			}
			catch (NumberFormatException e) {
				;
			}
		}
	}
	public void __computeMcd(ChannelCategory newChannelCategory, String mcd) {
		if ( mcd!=null && mcd.length() > 0 ) {
			if (NOT_MCD_CONST.equals(mcd)) {
				newChannelCategory.setMcd(false);
			}
			else if (MCD_CONST.equals(mcd)) {
				newChannelCategory.setMcd(true);
			}
		}
	}

}
