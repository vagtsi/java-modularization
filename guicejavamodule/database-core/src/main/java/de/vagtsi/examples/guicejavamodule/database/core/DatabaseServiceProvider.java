package de.vagtsi.examples.guicejavamodule.database.core;

import javax.inject.Inject;
import javax.inject.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.vagtsi.examples.guicejavamodule.api.NamedExtensionRegistry;

/**
 * Provider for retrieval of single named ("switched on") service instance. 
 */
public class DatabaseServiceProvider implements Provider<DatabaseService> {
  private static final Logger log = LoggerFactory.getLogger(DatabaseServiceProvider.class.getSimpleName());

  private final String databaseType = System.getProperty("database-type", "postgres");
  private NamedExtensionRegistry<DatabaseService> databaseServiceRegistry; 
  
  @Inject
  public DatabaseServiceProvider(NamedExtensionRegistry<DatabaseService> databaseServiceRegistry) {
    this.databaseServiceRegistry = databaseServiceRegistry;
  }

  @Override
  public DatabaseService get() {
    log.info("Retrieving database service of confiured type [{}]", databaseType);
    return databaseServiceRegistry.getExtensionByName(databaseType);
  }

}
