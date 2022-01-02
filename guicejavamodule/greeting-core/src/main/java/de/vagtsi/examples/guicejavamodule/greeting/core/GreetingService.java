package de.vagtsi.examples.guicejavamodule.greeting.core;

/**
 * Example service defined in the parent plugin to be implemented by child plugins.
 */
public interface GreetingService {
  /**
   * Get hello message in the implemented language. 
   */
  String hello();
}
