package ReactiveAPI

import play.api.mvc.{AnyContent, Request, RequestHeader}
import play.api.Play
import play.api.Play.current
import scala.collection.mutable
import play.api.cache.Cache

object Routing {
  // Methods which could appear on the Allow header (excluding OPTIONS)
  val methodList = List("GET", "POST", "PUT", "DELETE", "HEAD")

  // Gets available methods for a request (based on URI)
  def getMethods(request: RequestHeader) : List[String] = {
    Cache.getOrElse[List[String]]("options.url." + request.uri) {
      for(m <- methodList; if Play.application.routes.get.handlerFor(new RequestHeader {
        val remoteAddress = request.remoteAddress
        val headers = request.headers
        val queryString = request.queryString
        val version = request.version
        val method = m
        val path = request.path
        val uri = request.uri
        val tags = request.tags
        val id: Long = request.id
      }).isDefined) yield m
    }
  }

  // Works out whether a given route exists (uses request, based on URI)
  def routeExists(request: Request[AnyContent]) = {
    if(getMethods(request).length > 0) true else false
  }
}
