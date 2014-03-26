package org.devgateway.eudevfin.importing.metadata.repository;

import java.util.List;

import org.devgateway.eudevfin.importing.metadata.entity.ImportedFile;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ImportedFileRepository extends PagingAndSortingRepository<ImportedFile, Long> {
	
	@Override
	List<ImportedFile> findAll();
	
	ImportedFile findByFilename(String filename);
	ImportedFile findByHashcode(String hashcode);
	
}
