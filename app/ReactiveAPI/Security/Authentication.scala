package ReactiveAPI.Security

import _root_.play.api.http.Status
import _root_.play.api.mvc.{Results, SimpleResult, Request}
import scala.concurrent.Future
import akka.pattern.ask
import scala.concurrent.duration._
import play.api.mvc.ActionBuilder
import akka.util.Timeout
import scala.concurrent.ExecutionContext.Implicits.global
import ReactiveAPI.DefaultActors

// Authenticated action builder
object Authenticated extends ActionBuilder[AuthenticatedRequest] {
  implicit val timeout = Timeout(1.second)

  def invokeBlock[A](request: Request[A], block: (AuthenticatedRequest[A]) => Future[SimpleResult]) = {
    (DefaultActors.authenticationActor ask Authenticate(request)).mapTo[AuthenticationResult] flatMap { result =>
      block(AuthenticatedRequest[A](result.identity, request))
    } recover {
      case e =>
        e.printStackTrace
        Results.Status(Status.INTERNAL_SERVER_ERROR)
    }
  }
}

// AuthenticatedRequest wrapper
trait AuthenticatedRequest[+A] extends Request[A] {
  val identity: Option[Identity[Permission,Role]]
}
object AuthenticatedRequest {
 def apply[A](u: Option[Identity[Permission,Role]], r: Request[A]) = new AuthenticatedRequest[A] {
   def id = r.id
   def tags = r.tags
   def uri = r.uri
   def path = r.path
   def method = r.method
   def version = r.version
   def queryString = r.queryString
   def headers = r.headers
   lazy val remoteAddress = r.remoteAddress
   def username = None
   val body = r.body
   val identity = u
 }
}

// Case classes for communication using Akka
case class Authenticate[A](request: Request[A])
case class AuthenticationResult(identity: Option[Identity[Permission,Role]] = None)