package de.vagtsi.examples.guicejavamodule.database.mongodb;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.ProvidesIntoMap;
import com.google.inject.multibindings.StringMapKey;
import de.vagtsi.examples.guicejavamodule.database.core.DatabaseService;

public class MongoDatabaseModule extends AbstractModule {

  @ProvidesIntoMap
  @StringMapKey("mongodb")
  public DatabaseService mongodbDatabaseService() {
    return new MongoDatabaseServiceImpl();
  }
}
