package services;

import com.mongodb.MongoClient;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.mapping.MapperOptions;
import dev.morphia.mapping.MappingException;
import models.FoursquareVenue;
import models.User;
import models.UserVenue;
import models.VenueList;

public class MongoDbConectionService {

    static String workingDataBaseName = "TACS";

    final static Morphia morphia = new Morphia();
    static Datastore datastore = null;

static {
    try {
        // morphia.mapPackage("models.User");
        morphia.map(FoursquareVenue.class, UserVenue.class, VenueList.class, User.class);
        morphia.getMapper().setOptions(
            MapperOptions.builder()
                .storeEmpties(true)
                .build()
        );
    } catch(MappingException mapError) {
        mapError.printStackTrace();
        throw mapError;
    }
}

    public static Morphia getMorphia() {
        return morphia;
    }

    public static Datastore getDatastore() {
        if (datastore == null) {
            datastore = morphia.createDatastore(new MongoClient(), workingDataBaseName);
            datastore.ensureIndexes();
        }

        return datastore;
    }

    private static void setWorkingDataBase(String baseName) {
        if (!baseName.equals(workingDataBaseName)) {
            workingDataBaseName = baseName;
            datastore = null;
        }
    }

    static public void setWorkingDataBaseToProduction() {
        setWorkingDataBase("TACS");
    }

    static public void setWorkingDataBaseToTest(){
        setWorkingDataBase("TEST");
    }
}
