// TODO - do we need this and where is used?
$.ajaxSetup({
    type: "POST",
    async: false,
    traditional: true,
    scriptCharset: "utf-8",
    contentType: "application/x-www-form-urlencoded;charset=UTF-8",

    dataFilter: function (data, dtype) {
        // just tagging date

        Dashboards.lastServerResponse = Date.now();
        return data;
    }
});


// TODO - maybe we should wrap all this global variables in some 'app' object or maibe 'cdfplugin' object
var pathArray = window.location.pathname.split('/');
var webAppPath;
if (typeof (CONTEXT_PATH) !== 'undefined') {
    webAppPath = CONTEXT_PATH;
}
if (webAppPath === undefined) {
    webAppPath = "/" + pathArray[1];
}

if (webAppPath.endsWith("/")) {
    webAppPath = webAppPath.substr(0, webAppPath.length - 1);
}


// TODO - change the css and images path
var GB_ANIMATION = true;
var CDF_CHILDREN = 1;
var CDF_SELF = 2;
var ERROR_IMAGE = webAppPath + "/content/pentaho-cdf/resources/style/images/error.png";
var CDF_ERROR_DIV = 'cdfErrorDiv';


// Removed 'blockUI' and added CSS loader
// TODO - check all other references to 'blockUI'


// TODO - remove all commented blocks
// if (typeof $.SetImpromptuDefaults === 'function')
//     $.SetImpromptuDefaults({
//         prefix: 'colsJqi',
//         show: 'slideDown'
//     });

var Dashboards = {
    ERROR_CODES: {
        'QUERY_TIMEOUT': {
            msg: "Query timeout reached"
        },

        "COMPONENT_ERROR": {
            msg: "Error processing component"
        }
    },

    CDF_BASE_PATH: webAppPath + "/content/pentaho-cdf/",

    // TRAFFIC_RED: webAppPath + "/content/pentaho-cdf/resources/style/images/traffic_red.png",
    // TRAFFIC_YELLOW: webAppPath + "/content/pentaho-cdf/resources/style/images/traffic_yellow.png",
    // TRAFFIC_GREEN: webAppPath + "/content/pentaho-cdf/resources/style/images/traffic_green.png",

    viewFlags: {
        UNUSED: "unused",
        UNBOUND: "unbound",
        VIEW: "view"
    },

    parameterModel: new Backbone.Model(),

    /* globalContext determines if components and params are retrieved
     * from the current window's object or from the Dashboards singleton
     */

    globalContext: true,
    escapeParameterValues: true,

    /* Used to control progress indicator for async mode */
    runningCalls: 0,

    components: [],

    /* Holds the dashboard parameters if globalContext = false */
    parameters: [],

    // Holder for context
    context: {},


    /*
     * Legacy dashboards don't have priority, so we'll assign a very low priority to them.
     */
    // TODO - since all Dashboards should be 'new asynch' - check if we need this param: legacyPriority
    legacyPriority: -1000,

    /* Log lifecycle events? */
    logLifecycle: true,

    args: [],
    monthNames: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],

    lastServerResponse: Date.now(),
    serverCheckResponseTimeout: 1800000, //ms, will be overridden at init

    /* Reference to current language code . Used in every place where jquery
     * plugins used in CDF has native internationalization support (ex: Datepicker)
     */
    i18nCurrentLanguageCode: null,
    i18nSupport: null, // Reference to i18n objects

    loadingAnimationTimeout: undefined
};

_.extend(Dashboards, Backbone.Events);

// Log
Dashboards.log = function (m, type) {
    if (typeof console !== "undefined") {
        if (type && console[type]) {
            console[type]("CDF: " + m);
        } else if (type === 'exception' && !console.exception) {
            console.error(m.stack);
        } else {
            console.log("CDF: " + m);
        }
    }
};

Dashboards.error = function (m) {
    this.log(m, 'error');
}

Dashboards.getWebAppPath = function () {
    return webAppPath;
}

// REFRESH ENGINE begin

// Manages periodic refresh of components
Dashboards.RefreshEngine = function () {
    var NO_REFRESH = 0; //currently no distinction between explicitly disabled or not set
    var refreshQueue = []; //component refresh queue
    var activeTimer = null; //timer for individual component refresh

    var globalRefreshPeriod = NO_REFRESH;
    var globalTimer = null;

    Dashboards.RefreshEngine.QueueItem = function () {
        return {
            nextRefresh: 0,
            component: null
        };
    };

    // set global refresh and (re)start interval
    var startGlobalRefresh = function (refreshPeriod) {
        if (globalTimer !== null) {
            clearInterval(globalTimer);
            globalTimer = null;
        }

        globalRefreshPeriod = (refreshPeriod > 0) ? refreshPeriod : NO_REFRESH;

        if (globalRefreshPeriod !== NO_REFRESH) {
            globalTimer = setInterval("Dashboards.refreshEngine.fireGlobalRefresh()", globalRefreshPeriod * 1000);
        }
    };

    var clearFromQueue = function (component) {
        for (var i = 0; i < refreshQueue.length; i++) {
            if (refreshQueue[i].component === component) {
                refreshQueue.splice(i, 1);
                i--;
            }
        }
    };

    var clearQueue = function () {
        if (refreshQueue.length > 0) {
            refreshQueue.splice(0, refreshQueue.length);
        }
    };

    // binary search for elem's position in coll (nextRefresh asc order)
    var getSortedInsertPosition = function (coll, elem) {
        var high = coll.length - 1;
        var low = 0;
        var mid;

        while (low <= high) {
            mid = parseInt((low + high) / 2)
            if (coll[mid].nextRefresh > elem.nextRefresh) {
                high = mid - 1;
            } else if (coll[mid].nextRefresh < elem.nextRefresh) {
                low = mid + 1;
            } else { //===
                return mid;
            }
        }

        return low;
    };

    var sortedInsert = function (rtArray, rtInfo) {
        var pos = getSortedInsertPosition(rtArray, rtInfo);
        rtArray.splice(pos, 0, rtInfo);
    };

    var stopTimer = function () {
        if (activeTimer !== null) {
            clearTimeout(activeTimer);
            activeTimer = null;
        }
    };

    var restartTimer = function () {
        stopTimer();
        Dashboards.refreshEngine.fireRefresh();
    };

    var getCurrentTime = function () {
        var date = new Date();
        return date.getTime();
    };

    var isFirstInQueue = function (component) {
        return refreshQueue.length > 0 && refreshQueue[0].component === component;
    };

    var refreshComponent = function (component) {
        Dashboards.update(component);
    };

    var insertInQueue = function (component) {
        var time = getCurrentTime();

        // normalize invalid refresh
        if (component.refreshPeriod < 0) {
            component.refreshPeriod = NO_REFRESH;
        }

        if (component.refreshPeriod !== NO_REFRESH) {
            //get next refresh time for component
            var info = new Dashboards.RefreshEngine.QueueItem();
            info.nextRefresh = time + (component.refreshPeriod * 1000);
            info.component = component;
            sortedInsert(refreshQueue, info);
        }
    };

    return {
        // set a component's refresh period and clears it from the queue if there;
        // processComponent must be called to activate the refresh timer for the component
        registerComponent: function (component, refreshPeriod) {
            if (!component) return false;

            component.refreshPeriod = (refreshPeriod > 0) ? refreshPeriod : NO_REFRESH;

            var wasFirst = isFirstInQueue(component);
            clearFromQueue(component);

            if (wasFirst) {
                restartTimer();
            }

            return true;
        },

        getRefreshPeriod: function (component) {
            if (component && component.refreshPeriod > 0) {
                return component.refreshPeriod;
            } else {
                return NO_REFRESH;
            }
        },

        //sets next refresh for given component and inserts it in refreshQueue, restarts timer if needed
        processComponent: function (component) {
            clearFromQueue(component);
            insertInQueue(component);

            if (isFirstInQueue(component)) {
                restartTimer();
            }

            return true;
        },

        // clears queue, sets next refresh for all components, restarts timer
        processComponents: function () {
            clearQueue();
            for (var i = 0; i < Dashboards.components.length; i++) {
                insertInQueue(Dashboards.components[i]);
            }

            restartTimer();
            return true;
        },

        // pop due items from queue, refresh components and set next timeout
        fireRefresh: function () {
            activeTimer = null;
            var currentTime = getCurrentTime();

            while (refreshQueue.length > 0 &&
                refreshQueue[0].nextRefresh <= currentTime) {
                var info = refreshQueue.shift(); //pop first
                // call update, which calls processComponent
                refreshComponent(info.component);
            }
            if (refreshQueue.length > 0) {
                activeTimer = setTimeout("Dashboards.refreshEngine.fireRefresh()", refreshQueue[0].nextRefresh - currentTime);
            }
        },

        // called when a valid globalRefreshPeriod exists
        // updates all components without their own refresh period
        fireGlobalRefresh: function () {
            for (i = 0; i < Dashboards.components.length; i++) {
                var comp = Dashboards.components[i];
                if (!(comp.refreshPeriod > 0) //only update those without refresh
                    && comp.type !== "select") { //and that are not pov widgets
                    refreshComponent(comp);
                }
            }
        },

        setGlobalRefresh: function (refreshPeriod) {
            startGlobalRefresh(refreshPeriod);
        },

        getQueue: function () {
            return refreshQueue;
        }
    };
};

