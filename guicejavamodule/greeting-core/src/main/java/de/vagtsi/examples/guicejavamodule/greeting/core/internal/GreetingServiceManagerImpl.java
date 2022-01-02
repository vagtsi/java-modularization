package de.vagtsi.examples.guicejavamodule.greeting.core.internal;

import java.util.List;

import javax.inject.Inject;

import de.vagtsi.examples.guicejavamodule.api.ExtensionRegistry;
import de.vagtsi.examples.guicejavamodule.greeting.core.GreetingService;
import de.vagtsi.examples.guicejavamodule.greeting.core.GreetingServiceManager;

public class GreetingServiceManagerImpl implements GreetingServiceManager {
  
  private ExtensionRegistry<GreetingService> _greetingServiceRegistry;

  @Inject
  public GreetingServiceManagerImpl(ExtensionRegistry<GreetingService> greetingServiceRegistry) {
   _greetingServiceRegistry = greetingServiceRegistry; 
  }

  @Override
  public List<GreetingService> getAllGreeterServices() {
    return _greetingServiceRegistry.getAllExtensions();
  }

}
