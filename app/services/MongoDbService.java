package services;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import play.libs.Json;


import static com.mongodb.client.model.Filters.eq;

public class MongoDbService {

   static MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));

    static MongoDatabase database = mongoClient.getDatabase("TACS");

    static MongoCollection<Document> getCollection (String collectionName)
    {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        return collection;
    }

    static void insertDocument(MongoCollection<Document> collection, Object object){
        collection.insertOne(Document.parse(Json.toJson(object).toString()));
    }

    static FindIterable<Document> getDocument(MongoCollection<Document> collection, String key, Object value){
       return collection.find(eq(key, value));
    }

    static void deleteDocument(MongoCollection<Document> collection, FindIterable<Document> document){
        collection.findOneAndDelete((Bson) document);
    }





}
