module.exports = function(grunt) {
	// Project configuration
	grunt.initConfig({
		// Metadata - Project configuration
		pkg: grunt.file.readJSON('package.json'),
		banner: '/**\n  * <%= pkg.name %> - v<%= pkg.version %> - ' +
			'<%= grunt.template.today("yyyy-mm-dd") %>\n' +
			'<%= pkg.homepage ? "  * " + pkg.homepage + "\\n" : "" %>' +
			'  * <%= pkg.description %>\n' +
			'  * Licensed: <%= pkg.license %>\n' + 
			'  * Copyright (c) <%= grunt.template.today("yyyy") %> <%= pkg.author.name %> (<%= pkg.author.homepage %>)\n' +
			'  */\n',

		jshint: {
			// configure JSHint (documented at http://www.jshint.com/docs/)
			options: {
				bitwise: false,
				unused: false,		// TODO - this should be 'true'
				forin: true,
				nonstandard: true,
				curly: true,
				eqeqeq: true,
				immed: true,
				latedef: true,
				newcap: true,
				noarg: true,
				sub: true,
				undef: true,
				boss: true,
				eqnull: true,
				browser: true,
				smarttabs: true,

				// define the global variables
				globals: {
					$: true,
					jQuery: true,
					Backbone: true,
					_: true,
					console: true,
					module: true,
					define: true,
					require: true,
					Dashboards: true,
					Mustache: true,
					Base: true,

					// this are global variables for tests
					QUnit: true,
					sinon: true,
					ok: true,
					test: true,
					expect: true,
					equal: true,
					notEqual: true,
					deepEqual: true,
					deepNotEqual: true
				}	
			},

			grunt: {
				src: ['Gruntfile.js']
			},

			src: {
				// all source files excluding libraries
				src: [
					'src/main/webapp/js/Dashboards.js', 
					// 'src/main/webapp/js/**/*.js', 
					'!src/main/webapp/js/cdfplugin.js', 
					'!src/main/webapp/js/cdfplugin.min.js',
					'!src/main/webapp/js/libs/**/*.js',
					'!src/main/webapp/js/dataTables/**/*.js', 
					'!src/main/webapp/js/jquery-libs/**/*.js',
					'!src/main/webapp/js/modernizr-2.0.6.js',
					'!src/main/webapp/js/mustache.js'
				]
			},

			tests: {
				// all testing files excluding libraries
				src: ['src/test/resources/js/**/*.js', '!src/test/resources/js/libs/**/*.js']
			}
		},

		uglify: {
			options: {
				banner: '<%= banner %>'
			},

			min: {
				files: [
					{
						src: ['src/main/webapp/js/cdfplugin.js'],
						// src: ['target/cdfplugin.js'],
						dest: 'src/main/webapp/js/cdfplugin.min.js'
						// dest: 'target/cdfplugin.min.js'
					}
				]
			}
		},

		concat: {
			options: {
				separator: '; /* ************************ new file ************************ */\n'
			},
			
			dist: {
				src: [
						'src/main/webapp/js/libs/underscore-1.5.2.js', 
						'src/main/webapp/js/libs/backbone-1.1.0.js', 
						'src/main/webapp/js/mustache.js',
						// 'src/main/webapp/js/modernizr-2.0.6.js', 
						'src/main/webapp/js/jquery-libs/**/*.js',
						'src/main/webapp/js/dataTables/js/jquery.dataTables.min.js',
						'src/main/webapp/js/Base.js',
						'src/main/webapp/js/Dashboards.utilities.js',
						'src/main/webapp/js/Dashboards.js',
						'src/main/webapp/js/Dashboards.container.js',
						'src/main/webapp/js/Dashboards.optionsmanager.js',
						'src/main/webapp/js/Dashboards.popup.js',
						'src/main/webapp/js/components/**/*.js',
						'src/main/webapp/js/AddIns.js',
						'src/main/webapp/js/addIns/**/*.js',
						'src/main/webapp/js/queries/**/*.js',
						'src/main/webapp/js/OlapUtils.js'
					],
				dest: 'src/main/webapp/js/cdfplugin.js'
				// dest: 'target/cdfplugin.js'
			}
		},

		qunit: {
			sample: {
				src: ['src/test/resources/js/*Example.html']	
			}
		},

		clean: {
			build: {
				src: ['src/main/webapp/js/cdfplugin.js', 'src/main/webapp/js/cdfplugin.min.js']
			}
		}
	});

	// Load the plugin that provides the "uglify" task.
	grunt.loadNpmTasks('grunt-contrib-uglify');

	// Load the plugin that provides the "concat" task.
	grunt.loadNpmTasks('grunt-contrib-concat');

	// Load the plugin that provides the "lint" task.
	grunt.loadNpmTasks('grunt-contrib-jshint');

	// Load the plugin that provides the "qunit" task.
	grunt.loadNpmTasks('grunt-contrib-qunit');

	// Load the plugin that provides the "clean" task.
	grunt.loadNpmTasks('grunt-contrib-clean');

	// Default task.
	// grunt.registerTask('default', ['jshint:grunt', 'qunit', 'clean', 'concat', 'uglify']);

	grunt.registerTask('default', ['jshint:grunt', 'concat']);
};
