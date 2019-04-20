// @GENERATOR:play-routes-compiler
// @SOURCE:C:/Users/nicox/Workspace/tacs-2019c1/conf/routes
// @DATE:Sat Apr 20 20:20:59 ART 2019


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
