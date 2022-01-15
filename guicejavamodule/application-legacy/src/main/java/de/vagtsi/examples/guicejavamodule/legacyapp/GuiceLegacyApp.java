package de.vagtsi.examples.guicejavamodule.legacyapp;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.vagtsi.examples.guicejavamodule.api.ExtensionRegistry;
import de.vagtsi.examples.guicejavamodule.api.NamedExtensionRegistry;
import de.vagtsi.examples.guicejavamodule.database.core.DatabaseService;
import de.vagtsi.examples.guicejavamodule.greeting.core.GreetingService;
import de.vagtsi.examples.guicejavamodule.plugin.core.Plugin;
import de.vagtsi.examples.guicejavamodule.plugin.core.PluginModuleLoader;

public class GuiceLegacyApp {
  private static final Logger log = LoggerFactory.getLogger(GuiceLegacyApp.class.getSimpleName());

  public static void main(String[] args) {
    GuiceLegacyApp app = new GuiceLegacyApp();
    app.init();
  }

  private void init() {
    // load all plugins (jars) dynamically from plugin directory
    Path projectDir = Paths.get(".").normalize().toAbsolutePath();
    Map<String, Plugin> pluginModules = PluginModuleLoader.loadPluginsFromDirectory(
        projectDir.resolve("../application/build/libs/plugins").normalize());

    // call/print out all plugged in GreetingServices
    Plugin coreModule = pluginModules.get("plugin.greeting.core");
    @SuppressWarnings("unchecked")
    ExtensionRegistry<GreetingService> registry = coreModule.extensionRegistries().iterator().next();
    List<GreetingService> services = registry.getAllExtensions();
    
    log.info("{} GreetingServices are registered:", services.size());
    for (GreetingService service : services) {
      log.info("> {}", service.hello());
    }

    // check named database services
    Plugin dbCoreModule = pluginModules.get("plugin.database.core");
    @SuppressWarnings("unchecked")
    NamedExtensionRegistry<DatabaseService> dbRegistry = dbCoreModule.namedExtensionRegistries().get("database");
    Set<String> names = dbRegistry.getAllExtensionNames();
    log.info("{} DatabaseServices are registered {}", names.size(), names);
    DatabaseService databaseService = dbCoreModule.getService(DatabaseService.class);
    log.info("> succesful retrieved database service {}", databaseService.databaseName());
  }
}