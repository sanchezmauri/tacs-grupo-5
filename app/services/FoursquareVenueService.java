package services;

import models.FoursquareVenue;
import models.exceptions.UserException;
import org.bson.types.ObjectId;

import java.time.LocalDate;


public class FoursquareVenueService {
    public static void create(FoursquareVenue fqVenue) throws Exception {
        FoursquareVenue fqVenueFromDB = findById(fqVenue.getId());

        if (fqVenueFromDB == null) {
            MongoDbConectionService.getDatastore().save(fqVenue);
        } else {
            throw new UserException("ya había un fqVenue con id " + fqVenue.getId());
        }
    }

    public static FoursquareVenue findById(String id) {
        return MongoDbConectionService.getDatastore()
                .createQuery(FoursquareVenue.class)
                .field("id")
                .equal(new ObjectId(id))
                .first();
    }

}
