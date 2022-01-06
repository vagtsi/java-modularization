module plugin.api {
  exports de.vagtsi.examples.guicejavamodule.api;
  requires transitive com.google.guice;
  requires transitive org.slf4j;
  requires transitive java.inject;
}