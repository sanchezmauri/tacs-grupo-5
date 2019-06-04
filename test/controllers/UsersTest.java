package controllers;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Http;
import play.mvc.Result;

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
    public void createUser(){
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

}
