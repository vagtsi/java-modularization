module application {
  requires com.google.guice;
  requires java.inject;
  requires org.slf4j;

  requires plugin.api;
  requires plugin.core;
  requires plugin.greeting.core;
//  requires plugin.greeting.french;
//  requires plugin.greeting.english;
//  requires plugin.greeting.german;
  
  requires plugin.database.core;
//  requires plugin.database.mongodb;
//  requires plugin.database.postgres;

}
