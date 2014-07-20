/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.auth.common.util;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.devgateway.eudevfin.auth.common.domain.PersistedUser;
import org.devgateway.eudevfin.common.Constants;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 
 * @author mihai
 *
 */
public final class AuthUtils {

	public static final Map<String,String> countryNameIso2Map= new ConcurrentHashMap<String,String>();
	
	/**
	 * Initializes a simple singleton with country names / Locales
	 * Maybe we should move this to a spring bean!
	 */
	static {
		
		for (int i = 0; i < Locale.getISOCountries().length; i++) {
			Locale locale=new Locale("",Locale.getISOCountries()[i]);
			countryNameIso2Map.put(locale.getDisplayCountry(),Locale.getISOCountries()[i]);
		}
		
		
	}
	
		/**
	 * Returns the {@link Locale} for the current user. Uses the {@link Organization} to read the 
	 * country name using {@link Organization#getDonorName()}
	 * @param user
	 * @return
	 */
	public static String getIsoCountryLocaleForUser(PersistedUser user) {
		Organization organization = user.getGroup().getOrganization();
		if(organization==null) return null;
		
		String locale = organization.getLocale();
		organization.setLocale(Constants.DEFAULT_LOCALE);
		String donorName = organization.getDonorName();
		organization.setLocale(locale);
		
		//try to get the ISO-3166 for the country/donor name
		return countryNameIso2Map.get(donorName);
	}
	
	/**
	 * Gets the current logged in user, if any
	 * 
	 * @return the current logged in user
	 */
	public static PersistedUser getCurrentUser() {
		return (PersistedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	/**
	 * Gets the ISO-3166 for the current user
	 * @return
	 */
	public static String getIsoCountryForCurrentUser() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return null;
        }
		PersistedUser user=getCurrentUser();			
		if(user==null) return null;
		return getIsoCountryLocaleForUser(user);
	}

	
	/**
	 * Checks if the given user has the specified role
	 * 
	 * @param user
	 * @param role
	 * @return true if it has the role
	 */
	public static boolean userHasRole(PersistedUser user, String role) {
		for (GrantedAuthority authority : user.getAuthorities())
			if (authority.getAuthority().equals(role))
				return true;
		return false;
	}

	/**
	 * Checks if the current logged in user (if any) has the specified role
	 * 
	 * @param role
	 * @return true if it has the role
	 */
	public static boolean currentUserHasRole(String role) {
		PersistedUser user = getCurrentUser();	
		if (user == null)
			return false;
		return userHasRole(user, role);
	}
	
	/**
	 * @return the {@link Organization} assigned to the current
	 *         {@link PersistedUser} found in {@link Authentication}
	 */
	public static Organization getOrganizationForCurrentUser() {
		PersistedUser user = getCurrentUser();	
		if (user != null && user.getGroup() != null)
			return user.getGroup().getOrganization();
		return null;
	}
}
