package org.devgateway.eudevfin.projects.module.validator;

import org.apache.wicket.model.Model;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

/**
 * Behavior that checks if a {@link FormComponent} is valid. Valid
 * {@link FormComponent} objects get the CSS class 'formcomponent valid' and
 * invalid {@link FormComponent} objects get the CSS class 'formcomponent
 * invalid'.
 *
 * See {@link AjaxFormComponentUpdatingBehavior} for more details over the
 * parent class.
 *
 * You can use this code under Apache 2.0 license, as long as you retain the
 * copyright messages.
 *
 * Tested with Wicket 1.3.4
 *
 * @author Daan, StuQ.nl
 */
public class WordsValidator implements IValidator<String> {

    private int maxWords;

    /**
     * Constructor.
     *
     * @param wordsCount
     */
    public WordsValidator(int wordsCount) {
        this.maxWords = wordsCount;
    }

    private int wordsCount(String s) {
        int wordCount = 0;
        boolean word = false;
        int endOfLine = s.length() - 1;

        for (int i = 0; i < s.length(); i++) {
            // if the char is a letter, word = true.
            if (Character.isLetter(s.charAt(i)) && i != endOfLine) {
                word = true;
                // if char isn't a letter and there have been letters before,
                // counter goes up.
            } else if (!Character.isLetter(s.charAt(i)) && word) {
                wordCount++;
                word = false;
                // last word of String; if it doesn't end with a non letter, it
                // wouldn't count without this.
            } else if (Character.isLetter(s.charAt(i)) && i == endOfLine) {
                wordCount++;
            }
        }
        return wordCount;
    }

    @Override
    public void validate(IValidatable<String> validatable) {
        String phrase = validatable.getValue();

        if (wordsCount(phrase) > this.maxWords) {
            error(validatable);
        }
    }

    private void error(IValidatable<String> validatable) {
        ValidationError error = new ValidationError();
        error.setMessage("You have exceeded " + maxWords + " words.");
        validatable.error(error);
    }

}
