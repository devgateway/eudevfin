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
				bitwise: true,
				curly: true,
				eqeqeq: true,
				forin: true,
				immed: true,
				latedef: true,
				newcap: true,
				noarg: true,
				sub: true,
				undef: true,
				unused: true,
				boss: true,
				eqnull: true,
				browser: true,

				// define the global variables
				globals: {
					$: true,
					jQuery: true,
					console: true,
					module: true,
					define: true,
					require: true,

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
				src: ['src/main/resources/js/**/*.js', '!src/main/resources/js/dataTables/**/*.js', '!src/main/resources/js/jquery*.js']
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
						src: ['src/main/resources/js/cdfplugin.js'],
						// src: ['target/cdfplugin.js'],
						dest: 'src/main/resources/js/cdfplugin.min.js'
						// dest: 'target/cdfplugin.min.js'
					}
				]
			}
		},

		concat: {
			options: {
				separator: '; /* ************************ new file ************************ */\n'
			},

			// -rw-r--r--   1 ionut  staff    16987 Nov 18 15:32 mustache.js

			// -rw-r--r--   1 ionut  staff  374973 Sep 29  2012 jquery.ui.js
			// -rw-r--r--   1 ionut  staff   15570 Nov 18 15:19 jquery.blockUI.js
			// -rw-r--r--   1 ionut  staff   13107 Nov 18 15:32 jquery.i18n.properties.js
			// -rw-r--r--   1 ionut  staff  122355 Nov 13 10:37 jquery.sparkline.js


			// drwxr-xr-x   7 ionut  staff      238 Nov 13 10:37 dataTables

			// -rw-r--r--   1 ionut  staff     3918 Nov 13 10:37 Base.js
			// -rw-r--r--   1 ionut  staff   101641 Nov 18 17:16 Dashboards.js

			// drwxr-xr-x   5 ionut  staff      170 Nov 18 15:32 components


			// -rw-r--r--   1 ionut  staff     3371 Nov 18 15:32 AddIns.js
			// drwxr-xr-x   6 ionut  staff      204 Nov 18 17:15 addIns
			// drwxr-xr-x   5 ionut  staff      170 Nov 18 15:32 queries

			// -rw-r--r--   1 ionut  staff    37647 Nov 18 15:32 OlapUtils.js
			
			
			dist: {
				src: [
						'src/main/resources/js/underscore.js', 
						'src/main/resources/js/backbone.js',
						'src/main/resources/js/mustache.js',
						// 'src/main/resources/js/modernizr-2.0.6.js', 
						'src/main/resources/js/jquery-libs/**/*.js',
						// 'src/main/resources/js/jquery-libs/jquery.ui.js',
						// 'src/main/resources/js/jquery-libs/jquery.blockUI.js',
						// 'src/main/resources/js/jquery-libs/jquery.i18n.properties.js',
						// 'src/main/resources/js/jquery-libs/jquery.sparkline.js',
						'src/main/resources/js/dataTables/**/*s.js',
						'src/main/resources/js/string.js',
						'src/main/resources/js/Base.js',
						'src/main/resources/js/Dashboards.js',
						'src/main/resources/js/components/**/*.js',
						'src/main/resources/js/AddIns.js',
						'src/main/resources/js/addIns/**/*.js',
						'src/main/resources/js/queries/**/*.js',
						'src/main/resources/js/OlapUtils.js'
					],
				dest: 'src/main/resources/js/cdfplugin.js'
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
				src: ['target/cdfplugin.js', 'target/cdfplugin.min.js']
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
