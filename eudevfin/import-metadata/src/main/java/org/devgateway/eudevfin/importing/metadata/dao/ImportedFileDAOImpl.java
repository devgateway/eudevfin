/**
 * 
 */
package org.devgateway.eudevfin.importing.metadata.dao;

import java.util.List;

import org.devgateway.eudevfin.importing.metadata.entity.ImportedFile;
import org.devgateway.eudevfin.importing.metadata.repository.ImportedFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Alex
 *
 */
@Component
public class ImportedFileDAOImpl implements IImportedFileDAO {
	
	@Autowired
	private ImportedFileRepository repo;

	/* (non-Javadoc)
	 * @see org.devgateway.eudevfin.importing.metadata.dao.IImportedFileDAO#findDaoByHashCode(java.lang.String)
	 */
	@Override
	public ImportedFile findImportedFileByHashCode(final String hashcode) {
		final ImportedFile importedFile	= this.repo.findByHashcode(hashcode);
		return importedFile;
	}

	@Override
	public ImportedFile findImportedFileByFilename(final String filename) {
		final ImportedFile importedFile	= this.repo.findByFilename(filename);
		return importedFile;
	}

	@Override
	@Transactional
	public ImportedFile save(final ImportedFile importedFile) {
		return this.repo.save(importedFile);
	}

	@Override
	public List<ImportedFile> findAll() {
		return this.repo.findAll();
	}


}
