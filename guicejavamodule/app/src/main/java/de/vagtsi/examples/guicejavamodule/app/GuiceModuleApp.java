package de.vagtsi.examples.guicejavamodule.app;

import java.util.List;
import java.util.Map;
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
