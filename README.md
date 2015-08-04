# fdc-mock-helper
Utility functions for mocking (classes)

This little library provides only one method: newInstanceMockDependencies(Class, Object...)

This methode creates a new object of type Class, mocking all constuctor dependencies with Mockito.
Optionally, constructor arguments can be supplied that are favored before mocking the arguments.