Dashboards.refreshEngine = new Dashboards.RefreshEngine();
// REFRESH ENGINE end

Dashboards.setGlobalContext = function (globalContext) {
    this.globalContext = globalContext;
};

Dashboards.setLogLifecycle = function (logLifecycle) {
    this.logLifecycle = logLifecycle;
};

Dashboards.showProgressIndicator = function () {
    this.blockUIwithDrag();
};

Dashboards.hideProgressIndicator = function () {
    if (Dashboards.loadingAnimationTimeout !== undefined) {
        clearTimeout(Dashboards.loadingAnimationTimeout);
    }

    setTimeout(function () {
        $('.loading-bar').removeClass('is-active');
    }, 100);

    this.showErrorTooltip();
};

Dashboards.resetRunningCalls = function () {
    this.runningCalls = 0;
    setTimeout(_.bind(function () {
        this.hideProgressIndicator();
    }, this), 10);
};

Dashboards.getRunningCalls = function () {
    return this.runningCalls;
};

Dashboards.incrementRunningCalls = function () {
    this.runningCalls++;
    this.showProgressIndicator();
};

Dashboards.decrementRunningCalls = function () {
    this.runningCalls--;
    setTimeout(_.bind(function () {
        if (this.runningCalls <= 0) {
            this.hideProgressIndicator();
            this.runningCalls = 0; // Just in case
        }
    }, this), 10);
};

Dashboards.bindControl = function (control) {
    var Class = this._getControlClass(control);
    if (!Class) {
        this.log("Object type " + control["type"] + " can't be mapped to a valid class", "error");
    } else {
        this._castControlToClass(control, Class);
    }

    this.bindExistingControl(control, Class);
};

Dashboards.bindExistingControl = function (control, Class) {
    if (!control.dashboard) {
        control.dashboard = this;

        // Ensure BaseComponent's methods
        this._castControlToComponent(control, Class);

        // Make sure we clean all events in the case we're redefining the control.
        if (typeof control.off === "function") {
            control.off("all");
        }

        // Endow it with the Backbone event system.
        $.extend(control, Backbone.Events);

        // Add logging lifeCycle
        this._addLogLifecycleToControl(control);

        // For legacy dashboards, we'll automatically assign some priority for component execution.
        if (control.priority === null || control.priority === "") {
            control.priority = this.legacyPriority++;
        }
    }

    return control;
};

Dashboards._castControlToClass = function (control, Class) {
    if (!(control instanceof Class)) {
        var controlImpl = this._makeInstance(Class);

        // Copy implementation into control
        $.extend(control, controlImpl);
    }
};

Dashboards._getControlClass = function (control) {
    // see if there is a class defined for this control
    var typeName = control.type;
    if (typeof typeName === 'function') {
        typeName = typeName.call(control);
    } // <=> control.type() ; the _this_ in the call is _control_

    var TypeName = typeName.substring(0, 1).toUpperCase() + typeName.substring(1);

    // try _TypeComponent_, _type_ and _Type_ as class names
    var typeNames = [TypeName + 'Component', typeName, TypeName];

    for (var i = 0, N = typeNames.length; i < N; i++) {
        // If the value of a name is not a function, keep on trying.
        var Class = window[typeNames[i]];
        if (Class && typeof Class === 'function') {
            return Class;
        }
    }
};

Dashboards._makeInstance = function (Class, args) {
    var o = Object.create(Class.prototype);
    if (args) {
        Class.apply(o, args);
    } else {
        Class.apply(o);
    }
    return o;
};

Dashboards._castControlToComponent = function (control, Class) {
    // Extend control with BaseComponent methods, if it's not an instance of it.
    // Also, avoid extending if _Class_ was already applied
    // and it is a subclass of BaseComponent.
    if (!(control instanceof BaseComponent) &&
        (!Class || !(Class.prototype instanceof BaseComponent))) {

        var baseProto = BaseComponent.prototype;
        for (var p in baseProto) {
            if (baseProto.hasOwnProperty(p) &&
                (control[p] === undefined) &&
                (typeof baseProto[p] === 'function')) {
                switch (p) {
                    // Exceptions
                case 'base':
                    break;

                    // Copy
                default:
                    control[p] = baseProto[p];
                    break;
                }
            }
        }
    }
};

Dashboards._addLogLifecycleToControl = function (control) {
    // Add logging lifeCycle
    control.on("all", function (e) {
        var dashs = this.dashboard;
        if (dashs && dashs.logLifecycle && e !== "cdf" && this.name !== "PostInitMarker" && typeof console !== "undefined") {
            var eventStr;
            var eventName = e.substr(4);
            switch (eventName) {
            case "preExecution":
                eventStr = ">Start";
                break;
            case "postExecution":
                eventStr = "<End  ";
                break;
            case "error":
                eventStr = "!Error";
                break;
            default:
                eventStr = "      ";
                break;
            }

            var timeInfo = _.template("Timing: {{elapsedSinceStartDesc}} since start, {{elapsedSinceStartDesc}} since last event", {
                elapsedSinceStartDesc: this.splitTimer()
            });
            console.log("%c          [Lifecycle " + eventStr + "] " + this.name + " [" + this.type + "]" + " (P: " + this.priority + " ): " +
                e.substr(4) + " " + timeInfo + " (Running: " + this.dashboard.runningCalls + ")", "color: " + this.getLogColor());
        }
    });
};

