package app.backend.helpers

import io.vertx.core.json.JsonArray

fun <T>JsonArray.asList(): List<T> {
  @Suppress("UNCHECKED_CAST")
  return this.mapIndexed { index, _ ->
    this.getValue(index) as T
  }
}
