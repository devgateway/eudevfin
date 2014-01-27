/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dashboard.components;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

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

	private String type;
	private int priority;

    private List<String> listeners;
    private List<String> parameters;

	private Boolean executeAtStart;

    public BaseParameters(String id) {
	    this.name = id;
	    this.htmlObject = id;
	    this.priority = 5;
	    this.listeners = new ArrayList<>();
	    this.parameters = new ArrayList<>();
	    this.executeAtStart = Boolean.TRUE;
    }

	public BaseParameters(String name, String id) {
		this.name = name;
		this.htmlObject = id;
		this.priority = 5;
		this.listeners = new ArrayList<>();
		this.parameters = new ArrayList<>();
		this.executeAtStart = Boolean.TRUE;
	}

    public void addFilter(Filter filter) {
        listeners.add(filter.getParameters().getParameter());
    }

    public String toJson() {
        Gson gson = new Gson();
        String ret = gson.toJson(getInstance());
        return ret;
    }

    protected abstract BaseParameters getInstance();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHtmlObject() {
		return htmlObject;
	}

	public void setHtmlObject(String htmlObject) {
		this.htmlObject = htmlObject;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public List<String> getListeners() {
		return listeners;
	}

	public void setListeners(List<String> listeners) {
		this.listeners = listeners;
	}

	public List<String> getParameters() {
		return parameters;
	}

	public void setParameters(List<String> parameters) {
		this.parameters = parameters;
	}

	public Boolean getExecuteAtStart() {
		return executeAtStart;
	}

	public void setExecuteAtStart(Boolean executeAtStart) {
		this.executeAtStart = executeAtStart;
	}
}
