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

	/*
	 Format for displaying percentages in tables results

	 Added the following style for this add in component
	 .tableComponent td.percentFormat {
	 text-align: right;
	 font-family: monospace;
	 }
	 */
	var percentFormat = {
		name: "percentFormat",
		label: "percentFormat",
		defaults: {
			valueFormat: function (v, format, st) {
                if(v === null) {
                    return null;
                }
				return sprintf('%.2f', v) + ' %';
			}
		},

		init: function (){
			$.fn.dataTableExt.oSort[this.name+'-asc'] = $.fn.dataTableExt.oSort['numeric-asc'];
			$.fn.dataTableExt.oSort[this.name+'-desc'] = $.fn.dataTableExt.oSort['numeric-desc'];
		},

		implementation: function (tgt, st, opt) {
			var t = $(tgt);
            if(st.value === null) {
                t.html('');
            } else {
			    t.html((st.colFormat ? sprintf(st.colFormat, st.value) : st.value) + ' %');
            }
		}
	};

	Dashboards.registerAddIn("Table", "colType", new AddIn(percentFormat));
})();
