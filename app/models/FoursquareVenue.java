package models;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

import java.time.LocalDate;

// Este objeto guarda data básica de Foursquare
// y sirve para cumplir el requerimiento
// saber cantidad de lugares desde una fecha
// Es compartido por varios users venues.
// Esto último puede no ser necesario,
// porque podríamos hacer que los lugares de usuario repitan la data
// Como esta @referenciado, está en su propia collection (fq_venues).
@Entity("fq_venues")
public class FoursquareVenue {

    public FoursquareVenue() { }

    @Id
    private String id;
    private String name;
    private String address;
    private LocalDate added;

    public FoursquareVenue(String fsId, String name, String address, LocalDate added) {
        this.name = name;
        this.address = address;
        this.added = added;
        this.id = fsId;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) { this.id = id; }

    public String getName() {
        return name;
    }
    public void setName(String name) { this.name = name; }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) { this.address = address; }

    public LocalDate getAdded() { return added; }
    public void setAdded(LocalDate added) { this.added = added; }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        FoursquareVenue otherFqVenue = (FoursquareVenue) other;
        return  id.equals(otherFqVenue.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return String.format("foursquareVenue: %s (%s)\nAddress: %s\nAdded: %s",
                name, id, address, added.toString());
    }
}

