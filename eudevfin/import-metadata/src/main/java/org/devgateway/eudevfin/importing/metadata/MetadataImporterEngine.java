package org.devgateway.eudevfin.importing.metadata;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.financial.dao.AbstractDaoImpl;
import org.devgateway.eudevfin.financial.dao.AreaDaoImpl;
import org.devgateway.eudevfin.financial.dao.CategoryDaoImpl;
import org.devgateway.eudevfin.financial.dao.OrganizationDaoImpl;
import org.devgateway.eudevfin.importing.metadata.mapping.AreaMapper;
import org.devgateway.eudevfin.importing.metadata.mapping.CategoryMapper;
import org.devgateway.eudevfin.importing.metadata.mapping.ChannelCategoryMapper;
import org.devgateway.eudevfin.importing.metadata.mapping.OrganizationMapper;
import org.devgateway.eudevfin.importing.metadata.streamprocessors.ExcelStreamProcessor;
import org.devgateway.eudevfin.importing.metadata.streamprocessors.StreamProcessorInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MetadataImporterEngine {
	private static Logger logger	= Logger.getLogger(MetadataImporterEngine.class);
	
	@Autowired
	OrganizationDaoImpl orgDaoImpl;
	
	@Autowired
	CategoryDaoImpl categDaoImpl;
	
	@Autowired
	AreaDaoImpl areaDaoImpl;
	
	
	@Resource(name="metadataSourceList")
	List<String> metadataSourceList;
	

	HashMap<String, AbstractDaoImpl> servicesMap;

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void process () {
		this.populateServicesMap();
		
		List<StreamProcessorInterface> list 	= this.getStreamProcessors();
		
		for (int i=0; i<list.size(); i++) {
			StreamProcessorInterface streamProcessorInterface = list.get(i);
			logger.info( String.format("%d) Starting import...",i+1) );
			AbstractDaoImpl service	= 
					this.servicesMap.get(streamProcessorInterface.getMapperClassName() );
			int numOfSavedEntities	= 0;
			while ( streamProcessorInterface.hasNextObject() ) {
				Object entity	= streamProcessorInterface.generateNextObject();
				service.save(entity);
				numOfSavedEntities++;
			}
			logger.info(String.format("-> finsihed! Imported %d entities !", numOfSavedEntities));
			
		}
	}


	private List<StreamProcessorInterface> getStreamProcessors() {
		logger.info("Will start import for the following files:");
		ArrayList<StreamProcessorInterface> processors = new ArrayList<>();
		for (int i = 0; i < this.metadataSourceList.size(); i++) {
			String filename		= this.metadataSourceList.get(i);
			logger.info( String.format("%d) %s", i+1, filename) );
			InputStream is		= this.getClass().getResourceAsStream(filename);
			if ( filename.endsWith("xls") || filename.endsWith("xlsx") ) {
				ExcelStreamProcessor processor	= new ExcelStreamProcessor(is);
				processors.add(processor); 
			}
		}
		return processors;
	}


	private void populateServicesMap() {
		this.servicesMap	= new HashMap<String, AbstractDaoImpl>();
		this.servicesMap.put(OrganizationMapper.class.getName(), orgDaoImpl);
		this.servicesMap.put(CategoryMapper.class.getName(), categDaoImpl);
		this.servicesMap.put(AreaMapper.class.getName(), areaDaoImpl);
		this.servicesMap.put(ChannelCategoryMapper.class.getName(), categDaoImpl);
		
	}

}
