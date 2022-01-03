package de.vagtsi.examples.guicejavamodule.app;

import java.util.Map;
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

  public static Map<String, PluginModule> loadPluginModules() {
    log.info("Scanning for all plugin modules");
    ServiceLoader<Module> pluginLoader = ServiceLoader.load(com.google.inject.Module.class);
    Map<String, PluginModule> pluginModules = pluginLoader.stream().map(Provider::get).map(m -> new PluginModule(m))
            .collect(Collectors.toConcurrentMap(PluginModule::moduleName, Function.identity()));

    log.info("> found {} plugin modules: {}", pluginModules.size(), pluginModules.keySet());
    
    return pluginModules;
  }
}
