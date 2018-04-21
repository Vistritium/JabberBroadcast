package jabberbroadcast.jabber

import akka.actor.ActorRef
import jabberbroadcast.broadcast.{Broadcast, PrivateMessage}
import jabberbroadcast.{AppActor, Config}
import rocks.xmpp.core.stanza.MessageEvent

class JabberMessageEvents(messagesProperties: MessagesProperties, serviceName: String, broadcastConsumer: ActorRef) extends AppActor{

  //val broadcastBots = Config.config.getStringList("jabber.broadcastBots").toList
  val broadcastBots: List[String] = messagesProperties.botsResNames
  val isPrivateEnabled: Boolean = Config().getBoolean("jabber.private")

  override def receive: Receive = {

    case m: MessageEvent => {
      val from = m.getMessage.getFrom
      logger.info(s"local ${from.getLocal} domain ${from.getDomain} resource ${from.getResource}");
      if (broadcastBots.contains(from.getResource)) {
        broadcastConsumer ! Broadcast(from.getResource, m.getMessage.getBody, serviceName, Some(messagesProperties.icon))
      } else {
        if(isPrivateEnabled){
          logger.info(s"Private message:\n${m.getMessage.getBody}")
          broadcastConsumer ! PrivateMessage(from.getResource, m.getMessage.getBody, serviceName, Some(messagesProperties.icon))
        }
      }


    }

  }


}
