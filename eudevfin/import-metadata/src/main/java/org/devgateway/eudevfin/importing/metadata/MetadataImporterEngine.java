package org.devgateway.eudevfin.importing.metadata;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.common.dao.AbstractDaoImpl;
import org.devgateway.eudevfin.common.dao.translation.AbstractTranslateable;
import org.devgateway.eudevfin.importing.metadata.dao.IImportedFileDAO;
import org.devgateway.eudevfin.importing.metadata.exception.InvalidDataException;
import org.devgateway.eudevfin.importing.metadata.hash.FileHashHelperImpl;
import org.devgateway.eudevfin.importing.metadata.hash.IFileHashHelper;
import org.devgateway.eudevfin.importing.metadata.storing.IStoringEngine;
import org.devgateway.eudevfin.importing.metadata.streamprocessors.ExcelStreamProcessor;
import org.devgateway.eudevfin.importing.metadata.streamprocessors.StreamProcessorInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
public class MetadataImporterEngine {
	private static Logger logger	= Logger.getLogger(MetadataImporterEngine.class);
	
	@Autowired
	private IImportedFileDAO importedFileDao;

	
	@SuppressWarnings("rawtypes")
	Map<String, AbstractDaoImpl> servicesMap;
	
	@Resource(name="metadataSourceList")
	List<String> metadataSourceList;
	
	@SuppressWarnings("rawtypes")
	@Resource
	List<IStoringEngine> storingEngineList;
	

	
	@SuppressWarnings({ "rawtypes" })
	public void process () {
//		this.populateServicesMap();
		final Map<String, IStoringEngine<AbstractTranslateable>> storingEngineMap	= this.populateStoringEnginesMap();
		
		final List<StreamProcessorInterface> streamProcessors 		= this.populateStreamProcessors();
		final List<IFileHashHelper> fileHashHelpers		= this.populateStoringEngines();
		
		for (int i=0; i<streamProcessors.size(); i++) {
			final StreamProcessorInterface streamProcessorInterface = streamProcessors.get(i);
			final IFileHashHelper fileHashHelper	= fileHashHelpers.get(i);
			logger.info( String.format("%d) Starting import...",i+1) );

			if ( !fileHashHelper.checkAlreadyLoaded() ) {
				final IStoringEngine<AbstractTranslateable> storingEngine	= 
						storingEngineMap.get(streamProcessorInterface.getMapperClassName());
				int numOfSavedEntities	= 0;
				while ( streamProcessorInterface.hasNextObject() ) {
					final Object entity	= streamProcessorInterface.generateNextObject();
					try {
						if ( storingEngine.process((AbstractTranslateable)entity) ) {
							numOfSavedEntities++;
						}
					}
					catch(final DataIntegrityViolationException e) {
						throw new InvalidDataException(
								String.format("There was a problem with saving the following entity to the db: %s", entity.toString() )
								, e);
					}
				}
				logger.info(String.format("-> finished! Imported %d entities !", numOfSavedEntities));
				streamProcessorInterface.close();
				fileHashHelper.markAsLoaded();
			}
			
		}
	}


	private List<StreamProcessorInterface> populateStreamProcessors() {
		logger.info("Will check import for the following files:");
		
		final ArrayList<StreamProcessorInterface> processors = new ArrayList<>();
		for (int i = 0; i < this.metadataSourceList.size(); i++) {
			final String filename		= this.metadataSourceList.get(i);
			logger.info( String.format("%d) %s", i+1, filename) );
			
			final InputStream is = this.getClass()
					.getResourceAsStream(filename);
			if (filename.endsWith("xls") || filename.endsWith("xlsx")) {
				final ExcelStreamProcessor processor = new ExcelStreamProcessor(
						is);
				processors.add(processor);
			}
		}
		return processors;
	}
	
	private List<IFileHashHelper> populateStoringEngines() {
		final ArrayList<IFileHashHelper> list = new ArrayList<>();
		for (int i = 0; i < this.metadataSourceList.size(); i++) {
			final String filename			= this.metadataSourceList.get(i);
			final IFileHashHelper fileHashHelper	= this.createFileHashHelper(filename);
			list.add(fileHashHelper);
		}
		return list;
	}


	private IFileHashHelper createFileHashHelper(final String filename){
		final InputStream isForHash		= this.getClass().getResourceAsStream(filename);
		final IFileHashHelper hashHelper	= new FileHashHelperImpl();
		hashHelper.setup(filename, isForHash, this.importedFileDao);
		
		return hashHelper;
	}  
	
//	private boolean checkFileAlreadyImported(IFileHashHelper) {
//		boolean result	= true;
//		final InputStream isForHash		= this.getClass().getResourceAsStream(filename);
//		
//		final IFileHashHelper hashHelper	= new FileHashHelperImpl();
//		hashHelper.setup(filename, isForHash, this.importedFileDao);
//		
//		if ( !hashHelper.checkAlreadyLoaded() ) {
//			hashHelper.markAsLoaded();
//			result	= false;
//		}
//
//		return result;
//		
//	}

	
	@SuppressWarnings("rawtypes")
	private Map<String, IStoringEngine<AbstractTranslateable>> populateStoringEnginesMap() {
		if ( this.storingEngineList != null ) {
			final Map<String, IStoringEngine<AbstractTranslateable>> storingEngineMap	= 
					new HashMap<String, IStoringEngine<AbstractTranslateable>>();
			for (final IStoringEngine<AbstractTranslateable> storingEngine : this.storingEngineList) {
				storingEngineMap.put(storingEngine.getRelatedMapper().getName(), storingEngine);
			}
			return storingEngineMap;
		}
		return null;
	}

}
