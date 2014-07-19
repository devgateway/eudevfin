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


import org.devgateway.eudevfin.auth.common.service.AuthenticationService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

/**
 * @author mihai
 *
 */
public class RemoteAuthenticationTest extends AbstractAuthTest {
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Test
	public void testAuthenticateUser() {
			Authentication authentication = authenticationService
					.authenticate(new UsernamePasswordAuthenticationToken(
							"admin", "admin"));	
			logger.info("Authenticated as "+authentication);
			Assert.assertNotNull(authentication);
	}
}
