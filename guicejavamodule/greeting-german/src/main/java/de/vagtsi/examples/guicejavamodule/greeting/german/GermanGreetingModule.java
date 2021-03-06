package de.vagtsi.examples.guicejavamodule.greeting.german;

import javax.inject.Singleton;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.ProvidesIntoSet;
import de.vagtsi.examples.guicejavamodule.greeting.core.GreetingService;
import de.vagtsi.examples.guicejavamodule.greeting.german.internal.GermanGreetingServiceImpl;

public class GermanGreetingModule extends AbstractModule {
  
  @ProvidesIntoSet
  @Singleton
  public GreetingService germanGreetingService() {
    return new GermanGreetingServiceImpl();
  }
}
