package app.backend.helpers

import app.backend.SeedTestData
import app.backend.utils.ScriptRunner
import io.ebean.DB

object DropCreate {
  fun run() {
    val transaction = DB.beginTransaction()
    val conn = transaction.connection
    val runner = ScriptRunner(conn, false, false)
    runner.runScript("db-drop-all.sql")
    runner.runScript("db-create-all.sql")
//    runner.runScript("src/test/resources/seed.sql")
    transaction.commit()
    SeedTestData.run()
  }
}
