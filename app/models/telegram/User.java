package models.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {


    public Integer id;
    public String username;
    @JsonProperty("is_bot") public Boolean isBot;
    @JsonProperty("first_name") public String FirstName;
    @JsonProperty("last_name") public String LastName;
    @JsonProperty("language_code") public String languageCode;

    /*
     *  "id":277016262,
     *           "is_bot":false,
     *           "first_name":"Martin",
     *           "last_name":"Loguancio",
     *           "username":"maadlog",
     *           "language_code":"es"
     */
}
