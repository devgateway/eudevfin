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
            }
            else if ((c > 127) && (c < 2048)) {
                utftext += String.fromCharCode((c >> 6) | 192);
                utftext += String.fromCharCode((c & 63) | 128);
            }
            else {
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
        var c = 0, c2 = 0, c3 = 0;

        while (i < utftext.length) {

            c = utftext.charCodeAt(i);

            if (c < 128) {
                string += String.fromCharCode(c);
                i++;
            }
            else if ((c > 191) && (c < 224)) {
                c2 = utftext.charCodeAt(i + 1);
                string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
                i += 2;
            }
            else {
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
    }
    else {
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
            }
            else if (m.code === 'b') {
                m.argument = String(Math.abs(parseInt(m.argument)).toString(2));
                substitution = sprintfWrapper.convert(m, true);
            }
            else if (m.code === 'c') {
                m.argument = String(String.fromCharCode(parseInt(Math.abs(parseInt(m.argument)))));
                substitution = sprintfWrapper.convert(m, true);
            }
            else if (m.code === 'd') {
                m.argument = toFormatedString(String(Math.abs(parseInt(m.argument))));
                substitution = sprintfWrapper.convert(m);
            }
            else if (m.code === 'f') {
                m.argument = toFormatedString(String(Math.abs(parseFloat(m.argument)).toFixed(m.precision ? m.precision : 6)));
                substitution = sprintfWrapper.convert(m);
            }
            else if (m.code === 'o') {
                m.argument = String(Math.abs(parseInt(m.argument)).toString(8));
                substitution = sprintfWrapper.convert(m);
            }
            else if (m.code === 's') {
                m.argument = m.argument.substring(0, m.precision ? m.precision : m.argument.length);
                substitution = sprintfWrapper.convert(m, true);
            }
            else if (m.code === 'x') {
                m.argument = String(Math.abs(parseInt(m.argument)).toString(16));
                substitution = sprintfWrapper.convert(m);
            }
            else if (m.code === 'X') {
                m.argument = String(Math.abs(parseInt(m.argument)).toString(16));
                substitution = sprintfWrapper.convert(m).toUpperCase();
            }
            else {
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
