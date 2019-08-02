package app.backend.core

import com.fasterxml.jackson.annotation.JsonValue

enum class ReplyType(private val text: String) {
  SUCCESS("success"),
  ERROR("error"),
  WARNING("warning"),
  MESSAGE("message");

  @JsonValue fun jsonValue() = text
}

data class Reply(var type: ReplyType, var message: String = "", var payload: Any? = null) {
  companion object {
    fun success(message: String = "", payload: Any? = null) =  Reply(ReplyType.SUCCESS, message, payload)
    fun error(message: String = "", payload: Any? = null) =  Reply(ReplyType.ERROR, message, payload)
    fun warning(message: String = "", payload: Any? = null) =  Reply(ReplyType.WARNING, message, payload)
    fun message(message: String = "", payload: Any? = null) =  Reply(ReplyType.MESSAGE, message, payload)
  }
}
