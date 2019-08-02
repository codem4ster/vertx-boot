package app.backend.core

import io.vertx.core.buffer.Buffer
import io.vertx.ext.web.RoutingContext

class ScramblerController {

  fun inflate(ctx: RoutingContext) {
    val bodyJson = Scrambler.inflate(ctx.bodyAsString)
    ctx.body = Buffer.buffer(bodyJson)
    ctx.next()
  }

  fun deflate(ctx: RoutingContext) {
    val response = ctx.response()
    val headers = mapOf("Content-Type" to "text/plain")
    headers.forEach { (key, value) -> response.putHeader(key, value) }
    val responseData: String = ctx["response"]
    response.end(Scrambler.deflate(responseData))
  }

}
