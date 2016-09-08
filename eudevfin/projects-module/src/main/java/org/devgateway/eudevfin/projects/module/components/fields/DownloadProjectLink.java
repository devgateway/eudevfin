/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.module.components.fields;

import de.agilecoders.wicket.core.markup.html.bootstrap.components.TooltipBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.components.TooltipConfig;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;

import java.io.IOException;
import java.io.OutputStream;
import org.devgateway.eudevfin.projects.common.entities.ProjectFileWrapper;

public class DownloadProjectLink extends Link<ProjectFileWrapper> {
    private static final TooltipConfig tooltipConfig = new TooltipConfig().withPlacement(TooltipConfig.Placement.bottom);

    public DownloadProjectLink(String id, IModel<ProjectFileWrapper> model) {
        super(id, model);
        add(new IconBehavior(IconType.download));
        add(new TooltipBehavior(new StringResourceModel("downloadUploadedFileTooltip", this, null), tooltipConfig));
    }

    @Override
    public void onClick() {
        AbstractResourceStreamWriter rstream = new AbstractResourceStreamWriter() {
            @Override
            public void write(OutputStream output) throws IOException {
                output.write(getModelObject().getContent().getBytes());
            }

            @Override
            public String getContentType() {
                return getModelObject().getContentType();
            }
        };

        ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(rstream, getModelObject().getName());
        handler.setContentDisposition(ContentDisposition.ATTACHMENT);
        getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
    }
}
