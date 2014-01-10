package org.devgateway.eudevfin.importing.metadata;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.devgateway.eudevfin.common.service.BaseEntityService;
import org.devgateway.eudevfin.financial.AbstractTranslateable;
import org.devgateway.eudevfin.financial.Category;
import org.devgateway.eudevfin.financial.Organization;
import org.devgateway.eudevfin.financial.dao.AbstractDaoImpl;
import org.devgateway.eudevfin.financial.dao.OrganizationDaoImpl;
import org.devgateway.eudevfin.financial.service.CategoryService;
import org.devgateway.eudevfin.financial.service.OrganizationService;
import org.devgateway.eudevfin.importing.metadata.mapping.OrganizationMapper;
import org.devgateway.eudevfin.importing.metadata.streamprocessors.ExcelStreamProcessor;
import org.devgateway.eudevfin.importing.metadata.streamprocessors.StreamProcessorInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class MetadataImporterEngine {
	@Autowired
	OrganizationDaoImpl orgDaoImpl;
	
	
	@Resource(name="metadataSourceList")
	List<String> metadataSourceList;
	

	HashMap<String, AbstractDaoImpl> servicesMap;

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void process () {
		this.populateServicesMap();
		
		List<StreamProcessorInterface> list 	= this.getStreamProcessors();
		
		for (StreamProcessorInterface streamProcessorInterface : list) {
			List<? extends AbstractTranslateable> entities			= streamProcessorInterface.generateObjectList();
			for (AbstractTranslateable abstractTranslateable : entities) {
				AbstractDaoImpl service	= 
						this.servicesMap.get(streamProcessorInterface.getEntityClassName() );
				service.save(abstractTranslateable);
			}
			
		}
	}


	private List<StreamProcessorInterface> getStreamProcessors() {
		ArrayList<StreamProcessorInterface> processors = new ArrayList<>();
		for (String filename : this.metadataSourceList) {
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
		
	}

}
