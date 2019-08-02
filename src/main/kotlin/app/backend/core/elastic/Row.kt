package app.backend.core.elastic

import io.vertx.core.json.JsonObject

data class Row(
  val score: Double?,
  val source: JsonObject?
)
