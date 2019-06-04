package services;

import com.mongodb.MongoClient;
import dev.morphia.Datastore;
import dev.morphia.Morphia;

public class MongoDbConectionService {

    static String workingDataBaseName = "TACS";

    final static Morphia morphia = new Morphia();
static {
    morphia.mapPackage("models");
}

    public static Morphia getMorphia() {
        return morphia;
    }

    public static Datastore getDatastore() {
        Datastore datastore = morphia.createDatastore(new MongoClient(), workingDataBaseName);
        datastore.ensureIndexes();
        return datastore;
    }



    static public void setWorkingDataBaseToProduction(){
    workingDataBaseName = "TACS";
    }

    static public void setWorkingDataBaseToTest(){
        workingDataBaseName = "TEST";
    }

}
