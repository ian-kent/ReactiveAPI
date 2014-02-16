package ReactiveAPI.Security

import akka.actor.Actor
import Rights.StringHelper

// Actor responsible for authorisation
class Authorisor extends Actor {
  def receive = {
    case Authorise(role, resource, identity) =>
      if(identity.isEmpty && role != role"home")
        sender ! AuthorisationResult(valid = false)
      else
        sender ! AuthorisationResult(valid = true)
    case _ => sender ! AuthorisationResult
  }
}