package models;

import java.util.ArrayList;
import java.util.List;

public class PlaceList {
    private Long id;
    private String name;
    private List<String> places;

    public PlaceList(Long id, String name) {
        this.id = id;
        this.name = name;
        places = new ArrayList<>();
    }

    public void addPlace(String place) {
        places.add(place);
    }

    public void removePlace(String place) {
        places.remove(place);
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String newName) { name = newName; }
    public List<String> getPlaces() { return places; }
}
