package services;

import com.mongodb.MongoClient;
import dev.morphia.Datastore;
import dev.morphia.Morphia;

public class mongoDbConectionService {

    final static Morphia morphia = new Morphia();

static {
    morphia.mapPackage("models");
    final Datastore datastore = morphia.createDatastore(new MongoClient(), "TACS");
    datastore.ensureIndexes();
}

}
