name := "JabberBroadcast"

version := "0.1"

scalaVersion := "2.12.5"

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.3.3",
  "rocks.xmpp" % "xmpp-core-client" % "0.7.5",
  "rocks.xmpp" % "xmpp-core" % "0.7.5",
  "commons-io" % "commons-io" % "2.6",
  "com.typesafe.akka" %% "akka-actor" % "2.5.12",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "org.apache.commons" % "commons-lang3" % "3.7",
  "com.google.inject" % "guice" % "4.2.0",
  "net.codingwell" %% "scala-guice" % "4.2.0",
  "com.squareup.okhttp3" % "okhttp" % "3.10.0",
  "com.jsuereth" %% "scala-arm" % "2.0",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.9.5",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.5"
)