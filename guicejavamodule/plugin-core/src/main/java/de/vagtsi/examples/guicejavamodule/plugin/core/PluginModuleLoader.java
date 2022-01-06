package de.vagtsi.examples.guicejavamodule.plugin.core;

import java.lang.module.Configuration;
import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.inject.Module;

/**
 * Utility class for loading all Java Guice plugin modules from class (module) path. 
 */
public class PluginModuleLoader {
  private static final Logger log = LoggerFactory.getLogger(PluginModuleLoader.class.getSimpleName());
  
  /**
   * Load all Guice plugin modules dynamically via service loader according to provided the type {@link Module}
   * from the given file system directory.
   * @return map of all found modules in the directory with module name as key
   */
  public static Map<String, PluginModule> loadPluginsFromDirectory(Path pluginsDir) {
    long start = System.currentTimeMillis();
    log.info("Scanning for all plugin modules in directory {}", pluginsDir);

    // search for all modules in the plugins directory
    ModuleFinder pluginsFinder = ModuleFinder.of(pluginsDir);
    
    // retrieve all names of all found plugin modules
    List<String> plugins = pluginsFinder
            .findAll()
            .stream()
            .map(ModuleReference::descriptor)
            .map(ModuleDescriptor::name)
            .collect(Collectors.toList());
    log.info("> found {} plugin modules: {}", plugins.size(), plugins);
    
    // create configuration for resolve plugin modules (resolved the module graph)
    // note: we are using the jav runtime boot layer as parent!
    Configuration pluginsConfiguration = ModuleLayer.boot()
        .configuration()
        .resolveAndBind(pluginsFinder, ModuleFinder.of(), plugins);

    // create the new module layer for the plugins
    ModuleLayer layer = ModuleLayer
            .boot()
            .defineModulesWithOneLoader(pluginsConfiguration, ClassLoader.getSystemClassLoader());

    ServiceLoader<Module> pluginLoader = ServiceLoader.load(layer, com.google.inject.Module.class);
    
    // finally: load the guice modules (services) from the plugins and initialize all injectors
    Map<String, PluginModule> pluginModules = initializePluginModules(pluginLoader);
    
    long duration = System.currentTimeMillis() - start;
    log.info("> finished initialization of {} plugins within {} ms",
        pluginModules.size(), duration);
    
    return pluginModules;
  }

  /**
   * Load all Guice plugin modules via service loader according to provided the type {@link Module}. 
   * @return map of all found modules in the module path with module name as key
   */
  public static Map<String, PluginModule> loadPluginsFromModulePath() {
    long start = System.currentTimeMillis();
    log.info("Scanning for all plugin modules in module path");
    ServiceLoader<Module> pluginLoader = ServiceLoader.load(com.google.inject.Module.class);
    Map<String, PluginModule> pluginModules = initializePluginModules(pluginLoader);
    
    long duration = System.currentTimeMillis() - start;
    log.info("> finished initialization of {} plugins within {} ms",
        pluginModules.size(), duration);
    
    return pluginModules;
  }
  
  // ----  private ----

  private PluginModuleLoader() {
    //utility
  }

  /**
   * Initialize all Guice plugin modules retrieved via service loader.
   * @param loader the plaugin (servive) loader for retrieving the Guice modules
   * @return all initialized plugin modules, ready to be used
   */
  private static Map<String, PluginModule> initializePluginModules(ServiceLoader<Module> loader) {
    Map<String, PluginModule> pluginModules = loader.stream()
        .map(Provider::get)
        .map(PluginModule::new)
        .collect(Collectors.toConcurrentMap(PluginModule::moduleName, Function.identity()));

    log.info("> found {} plugin modules in module path: {}", pluginModules.size(), pluginModules.keySet());
    
    resolveDependencies(pluginModules);
    
    // initialize all plugin modules (dependencies implicitly first)
    for (PluginModule pluginModule : pluginModules.values()) {
      pluginModule.initialize();
    }
    
    return pluginModules;
  }

  /**
   * Resolve plugin dependcies according to the java module requirements 
   */
  private static void resolveDependencies(Map<String, PluginModule> pluginModules) {
    for (PluginModule pluginModule : pluginModules.values()) {
      ModuleDescriptor descriptor = pluginModule.javaModule().getDescriptor();
      List<PluginModule> dependencies = descriptor.requires().stream()
          .map(r -> pluginModules.get(r.name()))
          .filter(Objects::nonNull)
          .collect(Collectors.toList());
      if (dependencies.isEmpty()) {
        log.info("> plugin module {} has no parent dependency", pluginModule);
      } else {
        if (dependencies.size() > 1) {
          log.warn("> plugin module {} has more {} instead of only one supported parent dependencies: {}! Using first one only!", pluginModule, dependencies.size(), dependencies);
        }

        //assign parent plugin dependency to the module instance
        pluginModule.setParentPlugin(dependencies.get(0));
        
        log.info("> plugin module {} has {} dependencies: {}", pluginModule, dependencies.size(), dependencies);
      }
    }
  }
}
