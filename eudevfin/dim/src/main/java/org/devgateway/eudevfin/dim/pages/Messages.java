/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.pages;

import de.agilecoders.wicket.core.markup.html.bootstrap.form.InputBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.auth.common.domain.PersistedUser;
import org.devgateway.eudevfin.auth.common.util.AuthUtils;
import org.devgateway.eudevfin.common.service.PagingHelper;
import org.devgateway.eudevfin.financial.FileWrapper;
import org.devgateway.eudevfin.financial.Message;
import org.devgateway.eudevfin.financial.service.MessageService;
import org.devgateway.eudevfin.ui.common.components.*;
import org.devgateway.eudevfin.ui.common.components.util.ListGeneratorInterface;
import org.devgateway.eudevfin.ui.common.pages.HeaderFooter;
import org.devgateway.eudevfin.ui.common.providers.PersistedUserChoiceProvider;
import org.joda.time.LocalDateTime;
import org.springframework.data.domain.PageRequest;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Alexandru Artimon
 * @since 15/08/14
 */
@MountPath(value = "/messages")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public class Messages extends HeaderFooter {
    @SpringBean
    private MessageService mxService;
    @SpringBean
    private PersistedUserChoiceProvider userChoiceProvider;
    private final Form<Message> form;
    private final TextAreaInputField messageInputField;

    public void reply(PersistedUser from, Collection<PersistedUser> toList, String subject, AjaxRequestTarget target) {
        Message msg = form.getModel().getObject();
        ArrayList<PersistedUser> to = new ArrayList<PersistedUser>();
        to.add(from);
        if (toList.size() > 1) { //we have a message that has been sent to other users than current user
            to.addAll(toList);

            //eliminate current user from list
            Iterator<PersistedUser> it = to.iterator();
            while (it.hasNext()) {
                PersistedUser u = it.next();
                if (u.getId().equals(AuthUtils.getCurrentUser().getId()))
                    it.remove();
            }
        }

        msg.setTo(to);
        msg.setSubject(subject);

        target.add(form);
        target.appendJavaScript("$('body').scrollTop(0); $('#" + messageInputField.getMarkupId() + " textarea').focus();");
    }

    public Messages() {
        super();

        ListGeneratorInterface<Message> listGenerator = new ListGeneratorInterface<Message>() {
            @Override
            public PagingHelper<Message> getResultsList(int pageNumber, int pageSize) {
                return PagingHelper.createPagingHelperFromPage(mxService.findByTo(AuthUtils.getCurrentUser(), new PageRequest(pageNumber - 1, pageSize)));
            }
        };

        final MessagesListTable list = new MessagesListTable("list", listGenerator);
        add(list);

        final Model<Message> messageModel = new Model<>(new Message());

        form = new Form<>("form", messageModel);
        form.setOutputMarkupId(true);
        add(form);

        //DropDownField<PersistedUser> to = new DropDownField<>("to", new PropertyModel<PersistedUser>(messageModel, "to"), userChoiceProvider);
        MultiSelectField<PersistedUser> to = new MultiSelectField<>("to", new PropertyModel<Collection<PersistedUser>>(messageModel, "to"), userChoiceProvider);
        to.required();
        to.setSize(InputBehavior.Size.XXLarge);
        form.add(to);

        TextInputField<String> subject = new TextInputField<>("subject", new PropertyModel<String>(messageModel, "subject"));
        subject.typeString();
        subject.setSize(InputBehavior.Size.XXLarge);
        form.add(subject);

        messageInputField = new TextAreaInputField("message", new PropertyModel<String>(messageModel, "message"));
        messageInputField.setOutputMarkupId(true);
        messageInputField.setSize(InputBehavior.Size.XXLarge);
        messageInputField.maxContentLength(Message.MAX_BODY_SIZE);
        form.add(messageInputField);

        MultiFileUploadField attachments = new MultiFileUploadField("attachments", new PropertyModel<Collection<FileWrapper>>(messageModel, "attachments"));
        form.add(attachments);

        BootstrapSubmitButton send = new BootstrapSubmitButton("send", new StringResourceModel("send.label", this, null, null)) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                final Message message = messageModel.getObject();
                message.setFrom(AuthUtils.getCurrentUser());
                message.setSendDate(LocalDateTime.now());
                mxService.save(message);

                messageModel.setObject(new Message());
                target.add(form);

                list.reloadMessageList(target);
            }
        };
        form.add(send);
    }
}
