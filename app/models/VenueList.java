package models;

import java.util.ArrayList;
import java.util.List;

public class VenueList {
    private Long id;
    private String name;
    private List<UserVenue> venues;

    public VenueList(Long id, String name) {
        this.id = id;
        this.name = name;
        this.venues = new ArrayList<>();
    }

    public void addVenue(UserVenue venue) {
        venues.add(venue);
    }

    public boolean removeVenue(Long venueId) {
        return venues.removeIf(venue -> venue.getId().equals(venueId));
    }

    public boolean hasVenue(Long venueId) {
        return venues.stream().anyMatch(venue -> venue.getId().equals(venueId));
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String newName) { name = newName; }
    public List<UserVenue> getVenues() { return venues; }
}