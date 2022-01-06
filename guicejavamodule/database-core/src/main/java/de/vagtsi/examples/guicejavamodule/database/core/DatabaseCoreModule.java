package de.vagtsi.examples.guicejavamodule.database.core;

import javax.inject.Singleton;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.multibindings.ProvidesIntoMap;
import com.google.inject.multibindings.StringMapKey;
import de.vagtsi.examples.guicejavamodule.api.NamedExtensionRegistry;
import de.vagtsi.examples.guicejavamodule.api.SimpleNamedExtensionRegistry;

public class DatabaseCoreModule extends AbstractModule {
  
  @Override
  protected void configure() {
    //register DatabaseService provider for retrieval of single named ("switched on") service instance
    bind(DatabaseService.class).toProvider(DatabaseServiceProvider.class);
  }
  
  @SuppressWarnings({"rawtypes", "exports"})
  @Singleton
  @ProvidesIntoMap
  @StringMapKey("database")
  public NamedExtensionRegistry databaseServiceRegistryMapEntry(NamedExtensionRegistry<DatabaseService> databaseServiceRegistry) {
    return databaseServiceRegistry;
  }
  
  @Provides
  @Singleton
  public NamedExtensionRegistry<DatabaseService> databaseServiceRegistry() {
    return SimpleNamedExtensionRegistry.create(DatabaseService.class);
  }
  
}
