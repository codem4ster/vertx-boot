package app.backend.console

import io.ebean.annotation.Platform
import io.ebean.dbmigration.DbMigration


fun main(args: Array<String>) {
  // optionally specify the version and name
  //System.setProperty("ddl.migration.version", "1.1");
  //System.setProperty("ddl.migration.name", "support end dating")

  // generate a migration using drops from a prior version
  //System.setProperty("ddl.migration.pendingDropsFor", "1.2");

  val dbMigration = DbMigration.create()
  dbMigration.setPlatform(Platform.POSTGRES)
  // generate the migration ddl and xml
  // ... with EbeanServer in "offline" mode
  dbMigration.generateMigration()
}
