package de.vagtsi.examples.guicejavamodule.database.core;

import de.vagtsi.examples.guicejavamodule.api.PluginModule;
import de.vagtsi.examples.guicejavamodule.api.SimpleNamedExtensionRegistry;

public class DatabaseCoreModule extends PluginModule {
  
 public static final String DATABASETYPE_PROPNAME = "guicejavaapp.databasetype";

@Override
  protected void configure() {
	//create and register named extension registry
	bindExtensionRegistry(SimpleNamedExtensionRegistry.create(DatabaseService.class, DATABASETYPE_PROPNAME));
  }
}
