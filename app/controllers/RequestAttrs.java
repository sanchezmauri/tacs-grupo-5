package controllers;

import models.User;
import play.libs.typedmap.TypedKey;

public class RequestAttrs {
    public static final TypedKey<User> USER = TypedKey.create("user");
}
