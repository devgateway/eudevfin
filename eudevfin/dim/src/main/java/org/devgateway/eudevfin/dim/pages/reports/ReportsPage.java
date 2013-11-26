/*******************************************************************************
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors:
 *    aartimon
 ******************************************************************************/

package org.devgateway.eudevfin.dim.pages.reports;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.devgateway.eudevfin.dim.core.Constants;
import org.devgateway.eudevfin.dim.core.pages.HeaderFooter;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath(value = "/reports")
@AuthorizeInstantiation(Constants.ROLE_USER)
public class ReportsPage extends HeaderFooter {

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(JavaScriptHeaderItem.forUrl("/js/cdfplugin.js"));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(ReportsPage.class, "reports.js")));
    }
}
