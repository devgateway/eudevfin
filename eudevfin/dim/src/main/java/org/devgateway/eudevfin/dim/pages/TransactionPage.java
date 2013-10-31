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

package org.devgateway.eudevfin.dim.pages;

import com.vaynberg.wicket.select2.ChoiceProvider;
import com.vaynberg.wicket.select2.Response;
import com.vaynberg.wicket.select2.StringTextChoiceProvider;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.devgateway.eudevfin.dim.core.Constants;
import org.devgateway.eudevfin.dim.core.components.*;
import org.devgateway.eudevfin.dim.core.pages.HeaderFooter;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.ArrayList;
import java.util.Date;

@MountPath(value = "/transaction")
@AuthorizeInstantiation(Constants.ROLE_USER)
public class TransactionPage extends HeaderFooter {

    private static final String[] ddValues = {"Bulgaria", "Romania", "Georgia", "Italia", "Slovacia", "Rusia"};

    public TransactionPage() {
        Form form = new Form("form");
        add(form);

        Model<String> emptyString = Model.of("");
        AbstractInputField input = new TextInputField<String>("input", emptyString, "input").enableRequired();
        form.add(input);

        TextInputField email = new TextInputField<String>("email", emptyString, "email");
        email.enableRequired();
        email.decorateAsEmailField();
        form.add(email);

        DateInputField date = new DateInputField("date", Model.of(new Date()), "date");
        form.add(date);

        ChoiceProvider<String> choiceProvider = new StringTextChoiceProvider() {
            @Override
            public void query(String term, int page, Response<String> response) {
                String upperTerm = term.toUpperCase();
                for (String val: ddValues){
                    if (val.toUpperCase().contains(upperTerm))
                        response.add(val);
                }
            }
        };

        DropDownField dd = new DropDownField<String>("dropdown", Model.of(""), "dropdown", choiceProvider);
        form.add(dd);

        MultiSelectField ms = new MultiSelectField<String>("multi", new Model(new ArrayList<String>()), "multi", choiceProvider);
        form.add(ms);

        CheckBoxField cb = new CheckBoxField("checkbox", Model.of(Boolean.FALSE), "checkbox");
        form.add(cb);

    }
}
