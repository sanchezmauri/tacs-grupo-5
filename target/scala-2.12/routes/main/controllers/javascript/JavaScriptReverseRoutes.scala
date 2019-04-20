// @GENERATOR:play-routes-compiler
// @SOURCE:C:/Users/nicox/Workspace/tacs-grupo-5/conf/routes
// @DATE:Sat Apr 20 20:43:15 ART 2019

import play.api.routing.JavaScriptReverseRoute


import _root_.controllers.Assets.Asset
import _root_.play.libs.F

// @LINE:6
package controllers.javascript {

  // @LINE:21
  class ReverseVenuesController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:22
    def usersInterested: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.VenuesController.usersInterested",
      """
        function(id0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/venues/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[Long]].javascriptUnbind + """)("id", id0)) + "/userCount"})
        }
      """
    )
  
    // @LINE:23
    def venuesAddedSince: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.VenuesController.venuesAddedSince",
      """
        function(since0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/venues/count" + _qS([(since0 == null ? null : (""" + implicitly[play.api.mvc.QueryStringBindable[String]].javascriptUnbind + """)("since", since0))])})
        }
      """
    )
  
    // @LINE:21
    def search: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.VenuesController.search",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/venues"})
        }
      """
    )
  
  }

  // @LINE:6
  class ReverseAuthenticateController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:7
    def logout: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.AuthenticateController.logout",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "api/security/logout"})
        }
      """
    )
  
    // @LINE:6
    def authenticate: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.AuthenticateController.authenticate",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "api/security/login"})
        }
      """
    )
  
  }

  // @LINE:11
  class ReverseUsersController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:16
    def listsCount: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.UsersController.listsCount",
      """
        function(user0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/users/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[models.User]].javascriptUnbind + """)("user", user0)) + "/lists/count"})
        }
      """
    )
  
    // @LINE:17
    def placesCount: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.UsersController.placesCount",
      """
        function(user0,visited1) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/users/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[models.User]].javascriptUnbind + """)("user", user0)) + "/places/count" + _qS([(""" + implicitly[play.api.mvc.QueryStringBindable[java.util.Optional[java.lang.Boolean]]].javascriptUnbind + """)("visited", visited1)])})
        }
      """
    )
  
    // @LINE:18
    def lastAccess: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.UsersController.lastAccess",
      """
        function(user0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/users/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[models.User]].javascriptUnbind + """)("user", user0)) + "/lastAccess"})
        }
      """
    )
  
    // @LINE:15
    def user: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.UsersController.user",
      """
        function(user0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/users/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[models.User]].javascriptUnbind + """)("user", user0))})
        }
      """
    )
  
    // @LINE:14
    def list: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.UsersController.list",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/users"})
        }
      """
    )
  
    // @LINE:11
    def create: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.UsersController.create",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "api/users"})
        }
      """
    )
  
  }


}
