package app.backend.core

import app.backend.helpers.setCorsHeaders
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext

open class Controller(val config: JsonObject) {

  fun allowCors(ctx: RoutingContext) {
    ctx.setCorsHeaders()
    if (ctx.request().method().name == "OPTIONS") {
      val response = ctx.response()
      response.statusCode = 204
      response.end()
    } else {
      ctx.next()
    }
  }

}
