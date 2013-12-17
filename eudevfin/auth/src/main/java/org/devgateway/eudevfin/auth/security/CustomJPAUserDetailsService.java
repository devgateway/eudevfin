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

			boolean enabled = true;
			boolean accountNonExpired = true;
			boolean credentialsNonExpired = true;

			return new User(domainUser.getUsername(), domainUser.getPassword()
					.toLowerCase(), enabled, accountNonExpired,
					credentialsNonExpired, domainUser.isEnabled(),
					getGrantedAuthorities(domainUser));

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Reads {@link PersistedAuthority} objects from the
	 * {@link org.devgateway.eudevfin.auth.common.domain.PersistedUser#getAuthorities()} and also from the
	 * {@link PersistedUserGroup#getAuthorities()} (only if the {@link User} belongs to only one
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
		for (PersistedAuthority authority : domainUser.getAuthorities()) {
			grantedAuth
					.add(new SimpleGrantedAuthority(authority.getAuthority()));
		}

		// get group authorities ONLY IF the user belongs to one group.
		// otherwise this selection needs to be done later, the user will be
		// prompted to choose a working group
		if (domainUser.getGroups() != null & domainUser.getGroups().size() == 1)
			for (PersistedAuthority authority : domainUser.getGroups().iterator().next()
					.getAuthorities()) {
				grantedAuth.add(new SimpleGrantedAuthority(authority
						.getAuthority()));
			}

		return grantedAuth;
	}
}
