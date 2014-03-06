/*
 * Dashboards Popups
 */

var wd = wd || {};
wd.cdf = wd.cdf || {};
wd.cdf.popups = wd.cdf.popups || {};

wd.cdf.popups.okPopup = {
    template: _.template(
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
    template: _.template(
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
    template: _.template(
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
