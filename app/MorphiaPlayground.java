import dev.morphia.mapping.MappingException;
import models.FoursquareVenue;
import models.Rol;
import models.User;
import models.VenueList;
import models.exceptions.UserException;
import services.FoursquareVenueService;
import services.MongoDbConnectionService;
import services.UsersService;

public class MorphiaPlayground {

    //Commented because static playground doesn't allow for dependency injected Services

    /*
    private static void createUserOneList() {
        try {
            User pepe = new User("capo", "capo@hotmail.com", "123", Rol.SYSUSER);
            new UsersService(new MongoDbConnectionService()).create(pepe);


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
            "El Lugar donde cosas salvajes",
            "Ambroseti 123"
        );

        VenueList list = user.getAllLists().get(0);

        user.addVenueToList(list, fqVenue).ifPresent(addedVenue -> {
            UsersService.addVenueToList(user, list, addedVenue);
        });

    }
*/
    public static void main(String[] args) {
        //createUserOneList();
        //createUserWithList();
        //addVenueToList();
    }
}
