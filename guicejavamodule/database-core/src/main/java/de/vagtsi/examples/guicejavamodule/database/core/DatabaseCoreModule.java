package de.vagtsi.examples.guicejavamodule.database.core;

import javax.inject.Singleton;

import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.ProvidesIntoMap;
import com.google.inject.multibindings.StringMapKey;

import de.vagtsi.examples.guicejavamodule.api.NamedExtensionRegistry;
import de.vagtsi.examples.guicejavamodule.api.NamedServiceProvider;
import de.vagtsi.examples.guicejavamodule.api.PluginModule;
import de.vagtsi.examples.guicejavamodule.api.SimpleNamedExtensionRegistry;

public class DatabaseCoreModule extends PluginModule {
  
  @Override
  protected void configure() {
    //register DatabaseService provider for retrieval of single named ("switched on") service instance
//    bind(DatabaseService.class).toProvider((Provider<? extends DatabaseService>) Key.get(TypeLiteral.get(
//    		Types.newParameterizedType(NamedServiceProvider.class, DatabaseService.class))));
    bind(DatabaseService.class).toProvider(new TypeLiteral<NamedServiceProvider<DatabaseService>>() {});
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
    return SimpleNamedExtensionRegistry.create(DatabaseService.class, "guicejavaapp.databasetype");
  }
  
}
