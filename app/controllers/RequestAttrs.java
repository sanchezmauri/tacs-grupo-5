package controllers;

import models.User;
import models.VenueList;
import play.libs.typedmap.TypedKey;

public class RequestAttrs {
    public static final TypedKey<User> USER = TypedKey.create("user");
    public static final TypedKey<VenueList> LIST = TypedKey.create("list");
}
