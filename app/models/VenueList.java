package models;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Reference;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
public class VenueList {
    @Id
    private String id;
    private String name;
    @Reference
    private List<UserVenue> venues;

    public VenueList(){

    }

    public VenueList( String name) {
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

    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String newName) { name = newName; }
    public List<UserVenue> getVenues() { return venues; }
}