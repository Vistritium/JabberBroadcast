package jabberbroadcast.jabber

import akka.actor.SupervisorStrategy.Stop
import akka.actor.{ActorKilledException, ActorRef, OneForOneStrategy, Props}
import jabberbroadcast.AppActor
import rocks.xmpp.core.stanza.MessageEvent

import scala.concurrent.duration._

class JabberConnectionManager(val connPops: JabberConnectionConfiguration, val messageProps: MessagesProperties,
                              broadcastConsumer: ActorRef) extends AppActor {

  var jabberConnection: ActorRef = null

  private case object StartJabberConnection

  override def preStart(): Unit = {
    super.preStart()
    startJabberConnection()
  }

  override val supervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = -1) {
      case t: ActorKilledException => {
        context.system.scheduler.scheduleOnce(10 seconds, self, StartJabberConnection)
        logger.info("Stopping JabberConnection: killed")
        Stop
      }
      case t => {
        context.system.scheduler.scheduleOnce(10 seconds, self, StartJabberConnection)
        logger.info(s"Stopping JabberConnection: ${t.getMessage} stack: ${t.getStackTrace.map(_.toString).mkString("\n")}")
        Stop
      }
    }

  override def receive: Receive = {
    case StartJabberConnection => startJabberConnection()
    case x: MessageEvent => jabberConnection forward x
    case x => logger.info(s"Message $x")
  }

  def startJabberConnection(): Unit = {
    logger.info("Starting jabber connection")
    val props = Props(classOf[JabberConnection], connPops, messageProps, broadcastConsumer: ActorRef)
    jabberConnection = context.actorOf(props, classOf[JabberConnection].getSimpleName)
  }


}
