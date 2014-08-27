/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.financial.repository;

import org.devgateway.eudevfin.auth.common.domain.PersistedUser;
import org.devgateway.eudevfin.financial.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Alexandru Artimon
 * @since 16/08/14
 */

public interface MessageRepository extends JpaRepository<Message, Long> {

    Page<Message> findByToOrderBySendDateDesc(PersistedUser to, Pageable pageable);

    Page<Message> findByToAndReadStatusFalse(PersistedUser to, Pageable pageable);
}

