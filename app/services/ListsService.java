package services;

import dev.morphia.Key;
import models.User;
import models.VenueList;
import org.bson.types.ObjectId;

import java.util.Objects;
import java.util.Optional;

public class ListsService {
    public static Key<VenueList> create(VenueList list) {
        return MongoDbConnectionService.getDatastore().save(list);
    }

    public static Optional<VenueList> getById(String listId) {

        var query = MongoDbConnectionService.getDatastore()
                .createQuery(VenueList.class)
                .field("_id")
                .equal(listId);

        var user = MongoDbConnectionService.getDatastore()
                .createQuery(User.class)
                .field("venueslists")
                .elemMatch(query)
                .first();

        if (user == null) {
            return Optional.empty();
        } else {
            return user.getList(listId);
        }


    }
}

