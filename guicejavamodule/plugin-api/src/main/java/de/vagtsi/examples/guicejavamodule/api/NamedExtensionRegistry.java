package de.vagtsi.examples.guicejavamodule.api;

import java.util.Set;
import javax.inject.Provider;

/**
 * Registry for multiple type instances acting as plugin extensions with unique name for each instance.
 * Additionally the extensions are initialized lazily on the very first retrieval access via {@link #getExtensionByName(String)}.
 *  
 * @param <T> type of the extension managed by this registry
 */
public interface NamedExtensionRegistry<T> {
  void registerExtension(String name, Provider<T> extension);
  void unregisterExtension(String name);
  Set<String> getAllExtensionNames();
  T getExtensionByName(String name);
  Class<T> getExtensionType();
  String systemProperty();
}
