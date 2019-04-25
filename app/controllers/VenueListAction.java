package controllers;

import models.User;
import play.mvc.Result;
import play.mvc.Http;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VenueListAction extends play.mvc.Action.Simple {
    private static Pattern LIST_PATTERN = Pattern.compile("/lists/(\\d+)/?");

    public CompletionStage<Result> call(Http.Request req) {
        User user = req.attrs().get(RequestAttrs.USER);
        Matcher matcher = LIST_PATTERN.matcher(req.path());
        boolean foundListId = matcher.find();

        if (!foundListId)
            return listNotFound();

        String listIdStr = matcher.group(1);
        Long listId = Long.parseLong(listIdStr);

        return user.getList(listId)
                .map(list -> delegate.call(req.addAttr(RequestAttrs.LIST, list)))
                .orElse(listNotFound());
    }

    private CompletionStage<Result> listNotFound() {
        return CompletableFuture.completedFuture(badRequest("No list found"));
    }
}
