/**
 * *****************************************************************************
 * Copyright (c) 2014 Development Gateway. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the GNU
 * Public License v3.0 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************
 */
package org.devgateway.eudevfin.ui.common.forms;

import java.io.Serializable;

import org.devgateway.eudevfin.metadata.common.domain.Area;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.joda.time.LocalDateTime;

public class SearchBoxPanelForm implements Serializable {
    private static final long serialVersionUID = 7823208228069387955L;
    private LocalDateTime year;
    private String searchString;
    private Category sector;
    private Organization extendingAgency;
    private Area recipient;
    private String formType;

    public void reset() {
        year = null;
        searchString = null;
        sector = null;
        extendingAgency = null;
        formType = null;
        recipient = null;
    }

    /**
     * @return the searchString
     */
    public String getSearchString() {
        return searchString;
    }

    /**
     * @param searchString the searchString to set
     */
    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    /**
     * @return the sector
     */
    public Category getSector() {
        return sector;
    }

    /**
     * @param sector the sector to set
     */
    public void setSector(Category sector) {
        this.sector = sector;
    }

    /**
     * @return the extendingAgency
     */
    public Organization getExtendingAgency() {
        return extendingAgency;
    }

    /**
     * @param extendingAgency the extendingAgency to set
     */
    public void setExtendingAgency(Organization extendingAgency) {
        this.extendingAgency = extendingAgency;
    }

    /**
     * @return the formType
     */
    public String getFormType() {
        return formType;
    }

    /**
     * @param formType the formType to set
     */
    public void setFormType(String formType) {
        this.formType = formType;
    }

    /**
     * @return the recipient
     */
    public Area getRecipient() {
        return recipient;
    }

    /**
     * @param recipient the recipient to set
     */
    public void setRecipient(Area recipient) {
        this.recipient = recipient;
    }

    /**
     * @return the year
     */
    public LocalDateTime getYear() {
        return year;
    }

    /**
     * @param year the year to set
     */
    public void setYear(LocalDateTime year) {
        this.year = year;
    }
}
