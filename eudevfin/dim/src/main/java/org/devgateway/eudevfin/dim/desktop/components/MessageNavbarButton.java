/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.desktop.components;

import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarButton;
import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.eudevfin.auth.common.domain.PersistedUser;
import org.devgateway.eudevfin.auth.common.util.AuthUtils;
import org.devgateway.eudevfin.dim.pages.Messages;
import org.devgateway.eudevfin.financial.Message;
import org.devgateway.eudevfin.financial.service.MessageService;
import org.springframework.data.domain.PageRequest;

/**
 * @author Alexandru Artimon
 * @since 17/08/14
 */
public class MessageNavbarButton extends NavbarButton<Messages> {
    @SpringBean
    private MessageService mxService;

    public MessageNavbarButton(Class<Messages> pageClass, IModel<String> label) {
        super(pageClass, label);
        this.setOutputMarkupId(true);
    }

    @Override
    protected Component newLabel(String markupId) {
        final Component label = super.newLabel(markupId);
        label.setEscapeModelStrings(false);
        return label;
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();

        //final StringResourceModel messagesLabel = new StringResourceModel("navbar.messages", this.getPage(), null, null);
        PersistedUser currentUser = AuthUtils.getCurrentUser();
        long numberOfMessages = 0;
        if (currentUser != null) {
            org.springframework.data.domain.Page<Message> messages = mxService.findByToAndReadStatusFalse(currentUser, new PageRequest(0, 1));
            numberOfMessages = messages.getTotalElements();
        }

        String labelText = "";
        if (numberOfMessages > 0)
            labelText = "<span class=\"messageBadge\">" + String.valueOf(numberOfMessages) + "</span>";
        Model<String> label = Model.of(labelText);
        this.setLabel(label);
    }


}
