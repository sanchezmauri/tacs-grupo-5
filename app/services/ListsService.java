package services;

import dev.morphia.Key;
import models.VenueList;

public class ListsService {
    public static Key<VenueList> create(VenueList list) {
        return MongoDbConnectionService.getDatastore().save(list);
    }
}
