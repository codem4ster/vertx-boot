package app.backend.helpers

import app.backend.core.InteractorResult
import app.backend.core.Ok
import app.backend.core.UserError
import com.kamedon.validation.Validation
import io.ebean.Model
import io.vertx.core.json.JsonObject
import kotlin.reflect.KFunction

fun Model.updateAttributes(attributes: JsonObject) {
  val fieldNames = attributes.map { (key, _) -> key }.toTypedArray()
  updateAttributes(attributes, *fieldNames)
}

fun Model.updateAttributes(attributes: JsonObject, vararg permitted: String) {
  for (param in permitted) {
    val methodName = "set${param.capitalize()}"
    val method = this::class.java.methods.find { methodName == it.name } ?: continue
    method.isAccessible = true
    val value = attributes.getValue(param) ?: continue
    method.invoke(this, value)
    markAsDirty()
  }
}

fun Model.validate(fn: KFunction<Any?>): InteractorResult {
  @Suppress("UNCHECKED_CAST")
  val validation = fn.call() as Validation<Any>
  val errors = validation.validate(this)
  return if (errors.isNotEmpty()) {
    val errorData =  errors as Map<*, *>
    val message = errorData.values.joinToString("\n") {
      if (it is List<*>)  it.joinToString("\n") else ""
    }
    UserError(message)
  } else {
    Ok()
  }
}


