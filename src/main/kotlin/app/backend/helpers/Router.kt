package app.backend.helpers

import io.vertx.ext.web.Route
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun Route.crHandler(fn: suspend (RoutingContext) -> Unit) {
  handler { ctx ->
    GlobalScope.launch(ctx.vertx().dispatcher()) {
      try { fn(ctx) } catch (e: Exception) { ctx.fail(e) }
    }
  }
}
