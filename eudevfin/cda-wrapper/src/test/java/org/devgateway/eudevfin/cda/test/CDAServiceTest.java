package org.devgateway.eudevfin.cda.test;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.cda.domain.QueryResult;
import org.devgateway.eudevfin.cda.service.QueryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { 
		"classpath:META-INF/commonAuthContext.xml",
		"classpath:META-INF/financialContext.xml",
		"classpath:META-INF/cdaContext.xml"
		})

public class CDAServiceTest {
	protected static Logger logger = Logger.getLogger(CDAServiceTest.class);
	
	@Autowired
	QueryService service;

	@Test
	public void test() {
		QueryResult result = service.doQuery("simpleSQLQuery");
		logger.info("Result: " + result.toString());
		assertTrue((result.toString() != ""));
	}

}
