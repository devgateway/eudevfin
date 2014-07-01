package org.devgateway.eudevfin.exchange.test;

/*******************************************************************************
 * Copyright (c)  2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors:
 *    mpostelnicu
 ******************************************************************************/

import java.util.LinkedHashMap;

import org.devgateway.eudevfin.exchange.service.ExternalExchangeQueryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author mpostelnicu
 * @since 23 October 2013
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/exchangeContext.xml"})
public class TestGetAllCurrencies {
	
	@Autowired
	ExternalExchangeQueryService exchangeQueryService;
	
	@Test
	public void testGetAllCurrencies() {	
		LinkedHashMap<String,String> json = exchangeQueryService.getCurrenciesAndNames();
		//System.out.println(json);
	}
	
	
}
