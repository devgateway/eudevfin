// CONTAINER begin
(function (D) {
    function Container() {
        // PUBLIC

        // register(type, what [, scope])
        // register(type, name, what [, scope])
        this.register = function (type, name, what, scope) {
            if (!type) {
                throw new Error("Argument 'type' is required.");
            }
            if (typeof type !== 'string') {
                throw new Error("Argument 'type' must be a string.");
            }

            if (name != null) {
                if (typeof name !== 'string') {
                    scope = what;
                    what = name;
                    name = null;
                } else if (!name) {
                    name = null;
                }
            }

            if (!what) {
                throw new Error("Argument 'what' is required.");
            }

            var holder;
            switch (typeof what) {
                case 'function':
                    holder = new FactoryHolder(this, what, scope);
                    break;
                case 'object':
                    holder = new InstanceHolder(this, what, scope);
                    break;
                default:
                    throw new Error("Argument 'what' is of an invalid type.");
            }

            if (!name) {
                name = '';
            }

            var holdersByName = _typesTable[type] || (_typesTable[type] = {});
            var currHolder = holdersByName[name];
            if (currHolder) {
                // throw? log?
                currHolder.dispose();
            }
            holdersByName[name] = holder;
        };

        this.has = function (type, name) {
            return !!getHolder(type, name, true);
        };

        this.canNew = function (type, name) {
            return getHolder(type, name, false) instanceof FactoryHolder;
        };

        this.get = function (type, name) {
            return get(type, name, null, false, false);
        };

        this.tryGet = function (type, name) {
            return get(type, name, null, false, true);
        };

        this.getNew = function (type, name, config) {
            return get(type, name, config, true, false);
        };

        this.tryGetNew = function (type, name, config) {
            return get(type, name, config, true, true);
        };

        this.getAll = function (type) {
            return getAll(type, false);
        };

        this.tryGetAll = function (type) {
            return getAll(type, true);
        };

        this.listType = function (type) {
            return getType(type, false);
        };

        this.tryListType = function (type) {
            return getType(type, true);
        };

        this.dispose = function () {
            if (_typesTable) {
                for (var type in _typesTable) {
                    var holdersByName = _typesTable[type];
                    for (var name in holdersByName) {
                        holdersByName[name].dispose();
                    }
                }

                _typesTable = null;
            }
        };

        // PRIVATE
        var _typesTable = {}; // type -> []

        function getType(type, isTry) {
            if (!type) {
                throw new Error("Argument 'type' is required.");
            }
            if (typeof type !== 'string') {
                throw new Error("Argument 'type' must be a string.");
            }

            var holdersByName = _typesTable[type];
            if (!isTry && (!holdersByName || isOwnEmpty(holdersByName))) {
                throw new Error("There are no registrations for type '" + type + "'.");
            }
            return holdersByName;
        }

        function getHolder(type, name, isTry) {
            var holder;
            var holdersByName = getType(type, isTry);
            if (holdersByName) {
                holder = holdersByName[name || ''];
                if (!holder && !isTry) {
                    throw new Error(
                        "There is no registration for type '" + type + "'" +
                            (name ? (" and name '" + name + "'") : "") + ".");
                }
            }

            return holder;
        }

        function get(type, name, config, isNew, isTry) {
            if (typeof name !== 'string') {
                config = name;
                name = '';
            }

            var holder = getHolder(type, name, isTry);

            // Can't store as singletons instances with special config params
            if (config) {
                isNew = true;
            } else if (!isNew) {
                config = {};
            }

            return holder ? holder.build(config, isNew) : null;
        }

        function getAll(type, isTry) {
            var holdersByName = getType(type, isTry);

            // Includes the default (unnamed) instance
            var instances = [];
            for (var name in holdersByName) {
                instances.push(holdersByName[name].build({}, false));
            }
            return instances;
        }
    }

    // Shared/Static stuff

    // Allows creating multiple instances
    function FactoryHolder(container, factory, scope) {
        var instance;

        if (!scope) {
            scope = 'instance';
        }

        this.build = function (config, buildNew) {
            if (instance && !buildNew) {
                return instance;
            }

            var inst = factory(container, config);

            if (!buildNew && scope === 'singleton') {
                instance = inst;
            }

            return inst;
        };

        this.dispose = function () {
            if (instance) {
                doDispose(instance);
                instance = null;
            }
        };
    }

    function InstanceHolder(container, instance, scope) {
        if (!scope) {
            scope = 'external';
        }

        this.build = function (/*config, buildNew*/) {
            return instance;
        };

        // external scope is managed outside the container
        this.dispose = function () {
            if (instance) {
                scope === 'singleton' && doDispose(instance);
                instance = null;
            }
        };
    }

    // Fwk stuff
    function doDispose(instance) {
        if (typeof instance.dispose === 'function') {
            instance.dispose();
        }
    }

    var hasOwn = Object.prototype.hasOwnProperty;

    function isOwnEmpty(o) {
        // tolerates o == null
        for (var n in o) {
            if (hasOwn.call(o, n)) {
                return false;
            }
        }
        return true;
    }

    // Export
    D.Container = Container;
})(Dashboards);
// CONTAINER end

