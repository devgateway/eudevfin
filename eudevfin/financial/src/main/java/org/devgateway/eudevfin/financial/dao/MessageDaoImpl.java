/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.financial.dao;

import org.devgateway.eudevfin.auth.common.domain.PersistedUser;
import org.devgateway.eudevfin.common.dao.AbstractDaoImpl;
import org.devgateway.eudevfin.common.spring.integration.NullableWrapper;
import org.devgateway.eudevfin.financial.Message;
import org.devgateway.eudevfin.financial.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.integration.annotation.Header;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Alexandru Artimon
 * @since 16/08/14
 */
@Component
@Lazy(value = false)
public class MessageDaoImpl extends AbstractDaoImpl<Message, Long, MessageRepository> {
    @Autowired
    private MessageRepository repo;

    @Override
    protected MessageRepository getRepo() {
        return repo;
    }

    @Override
    @ServiceActivator(inputChannel = "findAllAsListMessageChannel")
    public List<Message> findAllAsList() {
        return super.findAllAsList();
    }

    @Override
    @ServiceActivator(inputChannel = "saveMessageChannel")
    public NullableWrapper<Message> save(Message e) {
        return super.save(e);
    }

    @Override
    public Iterable<Message> findAll() {
        return super.findAll();
    }

    @Override
    @ServiceActivator(inputChannel = "deleteMessageChannel")
    public void delete(Message e) {
        super.delete(e);
    }

    @Override
    @ServiceActivator(inputChannel = "findMessageByIdChannel")
    public NullableWrapper<Message> findOne(Long id) {
        return super.findOne(id);
    }

    @ServiceActivator(inputChannel = "findMessageByToPageableChannel")
    public Page<Message> findByTo(PersistedUser to, @Header("pageable") Pageable pageable) {
        return repo.findByToOrderBySendDateDesc(to, pageable);
    }

    @ServiceActivator(inputChannel = "findMessageByToAndReadStatusFalsePageableChannel")
    public Page<Message> findByToAndReadStatusFalse(PersistedUser to, @Header("pageable") Pageable pageable) {
        return repo.findByToAndReadStatusFalse(to, pageable);
    }

    @ServiceActivator(inputChannel = "findMessageByGeneralSearchPageableChannel")
    public Page<Message> findByGeneralSearch(String searchString, @Header("pageable") Pageable pageable) {
        return repo.findAll(pageable);
    }
}
