package de.vagtsi.examples.guicejavamodule.database.postgres;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.ProvidesIntoMap;
import com.google.inject.multibindings.StringMapKey;
import de.vagtsi.examples.guicejavamodule.database.core.DatabaseService;
import de.vagtsi.examples.guicejavamodule.database.postgres.internal.PostgresDatabaseServiceImpl;

public class PostgresDatabaseModule extends AbstractModule {

  @ProvidesIntoMap
  @StringMapKey("postgres")
  public DatabaseService postgresDatabaseService() {
    return new PostgresDatabaseServiceImpl();
  }

}
