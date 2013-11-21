package org.devgateway.eudevfin.cda.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

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
		Map<String,String> params = new HashMap<String, String>();
		params.put("dataAccessId", "simpleSQLQuery");
		
		QueryResult result = service.doQuery(params);
		logger.info("Result: " + result.toString());
		assertTrue((result.toString() != ""));
	}

}
