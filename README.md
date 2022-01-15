# java-modularization
Examples using different frameworks for creating modular java applications. The modularisation requirements are:
- modules must be able to hide internals and expose public
- modules have to define their dependencies (to other modules) explicitly
- modules provide local services defined by plain java interfaces

## Example 1: Guice Java Module Plugin example
The project [Guice Java Module Plugin example](guicejavamodule/) exposes [Guice](https://github.com/google/guice) dependency injection modules together with Java modules [JPMS/Java Platform Modukle System](https://en.wikipedia.org/wiki/Java_Platform_Module_System) in order to create a modular application, which even supports extending defined service interfaces.

## Example 2: OSGI (in development)
The project [OSGi Bundle example](osgibundle/) implemments the same scenario shown in example 1 as OSGi bundles [OSGi](https://www.osgi.org/) exposing OSGi services.