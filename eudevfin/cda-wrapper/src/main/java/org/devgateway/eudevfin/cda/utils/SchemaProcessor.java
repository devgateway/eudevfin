package org.devgateway.eudevfin.cda.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import mondrian.i18n.LocalizingDynamicSchemaProcessor;
import mondrian.olap.Util;
import mondrian.olap.Util.PropertyList;

public class SchemaProcessor extends LocalizingDynamicSchemaProcessor {
	
	@Override
	public String processSchema(String schemaUrl, PropertyList connectInfo)
			throws Exception {
        InputStream in = Util.readVirtualFile(schemaUrl);
        return filter(schemaUrl, connectInfo, in);
	}
	
	  /**
     * Reads the contents of a schema as a stream and returns the result as
     * a string.
     *
     * <p>The default implementation returns the contents of the schema
     * unchanged.
     *
     * @param schemaUrl the URL of the catalog
     * @param connectInfo Connection properties
     * @param stream Schema contents represented as a stream
     * @return the modified schema
     * @throws Exception if an error occurs
     */
    public String filter(
        String schemaUrl,
        Util.PropertyList connectInfo,
        InputStream stream)
        throws Exception
    {
    	connectInfo.put("Locale", org.springframework.context.i18n.LocaleContextHolder.getLocale().getLanguage());
    	
        String schema = super.filter(schemaUrl, connectInfo, stream);
        return schema.replaceAll("@@LOCALE@@", org.springframework.context.i18n.LocaleContextHolder.getLocale().getLanguage() );
    }
}
