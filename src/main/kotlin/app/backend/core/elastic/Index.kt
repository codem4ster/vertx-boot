package app.backend.core.elastic

import io.vertx.core.buffer.Buffer
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.client.HttpResponse

suspend fun Client.index(
  name: String, block: suspend Index.() -> HttpResponse<Buffer>
): Any {
  val index = Index(this, name)
  return block.invoke(index)
}

fun Client.index(name: String) = Index(this, name)

class Index(private val client: Client, private val name: String) {
  suspend fun get(uri: String, body: JsonObject? = null): HttpResponse<Buffer> {
    val uriWithIndex = "$name$uri"
    return client.get(uriWithIndex, body)
  }

  suspend fun put(uri: String, body: JsonObject? = null): HttpResponse<Buffer> {
    val uriWithIndex = "$name$uri"
    return client.put(uriWithIndex, body)
  }

  suspend fun post(uri: String, body: JsonObject? = null): HttpResponse<Buffer> {
    val uriWithIndex = "$name$uri"
    return client.post(uriWithIndex, body)
  }

  suspend fun patch(uri: String, body: JsonObject? = null): HttpResponse<Buffer> {
    val uriWithIndex = "$name$uri"
    return client.patch(uriWithIndex, body)
  }

  suspend fun delete(uri: String, body: JsonObject? = null): HttpResponse<Buffer> {
    val uriWithIndex = "$name$uri"
    return client.delete(uriWithIndex, body)
  }
}
