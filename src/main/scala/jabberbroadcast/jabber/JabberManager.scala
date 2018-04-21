package jabberbroadcast.jabber

import akka.actor.{ActorRef, Props}
import jabberbroadcast.{AppActor, Config}
import rocks.xmpp.core.stanza.MessageEvent

import scala.collection.JavaConverters._

class JabberManager(broadcastConsumer: ActorRef) extends AppActor {

  private val configsStrings = Config().getStringList("jabber.configs").asScala.toList
  private val configs = configsStrings.map(configName => {
    val config = Config().getConfig(s"jabber.$configName")
    val connectionConfig = JabberConnectionConfiguration.fromConfig(config)
    val msgConfig = MessagesProperties.fromConfig(config)
    (connectionConfig, msgConfig, configName)
  })

  private var actors: List[ActorRef] = _

  override def preStart(): Unit = {
    logger.info("JabberManager enabled")
    actors = configs.map(config => {
      val props = Props(classOf[JabberConnectionManager], config._1, config._2, broadcastConsumer)
      context.actorOf(props, config._3)
    })

    super.preStart()
  }

  override def receive: Receive = {
    case x: MessageEvent => actors.foreach(_ ! x)
    case x => logger.debug(s"Received $x")
  }
}