[![Build Status](https://travis-ci.org/freiheit-com/fdc-mock-helper.svg?branch=master)](https://travis-ci.org/freiheit-com/fdc-mock-helper)

# fdc-mock-helper
Utility functions for mocking (classes)

This little library provides only one method: newInstanceMockDependencies(Class, Object...)

This methode creates a new object of type Class, mocking all constuctor dependencies with Mockito.
Optionally, constructor arguments can be supplied that are favored before mocking the arguments.



