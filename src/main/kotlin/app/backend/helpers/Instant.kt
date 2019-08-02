package app.backend.helpers

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

fun Instant.toIso8601(): String? {
  val date = Date.from(this)
  val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
  sdf.timeZone = TimeZone.getTimeZone("CET")
  return sdf.format(date)
}

fun Instant.formatTr(): String? {
  val formatter = DateTimeFormatter
    .ofLocalizedDateTime(FormatStyle.MEDIUM)
    .withLocale(Locale.forLanguageTag("tr-TR"))
    .withZone(ZoneId.of("Europe/Istanbul"))
  return formatter.format(this)
}

fun Instant.formatTrMini(): String? {
  val formatter = DateTimeFormatter
    .ofPattern("d MMMM yyyy")
    .withLocale(Locale.forLanguageTag("tr-TR"))
    .withZone(ZoneId.of("Europe/Istanbul"))
  return formatter.format(this)
}
