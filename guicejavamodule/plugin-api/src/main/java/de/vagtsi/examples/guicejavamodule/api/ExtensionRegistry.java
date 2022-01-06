package de.vagtsi.examples.guicejavamodule.api;

import java.util.List;

/**
 * Registry for multiple type instances acting as plugin extensions.
 *  
 * @param <T> type of the extension managed by this registry
 */
public interface ExtensionRegistry<T> {
	void registerExtension(T extension);
	void unregisterExtension(T extension);
	List<T> getAllExtensions();
	Class<T> getExtensionType();
}
