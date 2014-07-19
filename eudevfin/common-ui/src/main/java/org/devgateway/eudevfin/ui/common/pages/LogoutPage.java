/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/

package org.devgateway.eudevfin.ui.common.pages;

import org.apache.wicket.markup.html.GenericWebPage;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath(value = "/logout")
public class LogoutPage extends GenericWebPage {
    public LogoutPage() {
        getSession().invalidate();
        setResponsePage(getApplication().getHomePage());
    }
}
