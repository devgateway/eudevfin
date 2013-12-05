package org.devgateway.eudevfin.cda.utils;

import mondrian.olap.Util.PropertyList;
import mondrian.spi.impl.FilterDynamicSchemaProcessor;

public class SchemaProcessor extends FilterDynamicSchemaProcessor {
	
	@Override
	public String processSchema(String schemaUrl, PropertyList connectInfo)
			throws Exception {

		return super.processSchema(schemaUrl, connectInfo);
	}

}
