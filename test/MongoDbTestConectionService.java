import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.mapping.MapperOptions;
import models.Rol;
import models.User;
import org.junit.Assert;
import org.junit.Test;

public class MongoDbTestConectionService {

    static String clientURI = "Paste here the connection string from the TACS Gdoc";

    @Test
    public void connect(){
        Morphia morphia = new Morphia();
        morphia.map(User.class);
        morphia.getMapper().setOptions(
                MapperOptions.builder()
                        .storeEmpties(true)
                        .build()
        );
        try {
            morphia.mapPackage("models");
            Datastore datastore = morphia.createDatastore(new MongoClient(new MongoClientURI(clientURI)), "TEST");
            datastore.ensureIndexes();

            User testUser = new User("Test", "John","PasswordHArd123", Rol.SYSUSER);
            datastore.save(testUser);


            var query = datastore.find(User.class);

            Assert.assertEquals(query.count(), 1);

            datastore.getDatabase().drop();

        }catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }

    }


}
