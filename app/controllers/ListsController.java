package controllers;

import annotations.Authenticate;
import com.fasterxml.jackson.databind.JsonNode;
import models.User;
import models.UserVenue;
import models.VenueList;
import models.Venue;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repos.UserRepository;

import java.util.*;
import java.util.function.BiConsumer;
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
    public Result delete(Long listId, Http.Request request) {
        User user = request.attrs().get(RequestAttrs.USER);

        if (user.removeList(listId)) {
            return ok();
        } else {
            return listNotFound(user, listId);
        }
    }

    @Authenticate(types = {"SYSUSER"})
    public Result changeListName(Long listId, Http.Request request) {
        JsonNode changeJson = request.body().asJson();

        if (!changeJson.has("name")) {
            return badRequest(Utils.createErrorMessage("Missing field: name."));
        }

        User user = request.attrs().get(RequestAttrs.USER);
        Optional<VenueList> list = user.getList(listId);

        return list.map(l -> {
            String newName = changeJson.get("name").asText();
            l.setName(newName);
            return ok(Json.toJson(l));
        }).orElseGet(
            () -> listNotFound(user, listId)
        );
    }

    // usado por addPlaceToList y removePlaceFromList
    private Result venueListHandler(Long listId, Http.Request request, BiConsumer<VenueList, Long> venueListOperation) {
        JsonNode venueIdJson = request.body().asJson();

        if (!venueIdJson.has("venueId")) {
            return badRequest("Missing field: venueId");
        }

        long venueId = venueIdJson.get("venueId").asLong();
        User user = request.attrs().get(RequestAttrs.USER);

        Optional<VenueList> maybeList = user.getList(listId);

        if (!maybeList.isPresent()) {
            return listNotFound(user, listId);
        }

        venueListOperation.accept(maybeList.get(), venueId);

        return ok();
    }

    // acá dejo solo users porque necesito agregarle al user un
    @Authenticate(types = {"SYSUSER"})
    public Result addPlaceToList(Long listId, Http.Request request) {
        return venueListHandler(listId, request, (list, venueId) -> list.addVenue(new UserVenue(venueId, "", false)));
    }

    @Authenticate(types = {"SYSUSER"})
    public Result removePlaceFromList(Long listId, Http.Request request) {
        return venueListHandler(listId, request, VenueList::removeVenue);
    }

    private Result listNotFound(User user, Long id) {
        return notFound(
                Utils.createErrorMessage("User [" + user.getId().toString() + "] hasn't list [" + id.toString() + "]")
        );
    }

    public Result compareLists(Long listId1, Long listId2) {
        List<Venue> commonVenues = new ArrayList<>();
        commonVenues.add(new Venue(1L, "Maxi Kiosco"));
        commonVenues.add(new Venue(2L, "Verdulería"));
        return ok(Json.toJson(commonVenues));
    }
}