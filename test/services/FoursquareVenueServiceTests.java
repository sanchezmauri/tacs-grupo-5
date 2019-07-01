package services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FoursquareVenueServiceTests {


    private MongoDbConnectionService dbConnectionService;
    private FoursquareVenueService service;
    @Before
    public void setUp() {
        dbConnectionService = new MongoDbConnectionService("mongodb://localhost:27017");

        service = new FoursquareVenueService(dbConnectionService);
    }

    @After
    public void tearDown(){

    }

    @Test
    public void find(){

        var res = service.getUsersInterestedCount("Zapatos");

    }
}