Dashboards.getErrorObj = function (errorCode) {
    return Dashboards.ERROR_CODES[errorCode] || {};
};

Dashboards.parseServerError = function (resp, txtStatus, error) {
    var out = {};
    var regexs = [{
        match: /Query timeout/,
        msg: Dashboards.getErrorObj('QUERY_TIMEOUT').msg
    }];

    out.error = error;
    out.msg = Dashboards.getErrorObj('COMPONENT_ERROR').msg;
    var str = $('<div/>').html(resp.responseText).find('h1').text();
    _.find(regexs, function (el) {
        if (str.match(el.match)) {
            out.msg = el.msg;
            return true;
        } else {
            return false;
        }
    });
    out.errorStatus = txtStatus;

    return out
};

Dashboards.handleServerError = function () {
    var err = Dashboards.parseServerError.apply(this, arguments);

    Dashboards.errorNotification(err);
    Dashboards.trigger('cdf cdf:serverError', this);
    Dashboards.resetRunningCalls();
};

Dashboards.errorNotification = function (err, ph) {
    if (ph) {
        wd.cdf.notifications.component.render(
            $(ph), {
                title: err.msg,
                desc: ""
            });
    } else {
        wd.cdf.notifications.growl.render({
            title: err.msg,
            desc: ''
        });
    }
};

/**
 * Default impl when not logged in
 */
Dashboards.loginAlert = function (newOpts) {
    var opts = {
        header: "Warning",
        desc: "You are no longer logged in or the connection to the server timed out",
        button: "Click to reload this page",
        callback: function () {
            window.location.reload(true);
        }
    };
    opts = _.extend({}, opts, newOpts);

    wd.cdf.popups.okPopup.show(opts);
    this.trigger('cdf cdf:loginError', this);
};

Dashboards.checkServer = function () {
    // check if is connecting to server ok - use post to avoid cache
    var retVal = false;
    $.ajax({
        type: 'POST',
        async: false,
        dataType: 'json',
        url: Dashboards.CDF_BASE_PATH + 'ping',
        success: function (result) {
            if (result && result.ping === 'ok') {
                retVal = true;
            } else {
                retVal = false;
            }
        },
        error: function () {
            retVal = false;
        }

    });
    return retVal;
};


Dashboards.restoreDuplicates = function () {
    /*
     * We mark duplicates by appending an _nn suffix to their names.
     */
    var dupes = this.components.filter(function (c) {
        return c.type === 'duplicate'
    }),
        suffixes = {},
        params = {};
    /*
     *
     * The suffixes object then maps those suffixes to a mapping of
     * the root parameter names to their respective values.
     * E.g. a parameter 'foo_1 = 1' yields '{_1: {foo: 1}}'
     */
    Object.keys(params).filter(function (e) {
        return /(_[0-9]+)+$/.test(e);
    }).map(function (e) {
        var parts = e.match(/(.*?)((_[0-9]+)+)$/),
            name = parts[1],
            suffix = parts[2];
        if (!suffixes[suffix]) {
            suffixes[suffix] = {}
        }
        suffixes[suffix][name] = params[e];
        return e;
    });


    /*
     * Once we have the suffix list, we'll check each suffix's
     * parameter list against each of the DuplicateComponents
     * in the dashboard.
     */
    var myself = this;
    for (var s in suffixes)
        if (suffixes.hasOwnProperty(s)) {
            var params = suffixes[s];
            $.each(dupes, function (i, e) {
                var p;
                for (p = 0; p < e.parameters.length; p++) {
                    if (!params.hasOwnProperty(e.parameters[p])) {
                        return;
                    }
                }
                e.duplicate(params);
            });
        }
}

// TODO - blockui need to be rename to reflect the new loader approach
Dashboards.blockUIwithDrag = function () {
    if (Dashboards.loadingAnimationTimeout !== undefined) {
        clearTimeout(Dashboards.loadingAnimationTimeout);
    }

    Dashboards.loadingAnimationTimeout = setTimeout(function () {
        $('.loading-bar').addClass('is-active');
    }, 3000);
};

Dashboards.updateLifecycle = function (object) {
    var silent = object.lifecycle ? !! object.lifecycle.silent : false;

    if (object.disabled) {
        return;
    }
    if (!silent) {
        this.incrementRunningCalls();
    }
    var handler = _.bind(function () {
        try {
            var shouldExecute;
            if (!(typeof (object.preExecution) === 'undefined')) {
                shouldExecute = object.preExecution.apply(object);
            }
            /*
             * If `preExecution` returns anything, we should use its truth value to
             * determine whether the component should execute. If it doesn't return
             * anything (or returns `undefined`), then by default the component
             * should update.
             */
            shouldExecute = typeof shouldExecute !== "undefined" ? !! shouldExecute : true;
            object.trigger('cdf cdf:preExecution', object, shouldExecute);
            if (!shouldExecute) {
                return; // if preExecution returns false, we'll skip the update
            }
            if (object.tooltip !== undefined) {
                object._tooltip = typeof object["tooltip"] === 'function' ? object.tooltip() : object.tooltip;
            }
            // first see if there is an objectImpl
            if ((object.update !== undefined) &&
                (typeof object['update'] === 'function')) {
                object.update();

                // check if component has periodic refresh and schedule next update
                this.refreshEngine.processComponent(object);

            } else {
                // unsupported update call
            }

            if (!(typeof (object.postExecution) === 'undefined')) {
                object.postExecution.apply(object);
            }
            // if we have a tooltip component, how is the time.
            if (object._tooltip !== undefined) {
                $("#" + object.htmlObject).attr("title", object._tooltip).tooltip({
                    delay: 0,
                    track: true,
                    fade: 250
                });
            }
        } catch (e) {
            var ph = (object.htmlObject) ? $('#' + object.htmlObject) : undefined,
                msg = Dashboards.getErrorObj('COMPONENT_ERROR').msg + ' (' + object.name.replace('render_', '') + ')';
            this.errorNotification({
                msg: msg
            }, ph);
            this.log("Error updating " + object.name + ":", 'error');
            this.log(e, 'exception');
        } finally {
            if (!silent) {
                this.decrementRunningCalls();
            }
        }

        // Triggering the event for the rest of the process
        object.trigger('cdf cdf:postExecution', object);

    }, this);
    setTimeout(handler, 1);
};

Dashboards.update = function (component) {
    /*
     * It's not unusual to have several consecutive calls to `update` -- it can
     * happen, e.g, as a result of using `DuplicateComponent` to clone a number
     * of components. If we pass each update individually to `updateAll`, the
     * first call will pass through directly, while the remaining calls will
     * result in the components being queued up for update only after the first
     * finished. To prevent this, we build a list of components waiting to be
     * updated, and only pass those forward to `updateAll` if we haven't had any
     * more calls within 5 miliseconds of the last.
     */
    if (!this.updateQueue) {
        this.updateQueue = [];
    }
    this.updateQueue.push(component);
    if (this.updateTimeout) {
        clearTimeout(this.updateTimeout);
    }

    var handler = _.bind(function () {
        this.updateAll(this.updateQueue);
        delete this.updateQueue;
    }, this);
    this.updateTimeout = setTimeout(handler, 5);
};

