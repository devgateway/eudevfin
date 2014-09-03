/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/

/**
 *
 */
package org.devgateway.eudevfin.ui.common.providers;

import org.devgateway.eudevfin.auth.common.domain.PersistedUser;
import org.devgateway.eudevfin.auth.common.service.PersistedUserService;
import org.devgateway.eudevfin.common.service.BaseEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author mihai
 */
@Component
public class PersistedUserChoiceProvider extends
        AbstractTextChoiceProvider<PersistedUser> {

    @Autowired
    protected PersistedUserService persistedUserService;

    private static final long serialVersionUID = -7413659137155284215L;

    public PersistedUserChoiceProvider() {

    }

    public static String userToString(PersistedUser user) {
        String ret = "";
        if (user.getFirstName() != null || user.getLastName() != null || user.getEmail() != null) {
            ret += (user.getFirstName() != null ? user.getFirstName() + " " : "");
            ret += (user.getLastName() != null ? user.getLastName() + " " : "");
            ret += (user.getEmail() != null ? "<" + user.getEmail() + "> " : "");
        } else if (user.getUsername() != null)
            ret = user.getUsername();
        return ret;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.vaynberg.wicket.select2.TextChoiceProvider#getDisplayText(java.lang
     * .Object)
     */
    @Override
    public String getDisplayText(PersistedUser choice) {
        return userToString(choice); //choice.getUsername();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.vaynberg.wicket.select2.TextChoiceProvider#getId(java.lang.Object)
     */
    @Override
    public Object getId(PersistedUser choice) {
        return choice.getId();
    }


    @Override
    protected BaseEntityService<PersistedUser> getService() {
        return persistedUserService;
    }

    @Override
    public void detach() {
        //Spring component no need to detach if added into wicket components with @SpringBean
    }
}
