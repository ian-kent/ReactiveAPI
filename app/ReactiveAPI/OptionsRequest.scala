package ReactiveAPI

import play.api.mvc._

object OptionsRequest extends Controller {
  def rootOptions = options("/")
  def options(url: String) = Action {
    Routing.routeExists(_) match {
      case true => NoContent
      case _ => NotFound
    }
  }
}
