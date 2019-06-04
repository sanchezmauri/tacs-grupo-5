package controllers;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Http;
import play.mvc.Result;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static play.test.Helpers.*;
import static play.test.Helpers.POST;

public class UsersTest {

    @Before
    public void startDbTestingMode(){
        SetUp.startTestDataBase();
    }

    @After
    public void stopDbTestingMode(){
        SetUp.dropTestDataBase();
    }

    @Test
    public void create(){
        try {
            ObjectNode user = JsonNodeFactory.instance.objectNode();
            user.put("name","test");
            user.put("email","test@gmail.com");
            user.put("password","test");
            Result result = route(fakeApplication(), fakeRequest(POST, "/api/users").bodyJson(user));
            assertEquals(CREATED, result.status());
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Test
    public void getAll(){
        try {

            SetUp.initSystem();
            Optional<Http.Cookie> cookie = SetUp.loginAPI("admin", "admin");
            ObjectNode user = JsonNodeFactory.instance.objectNode();
            user.put("name","test");
            user.put("email","test@gmail.com");
            user.put("password","test");
             route(fakeApplication(), fakeRequest(POST, "/api/users").bodyJson(user));
            user = JsonNodeFactory.instance.objectNode();
            user.put("name","prueba");
            user.put("email","prueba@gmail.com");
            user.put("password","prueba");
             route(fakeApplication(), fakeRequest(POST, "/api/users").bodyJson(user));

             Result result = route(fakeApplication(), fakeRequest(GET, "/api/users").cookie(cookie.get()));

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

}
