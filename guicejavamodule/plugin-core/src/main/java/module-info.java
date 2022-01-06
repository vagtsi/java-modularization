module plugin.core {
  exports de.vagtsi.examples.guicejavamodule.plugin.core;
  requires transitive plugin.api;
  requires transitive com.google.guice;
  requires transitive org.slf4j;
  requires transitive java.inject;
}