Dashboards.updateComponent = function (object) {
    if (Date.now() - Dashboards.lastServerResponse > Dashboards.serverCheckResponseTimeout) {
        //too long in between ajax communications
        if (!Dashboards.checkServer()) {
            Dashboards.hideProgressIndicator();
            Dashboards.loginAlert();
            throw "not logged in";
        }
    }

    if (object.isManaged === false && object.update) {
        object.update();
        // check if component has periodic refresh and schedule next update
        this.refreshEngine.processComponent(object);
    } else {
        this.updateLifecycle(object);
    }
};

Dashboards.createAndCleanErrorDiv = function () {
    if ($("#" + CDF_ERROR_DIV).length === 0) {
        $("body").append("<div id='" + CDF_ERROR_DIV + "'></div>");
    }
    $("#" + CDF_ERROR_DIV).empty();
};

Dashboards.showErrorTooltip = function () {
    $(function () {
        $(".cdf_error").tooltip({
            delay: 0,
            track: true,
            fade: 250,
            showBody: " -- "
        })
    });
};

Dashboards.getComponent = function (name) {
    for (var i in this.components) {
        if (this.components[i].name === name)
            return this.components[i];
    }
};

Dashboards.getComponentByName = function (name) {
    /*
     * weird things happens when global context is true - CDF thinks that
     * the components are attached to the window object.
     *
     * 'globalContext' - determines if components and params are retrieved
     * from the current window's object or from the Dashboards singleton
     */
    return this.getComponent(name);
};

Dashboards.addComponents = function (components) {
    components.forEach(function (component) {
        this.bindControl(component);
        this.components.push(component);
    }, this);
};

Dashboards.addComponent = function (component, options) {
    this.removeComponent(component);

    // Attempt to convert over to component implementation
    this.bindControl(component);

    var index = options && options.index;
    var L = this.components.length;
    if (index === null || index < 0 || index > L) {
        index = L;
    } // <=> push
    this.components[index] = component;
};

Dashboards.getComponentIndex = function (compOrNameOrIndex) {
    if (compOrNameOrIndex !== null) {
        switch (typeof compOrNameOrIndex) {
        case 'string':
            for (var i = 0, cs = this.components, L = cs.length; i < L; i++) {
                if (cs[i].name === compOrNameOrIndex) {
                    return i;
                }
            }
            break;
        case 'number':
            if (compOrNameOrIndex >= 0 && compOrNameOrIndex < this.components.length) {
                return compOrNameOrIndex;
            }
            break;

        default:
            return this.components.indexOf(compOrNameOrIndex);
        }
    }
    return -1;
};

Dashboards.removeComponent = function (compOrNameOrIndex) {
    var index = this.getComponentIndex(compOrNameOrIndex);
    var comp = null;
    if (index >= 0) {
        var cs = this.components;
        comp = cs[index];
        cs.splice(index, 1);
        comp.dashboard = null;

        comp.off('cdf:postExecution');
        comp.off('cdf:preExecution');
        comp.off('cdf:error');
        comp.off('all');
    }

    return comp;
};

Dashboards.registerEvent = function (ev, callback) {
    if (typeof this.events === 'undefined') {
        this.events = {};
    }
    this.events[ev] = callback;
};

Dashboards.addArgs = function (url) {
    if (url !== undefined)
        this.args = getURLParameters(url);
};

Dashboards.setI18nSupport = function (lc, i18nRef) {
    // Update global reference to i18n objects if needed
    if (i18nRef !== "undefined" && lc !== "undefined") {
        this.i18nCurrentLanguageCode = lc;
        this.i18nSupport = i18nRef;
    }

};

Dashboards.init = function (components) {
    var myself = this;

    // perform Mustache.js style templating with underscore
    _.templateSettings = {
        interpolate: /\{\{(.+?)\}\}/g
    };

    // here it was Storage initialization which was removed since we don't use it
    if (this.context !== null && this.context.sessionTimeout !== null) {
        //defaulting to 90% of ms value of sessionTimeout
        Dashboards.serverCheckResponseTimeout = this.context.sessionTimeout * 900;
    }

    this.restoreView();
    this.syncParametersInit();

    if ($.isArray(components)) {
        this.addComponents(components);
    }

    $(function () {
        myself.initEngine();
    });
};


/* Keep parameters master and slave in sync. The master parameter's
 * initial value takes precedence over the slave parameter's when
 * initializing the dashboard.
 */
Dashboards.syncParameters = function (master, slave) {
    this.setParameter(slave, this.getParameterValue(master));
    // this.parameterModel.change();
    this.parameterModel.on("change:" + master, function (m, v) {
        this.fireChange(slave, v)
    }, this);
    this.parameterModel.on("change:" + slave, function (m, v) {
        this.fireChange(master, v)
    }, this);
}

Dashboards.chains = [];
Dashboards.syncedParameters = {};
/* Register parameter pairs that will be synced on dashboard init. We'll store
 * the dependency pairings in Dashboards.syncedParameters,as an object mapping
 * master parameters to an array of all its slaves (so {a: [b,c]} means that
 * both *b* and *c* are subordinate to *a*), and in Dashboards.chains wel'll
 * store an array of arrays representing a list of separate dependency trees.
 * An entry of the form [a, b, c] means that *a* doesn't depend on either *b*
 * or *c*, and that *b* doesn't depend on *c*. Inversely, *b* depends on *a*,
 * and *c* depends on either *a* or *b*. You can have multiple such entries,
 * each representing a completely isolated set of dependencies.
 *
 * Note that we make no effort to detect circular dependencies. Behaviour is
 * undetermined should you provide such a case.
 */
Dashboards.syncParametersOnInit = function (master, slave) {
    var parameters = this.syncedParameters,
        currChain,
        masterChain,
        slaveChain, slaveChainIdx, i;
    if (!parameters[master]) parameters[master] = [];
    parameters[master].push(slave);

    /* When inserting an entry into Dashboards.chains, we need to check whether
     * any of the master or the slave are already in one of the chains.
     */
    for (i = 0; i < this.chains.length; i++) {
        currChain = this.chains[i];
        if (currChain.indexOf(master) > -1) {
            masterChain = currChain;
        }
        if (currChain.indexOf(slave) > -1) {
            slaveChain = currChain;
            slaveChainIdx = i;
        }
    }
    /* If both slave and master are present in different chains, we merge the
     * chains.
     *
     * If only one of the two is present, we insert the slave at the end
     * of the master's chain, or the master at the head of the slave's chain.
     *
     * Note that, since a parameter can be both a master and a slave, and because
     * no slave can have two masters, it is guaranteed that we can only add the
     * master to the head of the chain if the slave was the head before, and, when
     * adding the slave at the end of the master's chain, none of the parameters
     * between master and slave can depend on the slave. This means there is no
     * scenario where a chain can become inconsistent from prepending masters or
     * appending slaves.
     *
     * If neither master nor slave is present in the existing chains, we create a
     * new chain with [master, slave].
     */
    if (slaveChain && masterChain) {
        if (masterChain !== slaveChain) {
            args = slaveChain.slice();
            args.unshift(0);
            args.unshift(masterChain.length);
            [].splice.apply(masterChain, args);
            this.chains.splice(slaveChainIdx, 1);
        }
    } else if (slaveChain) {
        slaveChain.unshift(master);
    } else if (masterChain) {
        masterChain.push(slave)
    } else {
        this.chains.push([master, slave]);
    }
}

