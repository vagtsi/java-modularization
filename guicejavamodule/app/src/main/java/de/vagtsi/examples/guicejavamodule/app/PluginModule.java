package de.vagtsi.examples.guicejavamodule.app;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Binding;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.util.Types;

import de.vagtsi.examples.guicejavamodule.api.ExtensionRegistry;

/**
 * Instance of a Java (plugin) module using Guice as dependency injection framework.
 */
public class PluginModule {
  private static final Logger log = LoggerFactory.getLogger(PluginModule.class.getSimpleName());

  private final Module javaModule;
  private final com.google.inject.Module guiceModule;
  private Injector injector;
  private PluginModule parentPlugin;

  public PluginModule(com.google.inject.Module module) {
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

  public void setParentPlugin(PluginModule parentPlugin) {
    this.parentPlugin = parentPlugin;
  }

  @Override
  public String toString() {
    return moduleName();
  }

  public Set<ExtensionRegistry> extensionRegistries() {
    if (!isInitialized()) {
      throw new IllegalStateException("Module not initialized!");
    }

    return injector.getInstance(Key.get(setOf(ExtensionRegistry.class)));
  }

  public void initialize() {
    if (isInitialized()) {
      return;
    }

    // retrieve all extension service registries of parent plugin
    // (ensures all parent dependencies are initialized first (recursive!))
    Injector parentInjector = null;
    @SuppressWarnings("rawtypes")
    Set<ExtensionRegistry> registries = null;
    if (parentPlugin != null) {
      log.info("Retrieving extension registries of parent plugin '{}'", parentPlugin);
      parentInjector = parentPlugin.injector();
      registries = parentInjector.getInstance(Key.get(setOf(ExtensionRegistry.class)));
      log.info("> retrieved {} extension registries from parent plugin '{}'", registries.size(), parentPlugin);
    }

    log.info("Initalizing injector for plugin module '{}':", moduleName());
    injector = parentInjector != null ? parentInjector.createChildInjector(guiceModule) : Guice.createInjector(guiceModule);
    log.info(bindingsStartupMessage(injector));

    // register all extensions (if any)
    if (registries != null) {
      log.info("Registering extensions to {} registries of parent plugin {}", registries.size(), parentPlugin);
      for (ExtensionRegistry registry : registries) {
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

    log.info("> finished initialization of plugin module '{}'", moduleName());
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

  // ---- private ----
  @SuppressWarnings("unchecked")
  private static <T> TypeLiteral<Set<T>> setOf(Class<T> type) {
    return (TypeLiteral<Set<T>>) TypeLiteral.get(Types.setOf(type));
  }

  private Injector injector() {
    if (!isInitialized()) {
      initialize();
    }
    return injector;
  }


}
