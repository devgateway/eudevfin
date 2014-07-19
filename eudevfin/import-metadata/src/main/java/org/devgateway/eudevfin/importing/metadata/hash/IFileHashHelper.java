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
package org.devgateway.eudevfin.importing.metadata.hash;

import java.io.InputStream;

import org.devgateway.eudevfin.importing.metadata.dao.IImportedFileDAO;


/**
 * @author Alex
 *
 */
public interface IFileHashHelper {
	
	void setup(String filename, InputStream is, IImportedFileDAO importedFileDao);
	
	String computeHash();
	Boolean checkAlreadyLoaded();
	
	void markAsLoaded();
	
}
