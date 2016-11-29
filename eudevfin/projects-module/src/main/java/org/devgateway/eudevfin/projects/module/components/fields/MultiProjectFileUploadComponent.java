/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/

package org.devgateway.eudevfin.projects.module.components.fields;

import de.agilecoders.wicket.core.markup.html.bootstrap.components.TooltipBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.components.TooltipConfig;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.extensions.javascript.jasny.FileUploadField;
import org.apache.wicket.MarkupContainer;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.devgateway.eudevfin.projects.common.entities.ProjectFileContent;
import org.devgateway.eudevfin.projects.common.entities.ProjectFileWrapper;
import org.devgateway.eudevfin.projects.module.components.util.ProjectFileType;
import org.devgateway.eudevfin.ui.common.components.DetailedHelpControlGroup;
import org.devgateway.eudevfin.ui.common.components.PreviewableFormPanelAware;

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
public class MultiProjectFileUploadComponent extends FormComponentPanel<Collection<ProjectFileWrapper>> implements PreviewableFormPanelAware {
    private int maxFiles = 0;
    private static final TooltipConfig tooltipConfig = new TooltipConfig().withPlacement(TooltipConfig.Placement.bottom);
    protected ProjectFileType fileType = ProjectFileType.EXTENDED;

    public MultiProjectFileUploadComponent(String id, final IModel<Collection<ProjectFileWrapper>> model) {
        super(id, model);
        setOutputMarkupId(true);
    }

    @Override
    public boolean isFormInPreview() {
    	MarkupContainer parent = this.getParent();
    	
    	while (parent != null && !(parent instanceof PreviewableFormPanelAware))
			parent = parent.getParent();
    	
    	if (parent instanceof PreviewableFormPanelAware)
			return ((PreviewableFormPanelAware) parent).isFormInPreview();
    	return false;
    }
    
    @Override
    protected void onInitialize() {
        super.onInitialize();

        if (getModel().getObject() == null)
            getModel().setObject(new HashSet<ProjectFileWrapper>());
        addComponents();
    }

    private void addComponents() {
        WebMarkupContainer table = new WebMarkupContainer("table") {
            @Override
            protected void onConfigure() {
                setVisibilityAllowed(getModel().getObject() != null && getModel().getObject().size() > 0);
            }
        };
        add(table);

        AbstractReadOnlyModel<List<ProjectFileWrapper>> roModel = new AbstractReadOnlyModel<List<ProjectFileWrapper>>() {
            @Override
            public List<ProjectFileWrapper> getObject() {
                List<ProjectFileWrapper> fileList = new ArrayList<>();
                
                for(ProjectFileWrapper file : getModel().getObject()) {
                    if (ProjectFileType.fromString(file.getTags()) == fileType) 
                        fileList.add(file);
                }
                
                return fileList;
            }
        };
        
        ListView<ProjectFileWrapper> list = new ListView<ProjectFileWrapper>("list", roModel) {
            @Override
            protected void populateItem(final ListItem<ProjectFileWrapper> item) {
                Label label = new Label("label", item.getModelObject().getName());
                item.add(label);

                Link<ProjectFileWrapper> download = new DownloadProjectLink("download", item.getModel());
                item.add(download);

                IndicatingAjaxLink delete = new IndicatingAjaxLink("delete") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        MultiProjectFileUploadComponent.this.getModel().getObject().remove(item.getModelObject());
                        target.add(MultiProjectFileUploadComponent.this);
                        target.add(findParent(DetailedHelpControlGroup.class));
                    }
                };
                delete.add(new IconBehavior(IconType.remove));
                delete.add(new TooltipBehavior(new StringResourceModel("removeUploadedFileTooltip", MultiProjectFileUploadComponent.this, null), tooltipConfig));
                delete.setVisible(!isFormInPreview());
                item.add(delete);
            }
        };
        table.add(list);


        Form form = new Form("form");
        form.setVisible(!isFormInPreview());
        add(form);

        WebMarkupContainer selectFile = new WebMarkupContainer("selectFile");
        selectFile.add(new TooltipBehavior(new StringResourceModel("selectFileTooltip", MultiProjectFileUploadComponent.this, null), tooltipConfig));
        form.add(selectFile);


        @SuppressWarnings("unchecked") final IModel<List<FileUpload>> internalUploadModel = new Model();
        FileUploadField upload = new FileUploadField("file", internalUploadModel);
        selectFile.add(upload);

        WebMarkupContainer cancelSelection = new WebMarkupContainer("cancelSelection");
        cancelSelection.add(new TooltipBehavior(new StringResourceModel("cancelSelectionTooltip", MultiProjectFileUploadComponent.this, null), tooltipConfig));
        form.add(cancelSelection);


        UploadButton uploadButton = new UploadButton("uploadFile", internalUploadModel, getModel());
        form.add(uploadButton);
    }


    public MultiProjectFileUploadComponent maxFiles(int maxFiles) {
        this.maxFiles = maxFiles;
        return this;
    }

    @Override
    protected void convertInput() {
        final Collection<ProjectFileWrapper> modelObject = getModelObject();
        if (modelObject.size() == 0)
            setConvertedInput(null);
        else
            setConvertedInput(modelObject);
    }

    public void setProjectFileType(ProjectFileType projectFileType) {
        fileType = projectFileType;
    }


    private class UploadButton extends IndicatingAjaxButton {

        private final IModel<List<FileUpload>> uploadModel;
        private final IModel<Collection<ProjectFileWrapper>> filesModel;

        public UploadButton(String id, IModel<List<FileUpload>> uploadModel, IModel<Collection<ProjectFileWrapper>> filesModel) {
            super(id);
            this.uploadModel = uploadModel;
            this.filesModel = filesModel;

            add(new TooltipBehavior(new StringResourceModel("uploadFileTooltip", MultiProjectFileUploadComponent.this, null), tooltipConfig));
        }

        @Override
        protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
            super.onSubmit(target, form);

            List<FileUpload> files = uploadModel.getObject();
            if (files == null) {
                MultiProjectFileUploadComponent.this.error(new StringResourceModel("chooseFile", MultiProjectFileUploadComponent.this, null).getString());
                MultiProjectFileUploadComponent.this.invalid();
            } else {
                if (maxFiles > 0 && filesModel.getObject().size() + files.size() > maxFiles) {
                    MultiProjectFileUploadComponent.this.error(new StringResourceModel("tooManyFiles", MultiProjectFileUploadComponent.this, null, maxFiles).getString());
                    MultiProjectFileUploadComponent.this.invalid();
                }
                for (FileUpload file : files) {
                    ProjectFileWrapper fw = new ProjectFileWrapper();
                    fw.setTags(fileType.toString());
                    fw.setName(file.getClientFileName());
                    fw.setContentType(file.getContentType());
                    ProjectFileContent wrapperContent = new ProjectFileContent();
                    wrapperContent.setBytes(file.getBytes());
                    fw.setContent(wrapperContent);
                    filesModel.getObject().add(fw);
                }

            }
            target.add(findParent(DetailedHelpControlGroup.class));
        }
    }

}
