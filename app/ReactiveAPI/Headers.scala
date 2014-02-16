package ReactiveAPI

import play.api._
import play.api.mvc._
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global

object Headers {
  // These will be applied to all valid requests (including OPTIONS)
  val defaultHeaders = List(
    "Access-Control-Allow-Origin" -> "*",
    "Access-Control-Max-Age" -> "3600",
    "Access-Control-Allow-Headers" -> "Origin, Content-Type, Accept, Authorization",
    "Access-Control-Allow-Credentials" -> "true"
  )

  // Gets the result headers for a request (based on URI)
  def getHeaders(request: RequestHeader) = {
    val methods = (List("OPTIONS") ++ Routing.getMethods(request)).mkString(", ")
    defaultHeaders ++ List(
      "Access-Control-Allow-Methods" -> methods,
      "Allow" -> methods
    )
  }

  // An EssentialAction which adds headers to successful results
  def DefaultHeaders(action: EssentialAction): EssentialAction = EssentialAction { request =>
    action(request) map { result =>
      result.header.status match {
        case success if 200 to 299 contains success => result.withHeaders(Headers.getHeaders(request) : _*)
        case _ => result
      }
    }
  }
}

object DefaultHeaders {
  def apply(action: EssentialAction) = Headers.DefaultHeaders(action)
}