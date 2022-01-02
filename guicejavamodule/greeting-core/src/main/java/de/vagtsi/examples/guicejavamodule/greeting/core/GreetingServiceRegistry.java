package de.vagtsi.examples.guicejavamodule.greeting.core;

public interface GreetingServiceRegistry {

  void registerService(GreetingService service);
  void unregisterService(GreetingService service);
  
}
