package app.backend.controllers

import app.backend.core.Controller
import app.backend.core.Reply
import app.backend.helpers.endAsJson
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext

class SiteController(config: JsonObject): Controller(config) {

  suspend fun index(ctx: RoutingContext) {
    val reply = Reply.success("A successfull response")
    ctx.endAsJson(reply)
  }

}
