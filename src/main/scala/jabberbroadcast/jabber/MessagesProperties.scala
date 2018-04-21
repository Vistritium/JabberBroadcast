package jabberbroadcast.jabber

import com.typesafe.config.Config
import scala.collection.JavaConversions._

case class MessagesProperties(botsResNames: List[String], icon: String) {

}

object MessagesProperties{
  def fromConfig(jabberConfig: Config): MessagesProperties = {
    val broadcastBots = jabberConfig.getStringList("broadcastBots").toList
    val icon = jabberConfig.getString("icon")
    MessagesProperties(broadcastBots, icon)
  }
}
