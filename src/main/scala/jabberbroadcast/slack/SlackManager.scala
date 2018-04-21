package jabberbroadcast.slack

import jabberbroadcast.{AppActor, Config}
import jabberbroadcast.broadcast.{Broadcast, GenericMessage}
import okhttp3.{MediaType, Request, RequestBody}
import resource._

class SlackManager extends AppActor {

  private val config = Config().getConfig("slack")
  private val url = config.getString("webhookUrl")

  override def receive: Receive = {
    case message: GenericMessage => {
      logger.info(s"Received message $message")
      val body = new {
        val text = message.message
        val username = message.fromService
        val icon_emoji = message.icon.get
      }

      val request = new Request.Builder()
        .url(url)
        .post(
          RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
            Config.objectMapper.writeValueAsString(body))
        )
        .build()

      managed(Config.httpClient.newCall(request).execute())
        .acquireAndGet(response => {
          if (!response.isSuccessful) {
            logger.error(s"Not successfull response: ${response.code()} ${response.message()}\n${response.body()}")
          }
        })
    }
  }

  override def preStart(): Unit = {
    logger.info("SlackManager enabled")
    if (config.getBoolean("testMsg")) {
      self ! Broadcast("TEST_USER", "TEST MESSAGE!", "TEST_SERVICE", Some(":goons:"))
    }
    super.preStart()
  }
}
