/**
 * 
 */
package org.devgateway.eudevfin.auth.common.test;

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
@ContextConfiguration(locations = { "classpath:/META-INF/authContext.xml",
		"classpath:/META-INF/commonAuthContext.xml",
		"classpath:/META-INF/financialContext.xml" })
public abstract class AbstractAuthTest {
	
	protected static Logger logger = Logger.getLogger(AbstractAuthTest.class);
}