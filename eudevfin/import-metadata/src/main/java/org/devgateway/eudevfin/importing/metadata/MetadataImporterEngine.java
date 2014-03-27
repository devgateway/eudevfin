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
import org.devgateway.eudevfin.financial.dao.CategoryDaoImpl;
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
	
//	@Autowired
//	OrganizationDaoImpl orgDaoImpl;
//	
	@Autowired
	CategoryDaoImpl categDaoImpl;
//	
//	@Autowired
//	AreaDaoImpl areaDaoImpl;
	
	@SuppressWarnings("rawtypes")
	Map<String, AbstractDaoImpl> servicesMap;
	
	@Resource(name="metadataSourceList")
	List<String> metadataSourceList;
	
	@SuppressWarnings("rawtypes")
	@Resource
	List<IStoringEngine<AbstractTranslateable>> storingEngineList;
	
	@SuppressWarnings("rawtypes")
	Map<String, IStoringEngine<AbstractTranslateable>> storingEngineMap;

	
	@SuppressWarnings({ "rawtypes" })
	public void process () {
//		this.populateServicesMap();
		this.populateStoringEnginesMap();
		
		final List<StreamProcessorInterface> list 	= this.getStreamProcessors();
		
		for (int i=0; i<list.size(); i++) {
			final StreamProcessorInterface streamProcessorInterface = list.get(i);
			logger.info( String.format("%d) Starting import...",i+1) );
//			final AbstractDaoImpl service	= 
//					this.servicesMap.get(streamProcessorInterface.getMapperClassName() );
			final IStoringEngine<AbstractTranslateable> storingEngine	= 
					this.storingEngineMap.get(streamProcessorInterface.getMapperClassName());
			int numOfSavedEntities	= 0;
			while ( streamProcessorInterface.hasNextObject() ) {
				final Object entity	= streamProcessorInterface.generateNextObject();
				try {
//					service.save(entity);
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
			logger.info(String.format("-> finsihed! Imported %d entities !", numOfSavedEntities));
			streamProcessorInterface.close();
			
		}
	}


	private List<StreamProcessorInterface> getStreamProcessors() {
		logger.info("Will start import for the following files:");
		// A little hack to know if the import was ever run on this platform
		final boolean firstImportOnPlatform		= this.categDaoImpl.findAllAsList().size() == 0;
		final boolean firstRunOfUpdaterEngine	= this.importedFileDao.findAll().size() == 0;
		
		final ArrayList<StreamProcessorInterface> processors = new ArrayList<>();
		for (int i = 0; i < this.metadataSourceList.size(); i++) {
			final String filename		= this.metadataSourceList.get(i);
			logger.info( String.format("%d) %s", i+1, filename) );
			
			
			if ( !this.checkFileAlreadyImported(filename,firstImportOnPlatform, firstRunOfUpdaterEngine) ) {
				final InputStream is		= this.getClass().getResourceAsStream(filename);
				if ( filename.endsWith("xls") || filename.endsWith("xlsx") ) {
					final ExcelStreamProcessor processor	= new ExcelStreamProcessor(is);
					processors.add(processor); 
				}
			}
		}
		return processors;
	}


	private boolean checkFileAlreadyImported(final String filename, final boolean firstImportOnPlatform, final boolean firstRunOfUpdaterEngine) {
		boolean result	= true;
		final InputStream isForHash		= this.getClass().getResourceAsStream(filename);
		
		final IFileHashHelper hashHelper	= new FileHashHelperImpl();
		hashHelper.setup(filename, isForHash, this.importedFileDao);
		
//		if (!firstImportOnPlatform && firstRunOfUpdaterEngine) {
//			hashHelper.markAsLoaded();
//		}
//		else {
			if ( !hashHelper.checkAlreadyLoaded() ) {
				hashHelper.markAsLoaded();
				result	= false;
			}
//		}

		return result;
		
	}

//	private void populateServicesMap() {
//		this.servicesMap	= new HashMap<String, AbstractDaoImpl>();
//		this.servicesMap.put(OrganizationMapper.class.getName(), this.orgDaoImpl);
//		this.servicesMap.put(CategoryMapper.class.getName(), this.categDaoImpl);
//		this.servicesMap.put(AreaMapper.class.getName(), this.areaDaoImpl);
//		this.servicesMap.put(ChannelCategoryMapper.class.getName(), this.categDaoImpl);
//		this.servicesMap.put(CurrencyCategoryMapper.class.getName(), this.categDaoImpl);
//	}
	
	private void populateStoringEnginesMap() {
		if ( this.storingEngineList != null ) {
			this.storingEngineMap	= new HashMap<String, IStoringEngine<AbstractTranslateable>>();
			for (final IStoringEngine<AbstractTranslateable> storingEngine : this.storingEngineList) {
				this.storingEngineMap.put(storingEngine.getRelatedMapper().getName(), storingEngine);
			}
		}
	}

}
