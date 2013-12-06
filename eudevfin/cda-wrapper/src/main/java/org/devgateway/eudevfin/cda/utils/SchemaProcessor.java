package org.devgateway.eudevfin.cda.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import mondrian.olap.Util;
import mondrian.olap.Util.PropertyList;
import mondrian.spi.impl.FilterDynamicSchemaProcessor;

public class SchemaProcessor extends FilterDynamicSchemaProcessor {
	
	@Override
	public String processSchema(String schemaUrl, PropertyList connectInfo)
			throws Exception {
		
		//@@LOCALE@@
		
		
		
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
    protected String filter(
        String schemaUrl,
        Util.PropertyList connectInfo,
        InputStream stream)
        throws Exception
    {
        BufferedReader in =
            new BufferedReader(
                new InputStreamReader(stream));
        try {
            StringBuilder builder = new StringBuilder();
            char[] buf = new char[2048];
            int readCount;
            while ((readCount = in.read(buf, 0, buf.length)) >= 0) {
                builder.append(buf, 0, readCount);
            }
            return builder.toString().replaceAll("@@LOCALE@@", org.springframework.context.i18n.LocaleContextHolder.getLocale().getLanguage() );
        } finally {
            in.close();
        }
    }
}