// ADDINS begin
(function (D) {
    D.addIns = new D.Container();

    //Normalization - Ensure component does not finish with component and capitalize first letter
    D.normalizeAddInKey = function (key, subKey) {
        if (key.indexOf('Component', key.length - 'Component'.length) !== -1)
            key = key.substring(0, key.length - 'Component'.length);
        key = key.charAt(0).toUpperCase() + key.substring(1);

        if (subKey) {
            key += "." + subKey;
        }

        return key;
    }

    D.registerAddIn = function (type, subType, addIn) {
        var type = this.normalizeAddInKey(type, subType),
            name = addIn.getName ? addIn.getName() : null;
        this.addIns.register(type, name, addIn);
    };

    D.hasAddIn = function (type, subType, addInName) {
        var type = this.normalizeAddInKey(type, subType);
        return Boolean(this.addIns && this.addIns.has(type, addInName));
    };

    D.getAddIn = function (type, subType, addInName) {
        var type = this.normalizeAddInKey(type, subType);
        try {
            var addIn = this.addIns.get(type, addInName);
            return addIn;
        } catch (e) {
            return null;
        }
    };

    D.setAddInDefaults = function (type, subType, addInName, defaults) {
        var addIn = this.getAddIn(type, subType, addInName);
        if (addIn) {
            addIn.setDefaults(defaults);
        }
    };
    D.listAddIns = function (type, subType) {
        var type = this.normalizeAddInKey(type, subType);
        var addInList = [];
        try {
            return this.addIns.listType(type);
        } catch (e) {
            return [];
        }
    };
})(Dashboards);
// ADDINS end

// QUERIES begin
(function (D) {

    var _BaseQuery = Base;

    D.getBaseQuery = function () {
        return _BaseQuery;
    };
    D.setBaseQuery = function (QueryClass) {
        if (_.isFunction(QueryClass) && QueryClass.extend) {
            _BaseQuery = QueryClass;
        }
    };

    D.queryFactories = new D.Container();

    D.registerQuery = function (type, query) {
        var BaseQuery = this.getBaseQuery();

        // Goes a level deeper one extending these properties. Usefull to preserve defaults and
        // options interfaces from BaseQuery.
        if (!_.isFunction(query) && _.isObject(query)) {
            var deepProperties = {};
            _.each(BaseQuery.prototype.deepProperties, function (prop) {
                deepProperties[prop] = _.extend({}, BaseQuery.prototype[prop], query[prop]);
            });
        }

        var QueryClass = ( _.isFunction(query) && query ) ||
            ( _.isObject(query) && BaseQuery.extend(_.extend({}, query, deepProperties)) );

        // Registers a new query factory with a custom class
        this.queryFactories.register('Query', type, function (container, config) {
            return new QueryClass(config);
        });
    };

    D.hasQuery = function (type) {
        return Boolean(this.queryFactories && this.queryFactories.has('Query', type));
    };

    D.getQuery = function (type, opts) {
        if (_.isUndefined(type)) {
            type = 'cda';
        } else if (_.isObject(type)) {
            opts = type;
            type = opts.queryType || 'cda';
        }
        var query = this.queryFactories.getNew('Query', type, opts);
        return query;
    };

    D.listQueries = function () {
        return _.keys(this.queryFactories.listType('Query'));
    };
})(Dashboards);
// QUERIES end
