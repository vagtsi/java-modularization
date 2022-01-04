package de.vagtsi.examples.guicejavamodule.greeting.core;

import javax.inject.Singleton;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.ProvidesIntoSet;
import de.vagtsi.examples.guicejavamodule.api.ExtensionRegistry;
import de.vagtsi.examples.guicejavamodule.api.SimpleExtensionRegistry;

public class GreetingCoreModule extends AbstractModule {
  @SuppressWarnings({"rawtypes", "exports"})
  @ProvidesIntoSet
  @Singleton
  public ExtensionRegistry greetingServiceRegistry() {
    return SimpleExtensionRegistry.create(GreetingService.class);
  }
}
