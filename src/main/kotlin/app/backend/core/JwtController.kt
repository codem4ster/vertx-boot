package app.backend.core

import app.backend.helpers.endAsJson
import com.auth0.jwt.interfaces.DecodedJWT
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.core.json.get

class JwtController(config: JsonObject) {
  private val env: String = config["VERTX_ENV"]

  fun authorize(ctx: RoutingContext) {
    val auth = ctx.request().headers().get("Authorization")
    if (auth != null) {
      val token = auth.split(" ").last()
      val decodedJwt = Jwt.verify(token)
      processJwt(decodedJwt, ctx)
    } else {
      giveAccessError(ctx)
    }
  }

  private fun processJwt(decodedJwt: DecodedJWT?, ctx: RoutingContext) {
    if (decodedJwt == null) {
      giveAccessError(ctx)
    } else {
      val currentUser = CurrentUser(
        decodedJwt.claims["ident"]!!.asString(),
        decodedJwt.claims["role"]!!.asString()
      )
      ctx.put("currentUser", currentUser)
      ctx.next()
    }
  }

  private fun giveAccessError(ctx: RoutingContext) {
    val reply = Reply.error("Bu işlemi yapmaya yetkiniz yok.")
    ctx.endAsJson(reply)
  }
}
