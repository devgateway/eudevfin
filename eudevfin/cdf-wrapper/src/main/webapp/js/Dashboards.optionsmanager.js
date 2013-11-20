// OPTIONS MANAGER begin
(function (D) {

    // This class is intended to be used as a generic Options Manager, by providing a way to
    // keep record of the values of an options set, but also custom readers, writers and validators 
    // for each of the options.
    function OptionsManager(config) { /* { defaults: {}, interfaces: {}, libraries: {} }*/
        var myself = this;

        // PROTECTED
        this._options = {};
        this._interfaces = {};
        this._libraries = {
            predicates: {
                tautology: function (value) {
                    return true
                },
                isFunction: _.isFunction,
                isPositive: function (value) {
                    return (_.isNumber(value) && value > 0);
                },
                isObjectOrPropertiesArray: function (value) {
                    return _.isArray(value) || _.isObject(value);
                },
                isObject: _.isObject,
                isArray: _.isArray
            },
            mappers: {
                identity: _.identity,
                propertiesObject: function (value) {
                    return (_.isArray(value)) ? D.propertiesArrayToObject(value) : value;
                }
            }
        };

        // PUBLIC
        this.mixin = function (instance) {
            instance.getOption = this.getOption;
            instance.setOption = this.setOption;
        };

        this.init = function (defaults, interfaces, libraries) {
            var myself = this;
            this._libraries = $.extend(true, {}, this._libraries, libraries);
            _.each(interfaces, function (el, key) {
                setInterfaces(key, el);
            });
            _.each(defaults, function (el, key) {
                var ifaces = ( interfaces && interfaces[key] ) || {};
                setInterfaces(key, ifaces);
                setValue(key, el);
            })
        };

        this.setOption = function (opt, value, interfaces) {
            setInterfaces(opt, interfaces);
            var reader = getReader(opt),
                validator = getValidator(opt);
            if (validator(value)) {
                value = reader(value);
                setValue(opt, value);
                return true
            } else {
                throw new Error("Invalid Option " + opt.charAt(0).toUpperCase() + opt.slice(1));
            }
        };

        this.getOption = function (opt) {
            var writer = getWriter(opt),
                value = getValue(opt);
            return writer(value);
        };

        // PRIVATE
        function setInterfaces(opt, interfaces) {
            interfaces = interfaces || {};
            setReader(opt, interfaces['reader']);
            setWriter(opt, interfaces['writer']);
            setValidator(opt, interfaces['validator']);
        };

        function getReader(opt) {
            return get(myself._interfaces, opt, 'reader', myself._libraries.mappers['identity']
            )
        };
        function getWriter(opt) {
            return get(myself._interfaces, opt, 'writer', myself._libraries.mappers['identity']
            )
        };
        function getValidator(opt) {
            return get(myself._interfaces, opt, 'validator', myself._libraries.predicates['tautology']
            )
        };
        function getValue(opt) {
            return get(myself._options, opt, 'value')
        };

        // Reader, Writer and Validator work in the same way:
        // If the value is a function, use it. 
        // Otherwise, if it is a string and a valid library key, use it.
        // Otherwise, use a default library function: for readers and writers an indentity map, 
        //    for validators a predicate that always returns true.

        function setReader(opt, fn) {
            var lib = myself._libraries.mappers;
            fn = ( _.isFunction(fn) && fn ) || ( _.isString(fn) && lib[fn] ) || getReader(opt) || lib['identity'];
            return set(myself._interfaces, opt, 'reader', fn)
        };
        function setWriter(opt, fn) {
            var lib = myself._libraries.mappers;
            fn = ( _.isFunction(fn) && fn ) || ( _.isString(fn) && lib[fn] ) || getWriter(opt) || lib['identity'];
            return set(myself._interfaces, opt, 'writer', fn)
        };
        function setValidator(opt, fn) {
            var lib = myself._libraries.predicates;
            fn = ( _.isFunction(fn) && fn ) || ( _.isString(fn) && lib[fn] ) || getValidator(opt) || lib['tautology'];
            return set(myself._interfaces, opt, 'validator', fn)
        };
        function setValue(opt, value) {
            return set(myself._options, opt, 'value', value)
        };

        // Init
        this.init(config.defaults, config.interfaces, config.libraries);

    }

    // Shared / Static
    function get(container, opt, attr, defaultValue) {
        var val = defaultValue || undefined;
        if (container && container[opt] && container[opt].hasOwnProperty(attr)) {
            val = container[opt][attr];
        }
        return val
    }

    function set(container, opt, attr, value) {
        if (container && opt && attr) {
            container[opt] = container[opt] || {};
            container[opt][attr] = value;
        }
    }

    D.OptionsManager = OptionsManager;
})(Dashboards);
