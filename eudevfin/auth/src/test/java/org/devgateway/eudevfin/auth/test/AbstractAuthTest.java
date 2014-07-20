/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
/**
 * 
 */
package org.devgateway.eudevfin.auth.test;

import org.apache.log4j.Logger;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 
 * @author mihai
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/authContext.xml",
		"classpath:META-INF/commonAuthContext.xml",
		"classpath:META-INF/financialContext.xml",
		"classpath:META-INF/importMetadataContext.xml",		
		"classpath:META-INF/commonContext.xml"})
public abstract class AbstractAuthTest {
	
	protected static Logger logger = Logger.getLogger(AbstractAuthTest.class);
}