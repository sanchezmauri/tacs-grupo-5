// @GENERATOR:play-routes-compiler
// @SOURCE:C:/Users/nicox/Workspace/tacs-2019c1/conf/routes
// @DATE:Sat Apr 20 20:20:59 ART 2019

package controllers;

import router.RoutesPrefix;

public class routes {
  
  public static final controllers.ReverseVenuesController VenuesController = new controllers.ReverseVenuesController(RoutesPrefix.byNamePrefix());
  public static final controllers.ReverseAuthenticateController AuthenticateController = new controllers.ReverseAuthenticateController(RoutesPrefix.byNamePrefix());
  public static final controllers.ReverseUsersController UsersController = new controllers.ReverseUsersController(RoutesPrefix.byNamePrefix());

  public static class javascript {
    
    public static final controllers.javascript.ReverseVenuesController VenuesController = new controllers.javascript.ReverseVenuesController(RoutesPrefix.byNamePrefix());
    public static final controllers.javascript.ReverseAuthenticateController AuthenticateController = new controllers.javascript.ReverseAuthenticateController(RoutesPrefix.byNamePrefix());
    public static final controllers.javascript.ReverseUsersController UsersController = new controllers.javascript.ReverseUsersController(RoutesPrefix.byNamePrefix());
  }

}
