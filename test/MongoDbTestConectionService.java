import com.mongodb.MongoClient;
import dev.morphia.Datastore;
import dev.morphia.Morphia;

public class MongoDbTestConectionService {


        final static Morphia morphia = new Morphia();
        static {
            morphia.mapPackage("models");
            final Datastore datastore = morphia.createDatastore(new MongoClient(), "TEST");
            datastore.ensureIndexes();
        }

}
