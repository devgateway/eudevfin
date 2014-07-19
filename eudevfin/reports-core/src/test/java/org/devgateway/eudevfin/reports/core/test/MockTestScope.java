/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.reports.core.test;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ionut Dobre on 2/19/14.
 */
public class MockTestScope implements Scope {
    private final Map<String, Object> beanMap = new HashMap<String, Object>();

    public Object get(String name, ObjectFactory<?> factory) {
        Object bean = beanMap.get(name);
        if (null == bean) {
            bean = factory.getObject();
            beanMap.put(name, bean);
        }

        return bean;
    }

    public String getConversationId() {
        return null;
    }

    public void registerDestructionCallback(String arg0, Runnable arg1) {

    }

    public Object remove(String obj) {
        return beanMap.remove(obj);
    }

    public Object resolveContextualObject(String arg0) {
        return null;
    }
}
