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
package org.devgateway.eudevfin.importing.metadata.dao;

import java.util.List;

import org.devgateway.eudevfin.importing.metadata.entity.ImportedFile;

/**
 * @author Alex
 *
 */
public interface IImportedFileDAO {

	List<ImportedFile> findAll();
	ImportedFile findImportedFileByHashCode(String hashcode);
	ImportedFile findImportedFileByFilename(String filename);
	ImportedFile save(ImportedFile importedFile);
	
}
