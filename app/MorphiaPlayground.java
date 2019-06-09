import dev.morphia.Datastore;
import dev.morphia.mapping.MappingException;
import models.FoursquareVenue;
import models.Rol;
import models.User;
import models.VenueList;
import models.exceptions.UserException;
import org.bson.types.ObjectId;
import play.libs.Json;
import services.FoursquareVenueService;
import services.MongoDbConectionService;
import services.UsersService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class MorphiaPlayground {
    private static void createUserOneList() {
        try {
            User pepe = new User("capo", "capo@hotmail.com", "123", Rol.SYSUSER);
            UsersService.create(pepe);


        } catch (MappingException mapError) {
            mapError.printStackTrace();
        } catch (UserException mapError) {
            mapError.printStackTrace();
        }
    }

    private static void createUserWithList() {
        try {
            User pepe = new User("paul", "paul@hotmail.com", "123", Rol.SYSUSER);
            UsersService.create(pepe);

            VenueList newList = new VenueList("planetas");

            pepe.addList(newList);
            UsersService.addList(pepe, newList);

        } catch (MappingException mapError) {
            mapError.printStackTrace();
        } catch (UserException mapError) {
            mapError.printStackTrace();
        }
    }

    private static void addVenueToList() {
        User user = UsersService.findByEmail("paul@hotmail.com");

        FoursquareVenue fqVenue = FoursquareVenueService.getOrCreate(
                "4ed9866346907c1b41a0e5f6",
            "El Lugar donde cosas salvajes",
            "Ambroseti 123"
        );

        VenueList list = user.getAllLists().get(0);

        user.addVenueToList(list, fqVenue).ifPresent(addedVenue -> {
            UsersService.addVenueToList(user, list, addedVenue);
        });

    }

    public static void main(String[] args) {
        //createUserOneList();
        createUserWithList();
        addVenueToList();
    }
}
