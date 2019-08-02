package app.backend

import app.backend.core.elastic.Client
import io.vertx.config.ConfigRetriever
import io.vertx.ext.web.Router
import io.vertx.kotlin.config.getConfigAwait
import io.vertx.kotlin.core.http.listenAwait
import io.vertx.kotlin.coroutines.CoroutineVerticle
import org.slf4j.LoggerFactory

class MainVerticle : CoroutineVerticle() {
  override suspend fun start() {
    Globals.elasticClient = Client("http://127.0.0.1:9200", vertx, true)
    Globals.logger = LoggerFactory.getLogger(MainVerticle::class.java)
    val appConfig = ConfigRetriever.create(vertx).getConfigAwait()
    val router = Router.router(vertx)
    Routes(router, appConfig).build()

    val server = vertx.createHttpServer().requestHandler(router).listenAwait(1812)
    println("Http server started on port ${server.actualPort()}")
  }
}


