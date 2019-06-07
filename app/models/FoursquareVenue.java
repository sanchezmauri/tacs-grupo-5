package models;

import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

import java.time.LocalDate;

@Entity
public class FoursquareVenue {

    public FoursquareVenue(){
    }

    private String id;
    private String name;
    private String address;
    private LocalDate added;

    public FoursquareVenue(String id, String name, String address, LocalDate added) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.added = added;
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
}

