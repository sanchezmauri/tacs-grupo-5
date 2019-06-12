package models;

import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
public class VenueList {
    @Id
    private String id;
    private String name;
    @Embedded
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

    public boolean hasVenueWithFoursquareVenue(FoursquareVenue fqVenue) {
        return getVenueWithFoursquareVenue(fqVenue).isPresent();
    }

    public Optional<UserVenue> getVenueWithFoursquareVenue(FoursquareVenue fqVenue) {
        return venues.stream()
                .filter(venue -> venue.getFoursquareVenue().equals(fqVenue))
                .findAny();
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

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append(name)
                .append('(')
                .append(id)
                .append(") ")
                .append(venues.size())
                .append(" venues.\n");

        venues.forEach(venue -> stringBuilder.append(venue.toString()).append('\n'));

        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        VenueList otherList = (VenueList) other;
        if (otherList == null) return false;

        return  id.equals(otherList.id);
    }
}