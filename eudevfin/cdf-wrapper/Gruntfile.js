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
				src: ['src/main/resources/js/**/*.js']
			},

			tests: {
				// all testing files excluding libraries
				src: ['src/test/js/**/*.js', '!src/test/js/libs/**/*.js']
			}
		},

		uglify: {
			options: {
				banner: '<%= banner %>'
			},

			min: {
				files: [
					{
						src: ['src/main/resources/js/**/*.js'],
						dest: 'target/cdfplugin.min.js'
					}
				]
			}
		},

		concat: {
			options: {
				separator: ';'
			},
			
			dist: {
				src: ['src/main/resources/js/**/*.js'],
				dest: 'target/cdfplugin.js'
			}
		},

		qunit: {
			sample: {
				src: ['src/test/js/*Example.html']	
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
	grunt.registerTask('default', ['jshint:grunt', 'qunit', 'clean', 'uglify']);
};
