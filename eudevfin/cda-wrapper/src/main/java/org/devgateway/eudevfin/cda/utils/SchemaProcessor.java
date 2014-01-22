package org.devgateway.eudevfin.cda.utils;

import mondrian.i18n.LocalizingDynamicSchemaProcessor;
import mondrian.olap.MondrianProperties;
import mondrian.olap.Util;
import mondrian.olap.Util.PropertyList;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SchemaProcessor extends LocalizingDynamicSchemaProcessor {
	private static final Logger logger = Logger.getLogger(SchemaProcessor.class);

	private static final String DEFAULT_CURRENCY = "USD";


	private static final String DEFAULT_MONDRIAN_SCHEMA = "/org/devgateway/eudevfin/cda/service/financial.mondrian.xml";

	private ResourceBundle bundle;

	/**
	 * Regular expression for variables.
	 */
	private static final Pattern pattern = Pattern.compile("(%\\{.*?\\})");

	@Override
	public String processSchema(String schemaUrl, PropertyList connectInfo)
			throws Exception {
		// get the default schema
		schemaUrl = this.getClass().getResource(DEFAULT_MONDRIAN_SCHEMA).toString();

		InputStream in = Util.readVirtualFile(schemaUrl);
		return filter(schemaUrl, connectInfo, in);
	}

	void populate(String propFile) {
		if (propFile.endsWith(".properties")) {
			propFile = propFile.substring(0, propFile.length() - ".properties".length());
		} try {
			bundle = ResourceBundle.getBundle(propFile, Util.parseLocale(this.getLocale()), getClass().getClassLoader());
		} catch (Exception e) {
			logger.info("Mondrian: Warning: no suitable locale file found for locale '" + this.getLocale() + "'", e);
		}
	}

	private void loadProperties() {
		String propFile = MondrianProperties.instance().LocalePropFile.get();

		if (propFile != null) {
			populate(propFile);
		}
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
	public String filter(String schemaUrl, Util.PropertyList connectInfo, InputStream stream) throws Exception {
		String locale = org.springframework.context.i18n.LocaleContextHolder.getLocale().getLanguage();
		setLocale(locale);

		String currency = connectInfo.get("CURRENCY");
		if(currency == null || currency.equals("")){
			currency = DEFAULT_CURRENCY;
		}

		connectInfo.put("Locale", locale);

		// load local properties files
		loadProperties();

		String schema = super.filter(schemaUrl, connectInfo, stream);
		if (bundle != null) {
			schema = doRegExReplacements(schema);
		}
		schema = schema.replaceAll("@@LOCALE@@", locale);
		schema = schema.replaceAll("@@CURRENCY@@", currency);

		return schema;
	}

	private String doRegExReplacements (String schema) {
		StringBuffer intlSchema = new StringBuffer();
		Matcher match = pattern.matcher(schema);
		String key;
		while (match.find()) {
			key = extractKey(match.group());
			int start = match.start();
			int end = match.end();

			try {
				String intlProperty = bundle.getString(key);
				if (intlProperty != null) {
					match.appendReplacement(intlSchema, intlProperty);
				}
			} catch (MissingResourceException e) {
				logger.error("Missing resource for key [" + key + "]", e);
			} catch (NullPointerException e) {
				logger.error("missing resource key at substring(" + start + "," + end + ")", e);
			}
		}

		match.appendTail(intlSchema);

		return intlSchema.toString();
	}

	/**
	 * Removes leading '%{' and tailing '%' from the matched string
	 * to obtain the required key
	 */
	private String extractKey (String group) {
		return group.substring(2, group.length() - 1);
	}
}
