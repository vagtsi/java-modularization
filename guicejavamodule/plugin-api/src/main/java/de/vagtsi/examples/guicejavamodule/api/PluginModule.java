package de.vagtsi.examples.guicejavamodule.api;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

public class PluginModule extends AbstractModule {
  
  /**
   * Bind a {@link ExtensionRegistry} implementation to this module. 
   * @param <T> the extension registry class type
   * @param registry the implementation instance of the registry to be registered
   */
  @SuppressWarnings("rawtypes")
  protected <T extends ExtensionRegistry> void bindExtensionRegistry(T registry) {
    // we bind the registry to a set in order to support multiple extension registries in this module!
    // please note: 'newSetBinder()' does not create a new set on each call but only if not already exists!
    Multibinder<ExtensionRegistry> registryBinder = Multibinder.newSetBinder(binder(), ExtensionRegistry.class);
    registryBinder.addBinding().toInstance(registry);
  }
}
