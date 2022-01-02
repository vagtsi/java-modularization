package de.vagtsi.examples.guicejavamodule.api;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple implementation of a {@link ExtensionRegistry} tracking any registered
 * extension of type T.
 */
public class SimpleExtensionRegistry<T> implements ExtensionRegistry<T> {
	private final Logger log = LoggerFactory.getLogger(getClass().getName());

    private final Class<T> type;
	protected Set<T> registeredExtensions = ConcurrentHashMap.newKeySet();
	
	public SimpleExtensionRegistry(Class<T> type) {
		this.type = type;
	}

	@Override
	public void registerExtension(T extension) {
		if (!registeredExtensions.contains(extension)) {
			registeredExtensions.add(extension);
			log.info("Registered new extension [{}]", extension.getClass().getSimpleName());
		} else {
			log.warn("Rejecting registration attempt of already registered extension [{}]",
					extension.getClass().getSimpleName());
		}
	}

	@Override
	public void unregisterExtension(T extension) {
		boolean removed = registeredExtensions.remove(extension);
		if (removed) {
			log.info("Un-registered extension [{}]", extension.getClass().getSimpleName());
		} else {
			log.warn("Rejecting un-registration of not registered extension [{}]",
					extension.getClass().getSimpleName());
		}
	}

	@Override
	public List<T> getAllExtensions() {
		return List.copyOf(registeredExtensions);
	}

	@Override
	public Class<T> getExtensionType() {
		return type;
	}

	public static <T> SimpleExtensionRegistry<T> create(Class<T> type) {
		return new SimpleExtensionRegistry<>(type);
	}

}
