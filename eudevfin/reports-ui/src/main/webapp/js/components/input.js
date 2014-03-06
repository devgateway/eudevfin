var InputBaseComponent = UnmanagedComponent.extend({
    update: function () {
        var qd = this.queryDefinition;
        if (this.valuesArray && this.valuesArray.length > 0) {
            var handler = _.bind(function () {
                this.draw(this.valuesArray);
            }, this);
            this.synchronous(handler);
        } else if (qd) {
            var handler = _.bind(function (data) {
                var filtered;
                if (this.valueAsId) {
                    filtered = data.resultset.map(function (e) {
                        return [e[0], e[0]];
                    });
                } else {
                    filtered = data.resultset;
                }
                this.draw(filtered);
            }, this);
            this.triggerQuery(qd, handler);
        } else {
            /* Legacy XAction-based components are a wasps' nest, so
             * we'll steer clearfrom updating those for the time being
             */
            var handler = _.bind(function () {
                var data = this.getValuesArray();
                this.draw(data);
            }, this);
            this.synchronous(handler);

        }
    },

    // TODO: is the result of Dashoards.getParameterValue subject or not to HTML encoding?
    // Some controls in this file do html encode the result while others don't.

    /**
     * Obtains the value of this component's parameter.
     * <p>
     * If the parameter value is a function, the result of evaluating it is returned instead.
     * </p>
     * <p>
     * Normalizes return values by using {@link Dashboards.normalizeValue}.
     * </p>
     *
     * @return {*} the parameter value.
     */
    _getParameterValue: function () {
        return Dashboards.normalizeValue(
            Dashboards.ev(
                Dashboards.getParameterValue(this.parameter)));
    }
});


