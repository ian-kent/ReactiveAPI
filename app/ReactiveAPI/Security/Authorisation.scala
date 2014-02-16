package ReactiveAPI.Security

import play.api.mvc._
import scala.concurrent._
import scala.concurrent.Future
import ExecutionContext.Implicits.global
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
import ReactiveAPI._

// Authorised helpers
object Authorised {
  implicit val timeout = Timeout(1.second)
  def apply(identity: Option[Identity[Permission,Role]]) = new WrappedIdentity[Permission, Role](identity) {
    override def %|(role: => Role): Future[Boolean] = {
      (DefaultActors.authorisationActor ask Authorise[Role](role, None, identity)).mapTo[AuthorisationResult] map { result =>
        result.valid
      } recover {
        case e => false
      }
    }
    override def %|(permission: Permission): Future[Boolean] = {
      (DefaultActors.authorisationActor ask Authorise[Permission](permission, None, identity)).mapTo[AuthorisationResult] map { result =>
        result.valid
      } recover {
        case e => false
      }
    }
  }
}

case class Authorise[A](roleOrPermission: A, resource: Option[Resource], identity: Option[Identity[Permission,Role]])
case class AuthorisationResult(valid: Boolean = false)