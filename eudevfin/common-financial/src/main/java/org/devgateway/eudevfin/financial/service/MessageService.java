/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.financial.service;

import org.devgateway.eudevfin.auth.common.domain.PersistedUser;
import org.devgateway.eudevfin.common.service.BaseEntityService;
import org.devgateway.eudevfin.financial.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.integration.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * @author Alexandru Artimon
 * @since 16/08/14
 */
@Component
public interface MessageService extends BaseEntityService<Message> {

    Page<Message> findByTo(PersistedUser to, @Header("pageable") Pageable pageable);

    Page<Message> findByToAndReadStatusFalse(PersistedUser to, @Header("pageable") Pageable pageable);
}
