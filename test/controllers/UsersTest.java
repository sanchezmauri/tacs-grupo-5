package controllers;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Result;
import services.MongoDbConnectionService;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.CREATED;
import static play.test.Helpers.*;
import static play.test.Helpers.POST;

public class UsersTest {

    @Before
    public void setUpMongoDb(){
        MongoDbConnectionService.getInstance().setWorkingDataBaseToTest();
    }

    @After
    public void dropMongoDb(){
        MongoDbConnectionService.getDatastore().getDatabase().drop();
        MongoDbConnectionService.getInstance().setWorkingDataBaseToProduction();
    }

    @Test
    public void create(){
        try {
            ObjectNode user = JsonNodeFactory.instance.objectNode();
            user.put("name","test");
            user.put("email","test@gmail.com");
            user.put("password","test");
            user.put("rol","SYSUSER");
            Result result = route(fakeApplication(), fakeRequest(POST, "/api/users").bodyJson(user));
            assertEquals(CREATED, result.status());
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Test
    public void createDuplicated(){
        try {
            ObjectNode user = JsonNodeFactory.instance.objectNode();
            user.put("name","test");
            user.put("email","test@gmail.com");
            user.put("password","test");
            route(fakeApplication(), fakeRequest(POST, "/api/users").bodyJson(user));
            Result result = route(fakeApplication(), fakeRequest(POST, "/api/users").bodyJson(user));
            assertEquals(BAD_REQUEST, result.status());
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

}
