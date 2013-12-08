/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.core.temporary;

import com.vaynberg.wicket.select2.Response;
import com.vaynberg.wicket.select2.TextChoiceProvider;
import org.devgateway.eudevfin.financial.AbstractTranslateable;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author aartimon
 * @since 29/11/13
 */
public abstract class TempCP<T extends AbstractTranslateable> extends TextChoiceProvider<T> {

    private final T[] staticChoices;

    public TempCP(T[] staticChoices) {
        this.staticChoices = staticChoices;
    }

    @Override
    protected Object getId(T choice) {
        return choice.getId();
    }

    @Override
    public void query(String term, int page, Response<T> response) {
        for (T c : staticChoices)
            response.add(c);
    }

    @Override
    public Collection<T> toChoices(Collection<String> ids) {
        ArrayList<T> ret = new ArrayList<>();
        for (String id : ids) {
            Long lid = Long.parseLong(id);
            for (T c : staticChoices)
                if (c.getId().equals(lid))
                    ret.add(c);
        }
        return ret;
    }
}