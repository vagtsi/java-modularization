/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package de.vagtsi.examples.guicejavamodule.api;

import com.google.inject.Module;

/**
 * Definition of a plugin providing a Guice module.
 */
public interface PluginModule {

  /**
   * Unique id of the plugin.
   * @return String identifying the plugin, never null or empty 
   */
  String id();

  /**
   * Guice module providing all services of the plugin
   * to be initialized in a dedicated injector.
   * @return the Guice module instance of this plugin, never null
   */
  Module module();

  /**
   * List of all parent plugin ids this plugin is depending on.
   * Minimal implicit dependency is always the platform system.
   * @return all parent plugin ids, never null
   */
  default String[] parentIds() {
    return new String[0];
  }
}
