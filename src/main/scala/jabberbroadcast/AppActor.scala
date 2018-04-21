package jabberbroadcast
import akka.actor.Actor
import akka.util.Timeout
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.duration._

trait AppActor extends Actor with LazyLogging{
  implicit val timeout = Timeout(30 seconds)
  implicit val executor = context.system.dispatcher
}
