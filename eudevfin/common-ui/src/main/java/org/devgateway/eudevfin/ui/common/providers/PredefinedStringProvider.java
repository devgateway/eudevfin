/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.ui.common.providers;

import com.vaynberg.wicket.select2.ChoiceProvider;
import com.vaynberg.wicket.select2.Response;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONWriter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author idobre
 * @since 4/8/14
 */
public class PredefinedStringProvider extends ChoiceProvider<String> {
    private static final Logger logger = Logger.getLogger(PredefinedStringProvider.class);

    List<String> options;

    public PredefinedStringProvider (List<String> options) {
        this.options = options;
    }

    @Override
    public void query(String term, int page, Response<String> response) {
        final List<String> ret = new ArrayList<>();
        List<String> values;
        if (this.options != null && this.options.size() > 0) {
            values = this.options;
        } else {
            values = new ArrayList<>();
        }

        for (final String el : values) {
            if (el.toLowerCase().contains(term.toLowerCase())) {
                ret.add(el);
            }
        }
        response.addAll(ret);
    }

    @Override
    public void toJson(String choice, JSONWriter writer) throws JSONException {
        writer.key("id").value(choice.toString()).key("text").value(choice.toString());
    }

    @Override
    public Collection<String> toChoices(Collection<String> ids) {
        final List<String> ret = new ArrayList<>();
        if (ids != null) {
            for (final String id : ids) {
                try {
                    ret.add(id);
                } catch (final NumberFormatException e) {
                    logger.error(e.getMessage());
                }
            }
        }
        return ret;
    }
}
