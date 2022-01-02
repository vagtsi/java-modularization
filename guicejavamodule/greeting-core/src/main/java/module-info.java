module plugin.greeting.core {
  exports de.vagtsi.examples.guicejavamodule.greeting.core;
  requires plugin.api;
  requires transitive com.google.guice;
  requires java.inject;
  requires org.slf4j;
  
  provides de.vagtsi.examples.guicejavamodule.api.PluginModule
    with de.vagtsi.examples.guicejavamodule.greeting.core.GreetingCorePlugin;
}
