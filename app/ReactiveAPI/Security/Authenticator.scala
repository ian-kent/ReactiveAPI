package ReactiveAPI.Security

import akka.actor.Actor

// Actor responsible for authentication
class Authenticator extends Actor {
  def receive = {
    case Authenticate(request) =>
      request.headers.get("Authorization").getOrElse("") match {
        case OAuth2Identity.matcher(t) =>
          sender ! AuthenticationResult(validate(OAuth2Identity("Bearer", t)))
        case _ => sender ! AuthenticationResult(None)
      }
    case _ => sender ! AuthenticationResult(None)
  }
  def validate(identity : OAuth2Identity) : Option[OAuth2Identity] = {
    if(identity.kind == "Bearer") {
      Some(identity)
    } else None
  }
  case class OAuth2Identity(kind: String, token: String) extends Identity[Permission, Role]
  object OAuth2Identity {
    val matcher = "Bearer (.*)".r
  }
}