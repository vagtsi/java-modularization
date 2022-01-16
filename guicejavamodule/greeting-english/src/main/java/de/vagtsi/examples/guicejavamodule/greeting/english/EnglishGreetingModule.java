package de.vagtsi.examples.guicejavamodule.greeting.english;

import javax.inject.Singleton;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.ProvidesIntoSet;

import de.vagtsi.examples.guicejavamodule.greeting.core.ExampleCoreService;
import de.vagtsi.examples.guicejavamodule.greeting.core.GreetingService;
import de.vagtsi.examples.guicejavamodule.greeting.english.internal.EnglishGreetingServiceImpl;

public class EnglishGreetingModule extends AbstractModule {
  
  //provide extension which requires service from parent injector
  @ProvidesIntoSet
  @Singleton
  public GreetingService englishGreetingService(ExampleCoreService exampleCoreService) {
    return new EnglishGreetingServiceImpl(exampleCoreService);
  }
}