/*
 * Iterate over the registered parameter syncing chains,
 * and configure syncing for each parameter pair.
 */
Dashboards.syncParametersInit = function () {
    var parameters = this.syncedParameters,
        i, j, k, master, slave;
    for (i = 0; i < this.chains.length; i++) {
        for (j = 0; j < this.chains[i].length; j++) {
            var master = this.chains[i][j];
            if (!parameters[master]) continue;
            for (k = 0; k < parameters[master].length; k++) {
                slave = parameters[master][k];
                this.syncParameters(master, slave);
            }
        }
    }
}


Dashboards.initEngine = function () {
    // Should really throw an error? Or return?
    if (this.waitingForInit && this.waitingForInit.length) {
        this.log("Overlapping initEngine!", 'warn');
    }

    var myself = this;
    var components = this.components;

    this.incrementRunningCalls();
    if (this.logLifecycle && typeof console !== "undefined") {
        console.log("%c          [Lifecycle >Start] Init (Running: " + this.getRunningCalls() + ")", "color: #ddd ");
    }

    this.createAndCleanErrorDiv();
    // Fire all pre-initialization events
    if (typeof this.preInit === 'function') {
        this.preInit();
    }
    this.trigger("cdf cdf:preInit", this);
    /* Legacy Event -- don't rely on this! */
    $(window).trigger('cdfAboutToLoad');
    var myself = this;
    var updating = [],
        i;
    for (i = 0; i < components.length; i++) {
        if (components[i].executeAtStart) {
            updating.push(components[i]);
        }
    }

    if (!updating.length) {
        this.handlePostInit();
        return;
    }

    // Since we can get into racing conditions between last component's
    // preExecution and dashboard.postInit, we'll add a last component with very
    // low priority who's funcion is only to act as a marker.
    var postInitComponent = {
        name: "PostInitMarker",
        type: "unmanaged",
        lifecycle: {
            silent: true
        },
        executeAtStart: true,
        priority: 999999999
    };
    this.bindControl(postInitComponent)
    updating.push(postInitComponent);


    this.waitingForInit = updating.slice();

    var callback = function (comp, isExecuting) {
        /*
         * The `preExecution` event will pass two arguments (the component proper
         * and a flag telling us whether the preExecution test passed), so we can
         * test for that, and check whether the component is executing or not.
         * If it's not going to execute, we should check for postInit right now.
         * If it is, we shouldn't do anything.right now.
         */
        if (arguments.length === 2 && isExecuting) {
            return;
        }
        this.waitingForInit = _(this.waitingForInit).without(comp);
        comp.off('cdf:postExecution', callback);
        comp.off('cdf:preExecution', callback);
        comp.off('cdf:error', callback);
        this.handlePostInit();
    }

    for (var i = 0, len = updating.length; i < len; i++) {
        var component = updating[i];
        component.on('cdf:postExecution cdf:preExecution cdf:error', callback, myself);
    }
    Dashboards.updateAll(updating);
    if (components.length > 0) {
        myself.handlePostInit();
    }

};

Dashboards.handlePostInit = function () {
    if ((!this.waitingForInit || this.waitingForInit.length === 0) && !this.finishedInit) {
        this.trigger("cdf cdf:postInit", this);
        /* Legacy Event -- don't rely on this! */
        $(window).trigger('cdfLoaded');

        if (typeof this.postInit === "function") {
            this.postInit();
        }
        this.restoreDuplicates();
        this.finishedInit = true;

        this.decrementRunningCalls();
        if (this.logLifecycle && typeof console !== "undefined") {
            console.log("%c          [Lifecycle <End  ] Init (Running: " + this.getRunningCalls() + ")", "color: #ddd ");
        }
    }
};

Dashboards.resetAll = function () {
    this.createAndCleanErrorDiv();
    var compCount = this.components.length;
    for (var i = 0, len = this.components.length; i < len; i++) {
        this.components[i].clear();
    }
    var compCount = this.components.length;
    for (var i = 0, len = this.components.length; i < len; i++) {
        if (this.components[i].executeAtStart) {
            this.update(this.components[i]);
        }
    }
};

Dashboards.processChange = function (object_name) {

    //Dashboards.log("Processing change on " + object_name);

    var object = this.getComponentByName(object_name);
    var parameter = object.parameter;
    var value;
    if (typeof object['getValue'] === 'function') {
        value = object.getValue();
    }
    if (value === null) // We won't process changes on null values
        return;

    if (!(typeof (object.preChange) === 'undefined')) {
        var preChangeResult = object.preChange(value);
        value = preChangeResult !== undefined ? preChangeResult : value;
    }
    if (parameter) {
        this.fireChange(parameter, value);
    }
    if (!(typeof (object.postChange) === 'undefined')) {
        object.postChange(value);
    }
};

/* fireChange must accomplish two things:
 * first, we must change the parameters
 * second, we execute the components that listen for
 * changes on that parameter.
 *
 * Because some browsers won't draw the blockUI widgets
 * until the script has finished, we find the list of
 * components to update, then execute the actual update
 * in a function wrapped in a setTimeout, so the running
 * script has the opportunity to finish.
 */
Dashboards.fireChange = function (parameter, value) {
    var myself = this;
    this.createAndCleanErrorDiv();

    this.setParameter(parameter, value);
    // this.parameterModel.change();
    var toUpdate = [];
    var workDone = false;
    for (var i = 0, len = this.components.length; i < len; i++) {
        if ($.isArray(this.components[i].listeners)) {
            for (var j = 0; j < this.components[i].listeners.length; j++) {
                var comp = this.components[i];
                if (comp.listeners[j] === parameter && !comp.disabled) {
                    toUpdate.push(comp);
                    break;
                }
            }
        }
    }
    myself.updateAll(toUpdate);
};


/* Update components by priority. Expects as parameter an object where the keys
 * are the priorities, and the values are arrays of components that should be
 * updated at that priority level:
 *
 *    {
 *      0: [c1,c2],
 *      2: [c3],
 *      10: [c4]
 *    }
 *
 * Alternatively, you can pass an array of components, `[c1, c2, c3]`, in which
 * case the priority-keyed object will be created internally from the priority
 * values the components declare for themselves.
 *
 * Note that even though `updateAll` expects `components` to have numerical
 * keys, and that it does work if you pass it an array, `components` should be
 * an object, rather than an array, so as to allow negative keys (and so that
 * we can use it as a sparse array of sorts)
 */
