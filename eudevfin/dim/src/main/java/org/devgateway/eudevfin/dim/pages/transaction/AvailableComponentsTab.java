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

import com.vaynberg.wicket.select2.ChoiceProvider;
import com.vaynberg.wicket.select2.Response;
import com.vaynberg.wicket.select2.StringTextChoiceProvider;
import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.devgateway.eudevfin.dim.core.components.*;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author aartimon@developmentgateway.org
 * @since 01 November 2013
 */
public class AvailableComponentsTab extends Panel {

    //TODO: Remove
    private static final String[] ddValues = {"Bulgaria", "Romania", "Georgia", "Italia", "Slovacia", "Rusia"};


    private AvailableComponentsTab(String id) {
        super(id);

        Model<String> emptyString = Model.of("");
        TextInputField input = new TextInputField<String>("input", emptyString, "input").required();
        input.decorateMask("999-99-9999");
        add(input);

        TextInputField email = new TextInputField<String>("email", emptyString, "email");
        email.required();
        email.decorateAsEmailField();
        add(email);

        DateInputField date = new DateInputField("date", Model.of(new Date()), "date");
        add(date);

        ChoiceProvider<String> choiceProvider = new StringTextChoiceProvider() {
            @Override
            public void query(String term, int page, Response<String> response) {
                String upperTerm = term.toUpperCase();
                for (String val : ddValues) {
                    if (val.toUpperCase().contains(upperTerm))
                        response.add(val);
                }
            }
        };

        DropDownField dd = new DropDownField<String>("dropdown", Model.of(""), "dropdown", choiceProvider).disableSearch();

        add(dd);

        MultiSelectField ms = new MultiSelectField<String>("multi", new Model(new ArrayList<String>()), "multi", choiceProvider);
        add(ms);

        CheckBoxField cb = new CheckBoxField("checkbox", Model.of(Boolean.FALSE), "checkbox");
        add(cb);


    }

    public static ITab newTab(Component askingComponent) {
        return new AbstractTab(new StringResourceModel("tabs.available", askingComponent, null)) {
            private static final long serialVersionUID = -724508987522388955L;

            @Override
            public WebMarkupContainer getPanel(String panelId) {
                return new AvailableComponentsTab(panelId);
            }
        };
    }

}
