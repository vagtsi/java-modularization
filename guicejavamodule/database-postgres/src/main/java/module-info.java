module plugin.database.postgres {
  exports de.vagtsi.examples.guicejavamodule.database.postgres;
  requires plugin.api;
  requires transitive com.google.guice;
  requires java.inject;
  requires transitive plugin.database.core;
  requires org.slf4j;
  
  provides com.google.inject.Module
    with de.vagtsi.examples.guicejavamodule.database.postgres.PostgresDatabaseModule;
}
