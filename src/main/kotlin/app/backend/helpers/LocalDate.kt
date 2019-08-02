package app.backend.helpers

import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat

fun LocalDate.formatTr(): String? {
  val formatter =
    DateTimeFormat.forPattern("dd/MM/yyyy")
  return this.toString(formatter)!!

}
