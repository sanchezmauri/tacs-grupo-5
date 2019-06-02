package services;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import play.libs.Json;


import javax.inject.*;

public class MongoDbService {

   static MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));

    static MongoDatabase database = mongoClient.getDatabase("TACS");

    static MongoCollection<Document> getCollection (String collectionName)
    {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        return collection;
    }

    static void insert(MongoCollection<Document> collection, Object object){
        collection.insertOne(Document.parse(Json.toJson(object).toString()));
    }




}
