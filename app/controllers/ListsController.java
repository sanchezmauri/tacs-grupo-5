package controllers;

import annotations.Authenticate;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import controllers.actions.VenueListAction;
import models.venues.FSVenueSearch;
import models.FoursquareVenue;
import models.*;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.With;
import services.*;

import javax.inject.Inject;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ListsController extends Controller {


    private final UsersService usersService;
    private final ListsService listsService;
    private final UserVenuesService userVenuesService;

    @Inject
    public ListsController(
            UsersService usersService,
            ListsService listsService,
            UserVenuesService userVenuesService) {
        this.usersService = usersService;
        this.listsService = listsService;
        this.userVenuesService = userVenuesService;
    }

    @Authenticate(types = {"ROOT", "SYSUSER"})
    public Result list(Http.Request request) {
        User user = request.attrs().get(RequestAttrs.USER);
        List<VenueList> allLists;

        // si es admin, mandarle todas las listas
        // si es user, solo las de el
        if (user.getRol().equals(Rol.ROOT)) {
            allLists = usersService.index()
                    .stream()
                    .flatMap(u -> u.getAllLists().stream())
                    .collect(Collectors.toList());
        } else {
            allLists = user.getAllLists();
        }

        return ok(Json.toJson(allLists));
    }

    // acá dejo pasar solo users porque
    // agrego el venue al user que está logueado
    // por ahí si el admin quiere agregar a 1 user un lugar
    // debería hacer: users/:userId/lists/:listId en vez de lists/:listId
    @Authenticate(types = "SYSUSER")
    public Result create(Http.Request request) {
        // extraer esto en algún lado tipo validar json o algo
        JsonNode newListJson = request.body().asJson();

        if (newListJson == null) {
            return badRequest(Utils.createErrorMessage("Ill formatted json."));
        }

        if (!newListJson.has("name")) {
            return badRequest(
                    Utils.createErrorMessage("Missing field: name")
            );
        }

        String listName = newListJson.get("name").asText();

        VenueList newList = new VenueList(listName);

        User user = request.attrs().get(RequestAttrs.USER);
        user.addList(newList);
        usersService.addList(user, newList);
        return created(Json.toJson(newList));
    }

    @Authenticate(types = {"SYSUSER"})
    @With(VenueListAction.class)
    public Result delete(String listId, Http.Request request) {
        User user = request.attrs().get(RequestAttrs.USER);
        usersService.deleteUserVenueList(user, listId);
        user.removeList(listId);

        return ok();
    }

    @Authenticate(types = {"SYSUSER"})
    public Result changeListName(String listId, Http.Request request) {
        JsonNode changeJson = request.body().asJson();

        if (!changeJson.has("name"))
            return badRequest(Utils.createErrorMessage("Missing field: name."));
        String newName = changeJson.get("name").asText();

        listsService.updateName(listId, newName);
        var list = listsService.getById(listId);

        return ok(Json.toJson(list));
    }

    @Authenticate(types = {"SYSUSER"})
    @With(VenueListAction.class)
    public Result addVenuesToList(String listId, Http.Request request) {

        JsonNode venuesJson = request.body().asJson();

        if (venuesJson.isObject()) {
            venuesJson = Json.newArray().add(venuesJson);
        }

        User user = request.attrs().get(RequestAttrs.USER);
        VenueList list = request.attrs().get(RequestAttrs.LIST);

        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerFor(new TypeReference<List<FSVenueSearch>>() {});
        List<FSVenueSearch> venueSearches;

        try {
             venueSearches = reader.readValue(venuesJson);
        } catch (IOException e) {
            e.printStackTrace();

            return badRequest("Unable to parse request into valid Venues, please confirm the structure is\n { \"id\": \"<SomeId>\", \"name\": \"<A valid name>\", \"location\": { \"address\": \"<Your Address>\" ... } }");
        }

        usersService.addVenuesToList(user, list, venueSearches);

        return ok(Json.toJson(list));
    }

    @Authenticate(types = {"SYSUSER"})
    public Result removeVenueFromList(String listId, Http.Request request) throws Exception {

        JsonNode venueIdJson = request.body().asJson();

        if (!venueIdJson.has("id"))
            return badRequest("Missing field: venueId");

        String venueId = venueIdJson.get("id").asText();




        if (listsService.removeVenueFromList(listId, venueId)) {
            var list = listsService.getById(listId);
            return ok(Json.toJson(list));
        } else
            return badRequest(Utils.createErrorMessage("No venue with id " + venueId));
    }

    @Authenticate(types = {"SYSUSER"})
    @With(VenueListAction.class)
    public Result visitVenue(String listId, String venueId, Http.Request request) {
        VenueList venueList = request.attrs().get(RequestAttrs.LIST);
        User user = request.attrs().get(RequestAttrs.USER);
        UserVenue userVenue = usersService.visitUserVenue(user, listId, venueId);

        if (userVenue == null)
            return badRequest(Utils.createErrorMessage("User hasn't venue " + venueId));
        else
            return ok(Json.toJson(userVenue));
    }

    @Authenticate(types = {"ROOT"})
    public Result compareUsersLists(Http.Request request) {

        var list1Id = request.getQueryString("list1");
        var list2Id = request.getQueryString("list2");

        if (list1Id.isEmpty() || list2Id.isEmpty()) {
            return badRequest(
                    Utils.createErrorMessage("Both List Id's are required.")
            );
        }

        var list1 = listsService.getById(list1Id);
        var list2 = listsService.getById(list2Id);

        if (list1.isEmpty()) {
            return badRequest(
                    Utils.createErrorMessage("List 1 not found.")
            );
        } else if (list2.isEmpty()) {
            return badRequest(
                    Utils.createErrorMessage("List 2 not found.")
            );
        }

        Set<FoursquareVenue> list2FsVenues = list2.get().getVenues().stream()
                .map(UserVenue::getFoursquareVenue)
                .collect(Collectors.toSet());

        List<FoursquareVenue> intersection = list1.get().getVenues().stream()
                .map(UserVenue::getFoursquareVenue)
                .distinct()
                .filter(list2FsVenues::contains)
                .collect(Collectors.toList());

        return ok(Json.toJson(intersection));
    }

}