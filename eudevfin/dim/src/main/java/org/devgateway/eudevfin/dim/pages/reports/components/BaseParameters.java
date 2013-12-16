/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.pages.reports.components;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

/**
 * POJO for the base parameters to configure the reports entities
 * This is going to be transformed into a JSON and passed to the JS init function of the entity being built
 *
 * @author aartimon
 * @since 13/12/13
 */
public abstract class BaseParameters {
    private String name;
    private String htmlObject;

    private List<String> listeners;
    private List<String> parameters;

    public BaseParameters(String id) {
        listeners = new ArrayList<>();
        parameters = new ArrayList<>();
        name = id;
        htmlObject = id;
    }

    public void addFilter(Filter f) {
        listeners.add(f.getParameter());
    }

    public String toJson() {
        Gson gson = new Gson();
        String ret = gson.toJson(getInstance());
        return ret;
    }

    protected abstract BaseParameters getInstance();
}
