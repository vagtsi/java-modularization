module plugin.greeting.french {
  exports de.vagtsi.examples.guicejavamodule.greeting.french;
  requires plugin.api;
  requires transitive com.google.guice;
  requires java.inject;
  requires transitive plugin.greeting.core;
  requires org.slf4j;
  
  provides de.vagtsi.examples.guicejavamodule.api.PluginModule
    with de.vagtsi.examples.guicejavamodule.greeting.french.FrenchGreetingPlugin;
}
