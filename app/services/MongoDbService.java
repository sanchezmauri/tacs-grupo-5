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
    static String actualWorkingDataBaseName = getProductionDataBaseName();

    public static String getActualWorkingDataBaseName() {
        return actualWorkingDataBaseName;
    }

    public static void setActualWorkingDataBaseName(String actualWorkingDataBaseName) {
        MongoDbService.actualWorkingDataBaseName = actualWorkingDataBaseName;
    }

    public static String getProductionDataBaseName(){
        return "TACS";
    }

    public static String getTestDataBaseName(){
        return "TEST";
    }

    public static MongoDatabase getDataBase(String database){
        return mongoClient.getDatabase(database);
    }

    public static void dropDataBase(String database){
        mongoClient.getDatabase(database).drop();
    }

    private static MongoCollection<Document> getCollection(String database, String collectionName) {
        MongoCollection<Document> collection = getDataBase(database).getCollection(collectionName);
        return collection;
    }

    public static void insertDocument(String dataBase, String collectionName, Object object) {
        getCollection(dataBase, collectionName).insertOne(Document.parse(Json.toJson(object).toString()));
    }

    public static FindIterable<Document> getDocument(String dataBase, String collectionName, String key, Object value) {
        return getCollection(dataBase, collectionName).find(eq(key, value));
    }

    public static FindIterable<Document> getAllDocuments(String dataBase, String collectionName) {

        return getCollection(dataBase, collectionName).find();
    }

    public static void deleteDocument(String dataBase, String collectionName, FindIterable<Document> document) {
        getCollection(dataBase, collectionName).findOneAndDelete((Bson) document);
    }


}
