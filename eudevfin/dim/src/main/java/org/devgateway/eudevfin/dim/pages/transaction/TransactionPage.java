/*******************************************************************************
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors:
 *    aartimon
 ******************************************************************************/

package org.devgateway.eudevfin.dim.pages.transaction;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.devgateway.eudevfin.dim.core.Constants;
import org.devgateway.eudevfin.dim.core.components.tabs.BootstrapCssTabbedPanel;
import org.devgateway.eudevfin.dim.core.pages.HeaderFooter;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.ArrayList;
import java.util.List;

@MountPath(value = "/transaction")
@AuthorizeInstantiation(Constants.ROLE_USER)
public class TransactionPage extends HeaderFooter {

    public TransactionPage() {

        setModel(new CompoundPropertyModel(new FakeTransaction()));

        Form form = new Form("form");
        add(form);

        List<ITab> tabList = new ArrayList<>();
        tabList.add(IdentificationDataTab.newTab(this));
        tabList.add(BasicDataTab.newTab(this));
        tabList.add(AvailableComponentsTab.newTab(this));

        BootstrapCssTabbedPanel<ITab> bc = new BootstrapCssTabbedPanel<>("bc", tabList).positionTabs(BootstrapCssTabbedPanel.Orientation.RIGHT);
        form.add(bc);

    }

    private class FakeTransaction {
        //
        Integer reportingYear;
        String reportingCountry;
        String extendingAgency;
        Integer crsId;
        Integer donorProjectNumber;
        Integer natureSubmission;
        //
        String recipient;
        String CPA;
        String channelDelivery;
        String channelCode;
        String bilateralMultilateral;
        String typeOfFlow;
        String typeOfFinance;
        String typeOfAid;
        String activityProjectTitle;
        String sectorPurposeCode;



        private Integer getDonorProjectNumber() {
            return donorProjectNumber;
        }

        private void setDonorProjectNumber(Integer donorProjectNumber) {
            this.donorProjectNumber = donorProjectNumber;
        }

        private Integer getNatureSubmission() {
            return natureSubmission;
        }

        private void setNatureSubmission(Integer natureSubmission) {
            this.natureSubmission = natureSubmission;
        }

        private Integer getReportingYear() {
            return reportingYear;
        }

        private void setReportingYear(Integer reportingYear) {
            this.reportingYear = reportingYear;
        }

        private String getReportingCountry() {
            return reportingCountry;
        }

        private void setReportingCountry(String reportingCountry) {
            this.reportingCountry = reportingCountry;
        }

        private String getExtendingAgency() {
            return extendingAgency;
        }

        private void setExtendingAgency(String extendingAgency) {
            this.extendingAgency = extendingAgency;
        }

        private Integer getCrsId() {
            return crsId;
        }

        private void setCrsId(Integer crsId) {
            this.crsId = crsId;
        }
    }
}