Dashboards.updateAll = function (components) {
    if (!this.updating) {
        this.updating = {
            tiers: {},
            current: null
        };
    }
    if (components && _.isArray(components) && !_.isArray(components[0])) {
        var comps = {};
        _.each(components, function (c) {
            var prio = c.priority || 0;
            if (!comps[prio]) {
                comps[prio] = [];
            }
            comps[prio].push(c);
        });
        components = comps;
    }
    this.mergePriorityLists(this.updating.tiers, components);

    var updating = this.updating.current;
    if (updating === null || updating.components.length === 0) {
        var toUpdate = this.getFirstTier(this.updating.tiers);
        if (!toUpdate) return;
        this.updating.current = toUpdate;

        var postExec = function (component, isExecuting) {
            /*
             * We first need to figure out what event we're handling. `error` will
             * pass the component, error message and caught exception (if any) to
             * its event handler, while the `preExecution` event will pass two
             * arguments (the component proper and a flag telling us whether the
             * preExecution test passed).
             *
             * If we're not going to finish updating the component, either because
             * `preExecution` cancelled the update, or because we're in an `error`
             * event handler, we should queue up the next component right now.
             */
            if (arguments.length === 2 && typeof isExecuting === "boolean" && isExecuting) {
                return;
            }
            component.off("cdf:postExecution", postExec);
            component.off("cdf:preExecution", postExec);
            component.off("cdf:error", postExec);
            var current = this.updating.current;
            current.components = _.without(current.components, component);
            var tiers = this.updating.tiers;
            tiers[current.priority] = _.without(tiers[current.priority], component);
            this.updateAll();
        }
        /*
         * Any synchronous components we update will edit the `current.components`
         * list midway through this loop, so we need a separate copy of that list
         * so as to avoid messing up the indices.
         */
        var comps = this.updating.current.components.slice();
        for (var i = 0; i < comps.length; i++) {
            component = comps[i];
            // Start timer
            component.startTimer();
            component.on("cdf:postExecution cdf:preExecution cdf:error", postExec, this);

            // Logging this.updating. Uncomment if needed to trace issues with lifecycle
            // Dashboards.log("Processing "+ component.name +" (priority " + this.updating.current.priority +"); Next in queue: " +
            //  _(this.updating.tiers).map(function(v,k){return k + ": [" + _(v).pluck("name").join(",") + "]"}).join(", "));
            this.updateComponent(component);
        }
    }
}

/*
 * Given a list of component priority tiers, returns the highest priority
 * non-empty tier of components awaiting update, or null if no such tier exists.
 */
Dashboards.getFirstTier = function (tiers) {
    var keys = _.keys(tiers).sort(function (a, b) {
        return parseInt(a, 10) - parseInt(b, 10);
    }),
        i, tier;

    for (i = 0; i < keys.length; i++) {
        tier = tiers[keys[i]];
        if (tier.length > 0) {
            return {
                priority: keys[i],
                components: tier.slice()
            };
        }
    }
    return null;
}

/*
 * Add all components in priority list 'source' into priority list 'target'
 */
Dashboards.mergePriorityLists = function (target, source) {
    if (!source) {
        return;
    }
    for (var key in source)
        if (source.hasOwnProperty(key)) {
            if (_.isArray(target[key])) {
                target[key] = _.union(target[key], source[key]);
            } else {
                target[key] = source[key];
            }
        }
}

Dashboards.restoreView = function () {
    var p, params;
    if (!this.view) return;
    /* Because we're storing the parameters in OrientDB, and as OrientDB has some
     * serious issues when storing nested objects, we're stuck marshalling the
     * parameters into a JSON object and converting that JSON into a Base64 blob
     * before storage. So now we have to decode that mess.
     */
    params = JSON.parse(Base64.decode(this.view.params));
    for (p in params)
        if (params.hasOwnProperty(p)) {
            this.setParameter(p, params[p]);
        }
};

Dashboards.setParameterViewMode = function (parameter, value) {
    if (!this.viewParameters) this.viewParameters = {};
    if (arguments.length === 1) value = this.viewFlags.VIEW;
    //if(!Dashboards.viewFlags.hasOwnProperty(value)) throw
    this.viewParameters[parameter] = value;
};

Dashboards.isViewParameter = function (parameter) {
    if (!this.viewParameters) {
        return false;
    }
    return this.viewParameters[parameter];
};

/*
 * List the values for all dashboard parameters flagged as being View parameters
 */
Dashboards.getViewParameters = function () {
    if (!this.viewParameters) return {};
    var params = this.viewParameters,
        ret = {};
    for (var p in params)
        if (params.hasOwnProperty(p)) {
            if (params[p] === this.viewFlags.VIEW || params[p] === this.viewFlags.UNBOUND) {
                ret[p] = this.getParameterValue(p);
            }
        }
    return ret;
};

/*
 * List all dashboard parameters flagged as being Unbound View parameters
 */

Dashboards.getUnboundParameters = function () {
    if (!this.viewParameters) return [];
    var params = this.viewParameters,
        ret = []
    for (var p in params)
        if (params.hasOwnProperty(p)) {
            if (params[p] === this.viewFlags.UNBOUND) {
                ret.push(p);
            }
            return ret;
        }
};

Dashboards.getParameterValue = function (parameterName) {
    if (this.globalContext) {
        try {
            return eval(parameterName);
        } catch (e) {
            this.error(e);
            //return undefined;
        }
    } else {
        return this.parameters[parameterName];
    }
};

Dashboards.getQueryParameter = function (parameterName) {
    // Add "=" to the parameter name (i.e. parameterName=value)
    var queryString = window.location.search.substring(1);
    var parameterName = parameterName + "=";
    if (queryString.length > 0) {
        // Find the beginning of the string
        var begin = queryString.indexOf(parameterName);
        // If the parameter name is not found, skip it, otherwise return the value
        if (begin !== -1) {
            // Add the length (integer) to the beginning
            begin += parameterName.length;
            // Multiple parameters are separated by the "&" sign
            var end = queryString.indexOf("&", begin);
            if (end === -1) {
                end = queryString.length
            }
            // Return the string
            return decodeURIComponent(queryString.substring(begin, end));
        }
        // Return "" if no parameter has been found
        return "";
    }
};

Dashboards.setParameter = function (parameterName, parameterValue) {
    if (parameterName === undefined || parameterName === "undefined") {
        this.log('Dashboards.setParameter: trying to set undefined!!', 'warn');
        return;
    }
    if (this.globalContext) {
        //ToDo: this should really be sanitized!
        eval(parameterName + " = " + JSON.stringify(parameterValue));
    } else {
        if (this.escapeParameterValues) {
            this.parameters[parameterName] = encode_prepare_arr(parameterValue);
        } else {
            this.parameters[parameterName] = parameterValue;
        }
    }
    this.parameterModel.set(parameterName, parameterValue, {
        silent: true
    });
};


