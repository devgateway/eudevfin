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
