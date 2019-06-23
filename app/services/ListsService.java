package services;

import dev.morphia.Key;
import models.User;
import models.VenueList;
import org.bson.types.ObjectId;

import javax.inject.Inject;
import java.util.Objects;
import java.util.Optional;

public class ListsService {

    private MongoDbConnectionService dbConnectionService;

    @Inject
    public ListsService(MongoDbConnectionService dbConnection){
        this.dbConnectionService = dbConnection;
    }

    public Key<VenueList> create(VenueList list) {
        return dbConnectionService.getDatastore().save(list);
    }

    public Optional<VenueList> getById(String listId) {

        var query = dbConnectionService.getDatastore()
                .createQuery(VenueList.class)
                .field("_id")
                .equal(listId);

        var user = dbConnectionService.getDatastore()
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

