package org.devgateway.eudevfin.cda.utils;

import java.io.InputStream;

import mondrian.i18n.LocalizingDynamicSchemaProcessor;
import mondrian.olap.Util;
import mondrian.olap.Util.PropertyList;

public class SchemaProcessor extends LocalizingDynamicSchemaProcessor {
	
	private static final String DEFAULT_CURRENCY = "USD";

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
        String locale = org.springframework.context.i18n.LocaleContextHolder.getLocale().getLanguage();
        String currency = connectInfo.get("CURRENCY");
        if(currency == null || currency.equals("")){
        	currency = DEFAULT_CURRENCY;
        }

        connectInfo.put("Locale", locale);
    	
        String schema = super.filter(schemaUrl, connectInfo, stream);
        schema = schema.replaceAll("@@LOCALE@@", locale);
        schema = schema.replaceAll("@@CURRENCY@@", currency);
        return schema;
    }
}
