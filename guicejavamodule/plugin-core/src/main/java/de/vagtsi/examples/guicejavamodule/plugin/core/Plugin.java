package de.vagtsi.examples.guicejavamodule.plugin.core;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.inject.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Binding;
import com.google.inject.ConfigurationException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.util.Types;

import de.vagtsi.examples.guicejavamodule.api.ExtensionRegistry;
import de.vagtsi.examples.guicejavamodule.api.NamedExtensionRegistry;

/**
 * Instance of a plugin being a Java module using Guice as dependency injection framework.
 */
public class Plugin {
  private static final Logger log = LoggerFactory.getLogger(Plugin.class);

  private final Module javaModule;
  private final com.google.inject.Module guiceModule;
  private Injector injector;
  private Plugin parentPlugin;

  @SuppressWarnings("rawtypes")
  private Set<ExtensionRegistry> registries;

  @SuppressWarnings("rawtypes")
  private Set<NamedExtensionRegistry> namedRegistries;

  public Plugin(com.google.inject.Module module) {
    guiceModule = module;
    javaModule = module.getClass().getModule();
  }

  public boolean isInitialized() {
    return injector != null;
  }

  public String moduleName() {
    return javaModule.getName();
  }

  public Module javaModule() {
    return javaModule;
  }

  public void setParentPlugin(Plugin parentPlugin) {
    this.parentPlugin = parentPlugin;
  }

  @Override
  public String toString() {
    return moduleName();
  }

  /**
   * Retrieve the registry instance of given extension type.
   * @param extensionType the extension type of desired registry
   * @return the extension registry of given extension type or null if not exist in this plugin
   */
  @SuppressWarnings("unchecked")
  public <T> ExtensionRegistry<T> extensionRegistry(Class<T> extensionType) {
	 return registries.stream()
				.filter(r -> r.getExtensionType().equals(extensionType))
				.findFirst()
				.orElse(null);
  }
  
  /**
   * Retrieve the named registry instance of given extension type.
   * @param extensionType the extension type of desired registry
   * @return the named extension registry of given extension type or null if not exist in this plugin
   */
  @SuppressWarnings("unchecked")
  public <T> NamedExtensionRegistry<T> namedExtensionRegistry(Class<T> extensionType) {
	 return namedRegistries.stream()
				.filter(r -> r.getExtensionType().equals(extensionType))
				.findFirst()
				.orElse(null);
  }

  public void initialize() {
    if (isInitialized()) {
      return;
    }

    //retrieve Guice injector of parent module 
    //(ensures all parent dependencies are initialized first (recursive!))
    Injector parentInjector = parentPlugin != null ? parentPlugin.injector() : null;
    
    log.info("Initalizing injector for plugin module '{}':", moduleName());
    injector = parentInjector != null ? parentInjector.createChildInjector(guiceModule) : Guice.createInjector(guiceModule);
    log.info(bindingsStartupMessage(injector));

    // register all extensions (if any)
    if (parentPlugin != null) {
      registerExtensions();
    }
    
    //retrieve optional extension registries
    try {
      registries = injector.getInstance(Key.get(setOf(ExtensionRegistry.class)));
    } catch (ConfigurationException e) {
      log.debug("Plugin {} does not provide any extension registries", moduleName());
      registries = Collections.emptySet();
    }
    
    try {
      namedRegistries = injector.getInstance(Key.get(setOf(NamedExtensionRegistry.class)));
    } catch (ConfigurationException e) {
      log.debug("Plugin {} does not provide any named extension registries", moduleName());
      namedRegistries = Collections.emptySet();
    }

    log.info("> finished initialization of plugin module '{}'", moduleName());
  }

  public <T> T getService(Class<T> type) {
    return injector().getInstance(type);
  }

  // ---- private ----

  private Injector injector() {
    if (!isInitialized()) {
      initialize();
    }
    return injector;
  }

  @SuppressWarnings("rawtypes")
  private Set<ExtensionRegistry> extensionRegistries() {
    return registries;
  }

  @SuppressWarnings("rawtypes")
  private Set<NamedExtensionRegistry> namedExtensionRegistries() {
    return namedRegistries;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private void registerExtensions() {
    if (parentPlugin == null) {
      return;
    }
    
    if (!parentPlugin.extensionRegistries().isEmpty()) {
      log.info("Registering extensions to {} registries of parent plugin {}", parentPlugin.extensionRegistries().size(), parentPlugin);
      for (ExtensionRegistry registry : parentPlugin.extensionRegistries()) {
        Class<?> extensionType = registry.getExtensionType();
        Set<?> extensions = injector.getInstance(Key.get(setOf(extensionType)));
        if (!extensions.isEmpty()) {
          log.info("> registering {} extensions of type {} provided by plugin {} at parent plugin {}", extensions.size(), extensionType.getSimpleName(), moduleName(), parentPlugin);
          extensions.forEach(registry::registerExtension);
        } else {
          log.info("> no extensions of type {} provided by plugin {} for parent plugin {}", extensionType.getClass().getName(), moduleName(), parentPlugin);
        }
      }
    }

    if (!parentPlugin.namedExtensionRegistries().isEmpty()) {
      log.info("Registering named extensions to {} named registries of parent plugin {}", parentPlugin.namedExtensionRegistries().size(), parentPlugin);
      for (NamedExtensionRegistry registry : parentPlugin.namedExtensionRegistries()) {
        Class<?> extensionType = registry.getExtensionType();
        Map<String, ?> extensions = injector.getInstance(Key.get(mapOfProvider(extensionType)));
        if (!extensions.isEmpty()) {
          log.info("> registering {} named extensions of type {} provided by plugin {} at parent plugin {}", extensions.size(), extensionType.getSimpleName(), moduleName(), parentPlugin);
          extensions.forEach((k, v) -> registry.registerExtension(k, (Provider) v));
        } else {
          log.info("> no extensions of type {} provided by plugin {} for parent plugin {}", extensionType.getClass().getName(), moduleName(), parentPlugin);
        }
      }
    }
  }

  private String bindingsStartupMessage(Injector injector) {
    Map<Key<?>, Binding<?>> bindings = injector.getAllBindings();

    StringBuilder sb = new StringBuilder(System.lineSeparator());
    sb.append(String.format("--------- Dependency injection for %s initialised with %d bindings -----------%n", moduleName(), bindings.size()));

    bindings.forEach((k, v) -> {
      sb.append("\t").append(k.getTypeLiteral());
      if (k.getAnnotationType() != null) {
        sb.append(" (annotation=").append(k.getAnnotation()).append(")");
      }
      sb.append(System.lineSeparator());
    });

    sb.append("---------------------------------------------------------------------------------");
    return sb.toString();
  }

  @SuppressWarnings("unchecked")
  private static <T> TypeLiteral<Set<T>> setOf(Class<T> type) {
    return (TypeLiteral<Set<T>>) TypeLiteral.get(Types.setOf(type));
  }

  @SuppressWarnings("unchecked")
  private static <T> TypeLiteral<Map<String, Provider<T>>> mapOfProvider(Class<T> type) {
    return (TypeLiteral<Map<String, Provider<T>>>) TypeLiteral.get(Types.mapOf(String.class, Types.javaxProviderOf(type)));
  }

}
