package org.devgateway.eudevfin.ui.common.providers;

import com.vaynberg.wicket.select2.ChoiceProvider;
import com.vaynberg.wicket.select2.Response;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONWriter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author idobre
 * @since 4/7/14
 */
public class YearProvider extends ChoiceProvider<Integer> {
    private static final Logger logger = Logger.getLogger(YearProvider.class);

    List<Integer> possibleYears;

    public YearProvider (List<Integer> possibleYears) {
        if (possibleYears != null) {
            Collections.sort(possibleYears);
        }
        this.possibleYears = possibleYears;
    }

    @Override
    public void query(final String term, final int page, final Response<Integer> response) {
        final List<Integer> ret = new ArrayList<>();
        List<Integer> values;
        if (this.possibleYears != null && this.possibleYears.size() > 0) {
            values = this.possibleYears;
        } else {
            values = new ArrayList<>();
        }

        for (final Integer el : values) {
            if (el.toString().startsWith(term)) {
                ret.add(el);
            }
        }
        response.addAll(ret);
    }

    @Override
    public void toJson(final Integer choice, final JSONWriter writer) throws JSONException {
        writer.key("id").value(choice.toString()).key("text").value(choice.toString());
    }

    @Override
    public Collection<Integer> toChoices(final Collection<String> ids) {
        final List<Integer> ret = new ArrayList<>();
        if (ids != null) {
            for (final String id : ids) {
                try {
                    final Integer parsedInt = Integer.parseInt(id);
                    ret.add(parsedInt);
                } catch (final NumberFormatException e) {
                    logger.error(e.getMessage());
                }
            }
        }
        return ret;
    }
}
