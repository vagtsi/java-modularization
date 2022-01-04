package de.vagtsi.examples.guicejavamodule.app;

import java.lang.module.ModuleDescriptor;
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
  
  private PluginModuleLoader() {
    //utility
  }

  /**
   * Load all Guice plugin modules via service loader according to provided the type {@link Module}. 
   * @return map of all found modules in the module path with module name as key
   */
  public static Map<String, PluginModule> loadPluginModules() {
    long start = System.currentTimeMillis();
    log.info("Scanning for all plugin modules");
    ServiceLoader<Module> pluginLoader = ServiceLoader.load(com.google.inject.Module.class);
    Map<String, PluginModule> pluginModules = pluginLoader.stream()
        .map(Provider::get)
        .map(PluginModule::new)
        .collect(Collectors.toConcurrentMap(PluginModule::moduleName, Function.identity()));

    log.info("> found {} plugin modules: {}", pluginModules.size(), pluginModules.keySet());
    
    resolveDependencies(pluginModules);
    
    // initialize all plugin modules (dependencies implicitly first)
    for (PluginModule pluginModule : pluginModules.values()) {
      pluginModule.initialize();
    }
    
    long duration = System.currentTimeMillis() - start;
    log.info("> finished initialization of {} plugins within {} ms",
        pluginModules.size(), duration);
    
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
