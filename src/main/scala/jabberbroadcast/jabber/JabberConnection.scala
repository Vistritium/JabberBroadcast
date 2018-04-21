package jabberbroadcast.jabber

import java.nio.file.{Files, Paths}

import akka.actor.{ActorRef, Kill, Props}
import jabberbroadcast.{AppActor, Config}
import rocks.xmpp.core.session.{TcpConnectionConfiguration, XmppClient, XmppSessionConfiguration}
import rocks.xmpp.core.stanza.MessageEvent

import scala.concurrent.duration._

class JabberConnection(val connPops: JabberConnectionConfiguration, val messageProps: MessagesProperties,
                       broadcastConsumer: ActorRef) extends AppActor {

  private var xmppClient: XmppClient = _

  private object CheckConnection

  private val (domain, login, password, name) = JabberConnectionConfiguration.unapply(connPops).get

  private val jabberMessageEvents = {
    val props = Props(classOf[JabberMessageEvents], messageProps, name, broadcastConsumer)
    context.actorOf(props, classOf[JabberMessageEvents].getSimpleName)
  }

  override def preStart(): Unit = {
    val tls = Config().getBoolean("jabber.TLS")
    val xmppConfig = TcpConnectionConfiguration.builder().secure(tls).build()
    val sessionConfig = XmppSessionConfiguration.builder()
        .cacheDirectory(Files.createTempDirectory("jabberbroadcast_xmpp"))
        .build()
    xmppClient = new XmppClient(domain, sessionConfig, xmppConfig)

    xmppClient.addInboundMessageListener((t: MessageEvent) => {
      self ! t
    })

    xmppClient.connect()
    logger.info(s"Connected $login")
    xmppClient.login(login, password)
    logger.info(s"Logged $login")


    context.system.scheduler.scheduleOnce(5 seconds, self, CheckConnection)

    super.preStart()
  }

  override def receive: Receive = {

    case m: MessageEvent => {
      jabberMessageEvents forward m
    }

    case CheckConnection => {
      val connected = xmppClient.isConnected()
      if (!connected) {
        logger.info(s"Disconnected $login")
        self ! Kill
      }
      context.system.scheduler.scheduleOnce(5 seconds, self, CheckConnection)
    }



  }


}