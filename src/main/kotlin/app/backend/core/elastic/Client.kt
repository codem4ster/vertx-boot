package app.backend.core.elastic

import io.vertx.core.Vertx
import io.vertx.core.buffer.Buffer
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.client.HttpRequest
import io.vertx.ext.web.client.HttpResponse
import io.vertx.ext.web.client.WebClient
import io.vertx.kotlin.ext.web.client.sendAwait
import io.vertx.kotlin.ext.web.client.sendJsonObjectAwait

class Client(
  private val address: String = "http://127.0.0.1:9200",
  vertx: Vertx,
  val debug: Boolean = false
) {
  private val httpClient = WebClient.create(vertx)

  private suspend fun request(
    uri: String,
    body: JsonObject? = null,
    middle: (url: String) -> HttpRequest<Buffer>
  ): HttpResponse<Buffer>
  {
    val url = "$address/$uri"
    val getReq = middle(url)
    val response = when (body) {
      null -> getReq.sendAwait()
      else -> getReq.sendJsonObjectAwait(body)
    }
    if (debug) println(response.bodyAsString())
    return response
  }

  suspend fun get(
    uri: String, body: JsonObject? = null
  ): HttpResponse<Buffer> = request(uri, body) { httpClient.getAbs(it) }

  suspend fun put(
    uri: String, body: JsonObject? = null
  ): HttpResponse<Buffer> = request(uri, body) { httpClient.putAbs(it) }

  suspend fun delete(
    uri: String, body: JsonObject? = null
  ): HttpResponse<Buffer> = request(uri, body) { httpClient.deleteAbs(it) }

  suspend fun post(
    uri: String, body: JsonObject? = null
  ): HttpResponse<Buffer> = request(uri, body) { httpClient.postAbs(it) }

  suspend fun patch(
    uri: String, body: JsonObject? = null
  ): HttpResponse<Buffer> = request(uri, body) { httpClient.patchAbs(it) }

}
