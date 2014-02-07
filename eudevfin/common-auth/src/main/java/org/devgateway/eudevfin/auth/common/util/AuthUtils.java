package org.devgateway.eudevfin.auth.common.util;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.devgateway.eudevfin.auth.common.domain.PersistedUser;
import org.devgateway.eudevfin.common.Constants;
import org.devgateway.eudevfin.financial.Organization;
import org.springframework.security.core.Authentication;
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
	 * Gets the ISO-3166 for the current user
	 * @return
	 */
	public static String getIsoCountryForCurrentUser() {
		PersistedUser user=(PersistedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();				
		if(user==null) return null;
		return getIsoCountryLocaleForUser(user);
	}

	/**
	 * @return the {@link Organization} assigned to the current
	 *         {@link PersistedUser} found in {@link Authentication}
	 */
	public static Organization getOrganizationForCurrentUser() {
		PersistedUser user = (PersistedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user != null && user.getGroup() != null)
			return user.getGroup().getOrganization();
		return null;
	}
}
