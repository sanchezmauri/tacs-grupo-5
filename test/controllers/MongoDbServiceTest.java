package controllers;


import org.junit.Assert;
import org.junit.Test;
import services.MongoDbService;


public class MongoDbServiceTest {

    @Test
    public void createDataBase(){
        SetUp.startTestDataBase();
        Assert.assertNotNull(MongoDbService.getDataBase(MongoDbService.getTestDataBaseName()));
    }

    @Test
    public void dropDataBase(){
        SetUp.dropTestDataBase();
    }

}
