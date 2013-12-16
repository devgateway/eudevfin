//     Underscore.js 1.5.2
//     http://underscorejs.org
//     (c) 2009-2013 Jeremy Ashkenas, DocumentCloud and Investigative Reporters & Editors
//     Underscore may be freely distributed under the MIT license.

(function () {

    // Baseline setup
    // --------------

    // Establish the root object, `window` in the browser, or `exports` on the server.
    var root = this;

    // Save the previous value of the `_` variable.
    var previousUnderscore = root._;

    // Establish the object that gets returned to break out of a loop iteration.
    var breaker = {};

    // Save bytes in the minified (but not gzipped) version:
    var ArrayProto = Array.prototype, ObjProto = Object.prototype, FuncProto = Function.prototype;

    // Create quick reference variables for speed access to core prototypes.
    var
        push = ArrayProto.push,
        slice = ArrayProto.slice,
        concat = ArrayProto.concat,
        toString = ObjProto.toString,
        hasOwnProperty = ObjProto.hasOwnProperty;

    // All **ECMAScript 5** native function implementations that we hope to use
    // are declared here.
    var
        nativeForEach = ArrayProto.forEach,
        nativeMap = ArrayProto.map,
        nativeReduce = ArrayProto.reduce,
        nativeReduceRight = ArrayProto.reduceRight,
        nativeFilter = ArrayProto.filter,
        nativeEvery = ArrayProto.every,
        nativeSome = ArrayProto.some,
        nativeIndexOf = ArrayProto.indexOf,
        nativeLastIndexOf = ArrayProto.lastIndexOf,
        nativeIsArray = Array.isArray,
        nativeKeys = Object.keys,
        nativeBind = FuncProto.bind;

    // Create a safe reference to the Underscore object for use below.
    var _ = function (obj) {
        if (obj instanceof _) return obj;
        if (!(this instanceof _)) return new _(obj);
        this._wrapped = obj;
    };

    // Export the Underscore object for **Node.js**, with
    // backwards-compatibility for the old `require()` API. If we're in
    // the browser, add `_` as a global object via a string identifier,
    // for Closure Compiler "advanced" mode.
    if (typeof exports !== 'undefined') {
        if (typeof module !== 'undefined' && module.exports) {
            exports = module.exports = _;
        }
        exports._ = _;
    } else {
        root._ = _;
    }

    // Current version.
    _.VERSION = '1.5.2';

    // Collection Functions
    // --------------------

    // The cornerstone, an `each` implementation, aka `forEach`.
    // Handles objects with the built-in `forEach`, arrays, and raw objects.
    // Delegates to **ECMAScript 5**'s native `forEach` if available.
    var each = _.each = _.forEach = function (obj, iterator, context) {
        if (obj == null) return;
        if (nativeForEach && obj.forEach === nativeForEach) {
            obj.forEach(iterator, context);
        } else if (obj.length === +obj.length) {
            for (var i = 0, length = obj.length; i < length; i++) {
                if (iterator.call(context, obj[i], i, obj) === breaker) return;
            }
        } else {
            var keys = _.keys(obj);
            for (var i = 0, length = keys.length; i < length; i++) {
                if (iterator.call(context, obj[keys[i]], keys[i], obj) === breaker) return;
            }
        }
    };

    // Return the results of applying the iterator to each element.
    // Delegates to **ECMAScript 5**'s native `map` if available.
    _.map = _.collect = function (obj, iterator, context) {
        var results = [];
        if (obj == null) return results;
        if (nativeMap && obj.map === nativeMap) return obj.map(iterator, context);
        each(obj, function (value, index, list) {
            results.push(iterator.call(context, value, index, list));
        });
        return results;
    };

    var reduceError = 'Reduce of empty array with no initial value';

    // **Reduce** builds up a single result from a list of values, aka `inject`,
    // or `foldl`. Delegates to **ECMAScript 5**'s native `reduce` if available.
    _.reduce = _.foldl = _.inject = function (obj, iterator, memo, context) {
        var initial = arguments.length > 2;
        if (obj == null) obj = [];
        if (nativeReduce && obj.reduce === nativeReduce) {
            if (context) iterator = _.bind(iterator, context);
            return initial ? obj.reduce(iterator, memo) : obj.reduce(iterator);
        }
        each(obj, function (value, index, list) {
            if (!initial) {
                memo = value;
                initial = true;
            } else {
                memo = iterator.call(context, memo, value, index, list);
            }
        });
        if (!initial) throw new TypeError(reduceError);
        return memo;
    };

    // The right-associative version of reduce, also known as `foldr`.
    // Delegates to **ECMAScript 5**'s native `reduceRight` if available.
    _.reduceRight = _.foldr = function (obj, iterator, memo, context) {
        var initial = arguments.length > 2;
        if (obj == null) obj = [];
        if (nativeReduceRight && obj.reduceRight === nativeReduceRight) {
            if (context) iterator = _.bind(iterator, context);
            return initial ? obj.reduceRight(iterator, memo) : obj.reduceRight(iterator);
        }
        var length = obj.length;
        if (length !== +length) {
            var keys = _.keys(obj);
            length = keys.length;
        }
        each(obj, function (value, index, list) {
            index = keys ? keys[--length] : --length;
            if (!initial) {
                memo = obj[index];
                initial = true;
            } else {
                memo = iterator.call(context, memo, obj[index], index, list);
            }
        });
        if (!initial) throw new TypeError(reduceError);
        return memo;
    };

    // Return the first value which passes a truth test. Aliased as `detect`.
    _.find = _.detect = function (obj, iterator, context) {
        var result;
        any(obj, function (value, index, list) {
            if (iterator.call(context, value, index, list)) {
                result = value;
                return true;
            }
        });
        return result;
    };

    // Return all the elements that pass a truth test.
    // Delegates to **ECMAScript 5**'s native `filter` if available.
    // Aliased as `select`.
    _.filter = _.select = function (obj, iterator, context) {
        var results = [];
        if (obj == null) return results;
        if (nativeFilter && obj.filter === nativeFilter) return obj.filter(iterator, context);
        each(obj, function (value, index, list) {
            if (iterator.call(context, value, index, list)) results.push(value);
        });
        return results;
    };

    // Return all the elements for which a truth test fails.
    _.reject = function (obj, iterator, context) {
        return _.filter(obj, function (value, index, list) {
            return !iterator.call(context, value, index, list);
        }, context);
    };

    // Determine whether all of the elements match a truth test.
    // Delegates to **ECMAScript 5**'s native `every` if available.
    // Aliased as `all`.
    _.every = _.all = function (obj, iterator, context) {
        iterator || (iterator = _.identity);
        var result = true;
        if (obj == null) return result;
        if (nativeEvery && obj.every === nativeEvery) return obj.every(iterator, context);
        each(obj, function (value, index, list) {
            if (!(result = result && iterator.call(context, value, index, list))) return breaker;
        });
        return !!result;
    };

    // Determine if at least one element in the object matches a truth test.
    // Delegates to **ECMAScript 5**'s native `some` if available.
    // Aliased as `any`.
    var any = _.some = _.any = function (obj, iterator, context) {
        iterator || (iterator = _.identity);
        var result = false;
        if (obj == null) return result;
        if (nativeSome && obj.some === nativeSome) return obj.some(iterator, context);
        each(obj, function (value, index, list) {
            if (result || (result = iterator.call(context, value, index, list))) return breaker;
        });
        return !!result;
    };

    // Determine if the array or object contains a given value (using `===`).
    // Aliased as `include`.
    _.contains = _.include = function (obj, target) {
        if (obj == null) return false;
        if (nativeIndexOf && obj.indexOf === nativeIndexOf) return obj.indexOf(target) != -1;
        return any(obj, function (value) {
            return value === target;
        });
    };

    // Invoke a method (with arguments) on every item in a collection.
    _.invoke = function (obj, method) {
        var args = slice.call(arguments, 2);
        var isFunc = _.isFunction(method);
        return _.map(obj, function (value) {
            return (isFunc ? method : value[method]).apply(value, args);
        });
    };

    // Convenience version of a common use case of `map`: fetching a property.
    _.pluck = function (obj, key) {
        return _.map(obj, function (value) {
            return value[key];
        });
    };

    // Convenience version of a common use case of `filter`: selecting only objects
    // containing specific `key:value` pairs.
    _.where = function (obj, attrs, first) {
        if (_.isEmpty(attrs)) return first ? void 0 : [];
        return _[first ? 'find' : 'filter'](obj, function (value) {
            for (var key in attrs) {
                if (attrs[key] !== value[key]) return false;
            }
            return true;
        });
    };

    // Convenience version of a common use case of `find`: getting the first object
    // containing specific `key:value` pairs.
    _.findWhere = function (obj, attrs) {
        return _.where(obj, attrs, true);
    };

    // Return the maximum element or (element-based computation).
    // Can't optimize arrays of integers longer than 65,535 elements.
    // See [WebKit Bug 80797](https://bugs.webkit.org/show_bug.cgi?id=80797)
    _.max = function (obj, iterator, context) {
        if (!iterator && _.isArray(obj) && obj[0] === +obj[0] && obj.length < 65535) {
            return Math.max.apply(Math, obj);
        }
        if (!iterator && _.isEmpty(obj)) return -Infinity;
        var result = {computed: -Infinity, value: -Infinity};
        each(obj, function (value, index, list) {
            var computed = iterator ? iterator.call(context, value, index, list) : value;
            computed > result.computed && (result = {value: value, computed: computed});
        });
        return result.value;
    };

    // Return the minimum element (or element-based computation).
    _.min = function (obj, iterator, context) {
        if (!iterator && _.isArray(obj) && obj[0] === +obj[0] && obj.length < 65535) {
            return Math.min.apply(Math, obj);
        }
        if (!iterator && _.isEmpty(obj)) return Infinity;
        var result = {computed: Infinity, value: Infinity};
        each(obj, function (value, index, list) {
            var computed = iterator ? iterator.call(context, value, index, list) : value;
            computed < result.computed && (result = {value: value, computed: computed});
        });
        return result.value;
    };

    // Shuffle an array, using the modern version of the
    // [Fisher-Yates shuffle](http://en.wikipedia.org/wiki/Fisherâ€“Yates_shuffle).
    _.shuffle = function (obj) {
        var rand;
        var index = 0;
        var shuffled = [];
        each(obj, function (value) {
            rand = _.random(index++);
            shuffled[index - 1] = shuffled[rand];
            shuffled[rand] = value;
        });
        return shuffled;
    };

    // Sample **n** random values from an array.
    // If **n** is not specified, returns a single random element from the array.
    // The internal `guard` argument allows it to work with `map`.
    _.sample = function (obj, n, guard) {
        if (arguments.length < 2 || guard) {
            return obj[_.random(obj.length - 1)];
        }
        return _.shuffle(obj).slice(0, Math.max(0, n));
    };

    // An internal function to generate lookup iterators.
    var lookupIterator = function (value) {
        return _.isFunction(value) ? value : function (obj) {
            return obj[value];
        };
    };

    // Sort the object's values by a criterion produced by an iterator.
    _.sortBy = function (obj, value, context) {
        var iterator = lookupIterator(value);
        return _.pluck(_.map(obj,function (value, index, list) {
            return {
                value: value,
                index: index,
                criteria: iterator.call(context, value, index, list)
            };
        }).sort(function (left, right) {
                var a = left.criteria;
                var b = right.criteria;
                if (a !== b) {
                    if (a > b || a === void 0) return 1;
                    if (a < b || b === void 0) return -1;
                }
                return left.index - right.index;
            }), 'value');
    };

    // An internal function used for aggregate "group by" operations.
    var group = function (behavior) {
        return function (obj, value, context) {
            var result = {};
            var iterator = value == null ? _.identity : lookupIterator(value);
            each(obj, function (value, index) {
                var key = iterator.call(context, value, index, obj);
                behavior(result, key, value);
            });
            return result;
        };
    };

    // Groups the object's values by a criterion. Pass either a string attribute
    // to group by, or a function that returns the criterion.
    _.groupBy = group(function (result, key, value) {
        (_.has(result, key) ? result[key] : (result[key] = [])).push(value);
    });

    // Indexes the object's values by a criterion, similar to `groupBy`, but for
    // when you know that your index values will be unique.
    _.indexBy = group(function (result, key, value) {
        result[key] = value;
    });

    // Counts instances of an object that group by a certain criterion. Pass
    // either a string attribute to count by, or a function that returns the
    // criterion.
    _.countBy = group(function (result, key) {
        _.has(result, key) ? result[key]++ : result[key] = 1;
    });

    // Use a comparator function to figure out the smallest index at which
    // an object should be inserted so as to maintain order. Uses binary search.
    _.sortedIndex = function (array, obj, iterator, context) {
        iterator = iterator == null ? _.identity : lookupIterator(iterator);
        var value = iterator.call(context, obj);
        var low = 0, high = array.length;
        while (low < high) {
            var mid = (low + high) >>> 1;
            iterator.call(context, array[mid]) < value ? low = mid + 1 : high = mid;
        }
        return low;
    };

    // Safely create a real, live array from anything iterable.
    _.toArray = function (obj) {
        if (!obj) return [];
        if (_.isArray(obj)) return slice.call(obj);
        if (obj.length === +obj.length) return _.map(obj, _.identity);
        return _.values(obj);
    };

    // Return the number of elements in an object.
    _.size = function (obj) {
        if (obj == null) return 0;
        return (obj.length === +obj.length) ? obj.length : _.keys(obj).length;
    };

    // Array Functions
    // ---------------

    // Get the first element of an array. Passing **n** will return the first N
    // values in the array. Aliased as `head` and `take`. The **guard** check
    // allows it to work with `_.map`.
    _.first = _.head = _.take = function (array, n, guard) {
        if (array == null) return void 0;
        return (n == null) || guard ? array[0] : slice.call(array, 0, n);
    };

    // Returns everything but the last entry of the array. Especially useful on
    // the arguments object. Passing **n** will return all the values in
    // the array, excluding the last N. The **guard** check allows it to work with
    // `_.map`.
    _.initial = function (array, n, guard) {
        return slice.call(array, 0, array.length - ((n == null) || guard ? 1 : n));
    };

    // Get the last element of an array. Passing **n** will return the last N
    // values in the array. The **guard** check allows it to work with `_.map`.
    _.last = function (array, n, guard) {
        if (array == null) return void 0;
        if ((n == null) || guard) {
            return array[array.length - 1];
        } else {
            return slice.call(array, Math.max(array.length - n, 0));
        }
    };

    // Returns everything but the first entry of the array. Aliased as `tail` and `drop`.
    // Especially useful on the arguments object. Passing an **n** will return
    // the rest N values in the array. The **guard**
    // check allows it to work with `_.map`.
    _.rest = _.tail = _.drop = function (array, n, guard) {
        return slice.call(array, (n == null) || guard ? 1 : n);
    };

    // Trim out all falsy values from an array.
    _.compact = function (array) {
        return _.filter(array, _.identity);
    };

    // Internal implementation of a recursive `flatten` function.
    var flatten = function (input, shallow, output) {
        if (shallow && _.every(input, _.isArray)) {
            return concat.apply(output, input);
        }
        each(input, function (value) {
            if (_.isArray(value) || _.isArguments(value)) {
                shallow ? push.apply(output, value) : flatten(value, shallow, output);
            } else {
                output.push(value);
            }
        });
        return output;
    };

    // Flatten out an array, either recursively (by default), or just one level.
    _.flatten = function (array, shallow) {
        return flatten(array, shallow, []);
    };

    // Return a version of the array that does not contain the specified value(s).
    _.without = function (array) {
        return _.difference(array, slice.call(arguments, 1));
    };

    // Produce a duplicate-free version of the array. If the array has already
    // been sorted, you have the option of using a faster algorithm.
    // Aliased as `unique`.
    _.uniq = _.unique = function (array, isSorted, iterator, context) {
        if (_.isFunction(isSorted)) {
            context = iterator;
            iterator = isSorted;
            isSorted = false;
        }
        var initial = iterator ? _.map(array, iterator, context) : array;
        var results = [];
        var seen = [];
        each(initial, function (value, index) {
            if (isSorted ? (!index || seen[seen.length - 1] !== value) : !_.contains(seen, value)) {
                seen.push(value);
                results.push(array[index]);
            }
        });
        return results;
    };

    // Produce an array that contains the union: each distinct element from all of
    // the passed-in arrays.
    _.union = function () {
        return _.uniq(_.flatten(arguments, true));
    };

    // Produce an array that contains every item shared between all the
    // passed-in arrays.
    _.intersection = function (array) {
        var rest = slice.call(arguments, 1);
        return _.filter(_.uniq(array), function (item) {
            return _.every(rest, function (other) {
                return _.indexOf(other, item) >= 0;
            });
        });
    };

    // Take the difference between one array and a number of other arrays.
    // Only the elements present in just the first array will remain.
    _.difference = function (array) {
        var rest = concat.apply(ArrayProto, slice.call(arguments, 1));
        return _.filter(array, function (value) {
            return !_.contains(rest, value);
        });
    };

    // Zip together multiple lists into a single array -- elements that share
    // an index go together.
    _.zip = function () {
        var length = _.max(_.pluck(arguments, "length").concat(0));
        var results = new Array(length);
        for (var i = 0; i < length; i++) {
            results[i] = _.pluck(arguments, '' + i);
        }
        return results;
    };

    // Converts lists into objects. Pass either a single array of `[key, value]`
    // pairs, or two parallel arrays of the same length -- one of keys, and one of
    // the corresponding values.
    _.object = function (list, values) {
        if (list == null) return {};
        var result = {};
        for (var i = 0, length = list.length; i < length; i++) {
            if (values) {
                result[list[i]] = values[i];
            } else {
                result[list[i][0]] = list[i][1];
            }
        }
        return result;
    };

    // If the browser doesn't supply us with indexOf (I'm looking at you, **MSIE**),
    // we need this function. Return the position of the first occurrence of an
    // item in an array, or -1 if the item is not included in the array.
    // Delegates to **ECMAScript 5**'s native `indexOf` if available.
    // If the array is large and already in sort order, pass `true`
    // for **isSorted** to use binary search.
    _.indexOf = function (array, item, isSorted) {
        if (array == null) return -1;
        var i = 0, length = array.length;
        if (isSorted) {
            if (typeof isSorted == 'number') {
                i = (isSorted < 0 ? Math.max(0, length + isSorted) : isSorted);
            } else {
                i = _.sortedIndex(array, item);
                return array[i] === item ? i : -1;
            }
        }
        if (nativeIndexOf && array.indexOf === nativeIndexOf) return array.indexOf(item, isSorted);
        for (; i < length; i++) if (array[i] === item) return i;
        return -1;
    };

    // Delegates to **ECMAScript 5**'s native `lastIndexOf` if available.
    _.lastIndexOf = function (array, item, from) {
        if (array == null) return -1;
        var hasIndex = from != null;
        if (nativeLastIndexOf && array.lastIndexOf === nativeLastIndexOf) {
            return hasIndex ? array.lastIndexOf(item, from) : array.lastIndexOf(item);
        }
        var i = (hasIndex ? from : array.length);
        while (i--) if (array[i] === item) return i;
        return -1;
    };

    // Generate an integer Array containing an arithmetic progression. A port of
    // the native Python `range()` function. See
    // [the Python documentation](http://docs.python.org/library/functions.html#range).
    _.range = function (start, stop, step) {
        if (arguments.length <= 1) {
            stop = start || 0;
            start = 0;
        }
        step = arguments[2] || 1;

        var length = Math.max(Math.ceil((stop - start) / step), 0);
        var idx = 0;
        var range = new Array(length);

        while (idx < length) {
            range[idx++] = start;
            start += step;
        }

        return range;
    };

    // Function (ahem) Functions
    // ------------------

    // Reusable constructor function for prototype setting.
    var ctor = function () {
    };

    // Create a function bound to a given object (assigning `this`, and arguments,
    // optionally). Delegates to **ECMAScript 5**'s native `Function.bind` if
    // available.
    _.bind = function (func, context) {
        var args, bound;
        if (nativeBind && func.bind === nativeBind) return nativeBind.apply(func, slice.call(arguments, 1));
        if (!_.isFunction(func)) throw new TypeError;
        args = slice.call(arguments, 2);
        return bound = function () {
            if (!(this instanceof bound)) return func.apply(context, args.concat(slice.call(arguments)));
            ctor.prototype = func.prototype;
            var self = new ctor;
            ctor.prototype = null;
            var result = func.apply(self, args.concat(slice.call(arguments)));
            if (Object(result) === result) return result;
            return self;
        };
    };

    // Partially apply a function by creating a version that has had some of its
    // arguments pre-filled, without changing its dynamic `this` context.
    _.partial = function (func) {
        var args = slice.call(arguments, 1);
        return function () {
            return func.apply(this, args.concat(slice.call(arguments)));
        };
    };

    // Bind all of an object's methods to that object. Useful for ensuring that
    // all callbacks defined on an object belong to it.
    _.bindAll = function (obj) {
        var funcs = slice.call(arguments, 1);
        if (funcs.length === 0) throw new Error("bindAll must be passed function names");
        each(funcs, function (f) {
            obj[f] = _.bind(obj[f], obj);
        });
        return obj;
    };

    // Memoize an expensive function by storing its results.
    _.memoize = function (func, hasher) {
        var memo = {};
        hasher || (hasher = _.identity);
        return function () {
            var key = hasher.apply(this, arguments);
            return _.has(memo, key) ? memo[key] : (memo[key] = func.apply(this, arguments));
        };
    };

    // Delays a function for the given number of milliseconds, and then calls
    // it with the arguments supplied.
    _.delay = function (func, wait) {
        var args = slice.call(arguments, 2);
        return setTimeout(function () {
            return func.apply(null, args);
        }, wait);
    };

    // Defers a function, scheduling it to run after the current call stack has
    // cleared.
    _.defer = function (func) {
        return _.delay.apply(_, [func, 1].concat(slice.call(arguments, 1)));
    };

    // Returns a function, that, when invoked, will only be triggered at most once
    // during a given window of time. Normally, the throttled function will run
    // as much as it can, without ever going more than once per `wait` duration;
    // but if you'd like to disable the execution on the leading edge, pass
    // `{leading: false}`. To disable execution on the trailing edge, ditto.
    _.throttle = function (func, wait, options) {
        var context, args, result;
        var timeout = null;
        var previous = 0;
        options || (options = {});
        var later = function () {
            previous = options.leading === false ? 0 : new Date;
            timeout = null;
            result = func.apply(context, args);
        };
        return function () {
            var now = new Date;
            if (!previous && options.leading === false) previous = now;
            var remaining = wait - (now - previous);
            context = this;
            args = arguments;
            if (remaining <= 0) {
                clearTimeout(timeout);
                timeout = null;
                previous = now;
                result = func.apply(context, args);
            } else if (!timeout && options.trailing !== false) {
                timeout = setTimeout(later, remaining);
            }
            return result;
        };
    };

    // Returns a function, that, as long as it continues to be invoked, will not
    // be triggered. The function will be called after it stops being called for
    // N milliseconds. If `immediate` is passed, trigger the function on the
    // leading edge, instead of the trailing.
    _.debounce = function (func, wait, immediate) {
        var timeout, args, context, timestamp, result;
        return function () {
            context = this;
            args = arguments;
            timestamp = new Date();
            var later = function () {
                var last = (new Date()) - timestamp;
                if (last < wait) {
                    timeout = setTimeout(later, wait - last);
                } else {
                    timeout = null;
                    if (!immediate) result = func.apply(context, args);
                }
            };
            var callNow = immediate && !timeout;
            if (!timeout) {
                timeout = setTimeout(later, wait);
            }
            if (callNow) result = func.apply(context, args);
            return result;
        };
    };

    // Returns a function that will be executed at most one time, no matter how
    // often you call it. Useful for lazy initialization.
    _.once = function (func) {
        var ran = false, memo;
        return function () {
            if (ran) return memo;
            ran = true;
            memo = func.apply(this, arguments);
            func = null;
            return memo;
        };
    };

    // Returns the first function passed as an argument to the second,
    // allowing you to adjust arguments, run code before and after, and
    // conditionally execute the original function.
    _.wrap = function (func, wrapper) {
        return function () {
            var args = [func];
            push.apply(args, arguments);
            return wrapper.apply(this, args);
        };
    };

    // Returns a function that is the composition of a list of functions, each
    // consuming the return value of the function that follows.
    _.compose = function () {
        var funcs = arguments;
        return function () {
            var args = arguments;
            for (var i = funcs.length - 1; i >= 0; i--) {
                args = [funcs[i].apply(this, args)];
            }
            return args[0];
        };
    };

    // Returns a function that will only be executed after being called N times.
    _.after = function (times, func) {
        return function () {
            if (--times < 1) {
                return func.apply(this, arguments);
            }
        };
    };

    // Object Functions
    // ----------------

    // Retrieve the names of an object's properties.
    // Delegates to **ECMAScript 5**'s native `Object.keys`
    _.keys = nativeKeys || function (obj) {
        if (obj !== Object(obj)) throw new TypeError('Invalid object');
        var keys = [];
        for (var key in obj) if (_.has(obj, key)) keys.push(key);
        return keys;
    };

    // Retrieve the values of an object's properties.
    _.values = function (obj) {
        var keys = _.keys(obj);
        var length = keys.length;
        var values = new Array(length);
        for (var i = 0; i < length; i++) {
            values[i] = obj[keys[i]];
        }
        return values;
    };

    // Convert an object into a list of `[key, value]` pairs.
    _.pairs = function (obj) {
        var keys = _.keys(obj);
        var length = keys.length;
        var pairs = new Array(length);
        for (var i = 0; i < length; i++) {
            pairs[i] = [keys[i], obj[keys[i]]];
        }
        return pairs;
    };

    // Invert the keys and values of an object. The values must be serializable.
    _.invert = function (obj) {
        var result = {};
        var keys = _.keys(obj);
        for (var i = 0, length = keys.length; i < length; i++) {
            result[obj[keys[i]]] = keys[i];
        }
        return result;
    };

    // Return a sorted list of the function names available on the object.
    // Aliased as `methods`
    _.functions = _.methods = function (obj) {
        var names = [];
        for (var key in obj) {
            if (_.isFunction(obj[key])) names.push(key);
        }
        return names.sort();
    };

    // Extend a given object with all the properties in passed-in object(s).
    _.extend = function (obj) {
        each(slice.call(arguments, 1), function (source) {
            if (source) {
                for (var prop in source) {
                    obj[prop] = source[prop];
                }
            }
        });
        return obj;
    };

    // Return a copy of the object only containing the whitelisted properties.
    _.pick = function (obj) {
        var copy = {};
        var keys = concat.apply(ArrayProto, slice.call(arguments, 1));
        each(keys, function (key) {
            if (key in obj) copy[key] = obj[key];
        });
        return copy;
    };

    // Return a copy of the object without the blacklisted properties.
    _.omit = function (obj) {
        var copy = {};
        var keys = concat.apply(ArrayProto, slice.call(arguments, 1));
        for (var key in obj) {
            if (!_.contains(keys, key)) copy[key] = obj[key];
        }
        return copy;
    };

    // Fill in a given object with default properties.
    _.defaults = function (obj) {
        each(slice.call(arguments, 1), function (source) {
            if (source) {
                for (var prop in source) {
                    if (obj[prop] === void 0) obj[prop] = source[prop];
                }
            }
        });
        return obj;
    };

    // Create a (shallow-cloned) duplicate of an object.
    _.clone = function (obj) {
        if (!_.isObject(obj)) return obj;
        return _.isArray(obj) ? obj.slice() : _.extend({}, obj);
    };

    // Invokes interceptor with the obj, and then returns obj.
    // The primary purpose of this method is to "tap into" a method chain, in
    // order to perform operations on intermediate results within the chain.
    _.tap = function (obj, interceptor) {
        interceptor(obj);
        return obj;
    };

    // Internal recursive comparison function for `isEqual`.
    var eq = function (a, b, aStack, bStack) {
        // Identical objects are equal. `0 === -0`, but they aren't identical.
        // See the [Harmony `egal` proposal](http://wiki.ecmascript.org/doku.php?id=harmony:egal).
        if (a === b) return a !== 0 || 1 / a == 1 / b;
        // A strict comparison is necessary because `null == undefined`.
        if (a == null || b == null) return a === b;
        // Unwrap any wrapped objects.
        if (a instanceof _) a = a._wrapped;
        if (b instanceof _) b = b._wrapped;
        // Compare `[[Class]]` names.
        var className = toString.call(a);
        if (className != toString.call(b)) return false;
        switch (className) {
            // Strings, numbers, dates, and booleans are compared by value.
            case '[object String]':
                // Primitives and their corresponding object wrappers are equivalent; thus, `"5"` is
                // equivalent to `new String("5")`.
                return a == String(b);
            case '[object Number]':
                // `NaN`s are equivalent, but non-reflexive. An `egal` comparison is performed for
                // other numeric values.
                return a != +a ? b != +b : (a == 0 ? 1 / a == 1 / b : a == +b);
            case '[object Date]':
            case '[object Boolean]':
                // Coerce dates and booleans to numeric primitive values. Dates are compared by their
                // millisecond representations. Note that invalid dates with millisecond representations
                // of `NaN` are not equivalent.
                return +a == +b;
            // RegExps are compared by their source patterns and flags.
            case '[object RegExp]':
                return a.source == b.source &&
                    a.global == b.global &&
                    a.multiline == b.multiline &&
                    a.ignoreCase == b.ignoreCase;
        }
        if (typeof a != 'object' || typeof b != 'object') return false;
        // Assume equality for cyclic structures. The algorithm for detecting cyclic
        // structures is adapted from ES 5.1 section 15.12.3, abstract operation `JO`.
        var length = aStack.length;
        while (length--) {
            // Linear search. Performance is inversely proportional to the number of
            // unique nested structures.
            if (aStack[length] == a) return bStack[length] == b;
        }
        // Objects with different constructors are not equivalent, but `Object`s
        // from different frames are.
        var aCtor = a.constructor, bCtor = b.constructor;
        if (aCtor !== bCtor && !(_.isFunction(aCtor) && (aCtor instanceof aCtor) &&
            _.isFunction(bCtor) && (bCtor instanceof bCtor))) {
            return false;
        }
        // Add the first object to the stack of traversed objects.
        aStack.push(a);
        bStack.push(b);
        var size = 0, result = true;
        // Recursively compare objects and arrays.
        if (className == '[object Array]') {
            // Compare array lengths to determine if a deep comparison is necessary.
            size = a.length;
            result = size == b.length;
            if (result) {
                // Deep compare the contents, ignoring non-numeric properties.
                while (size--) {
                    if (!(result = eq(a[size], b[size], aStack, bStack))) break;
                }
            }
        } else {
            // Deep compare objects.
            for (var key in a) {
                if (_.has(a, key)) {
                    // Count the expected number of properties.
                    size++;
                    // Deep compare each member.
                    if (!(result = _.has(b, key) && eq(a[key], b[key], aStack, bStack))) break;
                }
            }
            // Ensure that both objects contain the same number of properties.
            if (result) {
                for (key in b) {
                    if (_.has(b, key) && !(size--)) break;
                }
                result = !size;
            }
        }
        // Remove the first object from the stack of traversed objects.
        aStack.pop();
        bStack.pop();
        return result;
    };

    // Perform a deep comparison to check if two objects are equal.
    _.isEqual = function (a, b) {
        return eq(a, b, [], []);
    };

    // Is a given array, string, or object empty?
    // An "empty" object has no enumerable own-properties.
    _.isEmpty = function (obj) {
        if (obj == null) return true;
        if (_.isArray(obj) || _.isString(obj)) return obj.length === 0;
        for (var key in obj) if (_.has(obj, key)) return false;
        return true;
    };

    // Is a given value a DOM element?
    _.isElement = function (obj) {
        return !!(obj && obj.nodeType === 1);
    };

    // Is a given value an array?
    // Delegates to ECMA5's native Array.isArray
    _.isArray = nativeIsArray || function (obj) {
        return toString.call(obj) == '[object Array]';
    };

    // Is a given variable an object?
    _.isObject = function (obj) {
        return obj === Object(obj);
    };

    // Add some isType methods: isArguments, isFunction, isString, isNumber, isDate, isRegExp.
    each(['Arguments', 'Function', 'String', 'Number', 'Date', 'RegExp'], function (name) {
        _['is' + name] = function (obj) {
            return toString.call(obj) == '[object ' + name + ']';
        };
    });

    // Define a fallback version of the method in browsers (ahem, IE), where
    // there isn't any inspectable "Arguments" type.
    if (!_.isArguments(arguments)) {
        _.isArguments = function (obj) {
            return !!(obj && _.has(obj, 'callee'));
        };
    }

    // Optimize `isFunction` if appropriate.
    if (typeof (/./) !== 'function') {
        _.isFunction = function (obj) {
            return typeof obj === 'function';
        };
    }

    // Is a given object a finite number?
    _.isFinite = function (obj) {
        return isFinite(obj) && !isNaN(parseFloat(obj));
    };

    // Is the given value `NaN`? (NaN is the only number which does not equal itself).
    _.isNaN = function (obj) {
        return _.isNumber(obj) && obj != +obj;
    };

    // Is a given value a boolean?
    _.isBoolean = function (obj) {
        return obj === true || obj === false || toString.call(obj) == '[object Boolean]';
    };

    // Is a given value equal to null?
    _.isNull = function (obj) {
        return obj === null;
    };

    // Is a given variable undefined?
    _.isUndefined = function (obj) {
        return obj === void 0;
    };

    // Shortcut function for checking if an object has a given property directly
    // on itself (in other words, not on a prototype).
    _.has = function (obj, key) {
        return hasOwnProperty.call(obj, key);
    };

    // Utility Functions
    // -----------------

    // Run Underscore.js in *noConflict* mode, returning the `_` variable to its
    // previous owner. Returns a reference to the Underscore object.
    _.noConflict = function () {
        root._ = previousUnderscore;
        return this;
    };

    // Keep the identity function around for default iterators.
    _.identity = function (value) {
        return value;
    };

    // Run a function **n** times.
    _.times = function (n, iterator, context) {
        var accum = Array(Math.max(0, n));
        for (var i = 0; i < n; i++) accum[i] = iterator.call(context, i);
        return accum;
    };

    // Return a random integer between min and max (inclusive).
    _.random = function (min, max) {
        if (max == null) {
            max = min;
            min = 0;
        }
        return min + Math.floor(Math.random() * (max - min + 1));
    };

    // List of HTML entities for escaping.
    var entityMap = {
        escape: {
            '&': '&amp;',
            '<': '&lt;',
            '>': '&gt;',
            '"': '&quot;',
            "'": '&#x27;'
        }
    };
    entityMap.unescape = _.invert(entityMap.escape);

    // Regexes containing the keys and values listed immediately above.
    var entityRegexes = {
        escape: new RegExp('[' + _.keys(entityMap.escape).join('') + ']', 'g'),
        unescape: new RegExp('(' + _.keys(entityMap.unescape).join('|') + ')', 'g')
    };

    // Functions for escaping and unescaping strings to/from HTML interpolation.
    _.each(['escape', 'unescape'], function (method) {
        _[method] = function (string) {
            if (string == null) return '';
            return ('' + string).replace(entityRegexes[method], function (match) {
                return entityMap[method][match];
            });
        };
    });

    // If the value of the named `property` is a function then invoke it with the
    // `object` as context; otherwise, return it.
    _.result = function (object, property) {
        if (object == null) return void 0;
        var value = object[property];
        return _.isFunction(value) ? value.call(object) : value;
    };

    // Add your own custom functions to the Underscore object.
    _.mixin = function (obj) {
        each(_.functions(obj), function (name) {
            var func = _[name] = obj[name];
            _.prototype[name] = function () {
                var args = [this._wrapped];
                push.apply(args, arguments);
                return result.call(this, func.apply(_, args));
            };
        });
    };

    // Generate a unique integer id (unique within the entire client session).
    // Useful for temporary DOM ids.
    var idCounter = 0;
    _.uniqueId = function (prefix) {
        var id = ++idCounter + '';
        return prefix ? prefix + id : id;
    };

    // By default, Underscore uses ERB-style template delimiters, change the
    // following template settings to use alternative delimiters.
    _.templateSettings = {
        evaluate: /<%([\s\S]+?)%>/g,
        interpolate: /<%=([\s\S]+?)%>/g,
        escape: /<%-([\s\S]+?)%>/g
    };

    // When customizing `templateSettings`, if you don't want to define an
    // interpolation, evaluation or escaping regex, we need one that is
    // guaranteed not to match.
    var noMatch = /(.)^/;

    // Certain characters need to be escaped so that they can be put into a
    // string literal.
    var escapes = {
        "'": "'",
        '\\': '\\',
        '\r': 'r',
        '\n': 'n',
        '\t': 't',
        '\u2028': 'u2028',
        '\u2029': 'u2029'
    };

    var escaper = /\\|'|\r|\n|\t|\u2028|\u2029/g;

    // JavaScript micro-templating, similar to John Resig's implementation.
    // Underscore templating handles arbitrary delimiters, preserves whitespace,
    // and correctly escapes quotes within interpolated code.
    _.template = function (text, data, settings) {
        var render;
        settings = _.defaults({}, settings, _.templateSettings);

        // Combine delimiters into one regular expression via alternation.
        var matcher = new RegExp([
            (settings.escape || noMatch).source,
            (settings.interpolate || noMatch).source,
            (settings.evaluate || noMatch).source
        ].join('|') + '|$', 'g');

        // Compile the template source, escaping string literals appropriately.
        var index = 0;
        var source = "__p+='";
        text.replace(matcher, function (match, escape, interpolate, evaluate, offset) {
            source += text.slice(index, offset)
                .replace(escaper, function (match) {
                    return '\\' + escapes[match];
                });

            if (escape) {
                source += "'+\n((__t=(" + escape + "))==null?'':_.escape(__t))+\n'";
            }
            if (interpolate) {
                source += "'+\n((__t=(" + interpolate + "))==null?'':__t)+\n'";
            }
            if (evaluate) {
                source += "';\n" + evaluate + "\n__p+='";
            }
            index = offset + match.length;
            return match;
        });
        source += "';\n";

        // If a variable is not specified, place data values in local scope.
        if (!settings.variable) source = 'with(obj||{}){\n' + source + '}\n';

        source = "var __t,__p='',__j=Array.prototype.join," +
            "print=function(){__p+=__j.call(arguments,'');};\n" +
            source + "return __p;\n";

        try {
            render = new Function(settings.variable || 'obj', '_', source);
        } catch (e) {
            e.source = source;
            throw e;
        }

        if (data) return render(data, _);
        var template = function (data) {
            return render.call(this, data, _);
        };

        // Provide the compiled function source as a convenience for precompilation.
        template.source = 'function(' + (settings.variable || 'obj') + '){\n' + source + '}';

        return template;
    };

    // Add a "chain" function, which will delegate to the wrapper.
    _.chain = function (obj) {
        return _(obj).chain();
    };

    // OOP
    // ---------------
    // If Underscore is called as a function, it returns a wrapped object that
    // can be used OO-style. This wrapper holds altered versions of all the
    // underscore functions. Wrapped objects may be chained.

    // Helper function to continue chaining intermediate results.
    var result = function (obj) {
        return this._chain ? _(obj).chain() : obj;
    };

    // Add all of the Underscore functions to the wrapper object.
    _.mixin(_);

    // Add all mutator Array functions to the wrapper.
    each(['pop', 'push', 'reverse', 'shift', 'sort', 'splice', 'unshift'], function (name) {
        var method = ArrayProto[name];
        _.prototype[name] = function () {
            var obj = this._wrapped;
            method.apply(obj, arguments);
            if ((name == 'shift' || name == 'splice') && obj.length === 0) delete obj[0];
            return result.call(this, obj);
        };
    });

    // Add all accessor Array functions to the wrapper.
    each(['concat', 'join', 'slice'], function (name) {
        var method = ArrayProto[name];
        _.prototype[name] = function () {
            return result.call(this, method.apply(this._wrapped, arguments));
        };
    });

    _.extend(_.prototype, {

        // Start chaining a wrapped Underscore object.
        chain: function () {
            this._chain = true;
            return this;
        },

        // Extracts the result from a wrapped and chained object.
        value: function () {
            return this._wrapped;
        }

    });

}).call(this);

 /* ************************ new file ************************ */
//     Backbone.js 1.1.0

//     (c) 2010-2011 Jeremy Ashkenas, DocumentCloud Inc.
//     (c) 2011-2013 Jeremy Ashkenas, DocumentCloud and Investigative Reporters & Editors
//     Backbone may be freely distributed under the MIT license.
//     For all details and documentation:
//     http://backbonejs.org

(function () {

    // Initial Setup
    // -------------

    // Save a reference to the global object (`window` in the browser, `exports`
    // on the server).
    var root = this;

    // Save the previous value of the `Backbone` variable, so that it can be
    // restored later on, if `noConflict` is used.
    var previousBackbone = root.Backbone;

    // Create local references to array methods we'll want to use later.
    var array = [];
    var push = array.push;
    var slice = array.slice;
    var splice = array.splice;

    // The top-level namespace. All public Backbone classes and modules will
    // be attached to this. Exported for both the browser and the server.
    var Backbone;
    if (typeof exports !== 'undefined') {
        Backbone = exports;
    } else {
        Backbone = root.Backbone = {};
    }

    // Current version of the library. Keep in sync with `package.json`.
    Backbone.VERSION = '1.1.0';

    // Require Underscore, if we're on the server, and it's not already present.
    var _ = root._;
    if (!_ && (typeof require !== 'undefined')) _ = require('underscore');

    // For Backbone's purposes, jQuery, Zepto, Ender, or My Library (kidding) owns
    // the `$` variable.
    Backbone.$ = root.jQuery || root.Zepto || root.ender || root.$;

    // Runs Backbone.js in *noConflict* mode, returning the `Backbone` variable
    // to its previous owner. Returns a reference to this Backbone object.
    Backbone.noConflict = function () {
        root.Backbone = previousBackbone;
        return this;
    };

    // Turn on `emulateHTTP` to support legacy HTTP servers. Setting this option
    // will fake `"PATCH"`, `"PUT"` and `"DELETE"` requests via the `_method` parameter and
    // set a `X-Http-Method-Override` header.
    Backbone.emulateHTTP = false;

    // Turn on `emulateJSON` to support legacy servers that can't deal with direct
    // `application/json` requests ... will encode the body as
    // `application/x-www-form-urlencoded` instead and will send the model in a
    // form param named `model`.
    Backbone.emulateJSON = false;

    // Backbone.Events
    // ---------------

    // A module that can be mixed in to *any object* in order to provide it with
    // custom events. You may bind with `on` or remove with `off` callback
    // functions to an event; `trigger`-ing an event fires all callbacks in
    // succession.
    //
    //     var object = {};
    //     _.extend(object, Backbone.Events);
    //     object.on('expand', function(){ alert('expanded'); });
    //     object.trigger('expand');
    //
    var Events = Backbone.Events = {

        // Bind an event to a `callback` function. Passing `"all"` will bind
        // the callback to all events fired.
        on: function (name, callback, context) {
            if (!eventsApi(this, 'on', name, [callback, context]) || !callback) return this;
            this._events || (this._events = {});
            var events = this._events[name] || (this._events[name] = []);
            events.push({callback: callback, context: context, ctx: context || this});
            return this;
        },

        // Bind an event to only be triggered a single time. After the first time
        // the callback is invoked, it will be removed.
        once: function (name, callback, context) {
            if (!eventsApi(this, 'once', name, [callback, context]) || !callback) return this;
            var self = this;
            var once = _.once(function () {
                self.off(name, once);
                callback.apply(this, arguments);
            });
            once._callback = callback;
            return this.on(name, once, context);
        },

        // Remove one or many callbacks. If `context` is null, removes all
        // callbacks with that function. If `callback` is null, removes all
        // callbacks for the event. If `name` is null, removes all bound
        // callbacks for all events.
        off: function (name, callback, context) {
            var retain, ev, events, names, i, l, j, k;
            if (!this._events || !eventsApi(this, 'off', name, [callback, context])) return this;
            if (!name && !callback && !context) {
                this._events = {};
                return this;
            }
            names = name ? [name] : _.keys(this._events);
            for (i = 0, l = names.length; i < l; i++) {
                name = names[i];
                if (events = this._events[name]) {
                    this._events[name] = retain = [];
                    if (callback || context) {
                        for (j = 0, k = events.length; j < k; j++) {
                            ev = events[j];
                            if ((callback && callback !== ev.callback && callback !== ev.callback._callback) ||
                                (context && context !== ev.context)) {
                                retain.push(ev);
                            }
                        }
                    }
                    if (!retain.length) delete this._events[name];
                }
            }

            return this;
        },

        // Trigger one or many events, firing all bound callbacks. Callbacks are
        // passed the same arguments as `trigger` is, apart from the event name
        // (unless you're listening on `"all"`, which will cause your callback to
        // receive the true name of the event as the first argument).
        trigger: function (name) {
            if (!this._events) return this;
            var args = slice.call(arguments, 1);
            if (!eventsApi(this, 'trigger', name, args)) return this;
            var events = this._events[name];
            var allEvents = this._events.all;
            if (events) triggerEvents(events, args);
            if (allEvents) triggerEvents(allEvents, arguments);
            return this;
        },

        // Tell this object to stop listening to either specific events ... or
        // to every object it's currently listening to.
        stopListening: function (obj, name, callback) {
            var listeningTo = this._listeningTo;
            if (!listeningTo) return this;
            var remove = !name && !callback;
            if (!callback && typeof name === 'object') callback = this;
            if (obj) (listeningTo = {})[obj._listenId] = obj;
            for (var id in listeningTo) {
                obj = listeningTo[id];
                obj.off(name, callback, this);
                if (remove || _.isEmpty(obj._events)) delete this._listeningTo[id];
            }
            return this;
        }

    };

    // Regular expression used to split event strings.
    var eventSplitter = /\s+/;

    // Implement fancy features of the Events API such as multiple event
    // names `"change blur"` and jQuery-style event maps `{change: action}`
    // in terms of the existing API.
    var eventsApi = function (obj, action, name, rest) {
        if (!name) return true;

        // Handle event maps.
        if (typeof name === 'object') {
            for (var key in name) {
                obj[action].apply(obj, [key, name[key]].concat(rest));
            }
            return false;
        }

        // Handle space separated event names.
        if (eventSplitter.test(name)) {
            var names = name.split(eventSplitter);
            for (var i = 0, l = names.length; i < l; i++) {
                obj[action].apply(obj, [names[i]].concat(rest));
            }
            return false;
        }

        return true;
    };

    // A difficult-to-believe, but optimized internal dispatch function for
    // triggering events. Tries to keep the usual cases speedy (most internal
    // Backbone events have 3 arguments).
    var triggerEvents = function (events, args) {
        var ev, i = -1, l = events.length, a1 = args[0], a2 = args[1], a3 = args[2];
        switch (args.length) {
            case 0:
                while (++i < l) (ev = events[i]).callback.call(ev.ctx);
                return;
            case 1:
                while (++i < l) (ev = events[i]).callback.call(ev.ctx, a1);
                return;
            case 2:
                while (++i < l) (ev = events[i]).callback.call(ev.ctx, a1, a2);
                return;
            case 3:
                while (++i < l) (ev = events[i]).callback.call(ev.ctx, a1, a2, a3);
                return;
            default:
                while (++i < l) (ev = events[i]).callback.apply(ev.ctx, args);
        }
    };

    var listenMethods = {listenTo: 'on', listenToOnce: 'once'};

    // Inversion-of-control versions of `on` and `once`. Tell *this* object to
    // listen to an event in another object ... keeping track of what it's
    // listening to.
    _.each(listenMethods, function (implementation, method) {
        Events[method] = function (obj, name, callback) {
            var listeningTo = this._listeningTo || (this._listeningTo = {});
            var id = obj._listenId || (obj._listenId = _.uniqueId('l'));
            listeningTo[id] = obj;
            if (!callback && typeof name === 'object') callback = this;
            obj[implementation](name, callback, this);
            return this;
        };
    });

    // Aliases for backwards compatibility.
    Events.bind = Events.on;
    Events.unbind = Events.off;

    // Allow the `Backbone` object to serve as a global event bus, for folks who
    // want global "pubsub" in a convenient place.
    _.extend(Backbone, Events);

    // Backbone.Model
    // --------------

    // Backbone **Models** are the basic data object in the framework --
    // frequently representing a row in a table in a database on your server.
    // A discrete chunk of data and a bunch of useful, related methods for
    // performing computations and transformations on that data.

    // Create a new model with the specified attributes. A client id (`cid`)
    // is automatically generated and assigned for you.
    var Model = Backbone.Model = function (attributes, options) {
        var attrs = attributes || {};
        options || (options = {});
        this.cid = _.uniqueId('c');
        this.attributes = {};
        if (options.collection) this.collection = options.collection;
        if (options.parse) attrs = this.parse(attrs, options) || {};
        attrs = _.defaults({}, attrs, _.result(this, 'defaults'));
        this.set(attrs, options);
        this.changed = {};
        this.initialize.apply(this, arguments);
    };

    // Attach all inheritable methods to the Model prototype.
    _.extend(Model.prototype, Events, {

        // A hash of attributes whose current and previous value differ.
        changed: null,

        // The value returned during the last failed validation.
        validationError: null,

        // The default name for the JSON `id` attribute is `"id"`. MongoDB and
        // CouchDB users may want to set this to `"_id"`.
        idAttribute: 'id',

        // Initialize is an empty function by default. Override it with your own
        // initialization logic.
        initialize: function () {
        },

        // Return a copy of the model's `attributes` object.
        toJSON: function (options) {
            return _.clone(this.attributes);
        },

        // Proxy `Backbone.sync` by default -- but override this if you need
        // custom syncing semantics for *this* particular model.
        sync: function () {
            return Backbone.sync.apply(this, arguments);
        },

        // Get the value of an attribute.
        get: function (attr) {
            return this.attributes[attr];
        },

        // Get the HTML-escaped value of an attribute.
        escape: function (attr) {
            return _.escape(this.get(attr));
        },

        // Returns `true` if the attribute contains a value that is not null
        // or undefined.
        has: function (attr) {
            return this.get(attr) != null;
        },

        // Set a hash of model attributes on the object, firing `"change"`. This is
        // the core primitive operation of a model, updating the data and notifying
        // anyone who needs to know about the change in state. The heart of the beast.
        set: function (key, val, options) {
            var attr, attrs, unset, changes, silent, changing, prev, current;
            if (key == null) return this;

            // Handle both `"key", value` and `{key: value}` -style arguments.
            if (typeof key === 'object') {
                attrs = key;
                options = val;
            } else {
                (attrs = {})[key] = val;
            }

            options || (options = {});

            // Run validation.
            if (!this._validate(attrs, options)) return false;

            // Extract attributes and options.
            unset = options.unset;
            silent = options.silent;
            changes = [];
            changing = this._changing;
            this._changing = true;

            if (!changing) {
                this._previousAttributes = _.clone(this.attributes);
                this.changed = {};
            }
            current = this.attributes, prev = this._previousAttributes;

            // Check for changes of `id`.
            if (this.idAttribute in attrs) this.id = attrs[this.idAttribute];

            // For each `set` attribute, update or delete the current value.
            for (attr in attrs) {
                val = attrs[attr];
                if (!_.isEqual(current[attr], val)) changes.push(attr);
                if (!_.isEqual(prev[attr], val)) {
                    this.changed[attr] = val;
                } else {
                    delete this.changed[attr];
                }
                unset ? delete current[attr] : current[attr] = val;
            }

            // Trigger all relevant attribute changes.
            if (!silent) {
                if (changes.length) this._pending = true;
                for (var i = 0, l = changes.length; i < l; i++) {
                    this.trigger('change:' + changes[i], this, current[changes[i]], options);
                }
            }

            // You might be wondering why there's a `while` loop here. Changes can
            // be recursively nested within `"change"` events.
            if (changing) return this;
            if (!silent) {
                while (this._pending) {
                    this._pending = false;
                    this.trigger('change', this, options);
                }
            }
            this._pending = false;
            this._changing = false;
            return this;
        },

        // Remove an attribute from the model, firing `"change"`. `unset` is a noop
        // if the attribute doesn't exist.
        unset: function (attr, options) {
            return this.set(attr, void 0, _.extend({}, options, {unset: true}));
        },

        // Clear all attributes on the model, firing `"change"`.
        clear: function (options) {
            var attrs = {};
            for (var key in this.attributes) attrs[key] = void 0;
            return this.set(attrs, _.extend({}, options, {unset: true}));
        },

        // Determine if the model has changed since the last `"change"` event.
        // If you specify an attribute name, determine if that attribute has changed.
        hasChanged: function (attr) {
            if (attr == null) return !_.isEmpty(this.changed);
            return _.has(this.changed, attr);
        },

        // Return an object containing all the attributes that have changed, or
        // false if there are no changed attributes. Useful for determining what
        // parts of a view need to be updated and/or what attributes need to be
        // persisted to the server. Unset attributes will be set to undefined.
        // You can also pass an attributes object to diff against the model,
        // determining if there *would be* a change.
        changedAttributes: function (diff) {
            if (!diff) return this.hasChanged() ? _.clone(this.changed) : false;
            var val, changed = false;
            var old = this._changing ? this._previousAttributes : this.attributes;
            for (var attr in diff) {
                if (_.isEqual(old[attr], (val = diff[attr]))) continue;
                (changed || (changed = {}))[attr] = val;
            }
            return changed;
        },

        // Get the previous value of an attribute, recorded at the time the last
        // `"change"` event was fired.
        previous: function (attr) {
            if (attr == null || !this._previousAttributes) return null;
            return this._previousAttributes[attr];
        },

        // Get all of the attributes of the model at the time of the previous
        // `"change"` event.
        previousAttributes: function () {
            return _.clone(this._previousAttributes);
        },

        // Fetch the model from the server. If the server's representation of the
        // model differs from its current attributes, they will be overridden,
        // triggering a `"change"` event.
        fetch: function (options) {
            options = options ? _.clone(options) : {};
            if (options.parse === void 0) options.parse = true;
            var model = this;
            var success = options.success;
            options.success = function (resp) {
                if (!model.set(model.parse(resp, options), options)) return false;
                if (success) success(model, resp, options);
                model.trigger('sync', model, resp, options);
            };
            wrapError(this, options);
            return this.sync('read', this, options);
        },

        // Set a hash of model attributes, and sync the model to the server.
        // If the server returns an attributes hash that differs, the model's
        // state will be `set` again.
        save: function (key, val, options) {
            var attrs, method, xhr, attributes = this.attributes;

            // Handle both `"key", value` and `{key: value}` -style arguments.
            if (key == null || typeof key === 'object') {
                attrs = key;
                options = val;
            } else {
                (attrs = {})[key] = val;
            }

            options = _.extend({validate: true}, options);

            // If we're not waiting and attributes exist, save acts as
            // `set(attr).save(null, opts)` with validation. Otherwise, check if
            // the model will be valid when the attributes, if any, are set.
            if (attrs && !options.wait) {
                if (!this.set(attrs, options)) return false;
            } else {
                if (!this._validate(attrs, options)) return false;
            }

            // Set temporary attributes if `{wait: true}`.
            if (attrs && options.wait) {
                this.attributes = _.extend({}, attributes, attrs);
            }

            // After a successful server-side save, the client is (optionally)
            // updated with the server-side state.
            if (options.parse === void 0) options.parse = true;
            var model = this;
            var success = options.success;
            options.success = function (resp) {
                // Ensure attributes are restored during synchronous saves.
                model.attributes = attributes;
                var serverAttrs = model.parse(resp, options);
                if (options.wait) serverAttrs = _.extend(attrs || {}, serverAttrs);
                if (_.isObject(serverAttrs) && !model.set(serverAttrs, options)) {
                    return false;
                }
                if (success) success(model, resp, options);
                model.trigger('sync', model, resp, options);
            };
            wrapError(this, options);

            method = this.isNew() ? 'create' : (options.patch ? 'patch' : 'update');
            if (method === 'patch') options.attrs = attrs;
            xhr = this.sync(method, this, options);

            // Restore attributes.
            if (attrs && options.wait) this.attributes = attributes;

            return xhr;
        },

        // Destroy this model on the server if it was already persisted.
        // Optimistically removes the model from its collection, if it has one.
        // If `wait: true` is passed, waits for the server to respond before removal.
        destroy: function (options) {
            options = options ? _.clone(options) : {};
            var model = this;
            var success = options.success;

            var destroy = function () {
                model.trigger('destroy', model, model.collection, options);
            };

            options.success = function (resp) {
                if (options.wait || model.isNew()) destroy();
                if (success) success(model, resp, options);
                if (!model.isNew()) model.trigger('sync', model, resp, options);
            };

            if (this.isNew()) {
                options.success();
                return false;
            }
            wrapError(this, options);

            var xhr = this.sync('delete', this, options);
            if (!options.wait) destroy();
            return xhr;
        },

        // Default URL for the model's representation on the server -- if you're
        // using Backbone's restful methods, override this to change the endpoint
        // that will be called.
        url: function () {
            var base = _.result(this, 'urlRoot') || _.result(this.collection, 'url') || urlError();
            if (this.isNew()) return base;
            return base + (base.charAt(base.length - 1) === '/' ? '' : '/') + encodeURIComponent(this.id);
        },

        // **parse** converts a response into the hash of attributes to be `set` on
        // the model. The default implementation is just to pass the response along.
        parse: function (resp, options) {
            return resp;
        },

        // Create a new model with identical attributes to this one.
        clone: function () {
            return new this.constructor(this.attributes);
        },

        // A model is new if it has never been saved to the server, and lacks an id.
        isNew: function () {
            return this.id == null;
        },

        // Check if the model is currently in a valid state.
        isValid: function (options) {
            return this._validate({}, _.extend(options || {}, { validate: true }));
        },

        // Run validation against the next complete set of model attributes,
        // returning `true` if all is well. Otherwise, fire an `"invalid"` event.
        _validate: function (attrs, options) {
            if (!options.validate || !this.validate) return true;
            attrs = _.extend({}, this.attributes, attrs);
            var error = this.validationError = this.validate(attrs, options) || null;
            if (!error) return true;
            this.trigger('invalid', this, error, _.extend(options, {validationError: error}));
            return false;
        }

    });

    // Underscore methods that we want to implement on the Model.
    var modelMethods = ['keys', 'values', 'pairs', 'invert', 'pick', 'omit'];

    // Mix in each Underscore method as a proxy to `Model#attributes`.
    _.each(modelMethods, function (method) {
        Model.prototype[method] = function () {
            var args = slice.call(arguments);
            args.unshift(this.attributes);
            return _[method].apply(_, args);
        };
    });

    // Backbone.Collection
    // -------------------

    // If models tend to represent a single row of data, a Backbone Collection is
    // more analagous to a table full of data ... or a small slice or page of that
    // table, or a collection of rows that belong together for a particular reason
    // -- all of the messages in this particular folder, all of the documents
    // belonging to this particular author, and so on. Collections maintain
    // indexes of their models, both in order, and for lookup by `id`.

    // Create a new **Collection**, perhaps to contain a specific type of `model`.
    // If a `comparator` is specified, the Collection will maintain
    // its models in sort order, as they're added and removed.
    var Collection = Backbone.Collection = function (models, options) {
        options || (options = {});
        if (options.model) this.model = options.model;
        if (options.comparator !== void 0) this.comparator = options.comparator;
        this._reset();
        this.initialize.apply(this, arguments);
        if (models) this.reset(models, _.extend({silent: true}, options));
    };

    // Default options for `Collection#set`.
    var setOptions = {add: true, remove: true, merge: true};
    var addOptions = {add: true, remove: false};

    // Define the Collection's inheritable methods.
    _.extend(Collection.prototype, Events, {

        // The default model for a collection is just a **Backbone.Model**.
        // This should be overridden in most cases.
        model: Model,

        // Initialize is an empty function by default. Override it with your own
        // initialization logic.
        initialize: function () {
        },

        // The JSON representation of a Collection is an array of the
        // models' attributes.
        toJSON: function (options) {
            return this.map(function (model) {
                return model.toJSON(options);
            });
        },

        // Proxy `Backbone.sync` by default.
        sync: function () {
            return Backbone.sync.apply(this, arguments);
        },

        // Add a model, or list of models to the set.
        add: function (models, options) {
            return this.set(models, _.extend({merge: false}, options, addOptions));
        },

        // Remove a model, or a list of models from the set.
        remove: function (models, options) {
            var singular = !_.isArray(models);
            models = singular ? [models] : _.clone(models);
            options || (options = {});
            var i, l, index, model;
            for (i = 0, l = models.length; i < l; i++) {
                model = models[i] = this.get(models[i]);
                if (!model) continue;
                delete this._byId[model.id];
                delete this._byId[model.cid];
                index = this.indexOf(model);
                this.models.splice(index, 1);
                this.length--;
                if (!options.silent) {
                    options.index = index;
                    model.trigger('remove', model, this, options);
                }
                this._removeReference(model);
            }
            return singular ? models[0] : models;
        },

        // Update a collection by `set`-ing a new list of models, adding new ones,
        // removing models that are no longer present, and merging models that
        // already exist in the collection, as necessary. Similar to **Model#set**,
        // the core operation for updating the data contained by the collection.
        set: function (models, options) {
            options = _.defaults({}, options, setOptions);
            if (options.parse) models = this.parse(models, options);
            var singular = !_.isArray(models);
            models = singular ? (models ? [models] : []) : _.clone(models);
            var i, l, id, model, attrs, existing, sort;
            var at = options.at;
            var targetModel = this.model;
            var sortable = this.comparator && (at == null) && options.sort !== false;
            var sortAttr = _.isString(this.comparator) ? this.comparator : null;
            var toAdd = [], toRemove = [], modelMap = {};
            var add = options.add, merge = options.merge, remove = options.remove;
            var order = !sortable && add && remove ? [] : false;

            // Turn bare objects into model references, and prevent invalid models
            // from being added.
            for (i = 0, l = models.length; i < l; i++) {
                attrs = models[i];
                if (attrs instanceof Model) {
                    id = model = attrs;
                } else {
                    id = attrs[targetModel.prototype.idAttribute];
                }

                // If a duplicate is found, prevent it from being added and
                // optionally merge it into the existing model.
                if (existing = this.get(id)) {
                    if (remove) modelMap[existing.cid] = true;
                    if (merge) {
                        attrs = attrs === model ? model.attributes : attrs;
                        if (options.parse) attrs = existing.parse(attrs, options);
                        existing.set(attrs, options);
                        if (sortable && !sort && existing.hasChanged(sortAttr)) sort = true;
                    }
                    models[i] = existing;

                    // If this is a new, valid model, push it to the `toAdd` list.
                } else if (add) {
                    model = models[i] = this._prepareModel(attrs, options);
                    if (!model) continue;
                    toAdd.push(model);

                    // Listen to added models' events, and index models for lookup by
                    // `id` and by `cid`.
                    model.on('all', this._onModelEvent, this);
                    this._byId[model.cid] = model;
                    if (model.id != null) this._byId[model.id] = model;
                }
                if (order) order.push(existing || model);
            }

            // Remove nonexistent models if appropriate.
            if (remove) {
                for (i = 0, l = this.length; i < l; ++i) {
                    if (!modelMap[(model = this.models[i]).cid]) toRemove.push(model);
                }
                if (toRemove.length) this.remove(toRemove, options);
            }

            // See if sorting is needed, update `length` and splice in new models.
            if (toAdd.length || (order && order.length)) {
                if (sortable) sort = true;
                this.length += toAdd.length;
                if (at != null) {
                    for (i = 0, l = toAdd.length; i < l; i++) {
                        this.models.splice(at + i, 0, toAdd[i]);
                    }
                } else {
                    if (order) this.models.length = 0;
                    var orderedModels = order || toAdd;
                    for (i = 0, l = orderedModels.length; i < l; i++) {
                        this.models.push(orderedModels[i]);
                    }
                }
            }

            // Silently sort the collection if appropriate.
            if (sort) this.sort({silent: true});

            // Unless silenced, it's time to fire all appropriate add/sort events.
            if (!options.silent) {
                for (i = 0, l = toAdd.length; i < l; i++) {
                    (model = toAdd[i]).trigger('add', model, this, options);
                }
                if (sort || (order && order.length)) this.trigger('sort', this, options);
            }

            // Return the added (or merged) model (or models).
            return singular ? models[0] : models;
        },

        // When you have more items than you want to add or remove individually,
        // you can reset the entire set with a new list of models, without firing
        // any granular `add` or `remove` events. Fires `reset` when finished.
        // Useful for bulk operations and optimizations.
        reset: function (models, options) {
            options || (options = {});
            for (var i = 0, l = this.models.length; i < l; i++) {
                this._removeReference(this.models[i]);
            }
            options.previousModels = this.models;
            this._reset();
            models = this.add(models, _.extend({silent: true}, options));
            if (!options.silent) this.trigger('reset', this, options);
            return models;
        },

        // Add a model to the end of the collection.
        push: function (model, options) {
            return this.add(model, _.extend({at: this.length}, options));
        },

        // Remove a model from the end of the collection.
        pop: function (options) {
            var model = this.at(this.length - 1);
            this.remove(model, options);
            return model;
        },

        // Add a model to the beginning of the collection.
        unshift: function (model, options) {
            return this.add(model, _.extend({at: 0}, options));
        },

        // Remove a model from the beginning of the collection.
        shift: function (options) {
            var model = this.at(0);
            this.remove(model, options);
            return model;
        },

        // Slice out a sub-array of models from the collection.
        slice: function () {
            return slice.apply(this.models, arguments);
        },

        // Get a model from the set by id.
        get: function (obj) {
            if (obj == null) return void 0;
            return this._byId[obj.id] || this._byId[obj.cid] || this._byId[obj];
        },

        // Get the model at the given index.
        at: function (index) {
            return this.models[index];
        },

        // Return models with matching attributes. Useful for simple cases of
        // `filter`.
        where: function (attrs, first) {
            if (_.isEmpty(attrs)) return first ? void 0 : [];
            return this[first ? 'find' : 'filter'](function (model) {
                for (var key in attrs) {
                    if (attrs[key] !== model.get(key)) return false;
                }
                return true;
            });
        },

        // Return the first model with matching attributes. Useful for simple cases
        // of `find`.
        findWhere: function (attrs) {
            return this.where(attrs, true);
        },

        // Force the collection to re-sort itself. You don't need to call this under
        // normal circumstances, as the set will maintain sort order as each item
        // is added.
        sort: function (options) {
            if (!this.comparator) throw new Error('Cannot sort a set without a comparator');
            options || (options = {});

            // Run sort based on type of `comparator`.
            if (_.isString(this.comparator) || this.comparator.length === 1) {
                this.models = this.sortBy(this.comparator, this);
            } else {
                this.models.sort(_.bind(this.comparator, this));
            }

            if (!options.silent) this.trigger('sort', this, options);
            return this;
        },

        // Pluck an attribute from each model in the collection.
        pluck: function (attr) {
            return _.invoke(this.models, 'get', attr);
        },

        // Fetch the default set of models for this collection, resetting the
        // collection when they arrive. If `reset: true` is passed, the response
        // data will be passed through the `reset` method instead of `set`.
        fetch: function (options) {
            options = options ? _.clone(options) : {};
            if (options.parse === void 0) options.parse = true;
            var success = options.success;
            var collection = this;
            options.success = function (resp) {
                var method = options.reset ? 'reset' : 'set';
                collection[method](resp, options);
                if (success) success(collection, resp, options);
                collection.trigger('sync', collection, resp, options);
            };
            wrapError(this, options);
            return this.sync('read', this, options);
        },

        // Create a new instance of a model in this collection. Add the model to the
        // collection immediately, unless `wait: true` is passed, in which case we
        // wait for the server to agree.
        create: function (model, options) {
            options = options ? _.clone(options) : {};
            if (!(model = this._prepareModel(model, options))) return false;
            if (!options.wait) this.add(model, options);
            var collection = this;
            var success = options.success;
            options.success = function (model, resp, options) {
                if (options.wait) collection.add(model, options);
                if (success) success(model, resp, options);
            };
            model.save(null, options);
            return model;
        },

        // **parse** converts a response into a list of models to be added to the
        // collection. The default implementation is just to pass it through.
        parse: function (resp, options) {
            return resp;
        },

        // Create a new collection with an identical list of models as this one.
        clone: function () {
            return new this.constructor(this.models);
        },

        // Private method to reset all internal state. Called when the collection
        // is first initialized or reset.
        _reset: function () {
            this.length = 0;
            this.models = [];
            this._byId = {};
        },

        // Prepare a hash of attributes (or other model) to be added to this
        // collection.
        _prepareModel: function (attrs, options) {
            if (attrs instanceof Model) {
                if (!attrs.collection) attrs.collection = this;
                return attrs;
            }
            options = options ? _.clone(options) : {};
            options.collection = this;
            var model = new this.model(attrs, options);
            if (!model.validationError) return model;
            this.trigger('invalid', this, model.validationError, options);
            return false;
        },

        // Internal method to sever a model's ties to a collection.
        _removeReference: function (model) {
            if (this === model.collection) delete model.collection;
            model.off('all', this._onModelEvent, this);
        },

        // Internal method called every time a model in the set fires an event.
        // Sets need to update their indexes when models change ids. All other
        // events simply proxy through. "add" and "remove" events that originate
        // in other collections are ignored.
        _onModelEvent: function (event, model, collection, options) {
            if ((event === 'add' || event === 'remove') && collection !== this) return;
            if (event === 'destroy') this.remove(model, options);
            if (model && event === 'change:' + model.idAttribute) {
                delete this._byId[model.previous(model.idAttribute)];
                if (model.id != null) this._byId[model.id] = model;
            }
            this.trigger.apply(this, arguments);
        }

    });

    // Underscore methods that we want to implement on the Collection.
    // 90% of the core usefulness of Backbone Collections is actually implemented
    // right here:
    var methods = ['forEach', 'each', 'map', 'collect', 'reduce', 'foldl',
        'inject', 'reduceRight', 'foldr', 'find', 'detect', 'filter', 'select',
        'reject', 'every', 'all', 'some', 'any', 'include', 'contains', 'invoke',
        'max', 'min', 'toArray', 'size', 'first', 'head', 'take', 'initial', 'rest',
        'tail', 'drop', 'last', 'without', 'difference', 'indexOf', 'shuffle',
        'lastIndexOf', 'isEmpty', 'chain'];

    // Mix in each Underscore method as a proxy to `Collection#models`.
    _.each(methods, function (method) {
        Collection.prototype[method] = function () {
            var args = slice.call(arguments);
            args.unshift(this.models);
            return _[method].apply(_, args);
        };
    });

    // Underscore methods that take a property name as an argument.
    var attributeMethods = ['groupBy', 'countBy', 'sortBy'];

    // Use attributes instead of properties.
    _.each(attributeMethods, function (method) {
        Collection.prototype[method] = function (value, context) {
            var iterator = _.isFunction(value) ? value : function (model) {
                return model.get(value);
            };
            return _[method](this.models, iterator, context);
        };
    });

    // Backbone.View
    // -------------

    // Backbone Views are almost more convention than they are actual code. A View
    // is simply a JavaScript object that represents a logical chunk of UI in the
    // DOM. This might be a single item, an entire list, a sidebar or panel, or
    // even the surrounding frame which wraps your whole app. Defining a chunk of
    // UI as a **View** allows you to define your DOM events declaratively, without
    // having to worry about render order ... and makes it easy for the view to
    // react to specific changes in the state of your models.

    // Creating a Backbone.View creates its initial element outside of the DOM,
    // if an existing element is not provided...
    var View = Backbone.View = function (options) {
        this.cid = _.uniqueId('view');
        options || (options = {});
        _.extend(this, _.pick(options, viewOptions));
        this._ensureElement();
        this.initialize.apply(this, arguments);
        this.delegateEvents();
    };

    // Cached regex to split keys for `delegate`.
    var delegateEventSplitter = /^(\S+)\s*(.*)$/;

    // List of view options to be merged as properties.
    var viewOptions = ['model', 'collection', 'el', 'id', 'attributes', 'className', 'tagName', 'events'];

    // Set up all inheritable **Backbone.View** properties and methods.
    _.extend(View.prototype, Events, {

        // The default `tagName` of a View's element is `"div"`.
        tagName: 'div',

        // jQuery delegate for element lookup, scoped to DOM elements within the
        // current view. This should be preferred to global lookups where possible.
        $: function (selector) {
            return this.$el.find(selector);
        },

        // Initialize is an empty function by default. Override it with your own
        // initialization logic.
        initialize: function () {
        },

        // **render** is the core function that your view should override, in order
        // to populate its element (`this.el`), with the appropriate HTML. The
        // convention is for **render** to always return `this`.
        render: function () {
            return this;
        },

        // Remove this view by taking the element out of the DOM, and removing any
        // applicable Backbone.Events listeners.
        remove: function () {
            this.$el.remove();
            this.stopListening();
            return this;
        },

        // Change the view's element (`this.el` property), including event
        // re-delegation.
        setElement: function (element, delegate) {
            if (this.$el) this.undelegateEvents();
            this.$el = element instanceof Backbone.$ ? element : Backbone.$(element);
            this.el = this.$el[0];
            if (delegate !== false) this.delegateEvents();
            return this;
        },

        // Set callbacks, where `this.events` is a hash of
        //
        // *{"event selector": "callback"}*
        //
        //     {
        //       'mousedown .title':  'edit',
        //       'click .button':     'save',
        //       'click .open':       function(e) { ... }
        //     }
        //
        // pairs. Callbacks will be bound to the view, with `this` set properly.
        // Uses event delegation for efficiency.
        // Omitting the selector binds the event to `this.el`.
        // This only works for delegate-able events: not `focus`, `blur`, and
        // not `change`, `submit`, and `reset` in Internet Explorer.
        delegateEvents: function (events) {
            if (!(events || (events = _.result(this, 'events')))) return this;
            this.undelegateEvents();
            for (var key in events) {
                var method = events[key];
                if (!_.isFunction(method)) method = this[events[key]];
                if (!method) continue;

                var match = key.match(delegateEventSplitter);
                var eventName = match[1], selector = match[2];
                method = _.bind(method, this);
                eventName += '.delegateEvents' + this.cid;
                if (selector === '') {
                    this.$el.on(eventName, method);
                } else {
                    this.$el.on(eventName, selector, method);
                }
            }
            return this;
        },

        // Clears all callbacks previously bound to the view with `delegateEvents`.
        // You usually don't need to use this, but may wish to if you have multiple
        // Backbone views attached to the same DOM element.
        undelegateEvents: function () {
            this.$el.off('.delegateEvents' + this.cid);
            return this;
        },

        // Ensure that the View has a DOM element to render into.
        // If `this.el` is a string, pass it through `$()`, take the first
        // matching element, and re-assign it to `el`. Otherwise, create
        // an element from the `id`, `className` and `tagName` properties.
        _ensureElement: function () {
            if (!this.el) {
                var attrs = _.extend({}, _.result(this, 'attributes'));
                if (this.id) attrs.id = _.result(this, 'id');
                if (this.className) attrs['class'] = _.result(this, 'className');
                var $el = Backbone.$('<' + _.result(this, 'tagName') + '>').attr(attrs);
                this.setElement($el, false);
            } else {
                this.setElement(_.result(this, 'el'), false);
            }
        }

    });

    // Backbone.sync
    // -------------

    // Override this function to change the manner in which Backbone persists
    // models to the server. You will be passed the type of request, and the
    // model in question. By default, makes a RESTful Ajax request
    // to the model's `url()`. Some possible customizations could be:
    //
    // * Use `setTimeout` to batch rapid-fire updates into a single request.
    // * Send up the models as XML instead of JSON.
    // * Persist models via WebSockets instead of Ajax.
    //
    // Turn on `Backbone.emulateHTTP` in order to send `PUT` and `DELETE` requests
    // as `POST`, with a `_method` parameter containing the true HTTP method,
    // as well as all requests with the body as `application/x-www-form-urlencoded`
    // instead of `application/json` with the model in a param named `model`.
    // Useful when interfacing with server-side languages like **PHP** that make
    // it difficult to read the body of `PUT` requests.
    Backbone.sync = function (method, model, options) {
        var type = methodMap[method];

        // Default options, unless specified.
        _.defaults(options || (options = {}), {
            emulateHTTP: Backbone.emulateHTTP,
            emulateJSON: Backbone.emulateJSON
        });

        // Default JSON-request options.
        var params = {type: type, dataType: 'json'};

        // Ensure that we have a URL.
        if (!options.url) {
            params.url = _.result(model, 'url') || urlError();
        }

        // Ensure that we have the appropriate request data.
        if (options.data == null && model && (method === 'create' || method === 'update' || method === 'patch')) {
            params.contentType = 'application/json';
            params.data = JSON.stringify(options.attrs || model.toJSON(options));
        }

        // For older servers, emulate JSON by encoding the request into an HTML-form.
        if (options.emulateJSON) {
            params.contentType = 'application/x-www-form-urlencoded';
            params.data = params.data ? {model: params.data} : {};
        }

        // For older servers, emulate HTTP by mimicking the HTTP method with `_method`
        // And an `X-HTTP-Method-Override` header.
        if (options.emulateHTTP && (type === 'PUT' || type === 'DELETE' || type === 'PATCH')) {
            params.type = 'POST';
            if (options.emulateJSON) params.data._method = type;
            var beforeSend = options.beforeSend;
            options.beforeSend = function (xhr) {
                xhr.setRequestHeader('X-HTTP-Method-Override', type);
                if (beforeSend) return beforeSend.apply(this, arguments);
            };
        }

        // Don't process data on a non-GET request.
        if (params.type !== 'GET' && !options.emulateJSON) {
            params.processData = false;
        }

        // If we're sending a `PATCH` request, and we're in an old Internet Explorer
        // that still has ActiveX enabled by default, override jQuery to use that
        // for XHR instead. Remove this line when jQuery supports `PATCH` on IE8.
        if (params.type === 'PATCH' && noXhrPatch) {
            params.xhr = function () {
                return new ActiveXObject("Microsoft.XMLHTTP");
            };
        }

        // Make the request, allowing the user to override any Ajax options.
        var xhr = options.xhr = Backbone.ajax(_.extend(params, options));
        model.trigger('request', model, xhr, options);
        return xhr;
    };

    var noXhrPatch = typeof window !== 'undefined' && !!window.ActiveXObject && !(window.XMLHttpRequest && (new XMLHttpRequest).dispatchEvent);

    // Map from CRUD to HTTP for our default `Backbone.sync` implementation.
    var methodMap = {
        'create': 'POST',
        'update': 'PUT',
        'patch': 'PATCH',
        'delete': 'DELETE',
        'read': 'GET'
    };

    // Set the default implementation of `Backbone.ajax` to proxy through to `$`.
    // Override this if you'd like to use a different library.
    Backbone.ajax = function () {
        return Backbone.$.ajax.apply(Backbone.$, arguments);
    };

    // Backbone.Router
    // ---------------

    // Routers map faux-URLs to actions, and fire events when routes are
    // matched. Creating a new one sets its `routes` hash, if not set statically.
    var Router = Backbone.Router = function (options) {
        options || (options = {});
        if (options.routes) this.routes = options.routes;
        this._bindRoutes();
        this.initialize.apply(this, arguments);
    };

    // Cached regular expressions for matching named param parts and splatted
    // parts of route strings.
    var optionalParam = /\((.*?)\)/g;
    var namedParam = /(\(\?)?:\w+/g;
    var splatParam = /\*\w+/g;
    var escapeRegExp = /[\-{}\[\]+?.,\\\^$|#\s]/g;

    // Set up all inheritable **Backbone.Router** properties and methods.
    _.extend(Router.prototype, Events, {

        // Initialize is an empty function by default. Override it with your own
        // initialization logic.
        initialize: function () {
        },

        // Manually bind a single named route to a callback. For example:
        //
        //     this.route('search/:query/p:num', 'search', function(query, num) {
        //       ...
        //     });
        //
        route: function (route, name, callback) {
            if (!_.isRegExp(route)) route = this._routeToRegExp(route);
            if (_.isFunction(name)) {
                callback = name;
                name = '';
            }
            if (!callback) callback = this[name];
            var router = this;
            Backbone.history.route(route, function (fragment) {
                var args = router._extractParameters(route, fragment);
                callback && callback.apply(router, args);
                router.trigger.apply(router, ['route:' + name].concat(args));
                router.trigger('route', name, args);
                Backbone.history.trigger('route', router, name, args);
            });
            return this;
        },

        // Simple proxy to `Backbone.history` to save a fragment into the history.
        navigate: function (fragment, options) {
            Backbone.history.navigate(fragment, options);
            return this;
        },

        // Bind all defined routes to `Backbone.history`. We have to reverse the
        // order of the routes here to support behavior where the most general
        // routes can be defined at the bottom of the route map.
        _bindRoutes: function () {
            if (!this.routes) return;
            this.routes = _.result(this, 'routes');
            var route, routes = _.keys(this.routes);
            while ((route = routes.pop()) != null) {
                this.route(route, this.routes[route]);
            }
        },

        // Convert a route string into a regular expression, suitable for matching
        // against the current location hash.
        _routeToRegExp: function (route) {
            route = route.replace(escapeRegExp, '\\$&')
                .replace(optionalParam, '(?:$1)?')
                .replace(namedParam, function (match, optional) {
                    return optional ? match : '([^\/]+)';
                })
                .replace(splatParam, '(.*?)');
            return new RegExp('^' + route + '$');
        },

        // Given a route, and a URL fragment that it matches, return the array of
        // extracted decoded parameters. Empty or unmatched parameters will be
        // treated as `null` to normalize cross-browser behavior.
        _extractParameters: function (route, fragment) {
            var params = route.exec(fragment).slice(1);
            return _.map(params, function (param) {
                return param ? decodeURIComponent(param) : null;
            });
        }

    });

    // Backbone.History
    // ----------------

    // Handles cross-browser history management, based on either
    // [pushState](http://diveintohtml5.info/history.html) and real URLs, or
    // [onhashchange](https://developer.mozilla.org/en-US/docs/DOM/window.onhashchange)
    // and URL fragments. If the browser supports neither (old IE, natch),
    // falls back to polling.
    var History = Backbone.History = function () {
        this.handlers = [];
        _.bindAll(this, 'checkUrl');

        // Ensure that `History` can be used outside of the browser.
        if (typeof window !== 'undefined') {
            this.location = window.location;
            this.history = window.history;
        }
    };

    // Cached regex for stripping a leading hash/slash and trailing space.
    var routeStripper = /^[#\/]|\s+$/g;

    // Cached regex for stripping leading and trailing slashes.
    var rootStripper = /^\/+|\/+$/g;

    // Cached regex for detecting MSIE.
    var isExplorer = /msie [\w.]+/;

    // Cached regex for removing a trailing slash.
    var trailingSlash = /\/$/;

    // Cached regex for stripping urls of hash and query.
    var pathStripper = /[?#].*$/;

    // Has the history handling already been started?
    History.started = false;

    // Set up all inheritable **Backbone.History** properties and methods.
    _.extend(History.prototype, Events, {

        // The default interval to poll for hash changes, if necessary, is
        // twenty times a second.
        interval: 50,

        // Gets the true hash value. Cannot use location.hash directly due to bug
        // in Firefox where location.hash will always be decoded.
        getHash: function (window) {
            var match = (window || this).location.href.match(/#(.*)$/);
            return match ? match[1] : '';
        },

        // Get the cross-browser normalized URL fragment, either from the URL,
        // the hash, or the override.
        getFragment: function (fragment, forcePushState) {
            if (fragment == null) {
                if (this._hasPushState || !this._wantsHashChange || forcePushState) {
                    fragment = this.location.pathname;
                    var root = this.root.replace(trailingSlash, '');
                    if (!fragment.indexOf(root)) fragment = fragment.slice(root.length);
                } else {
                    fragment = this.getHash();
                }
            }
            return fragment.replace(routeStripper, '');
        },

        // Start the hash change handling, returning `true` if the current URL matches
        // an existing route, and `false` otherwise.
        start: function (options) {
            if (History.started) throw new Error("Backbone.history has already been started");
            History.started = true;

            // Figure out the initial configuration. Do we need an iframe?
            // Is pushState desired ... is it available?
            this.options = _.extend({root: '/'}, this.options, options);
            this.root = this.options.root;
            this._wantsHashChange = this.options.hashChange !== false;
            this._wantsPushState = !!this.options.pushState;
            this._hasPushState = !!(this.options.pushState && this.history && this.history.pushState);
            var fragment = this.getFragment();
            var docMode = document.documentMode;
            var oldIE = (isExplorer.exec(navigator.userAgent.toLowerCase()) && (!docMode || docMode <= 7));

            // Normalize root to always include a leading and trailing slash.
            this.root = ('/' + this.root + '/').replace(rootStripper, '/');

            if (oldIE && this._wantsHashChange) {
                this.iframe = Backbone.$('<iframe src="javascript:0" tabindex="-1" />').hide().appendTo('body')[0].contentWindow;
                this.navigate(fragment);
            }

            // Depending on whether we're using pushState or hashes, and whether
            // 'onhashchange' is supported, determine how we check the URL state.
            if (this._hasPushState) {
                Backbone.$(window).on('popstate', this.checkUrl);
            } else if (this._wantsHashChange && ('onhashchange' in window) && !oldIE) {
                Backbone.$(window).on('hashchange', this.checkUrl);
            } else if (this._wantsHashChange) {
                this._checkUrlInterval = setInterval(this.checkUrl, this.interval);
            }

            // Determine if we need to change the base url, for a pushState link
            // opened by a non-pushState browser.
            this.fragment = fragment;
            var loc = this.location;
            var atRoot = loc.pathname.replace(/[^\/]$/, '$&/') === this.root;

            // Transition from hashChange to pushState or vice versa if both are
            // requested.
            if (this._wantsHashChange && this._wantsPushState) {

                // If we've started off with a route from a `pushState`-enabled
                // browser, but we're currently in a browser that doesn't support it...
                if (!this._hasPushState && !atRoot) {
                    this.fragment = this.getFragment(null, true);
                    this.location.replace(this.root + this.location.search + '#' + this.fragment);
                    // Return immediately as browser will do redirect to new url
                    return true;

                    // Or if we've started out with a hash-based route, but we're currently
                    // in a browser where it could be `pushState`-based instead...
                } else if (this._hasPushState && atRoot && loc.hash) {
                    this.fragment = this.getHash().replace(routeStripper, '');
                    this.history.replaceState({}, document.title, this.root + this.fragment + loc.search);
                }

            }

            if (!this.options.silent) return this.loadUrl();
        },

        // Disable Backbone.history, perhaps temporarily. Not useful in a real app,
        // but possibly useful for unit testing Routers.
        stop: function () {
            Backbone.$(window).off('popstate', this.checkUrl).off('hashchange', this.checkUrl);
            clearInterval(this._checkUrlInterval);
            History.started = false;
        },

        // Add a route to be tested when the fragment changes. Routes added later
        // may override previous routes.
        route: function (route, callback) {
            this.handlers.unshift({route: route, callback: callback});
        },

        // Checks the current URL to see if it has changed, and if it has,
        // calls `loadUrl`, normalizing across the hidden iframe.
        checkUrl: function (e) {
            var current = this.getFragment();
            if (current === this.fragment && this.iframe) {
                current = this.getFragment(this.getHash(this.iframe));
            }
            if (current === this.fragment) return false;
            if (this.iframe) this.navigate(current);
            this.loadUrl();
        },

        // Attempt to load the current URL fragment. If a route succeeds with a
        // match, returns `true`. If no defined routes matches the fragment,
        // returns `false`.
        loadUrl: function (fragment) {
            fragment = this.fragment = this.getFragment(fragment);
            return _.any(this.handlers, function (handler) {
                if (handler.route.test(fragment)) {
                    handler.callback(fragment);
                    return true;
                }
            });
        },

        // Save a fragment into the hash history, or replace the URL state if the
        // 'replace' option is passed. You are responsible for properly URL-encoding
        // the fragment in advance.
        //
        // The options object can contain `trigger: true` if you wish to have the
        // route callback be fired (not usually desirable), or `replace: true`, if
        // you wish to modify the current URL without adding an entry to the history.
        navigate: function (fragment, options) {
            if (!History.started) return false;
            if (!options || options === true) options = {trigger: !!options};

            var url = this.root + (fragment = this.getFragment(fragment || ''));

            // Strip the fragment of the query and hash for matching.
            fragment = fragment.replace(pathStripper, '');

            if (this.fragment === fragment) return;
            this.fragment = fragment;

            // Don't include a trailing slash on the root.
            if (fragment === '' && url !== '/') url = url.slice(0, -1);

            // If pushState is available, we use it to set the fragment as a real URL.
            if (this._hasPushState) {
                this.history[options.replace ? 'replaceState' : 'pushState']({}, document.title, url);

                // If hash changes haven't been explicitly disabled, update the hash
                // fragment to store history.
            } else if (this._wantsHashChange) {
                this._updateHash(this.location, fragment, options.replace);
                if (this.iframe && (fragment !== this.getFragment(this.getHash(this.iframe)))) {
                    // Opening and closing the iframe tricks IE7 and earlier to push a
                    // history entry on hash-tag change.  When replace is true, we don't
                    // want this.
                    if (!options.replace) this.iframe.document.open().close();
                    this._updateHash(this.iframe.location, fragment, options.replace);
                }

                // If you've told us that you explicitly don't want fallback hashchange-
                // based history, then `navigate` becomes a page refresh.
            } else {
                return this.location.assign(url);
            }
            if (options.trigger) return this.loadUrl(fragment);
        },

        // Update the hash location, either replacing the current entry, or adding
        // a new one to the browser history.
        _updateHash: function (location, fragment, replace) {
            if (replace) {
                var href = location.href.replace(/(javascript:|#).*$/, '');
                location.replace(href + '#' + fragment);
            } else {
                // Some browsers require that `hash` contains a leading #.
                location.hash = '#' + fragment;
            }
        }

    });

    // Create the default Backbone.history.
    Backbone.history = new History;

    // Helpers
    // -------

    // Helper function to correctly set up the prototype chain, for subclasses.
    // Similar to `goog.inherits`, but uses a hash of prototype properties and
    // class properties to be extended.
    var extend = function (protoProps, staticProps) {
        var parent = this;
        var child;

        // The constructor function for the new subclass is either defined by you
        // (the "constructor" property in your `extend` definition), or defaulted
        // by us to simply call the parent's constructor.
        if (protoProps && _.has(protoProps, 'constructor')) {
            child = protoProps.constructor;
        } else {
            child = function () {
                return parent.apply(this, arguments);
            };
        }

        // Add static properties to the constructor function, if supplied.
        _.extend(child, parent, staticProps);

        // Set the prototype chain to inherit from `parent`, without calling
        // `parent`'s constructor function.
        var Surrogate = function () {
            this.constructor = child;
        };
        Surrogate.prototype = parent.prototype;
        child.prototype = new Surrogate;

        // Add prototype properties (instance properties) to the subclass,
        // if supplied.
        if (protoProps) _.extend(child.prototype, protoProps);

        // Set a convenience property in case the parent's prototype is needed
        // later.
        child.__super__ = parent.prototype;

        return child;
    };

    // Set up inheritance for the model, collection, router, view and history.
    Model.extend = Collection.extend = Router.extend = View.extend = History.extend = extend;

    // Throw an error when a URL is needed, and none is supplied.
    var urlError = function () {
        throw new Error('A "url" property or function must be specified');
    };

    // Wrap an optional error callback with a fallback error event.
    var wrapError = function (model, options) {
        var error = options.error;
        options.error = function (resp) {
            if (error) error(model, resp, options);
            model.trigger('error', model, resp, options);
        };
    };

}).call(this);

 /* ************************ new file ************************ */
/*!
 * mustache.js - Logic-less {{mustache}} templates with JavaScript
 * http://github.com/janl/mustache.js
 */
var Mustache = (typeof module !== "undefined" && module.exports) || {};

(function (exports) {

    exports.name = "mustache.js";
    exports.version = "0.5.0-dev";
    exports.tags = ["{{", "}}"];
    exports.parse = parse;
    exports.compile = compile;
    exports.render = render;
    exports.clearCache = clearCache;

    // This is here for backwards compatibility with 0.4.x.
    exports.to_html = function (template, view, partials, send) {
        var result = render(template, view, partials);

        if (typeof send === "function") {
            send(result);
        } else {
            return result;
        }
    };

    var _toString = Object.prototype.toString;
    var _isArray = Array.isArray;
    var _forEach = Array.prototype.forEach;
    var _trim = String.prototype.trim;

    var isArray;
    if (_isArray) {
        isArray = _isArray;
    } else {
        isArray = function (obj) {
            return _toString.call(obj) === "[object Array]";
        };
    }

    var forEach;
    if (_forEach) {
        forEach = function (obj, callback, scope) {
            return _forEach.call(obj, callback, scope);
        };
    } else {
        forEach = function (obj, callback, scope) {
            for (var i = 0, len = obj.length; i < len; ++i) {
                callback.call(scope, obj[i], i, obj);
            }
        };
    }

    var spaceRe = /^\s*$/;

    function isWhitespace(string) {
        return spaceRe.test(string);
    }

    var trim;
    if (_trim) {
        trim = function (string) {
            return string == null ? "" : _trim.call(string);
        };
    } else {
        var trimLeft, trimRight;

        if (isWhitespace("\xA0")) {
            trimLeft = /^\s+/;
            trimRight = /\s+$/;
        } else {
            // IE doesn't match non-breaking spaces with \s, thanks jQuery.
            trimLeft = /^[\s\xA0]+/;
            trimRight = /[\s\xA0]+$/;
        }

        trim = function (string) {
            return string == null ? "" :
                String(string).replace(trimLeft, "").replace(trimRight, "");
        };
    }

    var escapeMap = {
        "&": "&amp;",
        "<": "&lt;",
        ">": "&gt;",
        '"': '&quot;',
        "'": '&#39;'
    };

    function escapeHTML(string) {
        return String(string).replace(/&(?!\w+;)|[<>"']/g, function (s) {
            return escapeMap[s] || s;
        });
    }

    /**
     * Adds the `template`, `line`, and `file` properties to the given error
     * object and alters the message to provide more useful debugging information.
     */
    function debug(e, template, line, file) {
        file = file || "<template>";

        var lines = template.split("\n"),
            start = Math.max(line - 3, 0),
            end = Math.min(lines.length, line + 3),
            context = lines.slice(start, end);

        var c;
        for (var i = 0, len = context.length; i < len; ++i) {
            c = i + start + 1;
            context[i] = (c === line ? " >> " : "    ") + context[i];
        }

        e.template = template;
        e.line = line;
        e.file = file;
        e.message = [file + ":" + line, context.join("\n"), "", e.message].join("\n");

        return e;
    }

    /**
     * Looks up the value of the given `name` in the given context `stack`.
     */
    function lookup(name, stack, defaultValue) {
        if (name === ".") {
            return stack[stack.length - 1];
        }

        var names = name.split(".");
        var lastIndex = names.length - 1;
        var target = names[lastIndex];

        var value, context, i = stack.length, j, localStack;
        while (i) {
            localStack = stack.slice(0);
            context = stack[--i];

            j = 0;
            while (j < lastIndex) {
                context = context[names[j++]];

                if (context == null) {
                    break;
                }

                localStack.push(context);
            }

            if (context && typeof context === "object" && target in context) {
                value = context[target];
                break;
            }
        }

        // If the value is a function, call it in the current context.
        if (typeof value === "function") {
            value = value.call(localStack[localStack.length - 1]);
        }

        if (value == null) {
            return defaultValue;
        }

        return value;
    }

    function renderSection(name, stack, callback, inverted) {
        var buffer = "";
        var value = lookup(name, stack);

        if (inverted) {
            // From the spec: inverted sections may render text once based on the
            // inverse value of the key. That is, they will be rendered if the key
            // doesn't exist, is false, or is an empty list.
            if (value == null || value === false || (isArray(value) && value.length === 0)) {
                buffer += callback();
            }
        } else if (isArray(value)) {
            forEach(value, function (value) {
                stack.push(value);
                buffer += callback();
                stack.pop();
            });
        } else if (typeof value === "object") {
            stack.push(value);
            buffer += callback();
            stack.pop();
        } else if (typeof value === "function") {
            var scope = stack[stack.length - 1];
            var scopedRender = function (template) {
                return render(template, scope);
            };
            buffer += value.call(scope, callback(), scopedRender) || "";
        } else if (value) {
            buffer += callback();
        }

        return buffer;
    }

    /**
     * Parses the given `template` and returns the source of a function that,
     * with the proper arguments, will render the template. Recognized options
     * include the following:
     *
     *   - file     The name of the file the template comes from (displayed in
     *              error messages)
     *   - tags     An array of open and close tags the `template` uses. Defaults
     *              to the value of Mustache.tags
     *   - debug    Set `true` to log the body of the generated function to the
     *              console
     *   - space    Set `true` to preserve whitespace from lines that otherwise
     *              contain only a {{tag}}. Defaults to `false`
     */
    function parse(template, options) {
        options = options || {};

        var tags = options.tags || exports.tags,
            openTag = tags[0],
            closeTag = tags[tags.length - 1];

        var code = [
            'var buffer = "";', // output buffer
            "\nvar line = 1;", // keep track of source line number
            "\ntry {",
            '\nbuffer += "'
        ];

        var spaces = [],      // indices of whitespace in code on the current line
            hasTag = false,   // is there a {{tag}} on the current line?
            nonSpace = false; // is there a non-space char on the current line?

        // Strips all space characters from the code array for the current line
        // if there was a {{tag}} on it and otherwise only spaces.
        var stripSpace = function () {
            if (hasTag && !nonSpace && !options.space) {
                while (spaces.length) {
                    code.splice(spaces.pop(), 1);
                }
            } else {
                spaces = [];
            }

            hasTag = false;
            nonSpace = false;
        };

        var sectionStack = [], updateLine, nextOpenTag, nextCloseTag;

        var setTags = function (source) {
            tags = trim(source).split(/\s+/);
            nextOpenTag = tags[0];
            nextCloseTag = tags[tags.length - 1];
        };

        var includePartial = function (source) {
            code.push(
                '";',
                updateLine,
                '\nvar partial = partials["' + trim(source) + '"];',
                '\nif (partial) {',
                '\n  buffer += render(partial,stack[stack.length - 1],partials);',
                '\n}',
                '\nbuffer += "'
            );
        };

        var openSection = function (source, inverted) {
            var name = trim(source);

            if (name === "") {
                throw debug(new Error("Section name may not be empty"), template, line, options.file);
            }

            sectionStack.push({name: name, inverted: inverted});

            code.push(
                '";',
                updateLine,
                '\nvar name = "' + name + '";',
                '\nvar callback = (function () {',
                '\n  return function () {',
                '\n    var buffer = "";',
                '\nbuffer += "'
            );
        };

        var openInvertedSection = function (source) {
            openSection(source, true);
        };

        var closeSection = function (source) {
            var name = trim(source);
            var openName = sectionStack.length != 0 && sectionStack[sectionStack.length - 1].name;

            if (!openName || name != openName) {
                throw debug(new Error('Section named "' + name + '" was never opened'), template, line, options.file);
            }

            var section = sectionStack.pop();

            code.push(
                '";',
                '\n    return buffer;',
                '\n  };',
                '\n})();'
            );

            if (section.inverted) {
                code.push("\nbuffer += renderSection(name,stack,callback,true);");
            } else {
                code.push("\nbuffer += renderSection(name,stack,callback);");
            }

            code.push('\nbuffer += "');
        };

        var sendPlain = function (source) {
            code.push(
                '";',
                updateLine,
                '\nbuffer += lookup("' + trim(source) + '",stack,"");',
                '\nbuffer += "'
            );
        };

        var sendEscaped = function (source) {
            code.push(
                '";',
                updateLine,
                '\nbuffer += escapeHTML(lookup("' + trim(source) + '",stack,""));',
                '\nbuffer += "'
            );
        };

        var line = 1, c, callback;
        for (var i = 0, len = template.length; i < len; ++i) {
            if (template.slice(i, i + openTag.length) === openTag) {
                i += openTag.length;
                c = template.substr(i, 1);
                updateLine = '\nline = ' + line + ';';
                nextOpenTag = openTag;
                nextCloseTag = closeTag;
                hasTag = true;

                switch (c) {
                    case "!": // comment
                        i++;
                        callback = null;
                        break;
                    case "=": // change open/close tags, e.g. {{=<% %>=}}
                        i++;
                        closeTag = "=" + closeTag;
                        callback = setTags;
                        break;
                    case ">": // include partial
                        i++;
                        callback = includePartial;
                        break;
                    case "#": // start section
                        i++;
                        callback = openSection;
                        break;
                    case "^": // start inverted section
                        i++;
                        callback = openInvertedSection;
                        break;
                    case "/": // end section
                        i++;
                        callback = closeSection;
                        break;
                    case "{": // plain variable
                        closeTag = "}" + closeTag;
                    // fall through
                    case "&": // plain variable
                        i++;
                        nonSpace = true;
                        callback = sendPlain;
                        break;
                    default: // escaped variable
                        nonSpace = true;
                        callback = sendEscaped;
                }

                var end = template.indexOf(closeTag, i);

                if (end === -1) {
                    throw debug(new Error('Tag "' + openTag + '" was not closed properly'), template, line, options.file);
                }

                var source = template.substring(i, end);

                if (callback) {
                    callback(source);
                }

                // Maintain line count for \n in source.
                var n = 0;
                while (~(n = source.indexOf("\n", n))) {
                    line++;
                    n++;
                }

                i = end + closeTag.length - 1;
                openTag = nextOpenTag;
                closeTag = nextCloseTag;
            } else {
                c = template.substr(i, 1);

                switch (c) {
                    case '"':
                    case "\\":
                        nonSpace = true;
                        code.push("\\" + c);
                        break;
                    case "\r":
                        // Ignore carriage returns.
                        break;
                    case "\n":
                        spaces.push(code.length);
                        code.push("\\n");
                        stripSpace(); // Check for whitespace on the current line.
                        line++;
                        break;
                    default:
                        if (isWhitespace(c)) {
                            spaces.push(code.length);
                        } else {
                            nonSpace = true;
                        }

                        code.push(c);
                }
            }
        }

        if (sectionStack.length != 0) {
            throw debug(new Error('Section "' + sectionStack[sectionStack.length - 1].name + '" was not closed properly'), template, line, options.file);
        }

        // Clean up any whitespace from a closing {{tag}} that was at the end
        // of the template without a trailing \n.
        stripSpace();

        code.push(
            '";',
            "\nreturn buffer;",
            "\n} catch (e) { throw {error: e, line: line}; }"
        );

        // Ignore `buffer += "";` statements.
        var body = code.join("").replace(/buffer \+= "";\n/g, "");

        if (options.debug) {
            if (typeof console != "undefined" && console.log) {
                console.log(body);
            } else if (typeof print === "function") {
                print(body);
            }
        }

        return body;
    }

    /**
     * Used by `compile` to generate a reusable function for the given `template`.
     */
    function _compile(template, options) {
        var args = "view,partials,stack,lookup,escapeHTML,renderSection,render";
        var body = parse(template, options);
        var fn = new Function(args, body);

        // This anonymous function wraps the generated function so we can do
        // argument coercion, setup some variables, and handle any errors
        // encountered while executing it.
        return function (view, partials) {
            partials = partials || {};

            var stack = [view]; // context stack

            try {
                return fn(view, partials, stack, lookup, escapeHTML, renderSection, render);
            } catch (e) {
                throw debug(e.error, template, e.line, options.file);
            }
        };
    }

    // Cache of pre-compiled templates.
    var _cache = {};

    /**
     * Clear the cache of compiled templates.
     */
    function clearCache() {
        _cache = {};
    }

    /**
     * Compiles the given `template` into a reusable function using the given
     * `options`. In addition to the options accepted by Mustache.parse,
     * recognized options include the following:
     *
     *   - cache    Set `false` to bypass any pre-compiled version of the given
     *              template. Otherwise, a given `template` string will be cached
     *              the first time it is parsed
     */
    function compile(template, options) {
        options = options || {};

        // Use a pre-compiled version from the cache if we have one.
        if (options.cache !== false) {
            if (!_cache[template]) {
                _cache[template] = _compile(template, options);
            }

            return _cache[template];
        }

        return _compile(template, options);
    }

    /**
     * High-level function that renders the given `template` using the given
     * `view` and `partials`. If you need to use any of the template options (see
     * `compile` above), you must compile in a separate step, and then call that
     * compiled function.
     */
    function render(template, view, partials) {
        return compile(template)(view, partials);
    }

})(Mustache);

 /* ************************ new file ************************ */
/******************************************************************************
 * jquery.i18n.properties
 *
 * Dual licensed under the GPL (http://dev.jquery.com/browser/trunk/jquery/GPL-LICENSE.txt) and
 * MIT (http://dev.jquery.com/browser/trunk/jquery/MIT-LICENSE.txt) licenses.
 *
 * @version     1.0.x
 * @author      Nuno Fernandes
 * @url         www.codingwithcoffee.com
 * @inspiration Localisation assistance for jQuery (http://keith-wood.name/localisation.html)
 *              by Keith Wood (kbwood{at}iinet.com.au) June 2007
 *
 *****************************************************************************/

(function ($) {
    $.i18n = {};

    /** Map holding bundle keys (if mode: 'map') */
    $.i18n.map = {};

    /**
     * Load and parse message bundle files (.properties),
     * making bundles keys available as javascript variables.
     *
     * i18n files are named <name>.js, or <name>_<language>.js or <name>_<language>_<country>.js
     * Where:
     *      The <language> argument is a valid ISO Language Code. These codes are the lower-case,
     *      two-letter codes as defined by ISO-639. You can find a full list of these codes at a
     *      number of sites, such as: http://www.loc.gov/standards/iso639-2/englangn.html
     *      The <country> argument is a valid ISO Country Code. These codes are the upper-case,
     *      two-letter codes as defined by ISO-3166. You can find a full list of these codes at a
     *      number of sites, such as: http://www.iso.ch/iso/en/prods-services/iso3166ma/02iso-3166-code-lists/list-en1.html
     *
     * Sample usage for a bundles/Messages.properties bundle:
     * $.i18n.properties({
 *      name:      'Messages', 
 *      language:  'en_US',
 *      path:      'bundles'
 * });
     * @param  name         (string/string[], required) names of file to load (eg, 'Messages' or ['Msg1','Msg2'])
     * @param  language     (string, optional) language/country code (eg, 'en', 'en_US', 'pt_PT'). if not specified, language reported by the browser will be used instead.
     * @param  path         (string, optional) path of directory that contains file to load
     * @param  mode         (string, optional) whether bundles keys are available as JavaScript variables/functions or as a map (eg, 'vars' or 'map')
     * @param  callback     (function, optional) callback function to be called after script is terminated
     */
    $.i18n.properties = function (settings) {
        // set up settings
        var defaults = {
            name: 'Messages',
            language: '',
            path: '',
            mode: 'vars',
            callback: function () {
            }
        };
        settings = $.extend(defaults, settings);
        if (settings.language === null || settings.language == '') {
            settings.language = $.i18n.browserLang();//normaliseLanguageCode(navigator.language /* Mozilla */ || navigator.userLanguage /* IE */);
        }
        if (settings.language === null) {
            settings.language = '';
        }

        // load and parse bundle files
        var files = getFiles(settings.name);
        for (i = 0; i < files.length; i++) {
            // 1. load base (eg, Messages.properties)
            loadAndParseFile(settings.path + files[i] + '.properties', settings.language, settings.mode);
            // 2. with language code (eg, Messages_pt.properties)
            if (settings.language.length >= 2) {
                loadAndParseFile(settings.path + files[i] + '_' + settings.language.substring(0, 2) + '.properties', settings.language, settings.mode);
            }
            // 3. with language code and country code (eg, Messages_pt_PT.properties)
            if (settings.language.length >= 5) {
                loadAndParseFile(settings.path + files[i] + '_' + settings.language.substring(0, 5) + '.properties', settings.language, settings.mode);
            }
        }

        // call callback
        if (settings.callback) {
            settings.callback();
        }
    };


    /**
     * When configured with mode: 'map', allows access to bundle values by specifying its key.
     * Eg, jQuery.i18n.prop('com.company.bundles.menu_add')
     */
    $.i18n.prop = function (key, placeHolderValues) {
        var value = $.i18n.map[key];
        if (value == null) {
            return '[' + key + ']';
        }
        if (!placeHolderValues) {
            //if(key == 'spv.lbl.modified') {alert(value);}
            return value;
        } else {
            for (var i = 0; i < placeHolderValues.length; i++) {
                var regexp = new RegExp('\\{(' + i + ')\\}', "g");
                value = value.replace(regexp, placeHolderValues[i]);
            }
            return value;
        }
    };

    /** Language reported by browser, normalized code */
    $.i18n.browserLang = function () {
        return normaliseLanguageCode(navigator.language /* Mozilla */ || navigator.userLanguage /* IE */);
    }


    /** Load and parse .properties files */
    function loadAndParseFile(filename, language, mode) {
        $.ajax({
            url: filename,
            async: false,
            contentType: "text/plain;charset=UTF-8",
            dataType: 'text',
            success: function (data, status) {
                var parsed = '';
                var parameters = data.split(/\n/);
                var regPlaceHolder = /(\{\d+\})/g;
                var regRepPlaceHolder = /\{(\d+)\}/g;
                var unicodeRE = /(\\u.{4})/ig;
                for (var i = 0; i < parameters.length; i++) {
                    parameters[i] = parameters[i].replace(/^\s\s*/, '').replace(/\s\s*$/, ''); // trim
                    if (parameters[i].length > 0 && parameters[i].match("^#") != "#") { // skip comments
                        var pair = parameters[i].split('=');
                        if (pair.length > 0) {
                            /** Process key & value */
                            var name = unescape(pair[0]).replace(/^\s\s*/, '').replace(/\s\s*$/, ''); // trim
                            var value = pair.length == 1 ? "" : pair[1];
                            for (var s = 2; s < pair.length; s++) {
                                value += '=' + pair[s];
                            }
                            value = value.replace(/"/g, '\\"'); // escape quotation mark (")
                            value = value.replace(/^\s\s*/, '').replace(/\s\s*$/, ''); // trim

                            /** Mode: bundle keys in a map */
                            if (mode == 'map' || mode == 'both') {
                                // handle unicode chars possibly left out
                                var unicodeMatches = value.match(unicodeRE);
                                if (unicodeMatches) {
                                    for (var u = 0; u < unicodeMatches.length; u++) {
                                        value = value.replace(unicodeMatches[u], unescapeUnicode(unicodeMatches[u]));
                                    }
                                }
                                // add to map
                                $.i18n.map[name] = value;
                            }

                            /** Mode: bundle keys as vars/functions */
                            if (mode == 'vars' || mode == 'both') {
                                // make sure namespaced key exists (eg, 'some.key')
                                checkKeyNamespace(name);

                                // value with variable substitutions
                                if (regPlaceHolder.test(value)) {
                                    var parts = value.split(regPlaceHolder);
                                    // process function args
                                    var first = true;
                                    var fnArgs = '';
                                    var usedArgs = [];
                                    for (var p = 0; p < parts.length; p++) {
                                        if (regPlaceHolder.test(parts[p]) && usedArgs.indexOf(parts[p]) == -1) {
                                            if (!first) {
                                                fnArgs += ',';
                                            }
                                            fnArgs += parts[p].replace(regRepPlaceHolder, 'v$1');
                                            usedArgs.push(parts[p]);
                                            first = false;
                                        }
                                    }
                                    parsed += name + '=function(' + fnArgs + '){';
                                    // process function body
                                    var fnExpr = '"' + value.replace(regRepPlaceHolder, '"+v$1+"') + '"';
                                    parsed += 'return ' + fnExpr + ';' + '};';

                                    // simple value
                                } else {
                                    parsed += name + '="' + value + '";';
                                }
                            }
                        }
                    }
                }
                eval(parsed);
            }
        });
    }

    /** Make sure namespace exists (for keys with dots in name) */
    function checkKeyNamespace(key) {
        var regDot = /\./;
        if (regDot.test(key)) {
            var fullname = '';
            var names = key.split(/\./);
            for (var i = 0; i < names.length; i++) {
                if (i > 0) {
                    fullname += '.';
                }
                fullname += names[i];
                if (eval('typeof ' + fullname + ' == "undefined"')) {
                    eval(fullname + '={};');
                }
            }
        }
    }

    /** Make sure filename is an array */
    function getFiles(names) {
        return (names && names.constructor == Array) ? names : [names];
    }

    /** Ensure language code is in the format aa_AA. */
    function normaliseLanguageCode(lang) {
        lang = lang.toLowerCase();
        if (lang.length > 3) {
            lang = lang.substring(0, 3) + lang.substring(3).toUpperCase();
        }
        return lang;
    }

    /** Unescape unicode chars ('\u00e3') */
    function unescapeUnicode(str) {
        // unescape unicode codes
        var codes = [];
        var code = parseInt(str.substr(2), 16);
        if (code >= 0 && code < Math.pow(2, 16)) {
            codes.push(code);
        }
        // convert codes to text
        var unescaped = '';
        for (var i = 0; i < codes.length; ++i) {
            unescaped += String.fromCharCode(codes[i]);
        }
        return unescaped;
    }

})(jQuery);

 /* ************************ new file ************************ */
/**
 *
 * jquery.sparkline.js
 *
 * v2.1
 * (c) Splunk, Inc
 * Contact: Gareth Watts (gareth@splunk.com)
 * http://omnipotent.net/jquery.sparkline/
 *
 * Generates inline sparkline charts from data supplied either to the method
 * or inline in HTML
 *
 * Compatible with Internet Explorer 6.0+ and modern browsers equipped with the canvas tag
 * (Firefox 2.0+, Safari, Opera, etc)
 *
 * License: New BSD License
 *
 * Copyright (c) 2012, Splunk Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright notice,
 *       this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of Splunk Inc nor the names of its contributors may
 *       be used to endorse or promote products derived from this software without
 *       specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *
 * Usage:
 *  $(selector).sparkline(values, options)
 *
 * If values is undefined or set to 'html' then the data values are read from the specified tag:
 *   <p>Sparkline: <span class="sparkline">1,4,6,6,8,5,3,5</span></p>
 *   $('.sparkline').sparkline();
 * There must be no spaces in the enclosed data set
 *
 * Otherwise values must be an array of numbers or null values
 *    <p>Sparkline: <span id="sparkline1">This text replaced if the browser is compatible</span></p>
 *    $('#sparkline1').sparkline([1,4,6,6,8,5,3,5])
 *    $('#sparkline2').sparkline([1,4,6,null,null,5,3,5])
 *
 * Values can also be specified in an HTML comment, or as a values attribute:
 *    <p>Sparkline: <span class="sparkline"><!--1,4,6,6,8,5,3,5 --></span></p>
 *    <p>Sparkline: <span class="sparkline" values="1,4,6,6,8,5,3,5"></span></p>
 *    $('.sparkline').sparkline();
 *
 * For line charts, x values can also be specified:
 *   <p>Sparkline: <span class="sparkline">1:1,2.7:4,3.4:6,5:6,6:8,8.7:5,9:3,10:5</span></p>
 *    $('#sparkline1').sparkline([ [1,1], [2.7,4], [3.4,6], [5,6], [6,8], [8.7,5], [9,3], [10,5] ])
 *
 * By default, options should be passed in as teh second argument to the sparkline function:
 *   $('.sparkline').sparkline([1,2,3,4], {type: 'bar'})
 *
 * Options can also be set by passing them on the tag itself.  This feature is disabled by default though
 * as there's a slight performance overhead:
 *   $('.sparkline').sparkline([1,2,3,4], {enableTagOptions: true})
 *   <p>Sparkline: <span class="sparkline" sparkType="bar" sparkBarColor="red">loading</span></p>
 * Prefix all options supplied as tag attribute with "spark" (configurable by setting tagOptionPrefix)
 *
 * Supported options:
 *   lineColor - Color of the line used for the chart
 *   fillColor - Color used to fill in the chart - Set to '' or false for a transparent chart
 *   width - Width of the chart - Defaults to 3 times the number of values in pixels
 *   height - Height of the chart - Defaults to the height of the containing element
 *   chartRangeMin - Specify the minimum value to use for the Y range of the chart - Defaults to the minimum value supplied
 *   chartRangeMax - Specify the maximum value to use for the Y range of the chart - Defaults to the maximum value supplied
 *   chartRangeClip - Clip out of range values to the max/min specified by chartRangeMin and chartRangeMax
 *   chartRangeMinX - Specify the minimum value to use for the X range of the chart - Defaults to the minimum value supplied
 *   chartRangeMaxX - Specify the maximum value to use for the X range of the chart - Defaults to the maximum value supplied
 *   composite - If true then don't erase any existing chart attached to the tag, but draw
 *           another chart over the top - Note that width and height are ignored if an
 *           existing chart is detected.
 *   tagValuesAttribute - Name of tag attribute to check for data values - Defaults to 'values'
 *   enableTagOptions - Whether to check tags for sparkline options
 *   tagOptionPrefix - Prefix used for options supplied as tag attributes - Defaults to 'spark'
 *   disableHiddenCheck - If set to true, then the plugin will assume that charts will never be drawn into a
 *           hidden dom element, avoding a browser reflow
 *   disableInteraction - If set to true then all mouseover/click interaction behaviour will be disabled,
 *       making the plugin perform much like it did in 1.x
 *   disableTooltips - If set to true then tooltips will be disabled - Defaults to false (tooltips enabled)
 *   disableHighlight - If set to true then highlighting of selected chart elements on mouseover will be disabled
 *       defaults to false (highlights enabled)
 *   highlightLighten - Factor to lighten/darken highlighted chart values by - Defaults to 1.4 for a 40% increase
 *   tooltipContainer - Specify which DOM element the tooltip should be rendered into - defaults to document.body
 *   tooltipClassname - Optional CSS classname to apply to tooltips - If not specified then a default style will be applied
 *   tooltipOffsetX - How many pixels away from the mouse pointer to render the tooltip on the X axis
 *   tooltipOffsetY - How many pixels away from the mouse pointer to render the tooltip on the r axis
 *   tooltipFormatter  - Optional callback that allows you to override the HTML displayed in the tooltip
 *       callback is given arguments of (sparkline, options, fields)
 *   tooltipChartTitle - If specified then the tooltip uses the string specified by this setting as a title
 *   tooltipFormat - A format string or SPFormat object  (or an array thereof for multiple entries)
 *       to control the format of the tooltip
 *   tooltipPrefix - A string to prepend to each field displayed in a tooltip
 *   tooltipSuffix - A string to append to each field displayed in a tooltip
 *   tooltipSkipNull - If true then null values will not have a tooltip displayed (defaults to true)
 *   tooltipValueLookups - An object or range map to map field values to tooltip strings
 *       (eg. to map -1 to "Lost", 0 to "Draw", and 1 to "Win")
 *   numberFormatter - Optional callback for formatting numbers in tooltips
 *   numberDigitGroupSep - Character to use for group separator in numbers "1,234" - Defaults to ","
 *   numberDecimalMark - Character to use for the decimal point when formatting numbers - Defaults to "."
 *   numberDigitGroupCount - Number of digits between group separator - Defaults to 3
 *
 * There are 7 types of sparkline, selected by supplying a "type" option of 'line' (default),
 * 'bar', 'tristate', 'bullet', 'discrete', 'pie' or 'box'
 *    line - Line chart.  Options:
 *       spotColor - Set to '' to not end each line in a circular spot
 *       minSpotColor - If set, color of spot at minimum value
 *       maxSpotColor - If set, color of spot at maximum value
 *       spotRadius - Radius in pixels
 *       lineWidth - Width of line in pixels
 *       normalRangeMin
 *       normalRangeMax - If set draws a filled horizontal bar between these two values marking the "normal"
 *                      or expected range of values
 *       normalRangeColor - Color to use for the above bar
 *       drawNormalOnTop - Draw the normal range above the chart fill color if true
 *       defaultPixelsPerValue - Defaults to 3 pixels of width for each value in the chart
 *       highlightSpotColor - The color to use for drawing a highlight spot on mouseover - Set to null to disable
 *       highlightLineColor - The color to use for drawing a highlight line on mouseover - Set to null to disable
 *       valueSpots - Specify which points to draw spots on, and in which color.  Accepts a range map
 *
 *   bar - Bar chart.  Options:
 *       barColor - Color of bars for postive values
 *       negBarColor - Color of bars for negative values
 *       zeroColor - Color of bars with zero values
 *       nullColor - Color of bars with null values - Defaults to omitting the bar entirely
 *       barWidth - Width of bars in pixels
 *       colorMap - Optional mappnig of values to colors to override the *BarColor values above
 *                  can be an Array of values to control the color of individual bars or a range map
 *                  to specify colors for individual ranges of values
 *       barSpacing - Gap between bars in pixels
 *       zeroAxis - Centers the y-axis around zero if true
 *
 *   tristate - Charts values of win (>0), lose (<0) or draw (=0)
 *       posBarColor - Color of win values
 *       negBarColor - Color of lose values
 *       zeroBarColor - Color of draw values
 *       barWidth - Width of bars in pixels
 *       barSpacing - Gap between bars in pixels
 *       colorMap - Optional mappnig of values to colors to override the *BarColor values above
 *                  can be an Array of values to control the color of individual bars or a range map
 *                  to specify colors for individual ranges of values
 *
 *   discrete - Options:
 *       lineHeight - Height of each line in pixels - Defaults to 30% of the graph height
 *       thesholdValue - Values less than this value will be drawn using thresholdColor instead of lineColor
 *       thresholdColor
 *
 *   bullet - Values for bullet graphs msut be in the order: target, performance, range1, range2, range3, ...
 *       options:
 *       targetColor - The color of the vertical target marker
 *       targetWidth - The width of the target marker in pixels
 *       performanceColor - The color of the performance measure horizontal bar
 *       rangeColors - Colors to use for each qualitative range background color
 *
 *   pie - Pie chart. Options:
 *       sliceColors - An array of colors to use for pie slices
 *       offset - Angle in degrees to offset the first slice - Try -90 or +90
 *       borderWidth - Width of border to draw around the pie chart, in pixels - Defaults to 0 (no border)
 *       borderColor - Color to use for the pie chart border - Defaults to #000
 *
 *   box - Box plot. Options:
 *       raw - Set to true to supply pre-computed plot points as values
 *             values should be: low_outlier, low_whisker, q1, median, q3, high_whisker, high_outlier
 *             When set to false you can supply any number of values and the box plot will
 *             be computed for you.  Default is false.
 *       showOutliers - Set to true (default) to display outliers as circles
 *       outlierIQR - Interquartile range used to determine outliers.  Default 1.5
 *       boxLineColor - Outline color of the box
 *       boxFillColor - Fill color for the box
 *       whiskerColor - Line color used for whiskers
 *       outlierLineColor - Outline color of outlier circles
 *       outlierFillColor - Fill color of the outlier circles
 *       spotRadius - Radius of outlier circles
 *       medianColor - Line color of the median line
 *       target - Draw a target cross hair at the supplied value (default undefined)
 *
 *
 *
 *   Examples:
 *   $('#sparkline1').sparkline(myvalues, { lineColor: '#f00', fillColor: false });
 *   $('.barsparks').sparkline('html', { type:'bar', height:'40px', barWidth:5 });
 *   $('#tristate').sparkline([1,1,-1,1,0,0,-1], { type:'tristate' }):
 *   $('#discrete').sparkline([1,3,4,5,5,3,4,5], { type:'discrete' });
 *   $('#bullet').sparkline([10,12,12,9,7], { type:'bullet' });
 *   $('#pie').sparkline([1,1,2], { type:'pie' });
 */

/*jslint regexp: true, browser: true, jquery: true, white: true, nomen: false, plusplus: false, maxerr: 500, indent: 4 */

(function (factory) {
    if (typeof define === 'function' && define.amd) {
        define(['jquery'], factory);
    }
    else {
        factory(jQuery);
    }
}
(function ($) {
    'use strict';

    var UNSET_OPTION = {},
        getDefaults, createClass, SPFormat, clipval, quartile, normalizeValue, normalizeValues,
        remove, isNumber, all, sum, addCSS, ensureArray, formatNumber, RangeMap,
        MouseHandler, Tooltip, barHighlightMixin,
        line, bar, tristate, discrete, bullet, pie, box, defaultStyles, initStyles,
        VShape, VCanvas_base, VCanvas_canvas, VCanvas_vml, pending, shapeCount = 0;

    /**
     * Default configuration settings
     */
    getDefaults = function () {
        return {
            // Settings common to most/all chart types
            common: {
                type: 'line',
                lineColor: '#00f',
                fillColor: '#cdf',
                defaultPixelsPerValue: 3,
                width: 'auto',
                height: 'auto',
                composite: false,
                tagValuesAttribute: 'values',
                tagOptionsPrefix: 'spark',
                enableTagOptions: false,
                enableHighlight: true,
                highlightLighten: 1.4,
                tooltipSkipNull: true,
                tooltipPrefix: '',
                tooltipSuffix: '',
                disableHiddenCheck: false,
                numberFormatter: false,
                numberDigitGroupCount: 3,
                numberDigitGroupSep: ',',
                numberDecimalMark: '.',
                disableTooltips: false,
                disableInteraction: false
            },
            // Defaults for line charts
            line: {
                spotColor: '#f80',
                highlightSpotColor: '#5f5',
                highlightLineColor: '#f22',
                spotRadius: 1.5,
                minSpotColor: '#f80',
                maxSpotColor: '#f80',
                lineWidth: 1,
                normalRangeMin: undefined,
                normalRangeMax: undefined,
                normalRangeColor: '#ccc',
                drawNormalOnTop: false,
                chartRangeMin: undefined,
                chartRangeMax: undefined,
                chartRangeMinX: undefined,
                chartRangeMaxX: undefined,
                tooltipFormat: new SPFormat('<span style="color: {{color}}">&#9679;</span> {{prefix}}{{y}}{{suffix}}')
            },
            // Defaults for bar charts
            bar: {
                barColor: '#3366cc',
                negBarColor: '#f44',
                stackedBarColor: ['#3366cc', '#dc3912', '#ff9900', '#109618', '#66aa00',
                    '#dd4477', '#0099c6', '#990099'],
                zeroColor: undefined,
                nullColor: undefined,
                zeroAxis: true,
                barWidth: 4,
                barSpacing: 1,
                chartRangeMax: undefined,
                chartRangeMin: undefined,
                chartRangeClip: false,
                colorMap: undefined,
                tooltipFormat: new SPFormat('<span style="color: {{color}}">&#9679;</span> {{prefix}}{{value}}{{suffix}}')
            },
            // Defaults for tristate charts
            tristate: {
                barWidth: 4,
                barSpacing: 1,
                posBarColor: '#6f6',
                negBarColor: '#f44',
                zeroBarColor: '#999',
                colorMap: {},
                tooltipFormat: new SPFormat('<span style="color: {{color}}">&#9679;</span> {{value:map}}'),
                tooltipValueLookups: { map: { '-1': 'Loss', '0': 'Draw', '1': 'Win' } }
            },
            // Defaults for discrete charts
            discrete: {
                lineHeight: 'auto',
                thresholdColor: undefined,
                thresholdValue: 0,
                chartRangeMax: undefined,
                chartRangeMin: undefined,
                chartRangeClip: false,
                tooltipFormat: new SPFormat('{{prefix}}{{value}}{{suffix}}')
            },
            // Defaults for bullet charts
            bullet: {
                targetColor: '#f33',
                targetWidth: 3, // width of the target bar in pixels
                performanceColor: '#33f',
                rangeColors: ['#d3dafe', '#a8b6ff', '#7f94ff'],
                base: undefined, // set this to a number to change the base start number
                tooltipFormat: new SPFormat('{{fieldkey:fields}} - {{value}}'),
                tooltipValueLookups: { fields: {r: 'Range', p: 'Performance', t: 'Target'} }
            },
            // Defaults for pie charts
            pie: {
                offset: 0,
                sliceColors: ['#3366cc', '#dc3912', '#ff9900', '#109618', '#66aa00',
                    '#dd4477', '#0099c6', '#990099'],
                borderWidth: 0,
                borderColor: '#000',
                tooltipFormat: new SPFormat('<span style="color: {{color}}">&#9679;</span> {{value}} ({{percent.1}}%)')
            },
            // Defaults for box plots
            box: {
                raw: false,
                boxLineColor: '#000',
                boxFillColor: '#cdf',
                whiskerColor: '#000',
                outlierLineColor: '#333',
                outlierFillColor: '#fff',
                medianColor: '#f00',
                showOutliers: true,
                outlierIQR: 1.5,
                spotRadius: 1.5,
                target: undefined,
                targetColor: '#4a2',
                chartRangeMax: undefined,
                chartRangeMin: undefined,
                tooltipFormat: new SPFormat('{{field:fields}}: {{value}}'),
                tooltipFormatFieldlistKey: 'field',
                tooltipValueLookups: { fields: { lq: 'Lower Quartile', med: 'Median',
                    uq: 'Upper Quartile', lo: 'Left Outlier', ro: 'Right Outlier',
                    lw: 'Left Whisker', rw: 'Right Whisker'} }
            }
        };
    };

    // You can have tooltips use a css class other than jqstooltip by specifying tooltipClassname
    defaultStyles = '.jqstooltip { ' +
        'position: absolute;' +
        'left: 0px;' +
        'top: 0px;' +
        'visibility: hidden;' +
        'background: rgb(0, 0, 0) transparent;' +
        'background-color: rgba(0,0,0,0.6);' +
        'filter:progid:DXImageTransform.Microsoft.gradient(startColorstr=#99000000, endColorstr=#99000000);' +
        '-ms-filter: "progid:DXImageTransform.Microsoft.gradient(startColorstr=#99000000, endColorstr=#99000000)";' +
        'color: white;' +
        'font: 10px arial, san serif;' +
        'text-align: left;' +
        'white-space: nowrap;' +
        'padding: 5px;' +
        'border: 1px solid white;' +
        'z-index: 10000;' +
        '}' +
        '.jqsfield { ' +
        'color: white;' +
        'font: 10px arial, san serif;' +
        'text-align: left;' +
        '}';

    /**
     * Utilities
     */

    createClass = function (/* [baseclass, [mixin, ...]], definition */) {
        var Class, args;
        Class = function () {
            this.init.apply(this, arguments);
        };
        if (arguments.length > 1) {
            if (arguments[0]) {
                Class.prototype = $.extend(new arguments[0](), arguments[arguments.length - 1]);
                Class._super = arguments[0].prototype;
            } else {
                Class.prototype = arguments[arguments.length - 1];
            }
            if (arguments.length > 2) {
                args = Array.prototype.slice.call(arguments, 1, -1);
                args.unshift(Class.prototype);
                $.extend.apply($, args);
            }
        } else {
            Class.prototype = arguments[0];
        }
        Class.prototype.cls = Class;
        return Class;
    };

    /**
     * Wraps a format string for tooltips
     * {{x}}
     * {{x.2}
     * {{x:months}}
     */
    $.SPFormatClass = SPFormat = createClass({
        fre: /\{\{([\w.]+?)(:(.+?))?\}\}/g,
        precre: /(\w+)\.(\d+)/,

        init: function (format, fclass) {
            this.format = format;
            this.fclass = fclass;
        },

        render: function (fieldset, lookups, options) {
            var self = this,
                fields = fieldset,
                match, token, lookupkey, fieldvalue, prec;
            return this.format.replace(this.fre, function () {
                var lookup;
                token = arguments[1];
                lookupkey = arguments[3];
                match = self.precre.exec(token);
                if (match) {
                    prec = match[2];
                    token = match[1];
                } else {
                    prec = false;
                }
                fieldvalue = fields[token];
                if (fieldvalue === undefined) {
                    return '';
                }
                if (lookupkey && lookups && lookups[lookupkey]) {
                    lookup = lookups[lookupkey];
                    if (lookup.get) { // RangeMap
                        return lookups[lookupkey].get(fieldvalue) || fieldvalue;
                    } else {
                        return lookups[lookupkey][fieldvalue] || fieldvalue;
                    }
                }
                if (isNumber(fieldvalue)) {
                    if (options.get('numberFormatter')) {
                        fieldvalue = options.get('numberFormatter')(fieldvalue);
                    } else {
                        fieldvalue = formatNumber(fieldvalue, prec,
                            options.get('numberDigitGroupCount'),
                            options.get('numberDigitGroupSep'),
                            options.get('numberDecimalMark'));
                    }
                }
                return fieldvalue;
            });
        }
    });

    // convience method to avoid needing the new operator
    $.spformat = function (format, fclass) {
        return new SPFormat(format, fclass);
    };

    clipval = function (val, min, max) {
        if (val < min) {
            return min;
        }
        if (val > max) {
            return max;
        }
        return val;
    };

    quartile = function (values, q) {
        var vl;
        if (q === 2) {
            vl = Math.floor(values.length / 2);
            return values.length % 2 ? values[vl] : (values[vl - 1] + values[vl]) / 2;
        } else {
            if (values.length % 2) { // odd
                vl = (values.length * q + q) / 4;
                return vl % 1 ? (values[Math.floor(vl)] + values[Math.floor(vl) - 1]) / 2 : values[vl - 1];
            } else { //even
                vl = (values.length * q + 2) / 4;
                return vl % 1 ? (values[Math.floor(vl)] + values[Math.floor(vl) - 1]) / 2 : values[vl - 1];

            }
        }
    };

    normalizeValue = function (val) {
        var nf;
        switch (val) {
            case 'undefined':
                val = undefined;
                break;
            case 'null':
                val = null;
                break;
            case 'true':
                val = true;
                break;
            case 'false':
                val = false;
                break;
            default:
                nf = parseFloat(val);
                if (val == nf) {
                    val = nf;
                }
        }
        return val;
    };

    normalizeValues = function (vals) {
        var i, result = [];
        for (i = vals.length; i--;) {
            result[i] = normalizeValue(vals[i]);
        }
        return result;
    };

    remove = function (vals, filter) {
        var i, vl, result = [];
        for (i = 0, vl = vals.length; i < vl; i++) {
            if (vals[i] !== filter) {
                result.push(vals[i]);
            }
        }
        return result;
    };

    isNumber = function (num) {
        return !isNaN(parseFloat(num)) && isFinite(num);
    };

    formatNumber = function (num, prec, groupsize, groupsep, decsep) {
        var p, i;
        num = (prec === false ? parseFloat(num).toString() : num.toFixed(prec)).split('');
        p = (p = $.inArray('.', num)) < 0 ? num.length : p;
        if (p < num.length) {
            num[p] = decsep;
        }
        for (i = p - groupsize; i > 0; i -= groupsize) {
            num.splice(i, 0, groupsep);
        }
        return num.join('');
    };

    // determine if all values of an array match a value
    // returns true if the array is empty
    all = function (val, arr, ignoreNull) {
        var i;
        for (i = arr.length; i--;) {
            if (ignoreNull && arr[i] === null) continue;
            if (arr[i] !== val) {
                return false;
            }
        }
        return true;
    };

    // sums the numeric values in an array, ignoring other values
    sum = function (vals) {
        var total = 0, i;
        for (i = vals.length; i--;) {
            total += typeof vals[i] === 'number' ? vals[i] : 0;
        }
        return total;
    };

    ensureArray = function (val) {
        return $.isArray(val) ? val : [val];
    };

    // http://paulirish.com/2008/bookmarklet-inject-new-css-rules/
    addCSS = function (css) {
        var tag;
        //if ('\v' == 'v') /* ie only */ {
        if (document.createStyleSheet) {
            document.createStyleSheet().cssText = css;
        } else {
            tag = document.createElement('style');
            tag.type = 'text/css';
            document.getElementsByTagName('head')[0].appendChild(tag);
            tag[(typeof document.body.style.WebkitAppearance == 'string') /* webkit only */ ? 'innerText' : 'innerHTML'] = css;
        }
    };

    // Provide a cross-browser interface to a few simple drawing primitives
    $.fn.simpledraw = function (width, height, useExisting, interact) {
        var target, mhandler;
        if (useExisting && (target = this.data('_jqs_vcanvas'))) {
            return target;
        }
        if (width === undefined) {
            width = $(this).innerWidth();
        }
        if (height === undefined) {
            height = $(this).innerHeight();
        }
        if ($.browser.hasCanvas) {
            target = new VCanvas_canvas(width, height, this, interact);
        } else if ($.browser.msie) {
            target = new VCanvas_vml(width, height, this);
        } else {
            return false;
        }
        mhandler = $(this).data('_jqs_mhandler');
        if (mhandler) {
            mhandler.registerCanvas(target);
        }
        return target;
    };

    $.fn.cleardraw = function () {
        var target = this.data('_jqs_vcanvas');
        if (target) {
            target.reset();
        }
    };

    $.RangeMapClass = RangeMap = createClass({
        init: function (map) {
            var key, range, rangelist = [];
            for (key in map) {
                if (map.hasOwnProperty(key) && typeof key === 'string' && key.indexOf(':') > -1) {
                    range = key.split(':');
                    range[0] = range[0].length === 0 ? -Infinity : parseFloat(range[0]);
                    range[1] = range[1].length === 0 ? Infinity : parseFloat(range[1]);
                    range[2] = map[key];
                    rangelist.push(range);
                }
            }
            this.map = map;
            this.rangelist = rangelist || false;
        },

        get: function (value) {
            var rangelist = this.rangelist,
                i, range, result;
            if ((result = this.map[value]) !== undefined) {
                return result;
            }
            if (rangelist) {
                for (i = rangelist.length; i--;) {
                    range = rangelist[i];
                    if (range[0] <= value && range[1] >= value) {
                        return range[2];
                    }
                }
            }
            return undefined;
        }
    });

    // Convenience function
    $.range_map = function (map) {
        return new RangeMap(map);
    };

    MouseHandler = createClass({
        init: function (el, options) {
            var $el = $(el);
            this.$el = $el;
            this.options = options;
            this.currentPageX = 0;
            this.currentPageY = 0;
            this.el = el;
            this.splist = [];
            this.tooltip = null;
            this.over = false;
            this.displayTooltips = !options.get('disableTooltips');
            this.highlightEnabled = !options.get('disableHighlight');
        },

        registerSparkline: function (sp) {
            this.splist.push(sp);
            if (this.over) {
                this.updateDisplay();
            }
        },

        registerCanvas: function (canvas) {
            var $canvas = $(canvas.canvas);
            this.canvas = canvas;
            this.$canvas = $canvas;
            $canvas.mouseenter($.proxy(this.mouseenter, this));
            $canvas.mouseleave($.proxy(this.mouseleave, this));
            $canvas.click($.proxy(this.mouseclick, this));
        },

        reset: function (removeTooltip) {
            this.splist = [];
            if (this.tooltip && removeTooltip) {
                this.tooltip.remove();
                this.tooltip = undefined;
            }
        },

        mouseclick: function (e) {
            var clickEvent = $.Event('sparklineClick');
            clickEvent.originalEvent = e;
            clickEvent.sparklines = this.splist;
            this.$el.trigger(clickEvent);
        },

        mouseenter: function (e) {
            $(document.body).unbind('mousemove.jqs');
            $(document.body).bind('mousemove.jqs', $.proxy(this.mousemove, this));
            this.over = true;
            this.currentPageX = e.pageX;
            this.currentPageY = e.pageY;
            this.currentEl = e.target;
            if (!this.tooltip && this.displayTooltips) {
                this.tooltip = new Tooltip(this.options);
                this.tooltip.updatePosition(e.pageX, e.pageY);
            }
            this.updateDisplay();
        },

        mouseleave: function () {
            $(document.body).unbind('mousemove.jqs');
            var splist = this.splist,
                spcount = splist.length,
                needsRefresh = false,
                sp, i;
            this.over = false;
            this.currentEl = null;

            if (this.tooltip) {
                this.tooltip.remove();
                this.tooltip = null;
            }

            for (i = 0; i < spcount; i++) {
                sp = splist[i];
                if (sp.clearRegionHighlight()) {
                    needsRefresh = true;
                }
            }

            if (needsRefresh) {
                this.canvas.render();
            }
        },

        mousemove: function (e) {
            this.currentPageX = e.pageX;
            this.currentPageY = e.pageY;
            this.currentEl = e.target;
            if (this.tooltip) {
                this.tooltip.updatePosition(e.pageX, e.pageY);
            }
            this.updateDisplay();
        },

        updateDisplay: function () {
            var splist = this.splist,
                spcount = splist.length,
                needsRefresh = false,
                offset = this.$canvas.offset(),
                localX = this.currentPageX - offset.left,
                localY = this.currentPageY - offset.top,
                tooltiphtml, sp, i, result, changeEvent;
            if (!this.over) {
                return;
            }
            for (i = 0; i < spcount; i++) {
                sp = splist[i];
                result = sp.setRegionHighlight(this.currentEl, localX, localY);
                if (result) {
                    needsRefresh = true;
                }
            }
            if (needsRefresh) {
                changeEvent = $.Event('sparklineRegionChange');
                changeEvent.sparklines = this.splist;
                this.$el.trigger(changeEvent);
                if (this.tooltip) {
                    tooltiphtml = '';
                    for (i = 0; i < spcount; i++) {
                        sp = splist[i];
                        tooltiphtml += sp.getCurrentRegionTooltip();
                    }
                    this.tooltip.setContent(tooltiphtml);
                }
                if (!this.disableHighlight) {
                    this.canvas.render();
                }
            }
            if (result === null) {
                this.mouseleave();
            }
        }
    });


    Tooltip = createClass({
        sizeStyle: 'position: static !important;' +
            'display: block !important;' +
            'visibility: hidden !important;' +
            'float: left !important;',

        init: function (options) {
            var tooltipClassname = options.get('tooltipClassname', 'jqstooltip'),
                sizetipStyle = this.sizeStyle,
                offset;
            this.container = options.get('tooltipContainer') || document.body;
            this.tooltipOffsetX = options.get('tooltipOffsetX', 10);
            this.tooltipOffsetY = options.get('tooltipOffsetY', 12);
            // remove any previous lingering tooltip
            $('#jqssizetip').remove();
            $('#jqstooltip').remove();
            this.sizetip = $('<div/>', {
                id: 'jqssizetip',
                style: sizetipStyle,
                'class': tooltipClassname
            });
            this.tooltip = $('<div/>', {
                id: 'jqstooltip',
                'class': tooltipClassname
            }).appendTo(this.container);
            // account for the container's location
            offset = this.tooltip.offset();
            this.offsetLeft = offset.left;
            this.offsetTop = offset.top;
            this.hidden = true;
            $(window).unbind('resize.jqs scroll.jqs');
            $(window).bind('resize.jqs scroll.jqs', $.proxy(this.updateWindowDims, this));
            this.updateWindowDims();
        },

        updateWindowDims: function () {
            this.scrollTop = $(window).scrollTop();
            this.scrollLeft = $(window).scrollLeft();
            this.scrollRight = this.scrollLeft + $(window).width();
            this.updatePosition();
        },

        getSize: function (content) {
            this.sizetip.html(content).appendTo(this.container);
            this.width = this.sizetip.width() + 1;
            this.height = this.sizetip.height();
            this.sizetip.remove();
        },

        setContent: function (content) {
            if (!content) {
                this.tooltip.css('visibility', 'hidden');
                this.hidden = true;
                return;
            }
            this.getSize(content);
            this.tooltip.html(content)
                .css({
                    'width': this.width,
                    'height': this.height,
                    'visibility': 'visible'
                });
            if (this.hidden) {
                this.hidden = false;
                this.updatePosition();
            }
        },

        updatePosition: function (x, y) {
            if (x === undefined) {
                if (this.mousex === undefined) {
                    return;
                }
                x = this.mousex - this.offsetLeft;
                y = this.mousey - this.offsetTop;

            } else {
                this.mousex = x = x - this.offsetLeft;
                this.mousey = y = y - this.offsetTop;
            }
            if (!this.height || !this.width || this.hidden) {
                return;
            }

            y -= this.height + this.tooltipOffsetY;
            x += this.tooltipOffsetX;

            if (y < this.scrollTop) {
                y = this.scrollTop;
            }
            if (x < this.scrollLeft) {
                x = this.scrollLeft;
            } else if (x + this.width > this.scrollRight) {
                x = this.scrollRight - this.width;
            }

            this.tooltip.css({
                'left': x,
                'top': y
            });
        },

        remove: function () {
            this.tooltip.remove();
            this.sizetip.remove();
            this.sizetip = this.tooltip = undefined;
            $(window).unbind('resize.jqs scroll.jqs');
        }
    });

    initStyles = function () {
        addCSS(defaultStyles);
    };

    $(initStyles);

    pending = [];
    $.fn.sparkline = function (userValues, userOptions) {
        return this.each(function () {
            var options = new $.fn.sparkline.options(this, userOptions),
                $this = $(this),
                render, i;
            render = function () {
                var values, width, height, tmp, mhandler, sp, vals;
                if (userValues === 'html' || userValues === undefined) {
                    vals = this.getAttribute(options.get('tagValuesAttribute'));
                    if (vals === undefined || vals === null) {
                        vals = $this.html();
                    }
                    values = vals.replace(/(^\s*<!--)|(-->\s*$)|\s+/g, '').split(',');
                } else {
                    values = userValues;
                }

                width = options.get('width') === 'auto' ? values.length * options.get('defaultPixelsPerValue') : options.get('width');
                if (options.get('height') === 'auto') {
                    if (!options.get('composite') || !$.data(this, '_jqs_vcanvas')) {
                        // must be a better way to get the line height
                        tmp = document.createElement('span');
                        tmp.innerHTML = 'a';
                        $this.html(tmp);
                        height = $(tmp).innerHeight() || $(tmp).height();
                        $(tmp).remove();
                        tmp = null;
                    }
                } else {
                    height = options.get('height');
                }

                if (!options.get('disableInteraction')) {
                    mhandler = $.data(this, '_jqs_mhandler');
                    if (!mhandler) {
                        mhandler = new MouseHandler(this, options);
                        $.data(this, '_jqs_mhandler', mhandler);
                    } else if (!options.get('composite')) {
                        mhandler.reset();
                    }
                } else {
                    mhandler = false;
                }

                if (options.get('composite') && !$.data(this, '_jqs_vcanvas')) {
                    if (!$.data(this, '_jqs_errnotify')) {
                        alert('Attempted to attach a composite sparkline to an element with no existing sparkline');
                        $.data(this, '_jqs_errnotify', true);
                    }
                    return;
                }

                sp = new $.fn.sparkline[options.get('type')](this, values, options, width, height);

                sp.render();

                if (mhandler) {
                    mhandler.registerSparkline(sp);
                }
            };
            // jQuery 1.3.0 completely changed the meaning of :hidden :-/
            if (($(this).html() && !options.get('disableHiddenCheck') && $(this).is(':hidden')) || ($.fn.jquery < '1.3.0' && $(this).parents().is(':hidden')) || !$(this).parents('body').length) {
                if (!options.get('composite') && $.data(this, '_jqs_pending')) {
                    // remove any existing references to the element
                    for (i = pending.length; i; i--) {
                        if (pending[i - 1][0] == this) {
                            pending.splice(i - 1, 1);
                        }
                    }
                }
                pending.push([this, render]);
                $.data(this, '_jqs_pending', true);
            } else {
                render.call(this);
            }
        });
    };

    $.fn.sparkline.defaults = getDefaults();


    $.sparkline_display_visible = function () {
        var el, i, pl;
        var done = [];
        for (i = 0, pl = pending.length; i < pl; i++) {
            el = pending[i][0];
            if ($(el).is(':visible') && !$(el).parents().is(':hidden')) {
                pending[i][1].call(el);
                $.data(pending[i][0], '_jqs_pending', false);
                done.push(i);
            } else if (!$(el).closest('html').length && !$.data(el, '_jqs_pending')) {
                // element has been inserted and removed from the DOM
                // If it was not yet inserted into the dom then the .data request
                // will return true.
                // removing from the dom causes the data to be removed.
                $.data(pending[i][0], '_jqs_pending', false);
                done.push(i);
            }
        }
        for (i = done.length; i; i--) {
            pending.splice(done[i - 1], 1);
        }
    };


    /**
     * User option handler
     */
    $.fn.sparkline.options = createClass({
        init: function (tag, userOptions) {
            var extendedOptions, defaults, base, tagOptionType;
            this.userOptions = userOptions = userOptions || {};
            this.tag = tag;
            this.tagValCache = {};
            defaults = $.fn.sparkline.defaults;
            base = defaults.common;
            this.tagOptionsPrefix = userOptions.enableTagOptions && (userOptions.tagOptionsPrefix || base.tagOptionsPrefix);

            tagOptionType = this.getTagSetting('type');
            if (tagOptionType === UNSET_OPTION) {
                extendedOptions = defaults[userOptions.type || base.type];
            } else {
                extendedOptions = defaults[tagOptionType];
            }
            this.mergedOptions = $.extend({}, base, extendedOptions, userOptions);
        },


        getTagSetting: function (key) {
            var prefix = this.tagOptionsPrefix,
                val, i, pairs, keyval;
            if (prefix === false || prefix === undefined) {
                return UNSET_OPTION;
            }
            if (this.tagValCache.hasOwnProperty(key)) {
                val = this.tagValCache.key;
            } else {
                val = this.tag.getAttribute(prefix + key);
                if (val === undefined || val === null) {
                    val = UNSET_OPTION;
                } else if (val.substr(0, 1) === '[') {
                    val = val.substr(1, val.length - 2).split(',');
                    for (i = val.length; i--;) {
                        val[i] = normalizeValue(val[i].replace(/(^\s*)|(\s*$)/g, ''));
                    }
                } else if (val.substr(0, 1) === '{') {
                    pairs = val.substr(1, val.length - 2).split(',');
                    val = {};
                    for (i = pairs.length; i--;) {
                        keyval = pairs[i].split(':', 2);
                        val[keyval[0].replace(/(^\s*)|(\s*$)/g, '')] = normalizeValue(keyval[1].replace(/(^\s*)|(\s*$)/g, ''));
                    }
                } else {
                    val = normalizeValue(val);
                }
                this.tagValCache.key = val;
            }
            return val;
        },

        get: function (key, defaultval) {
            var tagOption = this.getTagSetting(key),
                result;
            if (tagOption !== UNSET_OPTION) {
                return tagOption;
            }
            return (result = this.mergedOptions[key]) === undefined ? defaultval : result;
        }
    });


    $.fn.sparkline._base = createClass({
        disabled: false,

        init: function (el, values, options, width, height) {
            this.el = el;
            this.$el = $(el);
            this.values = values;
            this.options = options;
            this.width = width;
            this.height = height;
            this.currentRegion = undefined;
        },

        /**
         * Setup the canvas
         */
        initTarget: function () {
            var interactive = !this.options.get('disableInteraction');
            if (!(this.target = this.$el.simpledraw(this.width, this.height, this.options.get('composite'), interactive))) {
                this.disabled = true;
            } else {
                this.canvasWidth = this.target.pixelWidth;
                this.canvasHeight = this.target.pixelHeight;
            }
        },

        /**
         * Actually render the chart to the canvas
         */
        render: function () {
            if (this.disabled) {
                this.el.innerHTML = '';
                return false;
            }
            return true;
        },

        /**
         * Return a region id for a given x/y co-ordinate
         */
        getRegion: function (x, y) {
        },

        /**
         * Highlight an item based on the moused-over x,y co-ordinate
         */
        setRegionHighlight: function (el, x, y) {
            var currentRegion = this.currentRegion,
                highlightEnabled = !this.options.get('disableHighlight'),
                newRegion;
            if (x > this.canvasWidth || y > this.canvasHeight || x < 0 || y < 0) {
                return null;
            }
            newRegion = this.getRegion(el, x, y);
            if (currentRegion !== newRegion) {
                if (currentRegion !== undefined && highlightEnabled) {
                    this.removeHighlight();
                }
                this.currentRegion = newRegion;
                if (newRegion !== undefined && highlightEnabled) {
                    this.renderHighlight();
                }
                return true;
            }
            return false;
        },

        /**
         * Reset any currently highlighted item
         */
        clearRegionHighlight: function () {
            if (this.currentRegion !== undefined) {
                this.removeHighlight();
                this.currentRegion = undefined;
                return true;
            }
            return false;
        },

        renderHighlight: function () {
            this.changeHighlight(true);
        },

        removeHighlight: function () {
            this.changeHighlight(false);
        },

        changeHighlight: function (highlight) {
        },

        /**
         * Fetch the HTML to display as a tooltip
         */
        getCurrentRegionTooltip: function () {
            var options = this.options,
                header = '',
                entries = [],
                fields, formats, formatlen, fclass, text, i,
                showFields, showFieldsKey, newFields, fv,
                formatter, format, fieldlen, j;
            if (this.currentRegion === undefined) {
                return '';
            }
            fields = this.getCurrentRegionFields();
            formatter = options.get('tooltipFormatter');
            if (formatter) {
                return formatter(this, options, fields);
            }
            if (options.get('tooltipChartTitle')) {
                header += '<div class="jqs jqstitle">' + options.get('tooltipChartTitle') + '</div>\n';
            }
            formats = this.options.get('tooltipFormat');
            if (!formats) {
                return '';
            }
            if (!$.isArray(formats)) {
                formats = [formats];
            }
            if (!$.isArray(fields)) {
                fields = [fields];
            }
            showFields = this.options.get('tooltipFormatFieldlist');
            showFieldsKey = this.options.get('tooltipFormatFieldlistKey');
            if (showFields && showFieldsKey) {
                // user-selected ordering of fields
                newFields = [];
                for (i = fields.length; i--;) {
                    fv = fields[i][showFieldsKey];
                    if ((j = $.inArray(fv, showFields)) != -1) {
                        newFields[j] = fields[i];
                    }
                }
                fields = newFields;
            }
            formatlen = formats.length;
            fieldlen = fields.length;
            for (i = 0; i < formatlen; i++) {
                format = formats[i];
                if (typeof format === 'string') {
                    format = new SPFormat(format);
                }
                fclass = format.fclass || 'jqsfield';
                for (j = 0; j < fieldlen; j++) {
                    if (!fields[j].isNull || !options.get('tooltipSkipNull')) {
                        $.extend(fields[j], {
                            prefix: options.get('tooltipPrefix'),
                            suffix: options.get('tooltipSuffix')
                        });
                        text = format.render(fields[j], options.get('tooltipValueLookups'), options);
                        entries.push('<div class="' + fclass + '">' + text + '</div>');
                    }
                }
            }
            if (entries.length) {
                return header + entries.join('\n');
            }
            return '';
        },

        getCurrentRegionFields: function () {
        },

        calcHighlightColor: function (color, options) {
            var highlightColor = options.get('highlightColor'),
                lighten = options.get('highlightLighten'),
                parse, mult, rgbnew, i;
            if (highlightColor) {
                return highlightColor;
            }
            if (lighten) {
                // extract RGB values
                parse = /^#([0-9a-f])([0-9a-f])([0-9a-f])$/i.exec(color) || /^#([0-9a-f]{2})([0-9a-f]{2})([0-9a-f]{2})$/i.exec(color);
                if (parse) {
                    rgbnew = [];
                    mult = color.length === 4 ? 16 : 1;
                    for (i = 0; i < 3; i++) {
                        rgbnew[i] = clipval(Math.round(parseInt(parse[i + 1], 16) * mult * lighten), 0, 255);
                    }
                    return 'rgb(' + rgbnew.join(',') + ')';
                }

            }
            return color;
        }

    });

    barHighlightMixin = {
        changeHighlight: function (highlight) {
            var currentRegion = this.currentRegion,
                target = this.target,
                shapeids = this.regionShapes[currentRegion],
                newShapes;
            // will be null if the region value was null
            if (shapeids) {
                newShapes = this.renderRegion(currentRegion, highlight);
                if ($.isArray(newShapes) || $.isArray(shapeids)) {
                    target.replaceWithShapes(shapeids, newShapes);
                    this.regionShapes[currentRegion] = $.map(newShapes, function (newShape) {
                        return newShape.id;
                    });
                } else {
                    target.replaceWithShape(shapeids, newShapes);
                    this.regionShapes[currentRegion] = newShapes.id;
                }
            }
        },

        render: function () {
            var values = this.values,
                target = this.target,
                regionShapes = this.regionShapes,
                shapes, ids, i, j;

            if (!this.cls._super.render.call(this)) {
                return;
            }
            for (i = values.length; i--;) {
                shapes = this.renderRegion(i);
                if (shapes) {
                    if ($.isArray(shapes)) {
                        ids = [];
                        for (j = shapes.length; j--;) {
                            shapes[j].append();
                            ids.push(shapes[j].id);
                        }
                        regionShapes[i] = ids;
                    } else {
                        shapes.append();
                        regionShapes[i] = shapes.id; // store just the shapeid
                    }
                } else {
                    // null value
                    regionShapes[i] = null;
                }
            }
            target.render();
        }
    };

    /**
     * Line charts
     */
    $.fn.sparkline.line = line = createClass($.fn.sparkline._base, {
        type: 'line',

        init: function (el, values, options, width, height) {
            line._super.init.call(this, el, values, options, width, height);
            this.vertices = [];
            this.regionMap = [];
            this.xvalues = [];
            this.yvalues = [];
            this.yminmax = [];
            this.hightlightSpotId = null;
            this.lastShapeId = null;
            this.initTarget();
        },

        getRegion: function (el, x, y) {
            var i,
                regionMap = this.regionMap; // maps regions to value positions
            for (i = regionMap.length; i--;) {
                if (regionMap[i] !== null && x >= regionMap[i][0] && x <= regionMap[i][1]) {
                    return regionMap[i][2];
                }
            }
            return undefined;
        },

        getCurrentRegionFields: function () {
            var currentRegion = this.currentRegion;
            return {
                isNull: this.yvalues[currentRegion] === null,
                x: this.xvalues[currentRegion],
                y: this.yvalues[currentRegion],
                color: this.options.get('lineColor'),
                fillColor: this.options.get('fillColor'),
                offset: currentRegion
            };
        },

        renderHighlight: function () {
            var currentRegion = this.currentRegion,
                target = this.target,
                vertex = this.vertices[currentRegion],
                options = this.options,
                spotRadius = options.get('spotRadius'),
                highlightSpotColor = options.get('highlightSpotColor'),
                highlightLineColor = options.get('highlightLineColor'),
                highlightSpot, highlightLine;

            if (!vertex) {
                return;
            }
            if (spotRadius && highlightSpotColor) {
                highlightSpot = target.drawCircle(vertex[0], vertex[1],
                    spotRadius, undefined, highlightSpotColor);
                this.highlightSpotId = highlightSpot.id;
                target.insertAfterShape(this.lastShapeId, highlightSpot);
            }
            if (highlightLineColor) {
                highlightLine = target.drawLine(vertex[0], this.canvasTop, vertex[0],
                    this.canvasTop + this.canvasHeight, highlightLineColor);
                this.highlightLineId = highlightLine.id;
                target.insertAfterShape(this.lastShapeId, highlightLine);
            }
        },

        removeHighlight: function () {
            var target = this.target;
            if (this.highlightSpotId) {
                target.removeShapeId(this.highlightSpotId);
                this.highlightSpotId = null;
            }
            if (this.highlightLineId) {
                target.removeShapeId(this.highlightLineId);
                this.highlightLineId = null;
            }
        },

        scanValues: function () {
            var values = this.values,
                valcount = values.length,
                xvalues = this.xvalues,
                yvalues = this.yvalues,
                yminmax = this.yminmax,
                i, val, isStr, isArray, sp;
            for (i = 0; i < valcount; i++) {
                val = values[i];
                isStr = typeof(values[i]) === 'string';
                isArray = typeof(values[i]) === 'object' && values[i] instanceof Array;
                sp = isStr && values[i].split(':');
                if (isStr && sp.length === 2) { // x:y
                    xvalues.push(Number(sp[0]));
                    yvalues.push(Number(sp[1]));
                    yminmax.push(Number(sp[1]));
                } else if (isArray) {
                    xvalues.push(val[0]);
                    yvalues.push(val[1]);
                    yminmax.push(val[1]);
                } else {
                    xvalues.push(i);
                    if (values[i] === null || values[i] === 'null') {
                        yvalues.push(null);
                    } else {
                        yvalues.push(Number(val));
                        yminmax.push(Number(val));
                    }
                }
            }
            if (this.options.get('xvalues')) {
                xvalues = this.options.get('xvalues');
            }

            this.maxy = this.maxyorg = Math.max.apply(Math, yminmax);
            this.miny = this.minyorg = Math.min.apply(Math, yminmax);

            this.maxx = Math.max.apply(Math, xvalues);
            this.minx = Math.min.apply(Math, xvalues);

            this.xvalues = xvalues;
            this.yvalues = yvalues;
            this.yminmax = yminmax;

        },

        processRangeOptions: function () {
            var options = this.options,
                normalRangeMin = options.get('normalRangeMin'),
                normalRangeMax = options.get('normalRangeMax');

            if (normalRangeMin !== undefined) {
                if (normalRangeMin < this.miny) {
                    this.miny = normalRangeMin;
                }
                if (normalRangeMax > this.maxy) {
                    this.maxy = normalRangeMax;
                }
            }
            if (options.get('chartRangeMin') !== undefined && (options.get('chartRangeClip') || options.get('chartRangeMin') < this.miny)) {
                this.miny = options.get('chartRangeMin');
            }
            if (options.get('chartRangeMax') !== undefined && (options.get('chartRangeClip') || options.get('chartRangeMax') > this.maxy)) {
                this.maxy = options.get('chartRangeMax');
            }
            if (options.get('chartRangeMinX') !== undefined && (options.get('chartRangeClipX') || options.get('chartRangeMinX') < this.minx)) {
                this.minx = options.get('chartRangeMinX');
            }
            if (options.get('chartRangeMaxX') !== undefined && (options.get('chartRangeClipX') || options.get('chartRangeMaxX') > this.maxx)) {
                this.maxx = options.get('chartRangeMaxX');
            }

        },

        drawNormalRange: function (canvasLeft, canvasTop, canvasHeight, canvasWidth, rangey) {
            var normalRangeMin = this.options.get('normalRangeMin'),
                normalRangeMax = this.options.get('normalRangeMax'),
                ytop = canvasTop + Math.round(canvasHeight - (canvasHeight * ((normalRangeMax - this.miny) / rangey))),
                height = Math.round((canvasHeight * (normalRangeMax - normalRangeMin)) / rangey);
            this.target.drawRect(canvasLeft, ytop, canvasWidth, height, undefined, this.options.get('normalRangeColor')).append();
        },

        render: function () {
            var options = this.options,
                target = this.target,
                canvasWidth = this.canvasWidth,
                canvasHeight = this.canvasHeight,
                vertices = this.vertices,
                spotRadius = options.get('spotRadius'),
                regionMap = this.regionMap,
                rangex, rangey, yvallast,
                canvasTop, canvasLeft,
                vertex, path, paths, x, y, xnext, xpos, xposnext,
                last, next, yvalcount, lineShapes, fillShapes, plen,
                valueSpots, hlSpotsEnabled, color, xvalues, yvalues, i;

            if (!line._super.render.call(this)) {
                return;
            }

            this.scanValues();
            this.processRangeOptions();

            xvalues = this.xvalues;
            yvalues = this.yvalues;

            if (!this.yminmax.length || this.yvalues.length < 2) {
                // empty or all null valuess
                return;
            }

            canvasTop = canvasLeft = 0;

            rangex = this.maxx - this.minx === 0 ? 1 : this.maxx - this.minx;
            rangey = this.maxy - this.miny === 0 ? 1 : this.maxy - this.miny;
            yvallast = this.yvalues.length - 1;

            if (spotRadius && (canvasWidth < (spotRadius * 4) || canvasHeight < (spotRadius * 4))) {
                spotRadius = 0;
            }
            if (spotRadius) {
                // adjust the canvas size as required so that spots will fit
                hlSpotsEnabled = options.get('highlightSpotColor') && !options.get('disableInteraction');
                if (hlSpotsEnabled || options.get('minSpotColor') || (options.get('spotColor') && yvalues[yvallast] === this.miny)) {
                    canvasHeight -= Math.ceil(spotRadius);
                }
                if (hlSpotsEnabled || options.get('maxSpotColor') || (options.get('spotColor') && yvalues[yvallast] === this.maxy)) {
                    canvasHeight -= Math.ceil(spotRadius);
                    canvasTop += Math.ceil(spotRadius);
                }
                if (hlSpotsEnabled ||
                    ((options.get('minSpotColor') || options.get('maxSpotColor')) && (yvalues[0] === this.miny || yvalues[0] === this.maxy))) {
                    canvasLeft += Math.ceil(spotRadius);
                    canvasWidth -= Math.ceil(spotRadius);
                }
                if (hlSpotsEnabled || options.get('spotColor') ||
                    (options.get('minSpotColor') || options.get('maxSpotColor') &&
                        (yvalues[yvallast] === this.miny || yvalues[yvallast] === this.maxy))) {
                    canvasWidth -= Math.ceil(spotRadius);
                }
            }


            canvasHeight--;

            if (options.get('normalRangeMin') !== undefined && !options.get('drawNormalOnTop')) {
                this.drawNormalRange(canvasLeft, canvasTop, canvasHeight, canvasWidth, rangey);
            }

            path = [];
            paths = [path];
            last = next = null;
            yvalcount = yvalues.length;
            for (i = 0; i < yvalcount; i++) {
                x = xvalues[i];
                xnext = xvalues[i + 1];
                y = yvalues[i];
                xpos = canvasLeft + Math.round((x - this.minx) * (canvasWidth / rangex));
                xposnext = i < yvalcount - 1 ? canvasLeft + Math.round((xnext - this.minx) * (canvasWidth / rangex)) : canvasWidth;
                next = xpos + ((xposnext - xpos) / 2);
                regionMap[i] = [last || 0, next, i];
                last = next;
                if (y === null) {
                    if (i) {
                        if (yvalues[i - 1] !== null) {
                            path = [];
                            paths.push(path);
                        }
                        vertices.push(null);
                    }
                } else {
                    if (y < this.miny) {
                        y = this.miny;
                    }
                    if (y > this.maxy) {
                        y = this.maxy;
                    }
                    if (!path.length) {
                        // previous value was null
                        path.push([xpos, canvasTop + canvasHeight]);
                    }
                    vertex = [xpos, canvasTop + Math.round(canvasHeight - (canvasHeight * ((y - this.miny) / rangey)))];
                    path.push(vertex);
                    vertices.push(vertex);
                }
            }

            lineShapes = [];
            fillShapes = [];
            plen = paths.length;
            for (i = 0; i < plen; i++) {
                path = paths[i];
                if (path.length) {
                    if (options.get('fillColor')) {
                        path.push([path[path.length - 1][0], (canvasTop + canvasHeight)]);
                        fillShapes.push(path.slice(0));
                        path.pop();
                    }
                    // if there's only a single point in this path, then we want to display it
                    // as a vertical line which means we keep path[0]  as is
                    if (path.length > 2) {
                        // else we want the first value
                        path[0] = [path[0][0], path[1][1]];
                    }
                    lineShapes.push(path);
                }
            }

            // draw the fill first, then optionally the normal range, then the line on top of that
            plen = fillShapes.length;
            for (i = 0; i < plen; i++) {
                target.drawShape(fillShapes[i],
                    options.get('fillColor'), options.get('fillColor')).append();
            }

            if (options.get('normalRangeMin') !== undefined && options.get('drawNormalOnTop')) {
                this.drawNormalRange(canvasLeft, canvasTop, canvasHeight, canvasWidth, rangey);
            }

            plen = lineShapes.length;
            for (i = 0; i < plen; i++) {
                target.drawShape(lineShapes[i], options.get('lineColor'), undefined,
                    options.get('lineWidth')).append();
            }

            if (spotRadius && options.get('valueSpots')) {
                valueSpots = options.get('valueSpots');
                if (valueSpots.get === undefined) {
                    valueSpots = new RangeMap(valueSpots);
                }
                for (i = 0; i < yvalcount; i++) {
                    color = valueSpots.get(yvalues[i]);
                    if (color) {
                        target.drawCircle(canvasLeft + Math.round((xvalues[i] - this.minx) * (canvasWidth / rangex)),
                            canvasTop + Math.round(canvasHeight - (canvasHeight * ((yvalues[i] - this.miny) / rangey))),
                            spotRadius, undefined,
                            color).append();
                    }
                }

            }
            if (spotRadius && options.get('spotColor')) {
                target.drawCircle(canvasLeft + Math.round((xvalues[xvalues.length - 1] - this.minx) * (canvasWidth / rangex)),
                    canvasTop + Math.round(canvasHeight - (canvasHeight * ((yvalues[yvallast] - this.miny) / rangey))),
                    spotRadius, undefined,
                    options.get('spotColor')).append();
            }
            if (this.maxy !== this.minyorg) {
                if (spotRadius && options.get('minSpotColor')) {
                    x = xvalues[$.inArray(this.minyorg, yvalues)];
                    target.drawCircle(canvasLeft + Math.round((x - this.minx) * (canvasWidth / rangex)),
                        canvasTop + Math.round(canvasHeight - (canvasHeight * ((this.minyorg - this.miny) / rangey))),
                        spotRadius, undefined,
                        options.get('minSpotColor')).append();
                }
                if (spotRadius && options.get('maxSpotColor')) {
                    x = xvalues[$.inArray(this.maxyorg, yvalues)];
                    target.drawCircle(canvasLeft + Math.round((x - this.minx) * (canvasWidth / rangex)),
                        canvasTop + Math.round(canvasHeight - (canvasHeight * ((this.maxyorg - this.miny) / rangey))),
                        spotRadius, undefined,
                        options.get('maxSpotColor')).append();
                }
            }

            this.lastShapeId = target.getLastShapeId();
            this.canvasTop = canvasTop;
            target.render();
        }
    });

    /**
     * Bar charts
     */
    $.fn.sparkline.bar = bar = createClass($.fn.sparkline._base, barHighlightMixin, {
        type: 'bar',

        init: function (el, values, options, width, height) {
            var barWidth = parseInt(options.get('barWidth'), 10),
                barSpacing = parseInt(options.get('barSpacing'), 10),
                chartRangeMin = options.get('chartRangeMin'),
                chartRangeMax = options.get('chartRangeMax'),
                chartRangeClip = options.get('chartRangeClip'),
                stackMin = Infinity,
                stackMax = -Infinity,
                isStackString, groupMin, groupMax, stackRanges,
                numValues, i, vlen, range, zeroAxis, xaxisOffset, min, max, clipMin, clipMax,
                stacked, vlist, j, slen, svals, val, yoffset, yMaxCalc, canvasHeightEf;
            bar._super.init.call(this, el, values, options, width, height);

            // scan values to determine whether to stack bars
            for (i = 0, vlen = values.length; i < vlen; i++) {
                val = values[i];
                isStackString = typeof(val) === 'string' && val.indexOf(':') > -1;
                if (isStackString || $.isArray(val)) {
                    stacked = true;
                    if (isStackString) {
                        val = values[i] = normalizeValues(val.split(':'));
                    }
                    val = remove(val, null); // min/max will treat null as zero
                    groupMin = Math.min.apply(Math, val);
                    groupMax = Math.max.apply(Math, val);
                    if (groupMin < stackMin) {
                        stackMin = groupMin;
                    }
                    if (groupMax > stackMax) {
                        stackMax = groupMax;
                    }
                }
            }

            this.stacked = stacked;
            this.regionShapes = {};
            this.barWidth = barWidth;
            this.barSpacing = barSpacing;
            this.totalBarWidth = barWidth + barSpacing;
            this.width = width = (values.length * barWidth) + ((values.length - 1) * barSpacing);

            this.initTarget();

            if (chartRangeClip) {
                clipMin = chartRangeMin === undefined ? -Infinity : chartRangeMin;
                clipMax = chartRangeMax === undefined ? Infinity : chartRangeMax;
            }

            numValues = [];
            stackRanges = stacked ? [] : numValues;
            var stackTotals = [];
            var stackRangesNeg = [];
            for (i = 0, vlen = values.length; i < vlen; i++) {
                if (stacked) {
                    vlist = values[i];
                    values[i] = svals = [];
                    stackTotals[i] = 0;
                    stackRanges[i] = stackRangesNeg[i] = 0;
                    for (j = 0, slen = vlist.length; j < slen; j++) {
                        val = svals[j] = chartRangeClip ? clipval(vlist[j], clipMin, clipMax) : vlist[j];
                        if (val !== null) {
                            if (val > 0) {
                                stackTotals[i] += val;
                            }
                            if (stackMin < 0 && stackMax > 0) {
                                if (val < 0) {
                                    stackRangesNeg[i] += Math.abs(val);
                                } else {
                                    stackRanges[i] += val;
                                }
                            } else {
                                stackRanges[i] += Math.abs(val - (val < 0 ? stackMax : stackMin));
                            }
                            numValues.push(val);
                        }
                    }
                } else {
                    val = chartRangeClip ? clipval(values[i], clipMin, clipMax) : values[i];
                    val = values[i] = normalizeValue(val);
                    if (val !== null) {
                        numValues.push(val);
                    }
                }
            }
            this.max = max = Math.max.apply(Math, numValues);
            this.min = min = Math.min.apply(Math, numValues);
            this.stackMax = stackMax = stacked ? Math.max.apply(Math, stackTotals) : max;
            this.stackMin = stackMin = stacked ? Math.min.apply(Math, numValues) : min;

            if (options.get('chartRangeMin') !== undefined && (options.get('chartRangeClip') || options.get('chartRangeMin') < min)) {
                min = options.get('chartRangeMin');
            }
            if (options.get('chartRangeMax') !== undefined && (options.get('chartRangeClip') || options.get('chartRangeMax') > max)) {
                max = options.get('chartRangeMax');
            }

            this.zeroAxis = zeroAxis = options.get('zeroAxis', true);
            if (min <= 0 && max >= 0 && zeroAxis) {
                xaxisOffset = 0;
            } else if (zeroAxis == false) {
                xaxisOffset = min;
            } else if (min > 0) {
                xaxisOffset = min;
            } else {
                xaxisOffset = max;
            }
            this.xaxisOffset = xaxisOffset;

            range = stacked ? (Math.max.apply(Math, stackRanges) + Math.max.apply(Math, stackRangesNeg)) : max - min;

            // as we plot zero/min values a single pixel line, we add a pixel to all other
            // values - Reduce the effective canvas size to suit
            this.canvasHeightEf = (zeroAxis && min < 0) ? this.canvasHeight - 2 : this.canvasHeight - 1;

            if (min < xaxisOffset) {
                yMaxCalc = (stacked && max >= 0) ? stackMax : max;
                yoffset = (yMaxCalc - xaxisOffset) / range * this.canvasHeight;
                if (yoffset !== Math.ceil(yoffset)) {
                    this.canvasHeightEf -= 2;
                    yoffset = Math.ceil(yoffset);
                }
            } else {
                yoffset = this.canvasHeight;
            }
            this.yoffset = yoffset;

            if ($.isArray(options.get('colorMap'))) {
                this.colorMapByIndex = options.get('colorMap');
                this.colorMapByValue = null;
            } else {
                this.colorMapByIndex = null;
                this.colorMapByValue = options.get('colorMap');
                if (this.colorMapByValue && this.colorMapByValue.get === undefined) {
                    this.colorMapByValue = new RangeMap(this.colorMapByValue);
                }
            }

            this.range = range;
        },

        getRegion: function (el, x, y) {
            var result = Math.floor(x / this.totalBarWidth);
            return (result < 0 || result >= this.values.length) ? undefined : result;
        },

        getCurrentRegionFields: function () {
            var currentRegion = this.currentRegion,
                values = ensureArray(this.values[currentRegion]),
                result = [],
                value, i;
            for (i = values.length; i--;) {
                value = values[i];
                result.push({
                    isNull: value === null,
                    value: value,
                    color: this.calcColor(i, value, currentRegion),
                    offset: currentRegion
                });
            }
            return result;
        },

        calcColor: function (stacknum, value, valuenum) {
            var colorMapByIndex = this.colorMapByIndex,
                colorMapByValue = this.colorMapByValue,
                options = this.options,
                color, newColor;
            if (this.stacked) {
                color = options.get('stackedBarColor');
            } else {
                color = (value < 0) ? options.get('negBarColor') : options.get('barColor');
            }
            if (value === 0 && options.get('zeroColor') !== undefined) {
                color = options.get('zeroColor');
            }
            if (colorMapByValue && (newColor = colorMapByValue.get(value))) {
                color = newColor;
            } else if (colorMapByIndex && colorMapByIndex.length > valuenum) {
                color = colorMapByIndex[valuenum];
            }
            return $.isArray(color) ? color[stacknum % color.length] : color;
        },

        /**
         * Render bar(s) for a region
         */
        renderRegion: function (valuenum, highlight) {
            var vals = this.values[valuenum],
                options = this.options,
                xaxisOffset = this.xaxisOffset,
                result = [],
                range = this.range,
                stacked = this.stacked,
                target = this.target,
                x = valuenum * this.totalBarWidth,
                canvasHeightEf = this.canvasHeightEf,
                yoffset = this.yoffset,
                y, height, color, isNull, yoffsetNeg, i, valcount, val, minPlotted, allMin;

            vals = $.isArray(vals) ? vals : [vals];
            valcount = vals.length;
            val = vals[0];
            isNull = all(null, vals);
            allMin = all(xaxisOffset, vals, true);

            if (isNull) {
                if (options.get('nullColor')) {
                    color = highlight ? options.get('nullColor') : this.calcHighlightColor(options.get('nullColor'), options);
                    y = (yoffset > 0) ? yoffset - 1 : yoffset;
                    return target.drawRect(x, y, this.barWidth - 1, 0, color, color);
                } else {
                    return undefined;
                }
            }
            yoffsetNeg = yoffset;
            for (i = 0; i < valcount; i++) {
                val = vals[i];

                if (stacked && val === xaxisOffset) {
                    if (!allMin || minPlotted) {
                        continue;
                    }
                    minPlotted = true;
                }

                if (range > 0) {
                    height = Math.floor(canvasHeightEf * ((Math.abs(val - xaxisOffset) / range))) + 1;
                } else {
                    height = 1;
                }
                if (val < xaxisOffset || (val === xaxisOffset && yoffset === 0)) {
                    y = yoffsetNeg;
                    yoffsetNeg += height;
                } else {
                    y = yoffset - height;
                    yoffset -= height;
                }
                color = this.calcColor(i, val, valuenum);
                if (highlight) {
                    color = this.calcHighlightColor(color, options);
                }
                result.push(target.drawRect(x, y, this.barWidth - 1, height - 1, color, color));
            }
            if (result.length === 1) {
                return result[0];
            }
            return result;
        }
    });

    /**
     * Tristate charts
     */
    $.fn.sparkline.tristate = tristate = createClass($.fn.sparkline._base, barHighlightMixin, {
        type: 'tristate',

        init: function (el, values, options, width, height) {
            var barWidth = parseInt(options.get('barWidth'), 10),
                barSpacing = parseInt(options.get('barSpacing'), 10);
            tristate._super.init.call(this, el, values, options, width, height);

            this.regionShapes = {};
            this.barWidth = barWidth;
            this.barSpacing = barSpacing;
            this.totalBarWidth = barWidth + barSpacing;
            this.values = $.map(values, Number);
            this.width = width = (values.length * barWidth) + ((values.length - 1) * barSpacing);

            if ($.isArray(options.get('colorMap'))) {
                this.colorMapByIndex = options.get('colorMap');
                this.colorMapByValue = null;
            } else {
                this.colorMapByIndex = null;
                this.colorMapByValue = options.get('colorMap');
                if (this.colorMapByValue && this.colorMapByValue.get === undefined) {
                    this.colorMapByValue = new RangeMap(this.colorMapByValue);
                }
            }
            this.initTarget();
        },

        getRegion: function (el, x, y) {
            return Math.floor(x / this.totalBarWidth);
        },

        getCurrentRegionFields: function () {
            var currentRegion = this.currentRegion;
            return {
                isNull: this.values[currentRegion] === undefined,
                value: this.values[currentRegion],
                color: this.calcColor(this.values[currentRegion], currentRegion),
                offset: currentRegion
            };
        },

        calcColor: function (value, valuenum) {
            var values = this.values,
                options = this.options,
                colorMapByIndex = this.colorMapByIndex,
                colorMapByValue = this.colorMapByValue,
                color, newColor;

            if (colorMapByValue && (newColor = colorMapByValue.get(value))) {
                color = newColor;
            } else if (colorMapByIndex && colorMapByIndex.length > valuenum) {
                color = colorMapByIndex[valuenum];
            } else if (values[valuenum] < 0) {
                color = options.get('negBarColor');
            } else if (values[valuenum] > 0) {
                color = options.get('posBarColor');
            } else {
                color = options.get('zeroBarColor');
            }
            return color;
        },

        renderRegion: function (valuenum, highlight) {
            var values = this.values,
                options = this.options,
                target = this.target,
                canvasHeight, height, halfHeight,
                x, y, color;

            canvasHeight = target.pixelHeight;
            halfHeight = Math.round(canvasHeight / 2);

            x = valuenum * this.totalBarWidth;
            if (values[valuenum] < 0) {
                y = halfHeight;
                height = halfHeight - 1;
            } else if (values[valuenum] > 0) {
                y = 0;
                height = halfHeight - 1;
            } else {
                y = halfHeight - 1;
                height = 2;
            }
            color = this.calcColor(values[valuenum], valuenum);
            if (color === null) {
                return;
            }
            if (highlight) {
                color = this.calcHighlightColor(color, options);
            }
            return target.drawRect(x, y, this.barWidth - 1, height - 1, color, color);
        }
    });

    /**
     * Discrete charts
     */
    $.fn.sparkline.discrete = discrete = createClass($.fn.sparkline._base, barHighlightMixin, {
        type: 'discrete',

        init: function (el, values, options, width, height) {
            discrete._super.init.call(this, el, values, options, width, height);

            this.regionShapes = {};
            this.values = values = $.map(values, Number);
            this.min = Math.min.apply(Math, values);
            this.max = Math.max.apply(Math, values);
            this.range = this.max - this.min;
            this.width = width = options.get('width') === 'auto' ? values.length * 2 : this.width;
            this.interval = Math.floor(width / values.length);
            this.itemWidth = width / values.length;
            if (options.get('chartRangeMin') !== undefined && (options.get('chartRangeClip') || options.get('chartRangeMin') < this.min)) {
                this.min = options.get('chartRangeMin');
            }
            if (options.get('chartRangeMax') !== undefined && (options.get('chartRangeClip') || options.get('chartRangeMax') > this.max)) {
                this.max = options.get('chartRangeMax');
            }
            this.initTarget();
            if (this.target) {
                this.lineHeight = options.get('lineHeight') === 'auto' ? Math.round(this.canvasHeight * 0.3) : options.get('lineHeight');
            }
        },

        getRegion: function (el, x, y) {
            return Math.floor(x / this.itemWidth);
        },

        getCurrentRegionFields: function () {
            var currentRegion = this.currentRegion;
            return {
                isNull: this.values[currentRegion] === undefined,
                value: this.values[currentRegion],
                offset: currentRegion
            };
        },

        renderRegion: function (valuenum, highlight) {
            var values = this.values,
                options = this.options,
                min = this.min,
                max = this.max,
                range = this.range,
                interval = this.interval,
                target = this.target,
                canvasHeight = this.canvasHeight,
                lineHeight = this.lineHeight,
                pheight = canvasHeight - lineHeight,
                ytop, val, color, x;

            val = clipval(values[valuenum], min, max);
            x = valuenum * interval;
            ytop = Math.round(pheight - pheight * ((val - min) / range));
            color = (options.get('thresholdColor') && val < options.get('thresholdValue')) ? options.get('thresholdColor') : options.get('lineColor');
            if (highlight) {
                color = this.calcHighlightColor(color, options);
            }
            return target.drawLine(x, ytop, x, ytop + lineHeight, color);
        }
    });

    /**
     * Bullet charts
     */
    $.fn.sparkline.bullet = bullet = createClass($.fn.sparkline._base, {
        type: 'bullet',

        init: function (el, values, options, width, height) {
            var min, max, vals;
            bullet._super.init.call(this, el, values, options, width, height);

            // values: target, performance, range1, range2, range3
            this.values = values = normalizeValues(values);
            // target or performance could be null
            vals = values.slice();
            vals[0] = vals[0] === null ? vals[2] : vals[0];
            vals[1] = values[1] === null ? vals[2] : vals[1];
            min = Math.min.apply(Math, values);
            max = Math.max.apply(Math, values);
            if (options.get('base') === undefined) {
                min = min < 0 ? min : 0;
            } else {
                min = options.get('base');
            }
            this.min = min;
            this.max = max;
            this.range = max - min;
            this.shapes = {};
            this.valueShapes = {};
            this.regiondata = {};
            this.width = width = options.get('width') === 'auto' ? '4.0em' : width;
            this.target = this.$el.simpledraw(width, height, options.get('composite'));
            if (!values.length) {
                this.disabled = true;
            }
            this.initTarget();
        },

        getRegion: function (el, x, y) {
            var shapeid = this.target.getShapeAt(el, x, y);
            return (shapeid !== undefined && this.shapes[shapeid] !== undefined) ? this.shapes[shapeid] : undefined;
        },

        getCurrentRegionFields: function () {
            var currentRegion = this.currentRegion;
            return {
                fieldkey: currentRegion.substr(0, 1),
                value: this.values[currentRegion.substr(1)],
                region: currentRegion
            };
        },

        changeHighlight: function (highlight) {
            var currentRegion = this.currentRegion,
                shapeid = this.valueShapes[currentRegion],
                shape;
            delete this.shapes[shapeid];
            switch (currentRegion.substr(0, 1)) {
                case 'r':
                    shape = this.renderRange(currentRegion.substr(1), highlight);
                    break;
                case 'p':
                    shape = this.renderPerformance(highlight);
                    break;
                case 't':
                    shape = this.renderTarget(highlight);
                    break;
            }
            this.valueShapes[currentRegion] = shape.id;
            this.shapes[shape.id] = currentRegion;
            this.target.replaceWithShape(shapeid, shape);
        },

        renderRange: function (rn, highlight) {
            var rangeval = this.values[rn],
                rangewidth = Math.round(this.canvasWidth * ((rangeval - this.min) / this.range)),
                color = this.options.get('rangeColors')[rn - 2];
            if (highlight) {
                color = this.calcHighlightColor(color, this.options);
            }
            return this.target.drawRect(0, 0, rangewidth - 1, this.canvasHeight - 1, color, color);
        },

        renderPerformance: function (highlight) {
            var perfval = this.values[1],
                perfwidth = Math.round(this.canvasWidth * ((perfval - this.min) / this.range)),
                color = this.options.get('performanceColor');
            if (highlight) {
                color = this.calcHighlightColor(color, this.options);
            }
            return this.target.drawRect(0, Math.round(this.canvasHeight * 0.3), perfwidth - 1,
                Math.round(this.canvasHeight * 0.4) - 1, color, color);
        },

        renderTarget: function (highlight) {
            var targetval = this.values[0],
                x = Math.round(this.canvasWidth * ((targetval - this.min) / this.range) - (this.options.get('targetWidth') / 2)),
                targettop = Math.round(this.canvasHeight * 0.10),
                targetheight = this.canvasHeight - (targettop * 2),
                color = this.options.get('targetColor');
            if (highlight) {
                color = this.calcHighlightColor(color, this.options);
            }
            return this.target.drawRect(x, targettop, this.options.get('targetWidth') - 1, targetheight - 1, color, color);
        },

        render: function () {
            var vlen = this.values.length,
                target = this.target,
                i, shape;
            if (!bullet._super.render.call(this)) {
                return;
            }
            for (i = 2; i < vlen; i++) {
                shape = this.renderRange(i).append();
                this.shapes[shape.id] = 'r' + i;
                this.valueShapes['r' + i] = shape.id;
            }
            if (this.values[1] !== null) {
                shape = this.renderPerformance().append();
                this.shapes[shape.id] = 'p1';
                this.valueShapes.p1 = shape.id;
            }
            if (this.values[0] !== null) {
                shape = this.renderTarget().append();
                this.shapes[shape.id] = 't0';
                this.valueShapes.t0 = shape.id;
            }
            target.render();
        }
    });

    /**
     * Pie charts
     */
    $.fn.sparkline.pie = pie = createClass($.fn.sparkline._base, {
        type: 'pie',

        init: function (el, values, options, width, height) {
            var total = 0, i;

            pie._super.init.call(this, el, values, options, width, height);

            this.shapes = {}; // map shape ids to value offsets
            this.valueShapes = {}; // maps value offsets to shape ids
            this.values = values = $.map(values, Number);

            if (options.get('width') === 'auto') {
                this.width = this.height;
            }

            if (values.length > 0) {
                for (i = values.length; i--;) {
                    total += values[i];
                }
            }
            this.total = total;
            this.initTarget();
            this.radius = Math.floor(Math.min(this.canvasWidth, this.canvasHeight) / 2);
        },

        getRegion: function (el, x, y) {
            var shapeid = this.target.getShapeAt(el, x, y);
            return (shapeid !== undefined && this.shapes[shapeid] !== undefined) ? this.shapes[shapeid] : undefined;
        },

        getCurrentRegionFields: function () {
            var currentRegion = this.currentRegion;
            return {
                isNull: this.values[currentRegion] === undefined,
                value: this.values[currentRegion],
                percent: this.values[currentRegion] / this.total * 100,
                color: this.options.get('sliceColors')[currentRegion % this.options.get('sliceColors').length],
                offset: currentRegion
            };
        },

        changeHighlight: function (highlight) {
            var currentRegion = this.currentRegion,
                newslice = this.renderSlice(currentRegion, highlight),
                shapeid = this.valueShapes[currentRegion];
            delete this.shapes[shapeid];
            this.target.replaceWithShape(shapeid, newslice);
            this.valueShapes[currentRegion] = newslice.id;
            this.shapes[newslice.id] = currentRegion;
        },

        renderSlice: function (valuenum, highlight) {
            var target = this.target,
                options = this.options,
                radius = this.radius,
                borderWidth = options.get('borderWidth'),
                offset = options.get('offset'),
                circle = 2 * Math.PI,
                values = this.values,
                total = this.total,
                next = offset ? (2 * Math.PI) * (offset / 360) : 0,
                start, end, i, vlen, color;

            vlen = values.length;
            for (i = 0; i < vlen; i++) {
                start = next;
                end = next;
                if (total > 0) {  // avoid divide by zero
                    end = next + (circle * (values[i] / total));
                }
                if (valuenum === i) {
                    color = options.get('sliceColors')[i % options.get('sliceColors').length];
                    if (highlight) {
                        color = this.calcHighlightColor(color, options);
                    }

                    return target.drawPieSlice(radius, radius, radius - borderWidth, start, end, undefined, color);
                }
                next = end;
            }
        },

        render: function () {
            var target = this.target,
                values = this.values,
                options = this.options,
                radius = this.radius,
                borderWidth = options.get('borderWidth'),
                shape, i;

            if (!pie._super.render.call(this)) {
                return;
            }
            if (borderWidth) {
                target.drawCircle(radius, radius, Math.floor(radius - (borderWidth / 2)),
                    options.get('borderColor'), undefined, borderWidth).append();
            }
            for (i = values.length; i--;) {
                if (values[i]) { // don't render zero values
                    shape = this.renderSlice(i).append();
                    this.valueShapes[i] = shape.id; // store just the shapeid
                    this.shapes[shape.id] = i;
                }
            }
            target.render();
        }
    });

    /**
     * Box plots
     */
    $.fn.sparkline.box = box = createClass($.fn.sparkline._base, {
        type: 'box',

        init: function (el, values, options, width, height) {
            box._super.init.call(this, el, values, options, width, height);
            this.values = $.map(values, Number);
            this.width = options.get('width') === 'auto' ? '4.0em' : width;
            this.initTarget();
            if (!this.values.length) {
                this.disabled = 1;
            }
        },

        /**
         * Simulate a single region
         */
        getRegion: function () {
            return 1;
        },

        getCurrentRegionFields: function () {
            var result = [
                { field: 'lq', value: this.quartiles[0] },
                { field: 'med', value: this.quartiles[1] },
                { field: 'uq', value: this.quartiles[2] }
            ];
            if (this.loutlier !== undefined) {
                result.push({ field: 'lo', value: this.loutlier});
            }
            if (this.routlier !== undefined) {
                result.push({ field: 'ro', value: this.routlier});
            }
            if (this.lwhisker !== undefined) {
                result.push({ field: 'lw', value: this.lwhisker});
            }
            if (this.rwhisker !== undefined) {
                result.push({ field: 'rw', value: this.rwhisker});
            }
            return result;
        },

        render: function () {
            var target = this.target,
                values = this.values,
                vlen = values.length,
                options = this.options,
                canvasWidth = this.canvasWidth,
                canvasHeight = this.canvasHeight,
                minValue = options.get('chartRangeMin') === undefined ? Math.min.apply(Math, values) : options.get('chartRangeMin'),
                maxValue = options.get('chartRangeMax') === undefined ? Math.max.apply(Math, values) : options.get('chartRangeMax'),
                canvasLeft = 0,
                lwhisker, loutlier, iqr, q1, q2, q3, rwhisker, routlier, i,
                size, unitSize;

            if (!box._super.render.call(this)) {
                return;
            }

            if (options.get('raw')) {
                if (options.get('showOutliers') && values.length > 5) {
                    loutlier = values[0];
                    lwhisker = values[1];
                    q1 = values[2];
                    q2 = values[3];
                    q3 = values[4];
                    rwhisker = values[5];
                    routlier = values[6];
                } else {
                    lwhisker = values[0];
                    q1 = values[1];
                    q2 = values[2];
                    q3 = values[3];
                    rwhisker = values[4];
                }
            } else {
                values.sort(function (a, b) {
                    return a - b;
                });
                q1 = quartile(values, 1);
                q2 = quartile(values, 2);
                q3 = quartile(values, 3);
                iqr = q3 - q1;
                if (options.get('showOutliers')) {
                    lwhisker = rwhisker = undefined;
                    for (i = 0; i < vlen; i++) {
                        if (lwhisker === undefined && values[i] > q1 - (iqr * options.get('outlierIQR'))) {
                            lwhisker = values[i];
                        }
                        if (values[i] < q3 + (iqr * options.get('outlierIQR'))) {
                            rwhisker = values[i];
                        }
                    }
                    loutlier = values[0];
                    routlier = values[vlen - 1];
                } else {
                    lwhisker = values[0];
                    rwhisker = values[vlen - 1];
                }
            }
            this.quartiles = [q1, q2, q3];
            this.lwhisker = lwhisker;
            this.rwhisker = rwhisker;
            this.loutlier = loutlier;
            this.routlier = routlier;

            unitSize = canvasWidth / (maxValue - minValue + 1);
            if (options.get('showOutliers')) {
                canvasLeft = Math.ceil(options.get('spotRadius'));
                canvasWidth -= 2 * Math.ceil(options.get('spotRadius'));
                unitSize = canvasWidth / (maxValue - minValue + 1);
                if (loutlier < lwhisker) {
                    target.drawCircle((loutlier - minValue) * unitSize + canvasLeft,
                        canvasHeight / 2,
                        options.get('spotRadius'),
                        options.get('outlierLineColor'),
                        options.get('outlierFillColor')).append();
                }
                if (routlier > rwhisker) {
                    target.drawCircle((routlier - minValue) * unitSize + canvasLeft,
                        canvasHeight / 2,
                        options.get('spotRadius'),
                        options.get('outlierLineColor'),
                        options.get('outlierFillColor')).append();
                }
            }

            // box
            target.drawRect(
                Math.round((q1 - minValue) * unitSize + canvasLeft),
                Math.round(canvasHeight * 0.1),
                Math.round((q3 - q1) * unitSize),
                Math.round(canvasHeight * 0.8),
                options.get('boxLineColor'),
                options.get('boxFillColor')).append();
            // left whisker
            target.drawLine(
                Math.round((lwhisker - minValue) * unitSize + canvasLeft),
                Math.round(canvasHeight / 2),
                Math.round((q1 - minValue) * unitSize + canvasLeft),
                Math.round(canvasHeight / 2),
                options.get('lineColor')).append();
            target.drawLine(
                Math.round((lwhisker - minValue) * unitSize + canvasLeft),
                Math.round(canvasHeight / 4),
                Math.round((lwhisker - minValue) * unitSize + canvasLeft),
                Math.round(canvasHeight - canvasHeight / 4),
                options.get('whiskerColor')).append();
            // right whisker
            target.drawLine(Math.round((rwhisker - minValue) * unitSize + canvasLeft),
                Math.round(canvasHeight / 2),
                Math.round((q3 - minValue) * unitSize + canvasLeft),
                Math.round(canvasHeight / 2),
                options.get('lineColor')).append();
            target.drawLine(
                Math.round((rwhisker - minValue) * unitSize + canvasLeft),
                Math.round(canvasHeight / 4),
                Math.round((rwhisker - minValue) * unitSize + canvasLeft),
                Math.round(canvasHeight - canvasHeight / 4),
                options.get('whiskerColor')).append();
            // median line
            target.drawLine(
                Math.round((q2 - minValue) * unitSize + canvasLeft),
                Math.round(canvasHeight * 0.1),
                Math.round((q2 - minValue) * unitSize + canvasLeft),
                Math.round(canvasHeight * 0.9),
                options.get('medianColor')).append();
            if (options.get('target')) {
                size = Math.ceil(options.get('spotRadius'));
                target.drawLine(
                    Math.round((options.get('target') - minValue) * unitSize + canvasLeft),
                    Math.round((canvasHeight / 2) - size),
                    Math.round((options.get('target') - minValue) * unitSize + canvasLeft),
                    Math.round((canvasHeight / 2) + size),
                    options.get('targetColor')).append();
                target.drawLine(
                    Math.round((options.get('target') - minValue) * unitSize + canvasLeft - size),
                    Math.round(canvasHeight / 2),
                    Math.round((options.get('target') - minValue) * unitSize + canvasLeft + size),
                    Math.round(canvasHeight / 2),
                    options.get('targetColor')).append();
            }
            target.render();
        }
    });

    // Setup a very simple "virtual canvas" to make drawing the few shapes we need easier
    // This is accessible as $(foo).simpledraw()

    if ($.browser.msie && document.namespaces && !document.namespaces.v) {
        document.namespaces.add('v', 'urn:schemas-microsoft-com:vml', '#default#VML');
    }

    if ($.browser.hasCanvas === undefined) {
        $.browser.hasCanvas = document.createElement('canvas').getContext !== undefined;
    }

    VShape = createClass({
        init: function (target, id, type, args) {
            this.target = target;
            this.id = id;
            this.type = type;
            this.args = args;
        },
        append: function () {
            this.target.appendShape(this);
            return this;
        }
    });

    VCanvas_base = createClass({
        _pxregex: /(\d+)(px)?\s*$/i,

        init: function (width, height, target) {
            if (!width) {
                return;
            }
            this.width = width;
            this.height = height;
            this.target = target;
            this.lastShapeId = null;
            if (target[0]) {
                target = target[0];
            }
            $.data(target, '_jqs_vcanvas', this);
        },

        drawLine: function (x1, y1, x2, y2, lineColor, lineWidth) {
            return this.drawShape([
                [x1, y1],
                [x2, y2]
            ], lineColor, lineWidth);
        },

        drawShape: function (path, lineColor, fillColor, lineWidth) {
            return this._genShape('Shape', [path, lineColor, fillColor, lineWidth]);
        },

        drawCircle: function (x, y, radius, lineColor, fillColor, lineWidth) {
            return this._genShape('Circle', [x, y, radius, lineColor, fillColor, lineWidth]);
        },

        drawPieSlice: function (x, y, radius, startAngle, endAngle, lineColor, fillColor) {
            return this._genShape('PieSlice', [x, y, radius, startAngle, endAngle, lineColor, fillColor]);
        },

        drawRect: function (x, y, width, height, lineColor, fillColor) {
            return this._genShape('Rect', [x, y, width, height, lineColor, fillColor]);
        },

        getElement: function () {
            return this.canvas;
        },

        /**
         * Return the most recently inserted shape id
         */
        getLastShapeId: function () {
            return this.lastShapeId;
        },

        /**
         * Clear and reset the canvas
         */
        reset: function () {
            alert('reset not implemented');
        },

        _insert: function (el, target) {
            $(target).html(el);
        },

        /**
         * Calculate the pixel dimensions of the canvas
         */
        _calculatePixelDims: function (width, height, canvas) {
            // XXX This should probably be a configurable option
            var match;
            match = this._pxregex.exec(height);
            if (match) {
                this.pixelHeight = match[1];
            } else {
                this.pixelHeight = $(canvas).height();
            }
            match = this._pxregex.exec(width);
            if (match) {
                this.pixelWidth = match[1];
            } else {
                this.pixelWidth = $(canvas).width();
            }
        },

        /**
         * Generate a shape object and id for later rendering
         */
        _genShape: function (shapetype, shapeargs) {
            var id = shapeCount++;
            shapeargs.unshift(id);
            return new VShape(this, id, shapetype, shapeargs);
        },

        /**
         * Add a shape to the end of the render queue
         */
        appendShape: function (shape) {
            alert('appendShape not implemented');
        },

        /**
         * Replace one shape with another
         */
        replaceWithShape: function (shapeid, shape) {
            alert('replaceWithShape not implemented');
        },

        /**
         * Insert one shape after another in the render queue
         */
        insertAfterShape: function (shapeid, shape) {
            alert('insertAfterShape not implemented');
        },

        /**
         * Remove a shape from the queue
         */
        removeShapeId: function (shapeid) {
            alert('removeShapeId not implemented');
        },

        /**
         * Find a shape at the specified x/y co-ordinates
         */
        getShapeAt: function (el, x, y) {
            alert('getShapeAt not implemented');
        },

        /**
         * Render all queued shapes onto the canvas
         */
        render: function () {
            alert('render not implemented');
        }
    });

    VCanvas_canvas = createClass(VCanvas_base, {
        init: function (width, height, target, interact) {
            VCanvas_canvas._super.init.call(this, width, height, target);
            this.canvas = document.createElement('canvas');
            if (target[0]) {
                target = target[0];
            }
            $.data(target, '_jqs_vcanvas', this);
            $(this.canvas).css({ display: 'inline-block', width: width, height: height, verticalAlign: 'top' });
            this._insert(this.canvas, target);
            this._calculatePixelDims(width, height, this.canvas);
            this.canvas.width = this.pixelWidth;
            this.canvas.height = this.pixelHeight;
            this.interact = interact;
            this.shapes = {};
            this.shapeseq = [];
            this.currentTargetShapeId = undefined;
            $(this.canvas).css({width: this.pixelWidth, height: this.pixelHeight});
        },

        _getContext: function (lineColor, fillColor, lineWidth) {
            var context = this.canvas.getContext('2d');
            if (lineColor !== undefined) {
                context.strokeStyle = lineColor;
            }
            context.lineWidth = lineWidth === undefined ? 1 : lineWidth;
            if (fillColor !== undefined) {
                context.fillStyle = fillColor;
            }
            return context;
        },

        reset: function () {
            var context = this._getContext();
            context.clearRect(0, 0, this.pixelWidth, this.pixelHeight);
            this.shapes = {};
            this.shapeseq = [];
            this.currentTargetShapeId = undefined;
        },

        _drawShape: function (shapeid, path, lineColor, fillColor, lineWidth) {
            var context = this._getContext(lineColor, fillColor, lineWidth),
                i, plen;
            context.beginPath();
            context.moveTo(path[0][0] + 0.5, path[0][1] + 0.5);
            for (i = 1, plen = path.length; i < plen; i++) {
                context.lineTo(path[i][0] + 0.5, path[i][1] + 0.5); // the 0.5 offset gives us crisp pixel-width lines
            }
            if (lineColor !== undefined) {
                context.stroke();
            }
            if (fillColor !== undefined) {
                context.fill();
            }
            if (this.targetX !== undefined && this.targetY !== undefined &&
                context.isPointInPath(this.targetX, this.targetY)) {
                this.currentTargetShapeId = shapeid;
            }
        },

        _drawCircle: function (shapeid, x, y, radius, lineColor, fillColor, lineWidth) {
            var context = this._getContext(lineColor, fillColor, lineWidth);
            context.beginPath();
            context.arc(x, y, radius, 0, 2 * Math.PI, false);
            if (this.targetX !== undefined && this.targetY !== undefined &&
                context.isPointInPath(this.targetX, this.targetY)) {
                this.currentTargetShapeId = shapeid;
            }
            if (lineColor !== undefined) {
                context.stroke();
            }
            if (fillColor !== undefined) {
                context.fill();
            }
        },

        _drawPieSlice: function (shapeid, x, y, radius, startAngle, endAngle, lineColor, fillColor) {
            var context = this._getContext(lineColor, fillColor);
            context.beginPath();
            context.moveTo(x, y);
            context.arc(x, y, radius, startAngle, endAngle, false);
            context.lineTo(x, y);
            context.closePath();
            if (lineColor !== undefined) {
                context.stroke();
            }
            if (fillColor) {
                context.fill();
            }
            if (this.targetX !== undefined && this.targetY !== undefined &&
                context.isPointInPath(this.targetX, this.targetY)) {
                this.currentTargetShapeId = shapeid;
            }
        },

        _drawRect: function (shapeid, x, y, width, height, lineColor, fillColor) {
            return this._drawShape(shapeid, [
                [x, y],
                [x + width, y],
                [x + width, y + height],
                [x, y + height],
                [x, y]
            ], lineColor, fillColor);
        },

        appendShape: function (shape) {
            this.shapes[shape.id] = shape;
            this.shapeseq.push(shape.id);
            this.lastShapeId = shape.id;
            return shape.id;
        },

        replaceWithShape: function (shapeid, shape) {
            var shapeseq = this.shapeseq,
                i;
            this.shapes[shape.id] = shape;
            for (i = shapeseq.length; i--;) {
                if (shapeseq[i] == shapeid) {
                    shapeseq[i] = shape.id;
                }
            }
            delete this.shapes[shapeid];
        },

        replaceWithShapes: function (shapeids, shapes) {
            var shapeseq = this.shapeseq,
                shapemap = {},
                sid, i, first;

            for (i = shapeids.length; i--;) {
                shapemap[shapeids[i]] = true;
            }
            for (i = shapeseq.length; i--;) {
                sid = shapeseq[i];
                if (shapemap[sid]) {
                    shapeseq.splice(i, 1);
                    delete this.shapes[sid];
                    first = i;
                }
            }
            for (i = shapes.length; i--;) {
                shapeseq.splice(first, 0, shapes[i].id);
                this.shapes[shapes[i].id] = shapes[i];
            }

        },

        insertAfterShape: function (shapeid, shape) {
            var shapeseq = this.shapeseq,
                i;
            for (i = shapeseq.length; i--;) {
                if (shapeseq[i] === shapeid) {
                    shapeseq.splice(i + 1, 0, shape.id);
                    this.shapes[shape.id] = shape;
                    return;
                }
            }
        },

        removeShapeId: function (shapeid) {
            var shapeseq = this.shapeseq,
                i;
            for (i = shapeseq.length; i--;) {
                if (shapeseq[i] === shapeid) {
                    shapeseq.splice(i, 1);
                    break;
                }
            }
            delete this.shapes[shapeid];
        },

        getShapeAt: function (el, x, y) {
            this.targetX = x;
            this.targetY = y;
            this.render();
            return this.currentTargetShapeId;
        },

        render: function () {
            var shapeseq = this.shapeseq,
                shapes = this.shapes,
                shapeCount = shapeseq.length,
                context = this._getContext(),
                shapeid, shape, i;
            context.clearRect(0, 0, this.pixelWidth, this.pixelHeight);
            for (i = 0; i < shapeCount; i++) {
                shapeid = shapeseq[i];
                shape = shapes[shapeid];
                this['_draw' + shape.type].apply(this, shape.args);
            }
            if (!this.interact) {
                // not interactive so no need to keep the shapes array
                this.shapes = {};
                this.shapeseq = [];
            }
        }

    });

    VCanvas_vml = createClass(VCanvas_base, {
        init: function (width, height, target) {
            var groupel;
            VCanvas_vml._super.init.call(this, width, height, target);
            if (target[0]) {
                target = target[0];
            }
            $.data(target, '_jqs_vcanvas', this);
            this.canvas = document.createElement('span');
            $(this.canvas).css({ display: 'inline-block', position: 'relative', overflow: 'hidden', width: width, height: height, margin: '0px', padding: '0px', verticalAlign: 'top'});
            this._insert(this.canvas, target);
            this._calculatePixelDims(width, height, this.canvas);
            this.canvas.width = this.pixelWidth;
            this.canvas.height = this.pixelHeight;
            groupel = '<v:group coordorigin="0 0" coordsize="' + this.pixelWidth + ' ' + this.pixelHeight + '"' +
                ' style="position:absolute;top:0;left:0;width:' + this.pixelWidth + 'px;height=' + this.pixelHeight + 'px;"></v:group>';
            this.canvas.insertAdjacentHTML('beforeEnd', groupel);
            this.group = $(this.canvas).children()[0];
            this.rendered = false;
            this.prerender = '';
        },

        _drawShape: function (shapeid, path, lineColor, fillColor, lineWidth) {
            var vpath = [],
                initial, stroke, fill, closed, vel, plen, i;
            for (i = 0, plen = path.length; i < plen; i++) {
                vpath[i] = '' + (path[i][0]) + ',' + (path[i][1]);
            }
            initial = vpath.splice(0, 1);
            lineWidth = lineWidth === undefined ? 1 : lineWidth;
            stroke = lineColor === undefined ? ' stroked="false" ' : ' strokeWeight="' + lineWidth + 'px" strokeColor="' + lineColor + '" ';
            fill = fillColor === undefined ? ' filled="false"' : ' fillColor="' + fillColor + '" filled="true" ';
            closed = vpath[0] === vpath[vpath.length - 1] ? 'x ' : '';
            vel = '<v:shape coordorigin="0 0" coordsize="' + this.pixelWidth + ' ' + this.pixelHeight + '" ' +
                ' id="jqsshape' + shapeid + '" ' +
                stroke +
                fill +
                ' style="position:absolute;left:0px;top:0px;height:' + this.pixelHeight + 'px;width:' + this.pixelWidth + 'px;padding:0px;margin:0px;" ' +
                ' path="m ' + initial + ' l ' + vpath.join(', ') + ' ' + closed + 'e">' +
                ' </v:shape>';
            return vel;
        },

        _drawCircle: function (shapeid, x, y, radius, lineColor, fillColor, lineWidth) {
            var stroke, fill, vel;
            x -= radius;
            y -= radius;
            stroke = lineColor === undefined ? ' stroked="false" ' : ' strokeWeight="' + lineWidth + 'px" strokeColor="' + lineColor + '" ';
            fill = fillColor === undefined ? ' filled="false"' : ' fillColor="' + fillColor + '" filled="true" ';
            vel = '<v:oval ' +
                ' id="jqsshape' + shapeid + '" ' +
                stroke +
                fill +
                ' style="position:absolute;top:' + y + 'px; left:' + x + 'px; width:' + (radius * 2) + 'px; height:' + (radius * 2) + 'px"></v:oval>';
            return vel;

        },

        _drawPieSlice: function (shapeid, x, y, radius, startAngle, endAngle, lineColor, fillColor) {
            var vpath, startx, starty, endx, endy, stroke, fill, vel;
            if (startAngle === endAngle) {
                return;  // VML seems to have problem when start angle equals end angle.
            }
            if ((endAngle - startAngle) === (2 * Math.PI)) {
                startAngle = 0.0;  // VML seems to have a problem when drawing a full circle that doesn't start 0
                endAngle = (2 * Math.PI);
            }

            startx = x + Math.round(Math.cos(startAngle) * radius);
            starty = y + Math.round(Math.sin(startAngle) * radius);
            endx = x + Math.round(Math.cos(endAngle) * radius);
            endy = y + Math.round(Math.sin(endAngle) * radius);

            // Prevent very small slices from being mistaken as a whole pie
            if (startx === endx && starty === endy && (endAngle - startAngle) < Math.PI) {
                return;
            }

            vpath = [x - radius, y - radius, x + radius, y + radius, startx, starty, endx, endy];
            stroke = lineColor === undefined ? ' stroked="false" ' : ' strokeWeight="1px" strokeColor="' + lineColor + '" ';
            fill = fillColor === undefined ? ' filled="false"' : ' fillColor="' + fillColor + '" filled="true" ';
            vel = '<v:shape coordorigin="0 0" coordsize="' + this.pixelWidth + ' ' + this.pixelHeight + '" ' +
                ' id="jqsshape' + shapeid + '" ' +
                stroke +
                fill +
                ' style="position:absolute;left:0px;top:0px;height:' + this.pixelHeight + 'px;width:' + this.pixelWidth + 'px;padding:0px;margin:0px;" ' +
                ' path="m ' + x + ',' + y + ' wa ' + vpath.join(', ') + ' x e">' +
                ' </v:shape>';
            return vel;
        },

        _drawRect: function (shapeid, x, y, width, height, lineColor, fillColor) {
            return this._drawShape(shapeid, [
                [x, y],
                [x, y + height],
                [x + width, y + height],
                [x + width, y],
                [x, y]
            ], lineColor, fillColor);
        },

        reset: function () {
            this.group.innerHTML = '';
        },

        appendShape: function (shape) {
            var vel = this['_draw' + shape.type].apply(this, shape.args);
            if (this.rendered) {
                this.group.insertAdjacentHTML('beforeEnd', vel);
            } else {
                this.prerender += vel;
            }
            this.lastShapeId = shape.id;
            return shape.id;
        },

        replaceWithShape: function (shapeid, shape) {
            var existing = $('#jqsshape' + shapeid),
                vel = this['_draw' + shape.type].apply(this, shape.args);
            existing[0].outerHTML = vel;
        },

        replaceWithShapes: function (shapeids, shapes) {
            // replace the first shapeid with all the new shapes then toast the remaining old shapes
            var existing = $('#jqsshape' + shapeids[0]),
                replace = '',
                slen = shapes.length,
                i;
            for (i = 0; i < slen; i++) {
                replace += this['_draw' + shapes[i].type].apply(this, shapes[i].args);
            }
            existing[0].outerHTML = replace;
            for (i = 1; i < shapeids.length; i++) {
                $('#jqsshape' + shapeids[i]).remove();
            }
        },

        insertAfterShape: function (shapeid, shape) {
            var existing = $('#jqsshape' + shapeid),
                vel = this['_draw' + shape.type].apply(this, shape.args);
            existing[0].insertAdjacentHTML('afterEnd', vel);
        },

        removeShapeId: function (shapeid) {
            var existing = $('#jqsshape' + shapeid);
            this.group.removeChild(existing[0]);
        },

        getShapeAt: function (el, x, y) {
            var shapeid = el.id.substr(8);
            return shapeid;
        },

        render: function () {
            if (!this.rendered) {
                // batch the intial render into a single repaint
                this.group.innerHTML = this.prerender;
                this.rendered = true;
            }
        }
    });

}));

 /* ************************ new file ************************ */
/*
 * jQuery Tooltip plugin 1.3
 *
 * http://bassistance.de/jquery-plugins/jquery-plugin-tooltip/
 * http://docs.jquery.com/Plugins/Tooltip
 *
 * Copyright (c) 2006 - 2008 Jörn Zaefferer
 *
 * $Id: jquery.tooltip.js 5741 2008-06-21 15:22:16Z joern.zaefferer $
 * 
 * Dual licensed under the MIT and GPL licenses:
 *   http://www.opensource.org/licenses/mit-license.php
 *   http://www.gnu.org/licenses/gpl.html
 */
 
;(function($) {
	
		// the tooltip element
	var helper = {},
		// the current tooltipped element
		current,
		// the title of the current element, used for restoring
		title,
		// timeout id for delayed tooltips
		tID,
		// IE 5.5 or 6
		IE = $.browser.msie && /MSIE\s(5\.5|6\.)/.test(navigator.userAgent),
		// flag for mouse tracking
		track = false;
	
	$.tooltip = {
		blocked: false,
		defaults: {
			delay: 200,
			fade: false,
			showURL: true,
			extraClass: "",
			top: 15,
			left: 15,
			id: "tooltip"
		},
		block: function() {
			$.tooltip.blocked = !$.tooltip.blocked;
		}
	};
	
	$.fn.extend({
		tooltip: function(settings) {
			settings = $.extend({}, $.tooltip.defaults, settings);
			createHelper(settings);
			return this.each(function() {
					$.data(this, "tooltip", settings);
					this.tOpacity = helper.parent.css("opacity");
					// copy tooltip into its own expando and remove the title
					this.tooltipText = this.title;
					$(this).removeAttr("title");
					// also remove alt attribute to prevent default tooltip in IE
					this.alt = "";
				})
				.mouseover(save)
				.mouseout(hide)
				.click(hide);
		},
		fixPNG: IE ? function() {
			return this.each(function () {
				var image = $(this).css('backgroundImage');
				if (image.match(/^url\(["']?(.*\.png)["']?\)$/i)) {
					image = RegExp.$1;
					$(this).css({
						'backgroundImage': 'none',
						'filter': "progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled=true, sizingMethod=crop, src='" + image + "')"
					}).each(function () {
						var position = $(this).css('position');
						if (position != 'absolute' && position != 'relative')
							$(this).css('position', 'relative');
					});
				}
			});
		} : function() { return this; },
		unfixPNG: IE ? function() {
			return this.each(function () {
				$(this).css({'filter': '', backgroundImage: ''});
			});
		} : function() { return this; },
		hideWhenEmpty: function() {
			return this.each(function() {
				$(this)[ $(this).html() ? "show" : "hide" ]();
			});
		},
		url: function() {
			return this.attr('href') || this.attr('src');
		}
	});
	
	function createHelper(settings) {
		// there can be only one tooltip helper
		if( helper.parent )
			return;
		// create the helper, h3 for title, div for url
		helper.parent = $('<div id="' + settings.id + '"><h3></h3><div class="body"></div><div class="url"></div></div>')
			// add to document
			.appendTo(document.body)
			// hide it at first
			.hide();
			
		// apply bgiframe if available
		if ( $.fn.bgiframe )
			helper.parent.bgiframe();
		
		// save references to title and url elements
		helper.title = $('h3', helper.parent);
		helper.body = $('div.body', helper.parent);
		helper.url = $('div.url', helper.parent);
	}
	
	function settings(element) {
		return $.data(element, "tooltip");
	}
	
	// main event handler to start showing tooltips
	function handle(event) {
		// show helper, either with timeout or on instant
		if( settings(this).delay )
			tID = setTimeout(show, settings(this).delay);
		else
			show();
		
		// if selected, update the helper position when the mouse moves
		track = !!settings(this).track;
		$(document.body).bind('mousemove', update);
			
		// update at least once
		update(event);
	}
	
	// save elements title before the tooltip is displayed
	function save() {
		// if this is the current source, or it has no title (occurs with click event), stop
		if ( $.tooltip.blocked || this == current || (!this.tooltipText && !settings(this).bodyHandler) )
			return;

		// save current
		current = this;
		title = this.tooltipText;
		
		if ( settings(this).bodyHandler ) {
			helper.title.hide();
			var bodyContent = settings(this).bodyHandler.call(this);
			if (bodyContent.nodeType || bodyContent.jquery) {
				helper.body.empty().append(bodyContent)
			} else {
				helper.body.html( bodyContent );
			}
			helper.body.show();
		} else if ( settings(this).showBody ) {
			var parts = title.split(settings(this).showBody);
			helper.title.html(parts.shift()).show();
			helper.body.empty();
			for(var i = 0, part; (part = parts[i]); i++) {
				if(i > 0)
					helper.body.append("<br/>");
				helper.body.append(part);
			}
			helper.body.hideWhenEmpty();
		} else {
			helper.title.html(title).show();
			helper.body.hide();
		}
		
		// if element has href or src, add and show it, otherwise hide it
		if( settings(this).showURL && $(this).url() )
			helper.url.html( $(this).url().replace('http://', '') ).show();
		else 
			helper.url.hide();
		
		// add an optional class for this tip
		helper.parent.addClass(settings(this).extraClass);

		// fix PNG background for IE
		if (settings(this).fixPNG )
			helper.parent.fixPNG();
			
		handle.apply(this, arguments);
	}
	
	// delete timeout and show helper
	function show() {
		tID = null;
		if ((!IE || !$.fn.bgiframe) && settings(current).fade) {
			if (helper.parent.is(":animated"))
				helper.parent.stop().show().fadeTo(settings(current).fade, current.tOpacity);
			else
				helper.parent.is(':visible') ? helper.parent.fadeTo(settings(current).fade, current.tOpacity) : helper.parent.fadeIn(settings(current).fade);
		} else {
			helper.parent.show();
		}
		update();
	}
	
	/**
	 * callback for mousemove
	 * updates the helper position
	 * removes itself when no current element
	 */
	function update(event)	{
		if($.tooltip.blocked)
			return;
		
		if (event && event.target.tagName == "OPTION") {
			return;
		}
		
		// stop updating when tracking is disabled and the tooltip is visible
		if ( !track && helper.parent.is(":visible")) {
			$(document.body).unbind('mousemove', update)
		}
		
		// if no current element is available, remove this listener
		if( current == null ) {
			$(document.body).unbind('mousemove', update);
			return;	
		}
		
		// remove position helper classes
		helper.parent.removeClass("viewport-right").removeClass("viewport-bottom");
		
		var left = helper.parent[0].offsetLeft;
		var top = helper.parent[0].offsetTop;
		if (event) {
			// position the helper 15 pixel to bottom right, starting from mouse position
			left = event.pageX + settings(current).left;
			top = event.pageY + settings(current).top;
			var right='auto';
			if (settings(current).positionLeft) {
				right = $(window).width() - left;
				left = 'auto';
			}
			helper.parent.css({
				left: left,
				right: right,
				top: top
			});
		}
		
		var v = viewport(),
			h = helper.parent[0];
		// check horizontal position
		if (v.x + v.cx < h.offsetLeft + h.offsetWidth) {
			left -= h.offsetWidth + 20 + settings(current).left;
			helper.parent.css({left: left + 'px'}).addClass("viewport-right");
		}
		// check vertical position
		if (v.y + v.cy < h.offsetTop + h.offsetHeight) {
			top -= h.offsetHeight + 20 + settings(current).top;
			helper.parent.css({top: top + 'px'}).addClass("viewport-bottom");
		}
	}
	
	function viewport() {
		return {
			x: $(window).scrollLeft(),
			y: $(window).scrollTop(),
			cx: $(window).width(),
			cy: $(window).height()
		};
	}
	
	// hide helper and restore added classes and the title
	function hide(event) {
		if($.tooltip.blocked)
			return;
		// clear timeout if possible
		if(tID)
			clearTimeout(tID);
		// no more current element
		current = null;
		
		var tsettings = settings(this);
		function complete() {
			helper.parent.removeClass( tsettings.extraClass ).hide().css("opacity", "");
		}
		if ((!IE || !$.fn.bgiframe) && tsettings.fade) {
			if (helper.parent.is(':animated'))
				helper.parent.stop().fadeTo(tsettings.fade, 0, complete);
			else
				helper.parent.stop().fadeOut(tsettings.fade, complete);
		} else
			complete();
		
		if( settings(this).fixPNG )
			helper.parent.unfixPNG();
	}
	
})(jQuery);

 /* ************************ new file ************************ */
/*
 * File:        jquery.dataTables.min.js
 * Version:     1.7.4
 * Author:      Allan Jardine (www.sprymedia.co.uk)
 * Info:        www.datatables.net
 * 
 * Copyright 2008-2010 Allan Jardine, all rights reserved.
 *
 * This source file is free software, under either the GPL v2 license or a
 * BSD style license, as supplied with this software.
 * 
 * This source file is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the license files for details.
 */
(function (j, X, p) {
    j.fn.dataTableSettings = [];
    var E = j.fn.dataTableSettings;
    j.fn.dataTableExt = {};
    var n = j.fn.dataTableExt;
    n.sVersion = "1.7.4";
    n.sErrMode = "alert";
    n.iApiIndex = 0;
    n.oApi = {};
    n.afnFiltering = [];
    n.aoFeatures = [];
    n.ofnSearch = {};
    n.afnSortData = [];
    n.oStdClasses = {sPagePrevEnabled: "paginate_enabled_previous", sPagePrevDisabled: "paginate_disabled_previous", sPageNextEnabled: "paginate_enabled_next", sPageNextDisabled: "paginate_disabled_next", sPageJUINext: "", sPageJUIPrev: "", sPageButton: "paginate_button", sPageButtonActive: "paginate_active",
        sPageButtonStaticDisabled: "paginate_button", sPageFirst: "first", sPagePrevious: "previous", sPageNext: "next", sPageLast: "last", sStripOdd: "odd", sStripEven: "even", sRowEmpty: "dataTables_empty", sWrapper: "dataTables_wrapper", sFilter: "dataTables_filter", sInfo: "dataTables_info", sPaging: "dataTables_paginate paging_", sLength: "dataTables_length", sProcessing: "dataTables_processing", sSortAsc: "sorting_asc", sSortDesc: "sorting_desc", sSortable: "sorting", sSortableAsc: "sorting_asc_disabled", sSortableDesc: "sorting_desc_disabled",
        sSortableNone: "sorting_disabled", sSortColumn: "sorting_", sSortJUIAsc: "", sSortJUIDesc: "", sSortJUI: "", sSortJUIAscAllowed: "", sSortJUIDescAllowed: "", sSortJUIWrapper: "", sScrollWrapper: "dataTables_scroll", sScrollHead: "dataTables_scrollHead", sScrollHeadInner: "dataTables_scrollHeadInner", sScrollBody: "dataTables_scrollBody", sScrollFoot: "dataTables_scrollFoot", sScrollFootInner: "dataTables_scrollFootInner", sFooterTH: ""};
    n.oJUIClasses = {sPagePrevEnabled: "fg-button ui-button ui-state-default ui-corner-left", sPagePrevDisabled: "fg-button ui-button ui-state-default ui-corner-left ui-state-disabled",
        sPageNextEnabled: "fg-button ui-button ui-state-default ui-corner-right", sPageNextDisabled: "fg-button ui-button ui-state-default ui-corner-right ui-state-disabled", sPageJUINext: "ui-icon ui-icon-circle-arrow-e", sPageJUIPrev: "ui-icon ui-icon-circle-arrow-w", sPageButton: "fg-button ui-button ui-state-default", sPageButtonActive: "fg-button ui-button ui-state-default ui-state-disabled", sPageButtonStaticDisabled: "fg-button ui-button ui-state-default ui-state-disabled", sPageFirst: "first ui-corner-tl ui-corner-bl",
        sPagePrevious: "previous", sPageNext: "next", sPageLast: "last ui-corner-tr ui-corner-br", sStripOdd: "odd", sStripEven: "even", sRowEmpty: "dataTables_empty", sWrapper: "dataTables_wrapper", sFilter: "dataTables_filter", sInfo: "dataTables_info", sPaging: "dataTables_paginate fg-buttonset ui-buttonset fg-buttonset-multi ui-buttonset-multi paging_", sLength: "dataTables_length", sProcessing: "dataTables_processing", sSortAsc: "ui-state-default", sSortDesc: "ui-state-default", sSortable: "ui-state-default", sSortableAsc: "ui-state-default",
        sSortableDesc: "ui-state-default", sSortableNone: "ui-state-default", sSortColumn: "sorting_", sSortJUIAsc: "css_right ui-icon ui-icon-triangle-1-n", sSortJUIDesc: "css_right ui-icon ui-icon-triangle-1-s", sSortJUI: "css_right ui-icon ui-icon-carat-2-n-s", sSortJUIAscAllowed: "css_right ui-icon ui-icon-carat-1-n", sSortJUIDescAllowed: "css_right ui-icon ui-icon-carat-1-s", sSortJUIWrapper: "DataTables_sort_wrapper", sScrollWrapper: "dataTables_scroll", sScrollHead: "dataTables_scrollHead ui-state-default", sScrollHeadInner: "dataTables_scrollHeadInner",
        sScrollBody: "dataTables_scrollBody", sScrollFoot: "dataTables_scrollFoot ui-state-default", sScrollFootInner: "dataTables_scrollFootInner", sFooterTH: "ui-state-default"};
    n.oPagination = {two_button: {fnInit: function (g, l, r) {
        var s, v, y;
        if (g.bJUI) {
            s = p.createElement("a");
            v = p.createElement("a");
            y = p.createElement("span");
            y.className = g.oClasses.sPageJUINext;
            v.appendChild(y);
            y = p.createElement("span");
            y.className = g.oClasses.sPageJUIPrev;
            s.appendChild(y)
        } else {
            s = p.createElement("div");
            v = p.createElement("div")
        }
        s.className =
            g.oClasses.sPagePrevDisabled;
        v.className = g.oClasses.sPageNextDisabled;
        s.title = g.oLanguage.oPaginate.sPrevious;
        v.title = g.oLanguage.oPaginate.sNext;
        l.appendChild(s);
        l.appendChild(v);
        j(s).click(function () {
            g.oApi._fnPageChange(g, "previous") && r(g)
        });
        j(v).click(function () {
            g.oApi._fnPageChange(g, "next") && r(g)
        });
        j(s).bind("selectstart", function () {
            return false
        });
        j(v).bind("selectstart", function () {
            return false
        });
        if (g.sTableId !== "" && typeof g.aanFeatures.p == "undefined") {
            l.setAttribute("id", g.sTableId + "_paginate");
            s.setAttribute("id", g.sTableId + "_previous");
            v.setAttribute("id", g.sTableId + "_next")
        }
    }, fnUpdate: function (g) {
        if (g.aanFeatures.p)for (var l = g.aanFeatures.p, r = 0, s = l.length; r < s; r++)if (l[r].childNodes.length !== 0) {
            l[r].childNodes[0].className = g._iDisplayStart === 0 ? g.oClasses.sPagePrevDisabled : g.oClasses.sPagePrevEnabled;
            l[r].childNodes[1].className = g.fnDisplayEnd() == g.fnRecordsDisplay() ? g.oClasses.sPageNextDisabled : g.oClasses.sPageNextEnabled
        }
    }}, iFullNumbersShowPages: 5, full_numbers: {fnInit: function (g, l, r) {
        var s =
            p.createElement("span"), v = p.createElement("span"), y = p.createElement("span"), D = p.createElement("span"), w = p.createElement("span");
        s.innerHTML = g.oLanguage.oPaginate.sFirst;
        v.innerHTML = g.oLanguage.oPaginate.sPrevious;
        D.innerHTML = g.oLanguage.oPaginate.sNext;
        w.innerHTML = g.oLanguage.oPaginate.sLast;
        var x = g.oClasses;
        s.className = x.sPageButton + " " + x.sPageFirst;
        v.className = x.sPageButton + " " + x.sPagePrevious;
        D.className = x.sPageButton + " " + x.sPageNext;
        w.className = x.sPageButton + " " + x.sPageLast;
        l.appendChild(s);
        l.appendChild(v);
        l.appendChild(y);
        l.appendChild(D);
        l.appendChild(w);
        j(s).click(function () {
            g.oApi._fnPageChange(g, "first") && r(g)
        });
        j(v).click(function () {
            g.oApi._fnPageChange(g, "previous") && r(g)
        });
        j(D).click(function () {
            g.oApi._fnPageChange(g, "next") && r(g)
        });
        j(w).click(function () {
            g.oApi._fnPageChange(g, "last") && r(g)
        });
        j("span", l).bind("mousedown",function () {
            return false
        }).bind("selectstart", function () {
            return false
        });
        if (g.sTableId !== "" && typeof g.aanFeatures.p == "undefined") {
            l.setAttribute("id", g.sTableId + "_paginate");
            s.setAttribute("id", g.sTableId + "_first");
            v.setAttribute("id", g.sTableId + "_previous");
            D.setAttribute("id", g.sTableId + "_next");
            w.setAttribute("id", g.sTableId + "_last")
        }
    }, fnUpdate: function (g, l) {
        if (g.aanFeatures.p) {
            var r = n.oPagination.iFullNumbersShowPages, s = Math.floor(r / 2), v = Math.ceil(g.fnRecordsDisplay() / g._iDisplayLength), y = Math.ceil(g._iDisplayStart / g._iDisplayLength) + 1, D = "", w, x = g.oClasses;
            if (v < r) {
                s = 1;
                w = v
            } else if (y <= s) {
                s = 1;
                w = r
            } else if (y >= v - s) {
                s = v - r + 1;
                w = v
            } else {
                s = y - Math.ceil(r / 2) + 1;
                w = s + r - 1
            }
            for (r = s; r <=
                w; r++)D += y != r ? '<span class="' + x.sPageButton + '">' + r + "</span>" : '<span class="' + x.sPageButtonActive + '">' + r + "</span>";
            w = g.aanFeatures.p;
            var z, C = function () {
                g._iDisplayStart = (this.innerHTML * 1 - 1) * g._iDisplayLength;
                l(g);
                return false
            }, L = function () {
                return false
            };
            r = 0;
            for (s = w.length; r < s; r++)if (w[r].childNodes.length !== 0) {
                z = j("span:eq(2)", w[r]);
                z.html(D);
                j("span", z).click(C).bind("mousedown", L).bind("selectstart", L);
                z = w[r].getElementsByTagName("span");
                z = [z[0], z[1], z[z.length - 2], z[z.length - 1]];
                j(z).removeClass(x.sPageButton +
                    " " + x.sPageButtonActive + " " + x.sPageButtonStaticDisabled);
                if (y == 1) {
                    z[0].className += " " + x.sPageButtonStaticDisabled;
                    z[1].className += " " + x.sPageButtonStaticDisabled
                } else {
                    z[0].className += " " + x.sPageButton;
                    z[1].className += " " + x.sPageButton
                }
                if (v === 0 || y == v || g._iDisplayLength == -1) {
                    z[2].className += " " + x.sPageButtonStaticDisabled;
                    z[3].className += " " + x.sPageButtonStaticDisabled
                } else {
                    z[2].className += " " + x.sPageButton;
                    z[3].className += " " + x.sPageButton
                }
            }
        }
    }}};
    n.oSort = {"string-asc": function (g, l) {
        g = g.toLowerCase();
        l = l.toLowerCase();
        return g < l ? -1 : g > l ? 1 : 0
    }, "string-desc": function (g, l) {
        g = g.toLowerCase();
        l = l.toLowerCase();
        return g < l ? 1 : g > l ? -1 : 0
    }, "html-asc": function (g, l) {
        g = g.replace(/<.*?>/g, "").toLowerCase();
        l = l.replace(/<.*?>/g, "").toLowerCase();
        return g < l ? -1 : g > l ? 1 : 0
    }, "html-desc": function (g, l) {
        g = g.replace(/<.*?>/g, "").toLowerCase();
        l = l.replace(/<.*?>/g, "").toLowerCase();
        return g < l ? 1 : g > l ? -1 : 0
    }, "date-asc": function (g, l) {
        g = Date.parse(g);
        l = Date.parse(l);
        if (isNaN(g) || g === "")g = Date.parse("01/01/1970 00:00:00");
        if (isNaN(l) ||
            l === "")l = Date.parse("01/01/1970 00:00:00");
        return g - l
    }, "date-desc": function (g, l) {
        g = Date.parse(g);
        l = Date.parse(l);
        if (isNaN(g) || g === "")g = Date.parse("01/01/1970 00:00:00");
        if (isNaN(l) || l === "")l = Date.parse("01/01/1970 00:00:00");
        return l - g
    }, "numeric-asc": function (g, l) {
        return(g == "-" || g === "" ? 0 : g * 1) - (l == "-" || l === "" ? 0 : l * 1)
    }, "numeric-desc": function (g, l) {
        return(l == "-" || l === "" ? 0 : l * 1) - (g == "-" || g === "" ? 0 : g * 1)
    }};
    n.aTypes = [function (g) {
        if (g.length === 0)return"numeric";
        var l, r = false;
        l = g.charAt(0);
        if ("0123456789-".indexOf(l) == -1)return null;
        for (var s = 1; s < g.length; s++) {
            l = g.charAt(s);
            if ("0123456789.".indexOf(l) == -1)return null;
            if (l == ".") {
                if (r)return null;
                r = true
            }
        }
        return"numeric"
    }, function (g) {
        var l = Date.parse(g);
        if (l !== null && !isNaN(l) || g.length === 0)return"date";
        return null
    }, function (g) {
        if (g.indexOf("<") != -1 && g.indexOf(">") != -1)return"html";
        return null
    }];
    n.fnVersionCheck = function (g) {
        var l = function (w, x) {
            for (; w.length < x;)w += "0";
            return w
        }, r = n.sVersion.split(".");
        g = g.split(".");
        for (var s = "", v = "", y = 0, D = g.length; y < D; y++) {
            s += l(r[y],
                3);
            v += l(g[y], 3)
        }
        return parseInt(s, 10) >= parseInt(v, 10)
    };
    n._oExternConfig = {iNextUnique: 0};
    j.fn.dataTable = function (g) {
        function l() {
            this.fnRecordsTotal = function () {
                return this.oFeatures.bServerSide ? parseInt(this._iRecordsTotal, 10) : this.aiDisplayMaster.length
            };
            this.fnRecordsDisplay = function () {
                return this.oFeatures.bServerSide ? parseInt(this._iRecordsDisplay, 10) : this.aiDisplay.length
            };
            this.fnDisplayEnd = function () {
                return this.oFeatures.bServerSide ? this.oFeatures.bPaginate === false || this._iDisplayLength == -1 ?
                    this._iDisplayStart + this.aiDisplay.length : Math.min(this._iDisplayStart + this._iDisplayLength, this._iRecordsDisplay) : this._iDisplayEnd
            };
            this.sInstance = this.oInstance = null;
            this.oFeatures = {bPaginate: true, bLengthChange: true, bFilter: true, bSort: true, bInfo: true, bAutoWidth: true, bProcessing: false, bSortClasses: true, bStateSave: false, bServerSide: false};
            this.oScroll = {sX: "", sXInner: "", sY: "", bCollapse: false, bInfinite: false, iLoadGap: 100, iBarWidth: 0};
            this.aanFeatures = [];
            this.oLanguage = {sProcessing: "Processing...",
                sLengthMenu: "Show _MENU_ entries", sZeroRecords: "No matching records found", sEmptyTable: "No data available in table", sInfo: "Showing _START_ to _END_ of _TOTAL_ entries", sInfoEmpty: "Showing 0 to 0 of 0 entries", sInfoFiltered: "(filtered from _MAX_ total entries)", sInfoPostFix: "", sSearch: "Search:", sUrl: "", oPaginate: {sFirst: "First", sPrevious: "Previous", sNext: "Next", sLast: "Last"}, fnInfoCallback: null};
            this.aoData = [];
            this.aiDisplay = [];
            this.aiDisplayMaster = [];
            this.aoColumns = [];
            this.iNextId = 0;
            this.asDataSearch =
                [];
            this.oPreviousSearch = {sSearch: "", bRegex: false, bSmart: true};
            this.aoPreSearchCols = [];
            this.aaSorting = [
                [0, "asc", 0]
            ];
            this.aaSortingFixed = null;
            this.asStripClasses = [];
            this.asDestoryStrips = [];
            this.sDestroyWidth = 0;
            this.fnFooterCallback = this.fnHeaderCallback = this.fnRowCallback = null;
            this.aoDrawCallback = [];
            this.fnInitComplete = null;
            this.sTableId = "";
            this.nTableWrapper = this.nTBody = this.nTFoot = this.nTHead = this.nTable = null;
            this.bInitialised = false;
            this.aoOpenRows = [];
            this.sDom = "lfrtip";
            this.sPaginationType = "two_button";
            this.iCookieDuration = 7200;
            this.sCookiePrefix = "SpryMedia_DataTables_";
            this.fnCookieCallback = null;
            this.aoStateSave = [];
            this.aoStateLoad = [];
            this.sAjaxSource = this.oLoadedState = null;
            this.bAjaxDataGet = true;
            this.fnServerData = function (a, b, c) {
                j.ajax({url: a, data: b, success: c, dataType: "json", cache: false, error: function (d, f) {
                    f == "parsererror" && alert("DataTables warning: JSON data from server could not be parsed. This is caused by a JSON formatting error.")
                }})
            };
            this.fnFormatNumber = function (a) {
                if (a < 1E3)return a; else {
                    var b =
                        a + "";
                    a = b.split("");
                    var c = "";
                    b = b.length;
                    for (var d = 0; d < b; d++) {
                        if (d % 3 === 0 && d !== 0)c = "," + c;
                        c = a[b - d - 1] + c
                    }
                }
                return c
            };
            this.aLengthMenu = [10, 25, 50, 100];
            this.bDrawing = this.iDraw = 0;
            this.iDrawError = -1;
            this._iDisplayLength = 10;
            this._iDisplayStart = 0;
            this._iDisplayEnd = 10;
            this._iRecordsDisplay = this._iRecordsTotal = 0;
            this.bJUI = false;
            this.oClasses = n.oStdClasses;
            this.bSorted = this.bFiltered = false;
            this.oInit = null
        }

        function r(a) {
            return function () {
                var b = [B(this[n.iApiIndex])].concat(Array.prototype.slice.call(arguments));
                return n.oApi[a].apply(this,
                    b)
            }
        }

        function s(a) {
            var b, c;
            if (a.bInitialised === false)setTimeout(function () {
                s(a)
            }, 200); else {
                pa(a);
                z(a);
                K(a, true);
                a.oFeatures.bAutoWidth && Y(a);
                b = 0;
                for (c = a.aoColumns.length; b < c; b++)if (a.aoColumns[b].sWidth !== null)a.aoColumns[b].nTh.style.width = u(a.aoColumns[b].sWidth);
                if (a.oFeatures.bSort)O(a); else {
                    a.aiDisplay = a.aiDisplayMaster.slice();
                    F(a);
                    C(a)
                }
                if (a.sAjaxSource !== null && !a.oFeatures.bServerSide)a.fnServerData.call(a.oInstance, a.sAjaxSource, [], function (d) {
                    for (b = 0; b < d.aaData.length; b++)w(a, d.aaData[b]);
                    a.iInitDisplayStart = a._iDisplayStart;
                    if (a.oFeatures.bSort)O(a); else {
                        a.aiDisplay = a.aiDisplayMaster.slice();
                        F(a);
                        C(a)
                    }
                    K(a, false);
                    typeof a.fnInitComplete == "function" && a.fnInitComplete.call(a.oInstance, a, d)
                }); else a.oFeatures.bServerSide || K(a, false)
            }
        }

        function v(a, b, c) {
            o(a.oLanguage, b, "sProcessing");
            o(a.oLanguage, b, "sLengthMenu");
            o(a.oLanguage, b, "sEmptyTable");
            o(a.oLanguage, b, "sZeroRecords");
            o(a.oLanguage, b, "sInfo");
            o(a.oLanguage, b, "sInfoEmpty");
            o(a.oLanguage, b, "sInfoFiltered");
            o(a.oLanguage, b, "sInfoPostFix");
            o(a.oLanguage, b, "sSearch");
            if (typeof b.oPaginate != "undefined") {
                o(a.oLanguage.oPaginate, b.oPaginate, "sFirst");
                o(a.oLanguage.oPaginate, b.oPaginate, "sPrevious");
                o(a.oLanguage.oPaginate, b.oPaginate, "sNext");
                o(a.oLanguage.oPaginate, b.oPaginate, "sLast")
            }
            typeof b.sEmptyTable == "undefined" && typeof b.sZeroRecords != "undefined" && o(a.oLanguage, b, "sZeroRecords", "sEmptyTable");
            c && s(a)
        }

        function y(a, b) {
            a.aoColumns[a.aoColumns.length++] = {sType: null, _bAutoType: true, bVisible: true, bSearchable: true, bSortable: true, asSorting: ["asc",
                "desc"], sSortingClass: a.oClasses.sSortable, sSortingClassJUI: a.oClasses.sSortJUI, sTitle: b ? b.innerHTML : "", sName: "", sWidth: null, sWidthOrig: null, sClass: null, fnRender: null, bUseRendered: true, iDataSort: a.aoColumns.length - 1, sSortDataType: "std", nTh: b ? b : p.createElement("th"), nTf: null};
            b = a.aoColumns.length - 1;
            if (typeof a.aoPreSearchCols[b] == "undefined" || a.aoPreSearchCols[b] === null)a.aoPreSearchCols[b] = {sSearch: "", bRegex: false, bSmart: true}; else {
                if (typeof a.aoPreSearchCols[b].bRegex == "undefined")a.aoPreSearchCols[b].bRegex =
                    true;
                if (typeof a.aoPreSearchCols[b].bSmart == "undefined")a.aoPreSearchCols[b].bSmart = true
            }
            D(a, b, null)
        }

        function D(a, b, c) {
            b = a.aoColumns[b];
            if (typeof c != "undefined" && c !== null) {
                if (typeof c.sType != "undefined") {
                    b.sType = c.sType;
                    b._bAutoType = false
                }
                o(b, c, "bVisible");
                o(b, c, "bSearchable");
                o(b, c, "bSortable");
                o(b, c, "sTitle");
                o(b, c, "sName");
                o(b, c, "sWidth");
                o(b, c, "sWidth", "sWidthOrig");
                o(b, c, "sClass");
                o(b, c, "fnRender");
                o(b, c, "bUseRendered");
                o(b, c, "iDataSort");
                o(b, c, "asSorting");
                o(b, c, "sSortDataType")
            }
            if (!a.oFeatures.bSort)b.bSortable =
                false;
            if (!b.bSortable || j.inArray("asc", b.asSorting) == -1 && j.inArray("desc", b.asSorting) == -1) {
                b.sSortingClass = a.oClasses.sSortableNone;
                b.sSortingClassJUI = ""
            } else if (j.inArray("asc", b.asSorting) != -1 && j.inArray("desc", b.asSorting) == -1) {
                b.sSortingClass = a.oClasses.sSortableAsc;
                b.sSortingClassJUI = a.oClasses.sSortJUIAscAllowed
            } else if (j.inArray("asc", b.asSorting) == -1 && j.inArray("desc", b.asSorting) != -1) {
                b.sSortingClass = a.oClasses.sSortableDesc;
                b.sSortingClassJUI = a.oClasses.sSortJUIDescAllowed
            }
        }

        function w(a, b) {
            if (b.length != a.aoColumns.length && a.iDrawError != a.iDraw) {
                I(a, 0, "Added data (size " + b.length + ") does not match known number of columns (" + a.aoColumns.length + ")");
                a.iDrawError = a.iDraw;
                return-1
            }
            b = b.slice();
            var c = a.aoData.length;
            a.aoData.push({nTr: p.createElement("tr"), _iId: a.iNextId++, _aData: b, _anHidden: [], _sRowStripe: ""});
            for (var d, f, e = 0; e < b.length; e++) {
                d = p.createElement("td");
                if (b[e] === null)b[e] = "";
                if (typeof a.aoColumns[e].fnRender == "function") {
                    f = a.aoColumns[e].fnRender({iDataRow: c, iDataColumn: e,
                        aData: b, oSettings: a});
                    d.innerHTML = f;
                    if (a.aoColumns[e].bUseRendered)a.aoData[c]._aData[e] = f
                } else d.innerHTML = b[e];
                if (typeof b[e] != "string")b[e] += "";
                b[e] = j.trim(b[e]);
                if (a.aoColumns[e].sClass !== null)d.className = a.aoColumns[e].sClass;
                if (a.aoColumns[e]._bAutoType && a.aoColumns[e].sType != "string") {
                    f = Z(a.aoData[c]._aData[e]);
                    if (a.aoColumns[e].sType === null)a.aoColumns[e].sType = f; else if (a.aoColumns[e].sType != f)a.aoColumns[e].sType = "string"
                }
                if (a.aoColumns[e].bVisible) {
                    a.aoData[c].nTr.appendChild(d);
                    a.aoData[c]._anHidden[e] =
                        null
                } else a.aoData[c]._anHidden[e] = d
            }
            a.aiDisplayMaster.push(c);
            return c
        }

        function x(a) {
            var b, c, d, f, e, i, h, k;
            if (a.sAjaxSource === null) {
                h = a.nTBody.childNodes;
                b = 0;
                for (c = h.length; b < c; b++)if (h[b].nodeName.toUpperCase() == "TR") {
                    i = a.aoData.length;
                    a.aoData.push({nTr: h[b], _iId: a.iNextId++, _aData: [], _anHidden: [], _sRowStripe: ""});
                    a.aiDisplayMaster.push(i);
                    k = a.aoData[i]._aData;
                    i = h[b].childNodes;
                    d = e = 0;
                    for (f = i.length; d < f; d++)if (i[d].nodeName.toUpperCase() == "TD") {
                        k[e] = j.trim(i[d].innerHTML);
                        e++
                    }
                }
            }
            h = R(a);
            i = [];
            b = 0;
            for (c =
                     h.length; b < c; b++) {
                d = 0;
                for (f = h[b].childNodes.length; d < f; d++) {
                    e = h[b].childNodes[d];
                    e.nodeName.toUpperCase() == "TD" && i.push(e)
                }
            }
            i.length != h.length * a.aoColumns.length && I(a, 1, "Unexpected number of TD elements. Expected " + h.length * a.aoColumns.length + " and got " + i.length + ". DataTables does not support rowspan / colspan in the table body, and there must be one cell for each row/column combination.");
            h = 0;
            for (d = a.aoColumns.length; h < d; h++) {
                if (a.aoColumns[h].sTitle === null)a.aoColumns[h].sTitle = a.aoColumns[h].nTh.innerHTML;
                f = a.aoColumns[h]._bAutoType;
                e = typeof a.aoColumns[h].fnRender == "function";
                k = a.aoColumns[h].sClass !== null;
                var m = a.aoColumns[h].bVisible, q, t;
                if (f || e || k || !m) {
                    b = 0;
                    for (c = a.aoData.length; b < c; b++) {
                        q = i[b * d + h];
                        if (f)if (a.aoColumns[h].sType != "string") {
                            t = Z(a.aoData[b]._aData[h]);
                            if (a.aoColumns[h].sType === null)a.aoColumns[h].sType = t; else if (a.aoColumns[h].sType != t)a.aoColumns[h].sType = "string"
                        }
                        if (e) {
                            t = a.aoColumns[h].fnRender({iDataRow: b, iDataColumn: h, aData: a.aoData[b]._aData, oSettings: a});
                            q.innerHTML = t;
                            if (a.aoColumns[h].bUseRendered)a.aoData[b]._aData[h] =
                                t
                        }
                        if (k)q.className += " " + a.aoColumns[h].sClass;
                        if (m)a.aoData[b]._anHidden[h] = null; else {
                            a.aoData[b]._anHidden[h] = q;
                            q.parentNode.removeChild(q)
                        }
                    }
                }
            }
        }

        function z(a) {
            var b, c, d, f = 0;
            if (a.nTHead.getElementsByTagName("th").length !== 0) {
                b = 0;
                for (d = a.aoColumns.length; b < d; b++) {
                    c = a.aoColumns[b].nTh;
                    if (a.aoColumns[b].bVisible) {
                        if (a.aoColumns[b].sTitle != c.innerHTML)c.innerHTML = a.aoColumns[b].sTitle
                    } else {
                        c.parentNode.removeChild(c);
                        f++
                    }
                }
            } else {
                f = p.createElement("tr");
                b = 0;
                for (d = a.aoColumns.length; b < d; b++) {
                    c = a.aoColumns[b].nTh;
                    c.innerHTML = a.aoColumns[b].sTitle;
                    if (a.aoColumns[b].bVisible) {
                        if (a.aoColumns[b].sClass !== null)c.className = a.aoColumns[b].sClass;
                        f.appendChild(c)
                    }
                }
                j(a.nTHead).html("")[0].appendChild(f)
            }
            if (a.bJUI) {
                b = 0;
                for (d = a.aoColumns.length; b < d; b++) {
                    c = a.aoColumns[b].nTh;
                    f = p.createElement("div");
                    f.className = a.oClasses.sSortJUIWrapper;
                    j(c).contents().appendTo(f);
                    f.appendChild(p.createElement("span"));
                    c.appendChild(f)
                }
            }
            d = function () {
                this.onselectstart = function () {
                    return false
                };
                return false
            };
            if (a.oFeatures.bSort)for (b =
                                           0; b < a.aoColumns.length; b++)if (a.aoColumns[b].bSortable !== false) {
                $(a, a.aoColumns[b].nTh, b);
                j(a.aoColumns[b].nTh).mousedown(d)
            } else j(a.aoColumns[b].nTh).addClass(a.oClasses.sSortableNone);
            if (a.nTFoot !== null) {
                f = 0;
                c = a.nTFoot.getElementsByTagName("th");
                b = 0;
                for (d = c.length; b < d; b++)if (typeof a.aoColumns[b] != "undefined") {
                    a.aoColumns[b].nTf = c[b - f];
                    if (a.oClasses.sFooterTH !== "")a.aoColumns[b].nTf.className += " " + a.oClasses.sFooterTH;
                    if (!a.aoColumns[b].bVisible) {
                        c[b - f].parentNode.removeChild(c[b - f]);
                        f++
                    }
                }
            }
        }

        function C(a) {
            var b,
                c, d = [], f = 0, e = false;
            b = a.asStripClasses.length;
            c = a.aoOpenRows.length;
            a.bDrawing = true;
            if (typeof a.iInitDisplayStart != "undefined" && a.iInitDisplayStart != -1) {
                a._iDisplayStart = a.oFeatures.bServerSide ? a.iInitDisplayStart : a.iInitDisplayStart >= a.fnRecordsDisplay() ? 0 : a.iInitDisplayStart;
                a.iInitDisplayStart = -1;
                F(a)
            }
            if (!(a.oFeatures.bServerSide && !qa(a))) {
                a.oFeatures.bServerSide || a.iDraw++;
                if (a.aiDisplay.length !== 0) {
                    var i = a._iDisplayStart, h = a._iDisplayEnd;
                    if (a.oFeatures.bServerSide) {
                        i = 0;
                        h = a.aoData.length
                    }
                    for (i =
                             i; i < h; i++) {
                        var k = a.aoData[a.aiDisplay[i]], m = k.nTr;
                        if (b !== 0) {
                            var q = a.asStripClasses[f % b];
                            if (k._sRowStripe != q) {
                                j(m).removeClass(k._sRowStripe).addClass(q);
                                k._sRowStripe = q
                            }
                        }
                        if (typeof a.fnRowCallback == "function") {
                            m = a.fnRowCallback.call(a.oInstance, m, a.aoData[a.aiDisplay[i]]._aData, f, i);
                            if (!m && !e) {
                                I(a, 0, "A node was not returned by fnRowCallback");
                                e = true
                            }
                        }
                        d.push(m);
                        f++;
                        if (c !== 0)for (k = 0; k < c; k++)m == a.aoOpenRows[k].nParent && d.push(a.aoOpenRows[k].nTr)
                    }
                } else {
                    d[0] = p.createElement("tr");
                    if (typeof a.asStripClasses[0] !=
                        "undefined")d[0].className = a.asStripClasses[0];
                    e = p.createElement("td");
                    e.setAttribute("valign", "top");
                    e.colSpan = S(a);
                    e.className = a.oClasses.sRowEmpty;
                    e.innerHTML = typeof a.oLanguage.sEmptyTable != "undefined" && a.fnRecordsTotal() === 0 ? a.oLanguage.sEmptyTable : a.oLanguage.sZeroRecords.replace("_MAX_", a.fnFormatNumber(a.fnRecordsTotal()));
                    d[f].appendChild(e)
                }
                typeof a.fnHeaderCallback == "function" && a.fnHeaderCallback.call(a.oInstance, j(">tr", a.nTHead)[0], U(a), a._iDisplayStart, a.fnDisplayEnd(), a.aiDisplay);
                typeof a.fnFooterCallback == "function" && a.fnFooterCallback.call(a.oInstance, j(">tr", a.nTFoot)[0], U(a), a._iDisplayStart, a.fnDisplayEnd(), a.aiDisplay);
                f = p.createDocumentFragment();
                b = p.createDocumentFragment();
                if (a.nTBody) {
                    e = a.nTBody.parentNode;
                    b.appendChild(a.nTBody);
                    if (!a.oScroll.bInfinite || !a._bInitComplete || a.bSorted || a.bFiltered) {
                        c = a.nTBody.childNodes;
                        for (b = c.length - 1; b >= 0; b--)c[b].parentNode.removeChild(c[b])
                    }
                    b = 0;
                    for (c = d.length; b < c; b++)f.appendChild(d[b]);
                    a.nTBody.appendChild(f);
                    e !== null && e.appendChild(a.nTBody)
                }
                b =
                    0;
                for (c = a.aoDrawCallback.length; b < c; b++)a.aoDrawCallback[b].fn.call(a.oInstance, a);
                a.bSorted = false;
                a.bFiltered = false;
                a.bDrawing = false;
                if (typeof a._bInitComplete == "undefined") {
                    a._bInitComplete = true;
                    if (typeof a.fnInitComplete == "function" && (a.oFeatures.bServerSide || a.sAjaxSource === null))a.fnInitComplete.call(a.oInstance, a)
                }
            }
        }

        function L(a) {
            if (a.oFeatures.bSort)O(a, a.oPreviousSearch); else if (a.oFeatures.bFilter)P(a, a.oPreviousSearch); else {
                F(a);
                C(a)
            }
        }

        function qa(a) {
            if (a.bAjaxDataGet) {
                K(a, true);
                var b = a.aoColumns.length,
                    c = [], d;
                a.iDraw++;
                c.push({name: "sEcho", value: a.iDraw});
                c.push({name: "iColumns", value: b});
                c.push({name: "sColumns", value: aa(a)});
                c.push({name: "iDisplayStart", value: a._iDisplayStart});
                c.push({name: "iDisplayLength", value: a.oFeatures.bPaginate !== false ? a._iDisplayLength : -1});
                var f = [];
                for (d = 0; d < b; d++)f.push(a.aoColumns[d].sName);
                c.push({name: "sNames", value: f.join(",")});
                if (a.oFeatures.bFilter !== false) {
                    c.push({name: "sSearch", value: a.oPreviousSearch.sSearch});
                    c.push({name: "bRegex", value: a.oPreviousSearch.bRegex});
                    for (d = 0; d < b; d++) {
                        c.push({name: "sSearch_" + d, value: a.aoPreSearchCols[d].sSearch});
                        c.push({name: "bRegex_" + d, value: a.aoPreSearchCols[d].bRegex});
                        c.push({name: "bSearchable_" + d, value: a.aoColumns[d].bSearchable})
                    }
                }
                if (a.oFeatures.bSort !== false) {
                    f = a.aaSortingFixed !== null ? a.aaSortingFixed.length : 0;
                    var e = a.aaSorting.length;
                    c.push({name: "iSortingCols", value: f + e});
                    for (d = 0; d < f; d++) {
                        c.push({name: "iSortCol_" + d, value: a.aaSortingFixed[d][0]});
                        c.push({name: "sSortDir_" + d, value: a.aaSortingFixed[d][1]})
                    }
                    for (d = 0; d < e; d++) {
                        c.push({name: "iSortCol_" +
                            (d + f), value: a.aaSorting[d][0]});
                        c.push({name: "sSortDir_" + (d + f), value: a.aaSorting[d][1]})
                    }
                    for (d = 0; d < b; d++)c.push({name: "bSortable_" + d, value: a.aoColumns[d].bSortable})
                }
                a.fnServerData.call(a.oInstance, a.sAjaxSource, c, function (i) {
                    ra(a, i)
                });
                return false
            } else return true
        }

        function ra(a, b) {
            if (typeof b.sEcho != "undefined")if (b.sEcho * 1 < a.iDraw)return; else a.iDraw = b.sEcho * 1;
            if (!a.oScroll.bInfinite || a.oScroll.bInfinite && (a.bSorted || a.bFiltered))ba(a);
            a._iRecordsTotal = b.iTotalRecords;
            a._iRecordsDisplay = b.iTotalDisplayRecords;
            var c = aa(a);
            if (c = typeof b.sColumns != "undefined" && c !== "" && b.sColumns != c)var d = sa(a, b.sColumns);
            for (var f = 0, e = b.aaData.length; f < e; f++)if (c) {
                for (var i = [], h = 0, k = a.aoColumns.length; h < k; h++)i.push(b.aaData[f][d[h]]);
                w(a, i)
            } else w(a, b.aaData[f]);
            a.aiDisplay = a.aiDisplayMaster.slice();
            a.bAjaxDataGet = false;
            C(a);
            a.bAjaxDataGet = true;
            K(a, false)
        }

        function pa(a) {
            var b = p.createElement("div");
            a.nTable.parentNode.insertBefore(b, a.nTable);
            a.nTableWrapper = p.createElement("div");
            a.nTableWrapper.className = a.oClasses.sWrapper;
            a.sTableId !== "" && a.nTableWrapper.setAttribute("id", a.sTableId + "_wrapper");
            for (var c = a.nTableWrapper, d = a.sDom.split(""), f, e, i, h, k, m, q, t = 0; t < d.length; t++) {
                e = 0;
                i = d[t];
                if (i == "<") {
                    h = p.createElement("div");
                    k = d[t + 1];
                    if (k == "'" || k == '"') {
                        m = "";
                        for (q = 2; d[t + q] != k;) {
                            m += d[t + q];
                            q++
                        }
                        if (m == "H")m = "fg-toolbar ui-toolbar ui-widget-header ui-corner-tl ui-corner-tr ui-helper-clearfix"; else if (m == "F")m = "fg-toolbar ui-toolbar ui-widget-header ui-corner-bl ui-corner-br ui-helper-clearfix";
                        if (m.indexOf(".") != -1) {
                            k = m.split(".");
                            h.setAttribute("id", k[0].substr(1, k[0].length - 1));
                            h.className = k[1]
                        } else if (m.charAt(0) == "#")h.setAttribute("id", m.substr(1, m.length - 1)); else h.className = m;
                        t += q
                    }
                    c.appendChild(h);
                    c = h
                } else if (i == ">")c = c.parentNode; else if (i == "l" && a.oFeatures.bPaginate && a.oFeatures.bLengthChange) {
                    f = ta(a);
                    e = 1
                } else if (i == "f" && a.oFeatures.bFilter) {
                    f = ua(a);
                    e = 1
                } else if (i == "r" && a.oFeatures.bProcessing) {
                    f = va(a);
                    e = 1
                } else if (i == "t") {
                    f = wa(a);
                    e = 1
                } else if (i == "i" && a.oFeatures.bInfo) {
                    f = xa(a);
                    e = 1
                } else if (i == "p" && a.oFeatures.bPaginate) {
                    f =
                        ya(a);
                    e = 1
                } else if (n.aoFeatures.length !== 0) {
                    h = n.aoFeatures;
                    q = 0;
                    for (k = h.length; q < k; q++)if (i == h[q].cFeature) {
                        if (f = h[q].fnInit(a))e = 1;
                        break
                    }
                }
                if (e == 1 && f !== null) {
                    if (typeof a.aanFeatures[i] != "object")a.aanFeatures[i] = [];
                    a.aanFeatures[i].push(f);
                    c.appendChild(f)
                }
            }
            b.parentNode.replaceChild(a.nTableWrapper, b)
        }

        function wa(a) {
            if (a.oScroll.sX === "" && a.oScroll.sY === "")return a.nTable;
            var b = p.createElement("div"), c = p.createElement("div"), d = p.createElement("div"), f = p.createElement("div"), e = p.createElement("div"),
                i = p.createElement("div"), h = a.nTable.cloneNode(false), k = a.nTable.cloneNode(false), m = a.nTable.getElementsByTagName("thead")[0], q = a.nTable.getElementsByTagName("tfoot").length === 0 ? null : a.nTable.getElementsByTagName("tfoot")[0], t = typeof g.bJQueryUI != "undefined" && g.bJQueryUI ? n.oJUIClasses : n.oStdClasses;
            c.appendChild(d);
            e.appendChild(i);
            f.appendChild(a.nTable);
            b.appendChild(c);
            b.appendChild(f);
            d.appendChild(h);
            h.appendChild(m);
            if (q !== null) {
                b.appendChild(e);
                i.appendChild(k);
                k.appendChild(q)
            }
            b.className =
                t.sScrollWrapper;
            c.className = t.sScrollHead;
            d.className = t.sScrollHeadInner;
            f.className = t.sScrollBody;
            e.className = t.sScrollFoot;
            i.className = t.sScrollFootInner;
            c.style.overflow = "hidden";
            c.style.position = "relative";
            e.style.overflow = "hidden";
            f.style.overflow = "auto";
            c.style.border = "0";
            e.style.border = "0";
            d.style.width = "150%";
            h.removeAttribute("id");
            h.style.marginLeft = "0";
            a.nTable.style.marginLeft = "0";
            if (q !== null) {
                k.removeAttribute("id");
                k.style.marginLeft = "0"
            }
            d = j(">caption", a.nTable);
            i = 0;
            for (k = d.length; i <
                k; i++)h.appendChild(d[i]);
            if (a.oScroll.sX !== "") {
                c.style.width = u(a.oScroll.sX);
                f.style.width = u(a.oScroll.sX);
                if (q !== null)e.style.width = u(a.oScroll.sX);
                j(f).scroll(function () {
                    c.scrollLeft = this.scrollLeft;
                    if (q !== null)e.scrollLeft = this.scrollLeft
                })
            }
            if (a.oScroll.sY !== "")f.style.height = u(a.oScroll.sY);
            a.aoDrawCallback.push({fn: za, sName: "scrolling"});
            a.oScroll.bInfinite && j(f).scroll(function () {
                if (!a.bDrawing)if (j(this).scrollTop() + j(this).height() > j(a.nTable).height() - a.oScroll.iLoadGap)if (a.fnDisplayEnd() <
                    a.fnRecordsDisplay()) {
                    ca(a, "next");
                    F(a);
                    C(a)
                }
            });
            a.nScrollHead = c;
            a.nScrollFoot = e;
            return b
        }

        function za(a) {
            var b = a.nScrollHead.getElementsByTagName("div")[0], c = b.getElementsByTagName("table")[0], d = a.nTable.parentNode, f, e, i, h, k, m, q, t, H = [];
            i = a.nTable.getElementsByTagName("thead");
            i.length > 0 && a.nTable.removeChild(i[0]);
            if (a.nTFoot !== null) {
                k = a.nTable.getElementsByTagName("tfoot");
                k.length > 0 && a.nTable.removeChild(k[0])
            }
            i = a.nTHead.cloneNode(true);
            a.nTable.insertBefore(i, a.nTable.childNodes[0]);
            if (a.nTFoot !==
                null) {
                k = a.nTFoot.cloneNode(true);
                a.nTable.insertBefore(k, a.nTable.childNodes[1])
            }
            var J = da(i);
            f = 0;
            for (e = J.length; f < e; f++) {
                q = ea(a, f);
                J[f].style.width = a.aoColumns[q].sWidth
            }
            a.nTFoot !== null && M(function (A) {
                A.style.width = ""
            }, k.getElementsByTagName("tr"));
            f = j(a.nTable).outerWidth();
            if (a.oScroll.sX === "") {
                a.nTable.style.width = "100%";
                if (j.browser.msie && j.browser.version <= 7)a.nTable.style.width = u(j(a.nTable).outerWidth() - a.oScroll.iBarWidth)
            } else if (a.oScroll.sXInner !== "")a.nTable.style.width = u(a.oScroll.sXInner);
            else if (f == j(d).width() && j(d).height() < j(a.nTable).height()) {
                a.nTable.style.width = u(f - a.oScroll.iBarWidth);
                if (j(a.nTable).outerWidth() > f - a.oScroll.iBarWidth)a.nTable.style.width = u(f)
            } else a.nTable.style.width = u(f);
            f = j(a.nTable).outerWidth();
            e = a.nTHead.getElementsByTagName("tr");
            i = i.getElementsByTagName("tr");
            M(function (A, G) {
                m = A.style;
                m.paddingTop = "0";
                m.paddingBottom = "0";
                m.borderTopWidth = "0";
                m.borderBottomWidth = "0";
                m.height = 0;
                t = j(A).width();
                G.style.width = u(t);
                H.push(t)
            }, i, e);
            j(i).height(0);
            if (a.nTFoot !==
                null) {
                h = k.getElementsByTagName("tr");
                k = a.nTFoot.getElementsByTagName("tr");
                M(function (A, G) {
                    m = A.style;
                    m.paddingTop = "0";
                    m.paddingBottom = "0";
                    m.borderTopWidth = "0";
                    m.borderBottomWidth = "0";
                    t = j(A).width();
                    G.style.width = u(t);
                    H.push(t)
                }, h, k);
                j(h).height(0)
            }
            M(function (A) {
                A.innerHTML = "";
                A.style.width = u(H.shift())
            }, i);
            a.nTFoot !== null && M(function (A) {
                A.innerHTML = "";
                A.style.width = u(H.shift())
            }, h);
            if (j(a.nTable).outerWidth() < f)if (a.oScroll.sX === "")I(a, 1, "The table cannot fit into the current element which will cause column misalignment. It is suggested that you enable x-scrolling or increase the width the table has in which to be drawn");
            else a.oScroll.sXInner !== "" && I(a, 1, "The table cannot fit into the current element which will cause column misalignment. It is suggested that you increase the sScrollXInner property to allow it to draw in a larger area, or simply remove that parameter to allow automatic calculation");
            if (a.oScroll.sY === "")if (j.browser.msie && j.browser.version <= 7)d.style.height = u(a.nTable.offsetHeight + a.oScroll.iBarWidth);
            if (a.oScroll.sY !== "" && a.oScroll.bCollapse) {
                d.style.height = u(a.oScroll.sY);
                h = a.oScroll.sX !== "" && a.nTable.offsetWidth >
                    d.offsetWidth ? a.oScroll.iBarWidth : 0;
                if (a.nTable.offsetHeight < d.offsetHeight)d.style.height = u(j(a.nTable).height() + h)
            }
            h = j(a.nTable).outerWidth();
            c.style.width = u(h);
            b.style.width = u(h + a.oScroll.iBarWidth);
            b.parentNode.style.width = u(j(d).width());
            if (a.nTFoot !== null) {
                b = a.nScrollFoot.getElementsByTagName("div")[0];
                c = b.getElementsByTagName("table")[0];
                b.style.width = u(a.nTable.offsetWidth + a.oScroll.iBarWidth);
                c.style.width = u(a.nTable.offsetWidth)
            }
            if (a.bSorted || a.bFiltered)d.scrollTop = 0
        }

        function V(a) {
            if (a.oFeatures.bAutoWidth ===
                false)return false;
            Y(a);
            for (var b = 0, c = a.aoColumns.length; b < c; b++)a.aoColumns[b].nTh.style.width = a.aoColumns[b].sWidth
        }

        function ua(a) {
            var b = p.createElement("div");
            a.sTableId !== "" && typeof a.aanFeatures.f == "undefined" && b.setAttribute("id", a.sTableId + "_filter");
            b.className = a.oClasses.sFilter;
            b.innerHTML = a.oLanguage.sSearch + (a.oLanguage.sSearch === "" ? "" : " ") + '<input type="text" />';
            var c = j("input", b);
            c.val(a.oPreviousSearch.sSearch.replace('"', "&quot;"));
            c.keyup(function () {
                for (var d = a.aanFeatures.f, f = 0,
                         e = d.length; f < e; f++)d[f] != this.parentNode && j("input", d[f]).val(this.value);
                this.value != a.oPreviousSearch.sSearch && P(a, {sSearch: this.value, bRegex: a.oPreviousSearch.bRegex, bSmart: a.oPreviousSearch.bSmart})
            });
            c.keypress(function (d) {
                if (d.keyCode == 13)return false
            });
            return b
        }

        function P(a, b, c) {
            Aa(a, b.sSearch, c, b.bRegex, b.bSmart);
            for (b = 0; b < a.aoPreSearchCols.length; b++)Ba(a, a.aoPreSearchCols[b].sSearch, b, a.aoPreSearchCols[b].bRegex, a.aoPreSearchCols[b].bSmart);
            n.afnFiltering.length !== 0 && Ca(a);
            a.bFiltered = true;
            a._iDisplayStart = 0;
            F(a);
            C(a);
            fa(a, 0)
        }

        function Ca(a) {
            for (var b = n.afnFiltering, c = 0, d = b.length; c < d; c++)for (var f = 0, e = 0, i = a.aiDisplay.length; e < i; e++) {
                var h = a.aiDisplay[e - f];
                if (!b[c](a, a.aoData[h]._aData, h)) {
                    a.aiDisplay.splice(e - f, 1);
                    f++
                }
            }
        }

        function Ba(a, b, c, d, f) {
            if (b !== "") {
                var e = 0;
                b = ga(b, d, f);
                for (d = a.aiDisplay.length - 1; d >= 0; d--) {
                    f = ha(a.aoData[a.aiDisplay[d]]._aData[c], a.aoColumns[c].sType);
                    if (!b.test(f)) {
                        a.aiDisplay.splice(d, 1);
                        e++
                    }
                }
            }
        }

        function Aa(a, b, c, d, f) {
            var e = ga(b, d, f);
            if (typeof c == "undefined" || c ===
                null)c = 0;
            if (n.afnFiltering.length !== 0)c = 1;
            if (b.length <= 0) {
                a.aiDisplay.splice(0, a.aiDisplay.length);
                a.aiDisplay = a.aiDisplayMaster.slice()
            } else if (a.aiDisplay.length == a.aiDisplayMaster.length || a.oPreviousSearch.sSearch.length > b.length || c == 1 || b.indexOf(a.oPreviousSearch.sSearch) !== 0) {
                a.aiDisplay.splice(0, a.aiDisplay.length);
                fa(a, 1);
                for (c = 0; c < a.aiDisplayMaster.length; c++)e.test(a.asDataSearch[c]) && a.aiDisplay.push(a.aiDisplayMaster[c])
            } else {
                var i = 0;
                for (c = 0; c < a.asDataSearch.length; c++)if (!e.test(a.asDataSearch[c])) {
                    a.aiDisplay.splice(c -
                        i, 1);
                    i++
                }
            }
            a.oPreviousSearch.sSearch = b;
            a.oPreviousSearch.bRegex = d;
            a.oPreviousSearch.bSmart = f
        }

        function fa(a, b) {
            a.asDataSearch.splice(0, a.asDataSearch.length);
            b = typeof b != "undefined" && b == 1 ? a.aiDisplayMaster : a.aiDisplay;
            for (var c = 0, d = b.length; c < d; c++)a.asDataSearch[c] = ia(a, a.aoData[b[c]]._aData)
        }

        function ia(a, b) {
            for (var c = "", d = p.createElement("div"), f = 0, e = a.aoColumns.length; f < e; f++)if (a.aoColumns[f].bSearchable)c += ha(b[f], a.aoColumns[f].sType) + "  ";
            if (c.indexOf("&") !== -1) {
                d.innerHTML = c;
                c = d.textContent ?
                    d.textContent : d.innerText;
                c = c.replace(/\n/g, " ").replace(/\r/g, "")
            }
            return c
        }

        function ga(a, b, c) {
            if (c) {
                a = b ? a.split(" ") : ja(a).split(" ");
                a = "^(?=.*?" + a.join(")(?=.*?") + ").*$";
                return new RegExp(a, "i")
            } else {
                a = b ? a : ja(a);
                return new RegExp(a, "i")
            }
        }

        function ha(a, b) {
            if (typeof n.ofnSearch[b] == "function")return n.ofnSearch[b](a); else if (b == "html")return a.replace(/\n/g, " ").replace(/<.*?>/g, ""); else if (typeof a == "string")return a.replace(/\n/g, " ");
            return a
        }

        function O(a, b) {
            var c = [], d = n.oSort, f = a.aoData, e, i, h,
                k;
            if (!a.oFeatures.bServerSide && (a.aaSorting.length !== 0 || a.aaSortingFixed !== null)) {
                c = a.aaSortingFixed !== null ? a.aaSortingFixed.concat(a.aaSorting) : a.aaSorting.slice();
                for (h = 0; h < c.length; h++) {
                    e = c[h][0];
                    i = N(a, e);
                    k = a.aoColumns[e].sSortDataType;
                    if (typeof n.afnSortData[k] != "undefined") {
                        var m = n.afnSortData[k](a, e, i);
                        i = 0;
                        for (k = f.length; i < k; i++)f[i]._aData[e] = m[i]
                    }
                }
                if (X.runtime) {
                    var q = [], t = c.length;
                    for (h = 0; h < t; h++) {
                        e = a.aoColumns[c[h][0]].iDataSort;
                        q.push([e, a.aoColumns[e].sType + "-" + c[h][1]])
                    }
                    a.aiDisplayMaster.sort(function (H, J) {
                        for (var A, G = 0; G < t; G++) {
                            A = d[q[G][1]](f[H]._aData[q[G][0]], f[J]._aData[q[G][0]]);
                            if (A !== 0)return A
                        }
                        return 0
                    })
                } else {
                    this.ClosureDataTables = {fn: function () {
                    }, data: f, sort: n.oSort, master: a.aiDisplayMaster.slice()};
                    k = "this.ClosureDataTables.fn = function(a,b){var iTest, oSort=this.ClosureDataTables.sort, aoData=this.ClosureDataTables.data, aiOrig=this.ClosureDataTables.master;";
                    for (h = 0; h < c.length - 1; h++) {
                        e = a.aoColumns[c[h][0]].iDataSort;
                        i = a.aoColumns[e].sType;
                        k += "iTest = oSort['" + i + "-" + c[h][1] + "']( aoData[a]._aData[" +
                            e + "], aoData[b]._aData[" + e + "] ); if ( iTest === 0 )"
                    }
                    if (c.length > 0) {
                        e = a.aoColumns[c[c.length - 1][0]].iDataSort;
                        i = a.aoColumns[e].sType;
                        k += "iTest = oSort['" + i + "-" + c[c.length - 1][1] + "']( aoData[a]._aData[" + e + "], aoData[b]._aData[" + e + "] );if (iTest===0) return oSort['numeric-asc'](jQuery.inArray(a,aiOrig), jQuery.inArray(b,aiOrig)); return iTest;}";
                        eval(k);
                        a.aiDisplayMaster.sort(this.ClosureDataTables.fn)
                    }
                    this.ClosureDataTables = undefined
                }
            }
            if (typeof b == "undefined" || b)T(a);
            a.bSorted = true;
            if (a.oFeatures.bFilter)P(a,
                a.oPreviousSearch, 1); else {
                a.aiDisplay = a.aiDisplayMaster.slice();
                a._iDisplayStart = 0;
                F(a);
                C(a)
            }
        }

        function $(a, b, c, d) {
            j(b).click(function (f) {
                if (a.aoColumns[c].bSortable !== false) {
                    var e = function () {
                        var i, h;
                        if (f.shiftKey) {
                            for (var k = false, m = 0; m < a.aaSorting.length; m++)if (a.aaSorting[m][0] == c) {
                                k = true;
                                i = a.aaSorting[m][0];
                                h = a.aaSorting[m][2] + 1;
                                if (typeof a.aoColumns[i].asSorting[h] == "undefined")a.aaSorting.splice(m, 1); else {
                                    a.aaSorting[m][1] = a.aoColumns[i].asSorting[h];
                                    a.aaSorting[m][2] = h
                                }
                                break
                            }
                            k === false && a.aaSorting.push([c,
                                a.aoColumns[c].asSorting[0], 0])
                        } else if (a.aaSorting.length == 1 && a.aaSorting[0][0] == c) {
                            i = a.aaSorting[0][0];
                            h = a.aaSorting[0][2] + 1;
                            if (typeof a.aoColumns[i].asSorting[h] == "undefined")h = 0;
                            a.aaSorting[0][1] = a.aoColumns[i].asSorting[h];
                            a.aaSorting[0][2] = h
                        } else {
                            a.aaSorting.splice(0, a.aaSorting.length);
                            a.aaSorting.push([c, a.aoColumns[c].asSorting[0], 0])
                        }
                        O(a)
                    };
                    if (a.oFeatures.bProcessing) {
                        K(a, true);
                        setTimeout(function () {
                            e();
                            a.oFeatures.bServerSide || K(a, false)
                        }, 0)
                    } else e();
                    typeof d == "function" && d(a)
                }
            })
        }

        function T(a) {
            var b,
                c, d, f, e, i = a.aoColumns.length, h = a.oClasses;
            for (b = 0; b < i; b++)a.aoColumns[b].bSortable && j(a.aoColumns[b].nTh).removeClass(h.sSortAsc + " " + h.sSortDesc + " " + a.aoColumns[b].sSortingClass);
            f = a.aaSortingFixed !== null ? a.aaSortingFixed.concat(a.aaSorting) : a.aaSorting.slice();
            for (b = 0; b < a.aoColumns.length; b++)if (a.aoColumns[b].bSortable) {
                e = a.aoColumns[b].sSortingClass;
                d = -1;
                for (c = 0; c < f.length; c++)if (f[c][0] == b) {
                    e = f[c][1] == "asc" ? h.sSortAsc : h.sSortDesc;
                    d = c;
                    break
                }
                j(a.aoColumns[b].nTh).addClass(e);
                if (a.bJUI) {
                    c = j("span",
                        a.aoColumns[b].nTh);
                    c.removeClass(h.sSortJUIAsc + " " + h.sSortJUIDesc + " " + h.sSortJUI + " " + h.sSortJUIAscAllowed + " " + h.sSortJUIDescAllowed);
                    c.addClass(d == -1 ? a.aoColumns[b].sSortingClassJUI : f[d][1] == "asc" ? h.sSortJUIAsc : h.sSortJUIDesc)
                }
            } else j(a.aoColumns[b].nTh).addClass(a.aoColumns[b].sSortingClass);
            e = h.sSortColumn;
            if (a.oFeatures.bSort && a.oFeatures.bSortClasses) {
                d = W(a);
                if (d.length >= i)for (b = 0; b < i; b++)if (d[b].className.indexOf(e + "1") != -1) {
                    c = 0;
                    for (a = d.length / i; c < a; c++)d[i * c + b].className = j.trim(d[i * c + b].className.replace(e +
                        "1", ""))
                } else if (d[b].className.indexOf(e + "2") != -1) {
                    c = 0;
                    for (a = d.length / i; c < a; c++)d[i * c + b].className = j.trim(d[i * c + b].className.replace(e + "2", ""))
                } else if (d[b].className.indexOf(e + "3") != -1) {
                    c = 0;
                    for (a = d.length / i; c < a; c++)d[i * c + b].className = j.trim(d[i * c + b].className.replace(" " + e + "3", ""))
                }
                h = 1;
                var k;
                for (b = 0; b < f.length; b++) {
                    k = parseInt(f[b][0], 10);
                    c = 0;
                    for (a = d.length / i; c < a; c++)d[i * c + k].className += " " + e + h;
                    h < 3 && h++
                }
            }
        }

        function ya(a) {
            if (a.oScroll.bInfinite)return null;
            var b = p.createElement("div");
            b.className =
                a.oClasses.sPaging + a.sPaginationType;
            n.oPagination[a.sPaginationType].fnInit(a, b, function (c) {
                F(c);
                C(c)
            });
            typeof a.aanFeatures.p == "undefined" && a.aoDrawCallback.push({fn: function (c) {
                n.oPagination[c.sPaginationType].fnUpdate(c, function (d) {
                    F(d);
                    C(d)
                })
            }, sName: "pagination"});
            return b
        }

        function ca(a, b) {
            var c = a._iDisplayStart;
            if (b == "first")a._iDisplayStart = 0; else if (b == "previous") {
                a._iDisplayStart = a._iDisplayLength >= 0 ? a._iDisplayStart - a._iDisplayLength : 0;
                if (a._iDisplayStart < 0)a._iDisplayStart = 0
            } else if (b ==
                "next")if (a._iDisplayLength >= 0) {
                if (a._iDisplayStart + a._iDisplayLength < a.fnRecordsDisplay())a._iDisplayStart += a._iDisplayLength
            } else a._iDisplayStart = 0; else if (b == "last")if (a._iDisplayLength >= 0) {
                b = parseInt((a.fnRecordsDisplay() - 1) / a._iDisplayLength, 10) + 1;
                a._iDisplayStart = (b - 1) * a._iDisplayLength
            } else a._iDisplayStart = 0; else I(a, 0, "Unknown paging action: " + b);
            return c != a._iDisplayStart
        }

        function xa(a) {
            var b = p.createElement("div");
            b.className = a.oClasses.sInfo;
            if (typeof a.aanFeatures.i == "undefined") {
                a.aoDrawCallback.push({fn: Da,
                    sName: "information"});
                a.sTableId !== "" && b.setAttribute("id", a.sTableId + "_info")
            }
            return b
        }

        function Da(a) {
            if (!(!a.oFeatures.bInfo || a.aanFeatures.i.length === 0)) {
                var b = a._iDisplayStart + 1, c = a.fnDisplayEnd(), d = a.fnRecordsTotal(), f = a.fnRecordsDisplay(), e = a.fnFormatNumber(b), i = a.fnFormatNumber(c), h = a.fnFormatNumber(d), k = a.fnFormatNumber(f);
                if (a.oScroll.bInfinite)e = a.fnFormatNumber(1);
                e = a.fnRecordsDisplay() === 0 && a.fnRecordsDisplay() == a.fnRecordsTotal() ? a.oLanguage.sInfoEmpty + a.oLanguage.sInfoPostFix : a.fnRecordsDisplay() ===
                    0 ? a.oLanguage.sInfoEmpty + " " + a.oLanguage.sInfoFiltered.replace("_MAX_", h) + a.oLanguage.sInfoPostFix : a.fnRecordsDisplay() == a.fnRecordsTotal() ? a.oLanguage.sInfo.replace("_START_", e).replace("_END_", i).replace("_TOTAL_", k) + a.oLanguage.sInfoPostFix : a.oLanguage.sInfo.replace("_START_", e).replace("_END_", i).replace("_TOTAL_", k) + " " + a.oLanguage.sInfoFiltered.replace("_MAX_", a.fnFormatNumber(a.fnRecordsTotal())) + a.oLanguage.sInfoPostFix;
                if (a.oLanguage.fnInfoCallback !== null)e = a.oLanguage.fnInfoCallback(a, b,
                    c, d, f, e);
                a = a.aanFeatures.i;
                b = 0;
                for (c = a.length; b < c; b++)j(a[b]).html(e)
            }
        }

        function ta(a) {
            if (a.oScroll.bInfinite)return null;
            var b = '<select size="1" ' + (a.sTableId === "" ? "" : 'name="' + a.sTableId + '_length"') + ">", c, d;
            if (a.aLengthMenu.length == 2 && typeof a.aLengthMenu[0] == "object" && typeof a.aLengthMenu[1] == "object") {
                c = 0;
                for (d = a.aLengthMenu[0].length; c < d; c++)b += '<option value="' + a.aLengthMenu[0][c] + '">' + a.aLengthMenu[1][c] + "</option>"
            } else {
                c = 0;
                for (d = a.aLengthMenu.length; c < d; c++)b += '<option value="' + a.aLengthMenu[c] +
                    '">' + a.aLengthMenu[c] + "</option>"
            }
            b += "</select>";
            var f = p.createElement("div");
            a.sTableId !== "" && typeof a.aanFeatures.l == "undefined" && f.setAttribute("id", a.sTableId + "_length");
            f.className = a.oClasses.sLength;
            f.innerHTML = a.oLanguage.sLengthMenu.replace("_MENU_", b);
            j('select option[value="' + a._iDisplayLength + '"]', f).attr("selected", true);
            j("select", f).change(function () {
                var e = j(this).val(), i = a.aanFeatures.l;
                c = 0;
                for (d = i.length; c < d; c++)i[c] != this.parentNode && j("select", i[c]).val(e);
                a._iDisplayLength = parseInt(e,
                    10);
                F(a);
                if (a.fnDisplayEnd() == a.fnRecordsDisplay()) {
                    a._iDisplayStart = a.fnDisplayEnd() - a._iDisplayLength;
                    if (a._iDisplayStart < 0)a._iDisplayStart = 0
                }
                if (a._iDisplayLength == -1)a._iDisplayStart = 0;
                C(a)
            });
            return f
        }

        function va(a) {
            var b = p.createElement("div");
            a.sTableId !== "" && typeof a.aanFeatures.r == "undefined" && b.setAttribute("id", a.sTableId + "_processing");
            b.innerHTML = a.oLanguage.sProcessing;
            b.className = a.oClasses.sProcessing;
            a.nTable.parentNode.insertBefore(b, a.nTable);
            return b
        }

        function K(a, b) {
            if (a.oFeatures.bProcessing) {
                a =
                    a.aanFeatures.r;
                for (var c = 0, d = a.length; c < d; c++)a[c].style.visibility = b ? "visible" : "hidden"
            }
        }

        function ea(a, b) {
            for (var c = -1, d = 0; d < a.aoColumns.length; d++) {
                a.aoColumns[d].bVisible === true && c++;
                if (c == b)return d
            }
            return null
        }

        function N(a, b) {
            for (var c = -1, d = 0; d < a.aoColumns.length; d++) {
                a.aoColumns[d].bVisible === true && c++;
                if (d == b)return a.aoColumns[d].bVisible === true ? c : null
            }
            return null
        }

        function Q(a, b) {
            var c, d;
            c = a._iDisplayStart;
            for (d = a._iDisplayEnd; c < d; c++)if (a.aoData[a.aiDisplay[c]].nTr == b)return a.aiDisplay[c];
            c = 0;
            for (d = a.aoData.length; c < d; c++)if (a.aoData[c].nTr == b)return c;
            return null
        }

        function S(a) {
            for (var b = 0, c = 0; c < a.aoColumns.length; c++)a.aoColumns[c].bVisible === true && b++;
            return b
        }

        function F(a) {
            a._iDisplayEnd = a.oFeatures.bPaginate === false ? a.aiDisplay.length : a._iDisplayStart + a._iDisplayLength > a.aiDisplay.length || a._iDisplayLength == -1 ? a.aiDisplay.length : a._iDisplayStart + a._iDisplayLength
        }

        function Ea(a, b) {
            if (!a || a === null || a === "")return 0;
            if (typeof b == "undefined")b = p.getElementsByTagName("body")[0];
            var c =
                p.createElement("div");
            c.style.width = a;
            b.appendChild(c);
            a = c.offsetWidth;
            b.removeChild(c);
            return a
        }

        function Y(a) {
            var b = 0, c, d = 0, f = a.aoColumns.length, e, i = j("th", a.nTHead);
            for (e = 0; e < f; e++)if (a.aoColumns[e].bVisible) {
                d++;
                if (a.aoColumns[e].sWidth !== null) {
                    c = Ea(a.aoColumns[e].sWidthOrig, a.nTable.parentNode);
                    if (c !== null)a.aoColumns[e].sWidth = u(c);
                    b++
                }
            }
            if (f == i.length && b === 0 && d == f && a.oScroll.sX === "" && a.oScroll.sY === "")for (e = 0; e < a.aoColumns.length; e++) {
                c = j(i[e]).width();
                if (c !== null)a.aoColumns[e].sWidth = u(c)
            } else {
                b =
                    a.nTable.cloneNode(false);
                e = p.createElement("tbody");
                c = p.createElement("tr");
                b.removeAttribute("id");
                b.appendChild(a.nTHead.cloneNode(true));
                if (a.nTFoot !== null) {
                    b.appendChild(a.nTFoot.cloneNode(true));
                    M(function (h) {
                        h.style.width = ""
                    }, b.getElementsByTagName("tr"))
                }
                b.appendChild(e);
                e.appendChild(c);
                e = j("thead th", b);
                if (e.length === 0)e = j("tbody tr:eq(0)>td", b);
                e.each(function (h) {
                    this.style.width = "";
                    h = ea(a, h);
                    if (h !== null && a.aoColumns[h].sWidthOrig !== "")this.style.width = a.aoColumns[h].sWidthOrig
                });
                for (e =
                         0; e < f; e++)if (a.aoColumns[e].bVisible) {
                    d = Fa(a, e);
                    if (d !== null) {
                        d = d.cloneNode(true);
                        c.appendChild(d)
                    }
                }
                e = a.nTable.parentNode;
                e.appendChild(b);
                if (a.oScroll.sX !== "" && a.oScroll.sXInner !== "")b.style.width = u(a.oScroll.sXInner); else if (a.oScroll.sX !== "") {
                    b.style.width = "";
                    if (j(b).width() < e.offsetWidth)b.style.width = u(e.offsetWidth)
                } else if (a.oScroll.sY !== "")b.style.width = u(e.offsetWidth);
                b.style.visibility = "hidden";
                Ga(a, b);
                f = j("tbody tr:eq(0)>td", b);
                if (f.length === 0)f = j("thead tr:eq(0)>th", b);
                for (e = c = 0; e < a.aoColumns.length; e++)if (a.aoColumns[e].bVisible) {
                    d =
                        j(f[c]).width();
                    if (d !== null && d > 0)a.aoColumns[e].sWidth = u(d);
                    c++
                }
                a.nTable.style.width = u(j(b).outerWidth());
                b.parentNode.removeChild(b)
            }
        }

        function Ga(a, b) {
            if (a.oScroll.sX === "" && a.oScroll.sY !== "") {
                j(b).width();
                b.style.width = u(j(b).outerWidth() - a.oScroll.iBarWidth)
            } else if (a.oScroll.sX !== "")b.style.width = u(j(b).outerWidth())
        }

        function Fa(a, b, c) {
            if (typeof c == "undefined" || c) {
                c = Ha(a, b);
                b = N(a, b);
                if (c < 0)return null;
                return a.aoData[c].nTr.getElementsByTagName("td")[b]
            }
            var d = -1, f, e;
            c = -1;
            var i = p.createElement("div");
            i.style.visibility = "hidden";
            i.style.position = "absolute";
            p.body.appendChild(i);
            f = 0;
            for (e = a.aoData.length; f < e; f++) {
                i.innerHTML = a.aoData[f]._aData[b];
                if (i.offsetWidth > d) {
                    d = i.offsetWidth;
                    c = f
                }
            }
            p.body.removeChild(i);
            if (c >= 0) {
                b = N(a, b);
                if (a = a.aoData[c].nTr.getElementsByTagName("td")[b])return a
            }
            return null
        }

        function Ha(a, b) {
            for (var c = -1, d = -1, f = 0; f < a.aoData.length; f++) {
                var e = a.aoData[f]._aData[b];
                if (e.length > c) {
                    c = e.length;
                    d = f
                }
            }
            return d
        }

        function u(a) {
            if (a === null)return"0px";
            if (typeof a == "number") {
                if (a < 0)return"0px";
                return a + "px"
            }
            var b = a.charCodeAt(a.length - 1);
            if (b < 48 || b > 57)return a;
            return a + "px"
        }

        function Ma(a, b) {
            if (a.length != b.length)return 1;
            for (var c = 0; c < a.length; c++)if (a[c] != b[c])return 2;
            return 0
        }

        function Z(a) {
            for (var b = n.aTypes, c = b.length, d = 0; d < c; d++) {
                var f = b[d](a);
                if (f !== null)return f
            }
            return"string"
        }

        function B(a) {
            for (var b = 0; b < E.length; b++)if (E[b].nTable == a)return E[b];
            return null
        }

        function U(a) {
            for (var b = [], c = a.aoData.length, d = 0; d < c; d++)b.push(a.aoData[d]._aData);
            return b
        }

        function R(a) {
            for (var b = [], c =
                a.aoData.length, d = 0; d < c; d++)b.push(a.aoData[d].nTr);
            return b
        }

        function W(a) {
            var b = R(a), c = [], d, f = [], e, i, h, k;
            e = 0;
            for (i = b.length; e < i; e++) {
                c = [];
                h = 0;
                for (k = b[e].childNodes.length; h < k; h++) {
                    d = b[e].childNodes[h];
                    d.nodeName.toUpperCase() == "TD" && c.push(d)
                }
                h = d = 0;
                for (k = a.aoColumns.length; h < k; h++)if (a.aoColumns[h].bVisible)f.push(c[h - d]); else {
                    f.push(a.aoData[e]._anHidden[h]);
                    d++
                }
            }
            return f
        }

        function ja(a) {
            return a.replace(new RegExp("(\\/|\\.|\\*|\\+|\\?|\\||\\(|\\)|\\[|\\]|\\{|\\}|\\\\|\\$|\\^)", "g"), "\\$1")
        }

        function ka(a, b) {
            for (var c = -1, d = 0, f = a.length; d < f; d++)if (a[d] == b)c = d; else a[d] > b && a[d]--;
            c != -1 && a.splice(c, 1)
        }

        function sa(a, b) {
            b = b.split(",");
            for (var c = [], d = 0, f = a.aoColumns.length; d < f; d++)for (var e = 0; e < f; e++)if (a.aoColumns[d].sName == b[e]) {
                c.push(e);
                break
            }
            return c
        }

        function aa(a) {
            for (var b = "", c = 0, d = a.aoColumns.length; c < d; c++)b += a.aoColumns[c].sName + ",";
            if (b.length == d)return"";
            return b.slice(0, -1)
        }

        function I(a, b, c) {
            a = a.sTableId === "" ? "DataTables warning: " + c : "DataTables warning (table id = '" + a.sTableId + "'): " + c;
            if (b ===
                0)if (n.sErrMode == "alert")alert(a); else throw a; else typeof console != "undefined" && typeof console.log != "undefined" && console.log(a)
        }

        function ba(a) {
            a.aoData.splice(0, a.aoData.length);
            a.aiDisplayMaster.splice(0, a.aiDisplayMaster.length);
            a.aiDisplay.splice(0, a.aiDisplay.length);
            F(a)
        }

        function la(a) {
            if (!(!a.oFeatures.bStateSave || typeof a.bDestroying != "undefined")) {
                var b, c, d, f = "{";
                f += '"iCreate":' + (new Date).getTime() + ",";
                f += '"iStart":' + a._iDisplayStart + ",";
                f += '"iEnd":' + a._iDisplayEnd + ",";
                f += '"iLength":' + a._iDisplayLength +
                    ",";
                f += '"sFilter":"' + encodeURIComponent(a.oPreviousSearch.sSearch) + '",';
                f += '"sFilterEsc":' + !a.oPreviousSearch.bRegex + ",";
                f += '"aaSorting":[ ';
                for (b = 0; b < a.aaSorting.length; b++)f += "[" + a.aaSorting[b][0] + ',"' + a.aaSorting[b][1] + '"],';
                f = f.substring(0, f.length - 1);
                f += "],";
                f += '"aaSearchCols":[ ';
                for (b = 0; b < a.aoPreSearchCols.length; b++)f += '["' + encodeURIComponent(a.aoPreSearchCols[b].sSearch) + '",' + !a.aoPreSearchCols[b].bRegex + "],";
                f = f.substring(0, f.length - 1);
                f += "],";
                f += '"abVisCols":[ ';
                for (b = 0; b < a.aoColumns.length; b++)f +=
                    a.aoColumns[b].bVisible + ",";
                f = f.substring(0, f.length - 1);
                f += "]";
                b = 0;
                for (c = a.aoStateSave.length; b < c; b++) {
                    d = a.aoStateSave[b].fn(a, f);
                    if (d !== "")f = d
                }
                f += "}";
                Ia(a.sCookiePrefix + a.sInstance, f, a.iCookieDuration, a.sCookiePrefix, a.fnCookieCallback)
            }
        }

        function Ja(a, b) {
            if (a.oFeatures.bStateSave) {
                var c, d, f;
                d = ma(a.sCookiePrefix + a.sInstance);
                if (d !== null && d !== "") {
                    try {
                        c = typeof j.parseJSON == "function" ? j.parseJSON(d.replace(/'/g, '"')) : eval("(" + d + ")")
                    } catch (e) {
                        return
                    }
                    d = 0;
                    for (f = a.aoStateLoad.length; d < f; d++)if (!a.aoStateLoad[d].fn(a,
                        c))return;
                    a.oLoadedState = j.extend(true, {}, c);
                    a._iDisplayStart = c.iStart;
                    a.iInitDisplayStart = c.iStart;
                    a._iDisplayEnd = c.iEnd;
                    a._iDisplayLength = c.iLength;
                    a.oPreviousSearch.sSearch = decodeURIComponent(c.sFilter);
                    a.aaSorting = c.aaSorting.slice();
                    a.saved_aaSorting = c.aaSorting.slice();
                    if (typeof c.sFilterEsc != "undefined")a.oPreviousSearch.bRegex = !c.sFilterEsc;
                    if (typeof c.aaSearchCols != "undefined")for (d = 0; d < c.aaSearchCols.length; d++)a.aoPreSearchCols[d] = {sSearch: decodeURIComponent(c.aaSearchCols[d][0]), bRegex: !c.aaSearchCols[d][1]};
                    if (typeof c.abVisCols != "undefined") {
                        b.saved_aoColumns = [];
                        for (d = 0; d < c.abVisCols.length; d++) {
                            b.saved_aoColumns[d] = {};
                            b.saved_aoColumns[d].bVisible = c.abVisCols[d]
                        }
                    }
                }
            }
        }

        function Ia(a, b, c, d, f) {
            var e = new Date;
            e.setTime(e.getTime() + c * 1E3);
            c = X.location.pathname.split("/");
            a = a + "_" + c.pop().replace(/[\/:]/g, "").toLowerCase();
            var i;
            if (f !== null) {
                i = typeof j.parseJSON == "function" ? j.parseJSON(b) : eval("(" + b + ")");
                b = f(a, i, e.toGMTString(), c.join("/") + "/")
            } else b = a + "=" + encodeURIComponent(b) + "; expires=" + e.toGMTString() +
                "; path=" + c.join("/") + "/";
            f = "";
            e = 9999999999999;
            if ((ma(a) !== null ? p.cookie.length : b.length + p.cookie.length) + 10 > 4096) {
                a = p.cookie.split(";");
                for (var h = 0, k = a.length; h < k; h++)if (a[h].indexOf(d) != -1) {
                    var m = a[h].split("=");
                    try {
                        i = eval("(" + decodeURIComponent(m[1]) + ")")
                    } catch (q) {
                        continue
                    }
                    if (typeof i.iCreate != "undefined" && i.iCreate < e) {
                        f = m[0];
                        e = i.iCreate
                    }
                }
                if (f !== "")p.cookie = f + "=; expires=Thu, 01-Jan-1970 00:00:01 GMT; path=" + c.join("/") + "/"
            }
            p.cookie = b
        }

        function ma(a) {
            var b = X.location.pathname.split("/");
            a = a + "_" +
                b[b.length - 1].replace(/[\/:]/g, "").toLowerCase() + "=";
            b = p.cookie.split(";");
            for (var c = 0; c < b.length; c++) {
                for (var d = b[c]; d.charAt(0) == " ";)d = d.substring(1, d.length);
                if (d.indexOf(a) === 0)return decodeURIComponent(d.substring(a.length, d.length))
            }
            return null
        }

        function da(a) {
            a = a.getElementsByTagName("tr");
            if (a.length == 1)return a[0].getElementsByTagName("th");
            var b = [], c = [], d, f, e, i, h, k, m = function (G, Na, na) {
                for (; typeof G[Na][na] != "undefined";)na++;
                return na
            }, q = function (G) {
                if (typeof b[G] == "undefined")b[G] = []
            };
            d = 0;
            for (i = a.length; d < i; d++) {
                q(d);
                var t = 0, H = [];
                f = 0;
                for (h = a[d].childNodes.length; f < h; f++)if (a[d].childNodes[f].nodeName.toUpperCase() == "TD" || a[d].childNodes[f].nodeName.toUpperCase() == "TH")H.push(a[d].childNodes[f]);
                f = 0;
                for (h = H.length; f < h; f++) {
                    var J = H[f].getAttribute("colspan") * 1, A = H[f].getAttribute("rowspan") * 1;
                    if (!J || J === 0 || J === 1) {
                        k = m(b, d, t);
                        b[d][k] = H[f].nodeName.toUpperCase() == "TD" ? 4 : H[f];
                        if (A || A === 0 || A === 1)for (e = 1; e < A; e++) {
                            q(d + e);
                            b[d + e][k] = 2
                        }
                        t++
                    } else {
                        k = m(b, d, t);
                        for (e = 0; e < J; e++)b[d][k + e] = 3;
                        t += J
                    }
                }
            }
            d =
                0;
            for (i = b.length; d < i; d++) {
                f = 0;
                for (h = b[d].length; f < h; f++)if (typeof b[d][f] == "object")c[f] = b[d][f]
            }
            return c
        }

        function Ka() {
            var a = p.createElement("p"), b = a.style;
            b.width = "100%";
            b.height = "200px";
            var c = p.createElement("div");
            b = c.style;
            b.position = "absolute";
            b.top = "0px";
            b.left = "0px";
            b.visibility = "hidden";
            b.width = "200px";
            b.height = "150px";
            b.overflow = "hidden";
            c.appendChild(a);
            p.body.appendChild(c);
            b = a.offsetWidth;
            c.style.overflow = "scroll";
            a = a.offsetWidth;
            if (b == a)a = c.clientWidth;
            p.body.removeChild(c);
            return b -
                a
        }

        function M(a, b, c) {
            for (var d = 0, f = b.length; d < f; d++)for (var e = 0, i = b[d].childNodes.length; e < i; e++)if (b[d].childNodes[e].nodeType == 1)typeof c != "undefined" ? a(b[d].childNodes[e], c[d].childNodes[e]) : a(b[d].childNodes[e])
        }

        function o(a, b, c, d) {
            if (typeof d == "undefined")d = c;
            if (typeof b[c] != "undefined")a[d] = b[c]
        }

        this.oApi = {};
        this.fnDraw = function (a) {
            var b = B(this[n.iApiIndex]);
            if (typeof a != "undefined" && a === false) {
                F(b);
                C(b)
            } else L(b)
        };
        this.fnFilter = function (a, b, c, d, f) {
            var e = B(this[n.iApiIndex]);
            if (e.oFeatures.bFilter) {
                if (typeof c ==
                    "undefined")c = false;
                if (typeof d == "undefined")d = true;
                if (typeof f == "undefined")f = true;
                if (typeof b == "undefined" || b === null) {
                    P(e, {sSearch: a, bRegex: c, bSmart: d}, 1);
                    if (f && typeof e.aanFeatures.f != "undefined") {
                        b = e.aanFeatures.f;
                        c = 0;
                        for (d = b.length; c < d; c++)j("input", b[c]).val(a)
                    }
                } else {
                    e.aoPreSearchCols[b].sSearch = a;
                    e.aoPreSearchCols[b].bRegex = c;
                    e.aoPreSearchCols[b].bSmart = d;
                    P(e, e.oPreviousSearch, 1)
                }
            }
        };
        this.fnSettings = function () {
            return B(this[n.iApiIndex])
        };
        this.fnVersionCheck = n.fnVersionCheck;
        this.fnSort = function (a) {
            var b =
                B(this[n.iApiIndex]);
            b.aaSorting = a;
            O(b)
        };
        this.fnSortListener = function (a, b, c) {
            $(B(this[n.iApiIndex]), a, b, c)
        };
        this.fnAddData = function (a, b) {
            if (a.length === 0)return[];
            var c = [], d, f = B(this[n.iApiIndex]);
            if (typeof a[0] == "object")for (var e = 0; e < a.length; e++) {
                d = w(f, a[e]);
                if (d == -1)return c;
                c.push(d)
            } else {
                d = w(f, a);
                if (d == -1)return c;
                c.push(d)
            }
            f.aiDisplay = f.aiDisplayMaster.slice();
            if (typeof b == "undefined" || b)L(f);
            return c
        };
        this.fnDeleteRow = function (a, b, c) {
            var d = B(this[n.iApiIndex]);
            a = typeof a == "object" ? Q(d, a) : a;
            var f = d.aoData.splice(a, 1), e = j.inArray(a, d.aiDisplay);
            d.asDataSearch.splice(e, 1);
            ka(d.aiDisplayMaster, a);
            ka(d.aiDisplay, a);
            typeof b == "function" && b.call(this, d, f);
            if (d._iDisplayStart >= d.aiDisplay.length) {
                d._iDisplayStart -= d._iDisplayLength;
                if (d._iDisplayStart < 0)d._iDisplayStart = 0
            }
            if (typeof c == "undefined" || c) {
                F(d);
                C(d)
            }
            return f
        };
        this.fnClearTable = function (a) {
            var b = B(this[n.iApiIndex]);
            ba(b);
            if (typeof a == "undefined" || a)C(b)
        };
        this.fnOpen = function (a, b, c) {
            var d = B(this[n.iApiIndex]);
            this.fnClose(a);
            var f =
                p.createElement("tr"), e = p.createElement("td");
            f.appendChild(e);
            e.className = c;
            e.colSpan = S(d);
            e.innerHTML = b;
            b = j("tr", d.nTBody);
            j.inArray(a, b) != -1 && j(f).insertAfter(a);
            d.aoOpenRows.push({nTr: f, nParent: a});
            return f
        };
        this.fnClose = function (a) {
            for (var b = B(this[n.iApiIndex]), c = 0; c < b.aoOpenRows.length; c++)if (b.aoOpenRows[c].nParent == a) {
                (a = b.aoOpenRows[c].nTr.parentNode) && a.removeChild(b.aoOpenRows[c].nTr);
                b.aoOpenRows.splice(c, 1);
                return 0
            }
            return 1
        };
        this.fnGetData = function (a) {
            var b = B(this[n.iApiIndex]);
            if (typeof a !=
                "undefined") {
                a = typeof a == "object" ? Q(b, a) : a;
                return b.aoData[a]._aData
            }
            return U(b)
        };
        this.fnGetNodes = function (a) {
            var b = B(this[n.iApiIndex]);
            if (typeof a != "undefined")return b.aoData[a].nTr;
            return R(b)
        };
        this.fnGetPosition = function (a) {
            var b = B(this[n.iApiIndex]);
            if (a.nodeName.toUpperCase() == "TR")return Q(b, a); else if (a.nodeName.toUpperCase() == "TD")for (var c = Q(b, a.parentNode), d = 0, f = 0; f < b.aoColumns.length; f++)if (b.aoColumns[f].bVisible) {
                if (b.aoData[c].nTr.getElementsByTagName("td")[f - d] == a)return[c, f - d, f]
            } else d++;
            return null
        };
        this.fnUpdate = function (a, b, c, d, f) {
            var e = B(this[n.iApiIndex]), i;
            b = typeof b == "object" ? Q(e, b) : b;
            if (typeof a != "object") {
                i = a;
                e.aoData[b]._aData[c] = i;
                if (e.aoColumns[c].fnRender !== null) {
                    i = e.aoColumns[c].fnRender({iDataRow: b, iDataColumn: c, aData: e.aoData[b]._aData, oSettings: e});
                    if (e.aoColumns[c].bUseRendered)e.aoData[b]._aData[c] = i
                }
                c = N(e, c);
                if (c !== null)e.aoData[b].nTr.getElementsByTagName("td")[c].innerHTML = i
            } else {
                if (a.length != e.aoColumns.length) {
                    I(e, 0, "An array passed to fnUpdate must have the same number of columns as the table in question - in this case " +
                        e.aoColumns.length);
                    return 1
                }
                for (var h = 0; h < a.length; h++) {
                    i = a[h];
                    e.aoData[b]._aData[h] = i;
                    if (e.aoColumns[h].fnRender !== null) {
                        i = e.aoColumns[h].fnRender({iDataRow: b, iDataColumn: h, aData: e.aoData[b]._aData, oSettings: e});
                        if (e.aoColumns[h].bUseRendered)e.aoData[b]._aData[h] = i
                    }
                    c = N(e, h);
                    if (c !== null)e.aoData[b].nTr.getElementsByTagName("td")[c].innerHTML = i
                }
            }
            a = j.inArray(b, e.aiDisplay);
            e.asDataSearch[a] = ia(e, e.aoData[b]._aData);
            if (typeof f == "undefined" || f)V(e);
            if (typeof d == "undefined" || d)L(e);
            return 0
        };
        this.fnSetColumnVis =
            function (a, b, c) {
                var d = B(this[n.iApiIndex]), f, e;
                e = d.aoColumns.length;
                var i, h;
                if (d.aoColumns[a].bVisible != b) {
                    i = j(">tr", d.nTHead)[0];
                    var k = j(">tr", d.nTFoot)[0], m = [], q = [];
                    for (f = 0; f < e; f++) {
                        m.push(d.aoColumns[f].nTh);
                        q.push(d.aoColumns[f].nTf)
                    }
                    if (b) {
                        for (f = b = 0; f < a; f++)d.aoColumns[f].bVisible && b++;
                        if (b >= S(d)) {
                            i.appendChild(m[a]);
                            k && k.appendChild(q[a]);
                            f = 0;
                            for (e = d.aoData.length; f < e; f++) {
                                i = d.aoData[f]._anHidden[a];
                                d.aoData[f].nTr.appendChild(i)
                            }
                        } else {
                            for (f = a; f < e; f++) {
                                h = N(d, f);
                                if (h !== null)break
                            }
                            i.insertBefore(m[a],
                                i.getElementsByTagName("th")[h]);
                            k && k.insertBefore(q[a], k.getElementsByTagName("th")[h]);
                            W(d);
                            f = 0;
                            for (e = d.aoData.length; f < e; f++) {
                                i = d.aoData[f]._anHidden[a];
                                d.aoData[f].nTr.insertBefore(i, j(">td:eq(" + h + ")", d.aoData[f].nTr)[0])
                            }
                        }
                        d.aoColumns[a].bVisible = true
                    } else {
                        i.removeChild(m[a]);
                        k && k.removeChild(q[a]);
                        h = W(d);
                        f = 0;
                        for (e = d.aoData.length; f < e; f++) {
                            i = h[f * d.aoColumns.length + a * 1];
                            d.aoData[f]._anHidden[a] = i;
                            i.parentNode.removeChild(i)
                        }
                        d.aoColumns[a].bVisible = false
                    }
                    f = 0;
                    for (e = d.aoOpenRows.length; f < e; f++)d.aoOpenRows[f].nTr.colSpan =
                        S(d);
                    if (typeof c == "undefined" || c) {
                        V(d);
                        C(d)
                    }
                    la(d)
                }
            };
        this.fnPageChange = function (a, b) {
            var c = B(this[n.iApiIndex]);
            ca(c, a);
            F(c);
            if (typeof b == "undefined" || b)C(c)
        };
        this.fnDestroy = function () {
            var a = B(this[n.iApiIndex]), b = a.nTableWrapper.parentNode, c = a.nTBody, d, f;
            a.bDestroying = true;
            d = 0;
            for (f = a.aoColumns.length; d < f; d++)a.aoColumns[d].bVisible === false && this.fnSetColumnVis(d, true);
            j("tbody>tr>td." + a.oClasses.sRowEmpty, a.nTable).parent().remove();
            if (a.nTable != a.nTHead.parentNode) {
                j(">thead", a.nTable).remove();
                a.nTable.appendChild(a.nTHead)
            }
            if (a.nTFoot && a.nTable != a.nTFoot.parentNode) {
                j(">tfoot", a.nTable).remove();
                a.nTable.appendChild(a.nTFoot)
            }
            a.nTable.parentNode.removeChild(a.nTable);
            j(a.nTableWrapper).remove();
            a.aaSorting = [];
            a.aaSortingFixed = [];
            T(a);
            j(R(a)).removeClass(a.asStripClasses.join(" "));
            if (a.bJUI) {
                j("th", a.nTHead).removeClass([n.oStdClasses.sSortable, n.oJUIClasses.sSortableAsc, n.oJUIClasses.sSortableDesc, n.oJUIClasses.sSortableNone].join(" "));
                j("th span", a.nTHead).remove()
            } else j("th", a.nTHead).removeClass([n.oStdClasses.sSortable,
                n.oStdClasses.sSortableAsc, n.oStdClasses.sSortableDesc, n.oStdClasses.sSortableNone].join(" "));
            b.appendChild(a.nTable);
            d = 0;
            for (f = a.aoData.length; d < f; d++)c.appendChild(a.aoData[d].nTr);
            a.nTable.style.width = u(a.sDestroyWidth);
            j(">tr:even", c).addClass(a.asDestoryStrips[0]);
            j(">tr:odd", c).addClass(a.asDestoryStrips[1]);
            d = 0;
            for (f = E.length; d < f; d++)E[d] == a && E.splice(d, 1)
        };
        this.fnAdjustColumnSizing = function (a) {
            V(B(this[n.iApiIndex]));
            if (typeof a == "undefined" || a)this.fnDraw(false)
        };
        for (var oa in n.oApi)if (oa)this[oa] =
            r(oa);
        this.oApi._fnExternApiFunc = r;
        this.oApi._fnInitalise = s;
        this.oApi._fnLanguageProcess = v;
        this.oApi._fnAddColumn = y;
        this.oApi._fnColumnOptions = D;
        this.oApi._fnAddData = w;
        this.oApi._fnGatherData = x;
        this.oApi._fnDrawHead = z;
        this.oApi._fnDraw = C;
        this.oApi._fnReDraw = L;
        this.oApi._fnAjaxUpdate = qa;
        this.oApi._fnAjaxUpdateDraw = ra;
        this.oApi._fnAddOptionsHtml = pa;
        this.oApi._fnFeatureHtmlTable = wa;
        this.oApi._fnScrollDraw = za;
        this.oApi._fnAjustColumnSizing = V;
        this.oApi._fnFeatureHtmlFilter = ua;
        this.oApi._fnFilterComplete =
            P;
        this.oApi._fnFilterCustom = Ca;
        this.oApi._fnFilterColumn = Ba;
        this.oApi._fnFilter = Aa;
        this.oApi._fnBuildSearchArray = fa;
        this.oApi._fnBuildSearchRow = ia;
        this.oApi._fnFilterCreateSearch = ga;
        this.oApi._fnDataToSearch = ha;
        this.oApi._fnSort = O;
        this.oApi._fnSortAttachListener = $;
        this.oApi._fnSortingClasses = T;
        this.oApi._fnFeatureHtmlPaginate = ya;
        this.oApi._fnPageChange = ca;
        this.oApi._fnFeatureHtmlInfo = xa;
        this.oApi._fnUpdateInfo = Da;
        this.oApi._fnFeatureHtmlLength = ta;
        this.oApi._fnFeatureHtmlProcessing = va;
        this.oApi._fnProcessingDisplay =
            K;
        this.oApi._fnVisibleToColumnIndex = ea;
        this.oApi._fnColumnIndexToVisible = N;
        this.oApi._fnNodeToDataIndex = Q;
        this.oApi._fnVisbleColumns = S;
        this.oApi._fnCalculateEnd = F;
        this.oApi._fnConvertToWidth = Ea;
        this.oApi._fnCalculateColumnWidths = Y;
        this.oApi._fnScrollingWidthAdjust = Ga;
        this.oApi._fnGetWidestNode = Fa;
        this.oApi._fnGetMaxLenString = Ha;
        this.oApi._fnStringToCss = u;
        this.oApi._fnArrayCmp = Ma;
        this.oApi._fnDetectType = Z;
        this.oApi._fnSettingsFromNode = B;
        this.oApi._fnGetDataMaster = U;
        this.oApi._fnGetTrNodes = R;
        this.oApi._fnGetTdNodes =
            W;
        this.oApi._fnEscapeRegex = ja;
        this.oApi._fnDeleteIndex = ka;
        this.oApi._fnReOrderIndex = sa;
        this.oApi._fnColumnOrdering = aa;
        this.oApi._fnLog = I;
        this.oApi._fnClearTable = ba;
        this.oApi._fnSaveState = la;
        this.oApi._fnLoadState = Ja;
        this.oApi._fnCreateCookie = Ia;
        this.oApi._fnReadCookie = ma;
        this.oApi._fnGetUniqueThs = da;
        this.oApi._fnScrollBarWidth = Ka;
        this.oApi._fnApplyToChildren = M;
        this.oApi._fnMap = o;
        var La = this;
        return this.each(function () {
            var a = 0, b, c, d, f;
            a = 0;
            for (b = E.length; a < b; a++) {
                if (E[a].nTable == this)if (typeof g == "undefined" ||
                    typeof g.bRetrieve != "undefined" && g.bRetrieve === true)return E[a].oInstance; else if (typeof g.bDestroy != "undefined" && g.bDestroy === true) {
                    E[a].oInstance.fnDestroy();
                    break
                } else {
                    I(E[a], 0, "Cannot reinitialise DataTable.\n\nTo retrieve the DataTables object for this table, please pass either no arguments to the dataTable() function, or set bRetrieve to true. Alternatively, to destory the old table and create a new one, set bDestroy to true (note that a lot of changes to the configuration can be made through the API which is usually much faster).");
                    return
                }
                if (E[a].sTableId !== "" && E[a].sTableId == this.getAttribute("id")) {
                    E.splice(a, 1);
                    break
                }
            }
            var e = new l;
            E.push(e);
            var i = false, h = false;
            a = this.getAttribute("id");
            if (a !== null) {
                e.sTableId = a;
                e.sInstance = a
            } else e.sInstance = n._oExternConfig.iNextUnique++;
            if (this.nodeName.toLowerCase() != "table")I(e, 0, "Attempted to initialise DataTables on a node which is not a table: " + this.nodeName); else {
                e.oInstance = La;
                e.nTable = this;
                e.oApi = La.oApi;
                e.sDestroyWidth = j(this).width();
                if (typeof g != "undefined" && g !== null) {
                    e.oInit =
                        g;
                    o(e.oFeatures, g, "bPaginate");
                    o(e.oFeatures, g, "bLengthChange");
                    o(e.oFeatures, g, "bFilter");
                    o(e.oFeatures, g, "bSort");
                    o(e.oFeatures, g, "bInfo");
                    o(e.oFeatures, g, "bProcessing");
                    o(e.oFeatures, g, "bAutoWidth");
                    o(e.oFeatures, g, "bSortClasses");
                    o(e.oFeatures, g, "bServerSide");
                    o(e.oScroll, g, "sScrollX", "sX");
                    o(e.oScroll, g, "sScrollXInner", "sXInner");
                    o(e.oScroll, g, "sScrollY", "sY");
                    o(e.oScroll, g, "bScrollCollapse", "bCollapse");
                    o(e.oScroll, g, "bScrollInfinite", "bInfinite");
                    o(e.oScroll, g, "iScrollLoadGap", "iLoadGap");
                    o(e, g, "asStripClasses");
                    o(e, g, "fnRowCallback");
                    o(e, g, "fnHeaderCallback");
                    o(e, g, "fnFooterCallback");
                    o(e, g, "fnCookieCallback");
                    o(e, g, "fnInitComplete");
                    o(e, g, "fnServerData");
                    o(e, g, "fnFormatNumber");
                    o(e, g, "aaSorting");
                    o(e, g, "aaSortingFixed");
                    o(e, g, "aLengthMenu");
                    o(e, g, "sPaginationType");
                    o(e, g, "sAjaxSource");
                    o(e, g, "iCookieDuration");
                    o(e, g, "sCookiePrefix");
                    o(e, g, "sDom");
                    o(e, g, "oSearch", "oPreviousSearch");
                    o(e, g, "aoSearchCols", "aoPreSearchCols");
                    o(e, g, "iDisplayLength", "_iDisplayLength");
                    o(e, g, "bJQueryUI",
                        "bJUI");
                    o(e.oLanguage, g, "fnInfoCallback");
                    typeof g.fnDrawCallback == "function" && e.aoDrawCallback.push({fn: g.fnDrawCallback, sName: "user"});
                    typeof g.fnStateSaveCallback == "function" && e.aoStateSave.push({fn: g.fnStateSaveCallback, sName: "user"});
                    typeof g.fnStateLoadCallback == "function" && e.aoStateLoad.push({fn: g.fnStateLoadCallback, sName: "user"});
                    e.oFeatures.bServerSide && e.oFeatures.bSort && e.oFeatures.bSortClasses && e.aoDrawCallback.push({fn: T, sName: "server_side_sort_classes"});
                    if (typeof g.bJQueryUI != "undefined" &&
                        g.bJQueryUI) {
                        e.oClasses = n.oJUIClasses;
                        if (typeof g.sDom == "undefined")e.sDom = '<"H"lfr>t<"F"ip>'
                    }
                    if (e.oScroll.sX !== "" || e.oScroll.sY !== "")e.oScroll.iBarWidth = Ka();
                    if (typeof g.iDisplayStart != "undefined" && typeof e.iInitDisplayStart == "undefined") {
                        e.iInitDisplayStart = g.iDisplayStart;
                        e._iDisplayStart = g.iDisplayStart
                    }
                    if (typeof g.bStateSave != "undefined") {
                        e.oFeatures.bStateSave = g.bStateSave;
                        Ja(e, g);
                        e.aoDrawCallback.push({fn: la, sName: "state_save"})
                    }
                    if (typeof g.aaData != "undefined")h = true;
                    if (typeof g != "undefined" &&
                        typeof g.aoData != "undefined")g.aoColumns = g.aoData;
                    if (typeof g.oLanguage != "undefined")if (typeof g.oLanguage.sUrl != "undefined" && g.oLanguage.sUrl !== "") {
                        e.oLanguage.sUrl = g.oLanguage.sUrl;
                        j.getJSON(e.oLanguage.sUrl, null, function (q) {
                            v(e, q, true)
                        });
                        i = true
                    } else v(e, g.oLanguage, false)
                } else g = {};
                if (typeof g.asStripClasses == "undefined") {
                    e.asStripClasses.push(e.oClasses.sStripOdd);
                    e.asStripClasses.push(e.oClasses.sStripEven)
                }
                c = false;
                d = j("tbody>tr", this);
                a = 0;
                for (b = e.asStripClasses.length; a < b; a++)if (d.filter(":lt(2)").hasClass(e.asStripClasses[a])) {
                    c =
                        true;
                    break
                }
                if (c) {
                    e.asDestoryStrips = ["", ""];
                    if (j(d[0]).hasClass(e.oClasses.sStripOdd))e.asDestoryStrips[0] += e.oClasses.sStripOdd + " ";
                    if (j(d[0]).hasClass(e.oClasses.sStripEven))e.asDestoryStrips[0] += e.oClasses.sStripEven;
                    if (j(d[1]).hasClass(e.oClasses.sStripOdd))e.asDestoryStrips[1] += e.oClasses.sStripOdd + " ";
                    if (j(d[1]).hasClass(e.oClasses.sStripEven))e.asDestoryStrips[1] += e.oClasses.sStripEven;
                    d.removeClass(e.asStripClasses.join(" "))
                }
                a = this.getElementsByTagName("thead");
                c = a.length === 0 ? [] : da(a[0]);
                var k;
                if (typeof g.aoColumns == "undefined") {
                    k = [];
                    a = 0;
                    for (b = c.length; a < b; a++)k.push(null)
                } else k = g.aoColumns;
                a = 0;
                for (b = k.length; a < b; a++) {
                    if (typeof g.saved_aoColumns != "undefined" && g.saved_aoColumns.length == b) {
                        if (k[a] === null)k[a] = {};
                        k[a].bVisible = g.saved_aoColumns[a].bVisible
                    }
                    y(e, c ? c[a] : null)
                }
                if (typeof g.aoColumnDefs != "undefined")for (a = g.aoColumnDefs.length - 1; a >= 0; a--) {
                    var m = g.aoColumnDefs[a].aTargets;
                    j.isArray(m) || I(e, 1, "aTargets must be an array of targets, not a " + typeof m);
                    c = 0;
                    for (d = m.length; c < d; c++)if (typeof m[c] ==
                        "number" && m[c] >= 0) {
                        for (; e.aoColumns.length <= m[c];)y(e);
                        D(e, m[c], g.aoColumnDefs[a])
                    } else if (typeof m[c] == "number" && m[c] < 0)D(e, e.aoColumns.length + m[c], g.aoColumnDefs[a]); else if (typeof m[c] == "string") {
                        b = 0;
                        for (f = e.aoColumns.length; b < f; b++)if (m[c] == "_all" || e.aoColumns[b].nTh.className.indexOf(m[c]) != -1)D(e, b, g.aoColumnDefs[a])
                    }
                }
                if (typeof k != "undefined") {
                    a = 0;
                    for (b = k.length; a < b; a++)D(e, a, k[a])
                }
                a = 0;
                for (b = e.aaSorting.length; a < b; a++) {
                    if (e.aaSorting[a][0] >= e.aoColumns.length)e.aaSorting[a][0] = 0;
                    k = e.aoColumns[e.aaSorting[a][0]];
                    if (typeof e.aaSorting[a][2] == "undefined")e.aaSorting[a][2] = 0;
                    if (typeof g.aaSorting == "undefined" && typeof e.saved_aaSorting == "undefined")e.aaSorting[a][1] = k.asSorting[0];
                    c = 0;
                    for (d = k.asSorting.length; c < d; c++)if (e.aaSorting[a][1] == k.asSorting[c]) {
                        e.aaSorting[a][2] = c;
                        break
                    }
                }
                T(e);
                this.getElementsByTagName("thead").length === 0 && this.appendChild(p.createElement("thead"));
                this.getElementsByTagName("tbody").length === 0 && this.appendChild(p.createElement("tbody"));
                e.nTHead = this.getElementsByTagName("thead")[0];
                e.nTBody = this.getElementsByTagName("tbody")[0];
                if (this.getElementsByTagName("tfoot").length > 0)e.nTFoot = this.getElementsByTagName("tfoot")[0];
                if (h)for (a = 0; a < g.aaData.length; a++)w(e, g.aaData[a]); else x(e);
                e.aiDisplay = e.aiDisplayMaster.slice();
                e.bInitialised = true;
                i === false && s(e)
            }
        })
    }
})(jQuery, window, document);

 /* ************************ new file ************************ */
/*
 Base.js, version 1.1a
 Copyright 2006-2010, Dean Edwards
 License: http://www.opensource.org/licenses/mit-license.php
 */

var Base = function () {
    // dummy
};

Base.extend = function (_instance, _static) { // subclass
    var extend = Base.prototype.extend;

    // build the prototype
    Base._prototyping = true;
    var proto = new this;
    extend.call(proto, _instance);
    proto.base = function () {
        // call this method from any other method to invoke that method's ancestor
    };
    delete Base._prototyping;

    // create the wrapper for the constructor function
    //var constructor = proto.constructor.valueOf(); //-dean
    var constructor = proto.constructor;
    var klass = proto.constructor = function () {
        if (!Base._prototyping) {
            if (this._constructing || this.constructor == klass) { // instantiation
                this._constructing = true;
                constructor.apply(this, arguments);
                delete this._constructing;
            } else if (arguments[0] != null) { // casting
                return (arguments[0].extend || extend).call(arguments[0], proto);
            }
        }
    };

    // build the class interface
    klass.ancestor = this;
    klass.extend = this.extend;
    klass.forEach = this.forEach;
    klass.implement = this.implement;
    klass.prototype = proto;
    klass.toString = this.toString;
    klass.valueOf = function (type) {
        //return (type == "object") ? klass : constructor; //-dean
        return (type == "object") ? klass : constructor.valueOf();
    };
    extend.call(klass, _static);
    // class initialisation
    if (typeof klass.init == "function") klass.init();
    return klass;
};

Base.prototype = {
    extend: function (source, value) {
        if (arguments.length > 1) { // extending with a name/value pair
            var ancestor = this[source];
            if (ancestor && (typeof value == "function") && // overriding a method?
                // the valueOf() comparison is to avoid circular references
                (!ancestor.valueOf || ancestor.valueOf() != value.valueOf()) &&
                /\bbase\b/.test(value)) {
                // get the underlying method
                var method = value.valueOf();
                // override
                value = function () {
                    var previous = this.base || Base.prototype.base;
                    this.base = ancestor;
                    var returnValue = method.apply(this, arguments);
                    this.base = previous;
                    return returnValue;
                };
                // point to the underlying method
                value.valueOf = function (type) {
                    return (type == "object") ? value : method;
                };
                value.toString = Base.toString;
            }
            this[source] = value;
        } else if (source) { // extending with an object literal
            var extend = Base.prototype.extend;
            // if this object has a customised extend method then use it
            if (!Base._prototyping && typeof this != "function") {
                extend = this.extend || extend;
            }
            var proto = {
                toSource: null
            };
            // do the "toString" and other methods manually
            var hidden = ["constructor", "toString", "valueOf"];
            // if we are prototyping then include the constructor
            var i = Base._prototyping ? 0 : 1;
            while (key = hidden[i++]) {
                if (source[key] != proto[key]) {
                    extend.call(this, key, source[key]);

                }
            }
            // copy each of the source object's properties to this object
            for (var key in source) {
                if (!proto[key]) extend.call(this, key, source[key]);
            }
        }
        return this;
    }
};

// initialise
Base = Base.extend({
    constructor: function () {
        this.extend(arguments[0]);
    }
}, {
    ancestor: Object,
    version: "1.1",

    forEach: function (object, block, context) {
        for (var key in object) {
            if (this.prototype[key] === undefined) {
                block.call(context, object[key], key, object);
            }
        }
    },

    implement: function () {
        for (var i = 0; i < arguments.length; i++) {
            if (typeof arguments[i] == "function") {
                // if it's a function, call it
                arguments[i](this.prototype);
            } else {
                // add the interface using the extend method
                this.prototype.extend(arguments[i]);
            }
        }
        return this;
    },

    toString: function () {
        return String(this.valueOf());
    }
});

 /* ************************ new file ************************ */
// TODO - check if we use all this functions!

/**
 *
 * UTF-8 data encode / decode
 * http://www.webtoolkit.info/
 *
 **/
var Utf8 = {
    // public method for url encoding
    encode: function (string) {
        string = string.replace(/\r\n/g, "\n");
        var utftext = "";

        for (var n = 0; n < string.length; n++) {

            var c = string.charCodeAt(n);

            if (c < 128) {
                utftext += String.fromCharCode(c);
            } else if ((c > 127) && (c < 2048)) {
                utftext += String.fromCharCode((c >> 6) | 192);
                utftext += String.fromCharCode((c & 63) | 128);
            } else {
                utftext += String.fromCharCode((c >> 12) | 224);
                utftext += String.fromCharCode(((c >> 6) & 63) | 128);
                utftext += String.fromCharCode((c & 63) | 128);
            }

        }

        return utftext;
    },

    // public method for url decoding
    decode: function (utftext) {
        var string = "";
        var i = 0;
        var c = 0,
            c2 = 0,
            c3 = 0;

        while (i < utftext.length) {

            c = utftext.charCodeAt(i);

            if (c < 128) {
                string += String.fromCharCode(c);
                i++;
            } else if ((c > 191) && (c < 224)) {
                c2 = utftext.charCodeAt(i + 1);
                string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
                i += 2;
            } else {
                c2 = utftext.charCodeAt(i + 1);
                c3 = utftext.charCodeAt(i + 2);
                string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
                i += 3;
            }
        }

        return string;
    }
};

var encode_prepare = function (s) {
    if (s !== null) {
        s = s.replace(/\+/g, " ");
        if ($.browser === "msie" || $.browser === "opera") {
            return Utf8.decode(s);
        }
    }
    return s;
};

var encode_prepare_arr = function (value) {
    if (typeof value === "number") {
        return value;
    } else if ($.isArray(value)) {
        var a = new Array(value.length);
        $.each(value, function (i, val) {
            a[i] = encode_prepare(val);
        });
        return a;
    } else {
        return encode_prepare(value);
    }
};

var getURLParameters = function (sURL) {
    var arrParam = [];

    if (sURL.indexOf("?") > 0) {
        var arrParams = sURL.split("?");
        var arrURLParams = arrParams[1].split("&");

        for (var i = 0; i < arrURLParams.length; i++) {
            var sParam = arrURLParams[i].split("=");

            if (sParam[0].indexOf("param", 0) === 0) {
                var parameter = [sParam[0].substring(5, sParam[0].length), unescape(sParam[1])];
                arrParam.push(parameter);
            }
        }
    }

    return arrParam;
};

var toFormatedString = function (value) {
    value += '';
    var x = value.split('.');
    var x1 = x[0];
    var x2 = x.length > 1 ? '.' + x[1] : '';
    var rgx = /(\d+)(\d{3})/;
    while (rgx.test(x1)) {
        x1 = x1.replace(rgx, '$1' + ',' + '$2');
    }

    return x1 + x2;
};

//quote csv values in a way compatible with CSVTokenizer
var doCsvQuoting = function (value, separator, alwaysEscape) {
    var QUOTE_CHAR = '"';
    if (separator === null) {
        return value;
    }
    if (value === null) {
        return null;
    }
    if (value.indexOf(QUOTE_CHAR) >= 0) {
        //double them
        value = value.replace(QUOTE_CHAR, QUOTE_CHAR.concat(QUOTE_CHAR));
    }
    if (alwaysEscape || value.indexOf(separator) >= 0) {
        //quote value
        value = QUOTE_CHAR.concat(value, QUOTE_CHAR);
    }
    return value;
};

/*==================================================
 *  String Utility Functions and Constants
 *==================================================
 */

String.prototype.trim = function () {
    return this.replace(/^\s+|\s+$/g, '');
};

String.prototype.startsWith = function (prefix) {
    return this.length >= prefix.length && this.substr(0, prefix.length) === prefix;
};

String.prototype.endsWith = function (suffix) {
    return this.length >= suffix.length && this.substr(this.length - suffix.length) === suffix;
};

String.substitute = function (s, objects) {
    var result = "";
    var start = 0;
    while (start < s.length - 1) {
        var percent = s.indexOf("%", start);
        if (percent < 0 || percent === s.length - 1) {
            break;
        } else if (percent > start && s.charAt(percent - 1) === "\\") {
            result += s.substring(start, percent - 1) + "%";
            start = percent + 1;
        } else {
            var n = parseInt(s.charAt(percent + 1));
            if (isNaN(n) || n >= objects.length) {
                result += s.substring(start, percent + 2);
            } else {
                result += s.substring(start, percent) + objects[n].toString();
            }
            start = percent + 2;
        }
    }

    if (start < s.length) {
        result += s.substring(start);
    }
    return result;
};

/**
 *  Javascript sprintf
 *  http://www.webtoolkit.info/
 **/
var sprintfWrapper = {
    init: function () {

        if (typeof arguments === 'undefined') {
            return null;
        }
        if (arguments.length < 1) {
            return null;
        }
        if (typeof arguments[0] !== 'string') {
            return null;
        }
        if (typeof RegExp === 'undefined') {
            return null;
        }

        var string = arguments[0];
        var exp = new RegExp(/(%([%]|(\-)?(\+|\x20)?(0)?(\d+)?(\.(\d)?)?([bcdfosxX])))/g);
        var matches = [];
        var strings = [];
        var convCount = 0;
        var stringPosStart = 0;
        var stringPosEnd = 0;
        var matchPosEnd = 0;
        var newString = '';
        var match = null;

        while ((match = exp.exec(string))) {
            if (match[9]) {
                convCount += 1;
            }

            stringPosStart = matchPosEnd;
            stringPosEnd = exp.lastIndex - match[0].length;
            strings[strings.length] = string.substring(stringPosStart, stringPosEnd);

            matchPosEnd = exp.lastIndex;

            var negative = parseInt(arguments[convCount]) < 0;
            if (!negative) {
                negative = parseFloat(arguments[convCount]) < 0;
            }

            matches[matches.length] = {
                match: match[0],
                left: match[3] ? true : false,
                sign: match[4] || '',
                pad: match[5] || ' ',
                min: match[6] || 0,
                precision: match[8],
                code: match[9] || '%',
                negative: negative,
                argument: String(arguments[convCount])
            };
        }
        strings[strings.length] = string.substring(matchPosEnd);

        if (matches.length === 0) {
            return string;
        }
        if ((arguments.length - 1) < convCount) {
            return null;
        }

        match = null;
        var i = null;

        for (i = 0; i < matches.length; i++) {
            var m = matches[i];
            var substitution;
            if (m.code === '%') {
                substitution = '%';
            } else if (m.code === 'b') {
                m.argument = String(Math.abs(parseInt(m.argument)).toString(2));
                substitution = sprintfWrapper.convert(m, true);
            } else if (m.code === 'c') {
                m.argument = String(String.fromCharCode(parseInt(Math.abs(parseInt(m.argument)))));
                substitution = sprintfWrapper.convert(m, true);
            } else if (m.code === 'd') {
                m.argument = toFormatedString(String(Math.abs(parseInt(m.argument))));
                substitution = sprintfWrapper.convert(m);
            } else if (m.code === 'f') {
                m.argument = toFormatedString(String(Math.abs(parseFloat(m.argument)).toFixed(m.precision ? m.precision : 6)));
                substitution = sprintfWrapper.convert(m);
            } else if (m.code === 'o') {
                m.argument = String(Math.abs(parseInt(m.argument)).toString(8));
                substitution = sprintfWrapper.convert(m);
            } else if (m.code === 's') {
                m.argument = m.argument.substring(0, m.precision ? m.precision : m.argument.length);
                substitution = sprintfWrapper.convert(m, true);
            } else if (m.code === 'x') {
                m.argument = String(Math.abs(parseInt(m.argument)).toString(16));
                substitution = sprintfWrapper.convert(m);
            } else if (m.code === 'X') {
                m.argument = String(Math.abs(parseInt(m.argument)).toString(16));
                substitution = sprintfWrapper.convert(m).toUpperCase();
            } else {
                substitution = m.match;
            }

            newString += strings[i];
            newString += substitution;
        }

        newString += strings[i];

        return newString;
    },

    convert: function (match, nosign) {
        if (nosign) {
            match.sign = '';
        } else {
            match.sign = match.negative ? '-' : match.sign;
        }
        var l = match.min - match.argument.length + 1 - match.sign.length;
        var pad = new Array(l < 0 ? 0 : l).join(match.pad);
        if (!match.left) {
            if (match.pad === '0' || nosign) {
                return match.sign + pad + match.argument;
            } else {
                return pad + match.sign + match.argument;
            }
        } else {
            if (match.pad === '0' || nosign) {
                return match.sign + match.argument + pad.replace(/0/g, ' ');
            } else {
                return match.sign + match.argument + pad;
            }
        }
    }
};

var sprintf = sprintfWrapper.init;

/*
 * UTILITY STUFF
 */

(function () {
    function accessorDescriptor(field, fun) {
        var desc = {
            enumerable: true,
            configurable: true
        };
        desc[field] = fun;
        return desc;
    }

    this.defineGetter = function defineGetter(obj, prop, get) {
        if (Object.prototype.__defineGetter__) {
            return obj.__defineGetter__(prop, get);
        }

        if (Object.defineProperty) {
            return Object.defineProperty(obj, prop, accessorDescriptor("get", get));
        }

        throw new Error("browser does not support getters");
    };

    this.defineSetter = function defineSetter(obj, prop, set) {
        if (Object.prototype.__defineSetter__) {
            return obj.__defineSetter__(prop, set);
        }
        if (Object.defineProperty) {
            return Object.defineProperty(obj, prop, accessorDescriptor("set", set));
        }

        throw new Error("browser does not support setters");
    };
})();

 /* ************************ new file ************************ */
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
    i18nSupport: null // Reference to i18n objects
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

Dashboards.showProgressIndicator = function () {
    this.blockUIwithDrag();
};

Dashboards.hideProgressIndicator = function () {
    $('.loading-bar').removeClass('is-active');
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

            var timeInfo = Mustache.render("Timing: {{elapsedSinceStartDesc}} since start, {{elapsedSinceStartDesc}} since last event", this.splitTimer());
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
    $('.loading-bar').addClass('is-active');
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
    if (this.globalContext) {
        return eval(name);
    } else {
        return this.getComponent(name);
    }
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

 /* ************************ new file ************************ */
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
                    if (_typesTable.hasOwnProperty(type)) {
                        var holdersByName = _typesTable[type];
                        for (var name in holdersByName) {
                            if (holdersByName.hasOwnProperty(name)) {
                                holdersByName[name].dispose();
                            }
                        }
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
                if (holdersByName.hasOwnProperty(name)) {
                    instances.push(holdersByName[name].build({}, false));
                }
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

        this.build = function ( /*config, buildNew*/ ) {
            return instance;
        };

        // external scope is managed outside the container
        this.dispose = function () {
            if (instance) {
                scope = 'singleton' && doDispose(instance);
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
        if (key.indexOf('Component', key.length - 'Component'.length) !== -1) {
            key = key.substring(0, key.length - 'Component'.length);
        }

        key = key.charAt(0).toUpperCase() + key.substring(1);

        if (subKey) {
            key += "." + subKey;
        }

        return key;
    };

    D.registerAddIn = function (type, subType, addIn) {
        var _type = this.normalizeAddInKey(type, subType),
            name = addIn.getName ? addIn.getName() : null;
        this.addIns.register(_type, name, addIn);
    };

    D.hasAddIn = function (type, subType, addInName) {
        var _type = this.normalizeAddInKey(type, subType);
        return Boolean(this.addIns && this.addIns.has(_type, addInName));
    };

    D.getAddIn = function (type, subType, addInName) {
        var _type = this.normalizeAddInKey(type, subType);
        try {
            var addIn = this.addIns.get(_type, addInName);
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
        var _type = this.normalizeAddInKey(type, subType);
        var addInList = [];
        try {
            return this.addIns.listType(_type);
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
        var deepProperties = {};

        // Goes a level deeper one extending these properties. Usefull to preserve defaults and
        // options interfaces from BaseQuery.
        if (!_.isFunction(query) && _.isObject(query)) {
            _.each(BaseQuery.prototype.deepProperties, function (prop) {
                deepProperties[prop] = _.extend({}, BaseQuery.prototype[prop], query[prop]);
            });
        }

        var QueryClass = (_.isFunction(query) && query) ||
            (_.isObject(query) && BaseQuery.extend(_.extend({}, query, deepProperties)));

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

 /* ************************ new file ************************ */
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
                    return true;
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
                var ifaces = (interfaces && interfaces[key]) || {};
                setInterfaces(key, ifaces);
                setValue(key, el);
            });
        };

        this.setOption = function (opt, value, interfaces) {
            setInterfaces(opt, interfaces);
            var reader = getReader(opt),
                validator = getValidator(opt);
            if (validator(value)) {
                value = reader(value);
                setValue(opt, value);
                return true;
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
        }

        function getReader(opt) {
            return get(myself._interfaces, opt, 'reader', myself._libraries.mappers['identity']);
        }

        function getWriter(opt) {
            return get(myself._interfaces, opt, 'writer', myself._libraries.mappers['identity']);
        }

        function getValidator(opt) {
            return get(myself._interfaces, opt, 'validator', myself._libraries.predicates['tautology']);
        }

        function getValue(opt) {
            return get(myself._options, opt, 'value');
        }

        // Reader, Writer and Validator work in the same way:
        // If the value is a function, use it. 
        // Otherwise, if it is a string and a valid library key, use it.
        // Otherwise, use a default library function: for readers and writers an indentity map, 
        //    for validators a predicate that always returns true.

        function setReader(opt, fn) {
            var lib = myself._libraries.mappers;
            fn = (_.isFunction(fn) && fn) || (_.isString(fn) && lib[fn]) || getReader(opt) || lib['identity'];
            return set(myself._interfaces, opt, 'reader', fn);
        }

        function setWriter(opt, fn) {
            var lib = myself._libraries.mappers;
            fn = (_.isFunction(fn) && fn) || (_.isString(fn) && lib[fn]) || getWriter(opt) || lib['identity'];
            return set(myself._interfaces, opt, 'writer', fn);
        }

        function setValidator(opt, fn) {
            var lib = myself._libraries.predicates;
            fn = (_.isFunction(fn) && fn) || (_.isString(fn) && lib[fn]) || getValidator(opt) || lib['tautology'];
            return set(myself._interfaces, opt, 'validator', fn);
        }

        function setValue(opt, value) {
            return set(myself._options, opt, 'value', value);
        }

        // Init
        this.init(config.defaults, config.interfaces, config.libraries);

    }

    // Shared / Static
    function get(container, opt, attr, defaultValue) {
        var val = defaultValue || undefined;
        if (container && container[opt] && container[opt].hasOwnProperty(attr)) {
            val = container[opt][attr];
        }

        return val;
    }

    function set(container, opt, attr, value) {
        if (container && opt && attr) {
            container[opt] = container[opt] || {};
            container[opt][attr] = value;
        }
    }

    D.OptionsManager = OptionsManager;
})(Dashboards);

 /* ************************ new file ************************ */
/*
 * Dashboards Popups
 */

var wd = wd || {};
wd.cdf = wd.cdf || {};
wd.cdf.popups = wd.cdf.popups || {};

wd.cdf.popups.okPopup = {
    template: Mustache.compile(
        "<div class='cdfPopup'>" +
        "  <div class='cdfPopupHeader'>{{{header}}}</div>" +
        "  <div class='cdfPopupBody'>" +
        "    <div class='cdfPopupDesc'>{{{desc}}}</div>" +
        "    <div class='cdfPopupButton'>{{{button}}}</div>" +
        "  </div>" +
        "</div>"),
    defaults: {
        header: "Title",
        desc: "Description Text",
        button: "Button Text",
        callback: function () {
            return true;
        }
    },
    $el: undefined,
    show: function (opts) {
        if (opts || this.firstRender) {
            this.render(opts);
        }
        this.$el.show();
    },
    hide: function () {
        this.$el.hide();
    },
    render: function (newOpts) {
        var opts = _.extend({}, this.defaults, newOpts);
        var myself = this;

        if (this.firstRender) {
            this.$el = $('<div/>').addClass('cdfPopupContainer')
                .hide()
                .appendTo('body');
            this.firstRender = false;
        }

        this.$el.empty().html(this.template(opts));
        this.$el.find('.cdfPopupButton').click(function () {
            opts.callback();
            myself.hide();
        });
    },
    firstRender: true
};


/*
 * Error information divs
 *
 *
 */
wd.cdf.notifications = wd.cdf.notifications || {};

wd.cdf.notifications.component = {
    template: Mustache.compile(
        "<div class='cdfNotification component {{#isSmallComponent}}small{{/isSmallComponent}}'>" +
        "  <div class='cdfNotificationBody'>" +
        "    <div class='cdfNotificationImg'>&nbsp;</div>" +
        "    <div class='cdfNotificationTitle' title='{{title}}'>{{{title}}}</div>" +
        "    <div class='cdfNotificationDesc' title='{{desc}}'>{{{desc}}}</div>" +
        "  </div>" +
        "</div>"),
    defaults: {
        title: "Component Error",
        desc: "Error processing component."
    },
    render: function (ph, newOpts) {
        var opts = _.extend({}, this.defaults, newOpts);
        opts.isSmallComponent = ($(ph).width() < 300);
        $(ph).empty().html(this.template(opts));
        var $nt = $(ph).find('.cdfNotification');
        $nt.css({
            'line-height': $nt.height() + 'px'
        });
    }
};

wd.cdf.notifications.growl = {
    template: Mustache.compile(
        "<div class='cdfNotification growl'>" +
        "  <div class='cdfNotificationBody'>" +
        "    <h1 class='cdfNotificationTitle' title='{{title}}'>{{{title}}}</h1>" +
        "    <h2 class='cdfNotificationDesc' title='{{desc}}'>{{{desc}}}</h2>" +
        "  </div>" +
        "</div>"),

    // TODO - this must be changed! since we don't use blockUI lib anymore!
    defaults: {
        title: 'Title',
        desc: 'Default CDF notification.',
        timeout: 4000,
        onUnblock: function () {
            return true;
        },
        css: $.extend({}, {
            position: 'absolute',
            width: '100%',
            top: '10px'
        }),
        showOverlay: false,
        fadeIn: 700,
        fadeOut: 1000,
        centerY: false
    },
    render: function (newOpts) {
        var opts = _.extend({}, this.defaults, newOpts),
            $m = $(this.template(opts)),
            myself = this;
        opts.message = $m;
        var outerUnblock = opts.onUnblock;
        opts.onUnblock = function () {
            myself.$el.hide();
            outerUnblock.call(this);
        };
        if (this.firstRender) {
            this.$el = $('<div/>').addClass('cdfNotificationContainer')
                .hide()
                .appendTo('body');
            this.firstRender = false;
        }
        // TODO - removed the blockUI call
        // this.$el.show().block(opts);
    },
    firstRender: true
};

 /* ************************ new file ************************ */
BaseComponent = Base.extend({
    visible: true,
    isManaged: true,
    timerStart: 0,
    timerSplit: 0,
    elapsedSinceSplit: -1,
    elapsedSinceStart: -1,
    logColor: undefined,

    clear: function () {
        $("#" + this.htmlObject).empty();
    },

    copyEvents: function (target, events) {
        _.each(events, function (evt, evtName) {
            var e = evt,
                tail = evt.tail;
            while ((e = e.next) !== tail) {
                target.on(evtName, e.callback, e.context);
            }
        });
    },

    clone: function (parameterRemap, componentRemap, htmlRemap) {
        var that, dashboard, callbacks;
        /*
         * `dashboard` points back to this component, so we need to remove it from
         * the original component before cloning, lest we enter an infinite loop.
         * `_callbacks` contains the event bindings for the Backbone Event mixin
         * and may also point back to the dashboard. We want to clone that as well,
         * but have to be careful about it.
         */
        dashboard = this.dashboard;
        callbacks = this._callbacks;
        delete this.dashboard;
        delete this._callbacks;
        that = $.extend(true, {}, this);
        that.dashboard = this.dashboard = dashboard;
        this._callbacks = callbacks;
        this.copyEvents(that, callbacks);

        if (that.parameters) {
            that.parameters = that.parameters.map(function (param) {
                if (param[1] in parameterRemap) {
                    return [param[0], parameterRemap[param[1]]];
                } else {
                    return param;
                }
            });
        }
        if (that.components) {
            that.components = that.components.map(function (comp) {
                if (comp in componentRemap) {
                    return componentRemap[comp];
                } else {
                    return comp;
                }
            });
        }
        that.htmlObject = !that.htmlObject ? undefined : htmlRemap[that.htmlObject];
        if (that.listeners) {
            that.listeners = that.listeners.map(function (param) {
                if (param in parameterRemap) {
                    return parameterRemap[param];
                } else {
                    return param;
                }
            });
        }
        if (that.parameter && that.parameter in parameterRemap) {
            that.parameter = parameterRemap[that.parameter];
        }
        return that;
    },
    getAddIn: function (slot, addIn) {
        var type = typeof this.type === "function" ? this.type() : this.type;
        return Dashboards.getAddIn(type, slot, addIn);
    },
    hasAddIn: function (slot, addIn) {
        var type = typeof this.type === "function" ? this.type() : this.type;
        return Dashboards.hasAddIn(type, slot, addIn);
    },
    getValuesArray: function () {


        var jXML;
        if (typeof (this.valuesArray) === 'undefined' || this.valuesArray.length === 0) {
            if (typeof (this.queryDefinition) !== 'undefined') {

                var vid = (this.queryDefinition.queryType === "sql") ? "sql" : "none";
                if ((this.queryDefinition.queryType === "mdx") && (!this.valueAsId)) {
                    vid = "mdx";
                } else if (this.queryDefinition.dataAccessId !== undefined && !this.valueAsId) {
                    vid = 'cda';
                }
                QueryComponent.makeQuery(this);
                var myArray = [];
                for (p in this.result) {
                    if (this.result.hasOwnProperty(p)) {
                        switch (vid) {
                        case "sql":
                            myArray.push([this.result[p][0], this.result[p][1]]);
                            break;
                        case "mdx":
                            myArray.push([this.result[p][1], this.result[p][0]]);
                            break;
                        case 'cda':
                            myArray.push([this.result[p][0], this.result[p][1]]);
                            break;
                        default:
                            myArray.push([this.result[p][0], this.result[p][0]]);
                            break;
                        }
                    }
                }

                return myArray;
            } else {

                //go through parameter array and update values
                var p = new Array(this.parameters ? this.parameters.length : 0);
                for (var i = 0, len = p.length; i < len; i++) {
                    var key = this.parameters[i][0];
                    var value = this.parameters[i][1] === "" || this.parameters[i][1] === "NIL" ? this.parameters[i][2] : Dashboards.getParameterValue(this.parameters[i][1]);
                    p[i] = [key, value];
                }

                //execute the xaction to populate the selector
                var myself = this;
                if (this.url) {
                    var arr = {};
                    $.each(p, function (i, val) {
                        arr[val[0]] = val[1];
                    });
                    jXML = Dashboards.parseXActionResult(myself, Dashboards.urlAction(this.url, arr));
                } else {
                    jXML = Dashboards.callPentahoAction(myself, this.solution, this.path, this.action, p, null);
                }
                //transform the result int a javascript array
                var myArray = this.parseArray(jXML, false);
                return myArray;
            }
        } else {
            return this.valuesArray;
        }
    },
    parseArray: function (jData, includeHeader) {

        if (jData === null) {
            return []; //we got an error...
        }

        if ($(jData).find("CdaExport").size() > 0) {
            return this.parseArrayCda(jData, includeHeader);
        }

        var myArray = [];

        var jHeaders = $(jData).find("COLUMN-HDR-ITEM");
        if (includeHeader && jHeaders.size() > 0) {
            var _a = [];
            jHeaders.each(function () {
                _a.push($(this).text());
            });
            myArray.push(_a);
        }

        var jDetails = $(jData).find("DATA-ROW");
        jDetails.each(function () {
            var _a = [];
            $(this).children("DATA-ITEM").each(function () {
                _a.push($(this).text());
            });
            myArray.push(_a);
        });

        return myArray;

    },
    parseArrayCda: function (jData, includeHeader) {
        //ToDo: refactor with parseArray?..use as parseArray?..
        var myArray = [];

        var jHeaders = $(jData).find("ColumnMetaData");
        if (jHeaders.size() > 0) {
            if (includeHeader) { //get column names
                var _a = [];
                jHeaders.each(function () {
                    _a.push($(this).attr("name"));
                });
                myArray.push(_a);
            }
        }

        //get contents
        var jDetails = $(jData).find("Row");
        jDetails.each(function () {
            var _a = [];
            $(this).children("Col").each(function () {
                _a.push($(this).text());
            });
            myArray.push(_a);
        });

        return myArray;

    },

    setAddInDefaults: function (slot, addIn, defaults) {
        var type = typeof this.type === "function" ? this.type() : this.type;
        Dashboards.setAddInDefaults(type, slot, addIn, defaults);
    },
    setAddInOptions: function (slot, addIn, options) {
        if (!this.addInOptions) {
            this.addInOptions = {};
        }

        if (!this.addInOptions[slot]) {
            this.addInOptions[slot] = {};
        }
        this.addInOptions[slot][addIn] = options;
    },

    getAddInOptions: function (slot, addIn) {
        var opts = null;
        try {
            opts = this.addInOptions[slot][addIn];
        } catch (e) {
            /* opts is still null, no problem */
        }
        /* opts is falsy if null or undefined */
        return opts || {};
    },

    startTimer: function () {

        this.timerStart = new Date();
        this.timerSplit = new Date();

    },

    splitTimer: function () {

        // Sanity check, in case this component doesn't follow the correct workflow
        if (this.elapsedSinceStart === -1 || this.elapsedSinceSplit === -1) {
            this.startTimer();
        }

        var now = new Date();

        this.elapsedSinceStart = now.getTime() - this.timerStart.getTime();
        this.elapsedSinceSplit = now.getTime() - this.timerSplit.getTime();

        this.timerSplit = now;
        return this.getTimerInfo();
    },

    formatTimeDisplay: function (t) {
        return Math.log(t) / Math.log(10) >= 3 ? Math.round(t / 100) / 10 + "s" : t + "ms";
    },

    getTimerInfo: function () {

        return {
            timerStart: this.timerStart,
            timerSplit: this.timerSplit,
            elapsedSinceStart: this.elapsedSinceStart,
            elapsedSinceStartDesc: this.formatTimeDisplay(this.elapsedSinceStart),
            elapsedSinceSplit: this.elapsedSinceSplit,
            elapsedSinceSplitDesc: this.formatTimeDisplay(this.elapsedSinceSplit)
        }

    },

    /*
     * This method assigns and returns a unique and somewhat randomish color for
     * this log. The goal is to be able to track cdf lifecycle more easily in
     * the console logs. We're returning a Hue value between 0 and 360, a range between 0
     * and 75 for saturation and between 45 and 80 for value
     *
     */

    getLogColor: function () {

        if (this.logColor) {
            return this.logColor;
        } else {
            // generate a unique,

            var hashCode = function (str) {
                var hash = 0;
                if (str.length === 0) {
                    return hash;
                }

                for (var i = 0; i < str.length; i++) {
                    var chr = str.charCodeAt(i);
                    hash = ((hash << 5) - hash) + chr;
                    hash = hash & hash; // Convert to 32bit integer
                }
                return hash;
            };

            var hash = hashCode(this.name).toString();
            var hueSeed = hash.substr(hash.length - 6, 2) || 0;
            var saturationSeed = hash.substr(hash.length - 2, 2) || 0;
            var valueSeed = hash.substr(hash.length - 4, 2) || 0;

            this.logColor = Dashboards.hsvToRgb(360 / 100 * hueSeed, 75 / 100 * saturationSeed, 45 + (80 - 45) / 100 * valueSeed);
            return this.logColor;

        }


    }

});


var TextComponent = BaseComponent.extend({
    update: function () {
        $("#" + this.htmlObject).html(this.expression());
    }
});

var QueryComponent = BaseComponent.extend({
    visible: false,
    update: function () {
        QueryComponent.makeQuery(this);
    },
    warnOnce: function () {
        Dashboards.log("Warning: QueryComponent behaviour is due to change. See " +
            "http://www.webdetails.org/redmine/projects/cdf/wiki/QueryComponent" +
            " for more information");
        delete(this.warnOnce);
    }
}, {
    makeQuery: function (object) {

        if (this.warnOnce) {
            this.warnOnce();
        }
        var cd = object.queryDefinition;
        if (cd === undefined) {
            Dashboards.log("Fatal - No query definition passed", "error");
            return;
        }
        var query = Dashboards.getQuery(cd);
        object.queryState = query;

        // Force synchronous queries
        query.setAjaxOptions({
            async: false
        });

        query.fetchData(object.parameters, function (values) {
            // We need to make sure we're getting data from the right place,
            // depending on whether we're using CDA

            var changedValues;

            object.metadata = values.metadata;
            object.result = values.resultset !== undefined ? values.resultset : values;
            object.queryInfo = values.queryInfo;

            if ((typeof (object.postFetch) === 'function')) {
                changedValues = object.postFetch(values);
            }
            if (changedValues !== undefined) {
                values = changedValues;

            }

            if (object.resultvar !== undefined) {
                Dashboards.setParameter(object.resultvar, object.result);
            }
            object.result = values.resultset !== undefined ? values.resultset : values;
            if (typeof values.resultset !== "undefined") {
                object.metadata = values.metadata;
                object.queryInfo = values.queryInfo;
            }
        });

    }
});

var MdxQueryGroupComponent = BaseComponent.extend({
    visible: false,
    update: function () {
        OlapUtils.updateMdxQueryGroup(this);
    }
});


/*
 * UnmanagedComponent is an advanced version of the BaseComponent that allows
 * control over the core CDF lifecycle for implementing components. It should
 * be used as the base class for all components that desire to implement an
 * asynchronous lifecycle, as CDF cannot otherwise ensure that the postExecution
 * callback is correctly handled.
 */
var UnmanagedComponent = BaseComponent.extend({
    isManaged: false,
    isRunning: false,

    /*
     * Handle calling preExecution when it exists. All components extending
     * UnmanagedComponent should either use one of the three lifecycles declared
     * in this class (synchronous, triggerQuery, triggerAjax), or call this method
     * explicitly at the very earliest opportunity. If preExec returns a falsy
     * value, component execution should be cancelled as close to immediately as
     * possible.
     */
    preExec: function () {
        /*
         * runCounter gets incremented every time we run a query, allowing us to
         * determine whether the query has been called again after us.
         */
        if (typeof this.runCounter === "undefined") {
            this.runCounter = 0;
        }
        var ret;
        if (typeof this.preExecution === "function") {
            try {
                ret = this.preExecution();
                ret = typeof ret === "undefined" || ret;
            } catch (e) {
                this.error(Dashboards.getErrorObj('COMPONENT_ERROR').msg, e);
                this.dashboard.log(e, "error");
                ret = false;
            }
        } else {
            ret = true;
        }
        this.trigger('cdf cdf:preExecution', this, ret);
        return ret;
    },

    /*
     * Handle calling postExecution when it exists. All components extending
     * UnmanagedComponent should either use one of the three lifecycles declared
     * in this class (synchronous, triggerQuery, triggerAjax), or call this method
     * explicitly immediately before yielding control back to CDF.
     */
    postExec: function () {
        if (typeof this.postExecution === "function") {
            this.postExecution();
        }
        this.trigger('cdf cdf:postExecution', this);
    },

    drawTooltip: function () {
        if (this.tooltip) {
            this._tooltip = typeof this.tooltip === "function" ?
                this.tooltip() :
                this.tooltip;
        }
    },
    showTooltip: function () {
        if (typeof this._tooltip !== "undefined") {
            $("#" + this.htmlObject).attr("title", this._tooltip).tooltip({
                delay: 0,
                track: true,
                fade: 250
            });
        }
    },

    /*
     * The synchronous lifecycle handler closely resembles the core CDF lifecycle,
     * and is provided as an alternative for components that desire the option to
     * alternate between a synchronous and asynchronous style lifecycles depending
     * on external configuration (e.g. if it can take values from either a static
     * array or a query). It take the component drawing method as a callback.
     */
    synchronous: function (callback, args) {
        if (!this.preExec()) {
            return;
        }
        var silent = this.isSilent();
        if (!silent) {
            this.block();
        }
        setTimeout(_.bind(function () {
            try {
                /* The caller should specify what 'this' points at within the callback
                 * via a Function#bind or _.bind. Since we need to pass a 'this' value
                 * to call, the component itself is the only sane value to pass as the
                 * callback's 'this' as an alternative to using bind.
                 */
                callback.call(this, args || []);
                this.drawTooltip();
                this.postExec();
                this.showTooltip();
            } catch (e) {
                this.error(Dashboards.getErrorObj('COMPONENT_ERROR').msg, e);
                this.dashboard.log(e, "error");
            } finally {
                if (!silent) {
                    this.unblock();
                }
            }
        }, this), 10);
    },

    /*
     * The triggerQuery lifecycle handler builds a lifecycle around Query objects.
     *
     * It takes a query definition object that is passed directly into the Query
     * constructor, and the component rendering callback, and implements the full
     * preExecution->block->render->postExecution->unblock lifecycle. This method
     * detects concurrent updates to the component and ensures that only one
     * redraw is performed.
     */
    triggerQuery: function (queryDef, callback, userQueryOptions) {
        if (!this.preExec()) {
            return;
        }
        var silent = this.isSilent();
        if (!silent) {
            this.block();
        }
        userQueryOptions = userQueryOptions || {};
        /*
         * The query response handler should trigger the component-provided callback
         * and the postExec stage if the call wasn't skipped, and should always
         * unblock the UI
         */
        var success = _.bind(function (data) {
            callback(data);
            this.postExec();
        }, this);
        var always = _.bind(function () {
            if (!silent) {
                this.unblock();
            }
        }, this);
        var handler = this.getSuccessHandler(success, always),
            errorHandler = this.getErrorHandler();

        var query = this.queryState = this.query = Dashboards.getQuery(queryDef);
        var ajaxOptions = {
            async: true
        };
        if (userQueryOptions.ajax) {
            _.extend(ajaxOptions, userQueryOptions.ajax);
        }
        query.setAjaxOptions(ajaxOptions);
        if (userQueryOptions.pageSize) {
            query.setPageSize(userQueryOptions.pageSize);
        }
        query.fetchData(this.parameters, handler, errorHandler);
    },

    /*
     * The triggerAjax method implements a lifecycle based on generic AJAX calls.
     * It implements the full preExecution->block->render->postExecution->unblock
     * lifecycle.
     *
     * triggerAjax can be used with either of the following call conventions:
     * - this.triggerAjax(url,params,callback);
     * - this.triggerAjax({url: url, data: params, ...},callback);
     * In the second case, you can add any other jQuery.Ajax parameters you desire
     * to the object, but triggerAjax will take control over the success and error
     * callbacks.
     */
    triggerAjax: function (url, params, callback) {
        if (!this.preExec()) {
            return;
        }
        var silent = this.isSilent();
        if (!silent) {
            this.block();
        }
        var ajaxParameters = {
            async: true
        };
        /* Detect call convention used and adjust parameters */
        if (typeof callback !== "function") {
            callback = params;
            _.extend(ajaxParameters, url);
        } else {
            _.extend(ajaxParameters, {
                url: url,
                data: params
            });
        }
        var success = _.bind(function (data) {
            callback(data);
            this.trigger('cdf cdf:render', this, data);
            this.postExec();
        }, this);
        var always = _.bind(function () {
            if (!silent) {
                this.unblock();
            }
        }, this);
        ajaxParameters.success = this.getSuccessHandler(success, always);
        ajaxParameters.error = this.getErrorHandler();
        jQuery.ajax(ajaxParameters);
    },


    /*
     * Increment the call counter, so we can keep track of the order in which
     * requests were made.
     */
    callCounter: function () {
        return ++this.runCounter;
    },

    /* Trigger an error event on the component. Takes as arguments the error
     * message and, optionally, a `cause` object.
     * Also
     */
    error: function (msg, cause) {
        msg = msg || Dashboards.getErrorObj('COMPONENT_ERROR').msg;
        if (!this.isSilent()) {
            this.unblock();
        }
        this.errorNotification({
            error: cause,
            msg: msg
        });
        this.trigger("cdf cdf:error", this, msg, cause || null);
    },
    /*
     * Build a generic response handler that runs the success callback when being
     * called in response to the most recent AJAX request that was triggered for
     * this component (as determined by comparing counter and this.runCounter),
     * and always calls the always callback. If the counter is not provided, it'll
     * be generated automatically.
     *
     * Accepts the following calling conventions:
     *
     * - this.getSuccessHandler(counter, success, always)
     * - this.getSuccessHandler(counter, success)
     * - this.getSuccessHandler(success, always)
     * - this.getSuccessHandler(success)
     */
    getSuccessHandler: function (counter, success, always) {

        if (arguments.length === 1) {
            /* getSuccessHandler(success) */
            success = counter;
            counter = this.callCounter();
        } else if (typeof counter === 'function') {
            /* getSuccessHandler(success,always) */
            always = success;
            success = counter;
            counter = this.callCounter();
        }
        return _.bind(function (data) {
                var newData;
                if (counter >= this.runCounter) {
                    try {
                        if (typeof this.postFetch === "function") {
                            newData = this.postFetch(data);
                            this.trigger('cdf cdf:postFetch', this, data);
                            data = typeof newData === "undefined" ? data : newData;
                        }
                        success(data);
                    } catch (e) {
                        this.error(Dashboards.getErrorObj('COMPONENT_ERROR').msg, e);
                        this.dashboard.log(e, "error");
                    }
                }
                if (typeof always === "function") {
                    always();
                }
            },
            this);
    },

    getErrorHandler: function () {
        return _.bind(function () {
                var err = Dashboards.parseServerError.apply(this, arguments);
                this.error(err.msg, err.error);
            },
            this);
    },
    errorNotification: function (err, ph) {
        ph = ph || ((this.htmlObject) ? $('#' + this.htmlObject) : undefined);
        var name = this.name.replace('render_', '');
        err.msg = err.msg + ' (' + name + ')';
        Dashboards.errorNotification(err, ph);
    },

    /*
     * Trigger UI blocking while the component is updating. Default implementation
     * uses the global CDF blockUI, but implementers are encouraged to override
     * with per-component blocking where appropriate (or no blocking at all in
     * components that support it!)
     */
    block: function () {
        if (!this.isRunning) {
            this.dashboard.incrementRunningCalls();
            this.isRunning = true;
        }

    },

    /*
     * Trigger UI unblock when the component finishes updating. Functionality is
     * defined as undoing whatever was done in the block method. Should also be
     * overridden in components that override UnmanagedComponent#block.
     */
    unblock: function () {

        if (this.isRunning) {
            this.dashboard.decrementRunningCalls();
            this.isRunning = false;
        }
    },

    isSilent: function () {
        return (this.lifecycle) ? !! this.lifecycle.silent : false;
    }
});

 /* ************************ new file ************************ */
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

var SelectMultiComponent = SelectBaseComponent.extend({
    getValue: function () {
        var ph = $("#" + this.htmlObject + " select");
        var val = ph.val();
        return val === null ? [] : val;
    },


    /**
     * Obtains the normalized and defaulted value of
     * the {@link #isMultiple} option.
     *
     * @override
     * @return {boolean}
     */
    _allowMultipleValues: function () {
        return this.isMultiple === null || !! this.isMultiple;
    },

    /**
     * When the size option is unspecified,
     * and multiple values are allowed,
     * returns the number of items in the
     * provided possible values list.
     *
     * @override
     */
    _getListSize: function (values) {
        var size = this.base(values);
        if (size === null) {
            if (!this._allowMultipleValues()) {
                size = values.length;
            } // TODO: otherwise no default... Why?
        }

        return size;
    }
});


var TextInputComponent = BaseComponent.extend({
    update: function () {
        var name = this.name;
        var selectHTML = "<input type='text' id='" + name +
            "' name='" + name +
            "' value='" + Dashboards.getParameterValue(this.parameter) +
            (this.size ? ("' size='" + this.size) : "") +
            (this.maxLength ? ("' maxlength='" + this.maxLength) : "") +
            "'>";

        $("#" + this.htmlObject).html(selectHTML);

        $("#" + name)
            .change(function () {
                Dashboards.processChange(name);
            })
            .keyup(function (ev) {
                if (ev.keyCode === 13) {
                    Dashboards.processChange(name);
                }
            });
    },
    getValue: function () {
        return $("#" + this.name).val();
    }
});


var TextareaInputComponent = BaseComponent.extend({
    update: function () {
        var name = this.name;
        var selectHTML = "<textarea id='" + name +
            "' name='" + name +
            (this.numRows ? ("' rows='" + this.numRows) : "") +
            (this.numColumns ? ("' cols='" + this.numColumns) : "") +
            "'>" +
            Dashboards.getParameterValue(this.parameter) +
            '</textarea>';

        $("#" + this.htmlObject).html(selectHTML);

        $("#" + name)
            .change(function () {
                Dashboards.processChange(name);
            })
            .keyup(function (ev) {
                if (ev.keyCode === 13) {
                    Dashboards.processChange(name);
                }
            });
    },
    getValue: function () {
        return $("#" + this.name).val();
    }
});


// Start by setting a sane i18n default to datepicker
//TODO: move this to where we know for sure datepicker is loaded..
if ($.datepicker) {
    $(function () {
        $.datepicker.setDefaults($.datepicker.regional[''])
    });
}

var DateInputComponent = BaseComponent.extend({
    update: function () {
        var format = (this.dateFormat === undefined || this.dateFormat === null) ? 'yy-mm-dd' : this.dateFormat;
        var myself = this;

        var startDate, endDate;

        if (this.startDate === 'TODAY') startDate = new Date();
        else if (this.startDate) startDate = $.datepicker.parseDate(format, this.startDate);

        if (this.endDate === 'TODAY') endDate = new Date();
        else if (this.endDate) endDate = $.datepicker.parseDate(format, this.endDate);

        //ToDo: stretch interval to catch defaultValue?..
        //Dashboards.getParameterValue(this.parameter))

        $("#" + this.htmlObject).html($("<input/>").attr("id", this.name).attr("value", Dashboards.getParameterValue(this.parameter)).css("width", "80px"));
        $(function () {
            $("#" + myself.htmlObject + " input").datepicker({
                dateFormat: format,
                changeMonth: true,
                changeYear: true,
                minDate: startDate,
                maxDate: endDate,
                onSelect: function (date, input) {
                    Dashboards.processChange(myself.name);
                }
            });
            // Add JQuery DatePicker standard localization support only if the dashboard is localized
            if (typeof Dashboards.i18nSupport !== "undefined" && Dashboards.i18nSupport !== null) {
                var $input = $("#" + myself.htmlObject + " input");

                $input.datepicker('option', $.datepicker.regional[Dashboards.i18nCurrentLanguageCode]);


                //Setup alt field and format to keep iso format
                $input.parent().append($('<hidden>').attr("id", myself.name + "_hidden"));
                $input.datepicker("option", "altField", "#" + myself.name + "_hidden");
                $input.datepicker("option", "altFormat", format);
            }
        });
    },
    getValue: function () {
        if (typeof Dashboards.i18nSupport !== "undefined" && Dashboards.i18nSupport !== null)
            return $("#" + this.name + "_hidden").val();
        else
            return $("#" + this.name).val();
    }
});


var DateRangeInputComponent = BaseComponent.extend({
    update: function () {
        var dr;
        if (this.singleInput === undefined || this.singleInput === true) {
            dr = $("<input/>").attr("id", this.name).attr("value", Dashboards.getParameterValue(this.parameter[0]) + " > " + Dashboards.getParameterValue(this.parameter[1])).css("width", "170px");
            $("#" + this.htmlObject).html(dr);
        } else {
            dr = $("<input/>").attr("id", this.name).attr("value", Dashboards.getParameterValue(this.parameter[0])).css("width", "80px");
            $("#" + this.htmlObject).html(dr);
            dr.after($("<input/>").attr("id", this.name + "2").attr("value", Dashboards.getParameterValue(this.parameter[1])).css("width", "80px"));
            if (this.inputSeparator !== undefined) {
                dr.after(this.inputSeparator);
            }
        }
        var offset = dr.offset();
        var myself = this;
        var earliestDate = this.earliestDate !== undefined ? this.earliestDate : Date.parse('-1years');
        var latestDate = this.latestDate !== undefined ? this.latestDate : Date.parse('+1years');
        var leftOffset = this.leftOffset !== undefined ? this.leftOffset : 0;
        var topOffset = this.topOffset !== undefined ? this.topOffset : 15;

        var changed, closed;

        function triggerWhenDone() {
            if (changed && closed) {
                myself.fireInputChange(myself.startValue, myself.endValue);
                changed = closed = false;
            }
        };

        var format = (myself.dateFormat === undefined || myself.dateFormat === null) ? 'yy-mm-dd' : myself.dateFormat;

        $(function () {
            $("#" + myself.htmlObject + " input").daterangepicker({
                posX: offset.left + leftOffset,
                posY: offset.top + topOffset,
                earliestDate: earliestDate,
                latestDate: latestDate,
                dateFormat: format,
                onOpen: function () {
                    changed = closed = false;
                    myself.startValue = null;
                    myself.endValue = null;
                },
                onDateSelect: function (rangeA, rangeB) {
                    changed = true;
                    myself.storeChanges(rangeA, rangeB);
                    triggerWhenDone();
                },
                onClose: function () {
                    closed = true;
                    triggerWhenDone();
                }
            });
        });
    },

    fireInputChange: function (start, end) {
        //TODO: review this!
        if (this.preChange) {
            this.preChange(start, end);
        }

        if (this.parameter) {
            if (this.parameter.length === 2) Dashboards.setParameter(this.parameter[1], end);
            if (this.parameter.length > 0) Dashboards.fireChange(this.parameter[0], start);
        }

        if (this.postChange) {
            this.postChange(start, end);
        }
    },

    storeChanges: function (start, end) {
        this.startValue = start;
        this.endValue = end;
    }
}, {
    fireDateRangeInputChange: function (name, rangeA, rangeB) {
        // WPG: can we just use the parameter directly?
        var object = Dashboards.getComponentByName(name);
        if (!(typeof (object.preChange) === 'undefined')) {
            object.preChange(rangeA, rangeB);
        }
        var parameters = eval(name + ".parameter");
        // set the second date and fireChange the first
        Dashboards.setParameter(parameters[1], rangeB);
        Dashboards.fireChange(parameters[0], rangeA);
        if (!(typeof (object.postChange) === 'undefined')) {
            object.postChange(rangeA, rangeB);
        }
    }
});

var MonthPickerComponent = BaseComponent.extend({
    update: function () {
        var selectHTML = this.getMonthPicker(this.name, this.size, this.initialDate, this.minDate, this.maxDate, this.months);
        $("#" + this.htmlObject).html(selectHTML);
        var myself = this;
        $("#" + this.name).change(function () {
            Dashboards.processChange(myself.name);
        });
    },
    getValue: function () {
        var value = $("#" + this.name).val();

        var year = value.substring(0, 4);
        var month = parseInt(value.substring(5, 7) - 1);
        var d = new Date(year, month, 1);

        // rebuild picker
        var selectHTML = this.getMonthPicker(this.name, this.size, d, this.minDate, this.maxDate, this.months);
        $("#" + this.htmlObject).html(selectHTML);
        var myself = this;
        $("#" + this.name).change(function () {
            Dashboards.processChange(myself.name);
        });
        return value;
    },
    parseDate: function (aDateString) {
        //This works assuming the Date comes in this format -> yyyy-mm-dd or yyyy-mm
        //Date.UTC(year[year after 1900],month[0 to 11],day[1 to 31], hours[0 to 23], min[0 to 59], sec[0 to 59], ms[0 to 999])
        var parsedDate = null;
        var yearIndex = 0,
            monthIndex = 1,
            dayindex = 2;
        var split = aDateString.split("-");
        var year, month, day;

        if (split.length === 3) {
            year = parseInt(split[yearIndex]);
            month = parseInt(split[monthIndex]);
            day = parseInt(split[dayindex]);
            parsedDate = new Date(Date.UTC(year, (month - 1), day));
        } else if (split.length === 2) {
            year = parseInt(split[yearIndex]);
            month = parseInt(split[monthIndex]);
            parsedDate = new Date(Date.UTC(year, (month - 1)));
        }

        return parsedDate;
    },
    getMonthsAppart: function (aDateOne, aDateTwo) {
        var min, max;
        if (aDateOne < aDateTwo) {
            min = aDateOne;
            max = aDateTwo;
        } else {
            min = aDateTwo;
            max = aDateOne;
        }

        var yearsAppart = (max.getFullYear() - min.getFullYear());
        var monthsToAdd = yearsAppart * 12;
        var monthCount = (max.getMonth() - min.getMonth()) + monthsToAdd; //TODO verify this calculation

        return monthCount;
    },
    normalizeDateToCompare: function (dateObject) {
        var normalizedDate = dateObject;
        normalizedDate.setDate(1);
        normalizedDate.setHours(0);
        normalizedDate.setMinutes(0);
        normalizedDate.setSeconds(0);
        normalizedDate.setMilliseconds(0);

        return normalizedDate;

    },
    getMonthPicker: function (object_name, object_size, initialDate, minDate, maxDate, monthCount) {


        var selectHTML = "<select";
        selectHTML += " id='" + object_name + "'";

        if (initialDate === undefined || initialDate === null) {
            initialDate = new Date();
        }
        if (minDate === undefined || minDate === null) {
            minDate = new Date();
            minDate.setYear(1980);
        }
        if (maxDate === undefined || maxDate === null) {
            maxDate = new Date();
            maxDate.setYear(2060);
        }

        //if any of the dates comes in string format this will parse them
        if (typeof initialDate === "string") {
            initialDate = this.parseDate(initialDate);
        }
        if (typeof minDate === "string") {
            minDate = this.parseDate(minDate);
        }
        if (typeof maxDate === "string") {
            maxDate = this.parseDate(maxDate);
        }

        // if monthCount is not defined we'll use everything between max and mindate
        var monthCountUndefined = false;
        if (monthCount === undefined || monthCount === 0) {
            monthCount = this.getMonthsAppart(minDate, maxDate);
            monthCountUndefined = true;
        }

        //set size
        if (object_size !== undefined) {
            selectHTML += " size='" + object_size + "'";
        }
        selectHTML += '>';

        var currentDate = new Date(+initialDate);

        /*
         * This block is to make sure the months are compared equally. A millisecond can ruin the comparation.
         */

        if (monthCountUndefined === true) {
            currentDate.setMonth(currentDate.getMonth() - (this.getMonthsAppart(minDate, currentDate)) - 1);
        } else {
            currentDate.setMonth(currentDate.getMonth() - (monthCount / 2) - 1);
        }
        currentDate = this.normalizeDateToCompare(currentDate);
        var normalizedMinDate = this.normalizeDateToCompare(minDate);
        var normalizedMaxDate = this.normalizeDateToCompare(maxDate);

        for (var i = 0; i <= monthCount; i++) {

            currentDate.setMonth(currentDate.getMonth() + 1);

            if (currentDate >= normalizedMinDate && currentDate <= normalizedMaxDate) {
                selectHTML += "<option value = '" + currentDate.getFullYear() + "-" + this.zeroPad((currentDate.getMonth() + 1), 2) + "' ";

                if (currentDate.getFullYear() === initialDate.getFullYear() && currentDate.getMonth() === initialDate.getMonth()) {
                    selectHTML += "selected='selected'"
                }

                selectHTML += ">" + Dashboards.monthNames[currentDate.getMonth()] + " " + currentDate.getFullYear() + "</option>";
            }
        }

        selectHTML += "</select>";

        return selectHTML;
    },
    zeroPad: function (num, size) {
        var n = "00000000000000" + num;
        return n.substring(n.length - size, n.length);
    }
});

var ToggleButtonBaseComponent = InputBaseComponent.extend({
    draw: function (myArray) {

        var selectHTML = "";

        //default
        var currentVal = Dashboards.getParameterValue(this.parameter);
        currentVal = (typeof currentVal === 'function') ? currentVal() : currentVal;

        var isSelected = false;

        var currentValArray = [];
        if (currentVal instanceof Array || (typeof (currentVal) === "object" && currentVal.join)) {
            currentValArray = currentVal;
        } else if (typeof (currentVal) === "string") {
            currentValArray = currentVal.split("|");
        }

        // check to see if current selected values are in the current values array. If not check to see if we should default to the first
        var vid = this.valueAsId === false ? 0 : 1;
        var hasCurrentVal = false;
        outer: for (var i = 0; i < currentValArray.length; i++) {
            for (var y = 0; y < myArray.length; y++) {
                if (currentValArray[i] === myArray[y][vid]) {
                    hasCurrentVal = true;
                    break outer;
                }
            }
        }
        // if there will be no selected value, but we're to default if empty, select the first
        if (!hasCurrentVal && this.defaultIfEmpty) {
            currentValArray = [myArray[0][vid]];

            this.currentVal = currentValArray;
            Dashboards.setParameter(this.parameter, currentValArray);
            Dashboards.processChange(this.name);
        }
        // (currentValArray === null && this.defaultIfEmpty)? firstVal : null


        selectHTML += "<ul class='" + ((this.verticalOrientation) ? "toggleGroup vertical" : "toggleGroup horizontal") + "'>"
        for (var i = 0, len = myArray.length; i < len; i++) {
            selectHTML += "<li class='" + ((this.verticalOrientation) ? "toggleGroup vertical" : "toggleGroup horizontal") + "'><label><input onclick='ToggleButtonBaseComponent.prototype.callAjaxAfterRender(\"" + this.name + "\")'";

            isSelected = false;
            for (var j = 0, valLength = currentValArray.length; j < valLength; j++) {
                isSelected = currentValArray[j] === myArray[i][vid];
                if (isSelected) {
                    break;
                }
            }

            if (this.type === 'radio' || this.type === 'radioComponent') {
                if ((i === 0 && !hasCurrentVal) ||
                    (hasCurrentVal && (myArray[i][vid] === currentVal))) {
                    selectHTML += " CHECKED";
                }
                selectHTML += " type='radio'";
            } else {
                if ((i === 0 && !hasCurrentVal && this.defaultIfEmpty) ||
                    (hasCurrentVal && isSelected)) {
                    selectHTML += " CHECKED";
                }
                selectHTML += " type='checkbox'";
            }
            selectHTML += "class='" + this.name + "' name='" + this.name + "' value='" + myArray[i][vid] + "' /> " + myArray[i][1] + "</label></li>" + ((this.separator === undefined || this.separator === null || this.separator === "null") ? "" : this.separator);
        }
        selectHTML += "</ul>"
        // update the placeholder
        $("#" + this.htmlObject).html(selectHTML);
        this.currentVal = null;
    },
    callAjaxAfterRender: function (name) {
        setTimeout(function () {
            Dashboards.processChange(name)
        }, 1);
    }
});

var RadioComponent = ToggleButtonBaseComponent.extend({
    getValue: function () {
        if (this.currentVal !== 'undefined' && this.currentVal !== null) {
            return this.currentVal;
        } else {
            return $("#" + this.htmlObject + " ." + this.name + ":checked").val()
        }
    }
});

var CheckComponent = ToggleButtonBaseComponent.extend({
    getValue: function () {
        if (this.currentVal !== 'undefined' && this.currentVal !== null) {
            return this.currentVal;
        } else {
            var a = [];
            $("#" + this.htmlObject + " ." + this.name + ":checked").each(function (i, val) {
                a.push($(this).val());
            });
            return a;
        }
    }
});

var MultiButtonComponent = ToggleButtonBaseComponent.extend({
    indexes: [], //used as static
    draw: function (myArray) {
        this.cachedArray = myArray;
        var cssWrapperClass = "pentaho-toggle-button pentaho-toggle-button-up " + ((this.verticalOrientation) ? "pentaho-toggle-button-vertical" : "pentaho-toggle-button-horizontal");
        var selectHTML = "";
        var firstVal;

        var valIdx = this.valueAsId ? 1 : 0;
        var lblIdx = 1;

        if (this.isMultiple === undefined) this.isMultiple = false;

        var ph = $("<div>");
        ph.appendTo($("#" + this.htmlObject).empty());
        for (var i = 0, len = myArray.length; i < len; i++) {
            var value = myArray[i][valIdx],
                label = myArray[i][lblIdx],
                classes = cssWrapperClass + this.getExtraCss(i, len, this.verticalOrientation),
                selector;

            value = (value === null ? null : value.replace('"', '&quot;'));
            label = (label === null ? null : label.replace('"', '&quot;'));

            if (i === 0) {
                firstVal = value;
            }

            selectHTML = "<div class='" + classes + "'><button name='" + this.name + "'>" + label + "</button  >" + "</div>";
            selector = $(selectHTML);
            // We wrap the click handler in a self-executing function so that we can capture 'i'.
            var myself = this;
            (function (index) {
                selector.click(function () {
                    MultiButtonComponent.prototype.clickButton(myself.htmlObject, myself.name, index, myself.isMultiple, myself.verticalOrientation);
                });
            }(i));
            ph.append(selector);
            if (!(this.separator === undefined || this.separator === null || this.separator === "null") && i !== myArray.length - 1) {
                ph.append(this.separator);
            }
        }


        //default
        var currentVal = Dashboards.ev(Dashboards.getParameterValue(this.parameter));

        var isSelected = false;

        var currentValArray;
        if (currentVal === null) {
            currentValArray = [];
        } else if (currentVal instanceof Array || (typeof (currentVal) === "object" && currentVal.join)) {
            currentValArray = currentVal;
        } else {
            currentValArray = currentVal.toString().split("|");
        }

        var foundDefault = false;
        this.clearSelections(this.htmlObject, this.name, this.verticalOrientation);
        for (var i = 0; i < myArray.length; i++) {

            isSelected = false;
            for (var j = 0, valLength = currentValArray.length; j < valLength; j++) {
                isSelected = currentValArray[j] === myArray[i][valIdx];
                if (isSelected) {
                    break;
                }
            }


            if (($.isArray(currentVal) && isSelected || isSelected) || (myArray[i][valIdx] === currentVal || myArray[i][lblIdx] === currentVal)) {

                MultiButtonComponent.prototype.clickButton(this.htmlObject, this.name, i, this.isMultiple, this.verticalOrientation, true);

                foundDefault = true;
                if (!this.isMultiple) {
                    break;
                }
            }
        }
        if (((!foundDefault && !this.isMultiple) || (!foundDefault && this.isMultiple && this.defaultIfEmpty)) && myArray.length > 0) {
            //select first value
            if ((currentVal === null || currentVal === "" || (typeof (currentVal) === "object" && currentVal.length === 0)) && this.parameter) {
                Dashboards.fireChange(this.parameter, (this.isMultiple) ? [firstVal] : firstVal);
            }

            MultiButtonComponent.prototype.clickButton(this.htmlObject, this.name, 0, this.isMultiple, this.verticalOrientation, true);
        }

        // set up hovering
        $(".pentaho-toggle-button").hover(function () {
            $(this).addClass("pentaho-toggle-button-up-hovering");
        }, function () {
            $(this).removeClass("pentaho-toggle-button-up-hovering");
        });
        // set up hovering when inner button is hovered
        $(".pentaho-toggle-button button").hover(function () {
            $(this).parent().addClass("pentaho-toggle-button-up-hovering");
        }, function () {
            // don't remove it, since it's inside the outer div it will handle that
        });

    },

    getValue: function () {
        if (this.isMultiple) {
            var indexes = MultiButtonComponent.prototype.getSelectedIndex(this.name);
            var a = [];
            // if it is not an array, handle that too
            if (indexes.length === undefined) {
                a.push(this.getValueByIdx(indexes));
            } else {
                for (var i = 0; i < indexes.length; i++) {
                    a.push(this.getValueByIdx(indexes[i]));
                }
            }
            return a;
        } else {
            return this.getValueByIdx(MultiButtonComponent.prototype.getSelectedIndex(this.name));
        }
    },

    getValueByIdx: function (idx) {
        return this.cachedArray[idx][this.valueAsId ? 1 : 0];
    },

    getSelecetedCss: function (verticalOrientation) {
        return "pentaho-toggle-button pentaho-toggle-button-down " + ((verticalOrientation) ? "pentaho-toggle-button-vertical" : "pentaho-toggle-button-horizontal");
    },
    getUnselectedCss: function (verticalOrientation) {
        return "pentaho-toggle-button pentaho-toggle-button-up " + ((verticalOrientation) ? "pentaho-toggle-button-vertical" : "pentaho-toggle-button-horizontal");
    },

    //static MultiButtonComponent.prototype.clickButton
    // This method should be broken up so the UI state code is reusable outside of event processing
    clickButton: function (htmlObject, name, index, isMultiple, verticalOrientation, updateUIOnly) {

        var cssWrapperClass = this.getUnselectedCss(verticalOrientation);
        var cssWrapperClassSelected = this.getSelecetedCss(verticalOrientation);

        var buttons = $("#" + htmlObject + " button");
        if (isMultiple) { //toggle button
            if (this.indexes[name] === undefined) this.indexes[name] = [];
            else if (!$.isArray(this.indexes[name])) this.indexes[name] = [this.indexes[name]]; //!isMultiple->isMultiple

            var disable = false;
            for (var i = 0; i < this.indexes[name].length; ++i) {
                if (this.indexes[name][i] === index) {
                    disable = true;
                    this.indexes[name].splice(i, 1);
                    break;
                }
            }
            if (disable) {
                buttons[index].parentNode.className = cssWrapperClass + this.getExtraCss(index, buttons.length, verticalOrientation);
            } else {
                buttons[index].parentNode.className = cssWrapperClassSelected + this.getExtraCss(index, buttons.length, verticalOrientation);
                this.indexes[name].push(index);
            }
        } else { //de-select old, select new
            this.clearSelections(htmlObject, name, verticalOrientation);
            this.indexes[name] = index;
            buttons[index].parentNode.className = cssWrapperClassSelected + this.getExtraCss(index, buttons.length, verticalOrientation);
        }
        if (!updateUIOnly) {
            this.callAjaxAfterRender(name);
        }
    },

    clearSelections: function (htmlObject, name, verticalOrientation) {
        var buttons = $("#" + htmlObject + " button");
        var cssWrapperClass = this.getUnselectedCss(verticalOrientation);
        for (var i = 0; i < buttons.length; i++) {
            buttons[i].parentNode.className = cssWrapperClass + this.getExtraCss(i, buttons.length, verticalOrientation);
        }

        this.indexes[name] = [];
    },

    getExtraCss: function (index, count, verticalOrientation) {
        var css = "";
        if (index === 0 && count === 1) {
            // both first & last
            return " pentaho-toggle-button-single";
        }
        if (index === 0) {
            css += " " + ((verticalOrientation) ? " pentaho-toggle-button-vertical-first" : " pentaho-toggle-button-horizontal-first");
        } else if (index === count - 1) {
            css += " " + ((verticalOrientation) ? " pentaho-toggle-button-vertical-last" : " pentaho-toggle-button-horizontal-last");
        }
        return css;
    },

    //static MultiButtonComponent.prototype.getSelectedIndex
    getSelectedIndex: function (name) {
        return this.indexes[name];
    }
});

var AutocompleteBoxComponent = BaseComponent.extend({
    searchedWord: '',
    result: [],

    queryServer: function (searchString) {

        if (!this.parameters) this.parameters = [];

        if (this.searchParam) {
            this.parameters = [
                [this.searchParam, this.getInnerParameterName()]
            ];
        } else if (this.parameters.length > 0) {
            this.parameters[0][1] = this.getInnerParameterName();
        }

        if (this.maxResults) {
            this.queryDefinition.pageSize = this.maxResults;
        }
        Dashboards.setParameter(this.getInnerParameterName(), this.getTextBoxValue());
        QueryComponent.makeQuery(this);
    },

    getTextBoxValue: function () {
        return this.textbox.val();
    },

    getInnerParameterName: function () {
        return this.parameter + '_textboxValue';
    },

    update: function () {

        $("#" + this.htmlObject).empty();

        var initialValue = null;
        if (this.parameter) {
            initialValue = Dashboards.getParameterValue(this.parameter);
        }

        var myself = this;

        //init parameter
        // TODO: FIXME: dcl - Isn't the following
        // this.getInnerParameterName missing parentesis?
        if (!Dashboards.getParameterValue(this.getInnerParameterName)) {
            Dashboards.setParameter(this.getInnerParameterName(), '');
        }

        var processChange = myself.processChange === undefined ? function (objName) {
                Dashboards.processChange(objName);
            } : function (objName) {
                myself.processChange();
            };
        var processElementChange = myself.processElementChange === true ? function (value) {
                Dashboards.fireChange(myself.parameter, value);
            } : undefined;

        //TODO:typo on minTextLength
        if (this.minTextLenght === undefined) {
            this.minTextLenght = 0;
        }

        var opt = {
            list: function () {
                var val = myself.textbox.val();
                if (val.length >= myself.minTextLenght && !(val === '' //nothing to search
                    ||
                    val === myself.searchedWord ||
                    ((myself.queryInfo !== null && myself.result.length === myself.queryInfo.totalRows) && //has all results
                        myself.searchedWord !== '' &&
                        ((myself.matchType === "fromStart") ?
                            val.indexOf(myself.searchedWord) === 0 :
                            val.indexOf(myself.searchedWord) > -1)))) //searchable in local results
                {
                    myself.queryServer(val);
                    myself.searchedWord = val;
                }
                var list = [];
                for (p in myself.result)
                    if (myself.result.hasOwnProperty(p)) {
                        var obj = {};
                        obj.text = myself.result[p][0];
                        list.push(obj);
                    }
                return list;
            },
            matchType: myself.matchType === undefined ? "fromStart" : myself.matchType,
            /*fromStart,all*/
            processElementChange: processElementChange,
            processChange: function (obj, value) {
                obj.value = value;
                processChange(obj.name);
            },
            multiSelection: myself.selectMulti === undefined ? false : myself.selectMulti,
            checkValue: myself.checkValue === undefined ? true : myself.checkValue,
            minTextLenght: myself.minTextLenght === undefined ? 0 : myself.minTextLenght,
            scrollHeight: myself.scrollHeight,
            applyButton: myself.showApplyButton === undefined ? true : myself.showApplyButton,
            tooltipMessage: myself.tooltipMessage === undefined ? "Click it to Apply" : myself.tooltipMessage,
            addTextElements: myself.addTextElements === undefined ? true : myself.addTextElements,
            externalApplyButtonId: myself.externalApplyButtonId,
            //    selectedValues: initialValue,
            parent: myself
        };


        this.autoBoxOpt = $("#" + this.htmlObject).autobox(opt);

        //setInitialValue
        this.autoBoxOpt.setInitialValue(this.htmlObject, initialValue, this.name);

        this.textbox = $('#' + this.htmlObject + ' input');
    },
    getValue: function () {
        return this.value;
    },
    processAutoBoxChange: function () {
        this.autoBoxOpt.processAutoBoxChange();
    }
});

var ButtonComponent = BaseComponent.extend({
    update: function () {
        var myself = this;
        var b = $("<button type='button'/>").text(this.label).unbind("click").bind("click", function () {
            return myself.expression.apply(myself, arguments);
        });
        if (typeof this.buttonStyle === "undefined" || this.buttonStyle === "themeroller")
            b.button();
        b.appendTo($("#" + this.htmlObject).empty());
    }
});

 /* ************************ new file ************************ */
/*
 * Function: fnLengthChange
 * Purpose:  Change the number of records on display
 * Returns:  array:
 * Inputs:   object:oSettings - DataTables settings object
 *           int:iDisplay - New display length
 */
// Ensure we load dataTables before this line. If not, just keep going
if ($.fn.dataTableExt !== undefined) {
    $.fn.dataTableExt.oApi.fnLengthChange = function (oSettings, iDisplay) {
        oSettings._iDisplayLength = iDisplay;
        oSettings.oApi._fnCalculateEnd(oSettings);

        // If we have space to show extra rows backing up from the end point - then do so
        if (oSettings._iDisplayEnd === oSettings.aiDisplay.length) {
            oSettings._iDisplayStart = oSettings._iDisplayEnd - oSettings._iDisplayLength;
            if (oSettings._iDisplayStart < 0) {
                oSettings._iDisplayStart = 0;
            }
        }

        if (oSettings._iDisplayLength === -1) {
            oSettings._iDisplayStart = 0;
        }

        oSettings.oApi._fnDraw(oSettings);

        $('select', oSettings.oFeatures.l).val(iDisplay);
    };
    /* Example
     * $(document).ready(function() {
     *    var oTable = $('#example').dataTable();
     *    oTable.fnLengthChange( 100 );
     * } );
     */
}

var TableComponent = UnmanagedComponent.extend({
        ph: undefined,

        update: function () {
            if (!this.preExec()) {
                return;
            }
            if (!this.htmlObject) {
                return this.error("TableComponent requires an htmlObject");
            }
            try {
                this.block();
                this.setup();
                if (this.chartDefinition.paginateServerside) {
                    this.paginatingUpdate();
                } else {
                    /* The non-paging query handler only needs to concern itself
                     * with handling postFetch and calling the draw function
                     */
                    var success = _.bind(function (data) {
                        this.rawData = data;
                        this.processTableComponentResponse(data)
                    }, this);
                    var handler = this.getSuccessHandler(success);

                    this.queryState.setAjaxOptions({
                        async: true
                    });
                    this.queryState.fetchData(this.parameters, handler);
                }
            } catch (e) {
                /*
                 * Something went wrong and we won't have handlers firing in the future
                 * that will trigger unblock, meaning we need to trigger unblock manually.
                 */
                this.dashboard.error(e);
                this.unblock();
            }
        },

        paginatingUpdate: function () {
            var cd = this.chartDefinition;
            this.extraOptions = this.extraOptions || [];
            this.extraOptions.push(["bServerSide", true]);
            this.extraOptions.push(["bProcessing", true]);
            this.queryState.setPageSize(parseInt(cd.displayLength || 10));
            this.queryState.setCallback(_.bind(function (values) {
                changedValues = undefined;
                if ((typeof (this.postFetch) === 'function')) {
                    changedValues = this.postFetch(values);
                }
                if (changedValues !== undefined) {
                    values = changedValues;
                }
                this.processTableComponentResponse(values);
            }, this));
            this.queryState.setParameters(this.parameters);
            this.queryState.setAjaxOptions({
                async: true
            });
            this.processTableComponentResponse();
        },

        /* Initial setup: clearing out the htmlObject and building the query object */
        setup: function () {
            var cd = this.chartDefinition;
            if (cd === undefined) {
                Dashboards.log("Fatal - No chart definition passed", "error");
                return;
            }
            cd["tableId"] = this.htmlObject + "Table";

            // Clear previous table
            this.ph = $("#" + this.htmlObject).empty();
            // remove drawCallback from the parameters, or
            // it'll be called before we have an actual table...
            var croppedCd = $.extend({}, cd);
            croppedCd.drawCallback = undefined;
            this.queryState = Dashboards.getQuery(croppedCd);
            this.query = this.queryState; // for analogy with ccc component's name
            // make sure to clean sort options
            var sortBy = this.chartDefinition.sortBy || [],
                sortOptions = [];
            for (var i = 0; i < sortBy.length; i++) {
                var col = sortBy[i][0];
                var dir = sortBy[i][1];
                sortOptions.push(col + (dir === "asc" ? "A" : "D"));
            }
            this.queryState.setSortBy(sortOptions);
        },

        pagingCallback: function (url, params, callback, dataTable) {
            function p(sKey) {
                for (var i = 0, iLen = params.length; i < iLen; i++) {
                    if (params[i].name === sKey) {
                        return params[i].value;
                    }
                }
                return null;
            }

            var sortingCols = p("iSortingCols"),
                sort = [];
            if (sortingCols > 0) {
                for (var i = 0; i < sortingCols; i++) {
                    var col = p("iSortCol_" + i);
                    var dir = p("sSortDir_" + i);
                    sort.push(col + (dir === "asc" ? "A" : "D"));
                }
            }
            var query = this.queryState,
                myself = this;
            query.setSortBy(sort.join(","));
            query.setPageSize(parseInt(p("iDisplayLength")));
            query.setPageStartingAt(p("iDisplayStart"));
            query.setSearchPattern(p("sSearch") ? p("sSearch") : "");
            query.fetchData(function (d) {
                if (myself.postFetch) {
                    var mod = myself.postFetch(d, dataTable);
                    if (typeof mod !== "undefined") {
                        d = mod;
                    }
                }
                var response = {
                    iTotalRecords: d.queryInfo.totalRows,
                    iTotalDisplayRecords: d.queryInfo.totalRows
                };
                response.aaData = d.resultset;
                response.sEcho = p("sEcho");
                myself.rawData = d;
                callback(response);
            });
        },

        /*
         * Callback for when the table is finished drawing. Called every time there
         * is a redraw event (so not only updates, but also pagination and sorting).
         * We handle addIns and such things in here.
         */
        fnDrawCallback: function (dataTableSettings) {
            var dataTable = dataTableSettings.oInstance,
                cd = this.chartDefinition,
                myself = this,
                handleAddIns = _.bind(this.handleAddIns, this);
            this.ph.find("tbody tr").each(function (row, tr) {
                /*
                 * Reject rows that are not actually part
                 * of the datatable (e.g. nested tables)
                 */
                if (dataTable.fnGetPosition(tr) === null) {
                    return true;
                }

                $(tr).children("td").each(function (col, td) {

                    var foundAddIn = handleAddIns(dataTable, td);
                    /* 
                     * Process column format for those columns
                     * where we didn't find a matching addIn
                     */
                    if (!foundAddIn && cd.colFormats) {
                        var position = dataTable.fnGetPosition(td),
                            rowIdx = position[0],
                            colIdx = position[2],
                            format = cd.colFormats[colIdx],
                            value = myself.rawData.resultset[rowIdx][colIdx];
                        if (format && (typeof value !== "undefined" && value !== null)) {
                            $(td).text(sprintf(format, value));
                        }
                    }
                });
            });

            /* Old urlTemplate code. This needs to be here for backward compatibility */
            if (cd.urlTemplate !== undefined) {
                var td = $("#" + myself.htmlObject + " td:nth-child(1)");
                td.addClass('cdfClickable');
                td.bind("click", function (e) {
                    var regex = new RegExp("{" + cd.parameterName + "}", "g");
                    var f = cd.urlTemplate.replace(regex, $(this).text());
                    eval(f);
                });
            }
            /* Handle post-draw callback the user might have provided */
            if (typeof cd.drawCallback === 'function') {
                cd.drawCallback.apply(myself, arguments);
            }
        },

        /*
         * Handler for when the table finishes initialising. This only happens once,
         * when the table *initialises* ,as opposed to every time the table is drawn,
         * so it provides us with a good place to add the postExec callback.
         */
        fnInitComplete: function () {
            this.postExec();
            this.unblock();
        },

        /*
         * Resolve and call addIns for the given td in the context of the given
         * dataTable. Returns true if there was an addIn and it was successfully
         * called, or false otherwise.
         */
        handleAddIns: function (dataTable, td) {
            var cd = this.chartDefinition,
                position = dataTable.fnGetPosition(td),
                rowIdx = position[0],
                colIdx = position[2],
                colType = cd.colTypes[colIdx],
                addIn = this.getAddIn("colType", colType),
                state = {},
                target = $(td),
                results = this.rawData;
            if (!addIn) {
                return false;
            }
            try {
                if (!(target.parents('tbody').length)) {
                    return;
                } else if (target.get(0).tagName !== 'TD') {
                    target = target.closest('td');
                }
                state.rawData = results;
                state.tableData = dataTable.fnGetData();
                state.colIdx = colIdx;
                state.rowIdx = rowIdx;
                state.series = results.resultset[state.rowIdx][0];
                state.category = results.metadata[state.colIdx].colName;
                state.value = results.resultset[state.rowIdx][state.colIdx];
                if (cd.colFormats) {
                    state.colFormat = cd.colFormats[state.colIdx];
                }
                state.target = target;
                addIn.call(td, state, this.getAddInOptions("colType", addIn.getName()));
                return true;
            } catch (e) {
                this.dashboard.error(e);
                return false;
            }
        },

        processTableComponentResponse: function (json) {
            var myself = this,
                cd = this.chartDefinition,
                extraOptions = {};

            this.ph.trigger('cdfTableComponentProcessResponse');

            // Set defaults for headers / types
            if (typeof cd.colHeaders === "undefined" || cd.colHeaders.length === 0)
                cd.colHeaders = json.metadata.map(function (i) {
                    return i.colName
                });

            if (typeof cd.colTypes === "undefined" || cd.colTypes.length === 0)
                cd.colTypes = json.metadata.map(function (i) {
                    return i.colType.toLowerCase()
                });

            var dtData0 = TableComponent.getDataTableOptions(cd);

            // Build a default config from the standard options
            $.each(this.extraOptions ? this.extraOptions : {}, function (i, e) {
                extraOptions[e[0]] = e[1];
            });
            var dtData = $.extend(cd.dataTableOptions, dtData0, extraOptions);


            /* Configure the table event handlers */
            dtData.fnDrawCallback = _.bind(this.fnDrawCallback, this);
            dtData.fnInitComplete = _.bind(this.fnInitComplete, this);
            /* fnServerData is required for server-side pagination */
            if (dtData.bServerSide) {
                var myself = this;
                dtData.fnServerData = function (u, p, c) {
                    myself.pagingCallback(u, p, c, this);
                };
            }

            /* We need to make sure we're getting data from the right place,
             * depending on whether we're using CDA
             */
            if (json) {
                dtData.aaData = json.resultset;
            }

            this.ph.html("<table id='" + this.htmlObject + "Table' class='tableComponent' width='100%'></table>");
            /*
             * We'll first initialize a blank table so that we have a
             * table handle to work with while the table is redrawing
             */
            this.dataTable = $("#" + this.htmlObject + 'Table').dataTable(dtData);

            // We'll create an Array to keep track of the open expandable rows.
            this.dataTable.anOpen = [];


            myself.ph.find('table').bind('click', function (e) {
                if (typeof cd.clickAction === 'function' || myself.expandOnClick) {
                    var state = {},
                        target = $(e.target),
                        results = myself.rawData;
                    if (!(target.parents('tbody').length)) {
                        return;
                    } else if (target.get(0).tagName !== 'TD') {
                        target = target.closest('td');
                    }
                    var position = myself.dataTable.fnGetPosition(target.get(0));
                    state.rawData = myself.rawData;
                    state.tableData = myself.dataTable.fnGetData();
                    state.colIdx = position[2];
                    state.rowIdx = position[0];
                    state.series = results.resultset[state.rowIdx][0];

                    state.category = results.metadata[state.colIdx].colName;
                    state.value = results.resultset[state.rowIdx][state.colIdx];
                    state.colFormat = cd.colFormats[state.colIdx];


                    state.target = target;


                    if (myself.expandOnClick) {
                        myself.handleExpandOnClick(state);
                    }
                    if (cd.clickAction) {
                        cd.clickAction.call(myself, state);
                    }
                }
            });
            myself.ph.trigger('cdfTableComponentFinishRendering');
        },

        handleExpandOnClick: function (event) {
            var myself = this,
                detailContainerObj = myself.expandContainerObject,
                activeclass = "expandingClass";

            if (typeof activeclass === 'undefined') {
                activeclass = "activeRow";
            }

            var obj = event.target.closest("tr"),
                a = event.target.closest("a");

            if (a.hasClass('info')) {
                return;
            } else {
                var row = obj.get(0),
                    value = event.series,
                    htmlContent = $("#" + detailContainerObj).html(),
                    anOpen = myself.dataTable.anOpen,
                    i = $.inArray(row, anOpen);

                if (obj.hasClass(activeclass)) {
                    obj.removeClass(activeclass);
                    myself.dataTable.fnClose(row);
                    anOpen.splice(i, 1);

                } else {
                    // Closes all open expandable rows .
                    for (var j = 0; j < anOpen.length; j++) {
                        $(anOpen[j]).removeClass(activeclass);
                        myself.dataTable.fnClose(anOpen[j]);
                        anOpen.splice(j, 1);
                    }
                    obj.addClass(activeclass);

                    anOpen.push(row);
                    // Since the switch to async, we need to open it first
                    myself.dataTable.fnOpen(row, htmlContent, activeclass);

                    //Read parameters and fire changes
                    var results = myself.queryState.lastResults();
                    $(myself.expandParameters).each(function f(i, elt) {
                        Dashboards.fireChange(elt[1], results.resultset[event.rowIdx][parseInt(elt[0], 10)]);
                    });

                };
            };
            $("td.expandingClass").click(
                function (event) {
                    //Does nothing but it prevents problems on expandingClass clicks!
                    event.stopPropagation();
                    return;
                }
            );
        }
    },

    {
        getDataTableOptions: function (options) {
            var dtData = {};

            if (options.tableStyle === "themeroller") {
                dtData.bJQueryUI = true;
            }
            dtData.bInfo = options.info;
            dtData.iDisplayLength = options.displayLength;
            dtData.bLengthChange = options.lengthChange;
            dtData.bPaginate = options.paginate;
            dtData.bSort = options.sort;
            dtData.bFilter = options.filter;
            dtData.sPaginationType = options.paginationType;
            dtData.sDom = options.sDom;
            dtData.aaSorting = options.sortBy;

            if (typeof options.oLanguage === "string") {
                dtData.oLanguage = eval("(" + options.oLanguage + ")"); //TODO: er...
            } else {
                dtData.oLanguage = options.oLanguage;
            }

            if (options.colHeaders !== undefined) {
                dtData.aoColumns = new Array(options.colHeaders.length);
                for (var i = 0; i < options.colHeaders.length; i++) {
                    dtData.aoColumns[i] = {}
                    dtData.aoColumns[i].sClass = "column" + i;
                };
                $.each(options.colHeaders, function (i, val) {
                    dtData.aoColumns[i].sTitle = val;
                    if (val === "") dtData.aoColumns[i].bVisible = false;
                }); // colHeaders
                if (options.colTypes !== undefined) {
                    $.each(options.colTypes, function (i, val) {
                        var col = dtData.aoColumns[i];
                        // Specific case: hidden cols
                        if (val === "hidden") col.bVisible = false;
                        col.sClass += " " + val;
                        col.sType = val;

                    })
                }; // colTypes
                if (options.colFormats !== undefined) {
                    // Changes are made directly to the json

                }; // colFormats

                var bAutoWidth = true;
                if (options.colWidths !== undefined) {
                    $.each(options.colWidths, function (i, val) {
                        if (val !== null) {
                            dtData.aoColumns[i].sWidth = val;
                            bAutoWidth = false;
                        }
                    })
                }; //colWidths
                dtData.bAutoWidth = bAutoWidth;

                if (options.colSortable !== undefined) {
                    $.each(options.colSortable, function (i, val) {
                        if (val !== null && (!val || val === "false")) {
                            dtData.aoColumns[i].bSortable = false
                        }
                    })
                }; //colSortable
                if (options.colSearchable !== undefined) {
                    $.each(options.colSearchable, function (i, val) {
                        if (val !== null && (!val || val === "false")) {
                            dtData.aoColumns[i].bSearchable = false
                        }
                    })
                }; //colSearchable

            }

            return dtData;
        }
    });

 /* ************************ new file ************************ */
/**
 * Creates a new AddIn.
 *
 * The options parameter needs a label and name member, and must have
 * either a value (for static Add Ins) or implementation member (for
 * scriptable Add Ins). Should the AddIn support configuration, then
 * there should also be an options.defaults member containing the
 * default values for the configurable settings.
 *
 * @class AddIns come in two varieties: Static AddIns
 * represent static data or behaviour, whereas Scriptable AddIns
 * represent dynamic, context-dependent behaviour.
 *
 * @property {String} label  The AddIn's human-readable label. (read only)
 * @property {String} name The internal identifier for the AddIn. (read only)
 * @parameters options {Object} The options for the AddIn.
 */

function AddIn(options) {

    var myself = options;
    if (typeof options !== "object") {
        throw TypeError;
    }
    /* We expect either an implementation or a value. */
    if (!options.label || !options.name || (!options.implementation && !options.value)) {
        throw TypeError;
    }
    var _name = options.name,
        _label = options.label,
        _type = options.implementation ? "scriptable" : "static",
        /* It's OK if any of these ends up being undefined */
        _implementation = options.implementation,
        _defaults = options.defaults,
        _value = options.options;

    /* Do we have an init method? Call it now */
    if (typeof options.init === 'function') {
        options.init.call(myself);
    }

    this.getLabel = function () {
        return _label;
    };
    this.getName = function () {
        return _name;
    };

    /**
     * Call the AddIn. If the AddIn is static, all parameters are
     * irrelevant, and this method will simply return the value.
     *
     * In a dynamic AddIn, the implementation will be passed the
     * the target DOM Element (whatever element is relevant,
     * e.g. the element that was clicked on, or the table cell
     * that's being processed), a state object with whatever
     * context is relevant for the AddIn to fulfill its purpose,
     * and optionally any overriding options.
     *
     * Components are allowed to pass undefined as the target if
     * no Elements make sense in context, and
     *
     * @parameter target {Element} The relevant DOM Element.
     * @parameter state {Object} A representation of the necessary
     * context for the AddIn to operate.
     * @parameter options {Object} Configuration options for the AddIn
     */
    this.call = function (target, state, options) {
        if (!_implementation) {
            return Dashboards.clone(_value);
        }
        options = typeof options === "function" ? options(state) : options;
        var evaluatedDefaults = typeof _defaults === "function" ? _defaults(state) : _defaults;
        var compiledOptions = jQuery.extend(true, {}, evaluatedDefaults, options);
        try {
            return _implementation.call(myself, target, state, compiledOptions);
        } catch (e) {
            Dashboards.log("Addin Error [" + this.getName() + "]: " + e, "error");
        }
    };

    this.setDefaults = function (defaults) {

        if (typeof defaults === 'function') {
            _defaults = defaults;
        } else {
            _defaults = jQuery.extend(true, {}, _defaults, defaults);
        }
    };
}

 /* ************************ new file ************************ */
(function () {

    /* Sparkline AddIn, based on jquery.sparkline.js sparklines.
     *
     */
    var sparkline = {
        name: "sparkline",
        label: "Sparkline",
        defaults: {
            type: 'line'
        },
        init: function () {

            // Register this for datatables sort
            var myself = this;
            $.fn.dataTableExt.oSort[this.name + '-asc'] = function (a, b) {
                return myself.sort(a, b);
            };
            $.fn.dataTableExt.oSort[this.name + '-desc'] = function (a, b) {
                return myself.sort(b, a);
            };

        },

        sort: function (a, b) {
            return this.sumStrArray(a) - this.sumStrArray(b);
        },

        sumStrArray: function (arr) {
            return arr.split(',').reduce(function (prev, curr, index, array) {
                Dashboards.log("Current " + curr + "; prev " + prev);
                return parseFloat(curr) + (typeof (prev) === 'number' ? prev : parseFloat(prev));
            });
        },

        implementation: function (tgt, st, opt) {
            var t = $(tgt);
            t.sparkline(st.value.split(/,/), opt);
            t.removeClass("sparkline");
        }
    };
    Dashboards.registerAddIn("Table", "colType", new AddIn(sparkline));

    var link = {
        name: "hyperlink",
        label: "Hyperlink",
        defaults: {
            openInNewTab: true,
            prependHttpIfNeeded: true,
            regexp: null,
            pattern: null,
            urlReference: 2,
            labelReference: 1
        },

        init: function () {
            $.fn.dataTableExt.oSort[this.name + '-asc'] = $.fn.dataTableExt.oSort['string-asc'];
            $.fn.dataTableExt.oSort[this.name + '-desc'] = $.fn.dataTableExt.oSort['string-desc'];
        },

        implementation: function (tgt, st, opt) {
            var ph = $(tgt);
            var link, label;
            if (opt.pattern) {
                var re = new RegExp(opt.pattern),
                    results = re.exec(st.value);
                link = results[opt.urlReference];
                label = results[opt.labelReference];
            } else {
                link = st.value;
                label = st.value;
            }
            if (opt.prependHttpIfNeeded && !/^https?:\/\//.test(link)) {
                link = "http://" + link;
            }
            // is this text an hyperlink? 
            if (opt.regexp === null || (new RegExp(opt.regexp).test(st.value))) {
                var a = $("<a></a>").attr("href", link).addClass("hyperlinkAddIn");
                a.text(label);
                if (opt.openInNewTab) {
                    a.attr("target", "_blank");
                }
                ph.empty().append(a);
            }
        }

    };
    Dashboards.registerAddIn("Table", "colType", new AddIn(link));

    var formattedText = {
        name: "formattedText",
        label: "Formatted Text",
        defaults: {
            textFormat: function (v, st) {
                return st.colFormat ? sprintf(st.colFormat, v) : v;
            }
        },

        init: function () {
            $.fn.dataTableExt.oSort[this.name + '-asc'] = $.fn.dataTableExt.oSort['string-asc'];
            $.fn.dataTableExt.oSort[this.name + '-desc'] = $.fn.dataTableExt.oSort['string-desc'];
        },

        implementation: function (tgt, st, opt) {
            var text = opt.textFormat.call(this, st.value, st, opt);
            $(tgt).empty().append(text);
        }

    };
    Dashboards.registerAddIn("Table", "colType", new AddIn(formattedText));

    var localizedText = {
        name: "localizedText",
        label: "Localized Text",
        defaults: {
            localize: function (v) {
                return Dashboards.i18nSupport.prop(v);
            }
        },

        init: function () {
            $.fn.dataTableExt.oSort[this.name + '-asc'] = $.fn.dataTableExt.oSort['string-asc'];
            $.fn.dataTableExt.oSort[this.name + '-desc'] = $.fn.dataTableExt.oSort['string-desc'];
        },

        implementation: function (tgt, st, opt) {
            if (typeof Dashboards.i18nSupport !== "undefined" && Dashboards.i18nSupport !== null) {
                var text = this.defaults.localize(st.value);
                $(tgt).empty().append(text);
                //change data, too, in order for search and sorting to work correctly on the localized text
                st.tableData[st.rowIdx][st.colIdx] = text;
            }
        }

    };
    Dashboards.registerAddIn("Table", "colType", new AddIn(localizedText));
})();

 /* ************************ new file ************************ */
/*
 * queryTypes.js
 *
 * Registers several query types and sets the base query class.
 *
 * Additional query types can be registered at any time using the Dashboards method:
 *    Dashboards.registerQuery( name, query )
 * The second argument, the query definition, can be one of two things:
 *    1. An object, which will be used to extend the BaseQuery class, and the resulting class used
 *       to create new query instances.
 *    2. A class constructor function, which will be used directly to create new query instances
 *       without depending on BaseQuery.
 *
 * Additionally, the BaseQuery class can be set to something other than the default by using:
 *    Dashboards.setBaseQuery( constructor )
 * but this is a risky operation which considerable implications. Use at your own risk!
 *
 */
(function () {
    var BaseQuery = Base.extend({
        name: "baseQuery",
        label: "Base Query",
        deepProperties: ['defaults', 'interfaces'],
        defaults: {
            successCallback: function () {
                Dashboards.log('Query callback not defined. Override.');
            },
            errorCallback: Dashboards.handleServerError,
            lastResultSet: null,
            page: 0,
            pageSize: 0,
            params: {},
            ajaxOptions: {
                async: false,
                type: 'POST'
            },
            url: ''
        },
        interfaces: {
            params: {
                reader: 'propertiesObject',
                validator: 'isObjectOrPropertiesArray'
            },
            successCallback: {
                validator: 'isFunction'
            },
            errorCallback: {
                validator: 'isFunction'
            },
            pageSize: {
                validator: 'isPositive'
            }

        },
        constructor: function (config) {
            if (Dashboards && Dashboards.OptionsManager) {
                this._optionsManager = new Dashboards.OptionsManager(this);
                this._optionsManager.mixin(this);
            }
            this.init(config);
        },
        // Default options interface in case there is no options manager defined.
        getOption: function (prop) {
            // Fallback for when Dashboards.OptionManager is not available
            return this.defaults[prop];
        },
        setOption: function (prop, value) {
            // Fallback for when Dashboards.OptionManager is not available
            this.defaults[prop] = value;
        },
        init: function (opts) {
            // Override
        },
        getSuccessHandler: function (callback) {
            var myself = this;
            return function (json) {
                // TODO - changed this in order to make the response a JSON object
                // var JSONresponse = $.parseJSON(json
                myself.setOption('lastResultSet', json);
                var clone = $.extend(true, {}, myself.getOption('lastResultSet'));
                callback(clone);
            };
        },
        getErrorHandler: function (callback) {
            return function (resp, txtStatus, error) {
                if (callback) {
                    callback(resp, txtStatus, error);
                }
            };
        },
        doQuery: function (outsideCallback) {
            if (typeof this.getOption('successCallback') !== 'function') {
                throw 'QueryNotInitialized';
            }
            var url = this.getOption('url'),
                callback = (outsideCallback ? outsideCallback : this.getOption('successCallback')),
                errorCallback = this.getOption('errorCallback'),
                queryDefinition = this.buildQueryDefinition();

            var settings = _.extend({}, this.getOption('ajaxOptions'), {
                data: queryDefinition,
                url: url,
                success: this.getSuccessHandler(callback),
                error: this.getErrorHandler(errorCallback)
            });

            $.ajax(settings);
        },
        exportData: function () {
            // Override 
        },
        setAjaxOptions: function (newOptions) {
            this.setOption('ajaxOptions', _.extend({}, this.getOption('ajaxOptions'), newOptions));
        },
        setSortBy: function (sortBy) {
            // Override 
        },
        sortBy: function (sortBy, outsideCallback) {
            // Override 
        },
        fetchData: function (params, successCallback, errorCallback) {
            switch (arguments.length) {
            case 0:
                if (this.getOption('params') && this.getOption('successCallback')) {
                    return this.doQuery();
                }
                break;
            case 1:
                if (typeof arguments[0] === "function") {
                    /* If we're receiving _only_ the callback, we're not
                     * going to change the internal callback
                     */
                    return this.doQuery(arguments[0]);
                } else if (!_.isEmpty(arguments[0]) &&
                    (_.isObject(arguments[0]) || _.isArray(arguments[0]))) {
                    this.setOption('params', arguments[0] || {});
                    return this.doQuery();
                }
                break;
            case 2:
                if (typeof arguments[0] === "function") {
                    this.setOption('successCallback', arguments[0]);
                    this.setOption('errorCallback', arguments[1]);
                    return this.doQuery();
                } else {
                    this.setOption('params', arguments[0] || {});
                    this.setOption('successCallback', arguments[1]);
                    return this.doQuery();
                }
                break;
            default:
                /* We're just going to discard anything over two params */
                this.setOption('params', params);
                this.setOption('successCallback', successCallback);
                this.setOption('errorCallback', errorCallback);
                return this.doQuery();
            }
            /* If we haven't hit a return by this time,
             * the user gave us some wrong input
             */
            throw "InvalidInput";
        },

        // Result caching
        lastResults: function () {
            if (this.getOption('lastResultSet') !== null) {
                return $.extend(true, {}, this.getOption('lastResultSet'));
            } else {
                throw "NoCachedResults";
            }
        },
        reprocessLastResults: function (outerCallback) {
            if (this.getOption('lastResultSet') !== null) {
                var clone = $.extend(true, {}, this.getOption('lastResultSet'));
                var callback = outerCallback || this.getOption('successCallback');
                return callback(clone);
            } else {
                throw "NoCachedResults";
            }
        },
        reprocessResults: function (outsideCallback) {
            if (this.getOption('lastResultSet') !== null) {
                var clone = $.extend(true, {}, this.getOption('lastResultSet'));
                var callback = (outsideCallback ? outsideCallback : this.getOption('successCallback'));
                callback(clone);
            } else {
                throw "NoCachedResults";
            }
        },
        setParameters: function (params) {
            this.setOption('params', params);
        },
        setCallback: function (callback) {
            this.setOption('successCallback', callback);
        },
        setErrorCallback: function (callback) {
            this.setOption('errorCallback', callback);
        },
        setSearchPattern: function (pattern) {
            this.setOption('searchPattern', pattern);
        },

        /* Pagination
         *
         * We paginate by having an initial position ( page ) and page size ( pageSize )
         * Paginating consists of incrementing/decrementing the initial position by
         * the page size. All paging operations change the paging cursor.
         */

        // Gets the next _pageSize results
        nextPage: function (outsideCallback) {
            var page = this.getOption('page'),
                pageSize = this.getOption('pageSize');
            if (pageSize > 0) {
                page += pageSize;
                this.setOption('page', page);
                return this.doQuery(outsideCallback);
            } else {
                throw "InvalidPageSize";
            }
        },
        // Gets the previous _pageSize results
        previousPage: function (outsideCallback) {
            var page = this.getOption('page'),
                pageSize = this.getOption('pageSize');
            if (page > pageSize) {
                page -= pageSize;
                this.setOption('page', page);
                return this.doQuery(outsideCallback);
            } else if (_pageSize > 0) {
                this.setOption('page', 0);
                return this.doQuery(outsideCallback);
            } else {
                throw "AtBeggining";
            }
        },
        // Gets the page-th set of _pageSize results (0-indexed)
        getPage: function (targetPage, outsideCallback) {
            var page = this.getOption('page'),
                pageSize = this.getOption('pageSize');
            if (targetPage * pageSize === page) {
                return false;
            } else if (typeof targetPage === 'number' && targetPage >= 0) {
                this.setOption('page', targetPage * pageSize);
                return this.doQuery(outsideCallback);
            } else {
                throw "InvalidPage";
            }
        },

        // Gets pageSize results starting at page
        setPageStartingAt: function (targetPage) {
            if (targetPage === this.getOption('page')) {
                return false;
            } else if (typeof targetPage === 'number' && targetPage >= 0) {
                this.setOption('page', targetPage);
            } else {
                throw "InvalidPage";
            }
        },

        pageStartingAt: function (page, outsideCallback) {
            if (this.setPageStartingAt(page) !== false) {
                return this.doQuery(outsideCallback);
            } else {
                return false;
            }
        },

        // Sets the page size
        setPageSize: function (pageSize) {
            this.setOption('pageSize', pageSize);
        },

        // sets _pageSize to pageSize, and gets the first page of results
        initPage: function (pageSize, outsideCallback) {
            if (pageSize === this.getOption('pageSize') && this.getOption('page') === 0) {
                return false;
            } else if (typeof pageSize === 'number' && pageSize > 0) {
                this.setOption('page', 0);
                this.setOption('pageSize', pageSize);
                return this.doQuery(outsideCallback);
            } else {
                throw "InvalidPageSize";
            }
        }
    });
    // Sets the query class that can extended to create new ones. 
    // The registered Base needs to have an extend method.
    Dashboards.setBaseQuery(BaseQuery);

    var cdaQueryOpts = {
        name: 'cda',
        label: 'CDA Query',
        defaults: {
            url: '/cda/doQuery',
            file: '',
            id: '',
            outputIdx: '1',
            sortBy: '',
            ajaxOptions: {},
            searchPattern: ''
        },

        init: function (opts) {
            if (typeof opts.path !== 'undefined' && typeof opts.dataAccessId !== 'undefined') {
                // CDA-style cd object
                this.setOption('file', opts.path);
                this.setOption('id', opts.dataAccessId);
                if (typeof opts.sortBy === 'string' && opts.sortBy.match("^(?:[0-9]+[adAD]?,?)*$")) {
                    this.setOption('sortBy', opts.sortBy);
                }
                if (opts.pageSize !== null) {
                    this.setOption('pageSize', opts.pageSize);
                }
                if (opts.outputIndexId !== null) {
                    this.setOption('outputIdx', opts.outputIndexId);
                }
            } else {
                throw 'InvalidQuery';
            }
        },

        buildQueryDefinition: function (overrides) {
            overrides = (overrides instanceof Array) ? Dashboards.propertiesArrayToObject(overrides) : (overrides || {});
            var queryDefinition = {};

            var cachedParams = this.getOption('params'),
                params = $.extend({}, cachedParams, overrides);

            _.each(params, function (value, name) {
                value = Dashboards.getParameterValue(value);
                if ($.isArray(value) && value.length === 1 && ('' + value[0]).indexOf(';') >= 0) {
                    //special case where single element will wrongly be treated as a parseable array by cda
                    value = doCsvQuoting(value[0], ';');
                }
                //else will not be correctly handled for functions that return arrays
                if (typeof value === 'function') {
                    value = value();
                }
                queryDefinition['param' + name] = value;
            });
            queryDefinition.path = this.getOption('file');
            queryDefinition.dataAccessId = this.getOption('id');
            queryDefinition.outputIndexId = this.getOption('outputIdx');
            queryDefinition.pageSize = this.getOption('pageSize');
            queryDefinition.pageStart = this.getOption('page');
            queryDefinition.sortBy = this.getOption('sortBy');
            queryDefinition.paramsearchBox = this.getOption('searchPattern');
            return queryDefinition;
        },

        /*
         * Public interface
         */

        exportData: function (outputType, overrides, options) {
            if (!options) {
                options = {};
            }
            var queryDefinition = this.buildQueryDefinition(overrides);
            queryDefinition.outputType = outputType;
            if (outputType === 'csv' && options.separator) {
                queryDefinition.settingcsvSeparator = options.separator;
            }
            if (options.filename) {
                queryDefinition.settingattachmentName = options.filename;
            }
            if (outputType === 'xls' && options.template) {
                queryDefinition.settingtemplateName = options.template;
            }
            if (options.columnHeaders) {
                queryDefinition.settingcolumnHeaders = options.columnHeaders;
            }

            if (options.dtFilter !== null) {
                queryDefinition.settingdtFilter = options.dtFilter;
                if (options.dtSearchableColumns !== null) {
                    queryDefinition.settingdtSearchableColumns = options.dtSearchableColumns;
                }
            }
            queryDefinition.wrapItUp = 'wrapit';

            var successCallback = function (uuid) {
                var _exportIframe = $('<iframe style="display:none">');
                _exportIframe.detach();
                //_exportIframe[0].src = webAppPath + 'content/cda/unwrapQuery?' + $.param( {"path": queryDefinition.path, "uuid": uuid});
                _exportIframe[0].src = '/pentaho/content/cda/unwrapQuery?' + $.param({
                    "path": queryDefinition.path,
                    "uuid": uuid
                });
                _exportIframe.appendTo($('body'));
            };
            $.ajax({
                type: 'POST',
                async: false,
                data: queryDefinition,
                url: this.getOption('url'),
                success: successCallback
            });
        },

        /* Sorting
         *
         * CDA expects an array of terms consisting of a number and a letter
         * that's either 'A' or 'D'. Each term denotes, in order, a column
         * number and sort direction: 0A would then be sorting the first column
         * ascending, and 1D would sort the second column in descending order.
         * This function accepts either an array with the search terms, or
         * a comma-separated string with the terms:  "0A,1D" would then mean
         * the same as the array ["0A","1D"], which would sort the results
         * first by the first column (ascending), and then by the second
         * column (descending).
         */
        setSortBy: function (sortBy) {
            var newSort,
                myself = this;
            if (sortBy === null || sortBy === undefined || sortBy === '') {
                newSort = '';
            }
            /* If we have a string as input, we need to split it into
             * an array of sort terms. Also, independently of the parameter
             * type, we need to convert everything to upper case, since want
             * to accept 'a' and 'd' even though CDA demands capitals.
             */
            else if (typeof sortBy === "string") {
                /* Valid sortBy Strings are column numbers, optionally
                 * succeeded by A or D (ascending or descending), and separated by commas
                 */
                if (!sortBy.match("^(?:[0-9]+[adAD]?,?)*$")) {
                    throw "InvalidSortExpression";
                }
                /* Break the string into its constituent terms, filter out empty terms, if any */
                newSort = sortBy.toUpperCase().split(',').filter(function (e) {
                    return e !== "";
                });
            } else if (sortBy instanceof Array) {
                newSort = sortBy.map(function (d) {
                    return d.toUpperCase();
                });
                /* We also need to validate that each individual term is valid */
                var invalidEntries = newSort.filter(function (e) {
                    return !e.match("^[0-9]+[adAD]?,?$");
                });
                if (invalidEntries.length > 0) {
                    throw "InvalidSortExpression";
                }
            }

            /* We check whether the parameter is the same as before,
             * and notify the caller on whether it changed
             */
            var same;
            if (newSort instanceof Array) {
                same = newSort.length !== myself.getOption('sortBy').length;
                $.each(newSort, function (i, d) {
                    same = (same && d === myself.getOption('sortBy')[i]);
                    if (!same) {
                        return false;
                    }
                });
            } else {
                same = (newSort === this.getOption('sortBy'));
            }
            this.setOption('sortBy', newSort);
            return !same;
        },

        sortBy: function (sortBy, outsideCallback) {
            /* If the parameter is not the same, and we have a valid state,
             * we can fire the query.
             */
            var changed = this.setSortBy(sortBy);
            if (!changed) {
                return false;
            } else if (this.getOption('successCallback') !== null) {
                return this.doQuery(outsideCallback);
            }
        }
    };
    // Registering an object will use it to create a class by extending Dashboards.BaseQuery, 
    // and use that class to generate new queries.
    Dashboards.registerQuery("cda", cdaQueryOpts);
})();

 /* ************************ new file ************************ */
/******************************************************************************************************/
/**************************************** OLAP UTILS**************************************************/
/******************************************************************************************************/


var OlapUtils = {};

var OlapUtils = {

    mdxGroups: {},
    evolutionType: "Week"
}

OlapUtils.initMdxQueryGroup = function (obj) {

    var mdxQueryGroup = new OlapUtils.mdxQueryGroup(obj.name);

    for (m in obj.mdxQueries) {

        mdxQueryGroup.addMdxQuery(
            obj.mdxQueries[m].name,
            obj.mdxQueries[m].query,
            obj.mdxQueries[m].dimension,
            obj.mdxQueries[m].axis,
            obj.mdxQueries[m].chart
        );
    }

    OlapUtils.mdxGroups[obj.name] = mdxQueryGroup;

    if (("#" + obj.htmlObject + "_evolutionType") !== undefined)
        $("#" + obj.htmlObject + "_evolutionType").html(mdxQueryGroup.printEvolutionType(obj.htmlObject + "_evolutionType"));

    return mdxQueryGroup;
}


OlapUtils.updateMdxQueryGroup = function (obj) {

    var mdxGroup = OlapUtils.mdxGroups[obj.name];
    if (mdxGroup === undefined) {
        mdxGroup = OlapUtils.initMdxQueryGroup(obj);
    }

    $("#" + obj.htmlObject).html(mdxGroup.printConditions());

}

OlapUtils.buttonsDescription = {
    "Drill Down": 'Drill down to the selected value and add the condition to the other charts',
    "Drill Up": 'Drill up and add the condition to the other charts',
    'Focus': "Focus on this value, adding the conditions to the other charts",
    'Exclude': "Exclude this value from the chart",
    "Expand": 'Expand the depth of the chart, showing an extra sublevel',
    "Collapse": 'Collapse all previous expansions to the top most level',
    "Reset All": 'Reset all filters and conditions from this chart group, returning to the original conditions',
    "Cancel": "Cancel"
}


OlapUtils.fireMdxGroupAction = function (mdxQueryGroup, idx, param1, param2, param3) {

    /**         http://jira.pentaho.com/browse/BISERVER-3542       *
     *                                 *
     * Prior to Pentaho 3.5, this function received only 3 parameters: *
     *(query,idx,PARAM).                           *
     * In Pentaho 3.5, the behavior of the x/y and TimeSeries Charts   *
     *changed, and this function passed to receive 5 parameters:
     *(query,idx,chartDefinition,PARAM,SERIES).            *
     * When chartType === AreaChart, the value used to drill through is *
     *SERIES, otherwise it's PARAM.                    */

    if (param2 !== undefined && param3 !== undefined) {
        cType = Dashboards.ev(param1.chartType);

        if (cType === "AreaChart")
            value = encode_prepare(param3);
        else
            value = encode_prepare(param2);
    } else
        value = encode_prepare(param1);


    var mdxQueryGroup = OlapUtils.mdxGroups[mdxQueryGroup];
    if (value === 'Others')
        return; // do nothing

    OlapUtils.lastClickedMdxQueryGroup = mdxQueryGroup;
    mdxQueryGroup.clickedIdx = idx;
    mdxQueryGroup.clickedValue = value;

    var clickedObj = mdxQueryGroup.mdxQueries[idx];

    var buttonsHash = {
        "Drill Down": 'drilldown',
        "Drill Up": 'drillup',
        'Focus': "focus",
        'Exclude': "filter",
        "Expand": 'expand',
        "Collapse": 'collapse',
        "Reset All": 'resetall',
        "Cancel": "cancel"
    };

    if (clickedObj.mdxQuery.axisDepth === 0)
        delete buttonsHash.Collapse;

    //get rowLevels
    var rl = clickedObj.mdxQuery.query.rowLevels;
    var d = typeof rl === "function" ? rl() : rl;

    if (clickedObj.mdxQuery.axisPos + clickedObj.mdxQuery.axisDepth >= d.length - 1) {
        delete buttonsHash["Drill Down"];
        delete buttonsHash.Expand;
    } else {
        delete buttonsHash.Focus;
    }

    if (clickedObj.mdxQuery.axisPos === 0)
        delete buttonsHash["Drill Up"];

    // Expanded ones can't drill || focus
    if (clickedObj.mdxQuery.axisDepth > 0) {
        delete buttonsHash["Drill Down"];
        delete buttonsHash.Focus;
        delete buttonsHash.Exclude;
    }


    var msg = "Available conditions: <br/> <ul>";
    $.each(buttonsHash, function (key, value) {
        msg += "<li>" + OlapUtils.buttonsDescription[key] + "</li>"
    });
    msg += "</ul>";
    $.prompt(msg, {
        buttons: buttonsHash,
        callback: OlapUtils.mdxQueryGroupActionCallback
    });

}


/******************************************************************************************************/
/***************************************** MDX QUERY ************************************************/
/******************************************************************************************************/


OlapUtils.mdxQuery = function (hash) {
    this.query = {};
    this.originalHash = {};
    this.update(hash);
    this.axisPos = 0;
    this.axisDepth = 0;
};

OlapUtils.mdxQuery.prototype.reset = function () {
    this.update(this.originalHash);
};

OlapUtils.mdxQuery.prototype.resetFilter = function (value) {
    var rf = this.query["filters"]["rows"];
    var index = rf.indexOf(value);
    rf.splice(index, 1);
    return index;
}

OlapUtils.mdxQuery.prototype.resetFilters = function () {
    this.query["filters"] = Dashboards.clone((this.originalHash["filters"] || {
        rows: [],
        columns: []
    }));
}

OlapUtils.mdxQuery.prototype.resetCondition = function (key) {
    delete this.query["members"][key];
    delete this.query["sets"][key];
    if (this.query["conditions"][key + "InitialValue"] !== undefined)
        this.query["where"][key] = this.query["conditions"][key + "InitialValue"];
    else
        delete this.query["where"][key];
}


OlapUtils.mdxQuery.prototype.update = function (hash) {
    this.originalHash = Dashboards.clone(hash);
    this.query["members"] = hash["members"] || [];
    this.query["sets"] = hash["sets"] || [];
    this.query["rows"] = hash["rows"] || "";
    this.query["rowDrill"] = hash["rowDrill"] || false;
    this.query["rowLevels"] = hash["rowLevels"] || [];
    this.query["orderBy"] = hash["orderBy"] || "";
    this.query["from"] = hash["from"] || "";
    this.query["columns"] = hash["columns"];
    this.query["columnDrill"] = hash["columnDrill"] || false;
    this.query["columnLevels"] = hash["columnLevels"] || [];
    this.query["nonEmptyRows"] = hash["nonEmptyRows"] && true;
    this.query["nonEmptyColumns"] = hash["nonEmptyColumns"] && true;
    this.query["swapRowsAndColumns"] = hash["swapRowsAndColumns"] || false;
    this.query["filters"] = hash["filters"] || {
        rows: [],
        columns: []
    };
    this.query["where"] = hash["where"] || {};
    this.query["extra"] = hash["extra"] || {};
    this.query["conditions"] = [];
    this.query["drills"] = [];
};


OlapUtils.mdxQuery.prototype.clone = function () {
    var c = Dashboards.clone(this);
    return c;
};


OlapUtils.mdxQuery.prototype.generateAxisPart = function (axisDrill, axis, axisLevels, orderBy) {
    if (axisDrill === false) {
        return axis;
    }

    //var dim = axis.indexOf(".") === -1?axis:axis.substr(0,axis.indexOf("."));
    var dim = axis.indexOf("].") === -1 ? axis : axis.substr(0, axis.indexOf("].") + 1);
    var axisLevel = this.axisPos + this.axisDepth;
    if (axisLevel > axisLevels.length - 1) {
        axisLevel = axisLevels.length - 1
    }
    var q = "Descendants(" + axis + ", " + dim + ".[" + axisLevels[axisLevel] + "],SELF)"
    if (orderBy === "")
        return q;

    return "Order(" + q + ", " + orderBy + " , BDESC)";

}


OlapUtils.mdxQuery.prototype.getQuery = function () {
    var query = "with ";
    // We need to evaluate the hash
    var _eh = [];
    for (p in this.query) {
        var key = p;
        var value = typeof this.query[p] === 'function' ? this.query[p]() : this.query[p];
        _eh[key] = value;
    }

    if (typeof _eh["sets"] === 'object' || typeof _eh["members"] === 'object') {
        for (s in _eh["sets"]) {
            var value = typeof _eh["sets"][s] === 'function' ? _eh["sets"][s]() : _eh["sets"][s];
            query += " set " + value + " \n";
        }
        for (m in _eh["members"]) {
            var value = typeof _eh["members"][m] === 'function' ? _eh["members"][m]() : _eh["members"][m];
            query += " member " + value + " \n";
        }
    }
    // Generate the col/row sets
    var columns = _eh["swapRowsAndColumns"] ? _eh["rows"] : _eh["columns"];
    var columnLevels = _eh["swapRowsAndColumns"] ? _eh["rowLevels"] : _eh["columnLevels"];
    var columnDrill = _eh["swapRowsAndColumns"] ? _eh["rowDrill"] : _eh["columnDrill"];
    var rows = _eh["swapRowsAndColumns"] ? _eh["columns"] : _eh["rows"];
    var rowLevels = _eh["swapRowsAndColumns"] ? _eh["columnLevels"] : _eh["rowLevels"];
    var rowDrill = _eh["swapRowsAndColumns"] ? _eh["columnDrill"] : _eh["rowDrill"];
    query += " set rowSet as {" + this.generateAxisPart(rowDrill, rows, rowLevels, _eh.orderBy) + "} \n";
    query += " set colSet as {" + this.generateAxisPart(columnDrill, columns, columnLevels, _eh.orderBy) + "} \n";

    var colFilter = [];
    var rowFilter = [];
    $.each(_eh["filters"]["rows"], function (j, k) {
        rowFilter.push(_eh["rows"] + ".Dimension.currentMember.Name <> '" + k + "' ");
    });
    if (_eh["swapRowsAndColumns"]) {
        query += " set rowFilter as " + (colFilter.length > 0 ? "Filter(rowSet," + colFilter.join(" and ") + " )" : "rowSet") + "\n";
        query += " set colFilter as " + (rowFilter.length > 0 ? "Filter(colSet," + rowFilter.join(" and ") + " )" : "colSet") + "\n";
    } else {
        query += " set rowFilter as " + (rowFilter.length > 0 ? "Filter(rowSet," + rowFilter.join(" and ") + " )" : "rowSet") + "\n";
        query += " set colFilter as " + (colFilter.length > 0 ? "Filter(colSet," + colFilter.join(" and ") + " )" : "colSet") + "\n";
    }


    query += "select " + (_eh["nonEmptyRows"] ? " NON EMPTY " : "") + " rowFilter on rows,\n ";
    query += " " + (_eh["nonEmptyColumns"] ? " NON EMPTY " : "") + " colFilter on columns\n ";
    query += " from " + _eh["from"] + "\n";

    var whereArray = [];
    $.each(_eh["where"], function (key, obj) {
        var el = typeof obj === 'function' ? obj() : obj
        if (el.length > 0) whereArray.push(el);
    });
    if (whereArray.length > 0) {
        query += " where ( " + whereArray.join(' , ') + " )";
    }
    //alert(query);
    return query;

}

OlapUtils.mdxQuery.prototype.exclude = function (value) {
    this.query["filters"]["rows"].push(value);
}

OlapUtils.mdxQuery.prototype.drillDown = function (value) {

    //Clear previous excludes
    this.resetFilters();

    this.query["drills"].push(this.query.rows);
    this.query.rows = value;
    this.axisPos++;
}

OlapUtils.mdxQuery.prototype.drillUp = function () {
    this.axisPos--;
    return (this.query.rows = this.query["drills"].pop());
}

OlapUtils.mdxQuery.prototype.addCondition = function (key, value) {
    return this.addConditionAux(key, value, 'focus');
}

OlapUtils.mdxQuery.prototype.addInitialCondition = function (key, value) {
    this.addConditionAux(key, value);
}

OlapUtils.mdxQuery.prototype.removeFilter = function (key, value) {
    this.removeCondition(key, value, 'exclude');

    //Re add previous foucs that was removed by exclude.
    var lastExclude = true
    for (v in this.query["conditions"][key]) {
        lastExclude = false;
        break;
    }
    if (lastExclude && this.query["conditions"][key + "previousDrillValue"] !== undefined) {
        this.addCondition(key, this.query["conditions"][key + "previousDrillValue"]);
        delete this.query["conditions"][key + "previousDrillValue"];
    }
}

OlapUtils.mdxQuery.prototype.removeCondition = function (key, value, op) {

    if (this.query["conditions"][key] !== undefined) {
        if (this.query["conditions"][key][value] !== undefined)
            delete this.query["conditions"][key][value];
        else { //Focus not present because exclusion condition set after focus => Remove all.
            delete this.query["conditions"][key];
            this.resetCondition(key);
            return true;
        }
    }

    var condition = value.substr(0, value.indexOf("]") + 1) + ".[Filter]";
    this.setCondition(key, condition, op);
    return false;
}

OlapUtils.mdxQuery.prototype.removeConditions = function (key) {
    this.query["conditions"][key] = [];
    this.resetCondition(key);
}

/**** NEXT FUNCTIONS ARE FOR INTERNAL USE ONLY *******/
OlapUtils.mdxQuery.prototype.replaceConditionsByDrill = function (key, value) {

    //Clear previous focus and excludes
    if (this.query["conditions"][key] !== undefined)
        this.query["conditions"][key] = [];

    return this.addConditionAux(key, value, 'drill');
}

OlapUtils.mdxQuery.prototype.replaceConditionByExclude = function (key, value) {
    return this.addConditionAux(key, value, 'exclude');
}

OlapUtils.mdxQuery.prototype.addConditionAux = function (key, value, op) {

    if (op === undefined) {
        this.query["where"][key] = value;
        return;
    }

    var condition = value.substr(0, value.indexOf("]") + 1) + ".[Filter]";

    //Store initial where cause for this key
    if (this.query["conditions"][key] === undefined) {
        this.query["conditions"][key] = [];
        if (this.query["where"][key] !== undefined)
            this.query["conditions"][key + "InitialValue"] = this.query["where"][key];
    }

    if (this.query["members"][key] === undefined) {
        if (op === 'exclude') {
            //this.addMember(key,condition + " as (( "+value+".parent) - ("+value+"))");
        } else
            this.addMember(key, condition + " as Aggregate(" + key + "Filter)");

    }

    this.query["conditions"][key][value] = op

    if (op !== 'exclude')
        delete this.query["conditions"][key + "previousDrillValue"];

    //Remove previous focus and drills for this value
    if (op !== 'drill') {
        var aux = [];
        for (v in this.query["conditions"][key]) {
            //Store previous focus for first exclude
            if (op === 'exclude' && this.query["conditions"][key + "previousDrillValue"] === undefined && this.query["conditions"][key][v] === 'drill') {
                this.query["conditions"][key + "previousDrillValue"] = v;
            }
            if (this.query["conditions"][key][v] === op) aux[v] = op;
        }
        this.query["conditions"][key] = aux;
    }

    return this.setCondition(key, condition, op);
}


OlapUtils.mdxQuery.prototype.setCondition = function (key, condition, op) {
    var set = [];
    for (v in this.query["conditions"][key])
        set.push(v);

    if (set.length > 0) {
        if (op === "focus" || op === "drill")
            this.addSet(key, key + "Filter as {" + set.join(",") + "}");
        else {
            this.addMember(key, condition + " as ( ( " + set[0] + ".parent) - (" + set.join(") - (") + "))");
        }
        if (condition !== undefined)
            this.query["where"][key] = condition;
    } else
        this.resetCondition(key);

    return set;
}

OlapUtils.mdxQuery.prototype.addSet = function (key, set) {

    this.query["sets"][key] = set;
}

OlapUtils.mdxQuery.prototype.addMember = function (key, member) {

    this.query["members"][key] = member;
}

/******************************************************************************************************/
/************************************ MDX QUERYGROUP ***********************************************/
/******************************************************************************************************/

OlapUtils.lastClickedMdxQueryGroup;

OlapUtils.mdxQueryGroup = function (name) {
    this.name = name;
    this.mdxQueries = {};
    this.clickedIdx = -1;
    this.clickedValue = "";
    this.activeFilters = {};
    this.activeConditions = {};
};

OlapUtils.mdxQueryGroup.prototype.addMdxQuery = function (idx, mdxQuery, filterDimension, filterAxis, chartObject) {
    this.mdxQueries[idx] = {
        mdxQuery: mdxQuery,
        filterDimension: filterDimension,
        filterAxis: filterAxis,
        chartObject: chartObject
    };
};

OlapUtils.mdxQueryGroup.prototype.removeMdxQuery = function (idx) {
    delete this.mdxQueries.idx;
};


OlapUtils.mdxQueryGroup.prototype.printConditions = function () {

    var out = "";
    var filters = 0;
    var conds = 0;

    for (i in this.activeFilters) {
        var a = this.activeFilters[i];
        if (a.length > 0 && ++filters === 1)
            out += "<i>Exclusions: </i>";
        var mdxGroupName = this.name;
        var o = [];
        $.each(a, function (j, k) {
            o.push(k[1] + " <a class=\"resetFilterButton\" href='javascript:OlapUtils.mdxGroups[\"" + mdxGroupName + "\"].removeFilter(\"" + i + "\",\"" + k[0] + "\")'>X</a>");
            ++filters;
        });

        if (o.length > 0)
            out += o.join(" , ") + " ;";

    }
    for (i in this.activeConditions) {
        var a = this.activeConditions[i];
        if (a.length > 0 && ++conds === 1)
            out += " <i>Focus: </i>";
        var mdxGroupName = this.name;
        var o = [];
        $.each(a, function (j, k) {
            o.push(k + " <a class=\"resetFilterButton\" href='javascript:OlapUtils.mdxGroups[\"" + mdxGroupName + "\"].removeCondition(\"" + i + "\",\"" + k + "\")'>X</a>");
            ++conds;
        });
        if (o.length > 0)
            out += o.join(" , ") + "; ";
    }

    if ((conds + filters) > 2)
        out += " <a  style=\"padding-left:15px;\" href='javascript:OlapUtils.mdxGroups[\"" + this.name + "\"].resetAll()'>Reset All</a>";

    return out;
}

OlapUtils.mdxQueryGroup.prototype.printEvolutionType = function (object) {
    var out = "";
    var myArray = [
        ["Week", "Week"],
        ["Month", "Month"],
        ["Year", "Year"]
    ];

    for (var i = 0, len = myArray.length; i < len; i++) {
        out += "<input onclick='OlapUtils.changeEvolutionType(\"" + object + "radio\")'";
        if (i === 0) {
            out += " CHECKED ";
        }
        out += "type='radio' id='" + object + "radio' name='" + object + "radio' value=" + myArray[i][1] + " /> " + myArray[i][1] + (object.separator === undefined ? "" : object.separator);
    }

    return out;
}

OlapUtils.mdxQueryGroup.prototype.drillDown = function (key, value) {

    Dashboards.incrementRunningCalls();
    var conditions = [];

    //CLean previous conditions,drill, and exclude messages for this id
    if (this.activeFilters !== undefined) delete this.activeFilters[key];
    if (this.activeConditions !== undefined) delete this.activeConditions[key];

    for (i in this.mdxQueries) {
        var obj = this.mdxQueries[i];

        if (i === key) {
            obj.mdxQuery.drillDown(value);
        } else
            conditions = obj.mdxQuery.replaceConditionsByDrill(key, value);

        Dashboards.update(obj.chartObject);
    }
    if (conditions.length > 0)
        this.activeConditions[key] = conditions;

    Dashboards.update(Dashboards.getComponent(this.name));
    Dashboards.decrementRunningCalls();
}

OlapUtils.mdxQueryGroup.prototype.drillUp = function (key) {

    Dashboards.incrementRunningCalls();
    var conditions = [];

    //CLean previous conditions,drill, and exclude messages for this id
    if (this.activeFilters !== undefined) delete this.activeFilters[key];
    if (this.activeConditions !== undefined) delete this.activeConditions[key];

    var keyObj = this.mdxQueries[key];
    var value = keyObj.mdxQuery.drillUp();
    for (i in this.mdxQueries) {
        var obj = this.mdxQueries[i];

        if (i !== key) {
            if (keyObj.mdxQuery.axisPos > 0)
                conditions = obj.mdxQuery.replaceConditionsByDrill(key, value);
            else
                obj.mdxQuery.removeCondition(key, value, 'drill');
        }

        Dashboards.update(obj.chartObject);
    }
    if (conditions.length > 0)
        this.activeConditions[key] = conditions;


    Dashboards.update(Dashboards.getComponent(this.name));
    Dashboards.decrementRunningCalls();
}

OlapUtils.mdxQueryGroup.prototype.replaceFocus = function (key, values) {

    for (i in this.mdxQueries)
        if (i !== key)
            this.mdxQueries[i].mdxQuery.removeConditions(key);

    this.focus(key, values);
}

OlapUtils.mdxQueryGroup.prototype.focus = function (key, values) {
    var conditions = [];

    Dashboards.incrementRunningCalls();

    //CLean previous conditions,drill, and exclude messages for this id
    if (this.activeFilters !== undefined) delete this.activeFilters[key];
    if (this.activeConditions !== undefined) delete this.activeConditions[key];

    for (i in this.mdxQueries) {
        if (i !== key) {
            var obj = this.mdxQueries[i];

            for (i = 0; i < values.length; i++) {
                conditions = obj.mdxQuery.addCondition(key, values[i]);
            }

            Dashboards.update(obj.chartObject);

        }
    }
    if (conditions.length > 0)
        this.activeConditions[key] = conditions;

    Dashboards.update(Dashboards.getComponent(this.name));
    Dashboards.decrementRunningCalls();
}

OlapUtils.mdxQueryGroup.prototype.exclude = function (key, value) {

    Dashboards.incrementRunningCalls();
    var members = value.split("].[");
    var memberValue = members[members.length - 1].replace("]", "");
    this.mdxQueries[key].mdxQuery.exclude(memberValue)
    Dashboards.update(this.mdxQueries[key].chartObject);

    var a = this.activeFilters[key] || [];
    a.push([memberValue, value]);
    this.activeFilters[key] = a;

    //Replace focus from active conditions by exclude
    for (i in this.mdxQueries) {
        var query = this.mdxQueries[i];
        if (i !== key) {
            query.mdxQuery.replaceConditionByExclude(key, value);
            Dashboards.update(query.chartObject);
        }
    }

    //Remove previous focus message
    if (this.activeConditions[key] !== undefined)
        var indexCondition = this.activeConditions[key].indexOf(value);
    if (indexCondition >= 0)
        this.activeConditions[key].splice(indexCondition, 1);

    Dashboards.decrementRunningCalls();

}

OlapUtils.mdxQueryGroup.prototype.expand = function (key) {
    this.mdxQueries[key].mdxQuery.axisDepth++;
    Dashboards.update(this.mdxQueries[key].chartObject);
}

OlapUtils.mdxQueryGroup.prototype.collapse = function (key) {
    this.mdxQueries[key].mdxQuery.axisDepth--;
    Dashboards.update(this.mdxQueries[key].chartObject);
}

OlapUtils.mdxQueryGroup.prototype.resetAll = function () {

    Dashboards.incrementRunningCalls();
    for (i in this.mdxQueries) {
        var obj = this.mdxQueries[i];
        obj.mdxQuery.reset();
        obj.mdxQuery.axisPos = 0;
        obj.mdxQuery.axisDepth = 0;
        Dashboards.update(obj.chartObject);
    }
    this.activeFilters = {};
    this.activeConditions = {};
    Dashboards.update(Dashboards.getComponent(this.name));
    Dashboards.decrementRunningCalls();

}

OlapUtils.mdxQueryGroup.prototype.removeCondition = function (key, value) {

    Dashboards.incrementRunningCalls();

    for (i in this.mdxQueries) {
        var obj = this.mdxQueries[i];
        //Remove Conditions and related filters(because filters are added after drill down)
        if (i !== key) {
            if (obj.mdxQuery.removeCondition(key, value, 'focus') && this.activeFilters[key] !== undefined)
                delete this.activeFilters[key];
        } else {
            obj.mdxQuery.query.rows = typeof obj.mdxQuery.originalHash.rows === 'function' ? obj.mdxQuery.originalHash.rows() : obj.mdxQuery.originalHash.rows;
            obj.mdxQuery.axisPos = 0;
            obj.mdxQuery.resetFilters();
        }

        Dashboards.update(obj.chartObject);
    }

    this.activeConditions[key].splice(this.activeConditions[key].indexOf(value), 1);

    Dashboards.update(Dashboards.getComponent(this.name));
    Dashboards.decrementRunningCalls();
}

OlapUtils.mdxQueryGroup.prototype.removeFilter = function (key, value) {

    Dashboards.incrementRunningCalls();
    var index = this.mdxQueries[key].mdxQuery.resetFilter(value);
    for (i in this.mdxQueries) {
        var obj = this.mdxQueries[i];
        if (i !== key)
            obj.mdxQuery.removeFilter(key, this.activeFilters[key][index][1]);
        Dashboards.update(obj.chartObject);
    }
    this.activeFilters[key].splice(index, 1);
    if (this.activeFilters[key].length === 0)
        delete this.activeFilters[key];

    Dashboards.update(Dashboards.getComponent(this.name));
    Dashboards.decrementRunningCalls();
}

OlapUtils.mdxQueryGroupActionCallback = function (value, m) {

    if (value === "cancel")
        return; // do nothing.

    Dashboards.incrementRunningCalls();

    var mqg = OlapUtils.lastClickedMdxQueryGroup;
    var clickedObj = mqg.mdxQueries[mqg.clickedIdx];
    var axis = typeof clickedObj.mdxQuery.query.rows === 'function' ? clickedObj.mdxQuery.query.rows() : clickedObj.mdxQuery.query.rows;

    if (value === "drilldown") {
        mqg.drillDown(mqg.clickedIdx, axis + ".[" + mqg.clickedValue + "]");
    } else if (value === "drillup") {
        mqg.drillUp(mqg.clickedIdx, axis + ".[" + mqg.clickedValue + "]");
    } else if (value === "focus") {
        mqg.focus(mqg.clickedIdx, [axis + ".[" + mqg.clickedValue + "]"]);
    } else if (value === "filter") {
        mqg.exclude(mqg.clickedIdx, axis + ".[" + mqg.clickedValue + "]");
    } else if (value === "expand") {
        mqg.expand(mqg.clickedIdx);
    } else if (value === "collapse") {
        mqg.collapse(mqg.clickedIdx);
    } else if (value === "resetall") {
        mqg.resetAll();
    }

    Dashboards.update(Dashboards.getComponent(mqg.name));
    Dashboards.decrementRunningCalls();
}

OlapUtils.getAxisPathString = function (axis, axisPath) {
    var a = [];
    $.each(axisPath, function (i, v) {
        a.push("[" + v + "]");
    });
    return axis + "." + a.join(".");
}

OlapUtils.changeEvolutionType = function (object) {


    var value = "";
    var selector = document.getElementsByName(object);
    for (var i = 0, len = selector.length; i < len; i++) {
        if (selector[i].checked) {
            value = selector[i].value;
            continue;
        };
    }

    this.fireChange("OlapUtils.evolutionType", value);
}

/******************************************************************************************************/
/************************************ GENERIC QUERIES **********************************************/
/******************************************************************************************************/


OlapUtils.GenericMdxQuery = Base.extend({

    mdxQuery: undefined,

    options: {},
    tableDefaults: {},
    chartDefaults: {},

    genericDefaults: {
        dateDim: '[Date]',
        dateLevel: '[Date]',
        dateLevelMonth: '[Month]',
        from: '[CubeName]',
        nonEmptyRows: true,
        rowDrill: true,
        measuresDim: '[Measures]',
        orderBy: undefined,
        debug: false
    },

    constructor: function (options) {},

    getQuery: function () {

        /*for(o in this.options)
         this.options[o] = OlapUtils.ev(this.options[o]);*/

        this.query = this.mdxQuery.getQuery();

        if (this.options.debug === true) {
            alert(this.query);
        }


        return this.query;
    },

    getDataTableOptions: function (options) {
        $.extend(this.tableDefaults, options);
        return TableComponent.getDataTableOptions(this.tableDefaults);
    },

    getChartOptions: function (options) {
        $.extend(this.chartDefaults, options);
        return this.chartDefaults;
    }


});


OlapUtils.EvolutionQuery = OlapUtils.GenericMdxQuery.extend({

    mdxQuery: undefined,
    thisMonth: "",
    lastMonth: "",
    lastYearMonth: "",

    specificDefaults: {
        baseDate: '2008-10-01',
        rows: '[Locale Codes]',
        rowLevels: ['Code'],
        measure: '[Total Month Requests]',
        debug: false
    },

    tableDefaults: {
        colHeaders: ["Dimension", 'Total', '% m/m', '% m/m-12', 'Last 12 months'],
        colTypes: ['string', 'numeric', 'numeric', 'numeric', 'sparkline'],
        colFormats: [null, '%.0f', '%.2f', '%.2f', null],
        colWidths: ['100px', '50px', '50px', '50px', '80px'],
        displayLength: 10,
        sparklineType: "line",
        sortBy: [
            [1, 'desc']
        ]
    },


    constructor: function (options, object) {

        this.options = jQuery.extend({}, this.genericDefaults, this.specificDefaults, options);
        var options = this.options;
        //options.baseDate = OlapUtils.ev(options.baseDate);
        var thisPeriod = options.dateDim + ".[This Period]";
        var lastPeriod = options.dateDim + ".[Previous Period]";
        var lastYearPeriod = options.dateDim + ".[Last Year Period]";
        var nonEmptyMeasure = ".[Not Null Measure]";

        this.queryBase = {
            from: options.from,
            rows: options.rows,
            rowLevels: options.rowLevels,
            rowDrill: options.rowDrill,
            nonEmptyRows: options.nonEmptyRows,
            columns: "" + options.measuresDim + nonEmptyMeasure + "," + options.measuresDim + ".[% m/m]," + options.measuresDim + ".[% m/m-12]," + options.measuresDim + ".[sparkdatamonths]",
            swapRowsAndColumns: false,
            orderBy: options.orderBy,
            sets: {
                "now": function () {
                    return "now as [Date].[Date].[" + Dashboards.ev(options.baseDate) + "].Lag(30.0)" + ":" + " [Date].[Date].[" + Dashboards.ev(options.baseDate) + "]"
                },
                "oneMonthAgo": function () {
                    return "oneMonthAgo as [Date].[Date].[" + Dashboards.ev(options.baseDate) + "].Lag(60.0)" + ":" + " [Date].[Date].[" + Dashboards.ev(options.baseDate) + "].Lag(30.0)"
                },
                "oneYearAgo": function () {
                    return "oneYearAgo as [Date].[Date].[" + Dashboards.ev(options.baseDate) + "].Lag(395.0)" + ":" + " [Date].[Date].[" + Dashboards.ev(options.baseDate) + "].Lag(365.0)"
                },
                "last12Months": function () {
                    return "last12Months as LastPeriods(12.0, Ancestor(" + options.dateDim + "." + options.dateLevel + ".[" + Dashboards.ev(options.baseDate) + "]," + options.dateDim + "." + options.dateLevelMonth + ")) "
                }
            },
            members: {
                todaysMonth: function () {
                    return "[Date].[TodaysMonth] as Aggregate( now )"
                },
                notNullMeasure: function () {
                    return "" + options.measuresDim + nonEmptyMeasure + " as Iif(isEmpty(" + options.measuresDim + "." + options.measure + "), 0 , " + options.measuresDim + "." + options.measure + ") "
                },
                thisPeriodMeasure: function () {
                    return "" + options.measuresDim + ".[This Period] as Aggregate(now*" + options.measuresDim + nonEmptyMeasure + ") "
                },
                previousPeriodMeasure: function () {
                    return "" + options.measuresDim + ".[Previous Period] as Aggregate(oneMonthAgo*" + options.measuresDim + nonEmptyMeasure + ") "
                },
                lastYearPeriodMeasure: function () {
                    return "" + options.measuresDim + ".[Last Year Period] as Aggregate(oneYearAgo*" + options.measuresDim + nonEmptyMeasure + ") "
                },
                mmMeasure: function () {
                    return "" + options.measuresDim + ".[% m/m] as 100.0*(" + options.measuresDim + nonEmptyMeasure + " / " + options.measuresDim + ".[Previous Period] - 1.0)  "
                },
                mm12Measure: function () {
                    return "" + options.measuresDim + ".[% m/m-12] as 100.0*(" + options.measuresDim + nonEmptyMeasure + " / " + options.measuresDim + ".[Last Year Period] - 1.0)  "
                },
                sparkdatamonths: function () {
                    return "" + options.measuresDim + ".[sparkdatamonths] as Generate([last12Months], Cast((" + options.measuresDim + nonEmptyMeasure + ") + 0.0 as String), \" , \") "
                }
            },
            where: {
                dateBase: "" + options.dateDim + ".[TodaysMonth]"
            }

        };
        // Init this querybase
        this.mdxQuery = new OlapUtils.mdxQuery(this.queryBase);

    },

    queryBase: {}


});


OlapUtils.DimensionAnalysisQuery = OlapUtils.GenericMdxQuery.extend({

    chartTypesTranslation: {},
    translationHash: {},
    mdxQuery: undefined,
    thisMonth: "",
    lastMonth: "",
    lastYearMonth: "",

    specificDefaults: {
        startDate: '2008-10-01',
        endDate: '2008-11-01',
        rows: '[Product Operating Systems]',
        rowLevels: ["Platform", "Version"],
        measure: '[Total Requests]',
        defaultChartType: "bar",
        debug: false,
        where: {}
    },

    tableDefaults: {
        colHeaders: ['Name', 'Value'],
        colTypes: ['string', 'numeric'],
        colFormats: [null, '%.0f'],
        sortBy: [
            [1, 'desc']
        ],
        lengthChange: false
    },

    chartDefaults: {
        domainLabelRotationDir: "up",
        domainLabelRotation: "0",
        orientation: "horizontal",
        title: "",
        isStacked: "true",
        is3d: false,
        foregroundAlpha: 0.8,
        showValues: true,
        chartType: function () {
            return this.parent.queryBase.extra.translationHash.chartType;
        },
        datasetType: function () {
            return this.parent.queryBase.extra.translationHash.datasetType;
        },
        includeLegend: function () {
            return this.parent.queryBase.extra.translationHash.includeLegend;
        },
        topCountAxis: function () {
            return this.parent.queryBase.extra.translationHash.axis[1];
        }

    },

    constructor: function (options, object) {

        this.options = jQuery.extend({}, this.genericDefaults, this.specificDefaults, options);
        var options = this.options;

        this.chartTypesTranslation = {
            "pie": {
                type: "jFreeChartComponent",
                chartType: "PieChart",
                datasetType: "CategoryDataset",
                axis: ["columns", "rows"],
                member: "(" + options.dateDim + ".[Date Range], " + options.measuresDim + ".[Avg])",
                includeLegend: false
            },
            "bar": {
                type: "jFreeChartComponent",
                chartType: "BarChart",
                datasetType: "CategoryDataset",
                axis: ["columns", "rows"],
                member: "(" + options.dateDim + ".[Date Range], " + options.measuresDim + ".[Avg])",
                includeLegend: false
            },
            "table": {
                type: "tableComponent",
                chartType: "PieChart",
                datasetType: "CategoryDataset",
                axis: ["columns", "rows"],
                member: "(" + options.dateDim + ".[Date Range], " + options.measuresDim + ".[Avg])",
                includeLegend: false
            },
            "trend": {
                type: "jFreeChartComponent",
                chartType: "AreaChart",
                datasetType: "TimeSeriesCollection",
                axis: ["rows", "columns"],
                member: "a",
                includeLegend: true
            }
        };

        this.queryBase = {
            from: options.from,
            rows: options.rows,
            rowLevels: options.rowLevels,
            rowDrill: options.rowDrill,
            nonEmptyRows: options.nonEmptyRows,
            columns: function () {
                return this.extra.translationHash["member"]
            },
            swapRowsAndColumns: function () {
                return this.extra.translationHash["axis"][0] === "rows"
            },
            orderBy: "Avg(a," + options.measuresDim + "." + options.measure + ")",

            sets: {
                "a": function () {
                    return "a as '(" + options.dateDim + "." + options.dateLevel + ".[" + Dashboards.ev(options.startDate) + "]:" + options.dateDim + "." + options.dateLevel + ".[" + Dashboards.ev(options.endDate) + "])'"
                }
            },
            members: {
                daterange: "" + options.dateDim + ".[Date Range] as Aggregate(a)",
                average: "" + options.measuresDim + ".[Avg] as 'Avg(a," + options.measuresDim + "." + options.measure + ")'"
            },
            where: options.where,
            extra: {}
        };

        // pass the properties of this to the chartDefaults
        var _chart = Dashboards.clone(this);
        delete _chart.chartDefaults;
        this.chartDefaults.parent = _chart;

        this.setChartType(options.defaultChartType);

        // Init this querybase
        this.mdxQuery = new OlapUtils.mdxQuery(this.queryBase);

    },

    setChartType: function (chartType) {
        this.queryBase.extra.translationHash = this.chartTypesTranslation[chartType];
        this.chartDefaults.parent.queryBase.extra.translationHash = this.chartTypesTranslation[chartType];
    },

    getComponentType: function () {
        return this.chartDefaults.parent.queryBase.extra.translationHash.type;
    },

    queryBase: {}
});