Dashboards.post = function (url, obj) {
    var form = '<form action="' + url + '" method="post">';
    for (var o in obj) {

        var v = (typeof obj[o] === 'function' ? obj[o]() : obj[o]);

        if (typeof v === 'string') {
            v = v.replace(/"/g, "\'")
        }

        form += '"<input type="hidden" name="' + o + '" value="' + v + '"/>';
    }

    form += '</form>';
    jQuery(form).appendTo('body').submit().remove();
};

Dashboards.getArgValue = function (key) {
    for (i = 0; i < this.args.length; i++) {
        if (this.args[i][0] === key) {
            return this.args[i][1];
        }
    }

    return undefined;
};

Dashboards.ev = function (o) {
    return typeof o === 'function' ? o() : o
};

// TODO - do we need this *pentaho* functions - since we don't use the Pentaho server
Dashboards.callPentahoAction = function (obj, solution, path, action, parameters, callback) {
    var myself = this;

    // Encapsulate pentahoAction call
    // Dashboards.log("Calling pentahoAction for " + obj.type + " " + obj.name + "; Is it visible?: " + obj.visible);
    if (typeof callback === 'function') {
        return this.pentahoAction(solution, path, action, parameters,
            function (json) {
                callback(myself.parseXActionResult(obj, json));
            }
        );
    } else {
        return this.parseXActionResult(obj, this.pentahoAction(solution, path, action, parameters, callback));
    }
};

Dashboards.urlAction = function (url, params, func) {
    return this.executeAjax('xml', url, params, func);
};

Dashboards.executeAjax = function (returnType, url, params, func) {
    var myself = this;
    // execute a url
    if (typeof func === "function") {
        // async
        return $.ajax({
            url: url,
            type: "POST",
            dataType: returnType,
            async: true,
            data: params,
            complete: function (XMLHttpRequest, textStatus) {
                func(XMLHttpRequest.responseXML);
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                myself.log("Found error: " + XMLHttpRequest + " - " + textStatus + ", Error: " + errorThrown, "error");
            }
        });
    }

    // Sync
    var result = $.ajax({
        url: url,
        type: "POST",
        dataType: returnType,
        async: false,
        data: params,
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            myself.log("Found error: " + XMLHttpRequest + " - " + textStatus + ", Error: " + errorThrown, "error");
        }

    });
    if (returnType === 'xml') {
        return result.responseXML;
    } else {
        return result.responseText;
    }

};

Dashboards.pentahoAction = function (solution, path, action, params, func) {
    return this.pentahoServiceAction('ServiceAction', 'xml', solution, path, action, params, func);
};

Dashboards.pentahoServiceAction = function (serviceMethod, returntype, solution, path, action, params, func) {
    // execute an Action Sequence on the server

    var url = this.getWebAppPath() + "/" + serviceMethod;

    // Add the solution to the params
    var arr = {};
    arr.wrapper = false;
    arr.solution = solution;
    arr.path = path;
    arr.action = action;
    $.each(params, function (i, val) {
        arr[val[0]] = val[1];
    });
    return this.executeAjax(returntype, url, arr, func);
};

