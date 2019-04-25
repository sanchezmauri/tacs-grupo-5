package controllers;

import annotations.Authenticate;
import com.fasterxml.jackson.databind.JsonNode;
import controllers.actions.VenueListAction;
import models.User;
import models.VenueList;
import models.Venue;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.With;
import repos.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

public class ListsController extends Controller {

    private long listId = 0L;
    private Long nextListId() {
        listId++;
        return listId;
    }

    @Authenticate(types = {"ROOT", "SYSUSER"})
    public Result list(Http.Request request) {
        User user = request.attrs().get(RequestAttrs.USER);
        List<VenueList> allLists;

        if (user.getRol().equals(User.Rol.ROOT)) {
            allLists = UserRepository
                    .all()
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
    @Authenticate(types = {"SYSUSER"})
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

        VenueList newList = new VenueList(nextListId(), listName);

        User user = request.attrs().get(RequestAttrs.USER);
        user.addList(newList);

        return created(Json.toJson(newList));
    }

    @Authenticate(types = {"SYSUSER"})
    @With(VenueListAction.class)
    public Result delete(Long listId, Http.Request request) {
        User user = request.attrs().get(RequestAttrs.USER);

        user.removeList(listId);

        return ok();
    }

    @Authenticate(types = {"SYSUSER"})
    @With(VenueListAction.class)
    public Result changeListName(Long listId, Http.Request request) {
        JsonNode changeJson = request.body().asJson();

        if (!changeJson.has("name"))
            return badRequest(Utils.createErrorMessage("Missing field: name."));

        VenueList list = request.attrs().get(RequestAttrs.LIST);
        String newName = changeJson.get("name").asText();

        list.setName(newName);

        return ok(Json.toJson(list));
    }

    @Authenticate(types = {"SYSUSER"})
    @With(VenueListAction.class)
    public Result addPlaceToList(Long listId, Http.Request request) {
        // todo: extraer parseo de json a algún lugar
        // esto de decir missing field y toda la gilada
        JsonNode venueIdJson = request.body().asJson();

        if (!venueIdJson.has("id"))
            return badRequest("Missing field: id");

        if  (!venueIdJson.has("name"))
            return badRequest("Missing field: name");

        long id = venueIdJson.get("id").asLong();
        String name = venueIdJson.get("name").asText();


        User user = request.attrs().get(RequestAttrs.USER);
        VenueList list = request.attrs().get(RequestAttrs.LIST);


        user.addVenueToList(list, id, name);
        return ok(Json.toJson(list));
    }

    @Authenticate(types = {"SYSUSER"})
    @With(VenueListAction.class)
    public Result removePlaceFromList(Long listId, Http.Request request) {
        JsonNode venueIdJson = request.body().asJson();

        if (!venueIdJson.has("id"))
            return badRequest("Missing field: venueId");

        Long venueId = venueIdJson.get("id").asLong();


        VenueList list = request.attrs().get(RequestAttrs.LIST);


        if (list.removeVenue(venueId))
            return ok(Json.toJson(list));
        else
            return badRequest(Utils.createErrorMessage("No venue with id " + venueId.toString()));
    }

    public Result compareLists(Long listId1, Long listId2) {
        List<Venue> commonVenues = new ArrayList<>();
        commonVenues.add(new Venue(1L, "Maxi Kiosco"));
        commonVenues.add(new Venue(2L, "Verdulería"));
        return ok(Json.toJson(commonVenues));
    }
}