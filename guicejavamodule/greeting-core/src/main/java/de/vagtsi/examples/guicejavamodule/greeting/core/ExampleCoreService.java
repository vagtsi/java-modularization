package de.vagtsi.examples.guicejavamodule.greeting.core;

/**
 * Simple example service to be injected into child plugins/injectors.
 */
public class ExampleCoreService {

  public String sayHello() {
    return "I'm a service of the core-module!";
  }
}
