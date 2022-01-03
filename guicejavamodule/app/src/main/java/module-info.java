module plugin.app {
  requires com.google.guice;
  requires java.inject;
  requires org.slf4j;

  requires plugin.api;
  requires plugin.greeting.core;
  requires plugin.greeting.french;

  uses com.google.inject.Module;
}
