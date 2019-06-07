package services;

import models.Venue;
import models.exceptions.UserException;
import org.bson.types.ObjectId;


public class VenuesService {

    public static Venue findById(String id)
    {
        return MongoDbConectionService.getDatastore().createQuery(Venue.class).field("id").equal(new ObjectId(id)).first();
    }

    public static void create(Venue venue) throws UserException {
        try {
            MongoDbConectionService.getDatastore().save(venue);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

