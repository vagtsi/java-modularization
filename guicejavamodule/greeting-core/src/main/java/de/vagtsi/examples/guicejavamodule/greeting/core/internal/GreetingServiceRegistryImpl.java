package de.vagtsi.examples.guicejavamodule.greeting.core.internal;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.vagtsi.examples.guicejavamodule.greeting.core.GreetingService;
import de.vagtsi.examples.guicejavamodule.greeting.core.GreetingServiceRegistry;

/**
 * Implementation of {@link GreetingServiceRegistry} tracking the registered {@link GreetingService} from Guice. 
 */
public class GreetingServiceRegistryImpl implements GreetingServiceRegistry {
  private static final Logger log = LoggerFactory.getLogger(GreetingServiceRegistryImpl.class.getSimpleName());

  private Set<GreetingService> registeredServices = ConcurrentHashMap.newKeySet();
  
  @Override
  public void registerService(GreetingService service) {
    if (!registeredServices.contains(service)) {
      registeredServices.add(service);
      log.info("Registered new service [{}]", service.getClass().getSimpleName());
    } else {
      log.warn("Rejecting registration attempt of already registered service [{}]", service.getClass().getSimpleName());
    }
  }

  @Override
  public void unregisterService(GreetingService service) {
   boolean removed = registeredServices.remove(service);
   if (removed) {
      log.info("Un-registered service [{}]", service.getClass().getSimpleName());
    } else {
      log.warn("Rejecting un-registration of not registered service [{}]", service.getClass().getSimpleName());
    }
  }
  
  List<GreetingService> getAllGreeterServices() {
    return List.copyOf(registeredServices);
  }

}
