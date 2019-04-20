// @GENERATOR:play-routes-compiler
// @SOURCE:C:/Users/nicox/Workspace/tacs-grupo-5/conf/routes
// @DATE:Sat Apr 20 20:43:15 ART 2019

import play.api.mvc.Call


import _root_.controllers.Assets.Asset
import _root_.play.libs.F

// @LINE:6
package controllers {

  // @LINE:21
  class ReverseVenuesController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:22
    def usersInterested(id:Long): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/venues/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[Long]].unbind("id", id)) + "/userCount")
    }
  
    // @LINE:23
    def venuesAddedSince(since:String = null): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/venues/count" + play.core.routing.queryString(List(if(since == null) None else Some(implicitly[play.api.mvc.QueryStringBindable[String]].unbind("since", since)))))
    }
  
    // @LINE:21
    def search(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/venues")
    }
  
  }

  // @LINE:6
  class ReverseAuthenticateController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:7
    def logout(): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "api/security/logout")
    }
  
    // @LINE:6
    def authenticate(): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "api/security/login")
    }
  
  }

  // @LINE:11
  class ReverseUsersController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:16
    def listsCount(user:models.User): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/users/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[models.User]].unbind("user", user)) + "/lists/count")
    }
  
    // @LINE:17
    def placesCount(user:models.User, visited:java.util.Optional[java.lang.Boolean]): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/users/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[models.User]].unbind("user", user)) + "/places/count" + play.core.routing.queryString(List(Some(implicitly[play.api.mvc.QueryStringBindable[java.util.Optional[java.lang.Boolean]]].unbind("visited", visited)))))
    }
  
    // @LINE:18
    def lastAccess(user:models.User): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/users/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[models.User]].unbind("user", user)) + "/lastAccess")
    }
  
    // @LINE:15
    def user(user:models.User): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/users/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[models.User]].unbind("user", user)))
    }
  
    // @LINE:14
    def list(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/users")
    }
  
    // @LINE:11
    def create(): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "api/users")
    }
  
  }


}
