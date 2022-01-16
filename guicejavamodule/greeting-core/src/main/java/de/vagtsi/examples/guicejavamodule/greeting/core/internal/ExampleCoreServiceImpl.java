package de.vagtsi.examples.guicejavamodule.greeting.core.internal;

import de.vagtsi.examples.guicejavamodule.greeting.core.ExampleCoreService;

/**
 * {@link ExampleCoreService} implementation in a not exported package to hide internals.
 */
public class ExampleCoreServiceImpl implements ExampleCoreService {

  @Override
  public String sayHello() {
    return "I'm a service of the core-module!";
  }
}
