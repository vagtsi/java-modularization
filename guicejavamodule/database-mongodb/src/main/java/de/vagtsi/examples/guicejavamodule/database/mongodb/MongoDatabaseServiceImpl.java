package de.vagtsi.examples.guicejavamodule.database.mongodb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.vagtsi.examples.guicejavamodule.database.core.DatabaseService;

public class MongoDatabaseServiceImpl implements DatabaseService {
  private static final Logger log = LoggerFactory.getLogger(MongoDatabaseServiceImpl.class.getSimpleName());

  public MongoDatabaseServiceImpl() {
    log.info("Created {} database service", databaseName());
  }

  @Override
  public String databaseName() {
    return "MongoDB";
  }

}
