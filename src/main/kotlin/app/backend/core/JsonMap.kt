package app.backend.core

import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import kotlin.reflect.KFunction

open class JsonMap {
  val arr = JsonArr()
  val jsonObject = JsonObject()

  fun obj(block: JsonObj.() -> Unit): JsonObject {
    val jsonObj = JsonObj()
    block(jsonObj)
    return jsonObj.jsonObject
  }

  infix fun String.to(val2: Any)  {
    jsonObject.put(this, val2)
  }
}

class JsonArr {
  operator fun get(vararg list: Any?): JsonArray {
    val jsonArray = JsonArray()
    list.forEach {
      jsonArray.add(it)
    }
    return jsonArray
  }
}

class JsonObj: JsonMap()

fun jsonObj(block: JsonMap.() -> Unit): JsonObject {
  val jsonObj = JsonMap()
  block(jsonObj)
  return jsonObj.jsonObject
}

fun jsonArr(block: JsonMap.() -> JsonArray): JsonArray {
  val jsonArr = JsonMap()
  return block(jsonArr)
}

