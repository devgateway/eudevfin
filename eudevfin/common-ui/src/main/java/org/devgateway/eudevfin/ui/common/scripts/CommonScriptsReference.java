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
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(CommonScriptsReference.class, "jspdf.min.js")));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(CommonScriptsReference.class, "utils.js")));
	}
}
