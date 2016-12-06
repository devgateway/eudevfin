/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.ui.common.scripts;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

public class CommonScriptsReference {

	public static void renderExportPageScripts(IHeaderResponse response) {
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(CommonScriptsReference.class, "canvg-1.3/rgbcolor.js")));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(CommonScriptsReference.class, "canvg-1.3/StackBlur.js")));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(CommonScriptsReference.class, "canvg-1.3/canvg.js")));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(CommonScriptsReference.class, "html2canvas.js")));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(CommonScriptsReference.class, "pixastic.all.js")));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(CommonScriptsReference.class, "jspdf.min.js")));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(CommonScriptsReference.class, "utils.js")));
	}
}
