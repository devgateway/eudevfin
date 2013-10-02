package org.devgateway.eudevfin.forms.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.request.Request;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

public class SpringWicketWebSession extends AuthenticatedWebSession {

	private static final Logger logger = Logger
			.getLogger(SpringWicketWebSession.class);

	@SpringBean(name = "authenticationManager")
	private AuthenticationManager authenticationManager;

	private HttpSession httpSession;

	public SpringWicketWebSession(Request request) {
		super(request);
		injectDependencies();
		ensureDependenciesNotNull();
		httpSession = ((HttpServletRequest) request.getContainerRequest())
				.getSession();
	}

	public static SpringWicketWebSession getSpringWicketWebSession() {
		return (SpringWicketWebSession) Session.get();
	}

	private void ensureDependenciesNotNull() {
		if (authenticationManager == null) {
			throw new IllegalStateException("Requires an authentication");
		}
	}

	private void injectDependencies() {
		Injector.get().inject(this);
	}

	@Override
	public boolean authenticate(String username, String password) {
		boolean authenticated = false;
		try {
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(
							username, password));
			SecurityContextHolder.getContext()
					.setAuthentication(authentication);
			authenticated = authentication.isAuthenticated();

		} catch (AuthenticationException e) {
			logger.warn("User " + username + " failed to login. Reason: ", e);
			authenticated = false;
		}
		return authenticated;
	}

	@Override
	public Roles getRoles() {
		Roles roles = new Roles();
		getRolesIfSignedIn(roles);
		return roles;
	}

	private void getRolesIfSignedIn(Roles roles) {
		if (isSignedIn()) {
			Authentication authentication = SecurityContextHolder.getContext()
					.getAuthentication();
			addRolesFromAuthentication(roles, authentication);
		}
	}

	private void addRolesFromAuthentication(Roles roles,
			Authentication authentication) {
		for (GrantedAuthority authority : authentication.getAuthorities()) {
			roles.add(authority.getAuthority());
		}
	}

}