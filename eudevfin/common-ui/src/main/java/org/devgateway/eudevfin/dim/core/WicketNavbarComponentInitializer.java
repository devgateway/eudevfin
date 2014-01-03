package org.devgateway.eudevfin.dim.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar.ComponentPosition;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface WicketNavbarComponentInitializer {
	ComponentPosition position();
}
