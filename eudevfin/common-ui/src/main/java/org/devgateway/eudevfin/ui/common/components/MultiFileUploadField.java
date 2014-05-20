/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.ui.common.components;

import org.apache.wicket.model.IModel;
import org.devgateway.eudevfin.financial.FileWrapper;

import java.util.Collection;

/**
 * Input Field that uses a {@link MultiFileUploadFormComponent} to handle file uploads
 *
 * @author Alexandru Artimon
 * @see org.devgateway.eudevfin.ui.common.components.MultiFileUploadFormComponent
 * @since 25/02/14
 */
public class MultiFileUploadField extends AbstractInputField<Collection<FileWrapper>, MultiFileUploadFormComponent> {

    public MultiFileUploadField(String id, IModel<Collection<FileWrapper>> model) {
        super(id, model);
    }

    public MultiFileUploadField maxFiles(int maxFiles) {
        field.maxFiles(maxFiles);
        return this;
    }

    @Override
    protected MultiFileUploadFormComponent newField(String id, IModel<Collection<FileWrapper>> model) {
        return new MultiFileUploadFormComponent(id, model);
    }
}
