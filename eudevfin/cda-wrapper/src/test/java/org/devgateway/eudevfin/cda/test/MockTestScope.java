package org.devgateway.eudevfin.cda.test;

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
