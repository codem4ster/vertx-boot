package app.backend.elastic

import app.backend.MainVerticle
import app.backend.core.elastic.Client
import app.backend.core.elastic.Result
import app.backend.core.elastic.index
import app.backend.core.jsonObj
import io.vertx.core.Vertx
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(VertxExtension::class)
class ClientTest {
  @BeforeEach
  fun deploy_verticle(vertx: Vertx, testContext: VertxTestContext) {
    vertx.deployVerticle(MainVerticle(), testContext.succeeding<String> { testContext.completeNow() })
  }

  @Test
  fun getClusterHealth(vertx: Vertx, testContext: VertxTestContext) {
    val elastic = Client("http://127.0.0.1:9200", vertx)
    runBlocking {
      val response = elastic.get("_cat/health?v&pretty=true")
      println(response.bodyAsString())
    }
    runBlocking {
      val response = elastic.get("_search?pretty=true", json {
        obj(
          "query" to obj(
            "match_all" to obj()
          )
        )
      })
      println(response.bodyAsString())
      val result = Result(response.bodyAsString())
      println(result.hits()[0].score)
    }
    runBlocking {
      //      @Suppress("UNCHECKED_CAST")
//      val response = elastic.index("nes_blog") {
//        put("?pretty")
//      } as HttpResponse<Buffer>
      val nesBlog = elastic.index("nes_blog")
//      val response = nesBlog.get("?pretty")

//      val response = nesBlog.put("/_doc/1?pretty", jsonObj {
//        "name" to "John Doe"
//        "another" to arr["name1", "name2", obj { "key1" to "value1" }]
//      })

//      val data = jsonArr {
//        arr[
//          "name1",
//          obj {
//            "key1" to "value1"
//            "key2" to "value2"
//          }
//        ]
//      }

//      println(response.bodyAsString())
    }
    testContext.completeNow()
  }
}
