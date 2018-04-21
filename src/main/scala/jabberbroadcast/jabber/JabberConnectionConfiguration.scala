package jabberbroadcast.jabber

import com.typesafe.config.Config

case class JabberConnectionConfiguration(domain: String, login: String, password: String, name: String) {
}

object JabberConnectionConfiguration {

  def fromConfig(jabberConfig: Config): JabberConnectionConfiguration = {
    val (domain, login, password, name) = {
      val config = jabberConfig.getConfig("connection")
      val domain = config.getString("domain")
      val login = config.getString("login")
      val password = config.getString("password")
      val name = jabberConfig.getString("name")
      (domain, login, password, name)
    }
    JabberConnectionConfiguration(domain, login, password, name)
  }


}

