package org.devgateway.eudevfin.reports.core.utils;

import mondrian.i18n.LocalizingDynamicSchemaProcessor;
import mondrian.olap.MondrianProperties;
import mondrian.olap.Util;
import mondrian.olap.Util.PropertyList;

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.auth.common.util.AuthUtils;
import org.devgateway.eudevfin.financial.util.FinancialTransactionUtil;
import org.joda.money.CurrencyUnit;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.io.InputStream;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SchemaProcessor extends LocalizingDynamicSchemaProcessor {
    private static final Logger logger = Logger.getLogger(SchemaProcessor.class);

    private static final String DEFAULT_CURRENCY = "USD";
	private static final String DEFAULT_LOCALE = "en";
    private static final String DEFAULT_COUNTRY_CURRENCY = "EUR";
    private static final String DEFAULT_MONDRIAN_SCHEMA = "org/devgateway/eudevfin/reports/core/service/financial.mondrian.xml";

    private ResourceBundle bundle;

    /**
     * Regular expression for variables.
     */
    private static final Pattern pattern = Pattern.compile("(%\\{.*?\\})");

    @Override
    public String processSchema(String schemaUrl, PropertyList connectInfo)
            throws Exception {
        // get the default schema - load it from the class path
        schemaUrl = this.getClass().getClassLoader().getResource(DEFAULT_MONDRIAN_SCHEMA).getPath();

        // don't read the schema from the file, load it as a stream from the class path
        // InputStream in = Util.readVirtualFile(schemaUrl);
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(DEFAULT_MONDRIAN_SCHEMA);

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
        String currency = connectInfo.get("CURRENCY");
        if (currency == null || currency.equals("")) {
        	currency = getCurrencyParameter();
        }

        String countryCurrency = connectInfo.get("COUNTRY_CURRENCY");
        if (countryCurrency == null || countryCurrency.equals("")) {
            CurrencyUnit currencyForCountryIso = FinancialTransactionUtil
                    .getCurrencyForCountryIso(AuthUtils
                            .getIsoCountryForCurrentUser());
            if (currencyForCountryIso != null) {
                countryCurrency = currencyForCountryIso.getCode();
            } else {
                countryCurrency = DEFAULT_COUNTRY_CURRENCY;
            }
        }

//		String locale = connectInfo.get("LOCALE");
//		if(locale == null || locale.equals("")){
//			locale = DEFAULT_LOCALE;
//			connectInfo.put("Locale", locale);
//		}
        // TODO temporary fix for ODAEU-134
        String locale = DEFAULT_LOCALE;
        connectInfo.put("Locale", locale);
		setLocale(locale);

        // load local properties files
        loadProperties();

        String schema = super.filter(schemaUrl, connectInfo, stream);
        if (bundle != null) {
            schema = doRegExReplacements(schema);
        }
        schema = schema.replaceAll("@@LOCALE@@", locale);
        schema = schema.replaceAll("@@CURRENCY@@", currency);
        schema = schema.replaceAll("@@COUNTRY_CURRENCY@@", countryCurrency);

        return schema;
    }

    private String getCurrencyParameter() {
    	String currency;
    	if(RequestContextHolder.getRequestAttributes() != null && RequestContextHolder.getRequestAttributes().getAttribute("CURRENCY", RequestAttributes.SCOPE_SESSION) != null && !"".equals((String)RequestContextHolder.getRequestAttributes().getAttribute("CURRENCY", RequestAttributes.SCOPE_SESSION)))
    	{
    		currency = (String)RequestContextHolder.getRequestAttributes().getAttribute("CURRENCY", RequestAttributes.SCOPE_SESSION);
    	}
    	else
    	{
    		currency = DEFAULT_CURRENCY;
    	}
		System.out.println("Current Currency:" + currency);
		return currency;
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
