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
