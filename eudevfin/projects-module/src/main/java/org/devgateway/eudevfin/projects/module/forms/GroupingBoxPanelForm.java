/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.projects.module.forms;

import java.io.Serializable;

public class GroupingBoxPanelForm implements Serializable {

    private static final long serialVersionUID = -4207835378383930432L;
    private String crsIdSearch;
    private String donorIdSearch;
    private Boolean active;

    public void reset() {
        donorIdSearch=null;
        crsIdSearch=null;
        active=false;
    }

    /**
     * @return the crsIdSearch
     */
    public String getCrsIdSearch() {
        return crsIdSearch;
    }

    /**
     * @param crsIdSearch the crsIdSearch to set
     */
    public void setCrsIdSearch(String crsIdSearch) {
        this.crsIdSearch = crsIdSearch;
    }

    /**
     * @return the donorIdSearch
     */
    public String getDonorIdSearch() {
        return donorIdSearch;
    }

    /**
     * @param donorIdSearch the donorIdSearch to set
     */
    public void setDonorIdSearch(String donorIdSearch) {
        this.donorIdSearch = donorIdSearch;
    }

    /**
     * @return the active
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(Boolean active) {
        this.active = active;
    }
}