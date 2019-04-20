// @GENERATOR:play-routes-compiler
// @SOURCE:C:/Users/nicox/Workspace/tacs-2019c1/conf/routes
// @DATE:Sat Apr 20 20:20:59 ART 2019

package router

import play.core.routing._
import play.core.routing.HandlerInvokerFactory._

import play.api.mvc._

import _root_.controllers.Assets.Asset
import _root_.play.libs.F

class Routes(
  override val errorHandler: play.api.http.HttpErrorHandler, 
  // @LINE:6
  AuthenticateController_1: controllers.AuthenticateController,
  // @LINE:11
  UsersController_2: controllers.UsersController,
  // @LINE:21
  VenuesController_0: controllers.VenuesController,
  val prefix: String
) extends GeneratedRouter {

   @javax.inject.Inject()
   def this(errorHandler: play.api.http.HttpErrorHandler,
    // @LINE:6
    AuthenticateController_1: controllers.AuthenticateController,
    // @LINE:11
    UsersController_2: controllers.UsersController,
    // @LINE:21
    VenuesController_0: controllers.VenuesController
  ) = this(errorHandler, AuthenticateController_1, UsersController_2, VenuesController_0, "/")

  def withPrefix(addPrefix: String): Routes = {
    val prefix = play.api.routing.Router.concatPrefix(addPrefix, this.prefix)
    router.RoutesPrefix.setPrefix(prefix)
    new Routes(errorHandler, AuthenticateController_1, UsersController_2, VenuesController_0, prefix)
  }

  private[this] val defaultPrefix: String = {
    if (this.prefix.endsWith("/")) "" else "/"
  }

  def documentation = List(
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/security/login""", """controllers.AuthenticateController.authenticate(request:Request)"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/security/logout""", """controllers.AuthenticateController.logout()"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/users""", """controllers.UsersController.create(request:Request)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/users""", """controllers.UsersController.list"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/users/""" + "$" + """user<[^/]+>""", """controllers.UsersController.user(user:models.User)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/users/""" + "$" + """user<[^/]+>/lists/count""", """controllers.UsersController.listsCount(user:models.User)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/users/""" + "$" + """user<[^/]+>/places/count""", """controllers.UsersController.placesCount(user:models.User, visited:java.util.Optional[java.lang.Boolean])"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/users/""" + "$" + """user<[^/]+>/lastAccess""", """controllers.UsersController.lastAccess(user:models.User)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/venues""", """controllers.VenuesController.search(request:Request)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/venues/""" + "$" + """id<[^/]+>/userCount""", """controllers.VenuesController.usersInterested(id:Long)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/venues/count""", """controllers.VenuesController.venuesAddedSince(since:String ?= null)"""),
    Nil
  ).foldLeft(List.empty[(String,String,String)]) { (s,e) => e.asInstanceOf[Any] match {
    case r @ (_,_,_) => s :+ r.asInstanceOf[(String,String,String)]
    case l => s ++ l.asInstanceOf[List[(String,String,String)]]
  }}


  // @LINE:6
  private[this] lazy val controllers_AuthenticateController_authenticate0_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/security/login")))
  )
  private[this] lazy val controllers_AuthenticateController_authenticate0_invoker = createInvoker(
    
    (req:play.mvc.Http.Request) =>
      AuthenticateController_1.authenticate(fakeValue[play.mvc.Http.Request]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.AuthenticateController",
      "authenticate",
      Seq(classOf[play.mvc.Http.Request]),
      "POST",
      this.prefix + """api/security/login""",
      """Auth""",
      Seq()
    )
  )

  // @LINE:7
  private[this] lazy val controllers_AuthenticateController_logout1_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/security/logout")))
  )
  private[this] lazy val controllers_AuthenticateController_logout1_invoker = createInvoker(
    AuthenticateController_1.logout(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.AuthenticateController",
      "logout",
      Nil,
      "POST",
      this.prefix + """api/security/logout""",
      """""",
      Seq()
    )
  )

  // @LINE:11
  private[this] lazy val controllers_UsersController_create2_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/users")))
  )
  private[this] lazy val controllers_UsersController_create2_invoker = createInvoker(
    
    (req:play.mvc.Http.Request) =>
      UsersController_2.create(fakeValue[play.mvc.Http.Request]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.UsersController",
      "create",
      Seq(classOf[play.mvc.Http.Request]),
      "POST",
      this.prefix + """api/users""",
      """ users resource""",
      Seq()
    )
  )

  // @LINE:14
  private[this] lazy val controllers_UsersController_list3_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/users")))
  )
  private[this] lazy val controllers_UsersController_list3_invoker = createInvoker(
    UsersController_2.list,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.UsersController",
      "list",
      Nil,
      "GET",
      this.prefix + """api/users""",
      """users (for admins)""",
      Seq()
    )
  )

  // @LINE:15
  private[this] lazy val controllers_UsersController_user4_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/users/"), DynamicPart("user", """[^/]+""",true)))
  )
  private[this] lazy val controllers_UsersController_user4_invoker = createInvoker(
    UsersController_2.user(fakeValue[models.User]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.UsersController",
      "user",
      Seq(classOf[models.User]),
      "GET",
      this.prefix + """api/users/""" + "$" + """user<[^/]+>""",
      """""",
      Seq()
    )
  )

  // @LINE:16
  private[this] lazy val controllers_UsersController_listsCount5_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/users/"), DynamicPart("user", """[^/]+""",true), StaticPart("/lists/count")))
  )
  private[this] lazy val controllers_UsersController_listsCount5_invoker = createInvoker(
    UsersController_2.listsCount(fakeValue[models.User]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.UsersController",
      "listsCount",
      Seq(classOf[models.User]),
      "GET",
      this.prefix + """api/users/""" + "$" + """user<[^/]+>/lists/count""",
      """""",
      Seq()
    )
  )

  // @LINE:17
  private[this] lazy val controllers_UsersController_placesCount6_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/users/"), DynamicPart("user", """[^/]+""",true), StaticPart("/places/count")))
  )
  private[this] lazy val controllers_UsersController_placesCount6_invoker = createInvoker(
    UsersController_2.placesCount(fakeValue[models.User], fakeValue[java.util.Optional[java.lang.Boolean]]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.UsersController",
      "placesCount",
      Seq(classOf[models.User], classOf[java.util.Optional[java.lang.Boolean]]),
      "GET",
      this.prefix + """api/users/""" + "$" + """user<[^/]+>/places/count""",
      """""",
      Seq()
    )
  )

  // @LINE:18
  private[this] lazy val controllers_UsersController_lastAccess7_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/users/"), DynamicPart("user", """[^/]+""",true), StaticPart("/lastAccess")))
  )
  private[this] lazy val controllers_UsersController_lastAccess7_invoker = createInvoker(
    UsersController_2.lastAccess(fakeValue[models.User]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.UsersController",
      "lastAccess",
      Seq(classOf[models.User]),
      "GET",
      this.prefix + """api/users/""" + "$" + """user<[^/]+>/lastAccess""",
      """""",
      Seq()
    )
  )

  // @LINE:21
  private[this] lazy val controllers_VenuesController_search8_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/venues")))
  )
  private[this] lazy val controllers_VenuesController_search8_invoker = createInvoker(
    
    (req:play.mvc.Http.Request) =>
      VenuesController_0.search(fakeValue[play.mvc.Http.Request]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.VenuesController",
      "search",
      Seq(classOf[play.mvc.Http.Request]),
      "GET",
      this.prefix + """api/venues""",
      """ venues resource""",
      Seq()
    )
  )

  // @LINE:22
  private[this] lazy val controllers_VenuesController_usersInterested9_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/venues/"), DynamicPart("id", """[^/]+""",true), StaticPart("/userCount")))
  )
  private[this] lazy val controllers_VenuesController_usersInterested9_invoker = createInvoker(
    VenuesController_0.usersInterested(fakeValue[Long]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.VenuesController",
      "usersInterested",
      Seq(classOf[Long]),
      "GET",
      this.prefix + """api/venues/""" + "$" + """id<[^/]+>/userCount""",
      """""",
      Seq()
    )
  )

  // @LINE:23
  private[this] lazy val controllers_VenuesController_venuesAddedSince10_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/venues/count")))
  )
  private[this] lazy val controllers_VenuesController_venuesAddedSince10_invoker = createInvoker(
    VenuesController_0.venuesAddedSince(fakeValue[String]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.VenuesController",
      "venuesAddedSince",
      Seq(classOf[String]),
      "GET",
      this.prefix + """api/venues/count""",
      """""",
      Seq()
    )
  )


  def routes: PartialFunction[RequestHeader, Handler] = {
  
    // @LINE:6
    case controllers_AuthenticateController_authenticate0_route(params@_) =>
      call { 
        controllers_AuthenticateController_authenticate0_invoker.call(
          req => AuthenticateController_1.authenticate(req))
      }
  
    // @LINE:7
    case controllers_AuthenticateController_logout1_route(params@_) =>
      call { 
        controllers_AuthenticateController_logout1_invoker.call(AuthenticateController_1.logout())
      }
  
    // @LINE:11
    case controllers_UsersController_create2_route(params@_) =>
      call { 
        controllers_UsersController_create2_invoker.call(
          req => UsersController_2.create(req))
      }
  
    // @LINE:14
    case controllers_UsersController_list3_route(params@_) =>
      call { 
        controllers_UsersController_list3_invoker.call(UsersController_2.list)
      }
  
    // @LINE:15
    case controllers_UsersController_user4_route(params@_) =>
      call(params.fromPath[models.User]("user", None)) { (user) =>
        controllers_UsersController_user4_invoker.call(UsersController_2.user(user))
      }
  
    // @LINE:16
    case controllers_UsersController_listsCount5_route(params@_) =>
      call(params.fromPath[models.User]("user", None)) { (user) =>
        controllers_UsersController_listsCount5_invoker.call(UsersController_2.listsCount(user))
      }
  
    // @LINE:17
    case controllers_UsersController_placesCount6_route(params@_) =>
      call(params.fromPath[models.User]("user", None), params.fromQuery[java.util.Optional[java.lang.Boolean]]("visited", None)) { (user, visited) =>
        controllers_UsersController_placesCount6_invoker.call(UsersController_2.placesCount(user, visited))
      }
  
    // @LINE:18
    case controllers_UsersController_lastAccess7_route(params@_) =>
      call(params.fromPath[models.User]("user", None)) { (user) =>
        controllers_UsersController_lastAccess7_invoker.call(UsersController_2.lastAccess(user))
      }
  
    // @LINE:21
    case controllers_VenuesController_search8_route(params@_) =>
      call { 
        controllers_VenuesController_search8_invoker.call(
          req => VenuesController_0.search(req))
      }
  
    // @LINE:22
    case controllers_VenuesController_usersInterested9_route(params@_) =>
      call(params.fromPath[Long]("id", None)) { (id) =>
        controllers_VenuesController_usersInterested9_invoker.call(VenuesController_0.usersInterested(id))
      }
  
    // @LINE:23
    case controllers_VenuesController_venuesAddedSince10_route(params@_) =>
      call(params.fromQuery[String]("since", Some(null))) { (since) =>
        controllers_VenuesController_venuesAddedSince10_invoker.call(VenuesController_0.venuesAddedSince(since))
      }
  }
}
