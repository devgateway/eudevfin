/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.core;

import org.apache.wicket.Component;

/**
 * @author Alexandru Artimon
 * @since 29/04/14
 */
public class DimComponentInfo {
    private String tabId;
    private String path;
    private Class<? extends Component> clazz;

    public DimComponentInfo(String tabId, String path, Class<? extends Component> clazz) {
        this.tabId = tabId;
        this.path = path;
        this.clazz = clazz;
    }

    public String getTabId() {
        return tabId;
    }

    public void setTabId(String tabId) {
        this.tabId = tabId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Class<? extends Component> getClazz() {
        return clazz;
    }

    public void setClazz(Class<? extends Component> clazz) {
        this.clazz = clazz;
    }
}