var SelectBaseComponent = InputBaseComponent.extend({
    visible: false,

    //defaultIfEmpty: [false]
    //isMultiple: [true]
    //size: when isMultiple===true, the default value is the number of possible values
    //externalPlugin:
    //extraOptions:
    //changeMode: ['immediate'], 'focus', 'timeout-focus'
    //changeTimeout: [2000], // in milliseconds
    //changeTimeoutScrollFraction: 1,
    //changeTimeoutChangeFraction: 5/8,
    //NOTE: changeMode 'timeout-focus' is not supported in mobile and fallsback to 'focus'

    draw: function (myArray) {
        var ph = $("#" + this.htmlObject);
        var name = this.name;

        // Build the HTML
        var selectHTML = "<select";

        var allowMultiple = this._allowMultipleValues();
        if (allowMultiple) {
            selectHTML += " multiple";
        }

        var placeholderText = this._getPlaceholderText();
        if (placeholderText) {
            selectHTML += " data-placeholder='" + placeholderText + "'";
        }

        var size = this._getListSize(myArray);
        if (size !== null) {
            selectHTML += " size='" + size + "'";
        }

        var extPlugin = this.externalPlugin;
        switch (extPlugin) {
        case "chosen":
            selectHTML += " class='chzn-select'";
            break;
        case "hynds":
            selectHTML += " class='hynds-select'";
            break;
        }

        selectHTML += ">";

        // ------

        var currentVal = this._getParameterValue();
        var currentVals = Dashboards.parseMultipleValues(currentVal); // may be null
        var valuesIndex = {};
        var firstVal;

        Dashboards.eachValuesArray(myArray, {
                valueAsId: this.valueAsId
            },
            function (value, label, id, index) {
                selectHTML += "<option value = '" + Dashboards.escapeHtml(value) + "' >" +
                    Dashboards.escapeHtml(label) +
                    "</option>";

                // For value validation, below
                if (!index) {
                    firstVal = value;
                }
                valuesIndex[value] = true;
            },
            this);

        selectHTML += "</select>";
        ph.html(selectHTML);

        // ------

        // All current values valid?
        var currentIsValid = true;

        // Filter out invalid current values
        if (currentVals !== null) {
            var i = currentVals.length;
            while (i--) {
                if (valuesIndex[currentVals[i]] !== true) {
                    // At least one invalid value
                    currentIsValid = false;
                    currentVals.splice(i, 1);
                }
            }
            if (!currentVals.length) {
                currentVals = null;
            }
        }

        /* If the current value for the parameter is invalid or empty,
         * we need to pick a sensible default.
         * If defaultIfEmpty is true, the first possible value is selected,
         * otherwise, nothing is selected.
         */
        var isEmpty = currentVals === null;
        var hasChanged = !currentIsValid;
        if (isEmpty && this.defaultIfEmpty && firstVal !== null) {
            // Won't remain empty
            currentVals = [firstVal];
            hasChanged = true;
        }


        // jQuery only cleans the value if it receives an empty array. 
        $("select", ph).val((currentVals === null && []) || currentVals);

        if (hasChanged) {
            // TODO: couldn't we just call fireChange(this.parameter, currentVals) ?
            Dashboards.setParameter(this.parameter, currentVals);
            Dashboards.processChange(name);
        }

        // TODO: shouldn't this be called right after setting the value of select?
        // Before hasChanged firing?
        switch (extPlugin) {
        case "chosen":
            ph.find("select.chzn-select").chosen(this._readExtraOptions());
            break;
        case "hynds":
            ph.find("select.hynds-select").multiselect({
                multiple: allowMultiple
            });
            break;
        }

        this._listenElement(ph);
    },

    /**
     * Indicates if the user can select multiple values.
     * The default implementation returns <tt>false</tt>.
     * @return {boolean}
     * @protected
     */
    _allowMultipleValues: function () {
        return false;
    },

    /**
     * Returns the placeholder label for empty values, or false if it is an non-empty String.
     * @protected
     */
    _getPlaceholderText: function () {
        var txt = this.placeholderText;
        return (_.isString(txt) && !_.isEmpty(txt) && txt) || false;
    },

    /**
     * The number of elements that the list should show
     * without scrolling.
     * The default implementation
     * returns the value of the {@link #size} property.
     *
     * @param {Array.<Array.<*>>} values the values array.
     * @return {?number}
     * @protected
     */
    _getListSize: function (values) {
        return this.size;
    },

    /**
     * Currently, reads extra options for the "chosen" plugin,
     * by transforming the array of key/value pair arrays
     * in {@link #extraOptions} into a JS object.
     *
     * @return {!Object.<string,*>} an options object.
     */
    _readExtraOptions: function () {
        if (this.externalPlugin && this.extraOptions) {
            return Dashboards.propertiesArrayToObject(this.extraOptions);
        }
    },

    /**
     * Installs listeners in the HTML element/object.
     * <p>
     *    The default implementation listens to the change event
     *    and dashboard-processes each change.
     * </p>
     * @param {!HTMLElement} elem the element.
     */
    _listenElement: function (elem) {
        var me = this;
        var prevValue = me.getValue();
        var stop;
        var check = function () {

            stop && stop();

            var currValue = me.getValue();
            if (!Dashboards.equalValues(prevValue, currValue)) {
                prevValue = currValue;
                Dashboards.processChange(me.name);
            }
        };

        var selElem = $("select", elem);

        selElem
            .keypress(function (ev) {
                if (ev.which === 13) {
                    check();
                }
            });

        var changeMode = this._getChangeMode();
        if (changeMode !== 'timeout-focus') {
            selElem
                .on(me._changeTrigger(), check);
        } else {

            var timScrollFraction = me.changeTimeoutScrollFraction;
            timScrollFraction = Math.max(0, timScrollFraction !== null ? timScrollFraction : 1);

            var timChangeFraction = me.changeTimeoutChangeFraction;
            timChangeFraction = Math.max(0, timChangeFraction !== null ? timChangeFraction : 5 / 8);

            var changeTimeout = Math.max(100, me.changeTimeout || 2000);
            var changeTimeoutScroll = timScrollFraction * changeTimeout;
            var changeTimeoutChange = timChangeFraction * changeTimeout;

            var timeoutHandle;

            stop = function () {
                if (timeoutHandle !== null) {
                    clearTimeout(timeoutHandle);
                    timeoutHandle = null;
                }
            };

            var renew = function (tim) {
                stop();
                timeoutHandle = setTimeout(check, tim || changeTimeout);
            };

            selElem
                .change(function () {
                    renew(changeTimeoutChange);
                })
                .scroll(function () {
                    renew(changeTimeoutScroll);
                })
                .focusout(check);
        }
    },

    /**
     * Obtains the change mode to use.
     *
     * <p>
     * The default implementation normalizes, validates and defaults
     * the change mode value.
     * </p>
     *
     * @return {!string} one of values:
     * <tt>'immediate'</tt>,
     * <tt>'focus'</tt> or
     * <tt>'timeout-focus'</tt>.
     */
    _getChangeMode: function () {
        var changeMode = this.changeMode;
        if (changeMode) {
            changeMode = changeMode.toLowerCase();
            switch (changeMode) {
            case 'immediate':
            case 'focus':
                return changeMode;

            case 'timeout-focus':
                // Mobiles do not support this strategy. Downgrade to 'focus'.
                if ((/android|ipad|iphone/i).test(navigator.userAgent)) {
                    return 'focus';
                }
                return changeMode;

            default:
                Dashboards.log("Invalid 'changeMode' value: '" + changeMode + "'.", 'warn');
            }
        }
        return 'immediate';
    },

    /**
     * Obtains an appropriate jQuery event name
     * for when testing for changes is done.
     *
     * @return {!string} the name of the event.
     */
    _changeTrigger: function () {
        /**
         * <p>
         * Mobile user agents show a dialog/popup for choosing amongst possible values,
         * for both single and multi-selection selects.
         * </p>
         * <ul>
         *   <li>iPad/iPhone -
         *       the popup shows a button "OK" only when in multiple selection.
         *       As the user clicks on the items, "change" events are fired.
         *       A "focusout" event is fired when the user dismisses the popup
         *       (by clicking on the button or outside of the popup).
         *   </li>
         *   <li>Android -
         *       the popup shows a button "Done" whether in single or multiple selection.
         *       As the user clicks on the items no events are fired.
         *       A change event is fired (whether or not values actually changed),
         *       when the user dismisses the popup.
         *   </li>
         *   <li>Desktops -
         *       no popup is shown.
         *       As the user clicks on the items, "change" events are fired.
         *       A "focusout" event is fired when it should...
         *   </li>
         * </ul>
         *
         * | Change mode: | Immediate  | Focus    | Timeout-Focus |
         * +--------------+------------+----------+---------------+
         * | Desktop      | change     | focusout | focusout      |
         * | iPad         | change     | focusout | -             |
         * | Android      | change *   | change   | -             |
         *
         * (*) this is the most immediate that android can do
         *     resulting in Immediate = Focus
         *
         *  On mobile devices the Done/OK is equiparated with the
         *  behavior of focus out and of the ENTER key.
         */
        if (this._getChangeMode() === 'immediate') {
            return 'change';
        }
        return (/android/i).test(navigator.userAgent) ? 'change' : 'focusout';
    }
});

var SelectComponent = SelectBaseComponent.extend({
    defaultIfEmpty: true,
    getValue: function () {
        return $("#" + this.htmlObject + " select").val();
    }
});
