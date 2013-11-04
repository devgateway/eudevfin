package org.devgateway.eudevfin.cda.test;

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
public class CDAJNDITest
{
	
	protected static Logger logger = Logger.getLogger(CDAJNDITest.class);

	@Autowired
	private DataSource dataSourceCda;
	

  @Test
  public void testJNDIDataSource() throws Exception
  {
	  
	  Assert.assertNotNull(dataSourceCda.getConnection());

  
  }}