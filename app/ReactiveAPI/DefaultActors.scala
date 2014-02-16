package ReactiveAPI

import play.libs.Akka
import akka.actor.Props
import ReactiveAPI.Security.{Authenticator, Authorisor}

object DefaultActors {
  val authorisationActor = Akka.system.actorOf(Props[Authorisor], name = "authorisation")
  val authenticationActor = Akka.system.actorOf(Props[Authenticator], name = "authentication")
}