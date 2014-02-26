/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.ui.common.components;

import de.agilecoders.wicket.core.markup.html.bootstrap.components.TooltipBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.components.TooltipConfig;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.extensions.javascript.jasny.FileUploadField;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.devgateway.eudevfin.financial.FileWrapper;
import org.devgateway.eudevfin.financial.FileWrapperContent;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Multi-file upload component that acts as a form component
 * Features:
 * - list of uploaded files with delete/download option
 * - select/change/remove file to upload to the form
 * - tooltips on buttons
 * - max files to upload
 * - can be set to be required
 * <p/>
 * Using the Jasny File Upload to make the file select field look better
 *
 * @author Alexandru Artimon
 * @see de.agilecoders.wicket.extensions.javascript.jasny.FileUploadField
 * @since 25/02/14
 */
public class MultiFileUploadFormComponent extends FormComponentPanel<Collection<FileWrapper>> {
    private int maxFiles = 0;
    private static final TooltipConfig tooltipConfig = new TooltipConfig().withPlacement(TooltipConfig.Placement.bottom);

    public MultiFileUploadFormComponent(String id, final IModel<Collection<FileWrapper>> model) {
        super(id, model);
        setOutputMarkupId(true);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        if (getModel().getObject() == null)
            getModel().setObject(new HashSet<FileWrapper>());
        addComponents();
    }

    private void addComponents() {
        WebMarkupContainer table = new WebMarkupContainer("table") {
            @Override
            protected void onConfigure() {
                setVisibilityAllowed(getModel().getObject().size() > 0);
            }
        };
        add(table);

        AbstractReadOnlyModel<List<FileWrapper>> roModel = new AbstractReadOnlyModel<List<FileWrapper>>() {
            @Override
            public List<FileWrapper> getObject() {
                return new ArrayList<>(getModel().getObject());
            }
        };

        ListView<FileWrapper> list = new ListView<FileWrapper>("list", roModel) {
            @Override
            protected void populateItem(final ListItem<FileWrapper> item) {
                Label label = new Label("label", item.getModelObject().getName());
                item.add(label);

                Link<FileWrapper> download = new DownloadLink("download", item.getModel());
                item.add(download);

                IndicatingAjaxLink delete = new IndicatingAjaxLink("delete") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        MultiFileUploadFormComponent.this.getModel().getObject().remove(item.getModelObject());
                        target.add(MultiFileUploadFormComponent.this);
                        target.add(findParent(DetailedHelpControlGroup.class));
                    }
                };
                delete.add(new IconBehavior(IconType.remove));
                delete.add(new TooltipBehavior(new StringResourceModel("removeUploadedFileTooltip", MultiFileUploadFormComponent.this, null), tooltipConfig));
                item.add(delete);
            }

        };
        table.add(list);


        Form form = new Form("form");
        add(form);

        WebMarkupContainer selectFile = new WebMarkupContainer("selectFile");
        selectFile.add(new TooltipBehavior(new StringResourceModel("selectFileTooltip", MultiFileUploadFormComponent.this, null), tooltipConfig));
        form.add(selectFile);

        @SuppressWarnings("unchecked") final IModel<List<FileUpload>> internalUploadModel = new Model();
        FileUploadField upload = new FileUploadField("file", internalUploadModel);
        selectFile.add(upload);

        WebMarkupContainer cancelSelection = new WebMarkupContainer("cancelSelection");
        cancelSelection.add(new TooltipBehavior(new StringResourceModel("cancelSelectionTooltip", MultiFileUploadFormComponent.this, null), tooltipConfig));
        form.add(cancelSelection);


        UploadButton uploadButton = new UploadButton("uploadFile", internalUploadModel, getModel());
        form.add(uploadButton);
    }


    public MultiFileUploadFormComponent maxFiles(int maxFiles) {
        this.maxFiles = maxFiles;
        return this;
    }

    @Override
    protected void convertInput() {
        final Collection<FileWrapper> modelObject = getModelObject();
        if (modelObject.size() == 0)
            setConvertedInput(null);
        else
            setConvertedInput(modelObject);
    }


    private class DownloadLink extends Link<FileWrapper> {

        public DownloadLink(String id, IModel<FileWrapper> model) {
            super(id, model);
            add(new IconBehavior(IconType.download));
            add(new TooltipBehavior(new StringResourceModel("downloadUploadedFileTooltip", MultiFileUploadFormComponent.this, null), tooltipConfig));
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

    private class UploadButton extends IndicatingAjaxButton {

        private final IModel<List<FileUpload>> uploadModel;
        private final IModel<Collection<FileWrapper>> filesModel;

        public UploadButton(String id, IModel<List<FileUpload>> uploadModel, IModel<Collection<FileWrapper>> filesModel) {
            super(id);
            this.uploadModel = uploadModel;
            this.filesModel = filesModel;

            add(new TooltipBehavior(new StringResourceModel("uploadFileTooltip", MultiFileUploadFormComponent.this, null), tooltipConfig));
        }

        @Override
        protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
            super.onSubmit(target, form);

            List<FileUpload> files = uploadModel.getObject();
            if (files == null) {
                MultiFileUploadFormComponent.this.error(new StringResourceModel("chooseFile", MultiFileUploadFormComponent.this, null).getString());
                MultiFileUploadFormComponent.this.invalid();
            } else {
                if (maxFiles > 0 && filesModel.getObject().size() + files.size() > maxFiles) {
                    MultiFileUploadFormComponent.this.error(new StringResourceModel("tooManyFiles", MultiFileUploadFormComponent.this, null, maxFiles).getString());
                    MultiFileUploadFormComponent.this.invalid();
                }
                for (FileUpload file : files) {
                    FileWrapper fw = new FileWrapper();
                    fw.setName(file.getClientFileName());
                    fw.setContentType(file.getContentType());
                    FileWrapperContent wrapperContent = new FileWrapperContent();
                    wrapperContent.setBytes(file.getBytes());
                    fw.setContent(wrapperContent);
                    filesModel.getObject().add(fw);
                }

            }
            target.add(findParent(DetailedHelpControlGroup.class));
        }
    }

}
