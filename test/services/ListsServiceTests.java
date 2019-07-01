package services;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoClient;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.mapping.MapperOptions;
import dev.morphia.query.Query;
import dev.morphia.query.UpdateOperations;
import models.FoursquareVenue;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ListsServiceTests {


    private MongoDbConnectionService dbConnectionService;
    private ListsService service;
    @Before
    public void setUp() {
        dbConnectionService = new MongoDbConnectionService("mongodb://localhost:27017");

        service = new ListsService(dbConnectionService);
    }

    @After
    public void tearDown(){

    }

    @Test
    public void listUpdateTest(){

        service.removeVenueFromList("5d18f0c2c60ad93602b1d912", "51805268e4b099161064786c");

    }


}
