package app.backend.core

import io.ebean.Model
import app.backend.helpers.getField
import java.lang.reflect.Field


open class Serializer {
  fun mapAttrs(list: List<Model>, vararg attrs: Any) =
    list.map { mapAttrs(it, *attrs) }

  fun mapAttrs(model: Model, vararg attrs: Any) =
    attrs.fold(mutableMapOf<String, Any?>()) { memo, item ->
      when (item) {
        is String -> {
          memo[item] = model.getField(item)
          memo
        }
        is Pair<*,*> -> {
          memo[item.first as String] = model.getField(item.second as String)
          memo
        }
        else -> memo
      }
    }

  private fun getField(model: Model, field: String): Field {
    val fieldObj = model.javaClass.fields.find { it.name == field }!!
    fieldObj.isAccessible = true
    return fieldObj
  }

}
