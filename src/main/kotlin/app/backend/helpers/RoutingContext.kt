package app.backend.helpers

import io.ebean.Model
import io.vertx.core.json.Json
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext

fun RoutingContext.endAsJson(obj: Any) {
  response()
    .putHeader("content-type", "application/json; charset=utf-8")
    .end(Json.encodePrettily(obj))
}

fun <T>JsonObject.getBean(castingClass: Class<T>, vararg allowedParameters: String): T {
  val classFields = castingClass.declaredFields
  val newObjFromClass = castingClass.newInstance()
  for (param in allowedParameters) {
    val foundField = classFields.find { it.name == param } ?: continue
    val value = this.getValue("param")
    foundField.set(newObjFromClass, value)
  }
  return newObjFromClass
}

fun <T>RoutingContext.getBean(
  keyOnJson: String,
  castingClass: Class<T>,
  vararg allowedParameters: String
): T {
  val body = this.bodyAsJson
  val objJson = body.getJsonObject(keyOnJson)
  return objJson.getBean(castingClass, *allowedParameters)
}

fun RoutingContext.setCorsHeaders() {
  val response = response()
  response.putHeader("Access-Control-Allow-Origin", request().getHeader("ORIGIN"))
  response.putHeader("Access-Control-Allow-Credentials", "true")
  response.putHeader("Access-Control-Allow-Headers", "Content-Type, Authorization")
  response.putHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PATCH, PUT, HEAD")
}
