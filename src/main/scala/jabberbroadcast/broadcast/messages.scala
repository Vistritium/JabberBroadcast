package jabberbroadcast.broadcast

trait GenericMessage {
  def fromUser: String
  def message: String
  def fromService: String
  def icon: Option[String]
}

case class Broadcast(fromUser: String, message: String, fromService: String, icon: Option[String]) extends GenericMessage
case class PrivateMessage(fromUser: String, message: String, fromService: String, icon: Option[String]) extends GenericMessage