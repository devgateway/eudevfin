package org.devgateway.eudevfin.auth.security;

import java.util.HashSet;
import java.util.Set;

import org.devgateway.eudevfin.auth.common.domain.PersistedAuthority;
import org.devgateway.eudevfin.auth.common.domain.PersistedUser;
import org.devgateway.eudevfin.auth.common.domain.PersistedUserGroup;
import org.devgateway.eudevfin.auth.repository.PersistedUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@link UserDetailsService} that uses JPA
 * 
 * @author mihai, krams
 * @see http
 *      ://krams915.blogspot.fi/2012/01/spring-security-31-implement_3065.html
 */
@Transactional(readOnly = true)
public class CustomJPAUserDetailsService implements UserDetailsService {

	@Autowired
	private PersistedUserRepository userRepository;

	/**
	 * Returns a populated {@link UserDetails} object. The username is first
	 * retrieved from the database and then mapped to a {@link UserDetails}
	 * object. We are currently using the {@link User} implementation from Spring
	 */
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		try {
			PersistedUser domainUser = userRepository
					.findByUsername(username);
			
			Set<GrantedAuthority> grantedAuthorities = getGrantedAuthorities(domainUser);
			domainUser.setAuthorities(grantedAuthorities);
			return domainUser;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Reads {@link PersistedAuthority} objects from the
	 * {@link org.devgateway.eudevfin.auth.common.domain.PersistedUser#getPersistedAuthorities()} and also from the
	 * {@link PersistedUserGroup#getPersistedAuthorities()} (only if the {@link User} belongs to only one
	 * {@link PersistedUserGroup}) and converts all {@link PersistedAuthority} objects to
	 * {@link GrantedAuthority}. 
	 * 
	 * @param domainUser
	 * @return a {@link Set} containing the {@link GrantedAuthority}S
	 */
	public static Set<GrantedAuthority> getGrantedAuthorities(
			PersistedUser domainUser) {

		Set<GrantedAuthority> grantedAuth = new HashSet<GrantedAuthority>();

		// get user authorities
		for (PersistedAuthority authority : domainUser.getPersistedAuthorities()) {
			grantedAuth
					.add(new SimpleGrantedAuthority(authority.getAuthority()));
		}

		//add any existing group authorities, if any
		if (domainUser.getGroup() != null && domainUser.getGroup().getPersistedAuthorities() != null)
			for (PersistedAuthority authority : domainUser.getGroup().getPersistedAuthorities()) {
				grantedAuth.add(new SimpleGrantedAuthority(authority.getAuthority()));
			}

		return grantedAuth;
	}
}
