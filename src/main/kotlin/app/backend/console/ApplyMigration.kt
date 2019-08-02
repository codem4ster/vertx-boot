package app.backend.console

import io.ebean.Ebean

fun main(args: Array<String>) {
  System.setProperty("disableTestProperties", "true")
  System.setProperty("ddl.migration.run", "true")

  Ebean.getDefaultServer()
  println("done")
}