Dashboards.parseXActionResult = function (obj, html) {

    var jXML = $(html);
    var error = jXML.find("SOAP-ENV\\:Fault");
    if (error.length === 0) {
        return jXML;
    }

    // error found. Parsing it
    var errorMessage = "Error executing component " + obj.name;
    var errorDetails = [];
    errorDetails[0] = " Error details for component execution " + obj.name + " -- ";
    errorDetails[1] = error.find("SOAP-ENV\\:faultstring").find("SOAP-ENV\\:Text:eq(0)").text();
    error.find("SOAP-ENV\\:Detail").find("message").each(function () {
        errorDetails.push($(this).text())
    });
    if (errorDetails.length > 8) {
        errorDetails = errorDetails.slice(0, 7);
        errorDetails.push("...");
    }

    var out = "<table class='errorMessageTable' border='0'><tr><td><img src='" + ERROR_IMAGE + "'></td><td><span class=\"cdf_error\" title=\" " + errorDetails.join('<br/>').replace(/"/g, "'") + "\" >" + errorMessage + " </span></td></tr></table/>";

    // if this is a hidden component, we'll place this in the error div
    if (obj.visible === false) {
        $("#" + CDF_ERROR_DIV).append("<br />" + out);
    } else {
        $('#' + obj.htmlObject).html(out);
    }


    return null;

};

Dashboards.escapeHtml = function (input) {
    var escaped = input
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/'/g, "&#39;")
        .replace(/"/g, "&#34;");
    return escaped;
};

(function (D) {
    // Conversion functions
    function _pa2obj(pArray) {
        var obj = {};
        for (var p in pArray)
            if (pArray.hasOwnProperty(p)) {
                var prop = pArray[p];
                obj[prop[0]] = prop[1];
            }
        return obj;
    };

    function _obj2pa(obj) {
        var pArray = [];
        for (var key in obj)
            if (obj.hasOwnProperty(key)) {
                pArray.push([key, obj[key]]);
            }
        return pArray;
    };

    // Exports
    // NOTE: using underscore.js predicates but we could also use Dashboards.isArray() and 
    //       Dashboards.isObject() (would need to create this one.)
    D.propertiesArrayToObject = function (pArray) {
        // Mantra 1: "Order matters!"
        // Mantra 2: "Arrays are Objects!"
        return (_.isArray(pArray) && _pa2obj(pArray)) || (_.isObject(pArray) && pArray) || undefined;
    };

    D.objectToPropertiesArray = function (obj) {
        // Mantra 1: "Order matters!"
        // Mantra 2: "Arrays are Objects!"
        return (_.isArray(obj) && obj) || (_.isObject(obj) && _obj2pa(obj)) || undefined;
    };
})(Dashboards);


/**
 * Traverses each <i>value</i>, <i>label</i> and <i>id</i> triple of a <i>values array</i>.
 *
 * @param {Array.<Array.<*>>} values the values array - an array of arrays.
 *   <p>
 *   Each second-level array is a <i>value specification</i> and contains
 *   a value and, optionally, a label and an id.
 *   It may have the following forms:
 *   </p>
 *   <ul>
 *     <li><tt>[valueAndLabel]</tt> - when having <i>length</i> one</li>
 *     <li><tt>[value, label,...]</tt> - when having <i>length</i> two or more and
 *         <tt>opts.valueAsId</tt> is falsy
 *     </li>
 *     <li><tt>[id, valueAndLabel,..]</tt> - when having <i>length</i> two or more and
 *         <tt>opts.valueAsId</tt> is truthy
 *     </li>
 *   </ul>
 * @param {object} opts an object with options.
 *
 * @param {?boolean=} [opts.valueAsId=false] indicates if the first element of
 *   the value specification array is the id, instead of the value.
 *
 * @param {function(string, string, string, number):?boolean} f
 * the traversal function that is to be called with
 * each value-label-id triple and with the JS content <tt>x</tt>.
 * The function is called with arguments: <tt>value</tt>, <tt>label</tt>,
 * <tt>id</tt> and <tt>index</tt>.
 * <p>
 * When the function returns the value <tt>false</tt>, traversal is stopped,
 * and <tt>false</tt> is returned.
 * </p>
 *
 * @param {object} x the JS context object on which <tt>f</tt> is to be called.
 *
 * @return {boolean} indicates if the traversal was complete, <tt>true</tt>,
 *   or if explicitly stopped by the traversal function, <tt>false</tt>.
 */
Dashboards.eachValuesArray = function (values, opts, f, x) {
    if (typeof opts === 'function') {
        x = f;
        f = opts;
        opts = null;
    }

    var valueAsId = !! (opts && opts.valueAsId);
    for (var i = 0, j = 0, L = values.length; i < L; i++) {
        var valSpec = values[i];
        if (valSpec && valSpec.length) {
            var v0 = valSpec[0];
            var value, label, id = undefined; // must reset on each iteration

            if (valSpec.length > 1) {
                if (valueAsId) {
                    id = v0;
                }
                label = "" + valSpec[1];
                value = (valueAsId || v0 === null) ? label : ("" + v0);
            } else {
                value = label = "" + v0;
            }

            if (f.call(x, value, label, id, j, i) === false) {
                return false;
            }
            j++;
        }
    }

    return true;
};


/**
 * Given a parameter value obtains an equivalent values array.
 *
 * <p>The parameter value may encode multiple values in a string format.</p>
 * <p>A nully (i.e. null or undefined) input value or an empty string result in <tt>null</tt>,
 *    and so the result of this method is normalized.
 * </p>
 * <p>
 * A string value may contain multiple values separated by the character <tt>|</tt>.
 * </p>
 * <p>An array or array-like object is returned without modification.</p>
 * <p>Any other value type returns <tt>null</tt>.</p>
 *
 * @param {*} value
 * a parameter value, as returned by {@link Dashboards.getParameterValue}.
 *
 * @return {null|!Array.<*>|!{join}} null or an array or array-like object.
 *
 * @static
 */
Dashboards.parseMultipleValues = function (value) {
    if (value !== null && value !== '') {
        // An array or array like?
        if (this.isArray(value)) {
            return value;
        }
        if (typeof value === "string") {
            return value.split("|");
        }
    }

    // null or of invalid type
    return null;
};

/**
 * Normalizes a value so that <tt>undefined</tt>, empty string
 * and empty array, are all translated to <tt>null</tt>.
 * @param {*} value the value to normalize.
 * @return {*} the normalized value.
 *
 * @static
 */
Dashboards.normalizeValue = function (value) {
    if (value === '' || value === null) {
        return null;
    }
    if (this.isArray(value) && !value.length) return null;
    return value;
};

/**
 * Determines if a value is considered an array.
 * @param {*} value the value.
 * @return {boolean}
 *
 * @static
 */
Dashboards.isArray = function (value) {
    // An array or array like?
    return !!value &&
        ((value instanceof Array) ||
        (typeof value === 'object' && value.join && value.length !== null));
};

/**
 * Determines if two values are considered equal.
 * @param {*} a the first value.
 * @param {*} b the second value.
 * @return {boolean}
 *
 * @static
 */
Dashboards.equalValues = function (a, b) {
    // Identical or both null/undefined?
    a = this.normalizeValue(a);
    b = this.normalizeValue(b);

    if (a === b) {
        return true;
    }

    if (this.isArray(a) && this.isArray(b)) {
        var L = a.length;
        if (L !== b.length) {
            return false;
        }
        while (L--) {
            if (!this.equalValues(a[L], b[L])) {
                return false;
            }
        }
        return true;
    }

    // Last try, give it to JS equals
    return a === b;
};

Dashboards.clone = function clone(obj) {
    var c = obj instanceof Array ? [] : {};

    for (var i in obj) {
        var prop = obj[i];

        if (typeof prop === 'object') {
            if (prop instanceof Array) {
                c[i] = [];

                for (var j = 0; j < prop.length; j++) {
                    if (typeof prop[j] !== 'object') {
                        c[i].push(prop[j]);
                    } else {
                        c[i].push(this.clone(prop[j]));
                    }
                }
            } else {
                c[i] = this.clone(prop);
            }
        } else {
            c[i] = prop;
        }
    }

    return c;
};

Dashboards.safeClone = function () {
    var options, name, src, copy, copyIsArray, clone,
        target = arguments[0] || {},
        i = 1,
        length = arguments.length,
        deep = false;

    // Handle a deep copy situation
    if (typeof target === "boolean") {
        deep = target;
        target = arguments[1] || {};
        // skip the boolean and the target
        i = 2;
    }

    // Handle case when target is a string or something (possible in deep copy)
    if (typeof target !== "object" && !jQuery.isFunction(target)) {
        target = {};
    }

    for (; i < length; i++) {
        // Only deal with non-null/undefined values
        if ((options = arguments[i]) !== null) {
            // Extend the base object
            for (name in options)
                if (options.hasOwnProperty(name)) {
                    src = target[name];
                    copy = options[name];

                    // Prevent never-ending loop
                    if (target === copy) {
                        continue;
                    }

                    // Recurse if we're merging plain objects or arrays
                    if (deep && copy && (jQuery.isPlainObject(copy) || (copyIsArray = jQuery.isArray(copy)))) {
                        if (copyIsArray) {
                            copyIsArray = false;
                            clone = src && jQuery.isArray(src) ? src : [];

                        } else {
                            clone = src && jQuery.isPlainObject(src) ? src : {};
                        }

                        // Never move original objects, clone them
                        target[name] = this.safeClone(deep, clone, copy);

                        // Don't bring in undefined values
                    } else if (copy !== undefined) {
                        target[name] = copy;
                    }
                }
        }
    }

    // Return the modified object
    return target;
};

// Based on the algorithm described at http://en.wikipedia.org/wiki/HSL_and_HSV.
/**
 * Converts an HSV to an RGB color value.
 *
 * @param {number} h Hue as a value between 0 - 360 (degrees)
 * @param {number} s Saturation as a value between 0 - 100 (%)
 * @param {number} v Value as a value between 0 - 100 (%)
 * @return {string} An rgb(...) color string.
 *
 * @static
 */
Dashboards.hsvToRgb = function (h, s, v) {
    v = v / 100; // 0 - 1
    s = s / 100; // idem

    var h6 = (h % 360) / 60;
    var chroma = v * s;
    var m = v - chroma;
    var h6t = Math.abs((h6 % 2) - 1);
    //var r = 1 - h6t;
    //var x = chroma * r;
    var x_m = v * (1 - s * h6t); // x + m
    var c_m = v; // chroma + m
    // floor(h6) (0, 1, 2, 3, 4, 5)

    var rgb;
    switch (~~h6) {
    case 0:
        rgb = [c_m, x_m, m];
        break;
    case 1:
        rgb = [x_m, c_m, m];
        break;
    case 2:
        rgb = [m, c_m, x_m];
        break;
    case 3:
        rgb = [m, x_m, c_m];
        break;
    case 4:
        rgb = [x_m, m, c_m];
        break;
    case 5:
        rgb = [c_m, m, x_m];
        break;
    }

    rgb.forEach(function (val, i) {
        rgb[i] = Math.min(255, Math.round(val * 256));
    });

    return "rgb(" + rgb.join(",") + ")";
};


// /*
//  * Query STUFF
//  * (Here for legacy reasons)
//  * 
//  */
// //Ctors:
// // Query(queryString) --> DEPRECATED
// // Query(queryDefinition{path, dataAccessId})
// // Query(path, dataAccessId)
// Query = function (cd, dataAccessId) {

//     var opts, queryType;

//     if (_.isObject(cd)) {
//         opts = Dashboards.safeClone(true, cd);
//         queryType = (_.isString(cd.queryType) && cd.queryType) || ( !_.isUndefined(cd.query) && 'legacy') ||
//             ( !_.isUndefined(cd.path) && !_.isUndefined(cd.dataAccessId) && 'cda') || undefined;
//     } else if (_.isString(cd) && _.isString(dataAccessId)) {
//         queryType = 'cda';
//         opts = {
//             path: cd,
//             dataAccessId: dataAccessId
//         };
//     }

//     if (!queryType) {
//         throw 'InvalidQuery'
//     }

//     return Dashboards.getQuery(queryType, opts);
// };
// QUERIES end
