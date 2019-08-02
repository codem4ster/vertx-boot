package app.backend.core

sealed class InteractorResult
data class UserError(val message: String) : InteractorResult()
data class Ok(val payload: Any? = null) : InteractorResult()

typealias ValidationErrors = Map<String, List<String>>

abstract class Interactor {
  abstract suspend fun run() : InteractorResult

  fun messagesOf(errors : ValidationErrors) =
    errors.values.joinToString("\n") {
      it.joinToString("\n")
    }

  fun firstMessageOf(errors : ValidationErrors) =
    errors.values.first().first()

  suspend inline fun <reified T : Any?, R: Any?>reply(
    successMsg: String = "",
    mutatePayload: (T)->R): Reply
  {
    val result = run()

    return when (result) {
      is UserError -> Reply.error(result.message)
      is Ok -> {
        val payload: T?
        if (result.payload is T) payload = result.payload else payload = null
        Reply.success(successMsg, mutatePayload(payload!!))
      }
    }
  }

  suspend fun reply(
    successMsg: String = ""
  ): Reply {
    val result = run()

    return when (result) {
      is UserError -> Reply.error(result.message)
      is Ok -> Reply.success(successMsg, result.payload)
    }
  }
}
