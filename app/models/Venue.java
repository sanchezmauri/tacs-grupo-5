package models;

public class Venue {
    private Long id;
    private String name;

    public Venue(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

