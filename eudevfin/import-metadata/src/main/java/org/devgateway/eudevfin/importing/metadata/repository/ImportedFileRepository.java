/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.importing.metadata.repository;

import java.util.List;

import org.devgateway.eudevfin.importing.metadata.entity.ImportedFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImportedFileRepository extends JpaRepository<ImportedFile, Long> {
	
	@Override
	List<ImportedFile> findAll();
	
	ImportedFile findByFilename(String filename);
	ImportedFile findByHashcode(String hashcode);
	
}
