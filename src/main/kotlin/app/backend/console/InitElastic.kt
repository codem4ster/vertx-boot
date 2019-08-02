package app.backend.console

import app.backend.core.elastic.Client
import app.backend.core.jsonObj
import app.backend.core.elastic.index
import io.vertx.core.Vertx
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) {
  runBlocking {
    val vertx = Vertx.vertx()
    val client = Client(vertx = vertx, debug = true)
    createIndex(client)

    vertx.close()
  }
}

suspend fun createIndex(client: Client) {
  val nesBlog = client.index("nes_blog")
  println("-----------  CREATE INDEX WITH MAPPING -----------------")
  println("Getting Indices : ")
  val response = client.get("_cat/indices?v")
  if (response.bodyAsString().contains("nes_blog")) {
    println("Deleting `nes_blog` : ")
    nesBlog.delete("?pretty")
  }
  println("Creating `nes_blog` with mapping: ")
  nesBlog.put("?pretty", jsonObj {
    "settings" to obj {
      "analysis" to obj {
        "filter" to obj {
          "turkish_stop" to obj { "type" to "stop"; "stopwords" to "_turkish_" }
          "turkish_lowercase" to obj { "type" to "lowercase"; "language" to "turkish" }
          "turkish_stemmer" to obj { "type" to "stemmer"; "language" to "turkish" }
        }
        "analyzer" to obj {
          "turkish_html" to obj {
            "type" to "custom"
            "tokenizer" to "standard"
            "char_filter" to arr["html_strip"]
            "filter" to arr[
              "apostrophe",
              "turkish_lowercase",
              "turkish_stop",
              "turkish_stemmer"
            ] }}}}
    "mappings" to obj {
      "properties" to obj {
        "content" to obj {
          "type" to "text"
          "analyzer" to "turkish_html" }}}
  })
}
