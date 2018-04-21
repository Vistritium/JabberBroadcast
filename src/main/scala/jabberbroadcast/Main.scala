package jabberbroadcast

import akka.actor.{ActorSystem, Props}
import jabberbroadcast.jabber.JabberManager
import jabberbroadcast.slack.SlackManager

object Main {

  def main(args: Array[String]): Unit = {

    val system = ActorSystem()

    val broadcastReceiver = if (Config().getBoolean("slack.enabled")) {
      system.actorOf(Props(new SlackManager()))
    } else system.deadLetters

    if (Config().getBoolean("jabber.enabled")) {
      system.actorOf(Props(new JabberManager(broadcastReceiver)))
    }

  }

}
