package controllers;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Rol;
import models.User;
import models.exceptions.UserException;
import play.mvc.Http;
import play.mvc.Result;
import repos.UserRepository;
import services.MongoDbService;
import services.UsersService;

import java.util.Optional;

import static play.test.Helpers.*;

public class SetUp {


    public static void startTestDataBase(){
        MongoDbService.setActualWorkingDataBaseName(MongoDbService.getTestDataBaseName());
        MongoDbService.getDataBase(MongoDbService.getActualWorkingDataBase());
    }

    public static void dropTestDataBase(){
        MongoDbService.setActualWorkingDataBaseName(MongoDbService.getProductionDataBaseName());
        MongoDbService.dropDataBase(MongoDbService.getTestDataBaseName());
    }

    public static Optional<Http.Cookie> loginAPI(String email, String password) {
        ObjectNode authNode = JsonNodeFactory.instance.objectNode();
        authNode.put("email", email);
        authNode.put("password", password);
        Result result = route(fakeApplication(), fakeRequest(POST, "/api/security/login").bodyJson(authNode));
        return result.cookies().getCookie("token");
    }

    public static void initSystem() throws UserException {
        User newUser = new User(
                UserRepository.nextId(),
                "admin","admin","admin", Rol.ROOT);
        UsersService.create(newUser);
    }

}
