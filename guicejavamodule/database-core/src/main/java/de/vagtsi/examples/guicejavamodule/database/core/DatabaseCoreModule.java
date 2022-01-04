package de.vagtsi.examples.guicejavamodule.database.core;

import javax.inject.Singleton;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.ProvidesIntoMap;
import com.google.inject.multibindings.StringMapKey;
import de.vagtsi.examples.guicejavamodule.api.NamedExtensionRegistry;
import de.vagtsi.examples.guicejavamodule.api.SimpleNamedExtensionRegistry;

public class DatabaseCoreModule extends AbstractModule {
  @SuppressWarnings({"rawtypes", "exports"})
  @Singleton
  @ProvidesIntoMap
  @StringMapKey("database")
  public NamedExtensionRegistry databaseServiceRegistry() {
    return SimpleNamedExtensionRegistry.create(DatabaseService.class);
  }
}
