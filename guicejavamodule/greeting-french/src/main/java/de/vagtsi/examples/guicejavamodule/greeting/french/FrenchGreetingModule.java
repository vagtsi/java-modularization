package de.vagtsi.examples.guicejavamodule.greeting.french;

import javax.inject.Singleton;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.ProvidesIntoSet;

import de.vagtsi.examples.guicejavamodule.greeting.core.GreetingService;

public class FrenchGreetingModule extends AbstractModule {
  
  @ProvidesIntoSet
  @Singleton
  public GreetingService frenchGreetingService() {
    return new FrenchGreetingServiceImpl();
  }
}
