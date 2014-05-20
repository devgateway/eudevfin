package org.devgateway.eudevfin.importing.metadata.hash;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.devgateway.eudevfin.importing.metadata.dao.IImportedFileDAO;
import org.devgateway.eudevfin.importing.metadata.entity.ImportedFile;
import org.springframework.transaction.annotation.Transactional;

public class FileHashHelperImpl implements IFileHashHelper {
	
	private static Logger logger	= Logger.getLogger(FileHashHelperImpl.class);
	
	private String filename;
	
	private InputStream inputStream;

	private IImportedFileDAO importedFileDao;
	
	private String hashcode;

	@Override
	public String computeHash() {
		String ret	= null;
		if ( this.hashcode == null ) {
			try (InputStream is = this.inputStream ) {
				this.hashcode	= DigestUtils.md5Hex(is);
				ret	= this.hashcode;
			} catch (final IOException e) {
				e.printStackTrace();
			}
		} else {
			ret	= this.hashcode;
		}
		return ret;
	}

	@Override
	public void setup(final String filename, final InputStream is, final IImportedFileDAO importedFileDao) {
		this.filename			= filename;
		this.inputStream		= is; 
		this.importedFileDao	= importedFileDao;
		
		this.computeHash();
		
	}

	@Override
	public Boolean checkAlreadyLoaded() {
		Boolean result	= null;
		final ImportedFile importedFile	= this.importedFileDao.findImportedFileByFilename(this.filename);
		if ( importedFile == null ) {
			result	= false;
			logger.info(String.format("File %s was never imported", this.filename));
		}
		else {
			if ( !this.hashcode.equals(importedFile.getHashcode()) ) {
				logger.info(String.format("File %s was modified", this.filename));
				result = false;
			}
			else {
				logger.info(String.format("File %s was already imported and not modified", this.filename));
				result	= true;
			}
		}
		return result;
	}

	@Override
	@Transactional
	public void markAsLoaded() {
		ImportedFile importedFile	= this.importedFileDao.findImportedFileByFilename(this.filename);
		
		if ( importedFile == null ) {
			importedFile	= new ImportedFile();
			importedFile.setFilename(this.filename);
		}
		importedFile.setHashcode(this.hashcode);
		
		this.importedFileDao.save(importedFile);
		
	}

}
