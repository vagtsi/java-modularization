package de.vagtsi.examples.guicejavamodule.database.postgres.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.vagtsi.examples.guicejavamodule.database.core.DatabaseService;

public class PostgresDatabaseServiceImpl implements DatabaseService {
  private static final Logger log = LoggerFactory.getLogger(PostgresDatabaseServiceImpl.class);

  public PostgresDatabaseServiceImpl() {
    log.info("Created {} database service", databaseName());
  }

  @Override
  public String databaseName() {
    return "PostgreSQL";
  }

}
