module plugin.database.core {
  exports de.vagtsi.examples.guicejavamodule.database.core;
  requires plugin.api;
  requires transitive com.google.guice;
  requires java.inject;
  requires org.slf4j;
  
  provides com.google.inject.Module
    with de.vagtsi.examples.guicejavamodule.database.core.DatabaseCoreModule;
}
