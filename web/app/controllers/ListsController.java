package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.PlaceList;
import models.Venue;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ListsController extends Controller {
    private List<PlaceList> placeLists = new ArrayList() {{
        add(new PlaceList(1L, "Restaurants Orientales"));
        add(new PlaceList(2L, "Cines"));
        add(new PlaceList(3L, "Paseo 1"));
    }};

    private Optional<PlaceList> find(Long id) {
        return placeLists.stream()
                        .filter(l -> l.getId().equals(id))
                        .findFirst();
    }

    public Result list() {
        return ok(Json.toJson(placeLists));
    }

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
        Long nextId = placeLists.get(placeLists.size() -1).getId() + 1;
        PlaceList newList = new PlaceList(nextId, listName);
        placeLists.add(newList);

        return created(Json.toJson(newList));
    }

    public Result delete(Long listId) {
        boolean didRemove = placeLists.removeIf(list -> list.getId().equals(listId));

        if (didRemove) {
            return ok();
        } else {
            return listNotFound(listId);
        }
    }

    public Result changeListName(Long listId, Http.Request request) {
        Optional<PlaceList> maybeList =  find(listId);

        if (!maybeList.isPresent()) {
            return listNotFound(listId);
        }

        JsonNode changeJson = request.body().asJson();

        if (!changeJson.has("name")) {
            return badRequest(Utils.createErrorMessage("Missing field name."));
        }

        PlaceList list = maybeList.get();

        list.setName(changeJson.get("name").asText());

        return ok(Json.toJson(list));
    }

    public Result compareLists(Long listId1, Long listId2) {
        List<Venue> commonVenues = new ArrayList<>();
        commonVenues.add(new Venue(1L, "Maxi Kiosco"));
        commonVenues.add(new Venue(2L, "Verdulería"));

        return ok(Json.toJson(commonVenues));
    }

    private Result listNotFound(Long id) {
        return notFound(
            Utils.createErrorMessage("No list with id = " + id.toString())
        );
    }
}
