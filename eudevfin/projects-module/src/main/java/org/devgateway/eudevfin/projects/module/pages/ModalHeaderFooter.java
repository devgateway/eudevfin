/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.module.pages;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.BootstrapBaseBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.html.ChromeFrameMetaTag;
import de.agilecoders.wicket.core.markup.html.bootstrap.html.HtmlTag;
import de.agilecoders.wicket.core.markup.html.bootstrap.html.OptimizedMobileViewportMetaTag;
import java.util.MissingResourceException;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.filter.FilteredHeaderItem;
import org.apache.wicket.markup.head.filter.HeaderResponseContainer;
import org.apache.wicket.markup.html.GenericWebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.lang.Classes;
import org.devgateway.eudevfin.ui.common.ApplicationJavaScript;
import org.devgateway.eudevfin.ui.common.FixBootstrapStylesCssResourceReference;

/**
 *
 * @author alcr
 */
@SuppressWarnings("WicketForgeJavaIdInspection")
public abstract class ModalHeaderFooter<T> extends GenericWebPage<T> {

    private static final long serialVersionUID = -5670950856779087691L;   
    protected Label pageTitle;

    protected ModalHeaderFooter() {
        initialize();
    }

    protected ModalHeaderFooter(PageParameters parameters) {
        super(parameters);
        initialize();
    }

    protected void initialize() {

        add(new HtmlTag("html"));
        add(new OptimizedMobileViewportMetaTag("viewport"));
        add(new ChromeFrameMetaTag("chrome-frame"));
      
        add(new HeaderResponseContainer("footer-container", "footer-container"));
        add(new BootstrapBaseBehavior());

        try {
            // check if the key is missing in the resource file
            getString(getClassName() + ".page.title");
            pageTitle = new Label("pageTitle", new StringResourceModel(getClassName() + ".page.title", this, null, null));
        } catch (MissingResourceException mre) {
            pageTitle = new Label("pageTitle", new StringResourceModel("page.title", this, null, null));
        }
        add(pageTitle);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(CssHeaderItem.forReference(FixBootstrapStylesCssResourceReference.INSTANCE));
        response.render(new FilteredHeaderItem(JavaScriptHeaderItem.forReference(ApplicationJavaScript.INSTANCE),
				"footer-container"));
    }

    protected String getClassName() {
        return Classes.simpleName(getClass());
    }
}
