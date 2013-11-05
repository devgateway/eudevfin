package org.devgateway.eudevfin.cda.test;

import java.sql.Connection;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pentaho.di.core.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { 
		"classpath:META-INF/commonAuthContext.xml",
		"classpath:META-INF/financialContext.xml",
		"classpath:META-INF/cdaContext.xml"
		})
public class CDADatabaseConnectivityTest
{
	
	protected static Logger logger = Logger.getLogger(CDADatabaseConnectivityTest.class);

	@Autowired
	private DataSource dataSourceCda;
	

  

  @Test
  public void testJNDIQuery() throws Exception
  {
	  logger.info("Indirect datasource reference through spring JNDI lookup:"+dataSourceCda);
	  Assert.assertNotNull(dataSourceCda);
	  
	  Object object = InitialContext.doLookup("java:comp/env/myDerbyDataSource");
	  logger.info("Direct JNDI datasource query returned:"+object);
	  
	  Assert.assertNotNull(object);
	  
	  Assert.assertTrue(object.equals(dataSourceCda));

  
  }
  
}