var FilterInfo = Backbone.View.extend({
    initialize: function(args) {
        this.workspace = args.workspace;
        this.id = _.uniqueId("filterinfo_");
        $(this.el).attr({ id: this.id });
        _.bindAll(this, "render", "update_filter_info", "process_data");
        this.workspace.bind('query:result', this.update_filter_info);
        $(this.workspace.el).find('.workspace_filter_info')
            .prepend($(this.el).show());
    },

    render: function() {
    	debugger;
    },
    
    update_filter_info: function(args) {
        return _.delay(this.process_data, 0, args);
    },
    
    process_data: function(args) {
    	var filterString = "";
    	var self = this;
    	args.workspace.query.action.get("/mdx", { 
            success: function(model, response) {
                var mdx = response.mdx;
                //Look for WHERE section, extract the string
                var whereSection = mdx.split("WHERE")[1];
                //Isolate where sections
                var re = /\{[^\}]*\}/;
                var result = re.exec(whereSection);
                debugger;
//                var whereSections = whereSection.split(",");
//                _.each(whereSections, function(obj){
//                	console.log(obj);
//                });

                $(self.el).text(response.mdx);
                $(self.el).show();
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


