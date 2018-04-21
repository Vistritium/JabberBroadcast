package jabberbroadcast

import java.io.File
import java.nio.file.{Files, Paths}
import java.util.Objects

import com.fasterxml.jackson.databind.{ObjectMapper, SerializationFeature}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.typesafe.config.ConfigFactory
import okhttp3.OkHttpClient

object Config {

  private val config: com.typesafe.config.Config = {
    val envConfigOpt = {
      var envConfig = System.getenv("ENV_CONFIG")
      if (Objects.isNull(envConfig)) {
        envConfig = System.getProperty("ENV_CONFIG")
      }
      if (Objects.nonNull(envConfig)) {
        Some(new File(envConfig))
      } else {
        val path = Paths.get("env.conf")
        if (Files.exists(path)) {
          Some(path.toFile)
        } else {
          None
        }
      }
    }

    (envConfigOpt match {
      case Some(envConfig) => ConfigFactory.parseFile(envConfig)
      case None => ConfigFactory.empty()
    }) withFallback ConfigFactory.load()
  }

  val httpClient: OkHttpClient = new OkHttpClient()

  val objectMapper: ObjectMapper = {
    val mapper = new ObjectMapper()
    mapper.enable(SerializationFeature.INDENT_OUTPUT)
    mapper.registerModule(DefaultScalaModule)
    mapper
  }

  def apply(): com.typesafe.config.Config = config

}
