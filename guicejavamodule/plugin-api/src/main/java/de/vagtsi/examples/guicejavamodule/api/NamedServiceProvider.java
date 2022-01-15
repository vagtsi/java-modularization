package de.vagtsi.examples.guicejavamodule.api;

import javax.inject.Inject;
import javax.inject.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provider for retrieval of single named ("switched on") service instance. 
 */
public class NamedServiceProvider<T> implements Provider<T> {
  private final Logger log = LoggerFactory.getLogger(NamedServiceProvider.class);

  private NamedExtensionRegistry<T> namedServiceRegistry; 
  
  @Inject
  public NamedServiceProvider(NamedExtensionRegistry<T> namedServiceRegistry) {
    this.namedServiceRegistry = namedServiceRegistry;
  }

  @Override
  public T get() {
	String configuredName = System.getProperty(namedServiceRegistry.systemProperty());
	if (configuredName == null) {
		throw new IllegalStateException(String.format(
				"No named service configured with system property '%s'", namedServiceRegistry.systemProperty()));
	}
    log.info("Retrieving named service of confiured name [{}]", configuredName);
    return namedServiceRegistry.getExtensionByName(configuredName);
  }

}
