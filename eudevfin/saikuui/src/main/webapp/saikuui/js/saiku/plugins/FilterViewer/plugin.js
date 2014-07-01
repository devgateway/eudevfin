var FilterInfo = Backbone.View.extend({
    initialize: function(args) {
        this.workspace = args.workspace;
        this.id = _.uniqueId("filterinfo_");
        $(this.el).attr({ id: this.id });
        _.bindAll(this, "render", "update_filter_info", "process_data");
        this.workspace.bind('query:result', this.update_filter_info);
        $(this.workspace.el).find('.workspace_filter_info')
            .prepend($(this.el));
        $(this.el.parentElement).hide();
    },

    render: function() {
    	debugger;
    },
    
    update_filter_info: function(args) {
        return _.delay(this.process_data, 0, args);
    },
    
    process_data: function(args) {
    	var self = this;
    	var selectedFilters = $(args.workspace.el).find(".fields_list[title='FILTER'] .level");
    	var requests = [];
    	var path = Settings.TOMCAT_WEBAPP + "/rest/saiku/admin/query/" + args.workspace.query.id;

    	_.each(selectedFilters, function(filter) {
    		var dimension = filter.href.split("/#")[1].split('/')[0];
    		requests.push($.ajax(path + "/axis/FILTER/dimension/" + dimension));
    	});
    	if(requests.length == 0) {
            $(self.el).html("");
            $(self.el.parentElement).hide();
    	}
    	$.when.apply($, requests).done(function () {
    		if(arguments.length > 0){
        		var filterString = "<strong>Selected Filters: </strong><br/>";
        	    $.each(arguments, function (i, data) {
        	    	var datum = (data instanceof Array) ? data[0] : data;
        	    	if(datum.caption){
            	    	filterString += "Dimension: " + datum.caption + " - ";
            	    	filterString += "Values: ";
            	    	_.each(datum.selections, function(value, idx){
            	    		filterString += value.caption;
            	    		if(datum.selections.length-1 != idx)
            	    			filterString += ", ";
            	    	});
            	    	filterString += "<br/>";
        	    	}

        	    });
                $(self.el).html(filterString);
                $(self.el.parentElement).show();
    		}
    		else
			{
                $(self.el).html("");
                $(self.el.parentElement).hide();
			}
    	});    	
    	
    }
});

/**
 * Start Plugin
 */ 
 Saiku.events.bind('session:new', function(session) {

        function new_workspace(args) {
            // Add filter info element
            if (typeof args.workspace.FilterInfo == "undefined") {
                args.workspace.FilterInfo = new FilterInfo({ workspace: args.workspace });
            }
        }

        function clear_workspace(args) {
                    if (typeof args.workspace.FilterInfo != "undefined") {
                        $(args.workspace.FilterInfo.el).show();

                    }
        }
        
        // Attach FilterInfo to existing tabs
        for(var i = 0; i < Saiku.tabs._tabs.length; i++) {
            var tab = Saiku.tabs._tabs[i];
            new_workspace({
                workspace: tab.content
            });
        };

        // Attach FilterInfo to future tabs
        Saiku.session.bind("workspace:new", new_workspace);
        Saiku.session.bind("workspace:clear", clear_workspace);
    });


