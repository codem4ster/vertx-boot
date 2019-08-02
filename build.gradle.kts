import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.plugins.ide.idea.model.IdeaModel
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  java
  application
  idea
  kotlin("jvm") version "1.3.40"
  kotlin("kapt") version "1.3.40"
  id("com.github.johnrengelman.shadow") version "5.0.0"
  id("io.ebean") version "11.39.1"
}

group = "app"
version = "0.0.1"
//ebean.debugLevel = 4
ebean.queryBeans = true


repositories {
  mavenCentral()
  jcenter()
  maven("http://kamedon.github.com/Validation/repository")
}

val vertxVersion = "3.7.1"
val kotlinVersion = "1.3.20"
val junitJupiterEngineVersion = "5.4.0"

application.mainClassName = "io.vertx.core.Launcher"
kapt.useBuildCache = true
kapt.correctErrorTypes = true

val mainVerticleName = "app.backend.MainVerticle"
//vertx.mainVerticle = mainVerticleName
val watchForChange = "src/**/*"
val doOnChange = "./gradlew classes"

dependencies {
  implementation("io.vertx:vertx-web:$vertxVersion")
  implementation("io.vertx:vertx-lang-kotlin-coroutines:$vertxVersion")
  implementation("io.vertx:vertx-lang-kotlin:$vertxVersion")
  implementation("io.vertx:vertx-config:$vertxVersion")
  implementation("io.vertx:vertx-web-client:$vertxVersion")
  implementation("org.jetbrains.kotlin:kotlin-reflect:1.3.31")
  implementation("com.kamedon:kotlin-validation:0.5.0")

  testImplementation("io.vertx:vertx-junit5:$vertxVersion")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitJupiterEngineVersion")
  testImplementation("org.junit.jupiter:junit-jupiter-api:$junitJupiterEngineVersion")

  implementation("org.mindrot:jbcrypt:0.4")
  implementation("com.auth0:java-jwt:3.4.1")
  implementation("joda-time:joda-time:2.10.3")

  //sl4j logging
  implementation("ch.qos.logback:logback-classic:0.9.26")
//  implementation("org.slf4j:slf4j-api:1.7.26")
//  implementation("org.slf4j:slf4j-simple:1.7.26")

  //ebean
  implementation("io.ebean:ebean:11.39.3")
  implementation("org.postgresql:postgresql:42.2.2")
  implementation("io.ebean:ebean-querybean:11.40.1")
//  implementation("io.ebean.tools:finder-generator:11.34.1")
//  testImplementation("io.ebean.tools:finder-generator:11.34.1")
  testImplementation("io.ebean:ebean-querybean:11.40.1")
  kapt("io.ebean:kotlin-querybean-generator:11.39.3")
  testImplementation("io.ebean.test:ebean-test-config:11.39.1")
}

tasks.named<KotlinCompile>("compileKotlin") {
  kotlinOptions.jvmTarget = "1.8"
}

tasks.named<KotlinCompile>("compileTestKotlin") {
  kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<ShadowJar> {
  manifest {
    attributes(mutableMapOf("Main-Verticle" to mainVerticleName))
  }
  mergeServiceFiles {
    include("META-INF/services/io.vertx.app.backend.core.spi.VerticleFactory")
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    showStandardStreams = true
    events("PASSED", "FAILED", "SKIPPED")
  }
}

tasks {
  val group = "console"

  create<JavaExec>("seedDb") {
    this.group = group
    main = "app.backend.console.SeedDbKt"
    classpath = sourceSets.getByName("main").runtimeClasspath
  }

  create<JavaExec>("createMigration") {
    this.group = group
    main = "app.backend.console.CreateMigrationKt"
    classpath = sourceSets.getByName("main").runtimeClasspath
  }

  create<JavaExec>("applyMigration") {
    this.group = group
    main = "app.backend.console.ApplyMigrationKt"
    classpath = sourceSets.getByName("main").runtimeClasspath
  }

  create<JavaExec>("createUser") {
    this.group = group
    main = "app.backend.console.CreateUserKt"
    classpath = sourceSets.getByName("main").runtimeClasspath
  }

  create<JavaExec>("initElastic") {
    this.group = group
    main = "app.backend.console.InitElasticKt"
    classpath = sourceSets.getByName("main").runtimeClasspath
  }
}


tasks.withType<JavaExec> {
  args = listOf(
    "run",
    mainVerticleName,
    "--redeploy=$watchForChange",
    "--launcher-class=${application.mainClassName}",
    "--on-redeploy=$doOnChange"
  )
}
