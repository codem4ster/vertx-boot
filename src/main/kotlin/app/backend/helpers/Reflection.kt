package app.backend.helpers

import kotlin.reflect.full.functions
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

@Throws(IllegalAccessException::class, ClassCastException::class)
inline fun <reified T> Any.getField(fieldName: String): T? {
  this::class.memberProperties.forEach { kCallable ->
    if (fieldName == kCallable.name) {
      kCallable.isAccessible = true
      return kCallable.getter.call(this) as T?
    }
  }
  this::class.functions.forEach { kCallable ->
    if (fieldName == kCallable.name) {
      kCallable.isAccessible = true
      return kCallable.call(this) as T?
    }
  }
  return null
}
