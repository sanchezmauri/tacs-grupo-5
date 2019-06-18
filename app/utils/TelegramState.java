package utils;

import models.User;
import models.telegram.Location;

import javax.inject.Singleton;
import java.util.HashMap;

@Singleton
public class TelegramState {

    public HashMap<Integer, String> loggedUserTokens = new HashMap<>();
    public HashMap<Integer, String> pendingOperations = new HashMap<>();
    public HashMap<Integer, Location> lastKnownLocations = new HashMap<>();
}
