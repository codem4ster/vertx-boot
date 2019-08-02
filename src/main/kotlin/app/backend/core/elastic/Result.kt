package app.backend.core.elastic

import app.backend.helpers.asList
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject

class Result(private val elasticResponse: String) {

  fun hits(): List<Row> {
    val result = Json.decodeValue(elasticResponse) as JsonObject
    return result.getJsonObject("hits")
      .getJsonArray("hits")
      .asList<JsonObject>()
      .map { it.run { Row(
        getDouble("_score"),
        getJsonObject("_source")
      ) } }
  }
}
