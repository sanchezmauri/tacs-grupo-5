package models.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Chat {

    public Long id;
    public String username;
    @JsonProperty("first_name") public String FirstName;
    @JsonProperty("last_name") public String LastName;
    public String type;

    /*
     *    "chat": {
     *           "id":277016262,
     *           "first_name":"Martin",
     *           "last_name":"Loguancio",
     *           "username":"maadlog",
     *           "type":"private"
     *       },
     */
}
