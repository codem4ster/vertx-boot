package app.backend.core

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import java.text.SimpleDateFormat
import java.util.*


val rfc7231DateFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz")
  .apply {
    timeZone = TimeZone.getTimeZone("GMT")
  }


class Jwt {
  companion object {
    private const val SECRET = "secret"

    fun sign(userIdent: String, role: String): Pair<String, Long> {
      val tomorrow = Calendar.getInstance()
      tomorrow.add(Calendar.HOUR, 3)

      val algorithm = Algorithm.HMAC256(SECRET)
      val jwt = JWT.create()
        .withClaim("ident", userIdent)
        .withClaim("role", role)
        .withExpiresAt(tomorrow.time)
        .sign(algorithm)
      tomorrow.add(Calendar.MINUTE, -5)
      return Pair(jwt, tomorrow.timeInMillis)
    }

    fun verify(token: String): DecodedJWT? {
      return try {
        val algorithm = Algorithm.HMAC256(SECRET)
        val verifier = JWT.require(algorithm)
          .build()
        verifier.verify(token)
      } catch (exception: JWTVerificationException) {
        null
      }
    }
  }
}
