package controllers;

import services.MongoDbService;

public class SetUp {

    public static void startTestDataBase(){
        MongoDbService.getDataBase(MongoDbService.getTestDataBaseName());
    }

    public static void dropTestDataBase(){
        MongoDbService.getDataBase(MongoDbService.getTestDataBaseName()).drop();
    }

}
