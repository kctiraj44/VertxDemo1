import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
  java
  application
  id("com.github.johnrengelman.shadow") version "7.1.2"
  id ("io.spring.dependency-management") version "1.0.15.RELEASE"
  id ("com.google.cloud.tools.jib") version "2.7.1"
}

//-------------------------------------------------------------------------------------


dependencyManagement {
  imports {
    mavenBom ("org.apache.logging.log4j:log4j-bom:2.21.0")
  }
}

//-------------------------------------------------------------------------------------



group = "com.peter"
version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
}

val vertxVersion = "4.4.6"
val junitJupiterVersion = "5.9.1"
val jacksonVersion = "2.15.0"
val mainVerticleName = "com.peter.vertex_starter.MainVerticle"
val launcherClassName = "io.vertx.core.Launcher"

val watchForChange = "src/**/*"
val doOnChange = "${projectDir}/gradlew classes"

application {
  mainClass.set(launcherClassName)
}

dependencies {
  implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
  implementation ("org.apache.logging.log4j:log4j-api")
  implementation ("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
  implementation ("org.apache.logging.log4j:log4j-core")
  implementation ("org.apache.logging.log4j:log4j-slf4j-impl")
  implementation ("org.slf4j:slf4j-api:1.7.30")

  implementation("io.vertx:vertx-core")
  testImplementation("io.vertx:vertx-junit5")
  testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
}

java {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
}

jib{
  from {
    image = "amazoncorretto:11"
  }
  to {
    image = "example/jib/vertx-starter"
  }
  container {
    mainClass = "io.vertx.core.Launcher"
    args = listOf("run", mainVerticleName)
    ports = listOf("8888")
  }
}

tasks.withType<ShadowJar> {
  archiveClassifier.set("fat")
  manifest {
    attributes(mapOf("Main-Verticle" to mainVerticleName))
  }
  mergeServiceFiles()
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    events = setOf(PASSED, SKIPPED, FAILED)
  }
}

tasks.withType<JavaExec> {
  args = listOf("run", mainVerticleName, "--redeploy=$watchForChange", "--launcher-class=$launcherClassName", "--on-redeploy=$doOnChange")
}
