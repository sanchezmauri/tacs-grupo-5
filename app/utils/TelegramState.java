package utils;

import models.VenueList;
import models.telegram.Location;
import models.venues.FSVenueSearch;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.List;

@Singleton
public class TelegramState {

    public HashMap<Long, String> loggedUserTokens = new HashMap<>();
    public HashMap<Long, String> pendingOperations = new HashMap<>();
    public HashMap<Long, HashMap<String, Object>> pendingOperationOptions = new HashMap<>();

    public HashMap<Long,HashMap<String, VenueList>> workingVenueList = new HashMap<>();
    public HashMap<Long, List<FSVenueSearch>> workingVenues = new HashMap<>();

    public HashMap<Long, Location> lastKnownLocations = new HashMap<>();
}
