package app.backend

import app.backend.controllers.*
import app.backend.core.Controller
import app.backend.core.JwtController
import app.backend.core.ScramblerController
import app.backend.helpers.crHandler
import app.backend.helpers.endAsJson
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Route
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.FaviconHandler
import io.vertx.kotlin.core.json.get

class Routes(
  private val router: Router,
  private val appConfig: JsonObject
) {
  private val coreController = Controller(appConfig)
  private val scramblerController = ScramblerController()
  private val jwtController = JwtController(appConfig)

  private val siteController = SiteController(appConfig)

  private fun customRoutes() {
//    val authorizedRoutes = mapOf(
//      "get" to listOf(),
//      "post" to listOf(),
//      "put" to listOf(),
//      "delete" to listOf()
//    )
//    setAuthRoutes(authorizedRoutes)


    router.get("/").crHandler(siteController::index)
  }

  // Web için best practice'ler içeren bir build oluşturur
  //   body'yi jsonobject'e çevirir
  //   favicon'u serve eder bkz: resources/favicon.ico
  //   environment development ise cors için tüm izinleri oluşturur
  //   environment production ise gönderilen veriyi şifreler alınan veriyi çözer
  fun build() {
    // body'i string olarak almaya yarıyor
    router.route().handler(BodyHandler.create())
    router.get("/favicon.ico").handler(FaviconHandler.create("src/main/resources/favicon.ico"))

    // environment
    val env: String = appConfig["VERTX_ENV"]

    // failure handler
    if (env != "production") router.route().failureHandler { frc ->
      val failure = frc.failure()
      failure.printStackTrace()

      frc.endAsJson(mapOf(
        "message" to failure.localizedMessage,
        "stack" to failure.stackTrace.map { it.toString() }
      ))
    }

    // eğer environment production ise api altındaki veriyi karıştır
    if (env == "production") {
      router.route("/api/*").handler(scramblerController::inflate)
    } else {
      // development ve test ortamlarında cors için izinleri ayarla
      router.route().handler(coreController::allowCors)
    }

    // kullanıcı tanımlı urller
    customRoutes()

    // eğer environment production ise api altındaki veriyi çöz
    if (env == "production") {
      router.route("/api/*").handler(scramblerController::deflate)
    }
  }

  private fun setAuthRoutes(authorizedRoutes: Map<String, List<String>>) {
    listOf("get", "post", "patch", "delete", "put").forEach { method ->
      authorizedRoutes[method]?.forEach { path ->
        val route = router.javaClass
          .getMethod(method, String::class.java)
          .invoke(router, path) as Route
        route.handler(jwtController::authorize)
      }
    }
  }
}
