module plugin.greeting.english {
  exports de.vagtsi.examples.guicejavamodule.greeting.english;
  requires plugin.api;
  requires transitive com.google.guice;
  requires java.inject;
  requires transitive plugin.greeting.core;
  requires org.slf4j;
  
  provides com.google.inject.Module
    with de.vagtsi.examples.guicejavamodule.greeting.english.EnglishGreetingModule;
}
