package models;

import services.CodesService;

import java.util.Map;

public class User {
    private Long id;
    private String name;
    // todo: placeLists

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static Map<String, Object> getDecodedMapFromToken(String token) throws Exception {
        return CodesService.decodeMapFromToken(token);
    }
}