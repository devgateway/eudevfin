/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.ui.common.components.util;

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
import org.devgateway.eudevfin.financial.FileWrapper;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Alexandru Artimon
 * @since 18/09/14
 */
public class DownloadLink extends Link<FileWrapper> {
    private static final TooltipConfig tooltipConfig = new TooltipConfig().withPlacement(TooltipConfig.Placement.bottom);

    public DownloadLink(String id, IModel<FileWrapper> model) {
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