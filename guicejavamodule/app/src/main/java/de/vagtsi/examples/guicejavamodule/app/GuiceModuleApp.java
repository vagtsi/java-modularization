package de.vagtsi.examples.guicejavamodule.app;

import java.lang.module.ModuleDescriptor;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.vagtsi.examples.guicejavamodule.api.ExtensionRegistry;
import de.vagtsi.examples.guicejavamodule.greeting.core.GreetingService;

public class GuiceModuleApp {
  private static final Logger log = LoggerFactory.getLogger(GuiceModuleApp.class.getSimpleName());

  public static void main(String[] args) {
    GuiceModuleApp app = new GuiceModuleApp();
    app.init();
  }

  private void init() {
    // load all plugins
    Map<String, PluginModule> pluginModules = PluginModuleLoader.loadPluginModules();

    // print out dependencies between plugins
    for (PluginModule pluginModule : pluginModules.values()) {
      ModuleDescriptor descriptor = pluginModule.javaModule().getDescriptor();
      List<PluginModule> dependencies = descriptor.requires().stream().map(r -> pluginModules.get(r.name())).filter(Objects::nonNull).collect(Collectors.toList());
      if (dependencies.isEmpty()) {
        log.info("> plugin module {} has no parent dependency", pluginModule);
      } else {
        if (dependencies.size() > 1) {
          log.warn("> plugin module {} has more {} instead of only one supported parent dependencies: {}! Using first one only!", pluginModule, dependencies.size(), dependencies);
        }
        pluginModule.setParentPlugin(dependencies.get(0));
        log.info("> plugin module {} has {} dependencies: {}", pluginModule, dependencies.size(), dependencies);
      }
    }

    // initialize all plugin modules (dependencies implicitly first)
    for (PluginModule pluginModule : pluginModules.values()) {
      pluginModule.initialize();
    }

    // call/print out all plugged in GreetingServices
    PluginModule coreModule = pluginModules.get("plugin.greeting.core");
    @SuppressWarnings("unchecked")
    ExtensionRegistry<GreetingService> registry = coreModule.extensionRegistries().iterator().next();
    List<GreetingService> services = registry.getAllExtensions();
    
    log.info("{} GreetingServices are registered:", services.size());
    for (GreetingService service : services) {
      log.info("> {}", service.hello());
    }
  }
}
