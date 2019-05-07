package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public boolean removeVenue(String venueId) {
        return venues.removeIf(venue -> venue.getId().equals(venueId));
    }

    public boolean hasVenue(String venueId) {
        return getVenue(venueId).isPresent();
    }

    public Optional<UserVenue> getVenue(String venueId) {
        return venues.stream()
            .filter(venue -> venue.getId().equals(venueId))
            .findAny();
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String newName) { name = newName; }
    public List<UserVenue> getVenues() { return venues; }
}