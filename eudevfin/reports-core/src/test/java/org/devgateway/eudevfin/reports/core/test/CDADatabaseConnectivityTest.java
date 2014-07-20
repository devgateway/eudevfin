/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.reports.core.test;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.naming.InitialContext;
import javax.sql.DataSource;


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
        "classpath:META-INF/reportsCoreContext.xml"
})
public class CDADatabaseConnectivityTest {
    protected static Logger logger = Logger.getLogger(CDADatabaseConnectivityTest.class);

    @Autowired
    private DataSource cdaDataSource;

    @Test
    public void testJNDIQuery () throws Exception {
        logger.info("Indirect datasource reference through spring JNDI lookup:" + cdaDataSource);
        Assert.assertNotNull(cdaDataSource);

        Object object = InitialContext.doLookup("java:comp/env/euDevFinDS");
        logger.info("Direct JNDI datasource query returned:" + object);

        Assert.assertNotNull(object);

        Assert.assertTrue(object.equals(cdaDataSource));
    }
}