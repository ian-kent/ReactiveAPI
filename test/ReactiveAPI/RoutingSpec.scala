package ReactiveAPI

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import play.api.mvc.{Controller, Results, Action}
import play.api.cache.{EhCachePlugin, CachePlugin}

class RoutingSpec extends Specification with Results {
  val appWithRoutes = () => FakeApplication(withRoutes = {
    case ("GET", "/") => Action{ Ok }
    case ("POST", "/") => Action{ Ok }
    case ("GET", "/foo/1") => Action{ Ok }
    case ("POST", "/foo/1") => Action{ Ok }
    case ("PUT", "/foo/1") => Action{ Ok }
    case ("DELETE", "/foo/1") => Action{ Ok }
    case ("OPTIONS", "/") => Action{ Ok }
    case ("OPTIONS", "/foo/1") => Action{ Ok }
  })

  "Routing" should {
    "correctly confirm route existence" in new WithApplication(app = appWithRoutes()) {
      Routing.routeExists(FakeRequest("GET","/")) should be equalTo true
      Routing.routeExists(FakeRequest("POST","/")) should be equalTo true
      Routing.routeExists(FakeRequest("PUT","/")) should be equalTo true
      Routing.routeExists(FakeRequest("DELETE","/")) should be equalTo true

      Routing.routeExists(FakeRequest("GET","/foo/1")) should be equalTo true
      Routing.routeExists(FakeRequest("POST","/foo/1")) should be equalTo true
      Routing.routeExists(FakeRequest("PUT","/foo/1")) should be equalTo true
      Routing.routeExists(FakeRequest("DELETE","/foo/1")) should be equalTo true

      Routing.routeExists(FakeRequest("GET","/foo")) should be equalTo false
      Routing.routeExists(FakeRequest("POST","/foo")) should be equalTo false
      Routing.routeExists(FakeRequest("PUT","/foo")) should be equalTo false
      Routing.routeExists(FakeRequest("DELETE","/foo")) should be equalTo false

      Routing.routeExists(FakeRequest("GET","/bar")) should be equalTo false
      Routing.routeExists(FakeRequest("POST","/bar")) should be equalTo false
      Routing.routeExists(FakeRequest("PUT","/bar")) should be equalTo false
      Routing.routeExists(FakeRequest("DELETE","/bar")) should be equalTo false
    }

    "return the correct method list" in new WithApplication(app = appWithRoutes()) {
      Routing.getMethods(FakeRequest("GET","/")) should contain(===(Seq("GET", "POST"))) and have size 2
      Routing.getMethods(FakeRequest("POST","/")) should contain(===(Seq("GET", "POST"))) and have size 2
      Routing.getMethods(FakeRequest("PUT","/")) should contain(===(Seq("GET", "POST"))) and have size 2
      Routing.getMethods(FakeRequest("DELETE","/")) should contain(===(Seq("GET", "POST"))) and have size 2

      Routing.getMethods(FakeRequest("GET","/foo")) must have size 0
      Routing.getMethods(FakeRequest("POST","/foo")) must have size 0
      Routing.getMethods(FakeRequest("PUT","/foo")) must have size 0
      Routing.getMethods(FakeRequest("DELETE","/foo")) must have size 0

      Routing.getMethods(FakeRequest("GET","/foo/1")) should contain(===(Seq("GET", "POST", "PUT", "DELETE"))) and have size 4
      Routing.getMethods(FakeRequest("POST","/foo/1")) should contain(===(Seq("GET", "POST", "PUT", "DELETE"))) and have size 4
      Routing.getMethods(FakeRequest("PUT","/foo/1")) should contain(===(Seq("GET", "POST", "PUT", "DELETE"))) and have size 4
      Routing.getMethods(FakeRequest("DELETE","/foo/1")) should contain(===(Seq("GET", "POST", "PUT", "DELETE"))) and have size 4
    }
  }
}
