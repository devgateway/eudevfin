var Currency = Backbone.View.extend({
    initialize: function(args) {
        this.workspace = args.workspace;
        
        // Create a unique ID for use as the CSS selector
        this.id = _.uniqueId("currency_");
        $(this.el).attr({ id: this.id });
        
        // Bind table rendering to query result event
        _.bindAll(this, "render");
        this.selectedCurrency = 'USD';
        
        this.workspace.toolbar.currency = this.show;
        this.workspace.bind('workspace:adjust', this.render);
        var self = this;
        $.ajax({
        	  url: "/jasperreports/currencylist"
        	}).done(function(data) {
        		self.selectedCurrency = data.selectedCurrency;
        		self.currencyList = data.list;
        		self.add_select();
        	});
    },
    
    add_select: function() {
        var $currency_select = 
            $('<a href="#currency" class="currency button i18n currency_button" style="width:30px" title="Change Currency"></a>')
            .css({  'background-image': "url('js/saiku/plugins/Currency/curr.png')",
                    'background-repeat':'no-repeat',
                    'background-position':'10% 50%',
                    'padding-left':'30px',
                    'text-decoration':'none'
                }).html(this.selectedCurrency);

        var $currency_li = $('<li class="seperator"></li>').append($currency_select);
        $(this.workspace.toolbar.el).find("ul").append($currency_li);
    },
    
    show: function(event, ui) {
        var self = this;
        $target = $(event.target).hasClass('button') ? $(event.target) : $(event.target).parent();
        
        $body = $(document);
        //$body.off('.contextMenu .contextMenuAutoHide');
        //$('.context-menu-list').remove();
        $.contextMenu('destroy', '.currency_button');
        console.log(this.workspace.currency.currencyList);
        var items = {};
        _.each(this.workspace.currency.currencyList, function(v){items[v]= JSON.parse('{"name":"' + v + '"}')});
        
        $.contextMenu({
                selector: '.currency_button',
                trigger: 'left',
                ignoreRightClick: true,
                callback: function(key, options) {
                    self.workspace.currency.updateCurrency(key);
                },
                items: items
        });
        $target.contextMenu();
    },
    
    updateCurrency: function(key){
    	var self = this;
    	var altUrl = Settings.REST_URL + Saiku.session.username + "/discover/refresh?" + new Date().getTime();
    	console.log(altUrl);
    	if(key != ""){
        	this.selectedCurrency = key;
            $.ajax({
          	  url: "/jasperreports/currency?reportCurrency=" + key
          	}).done(function() {
          		//self.workspace.refresh();
                $.ajax({
                	  url: altUrl
                	}).done(function() {
                		$(".currency_button").html(key);
                });
          	});
    	}
    },
    
    setOptions: function(event) {
        var type = $(event.target).attr('href').replace('#', '');
        try {
            this[type]();
        } catch (e) { }
        
        return false;
    },
    
    render: function() {
    	$('.currency_button').html(this.selectedCurrency);
        if (! $(this.workspace.toolbar.el).find('.currency').hasClass('on')) {
            return;
        }
    },
});

Saiku.events.bind('session:new', function(session) {

    function new_workspace(args) {
        // Add stats element
        if (typeof args.workspace.currency == "undefined") {
            args.workspace.currency = new Currency({ workspace: args.workspace });
        }
    }

    function clear_workspace(args) {
        if (typeof args.workspace.currency != "undefined") {
            $(args.workspace.currency.el).hide();
        }
    }
    
    // Attach stats to existing tabs
    for(var i = 0; i < Saiku.tabs._tabs.length; i++) {
        var tab = Saiku.tabs._tabs[i];
        new_workspace({
            workspace: tab.content
        });
    };

    // Attach stats to future tabs
    Saiku.session.bind("workspace:new", new_workspace);
    Saiku.session.bind("workspace:clear", clear_workspace);
});
