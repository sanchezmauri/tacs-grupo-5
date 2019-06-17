package services;

import com.google.inject.AbstractModule;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.typesafe.config.Config;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.mapping.MapperOptions;
import dev.morphia.mapping.MappingException;
import models.FoursquareVenue;
import models.User;
import models.UserVenue;
import models.VenueList;
import play.Environment;
import play.api.Play;
import play.inject.Binding;
import play.inject.Module;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;

@Singleton
public class MongoDbConnectionService {
/*
    private static MongoDbConnectionService _instance;

    public static MongoDbConnectionService getInstance() {
        if (_instance == null) {
            _instance = new MongoDbConnectionService();
        }
        return _instance;
    }

    public static Datastore getDatastore() {
        return getInstance().getInternalDatastore();
    }

    */
    private String workingDataBaseName = "TACS";

    private final Morphia morphia = new Morphia();
    private Datastore datastore = null;

    private final String mongoConnectionString;

    @Inject
    public MongoDbConnectionService(Config config) {
        this.mongoConnectionString = config.getString("mongo.connectionString");
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

    private Morphia getMorphia() {
        return morphia;
    }




    public Datastore getDatastore() {
        if (datastore == null) {
            System.out.println("The mongo Connection is --> "+mongoConnectionString+" <-- ");

            datastore = morphia.createDatastore(new MongoClient(new MongoClientURI(mongoConnectionString)), workingDataBaseName);
            datastore.ensureIndexes();
        }

        return datastore;
    }

    private void setWorkingDataBase(String baseName) {
        if (!baseName.equals(workingDataBaseName)) {
            workingDataBaseName = baseName;
            datastore = null;
        }
    }

    public void setWorkingDataBaseToProduction() {
        setWorkingDataBase("TACS");
    }

    public void setWorkingDataBaseToTest(){
        setWorkingDataBase("TEST");
    }

}
