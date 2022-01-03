module plugin.greeting.core {
  exports de.vagtsi.examples.guicejavamodule.greeting.core;
  requires plugin.api;
  requires transitive com.google.guice;
  requires java.inject;
  requires org.slf4j;
  
  provides com.google.inject.Module
    with de.vagtsi.examples.guicejavamodule.greeting.core.GreetingCoreModule;
}
