(function () {
	'use strict';

	var runTests = function () {
		module("Test Example", {
			setup: function() {
				// here we can create custom elements that we can append to the page
				$("body").append('<div id="someElement"></div>');
			},
			teardown: function() {
				// remove the elements created in 'setup' function
				$("#someElement").remove();
			}
		});

		test("Variable value is correct", function() {
			expect(1);

			equal(1, 1, 'value is correct');
		});

		test("Check some attribute", function() {
			expect(2);

			notEqual(window, undefined, 'the variable is defined');
			equal('John Doe', 'John Doe', 'check some attribute');
		});

		test("Check if the element is visible.", function() {
			expect(1);

			equal($("#someElement").attr('id'), 'someElement', 'check if we have an element');
		});
	};

	runTests();
})();
