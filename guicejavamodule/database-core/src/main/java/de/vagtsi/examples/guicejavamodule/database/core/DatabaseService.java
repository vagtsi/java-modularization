package de.vagtsi.examples.guicejavamodule.database.core;

/**
 * Example service defined in the parent plugin to be implemented by child plugins.
 */
public interface DatabaseService {
  /**
   * Expose the database implementation name. 
   */
  String databaseName();
}
