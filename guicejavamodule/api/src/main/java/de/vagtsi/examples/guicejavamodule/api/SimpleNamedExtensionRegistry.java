package de.vagtsi.examples.guicejavamodule.api;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.inject.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple implementation of a {@link NamedExtensionRegistry} tracking any registered extension of type T with its unique name.
 */
public class SimpleNamedExtensionRegistry<T> implements NamedExtensionRegistry<T> {
  private final Logger log = LoggerFactory.getLogger(getClass().getName());

  private final Class<T> type;
  protected ConcurrentHashMap<String, Provider<T>> registeredExtensions = new ConcurrentHashMap<>();
  protected ConcurrentHashMap<String, T> initializedExtensions = new ConcurrentHashMap<>();

  public SimpleNamedExtensionRegistry(Class<T> type) {
    this.type = type;
  }

  @Override
  public void registerExtension(String name, Provider<T> extension) {
    if (!registeredExtensions.contains(name)) {
      registeredExtensions.put(name, extension);
      log.info("Registered new extension '{}'", name);
    } else {
      log.warn("Rejecting registration attempt of already registered extension '{}'", name);
    }
  }

  @Override
  public void unregisterExtension(String name) {
    Provider<T> removed = registeredExtensions.remove(name);
    if (removed != null) {
      log.info("Un-registered extension '{}'", name);
    } else {
      log.warn("Rejecting un-registration of not registered extension '{}'", name);
    }
  }

  @Override
  public Set<String> getAllExtensionNames() {
    return registeredExtensions.keySet();
  }

  @Override
  public T getExtensionByName(String name) {
    return initializedExtensions.computeIfAbsent(name, this::initializeExtension);
  }

  @Override
  public Class<T> getExtensionType() {
    return type;
  }

  public static <T> SimpleNamedExtensionRegistry<T> create(Class<T> type) {
    return new SimpleNamedExtensionRegistry<>(type);
  }

  // --- private ---
  
  private T initializeExtension(String name) {
    Provider<T> provider = registeredExtensions.get(name);
    if (provider != null) {
      log.info("Initializing extension '{}'", name);
      return  provider.get();
    } else {
      throw new IllegalArgumentException(String.format(
          "Failed to retrieve extension with name '%s'", name));
    }
  }


}
