/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

/**
 *
 */
package org.devgateway.eudevfin.ui.common.validators;

import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.devgateway.eudevfin.metadata.common.domain.Category;

import java.util.regex.Pattern;

/**
 * @author mihai
 * @see org.apache.wicket.validation.validator.PatternValidator Matches a
 * {@link Category#getCode()} to a given {@link Pattern}
 */
public class CodePatternCategoryValidator extends Behavior implements IValidator<Category> {

    private static final long serialVersionUID = 7038929517889232434L;

    /**
     * the pattern to match
     */
    private final Pattern pattern;

    /**
     * whether to exclude matching input *
     */
    private boolean reverse = false;

    public CodePatternCategoryValidator(String patternRegex, int flags) {
        this.pattern = Pattern.compile(patternRegex, flags);
    }

    public CodePatternCategoryValidator(String patternRegex) {
        this.pattern = Pattern.compile(patternRegex);
    }

    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public void validate(IValidatable<Category> validatable) {
        // Check value against pattern
        if (pattern.matcher(validatable.getValue().getCode()).matches() == reverse) {
            ValidationError error = new ValidationError(this);
            error.setVariable("pattern", pattern.pattern());
            validatable.error(decorate(error, validatable));
        }
    }

    public CodePatternCategoryValidator reverse() {
        reverse = true;
        return this;
    }

    /**
     * Allows subclasses to decorate reported errors
     *
     * @param error
     * @param validatable
     * @return decorated error
     */
    protected ValidationError decorate(ValidationError error, IValidatable<Category> validatable) {
        return error;
    }

}
