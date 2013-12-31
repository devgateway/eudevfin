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

		jsbeautifier: {
		    files: [
					'src/main/webapp/js/*.js',
					'../dim/src/main/resources/org/devgateway/eudevfin/dim/pages/reports/**/*.js',
					'!src/main/webapp/js/cdfplugin.js', 
					'!src/main/webapp/js/cdfplugin.min.js',
					'!src/main/webapp/js/libs/**/*.js',
					'!src/main/webapp/js/dataTables/**/*.js', 
					'!src/main/webapp/js/jquery-libs/**/*.js',
					'!src/main/webapp/js/modernizr-2.0.6.js',
					'!src/main/webapp/js/mustache.js'
					],
			options: {
				js: {
					braceStyle: "collapse",
					breakChainedMethods: false,
					e4x: false,
					evalCode: false,
					indentChar: " ",
					indentLevel: 0,
					indentSize: 4,
					indentWithTabs: false,
					jslintHappy: true,
					keepArrayIndentation: false,
					keepFunctionIndentation: true,
					maxPreserveNewlines: 4,
					preserveNewlines: true,
					spaceBeforeConditional: true,
					spaceInParen: false,
					unescapeStrings: false,
					wrapLineLength: 0
				}
			}
		},

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
					app: true,
					Dashboards: true,
					Highcharts: true,
					AddIn: true,
					sprintf: true,

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
					'../dim/src/main/resources/org/devgateway/eudevfin/dim/pages/reports/**/*.js', 
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

		concat: {
			options: {
				separator: '\n /* ************************ new file ************************ */\n'
			},
			
			js: {
				src: [
						'src/main/webapp/js/libs/underscore-1.5.2.js', 
						'src/main/webapp/js/libs/backbone-1.1.0.js', 
						'src/main/webapp/js/mustache.js',
						// 'src/main/webapp/js/modernizr-2.0.6.js', 
						'src/main/webapp/js/jquery-libs/**/*.js',
						'src/main/webapp/js/dataTables/js/jquery.dataTables.js',
						'src/main/webapp/js/tableTools/js/ZeroClipboard.js',
						'src/main/webapp/js/tableTools/js/TableTools.js',
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
			},

			css: {
				src: [
						'src/main/webapp/js/cdf.css',
						'src/main/webapp/js/style.css'
					],
				dest: 'src/main/webapp/js/cdfcss.css'
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
						dest: 'src/main/webapp/js/cdfplugin.min.js'
					}
				]
			}
		},

		cssmin: {
			add_banner: {
				options: {
					banner: '<%= banner %>'
				},
				files: {
					'src/main/webapp/js/cdfcss.min.css': ['src/main/webapp/js/cdfcss.css']
				}
			}
		},

		qunit: {
			sample: {
				src: ['src/test/resources/js/*Example.html']	
			}
		},

		clean: {
			build: {
				src: [
					'src/main/webapp/js/cdfplugin.js', 
					'src/main/webapp/js/cdfplugin.min.js',
					'src/main/webapp/js/cdfcss.css',
					'src/main/webapp/js/cdfcss.min.css'
					]
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

	// Load the plugin that provides the "cssmin" task.
	grunt.loadNpmTasks('grunt-contrib-cssmin');

	// Load the plugin that provides the "clean" task.
	grunt.loadNpmTasks('grunt-contrib-clean');

	// Load the plugin that provides the "beautifier" task.
	grunt.loadNpmTasks('grunt-jsbeautifier');

	// dist task
	grunt.registerTask('dist', ['jshint', 'qunit', 'clean', 'jsbeautifier', 'concat', 'cssmin', 'uglify']);

	// Default task
	grunt.registerTask('default', ['jshint:grunt', 'concat', 'cssmin']);
};
