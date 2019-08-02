package app.backend

import io.vertx.core.Vertx
import io.vertx.ext.web.client.WebClient
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import io.vertx.kotlin.ext.web.client.sendAwait
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.Assertions.*

@ExtendWith(VertxExtension::class)
class TestMainVerticle {

  @BeforeEach
  fun deploy_verticle(vertx: Vertx, testContext: VertxTestContext) {
    vertx.deployVerticle(MainVerticle(), testContext.succeeding<String> { testContext.completeNow() })
  }

  @Test
  fun verticle_deployed(vertx: Vertx, testContext: VertxTestContext) {
    testContext.completeNow()
  }

  @Test
  fun checkThatTheIndexPageIsServed(vertx: Vertx, context: VertxTestContext) {
    val client = WebClient.create(vertx)
    runBlocking {
      val response = client.get(1812, "localhost", "/").sendAwait()
      assertEquals(200, response.statusCode())
      assert(response.bodyAsString().contains("Hello"))
    }
    context.completeNow()
  }
}
