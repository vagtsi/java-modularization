module plugin.greeting.german {
  exports de.vagtsi.examples.guicejavamodule.greeting.german;
  requires plugin.api;
  requires transitive com.google.guice;
  requires java.inject;
  requires transitive plugin.greeting.core;
  requires org.slf4j;
  
  provides com.google.inject.Module
    with de.vagtsi.examples.guicejavamodule.greeting.german.GermanGreetingModule;
}
