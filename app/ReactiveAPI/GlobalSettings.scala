package ReactiveAPI

import play.api.mvc.{EssentialAction, Handler, RequestHeader}

class GlobalSettings extends play.api.GlobalSettings {
  override def onRouteRequest(request: RequestHeader): Option[Handler] = {
    super.onRouteRequest(request) map {
      case a: EssentialAction => DefaultHeaders(a)
      case other => other
    }
  }
}


