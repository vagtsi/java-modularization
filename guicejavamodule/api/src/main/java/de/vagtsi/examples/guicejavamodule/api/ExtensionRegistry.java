package de.vagtsi.examples.guicejavamodule.api;

import java.util.List;

public interface ExtensionRegistry<T> {
	void registerExtension(T extension);
	void unregisterExtension(T extension);
	List<T> getAllExtensions();
	Class<T> getExtensionType();
}
