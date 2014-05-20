package org.devgateway.eudevfin.reports.core.test;

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.common.locale.LocaleHelperInterface;
import org.devgateway.eudevfin.reports.core.domain.QueryResult;
import org.devgateway.eudevfin.reports.core.service.QueryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:META-INF/commonAuthContext.xml",
        "classpath:META-INF/commonContext.xml",
        "classpath:META-INF/authContext.xml",
        "classpath:META-INF/financialContext.xml",
        "classpath:META-INF/commonFinancialContext.xml",
        "classpath:META-INF/importMetadataContext.xml",
        "classpath:META-INF/exchangeContext.xml",
        "classpath:META-INF/commonExchangeContext.xml",
        "classpath:META-INF/reportsCoreContext.xml",
        "classpath:testCDAContext.xml"
})
public class CDAServiceTest {
    protected static Logger logger = Logger.getLogger(CDAServiceTest.class);

    public static final String TEST_LOCALE = "en";

    @Autowired
    QueryService service;

    @Autowired
    @Qualifier("localeHelperRequest")
    LocaleHelperInterface localeHelper;

    @Before
    public void setUp () {
        localeHelper.setLocale(TEST_LOCALE);
    }

    @Test
    public void testCDAService () {
        Map<String,String> params = new HashMap<String, String>();
        params.put("dataAccessId", "simpleMDXQuery");

        QueryResult result = service.doQuery(params);
        logger.info("Result: " + result.toString());
        assertTrue((result.toString() != ""));
    }
}
