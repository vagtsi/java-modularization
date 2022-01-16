package de.vagtsi.examples.guicejavamodule.database.core;

import de.vagtsi.examples.guicejavamodule.api.PluginModule;
import de.vagtsi.examples.guicejavamodule.api.SimpleNamedExtensionRegistry;

public class DatabaseCoreModule extends PluginModule {
  
  @Override
  protected void configure() {
	//create and register named extension registry
	bindExtensionRegistry(SimpleNamedExtensionRegistry.create(DatabaseService.class, "guicejavaapp.databasetype"));
  }
}
