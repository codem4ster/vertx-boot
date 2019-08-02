rootProject.name = "app-backend"

pluginManagement {
  repositories {
    gradlePluginPortal()
    mavenCentral()
    mavenLocal()
  }
  resolutionStrategy {
    eachPlugin {
      when(requested.id.id) {
        "io.ebean" -> useModule("io.ebean:ebean-gradle-plugin:${requested.version}")
      }
    }
  }
}
